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
    <input type="hidden" id="colectivoTE" name="colectivoTE" value="3"/>
    <!-- Literales paracabecera de tabla-->
    <!-- Seccion Pagina JSP Trayectotia -->
    <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
        <p style="text-align: center; border: solid;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.2")%></p>
        <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.2.a")%></p>
    </div>
    <div id="divExperienciaAcreditada3">
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.programas.convocatorias.ordenes.decretos")%></p>
            <hr>
        </div>
        <div id="divColecOrdenesDecretosC3">
            <div style="clear: both;">
                <div id="listaColecOrdenesDecretosC3" name="listaColecOrdenesDecretosC3"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecOrdenesDecretosC3" name="botonAnadirColecOrdenesDecretosC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(3,1,null);"/>
                    <input type="button" id="botonModificarColecOrdenesDecretosC3" name="botonModificarColecOrdenesDecretosC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(3,1);"/>
                    <input type="button" id="botonEliminarColecOrdenesDecretosC3" name="botonEliminarColecOrdenesDecretosC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(3,1);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.convocatorias.lanbide")%></p>
            <hr>
        </div>
        <div id="divColecConvocatoriasC3">
            <div style="clear: both;">
                <div id="listaColecConvocatoriasC3" name="listaColecConvocatoriasC3"></div>
                 <div class="botonera">
                    <input type="button" id="botonGuardarColecConvocatoriasC3" name="botonGuardarColecConvocatoriasC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarDatosColecConvocatoriasPredefColectivo(3);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.otros.programas")%></p>
            <hr>
        </div>
        <div id="divColecOtrosProgramasC3">
            <div style="clear: both;">
                <div id="listaColecOtrosProgramasC3" name="listaColecOtrosProgramasC3"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecOtrosProgramasC3" name="botonAnadirColecOtrosProgramasC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(3,3,null);"/>
                    <input type="button" id="botonModificarColecOtrosProgramasC3" name="botonModificarColecOtrosProgramasC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(3,3);"/>
                    <input type="button" id="botonEliminarColecOtrosProgramasC3" name="botonEliminarColecOtrosProgramasC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(3,3);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.actividades.orientacion.profesional")%></p>
            <hr>
        </div>
        <div id="divColecActividadesOProC3">
            <div style="clear: both;">
                <div id="listaColecActividadesOProC3" name="listaColecActividadesOProC3"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecColecActividadesOProC3" name="botonAnadirColecColecActividadesOProC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(3,4,null);"/>
                    <input type="button" id="botonModificarColecActividadesOProC3" name="botonModificarColecActividadesOProC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(3,4);"/>
                    <input type="button" id="botonEliminarColecActividadesOProC3" name="botonEliminarColecActividadesOProC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(3,4);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.otros.programas.ejecucion")%></p>
            <hr>
        </div>
        <div id="divColecEjecucionProgramasEspC3">
            <div style="clear: both;">
                <div id="listaColecEjecucionProgramasEspC3" name="listaColecEjecucionProgramasEspC3"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecEjecucionProgramasEspC3" name="botonAnadirColecEjecucionProgramasEspC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(3,6,null);"/>
                    <input type="button" id="botonModificarColecEjecucionProgramasEspC3" name="botonModificarColecEjecucionProgramasEspC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(3,6);"/>
                    <input type="button" id="botonEliminarColecEjecucionProgramasEspC3" name="botonEliminarColecEjecucionProgramasEspC3" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(3,6);"/>
                </div>
            </div>
        </div>
    </div>
</div>

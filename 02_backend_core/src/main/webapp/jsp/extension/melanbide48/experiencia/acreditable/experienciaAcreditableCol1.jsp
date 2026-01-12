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
    <input type="hidden" id="colectivoTE" name="colectivoTE" value="1"/>
    <!-- Literales paracabecera de tabla-->
    <!-- Seccion Pagina JSP Trayectotia -->
    <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
        <p style="text-align: center; border: solid;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.1")%></p>
        <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.1.a")%></p>
    </div>
    <div id="divExperienciaAcreditada1">
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.convocatorias.lanbide.colectivo.especifico")%></p>
            <hr>
        </div>
        <div id="divColecConvocatoriasC1">
            <div style="clear: both;">
                <div id="listaColecConvocatoriasC1" name="listaColecConvocatoriasC1"></div>
                 <div class="botonera">
                    <input type="button" id="botonGuardarColecConvocatoriasC1" name="botonGuardarColecConvocatoriasC1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarDatosColecConvocatoriasPredefColectivo(1);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.actividades.orientacion.empleo.c1")%></p>
            <hr>
        </div>
        <div id="divColecActividadesOEmpC1">
            <div style="clear: both;">
                <div id="listaColecActividadesOEmpC1" name="listaColecActividadesOEmpC1"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecActividadesOEmpC1" name="botonAnadirColecActividadesOEmpC1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(1,5,null);"/>
                    <input type="button" id="botonModificarColecActividadesOEmpC1" name="botonModificarColecActividadesOEmpC1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(1,5);"/>
                    <input type="button" id="botonEliminarColecActividadesOEmpC1" name="botonEliminarColecActividadesOEmpC1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(1,5);"/>
                </div>
            </div>
        </div>        
    </div>
</div>

<%-- 
    Document   : m88AltaModificacionSocios
    Created on : 07-Feb-2022, 15:01:10
    Author     : INGDGC
--%>

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
<div>
    <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
        <span><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.texto.inicial.socios")%></span>
    </div>
    <div id="divMelanbide88Socios">
        <div style="clear: both;">
            <div id="listaMelanbide88Socios" name="listaMelanbide88Socios"></div>
        </div>
        <div class="botonera centrarElementos">
            <input type="button" id="botonAnadirMelanbide88Socios" name="botonAnadirMelanbide88Socios" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.nuevo")%>" onclick="anadirMelanbide88(1);"/>
            <input type="button" id="botonModificarMelanbide88Socios" name="botonModificarMelanbide88Socios" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarMelanbide88(1);"/>
            <input type="button" id="botonEliminarMelanbide88Socios" name="botonEliminarMelanbide88Socios" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarMelanbide88(1);"/>
        </div>
    </div>
    <!-- Literales para cabecera de tablas -->
    <input type="hidden" id="label_tabla_columna_nombre" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.nombre")%>"/>
    <input type="hidden" id="label_tabla_columna_apellido1" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido1")%>"/>
    <input type="hidden" id="label_tabla_columna_apellido2" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido2")%>"/>
    <input type="hidden" id="label_tabla_columna_documento" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.documento")%>"/>
</div>


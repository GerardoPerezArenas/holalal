<%-- 
    Document   : recargarLista
    Created on : 17 ene. 2024, 15:25:23
    Author     : gerardo.perez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%
int idiomaUsuario = 1;
UsuarioValueObject usuario = new UsuarioValueObject();
try {
    if (session != null) {
        if (usuario != null) {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            idiomaUsuario = usuario.getIdioma();
        }
    }
} catch(Exception ex){
}
MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
%>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    console.log('llegue a recargarLista.jsp');
    function actualizarListaTipo1() {
        try {
            var result = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            if (result !='' && result != null) {
            jsp_alerta("A", result);
            }
            parent.actualizarTablaTipo1();
        } catch (err) {
            console.log('Error en actualizarListaTipo1: ' + err);
        }
    }
</script>
<body onload="actualizarListaTipo1();">
</body>


<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.i18n.MeLanbide85I18n"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
MeLanbide85I18n meLanbide85I18n = MeLanbide85I18n.getInstance();
%>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    function actualizarListaContrataciones() {
        try {
            var result = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            if (result!=''){
                jsp_alerta("A", result);
            }
            parent.actualizaPestana(true);
        } catch (err) {
        }
    }
</script>
<body onload="actualizarListaContrataciones();">
</body>

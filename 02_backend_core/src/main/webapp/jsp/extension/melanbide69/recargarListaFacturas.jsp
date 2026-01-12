<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.i18n.MeLanbide69I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%

int idiomaUsuario = 1;
UsuarioValueObject usuario = new UsuarioValueObject();
try
{
    if (session != null) 
    {
        if (usuario != null) 
        {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            idiomaUsuario = usuario.getIdioma();
        }
    }
}
catch(Exception ex)
{

}


MeLanbide69I18n meLanbide69I18n = MeLanbide69I18n.getInstance();
%>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">    
    
    function actualizarListaFacturas(){
        try{
            var result = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            parent.actualizarTablaFacturas(true);
            jsp_alerta("A",result);
        }catch(err){
        
        }
    }
    
</script>
<body onload="actualizarListaFacturas();">
</body>

<script type="text/javascript">
</script>
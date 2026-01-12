<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%

int idiomaUsuario = 1;
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

}


MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
%>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">    
    
    function actualizarProspectoresSolicitudEca(){
        try{
            var result = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            parent.actualizarTablaProspectoresSolicitudEca(true);
            jsp_alerta("A",result);
        }catch(err){
        
        }
    }
    
</script>
<body onload="actualizarProspectoresSolicitudEca();">
</body>

<script type="text/javascript">
</script>

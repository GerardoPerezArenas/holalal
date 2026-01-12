<%-- 
    Document   : recargarConsultaPersDiscp
    Created on : 11-may-2020, 13:08:35
    Author     : kepa
--%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
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
MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
%>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript">
    function actualizarConsulta() {
        try {
            var mensaje = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            var resultado = '<%=request.getSession().getAttribute("resultado") != null ? request.getSession().getAttribute("resultado") : ""%>';
            var idDocumento = '<%=request.getSession().getAttribute("idDocumento") != null ? request.getSession().getAttribute("idDocumento") : ""%>';
            var nombreDocumento = '<%=request.getSession().getAttribute("nombreFichero") != null ? request.getSession().getAttribute("nombreFichero") : ""%>';
            parent.actualizarCertificado(true);
            if (resultado == '0') {                
                parent.rellenaOid(idDocumento);
                parent.rellenarNombreCert(nombreDocumento);
            } else {
                parent.rellenaOid("");
                parent.rellenarNombreCert("");
            }
            jsp_alerta("A", mensaje);
            elementoVisible('off', 'barraProgresoPersDiscp');
            pleaseWait('off');
        } catch (err) {
        }
    }
</script>
<body onload="actualizarConsulta();">
</body>
<script type="text/javascript">
</script>
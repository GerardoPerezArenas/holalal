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
<!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui-1.10.1.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-ui-1.10.2.custom.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-ui-1.10.2.custom.min.js"></script>-->

<link rel="stylesheet" href="//code.jquery.com/ui/1.10.2/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script src="https://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<script type="text/javascript">    
    
    function actualizarProspectoresJus(){
        try{
            //alert('recargar preparadores tras excel');
            var result = '<%=request.getSession().getAttribute("mensajeImportar") != null ? request.getSession().getAttribute("mensajeImportar") : ""%>';
            alert(result);
            parent.actualizarTablaProspectoresJustificacionEca(true);
            //jsp_alerta_grande("A",result);
            $("#mensaje").html(result);
            alert($("#dialogExcel").html());
            jsp_alerta_Grande("A",result,"Errores al procesar el Excel");
            //$("#dialogExcel").html()
            $("#dialogExcel").dialog();
        }catch(err){
            alert(err.toString());
        }
    }
</script>
<div id="dialogExcel"> <!-- onload="actualizarProspectoresJus();"-->
    <div id="dialogExcel1" title="Errores al procesar el Excel" style="overflow: auto">
        <p id="mensaje"></p>
    </div>
</div>
<script type="text/javascript">
    actualizarProspectoresJus();
</script>

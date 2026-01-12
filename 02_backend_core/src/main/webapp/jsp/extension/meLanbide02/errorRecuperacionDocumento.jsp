<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter"
                                         type="es.altia.flexia.integracion.moduloexterno.lanbide02.util.ConfigurationParameter" />
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<%
    String idiomaUsuario = (String)request.getAttribute("idioma_usuario");
    String nombreModulo  = (String)request.getAttribute("nombre_modulo");
%>
<TITLE><%=configuracion.getParameter("ETIQUETA_ERROR_" + idiomaUsuario,nombreModulo)%></TITLE>
<%@ include file="/jsp/plantillas/Metas.jsp" %>

<!-- Estilos -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">

    function pulsarCerrar(){
        window.close();        
    }
</script>

</head>

<BODY class="bandaBody">

<table id="tabla1" style="width: 100%;" cellpadding="0px" cellspacing="0px">
    <tr>
        <td>
            <table class="bordeExteriorPantallaPeke" cellspacing="0px" cellpadding="1px">
                <tr>
                    <td>
                        <table class="bordeInteriorPantallaPeke" cellspacing="1px">
                            <tr>
                                <td id="titulo" style="width: 100%; height: 25px" class="titulo">&nbsp;Error</td>
                            </tr>
                            <tr>
                                <td class="separadorTituloPantalla"></td>
                            </tr>
                            <tr>
                                <td class="contenidoPantalla" valign="top">
                                    <table cellspacing="0px" cellpadding="0px" border="0px" style="padding-top: 10px; padding-bottom:10px" align="center">
                                        <tr>
                                            <td	id="tabla" style="width: 630px;height:215px;" valign="top">
                                                
                                                <%=configuracion.getParameter("MSG_ERROR_AL_RECUPERAR_DOCUMENTO_" + idiomaUsuario,nombreModulo)%>
                                                
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <!-- Fin sombra inferior. -->
    </table>

    <!-- Separador. -->
    <table height="2px" cellpadding="0px" cellspacing="0px" border="0px">
        <tr>
            <td></td>
        </tr>
    </table>
    <!-- Fin separador. -->

<!-------------------------------------- BOTONES. ------------------------------------------>
<div style="border: 0; text-align: right">
    <table cellpadding="0px" cellspacing="0px" style="border: 0;" align="right">
        <tr>
            <td>
                <table cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td colspan="2">
                            <input type="button" class="botonGeneral" accesskey="C" value="Cerrar" name="cmdCerrar" id="cmdCerrar" 
                            onClick="javascript:pulsarCerrar();"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

</BODY>
</html:html>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide64.i18n.MeLanbide64I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConstantesMeLanbide64"%>

<%
    int idiomaUsuario = 0;
    int codOrganizacion = 0;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try {
        if (session != null) {
            if (usuario != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {}

    MeLanbide64I18n meLanbide64I18n = MeLanbide64I18n.getInstance();
        String numExpediente = request.getParameter("numero");
        String codigoProcedimiento = (String)request.getAttribute("codigoProcedimiento");
        String codTramite    = (String)request.getAttribute("codigoTramite");
        String ejercicioExp = (String)request.getAttribute("ejercicioExp");
        Config m_Config = ConfigServiceHelper.getConfig("common");

%>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>

<script type="text/javascript">    
    var codProcedimiento = "<%=codigoProcedimiento%>";
    var codTramite = "<%=codTramite%>";
    var numeroExpediente = "<%=numExpediente%>";
    var ejercicioExp = "<%=ejercicioExp%>";
    var ocuTramite = document.forms[0].ocurrenciaTramite.value;
        
    window.onload = function() {
        actualizarFechas();
    }
    
 function actualizarFechas(){
           try { 
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            parametros = '?tarea=preparar&modulo=MELANBIDE64&operacion=grabarPlazoRecurso&tipo=0&numero=<%=numExpediente%>'
                        + '&codTramite=<%=codTramite%>'
                        + '&ocurrenciaTramite=' + ocuTramite
                        ;
            $.ajax({
                url: url + parametros,
                type: 'POST',
                success: function(){
                    pleaseWait('off');
                }});
            } catch (Err) {
                alert("ERROR " + Err);
                pleaseWait('off');
            }//try-catch      
    }
    
    
</script>

<body>
    
</body>



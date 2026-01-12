<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 0;
 int apl = 5;
 String css = "";
    int codOrganizacion = 0;
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
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide39/ecaUtils.js"></script>
<script type="text/javascript">
    var msgValidacion = '';
            
    function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"
            ];

        if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                //no necesitamos hacer nada especial
                }
            }
        }else{
            return null;
        }
    }
    
    function inicializar(){
        window.focus();
    }
    
    function ejecutarListadoPuestosContratados(){  
        var control = new Date();      
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeResumenPuestosContratados&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeResumenPuestosContratados&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }  
    }
    
    function ejecutarListadoDatosPuestos(){
        var control = new Date();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeDatosPuestos&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeDatosPuestos&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }  
    }
    
    function ejecutarListadoEconomico(){
        var control = new Date();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeResumenEconomico&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide39/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
                 if(ano != null && ano != undefined && ano != ''){
                     var CONTEXT_PATH = '<%=request.getContextPath()%>'
                     var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                     var parametros = "";
                     var control = new Date();
                     parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=generarInformeResumenEconomico&tipo=0&ano='+ano+'&control='+control.getTime();
                     window.open(url+parametros, "_blank");
                 }
            });
        }        
    }
    
</script>
<body class="bandaBody" onload="inicializar();">
    <form id="formProcesos">
        <div style="height:550px; width: 100%;">
            <table width="100%" style="height: 550px;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.label.tit_procesos")%></td>
                </tr>                
                <tr>
                    <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                        <div id="contenidoProc" class="cuadroFondoBlanco" style="width:970px; height: 550px; overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                            <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.legend.informes")%></legend>
                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.label.RPC")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnRPC" name="btnRPC" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoPuestosContratados();">
                                        </div>
                                    </div>
                                </div>
                                <div class="lineaFormularioFont" style="padding-top: 50px;">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.label.RE")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnRPC" name="btnRPC" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoEconomico();">
                                        </div>
                                    </div>
                                </div>
                                <div class="lineaFormularioFont" style="padding-top: 50px;">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.label.JF")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnRPC" name="btnRPC" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoDatosPuestos();">
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</body>

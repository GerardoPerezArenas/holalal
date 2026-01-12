<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 0;
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
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<!--<script type="text/javascript" src="<//%=request.getContextPath()%>/scripts/calendario.js"></script> -->
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
    
    function ejecutarListadoCP(){        
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=cargarCriteriosFiltroListadoCP_APA&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),330,840,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=cargarCriteriosFiltroListadoCP_APA&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,870,'no','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                //recargarCriteriosListadoCP(result);
                var fila;
                for(var i = 1;i< result.length; i++){
                   fila = result[i];
                   var numExp =fila[0];
                   //alert(tipoacred);
                   
                }//for
                
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = '?tarea=preparar&modulo=MELANBIDE03&operacion=generarPDFCPAPA&tipo=0&numExp='+numExp;
                window.open(url+parametros, "_blank");
            }
        }
                
    }
    

</script>
<body class="bandaBody" onload="javascript:{if (window.top.principal.frames[0]&&window.top.principal.frames['menu'].Go)
        window.top.principal.frames['menu'].Go();inicializar();}">
    <form id="formProcesos">
        <div style="height:550px; width: 100%;">
            <table width="100%" style="height: 550px;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco"><%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.label.tit_procesos")%></td>
                </tr>
                <tr>
                    <td class="separadorTituloPantalla"></td>
                </tr>
                <tr>
                    <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                        <div id="contenidoProc" class="cuadroFondoBlanco" style="width:970px; height: 550px; overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                            <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                <legend class="legendAzul" align="center"><%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.legend.listados")%></legend>
                                <div class="lineaFormulario">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.label.ListadoCP")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnListadoCP" name="btnListadoCP" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoCP();">
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
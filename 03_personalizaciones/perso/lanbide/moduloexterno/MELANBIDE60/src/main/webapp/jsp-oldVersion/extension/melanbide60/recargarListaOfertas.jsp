<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.i18n.MeLanbide60I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmeOfertaVO" %>
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


MeLanbide60I18n meLanbide60I18n = MeLanbide60I18n.getInstance();

String numExpediente = request.getParameter("numExp");
%>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/extension/melanbide60/cmeUtils.js'/>"></script>

<script type="text/javascript">    
    
    function actualizarOfertasCme(){
        try{
            var codigoOperacion = '<%=request.getSession().getAttribute("codOperacionGuardarOfertaCme") != null ? request.getSession().getAttribute("codOperacionGuardarOfertaCme") : ""%>';
            if(codigoOperacion=="0"){
                parent.window.returnValue =  getListaOfertasCme();
                parent.barraProgresoCme('off', 'barraProgresoNuevaOferta');
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                parent.cerrarVentana();
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else if(codigoOperacion=="4"){
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
            parent.barraProgresoCme('off', 'barraProgresoNuevaOferta');
        }catch(err){
        
        }
    }
    
    function getListaOfertasCme(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=getListaOfertasNoDenegadasPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
            //var formData = new FormData(document.getElementById('formContrato'));
            ajax.send(parametros);
            if (ajax.readyState==4 && ajax.status==200){
                var xmlDoc = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                    var text = ajax.responseText;
                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                    xmlDoc.async="false";
                    xmlDoc.loadXML(text);
                }else{
                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                    xmlDoc = ajax.responseXML;
                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
            }//if (ajax.readyState==4 && ajax.status==200)
            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
        }
        catch(err){
            alert(err);
            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
            
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
</script>
<body onload="actualizarOfertasCme();">
</body>

<script type="text/javascript">
</script>
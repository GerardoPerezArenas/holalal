<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.i18n.MeLanbide07I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.text.SimpleDateFormat"%>

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
    {}
    MeLanbide07I18n meLanbide07I18n = MeLanbide07I18n.getInstance(); 

    String numExpediente    = request.getParameter("numero");
%>

<script type="text/javascript">
    
    function getXMLHttpRequest() {
        var aVersions = ["MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp", "Microsoft.XMLHttp"
        ];

        if (window.XMLHttpRequest) {
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                } catch (error) {
                    //no necesitamos hacer nada especial
                }
            }
        } else {
            return null;
        }
    }
    
    function validaExp(numeroExpediente) {
        if (numeroExpediente == null || numeroExpediente == '') {
            if (jsp_alerta("C", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.numExpVacio")%>')){
                $('input[name="cmdGrabarGeneral"]').click();
            } 
            
        } else {
            //ajax
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";

            parametros = '?tarea=preparar&modulo=MELANBIDE07&operacion=validarNumeroExpediente&tipo=0&numero=<%=numExpediente%>'
            + '&numeroExpediente=' + numeroExpediente;
            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);
                if (ajax.readyState == 4 && ajax.status == 200) {
                    var xmlDoc = null;
                    if (navigator.appName.indexOf("Internet Explorer") != - 1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var respuesta = nodos[0].childNodes[0].childNodes[0].nodeValue;
                if (respuesta == '0') {
                    jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.expediente")%>');
                    $('#NUMEXPSUPLEMENTARIO').focus();
                    document.getElementById('NUMEXPSUPLEMENTARIO').scrollIntoView({behavior: 'smooth'});
                } else if (respuesta == '2') {
                    jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }
            } catch (Err) {
                alert("ERROR " + Err);
            }//try-catch 
        }
    }
    
    $(document).ready(function() {
    
        $( document ).ajaxComplete(function() {
            if(estanCargadosCamposSuplementarios()){
               
            $('input[name="cmdGrabarGeneral"]').mousedown(function() {
                    var numeroExpediente = $('#NUMEXPSUPLEMENTARIO').val();
                    validaExp(numeroExpediente);
                });
            
            $('#campo_NUMEXPSUPLEMENTARIO td').on('change',function() {
                    var numeroExpediente = $('#NUMEXPSUPLEMENTARIO').val();
                    validaExp(numeroExpediente);
                });
                
           
            }
        });
    
        
       
       
        
    });
</script>

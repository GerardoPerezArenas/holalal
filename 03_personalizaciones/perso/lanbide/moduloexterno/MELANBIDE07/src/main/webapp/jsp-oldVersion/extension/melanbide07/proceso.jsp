<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.i18n.MeLanbide07I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*"%>

<%
    String sIdioma = request.getParameter("idioma");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide07/melanbide07.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide07/utils.js"/>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
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
    
    function realizarLlamada(parametros){ //+alert("llamando");
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
            //alert("0");
            //var formData = new FormData(document.getElementById('formContrato'));
            ajax.send(parametros);
            //alert("01");
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
            //alert("1");
            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var nodoValores;
            var hijosValores;
            //alert("2");
            for(var j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion != "0"){
                alert(codigoOperacion);
            }
                cerrarVentana();
            
        }
        catch(Err){

        }//try-catch
    }
    
    function cerrarVentana() {
        if (navigator.appName == "Microsoft Internet Explorer") {
            window.parent.window.opener = null;
            window.parent.window.close();
        } else if (navigator.appName == "Netscape") {
            top.window.opener = top;
            top.window.open('', '_parent', '');
            top.window.close();
        } else {
            window.close();
        }
    }
    
        function inicio() {
            try{    
                var control = new Date();
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                var parametros = '?tarea=preparar&modulo=MELANBIDE07&operacion=generarDeuda&tipo=0&numero=<%=numExpediente%>'
                                    +'&control='+control.getTime();
                //result = lanzarPopUpModal(url+parametros, 650, 1000, 'no', 'no');
                document.forms[0].target = "_self";
                document.forms[0].action = url + parametros;       
                document.forms[0].submit(); 
            }catch(err){
                alert('Se ha producido un error al abrir el documento');
            }
           //cerrarVentana();
           realizarLlamada(parametros);
        }
</script>
<body onLoad="inicio()">
    <div class="tab-page" id="tabPage341" style="height:520px; width: 100%;">
        <div style="clear: both;">
            <form name="pepe" method="post">
            </form>
        </div>
    </div>
    <script type="text/javascript">
        
    </script>
</body>
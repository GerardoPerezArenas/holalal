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
    //int idiomaUsuario = Integer.parseInt(sIdioma);
    /*UsuarioValueObject usuario = new UsuarioValueObject();
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
    MeLanbide07I18n meLanbide07I18n = MeLanbide07I18n.getInstance();*/
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
    
    function generarDeuda(){
        try{    
            var control = new Date();
            var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
            var parametros = '?tarea=preparar&modulo=MELANBIDE07&operacion=cargarDeuda&tipo=0&numero=<%=numExpediente%>'
                                +'&control='+control.getTime();
                        window.open(url+parametros, "_blank");
            //result = lanzarPopUpModal(url+parametros, 650, 1000, 'no', 'no');
            /*document.forms[0].target = "_blank";
            document.forms[0].action = url + parametros;       
            document.forms[0].submit(); */
        }catch(err){
            alert('Se ha producido un error al abrir el documento');
        }
        //realizarLlamada(parametros);
    }
    
    function realizarLlamada(parametros){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var nodoValores;
            var hijosValores;
            for(var j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion != "0"){
                alert(codigoOperacion);
                cerrarVentana();
            }
            
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
</script>
<body>
    <div class="tab-page" id="tabPage341" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana341">Carta de pago</h2>
        <script type="text/javascript">tp1_p341 = tp1.addTabPage( document.getElementById( "tabPage341" ) );</script>
        <div style="clear: both;">
            <form name="formulario256" method="post">
                <div class="botonera" style="padding-top: 20px; text-align: center; padding-top: 50px;">
                    <input type="button" id="btnGuardarCEI" name="btnGuardarCEI" class="botonGeneral" value="Obtener carta" onclick="generarDeuda();return false;">
                </div>
            </form>
        </div>
    </div>
</body>


<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide31.i18n.MeLanbide31I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide31.vo.FilaValidacionDatoSuplementarioVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide31I18n meLanbide31I18n = MeLanbide31I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

<script type="text/javascript">
    function actualizarValidacionesDatosSuplementarios(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE31&operacion=actualizarValidaciones&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            var listaValidacionesNueva = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="CAMPO"){
                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                        }
                        else if(hijosFila[cont].nodeName=="VALOR"){
                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                        }
                        else if(hijosFila[cont].nodeName=="RESULTADO"){
                            //fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            if (typeof(XMLSerializer) !== 'undefined') {
                                var serializer = new XMLSerializer();
                                fila[2] = serializer.serializeToString(hijosFila[cont].childNodes[0]);
                             } else if (hijosFila[cont].childNodes[0].xml) {
                                fila[2] = hijosFila[cont].childNodes[0].xml;
                             } else {
                                 try{
                                     var oXMLSerializer =  new XMLSerializer();
                                     fila[2] = oXMLSerializer.serializeToString(hijosFila[cont].childNodes[0]);
                                 }catch(err){
                                     
                                 }
                             }
                        }
                        else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                            if(hijosFila[cont].childNodes.length > 0)
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            else
                                fila[3] = '';
                        }
                    }
                    listaValidacionesNueva[j-1] = fila;
                    fila = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion=="0"){
                recargarTabla(listaValidacionesNueva);
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide31I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide31I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide31I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide31I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        }
        catch(Err){

        }//try-catch
    }
    
    
        
    function recargarTabla(result){
        var fila;
        listaValidacionesTabla = new Array();
        for(var i = 0;i< result.length; i++){
            fila = result[i];
            listaValidacionesTabla[i] = [fila[0], fila[1], fila[2], fila[3]];
        }
        tabValidaciones = new Tabla(document.all.validaciones, 894);
        tabValidaciones.addColumna('290','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col1")%>");
        tabValidaciones.addColumna('100','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col2")%>");
        tabValidaciones.addColumna('100','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col3")%>");
        tabValidaciones.addColumna('379','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col4")%>");

        tabValidaciones.displayCabecera=true;
        tabValidaciones.height = 300;
        tabValidaciones.lineas=listaValidacionesTabla;
        tabValidaciones.displayTabla();
    }
</script>


<body>
    <div class="tab-page" id="tabPage311" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana311"><%=meLanbide31I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p311 = tp1.addTabPage( document.getElementById( "tabPage311" ) );</script>
        <div style="clear: both;">
            <div>
                <div class="sub3titulo" style="height: 18px; clear: both; padding-top: 4px; text-align: left;">
                    <span>
                        <%=meLanbide31I18n.getMensaje(idiomaUsuario,"label.resultadoValidaciones")%>
                    </span>
                </div>
                <div id="validaciones" style="padding-top: 8px; width:920px; height: 365px; text-align: center; overflow-x: auto; overflow-y: auto; margin-left: auto; margin-right: auto;" align="center"></div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">    
    var tabValidaciones;
    var listaValidacionesTabla = new Array();

    tabValidaciones = new Tabla(document.all.validaciones, 894);
    tabValidaciones.addColumna('290','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col1")%>");
    tabValidaciones.addColumna('100','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col2")%>");
    tabValidaciones.addColumna('100','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col3")%>");
    tabValidaciones.addColumna('379','left',"<%= meLanbide31I18n.getMensaje(idiomaUsuario,"label.tabla.col4")%>");
    

    tabValidaciones.displayCabecera=true;
    tabValidaciones.height = 300;
    <%  		
        FilaValidacionDatoSuplementarioVO vo = null;
        List<FilaValidacionDatoSuplementarioVO> valList = (List<FilaValidacionDatoSuplementarioVO>)request.getAttribute("listaValidaciones");													
        if (valList != null && valList.size() >0){
            for (int indiceNot=0;indiceNot<valList.size();indiceNot++)
            {
                vo = valList.get(indiceNot);
                
    %>
        listaValidacionesTabla[<%=indiceNot%>] = ["<%=vo.getNombreCampo()%>","<%=vo.getValor()%>","<%=vo.getResultado()%>", "<%=vo.getObservaciones()%>"];
    <%
            }// for
        }// if
    %>
    tabValidaciones.lineas=listaValidacionesTabla;
    tabValidaciones.displayTabla();
</script>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide40.i18n.MeLanbide40I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerCertificadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerModuloFormativoVO" %>
<%
    String codCertificado = "";
    String cargarDatos = (String) request.getAttribute("cargarDatos");
    if(cargarDatos.equalsIgnoreCase("S")){
        CerCertificadoVO certificado = (CerCertificadoVO) request.getAttribute("certificado");
       // CerModuloFormativoVO moduloFormativo = (CerModuloFormativoVO) request.getAttribute("modulo");
        codCertificado = certificado.getCodCertificado();
    }
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
    MeLanbide40I18n meLanbide40I18n = MeLanbide40I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
 
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide40/melanbide40.css"/>

<script type="text/javascript">   
    var comboListaCertificados;
    var listaCodigosCertificados = new Array();
    var listaDescripcionesCertificados = new Array();
    var listaDescripcionesCertificadosEus = new Array();
    var tablaExpedientesCEPAP;
    var codigosExpDocumentales = new Array();
    
    function cargarDatosCert(){ 
        var codCertificado = "<%= codCertificado%>";
        buscaCodigoCertificado(codCertificado);
        
        var codModulo = new Array();
        var descModulo = new Array();
    }
    
    function buscaCodigoCertificado (codCertificado){
        comboListaCertificados.buscaCodigo(codCertificado);
    }
    
    
    function cargarDatosCertificado(){        
        var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
        var ajax = getXMLHttpRequest();
        var nodos = null;       
        var listaModulosTabla = new Array();
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
        var listaExpTabla = new Array();
        codigosExpDocumentales = new Array();
        
        limpiarPantalla();
        //bloquearModulosPracticas();
        
         var numExpLicitacion = "<%=numExpediente%>";
         var codOrganizacion = "<%=codOrganizacion%>";
        
        if(ajax!=null && codCertificadoSeleccionado!=null && codCertificadoSeleccionado!=""){
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            //var parametros = "&tarea=procesar&operacion=rellenarPorCertificado" + "&modulo=" + escape(nombreModuloLicitacion) + "&codOrganizacion=" + escape(codOrganizacion)
            //                   + "&numero=" + escape(numExpLicitacion) + "&tipo=0&codCertificado=" + escape(codCertificadoSeleccionado)+"&idioma="+<%=idiomaUsuario%>;
            var parametros = "&tarea=procesar&operacion=rellenarPorCertificado&modulo=MELANBIDE40&codOrganizacion=" + escape(codOrganizacion)
                                + "&numero=" + escape(numExpLicitacion) + "&tipo=0&codCertificado=" + escape(codCertificadoSeleccionado)+"&idioma="+<%=idiomaUsuario%>;
                           
                           
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            
            try{
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
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                
                if(codigoOperacion=="70"){                   
                   tablaExpedientesCEPAP.lineas = new Array();
                   tablaExpedientesCEPAP.displayTabla();
                    
                   for(j=0;hijos!=null && j<hijos.length;j++){
                   if(hijos[j].nodeName=="MODULOS_FORMATIVOS"){
                            var listaModulos = xmlDoc.getElementsByTagName("MODULOS_FORMATIVOS");
                            var nodosModulos = listaModulos[0];
                            var hijosModulos = nodosModulos.childNodes;
                            for(x=0;hijosModulos!=null && x<hijosModulos.length;x++){
                                var codModulo = "";
                                var desModulo = "";
                                var duracionModulo = "";
                                var nivel = "";
                                if(hijosModulos[x].nodeName == "MODULO_FORMATIVO"){
                                    var listaModulos = xmlDoc.getElementsByTagName("MODULO_FORMATIVO");
                                    for(x=0;listaModulos!=null && x<listaModulos.length;x++){
                                        var modulo = listaModulos[x].childNodes;
                                        for(y=0;modulo!=null && y<modulo.length;y++){
                                            if(modulo[y].nodeName=="CODMODULO"){
                                                codModulo = modulo[y].childNodes[0].nodeValue;
                                                if(document.getElementById("codModForm") != null){
                                                    document.getElementById("codModForm").value = codModulo;
                                                }//if(document.getElementById("codModForm") != null)
                                            }else if(modulo[y].nodeName=="DESMODULO_E"){
                                                desModulo = modulo[y].childNodes[0].nodeValue;
                                                if(document.getElementById("desModulo") != null){
                                                    document.getElementById("desModulo").value = desModulo;
                                                }//if(document.getElementById("desModulo") != null)
                                            }else if(modulo[y].nodeName=="NIVEL"){
                                                if(modulo[y].childNodes[0] != null && modulo[y].childNodes[0].nodeValue != null ){
                                                    nivel = modulo[y].childNodes[0].nodeValue;
                                                }else{
                                                    nivel = " ";
                                                }//if(modulo[y].childNodes[0] != null && modulo[y].childNodes[0].nodeValue != null )
                                                /*
                                                if(document.getElementById("nivelModForm") != null){
                                                    document.getElementById("nivelModForm").value = nivel;
                                                }//if(document.getElementById("nivelModForm") != null)
                                                */
                                            }else if(modulo[y].nodeName=="DURACION"){
                                                duracionModulo = modulo[y].childNodes[0].nodeValue;                                                
                                            }//if
                                        }//for(y=0;modulo!=null && y<modulo.length;y++)
                                        listaModulosTabla[x] = [codModulo,desModulo,nivel,duracionModulo];
                                    }//for(x=0;listaModulos!=null && x<listaModulos.length;x++)
                                }//if(hijosUnidades[x].nodeName == "MODULO_FORMATIVO")
                            }//for(x=0;hijosModulos!=null && x<hijosModulos.length;x++)                           
                        }//if
                        else if(hijos[j].nodeName=="EXP_CEPAP"){
                            var listaExpedientes = xmlDoc.getElementsByTagName("EXP_CEPAP");
                            var nodosExpedientes = listaExpedientes[0];
                            var hijosExpedientes = nodosExpedientes.childNodes;
                            for(x=0;hijosExpedientes!=null && x<hijosExpedientes.length;x++){
                                var expCEPAP = "";
                                var acreditado = "";
                                var motivo = "";                                
                                 if(hijosExpedientes[x].nodeName == "EXPEDIENTE"){
                                    var infoExpediente = xmlDoc.getElementsByTagName("EXPEDIENTE");   
                                    
                                    for (y =0;y<hijosExpedientes[x].childNodes.length;y++ ){                                
                                        if(hijosExpedientes[x].childNodes[y].nodeName=="NUMEXPEDIENTE"){
                                            expCEPAP = hijosExpedientes[x].childNodes[y].childNodes[0].nodeValue;
                                        }else if(hijosExpedientes[x].childNodes[y].nodeName=="ACREDITADO"){
                                            acreditado = hijosExpedientes[x].childNodes[y].childNodes[0].nodeValue;
                                        }else if(hijosExpedientes[x].childNodes[y].nodeName=="MOTACREDITADO"){
                                            motivo = hijosExpedientes[x].childNodes[y].childNodes[0].nodeValue;
                                        }
                                        
                                    }
                                    listaExpTabla[x] = [expCEPAP, acreditado=="0"?"SI":"NO"/*, motivo*/];
                           
                                 }
                                 
                            }
                        }
                   }
                   
                   tablaExpedientesCEPAP.lineas = listaExpTabla;
                   tablaExpedientesCEPAP.displayTabla();
                   
                   if(listaExpTabla.length > 0){
                       for(var c=0; codigosExpDocumentales != null && c<codigosExpDocumentales.length; c++){
                            var codigo = codigosExpDocumentales[c];  
                       }
                   }
                   
                }else if(codigoOperacion=="71"){
                    jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoDatosCertificado")%>');
                }else if(codigoOperacion=="72"){
                    jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoAreaCertificado")%>');
                }else if(codigoOperacion=="73"){
                    jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoFamiliaCertificado")%>');
                }else if(codigoOperacion=="73"){
                    jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoUnidadesCertificado")%>');
                }//if
            }catch(Err){
                alert("cargarDatosCertificado Error.descripcion: " + Err.description);
            }//try-catch
        }
    }//cargarDatosCertificado
    
        
    function limpiarPantalla(){
    //Borramos el módulo formativo
        document.getElementById("codModForm").value = "";
        document.getElementById("desModulo").value = "";
        tablaExpedientesCEPAP.lineas = new Array();
        tablaExpedientesCEPAP.displayTabla();
    }
    
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
  
    function guardarDatos(){

        var control = new Date();
        var parametros = 'tarea=preparar&modulo=MELANBIDE40&operacion=guardar&tipo=0&numero=<%=numExpediente%>'
                            +'&codCertificado='+document.getElementById('codListaCertificados').value
                            +'&codModPrac='+document.getElementById('codModForm').value                                
                            +'&control='+control.getTime();
        realizarLlamada(parametros);
         
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
                else if(hijos[j].nodeName=="MENSAJE"){
                    nodoValores = hijos[j];
                    hijosValores = nodoValores.childNodes;
                    document.getElementById('lblMensaje').innerHTML = hijosValores[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            /*if(codigoOperacion=="0"){
                document.getElementById('lblMensaje').className = "textoAzul";
            }else if(codigoOperacion=="1"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="2"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="3"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else{
                    
            }//if(
            */
           if(codigoOperacion=="0"){                                
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                cerrarVentana();
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else if(codigoOperacion=="71"){
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide40I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
            
           
            
            
           // deshabilitarCampos();
        }
        catch(Err){

        }//try-catch
    }
   
   function acreditarModulo(){

        var control = new Date();
        var parametros = 'tarea=preparar&modulo=MELANBIDE40&operacion=acreditar&tipo=0&numero=<%=numExpediente%>'
                            +'&codCertificado='+document.getElementById('codListaCertificados').value
                            +'&codModPrac='+document.getElementById('codModForm').value   
                            +'&control='+control.getTime();
        realizarLlamada(parametros);
         
    }
   
</script>

    <div class="tab-page" id="tabPage401" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana401"><%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p401 = tp1.addTabPage( document.getElementById( "tabPage401" ) );</script>
        <form action="/PeticionModuloIntegracion.do" method="POST">
        <div style="clear: both;">
            <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; height: 380px; padding: 20px;">
                
                
                <%-- Certificado --%>
                <div class="lineaFormulario">
                    <div style="width: 100%; float: left;">
                    <div class="etiqueta" style="width: 100px;float:left;text-align:left">
                        <%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.certificado")%>
                    </div>
                    <div class="columnP" style="float:left;">
                        <input type="text" name="codListaCertificados" id="codListaCertificados" size="10" class="inputTexto" value="" /><!--onkeyup="xAMayusculas(this);"-->
                        <input type="text" name="descListaCertificados"  id="descListaCertificados" size="80" class="inputTexto" readonly="true" value=""/>
                        <a href="" id="anchorListaCertificados" name="anchorListaCertificados">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                    </div>     
                </div>
                <div class="lineaFormulario">
                   <div style="width: 100%; float: left;">
                    <div class="etiqueta" style="width:100px;float: left;text-align:left" >
                        <%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.ModFormacion")%>:
                    </div>
                    <div class="columnP" width="110px" style="float:left;">
                        <input type="text" name="codModForm"  id="codModForm" size="10" class="inputTexto" readonly="true" value=""/>
                    </div>
                    <div class="columnP" style="float:left">
                        <textarea name="desModulo" id="desModulo" rows="3" cols="100" class="textareaTexto" readonly="true" value=""></textarea>
                    </div>
                    </div>
                </div>
                 
                <div class="botonera" style="padding-top: 20px; text-align: center; padding-top: 50px;">
                    <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="<%=meLanbide40I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatos();">
                </div>
                <br/>
                <div class="lineaFormulario">                    
                    <div class="sub3titulo">
                          <%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.listadoExpedientesCEPAP")%>
                    </div>
                    <div id="tablaExpedientesCEPAP" class="text" align="left"></div>
                </div>
                <div class="lineaFormulario"> </div>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnAcreditarExencion" name="btnAcreditarExencion" class="botonGeneral" style=" background-color: #2d4c9f;background-repeat: repeat-x;width:150px;" value="<%=meLanbide40I18n.getMensaje(idiomaUsuario, "btn.acreditar")%>" onclick="acreditarModulo()">
                </div>
            
        </div>
    </div>
    </form>


<script type="text/javascript">
  
   listaCodigosCertificados[0] = "";
   listaDescripcionesCertificados[0] = "";
   listaDescripcionesCertificadosEus[0] = "";
    
   var contador = 0;
        
    <logic:iterate id="certificados" name="listaCertificados" scope="request">
            listaCodigosCertificados[contador] = '<bean:write name="certificados" property="codCertificado" />';
            listaDescripcionesCertificados[contador] = '<bean:write name="certificados" property="descCertificadoC" />';
            listaDescripcionesCertificadosEus[contador] = '<bean:write name="certificados" property="descCertificadoE" />';
            contador++;
    </logic:iterate>      
    comboListaCertificados = new Combo("ListaCertificados");
    comboListaCertificados.addItems(listaCodigosCertificados, listaDescripcionesCertificados);
    comboListaCertificados.change = cargarDatosCertificado;
            
            
     /* Creamos la tabla */
        // TABLA DE LISTADO CEPAP
        if(document.all){           
            tablaExpedientesCEPAP = new Tabla(document.all.tablaExpedientesCEPAP,910);
        }else{
            
            tablaExpedientesCEPAP = new Tabla(document.getElementById("tablaExpedientesCEPAP"),910);
        }//if(document.all)

        
        tablaExpedientesCEPAP.addColumna('600','left','<%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.expedienteCEPAP")%>');
        tablaExpedientesCEPAP.addColumna('300','left','<%=meLanbide40I18n.getMensaje(idiomaUsuario,"label.acreditado")%>');
        //tablaExpedientesCEPAP.addColumna('600','left','<%//=meLanbide40I18n.getMensaje(idiomaUsuario,"label.motivoAcreditado")%>');
        
        
        tablaExpedientesCEPAP.height = 100;
        tablaExpedientesCEPAP.displayCabecera=true;
        tablaExpedientesCEPAP.lineas = new Array();
        tablaExpedientesCEPAP.displayTabla();        
            
            
     <% if(cargarDatos.equalsIgnoreCase("S")){ %>
    cargarDatosCert();
    <% } %>
</script>

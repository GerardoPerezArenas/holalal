<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.i18n.MeLanbide34I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresCalculo" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
 int apl = 5;
 String css = "";
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
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide34I18n meLanbide34I18n = MeLanbide34I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    ValoresCalculo valoresCalculo = (ValoresCalculo)request.getAttribute("valoresCalculo");
    String mensaje = (String)request.getAttribute("mensaje");
    boolean deshabilitado = (Boolean)request.getAttribute("deshabilitado");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide34/melanbide34.css"/>

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
    
    function guardarDatosCEI(){
        if(validarDatos()){
            var control = new Date();
            var parametros = 'tarea=preparar&modulo=MELANBIDE34&operacion=guardar&tipo=0&numero=<%=numExpediente%>'
                                +'&p1='+document.getElementById('p1').value
                                +'&p2='+document.getElementById('p2').value
                                +'&p3='+document.getElementById('p3').value
                                +'&p4='+document.getElementById('p4').value
                                +'&concedida='+document.getElementById('concedida').value
                                +'&control='+control.getTime();
            realizarLlamada(parametros);
        }else{
            jsp_alerta("A", msgValidacion);
        }
    }
    
//    function recalcularDatosCEI(){
//        var control = new Date();
//        var parametros = "tarea=preparar&modulo=MELANBIDE34&operacion=recalcular&tipo=0&numero=<%=numExpediente%>"
//                        +"&p1="+document.getElementById('p1').value
//                        +"&p2="+document.getElementById('p2').value
//                        +"&p3="+document.getElementById('p3').value
//                        +"&p4="+document.getElementById('p4').value
//                        +"&control='"+control.getTime();
//        realizarLlamada(parametros);
//    }

    function validarP1(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            var p1 = parseInt(document.getElementById('p1').value);
            var maxP1 = parseInt('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE1_CEI, ConstantesMeLanbide34.FICHERO_S75)%>')
            if(isNaN(p1) || p1 > maxP1){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.p1Incorrecto")%>'+maxP1;
                document.getElementById('p1').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('p1').removeAttribute("style");
                document.getElementById('p1').style.textAlign = 'right';
                if(recalcular){
                    recalcularDatosCEI();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                }
            }
            return correcto;
        }catch(err){
            return false;
        }
    }

    function validarP2(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            var p2 = parseInt(document.getElementById('p2').value);
            var maxP2 = parseInt('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE2_CEI, ConstantesMeLanbide34.FICHERO_S75)%>')
            if(isNaN(p2) || p2 > maxP2){
                
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.p2Incorrecto")%>'+maxP2;
                document.getElementById('p2').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('p2').removeAttribute("style");
                document.getElementById('p2').style.textAlign = 'right';
                if(recalcular){
                    recalcularDatosCEI();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                }
            }
            return correcto;
        }catch(err){
            return false;
        }
    }

    function validarP3(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            var p3 = parseInt(document.getElementById('p3').value);
            var maxP3 = parseInt('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE3_CEI, ConstantesMeLanbide34.FICHERO_S75)%>')
            if(isNaN(p3) || p3 > maxP3){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.p3Incorrecto")%>'+maxP3;
                document.getElementById('p3').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('p3').removeAttribute("style");
                document.getElementById('p3').style.textAlign = 'right';
                if(recalcular){
                    recalcularDatosCEI();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                }
            }
            return correcto;
        }catch(err){
            return false;
        }
    }

    function validarP4(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            var p4 = parseInt(document.getElementById('p4').value);
            var maxP4 = parseInt('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE4_CEI, ConstantesMeLanbide34.FICHERO_S75)%>')
            if(isNaN(p4) || p4 > maxP4){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.p4Incorrecto")%>'+maxP4;
                document.getElementById('p4').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('p4').removeAttribute("style");
                document.getElementById('p4').style.textAlign = 'right';
                if(recalcular){
                    recalcularDatosCEI();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                }
            }
            return correcto;
        }catch(err){
            return false;
        }
    }
    
    function validarConcedida(mostrarMensaje, recalcular){
        try{
            var correcto = true;
            if(mostrarMensaje){
                msgValidacion = '';
            }
            var concedida = parseFloat(document.getElementById('concedida').value);
            var limMax = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_LIMMAX_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            if(concedida < 0){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.concedidaPositivo")%>';
                document.getElementById('concedida').style.border = '1px solid red';
                correcto = false;
            }else if(concedida > limMax){
                if(msgValidacion == '')
                    msgValidacion = '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.error.concedidaMenorLimite")%>';
                document.getElementById('concedida').style.border = '1px solid red';
                correcto = false;
            }else{
                document.getElementById('concedida').removeAttribute("style");
                document.getElementById('concedida').style.textAlign = 'right';
                if(recalcular){
                    recalcularPagos();
                }
            }
            if(mostrarMensaje){
                if(!correcto){
                    jsp_alerta("A", msgValidacion);
                }
            }
            return correcto;
        }catch(err){
            return false;
        }
    }
    
    function validarDatos(){
        msgValidacion = '';
        var correcto = true;
        if(!validarP1(false, false)){
            correcto = false;
        }
        if(!validarP2(false, false)){
            correcto = false;
        }
        if(!validarP3(false, false)){
            correcto = false;
        }
        if(!validarP4(false, false)){
            correcto = false;
        }
        if(!validarConcedida(false, false)){
            correcto = false;
        }
        return correcto;
    }

    function recalcularDatosCEI(){
        try{
            var cien = 100.0;
            var sumando = 0.0;

            var p1 = parseInt(document.getElementById('p1').value);
            var p2 = parseInt(document.getElementById('p2').value);
            var p3 = parseInt(document.getElementById('p3').value);
            var p4 = parseInt(document.getElementById('p4').value);

            var sumPond = p1 + p2 + p3 + p4;

            //var limitePorc = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PORSUB_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            var limitePorc = parseFloat(document.getElementById('limporcentaje').value);
            /*var impSolic = parseFloat(document.getElementById('propuesta').value);
            var limite = impSolic * limitePorc;
            limite = limite / cien;
            limite = limite + sumando;
            limite = limite * 100;
            limite = limite / 100;*/

            var propuesta = limitePorc;
            propuesta = propuesta * sumPond;
            propuesta = propuesta / cien;
            propuesta = propuesta + sumando;
            propuesta = propuesta * 100;
            propuesta = propuesta / 100;

            var limMax = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_LIMMAX_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            var concedida = propuesta;
            if(concedida > limMax){
                concedida = limMax;
            }

            document.getElementById('total').value = sumPond;
            document.getElementById('propuesta').value = propuesta.toFixed(2);
            document.getElementById('concedida').value = concedida.toFixed(2);
            recalcularPagos();
        }catch(err){

        }
    }
    
    function recalcularPagos(){
    
            var cien = 100.0;
            var sumando = 0.0;
            var concedida = parseFloat(document.getElementById('concedida').value);
            var pp1 = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP1_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            var pago1 = concedida * pp1;
            pago1 = pago1 / cien;
            pago1 = pago1 + sumando;
            pago1 = pago1 * 100;
            pago1 = pago1 / 100;

            var pp2 = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP2_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            var pago2 = concedida * pp2;
            pago2 = pago2 / cien;
            pago2 = pago2 + sumando;
            pago2 = pago2 * 100;
            pago2 = pago2 / 100;

            var pp3 = parseFloat('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP3_CEI, ConstantesMeLanbide34.FICHERO_S75)%>');
            var pago3 = concedida * pp3;
            pago3 = pago3 / cien;
            pago3 = pago3 + sumando;
            pago3 = pago3 * 100;
            pago3 = pago3 / 100;
            
            document.getElementById('impPago1').value = pago1.toFixed(2);
            document.getElementById('impPago2').value = pago2.toFixed(2);
            document.getElementById('impPago3').value = pago3.toFixed(2);
    }
    
    function actualizarCalculosCEI(){
        var control = new Date();
        var parametros = "tarea=preparar&modulo=MELANBIDE34&operacion=refrescarPantalla&tipo=0&numero=<%=numExpediente%>&control='"+control.getTime();
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
                else if(hijos[j].nodeName=="VALORES"){
                    nodoValores = hijos[j];
                    hijosValores = nodoValores.childNodes;
                    for(var cont = 0; cont < hijosValores.length; cont++){
                        if(hijosValores[cont].nodeName=="LIMITEPORC"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('limporcentaje').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('limporcentaje').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="PROPUESTA"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('propuesta').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('propuesta').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="CONCEDIDA"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('concedida').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('concedida').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="TOTAL"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('total').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('total').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="PAGO1"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('impPago1').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('impPago1').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="PAGO2"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('impPago2').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('impPago2').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="PAGO3"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('impPago3').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('impPago3').value = '';
                            }
                        }
                    }
                }  
                else if(hijos[j].nodeName=="MENSAJE"){
                    nodoValores = hijos[j];
                    hijosValores = nodoValores.childNodes;
                    document.getElementById('lblMensaje').innerHTML = hijosValores[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion=="0"){
                document.getElementById('lblMensaje').className = "textoAzul";
            }else if(codigoOperacion=="1"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="2"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else if(codigoOperacion=="3"){
                document.getElementById('lblMensaje').className = "textoRojo";
            }else{
                    
            }//if(
            
            deshabilitarCampos();
        }
        catch(Err){

        }//try-catch
    }
    
    function deshabilitarCampos(){
        var clase = 'inputTexto<%=request.getAttribute("deshabilitado") != null && ((Boolean)request.getAttribute("deshabilitado")).booleanValue() == true ? " deshabilitado" : ""%>';
        document.getElementById('p1').className = clase;
        document.getElementById('p2').className = clase;
        document.getElementById('p3').className = clase;
        document.getElementById('p4').className = clase;
        document.getElementById('concedida').className = clase;
    }
</script>
<body>
    <div class="tab-page" id="tabPage341" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana341"><%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p341 = tp1.addTabPage( document.getElementById( "tabPage341" ) );</script>
        <div style="clear: both;">
            <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; height: 380px; padding: 20px;">
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.limite")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="limite" name="limite" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getLimite() != null ? valoresCalculo.getLimite() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                    <div style="float: left">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago1")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="impPago1" name="impPago1" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago1() != null ? valoresCalculo.getPago1() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.porcentaje")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="porcentaje" name="porcentaje" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPorcentaje() != null ? valoresCalculo.getPorcentaje() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                    <div style="float: left">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago2")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="impPago2" name="impPago2" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago2() != null ? valoresCalculo.getPago2() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.limporcentaje")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="limporcentaje" name="limporcentaje" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getLimitePorc() != null ? valoresCalculo.getLimitePorc() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                    <div style="float: left">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago3")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="impPago3" name="impPago3" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago3() != null ? valoresCalculo.getPago3() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p1")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="p1" name="p1" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP1() != null ? valoresCalculo.getP1() : ""%>" onchange="validarP1(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p2")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="p2" name="p2" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP2() != null ? valoresCalculo.getP2() : ""%>" onchange="validarP2(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p3")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="p3" name="p3" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP3() != null ? valoresCalculo.getP3() : ""%>" onchange="validarP3(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p4")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="p4" name="p4" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP4() != null ? valoresCalculo.getP4() : ""%>" onchange="validarP4(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.total")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="total" name="total" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getTotal() != null ? valoresCalculo.getTotal() : ""%>" readOnly/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.propuesta")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="propuesta" name="propuesta" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPropuesta() != null ? valoresCalculo.getPropuesta() : ""%>" readOnly/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.concedida")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="concedida" name="concedida" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="11" value="<%=valoresCalculo != null && valoresCalculo.getConcedida() != null ? valoresCalculo.getConcedida() : ""%>" onchange="validarConcedida(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="color: red; padding-top: 50px;">  
                    <b>
                        <label id="lblMensaje"><%=mensaje%></label>
                    </b>
                </div>
            </div>
            <div class="botonera" style="padding-top: 20px; text-align: center; padding-top: 50px;">
                <input type="button" id="btnGuardarCEI" name="btnGuardarCEI" class="botonGeneral" value="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatosCEI();">
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
</script>

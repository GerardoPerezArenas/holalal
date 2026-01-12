<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.i18n.MeLanbide34I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresCalculo" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide34.vo.SelectItem" %>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*"%>
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
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
    
    
    // lista sector
    List<SelectItem> listaResultado = new ArrayList<SelectItem>();
    if(request.getAttribute("lstResultado") != null)
        listaResultado = (List<SelectItem>)request.getAttribute("lstResultado");
    String lcodResul = "";
    String ldescResul = "";
    if (listaResultado != null && listaResultado.size() > 0) 
    {
        int i;
        SelectItem ts = null;
        
        for (i = 0; i < listaResultado.size() - 1; i++) 
        {
            ts = (SelectItem) listaResultado.get(i);
            lcodResul += "\"" + ts.getId().toString() + "\",";
            ldescResul += "\"" + ts.getLabel() + "\",";
        }
        ts = (SelectItem) listaResultado.get(i);
        lcodResul += "\"" + ts.getId().toString() + "\"";
        ldescResul += "\"" + ts.getLabel() + "\"";
        //ldescResul += "\"" + escape(ts.getLabel()) + "\"";
    }
    
    // lista motivo denegación
    List<SelectItem> listaMotivoDenegacion = new ArrayList<SelectItem>();
    if(request.getAttribute("lstMotivoDen") != null)
        listaMotivoDenegacion = (List<SelectItem>)request.getAttribute("lstMotivoDen");

    String lcodMotivoDen = "";
    String ldescMotivoDen = "";

    if (listaMotivoDenegacion != null && listaMotivoDenegacion.size() > 0) 
    {
        int i;
        SelectItem ts = null;
        
        for (i = 0; i < listaMotivoDenegacion.size() - 1; i++) 
        {
            ts = (SelectItem) listaMotivoDenegacion.get(i);
            lcodMotivoDen += "\"" + ts.getId().toString() + "\",";
            ldescMotivoDen += "\"" + ts.getLabel() + "\",";
        }
        ts = (SelectItem) listaMotivoDenegacion.get(i);
        lcodMotivoDen += "\"" + ts.getId().toString() + "\"";
        ldescMotivoDen += "\"" + ts.getLabel() + "\"";
        //ldescResul += "\"" + escape(ts.getLabel()) + "\"";
    }
    
    String ocultarMotivos ="display:none";
    if (valoresCalculo.getResulSubv()!=null && valoresCalculo.getResulSubv()=="N") ocultarMotivos="";
       
    
    String codTram = request.getParameter("codigoTramite");
       
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide34/melanbide34.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide34/utils.js"/>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
    var msgValidacion = '';
    
     var comboResultado;    
     var codResultado = [<%=lcodResul%>];
     var descResultado = [<%=ldescResul%>];
     
     var comboMotivoDen;
     var codMotivoDen = [<%=lcodMotivoDen%>];
     var descMotivoDen = [<%=ldescMotivoDen%>];
      
    
    
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
    
    function actualizarCamposSuplementarios(){
        var campoLimiteMax= 'T_<%=codTram%>_1_LIMITEMAX';
        var campoPorcentaje= 'T_<%=codTram%>_1_LIMITEPORC';
        var campoLimitePorc= 'T_<%=codTram%>_1_CUANLIM';
        var campoTotal= 'T_<%=codTram%>_1_SUMPOND';
        var campoP4= 'T_<%=codTram%>_1_P4';
        var campoP3= 'T_<%=codTram%>_1_P3';
        var campoP2= 'T_<%=codTram%>_1_P2';
        var campoP1= 'T_<%=codTram%>_1_P1';
        var campofecEstudio= 'T_<%=codTram%>_1_FECETECNICO';
        document.getElementById(campoLimiteMax).value = document.getElementById('limite').value;
        document.getElementById(campoPorcentaje).value = document.getElementById('porcentaje').value;
        document.getElementById(campoLimitePorc).value = document.getElementById('limporcentaje').value;
        document.getElementById(campoTotal).value = document.getElementById('total').value;
        document.getElementById(campoP4).value = document.getElementById('p4').value;
        document.getElementById(campoP3).value = document.getElementById('p3').value;
        document.getElementById(campoP2).value = document.getElementById('p2').value;
        document.getElementById(campoP1).value = document.getElementById('p1').value;
        document.getElementById(campofecEstudio).value = document.getElementById('fecEstudio').value;
    }
    
    
    function guardarDatosCEI(){
        if(validarDatos()){
            actualizarCamposSuplementarios();
            
            var control = new Date();
            var parametros = 'tarea=preparar&modulo=MELANBIDE34&operacion=guardar&tipo=0&numero=<%=numExpediente%>'
                                +'&p1='+document.getElementById('p1').value
                                +'&p2='+document.getElementById('p2').value
                                +'&p3='+document.getElementById('p3').value
                                +'&p4='+document.getElementById('p4').value
                                +'&concedida='+convertirANumero(document.getElementById('concedida').value)
                                +'&fecEstudio='+document.getElementById('fecEstudio').value
                                +'&resul='+document.getElementById('codResulSubvET').value
                                +'&motden1='+document.getElementById('codMotivoDen1ET').value
                                +'&motden2='+document.getElementById('codMotivoDen2ET').value
                                +'&motden3='+document.getElementById('codMotivoDen3ET').value
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
            
            //var concedida = parseFloat(document.getElementById('concedida').value);
            //reemplazarPuntosEca(document.getElementById('concedida'));
            FormatNumber(document.getElementById('concedida').value, 8, 2, 'concedida');
            var valor = document.getElementById('concedida').value;
            valor = convertirANumero(valor);
            var concedida = parseFloat(valor);
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
            propuesta = propuesta.toFixed(2).replace(/\./g,',');
            concedida = concedida.toFixed(2).replace(/\./g,',');
            document.getElementById('propuesta').value = propuesta ;
            document.getElementById('concedida').value = concedida;            
            FormatNumber(document.getElementById('propuesta').value, 8, 2, 'propuesta');
            FormatNumber(document.getElementById('concedida').value, 8, 2, 'concedida');
            
            recalcularPagos();
        }catch(err){

        }
    }
    
    function recalcularPagos(){
    
            var cien = 100.0;
            var sumando = 0.0;
           // var concedida = parseFloat(document.getElementById('concedida').value);
            var valor = document.getElementById('concedida').value;
            valor = convertirANumero(valor);
            var concedida = parseFloat(valor);
           
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
            
            document.getElementById('impPago1').value = pago1.toFixed(2).replace(/\./g,',');
            document.getElementById('impPago2').value = pago2.toFixed(2).replace(/\./g,',');
            document.getElementById('impPago3').value = pago3.toFixed(2).replace(/\./g,',');           
            
            FormatNumber(document.getElementById('impPago1').value, 8, 2, 'impPago1');
            FormatNumber(document.getElementById('impPago2').value, 8, 2, 'impPago2');
            FormatNumber(document.getElementById('impPago3').value, 8, 2, 'impPago3');
            
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
                  /*  for(var cont = 0; cont < hijosValores.length; cont++){
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
                        else if(hijosValores[cont].nodeName=="FETECNICO"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('fecEstudio').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('fecEstudio').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="RESULTADO"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('codResulSubvET').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('codResulSubvET').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="MOTDEN1"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('codMotivoDen1ET').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('codMotivoDen1ET').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="MOTDEN2"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('codMotivoDen2ET').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('codMotivoDen2ET').value = '';
                            }
                        }
                        else if(hijosValores[cont].nodeName=="MOTDEN3"){
                            if(hijosValores[cont].childNodes.length > 0){
                                document.getElementById('codMotivoDen3ET').value = hijosValores[cont].childNodes[0].nodeValue;
                            }
                            else{
                                document.getElementById('codMotivoDen3ET').value = '';
                            }
                        }
                    }*/
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
    
    function comprobarResultado(resultado){
        if (resultado=='N')
            document.getElementById('MD1').style.display='block';
        else document.getElementById('MD1').style.display='';
        
    }
    
    function deshabilitarCampos(){
        var clase = 'inputTexto<%=request.getAttribute("deshabilitado") != null && ((Boolean)request.getAttribute("deshabilitado")).booleanValue() == true ? " deshabilitado" : ""%>';
        document.getElementById('p1').className = clase;
        document.getElementById('p2').className = clase;
        document.getElementById('p3').className = clase;
        document.getElementById('p4').className = clase;
        document.getElementById('concedida').className = clase;
    }
    
    function mostrarCalFechaEstudio(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById('calfechaEstudio').src.indexOf('icono.gif') != -1 )
            showCalendar('forms[0]','fecEstudio',null,null,null,'','calfechaEstudio','',null,null,null,null,null,null,null,null,evento);
    }
    
    
    function cargarCombos(){
        comboResultado.addItems(codResultado, descResultado);
        comboMotivoDen1.addItems(codMotivoDen, descMotivoDen);
        comboMotivoDen2.addItems(codMotivoDen, descMotivoDen);
        comboMotivoDen3.addItems(codMotivoDen, descMotivoDen);
    }
    
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        //RESULTADO SUBVENCIÓN
        codigo = '<%=valoresCalculo != null && valoresCalculo.getResulSubv() != null ? valoresCalculo.getResulSubv() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codResultado.length && !encontrado)
            {
                codAct = codResultado[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descResultado[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descResulSubvET').value = desc;       
        
        //MOTIVO DENEGACION 1
        codigo = '<%=valoresCalculo != null && valoresCalculo.getMotivoDen1() != null ? valoresCalculo.getMotivoDen1() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codMotivoDen.length && !encontrado)
            {
                codAct = codMotivoDen[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descMotivoDen[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descMotivoDen1ET').value = desc;
        
        //MOTIVO DENEGACION 2
        codigo = '<%=valoresCalculo != null && valoresCalculo.getMotivoDen2() != null ? valoresCalculo.getMotivoDen2() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codMotivoDen.length && !encontrado)
            {
                codAct = codMotivoDen[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descMotivoDen[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descMotivoDen2ET').value = desc;
        
        //MOTIVO DENEGACION 3
        codigo = '<%=valoresCalculo != null && valoresCalculo.getMotivoDen3() != null ? valoresCalculo.getMotivoDen3() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codMotivoDen.length && !encontrado)
            {
                codAct = codMotivoDen[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descMotivoDen[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descMotivoDen3ET').value = desc;
}
</script>

    <div class="tab-page" id="tabPage341" style="width: 100%;">
        <h2 class="tab" id="pestana341"><%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p341 = tp1.addTabPage( document.getElementById( "tabPage341" ) );</script>
        <div style="clear: both;">
            <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; height: 380px; padding: 20px;">
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.costeEstudio")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="costeEstudio" name="costeEstudio" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getImpSolic() != null ? formateador.format(Float.valueOf(valoresCalculo.getImpSolic())) : ""%>" readOnly
                                   onkeyup="FormatNumber(this.value, 6, 0, this.id);"/>
                        </div>
                    </div>
                </div>                  
                
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.limites")+":          - "+meLanbide34I18n.getMensaje(idiomaUsuario,"label.porcentaje")%>
                        </div>
                         <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="porcentaje" name="porcentaje" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPorcentaje() != null ? valoresCalculo.getPorcentaje() : ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                        <div style="width: 170px; float: left; text-align: left;padding-left:3.5em">
                            <%="- "+meLanbide34I18n.getMensaje(idiomaUsuario,"label.importeMax")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="limite" name="limite" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getLimite() != null ? formateador.format(Float.valueOf(valoresCalculo.getLimite())) : ""%>" readOnly/>
                                 <input id="limporcentaje" name="limporcentaje" type="hidden" value="<%=valoresCalculo != null && valoresCalculo.getLimitePorc() != null ? valoresCalculo.getLimitePorc() : ""%>" />
                            </div>
                        </div>
                    </div>
                </div>
                            
                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;">
                    <fieldset style="margin-right:1.5em">
                        <legend>Criterios</legend>
                        <div style="width: 30px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p1")%>
                        </div>
                        <div style="width: 60px; float: left;">
                            <div style="float: left;">
                                <input id="p1" name="p1" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="4" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP1() != null ? valoresCalculo.getP1() : ""%>" onchange="validarP1(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                            </div>
                        </div>
                        <div style="width: 30px; padding-left: 0.5em;float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p2")%>
                        </div>
                        <div style="width: 60px; float: left;">
                            <div style="float: left;">
                                <input id="p2" name="p2" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="4" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP2() != null ? valoresCalculo.getP2() : ""%>" onchange="validarP2(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                            </div>
                        </div>
                        <div style="width: 30px; padding-left: 0.5em;float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p3")%>
                        </div>
                        <div style="width: 60px; float: left;">
                            <div style="float: left;">
                                <input id="p3" name="p3" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="4" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP3() != null ? valoresCalculo.getP3() : ""%>" onchange="validarP3(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                            </div>
                        </div>
                        <div style="width: 30px; padding-left: 0.5em;float: left; text-align: left;display:none;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.p4")%>
                        </div>
                        <div style="width: 60px; float: left;display:none;">
                            <div style="float: left;">
                                <input id="p4" name="p4" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="4" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getP4() != null ? valoresCalculo.getP4() : ""%>" onchange="validarP4(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                            </div>
                        </div>
                        <div style="width: 30px;padding-left: 0.5em; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.total")%>
                        </div>
                        <div style="width: 30px; float: left;">
                            <div style="float: left;">
                                <input id="total" name="total" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="4" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getTotal() != null ? valoresCalculo.getTotal() : ""%>" readOnly/>
                            </div>
                        </div>                        
                    </fieldset>   
                    </div>
                    
                    
                </div>
               
                <div class="lineaFormulario">
                     <div style="float: left;width:50%; padding-right:20px;padding-top: 1em; ">
                        <div style="width: 170px; float: left; text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.propuesta")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="propuesta" name="propuesta" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPropuesta() != null ? formateador.format(Float.valueOf(valoresCalculo.getPropuesta())): ""%>" readOnly/>
                            </div>
                        </div>
                    </div>
                        
                    <div style="float: left; padding-top: 1em;">
                        <div style="width: 170px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.fecEstudio")%>
                        </div>
                        <div style="width: 150px; float: left;">
                            <div style="float: left;">
                                <input id="fecEstudio" name="fecEstudio" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="10" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getFecEstudio() != null ? valoresCalculo.getFecEstudio() : ""%>" <%=deshabilitado ? "readOnly" : ""%>/>
                                <!--input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaBaja" name="fechaBaja" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaCpe(this, '<%=meLanbide34I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();" value=""/-->
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaEstudio(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaEstudio" name="calfechaEstudio" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.fecEstudio")%>" src="<%=request.getContextPath()%>/images/calendario/icono.gif"/>
                            </A>
                            </div>
                        </div>
                    </div>    
                </div>
                <div class="lineaFormulario" STYLE="">
                  <div style="width: 50%; float: left;padding-top: 1em;">
                    <div style="width: 170px; float: left; text-align: left;">
                        <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.concedida")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <div style="float: left;">
                            <input id="concedida" name="concedida" type="text" class="inputTexto <%=deshabilitado ? "deshabilitado" : ""%>" style="text-align: right;" size="20" maxlength="11" value="<%=valoresCalculo != null && valoresCalculo.getConcedida() != null ? formateador.format(Float.valueOf(valoresCalculo.getConcedida())): ""%>" 
                                   
                                   onchange="validarConcedida(true, true);" <%=deshabilitado ? "readOnly" : ""%>/>
                        </div>
                    </div>
                  </div>
                  <div style="float:left;width: 45%;padding-top: 1em;"> 
                       <div style="width: 170px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.ResulSubv")%>
                       </div>
                       
                   <div style="width: 130px; float: left;">                    
                        <div style="width: 220px; float: left;">
                            <input id="codResulSubvET" name="codResulSubvET" type="text" class="inputTexto" size="2" maxlength="3" 
                                   value="" onChange="comprobarResultado(this.value)">
                            <input id="descResulSubvET" name="descResulSubvET" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorResulSubvET" name="anchorResulSubvET" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSector" name="botonSector" style="cursor:hand;" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"
                                                                                             style="<%=deshabilitado ? "display:none" : ""%>"></span></a>
                        </div>
                    </div>
                                             
                </div>
               
               
                <div class="lineaFormulario">
                     <div style="width: 50%; float: left;">
                    <fieldset style="margin-right:1.5em"><legend>Pagos</legend>
                        <div class="lineaFormulario">
                           <div style="float: left">
                                <div style="width: 170px; float: left; text-align: left;">
                                    <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago1")%>
                                </div>
                                <div style="width: 150px; float: left;">
                                    <div style="float: left;">
                                        <input id="impPago1" name="impPago1" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago1() != null ? formateador.format(Float.valueOf(valoresCalculo.getPago1())) : ""%>" readOnly/>
                                    </div>
                                </div>
                            </div>
                        </div>
                         <div class="lineaFormulario">
                            <div style="float: left">
                                <div style="width: 170px; float: left; text-align: left;">
                                    <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago2")%>
                                </div>
                                <div style="width: 150px; float: left;">
                                    <div style="float: left;">
                                        <input id="impPago2" name="impPago2" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago2() != null ? formateador.format(Float.valueOf(valoresCalculo.getPago2())) : ""%>" readOnly/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <!--div style="width: 50%; float: left;">
                                <div style="width: 170px; float: left; text-align: left;">
                                    <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.limporcentaje")%>
                                </div>
                                <div style="width: 150px; float: left;">
                                    <div style="float: left;">
                                        <input id="limporcentaje" name="limporcentaje" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getLimitePorc() != null ? valoresCalculo.getLimitePorc() : ""%>" readOnly/>
                                    </div>
                                </div>
                            </div-->
                            <div style="float: left">
                                <div style="width: 170px; float: left; text-align: left;">
                                    <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.impPago3")%>
                                </div>
                                <div style="width: 150px; float: left;">
                                    <div style="float: left;">
                                        <input id="impPago3" name="impPago3" type="text" class="inputTexto deshabilitado" style="text-align: right;" size="20" maxlength="2" value="<%=valoresCalculo != null && valoresCalculo.getPago3() != null ? formateador.format(Float.valueOf(valoresCalculo.getPago3())) : ""%>" readOnly/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                     </div>
                   <div id="MD1" style="float:left;width: 45%;padding-top: 1em;"> 
                       <div style="width: 170px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.MotivoDen1")%>
                       </div>                       
                        <div style="width: 130px; float: left;">                    
                             <div style="width: 220px; float: left;">
                                 <input id="codMotivoDen1ET" name="codMotivoDen1ET" type="text" class="inputTexto" size="2" maxlength="3" 
                                        value="<%=valoresCalculo != null && valoresCalculo.getMotivoDen1()!=null ?valoresCalculo.getMotivoDen1():"" %>">
                                 <input id="descMotivoDen1ET" name="descMotivoDen1ET" type="text" class="inputTexto" size="22" readonly >
                                  <a id="anchorMotivoDen1ET" name="anchorMotivoDen1ET" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSector" name="botonSector" style="cursor:hand;" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"
                                                                                                    style="<%=deshabilitado ? "display:none" : ""%>"></span></a>
                             </div>
                         </div>                                             
                    </div>
                    <div style="float:left;width: 45%;padding-top: 1em;"> 
                       <div style="width: 170px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.MotivoDen2")%>
                       </div>                       
                        <div style="width: 130px; float: left;">                    
                             <div style="width: 220px; float: left;">
                                 <input id="codMotivoDen2ET" name="codMotivoDen2ET" type="text" class="inputTexto" size="2" maxlength="3" 
                                        value="<%=valoresCalculo != null && valoresCalculo.getMotivoDen2()!=null ?valoresCalculo.getMotivoDen2():"" %>">
                                 <input id="descMotivoDen2ET" name="descMotivoDen2ET" type="text" class="inputTexto" size="22" readonly >
                                  <a id="anchorMotivoDen2ET" name="anchorMotivoDen2ET" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSector" name="botonSector" style="cursor:hand;" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"
                                                                                                    style="<%=deshabilitado ? "display:none" : ""%>"></span></a>
                             </div>
                         </div>                                             
                    </div>
                    <div style="float:left;width: 45%;padding-top: 1em;"> 
                       <div style="width: 170px; float: left;text-align: left;">
                            <%=meLanbide34I18n.getMensaje(idiomaUsuario,"label.MotivoDen3")%>
                       </div>                       
                        <div style="width: 130px; float: left;">                    
                             <div style="width: 220px; float: left;">
                                 <input id="codMotivoDen3ET" name="codMotivoDen3ET" type="text" class="inputTexto" size="2" maxlength="3" 
                                        value="<%=valoresCalculo != null && valoresCalculo.getMotivoDen3()!=null ?valoresCalculo.getMotivoDen3():"" %>">
                                 <input id="descMotivoDen3ET" name="descMotivoDen3ET" type="text" class="inputTexto" size="22" readonly >
                                  <a id="anchorMotivoDen3ET" name="anchorMotivoDen3ET" href="">
                                  <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSector" name="botonSector" style="cursor:hand;" alt="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide34I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"
                                       style="<%=deshabilitado ? "display:none" : ""%>"></span></a>
                             </div>
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
	</div>
<script type="text/javascript">
        document.getElementById('codResulSubvET').value = '<%=valoresCalculo.getResulSubv() != null ? valoresCalculo.getResulSubv() : "" %>';        
        comboResultado = new Combo('ResulSubvET');
        comboMotivoDen1 = new Combo ('MotivoDen1ET');
        comboMotivoDen2 = new Combo ('MotivoDen2ET');
        comboMotivoDen3 = new Combo ('MotivoDen3ET');
        cargarCombos();
        cargarDescripcionesCombos();
</script>

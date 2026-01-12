<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

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
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    EcaSolPreparadoresVO preparadorModif = (EcaSolPreparadoresVO)request.getAttribute("preparadorModif");
    session.removeAttribute("preparadorModif");
    EcaSolPreparadoresVO preparadorOrigen = (EcaSolPreparadoresVO)request.getAttribute("preparadorOrigen");
    session.removeAttribute("preparadorOrigen");
    EcaSolPreparadoresVO sustituto = (EcaSolPreparadoresVO)request.getAttribute("sustituto");
    session.removeAttribute("sustituto");
    Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    EcaConfiguracionVO ecaConfig = (EcaConfiguracionVO)session.getAttribute("ecaConfiguracion");
    
    
    String tituloPagina = "";
    if(consulta)
    {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.consultaPreparador.tituloPagina");
    }
    else
    {
        if(preparadorModif != null)
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.modifPreparador.tituloPagina");
        }
        else
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nuevoPreparador.tituloPagina");
        }
    }
    
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
%>
<title><%=tituloPagina%></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>


<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
    
    var mensajeValidacion = '';
    var nuevo = true;
    
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
        
    function inicio(){
        try{        
            
            <%
            if(preparadorModif != null)
            {
            %>
                nuevo = false;
                document.getElementById('nif').value = '<%=preparadorModif.getNif() != null ? preparadorModif.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApel').value = '<%=preparadorModif.getNombre() != null ? preparadorModif.getNombre().toUpperCase() : "" %>';
                document.getElementById('fechaInicio').value = '<%=preparadorModif.getFecIni() != null ? format.format(preparadorModif.getFecIni()) : "" %>';
                document.getElementById('fechaFin').value = '<%=preparadorModif.getFecFin() != null ? format.format(preparadorModif.getFecFin()) : "" %>';
                document.getElementById('horasAnualesJC').value = '<%=preparadorModif.getHorasJC() != null ? preparadorModif.getHorasJC().toPlainString() : "" %>';
                document.getElementById('horasContrato').value = '<%=preparadorModif.getHorasCont() != null ? preparadorModif.getHorasCont().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEca').value = '<%=preparadorModif.getHorasEca() != null ? preparadorModif.getHorasEca().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJor').value = '<%=preparadorModif.getImpSSJC() != null ? preparadorModif.getImpSSJC().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJor').value = '<%=preparadorModif.getImpSSJR() != null ? preparadorModif.getImpSSJR().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEca').value = '<%=preparadorModif.getImpSSECA() != null ? preparadorModif.getImpSSECA().toPlainString() : "" %>';
                document.getElementById('numSegAnt').value = '<%=preparadorModif.getSegAnt() != null ? preparadorModif.getSegAnt().toString() : "" %>';
                document.getElementById('importeSegAnt').value = '<%=preparadorModif.getImpSegAnt() != null ? preparadorModif.getImpSegAnt().toPlainString() : "" %>';
                document.getElementById('c1h').value = '<%=preparadorModif.getInsC1H() != null ? preparadorModif.getInsC1H().toPlainString() : "" %>';
                document.getElementById('c1m').value = '<%=preparadorModif.getInsC1M() != null ? preparadorModif.getInsC1M().toPlainString() : "" %>';
                document.getElementById('c1total').value = '<%=preparadorModif.getInsC1() != null ? preparadorModif.getInsC1().toPlainString() : "" %>';
                document.getElementById('c2h').value = '<%=preparadorModif.getInsC2H() != null ? preparadorModif.getInsC2H().toPlainString() : "" %>';
                document.getElementById('c2m').value = '<%=preparadorModif.getInsC2M() != null ? preparadorModif.getInsC2M().toPlainString() : "" %>';
                document.getElementById('c2total').value = '<%=preparadorModif.getInsC2() != null ? preparadorModif.getInsC2().toPlainString() : "" %>';
                document.getElementById('c3h').value = '<%=preparadorModif.getInsC3H() != null ? preparadorModif.getInsC3H().toPlainString() : "" %>';
                document.getElementById('c3m').value = '<%=preparadorModif.getInsC3M() != null ? preparadorModif.getInsC3M().toPlainString() : "" %>';
                document.getElementById('c3total').value = '<%=preparadorModif.getInsC3() != null ? preparadorModif.getInsC3().toPlainString() : "" %>';
                document.getElementById('c4h').value = '<%=preparadorModif.getInsC4H() != null ? preparadorModif.getInsC4H().toPlainString() : "" %>';
                document.getElementById('c4m').value = '<%=preparadorModif.getInsC4M() != null ? preparadorModif.getInsC4M().toPlainString() : "" %>';
                document.getElementById('c4total').value = '<%=preparadorModif.getInsC4() != null ? preparadorModif.getInsC4().toPlainString() : "" %>';
                document.getElementById('inserciones').value = '<%=preparadorModif.getInsImporte() != null ? preparadorModif.getInsImporte().toPlainString() : "" %>';
                document.getElementById('insercionesSeg').value = '<%=preparadorModif.getInsSegImporte() != null ? preparadorModif.getInsSegImporte().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSS').value = '<%=preparadorModif.getCoste() != null ? preparadorModif.getCoste().toPlainString() : "" %>';
                document.getElementById('importeConcedido').value = '<%=preparadorModif.getImpteConcedido() != null ? preparadorModif.getImpteConcedido().toPlainString() : "" %>';
            <%
            }
    
            if(preparadorOrigen != null)
            {
            %>
                document.getElementById('nifOrigen').value = '<%=preparadorOrigen.getNif() != null ? preparadorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=preparadorOrigen.getNombre() != null ? preparadorOrigen.getNombre().toUpperCase() : "" %>';            
            <%
            }
            else
            {
            %>
                document.getElementById('nifOrigen').value = '';
                document.getElementById('nomApelOrigen').value = '';  
                document.getElementById('divPrepOrigen').style.display = 'none';
            <%
            }
    
            if(sustituto != null)
            {
            %>
                document.getElementById('nifSustituto').value = '<%=sustituto.getNif() != null ? sustituto.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelSustituto').value = '<%=sustituto.getNombre() != null ? sustituto.getNombre().toUpperCase() : "" %>';              
            <%
            }
            else
            {
            %>
                document.getElementById('nifSustituto').value = '';
                document.getElementById('nomApelSustituto').value = '';          
                document.getElementById('divPrepSustituto').style.display = 'none';             
            <%
            }
            %>
                   
                                     
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
            reemplazarPuntosEca(document.getElementById('c1h'));
            reemplazarPuntosEca(document.getElementById('c1m'));
            reemplazarPuntosEca(document.getElementById('c1total'));
            reemplazarPuntosEca(document.getElementById('c2h'));
            reemplazarPuntosEca(document.getElementById('c2m'));
            reemplazarPuntosEca(document.getElementById('c2total'));
            reemplazarPuntosEca(document.getElementById('c3h'));
            reemplazarPuntosEca(document.getElementById('c3m'));
            reemplazarPuntosEca(document.getElementById('c3total'));
            reemplazarPuntosEca(document.getElementById('c4h'));
            reemplazarPuntosEca(document.getElementById('c4m'));
            reemplazarPuntosEca(document.getElementById('c4total'));
            
            //FormatNumber(document.getElementById('numSegAnt').value, 6, 0, document.getElementById('numSegAnt').id)//entero            
            reemplazarPuntosEca(document.getElementById('importeSegAnt'));
            FormatNumber(document.getElementById('c1h').value, 8, 2, document.getElementById('c1h').id)//entero 
            FormatNumber(document.getElementById('c1m').value, 8, 2, document.getElementById('c1m').id)//entero 
            FormatNumber(document.getElementById('c1total').value, 8, 2, document.getElementById('c1total').id)//entero 
            FormatNumber(document.getElementById('c2m').value, 8, 2, document.getElementById('c2m').id)//entero 
            FormatNumber(document.getElementById('c2h').value, 8, 2, document.getElementById('c2h').id)//entero 
            FormatNumber(document.getElementById('c2total').value, 8, 2, document.getElementById('c2total').id)//entero 
            FormatNumber(document.getElementById('c3h').value, 8, 2, document.getElementById('c3h').id)//entero 
            FormatNumber(document.getElementById('c3m').value, 8, 2, document.getElementById('c3m').id)//entero 
            FormatNumber(document.getElementById('c3total').value, 8, 2, document.getElementById('c3total').id)//entero 
            FormatNumber(document.getElementById('c4h').value, 8, 2, document.getElementById('c4h').id)//entero 
            FormatNumber(document.getElementById('c4m').value, 8, 2, document.getElementById('c4m').id)//entero 
            FormatNumber(document.getElementById('c4total').value, 8, 2, document.getElementById('c4total').id)//entero 
            
            reemplazarPuntosEca(document.getElementById('inserciones'));
            reemplazarPuntosEca(document.getElementById('insercionesSeg'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSS'));
            reemplazarPuntosEca(document.getElementById('importeConcedido'));
            
            
            <%
                if(consulta == true)
                {
            %>
                    //Deshabilito todos los campos
                    document.getElementById('nif').disabled = true;
                document.getElementById('nomApel').disabled = true;
                document.getElementById('fechaInicio').disabled = true;
                document.getElementById('fechaFin').disabled = true;
                document.getElementById('horasAnualesJC').disabled = true;
                document.getElementById('horasContrato').disabled = true;
                document.getElementById('horasDedicacionEca').disabled = true;
                document.getElementById('costesSalarialesSSJor').disabled = true;
                document.getElementById('costesSalarialesSSPorJor').disabled = true;
                document.getElementById('costesSalarialesSSEca').disabled = true;
                document.getElementById('numSegAnt').disabled = true;
                document.getElementById('importeSegAnt').disabled = true;
                document.getElementById('c1h').disabled = true;
                document.getElementById('c1m').disabled = true;
                document.getElementById('c1total').disabled = true;
                document.getElementById('c2h').disabled = true;
                document.getElementById('c2m').disabled = true;
                document.getElementById('c2total').disabled = true;
                document.getElementById('c3h').disabled = true;
                document.getElementById('c3m').disabled = true;
                document.getElementById('c3total').disabled = true;
                document.getElementById('c4h').disabled = true;
                document.getElementById('c4m').disabled = true;
                document.getElementById('c4total').disabled = true;
                document.getElementById('inserciones').disabled = true;
                document.getElementById('insercionesSeg').disabled = true;
                document.getElementById('costesSalarialesSS').disabled = true;
                document.getElementById('importeConcedido').disabled = true;
                
                document.getElementById('btnAceptar').style.display = 'none';
                document.getElementById('btnCancelar').style.display = 'none';
                document.getElementById('btnCerrar').style.display = 'inline';
                document.getElementById('btnFechaFin').style.display = 'none';
                document.getElementById('btnFechaInicio').style.display = 'none';
            <%
                }
            %>    
            ajustarDecimalesImportes();
        }catch(err){
            
        }
    }
        
    function mostrarCalFechaInicio(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFechaInicio").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
    }
        
    function mostrarCalFechaFin(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFechaFin").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
    }
            
    function cancelar(){
        var resultado = jsp_alerta("","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
        if (resultado == 1){
            cerrarVentana();
        }
    }
    
    
    function cerrarVentana(){
      if(navigator.appName=="Microsoft Internet Explorer") { 
            window.parent.window.opener=null; 
            window.parent.window.close(); 
      } else if(navigator.appName=="Netscape") { 
            top.window.opener = top; 
            top.window.open('','_parent',''); 
            top.window.close(); 
       }else{
           window.close(); 
       } 
    }
    
    function guardar(){
        if(validarDatos()){
            
            document.getElementById('msgGuardandoDatos').style.display="inline";
            barraProgresoEca('on', 'barraProgresoNuevoPreparadorSolicitud');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>'
                +'&idPrep=<%=preparadorModif != null && preparadorModif.getSolPreparadoresCod() != null ? preparadorModif.getSolPreparadoresCod() : ""%>'
                +'&nif='+document.getElementById('nif').value
                +'&nomApel='+escape(document.getElementById('nomApel').value)
                +'&feIni='+document.getElementById('fechaInicio').value
                +'&feFin='+document.getElementById('fechaFin').value
                +'&horasAnualesJC='+convertirANumero(document.getElementById('horasAnualesJC').value)
                +'&horasContrato='+convertirANumero(document.getElementById('horasContrato').value)
                +'&horasECA='+convertirANumero(document.getElementById('horasDedicacionEca').value)
                +'&costesSSJor='+convertirANumero(document.getElementById('costesSalarialesSSJor').value)
                +'&costesSSPorJor='+convertirANumero(document.getElementById('costesSalarialesSSPorJor').value)
                +'&costesSSECA='+convertirANumero(document.getElementById('costesSalarialesSSEca').value)
                +'&segAnt='+convertirANumero(document.getElementById('numSegAnt').value)
                +'&importe='+convertirANumero(document.getElementById('importeSegAnt').value)
                +'&c1h='+convertirANumero(document.getElementById('c1h').value)
                +'&c1m='+convertirANumero(document.getElementById('c1m').value)
                +'&c1total='+convertirANumero(document.getElementById('c1total').value)
                +'&c2h='+convertirANumero(document.getElementById('c2h').value)
                +'&c2m='+convertirANumero(document.getElementById('c2m').value)
                +'&c2total='+convertirANumero(document.getElementById('c2total').value)
                +'&c3h='+convertirANumero(document.getElementById('c3h').value)
                +'&c3m='+convertirANumero(document.getElementById('c3m').value)
                +'&c3total='+convertirANumero(document.getElementById('c3total').value)
                +'&c4h='+convertirANumero(document.getElementById('c4h').value)
                +'&c4m='+convertirANumero(document.getElementById('c4m').value)
                +'&c4total='+convertirANumero(document.getElementById('c4total').value)
                +'&inserciones='+convertirANumero(document.getElementById('inserciones').value)
                +'&segIns='+convertirANumero(document.getElementById('insercionesSeg').value)
                +'&costesSS='+convertirANumero(document.getElementById('costesSalarialesSS').value)
                +'&impteConcedido='+convertirANumero(document.getElementById('importeConcedido').value)
                +'&idPrepOrigen=<%=preparadorOrigen != null && preparadorOrigen.getSolPreparadoresCod() != null ? preparadorOrigen.getSolPreparadoresCod() : ""%>'
                +'&control='+control.getTime();
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
                var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var msgValidacion = '';
            var listaPreparadores = new Array();
            var listaErrores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoErrores;
            var nodoCampo;
            var errores;
            var j;
            var camposErrores;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPreparadores[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    camposErrores = new Array();
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NUM_SEG_ANTERIORES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SEGUIMIENTOS_INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = '-';
                            }
                        }
                         else if(hijosFila[cont].nodeName=="IMPORTE_CONCEDIDO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TIPO_SUST"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[31] = nodoCampo.childNodes[0].nodeValue;
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ERRORES"){
                            listaErrores = new Array();
                            nodoErrores = hijosFila[cont];
                            errores = nodoErrores.childNodes;
                            for(var contE = 0; contE < errores.length; contE++){
                                if(errores[contE].nodeName=="ERROR"){
                                    if(errores[contE].childNodes.length > 0){
                                        listaErrores[contE] = errores[contE].childNodes[0].nodeValue;
                                    }
                                }
                            }
                            fila[29] = listaErrores;
                        }
                    }
                    fila[30] = camposErrores;
                    listaPreparadores[j] = fila;
                    fila = new Array();
                }else if(hijos[j].nodeName=="VALIDACION"){                            
                    msgValidacion = hijos[j].childNodes[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    /*var retArray = new Array();
                    retArray[0] = listaPreparadores;
                    retArray[1] = camposErrores;*/
                    window.returnValue =  listaPreparadores;
                    barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    cerrarVentana();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A", msgValidacion);
                }else if(codigoOperacion=="5"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifPreparadorRepetido")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch
            
            barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }
    
    function validarDatos(){
        var nif = document.getElementById('nif').value;
        if(nif == null || nif == ''){
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifVacio")%>';
            return false;
        }
        var nomApel = document.getElementById('nomApel').value;
        if(nomApel == null || nomApel == ''){
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nomApelVacio")%>';
            return false;
        }
        
        
        var correcto = true;
        mensajeValidacion = '';
        
        if(nuevo){
            if(!validarNIFEca(document.getElementById('nif'))){
                document.getElementById('nif').style.border = '1px solid red';
                if(mensajeValidacion == ''){
                    var letra = calcularLetraNifEca(document.getElementById('nif').value);
                    if(letra != ''){
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifIncorrecto")%>'+' '+'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.letraNifDeberiaSer")%>'+letra;
                    }else{
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifIncorrecto")%>';
                    }
                }
                correcto = false;
            }else{
                document.getElementById('nif').removeAttribute("style");
            }
        }
        
        
        if(!comprobarCaracteresEspecialesEca(nomApel)){
            document.getElementById('nomApel').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nomApelCaracteresEspeciales")%>';
            return false;
        }else{
            document.getElementById('nomApel').removeAttribute("style");
        }
        
        if(nuevo){
            if(!validarFechaEca(document.forms[0], document.getElementById('fechaInicio'))){
                document.getElementById('fechaInicio').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaIniIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaInicio').removeAttribute('style');
            }

            if(!validarFechaEca(document.forms[0], document.getElementById('fechaFin'))){
                document.getElementById('fechaFin').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaFinIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaFin').removeAttribute('style');
            }
            if(correcto){
                var feIni = document.getElementById('fechaInicio').value;
                var feFin = document.getElementById('fechaFin').value;
                if(feIni != '' && feFin != ''){
                    var array_fecha_ini = feIni.split('/');
                    var diaIni = array_fecha_ini[0];
                    var mesIni = array_fecha_ini[1];
                    var anoIni = array_fecha_ini[2];
                    var dIni = new Date(anoIni, mesIni-1, diaIni, 0, 0, 0, 0);

                    var array_fecha_fin = feFin.split('/');
                    var diaFin = array_fecha_fin[0];
                    var mesFin = array_fecha_fin[1];
                    var anoFin = array_fecha_fin[2];
                    var dFin = new Date(anoFin, mesFin-1, diaFin, 0, 0, 0, 0);


                    var n1 = dFin.getTime();
                    var n2 = dIni.getTime();
                    var result = n1 - n2;
                    if(result < 0){
                        document.getElementById('fechaInicio').style.border = '1px solid red';
                        document.getElementById('fechaFin').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaIniPosteriorFin")%>';
                        correcto = false;
                    }
                    else{
                        if(anoIni != '<%=anoExpediente%>'){
                            correcto = false;
                            document.getElementById('fechaInicio').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.fechasAno"), anoExpediente)%>';
                        }else{
                            document.getElementById('fechaInicio').removeAttribute('style');
                        }
                        if(anoFin != '<%=anoExpediente%>'){
                            correcto = false;
                            document.getElementById('fechaFin').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.fechasAno"), anoExpediente)%>';
                        }else{
                            document.getElementById('fechaFin').removeAttribute('style');
                        }
                    }
                }
            }
        }
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicación ECA
        var hajc = 0.0;
        var hc = 0.0;
        var hdECA = 0.0;

        if(document.getElementById('horasAnualesJC').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasAnualesJC'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasAnualesJC').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCIncorrecto")%>';
                }else{
                    document.getElementById('horasAnualesJC').removeAttribute('style');
                    hajc = parseFloat(convertirANumero(document.getElementById('horasAnualesJC').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCIncorrecto")%>';
            }
        }
        if(document.getElementById('horasContrato').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(hajc < hc){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCMenorHorasContrato")%>';
            }
            else{
                document.getElementById('horasAnualesJC').removeAttribute('style');
            }
            if(hc < hdECA){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoMenorHorasDedicacionEca")%>';
            }else{
                document.getElementById('horasContrato').removeAttribute('style');
            }
        }
        
        //Horas dedicación ECA >= 50% Horas contrato
        hc = 0.0;
        hdECA = 0.0;
        if(document.getElementById('horasContrato').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicación ECA
        var cssJC = 0.0;
        var cssPorJor = 0.0;
        var cssECA = 0.0;

        if(document.getElementById('costesSalarialesSSJor').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSJor'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSJor').removeAttribute('style');
                    cssJC = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSPorJor').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSPorJor'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSPorJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
                    cssPorJor = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSPorJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSPorJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSEcaIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSEca').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(cssJC < cssPorJor){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSalarialesJorMenorCostesSalarialesPorJor")%>';
            }
            else{
                document.getElementById('costesSalarialesSSJor').removeAttribute('style');
            }
            if(cssPorJor < cssECA){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSalarialesPorJorMenorCostesSalarialesEca")%>';
            }else{
                document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
            }
        }

        if(document.getElementById('numSegAnt').value != ''){
            try{
                if(!validarNumericoEca(document.getElementById('numSegAnt'))){
                    correcto = false;
                    document.getElementById('numSegAnt').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.numSegAntIncorrecto"), anoExpediente)%>';
                }else{
                    document.getElementById('numSegAnt').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('numSegAnt').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.numSegAntIncorrecto"), anoExpediente)%>';
            }
        }

       <%-- if(document.getElementById('importeSegAnt').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('importeSegAnt'), 8, 2)){
                    correcto = false;
                    document.getElementById('importeSegAnt').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.importeSegAntIncorrecto")%>';
                }else{
                    document.getElementById('importeSegAnt').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('importeSegAnt').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.importeSegAntIncorrecto")%>';
            }
        
            if(correcto && nuevo){
                //Importe Seguimientos: si es mayor al 25% de los costes salariales + seg social ECA, limitar a dicho valor
                var txtImporte = document.getElementById('importeSegAnt').value;
                //txtImporte = reemplazarTextoEca(txtImporte, /,/g, '.');
                txtImporte = convertirANumero(txtImporte);
                var importe = parseFloat(txtImporte);
                var poMax = <%=ecaConfig != null && ecaConfig.getPoMaxSeguimientos() != null ? ecaConfig.getPoMaxSeguimientos().doubleValue() : 1.0%>;
                var por = cssECA * poMax;
                if(importe < por){
                    //Hay que comprobar que corresponda a numSeg * config.getImSeguimiento()
                    if(document.getElementById('numSegAnt').value != ''){
                        var segAnt = parseInt(convertirANumero(document.getElementById('numSegAnt').value));
                        var imSeg = <%=ecaConfig != null && ecaConfig.getImSeguimiento() != null ? ecaConfig.getImSeguimiento().doubleValue() : ""%>;
                        if(imSeg != ''){
                            var res = imSeg * segAnt;
                            if(res != importe){
                                correcto = false;
                                document.getElementById('importeSegAnt').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.importeSegAntIncorrecto"), anoExpediente)%>';
                            }else{
                                document.getElementById('importeSegAnt').removeAttribute('style');
                            }
                        }
                    }
                }else{
                    document.getElementById('importeSegAnt').value = por;
                    reemplazarPuntosEca(document.getElementById('importeSegAnt'));
                }
            }
        }--%>

        if(document.getElementById('c1h').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c1h'),8,2)){
                    correcto = false;
                    document.getElementById('c1h').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1hIncorrecto")%>';
                }else{
                    document.getElementById('c1h').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c1h').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1hIncorrecto")%>';
            }
        }

        if(document.getElementById('c1m').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c1m'),8,2)){
                    correcto = false;
                    document.getElementById('c1m').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1mIncorrecto")%>';
                }else{
                    document.getElementById('c1m').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c1m').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1mIncorrecto")%>';
            }
        }

        if(document.getElementById('c1total').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c1total'),8,2)){
                    correcto = false;
                    document.getElementById('c1total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1totalIncorrecto")%>';
                }else{
                    document.getElementById('c1total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c1total').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c1totalIncorrecto")%>';
            }
        }

        if(document.getElementById('c2h').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c2h'),8,2)){
                    correcto = false;
                    document.getElementById('c2h').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2hIncorrecto")%>';
                }else{
                    document.getElementById('c2h').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c2h').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2hIncorrecto")%>';
            }
        }

        if(document.getElementById('c2m').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c2m'),8,2)){
                    correcto = false;
                    document.getElementById('c2m').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2mIncorrecto")%>';
                }else{
                    document.getElementById('c2m').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c2m').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2mIncorrecto")%>';
            }
        }

        if(document.getElementById('c2total').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c2total'),8,2)){
                    correcto = false;
                    document.getElementById('c2total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2totalIncorrecto")%>';
                }else{
                    document.getElementById('c2total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c2total').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c2totalIncorrecto")%>';
            }
        }

        if(document.getElementById('c3h').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c3h'),8,2)){
                    correcto = false;
                    document.getElementById('c3h').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3hIncorrecto")%>';
                }else{
                    document.getElementById('c3h').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c3h').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3hIncorrecto")%>';
            }
        }

        if(document.getElementById('c3m').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c3m'),8,2)){
                    correcto = false;
                    document.getElementById('c3m').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3mIncorrecto")%>';
                }else{
                    document.getElementById('c3m').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c3m').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3mIncorrecto")%>';
            }
        }

        if(document.getElementById('c3total').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c3total'),8,2)){
                    correcto = false;
                    document.getElementById('c3total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3totalIncorrecto")%>';
                }else{
                    document.getElementById('c3total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c3total').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c3totalIncorrecto")%>';
            }
        }

        if(document.getElementById('c4h').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c4h'),8,2)){
                    correcto = false;
                    document.getElementById('c4h').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4hIncorrecto")%>';
                }else{
                    document.getElementById('c4h').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c4h').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4hIncorrecto")%>';
            }
        }

        if(document.getElementById('c4m').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c4m'),8,2)){
                    correcto = false;
                    document.getElementById('c4m').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4mIncorrecto")%>';
                }else{
                    document.getElementById('c4m').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c4m').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4mIncorrecto")%>';
            }
        }

        if(document.getElementById('c4total').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c4total'),8,2)){
                    correcto = false;
                    document.getElementById('c4total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4totalIncorrecto")%>';
                }else{
                    document.getElementById('c4total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c4total').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.c4totalIncorrecto")%>';
            }
        }
        
        if(document.getElementById('inserciones').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('inserciones'), 8, 2)){
                    correcto = false;
                    document.getElementById('inserciones').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.insercionesIncorrecto")%>';
                }else{
                    document.getElementById('inserciones').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('inserciones').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.insercionesIncorrecto")%>';
            }
        }

        if(document.getElementById('insercionesSeg').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('insercionesSeg'), 8, 2)){
                    correcto = false;
                    document.getElementById('insercionesSeg').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.insercionesSegIncorrecto")%>';
                }else{
                    document.getElementById('insercionesSeg').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('insercionesSeg').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.insercionesSegIncorrecto")%>';
            }
        }

        if(document.getElementById('costesSalarialesSS').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSS'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSS').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costeSSIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSS').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSS').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costeSSIncorrecto")%>';
            }
        }
        
        //Los totales de cada colectivo = H + M
        if(correcto && nuevo){
            try
            {
                var c1h = document.getElementById('c1h').value != '' ? parseFloat(convertirANumero(document.getElementById('c1h').value)) : 0.0;
                var c1m = document.getElementById('c1m').value != '' ? parseFloat(convertirANumero(document.getElementById('c1m').value)) : 0.0;
                var c1tot = document.getElementById('c1total').value != '' ? parseFloat(convertirANumero(document.getElementById('c1total').value)) : 0.0;
                var suma1 = c1h + c1m;
                if(suma1 != c1tot){
                    correcto = false;
                    document.getElementById('c1total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC1Incorrecto")%>';
                }else{
                    document.getElementById('c1total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC1Incorrecto")%>';
            }
            
            try
            {
                var c2h = document.getElementById('c2h').value != '' ? parseFloat(convertirANumero(document.getElementById('c2h').value)) : 0.0;
                var c2m = document.getElementById('c2m').value != '' ? parseFloat(convertirANumero(document.getElementById('c2m').value)) : 0.0;
                var c2tot = document.getElementById('c2total').value != '' ? parseFloat(convertirANumero(document.getElementById('c2total').value)) : 0.0;
                var suma2 = c2h + c2m;
                if(suma2 != c2tot){
                    correcto = false;
                    document.getElementById('c2total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC2Incorrecto")%>';
                }else{
                    document.getElementById('c2total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC2Incorrecto")%>';
            }
            
            try
            {
                var c3h = document.getElementById('c3h').value != '' ? parseFloat(convertirANumero(document.getElementById('c3h').value)) : 0.0;
                var c3m = document.getElementById('c3m').value != '' ? parseFloat(convertirANumero(document.getElementById('c3m').value)) : 0.0;
                var c3tot = document.getElementById('c3total').value != '' ? parseFloat(convertirANumero(document.getElementById('c3total').value)) : 0.0;
                var suma3 = c3h + c3m;
                if(suma3 != c3tot){
                    correcto = false;
                    document.getElementById('c3total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC3Incorrecto")%>';
                }else{
                    document.getElementById('c3total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC3Incorrecto")%>';
            }
            
            try
            {
                var c4h = document.getElementById('c4h').value != '' ? parseFloat(convertirANumero(document.getElementById('c4h').value)) : 0.0;
                var c4m = document.getElementById('c4m').value != '' ? parseFloat(convertirANumero(document.getElementById('c4m').value)) : 0.0;
                var c4tot = document.getElementById('c4total').value != '' ? parseFloat(convertirANumero(document.getElementById('c4total').value)) : 0.0;
                var suma4 = c4h + c4m;
                if(suma4 != c4tot){
                    correcto = false;
                    document.getElementById('c4total').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC4Incorrecto")%>';
                }else{
                    document.getElementById('c4total').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('c4total').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insC4Incorrecto")%>';
            }
        
            //Importe de las inserciones. Cada Colectivo tiene un precio
            if(document.getElementById('inserciones').value != ''){
                var importe = parseFloat(convertirANumero(document.getElementById('inserciones').value));
                var importeCalculado = 0.0;

                if(document.getElementById('c1h').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c1h').value)) * <%=ecaConfig != null && ecaConfig.getImC1h() != null ? ecaConfig.getImC1h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c1m').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c1m').value)) * <%=ecaConfig != null && ecaConfig.getImC1m() != null ? ecaConfig.getImC1m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c2h').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c2h').value)) * <%=ecaConfig != null && ecaConfig.getImC2h() != null ? ecaConfig.getImC2h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c2m').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c2m').value)) * <%=ecaConfig != null && ecaConfig.getImC2m() != null ? ecaConfig.getImC2m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c3h').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c3h').value)) * <%=ecaConfig != null && ecaConfig.getImC3h() != null ? ecaConfig.getImC3h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c3m').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c3m').value)) * <%=ecaConfig != null && ecaConfig.getImC3m() != null ? ecaConfig.getImC3m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c4h').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c4h').value)) * <%=ecaConfig != null && ecaConfig.getImC4h() != null ? ecaConfig.getImC4h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c4m').value != ''){
                    importeCalculado += parseFloat(convertirANumero(document.getElementById('c4m').value)) * <%=ecaConfig != null && ecaConfig.getImC4m() != null ? ecaConfig.getImC4m().doubleValue() : 1.0%>;
                }

                if(importeCalculado != importe){
                    correcto = false;
                    document.getElementById('inserciones').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.insImporteIncorrecto")%>';
                }else{
                    document.getElementById('inserciones').removeAttribute('style');
                }
            }
            
            var csSScalculado = false;
            //Seguimientos + Inserciones: controlar que es la suma Importe + Inserciones
            if(document.getElementById('insercionesSeg').value != ''){
                if(document.getElementById('importeSegAnt').value != '' || document.getElementById('inserciones').value != ''){
                    var suma = 0.0;
                    var impSegAntTxt = document.getElementById('importeSegAnt').value;
                    //impSegAntTxt = reemplazarTextoEca(impSegAntTxt, /,/g, '.');
                    impSegAntTxt = convertirANumero(impSegAntTxt);
                    var insercionesTxt = document.getElementById('inserciones').value;
                    //insercionesTxt = reemplazarTextoEca(insercionesTxt, /,/g, '.');
                    insercionesTxt = convertirANumero(insercionesTxt);
                    suma += impSegAntTxt != '' ? parseFloat(impSegAntTxt) : 0.0;
                    suma += insercionesTxt != '' ? parseFloat(insercionesTxt) : 0.0;
                    suma = parseFloat(suma.toFixed(2));
                    var insercionesSegTxt = document.getElementById('insercionesSeg').value;
                    //insercionesSegTxt = reemplazarTextoEca(insercionesSegTxt, /,/g, '.');
                    insercionesSegTxt = convertirANumero(insercionesSegTxt);
                    var insSegImporte = parseFloat(insercionesSegTxt);
                    if(insSegImporte != suma){
                        correcto = false;
                        document.getElementById('insercionesSeg').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.segInsNoSumaImpIns")%>';
                    }else{
                        document.getElementById('insercionesSeg').removeAttribute('style');
                    }

                    csSScalculado = true;
                    //Comprobar costes salariales + ss
                    if(document.getElementById('costesSalarialesSS').value != ''){
                        var coste = parseFloat(convertirANumero(document.getElementById('costesSalarialesSS').value));
                        var minimo;
                        if(document.getElementById('costesSalarialesSSEca').value != ''){
                            var imSSEca = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                            minimo = Math.min(insSegImporte, imSSEca);
                        }else{
                            minimo = insSegImporte;
                        }


                        if(coste > minimo){
                            correcto = false;
                            document.getElementById('costesSalarialesSS').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.costesSalarialesSSIncorrecto")%>';
                        }else{
                            document.getElementById('costesSalarialesSS').removeAttribute('style');
                        }
                    }
                }
            }
            
            if(!csSScalculado){
                if(document.getElementById('costesSalarialesSS').value != ''){
                    if(document.getElementById('costesSalarialesSSEca').value){
                        var coste = parseFloat(convertirANumero(document.getElementById('costesSalarialesSS').value));
                        var minimo = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));

                        if(coste > minimo){
                            correcto = false;
                            document.getElementById('costesSalarialesSS').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.costesSalarialesSSIncorrecto")%>';
                        }else{
                            document.getElementById('costesSalarialesSS').removeAttribute('style');
                        }
                    }else{
                        correcto = false;
                        document.getElementById('costesSalarialesSS').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.costesSalarialesSSIncorrecto")%>';
                    }
                }
            }
        }
        
        return correcto;
    }
    
    function calcularImporteSeguimientos(campo){
        if(document.getElementById('numSegAnt').value != ''){
           
            /*if (campo.id == 'numSegAnt')
                campo.value = campo.value.replace(',00','');
            else  //reemplazarPuntosEca(campo);
                FormatNumber(document.getElementById('numSegAnt').value, 6, 0, document.getElementById('numSegAnt').id);*/
            var correcto = true;
            try{
                if(!validarNumericoEca(document.getElementById('numSegAnt'))){
                    correcto = false;
                    document.getElementById('numSegAnt').style.border = '1px solid red';
                }else{
                    document.getElementById('numSegAnt').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('numSegAnt').style.border = '1px solid red';
            }

            if(correcto){
                try
                {
                    var numSeg = parseInt(convertirANumero(document.getElementById('numSegAnt').value));
                    var imSeg = <%=ecaConfig != null && ecaConfig.getImSeguimiento() != null ? ecaConfig.getImSeguimiento().doubleValue() : 0.0%>;
                    var poMax = <%=ecaConfig != null && ecaConfig.getPoMaxSeguimientos() != null ? ecaConfig.getPoMaxSeguimientos().doubleValue() : 1.0%>;
                    var importe = 0.0;
                    importe = numSeg * imSeg;
                    //var cssECA = parseFloat(document.getElementById('costesSalarialesSSEca').value);
                    //importe = ajustarDecimalesEca(importe, 2);
                    var cssECA = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                    
                    var por = cssECA * poMax;
                    //por =ajustarDecimalesEca(por, 2);
                    if(importe > por){
                        document.getElementById('importeSegAnt').value = ajustarDecimalesEca(por, 2);                        
                        reemplazarPuntosEca(document.getElementById('importeSegAnt'));     
                    }else{
                        document.getElementById('importeSegAnt').value = ajustarDecimalesEca(importe, 2);
                        reemplazarPuntosEca(document.getElementById('importeSegAnt'));
                    }
                }catch(err){

                }
            }else{
                document.getElementById('importeSegAnt').value = '';
            }
        }else{
            document.getElementById('importeSegAnt').value = '';
        }
        calcularInsercionesSeg();
       
    }
    
    function deshabilitarImporteSeguimientos(){
        var campo = document.getElementById('costesSalarialesSSEca');
        if(campo.value != ''){
            reemplazarPuntosEca(campo);
            FormatNumber(campo.value, 8, 2, campo.id);
            document.getElementById('importeSegAnt').disabled = false;
        }else{
            document.getElementById('importeSegAnt').value = '';
            document.getElementById('importeSegAnt').disabled = true;
        }
    }
    
    function calcularTotalC1(){
        var c1h;
        var c1m;
        var tot;
        var hayValor = false;
        var valor1h = document.getElementById('c1h').value;
        valor1h = convertirANumero(valor1h);
        var valor1m = document.getElementById('c1m').value;
        valor1m = convertirANumero(valor1m);
        //FormatNumber(document.getElementById('c1h').value, 6, 0, document.getElementById('c1h').id);
        //FormatNumber(document.getElementById('c1m').value, 6, 0, document.getElementById('c1m').id);        
        try{
            c1h = parseFloat(valor1h);
            if(valor1h == '' || isNaN(valor1h)){
                c1h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c1h = 0.0;
        }
        
        try{
            c1m = parseFloat(valor1m);
            if(valor1m == '' || isNaN(valor1m)){
                c1m = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c1m = 0.0;
        }
        
        if(hayValor){
            tot = c1h + c1m;
        }else{
            tot = '';
        }
        document.getElementById('c1total').value = tot;
        //FormatNumber(document.getElementById('c1total').value, 6, 0, document.getElementById('c1total').id);
        reemplazarPuntosEca(document.getElementById('c1total'));
        calcularInserciones();
    }
    
    function calcularTotalC2(){
        var c2h;
        var c2m;
        var tot;
        var hayValor = false;
        var valor2h = document.getElementById('c2h').value;
        valor2h = convertirANumero(valor2h);
        var valor2m = document.getElementById('c2m').value;
        valor2m = convertirANumero(valor2m);
        //reemplazarPuntosEca(document.getElementById('c2h'));
        //reemplazarPuntosEca(document.getElementById('c2m'));
        
        //FormatNumber(document.getElementById('c2m').value, 6, 0, document.getElementById('c2m').id);
        //FormatNumber(document.getElementById('c2h').value, 6, 0, document.getElementById('c2h').id);
        try{
            c2h = parseFloat(valor2h);
            if(valor2h == '' || isNaN(valor2h)){
                c2h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c2h = 0.0;
        }
        
        try{
            c2m = parseFloat(valor2m);
            if(valor2m == '' || isNaN(valor2m)){
                c2m = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c2m = 0.0;
        }
        
        if(hayValor){
            tot = c2h + c2m;
        }else{
            tot = '';
        }
        document.getElementById('c2total').value = tot;
        reemplazarPuntosEca(document.getElementById('c2total'));
        //FormatNumber(document.getElementById('c2total').value, 6, 0, document.getElementById('c2total').id);
        
        calcularInserciones();
    }
    
    function calcularTotalC3(){
        var c3h;
        var c3m;
        var tot;
        var hayValor = false;
        var valor3h = document.getElementById('c3h').value;
        valor3h = convertirANumero(valor3h);
        var valor3m = document.getElementById('c3m').value;
        valor3m = convertirANumero(valor3m);
        //reemplazarPuntosEca(document.getElementById('c3h'));
        //reemplazarPuntosEca(document.getElementById('c3m'));
        //FormatNumber(document.getElementById('c3h').value, 6, 0, document.getElementById('c3h').id);
        //FormatNumber(document.getElementById('c3m').value, 6, 0, document.getElementById('c3m').id);
        try{
            c3h = parseFloat(valor3h);
            if(valor3h == '' || isNaN(valor3h)){
                c3h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c3h = 0.0;
        }
        
        try{
            c3m = parseFloat(valor3m);
            if(valor3m == '' || isNaN(valor3m)){
                c3m = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c3m = 0.0;
        }
        
        if(hayValor){
            tot = c3h + c3m;
        }else{
            tot = '';
        }
        document.getElementById('c3total').value = tot;
        reemplazarPuntosEca(document.getElementById('c3total'));
        //FormatNumber(document.getElementById('c3total').value, 6, 0, document.getElementById('c3total').id);
        calcularInserciones();
    }
    
    function calcularTotalC4(){
        var c4h;
        var c4m;
        var tot;
        var hayValor = false;
        var valor4h = document.getElementById('c4h').value;
        valor4h = convertirANumero(valor4h);
        var valor4m = document.getElementById('c4m').value;
        valor4m = convertirANumero(valor4m);
        //reemplazarPuntosEca(document.getElementById('c4h'));
        //reemplazarPuntosEca(document.getElementById('c4m'));
        //FormatNumber(document.getElementById('c4h').value, 6, 0, document.getElementById('c4h').id);
        //FormatNumber(document.getElementById('c4m').value, 6, 0, document.getElementById('c4m').id);
        try{
            c4h = parseFloat(valor4h);
            if(valor4h == '' || isNaN(valor4h)){
                c4h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c4h = 0.0;
        }
        
        try{
            c4m = parseFloat(valor4m);
            if(valor4m == '' || isNaN(valor4m)){
                c4m = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c4m = 0.0;
        }
        
        if(hayValor){
            tot = c4h + c4m;
        }else{
            tot = '';
        }
        document.getElementById('c4total').value = tot;
        reemplazarPuntosEca(document.getElementById('c4total'));
        //FormatNumber(document.getElementById('c4total').value, 6, 0, document.getElementById('c4total').id);
        calcularInserciones();
    }
    
    function calcularInserciones(){
        var importeCalculado = 0.0;
        var hayValor = false;
        var valor;

        if(document.getElementById('c1h').value != '' && validarNumericoDecimalEca(document.getElementById('c1h'),8,2)){
            hayValor = true;
            valor = document.getElementById('c1h').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC1h() != null ? ecaConfig.getImC1h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c1m').value != '' && validarNumericoDecimalEca(document.getElementById('c1m'),8,2)){
            hayValor = true;
            valor = document.getElementById('c1m').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC1m() != null ? ecaConfig.getImC1m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c2h').value != '' && validarNumericoDecimalEca(document.getElementById('c2h'),8,2)){
            hayValor = true;
            valor = document.getElementById('c2h').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC2h() != null ? ecaConfig.getImC2h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c2m').value != '' && validarNumericoDecimalEca(document.getElementById('c2m'),8,2)){
            hayValor = true;
            valor = document.getElementById('c2m').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC2m() != null ? ecaConfig.getImC2m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c3h').value != '' && validarNumericoDecimalEca(document.getElementById('c3h'),8,2)){
            hayValor = true;
            valor = document.getElementById('c3h').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC3h() != null ? ecaConfig.getImC3h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c3m').value != '' && validarNumericoDecimalEca(document.getElementById('c3m'),8,2)){
            hayValor = true;
            valor = document.getElementById('c3m').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC3m() != null ? ecaConfig.getImC3m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c4h').value != '' && validarNumericoDecimalEca(document.getElementById('c4h'),8,2)){
            hayValor = true;
            valor = document.getElementById('c4h').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC4h() != null ? ecaConfig.getImC4h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c4m').value != '' && validarNumericoDecimalEca(document.getElementById('c4m'),8,2)){
            hayValor = true;
            valor = document.getElementById('c4m').value;
            valor = convertirANumero(valor);
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC4m() != null ? ecaConfig.getImC4m().doubleValue() : 1.0%>;
        }
        if(hayValor){
            document.getElementById('inserciones').value = parseFloat(importeCalculado);
            reemplazarPuntosEca(document.getElementById('inserciones'));
            //FormatNumber(document.getElementById('inserciones').value, 8, 2, document.getElementById('inserciones').id);
        }else{
            document.getElementById('inserciones').value = '';
        }
        
        calcularInsercionesSeg();
    }
    
    function calcularInsercionesSeg(){
        if(document.getElementById('importeSegAnt').value != '' || document.getElementById('inserciones').value != ''){
            var suma = 0.0;
            var impSegAntTxt = document.getElementById('importeSegAnt').value;
            impSegAntTxt = convertirANumero(impSegAntTxt);
            var insercionesTxt = document.getElementById('inserciones').value;
            insercionesTxt = convertirANumero(insercionesTxt);
            suma += impSegAntTxt != '' ? parseFloat(impSegAntTxt) : 0.0;
            suma += insercionesTxt != '' ? parseFloat(insercionesTxt) : 0.0;
            document.getElementById('insercionesSeg').value = suma;
            reemplazarPuntosEca(document.getElementById('insercionesSeg'));
            //FormatNumber(document.getElementById('insercionesSeg').value, 8, 2, document.getElementById('insercionesSeg').id)
        }else{
            document.getElementById('insercionesSeg').value = '';
        }
        
        calcularCostesSalarialesSS();
    }
            
    function ajustarDecimalesImportes(){
        var campo;

        campo = document.getElementById('c1h');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c1m');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c1total');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c2h');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c2m');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c2total');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c3h');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c3m');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c3total');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c4h');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c4m');
        ajustarDecimalesCampo(campo, 2);
        campo = document.getElementById('c4total');
        ajustarDecimalesCampo(campo, 2);

        //Horas anueales jornada completa
        campo = document.getElementById('horasAnualesJC');
        ajustarDecimalesCampo(campo, 2);

        //Horas contrato
        campo = document.getElementById('horasContrato');
        ajustarDecimalesCampo(campo, 2);

        //Horas dedicacion ECA
        campo = document.getElementById('horasDedicacionEca');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS Jor
        campo = document.getElementById('costesSalarialesSSJor');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS % Jor
        campo = document.getElementById('costesSalarialesSSPorJor');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS ECA
        campo = document.getElementById('costesSalarialesSSEca');
        ajustarDecimalesCampo(campo, 2);

        //Importe seg ant
        campo = document.getElementById('importeSegAnt');
        ajustarDecimalesCampo(campo, 2);

        //Seguimientos + Inserciones
        campo = document.getElementById('insercionesSeg');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('costesSalarialesSS');
        ajustarDecimalesCampo(campo, 2);
        
        //Importe concedido
        campo = document.getElementById('importeConcedido');
        ajustarDecimalesCampo(campo, 2);
        
        //Inserciones
        campo = document.getElementById('inserciones');
        ajustarDecimalesCampo(campo, 2);
    }
    
    function calcularCostesSalarialesECA(){
        var c1 = document.getElementById('horasDedicacionEca');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 50, 25)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 50, 25)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 50, 25)){
                    try{
                        valor = c1.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSEca').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSEca').value = '';
        }
        
        calcularImporteSeguimientos(document.getElementById('costesSalarialesSSEca'));
        calcularCostesSalarialesSS();
    }
    
    function calcularCostesSalarialesPorJor(){
        var c1 = document.getElementById('horasContrato');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 50, 25)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 50, 25)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 50, 25)){
                    try{
                        valor = c1.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSPorJor').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSPorJor').value = '';
        }
    }
    
    function calcularCostesSalarialesSSJC(){
        var a = document.getElementById('horasAnualesJC');
        var f = document.getElementById('costesSalarialesSSEca');
        var c = document.getElementById('horasDedicacionEca');
       
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;
        
        if(a.value != '' && validarNumericoDecimalEca(a, 50, 25)){
            if(f.value != '' && validarNumericoDecimalEca(f, 50, 25)){
                if(c.value != '' && validarNumericoDecimalEca(c, 50, 25)){
                    try{
                        valor = a.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = f.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSJor').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSJor').value = '';
        }
    }
    
    function verComparacion(){        
            var control = new Date();
            var result = null;
            var opcion = '';
            
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=compararDatosPrepSol&tipo=0&numero=<%=numExpediente%>&idPrep=<%=preparadorModif != null && preparadorModif.getSolPreparadoresCod() != null ? preparadorModif.getSolPreparadoresCod() : ""%>&control='+control.getTime(),560,850,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=compararDatosPrepSol&tipo=0&numero=<%=numExpediente%>&idPrep=<%=preparadorModif != null && preparadorModif.getSolPreparadoresCod() != null ? preparadorModif.getSolPreparadoresCod() : ""%>&control='+control.getTime(),580,870,'no','no');
            }
            /*if (result != undefined){
                recargarTablaPreparadoresSolicitudEca(result, true);
            } */       
    }
    
    function calcularCostesSalarialesSS(){
        //reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
        //reemplazarPuntosEca(document.getElementById('insercionesSeg'));
        var txtSSEca = validarNumericoDecimalEca(document.getElementById('costesSalarialesSSEca'), 50, 25) ? document.getElementById('costesSalarialesSSEca').value : '';
        var txtTotal = validarNumericoDecimalEca(document.getElementById('insercionesSeg'), 50, 25) ? document.getElementById('insercionesSeg').value : '';
        txtSSEca = convertirANumero(txtSSEca);
        txtTotal = convertirANumero(txtTotal);
        var minimo;
        if(txtSSEca != '' && txtTotal != ''){
            var eca = parseFloat(txtSSEca);
            var tot = parseFloat(txtTotal);
            minimo = Math.min(eca, tot);
        }else if(txtSSEca != ''){
            minimo = parseFloat(txtSSEca);
        }else if(txtTotal != ''){
            minimo = parseFloat(txtTotal);
        }
        if(minimo != undefined && minimo != null){
            document.getElementById('costesSalarialesSS').value = minimo;
            reemplazarPuntosEca(document.getElementById('costesSalarialesSS'));
        }else{
            document.getElementById('costesSalarialesSS').value = '';
        }
        
    }
</script>
<body onload="inicio();" class="contenidoPantalla">
    <form>
        <div id="barraProgresoNuevoPreparadorSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgGuardandoDatos">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width:5%;height:20%;"></td>
                                            <td class="imagenHide"></td>
                                            <td style="width:5%;height:20%;"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="height:10%" ></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
        <div style="width: 100%; padding: 10px; text-align: left;">
            <div class="sub3titulo" style="text-align: left;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.datosPreparador")%>
                </span>
            </div>
            <div class="lineaFormulario" id="divPrepOrigen" style="height: 30px; margin-top: 10px; margin-bottom: 10px;">
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.preparadorOrigen")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifOrigen" name="nifOrigen" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 500px; float: left;">
                    <input type="text" id="nomApelOrigen" name="nomApelOrigen" size="78" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
            </div>
            <div style="clear:both"></div>
            <fieldset style="width: 94%;">    
                <legend>Preparador</legend>
                <div class="lineaFormulario">
                    <div style="width: 36px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nif")%><font color="red">*</font>
                    </div>
                    <div style="width: 101px; float: left;">
                        <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto"/>
                    </div>
                    <div style="width: 140px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nomApel")%><font color="red">*</font>
                    </div>
                    <div style="width: 540px; float: left;">
                        <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 169px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A id="btnFechaInicio" href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaInicio" name="calFechaInicio" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                    <div style="width: 155px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFin" name="fechaFin" onkeyup="javascript: return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A id="btnFechaFin" href="javascript:calClick(event);" onclick="mostrarCalFechaFin(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaFin" name="calFechaFin" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                        
                </div>
                <div class="lineaFormulario">
                    <div style="width: 155px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasAnualesJC")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico"
                               onkeyup="calcularCostesSalarialesSSJC();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularCostesSalarialesSSJC();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 117px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasContrato")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico"                           
                               onkeyup="calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 157px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasDedicacionEca")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 155; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSJor")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 117px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSPorJor")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 157px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSEca")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSEca" name="costesSalarialesSSEca" size="11" maxlength="9" class="inputTexto textoNumerico" onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 246px; float: left;">
                        <%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.numSegAnt"), anoExpediente)%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="numSegAnt" name="numSegAnt" size="11" maxlength="8" class="inputTexto textoNumerico" 
                               onkeyup="calcularImporteSeguimientos(this);FormatNumber(this.value, 8, 2, this.id);"
                               onblur=" ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 70px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.importeSegAnt")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="importeSegAnt" name="importeSegAnt" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1h")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c1h" name="c1h" size="11" maxlength="9" class="inputTexto textoNumerico"                                                    
                               onkeyup="calcularTotalC1();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC1();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1m")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c1m" name="c1m" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC1();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC1();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 72px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1total")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c1total" name="c1total" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2h")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c2h" name="c2h" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC2();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC2();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2m")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c2m" name="c2m" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC2();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC2();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 72px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2total")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c2total" name="c2total" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3h")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c3h" name="c3h" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC3();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC3();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3m")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c3m" name="c3m" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC3();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC3();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 72px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3total")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c3total" name="c3total" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4h")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c4h" name="c4h" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC4();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC4();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 51px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4m")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c4m" name="c4m" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularTotalC4();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularTotalC4();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 72px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4total")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="c4total" name="c4total" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 92px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.inserciones")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="inserciones" name="inserciones" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 190px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.insercionesSeg")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="insercionesSeg" name="insercionesSeg" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 150px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSS")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSS" name="costesSalarialesSS" size="11" maxlength="9" class="inputTexto textoNumerico" onkeyup="FormatNumber(this.value, 8, 2, this.id);" onblur="ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 162px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.impteConcedido")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="importeConcedido" name="importeConcedido" size="11" maxlength="9" class="inputTexto textoNumerico" onkeyup="FormatNumber(this.value, 8, 2, this.id);" onblur="ajustarDecimalesImportes();"/>
                    </div>
                </div>
            </fieldset>
            <div class="lineaFormulario" id="divPrepSustituto" style="height: 30px; margin-top: 10px; margin-bottom: 10px;">
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.preparadorSustituto")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifSustituto" name="nifSustituto" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApelSustituto" name="nomApelSustituto" size="78" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
               
            </div>
        </div>

            <div class="botonera" style="padding-top: 20px;">
                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                <input type="button" id="btnComparacion" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.comparacion")%>" onclick="verComparacion();">
            </div>
            
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
</body>

<script type="text/javascript">
    
    
</script>
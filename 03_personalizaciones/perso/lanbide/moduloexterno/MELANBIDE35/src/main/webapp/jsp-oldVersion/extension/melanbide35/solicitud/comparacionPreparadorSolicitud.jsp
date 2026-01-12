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
    EcaSolPreparadoresVO preparador = (EcaSolPreparadoresVO)request.getAttribute("preparador");
    session.removeAttribute("preparador");    
    EcaConfiguracionVO ecaConfig = (EcaConfiguracionVO)session.getAttribute("ecaConfiguracion");
    
    
    String tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.comparacionPreparador.tituloPagina");
    
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
            if(preparador != null)
            {
            %>
                nuevo = false;
                document.getElementById('nif').value = '<%=preparador.getNif() != null ? preparador.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApel').value = '<%=preparador.getNombre() != null ? preparador.getNombre().toUpperCase() : "" %>';
                document.getElementById('fechaInicio').value = '<%=preparador.getFecIni() != null ? format.format(preparador.getFecIni()) : "" %>';
                document.getElementById('fechaFin').value = '<%=preparador.getFecFin() != null ? format.format(preparador.getFecFin()) : "" %>';
                document.getElementById('horasAnualesJC').value = '<%=preparador.getHorasJC() != null ? preparador.getHorasJC().toPlainString() : "" %>';
                document.getElementById('horasContrato').value = '<%=preparador.getHorasCont() != null ? preparador.getHorasCont().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEca').value = '<%=preparador.getHorasEca() != null ? preparador.getHorasEca().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJor').value = '<%=preparador.getImpSSJC() != null ? preparador.getImpSSJC().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJor').value = '<%=preparador.getImpSSJR() != null ? preparador.getImpSSJR().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEca').value = '<%=preparador.getImpSSECA() != null ? preparador.getImpSSECA().toPlainString() : "" %>';
                document.getElementById('numSegAnt').value = '<%=preparador.getSegAnt() != null ? preparador.getSegAnt().toString() : "" %>';
                document.getElementById('importeSegAnt').value = '<%=preparador.getImpSegAnt() != null ? preparador.getImpSegAnt().toPlainString() : "" %>';
                document.getElementById('c1h').value = '<%=preparador.getInsC1H() != null ? preparador.getInsC1H().toPlainString() : "" %>';
                document.getElementById('c1m').value = '<%=preparador.getInsC1M() != null ? preparador.getInsC1M().toPlainString() : "" %>';
                document.getElementById('c1total').value = '<%=preparador.getInsC1() != null ? preparador.getInsC1().toPlainString() : "" %>';
                document.getElementById('c2h').value = '<%=preparador.getInsC2H() != null ? preparador.getInsC2H().toPlainString() : "" %>';
                document.getElementById('c2m').value = '<%=preparador.getInsC2M() != null ? preparador.getInsC2M().toPlainString() : "" %>';
                document.getElementById('c2total').value = '<%=preparador.getInsC2() != null ? preparador.getInsC2().toPlainString() : "" %>';
                document.getElementById('c3h').value = '<%=preparador.getInsC3H() != null ? preparador.getInsC3H().toPlainString() : "" %>';
                document.getElementById('c3m').value = '<%=preparador.getInsC3M() != null ? preparador.getInsC3M().toPlainString() : "" %>';
                document.getElementById('c3total').value = '<%=preparador.getInsC3() != null ? preparador.getInsC3().toPlainString() : "" %>';
                document.getElementById('c4h').value = '<%=preparador.getInsC4H() != null ? preparador.getInsC4H().toPlainString() : "" %>';
                document.getElementById('c4m').value = '<%=preparador.getInsC4M() != null ? preparador.getInsC4M().toPlainString() : "" %>';
                document.getElementById('c4total').value = '<%=preparador.getInsC4() != null ? preparador.getInsC4().toPlainString() : "" %>';
                document.getElementById('inserciones').value = '<%=preparador.getInsImporte() != null ? preparador.getInsImporte().toPlainString() : "" %>';
                document.getElementById('insercionesSeg').value = '<%=preparador.getInsSegImporte() != null ? preparador.getInsSegImporte().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSS').value = '<%=preparador.getCoste() != null ? preparador.getCoste().toPlainString() : "" %>';
                
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
                
                document.getElementById('nifCarga').value = '<%=preparador.getNif_Carga() != null ? preparador.getNif_Carga().toUpperCase() : "" %>';
                document.getElementById('nomApelCarga').value = '<%=preparador.getNombre_Carga() != null ? preparador.getNombre_Carga().toUpperCase() : "" %>';
                document.getElementById('fechaInicioCarga').value = '<%=preparador.getFecIni_Carga() != null ? format.format(preparador.getFecIni_Carga()) : "" %>';
                document.getElementById('fechaFinCarga').value = '<%=preparador.getFecFin_Carga() != null ? format.format(preparador.getFecFin_Carga()) : "" %>';
                document.getElementById('horasAnualesJCCarga').value = '<%=preparador.getHorasJC_Carga() != null ? preparador.getHorasJC_Carga().toPlainString() : "" %>';
                document.getElementById('horasContratoCarga').value = '<%=preparador.getHorasCont_Carga() != null ? preparador.getHorasCont_Carga().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEcaCarga').value = '<%=preparador.getHorasEca_Carga() != null ? preparador.getHorasEca_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJorCarga').value = '<%=preparador.getImpSSJC_Carga() != null ? preparador.getImpSSJC_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJorCarga').value = '<%=preparador.getImpSSJR_Carga() != null ? preparador.getImpSSJR_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEcaCarga').value = '<%=preparador.getImpSSECA_Carga() != null ? preparador.getImpSSECA_Carga().toPlainString() : "" %>';
                document.getElementById('numSegAntCarga').value = '<%=preparador.getSegAnt_Carga() != null ? preparador.getSegAnt_Carga().toString() : "" %>';
                document.getElementById('importeSegAntCarga').value = '<%=preparador.getImpSegAnt_Carga() != null ? preparador.getImpSegAnt_Carga().toPlainString() : "" %>';
                document.getElementById('c1hCarga').value = '<%=preparador.getInsC1H_Carga() != null ? preparador.getInsC1H_Carga().toPlainString() : "" %>';
                document.getElementById('c1mCarga').value = '<%=preparador.getInsC1M_Carga() != null ? preparador.getInsC1M_Carga().toPlainString() : "" %>';
                document.getElementById('c1totalCarga').value = '<%=preparador.getInsC1_Carga() != null ? preparador.getInsC1_Carga().toPlainString() : "" %>';
                document.getElementById('c2hCarga').value = '<%=preparador.getInsC2H_Carga() != null ? preparador.getInsC2H_Carga().toPlainString() : "" %>';
                document.getElementById('c2mCarga').value = '<%=preparador.getInsC2M_Carga() != null ? preparador.getInsC2M_Carga().toPlainString() : "" %>';
                document.getElementById('c2totalCarga').value = '<%=preparador.getInsC2_Carga() != null ? preparador.getInsC2_Carga().toPlainString() : "" %>';
                document.getElementById('c3hCarga').value = '<%=preparador.getInsC3H_Carga() != null ? preparador.getInsC3H_Carga().toPlainString() : "" %>';
                document.getElementById('c3mCarga').value = '<%=preparador.getInsC3M_Carga() != null ? preparador.getInsC3M_Carga().toPlainString() : "" %>';
                document.getElementById('c3totalCarga').value = '<%=preparador.getInsC3_Carga() != null ? preparador.getInsC3_Carga().toPlainString() : "" %>';
                document.getElementById('c4hCarga').value = '<%=preparador.getInsC4H_Carga() != null ? preparador.getInsC4H_Carga().toPlainString() : "" %>';
                document.getElementById('c4mCarga').value = '<%=preparador.getInsC4M_Carga() != null ? preparador.getInsC4M_Carga().toPlainString() : "" %>';
                document.getElementById('c4totalCarga').value = '<%=preparador.getInsC4_Carga() != null ? preparador.getInsC4_Carga().toPlainString() : "" %>';
                document.getElementById('insercionesCarga').value = '<%=preparador.getInsImporte_Carga()!= null ? preparador.getInsImporte_Carga().toPlainString() : "" %>';
                document.getElementById('insercionesSegCarga').value = '<%=preparador.getInsSegImporte_Carga() != null ? preparador.getInsSegImporte_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSCarga').value = '<%=preparador.getCoste_Carga() != null ? preparador.getCoste_Carga().toPlainString() : "" %>';
                reemplazarPuntosEca(document.getElementById('c1hCarga'));
                reemplazarPuntosEca(document.getElementById('c1mCarga'));
                reemplazarPuntosEca(document.getElementById('c1totalCarga'));
                reemplazarPuntosEca(document.getElementById('c2h'));
                reemplazarPuntosEca(document.getElementById('c2m'));
                reemplazarPuntosEca(document.getElementById('c2total'));
                reemplazarPuntosEca(document.getElementById('c3hCarga'));
                reemplazarPuntosEca(document.getElementById('c3mCarga'));
                reemplazarPuntosEca(document.getElementById('c3totalCarga'));
                reemplazarPuntosEca(document.getElementById('c4hCarga'));
                reemplazarPuntosEca(document.getElementById('c4mCarga'));
                reemplazarPuntosEca(document.getElementById('c4totalCarga'));
            <%
            }
            %>                   
                                    
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasAnualesJCCarga'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasContratoCarga'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEcaCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJorCarga'));            
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJorCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEcaCarga'));
            reemplazarPuntosEca(document.getElementById('importeSegAnt'));
            reemplazarPuntosEca(document.getElementById('importeSegAntCarga'));
            reemplazarPuntosEca(document.getElementById('inserciones'));
            reemplazarPuntosEca(document.getElementById('insercionesSeg'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSS'));
            reemplazarPuntosEca(document.getElementById('insercionesCarga'));
            reemplazarPuntosEca(document.getElementById('insercionesSegCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSCarga'));
            
             
            
            
            //Deshabilito todos los campos
                /*document.getElementById('nif').disabled = true;
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
                document.getElementById('costesSalarialesSS').disabled = true;*/
             
            ajustarDecimalesImportes();
        }catch(err){
            
        }
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
                    hajc = parseFloat(document.getElementById('horasAnualesJC').value);
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
                    hc = parseFloat(document.getElementById('horasContrato').value);
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
                    hdECA = parseFloat(document.getElementById('horasDedicacionEca').value);
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
                    hc = parseFloat(document.getElementById('horasContrato').value);
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
                    hdECA = parseFloat(document.getElementById('horasDedicacionEca').value);
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            hc *= 0.5;
            if(hdECA < hc){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEca50HorasContrato")%>';
            }else{
                document.getElementById('horasDedicacionEca').removeAttribute('style');
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
                    cssJC = parseFloat(document.getElementById('costesSalarialesSSJor').value);
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
                    cssPorJor = parseFloat(document.getElementById('costesSalarialesSSPorJor').value);
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
                    cssECA = parseFloat(document.getElementById('costesSalarialesSSEca').value);
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
                if(!validarNumericoEca(document.getElementById('numSegAnt'), 8, 2)){
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

        if(document.getElementById('importeSegAnt').value != ''){
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
                txtImporte = reemplazarTextoEca(txtImporte, /,/g, '.');
                var importe = parseFloat(txtImporte);
                var poMax = <%=ecaConfig != null && ecaConfig.getPoMaxSeguimientos() != null ? ecaConfig.getPoMaxSeguimientos().doubleValue() : 1.0%>;
                var por = cssECA * poMax;
                if(importe < por){
                    //Hay que comprobar que corresponda a numSeg * config.getImSeguimiento()
                    if(document.getElementById('numSegAnt').value != ''){
                        var segAnt = parseInt(document.getElementById('numSegAnt').value);
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
        }

        if(document.getElementById('c1h').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('c1h'), 8, 2)){
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
                if(!validarNumericoDecimalEca(document.getElementById('c1m'), 8, 2)){
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
                if(!validarNumericoEca(document.getElementById('c1total'))){
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
                if(!validarNumericoDecimalEca(document.getElementById('c2h'), 8, 2)){
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
                if(!validarNumericoDecimalEca(document.getElementById('c2m'), 8, 2)){
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
                if(!validarNumericoEca(document.getElementById('c2total'))){
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
                if(!validarNumericoDecimalEca(document.getElementById('c3h'), 8, 2)){
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
                if(!validarNumericoDecimalEca(document.getElementById('c3m'), 8, 2)){
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
                if(!validarNumericoEca(document.getElementById('c3total'))){
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
                if(!validarNumericoDecimalEca(document.getElementById('c4h'), 8, 2)){
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
                if(!validarNumericoDecimalEca(document.getElementById('c4m'), 8, 2)){
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
                if(!validarNumericoEca(document.getElementById('c4total'))){
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
                var c1h = document.getElementById('c1h').value != '' ? parseFloat(document.getElementById('c1h').value) : 0.0;
                var c1m = document.getElementById('c1m').value != '' ? parseFloat(document.getElementById('c1m').value) : 0.0;
                var c1tot = document.getElementById('c1total').value != '' ? parseFloat(document.getElementById('c1total').value) : 0.0;
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
                var c2h = document.getElementById('c2h').value != '' ? parseFloat(document.getElementById('c2h').value) : 0.0;
                var c2m = document.getElementById('c2m').value != '' ? parseFloat(document.getElementById('c2m').value) : 0.0;
                var c2tot = document.getElementById('c2total').value != '' ? parseFloat(document.getElementById('c2total').value) : 0.0;
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
                var c3h = document.getElementById('c3h').value != '' ? parseFloat(document.getElementById('c3h').value) : 0.0;
                var c3m = document.getElementById('c3m').value != '' ? parseFloat(document.getElementById('c3m').value) : 0.0;
                var c3tot = document.getElementById('c3total').value != '' ? parseFloat(document.getElementById('c3total').value) : 0.0;
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
                var c4h = document.getElementById('c4h').value != '' ? parseFloat(document.getElementById('c4h').value) : 0.0;
                var c4m = document.getElementById('c4m').value != '' ? parseFloat(document.getElementById('c4m').value) : 0.0;
                var c4tot = document.getElementById('c4total').value != '' ? parseFloat(document.getElementById('c4total').value) : 0.0;
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
                var importe = parseFloat(document.getElementById('inserciones').value);
                var importeCalculado = 0.0;

                if(document.getElementById('c1h').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c1h').value) * <%=ecaConfig != null && ecaConfig.getImC1h() != null ? ecaConfig.getImC1h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c1m').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c1m').value) * <%=ecaConfig != null && ecaConfig.getImC1m() != null ? ecaConfig.getImC1m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c2h').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c2h').value) * <%=ecaConfig != null && ecaConfig.getImC2h() != null ? ecaConfig.getImC2h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c2m').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c2m').value) * <%=ecaConfig != null && ecaConfig.getImC2m() != null ? ecaConfig.getImC2m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c3h').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c3h').value) * <%=ecaConfig != null && ecaConfig.getImC3h() != null ? ecaConfig.getImC3h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c3m').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c3m').value) * <%=ecaConfig != null && ecaConfig.getImC3m() != null ? ecaConfig.getImC3m().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c4h').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c4h').value) * <%=ecaConfig != null && ecaConfig.getImC4h() != null ? ecaConfig.getImC4h().doubleValue() : 1.0%>;
                }

                if(document.getElementById('c4m').value != ''){
                    importeCalculado += parseFloat(document.getElementById('c4m').value) * <%=ecaConfig != null && ecaConfig.getImC4m() != null ? ecaConfig.getImC4m().doubleValue() : 1.0%>;
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
            //Seguimientos + Inserciones: controlar que es la suma Importe + Inserciones
            if(document.getElementById('insercionesSeg').value != ''){
                if(document.getElementById('importeSegAnt').value != '' || document.getElementById('inserciones').value != ''){
                    var suma = 0.0;
                    var impSegAntTxt = document.getElementById('importeSegAnt').value;
                    impSegAntTxt = reemplazarTextoEca(impSegAntTxt, /,/g, '.');
                    var insercionesTxt = document.getElementById('inserciones').value;
                    insercionesTxt = reemplazarTextoEca(insercionesTxt, /,/g, '.');
                    suma += impSegAntTxt != '' ? parseFloat(impSegAntTxt) : 0.0;
                    suma += insercionesTxt != '' ? parseFloat(insercionesTxt) : 0.0;
                    var insercionesSegTxt = document.getElementById('insercionesSeg').value;
                    insercionesSegTxt = reemplazarTextoEca(insercionesSegTxt, /,/g, '.');
                    var insSegImporte = parseFloat(insercionesSegTxt);
                    if(insSegImporte != suma){
                        correcto = false;
                        document.getElementById('insercionesSeg').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.segInsNoSumaImpIns")%>';
                    }else{
                        document.getElementById('insercionesSeg').removeAttribute('style');
                    }
                }
            }
        }
        
        return correcto;
    }
    
    
    
    function deshabilitarImporteSeguimientos(){
        var campo = document.getElementById('costesSalarialesSSEca');
        if(campo.value != ''){
            reemplazarPuntosEca(campo);
            document.getElementById('importeSegAnt').disabled = false;
        }else{
            document.getElementById('importeSegAnt').value = '';
            document.getElementById('importeSegAnt').disabled = true;
        }
    }
                
    function ajustarDecimalesImportes(){
        var campo;

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

        //C1H
        campo = document.getElementById('c1h');
        ajustarDecimalesCampo(campo, 2);

        //C1M
        campo = document.getElementById('c1m');
        ajustarDecimalesCampo(campo, 2);

        //C1 TOTAL
        campo = document.getElementById('c1total');
        ajustarDecimalesCampo(campo, 2);

        //C2H
        campo = document.getElementById('c2h');
        ajustarDecimalesCampo(campo, 2);

        //C2M
        campo = document.getElementById('c2m');
        ajustarDecimalesCampo(campo, 2);

        //C2 TOTAL
        campo = document.getElementById('c2total');
        ajustarDecimalesCampo(campo, 2);

        //C3H
        campo = document.getElementById('c3h');
        ajustarDecimalesCampo(campo,2);

        //C3M
        campo = document.getElementById('c3m');
        ajustarDecimalesCampo(campo, 2);

        //C3 TOTAL
        campo = document.getElementById('c3total');
        ajustarDecimalesCampo(campo, 2);

        //C4H
        campo = document.getElementById('c4h');
        ajustarDecimalesCampo(campo, 2);

        //C4M
        campo = document.getElementById('c4m');
        ajustarDecimalesCampo(campo, 2);

        //C4 TOTAL
        campo = document.getElementById('c4total');
        ajustarDecimalesCampo(campo, 2);

        //Seguimientos + Inserciones
        campo = document.getElementById('insercionesSeg');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('costesSalarialesSS');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('numSegAnt');
        ajustarDecimalesCampo(campo, 0);
        
        //Inserciones
        campo = document.getElementById('inserciones');
        ajustarDecimalesCampo(campo, 2);

        //Horas anueales jornada completa
        campo = document.getElementById('horasAnualesJCCarga');
        ajustarDecimalesCampo(campo, 2);

        //Horas contrato
        campo = document.getElementById('horasContratoCarga');
        ajustarDecimalesCampo(campo, 2);

        //Horas dedicacion ECA
        campo = document.getElementById('horasDedicacionEcaCarga');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS Jor
        campo = document.getElementById('costesSalarialesSSJorCarga');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS % Jor
        campo = document.getElementById('costesSalarialesSSPorJorCarga');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS ECA
        campo = document.getElementById('costesSalarialesSSEcaCarga');
        ajustarDecimalesCampo(campo, 2);

        //Importe seg ant
        campo = document.getElementById('importeSegAntCarga');
        ajustarDecimalesCampo(campo, 2);

        //C1H
        campo = document.getElementById('c1hCarga');
        ajustarDecimalesCampo(campo, 2);

        //C1M
        campo = document.getElementById('c1mCarga');
        ajustarDecimalesCampo(campo, 2);

        //C1 TOTAL
        campo = document.getElementById('c1totalCarga');
        ajustarDecimalesCampo(campo, 2);

        //C2H
        campo = document.getElementById('c2hCarga');
        ajustarDecimalesCampo(campo, 2);

        //C2M
        campo = document.getElementById('c2mCarga');
        ajustarDecimalesCampo(campo, 2);

        //C2 TOTAL
        campo = document.getElementById('c2totalCarga');
        ajustarDecimalesCampo(campo, 2);

        //C3H
        campo = document.getElementById('c3hCarga');
        ajustarDecimalesCampo(campo, 2);

        //C3M
        campo = document.getElementById('c3mCarga');
        ajustarDecimalesCampo(campo, 2);

        //C3 TOTAL
        campo = document.getElementById('c3totalCarga');
        ajustarDecimalesCampo(campo, 2);

        //C4H
        campo = document.getElementById('c4hCarga');
        ajustarDecimalesCampo(campo, 2);

        //C4M
        campo = document.getElementById('c4mCarga');
        ajustarDecimalesCampo(campo, 2);

        //C4 TOTAL
        campo = document.getElementById('c4totalCarga');
        ajustarDecimalesCampo(campo, 2);

        //Seguimientos + Inserciones
        campo = document.getElementById('insercionesSegCarga');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('costesSalarialesSSCarga');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('numSegAntCarga');
        ajustarDecimalesCampo(campo, 0);
        
        //Inserciones
        campo = document.getElementById('insercionesCarga');
        ajustarDecimalesCampo(campo, 2);
    }
    
    function calcularCostesSalarialesECA(){
        var c1 = document.getElementById('horasDedicacionEca');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 8, 2)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 8, 2)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 8, 2)){
                    try{
                        valor = c1.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result /= parseFloat(valor);
                        
                        result = result.toFixed(2);

                        document.getElementById('costesSalarialesSSEca').value = result;
                        reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
                        
                        valorCalculado = true;
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSEca').value = '';
        }
        
        calcularImporteSeguimientos(document.getElementById('costesSalarialesSSEca'));
    }
    
    function calcularCostesSalarialesPorJor(){
        var c1 = document.getElementById('horasContrato');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 8, 2)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 8, 2)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 8, 2)){
                    try{
                        valor = c1.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = reemplazarTextoEca(valor, /,/g, '.');
                        result /= parseFloat(valor);
                        
                        result = result.toFixed(2);

                        document.getElementById('costesSalarialesSSPorJor').value = result;
                        reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
                        
                        valorCalculado = true;
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSPorJor').value = '';
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
            <div class="sub3titulo" style="clear: both; text-align: left;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.datosPreparador")%>
                </span>
            </div>
            <div style="float:right;padding:0.5em 3em;font-size:90%">                 
                <span class="textohistorico">* Datos carga inicial</span>&nbsp;
                <span class="textoactual">* Datos actuales</span>
            </div>
            <div class="lineaFormulario">
                <div style="width: 36px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nif")%>
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nifCarga" name="nifCarga" size="10" maxlength="10" class="inputTexto textohistorico readonly" readonly />
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto readonly" />
                </div>                
            </div>
            <div class="lineaFormulario">     
                <div style="width: 140px; float: left;">
                   <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nomApel")%>
                </div>
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApelCarga" name="nomApelCarga" size="83" maxlength="200" class="inputTexto textohistorico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">    
                <div style="width: 140px; float: left;">
                    &nbsp;
                </div>
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto readonly"/>
                </div>
            </div>            
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  textohistorico readonly" size="10" maxlength="10" id="fechaInicioCarga" name="fechaInicioCarga" readonly/>                   
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha readonly" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" />                    
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  textohistorico readonly" size="10" maxlength="10" id="fechaFinCarga" name="fechaFinCarga" readonly/>                    
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha readonly" size="10" maxlength="10" id="fechaFin" name="fechaFin" />                    
                </div>
            </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasAnualesJC")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasAnualesJCCarga" name="horasAnualesJCCarga" size="11" maxlength="9" class="inputTexto textoNumerico  textohistorico readonly"  readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
             </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasContrato")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContratoCarga" name="horasContratoCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly"  readonly/>
                </div>     
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>          
             </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasDedicacionEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEcaCarga" name="horasDedicacionEcaCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>   
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico readonly"  readonly/>
                </div>             
             </div>
            <div class="lineaFormulario">   
                 <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSJor")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSJorCarga" name="costesSalarialesSSJorCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly"  readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readonly"  readonly/>
                </div>
            </div>
            <div class="lineaFormulario">                
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSPorJor")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSPorJorCarga" name="costesSalarialesSSPorJorCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readonly " readonly/>
                </div>
             </div>
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSEcaCarga" name="costesSalarialesSSEcaCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSEca" name="costesSalarialesSSEca" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 290px; float: left;">
                    <%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.numSegAnt"), anoExpediente)%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="numSegAntCarga" name="numSegAntCarga" size="11" maxlength="8" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>   
                <div style="width: 100px; float: left;">
                    <input type="text" id="numSegAnt" name="numSegAnt" size="11" maxlength="8" class="inputTexto textoNumerico readonly" />
                </div>         
                <div style="width: 118px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.importeSegAnt")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="importeSegAntCarga" name="importeSegAntCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="importeSegAnt" name="importeSegAnt" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1h")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1hCarga" name="c1hCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>    
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1h" name="c1h" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>        
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1m")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1mCarga" name="c1mCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1m" name="c1m" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularTotalC1();" onblur="calcularTotalC1(); ajustarDecimalesImportes();"/>
                </div>
                <div style="width: 60px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c1total")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1totalCarga" name="c1totalCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c1total" name="c1total" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2h")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2hCarga" name="c2hCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly"  readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2h" name="c2h" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2m")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2mCarga" name="c2mCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly"  readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2m" name="c2m" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularTotalC2();" />
                </div>
                <div style="width: 60px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c2total")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2totalCarga" name="c2totalCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c2total" name="c2total" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3h")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c3hCarga" name="c3hCarga" size="11" maxlength="9" class="inputTexto textoNumerico  textohistorico readonly"  readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c3h" name="c3h" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3m")%>
                </div>
                 <div style="width: 100px; float: left;">
                    <input type="text" id="c3mCarga" name="c3mCarga" size="11" maxlength="9" class="inputTexto textoNumerico  textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c3m" name="c3m" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
                <div style="width: 60px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c3total")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c3totalCarga" name="c3totalCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c3total" name="c3total" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4h")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4hCarga" name="c4hCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4h" name="c4h" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
                <div style="width: 45px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4m")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4mCarga" name="c4mCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4m" name="c4m" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
                <div style="width: 60px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.c4total")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4totalCarga" name="c4totalCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" id="c4total" name="c4total" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 607px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.inserciones")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="insercionesCarga" name="insercionesCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="inserciones" name="inserciones" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
             </div>
            <div class="lineaFormulario">
                <div style="width: 607px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.insercionesSeg")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="insercionesSegCarga" name="insercionesSegCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="insercionesSeg" name="insercionesSeg" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 607px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSS")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSCarga" name="costesSalarialesSSCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSS" name="costesSalarialesSS" size="11" maxlength="9" class="inputTexto textoNumerico readonly" />
                </div>
            </div>

            <div class="botonera" style="padding-top: 20px;">                                
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
            </div>
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
</body>

<script type="text/javascript">
    
    
</script>
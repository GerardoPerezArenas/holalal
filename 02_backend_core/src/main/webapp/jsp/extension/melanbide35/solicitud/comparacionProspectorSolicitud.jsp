<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    int idiomaUsuario = 1;
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
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    EcaSolProspectoresVO prospector = (EcaSolProspectoresVO)request.getAttribute("prospector");
    session.removeAttribute("prospector");    
    EcaConfiguracionVO ecaConfig = (EcaConfiguracionVO)session.getAttribute("ecaConfiguracion");
    
    
    String tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.comparacionProspector.tituloPagina");
    
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
    
%>
<title><%=tituloPagina%></title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
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
            if(prospector != null)
            {
            %>
                nuevo = false;
                document.getElementById('nif').value = '<%=prospector.getNif() != null ? prospector.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApel').value = '<%=prospector.getNombre() != null ? prospector.getNombre().toUpperCase() : "" %>';
                document.getElementById('fechaInicio').value = '<%=prospector.getFecIni() != null ? format.format(prospector.getFecIni()) : "" %>';
                document.getElementById('fechaFin').value = '<%=prospector.getFecFin() != null ? format.format(prospector.getFecFin()) : "" %>';
                document.getElementById('horasAnualesJC').value = '<%=prospector.getHorasJC() != null ? prospector.getHorasJC().toPlainString() : "" %>';
                document.getElementById('horasContrato').value = '<%=prospector.getHorasCont() != null ? prospector.getHorasCont().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEca').value = '<%=prospector.getHorasEca() != null ? prospector.getHorasEca().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJor').value = '<%=prospector.getImpSSJC() != null ? prospector.getImpSSJC().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJor').value = '<%=prospector.getImpSSJR() != null ? prospector.getImpSSJR().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEca').value = '<%=prospector.getImpSSECA() != null ? prospector.getImpSSECA().toPlainString() : "" %>';
                document.getElementById('visitas').value = '<%=prospector.getVisitas() != null ? prospector.getVisitas().toString() : "" %>';
                document.getElementById('visitasImp').value = '<%=prospector.getVisitasImp() != null ? prospector.getVisitasImp().toPlainString() : "" %>';
                document.getElementById('coste').value = '<%=prospector.getCoste() != null ? prospector.getCoste().toPlainString() : "" %>';
                
                document.getElementById('nifCarga').value = '<%=prospector.getNif_Carga() != null ? prospector.getNif_Carga().toUpperCase() : "" %>';
                document.getElementById('nomApelCarga').value = '<%=prospector.getNombre_Carga() != null ? prospector.getNombre_Carga().toUpperCase() : "" %>';
                document.getElementById('fechaInicioCarga').value = '<%=prospector.getFecIni_Carga() != null ? format.format(prospector.getFecIni_Carga()) : "" %>';
                document.getElementById('fechaFinCarga').value = '<%=prospector.getFecFin_Carga() != null ? format.format(prospector.getFecFin_Carga()) : "" %>';
                document.getElementById('horasAnualesJCCarga').value = '<%=prospector.getHorasJC_Carga() != null ? prospector.getHorasJC_Carga().toPlainString() : "" %>';
                document.getElementById('horasContratoCarga').value = '<%=prospector.getHorasCont_Carga() != null ? prospector.getHorasCont_Carga().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEcaCarga').value = '<%=prospector.getHorasEca_Carga() != null ? prospector.getHorasEca_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJorCarga').value = '<%=prospector.getImpSSJC_Carga() != null ? prospector.getImpSSJC_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJorCarga').value = '<%=prospector.getImpSSJR_Carga() != null ? prospector.getImpSSJR_Carga().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEcaCarga').value = '<%=prospector.getImpSSECA_Carga() != null ? prospector.getImpSSECA_Carga().toPlainString() : "" %>';
                document.getElementById('visitasCarga').value = '<%=prospector.getVisitas_Carga() != null ? prospector.getVisitas_Carga().toString() : "" %>';
                document.getElementById('visitasImpCarga').value = '<%=prospector.getVisitasImp_Carga() != null ? prospector.getVisitasImp_Carga().toPlainString() : "" %>';
                document.getElementById('costeCarga').value = '<%=prospector.getCoste_Carga() != null ? prospector.getCoste_Carga().toPlainString() : "" %>';
                
            <%
            }
            %>
                   
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));                       
            reemplazarPuntosEca(document.getElementById('visitasImp'));
            reemplazarPuntosEca(document.getElementById('coste'));
            
            reemplazarPuntosEca(document.getElementById('horasAnualesJCCarga'));
            reemplazarPuntosEca(document.getElementById('horasContratoCarga'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEcaCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJorCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJorCarga'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEcaCarga'));                       
            reemplazarPuntosEca(document.getElementById('visitasImpCarga'));
            reemplazarPuntosEca(document.getElementById('costeCarga'));
            
            //FormatNumber(document.getElementById('visitas').value, 6,0,document.getElementById('visitas').id); 
            //FormatNumber(document.getElementById('visitasCarga').value, 6,0,document.getElementById('visitasCarga').id); 
            
            document.getElementById('btnCerrar').style.display = 'inline';
             
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
    
    function calcularImporteSeguimientos(campo){
        if(document.getElementById('numSegAnt').value != ''){
            reemplazarPuntosEca(campo);
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
                    var numSeg = parseInt(document.getElementById('numSegAnt').value);
                    var imSeg = <%=ecaConfig != null && ecaConfig.getImSeguimiento() != null ? ecaConfig.getImSeguimiento().doubleValue() : 0.0%>;
                    var poMax = <%=ecaConfig != null && ecaConfig.getPoMaxSeguimientos() != null ? ecaConfig.getPoMaxSeguimientos().doubleValue() : 1.0%>;
                    var importe = 0.0;
                    importe = numSeg * imSeg;
                    var cssECA = parseFloat(document.getElementById('costesSalarialesSSEca').value);
                    var por = cssECA * poMax;
                    if(importe > por){
                        document.getElementById('importeSegAnt').value = por;
                        reemplazarPuntosEca(document.getElementById('importeSegAnt'));
                    }else{
                        document.getElementById('importeSegAnt').value = importe;
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
        valor1h = reemplazarTextoEca(valor1h, /,/g, '.');
        var valor1m = document.getElementById('c1m').value;
        valor1m = reemplazarTextoEca(valor1m, /,/g, '.');
        reemplazarPuntosEca(document.getElementById('c1h'));
        reemplazarPuntosEca(document.getElementById('c1m'));
        try{
            c1h = parseInt(valor1h);
            if(valor1h == '' || isNaN(valor1h)){
                c1h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c1h = 0.0;
        }
        
        try{
            c1m = parseInt(valor1m);
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
        reemplazarPuntosEca(document.getElementById('c1total'));
        calcularInserciones();
    }
    
    function calcularTotalC2(){
        var c2h;
        var c2m;
        var tot;
        var hayValor = false;
        var valor2h = document.getElementById('c2h').value;
        valor2h = reemplazarTextoEca(valor2h, /,/g, '.');
        var valor2m = document.getElementById('c2m').value;
        valor2m = reemplazarTextoEca(valor2m, /,/g, '.');
        reemplazarPuntosEca(document.getElementById('c2h'));
        reemplazarPuntosEca(document.getElementById('c2m'));
        try{
            c2h = parseInt(valor2h);
            if(valor2h == '' || isNaN(valor2h)){
                c2h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c2h = 0.0;
        }
        
        try{
            c2m = parseInt(valor2m);
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
        calcularInserciones();
    }
    
    function calcularTotalC3(){
        var c3h;
        var c3m;
        var tot;
        var hayValor = false;
        var valor3h = document.getElementById('c3h').value;
        valor3h = reemplazarTextoEca(valor3h, /,/g, '.');
        var valor3m = document.getElementById('c3m').value;
        valor3m = reemplazarTextoEca(valor3m, /,/g, '.');
        reemplazarPuntosEca(document.getElementById('c3h'));
        reemplazarPuntosEca(document.getElementById('c3m'));
        try{
            c3h = parseInt(valor3h);
            if(valor3h == '' || isNaN(valor3h)){
                c3h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c3h = 0.0;
        }
        
        try{
            c3m = parseInt(valor3m);
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
        calcularInserciones();
    }
    
    function calcularTotalC4(){
        var c4h;
        var c4m;
        var tot;
        var hayValor = false;
        var valor4h = document.getElementById('c4h').value;
        valor4h = reemplazarTextoEca(valor4h, /,/g, '.');
        var valor4m = document.getElementById('c4m').value;
        valor4m = reemplazarTextoEca(valor4m, /,/g, '.');
        reemplazarPuntosEca(document.getElementById('c4h'));
        reemplazarPuntosEca(document.getElementById('c4m'));
        try{
            c4h = parseInt(valor4h);
            if(valor4h == '' || isNaN(valor4h)){
                c4h = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            c4h = 0.0;
        }
        
        try{
            c4m = parseInt(valor4m);
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
        calcularInserciones();
    }
    
    function calcularInserciones(){
        var importeCalculado = 0.0;
        var hayValor = false;
        var valor;

        if(document.getElementById('c1h').value != '' && validarNumericoDecimalEca(document.getElementById('c1h'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c1h').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC1h() != null ? ecaConfig.getImC1h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c1m').value != '' && validarNumericoDecimalEca(document.getElementById('c1m'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c1m').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC1m() != null ? ecaConfig.getImC1m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c2h').value != '' && validarNumericoDecimalEca(document.getElementById('c2h'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c2h').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC2h() != null ? ecaConfig.getImC2h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c2m').value != '' && validarNumericoDecimalEca(document.getElementById('c2m'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c2m').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC2m() != null ? ecaConfig.getImC2m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c3h').value != '' && validarNumericoDecimalEca(document.getElementById('c3h'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c3h').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC3h() != null ? ecaConfig.getImC3h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c3m').value != '' && validarNumericoDecimalEca(document.getElementById('c3m'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c3m').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC3m() != null ? ecaConfig.getImC3m().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c4h').value != '' && validarNumericoDecimalEca(document.getElementById('c4h'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c4h').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC4h() != null ? ecaConfig.getImC4h().doubleValue() : 1.0%>;
        }

        if(document.getElementById('c4m').value != '' && validarNumericoDecimalEca(document.getElementById('c4m'), 8, 2)){
            hayValor = true;
            valor = document.getElementById('c4m').value;
            valor = reemplazarTextoEca(valor, /,/g, '.');
            importeCalculado += parseFloat(valor) * <%=ecaConfig != null && ecaConfig.getImC4m() != null ? ecaConfig.getImC4m().doubleValue() : 1.0%>;
        }
        if(hayValor){
            document.getElementById('inserciones').value = parseInt(importeCalculado);
            reemplazarPuntosEca(document.getElementById('inserciones'));
        }else{
            document.getElementById('inserciones').value = '';
        }
        
        calcularInsercionesSeg();
    }
    
    function calcularInsercionesSeg(){
        if(document.getElementById('importeSegAnt').value != '' || document.getElementById('inserciones').value != ''){
            var suma = 0.0;
            var impSegAntTxt = document.getElementById('importeSegAnt').value;
            impSegAntTxt = reemplazarTextoEca(impSegAntTxt, /,/g, '.');
            var insercionesTxt = document.getElementById('inserciones').value;
            insercionesTxt = reemplazarTextoEca(insercionesTxt, /,/g, '.');
            suma += impSegAntTxt != '' ? parseFloat(impSegAntTxt) : 0.0;
            suma += insercionesTxt != '' ? parseFloat(insercionesTxt) : 0.0;
            document.getElementById('insercionesSeg').value = suma;
            reemplazarPuntosEca(document.getElementById('insercionesSeg'));
        }else{
            document.getElementById('insercionesSeg').value = '';
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

        //Visitas
        campo = document.getElementById('visitas');
        ajustarDecimalesCampo(campo, 2);

        //Seguimientos + Inserciones
        campo = document.getElementById('visitasImp');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('coste');
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

        //Visitas
        campo = document.getElementById('visitasCarga');
        ajustarDecimalesCampo(campo, 2);

        //Seguimientos + Inserciones
        campo = document.getElementById('visitasImpCarga');
        ajustarDecimalesCampo(campo, 2);
        
        //Costes salariales SS
        campo = document.getElementById('costeCarga');
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
        
        //calcularImporteSeguimientos(document.getElementById('costesSalarialesSSEca'));
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
            <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.datosProspector")%>
                    </span>
            </div>           
            <div style="float:right;padding:0.5em 3em;font-size:90%">
                <span class="textohistorico">* Datos carga inicial</span> &nbsp;
                <span class="textoactual">* Datos actuales</span>
            </div>
            <div class="lineaFormulario">
                <div style="width: 36px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.nif")%>
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nifCarga" name="nifCarga" size="10" maxlength="10" class="inputTexto textohistorico readonly" readonly />
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto  readonly" />
                </div>
            </div>
            <div class="lineaFormulario">     
                <div style="width: 140px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.nomApel")%>
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
                    <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto  readonly"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaInicio")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  textohistorico readonly" size="10" maxlength="10" id="fechaInicioCarga" name="fechaInicioCarga" readonly/>                   
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  readonly" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" />                    
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaFin")%>
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  textohistorico readonly" size="10" maxlength="10" id="fechaFinCarga" name="fechaFinCarga" readonly/>                    
                </div>
                <div style="width: 100px; float: left;">
                    <input type="text" class="inputTxtFecha  readonly" size="10" maxlength="10" id="fechaFin" name="fechaFin" />                    
                </div>
            </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasAnualesJC")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasAnualesJCCarga" name="horasAnualesJCCarga" size="11" maxlength="9" class="inputTexto textoNumerico  textohistorico readonly" onkeyup="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
                </div>
             </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasContrato")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContratoCarga" name="horasContratoCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" onkeyup="calcularCostesSalarialesPorJor();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();" readonly/>
                </div>  
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularCostesSalarialesPorJor();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();" readonly/>
                </div>             
             </div>                
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasDedicacionEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEcaCarga" name="horasDedicacionEcaCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" onkeyup="calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
                </div>     
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
                </div>           
             </div>
            <div class="lineaFormulario">   
                 <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.costesSalarialesSSJor")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSJorCarga" name="costesSalarialesSSJorCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" onkeyup="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readonly" onkeyup="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();reemplazarPuntosEca(this);" onblur="calcularCostesSalarialesPorJor();calcularCostesSalarialesECA();ajustarDecimalesImportes();" readonly/>
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
                    <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
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
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.visitas")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="visitasCarga" name="visitasCarga" size="11" maxlength="8" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>   
                <div style="width: 108px; float: left;">
                    <input type="text" id="visitas" name="visitas" size="11" maxlength="8" class="inputTexto textoNumerico readonly" readonly/>
                </div>         
             </div>
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.visitasImp")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="visitasImpCarga" name="visitasImpCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="visitasImp" name="visitasImp" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
                </div>
            </div>    
            <div class="lineaFormulario">
                <div style="width: 350px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.coste")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costeCarga" name="costeCarga" size="11" maxlength="9" class="inputTexto textoNumerico textohistorico readonly" readonly/>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="coste" name="coste" size="11" maxlength="9" class="inputTexto textoNumerico readonly" readonly/>
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

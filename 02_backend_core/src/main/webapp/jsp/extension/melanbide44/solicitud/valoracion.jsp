<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolValoracionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>

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
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    EcaSolValoracionVO valoracion = (EcaSolValoracionVO)request.getAttribute("valoracionSolicitud");
    
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    GeneralValueObject expedienteVO = null;
    String modoConsulta = "no";
    if(expForm != null)
    {
        expedienteVO = expForm.getExpedienteVO();
        if(expedienteVO != null)
        {
            modoConsulta = (String) expedienteVO.getAtributo("modoConsulta");
            if(modoConsulta != null)
            {
                modoConsulta = modoConsulta.toLowerCase();
            }
            else
            {
                modoConsulta = "no";
            }
        }
    }
%>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

<script type="text/javascript">        
    var mensajeValidacion = '';
    
    function guardarValoracionSolicitudEca(){
        if(validarValoracionSolicitudEca()){
            barraProgresoEca('on', 'barraProgresoValoracionSolicitudEca');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=guardarDatosValoracionSolicitud&tipo=0&numero=<%=numExpediente%>'
                        +'&numProyectosExp='+document.getElementById('numProyectosExp').value
                        +'&puntuacionExp='+convertirANumero(document.getElementById('puntuacionExp').value)
                        +'&porInsMujeres='+convertirANumero(document.getElementById('porInsMujeres').value)
                        +'&puntuacionInsMujeres='+convertirANumero(document.getElementById('puntuacionInsMujeres').value)
                        +'&numProyectosSensEmpresarial='+document.getElementById('numProyectosSensEmpresarial').value
                        +'&puntuacionSensEmpresarial='+convertirANumero(document.getElementById('puntuacionSensEmpresarial').value)
                        +'&totValoracionSol='+convertirANumero(document.getElementById('totValoracionSol').value)
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
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="ID"){
                        document.getElementById('idValSolEca').value = hijos[j].childNodes[0].nodeValue;
                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                barraProgresoEca('off', 'barraProgresoValoracionSolicitudEca');
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    mostrarPestanasSolicitudEca();
                    actualizarOtrasPestanasEca(4);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
        }else{
            jsp_alerta("A", mensajeValidacion);
        }
    }
    
    function validarValoracionSolicitudEca(){
        mensajeValidacion = '';
        var correcto = true;
        if(!validarNumProyectosExp()){
            correcto = false;
        }
        if(!validarPorcentajeInsMujeres()){
            correcto = false;
        }
        if(!validarNumProyectosSensEmpresarial()){
            correcto = false;
        }
        return correcto;
    }
    
    function validarNumProyectosExp(){
        var correcto = true;
        if(document.getElementById('numProyectosExp').value != ''){
            if(!validarNumericoEca(document.getElementById('numProyectosExp'))){
                document.getElementById('numProyectosExp').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosExpIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('numProyectosExp').value);
                    if(desp < 0){
                        //document.getElementById('numProyectosExp').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosExpIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        //document.getElementById('numProyectosExp').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosExpIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('numProyectosExp').removeAttribute("style");
                    }
                }
                catch(err){
                    //document.getElementById('numProyectosExp').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosExpIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        return correcto;
    }
    
    function validarPorcentajeInsMujeres(){
        var correcto = true;
        if(document.getElementById('porInsMujeres').value != ''){
            if(!validarNumericoDecimalEca(document.getElementById('porInsMujeres'), 8, 2)){
                document.getElementById('porInsMujeres').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.porInsMujeresIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('porInsMujeres').value);
                    if(desp < 0){
                        document.getElementById('porInsMujeres').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.porInsMujeresIncorrecto")%>';
                        correcto = false;
                    }else if(desp > 100){
                        document.getElementById('porInsMujeres').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.porInsMujeresIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('porInsMujeres').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('porInsMujeres').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.porInsMujeresIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        return correcto;
    }
    
    function validarNumProyectosSensEmpresarial(){
        var correcto = true;
        if(document.getElementById('numProyectosSensEmpresarial').value != ''){
            if(!validarNumericoEca(document.getElementById('numProyectosSensEmpresarial'))){
                document.getElementById('numProyectosSensEmpresarial').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosSensEmpresarial")%>';
                correcto = false;
            }
            else{
                try{
                    var desp = parseFloat(document.getElementById('numProyectosSensEmpresarial').value);
                    if(desp < 0){
                        document.getElementById('numProyectosSensEmpresarial').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosSensEmpresarial")%>';
                        correcto = false;
                    }else if(desp > 99999999.0){
                        document.getElementById('numProyectosSensEmpresarial').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosSensEmpresarial")%>';
                        correcto = false;
                    }else{
                        document.getElementById('numProyectosSensEmpresarial').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('numProyectosSensEmpresarial').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.valoracion.numProyectosSensEmpresarial")%>';
                    correcto = false;
                }
            }
        }
        return correcto;
    }
    
    function calcularPuntuacionExperiencia(){
        var valor = document.getElementById('numProyectosExp').value;
        
        if(valor != '' && validarNumericoEca(document.getElementById('numProyectosExp'))){
            var numP = parseFloat(document.getElementById('numProyectosExp').value);
            numP = numP * 1.5;
            if(numP > 15){
                numP = 15;
            }
            document.getElementById('puntuacionExp').value = numP;
            reemplazarPuntosEca(document.getElementById('puntuacionExp'));
            
        }else{
            document.getElementById('puntuacionExp').value = '';
        }
        calcularTotalValoracionSolicitudEca();
    }
    
    function calcularPuntuacionInsMujeres(){
        reemplazarPuntosEca(document.getElementById('porInsMujeres'));
        var valor = document.getElementById('porInsMujeres').value;
        if(valor != '' && validarNumericoDecimalEca(document.getElementById('porInsMujeres'), 50, 25)){
            valor = reemplazarTextoEca(valor, /,/g, '.');
            var numP = parseFloat(valor);
            if(valor == '' || isNaN(valor)){
                document.getElementById('puntuacionInsMujeres').value = '';
            }else{
                var puntos;
                if(numP < 0 || numP > 100){
                    puntos = '';
                }else if(numP < 20){
                    puntos = 0;
                }else if(numP >= 20 && numP < 30){
                    puntos = 2;
                }else if(numP >= 30 && numP < 40){
                    puntos = 4;
                }else if(numP >= 40 && numP < 50){
                    puntos = 6;
                }else if(numP >= 50 && numP < 60){
                    puntos = 8;
                }else if(numP >= 60){
                    puntos = 10;
                }
                document.getElementById('puntuacionInsMujeres').value = puntos;
                reemplazarPuntosEca(document.getElementById('puntuacionInsMujeres'));
            }
        }else{
            document.getElementById('puntuacionInsMujeres').value = '';
        }
        calcularTotalValoracionSolicitudEca();
    }
    
    function calcularPuntuacionSensEmpresarial(){
        var valor = document.getElementById('numProyectosSensEmpresarial').value;
        if(valor != '' && validarNumericoEca(document.getElementById('numProyectosSensEmpresarial'))){
            var numP = parseFloat(document.getElementById('numProyectosSensEmpresarial').value);
            if(numP > 10){
                numP = 10;
            }
            document.getElementById('puntuacionSensEmpresarial').value = numP;
            reemplazarPuntosEca(document.getElementById('puntuacionSensEmpresarial'));
        }else{
            document.getElementById('puntuacionSensEmpresarial').value = '';
        }
        calcularTotalValoracionSolicitudEca();
    }
    
    function calcularTotalValoracionSolicitudEca(){
        var sum1;
        var sum2;
        var sum3;
        var tot;
        var hayValor = false;
        try{
            var valor = document.getElementById('puntuacionExp').value;
            if(valor != undefined && valor != ''){
                valor = reemplazarTextoEca(valor, /,/g, '.');
            }
            sum1 = parseFloat(valor);
            if(isNaN(sum1)){
                sum1 = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            sum1 = 0.0;
        }
        
        try{
            var valor = document.getElementById('puntuacionInsMujeres').value;
            if(valor != undefined && valor != ''){
                valor = reemplazarTextoEca(valor, /,/g, '.');
            }
            sum2 = parseFloat(valor);
            if(isNaN(sum2)){
                sum2 = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            sum2 = 0.0;
        }
        
        try{
            var valor = document.getElementById('puntuacionSensEmpresarial').value;
            if(valor != undefined && valor != ''){
                valor = reemplazarTextoEca(valor, /,/g, '.');
            }
            sum3 = parseFloat(valor);
            if(isNaN(sum3)){
                sum3 = 0.0;
            }else{
                hayValor = true;
            }
        }catch(err){
            sum3 = 0.0;
        }
        if(hayValor){
            tot = sum1 + sum2 + sum3;
        }else{
            tot = '';
        }
        document.getElementById('totValoracionSol').value = tot;
        reemplazarPuntosEca(document.getElementById('totValoracionSol'));
    }
            
    function ajustarDecimalesImportesValoracion(){
        var campo;

        //Num Proyectos Exp
        campo = document.getElementById('numProyectosExp');
        ajustarDecimalesCampo(campo, 0);

        //Inserción de mujeres - Porcentaje
        campo = document.getElementById('porInsMujeres');
        ajustarDecimalesCampo(campo, 2);

        //Num Proyectos Exp
        campo = document.getElementById('numProyectosSensEmpresarial');
        ajustarDecimalesCampo(campo, 0);

        //Experiencia - Puntuación
        campo = document.getElementById('puntuacionExp');
        ajustarDecimalesCampo(campo, 2);

        //Inserción de mujeres - Puntuación
        campo = document.getElementById('puntuacionInsMujeres');
        ajustarDecimalesCampo(campo, 2);

        //Sensibilización empresarial - Puntuación
        campo = document.getElementById('puntuacionSensEmpresarial');
        ajustarDecimalesCampo(campo, 2);

        //Puntuación total
        campo = document.getElementById('totValoracionSol');
        ajustarDecimalesCampo(campo, 2);
    }
    
    
    
    function activarModoConsulta(){
        <%
            if(modoConsulta != null && modoConsulta.equalsIgnoreCase("si"))
            {
        %>
                document.getElementById('numProyectosExp').disabled = true;
                document.getElementById('numProyectosExp').readOnly = true;
                document.getElementById('porInsMujeres').disabled = true;
                document.getElementById('porInsMujeres').readOnly = true;
                document.getElementById('numProyectosSensEmpresarial').disabled = true;
                document.getElementById('numProyectosSensEmpresarial').readOnly = true;
        <%
            }
        %>
    }
</script>
<body>
    <div id="barraProgresoValoracionSolicitudEca" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgGuardandoDatosValoracionSolic">
                                                <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
    <div style="width: 920px; float: left; text-align: left; clear: both; height: 414px; overflow-x: hidden; overflow-y: auto; border: 1px solid #999999">
        <table style="width: 910px; font-size: 12px;text-align: center;">
            <tr class="negrita">
                <td style="text-align: left;" class="textoAzul"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.experiencia")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.numProyectos")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.puntuacion")%></td>
            </tr>
            <tr>
                <td style="text-align: left; padding-left: 40px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.experiencia.descripcion")%></td>
                <td><input id="numProyectosExp" name="numProyectosExp" type="text" size="6" class="inputTexto textoNumerico" maxlength="8" onkeyup="calcularPuntuacionExperiencia();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularPuntuacionExperiencia(); ajustarDecimalesImportesValoracion();"></td>
                <td><input id="puntuacionExp" name="puntuacionExp" type="text" size="6" class="inputTexto textoNumerico readOnly" maxlength="11" disabled="disabled"></td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td style="text-align: left;" class="textoAzul"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.insMujeres")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.porcentaje")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.puntuacion")%></td>
            </tr>
            <tr>
                <td style="text-align: left; padding-left: 40px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.insMujeres.descripcion")%></td>
                <td><input id="porInsMujeres" name="porInsMujeres" type="text" size="6" class="inputTexto textoNumerico" maxlength="5" onkeyup="calcularPuntuacionInsMujeres();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularPuntuacionInsMujeres(); ajustarDecimalesImportesValoracion();"></td>
                <td><input id="puntuacionInsMujeres" name="puntuacionInsMujeres" type="text" size="6" class="inputTexto textoNumerico readOnly" maxlength="11" disabled="disabled"></td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td style="text-align: left;" class="textoAzul"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.sensEmpresarial")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.numProyectos")%></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.puntuacion")%></td>
            </tr>
            <tr>
                <td style="text-align: left; padding-left: 40px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.sensEmpresarial.descripcion")%></td>
                <td><input id="numProyectosSensEmpresarial" name="numProyectosSensEmpresarial" type="text" size="6" class="inputTexto textoNumerico" maxlength="8" onkeyup="calcularPuntuacionSensEmpresarial();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularPuntuacionSensEmpresarial(); ajustarDecimalesImportesValoracion();"></td>
                <td><input id="puntuacionSensEmpresarial" name="puntuacionSensEmpresarial" type="text" size="6" class="inputTexto textoNumerico readOnly" maxlength="11" disabled="disabled"></td>
            </tr>
            <tr>
                <td colspan="3">
                    <div class="separadorTabla"></div>
                </td>
            </tr>
            <tr class="negrita">
                <td></td>
                <td><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.total").toUpperCase()%></td>
                <td><input id="totValoracionSol" name="totValoracionSol" type="text" size="6" class="inputTexto textoNumerico readOnly" maxlength="11" disabled="disabled"></td>
            </tr>
        </table>  
    </div>
    <div class="botonera" style="height: 50px; padding-top: 20px;">
        <input type="button" id="btnGuardarValoracionSolic" name="btnGuardarValoracionSolic" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.guardar")%>" onclick="guardarValoracionSolicitudEca();">
    </div> 
</body>

<script type="text/javascript">
    <%
        if(valoracion != null)
        {
    %>
            document.getElementById('numProyectosExp').value = '<%=valoracion.getExperienciaNum() != null ? valoracion.getExperienciaNum().toString() : ""%>';
            calcularPuntuacionExperiencia();
            document.getElementById('porInsMujeres').value = '<%=valoracion.getInsMujeresNum() != null ? valoracion.getInsMujeresNum().toPlainString() : ""%>';
            calcularPuntuacionInsMujeres();
            document.getElementById('numProyectosSensEmpresarial').value = '<%=valoracion.getSensibilidadNum() != null ? valoracion.getSensibilidadNum().toString() : ""%>';
            calcularPuntuacionSensEmpresarial();

    <%
        }
    %>
    ajustarDecimalesImportesValoracion();
        
    activarModoConsulta();
</script>

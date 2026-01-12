<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaUbicCentroEmpleoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaTrayectoriaCentroEmpleoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
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
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
    
    EntidadVO entidad = (EntidadVO)request.getAttribute("entidad");
    
    // Combo especialidades
    String lcodEspecialidades = "";
    String ldescEspecialidades = "";
    List<SelectItem> listaEspecialidades = new ArrayList<SelectItem>();
    if(request.getAttribute("listaEspecialidades") != null)
                listaEspecialidades = (List<SelectItem>)request.getAttribute("listaEspecialidades");        
    
    if (listaEspecialidades != null && listaEspecialidades.size() > 0) 
    {
        int i;
        SelectItem si = null;
        for (i = 0; i < listaEspecialidades.size() - 1; i++) 
        {
            si = (SelectItem) listaEspecialidades.get(i);
            lcodEspecialidades += "\"" + si.getId() + "\",";
            ldescEspecialidades += "\"" + escape(si.getLabel()) + "\",";
        }
        si = (SelectItem) listaEspecialidades.get(i);
        lcodEspecialidades += "\"" + si.getId() + "\"";
        ldescEspecialidades += "\"" + escape(si.getLabel()) + "\"";
    }
%>

<%!
// Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript">
    
    function mostrarPreguntas(){
        if(navigator.appName.indexOf("Internet Explorer") == -1){
            ocultarTabulacionesPreguntas();
        }
        var especialidad = '<%=entidad != null && entidad.getOriExpCod() != null ? entidad.getOriExpCod() : ""%>';
        document.getElementById('codPregunta3').value = especialidad;
        var espOtros = '<%=entidad != null && entidad.getOriEntOtros() != null ? entidad.getOriEntOtros() : ""%>';
        document.getElementById('textoLibrePregunta3').value = espOtros;
        cargarDescripcionesCombos();
        
        var acolocacion = '<%=entidad != null && entidad.getOriEntAcolocacion() != null ? entidad.getOriEntAcolocacion() : ""%>';
        if(acolocacion != null && acolocacion != ''){
            if(acolocacion == 'S'){
                document.getElementById('radioPregunta4S').checked = true;
            }else{
                document.getElementById('radioPregunta4N').checked = true;
            }
        }
        var pregunta1 = '<%=entidad != null && entidad.getOriEntAdmLocal() != null ? entidad.getOriEntAdmLocal() : ""%>';
        if(pregunta1 != null && pregunta1 != ''){
            if(pregunta1.toUpperCase() == 'S'){
                document.getElementById('radioPregunta1S').checked = true;
                document.getElementById('pregunta2').style.display = 'block';
            }else{
                document.getElementById('radioPregunta1N').checked = true;
                //document.getElementById('pregunta2').style.display = 'none';
                //mostrarPregunta3();
                //mostrarPregunta4();
                document.getElementById('pregunta5').style.display = 'block';
                document.getElementById('pregunta6').style.display = 'block';
            }
        }
        else{
            //document.getElementById('liPregunta2').style.display = 'none';
            //document.getElementById('radioPregunta2S').checked = false;
            //document.getElementById('radioPregunta2N').checked = false;
            //ocultarPregunta3();
            //ocultarPregunta4();
            document.getElementById('pregunta5').style.display = 'block';
            document.getElementById('pregunta6').style.display = 'block';
        }
        
        
        

        var pregunta2 = '<%=entidad != null && entidad.getOriEntSupramun() != null ? entidad.getOriEntSupramun() : ""%>';
        if(pregunta2 != null && pregunta2 != ''){
            if(pregunta2.toUpperCase() == 'N'){
                document.getElementById('radioPregunta2N').checked = true;
                //mostrarPregunta3();
                //mostrarPregunta4();
            }else{
                document.getElementById('radioPregunta2S').checked = true;
                //ocultarPregunta3();
                //ocultarPregunta4();
            }
        }else{
            //ocultarPregunta3();
            //ocultarPregunta4();
        }
        
        
        var numTrab = '<%=entidad != null && entidad.getOriEntNumtrab() != null ? entidad.getOriEntNumtrab().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta5').value = numTrab;
        var numTrabDisc = '<%=entidad != null && entidad.getOriEntNumtrabDisc() != null ? entidad.getOriEntNumtrabDisc().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta6').value = numTrabDisc;
        
    }
            
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";


        //Especialidades
        codigo = "<%=entidad != null && entidad.getOriExpCod() != null ? entidad.getOriExpCod() : ""%>";
        if(codigo != null && codigo != "")
        {
            for(var i=0; i<codEspecialidades.length; i++)
            {
                codAct = codEspecialidades[i];
                if(codAct == codigo)
                {
                    desc = descEspecialidades[i];
                }
            }
        }
        document.getElementById('descPregunta3').value = desc;
    }
    
    
    function cambioPregunta1(){
        if(document.getElementById('radioPregunta1S').checked){
            document.getElementById('liPregunta2').style.display = 'block';
        }else{
            document.getElementById('liPregunta2').style.display = 'none';
            document.getElementById('radioPregunta2S').checked = false;
            document.getElementById('radioPregunta2N').checked = false;
            ocultarPregunta3();
            ocultarPregunta4();
        }
    }
    
    function cambioPregunta2(){
        if(document.getElementById('radioPregunta2S').checked){
            ocultarPregunta3();
            ocultarPregunta4();
        }else{
            mostrarPregunta3();
            mostrarPregunta4();
        }
    }
    
    function mostrarPregunta3(){
        comboEspecialidades = new Combo("Pregunta3");
        comboEspecialidades.change = mostrarTextoLibrePregunta3;
        comboEspecialidades.addItems(codEspecialidades, descEspecialidades);

        document.getElementById('pregunta3').style.display = 'block';
        document.getElementById('liPregunta3').style.display = 'block';
        mostrarTextoLibrePregunta3();   
    }
    
    function ocultarPregunta3(){
        document.getElementById('pregunta3').style.display = 'none';
        document.getElementById('liPregunta3').style.display = 'none';
        resetCombo('Pregunta3');
        mostrarTextoLibrePregunta3();
    }
    
    function mostrarPregunta4(){
        document.getElementById('pregunta4').style.display = 'block';
        document.getElementById('liPregunta4').style.display = 'block';
    }
    
    function ocultarPregunta4(){
        document.getElementById('pregunta4').style.display = 'none';
        document.getElementById('liPregunta4').style.display = 'none';
        document.getElementById('radioPregunta4S').checked = false;
        document.getElementById('radioPregunta4N').checked = false;
    }
    
    function mostrarTextoLibrePregunta3(){
        var codOtros = '<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
        var codSeleccionado = document.getElementById('codPregunta3').value;
        var otrosSeleccionado = false;
        if(codOtros != null && codOtros != ''){
            if(codSeleccionado != null && codSeleccionado != ''){
                if(codOtros == codSeleccionado){
                    otrosSeleccionado = true;
                }
            }
        }
        if(otrosSeleccionado){
            document.getElementById('campoLibrePregunta3').style.display = 'block';
        }else{
            document.getElementById('campoLibrePregunta3').style.display = 'none';
            document.getElementById('textoLibrePregunta3').value = '';
        }
    }
    
    function ocultarTabulacionesPreguntas(){
        document.getElementById('listaPreguntas').style.listStyle = 'none';
    }
    
    function deshabilitarRadiosOtrosDatos(){
        if(document.forms[0].modoConsulta.value == "si"){
            document.getElementById('radioPregunta1S').disabled = true;
            document.getElementById('radioPregunta1N').disabled = true;

            document.getElementById('radioPregunta2S').disabled = true;
            document.getElementById('radioPregunta2N').disabled = true;

            document.getElementById('radioPregunta4S').disabled = true;
            document.getElementById('radioPregunta4N').disabled = true;
            
            document.getElementById('botonPregunta3').style.display = 'none';
        }
    }
    
    function validarOtrosDatos(){
        
        
        var txt = document.getElementById('textoLibrePregunta3').value;
        if(!comprobarCaracteresEspecialesCE(txt)){
            document.getElementById('textoLibrePregunta3').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.textoLibrePregunta3CaracteresEspeciales")%>';
            return false;
        }else{
            mensajeValidacion = '';
            document.getElementById('textoLibrePregunta3').removeAttribute("style");
        }
        
        var correcto = true;
        
        var str = document.getElementById('textoLibrePregunta5').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta5'))){
                document.getElementById('textoLibrePregunta5').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta5').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta5').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta5').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta5').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta5').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        str = document.getElementById('textoLibrePregunta6').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta6'))){
                document.getElementById('textoLibrePregunta6').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta6').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta6').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta6').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta6').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta6').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        
        return correcto;
    }
    
    function getParametrosOtrosDatosCE(){
        var parametros = "";
        var respuesta1 = '';
        var respuesta2 = '';
        var respuesta4 = '';
        if(document.getElementById('radioPregunta1S').checked)
            respuesta1 = 'S';
        else if(document.getElementById('radioPregunta1N').checked)
            respuesta1 = 'N';
        else
            respuesta1 = '';
        if(document.getElementById('radioPregunta2S').checked)
            respuesta2 = 'S';
        else if(document.getElementById('radioPregunta2N').checked)
            respuesta2 = 'N';
        else
            respuesta2 = '';
        if(document.getElementById('radioPregunta4S').checked)
            respuesta4 = 'S';
        else if(document.getElementById('radioPregunta4N').checked)
            respuesta4 = 'N';
        else
            respuesta4 = '';
        parametros = 'admLocal='+respuesta1
            +'&supramun='+respuesta2
            +'&especialidad='+document.getElementById('codPregunta3').value
            +'&otros='+escape(document.getElementById('textoLibrePregunta3').value)
            +'&acolocacion='+respuesta4
            +'&numTrab='+escape(document.getElementById('textoLibrePregunta5').value)
            +'&numTrabDisc='+escape(document.getElementById('textoLibrePregunta6').value);
        return parametros;
    }
    
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";


        //Especialidades
        codigo = "<%=entidad != null && entidad.getOriExpCod() != null ? entidad.getOriExpCod() : ""%>";
        if(codigo != null && codigo != "")
        {
            for(var i=0; i<codEspecialidades.length; i++)
            {
                codAct = codEspecialidades[i];
                if(codAct == codigo)
                {
                    desc = descEspecialidades[i];
                }
            }
        }
        document.getElementById('descPregunta3').value = desc;
    }
</script>

<body>
    <div id="otrosDatos" style="height: 220px;">
        <div class="tituloAzul">
            <span>
                <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.otrosDatos")%>
            </span>
        </div>
            <ul id="listaPreguntas">
            <li id="liPregunta1" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta1">
                    <div style="width: 300px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta1")%>
                    </div>
                    <div style="width: 100px; float: left;">
                        <div style="float: left;">
                            <input type="radio" name="radioPregunta1" id="radioPregunta1S" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                            <input type="radio" name="radioPregunta1" id="radioPregunta1N" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                        </div>
                    </div>
                </div>
            </li>
            <li id="liPregunta2" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta2">
                    <div style="width: 300px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta2")%>
                    </div>
                    <div style="width: 100px; float: left;">
                        <div style="float: left;">
                            <input type="radio" name="radioPregunta2" id="radioPregunta2S" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                            <input type="radio" name="radioPregunta2" id="radioPregunta2N" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                        </div>
                    </div>
                </div>
            </li>
            <li id="liPregunta3" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta3">
                    <div style="width: 130px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codPregunta3" name="codPregunta3" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descPregunta3" name="descPregunta3" type="text" class="inputTexto" size="30" readonly>
                            <a id="anchorPregunta3" name="anchorPregunta3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPregunta3" name="botonPregunta3" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div id="campoLibrePregunta3" style="width: 100%; clear: both; text-align: left;">
                        <input id="textoLibrePregunta3" name="textoLibrePregunta3" type="text" class="inputTexto" size="100" maxlength="100">
                    </div>
                </div>
            </li>
            <li id="liPregunta4" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta4">
                    <div style="width: 570px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta4")%>
                    </div>
                    <div style="width: 100px; float: left;">
                        <div style="float: left;">
                            <input type="radio" name="radioPregunta4" id="radioPregunta4S" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                            <input type="radio" name="radioPregunta4" id="radioPregunta4N" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                        </div>
                    </div>
                </div>
            </li>
            <li id="liPregunta5" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta5">
                    <div style="width: 220px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta5")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="textoLibrePregunta5" name="textoLibrePregunta5" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                        </div>
                    </div>
                </div>
            </li>
            <li id="liPregunta6" style="vertical-align: middle;">
                <div class="lineaFormulario" id="pregunta6">
                    <div style="width: 220px; float: left; display: inline-table; text-align: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta6")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="textoLibrePregunta6" name="textoLibrePregunta6" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</body>

<script type="text/javascript">   
    var comboEspecialidades = new Combo("Pregunta3");
    comboEspecialidades.change = mostrarTextoLibrePregunta3;
        
    var codEspecialidades = [<%=lcodEspecialidades%>];
    var descEspecialidades = [<%=ldescEspecialidades%>];
    
    comboEspecialidades.addItems(codEspecialidades, descEspecialidades);
    
    mostrarPreguntas();
    mostrarTextoLibrePregunta3();
</script>

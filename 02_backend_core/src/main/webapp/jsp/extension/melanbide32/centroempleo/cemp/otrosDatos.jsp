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
    String sIdioma = request.getParameter("idioma")!=null?request.getParameter("idioma"):"1";
    String ejercicioStr = request.getAttribute("ejercicio")!=null?(String)request.getAttribute("ejercicio"):"0";
    int ejercicio = Integer.parseInt(ejercicioStr);
    String ocultaDespleEntiEspeciEn = (ejercicio<2017?"false":"true");
    String codigoLabelPregunta1 = "ce.label.pregunta1";
    String codigoLabelPregunta2 = "ce.label.pregunta2";
    String codigoLabelPregunta4 = "ce.label.pregunta4";
    String ocultoAntes2017="";
    if(ejercicio>=2017){
    codigoLabelPregunta1 += ".localMunicipal";
    codigoLabelPregunta2 += ".supramunicipal";
    codigoLabelPregunta4 += ".agenciaColocac";
    ocultoAntes2017="display: none;";
    }
    
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
    //*************** COMPATIBILIDAD INDEXOF EN UN ARRAY PARA IE EXPLORER
    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function (searchElement /*, fromIndex */) {
            "use strict";
            if (this == null) {
                throw new TypeError();
            }
            var t = Object(this);
            var len = t.length >>> 0;
            if (len === 0) {
                return -1;
            }
            var n = 0;
            if (arguments.length > 1) {
                n = Number(arguments[1]);
                if (n != n) { // para verificar si es NaN
                    n = 0;
                } else if (n != 0 && n != Infinity && n != -Infinity) {
                    n = (n > 0 || -1) * Math.floor(Math.abs(n));
                }
            }
            if (n >= len) {
                return -1;
            }
            var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
            for (; k < len; k++) {
                if (k in t && t[k] === searchElement) {
                    return k;
                }
            }
            return -1;
        }
    }
    //***************

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
        else
        {
            codAct='<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
            var index= codEspecialidades.indexOf('<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>');
            desc = descEspecialidades[index];
            /*
            codAct=codEspecialidades[(codEspecialidades.length)-1];
            desc = descEspecialidades[(codEspecialidades.length)-1];
            */
        }
        document.getElementById('descPregunta3').value = desc;
    }
    
    //Preguntas columna solicitado
    function mostrarPreguntas(){
        var especialidad = '<%=entidad != null && entidad.getOriExpCod() != null ? entidad.getOriExpCod() : ""%>';
        if(especialidad==null || especialidad=='')
            especialidad='<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
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
                if('<%=ejercicio%>'<2017){
                document.getElementById('pregunta2').style.display = 'block';
                }
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
        
        // Nuevos campos convocatoria 2017
        <%if(ejercicio>=2017){%>
        var preguntaNueva = '<%=entidad != null && entidad.getTipAtenPerDiscapa() != null ? entidad.getTipAtenPerDiscapa() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipopersDiscapacidN').checked = true;
            }else{
                document.getElementById('radioTipopersDiscapacidS').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipAtenColRiesExc()!=null? entidad.getTipAtenColRiesExc() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTiporiesgoExclusioN').checked = true;
            }else{
                document.getElementById('radioTiporiesgoExclusioS').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipCentroForProfe() != null ? entidad.getTipCentroForProfe() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipoformaProfesionN').checked = true;
            }else{
                document.getElementById('radioTipoformaProfesionS').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipOrgSindiEmpres() != null ? entidad.getTipOrgSindiEmpres() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipoorgSindiEmpresN').checked = true;
            }else{
                document.getElementById('radioTipoorgSindiEmpresS').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipSinAnimoLucro() != null ? entidad.getTipSinAnimoLucro() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTiposinAnimoLucroN').checked = true;
            }else{
                document.getElementById('radioTiposinAnimoLucroS').checked = true;
            }
        }
        <% }%>
        var numTrab = '<%=entidad != null && entidad.getOriEntNumtrab() != null ? entidad.getOriEntNumtrab().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta5').value = numTrab;
        var numTrabDisc = '<%=entidad != null && entidad.getOriEntNumtrabDisc() != null ? entidad.getOriEntNumtrabDisc().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta6').value = numTrabDisc;
        var porTrabDisc = '<%=entidad != null && entidad.getOriEntPortrabDisc() != null ? entidad.getOriEntPortrabDisc().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta7').value = porTrabDisc;
        
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
            document.getElementById('textoLibrePregunta3').style.display = 'block';
        }else{
            document.getElementById('textoLibrePregunta3').style.display = 'none';
            document.getElementById('textoLibrePregunta3').value = '';
        }
    }
    
    //Fin preguntas columna solicitado
    
    //Preguntas columna validado
    function mostrarPreguntasVal(){
        var especialidad = '<%=entidad != null && entidad.getOriExpCodVal() != null ? entidad.getOriExpCodVal() : ""%>';
        if(especialidad==null || especialidad=='')
            especialidad='<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
        document.getElementById('codPregunta3Val').value = especialidad;
        var espOtros = '<%=entidad != null && entidad.getOriEntOtrosVal() != null ? entidad.getOriEntOtrosVal() : ""%>';
        document.getElementById('textoLibrePregunta3Val').value = espOtros;
        cargarDescripcionesCombosVal();
        
        var acolocacion = '<%=entidad != null && entidad.getOriEntAcolocacionVal() != null ? entidad.getOriEntAcolocacionVal() : ""%>';
        if(acolocacion != null && acolocacion != ''){
            if(acolocacion == 'S'){
                document.getElementById('radioPregunta4SVal').checked = true;
            }else{
                document.getElementById('radioPregunta4NVal').checked = true;
            }
        }
        var pregunta1 = '<%=entidad != null && entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : ""%>';
        if(pregunta1 != null && pregunta1 != ''){
            if(pregunta1.toUpperCase() == 'S'){
                document.getElementById('radioPregunta1SVal').checked = true;
                if('<%=ejercicio%>'<2017){
                document.getElementById('pregunta2Val').style.display = 'block';
                }
            }else{
                document.getElementById('radioPregunta1NVal').checked = true;
                //document.getElementById('pregunta2').style.display = 'none';
                //mostrarPregunta3();
                //mostrarPregunta4();
                document.getElementById('pregunta5Val').style.display = 'block';
                document.getElementById('pregunta6Val').style.display = 'block';
            }
        }
        else{
            //document.getElementById('liPregunta2').style.display = 'none';
            //document.getElementById('radioPregunta2S').checked = false;
            //document.getElementById('radioPregunta2N').checked = false;
            //ocultarPregunta3();
            //ocultarPregunta4();
            document.getElementById('pregunta5Val').style.display = 'block';
            document.getElementById('pregunta6Val').style.display = 'block';
        }
        
        
        

        var pregunta2 = '<%=entidad != null && entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : ""%>';
        if(pregunta2 != null && pregunta2 != ''){
            if(pregunta2.toUpperCase() == 'N'){
                document.getElementById('radioPregunta2NVal').checked = true;
                //mostrarPregunta3();
                //mostrarPregunta4();
            }else{
                document.getElementById('radioPregunta2SVal').checked = true;
                //ocultarPregunta3();
                //ocultarPregunta4();
            }
        }else{
            //ocultarPregunta3();
            //ocultarPregunta4();
        }
        
        // Nuevos campos convocatoria 2017
        <%if(ejercicio>=2017){%>
        var preguntaNueva = '<%=entidad != null && entidad.getTipAtenPerDiscapaVAL() != null ? entidad.getTipAtenPerDiscapaVAL() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipopersDiscapacidNVal').checked = true;
            }else{
                document.getElementById('radioTipopersDiscapacidSVal').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipAtenColRiesExcVAL()!=null? entidad.getTipAtenColRiesExcVAL() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTiporiesgoExclusioNVal').checked = true;
            }else{
                document.getElementById('radioTiporiesgoExclusioSVal').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipCentroForProfeVAL() != null ? entidad.getTipCentroForProfeVAL() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipoformaProfesionNVal').checked = true;
            }else{
                document.getElementById('radioTipoformaProfesionSVal').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipOrgSindiEmpresVAL() != null ? entidad.getTipOrgSindiEmpresVAL() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTipoorgSindiEmpresNVal').checked = true;
            }else{
                document.getElementById('radioTipoorgSindiEmpresSVal').checked = true;
            }
        }
        preguntaNueva="";
        preguntaNueva = '<%=entidad != null && entidad.getTipSinAnimoLucroVAL() != null ? entidad.getTipSinAnimoLucroVAL() : ""%>';
        if(preguntaNueva != null && preguntaNueva != ''){
            if(preguntaNueva.toUpperCase() == 'N'){
                document.getElementById('radioTiposinAnimoLucroNVal').checked = true;
            }else{
                document.getElementById('radioTiposinAnimoLucroSVal').checked = true;
            }
        }
        <% }%>
            
        var numTrab = '<%=entidad != null && entidad.getOriEntNumtrabVal() != null ? entidad.getOriEntNumtrabVal().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta5Val').value = numTrab;
        var numTrabDisc = '<%=entidad != null && entidad.getOriEntNumtrabDiscVal() != null ? entidad.getOriEntNumtrabDiscVal().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta6Val').value = numTrabDisc;
        var porTrabDisc = '<%=entidad != null && entidad.getOriEntPortrabDiscVal() != null ? entidad.getOriEntPortrabDiscVal().toString().replaceAll("\\.", ",") : ""%>';
        document.getElementById('textoLibrePregunta7Val').value = porTrabDisc;
        
    }
    
    function cambioPregunta1Val(){
        if(document.getElementById('radioPregunta1SVal').checked){
            document.getElementById('liPregunta2Val').style.display = 'block';
        }else{
            document.getElementById('liPregunta2Val').style.display = 'none';
            document.getElementById('radioPregunta2SVal').checked = false;
            document.getElementById('radioPregunta2NVal').checked = false;
            ocultarPregunta3Val();
            ocultarPregunta4Val();
        }
    }
    
    function cambioPregunta2Val(){
        if(document.getElementById('radioPregunta2SVal').checked){
            ocultarPregunta3Val();
            ocultarPregunta4Val();
        }else{
            mostrarPregunta3Val();
            mostrarPregunta4Val();
        }
    }
    
    function mostrarPregunta3Val(){
        comboEspecialidadesVal = new Combo("Pregunta3Val");
        comboEspecialidadesVal.change = mostrarTextoLibrePregunta3Val;
        comboEspecialidadesVal.addItems(codEspecialidades, descEspecialidades);

        document.getElementById('pregunta3Val').style.display = 'block';
        document.getElementById('liPregunta3Val').style.display = 'block';
        mostrarTextoLibrePregunta3Val();   
    }
    
    function ocultarPregunta3Val(){
        document.getElementById('pregunta3Val').style.display = 'none';
        document.getElementById('liPregunta3Val').style.display = 'none';
        resetCombo('Pregunta3Val');
        mostrarTextoLibrePregunta3Val();
    }
    
    function mostrarPregunta4Val(){
        document.getElementById('pregunta4Val').style.display = 'block';
        document.getElementById('liPregunta4Val').style.display = 'block';
    }
    
    function ocultarPregunta4Val(){
        document.getElementById('pregunta4Val').style.display = 'none';
        document.getElementById('liPregunta4Val').style.display = 'none';
        document.getElementById('radioPregunta4SVal').checked = false;
        document.getElementById('radioPregunta4NVal').checked = false;
    }
    
    function mostrarTextoLibrePregunta3Val(){
        var codOtros = '<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
        var codSeleccionado = document.getElementById('codPregunta3Val').value;
        var otrosSeleccionado = false;
        if(codOtros != null && codOtros != ''){
            if(codSeleccionado != null && codSeleccionado != ''){
                if(codOtros == codSeleccionado){
                    otrosSeleccionado = true;
                }
            }
        }
        if(otrosSeleccionado){
            document.getElementById('textoLibrePregunta3Val').style.display = 'block';
        }else{
            document.getElementById('textoLibrePregunta3Val').style.display = 'none';
            document.getElementById('textoLibrePregunta3Val').value = '';
        }
    }
    
    function  mostrarliPregunta2(){
        var selRadio = document.getElementById('radioPregunta1S').checked;
        var selRadioVal = document.getElementById('radioPregunta1SVal').checked;
        
        if(selRadio){
            document.getElementById('groupradioPreg2').style.display = 'block';
            //document.getElementById('radioPregunta2N').style.display = 'block';
            // Al mostrar mostramos lo que se ha recogidode BD -- sino por defecto ponemos 'N'
            /*
            var pregunta2 = '<%=entidad != null && entidad.getOriEntSupramun() != null ? entidad.getOriEntSupramun() : ""%>';
            if(pregunta2 != null && pregunta2 != ''){
                if(pregunta2.toUpperCase() == 'N'){
                    document.getElementById('radioPregunta2N').checked = true;
                }else{
                    document.getElementById('radioPregunta2S').checked = true;
                }
            }else{
                document.getElementById('radioPregunta2N').checked = true;
            }
            */
        }
        else{
            document.getElementById('groupradioPreg2').style.display = 'none';
            //document.getElementById('radioPregunta2N').style.display = 'none';
            // Al ocultar asigamos el valor por defecto 'N' a la pregunta 2
            document.getElementById('radioPregunta2S').checked=false;
            document.getElementById('radioPregunta2N').checked=true;
        }
        if(selRadioVal){
            document.getElementById('groupradioPreg2Val').style.display = 'block';
            //document.getElementById('radioPregunta2NVal').style.display = 'block';
            // Al mostrar mostramos lo que se ha recogido de BD -- sino por defecto ponemos 'N'
           /*
            var pregunta2 = '<%=entidad != null && entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : ""%>';
            if(pregunta2 != null && pregunta2 != ''){
                if(pregunta2.toUpperCase() == 'N'){
                    document.getElementById('radioPregunta2NVal').checked = true;
                }else{
                    document.getElementById('radioPregunta2SVal').checked = true;
                }
            }else{
                document.getElementById('radioPregunta2NVal').checked = true;
            }
            */
        }
        else{
            document.getElementById('groupradioPreg2Val').style.display = 'none';
            //document.getElementById('radioPregunta2NVal').style.display = 'none';
            // al ocultar asigamos el valor por defecto 'N' a la pregunta 2
            document.getElementById('radioPregunta2SVal').checked=false;
            document.getElementById('radioPregunta2NVal').checked=true;
        }
        
        if(!selRadio && !selRadioVal){
            document.getElementById('grouptextoPreg2').style.display = 'none';
        }
        else
            document.getElementById('grouptextoPreg2').style.display = 'block';
    }
    
    //Fin preguntas columna validado
    
    function deshabilitarRadiosOtrosDatos(){
        if(document.forms[0].modoConsulta.value == "si"){
            document.getElementById('radioPregunta1S').disabled = true;
            document.getElementById('radioPregunta1N').disabled = true;

            document.getElementById('radioPregunta2S').disabled = true;
            document.getElementById('radioPregunta2N').disabled = true;

            document.getElementById('radioPregunta4S').disabled = true;
            document.getElementById('radioPregunta4N').disabled = true;
            
            document.getElementById('radioPregunta1SVal').disabled = true;
            document.getElementById('radioPregunta1NVal').disabled = true;

            document.getElementById('radioPregunta2SVal').disabled = true;
            document.getElementById('radioPregunta2NVal').disabled = true;

            document.getElementById('radioPregunta4SVal').disabled = true;
            document.getElementById('radioPregunta4NVal').disabled = true;
            
            document.getElementById('botonPregunta3').style.display = 'none';
            document.getElementById('botonPregunta3Val').style.display = 'none';
        }
    }
    
    function validarOtrosDatos(){
        
        //Solicitado
        var txt = document.getElementById('textoLibrePregunta3').value;
        if(!comprobarCaracteresEspecialesCE(txt)){
            document.getElementById('textoLibrePregunta3').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.textoLibrePregunta3CaracteresEspeciales")%>';
            return false;
        }else{
            mensajeValidacion = '';
            document.getElementById('textoLibrePregunta3').style.border ='';
            //document.getElementById('textoLibrePregunta3').removeAttribute("style");
        }
        
        var correcto = true;
        /*
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
        */
        str = document.getElementById('textoLibrePregunta7').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta7'))){
                document.getElementById('textoLibrePregunta7').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta7').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta7').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta7').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta7').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta7').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                    correcto = false;
                }
            }
        }

        //Validado
        var txt = document.getElementById('textoLibrePregunta3Val').value;
        if(!comprobarCaracteresEspecialesCE(txt)){
            document.getElementById('textoLibrePregunta3Val').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.textoLibrePregunta3ValCaracteresEspeciales")%>';
            return false;
        }else{
            mensajeValidacion = '';
            document.getElementById('textoLibrePregunta3Val').style.border='';
            //document.getElementById('textoLibrePregunta3Val').removeAttribute("style");
        }
        

        var correcto = true;
        /*
        var str = document.getElementById('textoLibrePregunta5Val').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta5Val'))){
                document.getElementById('textoLibrePregunta5Val').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta5Val').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta5Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta5Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta5Val').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta5Val').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        str = document.getElementById('textoLibrePregunta6Val').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta6Val'))){
                document.getElementById('textoLibrePregunta6Val').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta6Val').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta6Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta6Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta6Val').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta6Val').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                    correcto = false;
                }
            }
        }
        */
        str = document.getElementById('textoLibrePregunta7Val').value;
        if(str != null && str != ''){
            if(!validarNumericoDecimalCE(document.getElementById('textoLibrePregunta7Val'))){
                document.getElementById('textoLibrePregunta7Val').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                correcto = false;
            }
            else{
                try{
                    var num = parseFloat(document.getElementById('textoLibrePregunta7Val').value.replace(/\,/,"."));
                    if(num < 0){
                        document.getElementById('textoLibrePregunta7Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else if(num > 99999.0){
                        document.getElementById('textoLibrePregunta7Val').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.otrosDatosIncorrecto")%>';
                        correcto = false;
                    }else{
                        document.getElementById('textoLibrePregunta7Val').removeAttribute("style");
                    }
                }
                catch(err){
                    document.getElementById('textoLibrePregunta7Val').style.border = '1px solid red';
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
        
        var respuesta1Val = '';
        var respuesta2Val = '';
        var respuesta4Val = '';
        if(document.getElementById('radioPregunta1SVal').checked)
            respuesta1Val = 'S';
        else if(document.getElementById('radioPregunta1NVal').checked)
            respuesta1Val = 'N';
        else
            respuesta1Val = '';
        if(document.getElementById('radioPregunta2SVal').checked)
            respuesta2Val = 'S';
        else if(document.getElementById('radioPregunta2NVal').checked)
            respuesta2Val = 'N';
        else
            respuesta2Val = '';
        if(document.getElementById('radioPregunta4SVal').checked)
            respuesta4Val = 'S';
        else if(document.getElementById('radioPregunta4NVal').checked)
            respuesta4Val = 'N';
        else
            respuesta4Val = '';
        
        
        parametros = 'admLocal='+respuesta1
            +'&supramun='+respuesta2
            +'&especialidad='+(document.getElementById('codPregunta3')!=null?document.getElementById('codPregunta3').value:"")
            +'&otros='+(document.getElementById('textoLibrePregunta3')!=null?escape(document.getElementById('textoLibrePregunta3').value):"")
            +'&acolocacion='+respuesta4
            +'&numTrab='+escape(document.getElementById('textoLibrePregunta5').value)
            +'&numTrabDisc='+escape(document.getElementById('textoLibrePregunta6').value)
            +'&porTrabDisc='+escape(document.getElementById('textoLibrePregunta7').value)
            +'&admLocalVal='+respuesta1Val
            +'&supramunVal='+respuesta2Val
            +'&especialidadVal='+document.getElementById('codPregunta3Val').value
            +'&otrosVal='+escape(document.getElementById('textoLibrePregunta3Val').value)
            +'&acolocacionVal='+respuesta4Val
            +'&numTrabVal='+escape(document.getElementById('textoLibrePregunta5Val').value)
            +'&numTrabDiscVal='+escape(document.getElementById('textoLibrePregunta6Val').value)
            +'&porTrabDiscVal='+escape(document.getElementById('textoLibrePregunta7Val').value)
            +'&persDiscapacid='+(document.getElementById('radioTipopersDiscapacidS')!=null && document.getElementById('radioTipopersDiscapacidS').checked?"S":document.getElementById('radioTipopersDiscapacidN')!=null && document.getElementById('radioTipopersDiscapacidN').checked?"N":"")
            +'&riesgoExclusio='+(document.getElementById('radioTiporiesgoExclusioS')!=null && document.getElementById('radioTiporiesgoExclusioS').checked?"S":document.getElementById('radioTiporiesgoExclusioN')!=null && document.getElementById('radioTiporiesgoExclusioN').checked?"N":"")
            +'&formaProfesion='+(document.getElementById('radioTipoformaProfesionS')!=null && document.getElementById('radioTipoformaProfesionS').checked?"S":document.getElementById('radioTipoformaProfesionN')!=null && document.getElementById('radioTipoformaProfesionN').checked?"N":"")
            +'&orgSindiEmpres='+(document.getElementById('radioTipoorgSindiEmpresS')!=null && document.getElementById('radioTipoorgSindiEmpresS').checked?"S":document.getElementById('radioTipoorgSindiEmpresN')!=null && document.getElementById('radioTipoorgSindiEmpresN').checked?"N":"")
            +'&sinAnimoLucro='+(document.getElementById('radioTiposinAnimoLucroS')!=null && document.getElementById('radioTiposinAnimoLucroS').checked?"S":document.getElementById('radioTiposinAnimoLucroN')!=null && document.getElementById('radioTiposinAnimoLucroN').checked?"N":"")
            +'&persDiscapacidVAL='+(document.getElementById('radioTipopersDiscapacidSVal')!=null && document.getElementById('radioTipopersDiscapacidSVal').checked?"S":document.getElementById('radioTipopersDiscapacidNVal')!=null && document.getElementById('radioTipopersDiscapacidNVal').checked?"N":"")
            +'&riesgoExclusioVAL='+(document.getElementById('radioTiporiesgoExclusioSVal')!=null && document.getElementById('radioTiporiesgoExclusioSVal').checked?"S":document.getElementById('radioTiporiesgoExclusioNVal')!=null && document.getElementById('radioTiporiesgoExclusioNVal').checked?"N":"")
            +'&formaProfesionVAL='+(document.getElementById('radioTipoformaProfesionSVal')!=null && document.getElementById('radioTipoformaProfesionSVal').checked?"S":document.getElementById('radioTipoformaProfesionNVal')!=null && document.getElementById('radioTipoformaProfesionNVal').checked?"N":"")
            +'&orgSindiEmpresVAL='+(document.getElementById('radioTipoorgSindiEmpresSVal')!=null && document.getElementById('radioTipoorgSindiEmpresSVal').checked?"S":document.getElementById('radioTipoorgSindiEmpresNVal')!=null && document.getElementById('radioTipoorgSindiEmpresNVal').checked?"N":"")
            +'&sinAnimoLucroVAL='+(document.getElementById('radioTiposinAnimoLucroSVal')!=null && document.getElementById('radioTiposinAnimoLucroSVal').checked?"S":document.getElementById('radioTiposinAnimoLucroNVal')!=null && document.getElementById('radioTiposinAnimoLucroNVal').checked?"N":"")
    ;
        return parametros;
    }
    /* La comennto --  esta repetida -- hecha en la linea 69
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
        else
        {
            codAct=codEspecialidades[(codEspecialidades.length)-1];
            desc = descEspecialidades[(codEspecialidades.length)-1];
        }
        document.getElementById('descPregunta3').value = desc;
    }
    */
    function cargarDescripcionesCombosVal(){
        var desc = "";
        var codAct = "";
        var codigo = "";


        //Especialidades
        codigo = "<%=entidad != null && entidad.getOriExpCodVal() != null ? entidad.getOriExpCodVal() : ""%>";
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
        else
        {
            codAct='<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>';
            var index= codEspecialidades.indexOf('<%=ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>');
            desc = descEspecialidades[index];
            /*
            codAct=codEspecialidades.[(codEspecialidades.length)-1];
            desc = descEspecialidades[(codEspecialidades.length)-1];
            */
        }
        document.getElementById('descPregunta3Val').value = desc;
    }
    
    function copiarOtrosDatosCE(){
        document.getElementById('radioPregunta1SVal').checked = document.getElementById('radioPregunta1S').checked;
        document.getElementById('radioPregunta1NVal').checked = document.getElementById('radioPregunta1N').checked;
        document.getElementById('radioPregunta2SVal').checked = document.getElementById('radioPregunta2S').checked;
        document.getElementById('radioPregunta2NVal').checked = document.getElementById('radioPregunta2N').checked;
        document.getElementById('radioPregunta4SVal').checked = document.getElementById('radioPregunta4S').checked;
        document.getElementById('radioPregunta4NVal').checked = document.getElementById('radioPregunta4N').checked;
        document.getElementById('codPregunta3Val').value = document.getElementById('codPregunta3').value;
        document.getElementById('textoLibrePregunta3Val').value = document.getElementById('textoLibrePregunta3').value;
        document.getElementById('textoLibrePregunta5Val').value = document.getElementById('textoLibrePregunta5').value;
        document.getElementById('textoLibrePregunta6Val').value = document.getElementById('textoLibrePregunta6').value;
        document.getElementById('textoLibrePregunta7Val').value = document.getElementById('textoLibrePregunta7').value;
        document.getElementById('descPregunta3Val').value = document.getElementById('descPregunta3').value;
        <%if(ejercicio>=2017){%>
        document.getElementById('radioTipopersDiscapacidSVal').checked = document.getElementById('radioTipopersDiscapacidS').checked;
        document.getElementById('radioTipopersDiscapacidNVal').checked = document.getElementById('radioTipopersDiscapacidN').checked;
        document.getElementById('radioTiporiesgoExclusioSVal').checked = document.getElementById('radioTiporiesgoExclusioS').checked;
        document.getElementById('radioTiporiesgoExclusioNVal').checked = document.getElementById('radioTiporiesgoExclusioN').checked;
        document.getElementById('radioTipoformaProfesionSVal').checked = document.getElementById('radioTipoformaProfesionS').checked;
        document.getElementById('radioTipoformaProfesionNVal').checked = document.getElementById('radioTipoformaProfesionN').checked;
        document.getElementById('radioTipoorgSindiEmpresSVal').checked = document.getElementById('radioTipoorgSindiEmpresS').checked;
        document.getElementById('radioTipoorgSindiEmpresNVal').checked = document.getElementById('radioTipoorgSindiEmpresN').checked;
        document.getElementById('radioTiposinAnimoLucroSVal').checked = document.getElementById('radioTiposinAnimoLucroS').checked;
        document.getElementById('radioTiposinAnimoLucroNVal').checked = document.getElementById('radioTiposinAnimoLucroN').checked;
        <% }%>
        mostrarliPregunta2();
        mostrarTextoLibrePregunta3();
        mostrarTextoLibrePregunta3Val();
    }
</script>
<body>
    
    <div class="tituloAzul">
        <span>
            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.otrosDatos")%>
        </span>
    </div>
    <div id="otrosDatos" style="height: 350px; width: 100%; float: left;">
        <table id="listaPreguntas" style="font: 11px Tahoma; width: 1000; table-layout: fixed">
            <tr style="height: 50px; text-align: center; table-layout: fixed">
                <td class="column400Fix">
                    
                </td>
                <td style="font-weight:bold;" class="column250FixCentText">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.solicitado").toUpperCase()%>
                </td>
                <td style="font-weight:bold;" class="column250FixCentText">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.validado").toUpperCase()%>
                </td>
            </tr>
            <tr id="liPregunta1" style="vertical-align: middle;">
                <td id="pregunta1Texto" class="column400Fix">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,codigoLabelPregunta1)%>
                </td>
                <td id="pregunta1" class="column250FixCentText">
                    <input type="radio" name="radioPregunta1" id="radioPregunta1S" value="S" onclick="mostrarliPregunta2()"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta1" id="radioPregunta1N" value="N" onclick="mostrarliPregunta2()"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta1Val" class="column250FixCentText">
                    <input type="radio" name="radioPregunta1Val" id="radioPregunta1SVal" value="S" onclick="mostrarliPregunta2()"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta1Val" id="radioPregunta1NVal" value="N" onclick="mostrarliPregunta2()"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta2" style="vertical-align: middle;">
                <td id="pregunta2Texto" class="column400Fix">
                    <div id="grouptextoPreg2">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,codigoLabelPregunta2)%>
                    </div>
                </td>
                <td id="pregunta2" class="column250FixCentText">
                    <div id="groupradioPreg2">
                    <input type="radio" name="radioPregunta2" id="radioPregunta2S" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta2" id="radioPregunta2N" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                    </div>
                </td>
                <td id="pregunta2Val" class="column250FixCentText">
                    <div id="groupradioPreg2Val">
                    <input type="radio" name="radioPregunta2Val" id="radioPregunta2SVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta2Val" id="radioPregunta2NVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                    </div>
                </td>
            </tr>
            
            <tr id="liPregunta3" style="vertical-align: middle;<%=ocultoAntes2017%>">
                <td id="pregunta3Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3")%>
                </td>
                <td id="pregunta3" class="column250FixCentText" style="text-align: left;">
                    <div id="entiEspecAntes2017" >
                        <input id="codPregunta3" name="codPregunta3" type="text" class="inputTexto" size="2" maxlength="8" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descPregunta3" name="descPregunta3" type="text" class="inputTexto" size="30" readonly>
                        <a id="anchorPregunta3" name="anchorPregunta3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPregunta3" name="botonPregunta3" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </td>
                <td id="pregunta3Val" class="column250FixCentText" style="text-align: left;">
                    <input id="codPregunta3Val" name="codPregunta3Val" type="text" class="inputTexto" size="2" maxlength="8" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                    <input id="descPregunta3Val" name="descPregunta3Val" type="text" class="inputTexto" size="30" readonly>
                    <a id="anchorPregunta3Val" name="anchorPregunta3Val" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPregunta3Val" name="botonPregunta3Val" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                </td>
            </tr>
            <tr style="<%=ocultoAntes2017%>">
                <td class="column400Fix">
                </td>
                <td id="campoLibrePregunta3" class="column250FixCentText" style="text-align: left;">
                    <input id="textoLibrePregunta3" name="textoLibrePregunta3" type="text" class="inputTexto" size="39" maxlength="100">
                </td>
                <td id="campoLibrePregunta3Val" class="column250FixCentText" style="text-align: left;">
                    <input id="textoLibrePregunta3Val" name="textoLibrePregunta3Val" type="text" class="inputTexto" size="39" maxlength="100">
                </td>
            </tr>
            <% if(ejercicio>=2017){ %>
            <tr id="liPregunta31" style="vertical-align: middle;">
                <td id="pregunta31Texto" class="column400Fix" >
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3.persDiscapacid")%>
                </td>
                <td id="pregunta31" class="column250FixCentText">
                    <input type="radio" name="radioTipopersDiscapacid" id="radioTipopersDiscapacidS" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipopersDiscapacid" id="radioTipopersDiscapacidN" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta31Val" class="column250FixCentText">
                    <input type="radio" name="radioTipopersDiscapacidVal" id="radioTipopersDiscapacidSVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipopersDiscapacidVal" id="radioTipopersDiscapacidNVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta32" style="vertical-align: middle;">
                <td id="pregunta32Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3.riesgoExclusio")%>
                </td>
                <td id="pregunta32" class="column250FixCentText">
                    <input type="radio" name="radioTiporiesgoExclusio" id="radioTiporiesgoExclusioS" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTiporiesgoExclusio" id="radioTiporiesgoExclusioN" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta32Val" class="column250FixCentText">
                    <input type="radio" name="radioTiporiesgoExclusioVal" id="radioTiporiesgoExclusioSVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTiporiesgoExclusioVal" id="radioTiporiesgoExclusioNVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta33" style="vertical-align: middle;">
                <td id="pregunta33Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3.formaProfesion")%>
                </td>
                <td id="pregunta33" class="column250FixCentText">
                    <input type="radio" name="radioTipoformaProfesion" id="radioTipoformaProfesionS" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipoformaProfesion" id="radioTipoformaProfesionN" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta33Val" class="column250FixCentText">
                    <input type="radio" name="radioTipoformaProfesionVal" id="radioTipoformaProfesionSVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipoformaProfesionVal" id="radioTipoformaProfesionNVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta34" style="vertical-align: middle;">
                <td id="pregunta34Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3.orgSindiEmpres")%>
                </td>
                <td id="pregunta34" class="column250FixCentText">
                    <input type="radio" name="radioTipoorgSindiEmpres" id="radioTipoorgSindiEmpresS" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipoorgSindiEmpres" id="radioTipoorgSindiEmpresN" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta34Val" class="column250FixCentText">
                    <input type="radio" name="radioTipoorgSindiEmpresVal" id="radioTipoorgSindiEmpresSVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTipoorgSindiEmpresVal" id="radioTipoorgSindiEmpresNVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta35" style="vertical-align: middle;">
                <td id="pregunta35Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta3.sinAnimoLucro")%>
                </td>
                <td id="pregunta35" class="column250FixCentText">
                    <input type="radio" name="radioTiposinAnimoLucro" id="radioTiposinAnimoLucroS" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTiposinAnimoLucro" id="radioTiposinAnimoLucroN" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta35Val" class="column250FixCentText">
                    <input type="radio" name="radioTiposinAnimoLucroVal" id="radioTiposinAnimoLucroSVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioTiposinAnimoLucroVal" id="radioTiposinAnimoLucroNVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <% }%>
            <tr id="liPregunta4" style="vertical-align: middle;">
                <td id="pregunta4Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,codigoLabelPregunta4)%>
                </td>
                <td id="pregunta4" class="column250FixCentText">
                    <input type="radio" name="radioPregunta4" id="radioPregunta4S" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta4" id="radioPregunta4N" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
                <td id="pregunta4Val" class="column250FixCentText">
                    <input type="radio" name="radioPregunta4Val" id="radioPregunta4SVal" value="S"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.si")%>
                    <input type="radio" name="radioPregunta4Val" id="radioPregunta4NVal" value="N"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "comun.label.no")%>
                </td>
            </tr>
            <tr id="liPregunta5" style="vertical-align: middle; display: none;">
                <td id="pregunta5Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta5")%>
                </td>
                <td id="pregunta5" class="column250FixCentText">
                    <input id="textoLibrePregunta5" name="textoLibrePregunta5" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
                <td id="pregunta5Val" class="column250FixCentText">
                    <input id="textoLibrePregunta5Val" name="textoLibrePregunta5Val" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
            </tr>
            <tr id="liPregunta6" style="vertical-align: middle;display:none;">
                <td id="pregunta6Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta6")%>
                </td>
                <td id="pregunta6" class="column250FixCentText">
                    <input id="textoLibrePregunta6" name="textoLibrePregunta6" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
                <td id="pregunta6Val" class="column250FixCentText">
                    <input id="textoLibrePregunta6Val" name="textoLibrePregunta6Val" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
            </tr>
            <tr id="liPregunta7" style="vertical-align: middle;">
                <td id="pregunta7Texto" class="column400Fix">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.pregunta7")%>
                </td>
                <td id="pregunta7" class="column250FixCentText">
                    <input id="textoLibrePregunta7" name="textoLibrePregunta7" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
                <td id="pregunta7Val" class="column250FixCentText">
                    <input id="textoLibrePregunta7Val" name="textoLibrePregunta7Val" type="text" class="inputTexto" size="5" maxlength="8" onchange="reemplazarPuntos(this);">
                </td>
            </tr>   
        </table>
    </div>
    <br />
    <div class="botonera">
        <input type="button" id="btnCopiarOtrosDatosCE" name="btnCopiarOtrosDatosCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.btn.copiarDatosCE")%>" onclick="copiarOtrosDatosCE();">
    </div>
</body>

<script type="text/javascript">   
    var codEspecialidades = [<%=lcodEspecialidades%>];
    var descEspecialidades = [<%=ldescEspecialidades%>];
    
    var comboEspecialidades = new Combo("Pregunta3");
    comboEspecialidades.change = mostrarTextoLibrePregunta3;
    
    comboEspecialidades.addItems(codEspecialidades, descEspecialidades);
    
    var comboEspecialidadesVal = new Combo("Pregunta3Val");
    comboEspecialidadesVal.change = mostrarTextoLibrePregunta3Val;
    
    comboEspecialidadesVal.addItems(codEspecialidades, descEspecialidades);
    
    mostrarPreguntas();
    mostrarPreguntasVal();
    mostrarTextoLibrePregunta3();
    mostrarTextoLibrePregunta3Val();
    mostrarliPregunta2();
</script>

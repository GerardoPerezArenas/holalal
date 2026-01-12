<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.CerCertificadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<div>
        <%
            EspecialidadesVO datModif = new EspecialidadesVO();
            EspecialidadesVO objectVO = new EspecialidadesVO();
            List<EspecialidadesVO> listaEspSol = new ArrayList<EspecialidadesVO>();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();
            
            String codCertificado = "";
            String codigo = "";

            int idiomaUsuario = 1;
            try
            {
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

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                numExpediente    = request.getParameter("numero");
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (EspecialidadesVO)request.getAttribute("datModif");
                }
                if(request.getAttribute("listaEspSol") != null){
                    listaEspSol  = (List<EspecialidadesVO>)request.getAttribute("listaEspSol");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

         <script type="text/javascript">

            var mensajeValidacion = '';

            var comboListaCertificados;
            var listaCodigosCertificados = new Array();
            var listaDescripcionesCertificados = new Array();
            
            var comboListaMotivos;
            var listaCodigosMotivos = new Array();
            var listaDescripcionesMotivos = new Array();
            
            var comboPresencial;
            var comboTeleformacion;
            var comboAcreditado;
            
            
            var listaSiNo = new Array();
            var listaCodigosSiNo = new Array();
            listaCodigosSiNo[0] = 0;
            listaCodigosSiNo[1] = 1;
            listaCodigosSiNo[2] = 3;
            listaSiNo[0] = '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.si")%>';
            listaSiNo[1] = '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.no")%>';
            listaSiNo[2] = '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.desistido")%>';
            
            function rellenardatEspModificar(){
                if('<%=datModif%>' != null){
                    buscaCodigoCertificado('<%=datModif.getCodCP() != null ? datModif.getCodCP() : ""%>');  
                    buscaCodPresencial('<%=datModif.getInscripcionPresencial() != null ? datModif.getInscripcionPresencial() : ""%>'); 
                    buscaCodTeleformacion('<%=datModif.getInscripcionTeleformacion() != null ? datModif.getInscripcionTeleformacion() : ""%>');  
                    buscaCodAcreditado('<%=datModif.getAcreditacion() != null ? datModif.getAcreditacion() : ""%>');  
                    buscaCodigoMotivo('<%=datModif.getMotDeneg() != null ? datModif.getMotDeneg() : ""%>');  
                }
                else
                    alert('No hemos podido cargar los datos para modificar');
            }
            
            
            
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
                
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";

                    if(nuevo != null && nuevo == "1"){
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=crearEspecialidad&tipo=0&numero=<%=numExpediente%>"
                                +"&codigocp="+document.getElementById('codListaCertificados').value
                                +"&denominacion="+ escape(document.getElementById('descListaCertificados').value)
                                +"&presencial="+ document.getElementById('codPresencial').value
                                +"&teleformacion="+ document.getElementById('codTeleformacion').value
                                +"&acreditacion="+ document.getElementById('codAcreditado').value
                                +"&motDeneg="+ document.getElementById('codListaMotivos').value;
                        
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=modificarEspecialidad&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&codigocp="+document.getElementById('codListaCertificados').value
                                +"&denominacion="+ escape(document.getElementById('descListaCertificados').value)
                                +"&presencial="+ document.getElementById('codPresencial').value
                                +"&teleformacion="+ document.getElementById('codTeleformacion').value
                                +"&acreditacion="+ document.getElementById('codAcreditado').value
                                +"&motDeneg="+ document.getElementById('codListaMotivos').value;
                    }
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
                        var listaEspecialidades = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaEspecialidades[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                        if(hijosFila[cont].nodeName=="ID"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[0] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_CODCP"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_DENOM"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_PRESE"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[3] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_TELEF"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[4] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_ACRED"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[5] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ESP_MOT_DENEG"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[6] = '-';
                                            }
                                        }
                                }
                                listaEspecialidades[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  listaEspecialidades;
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }else{
                    jsp_alerta("A", mensajeValidacion);
                }
            }
  
            function validarNumerico(numero){
                try{
                    if (Trim(numero.value)!='') {
                        return /^([0-9])*$/.test(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
            }
            
            function validarNumericoDecimal(numero){
                try{
                    if(Trim(numero.value) != ''){
                        return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
            }
            
            function cancelar(){
                var resultado = 1; //jsp_alerta('','<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=="Microsoft Internet Explorer") { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                }else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                }else{
                     window.close(); 
                } 
            }
        
            function reemplazarPuntos(campo){
                try{
                    var valor = campo.value;
                    if(valor != null && valor != ''){
                        valor = valor.replace(/\./g,',');
                        campo.value = valor;
                    }
                }
                catch(err){
                }
            }
            
            function comprobarCaracteresEspeciales(texto){
                //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
                var iChars = "&'<>|^\\%";   // / quitado para carga automatica de trayectorias 30052014
                for (var i = 0; i < texto.length; i++) {
                    if (iChars.indexOf(texto.charAt(i)) != -1) {
                        return false;
                    }
                }
                return true;
            }
            
            function buscaCodigoCertificado (codCertificado){
            comboListaCertificados.buscaCodigo(codCertificado);
            }
            
            function buscaCodigoMotivo (codigo){
            comboListaMotivos.buscaCodigo(codigo);
            }
            
            function buscaCodPresencial (codPresencial){
            comboPresencial.buscaCodigo(codPresencial);
            }
            
            function buscaCodTeleformacion (codTeleformacion){
            comboTeleformacion.buscaCodigo(codTeleformacion);
            }
            
            function buscaCodAcreditado (codAcreditado){
            comboAcreditado.buscaCodigo(codAcreditado);
            }
            
            function cargarDatosCertificado(){ 
                var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
                buscaCodigoCertificado(codCertificadoSeleccionado);
            }
            
            function cargarDatosMotivo(){ 
                var codMotivoSeleccionado = document.getElementById("codListaMotivos").value;
                buscaCodigoMotivo(codMotivoSeleccionado);
            }
            
            function presencial(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codPresencial") != null){
                    var codigo = document.getElementById("codPresencial").value;
                    buscaCodPresencial(codigo);
                }
            }//teleformacion
            
            function teleformacion(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codTeleformacion") != null){
                    var codigo = document.getElementById("codTeleformacion").value;
                    buscaCodTeleformacion(codigo);
                }
            }//teleformacion
            
            function acreditado(){
               //Recuperamos el valor seleccionado en el combo.
               if(document.getElementById("codAcreditado") != null){
                   var codigo = document.getElementById("codAcreditado").value;
                   if(codigo=="1"){
                       document.getElementById("divMotDeneg").style.visibility = "visible";
                   }else{
                       document.getElementById("divMotDeneg").style.visibility = "hidden";
                       document.getElementById("codListaMotivos").value="";
                   }
                   buscaCodAcreditado(codigo);
               }
            }//acreditado
            
            function validarDatos(){
                mensajeValidacion = "";
                var correcto = true;
                var codigocp = document.getElementById('codListaCertificados').value;
                if(codigocp == null || codigocp == ''){
                    mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "especialidades.msg.todosCamposOblig")%>';
                    return false;
                }
                
                var denominacion = document.getElementById('descListaCertificados').value;
                if(denominacion == null || denominacion == ''){
                    mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "especialidades.msg.todosCamposOblig")%>';
                    return false;
                }
                
               return correcto;
            }
            
            function compruebacodesp(codigocp){
                var codcplista;
                <%
                    if (listaEspSol!= null && listaEspSol.size() >0){
                        for (int indice=0;indice<listaEspSol.size();indice++)
                        {
                            objectVO = listaEspSol.get(indice);
                %>           
                        codcplista = '<%=objectVO.getCodCP()%>';
                        if(codcplista!=null && codcplista==codigocp){
                            if(nuevo != null && nuevo == "0"){
                                var datModif = '<%=datModif != null && datModif.getCodCP()!= null ? datModif.getCodCP() : 0 %>';
                                if(datModif==codigocp)
                                    return true;
                            }
                            return false;
                        }
                <%
                        }// for
                    }// if
                %>
                return true;
            }
            
    </script>

<div class="contenidoPantalla">
        <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.nuevaEspecialidadSol")%>
                    </span>
                </div>

                <div class="lineaFormulario">
                    <div class="etiqueta" style="width: 190px; float: left;" >
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"especialidades.label.descEspecialidad")%>
                    </div>
                    <br>
                    <div>
                        <div class="etiqueta" >
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.certificado")%>
                        </div>
                        <div>
                            <input type="text" name="codListaCertificados" id="codListaCertificados" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaCertificados"  id="descListaCertificados" size="90" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaCertificados" name="anchorListaCertificados">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                     name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>
                 <br>
                <div class="lineaFormulario" style="display: none;">
                    <div class="etiqueta" style="width: 190px; float: left; font-weight: bold;" >
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.inscripcion")%>
                    </div>
                    <br>
                    <div style="width: 450px; float: left;">
                        <div class="etiqueta" style="width: 290px; float: left;">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.presencial")%>
                        </div>
                        <div>
                            <input type="text" name="codPresencial" id="codPresencial" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descPresencial"  id="descPresencial" size="10" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorPresencial" name="anchorPresencial">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonPresencial"
                                    name="botonPresencial" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                    <br>                
                    <div style="width: 450px; float: left;">
                        <div class="etiqueta"  style="width: 290px; float: left;">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.teleformacion")%>
                        </div>
                        <div>
                            <input type="text" name="codTeleformacion" id="codTeleformacion" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descTeleformacion"  id="descTeleformacion" size="10" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorTeleformacion" name="anchorTeleformacion">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTeleformacion"
                                    name="botonTeleformacion" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" >
                    <div style="width: 450px; float: left;">
                        <div class="etiqueta"  style="width: 290px; float: left;">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.acreditado")%>
                        </div>
                        <div>
                            <input type="text" name="codAcreditado" id="codAcreditado" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descAcreditado"  id="descAcreditado" size="10" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorAcreditado" name="anchorAcreditado">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAcreditado"
                                    name="botonAcreditado" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>
                <br><br>
                
                <div class="lineaFormulario" id="divMotDeneg" style="visibility:hidden">
                    <div>
                        <div class="etiqueta" >
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidades.motivos_denegacion")%>
                        </div>
                        <div>
                            <input type="text" name="codListaMotivos" id="codListaMotivos" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaMotivos"  id="descListaMotivos" size="90" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaMotivos" name="anchorListaMotivos">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion1"
                                     name="botonAplicacion1" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>
   
                                     
                <br><br>                     
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </div>
    
    <script type="text/javascript">

        listaCodigosCertificados[0] = "";
        listaDescripcionesCertificados[0] = "";

        /* Lista con los certificados recuperados de la BBDD */
        var contador = 0;
        <logic:iterate id="certificados" name="listaCertificados" scope="request">
        listaCodigosCertificados[contador] = ['<bean:write name="certificados" property="codCertificado" />'];
        listaDescripcionesCertificados[contador] = ['<bean:write name="certificados" property="descCertificadoC" />'];
        contador++;
        </logic:iterate>
        
        var comboListaCertificados = new Combo("ListaCertificados");
        comboListaCertificados.addItems(listaCodigosCertificados, listaDescripcionesCertificados);
        comboListaCertificados.change = cargarDatosCertificado;

        var comboPresencial = new Combo("Presencial");
        comboPresencial.addItems(listaCodigosSiNo, listaSiNo);
        comboPresencial.change = presencial;

        var comboTeleformacion = new Combo("Teleformacion");
        comboTeleformacion.addItems(listaCodigosSiNo, listaSiNo);
        comboTeleformacion.change = teleformacion;

        var comboAcreditado = new Combo("Acreditado");
        comboAcreditado.addItems(listaCodigosSiNo, listaSiNo);
        comboAcreditado.change = acreditado;
        
        
        

        listaCodigosMotivos[0] = "";
        listaDescripcionesMotivos[0] = "";

        /* Lista con los certificados recuperados de la BBDD */
        var contadorM = 0;
        <logic:iterate id="motivosDeneg" name="listaMotivos" scope="request">
        listaCodigosMotivos[contadorM] = ['<bean:write name="motivosDeneg" property="codigo" />'];
        listaDescripcionesMotivos[contadorM] = ['<bean:write name="motivosDeneg" property="descripcion" />'];
        contadorM++;
        </logic:iterate>
        
        var comboListaMotivos = new Combo("ListaMotivos");
        comboListaMotivos.addItems(listaCodigosMotivos, listaDescripcionesMotivos);
        comboListaMotivos.change = cargarDatosMotivo;
        
        var nuevo = "<%=nuevo%>";
            if(nuevo==0){
                rellenardatEspModificar();
            }
    </script>
    
</div>
   
    
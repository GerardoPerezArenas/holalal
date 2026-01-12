<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>


<div>
    <%
        RegistroErrorVO datModif = new RegistroErrorVO();
        RegistroErrorVO objectVO = new RegistroErrorVO();

        String nuevo = "";
        String fechaError = "";
        String fechaRevision = "";
        
        MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
        
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

            nuevo = (String)request.getAttribute("nuevo");

            if(request.getAttribute("datModif") != null)
            {
                datModif = (RegistroErrorVO)request.getAttribute("datModif");
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                fechaError = datModif.getFechaError()!=null ? datModif.getFechaError().substring(0,10):"";
                fechaRevision = datModif.getFechaRevisionError()!=null ? datModif.getFechaRevisionError().substring(0,10) : "";
            }
        }
        catch(Exception ex)
        {
            %>
            alert("Tenemos una exception");
            <%
        }
    %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
    
    <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/JavaScriptUtil.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/Parsers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/InputMask.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide53/lanbide.js"></script>
    <!-- Eventos onKeyPress compatibilidad firefox  -->
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>
   
    <script type="text/javascript">

        var mensajeValidacion = '';
        var nuevo = '<%=nuevo%>';
        
        function  cargarDatosCheck(){
            var notificadoE = '<%=datModif.getErrorNotificado()!=null ? datModif.getErrorNotificado() : ""%>';
            var revisadoE = '<%=datModif.getErrorRevisado()!=null ? datModif.getErrorRevisado() : ""%>';
            if(notificadoE!=null && notificadoE=='<%=ConstantesMeLanbide53.STANDFOR_SI_S%>'){
                document.getElementById("ck_notificado").checked=true;
            }else{
                document.getElementById("ck_notificado").checked=false;
            }
            if(revisadoE!=null && revisadoE=='<%=ConstantesMeLanbide53.STANDFOR_SI_S%>'){
                document.getElementById("ck_revisado").checked=true;
            }else{
                document.getElementById("ck_revisado").checked=false;
            }
        }
     
        function getXMLHttpRequest() {
            var aVersions = ["MSXML2.XMLHttp.5.0",
                "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                "MSXML2.XMLHttp", "Microsoft.XMLHttp"
            ];

            if (window.XMLHttpRequest) {
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                for (var i = 0; i < aVersions.length; i++) {
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    } catch (error) {
                        //no necesitamos hacer nada especial
                    }
                }
            } else {
                return null;
            }
        }
        
        function prepararFiltroChecked(idElement){
            if(document.getElementById(idElement)!=null && document.getElementById(idElement).checked){
                return '<%=ConstantesMeLanbide53.BOOLEAN_TRUE%>'; 
            }else 
                return "";
        }

        function guardarDatos() {

            if (validarDatos()) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var nuevo = "<%=nuevo%>";

                if (nuevo != null && nuevo == "1") {
                    parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=crearNuevaGestionError&tipo=0"  
                            + "&meLanbide53FechaError=" + document.getElementById('meLanbide53FechaError').value
                            + "&mensaje_error=" + escape(document.getElementById('mensaje_error').value)
                            + "&causa_error=" + escape(document.getElementById('causa_error').value)
                            + "&mensaje_exception=" + escape(document.getElementById('mensaje_exception').value)
                            + "&traza=" + escape(document.getElementById('traza').value)
                            + "&idprocedimiento=" + document.getElementById('idprocedimiento').value
                            + "&identificador_error=" + document.getElementById('identificador_error').value
                            + "&clave=" + document.getElementById('clave').value
                            + "&sistema_origen=" + document.getElementById('sistema_origen').value
                            + "&ubicacion_error=" + document.getElementById('ubicacion_error').value
                            + "&fichero_log=" + document.getElementById('fichero_log').value
                            + "&evento=" + document.getElementById('evento').value
                            + "&numero_expediente=" + document.getElementById('numero_expediente').value
                            + "&ck_notificado=" + prepararFiltroChecked('ck_notificado')
                            + "&ck_revisado=" + prepararFiltroChecked('ck_revisado')
                            + "&meLanbide53FechaRevision=" + document.getElementById('meLanbide53FechaRevision').value
                            + "&observaciones_revision=" + escape(document.getElementById('observaciones_revision').value)
                   ;

                }
                else {
                    parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=modificarGestionError&tipo=0"
                            + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
//                            + "&meLanbide53FechaError=" + document.getElementById('meLanbide53FechaError').value
//                            + "&mensaje_error=" + escape(document.getElementById('mensaje_error').value)
//                            + "&causa_error=" + escape(document.getElementById('causa_error').value)
//                            + "&mensaje_exception=" + escape(document.getElementById('mensaje_exception').value)
//                            + "&traza=" + escape(document.getElementById('traza').value)
//                            + "&idprocedimiento=" + document.getElementById('idprocedimiento').value
//                            + "&identificador_error=" + document.getElementById('identificador_error').value
//                            + "&clave=" + document.getElementById('clave').value
//                            + "&sistema_origen=" + document.getElementById('sistema_origen').value
//                            + "&ubicacion_error=" + document.getElementById('ubicacion_error').value
//                            + "&fichero_log=" + document.getElementById('fichero_log').value
//                            + "&evento=" + document.getElementById('evento').value
//                            + "&numero_expediente=" + document.getElementById('numero_expediente').value
//                            + "&ck_notificado=" + prepararFiltroChecked('ck_notificado')
                            + "&ck_revisado=" + prepararFiltroChecked('ck_revisado')
                            + "&meLanbide53FechaRevision=" + document.getElementById('meLanbide53FechaRevision').value
                            + "&observaciones_revision=" + escape(document.getElementById('observaciones_revision').value)
                    ;
                }
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    //var formData = new FormData(document.getElementById('formContrato'));
                    ajax.send(parametros);
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var lista = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            lista[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "REG_ERR_ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[0] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_FEC_ERROR") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[1] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_MEN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[2] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_EXCEP_MEN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[3] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_CAUSA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[4] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_TRAZA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[5] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_ID_PROC") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[6] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_IDEN_ERR_ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[7] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_ID_CLAVE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[8] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_NOT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[9] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_REV") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[10] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_FEC_REV") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[11] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_OBS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[12] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_SIS_ORIG") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[13] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_SITU") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[14] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_LOG") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[15] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "REG_ERR_EVEN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[16] = '-';
                                        }
                                    }else if (hijosFila[cont].nodeName == "REG_ERR_ID_FLEXIA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[17] = '-';
                                        }
                                    }
                            }// for elementos de la fila
                            lista[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        //jsp_alerta("A",'Correcto');
                        window.returnValue = lista;
                        cerrarVentana();
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } catch (Err) {
                }//try-catch
            } else {
                jsp_alerta("A", mensajeValidacion);
            }
        }

        function validarNumerico(numero) {
            try {
                if (Trim(numero.value) != '') {
                    return /^([0-9])*$/.test(numero.value);
                }
            }
            catch (err) {
                return false;
            }
        }

        function validarNumericoDecimal(numero) {
            try {
                if (Trim(numero.value) != '') {
                    return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
                }
            }
            catch (err) {
                return false;
            }
        }

        function cancelar() {
            var resultado = jsp_alerta('','<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
            if (resultado == 1) {
                cerrarVentana();
            }
        }

        function cerrarVentana() {
            if (navigator.appName == "Microsoft Internet Explorer") {
                window.parent.window.opener = null;
                window.parent.window.close();
            } else if (navigator.appName == "Netscape") {
                top.window.opener = top;
                top.window.open('', '_parent', '');
                top.window.close();
            } else {
                window.close();
            }
        }

        function reemplazarPuntos(campo) {
            try {
                var valor = campo.value;
                if (valor != null && valor != '') {
                    valor = valor.replace(/\./g, ',');
                    campo.value = valor;
                }
            }
            catch (err) {
            }
        }

        function comprobarCaracteresEspeciales(texto) {
            //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
            var iChars = "&'<>|^\\%";   
            for (var i = 0; i < texto.length; i++) {
                if (iChars.indexOf(texto.charAt(i)) != -1) {
                    return false;
                }
            }
            return true;
        }

        
        function validarDatos() {
            mensajeValidacion = "";
            var correcto = true;
            var checkRevisado = document.getElementById('ck_revisado').checked;
            if(checkRevisado){
               var fechaRevision = document.getElementById('meLanbide53FechaRevision').value;
                if (fechaRevision == null || fechaRevision == '') 
                {
                    mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.fechaRevisionOblig")%>';
                    return false;
                } 
            }else{
                mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.checkRevisadoOblig")%>';
                return false;
            }
            var observaciones = document.getElementById('observaciones_revision').value;
            if (observaciones != null && observaciones != '') {
                if(!compruebaTamanoCampo(document.getElementById('observaciones_revision'),4000)){
                         mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>' + ' 4000 Caracteres.';
                         return false;
                    }
            }
        return correcto;
        }
        
        function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value)!='') {
                  var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                  if (!D[0]){
                    jsp_alerta("A","<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                    document.getElementById(inputFecha.name).focus();
                    document.getElementById(inputFecha.name).select();
                    return false;
                  } else {
                    inputFecha.value = D[1];
                    return true;
                  }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide
            
        //Funcion para el calendario de fecha error
        function mostrarCalFechaError(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide53FechaError").src.indexOf("icono.gif") != -1 )
                //showCalendar('forms[0]','meLanbide53FechaError',null,null,null,'','calMeLanbide53FechaError','',null,null,null,null,null,null,null, null,evento);        
                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"msg.campo.readonly")%>');
        }//mostrarCalFechaError
        //Funcion para el calendario de fecha revision
        function mostrarCalFechaRevision(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide53FechaRevision").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide53FechaRevision',null,null,null,'','calMeLanbide53FechaRevision','',null,null,null,null,null,null,null, null,evento);        
        }//mostrarCalFechaRevision

        function compruebaTamanoCampo(elemento, maxTex){
            var texto = elemento.value;
            if(texto.length>maxTex){
                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                elemento.focus();
                return false;
            }
            return true;
        }
            
    </script>

    <div class="contenidoPantalla">
        <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.mantenimientoErorres")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53FechaError")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly type="text" class="inputTxtFecha" 
                                   id="meLanbide53FechaError" name="meLanbide53FechaError"
                                   maxlength="10"  size="10"
                                   value="<%=fechaError%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;"  onClick="mostrarCalFechaError(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG  style="border: 0px solid none" height="17" id="calMeLanbide53FechaError" name="calMeLanbide53FechaError" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                </div>  
                <div style="clear: both;"></div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.mensaje.error")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <div style="width: 450px; float: left;">
                                <div style="float: left;">
                                    <textarea readonly class="inputTextoM53" rows="2" cols="65" id="mensaje_error" name="mensaje_error" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getMensajeError() != null ? datModif.getMensajeError()  : ""%></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="clear: both;"></div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.causa.exception")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <div style="width: 450px; float: left;">
                                <div style="float: left;">
                                    <textarea readonly class="inputTextoM53" rows="2" cols="65" id="causa_error" name="causa_error" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getCausaException() != null ? datModif.getCausaException()  : ""%></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="clear: both;"></div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.mensaje.exception")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea readonly class="inputTextoM53" rows="2" cols="65" id="mensaje_exception" name="mensaje_exception" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getMensajeException() != null ? datModif.getMensajeException()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="clear: both;"></div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.mensaje.traza")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea readonly class="inputTextoM53" rows="2" cols="65" id="traza" name="traza" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getTrazaError() != null ? datModif.getTrazaError()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div style="clear: both;"></div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.idprocedimiento")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="idprocedimiento" name="idprocedimiento" type="text" class="inputTextoM53" size="30" maxlength="25" 
                            value="<%=datModif != null && datModif.getIdProcedimiento() != null ? datModif.getIdProcedimiento() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.id.fk")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="identificador_error" name="identificador_error" type="text" class="inputTextoM53" size="30" maxlength="25" 
                            value="<%=datModif != null && datModif.getIdErrorFK() != null ? datModif.getIdErrorFK() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.clave")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="clave" name="clave" type="text" class="inputTextoM53" size="30" maxlength="32" 
                            value="<%=datModif != null && datModif.getClave() != null ? datModif.getClave() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.sistema.origen")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="sistema_origen" name="sistema_origen" type="text" class="inputTextoM53" size="30" maxlength="32" 
                            value="<%=datModif != null && datModif.getSistemaOrigen() != null ? datModif.getSistemaOrigen() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.ubicacion")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="ubicacion_error" name="ubicacion_error" type="text" class="inputTextoM53" size="100" maxlength="100" 
                            value="<%=datModif != null && datModif.getUbicacionError() != null ? datModif.getUbicacionError() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br> 
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.log")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left; " >
                            <input readonly  id="fichero_log" name="fichero_log" type="text" class="inputTextoM53" size="30" maxlength="32" 
                                  value="<%=datModif != null && datModif.getFicheroLog() != null ? datModif.getFicheroLog() : ""%>"/>
                        </div>
                    </div>
                </div> 
                <br><br>
                <div class="lineaFormulario">
                    <div class="etiqueta" style="width: 190px; float: left;">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.evento")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="evento" name="evento" type="text" class="inputTextoM53" size="50" maxlength="100" 
                                   value="<%=datModif != null && datModif.getEvento() != null ? datModif.getEvento() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.numexpediente")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input readonly id="numero_expediente" name="numero_expediente" type="text" class="inputTextoM53" size="50" maxlength="100" 
                                   value="<%=datModif != null && datModif.getEvento() != null ? datModif.getEvento() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin: 2px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                                <%--<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.horasalida")%>--%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.notificado")%></label>
                                <input disabled="true" id="ck_notificado" name="ck_notificado" type="checkbox" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.notificado")%>">
                            </div>
                        </div>
                    </div>
                    <br><br>
                <fieldset style="border: 1px darkgrey solid;">
                    <legend><label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.area.actualizacion.error")%></label></legend>
                    <div class="lineaFormulario" style="margin: 2px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                                <%--<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.horasalida")%>--%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.revisado")%></label>
                                <input id="ck_revisado" name="ck_revisado" type="checkbox" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.revisado")%>">
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario" style="margin: 2px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.fecharevision")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="meLanbide53FechaRevision" name="meLanbide53FechaRevision"
                                       maxlength="10"  size="10"
                                       value="<%=fechaRevision%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaRevision(event);return false;" style="text-decoration:none;">   
                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide53FechaRevision" name="calMeLanbide53FechaRevision" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                    <br>
                    <div class="lineaFormulario" style="margin: 2px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.observaciones")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <div style="width: 450px; float: left;">
                                    <div style="float: left;">
                                        <textarea class="inputTextoM53" rows="5" cols="80" id="observaciones_revision" name="observaciones_revision" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getObservacionesError() != null ? datModif.getObservacionesError()  : ""%></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="clear: both;"></div>
                    <br>
                </fieldset>
                <br><br>
                <div class="lineaFormulario">
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                    </div>
                </div>
            </div>
                <div id="reloj" style="font-size:20px;"></div>
        </form>
        <script type="text/javascript">
        
        cargarDatosCheck();
        
        function startTime(){            
            today=new Date();
            h=today.getHours();
            m=today.getMinutes();
            s=today.getSeconds();
            h=checkTime(h);
            m=checkTime(m);
            s=checkTime(s);
            //document.getElementById('reloj').innerHTML=h+":"+m+":"+s;
            //document.getElementById('hora_entrada').value=h+":"+m+":"+s;
            //t=setTimeout('startTime()',500);
             //clearTimeout();
        }
        function checkTime(i){
            if (i<10) {i="0" + i;}return i;
        }
        //window.onload=function(){
            //startTime();
        //}
        
        function formatearHora(control,evento){  
            evento = (evento) ? evento : window.event;  
            var charCode = (evento.which) ? evento.which : evento.keyCode;  
            
            var ignore = evento.altKey || evento.ctrlKey || inArray(charCode, JST_IGNORED_KEY_CODES);
            if (!ignore) {
                var range = getInputSelectionRange(control);
                if (range != null && range[0] != range[1]) {
                    replaceSelection(this, "");
                }
            }
            
            if (!ignore) {  //charCode >= 48 && charCode <= 56 && control.value.length < 8
                var i = control.value.length;  
                var texto = "";  
                if (i == 2 || i == 5) {
                    texto = control.value+":";  
                    control.value = texto;  
                }  
                return true;  
            }  
            return false;  
        }
        
        function comprobarMaskaraHora(control){
            if(control!=null){
                if(control.value==""){
                    return true;
                }
                var texto = control.value.split(':');
                if(texto.length!=3){
                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                    control.focus();
                     control.select();
                    return false;
                }
                if(!(texto[0]>=0 && texto[0]<=24)){
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        control.focus();
                         control.select();
                        return false;
                }else{
                    if(texto[0].length<2){
                        texto[0]= '0'+texto[0];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                }
                if(!(texto[1]>=0 && texto[1]<=59) || !(texto[2]>=0 && texto[2]<=59)){
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        control.focus();
                         control.select();
                        return false;
                }else{
                    if(texto[1].length<2){
                        texto[1]= '0'+texto[1];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                    if(texto[2].length<2){
                        texto[2]= '0'+texto[2];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                     
                }
            }
            else{
                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
            }
        }

        var errorMessage = "<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%> : ${value} . El Formato Debe ser:  ${mask}";

    </script>
    <div id="popupcalendar" class="text"></div>        
    </div>
</div>
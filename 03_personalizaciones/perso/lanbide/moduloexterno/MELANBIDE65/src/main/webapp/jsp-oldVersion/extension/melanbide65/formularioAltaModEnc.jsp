<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>


<div>
    <%
        EncargadoVO objEncMod = null;
        String procesoC = "";
        String expediente = "";
        String identEncarg="";
        
        MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
        
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

            expediente = (String)request.getAttribute("numExpediente");
            if(request.getAttribute("datosEncargado") != null){
                objEncMod = (EncargadoVO)request.getAttribute("datosEncargado");
                identEncarg = (String)request.getAttribute("identificador");
                procesoC = "mod";
            } else procesoC = "alta";
        }
        catch(Exception ex)
        {
        }
    %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide65/melanbide65.css"/>
    
    <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/JavaScriptUtil.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/Parsers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/InputMask.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/lanbide.js"></script>
    
   
    <script type="text/javascript">
       var mensajeValidacionC = '';
       var procesoC ='<%=procesoC%>';
        
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

        function guardarDatosEncargado() {

            if (validarDatosEncargado()) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros;
                if(procesoC=='alta'){
                    parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=darAltaEncargado&tipo=0";
                } else {
                    parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=modificarEncargado&tipo=0&identificador=<%=identEncarg%>";
                }
                parametros += "&expediente=<%=expediente%>"
                            + "&numLinea=" + document.getElementById('numLineaAnexoC').value
                            + "&nombre=" + escape(document.getElementById('nombreAnexoC').value)
                            + "&apellidos=" + escape(document.getElementById('apellidosAnexoC').value)
                            + "&sexo=" + document.getElementById('valorSexoAnexoC').value
                            + "&dni=" + escape(document.getElementById('dniAnexoC').value)
                            + "&fecha=" + document.getElementById('fecha').value
                            + "&jornadaCompleta=" + document.getElementById('jornadaCompletaAnexoC').value
                            + "&jornadaParcialPor=" + document.getElementById('jornadaParcialPorAnexoC').value
                            + "&jornadaParcialPag=" + document.getElementById('jornadaParcialPagAnexoC').value;
                    
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
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
                    for (var j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            lista[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "SEXO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DNI") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECHAALTA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADACOMPLETA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPOR") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[8].toString();
                                        tex = tex.replace(".", ",");
                                        fila[8] = tex;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[9] = '-';
                                    }
                                } 
                            }// for elementos de la fila
                            lista[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    
                    if (codigoOperacion == "0") {
                        window.returnValue = lista;
                        cerrarVentana();
                    } else  if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
                    } else if (codigoOperacion == "3" || codigoOperacion == "5") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                    } else if (codigoOperacion == "6") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                    } else if (codigoOperacion == "7") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                    } else if (codigoOperacion == "-1") {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            } else {
                jsp_alerta("A", mensajeValidacionC);
            }
        }

        function cancelar() {
            var resultado = jsp_alerta('','<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

        function validarDatosEncargado() {
            mensajeValidacionC = "";
            var correcto = true;
            
            var valor = document.getElementById('numLineaAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numLineaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('nombreAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('apellidosAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.apellidosObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('valorSexoAnexoC').value;
            if (valor == '0') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.sexoObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('dniAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('fecha').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.fechaAltaObligatorio")%>';
                return false;
            } 
            
            valor = document.getElementById('jornadaCompletaAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.jornadaCompletaOblig")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('jornadaParcialPorAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.jornadaParcialPorOblig")%>';
                return false;
            } else{
                if(!validarNumericoDecimal(valor)){
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porJorNumericoDecimal")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('jornadaParcialPagAnexoC').value;
            if (valor == null || valor == '') {
                mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.jornadaParcialPagOblig")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            return correcto;
        }
        
        function validarNumerico(numero){
            try{
                if (Trim(numero)!='') {
                    return /^([0-9])*$/.test(numero);
                }
            }
            catch(err){
                return false;
            }
        }

        function validarNumericoDecimal(numero){
            try{
                if(Trim(numero) != ''){
                    return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero);
                }
            }
            catch(err){
                return false;
            }
        }
        
    function mostrarCalFechaAB(evento) { 
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFecha").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fecha',null,null,null,'','calFecha','',null,null,null,null,null,null,null, null,evento);  

    } 
    
    function seleccionarSexoEncargado(value){
        document.getElementById('valorSexoAnexoC').value = value;
    }
    </script>

    <div class="contenidoPantalla">
        <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span><%= procesoC.equals("alta") ? meLanbide65I18n.getMensaje(idiomaUsuario,"label.altaEncargado") : 
                            meLanbide65I18n.getMensaje(idiomaUsuario,"label.modEncargado")%></span>
                </div>                        
                <br><br>   
                <div style="margin-left: 10px">
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="numLineaAnexoC" name="numLineaAnexoC" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objEncMod != null  ? objEncMod.getNumLinea() : ""%>" />
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="nombreAnexoC" name="nombreAnexoC" type="text" class="inputTexto" size="30" maxlength="80"
                                       value="<%=objEncMod != null && objEncMod.getNombre() != null ? objEncMod.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="apellidosAnexoC" name="apellidosAnexoC" type="text" class="inputTexto" size="60" maxlength="100"
                                       value="<%=objEncMod != null && objEncMod.getApellidos() != null ? objEncMod.getApellidos() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <label class="etiqueta"><input name="sexoAnexoC" type="radio" value="1" onclick="seleccionarSexoEncargado(this.value)"
                                                               <%=(objEncMod != null && objEncMod.getSexo()==1)? " checked" : ""%>/> Varón</label>
                                <label class="etiqueta"><input name="sexoAnexoC" type="radio" value="2" onclick="seleccionarSexoEncargado(this.value)"
                                                               <%=(objEncMod != null && objEncMod.getSexo()==2)? " checked" : ""%>/> Mujer</label>
                                <input type="hidden" name="valorSexoAnexoC" id="valorSexoAnexoC" value="<%=objEncMod != null ? String.valueOf(objEncMod.getSexo()) : "0"%>">
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.dni")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="dniAnexoC" name="dniAnexoC" type="text" class="inputTexto" size="30" maxlength="25" 
                                       value="<%=objEncMod != null && objEncMod.getDni() != null ? objEncMod.getDni() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.fechaAlta")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fecha" name="fecha"
                                       maxlength="10"  size="10"
                                       value="<%=objEncMod != null && objEncMod.getFecAltaAsStr()!= null ? objEncMod.getFecAltaAsStr(): ""%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaAB(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFecha" name="calFecha" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.jornadaLaboralCompleta")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="jornadaCompletaAnexoC" name="jornadaCompletaAnexoC" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objEncMod != null ? objEncMod.getJornadaCompleta(): ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.jornadaLaboralParcialPor")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="jornadaParcialPorAnexoC" name="jornadaParcialPorAnexoC" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objEncMod != null ? String.valueOf(objEncMod.getJornadaParcialPor()).replace(".",",") : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.jornadaLaboralParcialPag")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="jornadaParcialPagAnexoC" name="jornadaParcialPagAnexoC" type="text" class="inputTexto" size="15" maxlength="5"
                                    value="<%=objEncMod != null ? objEncMod.getJornadaParcialPag(): ""%>"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="margin-top: 100px">
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnAceptarAnexoC" name="btnAceptarAnexoC" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosEncargado();"/>
                        <input type="button" id="btnCancelarAnexoC" name="btnCancelarAnexoC" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                    </div>
                </div>
            </div>
        </form>
    <div id="popupcalendar" class="text"></div>        
    </div>

   
    
</div>
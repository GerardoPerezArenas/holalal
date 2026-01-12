<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TrabajadorVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


<div>
    <%
        TrabajadorVO objTrMod = null;
        String procesoA = "";
        String expediente = "";
        String identTr="";
        
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
            if(request.getAttribute("datosRegistro") != null){
                objTrMod = (TrabajadorVO)request.getAttribute("datosRegistro");
                identTr = (String)request.getAttribute("identificador");
                procesoA = "mod";
            } else procesoA = "alta";
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
       var mensajeValidacionA = '';
       var procesoA ='<%=procesoA%>';
        
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

        function guardarDatosTrabajador() {

            if (validarDatosTrabajador()) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros;
                if(procesoA=='alta'){
                    parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=darAltaTrabajador&tipo=0";
                } else {
                    parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=modificarTrabajador&tipo=0&identificador=<%=identTr%>";
                }
                parametros += "&expediente=<%=expediente%>"
                            + "&numLinea=" + document.getElementById('numLineaAnexoA').value
                            + "&nombre=" + escape(document.getElementById('nombreAnexoA').value)
                            + "&apellidos=" + escape(document.getElementById('apellidosAnexoA').value)
                            + "&sexo=" + document.getElementById('valorSexoAnexoA').value
                            + "&dni=" + escape(document.getElementById('dniAnexoA').value)
                            + "&discPsiquica=" + document.getElementById('discPsiquicaAnexoA').value
                            + "&discFisica=" + document.getElementById('discFisicaAnexoA').value
                            + "&contrIndefCompl=" + document.getElementById('contrIndefComplAnexoA').value
                            + "&contrIndefParcial=" + document.getElementById('contrIndefParcialAnexoA').value
                            + "&contrIndefParcialPag=" + document.getElementById('contrIndefParcialPagAnexoA').value
                            + "&contrTempCompl=" + document.getElementById('contrTempComplAnexoA').value
                            + "&contrTempParcial=" + document.getElementById('contrTempParcialAnexoA').value
                            + "&contrTempParcialPag=" + document.getElementById('contrTempParcialPagAnexoA').value;
                    
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
                                } else if (hijosFila[cont].nodeName == "DISCPSIQUICA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DISCFISICA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFCOMPL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFPARCIAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[9].toString();
                                        tex = tex.replace(".", ",");
                                        fila[9] = tex;
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRINDEFPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPCOMPL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPPARCIAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[12].toString();
                                        tex = tex.replace(".", ",");
                                        fila[12] = tex;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRTEMPPARCIALPAG") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
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
                jsp_alerta("A", mensajeValidacionA);
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

        function validarDatosTrabajador() {
            mensajeValidacionA = "";
            var correcto = true;
            
            var valor = document.getElementById('numLineaAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numLineaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('nombreAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('apellidosAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.apellidosObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('valorSexoAnexoA').value;
            if (valor == '0') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.sexoObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('dniAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                return false;
            }
            
            valor = document.getElementById('discPsiquicaAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.discPsiquicaOblig")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('discFisicaAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.discFisicaOblig")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrIndefComplAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contIndefComp")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrIndefParcialAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contIndefJornada")%>';
                return false;
            } else{
                if(!validarNumericoDecimal(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porJorNumericoDecimal")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrIndefParcialPagAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contIndefJornadaNPag")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrTempComplAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contTempComp")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrTempParcialAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contTempJornada")%>';
                return false;
            } else{
                if(!validarNumericoDecimal(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porJorNumericoDecimal")%>';
                    return false;
                }
            }
            
            valor = document.getElementById('contrTempParcialPagAnexoA').value;
            if (valor == null || valor == '') {
                mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.contTempJornadaNPag")%>';
                return false;
            } else{
                if(!validarNumerico(valor)){
                    mensajeValidacionA = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.numPaginaNumerico")%>';
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
    
    function seleccionarSexoTrabajador(value){
        document.getElementById('valorSexoAnexoA').value = value;
    }
    </script>

    <div class="contenidoPantalla">
        <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span><%= procesoA.equals("alta") ? meLanbide65I18n.getMensaje(idiomaUsuario,"label.altaTrabajador") : 
                            meLanbide65I18n.getMensaje(idiomaUsuario,"label.modTrabajador")%></span>
                </div>                        
                <br><br>   
                <div style="margin-left: 10px">
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="numLineaAnexoA" name="numLineaAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objTrMod != null  ? objTrMod.getNumLinea() : ""%>" />
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
                                <input id="nombreAnexoA" name="nombreAnexoA" type="text" class="inputTexto" size="30" maxlength="80"
                                       value="<%=objTrMod != null && objTrMod.getNombre() != null ? objTrMod.getNombre() : ""%>"/>
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
                                <input id="apellidosAnexoA" name="apellidosAnexoA" type="text" class="inputTexto" size="60" maxlength="100"
                                       value="<%=objTrMod != null && objTrMod.getApellidos() != null ? objTrMod.getApellidos() : ""%>"/>
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
                                <label class="etiqueta"><input name="sexoAnexoA" type="radio" value="1" onclick="seleccionarSexoTrabajador(this.value)"
                                                               <%=(objTrMod != null && objTrMod.getSexo()==1)? " checked" : ""%>/> Varón</label>
                                <label class="etiqueta"><input name="sexoAnexoA" type="radio" value="2" onclick="seleccionarSexoTrabajador(this.value)"
                                                               <%=(objTrMod != null && objTrMod.getSexo()==2)? " checked" : ""%>/> Mujer</label>
                                <input type="hidden" name="valorSexoAnexoA" id="valorSexoAnexoA" value="<%=objTrMod != null ? String.valueOf(objTrMod.getSexo()) : "0"%>">
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
                                <input id="dniAnexoA" name="dniAnexoA" type="text" class="inputTexto" size="30" maxlength="25" 
                                       value="<%=objTrMod != null && objTrMod.getDni() != null ? objTrMod.getDni() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tipoYGradoDisPsiquica")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="discPsiquicaAnexoA" name="discPsiquicaAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objTrMod != null  ? objTrMod.getTipoDiscPsiquica() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tipoYGradoDisFisica")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="discFisicaAnexoA" name="discFisicaAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objTrMod != null ? objTrMod.getTipoDiscFisica() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoIndefinido")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrIndefComplAnexoA" name="contrIndefCompAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objTrMod != null ? objTrMod.getDurContrIndefComp() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoIndefinidoJornada")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrIndefParcialAnexoA" name="contrIndefParcialAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                    value="<%=objTrMod != null ? String.valueOf(objTrMod.getDurContrIndefParcialJ()).replace(".",",") : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoIndefinidoJornadaNPag")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrIndefParcialPagAnexoA" name="contrIndefParcialPagAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                    value="<%=objTrMod != null ? objTrMod.getDurContrIndefParcialP() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoTemporal")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrTempComplAnexoA" name="contrTempCompAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=objTrMod != null ? objTrMod.getDurContrTempComp() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoTemporalJornada")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrTempParcialAnexoA" name="contrTempParcialAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                    value="<%=objTrMod != null ? String.valueOf(objTrMod.getDurContrTempParcialJ()).replace(".",",") : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.durContratoTemporalJornadaNPag")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input id="contrTempParcialPagAnexoA" name="contrTempParcialPagAnexoA" type="text" class="inputTexto" size="15" maxlength="5"
                                    value="<%=objTrMod != null ? objTrMod.getDurContrTempParcialP() : ""%>"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="margin-top: 100px">
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnAceptarAnexoA" name="btnAceptarAnexoA" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosTrabajador();"/>
                        <input type="button" id="btnCancelarAnexoA" name="btnCancelarAnexoA" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                    </div>
                </div>
            </div>
        </form>        
    </div>

   
    
</div>
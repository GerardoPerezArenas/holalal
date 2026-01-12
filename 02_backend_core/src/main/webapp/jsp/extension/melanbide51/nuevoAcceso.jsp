<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.i18n.MeLanbide51I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConstantesMeLanbide51" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>


<div>
    <%
        ControlAccesoVO datModif = new ControlAccesoVO();
        ControlAccesoVO objectVO = new ControlAccesoVO();

        String codOrganizacion = "";
        String codMotivoVisita = "";
        String nuevo = "";
        String fecha = "";
        String fechaIV="";
        String fechaFV="";

        
        MeLanbide51I18n meLanbide51I18n = MeLanbide51I18n.getInstance();
        //ConstantesMeLanbide51 _constantesMeLanbide51 = ConstantesMeLanbide51();

        int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
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
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
                    }
                }
            }
            catch(Exception ex)
            {

            }

            codOrganizacion  = request.getParameter("codOrganizacionModulo");
            nuevo = (String)request.getAttribute("nuevo");

            if(request.getAttribute("datModif") != null)
            {
                datModif = (ControlAccesoVO)request.getAttribute("datModif");
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                fecha = formatoFecha.format(datModif.getFecha());
                if (datModif.getFechaIV()!=null){
                fechaIV = formatoFecha.format(datModif.getFechaIV());
                }
                if (datModif.getFechaFV()!=null){
                fechaFV = formatoFecha.format(datModif.getFechaFV());
                }
            }
        }
        catch(Exception ex)
        {
        }
    %>
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide51/melanbide51.css"/>
    
    <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/JavaScriptUtil.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/Parsers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/InputMask.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/lanbide.js"></script>
    <!-- Eventos onKeyPress compatibilidad firefox  -->
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>
   
    <script type="text/javascript">

       var mensajeValidacion = '';

       var comboListaMotivoVisita;
       var listaCodigosMotivoVisita = new Array();
       var listaDescripcionesMotivoVisita = new Array();
   
      
       function rellenardatModificar() {
            if ('<%=datModif%>' != null) {
                buscaCodigoMotivoVisita('<%=datModif.getCod_mot_visita() != null ? datModif.getCod_mot_visita() : ""%>');
            }
            else
                alert('No hemos podido cargar los datos para modificar');
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

        function guardarDatos() {

            if (validarDatos()) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var nuevo = "<%=nuevo%>";

                if (nuevo != null && nuevo == "1") {
                    parametros = "tarea=preparar&modulo=MELANBIDE51&operacion=crearNuevoAcceso&tipo=0"  
                            + "&fecha=" + document.getElementById('meLanbide51Fecha').value
                            + "&notarjeta=" + document.getElementById('notarjeta').value
                            + "&dni_nif=" + document.getElementById('dni_nif').value
                            + "&nombre=" + document.getElementById('nombre').value
                            + "&apellido1=" + document.getElementById('apellido1').value
                            + "&apellido2=" + document.getElementById('apellido2').value
                            + "&telefono=" + document.getElementById('telefono').value
                            + "&empresa_entidad=" + document.getElementById('empresa_entidad').value
                            + "&serv_visitado=" + document.getElementById('serv_visitado').value
                            + "&pers_contacto=" + document.getElementById('pers_contacto').value
                            + "&motiv_visita=" + document.getElementById('codListaMotivoVisita').value
                            + "&fechaIV=" + document.getElementById('fecInicioVigencia').value
                            + "&fechaFV=" + document.getElementById('fecFinVigencia').value
                            + "&hora_entrada=" + document.getElementById('hora_entrada').value
                            + "&hora_salida=" + document.getElementById('hora_salida').value
                            + "&observaciones=" + escape(document.getElementById('observaciones').value)
                    ;

                }
                else {
                    parametros = "tarea=preparar&modulo=MELANBIDE51&operacion=modificarAcceso&tipo=0"
                            + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                            + "&fecha=" + document.getElementById('meLanbide51Fecha').value
                            + "&notarjeta=" + document.getElementById('notarjeta').value
                            + "&dni_nif=" + document.getElementById('dni_nif').value
                            + "&nombre=" + document.getElementById('nombre').value
                            + "&apellido1=" + document.getElementById('apellido1').value
                            + "&apellido2=" + document.getElementById('apellido2').value
                            + "&telefono=" + document.getElementById('telefono').value
                            + "&empresa_entidad=" + document.getElementById('empresa_entidad').value
                            + "&serv_visitado=" + document.getElementById('serv_visitado').value
                            + "&pers_contacto=" + document.getElementById('pers_contacto').value
                            + "&motiv_visita=" + document.getElementById('codListaMotivoVisita').value
                            + "&fechaIV=" + document.getElementById('fecInicioVigencia').value
                            + "&fechaFV=" + document.getElementById('fecFinVigencia').value
                            + "&hora_entrada=" + document.getElementById('hora_entrada').value
                            + "&hora_salida=" + document.getElementById('hora_salida').value
                            + "&observaciones=" + escape(document.getElementById('observaciones').value)
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
                                if (hijosFila[cont].nodeName == "ID") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[0] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[1] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_NUMTAR") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[2] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_DNI_CIF") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[3] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_NOMBRE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[4] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_APE1") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[5] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_APE2") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[6] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_TEL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[7] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_EMPENT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[8] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_SERVVIS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[9] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_PERSCONT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[10] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_MOTVIS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[11] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHAIV") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[12] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHAFV") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[13] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_HORENT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[14] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_HORSAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[15] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_OBSER") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[16] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "DESC_MOTVIS") {
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
                        self.parent.opener.retornoXanelaAuxiliar( lista);
                        cerrarVentana();
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
            var resultado = jsp_alerta('','<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

        function buscaCodigoMotivoVisita(codMotivoVisita) {
            comboListaMotivoVisita.buscaCodigo(codMotivoVisita);
        }

        function cargarDatosMotivoVisita() {
            var codMotivoVisitaSeleccionado = document.getElementById("codListaMotivoVisita").value;
            buscaCodigoMotivoVisita(codMotivoVisitaSeleccionado);
        }

        function validarDatos() {
            mensajeValidacion = "";
            var correcto = true;
            //var codigoMotivoVisita = document.getElementById('codListaMotivoVisita').value;
            //if (codigoMotivoVisita == null || codigoMotivoVisita == '') {
            //    mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.MotivoVisitaObligatorio")%>';
            //    return false;
            //}
            
            var fecha = document.getElementById('meLanbide51Fecha').value;
            if (fecha == null || fecha == '') {
                mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                return false;
            }

            var notarjeta = document.getElementById('notarjeta').value;
            if (notarjeta == null || notarjeta == '') {
                mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.numeroTarjetaObligatorio")%>';
                return false;
            }
            
            var dni_nif = document.getElementById('dni_nif').value;
            if (dni_nif == null || dni_nif == '') {
                mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                return false;
            }
            
            var nombre = document.getElementById('nombre').value;
            if (nombre == null || nombre == '') {
                mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                return false;
            }
            var horaentrada = document.getElementById('hora_entrada').value;
            if (horaentrada == null || horaentrada == '') {
                mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaentradaObligatorio")%>';
                return false;
            }
            
            var observaciones = document.getElementById('observaciones').value;
            if (observaciones != null && observaciones != '') {
                if(!compruebaTamanoCampo(document.getElementById('observaciones'),1000)){
                         mensajeValidacion = '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>' + ' 1000 Caracteres.';
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
                    jsp_alerta("A","<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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
            
        //Funcion para el calendario de fecha 
        function mostrarCalFecha(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide51Fecha").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide51Fecha',null,null,null,'','calMeLanbide51Fecha','',null,null,null,null,null,null,null, null,evento);  
            
        }//mostrarCalFechaAdqui
        
        function mostrarCalFechaIVigen(evento) { 
           if(window.event) 
               evento = window.event;
           if (document.getElementById("calFecInicioVigencia").src.indexOf("icono.gif") != -1 )
              showCalendar('forms[0]','fecInicioVigencia',null,null,null,'','calFecInicioVigencia','',null,null,null,null,null,null,null, null,evento);  
           
       }
       function mostrarCalFechaFVigen(evento) { 
           if(window.event) 
               evento = window.event;
           if (document.getElementById("calFecFinVigencia").src.indexOf("icono.gif") != -1 )
              showCalendar('forms[0]','fecFinVigencia',null,null,null,'','calFecFinVigencia','',null,null,null,null,null,null,null, null,evento);  
           
       }

        function compruebaTamanoCampo(elemento, maxTex){
            var texto = elemento.value;
            if(texto.length>maxTex){
                jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                elemento.focus();
                return false;
            }
            return true;
        }
        
        function obtenerUID(){
            //alert("Hola");
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                    parametros = "tarea=preparar&modulo=MELANBIDE51&operacion=leerSmartCard&tipo=0"  
                    ;

                try {
                   // alert("estoy en el try")
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
                   // alert("Si falla sale Errorea");
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                 
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var uidTarjeta = null;                
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "CODIGO_UID") {
                            uidTarjeta = hijos[j].childNodes[0].nodeValue;
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        //jsp_alerta("A",uidTarjeta);
                        //alert("Exito");
                        document.getElementById("notarjeta").value = uidTarjeta;
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorNoCardUID")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorLectorUID")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } catch (Err) {
                //alert("Errorea");
                }//try-catch
        }
        
        function comprobarCampoBusq(dni_nif){
            dni_nif=document.getElementById('dni_nif').value;
            if (dni_nif !== null && dni_nif !== "" ){
                cargarDatosVisita();
            }
        }
        
         function cargarDatosVisita(){
        
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                    parametros = "tarea=preparar&modulo=MELANBIDE51&operacion=leerDatosVisitaDNI&tipo=0"  
                     + "&dni_nif=" + document.getElementById('dni_nif').value
                    ;

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
                   // alert("Si falla sale Errorea");
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                 
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var existeVisita = null;
                    var nombreVisita = null;
                    var ape1Visita = null;
                    var ape2Visita = null;
                    var tfnoVisita = null;
                    var empEntVisita = null;
                   
                    
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION") 
                        else if (hijos[j].nodeName == "EXISTE_VISITA"){
                            existeVisita = hijos[j].childNodes[0].nodeValue;//bool
                        }
                         else if (hijos[j].nodeName == "M51CA_NOMBRE") {
                            if(hijos[j].childNodes.length>0 && hijos[j].childNodes[0].nodeValue!="null"){
                            nombreVisita = hijos[j].childNodes[0].nodeValue;
                            }
                            else
                               nombreVisita=""; 
                        
                        }
                        else if (hijos[j].nodeName == "M51CA_APE1") {
                            if(hijos[j].childNodes.length>0 && hijos[j].childNodes[0].nodeValue!="null"){
                                ape1Visita = hijos[j].childNodes[0].nodeValue;
                            }
                            else
                               ape1Visita="";
                            
                        }
                        else if (hijos[j].nodeName == "M51CA_APE2") {
                            if(hijos[j].childNodes.length>0 && hijos[j].childNodes[0].nodeValue!="null"){
                                ape2Visita = hijos[j].childNodes[0].nodeValue;
                            }
                            else
                               ape2Visita="";
                            
                        }
                        else if (hijos[j].nodeName == "M51CA_TEL") {
                            if(hijos[j].childNodes.length>0 && hijos[j].childNodes[0].nodeValue!="null"){
                                tfnoVisita = hijos[j].childNodes[0].nodeValue;
                            }
                            else
                               tfnoVisita="";
                           
                        }
                        else if (hijos[j].nodeName == "M51CA_EMPENT") {
                            if(hijos[j].childNodes.length>0 && hijos[j].childNodes[0].nodeValue!="null"){
                                empEntVisita = hijos[j].childNodes[0].nodeValue;
                            }
                            else
                               empEntVisita="";
                            
                        }
                        
                        
                        
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        if(existeVisita== "1"){
                            document.getElementById("nombre").value=nombreVisita;
                            document.getElementById("apellido1").value=ape1Visita;
                            document.getElementById("apellido2").value=ape2Visita;
                            document.getElementById("telefono").value=tfnoVisita;
                            document.getElementById("empresa_entidad").value=empEntVisita; 
                               if (document.getElementById("dni_nif").value !== null && document.getElementById("dni_nif").value !==""){
                                
                                     document.getElementById("serv_visitado").focus();
                                }
                        } 
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorAcceso")%>');
                    }else {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } catch (Err) {
                //alert("Errorea");
                }//try-catch
        }
            
    </script>

    <div class="contenidoPantalla">
        <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.meLanbide51Fecha")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input type="text" class="inputTxtFecha" 
                                   id="meLanbide51Fecha" name="meLanbide51Fecha"
                                   maxlength="10"  size="10"
                                   value="<%=fecha%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecha(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG style="border: 0px solid none" height="17" id="calMeLanbide51Fecha" name="calMeLanbide51Fecha" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                </div>                            
                <br><br>   
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.notarjeta")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="notarjeta" name="notarjeta" type="text" class="inputTexto" size="30" maxlength="30" 
                                   value="<%=datModif != null && datModif.getNo_tarjeta() != null ? datModif.getNo_tarjeta() : ""%>"/>                           
                             <input type="button" id="btnObtenerUID" name="btnObtenerUID" class="botonGeneral" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.obtenerUID")%>" onclick="obtenerUID();"/> 
                        </div>
                    </div>  
                </div>        
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.dni_nif")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="dni_nif" name="dni_nif" type="text" class="inputTexto" size="15" maxlength="15" 
                                   onblur="comprobarCampoBusq();"
                            value="<%=datModif != null && datModif.getNif_Dni() != null ? datModif.getNif_Dni() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="nombre" name="nombre" type="text" class="inputTexto" size="30" maxlength="50" 
                            value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="apellido1" name="apellido1" type="text" class="inputTexto" size="30" maxlength="50" 
                            value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="apellido2" name="apellido2" type="text" class="inputTexto" size="30" maxlength="50" 
                            value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.telefono")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="telefono" name="telefono" type="text" class="inputTexto" size="15" maxlength="15" 
                            value="<%=datModif != null && datModif.getTelefono() != null ? datModif.getTelefono() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.empresa_entidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="empresa_entidad" name="empresa_entidad" type="text" class="inputTexto" size="50" maxlength="150" 
                            value="<%=datModif != null && datModif.getEmpresa_entidad() != null ? datModif.getEmpresa_entidad() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.serv_visitado")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="serv_visitado" name="serv_visitado" type="text" class="inputTexto" size="50" maxlength="150" 
                            value="<%=datModif != null && datModif.getServicio_visitado() != null ? datModif.getServicio_visitado() : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br> 
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.pers_contacto")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="pers_contacto" name="pers_contacto" type="text" class="inputTexto" size="50" maxlength="150" 
                                  value="<%=datModif != null && datModif.getPersona_contacto() != null ? datModif.getPersona_contacto() : ""%>"/>
                        
                        </div>
                    </div>
                </div> 
                <br><br>
                <div class="lineaFormulario">
                    <div class="etiqueta" style="width: 190px; float: left;">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.motivovisita")%>
                    </div>
                    <div>
                        <div>
                            <input type="text" name="codListaMotivoVisita" id="codListaMotivoVisita" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaMotivoVisita"  id="descListaMotivoVisita" size="60" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaMotivoVisita" name="anchorListaMotivoVisita">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                     name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </div>
                    </div>
                </div>
                <br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;text-align: left;" class="etiqueta" >
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.fechaInicioVigencia")%>
                    </div>
                    <div style="float: left;">
                            <input type="text" class="inputTxtFecha" 
                            id="fecInicioVigencia" name="fecInicioVigencia"
                            maxlength="10"  size="10"
                            value="<%=fechaIV%>"
                            onkeyup = "return SoloCaracteresFechaLanbide(this);"
                            onblur = "javascript:return comprobarFechaLanbide(this);"
                            onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIVigen(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG style="border: 0px solid none" height="17" id="calFecInicioVigencia" name="calFecInicioVigencia" border="0" 
                                src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                    </div>
                </div>  <!--class lineaformulario   -->       
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;text-align: left;" class="etiqueta" >
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.fechaFinVigencia")%>
                    </div>
                    <div style="float: left;">
                        <input type="text" class="inputTxtFecha" 
                        id="fecFinVigencia" name="fecFinVigencia"
                        maxlength="10"  size="10"
                        value="<%=fechaFV%>"
                        onkeyup = "return SoloCaracteresFechaLanbide(this);"
                        onblur = "javascript:return comprobarFechaLanbide(this);"
                        onfocus="javascript:this.select();"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFVigen(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                            <IMG style="border: 0px solid none" height="17" id="calFecFinVigencia" name="calFecFinVigencia" border="0" 
                            src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                        </A>
                    </div>
                </div> <!--class lineaformulario   -->      
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.horaentrada")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="hora_entrada" name="hora_entrada" type="text" class="inputTexto" size="10" maxlength="8" 
                            value="<%=datModif != null && datModif.getHora_entrada() != null ? datModif.getHora_entrada().toString() : ""%>"
                            onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.horasalida")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="hora_salida" name="hora_salida" type="text" class="inputTexto" size="10" maxlength="8"   
                            value="<%=datModif != null && datModif.getHora_salida() != null ? datModif.getHora_salida().toString() : ""%>"
                            onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)" />
                            <!--<input id="date4" name="date4">-->
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.observaciones")%>
                    </div>
                    <div style="float: left; width: 75%">
                        <div style="width: 450px; float: left;">
                            <textarea rows="6" cols="75" id="observaciones" name="observaciones" maxlength="1000" onblur="compruebaTamanoCampo(this,1000)"><%=datModif != null && datModif.getObservaciones() != null ? datModif.getObservaciones() : ""%></textarea>
                        </div>
                    </div>
                </div>
                <br><br/>
                <div class="lineaFormulario">
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                    </div>
                </div>
            </div>
                <div id="reloj" style="font-size:20px;"></div>
        </form>
        <script type="text/javascript">

        listaCodigosMotivoVisita[0] = "";
        listaDescripcionesMotivoVisita[0] = "";

        /* Lista con los Vlaores de desplegable Motivos  Visita recuperados En BBDD */
        var contador = 0;
        
        <logic:iterate id="motivoVisita" name="listaMotivoVisita" scope="request">
        listaCodigosMotivoVisita[contador] = ['<bean:write name="motivoVisita" property="des_val_cod" />'];
        listaDescripcionesMotivoVisita[contador] = ['<bean:write name="motivoVisita" property="des_nom" />'];
        contador++;
        </logic:iterate>
            
        var comboListaMotivoVisita = new Combo("ListaMotivoVisita");
        comboListaMotivoVisita.addItems(listaCodigosMotivoVisita, listaDescripcionesMotivoVisita);
        comboListaMotivoVisita.change = cargarDatosMotivoVisita;


        var nuevo = "<%=nuevo%>";
        if (nuevo == 0) {
            rellenardatModificar();
        }
        //alert('<%=datModif != null && datModif.getPersona_contacto() != null ? datModif.getPersona_contacto() : " no coge nada"%>');

        function startTime(){            
            today=new Date();
            h=today.getHours();
            m=today.getMinutes();
            s=today.getSeconds();
            h=checkTime(h);
            m=checkTime(m);
            s=checkTime(s);
            //document.getElementById('reloj').innerHTML=h+":"+m+":"+s;
            document.getElementById('hora_entrada').value=h+":"+m+":"+s;
            //t=setTimeout('startTime()',500);
             //clearTimeout();
        }
        function checkTime(i){
            if (i<10) {i="0" + i;}return i;
        }
        //window.onload=function(){
       if('<%=nuevo%>' ==1)
            startTime();
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
                    jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                    control.focus();
                     control.select();
                    return false;
                }
                if(!(texto[0]>=0 && texto[0]<=24)){
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
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
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
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
                jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
            }
        }

        
        // Apartado de configuraicon para Formato de campo Hora de salia y Entrada
        
        //document.getElementById("hora_entrada").addEventListener('input',startTime,false);
        //document.getElementById("hora_salida").addEventListener('input',startTime,false);
        
        var errorMessage = "<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%> : ${value} . El Formato Debe ser:  ${mask}";
        /*
        var dateParser = new DateParser("HH:mm:ss");
        var dateMask = new DateMask(dateParser,document.getElementById("date4"));
        dateMask.validationMessage = errorMessage;
        */
        /*
        var dateParser3 = new DateParser("HH:mm:ss");
        var dateMask3 = new DateMask(dateParser3, document.getElementById("hora_entrada"));
        dateMask3.validationMessage = errorMessage;
        
        var dateParser4 = new DateParser("HH:mm:ss");
        var dateMask4 = new DateMask(dateParser4, document.getElementById("hora_salida"));
        dateMask4.validationMessage = errorMessage;
        */
        // Fin Apartado configuraci�n Formato campos Horas
        
        
         //---------- pruebas ------------------
        
                
        
    </script>
    <div id="popupcalendar" class="text"></div>        
    </div>

   
    
</div>

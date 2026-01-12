<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.i18n.MeLanbide84I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConstantesMeLanbide84" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mantenimiento CONTRATACIONES</title>
        <%
            PersonaVO datModif = new PersonaVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String expediente = "";        
            String fecNac = "";
            String fecInicio = "";
            String fecSolicitud = "";
        
            MeLanbide84I18n meLanbide84I18n = MeLanbide84I18n.getInstance();

            expediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        if (usuario != null) {
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
                if(request.getAttribute("datModif") != null) {
                    datModif = (PersonaVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");                                        
                    if (datModif.getFechaNacimiento()!=null){
                        fecNac = formatoFecha.format(datModif.getFechaNacimiento());
                    }
                    if (datModif.getFechaInicio()!=null){
                        fecInicio = formatoFecha.format(datModif.getFechaInicio());
                    }
                    if (datModif.getFechaSolicitud()!=null){
                        fecSolicitud = formatoFecha.format(datModif.getFechaSolicitud());
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
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide84/melanbide84.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide84/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide84/EntapUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';
            var numExp = "<%=expediente%>";
            var ejercicio = numExp.split("/")[0];

            //Desplegable nivel academico
            var comboListaNivel;
            var listaCodigosNivel = new Array();
            var listaDescripcionesNivel = new Array();
            function buscaCodigoNivel(nivel) {
                comboListaNivel.buscaCodigo(nivel);
            }
            function cargarDatosNivel() {
                var nivelSeleccionado = document.getElementById("codListaNivel").value;
                buscaCodigoNivel(nivelSeleccionado);
            }

            //Desplegable programa
            var comboListaPrograma;
            var listaCodigosPrograma = new Array();
            var listaDescripcionesPrograma = new Array();
            function buscaCodigoPrograma(programa) {
                if (ejercicio >= 2025) {
                    comboListaPrograma.buscaCodigo("APEAI");
                } else {
                    comboListaPrograma.buscaCodigo(programa);
                }
            }
            function cargarDatosProgramas() {
                var programaSeleccionado = document.getElementById("codListaPrograma").value;
                if (ejercicio >= 2025) {
                    buscaCodigoPrograma("APEAI");
                } else {
                    buscaCodigoPrograma(programaSeleccionado);
                }
            }

            // sexo
            var comboListaSexo;
            var listaCodigosSexo = new Array();
            var listaDescripcionesSexo = new Array();
            function buscaCodigoSexo(sexo) {
                comboListaSexo.buscaCodigo(sexo);
            }
            function cargarDatosSexo() {
                var sexoSeleccionado = document.getElementById("codListaSexo").value;
                buscaCodigoSexo(sexoSeleccionado);
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFecnac(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecnac").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecNac', null, null, null, '', 'calFecnac', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFecInicio(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecInicio").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecInicio', null, null, null, '', 'calFecInicio', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFecSolicitud(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecSolicitud").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecSolicitud', null, null, null, '', 'calFecSolicitud', '', null, null, null, null, null, null, null, null, evento);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoCargaXML');
                    var parametros = "";
                    var ape2 = "";

                    if (document.getElementById('ape2').value == null || document.getElementById('ape2').value == '') {
                        ape2 = "";
                    } else {
                        ape2 = document.getElementById('ape2').value.replace(/\'/g, "''");
                    }

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE84&operacion=crearNuevaPersona&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE84&operacion=modificarPersona&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&expediente=<%=expediente%>'
                            + "&dni=" + document.getElementById('dni').value
                            + '&nombre=' + document.getElementById('nombre').value.replace(/\'/g, "''")
                            + "&ape1=" + document.getElementById('ape1').value.replace(/\'/g, "''")
                            + "&ape2=" + ape2
                            + "&fecNac=" + document.getElementById('fecNac').value
                            + "&sexo=" + document.getElementById('codListaSexo').value
                            + "&nivel=" + document.getElementById('codListaNivel').value
                            + "&calle=" + document.getElementById('calle').value.replace(/\'/g, "''")
                            + "&numero=" + (document.getElementById('numero').value != null && document.getElementById('numero').value != "" ? document.getElementById('numero').value : "")
                            + "&letra=" + (document.getElementById('letra').value != null && document.getElementById('letra').value != "" ? document.getElementById('letra').value : "")
                            + "&planta=" + (document.getElementById('planta').value != null && document.getElementById('planta').value != "" ? document.getElementById('planta').value : "")
                            + "&puerta=" + (document.getElementById('puerta').value != null && document.getElementById('puerta').value != "" ? document.getElementById('puerta').value : "")
                            + "&codPost=" + document.getElementById('codPost').value
                            + "&localidad=" + document.getElementById('localidad').value
                            + '&programa=' + document.getElementById('codListaPrograma').value
                            + "&fecInicio=" + document.getElementById('fecInicio').value
                            + "&fecSolicitud=" + document.getElementById('fecSolicitud').value
                            ;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaModificacion,
                            error: mostrarErrorAltaModificacion
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoCargaXML');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";

                var campo = document.getElementById('dni');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                    return false;
                } else if (!validarNif(campo)) {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                    return false;
                }

                campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }
                campo = document.getElementById('ape1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.apel1")%>';
                    return false;
                }
                campo = document.getElementById('fecNac').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.fecNac")%>';
                    return false;
                }
                campo = document.getElementById('codListaSexo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                    return false;
                }
                campo = document.getElementById('codListaNivel').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.nivel")%>';
                    return false;
                }
                campo = document.getElementById('calle').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.calle")%>';
                    return false;
                }
                campo = document.getElementById('numero');
                if (campo != null && campo != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.numero.format")%>';
                        return false;
                    }
                }
                campo = document.getElementById('codPost');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.codPost")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.codPost.format")%>';
                    return false;
                }
                campo = document.getElementById('localidad').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.localidad")%>';
                    return false;
                }
                campo = document.getElementById('codListaPrograma').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.programa")%>';
                    return false;
                }
                campo = document.getElementById('fecInicio').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.fecInicio")%>';
                    return false;
                }
                campo = document.getElementById('fecSolicitud').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.fecSolicitud")%>';
                    return false;
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenardatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoNivel('<%=datModif.getNivelAcademico() != null ? datModif.getNivelAcademico() : ""%>');
                    buscaCodigoPrograma('<%=datModif.getPrograma() != null ? datModif.getPrograma() : ""%>');
                    buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoCargaXML');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                var direccion;
                if (codigoOperacion == "0") {
                    var personas = datos.tabla.lista;
                    if (personas.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = personas;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError(codigoOperacion);
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaModificacion() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarError(codigo);
            }

            function mostrarError(codigo) {
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoCargaXML');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoCargaXML" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">                            
                        </span>
                    </div>
                    <fieldset id="personales" name="personales">
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input id="dni" name="dni" type="text" class="inputTextoObligatorio" size="12" maxlength="9" onkeyup="xAMayusculas(this);"
                                       disabled value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>"/>                            
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="80" 
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                        <br><br>
                        <!--    apel1 apel2        -->
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input id="ape1" name="ape1" type="text" class="inputTextoObligatorio" size="30" maxlength="100""
                                       value="<%=datModif != null && datModif.getApel1() != null ? datModif.getApel1() : ""%>"/>
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input id="ape2" name="ape2" type="text" class="inputTexto" size="30" maxlength="100" "
                                       value="<%=datModif != null && datModif.getApel2() != null ? datModif.getApel2() : ""%>"/>
                            </div>
                        </div>
                        <br><br>                            
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.fecNac")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fecNac" name="fecNac"
                                       maxlength="10"  size="10"
                                       value="<%=fecNac%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecnac(event);
                                        return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFecnac" name="calFecnac" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                            </div>
                            <div style=" float: left; margin-left: 10px;">
                                <input type="text" name="codListaSexo" id="codListaSexo" size="6" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaSexo"  id="descListaSexo" size="40" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaSexo" name="anchorListaSexo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                        <br><br>                            
                        <div class="lineaFormulario">
                            <div class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.nivel")%>
                            </div>
                            <div style=" float: left; margin-left: 10px;">
                                <input type="text" name="codListaNivel" id="codListaNivel" size="6" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaNivel"  id="descListaNivel" size="40" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaNivel" name="anchorListaNivel">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="direccion" name="direccion">
                        <legend class="legendAzul"><%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.direccion")%></legend>
                        <!--    calle numero letra planta puerta codPost localidad        -->
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.calle")%>
                            </div>
                            <div style="width: 300px; float: left;  margin-left: 10px;">
                                <input id="calle" name="calle" type="text" class="inputTextoObligatorio" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getCalle() != null ? datModif.getCalle() : ""%>"/>
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.numero")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="numero" name="numero" type="text" class="inputTexto" size="6" maxlength="5" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getNumero() != null ? datModif.getNumero() : ""%>"/>
                            </div>  
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.letra")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="letra" name="letra" type="text" class="inputTexto" size="2" maxlength="1" 
                                       value="<%=datModif != null && datModif.getLetra() != null ? datModif.getLetra() : ""%>"/>
                            </div> 
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.planta")%>
                            </div>
                            <div style="width: 300px; margin-left: 10px;  float: left; text-align: left;">
                                <input id="planta" name="planta" type="text" class="inputTexto" size="6" maxlength="5" 
                                       value="<%=datModif != null && datModif.getPlanta() != null ? datModif.getPlanta() : ""%>"/>
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.puerta")%>
                            </div>
                            <div style="width: auto; margin-left: 10px; float: left; text-align: left;">
                                <input id="puerta" name="puerta" type="text" class="inputTexto" size="6" maxlength="4" 
                                       value="<%=datModif != null && datModif.getPuerta() != null ? datModif.getPuerta() : ""%>"/>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.codPost")%>
                            </div>
                            <div style="width: 300px;  margin-left: 10px;float: left;">
                                <input id="codPost" name="codPost" type="text" class="inputTextoObligatorio" size="6" maxlength="5" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getCodPost() != null ? datModif.getCodPost() : ""%>"/>
                            </div>
                            <div style="margin-left: 30px;" class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.localidad")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="localidad" name="localidad" type="text" class="inputTextoObligatorio" size="30" maxlength="30" 
                                       value="<%=datModif != null && datModif.getLocalidad() != null ? datModif.getLocalidad() : ""%>"/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="program" name="program">
                        <legend class="legendAzul"><%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.programa.field")%></legend>
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.programa")%>
                            </div>
                            <div style=" float: left; margin-left: 10px;">
                                <input type="text" name="codListaPrograma" id="codListaPrograma" size="6" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="4" />
                                <input type="text" name="descListaPrograma"  id="descListaPrograma" size="60" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaPrograma" name="anchorListaPrograma">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.fecInicio")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fecInicio" name="fecInicio"
                                       maxlength="10"  size="10"
                                       value="<%=fecInicio%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecInicio(event);
                                        return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFecInicio" name="calFecInicio" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  class="etiquetaEntap">
                                <%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.fecSolicitud")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fecSolicitud" name="fecSolicitud"
                                       maxlength="10"  size="10"
                                       value="<%=fecSolicitud%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecSolicitud(event);
                                        return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFecSolicitud" name="calFecSolicitud" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                    </fieldset>
                    <div class="lineaFormulario">
                        <div class="botonera" style="padding-top: 15px; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>

                </div>
            </form>
            <script type="text/javascript">
                listaCodigosNivel[0] = "";
                listaDescripcionesNivel[0] = "";
                contador = 0;

                <logic:iterate id="nivel" name="listaNivel" scope="request">
                listaCodigosNivel[contador] = ['<bean:write name="nivel" property="des_val_cod" />'];
                listaDescripcionesNivel[contador] = ['<bean:write name="nivel" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaNivel = new Combo("ListaNivel");
                comboListaNivel.addItems(listaCodigosNivel, listaDescripcionesNivel);
                comboListaNivel.change = cargarDatosNivel;

                /* PROGRAMA*/
                listaCodigosPrograma[0] = "";
                listaDescripcionesPrograma[0] = "";
                contador = 0;

                <logic:iterate id="programa" name="listaPrograma" scope="request">
                listaCodigosPrograma[contador] = ['<bean:write name="programa" property="des_val_cod" />'];
                listaDescripcionesPrograma[contador] = ['<bean:write name="programa" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaPrograma = new Combo("ListaPrograma");
                comboListaPrograma.addItems(listaCodigosPrograma, listaDescripcionesPrograma);
                comboListaPrograma.change = cargarDatosProgramas;

                /* SEXO */
                listaCodigosSexo[0] = "";
                listaDescripcionesSexo[0] = "";
                contador = 0;

                <logic:iterate id="sexo" name="listaSexo" scope="request">
                listaCodigosSexo[contador] = ['<bean:write name="sexo" property="des_val_cod" />'];
                listaDescripcionesSexo[contador] = ['<bean:write name="sexo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSexo = new Combo("ListaSexo");
                comboListaSexo.addItems(listaCodigosSexo, listaDescripcionesSexo);
                comboListaSexo.change = cargarDatosSexo;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "label.modifPersona")%>';
                    rellenardatModificar();
                } else {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "label.nuevaPersona")%>';
                    document.getElementById('dni').disabled = false;
                    if (ejercicio >= 2025) {
                        buscaCodigoPrograma("APEAI");
                        document.getElementById('codListaPrograma').disabled = true;
                        document.getElementById('anchorListaPrograma').disabled = true;
                    }
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

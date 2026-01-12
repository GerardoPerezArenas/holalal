<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.i18n.MeLanbide85I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConstantesMeLanbide85" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaContratacionVO datModif = new FilaContratacionVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            String fechanacimiento2 = "";
            String fechainicio2 = "";
            String fechainicio3 = "";
            String fechafin3 = "";
            
        
            MeLanbide85I18n meLanbide85I18n = MeLanbide85I18n.getInstance();

            numExpediente = (String)request.getAttribute("numExp");
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

             //   codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (FilaContratacionVO)request.getAttribute("datModif");     
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fechanacimiento2 = formatoFecha.format(datModif.getFechanacimiento2());
                    fechainicio2 = formatoFecha.format(datModif.getFechainicio2());
                    fechainicio3 = formatoFecha.format(datModif.getFechainicio3());
                    fechafin3 = formatoFecha.format(datModif.getFechafin3());
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide85/melanbide85.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide85/IkerUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

//Desplegable  Durcontrato1
            var comboListaDurcontrato1;
            var listaCodigosDurcontrato1 = new Array();
            var listaDescripcionesDurcontrato1 = new Array();
            function buscaCodigoDurcontrato1(tipo) {
                comboListaDurcontrato1.buscaCodigo(tipo);
            }
            function cargarDatosDurcontrato1() {
                var tipoSeleccionado = document.getElementById("codListaDurcontrato1").value;
                buscaCodigoDurcontrato1(tipoSeleccionado);
            }
//Desplegable  GrupoCotiz1
            var comboListaGrupo1;
            var listaCodigosGrupo1 = new Array();
            var listaDescripcionesGrupo1 = new Array();
            function buscaCodigoGrupo1(tipo) {
                comboListaGrupo1.buscaCodigo(tipo);
            }
            function cargarDatosGrupo1() {
                var tipoSeleccionado = document.getElementById("codListaGrupo1").value;
                buscaCodigoGrupo1(tipoSeleccionado);
            }
//Desplegable  Empverde1
            var comboListaEmpverde1;
            var listaCodigosEmpverde1 = new Array();
            var listaDescripcionesEmpverde1 = new Array();
            function buscaCodigoEmpverde1(tipo) {
                comboListaEmpverde1.buscaCodigo(tipo);
            }
            function cargarDatosEmpverde1() {
                var tipoSeleccionado = document.getElementById("codListaEmpverde1").value;
                buscaCodigoEmpverde1(tipoSeleccionado);
            }
//Desplegable  Empdigit1
            var comboListaEmpdigit1;
            var listaCodigosEmpdigit1 = new Array();
            var listaDescripcionesEmpdigit1 = new Array();
            function buscaCodigoEmpdigit1(tipo) {
                comboListaEmpdigit1.buscaCodigo(tipo);
            }
            function cargarDatosEmpdigit1() {
                var tipoSeleccionado = document.getElementById("codListaEmpdigit1").value;
                buscaCodigoEmpdigit1(tipoSeleccionado);
            }
//Desplegable  Empgen1
            var comboListaEmpgen1;
            var listaCodigosEmpgen1 = new Array();
            var listaDescripcionesEmpgen1 = new Array();
            function buscaCodigoEmpgen1(tipo) {
                comboListaEmpgen1.buscaCodigo(tipo);
            }
            function cargarDatosEmpgen1() {
                var tipoSeleccionado = document.getElementById("codListaEmpgen1").value;
                buscaCodigoEmpgen1(tipoSeleccionado);
            }
            
//Desplegable  Sexo2
            var comboListaSexo2;
            var listaCodigosSexo2 = new Array();
            var listaDescripcionesSexo2 = new Array();
            function buscaCodigoSexo2(tipo) {
                comboListaSexo2.buscaCodigo(tipo);
            }
            function cargarDatosSexo2() {
                var tipoSeleccionado = document.getElementById("codListaSexo2").value;
                buscaCodigoSexo2(tipoSeleccionado);
            }
//Desplegable  GrupoCotiz2
            var comboListaGrupo2;
            var listaCodigosGrupo2 = new Array();
            var listaDescripcionesGrupo2 = new Array();
            function buscaCodigoGrupo2(tipo) {
                comboListaGrupo2.buscaCodigo(tipo);
            }
            function cargarDatosGrupo2() {
                var tipoSeleccionado = document.getElementById("codListaGrupo2").value;
                buscaCodigoGrupo2(tipoSeleccionado);
            }
//Desplegable  Durcontrato2
            var comboListaDurcontrato2;
            var listaCodigosDurcontrato2 = new Array();
            var listaDescripcionesDurcontrato2 = new Array();
            function buscaCodigoDurcontrato2(tipo) {
                comboListaDurcontrato2.buscaCodigo(tipo);
            }
            function cargarDatosDurcontrato2() {
                var tipoSeleccionado = document.getElementById("codListaDurcontrato2").value;
                buscaCodigoDurcontrato2(tipoSeleccionado);
            }
//Desplegable  Empverde2
            var comboListaEmpverde2;
            var listaCodigosEmpverde2 = new Array();
            var listaDescripcionesEmpverde2 = new Array();
            function buscaCodigoEmpverde2(tipo) {
                comboListaEmpverde2.buscaCodigo(tipo);
            }
            function cargarDatosEmpverde2() {
                var tipoSeleccionado = document.getElementById("codListaEmpverde2").value;
                buscaCodigoEmpverde2(tipoSeleccionado);
            }
//Desplegable  Empdigit2
            var comboListaEmpdigit2;
            var listaCodigosEmpdigit2 = new Array();
            var listaDescripcionesEmpdigit2 = new Array();
            function buscaCodigoEmpdigit2(tipo) {
                comboListaEmpdigit2.buscaCodigo(tipo);
            }
            function cargarDatosEmpdigit2() {
                var tipoSeleccionado = document.getElementById("codListaEmpdigit2").value;
                buscaCodigoEmpdigit2(tipoSeleccionado);
            }
//Desplegable  Empgen2
            var comboListaEmpgen2;
            var listaCodigosEmpgen2 = new Array();
            var listaDescripcionesEmpgen2 = new Array();
            function buscaCodigoEmpgen2(tipo) {
                comboListaEmpgen2.buscaCodigo(tipo);
            }
            function cargarDatosEmpgen2() {
                var tipoSeleccionado = document.getElementById("codListaEmpgen2").value;
                buscaCodigoEmpgen2(tipoSeleccionado);
            }

//Desplegable  Durcontrato3
            var comboListaDurcontrato3;
            var listaCodigosDurcontrato3 = new Array();
            var listaDescripcionesDurcontrato3 = new Array();
            function buscaCodigoDurcontrato3(tipo) {
                comboListaDurcontrato3.buscaCodigo(tipo);
            }
            function cargarDatosDurcontrato3() {
                var tipoSeleccionado = document.getElementById("codListaDurcontrato3").value;
                buscaCodigoDurcontrato3(tipoSeleccionado);
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFechaNacimiento2(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaNacimiento2").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechanacimiento2', null, null, null, '', 'calFechaNacimiento2', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaInicio2(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicio2").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechainicio2', null, null, null, '', 'calFechaInicio2', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaInicio3(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicio3").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechainicio3', null, null, null, '', 'calFechaInicio3', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaFin3(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFin3").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechafin3', null, null, null, '', 'calFechaFin3', '', null, null, null, null, null, null, null, null, evento);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoIKER');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE85&operacion=crearNuevaContratacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE85&operacion=modificarContratacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&numpuesto=" + document.getElementById('numpuesto').value
                            + "&denompuesto1=" + document.getElementById('denompuesto1').value
                            + "&titulacion1=" + document.getElementById('titulacion1').value
                            + "&municipioct1=" + document.getElementById('municipioct1').value
                            + "&durcontrato1=" + document.getElementById('codListaDurcontrato1').value
                            + "&grupocotiz1=" + document.getElementById('codListaGrupo1').value
                            + "&costesalarial1=" + document.getElementById('costesalarial1').value
                            + "&costeepis1=" + document.getElementById('costeepis1').value
                            + "&subvsolicitada1=" + document.getElementById('subvsolicitada1').value
                            + "&empverde1=" + document.getElementById('codListaEmpverde1').value
                            + "&empdigit1=" + document.getElementById('codListaEmpdigit1').value
                            + "&empgen1=" + document.getElementById('codListaEmpgen1').value
                            // CONTRATACION_INI
                            + "&noferta2=" + (document.getElementById('noferta2').value != null && document.getElementById('noferta2').value != "" ? document.getElementById('noferta2').value : "")
                            + "&nombre2=" + (document.getElementById('nombre2').value != null && document.getElementById('nombre2').value != "" ? document.getElementById('nombre2').value : "")
                            + "&apellido12=" + (document.getElementById('apellido12').value != null && document.getElementById('apellido12').value != "" ? document.getElementById('apellido12').value : "")
                            + "&apellido22=" + (document.getElementById('apellido22').value != null && document.getElementById('apellido22').value != "" ? document.getElementById('apellido22').value : "")
                            + "&dninie2=" + (document.getElementById('dninie2').value != null && document.getElementById('dninie2').value != "" ? document.getElementById('dninie2').value : "")
                            + "&sexo2=" + (document.getElementById('codListaSexo2').value != null && document.getElementById('codListaSexo2').value != "" ? document.getElementById('codListaSexo2').value : "")
                            + "&titulacion2=" + (document.getElementById('titulacion2').value != null && document.getElementById('titulacion2').value != "" ? document.getElementById('titulacion2').value : "")
                            + "&denompuesto2=" + (document.getElementById('denompuesto2').value != null && document.getElementById('denompuesto2').value != "" ? document.getElementById('denompuesto2').value : "")
                            + "&municipioct2=" + (document.getElementById('municipioct2').value != null && document.getElementById('municipioct2').value != "" ? document.getElementById('municipioct2').value : "")
                            + "&grupocotiz2=" + (document.getElementById('codListaGrupo2').value != null && document.getElementById('codListaGrupo2').value != "" ? document.getElementById('codListaGrupo2').value : "")
                            + "&durcontrato2=" + (document.getElementById('codListaDurcontrato2').value != null && document.getElementById('codListaDurcontrato2').value != "" ? document.getElementById('codListaDurcontrato2').value : "")
                            + "&fechanacimiento2=" + (document.getElementById('fechanacimiento2').value != null && document.getElementById('fechanacimiento2').value != "" ? document.getElementById('fechanacimiento2').value : "")
                            + "&fechainicio2=" + (document.getElementById('fechainicio2').value != null && document.getElementById('fechainicio2').value != "" ? document.getElementById('fechainicio2').value : "")
                            + "&edad=" + (document.getElementById('edad').value != null && document.getElementById('edad').value != "" ? document.getElementById('edad').value : "")
                            + "&retribucionbruta2=" + (document.getElementById('retribucionbruta2').value != null && document.getElementById('retribucionbruta2').value != "" ? document.getElementById('retribucionbruta2').value : "")
                            + "&empverde2=" + (document.getElementById('codListaEmpverde2').value != null && document.getElementById('codListaEmpverde2').value != "" ? document.getElementById('codListaEmpverde2').value : "")
                            + "&empdigit2=" + (document.getElementById('codListaEmpdigit2').value != null && document.getElementById('codListaEmpdigit2').value != "" ? document.getElementById('codListaEmpdigit2').value : "")
                            + "&empgen2=" + (document.getElementById('codListaEmpgen2').value != null && document.getElementById('codListaEmpgen2').value != "" ? document.getElementById('codListaEmpgen2').value : "")
                            // CONTRATACION_FIN
                            + "&nombre3=" + (document.getElementById('nombre3').value != null && document.getElementById('nombre3').value != "" ? document.getElementById('nombre3').value : "")
                            + "&apellido13=" + (document.getElementById('apellido13').value != null && document.getElementById('apellido13').value != "" ? document.getElementById('apellido13').value : "")
                            + "&apellido23=" + (document.getElementById('apellido23').value != null && document.getElementById('apellido23').value != "" ? document.getElementById('apellido23').value : "")
                            + "&dninie3=" + (document.getElementById('dninie3').value != null && document.getElementById('dninie3').value != "" ? document.getElementById('dninie3').value : "")
                            + "&durcontrato3=" + (document.getElementById('codListaDurcontrato3').value != null && document.getElementById('codListaDurcontrato3').value != "" ? document.getElementById('codListaDurcontrato3').value : "")
                            + "&fechainicio3=" + (document.getElementById('fechainicio3').value != null && document.getElementById('fechainicio3').value != "" ? document.getElementById('fechainicio3').value : "")
                            + "&fechafin3=" + (document.getElementById('fechafin3').value != null && document.getElementById('fechafin3').value != "" ? document.getElementById('fechafin3').value : "")
                            + "&costesalarial3=" + (document.getElementById('costesalarial3').value != null && document.getElementById('costesalarial3').value != "" ? document.getElementById('costesalarial3').value : "")
                            + "&costesss3=" + (document.getElementById('costesss3').value != null && document.getElementById('costesss3').value != "" ? document.getElementById('costesss3').value : "")
                            + "&costeepis3=" + (document.getElementById('costeepis3').value != null && document.getElementById('costeepis3').value != "" ? document.getElementById('costeepis3').value : "")
                            + "&costetotalreal3=" + (document.getElementById('costetotalreal3').value != null && document.getElementById('costetotalreal3').value != "" ? document.getElementById('costetotalreal3').value : "")
                            + "&subvconcedidalan3=" + (document.getElementById('subvconcedidalan3').value != null && document.getElementById('subvconcedidalan3').value != "" ? document.getElementById('subvconcedidalan3').value : "")
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
                        elementoVisible('off', 'barraProgresoIKER');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                // Obligatorios
                var campo = document.getElementById('numpuesto');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.numPuesto")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.numPuesto.format")%>';
                    return false;
                }
                campo = document.getElementById('denompuesto1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.denomPuesto1")%>';
                    return false;
                }
                campo = document.getElementById('titulacion1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.titulacion1")%>';
                    return false;
                }
                campo = document.getElementById('municipioct1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.municipioCT1")%>';
                    return false;
                }
                campo = document.getElementById('codListaDurcontrato1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.durContrato1")%>';
                    return false;
                }
                campo = document.getElementById('codListaGrupo1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.grupoCotiz1")%>';
                    return false;
                }
                campo = document.getElementById('costesalarial1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeSalarial1")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeSalarial1.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('costeepis1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeEpis1")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeEpis1.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('subvsolicitada1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada1")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada1.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('codListaEmpverde1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.empverde1")%>';
                    return false;
                }
                campo = document.getElementById('codListaEmpdigit1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.empdigit1")%>';
                    return false;
                }
                campo = document.getElementById('codListaEmpgen1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.empgen1")%>';
                    return false;
                }
                
                // no obligatorios
                campo = document.getElementById('dninie2');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                campo = document.getElementById('edad');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.edad.format")%>';
                        return false;
                    }
                }
                campo = document.getElementById('retribucionbruta2').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.retribucionBruta2.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('dninie3');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costesalarial3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeSalarial3.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costesss3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costesSS3.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costeepis3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeEpis1.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costetotalreal3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.costeTotalReal.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('subvconcedidalan3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                        mensajeValidacion = '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.subvConcedidaLan3.errNumerico")%>';
                        return false;
                    }
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoDurcontrato1('<%=datModif.getDurcontrato1() != null ? datModif.getDurcontrato1() : ""%>');
                    buscaCodigoGrupo1('<%=datModif.getGrupocotiz1() != null ? datModif.getGrupocotiz1() : ""%>');
                    buscaCodigoEmpverde1('<%=datModif.getEmpverde1() != null ? datModif.getEmpverde1() : ""%>');
                    buscaCodigoEmpdigit1('<%=datModif.getEmpdigit1() != null ? datModif.getEmpdigit1() : ""%>');
                    buscaCodigoEmpgen1('<%=datModif.getEmpgen1() != null ? datModif.getEmpgen1() : ""%>');
                    buscaCodigoSexo2('<%=datModif.getSexo2() != null ? datModif.getSexo2() : ""%>');
                    buscaCodigoGrupo2('<%=datModif.getGrupocotiz2() != null ? datModif.getGrupocotiz2() : ""%>');
                    buscaCodigoDurcontrato2('<%=datModif.getDurcontrato2() != null ? datModif.getDurcontrato2() : ""%>');
                    buscaCodigoEmpverde2('<%=datModif.getEmpverde2() != null ? datModif.getEmpverde2() : ""%>');
                    buscaCodigoEmpdigit2('<%=datModif.getEmpdigit2() != null ? datModif.getEmpdigit2() : ""%>');
                    buscaCodigoEmpgen2('<%=datModif.getEmpgen2() != null ? datModif.getEmpgen2() : ""%>');
                    buscaCodigoDurcontrato3('<%=datModif.getDurcontrato3() != null ? datModif.getDurcontrato3() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function desbloquearCampos() {
                document.getElementById('numpuesto').disabled = false;
                document.getElementById('denompuesto1').disabled = false;
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoIKER');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var contrataciones = datos.tabla.lista;
                    if (contrataciones.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = contrataciones;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError(5);
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
                elementoVisible('off', 'barraProgresoIKER');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormato(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFecha

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoIKER');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoIKER" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                            <%=nuevo != null && nuevo=="1" ? meLanbide85I18n.getMensaje(idiomaUsuario,"label.nuevaContratacion"):meLanbide85I18n.getMensaje(idiomaUsuario,"label.modifContratacion")%>
                        </span>
                    </div>
                    <fieldset id="puesto" name="puesto" style="width: 60%;">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.puesto")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker" style="width: 60px;">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.numPuesto")%>
                            </div>
                            <div style="width: 80px; float: left;">
                                <input id="numpuesto" name="numpuesto" type="text" class="inputTextoObligatorio" size="6" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getNumpuesto() != null ? datModif.getNumpuesto() : ""%>" disabled/>
                            </div>
                            <div class="etiquetaIker" style="width: 110px;">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.denomPuesto")%>
                            </div>
                            <div style="width:275px; float: left;">
                                <input id="denompuesto1" name="denompuesto1" type="text" class="inputTextoObligatorio" size="60" maxlength="500" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getDenompuesto1() != null ? datModif.getDenompuesto1() : ""%>" disabled/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contratacion" name="contratacion">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.contratacion")%></legend>
                        <div class="lineaFormulario">
                            <div  class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.titulacion")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="titulacion1" name="titulacion1" type="text" class="inputTextoObligatorio" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getTitulacion1() != null ? datModif.getTitulacion1() : ""%>" />
                            </div>
                            <div  class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.municipioct")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="municipioct1" name="municipioct1" type="text" class="inputTextoObligatorio" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getMunicipioct1() != null ? datModif.getMunicipioct1() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">                   
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaDurcontrato1" id="codListaDurcontrato1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaDurcontrato1"  id="descListaDurcontrato1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaDurcontrato1" name="anchorListaDurcontrato1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.grupo")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaGrupo1" id="codListaGrupo1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaGrupo1"  id="descListaGrupo1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaGrupo1" name="anchorListaGrupo1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeSalarial")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="costesalarial1" name="costesalarial1" type="text" class="inputTextoObligatorio" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostesalarial1() != null ? datModif.getCostesalarial1().toString().replace(".",",") : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeEpis")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="costeepis1" name="costeepis1" type="text" class="inputTextoObligatorio" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCosteepis1() != null ? datModif.getCosteepis1().toString().replace(".",",") : ""%>" />
                            </div>
                        </div>
                        <br><br>
                            <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empVerde")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpverde1" id="codListaEmpverde1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpverde1"  id="descListaEmpverde1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpverde1" name="anchorListaEmpverde1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empDigital")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpdigit1" id="codListaEmpdigit1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpdigit1"  id="descListaEmpdigit1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpdigit1" name="anchorListaEmpdigit1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                                                     
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.subvSolicitada")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="subvsolicitada1" name="subvsolicitada1" type="text" class="inputTextoObligatorio" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getSubvsolicitada1() != null ? datModif.getSubvsolicitada1().toString().replace(".",",") : ""%>" />
                            </div>
                        
                          <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empGeneral")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpgen1" id="codListaEmpgen1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpgen1"  id="descListaEmpgen1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpgen1" name="anchorListaEmpgen1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>   
                        </div>    
                    </fieldset>
                    <fieldset id="contrataIni" name="contrataIni">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.contrataIni")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.denomPuesto")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="denompuesto2" name="denompuesto2" type="text" class="inputTexto" size="30" maxlength="500" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getDenompuesto2() != null ? datModif.getDenompuesto2() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.oferta")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="noferta2" name="noferta2" type="text" class="inputTexto" size="30" maxlength="50" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getNoferta2() != null ? datModif.getNoferta2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="dninie2" name="dninie2" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getDninie2() != null ? datModif.getDninie2() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="nombre2" name="nombre2" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombre2() != null ? datModif.getNombre2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="apellido12" name="apellido12" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido12() != null ? datModif.getApellido12() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="apellido22" name="apellido22" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido22() != null ? datModif.getApellido22() : ""%>" />
                            </div>                            
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">                            
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaSexo2" id="codListaSexo2" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaSexo2"  id="descListaSexo2" size="10" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaSexo2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.grupo")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaGrupo2" id="codListaGrupo2" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaGrupo2"  id="descListaGrupo2" size="15" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaGrupo2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.titulacion")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="titulacion2" name="titulacion2" type="text" class="inputTexto" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getTitulacion2() != null ? datModif.getTitulacion2() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.municipioct")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="municipioct2" name="municipioct2" type="text" class="inputTexto" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getMunicipioct2() != null ? datModif.getMunicipioct2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>                        
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaDurcontrato2" id="codListaDurcontrato2" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaDurcontrato2"  id="descListaDurcontrato2" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaDurcontrato2" name="anchorListaDurcontrato2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.fInicio")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechainicio2" name="fechainicio2"
                                       maxlength="10"  size="10"
                                       value="<%=fechainicio2%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio2(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaInicio2" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.fNac")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechanacimiento2" name="fechanacimiento2"
                                       maxlength="10"  size="10"
                                       value="<%=fechanacimiento2%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaNacimiento2(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaNacimiento2" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.edad")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="edad" name="edad" type="text" class="inputTexto" size="4" maxlength="2" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getEdad() != null ? datModif.getEdad() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">                  
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.retribucion")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="retribucionbruta2" name="retribucionbruta2" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getRetribucionbruta2() != null ? datModif.getRetribucionbruta2().toString().replace(".",",")  : ""%>" />
                            </div>
                           <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empDigital")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpdigit2" id="codListaEmpdigit2" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpdigit2"  id="descListaEmpdigit2" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpdigit2" name="anchorListaEmpdigit2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div> 
                            
                        </div>  
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empVerde")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpverde2" id="codListaEmpverde2" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpverde2"  id="descListaEmpverde2" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpverde2" name="anchorListaEmpverde2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            
                              <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.empGeneral")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaEmpgen2" id="codListaEmpgen2" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaEmpgen2"  id="descListaEmpgen2" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaEmpgen2" name="anchorListaEmpgen2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                          
                        </div>
                    </fieldset>
                    <fieldset id="contrataFin" name="contrataFin">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.contrataFin")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="dninie3" name="dninie3" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getDninie3() != null ? datModif.getDninie3() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="nombre3" name="nombre3" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombre3() != null ? datModif.getNombre3() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="apellido13" name="apellido13" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido13() != null ? datModif.getApellido13() : ""%>" />
                            </div> 

                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input id="apellido23" name="apellido23" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido23() != null ? datModif.getApellido23() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" name="codListaDurcontrato3" id="codListaDurcontrato3" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaDurcontrato3"  id="descListaDurcontrato3" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaDurcontrato3" name="anchorListaDurcontrato3">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.fInicio")%>
                            </div> 
                            <div style="width:300px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechainicio3" name="fechainicio3"
                                       maxlength="10"  size="10"
                                       value="<%=fechainicio3%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio3(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaInicio3"  border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.fFin")%>
                            </div>
                            <div style="width:300px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechafin3" name="fechafin3"
                                       maxlength="10"  size="10"
                                       value="<%=fechafin3%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin3(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaFin3"  border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                        <br><br>                            
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeSal")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="costesalarial3" name="costesalarial3" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostesalarial3() != null ? datModif.getCostesalarial3().toString().replace(".",",")  : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeSS")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="costesss3" name="costesss3" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostesss3() != null ? datModif.getCostesss3().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeEpis")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="costeepis3" name="costeepis3" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCosteepis3() != null ? datModif.getCosteepis3().toString().replace(".",",")  : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.costeTotal")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="costetotalreal3" name="costetotalreal3" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostetotalreal3() != null ? datModif.getCostetotalreal3().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>	
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.subvConcedida")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="subvconcedidalan3" name="subvconcedidalan3" type="text" class="inputTexto" size="10" maxlength="11" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getSubvconcedidalan3() != null ? datModif.getSubvconcedidalan3().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>
                    </fieldset>
                    <br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
            
                /*  DURCONTRATO 1 */
                listaCodigosDurcontrato1[0] = "";
                listaDescripcionesDurcontrato1[0] = "";
                contador = 0;
                <logic:iterate id="durcontrato1" name="listaMesContrato" scope="request">
                listaCodigosDurcontrato1[contador] = ['<bean:write name="durcontrato1" property="des_val_cod" />'];
                listaDescripcionesDurcontrato1[contador] = ['<bean:write name="durcontrato1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaDurcontrato1 = new Combo("ListaDurcontrato1");
                comboListaDurcontrato1.addItems(listaCodigosDurcontrato1, listaDescripcionesDurcontrato1);
                comboListaDurcontrato1.change = cargarDatosDurcontrato1;
              
                /*  GRUPO 1 */
                listaCodigosGrupo1[0] = "";
                listaDescripcionesGrupo1[0] = "";
                contador = 0;
                <logic:iterate id="grupo1" name="listaGrupoCot" scope="request">
                listaCodigosGrupo1[contador] = ['<bean:write name="grupo1" property="des_val_cod" />'];
                listaDescripcionesGrupo1[contador] = ['<bean:write name="grupo1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaGrupo1 = new Combo("ListaGrupo1");
                comboListaGrupo1.addItems(listaCodigosGrupo1, listaDescripcionesGrupo1);
                comboListaGrupo1.change = cargarDatosGrupo1;
                
                /*  EMPVERDE 1 */
                listaCodigosEmpverde1[0] = "";
                listaDescripcionesEmpverde1[0] = "";
                contador = 0;
                <logic:iterate id="empverde1" name="listaBool" scope="request">
                listaCodigosEmpverde1[contador] = ['<bean:write name="empverde1" property="des_val_cod" />'];
                listaDescripcionesEmpverde1[contador] = ['<bean:write name="empverde1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpverde1 = new Combo("ListaEmpverde1");
                comboListaEmpverde1.addItems(listaCodigosEmpverde1, listaDescripcionesEmpverde1);
                comboListaEmpverde1.change = cargarDatosEmpverde1;
                
                /*  EMPDIGIT 1 */
                listaCodigosEmpdigit1[0] = "";
                listaDescripcionesEmpdigit1[0] = "";
                contador = 0;
                <logic:iterate id="empdigit1" name="listaBool" scope="request">
                listaCodigosEmpdigit1[contador] = ['<bean:write name="empdigit1" property="des_val_cod" />'];
                listaDescripcionesEmpdigit1[contador] = ['<bean:write name="empdigit1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpdigit1 = new Combo("ListaEmpdigit1");
                comboListaEmpdigit1.addItems(listaCodigosEmpdigit1, listaDescripcionesEmpverde1);
                comboListaEmpdigit1.change = cargarDatosEmpdigit1;
               
                /*  EMPGEN 1 */
                listaCodigosEmpgen1[0] = "";
                listaDescripcionesEmpgen1[0] = "";
                contador = 0;
                <logic:iterate id="empgen1" name="listaBool" scope="request">
                listaCodigosEmpgen1[contador] = ['<bean:write name="empgen1" property="des_val_cod" />'];
                listaDescripcionesEmpgen1[contador] = ['<bean:write name="empgen1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpgen1 = new Combo("ListaEmpgen1");
                comboListaEmpgen1.addItems(listaCodigosEmpgen1, listaDescripcionesEmpverde1);
                comboListaEmpgen1.change = cargarDatosEmpgen1;

                /*  SEXO 2 */
                listaCodigosSexo2[0] = "";
                listaDescripcionesSexo2[0] = "";
                contador = 0;
                <logic:iterate id="sexo2" name="listaSexo" scope="request">
                listaCodigosSexo2[contador] = ['<bean:write name="sexo2" property="des_val_cod" />'];
                listaDescripcionesSexo2[contador] = ['<bean:write name="sexo2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaSexo2 = new Combo("ListaSexo2");
                comboListaSexo2.addItems(listaCodigosSexo2, listaDescripcionesSexo2);
                comboListaSexo2.change = cargarDatosSexo2;

                /*  GRUPO 2 */
                listaCodigosGrupo2[0] = "";
                listaDescripcionesGrupo2[0] = "";
                contador = 0;
                <logic:iterate id="grupo2" name="listaGrupoCot" scope="request">
                listaCodigosGrupo2[contador] = ['<bean:write name="grupo2" property="des_val_cod" />'];
                listaDescripcionesGrupo2[contador] = ['<bean:write name="grupo2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaGrupo2 = new Combo("ListaGrupo2");
                comboListaGrupo2.addItems(listaCodigosGrupo2, listaDescripcionesGrupo2);
                comboListaGrupo2.change = cargarDatosGrupo2;
                
                /*  DURCONTRATO 2 */
                listaCodigosDurcontrato2[0] = "";
                listaDescripcionesDurcontrato2[0] = "";
                contador = 0;
                <logic:iterate id="durcontrato2" name="listaMesContrato" scope="request">
                listaCodigosDurcontrato2[contador] = ['<bean:write name="durcontrato2" property="des_val_cod" />'];
                listaDescripcionesDurcontrato2[contador] = ['<bean:write name="durcontrato2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaDurcontrato2 = new Combo("ListaDurcontrato2");
                comboListaDurcontrato2.addItems(listaCodigosDurcontrato2, listaDescripcionesDurcontrato2);
                comboListaDurcontrato2.change = cargarDatosDurcontrato2;
                
                /*  EMPVERDE 2 */
                listaCodigosEmpverde2[0] = "";
                listaDescripcionesEmpverde2[0] = "";
                contador = 0;
                <logic:iterate id="empverde2" name="listaBool" scope="request">
                listaCodigosEmpverde2[contador] = ['<bean:write name="empverde2" property="des_val_cod" />'];
                listaDescripcionesEmpverde2[contador] = ['<bean:write name="empverde2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpverde2 = new Combo("ListaEmpverde2");
                comboListaEmpverde2.addItems(listaCodigosEmpverde2, listaDescripcionesEmpverde2);
                comboListaEmpverde2.change = cargarDatosEmpverde2;
                
                /*  EMPDIGIT 2 */
                listaCodigosEmpdigit2[0] = "";
                listaDescripcionesEmpdigit2[0] = "";
                contador = 0;
                <logic:iterate id="empdigit2" name="listaBool" scope="request">
                listaCodigosEmpdigit2[contador] = ['<bean:write name="empdigit2" property="des_val_cod" />'];
                listaDescripcionesEmpdigit2[contador] = ['<bean:write name="empdigit2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpdigit2 = new Combo("ListaEmpdigit2");
                comboListaEmpdigit2.addItems(listaCodigosEmpdigit2, listaDescripcionesEmpverde2);
                comboListaEmpdigit2.change = cargarDatosEmpdigit2;
                
                /*  EMPGEN 2 */
                listaCodigosEmpgen2[0] = "";
                listaDescripcionesEmpgen2[0] = "";
                contador = 0;
                <logic:iterate id="empgen2" name="listaBool" scope="request">
                listaCodigosEmpgen2[contador] = ['<bean:write name="empgen2" property="des_val_cod" />'];
                listaDescripcionesEmpgen2[contador] = ['<bean:write name="empgen2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaEmpgen2 = new Combo("ListaEmpgen2");
                comboListaEmpgen2.addItems(listaCodigosEmpgen2, listaDescripcionesEmpverde2);
                comboListaEmpgen2.change = cargarDatosEmpgen2;
                
                /*  DURCONTRATO 3 */
                listaCodigosDurcontrato3[0] = "";
                listaDescripcionesDurcontrato3[0] = "";
                contador = 0;
                <logic:iterate id="durcontrato3" name="listaMesContrato" scope="request">
                listaCodigosDurcontrato3[contador] = ['<bean:write name="durcontrato3" property="des_val_cod" />'];
                listaDescripcionesDurcontrato3[contador] = ['<bean:write name="durcontrato3" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaDurcontrato3 = new Combo("ListaDurcontrato3");
                comboListaDurcontrato3.addItems(listaCodigosDurcontrato3, listaDescripcionesDurcontrato3);
                comboListaDurcontrato3.change = cargarDatosDurcontrato3;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                } else {
                    desbloquearCampos();
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

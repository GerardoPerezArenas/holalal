<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>

<html>
    <div style="width: 10%"> 
        <head>
            <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <%
                ContratacionVO datModif = new ContratacionVO();
      
                String codOrganizacion = "";
                String nuevo = "";        
                String numExpediente = "";
            
                MeLanbide15I18n meLanbide15I18n = MeLanbide15I18n.getInstance();

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

                    codOrganizacion  = request.getParameter("codOrganizacionModulo");
                    nuevo = (String)request.getAttribute("nuevo");
                    if(request.getAttribute("datModif") != null)
                    {
                        datModif = (ContratacionVO)request.getAttribute("datModif");    
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
            <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide15/melanbide15.css"/>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide15/CatpUtils.js"></script>
            <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
            <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            //Desplegable  DniNie
            var comboListaDniNie;
            var listaCodigosDniNie = new Array();
            var listaDescripcionesDniNie = new Array();
            var idBarra = 'barraProgresoCATP';


            function buscaCodigoDniNie(codDniNie) {
                comboListaDniNie.buscaCodigo(codDniNie);
            }
            function cargarDatosDniNie() {
                var codDniNieSeleccionado = document.getElementById("codListaDniNie").value;
                buscaCodigoDniNie(codDniNieSeleccionado);
            }

            function guardarDatosContratacion() {
                if (validarDatosContratacion()) {
                    elementoVisible('on', 'barraProgresoCATP');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=crearNuevaContratacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=modificarContratacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";

                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&dniCon=" + document.getElementById('codListaDniNie').value
                            + "&numIdenCon=" + document.getElementById('numIdenCon').value.toUpperCase()
                            + "&fecIniCon=" + document.getElementById('fecIniCon').value
                            + "&fecFinCon=" + document.getElementById('fecFinCon').value
                            + "&subvencionCon=" + document.getElementById('subvencionCon').value

                            ;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaContratacion,
                            error: mostrarErrorAltaContratacion
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoCATP');
                        mostrarErrorContratacion();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatosContratacion() {
                mensajeValidacion = "";
                var correcto = true;

                // Validación del campo DNI/NIE
                var dniNie = document.getElementById('codListaDniNie').value;
                if (dniNie == null || dniNie.trim() === '') {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.listaDniNie")%>';
                    return false;
                }

                // Validación del Número de Identificación
                var numIdenCon = document.getElementById('numIdenCon').value;
                if (numIdenCon == null || numIdenCon.trim() === '') {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie")%>';
                    return false;
                } else if (!validarDniNie(document.getElementById('numIdenCon'))) {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie.documentoIncorrecto")%>';
                    return false;
                }
                campo = document.getElementById('fecIniCon').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.fecIniCon")%>';
                    return false;
                }
                campo = document.getElementById('fecFinCon').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.fecFinCon")%>';
                    return false;
                }


                // Validación del campo Subvención
                var subvencionCon = document.getElementById('subvencionCon').value;
                if (subvencionCon && !validarNumericoDecimalPrecision(subvencionCon, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.SubvencionOri.formato")%>';
                    return false;
                }

                return true; // Todo correcto
            }


            function cancelarContratacion() {
                var resultado = jsp_alerta('', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaContratacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoCATP');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var accesos = datos.tabla.lista;
                    if (accesos.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = accesos;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarErrorContratacion(5);
                    }
                } else {
                    mostrarErrorContratacion(codigoOperacion);
                }
            }
            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoDniNie('<%=datModif.getDniCon() != null ? datModif.getDniCon() : ""%>');
                }
            }
            function mostrarErrorAltaContratacion() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarErrorContratacion(codigo);
            }

            function mostrarErrorContratacion(codigo) {
                elementoVisible('off', 'barraProgresoCATP');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }
            //Funcion para el calendario de fecha 
            function mostrarCalfecIniCon(event) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calfecIniCon").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecIniCon', null, null, null, '', 'calfecIniCon', '', null, null, null, null, null, null, null, null, event);

            }
            //Funcion para el calendario de fecha 
            function mostrarCalfecFinCon(event) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calfecFinCon").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecFinCon', null, null, null, '', 'calfecFinCon', '', null, null, null, null, null, null, null, null, event);

            }

            </script>
        </head>
        <body class="bandaBody box" onload="javascript:elementoVisible('off', 'barraProgresoCATP');">

            <div class="contenidoPantalla" style="width: 100%;">

                <!-- Barra de Progreso -->
                <div id="barraProgresoCATP" style="visibility: hidden;">
                    <div class="contenedorHidepage">
                        <div class="textoHide">
                            <span>
                                <%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                            </span>
                        </div>
                        <div class="imagenHide">
                            <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"></span>
                        </div>
                    </div>
                </div>
                <form
                    <!-- Título -->
                    <div class="sub3titulo" style="width: 100%; padding: 10px; text-align: center; font-size: 14px;">
                        <span id="tituloVentana">
                            <%=nuevo != null && nuevo=="1" ? meLanbide15I18n.getMensaje(idiomaUsuario,"label.nuevaCon") : meLanbide15I18n.getMensaje(idiomaUsuario,"label.modifCon")%>
                        </span>
                    </div>
                    <!-- Línea de formulario: DNI/NIE -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.dniNie")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaDniNie" id="codListaDniNie" size="1" class="inputCombo" value="" onkeyup="xAMayusculas(this);" maxlength="1"/>
                            <input type="text" name="descListaDniNie"  id="descListaDniNie" size="10" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaDniNie" name="anchorListaDniNie" title="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.dniNie")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>        
                    </div>
                    <br><br>
                    <!-- Línea de formulario: Número de Identificación -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.numIden")%>
                        </div>
                        <div>
                            <input id="numIdenCon" name="numIdenCon" type="text" class="inputTexto" size="22" maxlength="22"
                                   value="<%=datModif != null && datModif.getNumIdenCon() != null ? datModif.getNumIdenCon() : ""%>" />
                        </div>
                    </div>
                    <br>    

                    <!-- Línea de formulario: Fecha Inicial -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.fechaIni")%>
                        </div>
                        <div>
                            <input type="text" id="fecIniCon" name="fecIniCon" class="inputTxtFechaLanbide" size="10"
                                   maxlength="10" placeholder="dd/mm/yyyy" 
                                   value="<%=datModif != null && datModif.getFecIniConStr() != null ? datModif.getFecIniConStr() : ""%>"
                                   onkeypress="return permiteSoloFormatoFechas(event);" 
                                   onfocus="javascript:this.select();" 
                                   onblur="validarFormatoFecha(this);" />
                            <a href="javascript:void(0);" onclick="mostrarCalfecIniCon(event);">
                                <img id="calfecIniCon" src="<%=request.getContextPath()%>/images/calendario/icono.gif" alt="Calendario" style="border: 0;" height="17" width="25" />
                            </a>
                        </div>
                        <br>
                    </div>
                    <!-- Línea de formulario: Fecha de Fin -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.fecFinCon")%>
                        </div>
                        <div>
                            <input type="text" id="fecFinCon" name="fecFinCon" class="inputTxtFechaLanbide" size="10"
                                   maxlength="10" placeholder="dd/mm/yyyy" 
                                   value="<%=datModif != null && datModif.getFecFinConStr() != null ? datModif.getFecFinConStr() : ""%>"
                                   onkeypress="return permiteSoloFormatoFechas(event);" 
                                   onfocus="javascript:this.select();" 
                                   onblur="validarFormatoFecha(this);" />
                            <a href="javascript:void(0);" onclick="mostrarCalfecFinCon(event);">
                                <img id="calfecFinCon" src="<%=request.getContextPath()%>/images/calendario/icono.gif" alt="Calendario" style="border: 0;" height="17" width="25" />
                            </a>
                        </div>
                        <br>
                    </div>
                    <!-- Línea de formulario: Lista Subvencion -->

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.subvencionCon")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="subvencionCon" name="subvencionCon" type="text" class="inputTexto" size="18" maxlength="18" onchange="reemplazarPuntos(this);" 
                                       value="<%=datModif != null && datModif.getSubvencionCon() != null ? datModif.getSubvencionCon().toString().replace(".",",") : ""%>"/>

                            </div>
                        </div>

                    </div>
                    <br><br>
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosContratacion();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelarContratacion();"/>
                        </div>
                    </div>
            </div>
            </form>
    </div>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide15/CatpUtils.js"></script>

    <!-- Calendario Popup -->
    <div id="popupcalendar" class="text"></div>
</body>

<script type="text/javascript">
                           /*  DniNie */
                           listaCodigosDniNie[0] = "";
                           listaDescripcionesDniNie[0] = "";
                           contador = 0;
    <logic:iterate id="DniNie" name="listaDniNie" scope="request">
                           listaCodigosDniNie[contador] = ['<bean:write name="DniNie" property="des_val_cod" />'];
                           listaDescripcionesDniNie[contador] = ['<bean:write name="DniNie" property="des_nom" />'];
                           contador++;
    </logic:iterate>
                           var comboListaDniNie = new Combo("ListaDniNie");
                           comboListaDniNie.addItems(listaCodigosDniNie, listaDescripcionesDniNie);
                           comboListaDniNie.change = cargarDatosDniNie;
                           if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                               rellenarDesplegables();
                           }

</script>

<div/>    
</html> 

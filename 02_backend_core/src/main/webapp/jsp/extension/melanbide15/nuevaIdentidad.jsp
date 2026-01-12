<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.vo.IdentidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <div style="width: 10%"> 
        <head>
            <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <%
                IdentidadVO datModif = new IdentidadVO();
      
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
                        datModif = (IdentidadVO)request.getAttribute("datModif");    
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

                //Desplegable  sexo
                var comboListaSexo;
                var listaCodigosSexo = new Array();
                var listaDescripcionesSexo = new Array();
                function buscaCodigoSexo(tipo) {
                    comboListaSexo.buscaCodigo(tipo);
                }
                function cargarDatosSexo() {
                    var tipoSeleccionado = document.getElementById("codListaSexo").value;
                    buscaCodigoSexo(tipoSeleccionado);
                }

                //Desplegable  si/no
                var comboListaSustituto;
                var listaCodigosSustituto = new Array();
                var listaDescripcionesSustituto = new Array();
                function buscaCodigoSustituto(codSustituto) {
                    comboListaSustituto.buscaCodigo(codSustituto);
                }
                function cargarDatosSustituto() {
                    var codSustitutoSeleccionado = document.getElementById("codListaSustituto").value;
                    buscaCodigoSustituto(codSustitutoSeleccionado);
                }
                function guardarDatosIdentidad() {
                    if (validarDatosIdentidad()) {
                        elementoVisible('on', 'barraProgresoCATP'); // Mostrar barra de progreso
                        var parametros = "";

                        if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                            parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=crearNuevaIdentidad&tipo=0";
                        } else {
                            parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=modificarIdentidad&tipo=0"
                                    + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                        }

                        parametros += '&numExp=<%=numExpediente%>'
                                + "&dniNie=" + document.getElementById('codListaDniNie').value
                                + "&numIden=" + document.getElementById('numIden').value.toUpperCase()
                                + "&nombre=" + document.getElementById('nombre').value
                                + "&apellido1=" + document.getElementById('apellido1').value
                                + "&apellido2=" + document.getElementById('apellido2').value
                                + "&sexo=" + document.getElementById('codListaSexo').value
                                + "&fechaNacimiento=" + document.getElementById('fechaNacimiento').value
                                + "&sustituto=" + document.getElementById('codListaSustituto').value;

                        // Enviar los datos por AJAX
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaIdentidad,
                            error: mostrarErrorAltaIdentidad
                        }).always(() => elementoVisible('off', 'barraProgresoCATP')); // Ocultar barra de progreso
                    } else {
                        jsp_alerta("A", mensajeValidacion);
                    }
                }

                function validarDatosIdentidad() {
                    mensajeValidacion = "";
                    var correcto = true;
                    campo = document.getElementById('codListaDniNie').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.listaDniNie")%>';
                        return false;
                    }

                    campo = document.getElementById('numIden').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie")%>';
                        return false;
                    } else if (!validarDniNie(document.getElementById('numIden'))) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie.documentoIncorrecto")%>';
                        return false;
                    }
                    campo = document.getElementById('nombre').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                        return false;
                    }
                    campo = document.getElementById('apellido1').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                        return false;
                    }
                    campo = document.getElementById('apellido2').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.apellido2")%>';
                        return false;
                    }

                    campo = document.getElementById('codListaSexo').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                        return false;
                    }

                    campo = document.getElementById('fechaNacimiento').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.fechaNacimiento")%>';
                        return false;
                    }

                    campo = document.getElementById('codListaSustituto').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.listaSustituto")%>';
                        return false;
                    }

                    return true;

                }


                function cancelarIdentidad() {
                    var resultado = jsp_alerta('', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                    if (resultado == 1) {
                        cerrarVentana();
                    }
                }

                // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
                function procesarRespuestaAltaIdentidad(ajaxResult) {
                    elementoVisible('off', 'barraProgresoCATP');
                    var datos = JSON.parse(ajaxResult);
                    var codigoOperacion = datos.tabla.codigoOperacion;
                    if (codigoOperacion == "0") {
                        var identidad = datos.tabla.lista;
                        if (identidad.length > 0) {
                            var respuesta = new Array();
                            respuesta[0] = "0";
                            respuesta[1] = identidad;
                            self.parent.opener.retornoXanelaAuxiliar(respuesta);
                            cerrarVentana();
                        } else {
                            mostrarErrorIdentidad(5);
                        }
                    } else {
                        mostrarErrorIdentidad(codigoOperacion);
                    }
                }

                function rellenarDesplegables() {
                    if ('<%=datModif%>' != null) {
                        buscaCodigoDniNie('<%=datModif.getDniNie() != null ? datModif.getDniNie() : ""%>');
                        buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                        buscaCodigoSustituto('<%=datModif.getSustituto() != null ? datModif.getSustituto() : ""%>');
                    }
                }

                function mostrarErrorAltaIdentidad() {
                    var codigo;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                        codigo = "8";
                    } else {
                        codigo = "7";
                    }
                    mostrarErrorIdentidad(codigo);
                }

                function mostrarErrorIdentidad(codigo) {
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
                document.addEventListener('DOMContentLoaded', () => {
                    const barra = document.getElementById('barraProgresoCATP');
                    if (!barra) {
                        console.error('Error: No se encontró el elemento con id "barraProgresoCATP". Verifica el DOM.');
                    } else {
                        elementoVisible('off', 'barraProgresoCATP');
                    }
                });



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

                <!-- Formulario -->
                <form>
                    <!-- Título -->
                    <div class="sub3titulo" style="width: 100%; padding: 10px; text-align: center; font-size: 14px;">
                        <span id="tituloVentana">
                            <%=nuevo != null && nuevo=="1" ? meLanbide15I18n.getMensaje(idiomaUsuario,"label.nuevoParticipante") : meLanbide15I18n.getMensaje(idiomaUsuario,"label.modifParticipante")%>
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
                            <input id="numIden" name="numIden" type="text" class="inputTexto" size="22" maxlength="22"
                                   value="<%=datModif != null && datModif.getNumIden() != null ? datModif.getNumIden() : ""%>" />
                        </div>
                    </div>
                    <br>    
                    <!-- Línea de formulario: Nombre -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div>
                            <input id="nombre" name="nombre" type="text" class="inputTexto" size="60" maxlength="60"
                                   value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>" />
                        </div>
                    </div>
                    <br>
                    <!-- Línea de formulario: Apellido 1 -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                        </div>
                        <div>
                            <input id="apellido1" name="apellido1" type="text" class="inputTexto" size="60" maxlength="60"
                                   value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>" />
                        </div>
                    </div>
                    <br>
                    <!-- Línea de formulario: Apellido 2 -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                        </div>
                        <div>
                            <input id="apellido2" name="apellido2" type="text" class="inputTexto" size="60" maxlength="60"
                                   value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>" />
                        </div>
                    </div>
                    <br>
                    <!-- Línea de formulario: Sexo -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaSexo" id="codListaSexo" size="1" class="inputTextoObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                            <input type="text" name="descListaSexo"  id="descListaSexo" size="10" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaSexo" name="anchorListaSexo" title="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.sexo")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <br><br>
                    <!-- Línea de formulario: Fecha de Nacimiento -->
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.fechaNacimiento")%>
                        </div>
                        <div>
                            <input type="text" id="fechaNacimiento" name="fechaNacimiento" class="inputTxtFechaLanbide" size="10"
                                   maxlength="10" placeholder="dd/mm/yyyy" 
                                   value="<%=datModif != null && datModif.getFecNacStr() != null ? datModif.getFecNacStr() : ""%>"
                                   onkeypress="return permiteSoloFormatoFechas(event);" 
                                   onfocus="javascript:this.select();" 
                                   onblur="validarFormatoFecha(this);" />
                            <a href="javascript:void(0);" onclick="mostrarCalfechaNacimiento(event);">
                                <img id="calfechaNacimiento" src="<%=request.getContextPath()%>/images/calendario/icono.gif" alt="Calendario" style="border: 0;" height="17" width="25" />
                            </a>
                        </div>
                        <br>
                    </div>
                    <!-- Línea de formulario: Fecha de Sustituto --> 
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.sustituto")%>
                        </div>                                  
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaSustituto" id="codListaSustituto" size="1" class="inputCombo" value="" onkeyup="xAMayusculas(this);" maxlength="1"/>
                            <input type="text" name="descListaSustituto"  id="descListaSustituto" size="10" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaSustituto" name="anchorListaSustituto" title="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.sustituto")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>        
                    </div>        
                    <br><br>
                    <br><br>
                    <!-- Botones -->
                    <div class="botonera" style="width: 100%; text-align: center;">
                        <input type="button" id="btnAceptar" class="botonGeneral" 
                               value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" 
                               onclick="guardarDatosIdentidad();" />
                        <input type="button" id="btnCancelar" class="botonGeneral" 
                               value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" 
                               onclick="cancelarIdentidad();" />
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



                                   /*  SEXO */
                                   listaCodigosSexo[0] = "";
                                   listaDescripcionesSexo[0] = "";
                                   contador = 0;
            <logic:iterate id="Sexo" name="listaSexo" scope="request">
                                   listaCodigosSexo[contador] = ['<bean:write name="Sexo" property="des_val_cod" />'];
                                   listaDescripcionesSexo[contador] = ['<bean:write name="Sexo" property="des_nom" />'];
                                   contador++;
            </logic:iterate>
                                   var comboListaSexo = new Combo("ListaSexo");
                                   comboListaSexo.addItems(listaCodigosSexo, listaDescripcionesSexo);
                                   comboListaSexo.change = cargarDatosSexo;


                                   /* desplegable si/no */
                                   listaCodigosSustituto[0] = "";
                                   listaDescripcionesSustituto[0] = "";
                                   contador = 0;
            <logic:iterate id="Sustituto" name="listaSustituto" scope="request">
                                   listaCodigosSustituto[contador] = ['<bean:write name="Sustituto" property="des_val_cod" />'];
                                   listaDescripcionesSustituto[contador] = ['<bean:write name="Sustituto" property="des_nom" />'];
                                   contador++;
            </logic:iterate>
                                   var comboListaSustituto = new Combo("ListaSustituto");
                                   comboListaSustituto.addItems(listaCodigosSustituto, listaDescripcionesSustituto);
                                   comboListaSustituto.change = cargarDatosSustituto;


                                   if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                                       rellenarDesplegables();
                                   }


        </script>
        <div/>    
</html> 

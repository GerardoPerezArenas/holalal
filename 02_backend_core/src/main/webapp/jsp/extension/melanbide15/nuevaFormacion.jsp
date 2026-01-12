<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.vo.FormacionVO" %>
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
                FormacionVO datModif = new FormacionVO();
      
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
                        datModif = (FormacionVO)request.getAttribute("datModif");    
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



                function guardarDatosFormacion() {
                    if (validarDatosFormacion()) {
                        elementoVisible('on', 'barraProgresoCATP');
                        var parametros = "";
                        if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                            parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=crearNuevaFormacion&tipo=0";
                        } else {
                            parametros = "tarea=preparar&modulo=MELANBIDE15&operacion=modificarFormacion&tipo=0"
                                    + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                        }
                        parametros += '&numExp=<%=numExpediente%>'
                                + "&dniFor=" + document.getElementById('codListaDniNie').value
                                + "&numIdenFor=" + document.getElementById('numIdenFor').value.toUpperCase()
                                + "&horasFor=" + document.getElementById('horasFor').value
                                + "&porcentajeFor=" + document.getElementById('porcentajeFor').value
                                + "&subvencionFor=" + document.getElementById('subvencionFor').value

                                ;
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaAltaFormacion,
                                error: mostrarErrorAltaFormacion
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoCATP');
                            mostrarErrorFormacion();
                        }
                    } else {
                        jsp_alerta("A", mensajeValidacion);
                    }
                }



                function validarDatosFormacion() {
                    mensajeValidacion = "";
                    var campo = document.getElementById('codListaDniNie').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.listaDniNie")%>';
                        return false;
                    }
                    var campo = document.getElementById('numIdenFor').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie")%>';
                        return false;
                    } else if (!validarDniNie(document.getElementById('numIdenFor'))) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.dninie.documentoIncorrecto")%>';
                        return false;
                    }

                    // Validación del campo Horas de Formación

                    var horasFor = document.getElementById('horasFor').value;
                    if (horasFor == null || horasFor.trim() === '') {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.horasFormacion")%>';
                        alert(mensajeValidacion);
                        return false;
                    } else if (!validarNumericoDecimalPrecision(horasFor, 8, 2)) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.horasFormacion.formato")%>';
                        alert(mensajeValidacion);
                        return false;
                    }
                    // Validación del campo Porcentaje de Formación
                    var porcentajeFor = document.getElementById('porcentajeFor').value;
                    if (!porcentajeFor) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.porcFormacion")%>';
                        return false;
                    } else if (!validarNumericoDecimalPrecision(porcentajeFor, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.porcJorn.formato")%>';
                        return false;
                    } else if (!validarNumericoPorcentajeCien(parseFloat(porcentajeFor.replaceAll(".", ",")))) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.porcJorn.errRango")%>';
                        return false;
                    }

                    // Validación del campo Subvención
                    var subvencionFor = document.getElementById('subvencionFor').value;
                    if (subvencionFor && !validarNumericoDecimalPrecision(subvencionFor, 8, 2)) {
                        mensajeValidacion = '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.subvencionFormacion")%>';
                        return false;
                    }

                    // Si todas las validaciones pasan
                    return true;
                }



                // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
                function procesarRespuestaAltaFormacion(ajaxResult) {
                    try {
                        // Ocultar barra de progreso
                        elementoVisible('off', 'barraProgresoCATP');

                        // Parsear la respuesta del servidor
                        var datos = JSON.parse(ajaxResult);

                        // Verificar el código de operación
                        if (!datos || !datos.tabla || typeof datos.tabla.codigoOperacion === "undefined") {
                            mostrarErrorFormacion("2"); // Error genérico si la estructura es incorrecta
                            return;
                        }

                        var codigoOperacion = datos.tabla.codigoOperacion;

                        if (codigoOperacion === "0") {
                            // Operación exitosa
                            var formacion = datos.tabla.lista;

                            if (Array.isArray(formacion) && formacion.length > 0) {
                                var respuesta = ["0", formacion];
                                self.parent.opener.retornoXanelaAuxiliar(respuesta);
                                cerrarVentana();
                            } else {
                                // Lista vacía o inválida
                                mostrarErrorFormacion("4");
                            }
                        } else {
                            // Mostrar error correspondiente al código de operación
                            mostrarErrorFormacion(codigoOperacion);
                        }
                    } catch (error) {
                        // Manejar excepciones durante el procesamiento
                        console.error("Error procesando la respuesta AJAX:", error);
                        mostrarErrorFormacion("2"); // Error genérico
                    }
                }



                function cancelarFormacion() {
                    var resultado = jsp_alerta('', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                    if (resultado == 1) {
                        cerrarVentana();
                    }
                }
                function rellenarDesplegables() {
                    if ('<%=datModif%>' != null) {
                        buscaCodigoDniNie('<%=datModif.getDniFor() != null ? datModif.getDniFor() : ""%>');
                    }
                }
                function mostrarErrorAltaFormacion() {
                    var codigo;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                        codigo = "8";
                    } else {
                        codigo = "7";
                    }
                    mostrarErrorFormacion(codigo);
                }

                function mostrarErrorFormacion(codigo) {
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
                <form>
                    <div style="width: 100%; padding: 10px; text-align: left;">
                        <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                            <span id="tituloVentana" style="width: 98%;">      
                                <%=nuevo != null && nuevo=="1" ? meLanbide15I18n.getMensaje(idiomaUsuario,"label.nuevaFormacion"):meLanbide15I18n.getMensaje(idiomaUsuario,"label.modifFormacion")%>
                            </span>
                        </div>
                        <!-- Línea de formulario: DNIFOR -->
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
                                <input id="numIdenFor" name="numIdenFor" type="text" class="inputTexto" size="22" maxlength="22"
                                       value="<%=datModif != null && datModif.getNumIdenFor() != null ? datModif.getNumIdenFor() : ""%>" />
                            </div>
                        </div>
                        <br>    
                        <div class="lineaFormulario">
                            <div class="etiqueta" style="width: 250px; float: left;">
                                <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.horasFormacion")%>
                            </div>
                            <div style="width:450px; float: left; ">
                                <input id="horasFor" name="horasFor" type="text" class="inputTextoObligatorio" size="5" maxlength="6" onchange="reemplazarPuntos(this);" 
                                       value="<%=datModif != null && datModif.getHorasFor() != null ? datModif.getHorasFor().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>  
                        <div class="lineaFormulario">
                            <div class="etiqueta" style="width: 250px; float: left;">
                                <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.porcentajeFormacion")%>
                            </div>
                            <div style="width:450px; float: left; ">
                                <input id="porcentajeFor" name="porcentajeFor" type="text" class="inputTextoObligatorio" size="5" maxlength="6" onchange="reemplazarPuntos(this);" 
                                       value="<%=datModif != null && datModif.getPorcentajeFor() != null ? datModif.getPorcentajeFor().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                        </div>  
                        <div class="lineaFormulario">
                            <div class="etiqueta" style="width: 250px; float: left;">
                                <%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.subvencionFormacion")%>
                            </div>
                            <div style="width:450px; float: left; ">
                                <input id="subvencionFor" name="subvencionFor" type="text" size="10" class="inputTexto" maxlength="9" onchange="reemplazarPuntos(this);" 
                                       value="<%=datModif != null && datModif.getSubvencionFor() != null ? datModif.getSubvencionFor().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>               
                        <br>
                        <br><br>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="botonera" style="width: 100%; float: left; text-align: center;">
                                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosFormacion();"/>
                                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelarFormacion();"/>
                            </div>
                        </div>
                    </div>
                </form>
                <div id="popupcalendar" class="text"></div>        
            </div>
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
            listaCodigosDniNie[0] = "";
            listaDescripcionesDniNie[0] = "";
            contador = 0;

            if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                rellenarDesplegables();
            }


        </script>
        <div/>  

</html> 

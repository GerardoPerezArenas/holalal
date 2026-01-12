<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.i18n.MeLanbide14I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
           UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
            }
                        
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide14I18n meLanbide14I18n = MeLanbide14I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
//            Integer numEjercicios = (Integer)request.getAttribute("numEjercicios");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide14/melanbide14.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide14/ExtensionUtils.js"></script>        
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var parametrosBase = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE14'
                , operacion: null
                , tipo: 0
                , numExp: '<%=numExpediente%>'
                , id: null
                , ejerSolicitado: null
                , nuevo: null
                , ultimaOperacion: null
            };

            var tablaSolicitadas;
            var listaSolicitadas = new Array();
            var listaSolicitadasTabla = new Array();

            var comboEjercicios;
            var listaCodigosEjercicios = new Array();
            var listaDescripcionesEjercicios = new Array();

            function cargarDatosEjercicio() {
                var codEjercicioSeleccionado = document.getElementById("codListaEjercicio").value;
                buscaCodigoDesplegable(comboEjercicios, codEjercicioSeleccionado);
                cargarOperacionesSolicitadas(codEjercicioSeleccionado);
            }

            function cargarOperacionesSolicitadas(ejercicio) {
                if (ejercicio != '') {
                    var parametrosLlamada = $.extend({}, parametrosBase);
                    parametrosLlamada.operacion = 'cargarOperacionesSolicitadas';
                    parametrosLlamada.ejercicio = ejercicio;
                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametrosLlamada,
                            beforeSend: function () {
                                pleaseWait('on');
                            },
                            success: procesarRespuesaSolicitadas,
                            error: function () {
                                pleaseWait('off');
                                mostrarErrorPeticion("5");
                            }
                        });
                    } catch (Err) {
                        pleaseWait('off');
                        mostrarErrorPeticion();
                    }
                }
            }

            function nuevaOpeSolicitada() {
                // pasar el ultimo numero orden lista[lista.lenght -1][2]             
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE14&operacion=cargarMantenimientoOperacionSolicitada&tipo=0&nuevo=1&numExp=<%=numExpediente%>'
                        + '&ultimaOperacion=' + listaSolicitadas[listaSolicitadas.length - 1][2] + '&ejerSolicitado=' + document.getElementById("codListaEjercicio").value, 550, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            cargarTablaSolicitadas(result[1]);
                        }
                    }
                });
            }

            function modificarOpeSolicitada() {
                if (tablaSolicitadas.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE14&operacion=cargarMantenimientoOperacionSolicitada&tipo=0&nuevo=0&numExp=<%=numExpediente%>'
                            + '&id=' + listaSolicitadas[tablaSolicitadas.selectedIndex][0]+ '&ejerSolicitado=' + document.getElementById("codListaEjercicio").value, 600, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                cargarTablaSolicitadas(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function eliminarOpeSolicitada() {
                if (tablaSolicitadas.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        var parametrosLlamada = $.extend({}, parametrosBase);
                        parametrosLlamada.operacion = 'eliminarOpeSolicitada';
                        parametrosLlamada.id = listaSolicitadas[tablaSolicitadas.selectedIndex][0];
                        parametrosLlamada.ejerSolicitado = document.getElementById("codListaEjercicio").value;
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametrosLlamada,
                                beforeSend: function () {
                                    pleaseWait('on');
                                },
                                success: procesarRespuesaSolicitadas,
                                error: function () {
                                    pleaseWait('off');
                                    mostrarErrorPeticion("6");
                                }
                            });
                        } catch (Err) {
                            pleaseWait('off');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuesaSolicitadas(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                pleaseWait('off');
                if (codigoOperacion == "0") {
                    var opeSolicitadas = datos.tabla.lista;
                    if (opeSolicitadas.length > 0) {
                        document.getElementById('opeSolicitadas').style.display = 'block';
                        cargarTablaSolicitadas(opeSolicitadas);
                    } else {
                        mostrarErrorPeticion("5");
                    }
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function iniciarTablaSolicitadas() {
                tablaSolicitadas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaOpeSolicitadas'));

                tablaSolicitadas.addColumna('50', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.numOpePre")%>");
                tablaSolicitadas.addColumna('75', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.prioridad")%>");
                tablaSolicitadas.addColumna('250', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.objetivo")%>");
                tablaSolicitadas.addColumna('150', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.tipologia")%>");
                tablaSolicitadas.addColumna('50', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.fecInicio")%>");
                tablaSolicitadas.addColumna('50', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.fecFin")%>");
                tablaSolicitadas.addColumna('150', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.entidad")%>");
                tablaSolicitadas.addColumna('150', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tablaSolicitadas.organismo")%>");

                tablaSolicitadas.displayCabecera = true;
                tablaSolicitadas.height = 400;

            }

            function cargarTablaSolicitadas(operaciones) {
                var fila;
                listaSolicitadas = new Array();
                listaSolicitadasTabla = new Array();
                for (var i = 0; i < operaciones.length; i++) {
                    fila = operaciones[i];
                    listaSolicitadas[i] = [fila.id, fila.ejeOperacion, fila.numOpePre, fila.prioridad, fila.descPrioridad, fila.objetivo, fila.descObjetivo, fila.tipologia, fila.descTipologia, fila.fecInicio, fila.fecInicioStr, fila.fecFin, fila.fecFinStr, fila.entidad, fila.descEntidad, fila.organismo];
                    listaSolicitadasTabla[i] = [fila.numOpePre, fila.descPrioridad, fila.descObjetivo, fila.descTipologia, fila.fecInicioStr, fila.fecFinStr, fila.descEntidad, fila.organismo];
                }
                iniciarTablaSolicitadas();
                tablaSolicitadas.lineas = listaSolicitadasTabla;
                tablaSolicitadas.displayTablaConTooltips(listaSolicitadasTabla);
            }



        </script>        
    </head>    
    <body class="bandaBody">       
        <div class="tab-page" id="tabPage142" style="height:520px; width: 100%;">
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="insertarDatos" name="insertarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.insertarDatos")%>"/>
            <input type="hidden" id="actualizarDatos" name="actualizarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.actualizarDatos")%>"/> 
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>
            <h2 class="tab" id="pestana142"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaFinanciacion")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage142"));</script>
            <h2 class="legendTema" id="solicitud" style="padding-top: 15px;"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.Financiacion")%></h2> 
            <div>    
                <fieldset id="ejercicios"style="width: 38%;border-style: dashed;">
                    <legend class="legendTema" align="center" id="titEjercicios" name="titEjercicios"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.Ejercicios")%></legend>
                    <div class="lineaFormulario" id="selEjercicios" style="text-align: center; display: none;">
                        <div>
                            <input type="text" name="codListaEjercicio" id="codListaEjercicio" size="4" maxlength="4"  class="inputTexto" readonly="true" hidden="true" value="" />
                            <input type="text" name="descListaEjercicio" id="descListaEjercicio" size="4" class="inputTexto" readonly="true" value="" title="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.seleccionEjer")%>" />
                            <a href="" id="anchorListaEjercicio" name="anchorListaEjercicio">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <legend class="legendRojo" align="center" id="sinEjercicio" name="sinEjercicio"style="display: none;"><strong><%=meLanbide14I18n.getMensaje(idiomaUsuario,"msg.sinEjercicio")%></strong></legend>
                    </div>
                </fieldset>
                <fieldset id="opeSolicitadas" name="opeSolicitadas" style="margin-top: 15px;display: none;">
                    <legend class="legendTema" id="solicitud"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.Solicitadas")%></legend>
                    <div id="divGeneralSol">     
                        <div id="listaOpeSolicitadas"  align="center"></div>
                    </div>
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnNuevaSolicitada" name="btnNuevaSolicitada" class="botonGeneral"  value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="nuevaOpeSolicitada();">
                        <input type="button" id="btnModificarSolicitada" name="btnModificarSolicitada" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="modificarOpeSolicitada();">
                        <input type="button" id="btnEliminarSolicitada" name="btnEliminarSolicitada"   class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="eliminarOpeSolicitada();">
                    </div> 
                </fieldset>
                <fieldset id="opeJustificadas" name="opeJustificadas" style="margin-top: 15px;display: none;">
                    <legend class="legendTema" id="justificacion"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.Justificadas")%></legend>
                    <div id="divGeneralJus">     
                        <div id="listaOpeJustificadas"  align="center"></div>
                    </div>
                </fieldset>
            </div>
        </div>
        <script  type="text/javascript">
            listaCodigosEjercicios[0] = "";
            listaDescripcionesEjercicios[0] = "";
            <%
                List<String> List= null;
                if(request.getAttribute("listaEjercicios") != null) {
                    List =(List<String>)request.getAttribute("listaEjercicios");
                    if (List!= null && List.size() >0){
                        for (int indice = 0; indice < List.size(); indice++) {
            %>
            listaCodigosEjercicios[<%=indice%>] = ['<%=List.get(indice)%>'];
            listaDescripcionesEjercicios[<%=indice%>] = ['<%=List.get(indice)%>'];
            <%
                        }
                    }
            %>
            comboEjercicios = new Combo("ListaEjercicio");
            comboEjercicios.addItems(listaCodigosEjercicios, listaDescripcionesEjercicios);
            comboEjercicios.change = cargarDatosEjercicio;
            document.getElementById('selEjercicios').style.display = 'block';
            <%
                } else {
            %>
            document.getElementById('sinEjercicio').style.display = 'block';
            <%
                }
            %>

        </script>
    </body>
</html>
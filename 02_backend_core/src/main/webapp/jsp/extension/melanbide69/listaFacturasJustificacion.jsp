<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.i18n.MeLanbide69I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.util.ConstantesMeLanbide69" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoDesplegableVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoCampoSuplementarioVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ElementoDesplegableVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.vo.FacturaVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
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
        MeLanbide69I18n meLanbide69I18n = MeLanbide69I18n.getInstance();
        String codOrganizacion  = request.getParameter("codOrganizacionModulo");
        String numExpediente    = request.getParameter("numero");

 
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>    
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide69/melanbide69.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide69/lanbide.js"></script>

        <script type="text/javascript">
            var COD_FECSUBS = '<%=ConstantesMeLanbide69.getPropVal_CS_FECSUBSANAR(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_FECPRIMERPAGO = '<%=ConstantesMeLanbide69.getPropVal_CS_FECPRIMERPAGO(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_SUBS = '<%=ConstantesMeLanbide69.getPropVal_CS_SUBSANAR(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_PRIMPAGO = '<%=ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGO(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_PRIMPAGOCB = '<%=ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGOCB(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_IMPREINTEGRO = '<%=ConstantesMeLanbide69.getPropVal_CS_IMPREINTEGRO(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_REINTEGRAR = '<%=ConstantesMeLanbide69.getPropVal_CS_REINTEGRAR(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_SEGPAGO = '<%=ConstantesMeLanbide69.getPropVal_CS_SEGPAGO2(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_FECACUSE = '<%=ConstantesMeLanbide69.getPropVal_CS_FECACUSERECIBO(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_SUBV = '<%=ConstantesMeLanbide69.getPropVal_CS_SUBV(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_SUBVCB = '<%=ConstantesMeLanbide69.getPropVal_CS_SUBVCB(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_CAUSAREINT = '<%=ConstantesMeLanbide69.getPropVal_CS_CAUSAREINT(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var COD_CANTMIN = '<%=ConstantesMeLanbide69.getPropVal_CS_CANTMIN(codOrganizacion,ConstantesMeLanbide69.getCOD_PROCEDIMIENTO())%>';
            var baseUrl = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do?';
            var mensajeValidacion;
            var tablaFacturas;
            var listaFacturas;
            var listaTablaFacturas;
            var codsEstado = new Array();
            var descsEstado = new Array();
            var codsConcepto = new Array();
            var descsConcepto = new Array();
            var codsEntrega = new Array();
            var descsEntrega = new Array();
            var codsSubsanar = new Array();
            var descsSubsanar = new Array();
            var comboEstados;
            var comboConcepto;
            var comboDesglose;
            var codsDesglose;
            var descsDesglose;
            var comboEntregaFact;
            var comboEntregaJustif;
            var comboSubsanar;
            var datosCS;
            var valActualFechaSubs;

            // FUNCIONES DE RESPUESTA A EVENTOS
            function rellenarDatos(tableName, rowID) {
                if (tablaFacturas == tableName) {
                    if (rowID >= 0) {
                        comboEstados.buscaCodigo(listaFacturas[rowID][2]);
                        $('#fechaFact').val(listaTablaFacturas[rowID][1]);
                        $('#importeFact').val(listaTablaFacturas[rowID][5]);
                        $('#identifFact').val(listaTablaFacturas[rowID][2]);
                        comboConcepto.buscaCodigo(listaFacturas[rowID][5]);
                        comboEntregaFact.buscaCodigo(listaFacturas[rowID][8]);
                        comboEntregaJustif.buscaCodigo(listaFacturas[rowID][9]);
                        $('#observFact').val(listaFacturas[rowID][10]);

                        setTimeout(function () {
                            comboDesglose.buscaCodigo(listaFacturas[rowID][6]);
                        }, 350);

                        if (listaFacturas[rowID][5] == '2') {
                            comboDesglose.activate();
                        } else {
                            comboDesglose.deactivate();
                        }


                    } else
                        mostrarMensajeAviso(1);
                }
            }

            function cargarDesgloseConcepto() {
                if (comboConcepto.selectedIndex > 0) {
                    var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=cargarDesgloseConcepto&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';
                    var codConcepto = $('#codConceptoFact').val();

                    pleaseWait('on');
                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: {'codConcepto': codConcepto},
                            success: procesarRespuestaCargarDesgloseConcepto,
                            error: mostrarErrorRespuestaCargarDesgloseConcepto
                        });
                    } catch (Err) {
                        pleaseWait('off');
                        mostrarMensajeError();
                    }
                }
            }

            function guardarDatosFactura() {
                if (validaFormulario()) {
                    var estado = $('#codEstadosFact').val();
                    var fecha = $('#fechaFact').val();
                    var codConcepto = $('#codConceptoFact').val();
                    var codSubcpto = $('#codDesgloseFact').val();
                    var importe = ($('#importeFact').val());
                    var entregada = $('#codEntregaFact').val();
                    var justificada = $('#codEntregaJustif').val();
                    var observ = $('#observFact').val();
                    var identFact = $('#identifFact').val();
                    var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=guardarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                            + '&estado=' + estado + '&fecha=' + fecha + '&codConcepto=' + codConcepto + '&codSubcpto=' + codSubcpto + '&importe=' + importe + '&entreg=' + entregada
                            + '&justif=' + justificada + '&observ=' + observ + '&identFact=' + identFact;

                    realizarPeticionAjax(parametros);
                } else
                    jsp_alerta("A", mensajeValidacion);
            }

            function modificarDatosFactura() {
                if (tablaFacturas.selectedIndex != -1) {
                    if (validaFormulario()) {
                        var estado = $('#codEstadosFact').val();
                        var fecha = $('#fechaFact').val();
                        var codConcepto = $('#codConceptoFact').val();
                        var codSubcpto = $('#codDesgloseFact').val();
                        var importe = $('#importeFact').val();
                        var entregada = $('#codEntregaFact').val();
                        var justificada = $('#codEntregaJustif').val();
                        var observ = $('#observFact').val();
                        var identFact = $('#identifFact').val();
                        var id = listaFacturas[tablaFacturas.selectedIndex][0];
                        var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=guardarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                                + '&estado=' + estado + '&fecha=' + fecha + '&codConcepto=' + codConcepto + '&codSubcpto=' + codSubcpto + '&importe=' + importe + '&entreg=' + entregada
                                + '&justif=' + justificada + '&observ=' + observ + '&identFact=' + identFact + '&id=' + id;

                        realizarPeticionAjax(parametros);
                    } else
                        jsp_alerta("A", mensajeValidacion);
                } else
                    mostrarMensajeAviso(1);
            }

            function eliminarFactura() {
                if (tablaFacturas.selectedIndex != -1) {
                    var id = listaFacturas[tablaFacturas.selectedIndex][0];
                    var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=eliminarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                            + '&id=' + id;
                    if (jsp_alerta("C", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.preguntaEliminar")%>') == 1) {
                        realizarPeticionAjax(parametros);
                    }
                } else
                    mostrarMensajeAviso(1);
            }

            function limpiarFormulario() {
                comboEstados.selectItem(-1);
                $('#fechaFact').val("");
                comboConcepto.selectItem(-1);
                comboDesglose.selectItem(-1);
                $('#importeFact').val("");
                comboEntregaFact.selectItem(-1);
                comboEntregaJustif.selectItem(-1);
                $('#observFact').val("");
                $('#identifFact').val("");

                $fileupload = $('#fichero_xml');
                $fileupload.replaceWith($fileupload.clone(true));
            }

            function recuperarValoresCS() {
                var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=recuperarValoresCS&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';
                pleaseWait('on');
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: {},
                        success: procesarRespuestaRecuperarValoresCS,
                        error: mostrarErrorRespuestaRecuperarValoresCS
                    });
                } catch (Err) {
                    pleaseWait('off');
                    mostrarMensajeError();
                }
            }

            function recargarFechasCalculadas() {
                var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=recargarFechasCalculadas&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';
                pleaseWait('on');
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: {},
                        success: procesarRespuestaRecargarFechasCalculadas,
                        error: mostrarErrorRespuestaRecargarFechasCalculadas
                    });
                } catch (Err) {
                    pleaseWait('off');
                    mostrarMensajeError();
                }
            }

            function copiarFacturasSubsanar() {
                if (tablaFacturas.lineas.length > 0) {
                    copyToClipboard();
                } else
                    mostrarMensajeAviso(4);
            }

            function guardarValoresCS(tipo, valor) {
                var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=guardarValoresCS&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';
                var fecha = $('#fechaSubsanarFact').val();
                var subs = $('#codSubsanarFact').val();

                pleaseWait('on');
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: {'fecha': fecha, 'subs': subs},
                        success: procesarRespuestaGuardarValoresCS,
                        error: mostrarErrorRespuestaGuardarValoresCS
                    });
                } catch (Err) {
                    pleaseWait('off');
                    mostrarMensajeError();
                }
            }

            function comprobarCambioValor(nuevo, viejo) {
                if (nuevo != undefined && nuevo != viejo)
                    guardarValoresCS('fecha', nuevo);
            }

            // FUNCIONES DE PETICIONES AJAX
            function procesarRespuestaRecuperarValoresCS(result) {
                if (result) {
                    var datos = JSON.parse(result);
                    if (datos.tabla.error == "0") {
                        datosCS = datos.tabla.camposSuplementarios.tabla;
                        realizarCalculo();
                    } else {
                        var error = datos.tabla.error;
                        mostrarMensajeError(error);
                    }
                } else
                    mostrarMensajeError("12");
            }

            function mostrarErrorRespuestaRecuperarValoresCS() {
                pleaseWait('off');
                mostrarMensajeError();
            }

            function guardarImportePagoReintegro(tipo, campo1, valor1, campo2, cantminorada) {
                var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=guardarImportePago&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';

                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: {'tipoCalculo': tipo, 'campo1': campo1, 'valor1': valor1, 'campo2': campo2, 'cantminorada': cantminorada},
                        success: procesarRespuestaGuardarImportePago,
                        error: mostrarErrorRespuestaGuardarImportePago
                    });
                } catch (Err) {
                    pleaseWait('off');
                    mostrarMensajeError();
                }
            }

            function procesarRespuestaGuardarImportePago(result) {
                pleaseWait('off');
                if (result) {
                    var datos = JSON.parse(result);
                    if (datos.tabla.error == "0") {
                        // Reflejamos los cambios: tipoCalculo=reintegro -> actualizamos IMPREINTEGRO y NOJUST2P ó tipoCalculo=pago -> actualizamos IMPSEGUNDOPAGO
                        //se actualizan siempre los tres pagos porque han pedido que no se dejen valores vacíos y se metan los 0
                        if ($('#capaDatosSuplementarios_DEF')) {
                            eval("$('#" + COD_SEGPAGO + "').val(datos.tabla." + COD_SEGPAGO + ")");
                            eval("$('#" + COD_IMPREINTEGRO + "').val(datos.tabla." + COD_IMPREINTEGRO + ")");
                            if (datos.tabla.tipoCalculo == "pago") {
                                eval("$('input[name=cod" + COD_CAUSAREINT + "]').val('')");
                                eval("$('input[name=desc" + COD_CAUSAREINT + "]').val('')");
                                eval("$('input[name=cod" + COD_REINTEGRAR + "]').val('')");
                                eval("$('input[name=desc" + COD_REINTEGRAR + "]').val('')");
                            } else {
                                if (datos.tabla.tipoCalculo == "reintegro") {
                                    // Indico causa 6
                                    var codMotivo = '<%=ConstantesMeLanbide69.getCOD_MOTIVOREINT()%>';
                                    var descMotivo = '<%=ConstantesMeLanbide69.getVAL_MOTIVOREINT()%>';
                                    eval("$('input[name=cod" + COD_CAUSAREINT + "]').val(codMotivo)");
                                    eval("$('input[name=desc" + COD_CAUSAREINT + "]').val(descMotivo)");
                                    // pongo marca "A Reintegrar"
                                    var marca = '<%=ConstantesMeLanbide69.getCOD_AREINTEGRAR()%>';
                                    eval("$('input[name=cod" + COD_REINTEGRAR + "]').val(marca)");
                                    eval("$('input[name=desc" + COD_REINTEGRAR + "]').val(marca)");
                                }
                            }
                            // cantidad minorada
                            eval("$('#" + COD_CANTMIN + "').val(datos.tabla." + COD_CANTMIN + ")");
                        }
                        mostrarMensajeAviso(2);
                    } else {
                        var error = datos.tabla.error;
                        mostrarMensajeError(error);
                    }
                } else
                    mostrarMensajeError("12");
            }

            function mostrarErrorRespuestaGuardarImportePago() {
                pleaseWait('off');
                mostrarMensajeError();
            }

            function procesarRespuestaCargarDesgloseConcepto(result) {
                pleaseWait('off');
                var datos;
                if (result) {
                    datos = JSON.parse(result);
                    datos = datos.tabla;
                    var elementos = datos.desglose.paresCodVal;
                    codsDesglose = new Array();
                    descsDesglose = new Array();
                    if (elementos.length > 0) {
                        for (var cont = 0; cont < elementos.length; cont++) {
                            var elem = elementos[cont];
                            codsDesglose[cont] = elem.codigo;
                            descsDesglose[cont] = elem.valor;
                        }
                    }
                    comboDesglose.clearItems();
                    comboDesglose.addItems(codsDesglose, descsDesglose);
                    if (datos.concepto != "7") {
                        comboDesglose.deactivate();
                        comboDesglose.selectItem(1);
                    } else
                        comboDesglose.activate();
                } else
                    mostrarMensajeError();
            }

            function mostrarErrorRespuestaCargarDesgloseConcepto() {
                mostrarMensajeError();
            }

            function realizarPeticionAjax(parametros) {
                var ajax = getXMLHttpRequest();

                if (ajax != null) {
                    pleaseWait('on');

                    ajax.open("POST", baseUrl, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
                    try {
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            pleaseWait('off');

                            var text = ajax.responseText;
                            var datos = JSON.parse(text);
                            if (datos.tabla.error == "0") {
                                var facturas = datos.tabla.lista;
                                if (facturas.length > 0) {
                                    listaFacturas = new Array();
                                    listaTablaFacturas = new Array();

                                    for (var contador = 0; contador < facturas.length; contador++) {
                                        var fact = facturas[contador];
                                        var observ = fact.observaciones;

                                        listaTablaFacturas[contador] = [
                                            fact.codEstado != undefined ? fact.codEstado : '', fact.fechaStr, fact.identFactura, fact.descConcepto, fact.descDesgloseCpto, fact.importeStr, fact.descEntregaFact, fact.descEntregaJustif
                                        ];
                                        listaFacturas[contador] = [
                                            fact.codIdent, fact.numExpediente, fact.codEstado, fact.fecha, unescape(fact.identFactura), fact.codConcepto, fact.codDesgloseCpto,
                                            fact.importe, fact.codEntregaFact, fact.codEntregaJustif, unescape(observ)
                                        ];
                                    }

                                    recargarTabla(listaTablaFacturas);
                                    limpiarFormulario();
                                }
                            } else {
                                var error = datos.tabla.error;
                                mostrarMensajeError(error);
                            }

                        }//if (ajax.readyState==4 && ajax.status==200)
                    } catch (Err) {
                        mostrarMensajeError();
                        //alert("Error.descripcion: " + Err.description);
                    }//try-catch
                }//if(ajax!=null)
            }

            function procesarRespuestaRecargarFechasCalculadas(result) {
                pleaseWait('off');
                if (result) {
                    var datos = JSON.parse(result);
                    datos = datos.tabla;
                    if (datos.error == "0") {
                        var fecKeys = datos.keys;
                        for (var cont = 0; cont < fecKeys.length; cont++) {
                            var key = fecKeys[cont];
                            $('#' + key).val(eval("datos." + key));
                        }
                        $('#fechaSubsanarFact').val(datos.csFechaSubsanar.valor);
                        if (datos.csDesplSubsanar.valor != undefined)
                            comboSubsanar.buscaCodigo(datos.csDesplSubsanar.valor);
                        else
                            comboSubsanar.selectItem(0);
                    } else
                        mostrarMensajeError(datos.error);
                } else
                    mostrarMensajeError();
            }

            function mostrarErrorRespuestaRecargarFechasCalculadas() {
                mostrarMensajeError();
            }

            function copyToClipboard() {
                var url = baseUrl + 'tarea=preparar&modulo=MELANBIDE69&operacion=copiarTextoAPortapapeles&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>';

                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: {},
                        success: procesarRespuestaCopiarTextoAPortapapeles,
                        error: mostrarErrorRespuestaCopiarTextoAPortapapeles
                    });
                } catch (Err) {
                    pleaseWait('off');
                    mostrarMensajeError();
                }
            }

            function procesarRespuestaCopiarTextoAPortapapeles(result) {
                pleaseWait('off');
                if (result) {
                    var datos = JSON.parse(result);
                    datos = datos.tabla;
                    if (datos.error == "0") {
                        var str = datos.texto;
                        while (str.indexOf("\n") != -1) {
                            str = str.replace('\n', '<br>');
                        }
                        mostrarVentana(str, '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFactASubsanar")%>');
                    } else
                        mostrarMensajeError(datos.error);
                } else
                    mostrarMensajeError();
            }

            function mostrarErrorRespuestaCopiarTextoAPortapapeles() {
                mostrarMensajeError();
            }

            function procesarRespuestaGuardarValoresCS(result) {
                pleaseWait('off');
                if (result) {
                    var datos = JSON.parse(result);
                    datos = datos.tabla;
                    if (datos.error == "0") {
                        if ($('#capaDatosSuplementarios_DEF')) {
                            eval("$('#" + COD_FECSUBS + "').val(datos." + COD_FECSUBS + ")");
                            eval("$('input[name=cod" + COD_SUBS + "]').val(datos." + COD_SUBS + ")");
                            eval("$('input[name=desc" + COD_SUBS + "]').val(datos." + COD_SUBS + ")");
                        }
                    } else
                        mostrarMensajeError(datos.error);
                } else
                    mostrarMensajeError();
            }

            function mostrarErrorRespuestaGuardarValoresCS() {
                mostrarMensajeError();
            }

            // OTRAS FUNCIONES
            function recargarTabla(datos) {
                var justificado = calcularImporte(datos);
                datos[datos.length] = [
                    '', '', '', '', '<b>' + '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.filaTotal.col5")%>' + '</b>',
                    doubleToFormattedString(justificado), '', ''
                ];
                tablaFacturas.lineas = datos;
                tablaFacturas.displayTabla();
            }

            function inicializarDesplegables() {
                var contador = 0;
            <%
                InfoDesplegableVO desplegable = null;
                ArrayList<ElementoDesplegableVO> elementos = null;
                if(request.getAttribute("comboEstados")!=null){
                    desplegable = (InfoDesplegableVO) request.getAttribute("comboEstados");
                    elementos = desplegable.getParesCodVal();
                    for(int i=0; i<elementos.size(); i++){
                        ElementoDesplegableVO elem = elementos.get(i);
            %>
                codsEstado[contador] = '<%=elem.getCodigo()%>';
                descsEstado[contador++] = '<%=elem.getValor()%>';
            <%  } } %>

                comboEstados = new Combo("EstadosFact");
                comboEstados.addItems(codsEstado, descsEstado);

                contador = 0;
            <%
                if(request.getAttribute("comboConceptos")!=null){
                    desplegable = (InfoDesplegableVO) request.getAttribute("comboConceptos");
                    elementos = desplegable.getParesCodVal();
                    for(int i=0; i<elementos.size(); i++){
                        ElementoDesplegableVO elem = elementos.get(i);
            %>
                codsConcepto[contador] = '<%=elem.getCodigo()%>';
                descsConcepto[contador++] = '<%=elem.getValor()%>';
            <%  } } %>

                comboConcepto = new Combo("ConceptoFact");
                comboConcepto.addItems(codsConcepto, descsConcepto);
                comboConcepto.change = cargarDesgloseConcepto;

                comboDesglose = new Combo("DesgloseFact");
                comboDesglose.deactivate();

                contador = 0;
            <%
                if(request.getAttribute("comboSiNo")!=null){
                    desplegable = (InfoDesplegableVO) request.getAttribute("comboSiNo");
                    elementos = desplegable.getParesCodVal();
                    for(int i=0; i<elementos.size(); i++){
                        ElementoDesplegableVO elem = elementos.get(i);
            %>
                codsEntrega[contador] = '<%=elem.getCodigo()%>';
                descsEntrega[contador++] = '<%=elem.getValor()%>';
            <%  } } %>

                comboEntregaFact = new Combo("EntregaFact");
                comboEntregaFact.addItems(codsEntrega, descsEntrega);
                comboEntregaJustif = new Combo("EntregaJustif");
                comboEntregaJustif.addItems(codsEntrega, descsEntrega);

                contador = 0;
                var valorSubsanar = '';
            <%
                if(request.getAttribute("csDesplSubsanar")!=null){
                    InfoCampoSuplementarioVO campo = (InfoCampoSuplementarioVO) request.getAttribute("csDesplSubsanar");
                    desplegable = campo.getDesplegable();
                    elementos = desplegable.getParesCodVal();
                    for(int i=0; i<elementos.size(); i++){
                        ElementoDesplegableVO elem = elementos.get(i);
            %>
                codsSubsanar[contador] = '<%=elem.getCodigo()%>';
                descsSubsanar[contador++] = '<%=elem.getValor()%>';
            <%  } %>
                valorSubsanar = '<%=campo.getValor()%>';
                if (valorSubsanar == null || valorSubsanar == undefined || valorSubsanar == "null")
                    valorSubsanar = '';
            <% } %>

                comboSubsanar = new Combo("SubsanarFact");
                comboSubsanar.addItems(codsSubsanar, descsSubsanar);
                comboSubsanar.buscaCodigo(valorSubsanar);
                //rellenar campo suplementario fecha subsanar
                valActualFechaSubs = '';
            <%
                if(request.getAttribute("csFechaSubsanar")!=null){
                    InfoCampoSuplementarioVO campo = (InfoCampoSuplementarioVO) request.getAttribute("csFechaSubsanar");
                    String valFecSubs = (String) campo.getValor();
            %>
                valActualFechaSubs = '<%=valFecSubs%>';
                if (valActualFechaSubs == "null")
                    valActualFechaSubs = '';
            <% } %>

                $('#fechaSubsanarFact').val(valActualFechaSubs);
            }

            function inicializarListas() {
                listaFacturas = new Array();
                listaTablaFacturas = new Array();

                var contador = 0;
            <%
                if(request.getAttribute("relacionFacturas")!=null){
                    ArrayList<FacturaVO> relacionFacturas = (ArrayList<FacturaVO>) request.getAttribute("relacionFacturas");
                    if(relacionFacturas.size()>0){
                        for(int i=0; i<relacionFacturas.size(); i++){
                            FacturaVO factura = relacionFacturas.get(i);
                            String observ = factura.getObservaciones();
                            if(observ==null) observ = "";
            %>
                listaTablaFacturas[contador] = [
                    '<%=factura.getCodEstado()!=null?factura.getCodEstado():""%>', '<%=factura.getFechaStr()%>', '<%=factura.getIdentFactura()%>', '<%=factura.getDescConcepto()%>',
                    '<%=factura.getDescDesgloseCpto()%>', '<%=factura.getImporteStr()%>', '<%=factura.getDescEntregaFact()%>', '<%=factura.getDescEntregaJustif()%>'
                ];
                listaFacturas[contador++] = [
                    '<%=factura.getCodIdent()%>', '<%=factura.getNumExpediente()%>', '<%=factura.getCodEstado()!=null?factura.getCodEstado():""%>', '<%=factura.getFecha()%>', '<%=factura.getIdentFactura()%>',
                    '<%=factura.getCodConcepto()%>', '<%=factura.getCodDesgloseCpto()%>', '<%=factura.getImporte()%>', '<%=factura.getCodEntregaFact()%>',
                    '<%=factura.getCodEntregaJustif()%>', '<%=StringEscapeUtils.escapeJava(observ)%>'
                ];
            <%  } } } %>

                recargarTabla(listaTablaFacturas);
            }

            function mostrarMensajeError(codigo) {
                pleaseWait('off');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorInsDatos")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionIns")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionMod")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorModDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionElim")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorElimDatos")%>');
                } else if (codigo == "9") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorCargarLista")%>');
                } else if (codigo == "10") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorRecuperarValores")%>');
                } else if (codigo == "11") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorGuardarValores")%>');
                } else if (codigo == "12") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionCalcular")%>');
                } else if (codigo == "13") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorCopyToClipboard")%>');
                } else if (codigo == "14") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionGrabarCS")%>');
                } else if (codigo == "20") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"aviso.noHayFactASubs")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

            /*
             * Muestra mensajes de aviso según los siguientes códigos:
             * 1: No se ha seleccionado ninguna fila
             * 2: El cálculo se ha realizado correctamente
             * 3: No se puede realizar el cálculo. Rellene los campos suplementarios necesarios previamente
             * 4: La lista de facturas está vacía
             */
            function mostrarMensajeAviso(codigo) {
                var mensaje = "";
                if (codigo == "1") {
                    mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"aviso.noFilaSeleccionada")%>';
                } else if (codigo == "2") {
                    mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"aviso.exitoOperacionCalcular")%>';
                } else if (codigo == "3") {
                    mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"aviso.sinValoresParaCalcular")%>';
                } else if (codigo == "4") {
                    mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"aviso.noHayFacturas")%>';
                }
                jsp_alerta("A", mensaje);
            }

            function mostrarCalFechaFactura(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFact").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaFact', null, null, null, '', 'calFechaFact', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaSubsFactura(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaSubsanarFact").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaSubsanarFact', null, null, null, '', 'calFechaSubsanarFact', '', null, null, null, null, null, null, null, null, evento);
            }

            function realizarCalculo() {
                var valSubs = eval("datosCS." + COD_SUBS);
                var valFec = eval("datosCS." + COD_FECSUBS);
                var valFecPrimerPago = eval("datosCS." + COD_FECPRIMERPAGO);
                var pago1 = eval("datosCS." + COD_PRIMPAGO);
                var pago1CB = eval("datosCS." + COD_PRIMPAGOCB);
                var subvencion = eval("datosCS." + COD_SUBV);
                var subvencionCB = eval("datosCS." + COD_SUBVCB);
                var causa = eval("datosCS." + COD_CAUSAREINT);
                var campos = [pago1, subvencion];
                var camposCB = [pago1CB, subvencionCB];

                if (validarCSparaCalculo(campos) || validarCSparaCalculo(camposCB)) {
                    //el importe del primer pago puede ser CB o no... cogemos el que tenga para realizar el cálculo
                    if (pago1 == undefined || pago1 == null || pago1 == "") {//si no recibo pago1 cojo pago1CB y subvencionCB
                        pago1 = pago1CB;
                        subvencion = subvencionCB;
                    }
                    if (pago1 == undefined || valFecPrimerPago == "" || valFecPrimerPago == null || valFecPrimerPago == undefined) {//si campo suplementario fecha pago 1 is null o no hay fecha de pago1 -> pago1=0
                        pago1 = 0;
                    } else {
                        pago1 = eval(pago1);
                    }
                    if (subvencion == undefined)
                        subvencion = 0;
                    else
                        subvencion = eval(subvencion);

                    if (valSubs == "X" && (valFec == undefined || valFec == "" || valFec == null)) {
                        pleaseWait('off');
                        $("#msgSubsanar").html('<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.subsPendienteNoCalc")%>');
                    } else {
                        $("#msgSubsanar").html('');
                        var justificado = calcularImporte(tablaFacturas.lineas);
                        var pago2 = 0;
                        var reint = 0;
                        var cantmin = 0;
                        var campo1;
                        var valor1;
                        var campo2;
                        var tipoCalculo;
                        var preguntar = 0;
                        var guardar = false;

                        if (pago1 != 0) {
                            if (causa != undefined && causa != null && causa != "" && causa != 6) {//causa 6 es JUSTIFICACIÓN < AL PAGO
                                reint = pago1;
                                pago2 = 0;
                                tipoCalculo = "reintegrarTodo";
                            } else if (justificado > subvencion) {
                                pago2 = subvencion - pago1;
                                reint = 0;
                                tipoCalculo = "pago";
                            } else if (justificado >= pago1) {
                                pago2 = justificado - pago1;
                                reint = 0;
                                tipoCalculo = "pago";
                            } else if (justificado >= 0) {
                                reint = pago1 - justificado;
                                pago2 = 0;
                                tipoCalculo = "reintegro";
                            }
                            if ((pago2 > 0 && pago2 != (subvencion - pago1)) || pago2 == 0) {//si se paga menos que la resolución (subvencion), es decir, si el pago 2 no se hace en su integridad
                                cantmin = subvencion - justificado;
                            } else {
                                cantmin = 0;
                            }
                        } else {
                            cantmin = subvencion;
                            tipoCalculo = "pago";//aunque no se hace pago pero en el código sólo se usa para borrar la causa reintegro
                        }

                        //TODO: preguntar en todos los casos en los que se modifican valores existentes
                        var valReintegro = 0;
                        var valSegPago = 0;
                        if (tipoCalculo == "pago") {
                            valSegPago = eval("datosCS." + COD_SEGPAGO);
                            if (valSegPago != "" && valSegPago > 0)
                                preguntar = 1;
                            campo1 = COD_SEGPAGO;
                            campo2 = COD_IMPREINTEGRO;
                            valor1 = pago2;
                        } else {
                            valReintegro = eval("datosCS." + COD_IMPREINTEGRO);
                            if (valReintegro != "" && valReintegro > 0)
                                preguntar = 2;
                            campo1 = COD_IMPREINTEGRO;
                            campo2 = COD_SEGPAGO;
                            valor1 = reint;
                        }

                        if (preguntar > 0) {
                            var mensaje = "";
                            var valor = "";
                            if (preguntar == 1) {
                                mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.existeImporteSegPago")%>';
                                valor = valSegPago;
                            } else if (preguntar == 2) {
                                mensaje = '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.existeImporteReintegro")%>';
                                valor = valReintegro;
                            }
                            mensaje += " " + doubleToFormattedString(valor) + "<br/>" + '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.preguntaSustituirValor")%>';
                            if (jsp_alerta_Mediana("C", mensaje) == 1) {
                                guardar = true;
                            }
                        } else
                            guardar = true;
                        if (guardar)
                            guardarImportePagoReintegro(tipoCalculo, campo1, valor1, campo2, cantmin);
                        else
                            pleaseWait('off');
                    }
                } else {
                    pleaseWait('off');
                    mostrarMensajeAviso(3);
                }
            }

            function calcularImporte(lineasTabla) {
                var total = -1;
                if (lineasTabla.length > 0) {
                    total = 0;
                    for (var cont = 0; cont < lineasTabla.length; cont++) {
                        if (lineasTabla[cont][0] == "A") {
                            var valor = lineasTabla[cont][5];
                            while (valor.indexOf(".") != -1) {
                                valor = valor.replace(".", "");
                            }
                            valor = valor.replace(",", ".");
                            total += eval(valor);
                        }
                    }
                }
                return total;
            }

            function obtenerNombresColsTabla(tabla) {
                var tablaCols = tabla.columnas;
                var listaNombres = new Array();
                if (tablaCols.length > 0) {
                    for (var index = 0; index < tablaCols.length; index++) {
                        listaNombres[index] = tablaCols[index][1];
                    }
                }
                return listaNombres;
            }

            //VALIDACIONES
            function validaFormulario() {
                mensajeValidacion = "";
                var correcto = true;

                var codEstado = $('#codEstadosFact').val();
                var estado = $('#descEstadosFact').val();
                var fecha = $('#fechaFact').val();
                var codConcepto = $('#codConceptoFact').val();
                var concepto = $('#descConceptoFact').val();
                var codSubcpto = $('#codDesgloseFact').val();
                var subcpto = $('#descDesgloseFact').val();
                var importe = $('#importeFact').val();
                while (importe.indexOf(".") != -1) {
                    importe = importe.replace(".", "");
                }
                var codEntregada = $('#codEntregaFact').val();
                var entregada = $('#descEntregaFact').val();
                var codJustificada = $('#codEntregaJustif').val();
                var justificada = $('#descEntregaJustif').val();

                if (codEstado == null || codEstado == '' || estado == null || estado == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.estadoObligatorio")%>';
                    return false;
                }

                if (fecha == null || fecha == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                } else if (!ValidarFechaConFormatoLanbide(fecha)) {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.fechaIncorrecta")%>';
                    return false;
                }

                if (importe == null || importe == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.importeObligatorio")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimal(importe)) {
                        mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.importeIncorrecto")%>';
                        return false;
                    }
                }

                if (codConcepto == null || codConcepto == '' || concepto == null || concepto == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.conceptoObligatorio")%>';
                    return false;
                }

                if (codSubcpto == null || codSubcpto == '' || subcpto == null || subcpto == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.desgloseObligatorio")%>';
                    return false;
                }

                if (codEntregada == null || codEntregada == '' || entregada == null || entregada == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.entregaFactObligatorio")%>';
                    return false;
                }

                if (codJustificada == null || codJustificada == '' || justificada == null || justificada == '') {
                    mensajeValidacion = '<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "msg.entregaJustifObligatorio")%>';
                    return false;
                }

                return correcto;
            }

            function validarCSparaCalculo(datos) {
                var valida = true;
                for (var pos = 0; pos < datos.length; pos++) {
                    var valor = datos[pos];
                    if (valor == undefined || valor == "null" || valor == "") {
                        valida = false;
                        break
                    }
                }
                return valida;
            }

            function validarNumericoDecimal(numero) {
                try {
                    if (Trim(numero) != '') {
                        return /^([0-9])*(,([0-9])*)?$/.test(numero);
                    }
                } catch (err) {
                    return false;
                }
            }

            function pulsarInformeInterno() {
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                var parametros = "?tarea=preparar&modulo=MELANBIDE69&operacion=descargarInformeInterno&tipo=0";
                //document.forms[0].target = "ocultoPendientesAPA";
                document.forms[0].action = url + parametros;
                document.forms[0].submit();



            }//pulsarInformeInterno

            function pulsarCargarXML() {
                var hayFicheroSeleccionado = false;
                if (document.getElementById('fichero_xml').files) {
                    if (document.getElementById('fichero_xml').files[0]) {
                        hayFicheroSeleccionado = true;
                    }
                } else if (document.getElementById('fichero_xml').value != '') {
                    hayFicheroSeleccionado = true;
                }
                if (hayFicheroSeleccionado) {
                    var extension = document.getElementById('fichero_xml').value.split('.').pop().toLowerCase();

                    if (extension != 'zip') {
                        var resultado = jsp_alerta('A', '<%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                        return false;
                    }
                    var resultado = jsp_alerta('', '<%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                    if (resultado == 1) {
                        //document.getElementById('msgImportando').style.display='inline';
                        //document.getElementById('msgEliminandoSeguimientos').style.display='none';
                        barraProgresoEca('on', 'barraProgresoCargaXML');

                        /*document.getElementById('opcionescargains').style.display = 'none';
                         document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargo';
                         document.getElementById('btnCargarInsercionesPrep').disabled = false;
                         document.getElementById('opcionescargaseg').style.display = 'none';
                         document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargo';
                         document.getElementById('btnCargarSegumientosPrep').disabled = false;*/

                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                        var control = new Date();
                        var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=procesarXMLCarga&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
                        document.forms[0].action = url + '?' + parametros;
                        document.forms[0].enctype = 'multipart/form-data';
                        document.forms[0].encoding = 'multipart/form-data';
                        document.forms[0].method = 'POST';
                        document.forms[0].target = 'uploadFrameCarga';
                        document.forms[0].submit();
                    }
                    return false;
                } else {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                    return false;
                }
            }//pulsarCargarXML

            function actualizarTablaFacturas(actualizarOtrasPestanas) {
                try {
                    getListaFacturas();
                    document.getElementById('fichero_xml').value = '';
                    /*var result = getListaFacturas();
                     recargarTabla(result);//actualizarOtrasPestanas*/
                } catch (err) {
                }
                barraProgresoEca('off', 'barraProgresoCargaXML');
            }

            function getListaFacturas() {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = '';
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=getListaFacturas&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
                realizarPeticionAjax(parametros);
            }


        </script>
    </head>
    <body>
        <div class="tab-page" id="tabPage661" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana661"><%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
            <script type="text/javascript">var tp1_p661 = tp1.addTabPage(document.getElementById("tabPage661"));</script>

            <div id="barraProgresoCargaXML" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px" height="100%">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span id="msgImportando">
                                                        <%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                                    </span>                                                                                                
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width:5%;height:20%;"></td>
                                                <td class="imagenHide"></td>
                                                <td style="width:5%;height:20%;"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" style="height:10%" ></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <div style="clear: both;">
                <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
                    <div class=""><!--bloqueDatos-->
                        <FIELDSET>
                            <div class="lineaFormulario" style="float: left; width: 100%">
                                <%--Fecha inicio periodo--%>
                                <div id="capaFecIniPeriodo" style="margin-right: 150px">
                                    <div class="etiqueta" style="float:left;">
                                        <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFecIniPeriodo")%>:
                                    </div>
                                    <div class="columnP" style="float:left; margin-left: 18px">
                                        <input type="text" class="inputTxtFecha" id="fecIniPeriodo" name="fecIniPeriodo" maxlength="10" value="<%=request.getAttribute("fecIniPeriodo")%>" style="border: 0px" readonly="true"/>
                                    </div>                     
                                </div>
                                <%--campo suplementario duplicado subsanar--%>
                                <div id="capaSubsanarFact">
                                    <div class="etiqueta" style="float:left; margin-right: 30px">
                                        <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqASubsanar")%>:
                                    </div>
                                    <div class="columnP" style="float:left; width: 26%">
                                        <input type="text" style='width: 14%' name="codSubsanarFact" id="codSubsanarFact" class="inputTexto" onkeyup="xAMayusculas(this);"/>
                                        <input type="text" style='width: 66%' name="descSubsanarFact" id="descSubsanarFact" class="inputTexto" readonly="true" value=""/>
                                        <a href="" id="anchorSubsanarFact" name="anchorSubsanarFact">
                                            <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonSubsanarFact" name="botonSubsanarFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                                        </a>
                                    </div>
                                </div>
                            </div>					
                            <div class="lineaFormulario" style="width: 100%">
                                <%--Fecha fin periodo--%>
                                <div id="capaFecFinPeriodo" style="margin-right: 150px">
                                    <div class="etiqueta" style="float:left;">
                                        <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFecFinPeriodo")%>:
                                    </div>
                                    <div class="columnP" style="float:left; margin-left: 25px">
                                        <input type="text" class="inputTxtFecha" id="fecFinPeriodo" name="fecFinPeriodo" maxlength="10" value="<%=request.getAttribute("fecFinPeriodo")%>" style="border: 0px" readonly="true"/>
                                    </div>
                                </div>
                                <%-- Campo suplementario duplicado fecha de subsanación --%>
                                <div id="capaFechaSubsFact">
                                    <div class="etiqueta" style="float: left;" >
                                        <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFecSubsanar")%>:
                                    </div>
                                    <div class="columnP" style="float:left; width: 18%">
                                        <input type="text" class="inputTxtFecha" id="fechaSubsanarFact" name="fechaSubsanarFact" maxlength="10" onkeyup = "return SoloCaracteresFechaLanbide(this);" 
                                               onblur = "javascript:return comprobarFechaLanbide(this);" onfocus="javascript:this.select();" style="width: 55%"/>
                                        <A href="javascript:calClick();return false;" onClick="mostrarCalFechaSubsFactura(event);
                                                return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0px solid none" height="17" id="calFechaSubsanarFact" name="calFechaSubsanarFact" border="0" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>" > 
                                        </A>
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario" style="width: 100%">
                                <%--Fecha fin presentación justificación
                                <div id="capaFecFinPres">    
                                    <div class="etiqueta" style="float:left;">
                                        <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFecFinPresentacion")%>:
                                    </div>
                                    <div class="columnP" style="float:left; margin-left: 10px">
                                        <input type="text" class="inputTxtFecha" id="fecFinPresentacion" name="fecFinPresentacion" maxlength="10" value="<%=request.getAttribute("fecFinPresentacion")%>" style="border: 0px" readonly="true"/>
                                    </div>
                                </div>--%>
                                <div id="capaBtnGrabarCSFact" style="float: right">
                                    <input type="button" id="btnGrabarCSFact" name="btnGrabarCSFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqGrabarSubsanar")%>" onclick="guardarValoresCS();">
                                </div>
                            </div>
                        </FIELDSET>
                    </div>
                    <div class="lineaFormulario" style="width: 100%">
                        <%-- Estado --%>
                        <div class="etiqueta" style="margin-right: 71px; float:left;;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqEstado")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 26%">
                            <input type="text" style='width: 14%' name="codEstadosFact" id="codEstadosFact" class="inputTexto" onkeyup="xAMayusculas(this);"/>
                            <input type="text" style='width: 66%' name="descEstadosFact" id="descEstadosFact" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorEstadosFact" name="anchorEstadosFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonEstadosFact" name="botonEstadosFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                        <%--Fecha de factura--%>
                        <div class="etiqueta" style="float: left;margin-left: 30px; margin-right: 10px" >
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqFechaFact")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 18%">
                            <input type="text" class="inputTxtFecha" id="fechaFact" name="fechaFact" maxlength="10" onkeyup = "return SoloCaracteresFechaLanbide(this);" 
                                   onblur = "javascript:return comprobarFechaLanbide(this);" onfocus="javascript:this.select();" style="width: 55%"/>
                            <A href="javascript:calClick();return false;" onClick="mostrarCalFechaFactura(event);
                                    return false;"  style="text-decoration:none;" >
                                <IMG style="border: 0px solid none" height="17" id="calFechaFact" name="calFechaFact" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > 
                            </A>
                        </div>
                        <%--Importe--%>
                        <div class="etiqueta" style="float: left;margin-left: 30px; margin-right: 10px" >
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqImporte")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 20%">
                            <input type="text" name="importeFact" id="importeFact" class="inputTexto" style="width: 60%"/>
                        </div>
                    </div>
                    <%--Identificación--%>
                    <div class="lineaFormulario" style="float: left; width: 100%">
                        <div class="etiqueta" style="margin-right: 11px;float:left;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqIdentFact")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 87%">
                            <input type="text" style='width: 91%' name="identifFact" id="identifFact" class="inputTexto" onkeyup="xAMayusculas(this);" />
                        </div>
                    </div>
                    <%--Concepto--%>
                    <div class="lineaFormulario" style="float: left; width: 100%">
                        <div class="etiqueta" style="margin-right: 45px;float:left;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqConcepto")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 85%">
                            <input type="text" style='width: 4%' name="codConceptoFact" id="codConceptoFact" class="inputTexto" onkeyup="xAMayusculas(this);" />
                            <input type="text" style='width: 87%' name="descConceptoFact"  id="descConceptoFact" class="inputTexto" readonly="true"/>
                            <a href="" id="anchorConceptoFact" name="anchorConceptoFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonConceptoFact" name="botonConceptoFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                    </div>
                    <%--Desglose--%>
                    <div class="lineaFormulario" style="float: left; width: 100%">
                        <div class="etiqueta" style="margin-right: 19px;float:left;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqDesglose")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 85%">
                            <input type="text" style='width: 4%' name="codDesgloseFact" id="codDesgloseFact" class="inputTexto" onkeyup="xAMayusculas(this);"/>
                            <input type="text" style='width: 87%' name="descDesgloseFact"  id="descDesgloseFact" class="inputTexto" readonly="true"/>
                            <a href="" id="anchorDesgloseFact" name="anchorDesgloseFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonDesgloseFact" name="botonDesgloseFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="float: left; width: 100%">
                        <%--Entrega factura--%>
                        <div class="etiqueta" style="margin-right: 30px;float:left;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqEntregaFact")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 29%">
                            <input type="text" style='width: 12%' name="codEntregaFact" id="codEntregaFact" class="inputTexto" onkeyup="xAMayusculas(this);" />
                            <input type="text" style='width: 78%' name="descEntregaFact"  id="descEntregaFact" class="inputTexto" readonly="true" />
                            <a href="" id="anchorEntregaFact" name="anchorEntregaFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonEntregaFact" name="botonEntregaFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                        <%--Entrega Justificante--%>
                        <div class="etiqueta" style="margin-left: 84px; margin-right: 9px;float:left;">
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqEntregaJustif")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 29%">
                            <input type="text" style='width: 12%' name="codEntregaJustif" id="codEntregaJustif" class="inputTexto" onkeyup="xAMayusculas(this);" />
                            <input type="text" style='width: 78%' name="descEntregaJustif"  id="descEntregaJustif" class="inputTexto" readonly="true" />
                            <a href="" id="anchorEntregaJustif" name="anchorEntregaJustif">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonEntregaJustif" name="botonEntregaJustif" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                    </div>
                    <%--Observaciones--%>
                    <div class="lineaFormulario" style="float: left; width: 100%">
                        <div class="etiqueta" style="margin-right: 41px;float: left;" >
                            <%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.etiqObservaciones")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 85%">
                            <textarea style="width: 100%" name="observFact" id="observFact" rows="5" class="melanbide69_txtSinMayusculas"></textarea>
                        </div>
                    </div>
                    <div id='msgSubsanar' style="text-align: right;font-weight: bold; color: red"></div>
                    <br/>
                    <div class="botonera" style="text-align: right; margin-bottom: 5px;">
                        <input type="button" id="btnRegistrarFact" name="btnRegistrarFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqGrabar")%>" onclick="guardarDatosFactura();">
                        <input type="button" id="btnModificarFact" name="btnModificarFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqModificar")%>" onclick="modificarDatosFactura();">
                        <input type="button" id="btnEliminarFact" name="btnEliminarFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqEliminar")%>" onclick="eliminarFactura();">
                        <input type="button" id="btnLimpiarFact" name="btnLimpiarFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqLimpiar")%>" onclick="limpiarFormulario();">
                        <input type="button" id="btnCalcularFact" name="btnCalcularFact" class="botonGeneral" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqCalcular")%>" onclick="recuperarValoresCS();">
                    </div>
                    <div id="tablaFacturas" style="width: 100%"></div>
                    <div class="botonera" style="text-align: right;">
                        <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".zip">
                        <input type="button" id="btnCargarFacturas" name="btnCargarFacturas" class="botonMasLargo" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqCargarXML")%>" onclick=" pulsarCargarXML();">

                    </div><br/>
                    <div class="botonera" style="text-align: right;">

                        <input type="button" id="btnFactASubsanar" name="btnFactASubsanar" class="botonMasLargo" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqInformeApes")%>" onclick=" pulsarInformeInterno();">
                        <input type="button" id="btnFactASubsanar" name="btnFactASubsanar" class="botonMasLargo" value="<%=MeLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqFactASubsanar")%>" onclick="copiarFacturasSubsanar();">
                    </div>
                    <div class="modal" id="ventanaModal" style="display:none">
                        <h2 id="tituloModal"></h2>
                        <p id="contenidoModal"></p>
                    </div>
                    <div id="popup" style="display: none;">
                        <div class="content-popup">
                            <div class="close">
                                <a href="#" id="close" onclick="cerrarVentana();">
                                    <span class="fa fa-times" aria-hidden="true" title="Cerrar" style="color:#004595"></span>
                                </a>
                            </div>
                            <div>
                                <h2 id="popupTittle"></h2>
                                <div id="popupMessage"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 


        <script type="text/javascript">
            function barraProgresoEca(valor, idBarra) {
                if (valor == 'on') {
                    document.getElementById(idBarra).style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById(idBarra).style.visibility = 'hidden';
                }
            }

            // Creamos los combos
            inicializarDesplegables();

            /* Creamos la tabla */
            /*if(document.all){           
             tablaFacturas = new Tabla(document.all.tablaFacturas);
             }else{
             tablaFacturas = new Tabla(document.getElementById("tablaFacturas"));
             }//if(document.all)*/
            tablaFacturas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaFacturas'), 890);


            tablaFacturas.addColumna('30', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col1")%>');
            tablaFacturas.addColumna('65', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col2")%>');
            tablaFacturas.addColumna('85', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col8")%>');
            tablaFacturas.addColumna('280', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col3")%>');
            tablaFacturas.addColumna('300', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col4")%>');
            tablaFacturas.addColumna('60', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col5")%>');
            tablaFacturas.addColumna('35', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col6")%>');
            tablaFacturas.addColumna('35', 'left', '<%=MeLanbide69I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col7")%>');

            tablaFacturas.height = 200;
            tablaFacturas.displayCabecera = true;

            inicializarListas();
        </script>
    </body>
</html>
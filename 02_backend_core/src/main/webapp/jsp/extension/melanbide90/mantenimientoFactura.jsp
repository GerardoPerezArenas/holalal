<%-- 
    Document   : mantenimientoFactura
    Created on : 23-ago-2024, 10:57:10
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.i18n.MeLanbide90I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            
            FacturaVO datosModif = new FacturaVO();
            String codOrganizacion = "";
            String nuevo = "";
            String tituloPagina = "";
            String numExpediente = "";
            String codFamilia = "";
            String desFamilia = "";
            String numOrden ="";
            String fechaEmision = "";
            String fechaPago = "";
             //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide90I18n meLanbide90I18n = MeLanbide90I18n.getInstance();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");  
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
            
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
                } catch(Exception ex) {}
                
                numExpediente = (String)request.getAttribute("numExp");
                codFamilia = (String)request.getAttribute("codFamilia");
                desFamilia = (String)request.getAttribute("desFamilia");
                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                
                if(request.getAttribute("datosModif") != null) {
                    datosModif = (FacturaVO)request.getAttribute("datosModif");  
                    fechaEmision = formatoFecha.format(datosModif.getFecEmision());
                    fechaPago = formatoFecha.format(datosModif.getFecPago());
                }
                
                if (request.getAttribute("numOrden") != null) {
                    numOrden = (String)request.getAttribute("numOrden");  
                }
                
                if(nuevo=="1") {
                    tituloPagina = meLanbide90I18n.getMensaje(idiomaUsuario,"label.factura.alta");
                } else {
                    tituloPagina=meLanbide90I18n.getMensaje(idiomaUsuario,"label.factura.edicion");
                }
            } catch(Exception ex){}
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide90/melanbide90.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide90/IninUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <title><%=tituloPagina%></title>
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';
            var cargaInicial = false;
            //Desplegable  tipo gasto
            var comboListaGasto;
            var listaCodigosGasto = new Array();
            var listaDescripcionesGasto = new Array();
            function cargarDatosGasto() {
                var codSeleccionado = document.getElementById("codListaGasto").value;
                buscaCodigoDesplegable(comboListaGasto, codSeleccionado);
            }

            // Factura Validada
            var comboValidada;
            var listaCodigosValidada = new Array();
            var listaDescripcionesValidada = new Array();
            function cargarDatosValidada() {
                var codSeleccionado = document.getElementById("codListaValidada").value;
                buscaCodigoDesplegable(comboValidada, codSeleccionado);
                if(codSeleccionado == 'S')
                    buscaCodigoDesplegable(comboMotivo, "");
                comprobarCondicionesCalculo();
            }

            // Factura Validada
            var comboPorIvaValidado;
            var listaCodigosPorIvaValidado = new Array();
            var listaDescripcionesPorIvaValidado = new Array();
            function cargarDatosPorIvaValidado() {
                var codSeleccionado = document.getElementById("codListaPorIvaValidado").value;
                buscaCodigoDesplegable(comboPorIvaValidado, codSeleccionado);
                calcularTotalValidado();
            }

            //	IVA Subvencionable 
            var comboSubvencionable;
            var listaCodigosSubvencionable = new Array();
            var listaDescripcionesSubvencionable = new Array();
            function cargarDatosSubvencionable() {
                var codSeleccionado = document.getElementById("codListaSubvencionable").value;
                buscaCodigoDesplegable(comboSubvencionable, codSeleccionado);
                comprobarCondicionesCalculo();
            }

            //	 Motivo No Validada
            var comboMotivo;
            var listaCodigosMotivo = new Array();
            var listaDescripcionesMotivo = new Array();
            function cargarDatosMotivo() {
                var codSeleccionado = document.getElementById("codListaMotivo").value;
                buscaCodigoDesplegable(comboMotivo, codSeleccionado);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    pleaseWaitSinFrame('on');
                    var parametros = 'tarea=preparar&modulo=MELANBIDE90&tipo=0'
                            + '&numExp=<%=numExpediente%>'
                            + '&codFamilia=<%=codFamilia%>'
                            + '&numOrden=' + document.getElementById('numOrden').value
                            + '&tipoGasto=' + document.getElementById('codListaGasto').value
                            + '&proveedor=' + document.getElementById('proveedor').value
                            + '&numFactura=' + document.getElementById('numFactura').value
                            + '&fecEmision=' + document.getElementById('fechaEmision').value
                            + '&fecPago=' + document.getElementById('fechaPago').value
                            + '&base=' + document.getElementById('base').value
                            + '&iva=' + document.getElementById('importeIva').value
                            + '&validada=' + document.getElementById('codListaValidada').value
                            + '&subvencionable=' + document.getElementById('codListaSubvencionable').value
                            + '&importeTotal=' + document.getElementById('importeTotal').value
                            + '&importeVali=' + document.getElementById('importeValidado').value
                            + '&motivo=' + document.getElementById('codListaMotivo').value
                            + '&baseValidado=' + document.getElementById('baseValidado').value
                            + '&porIvaValidado=' + document.getElementById('codListaPorIvaValidado').value
                            + '&prorrataValidado=' + document.getElementById('prorrataValidado').value
                            + '&importeIvaValidado=' + document.getElementById('importeIvaValidado').value
                            + '&operacion='
                            ;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == '1') {
                        parametros += 'altaFactura';
                    } else {
                        parametros += 'modificarFactura&id=<%=datosModif != null && datosModif.getId() != null ? datosModif.getId().toString() : ""%>';
                    }
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
                        pleaseWaitSinFrame('off');
                        mostrarError();
                    }

                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = '';

                campo = document.getElementById('codListaGasto');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.gasto")%>';
                    return false;
                }
                campo = document.getElementById('proveedor');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.proveedor")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 150)) {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.proveedor.tamano")%>';
                    return false;
                }
                campo = document.getElementById('numFactura');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.numFactura")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 150)) {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.numFactura.tamano")%>';
                    return false;
                }
                campo = document.getElementById('fechaEmision');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.fechaEmision")%>';
                    return false;
                }
                campo = document.getElementById('fechaPago');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.fechaPago")%>';
                    return false;
                }
                campo = document.getElementById('base');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.base")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.base.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('importeIva');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeIva")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeIva.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('codListaValidada');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.validada")%>';
                    return false;
                }
                campo = document.getElementById('codListaSubvencionable');
                if ((campo.value == null || campo.value == '') && document.getElementById('codListaValidada').value == 'S') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.subvencionable")%>';
                    return false;
                }
                campo = document.getElementById('importeValidado');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeValidado")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeValidado.errNumerico")%>';
                    return false;
                } /*else if (document.getElementById('codListaValidada').value == 'S') {
                    var importe = Math.round(parseFloat(campo.value.replace(",", "."))*100)/100;
                    var tope;
                    if (document.getElementById('codListaSubvencionable').value == 'S') {
                        tope = parseFloat(document.getElementById("base").value.replace(",", ".")) + parseFloat(document.getElementById("importeIva").value.replace(",", "."));
                        tope =Math.round(tope*100)/100;
                        if (importe > tope) {
                            mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeValidado.mayorTotal")%>';
                            return false;
                        }
                    } else if (document.getElementById('codListaSubvencionable').value == 'N') {
                        tope = parseFloat(document.getElementById("base").value.replace(",", "."));
                        tope =Math.round(tope*100)/100;
                        if (importe > tope) {
                            mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.importeValidado.mayorBase")%>';
                            return false;
                        }
                    }
                }*/
                campo = document.getElementById('codListaMotivo');
                if ((campo.value == null || campo.value == '') && document.getElementById('codListaValidada').value == 'N') {
                    mensajeValidacion = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.factura.motivo")%>';
                    return false;
                }

                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenarDesplegables() {
                buscaCodigoDesplegable(comboListaGasto, '<%=datosModif.getTipoGasto() != null ? datosModif.getTipoGasto() : ""%>');
                buscaCodigoDesplegable(comboValidada, '<%=datosModif.getValidada() != null ? datosModif.getValidada() : ""%>');
                buscaCodigoDesplegable(comboSubvencionable, '<%=datosModif.getIvaSub() != null ? datosModif.getIvaSub() : ""%>');
                buscaCodigoDesplegable(comboMotivo, '<%=datosModif.getMotNoVal() != null ? datosModif.getMotNoVal() : ""%>');
                buscaCodigoDesplegable(comboPorIvaValidado, '<%=datosModif.getPorcentajeIvaValidado() != null ? datosModif.getPorcentajeIvaValidado() : ""%>');
            }

            function desbloquearCampos() {
                document.getElementById("codListaGasto").disabled = false;
                document.getElementById("proveedor").disabled = false;
                document.getElementById("numFactura").disabled = false;
                document.getElementById("fechaEmision").disabled = false;
                document.getElementById("fechaPago").disabled = false;
                document.getElementById("base").disabled = false;
                document.getElementById("importeIva").disabled = false;
                //document.getElementById("importeValidado").disabled = false;
            }

            function mostrarCalendarios(evento, nombreIdElementoCal, nombreIdInputTexFecha) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById(nombreIdInputTexFecha).src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', nombreIdElementoCal, null, null, null, '', nombreIdInputTexFecha, '', null, null, null, null, null, null, null, '', evento);
            }

            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoExtension(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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

            function comprobarCondicionesCalculo() {
                if (!cargaInicial) {
                    calcularTotalValidado();
                    /*
                    if (document.getElementById("codListaValidada").value == 'S') {
                        if (document.getElementById("codListaSubvencionable").value == 'S') {
                            var vali = parseFloat(document.getElementById("base").value.replace(",", ".")) + parseFloat(document.getElementById("importeIva").value.replace(",", "."));
                            document.getElementById("importeValidado").value = vali.toFixed(2).toString().replace(".", ",");
                        } else if (document.getElementById("codListaSubvencionable").value == 'N') {
                            document.getElementById("importeValidado").value = document.getElementById("base").value;
                        } else {
                            document.getElementById("importeValidado").value = '';
                        }
                        document.getElementById("importeValidado").disabled = false;
                    } else if (document.getElementById("codListaValidada").value == 'N') {
                        document.getElementById("importeValidado").value = '0';
                        document.getElementById("importeValidado").disabled = true;
                    } else {
                        document.getElementById("importeValidado").value = '';
                        document.getElementById("importeValidado").disabled = false;
                    }
                    */
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var Facturas = datos.tabla.lista;
                    if (Facturas.length > 0) {
                        pleaseWaitSinFrame('off');
                        var respuesta = new Array();
                        respuesta[0] = codigoOperacion;
                        respuesta[1] = Facturas;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        // Actualizar el campo suplementario en la pestana "Datos Suplementario", si esta cargado.
                        if(self.parent.opener.MINIMIS) {
                            var valorMinimisAplicarPantalla = self.parent.opener.MINIMIS.value;
                            var valorMinimisAplicarActualizado =  datos.tabla.valorMinimisAplicar;
                            if(valorMinimisAplicarPantalla != undefined && valorMinimisAplicarPantalla != valorMinimisAplicarActualizado)
                                self.parent.opener.MINIMIS.value = valorMinimisAplicarActualizado;
                        }
                        cerrarVentana();
                    } else {
                        mostrarError("5");
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
                pleaseWaitSinFrame('off');
                var msgtitle = '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
                switch (codigo) {
                    case "1":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                        break;
                    case "2":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                        break;
                    case "3":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                        break;
                    case "4":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                        break;
                    case "5":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                        break;
                    case "6":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                        break;
                    case "7":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>', msgtitle);
                        break;
                    case "8":
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>', msgtitle);
                        break;
                    default:
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                        break;
                }
            }

            function calcularTotalValidado(){
                // Total Factura
                let importeIvaCalculado = $('#base').val().replace(',','.') * ($('#descPorcentajeIva').val().replace(',','.') /100);
                let prorrataCalculado = (100 - $('#prorrata').val() )/100;
                let importeIvaMasProrrata = importeIvaCalculado * prorrataCalculado;
                $('#importeIva').val(importeIvaMasProrrata.toFixed(2).replace('.', ','))
                let totalCalculado = (Number($('#base').val().replace(',','.')) + importeIvaMasProrrata);
                $('#importeTotal').val(totalCalculado.toFixed(2).replace('.', ','));
                // Validado
                //$('#importeValidado').val(Number($('#baseValidado').val().replace(',','.'))  + $('#baseValidado').val().replace(',','.') * ($('#descListaPorIvaValidado').val() /100) * ((100 - $('#prorrataValidado').val() )/100))
                let baseValidada = $('#codListaValidada') != null && "S" == $('#codListaValidada').val() ? Number($('#baseValidado').val().replace(',','.')) : 0;
                let ivaValidada = $('#codListaSubvencionable') != null && "S" == $('#codListaSubvencionable').val() ? Number($('#descListaPorIvaValidado').val().replace(',','.')) : 0;
                let importeIvaValidadoCalculado = baseValidada * (ivaValidada /100);
                let prorrataValidadoCalculado = $('#codListaSubvencionable') != null && "S" == $('#codListaSubvencionable').val() ? (100 - Number($('#prorrataValidado').val()) )/100 : 0;
                let importeIvaValidadoMasProrrata = importeIvaValidadoCalculado * prorrataValidadoCalculado;
                $('#importeIvaValidado').val(importeIvaValidadoMasProrrata.toFixed(2).replace('.', ','));
                let totalValidadoCalculado = (baseValidada + importeIvaValidadoMasProrrata);
                $('#importeValidado').val(totalValidadoCalculado.toFixed(2).replace('.', ','));
            }
        </script>
    </head>
    <body class="bandaBody" onload="pleaseWaitSinFrame('off');">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.procesando")%>'/>
        </jsp:include>
        <div class="contenidoPantalla">            
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 95%;"/>
                    </div>
                    <fieldset id="formFacturas" name="formFacturas">
                        <!-- familia -->
                        <legend class="legendTema" align="center" id="titFamilia" name="titFamilia"><%=desFamilia%></legend>
                        <!-- orden -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.orden")%></div>
                            <div style="float: left;">
                                <input id="numOrden" name="numOrden" type="text" class="inputTexto" size="4" maxlength="4" disabled="true"
                                       value="<%=datosModif != null && datosModif.getNumOrden() != null ? datosModif.getNumOrden() : ""%>"/>
                            </div>
                        </div>                       
                        <!-- tipo gasto -->
                        <div class="lineaFormulario"> 
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.gasto")%></div>
                            <div>
                                <input type="text" name="codListaGasto" id="codListaGasto" size="1" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" disabled="true"/>
                                <input type="text" name="descListaGasto" id="descListaGasto" size="20" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaGasto" name="anchorListaGasto">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>                        
                        <!-- proveedor -->
                        <div class="lineaFormulario"> 
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.proveedor")%></div>
                            <div style="float: left;">
                                <input id="proveedor" name="proveedor" type="text" class="inputTextoObligatorio" size="90" maxlength="150" disabled="true"
                                       value="<%=datosModif != null && datosModif.getProveedor() != null ? datosModif.getProveedor() : ""%>"/>
                            </div>
                        </div>                        
                        <!-- numero factura -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.numFactura")%></div>
                            <div style="float: left;">
                                <input id="numFactura" name="numFactura" type="text" class="inputTextoObligatorio" size="90" maxlength="150" disabled="true"
                                       value="<%=datosModif != null && datosModif.getNumFactura() != null ? datosModif.getNumFactura() : ""%>"/>
                            </div>
                        </div>                        
                        <!-- fecha emision -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.fecEmision")%></div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio"  disabled="true"
                                       id="fechaEmision" name="fechaEmision" 
                                       maxlength="10"  size="10"
                                       value="<%=fechaEmision%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick='mostrarCalendarios(event, "fechaEmision", "calFechaEmision");
                                        return false;' style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaEmision" name="calFechaEmision" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>                        
                        <!-- fecha pago -->
                        <div class="lineaFormulario"> 
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.fecPago")%></div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio"  disabled="true"
                                       id="fechaPago" name="fechaPago"
                                       maxlength="10"  size="10"
                                       value="<%=fechaPago%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick='mostrarCalendarios(event, "fechaPago", "calFechaPago");
                                        return false;' style="text-decoration:none;">
                                    <IMG height="17" id="calFechaPago" name="calFechaPago" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>                        
                        <!-- base -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.base")%></div>
                            <div style="float: left;">
                                <input id="base" name="base" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);" onkeyup="return SoloDecimales(event);"
                                       value="<%=datosModif != null && datosModif.getImporteBase() != null ? datosModif.getImporteBase().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.base.validado")%></div>
                            <div style="float: left;">
                                <input id="baseValidado" name="baseValidado" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;"
                                       onchange="reemplazarPuntos(this);calcularTotalValidado();" onkeyup="return SoloDecimales(event);"
                                       value="<%=datosModif != null && datosModif.getImporteBaseValidado() != null ? datosModif.getImporteBaseValidado().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <!-- IVA -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.facturas.tabla.col8")%></div>
                            <div style="float: left;">
                                <input id="descPorcentajeIva" name="descPorcentajeIva" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);calcularTotalValidado();" onkeyup="return SoloDecimales(event);"
                                       value="<%=datosModif != null && datosModif.getDescPorcentajeIva() != null ? datosModif.getDescPorcentajeIva().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.porcentaje.iva.validado")%>
                            </div>
                            <div>
                                <input type="text" name="codListaPorIvaValidado" id="codListaPorIvaValidado" size="1" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaPorIvaValidado" id="descListaPorIvaValidado" size="4" class="inputComboObligatorio" readonly="true" value=""/>
                                <a href="" id="anchorListaPorIvaValidado" name="anchorListaPorIvaValidado">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"/>
                                </a>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.facturas.tabla.col12")%></div>
                            <div style="float: left;">
                                <input id="prorrata" name="prorrata" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);calcularTotalValidado();" onkeyup="return SoloDigitos(this);"
                                       value="<%=datosModif != null && datosModif.getProrrata() != null ? datosModif.getProrrata().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.porcentaje.prorrata.iva.validado")%></div>
                            <div style="float: left;">
                                <input id="prorrataValidado" name="prorrataValidado" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;"
                                       onchange="reemplazarPuntos(this);calcularTotalValidado();" onkeyup="return SoloDigitos(this);"
                                       value="<%=datosModif != null && datosModif.getProrrataValidado() != null ? datosModif.getProrrataValidado().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.iva")%></div>
                            <div style="float: left;">
                                <input id="importeIva" name="importeIva" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);" onkeyup="return SoloDecimales(event);"  
                                       value="<%=datosModif != null && datosModif.getImporteIva() != null ? datosModif.getImporteIva().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.iva.validado")%></div>
                            <div style="float: left;">
                                <input id="importeIvaValidado" name="importeIvaValidado" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);" onkeyup="return SoloDecimales(event);"
                                       value="<%=datosModif != null && datosModif.getImporteIvaValidado() != null ? datosModif.getImporteIvaValidado().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <!-- Factura Validada -->  
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.facValidada")%>
                            </div>
                            <div>
                                <input type="text" name="codListaValidada" id="codListaValidada" size="1" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaValidada" id="descListaValidada" size="4" class="inputComboObligatorio" readonly="true" value=""/>
                                <a href="" id="anchorListaValidada" name="anchorListaValidada">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"/>
                                </a>
                            </div>
                        </div>  
                        <!-- IVA Subvencionable -->  
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.subvencionable")%>
                            </div>
                            <div>
                                <input type="text" name="codListaSubvencionable" id="codListaSubvencionable" size="1" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaSubvencionable" id="descListaSubvencionable" size="4" class="inputComboObligatorio" readonly="true" value=""/>
                                <a href="" id="anchorListaSubvencionable" name="anchorListaSubvencionable">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"/>
                                </a>
                            </div>
                        </div>
                        <!-- Importe Validado -->
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalFamilia")%></div>
                            <div style="float: left;">
                                <input id="importeTotal" name="importeTotal" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);" onkeyup="return SoloDecimales(event);"
                                       value="<%=datosModif != null && datosModif.getImporteTotal() != null ? datosModif.getImporteTotal().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalValidado")%></div>
                            <div style="float: left;">
                                <input id="importeValidado" name="importeValidado" type="text" class="inputTextoObligatorio" size="10" maxlength="9" style="text-align: right;" disabled="true"
                                       onchange="reemplazarPuntos(this);" onkeyup="return SoloDecimales(event);"  
                                       value="<%=datosModif != null && datosModif.getImporteVali() != null ? datosModif.getImporteVali().toString().replaceAll("\\.", ",") : ""%>"/>
                            </div>
                        </div> 
                        <!--  Motivo No Validada -->  
                        <div class="lineaFormulario">
                            <div class="etiquetaInin"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.motivo")%>
                            </div>
                            <div>
                                <input type="text" name="codListaMotivo" id="codListaMotivo" size="2" maxlength="2" class="inputTexto" value="" onkeyup="SoloDigitos(this);"/>
                                <input type="text" name="descListaMotivo" id="descListaMotivo" size="40" class="inputCombo" readonly="true" value=""/>
                                <a href="" id="anchorListaMotivo" name="anchorListaMotivo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"/>
                                </a>
                            </div>
                        </div> 
                    </fieldset>
                    <div class="lineaFormulario">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>   
            </form>
            <script type="text/javascript">
                /* desplegable tipo gasto */
                listaCodigosGasto[0] = "";
                listaDescripcionesGasto[0] = "";
                contador = 0;
                <logic:iterate id="gasto" name="listaTipoGasto" scope="request">
                listaCodigosGasto[contador] = ['<bean:write name="gasto" property="des_val_cod" />'];
                listaDescripcionesGasto[contador] = ['<bean:write name="gasto" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaGasto = new Combo("ListaGasto");
                comboListaGasto.addItems(listaCodigosGasto, listaDescripcionesGasto);
                comboListaGasto.change = cargarDatosGasto;

                /* desplegable factura validada  */
                listaCodigosValidada[0] = "";
                listaDescripcionesValidada[0] = "";
                contador = 0;
                <logic:iterate id="validada" name="listaBool" scope="request"> // nombre en Java de la lista
                listaCodigosValidada[contador] = ['<bean:write name="validada" property="des_val_cod" />'];
                listaDescripcionesValidada[contador] = ['<bean:write name="validada" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboValidada = new Combo("ListaValidada");
                comboValidada.addItems(listaCodigosValidada, listaDescripcionesValidada);
                comboValidada.change = cargarDatosValidada;

                /* desplegable porcentaje iva validado  */
                listaCodigosPorIvaValidado[0] = "";
                listaDescripcionesPorIvaValidado[0] = "";
                contador = 0;
                <logic:iterate id="porIvaValidado" name="listaPorcentajes" scope="request"> // nombre en Java de la lista
                listaCodigosPorIvaValidado[contador] = ['<bean:write name="porIvaValidado" property="des_val_cod" />'];
                listaDescripcionesPorIvaValidado[contador] = ['<bean:write name="porIvaValidado" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboPorIvaValidado = new Combo("ListaPorIvaValidado");
                comboPorIvaValidado.addItems(listaCodigosPorIvaValidado, listaDescripcionesPorIvaValidado);
                comboPorIvaValidado.change = cargarDatosPorIvaValidado;

                /* desplegable IVA Subvencionable  */
                listaCodigosSubvencionable[0] = "";
                listaDescripcionesSubvencionable[0] = "";
                contador = 0;
                <logic:iterate id="Subvencionable" name="listaBool" scope="request"> // nombre en Java de la lista
                listaCodigosSubvencionable[contador] = ['<bean:write name="Subvencionable" property="des_val_cod" />'];
                listaDescripcionesSubvencionable[contador] = ['<bean:write name="Subvencionable" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboSubvencionable = new Combo("ListaSubvencionable");
                comboSubvencionable.addItems(listaCodigosSubvencionable, listaDescripcionesSubvencionable);
                comboSubvencionable.change = cargarDatosSubvencionable;

                /* desplegable  Motivo  No Validada */
                contador = 0;

                <logic:iterate id="Motivo" name="listaMotivo" scope="request"> // nombre en Java de la lista
                listaCodigosMotivo[contador] = ['<bean:write name="Motivo" property="des_val_cod"/>'];
                listaDescripcionesMotivo[contador] = ['<bean:write name="Motivo" property="des_nom"/>'];
                contador++;
                </logic:iterate>
                comboMotivo = new Combo("ListaMotivo");
                comboMotivo.addItems(listaCodigosMotivo, listaDescripcionesMotivo);
                comboMotivo.change = cargarDatosMotivo;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    cargaInicial = true;
                    rellenarDesplegables();
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.factura.edicion")%>';
                    cargaInicial = false;
                } else {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.factura.alta")%>';
                    document.getElementById('numOrden').value = '<%=numOrden%>';
                    desbloquearCampos();
                }
                calcularTotalValidado();
            </script>
        </div>
    </body>
</html>
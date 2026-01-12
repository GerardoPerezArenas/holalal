<%-- 
    Document   : detalleFacturas
    Created on : 12-jul-2024, 14:57:24
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.i18n.MeLanbide90I18n"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FamiliaSolicitadaVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
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
            MeLanbide90I18n meLanbide90I18n = MeLanbide90I18n.getInstance();
            
            String numExpediente = (String)request.getAttribute("numExp");
            String modalidad = (String)request.getAttribute("modalidad");
            Integer familiasSol = (Integer)request.getAttribute("familias");
            
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide90/melanbide90.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide90/IninUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var modalidad = '<%=modalidad != null ? modalidad : ""%>';
            var familiasSol = <%=familiasSol%>;

            var lista;
            var tablaSeleccionada;
            var parametrosLlamada = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE90'
                , operacion: null
                , tipo: 0
                , numExp: '<%=numExpediente%>'
                , id: null
                , codFamilia: null
                , numFamilias:<%=familiasSol%>
            };

            var totalBase01 = 0;
            var totalIva01 = 0;
            var totalTotal01 = 0;
            var totalVali01 = 0;

            var totalBase02 = 0;
            var totalIva02 = 0;
            var totalTotal02 = 0;
            var totalVali02 = 0;

            var totalBase03 = 0;
            var totalIva03 = 0;
            var totalTotal03 = 0;
            var totalVali03 = 0;

            var totalBaseExp = 0;
            var totalIvaExp = 0;
            var totalTotalExp = 0;
            var totalValiExp = 0;

            function nuevaFactura(familia) {
                familiaElegida(familia);
                if (tablaSeleccionada != undefined) {
                    // pasar el ultimo numero orden de esa familia 
                    // lista[lista.lenght -1][3]
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE90&operacion=cargarMantenimientoFactura&tipo=0&nuevo=1&numExp=<%=numExpediente%>&ultimaFactura='
                            + lista[lista.length - 1][3] + '&codFamilia=' + lista[lista.length - 1][1], 750, 900, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTabla(result[1], familia);
                            }
                        }
                    });
                }
            }

            function modificarFactura(familia) {
                familiaElegida(familia);
                if (tablaSeleccionada != undefined) {
                    if (tablaSeleccionada.selectedIndex != -1) {
                        lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE90&operacion=cargarMantenimientoFactura&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id='
                                + lista[tablaSeleccionada.selectedIndex][0] + '&codFamilia=' + lista[tablaSeleccionada.selectedIndex][1], 750, 900, 'no', 'no', function (result) {
                            if (result != undefined) {
                                if (result[0] == '0') {
                                    recargarTabla(result[1], familia);
                                }
                            }
                        });
                    } else {
                        jsp_alerta('A', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }
            }

            function eliminarFactura(familia) {
                familiaElegida(familia);
                if (tablaSeleccionada != undefined) {
                    if (tablaSeleccionada.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarFactura")%>');
                        if (resultado == 1) {
                            var dataParameter = $.extend({}, parametrosLlamada);
                            dataParameter.operacion = 'eliminarFactura';
                            dataParameter.id = lista[tablaSeleccionada.selectedIndex][0];
                            dataParameter.codFamilia = lista[tablaSeleccionada.selectedIndex][1];
                            try {
                                $.ajax({
                                    url: url,
                                    type: 'POST',
                                    async: true,
                                    data: dataParameter,
                                    beforeSend: function () {
                                        pleaseWait('on');
                                    },
                                    success: function (respuesta) {
                                        var datos = JSON.parse(respuesta);
                                        var codigoOperacion = datos.tabla.codigoOperacion;
                                        if (codigoOperacion == "0") {
                                            var facturas = datos.tabla.lista;
                                            if (facturas.length > 0) {
                                                pleaseWait('off');
                                                recargarTabla(facturas, familia);
                                            } else {
                                                mostrarErrorPeticion("5");
                                            }
                                        } else {
                                            mostrarErrorPeticion(codigoOperacion);
                                        }
                                    },
                                    error: function () {
                                        mostrarErrorPeticion("6");
                                    }
                                });
                            } catch (Err) {
                                mostrarErrorPeticion();
                            }
                        }
                    } else {
                        jsp_alerta('A', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.noTabla")%>');
                }
            }

            function volcarDatos() {
                var dataParameter = $.extend({}, parametrosLlamada);
                dataParameter.operacion = 'volcarDatos';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: dataParameter,
                        beforeSend: function () {
                            pleaseWait('on');
                        },
                        success: function (respuesta) {
                            var datos = JSON.parse(respuesta);
                            var codigoOperacion = datos.tabla.codigoOperacion;
                            if (codigoOperacion == "0") {
                                jsp_alerta('A', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "error.tituloOK")%>');
                                recargarDatosExpediente();
                            } else if (codigoOperacion == "01") {
                                jsp_alerta('A', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "msg.minimisExp")%>', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "error.tituloOK")%>');
                                recargarDatosExpediente();
                            } else {
                                mostrarErrorPeticion(codigoOperacion);
                            }
                        },
                        error: function () {
                            mostrarErrorPeticion("2");
                        }
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            function pulsarExportarExcelListaFacturas() {
                window.open(url + '?tarea=preparar&modulo=MELANBIDE90&operacion=generarExcelListafacturas&tipo=0&numExp=<%=numExpediente%>', "_blank");
            }

            function mostrarErrorPeticion(codigo) {
                pleaseWait('off');
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
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.noTabla")%>', msgtitle);
                        break;
                    default:
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                        break;
                }
            }

            function familiaElegida(familia) {
                switch (familia) {
                    case 1:
                        lista = listaFamilia01;
                        tablaSeleccionada = tablaFamilia01;
                        break;
                    case 2:
                        lista = listaFamilia02;
                        tablaSeleccionada = tablaFamilia02;
                        break;
                    case 3:
                        lista = listaFamilia03;
                        tablaSeleccionada = tablaFamilia03;
                        break;
                    default:
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.noTabla")%>');
                        break;
                }
            }

            function crearTablas() {
                crearTablaF01();
                crearTablaF02();
                crearTablaF03();
            }

            function crearTablaF01() {
                tablaFamilia01 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaFamilia01'));
                tablaFamilia01.addColumna('0', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col0")%>'); // ID
                tablaFamilia01.addColumna('20', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col1")%>'); // Orden
                tablaFamilia01.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col2")%>'); // Tipo Gasto
                tablaFamilia01.addColumna('200', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col3")%>'); // Proveedor
                tablaFamilia01.addColumna('125', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col4")%>'); // Num Factura
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col5")%>'); // Fecha Emision
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col6")%>'); // Fecha Pago
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col7")%>'); // Base
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col17")%>'); // Base Validado
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col8")%>'); // I.V.A. %
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col18")%>'); // I.V.A. %  Validado
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col9")%>'); // IVA
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col19")%>'); // IVA Validado
                //tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col10")%>'); // Exento
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col12")%>'); // Prorrata IVA %
                tablaFamilia01.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col20")%>'); // Prorrata IVA % Validado
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col11")%>'); // Total
                tablaFamilia01.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col13")%>'); // Validada
                tablaFamilia01.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col14")%>'); // IVA subvencionable  
                tablaFamilia01.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col15")%>'); //  Imp validado
                tablaFamilia01.addColumna('50', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col16")%>'); // motivo NO validada

                tablaFamilia01.displayCabecera = true;
                tablaFamilia01.height = '400';
                tablaFamilia01.lineas = listaFamilia01Tabla;
                tablaFamilia01.displayTablaConTooltips(listaFamilia01Titulos);
            }

            function crearTablaF02() {
                tablaFamilia02 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaFamilia02'));
                tablaFamilia02.addColumna('0', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col0")%>'); // ID
                tablaFamilia02.addColumna('20', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col1")%>'); // Orden
                tablaFamilia02.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col2")%>'); // Tipo Gasto
                tablaFamilia02.addColumna('200', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col3")%>'); // Proveedor
                tablaFamilia02.addColumna('125', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col4")%>'); // Num Factura
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col5")%>'); // Fecha Emision
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col6")%>'); // Fecha Pago
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col7")%>'); // Base
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col17")%>'); // Base Validado
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col8")%>'); // I.V.A. %
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col18")%>'); // I.V.A. %  Validado
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col9")%>'); // IVA
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col19")%>'); // IVA Validado
                //tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col10")%>'); // Exento
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col12")%>'); // Prorrata IVA %
                tablaFamilia02.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col20")%>'); // Prorrata IVA % Validado
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col11")%>'); // Total
                tablaFamilia02.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col13")%>'); // Validada
                tablaFamilia02.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col14")%>'); // IVA subvencionable  
                tablaFamilia02.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col15")%>'); //  Imp validado
                tablaFamilia02.addColumna('50', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col16")%>'); // motivo NO validada

                tablaFamilia02.displayCabecera = true;
                tablaFamilia02.height = '400';
                tablaFamilia02.lineas = listaFamilia02Tabla;
                tablaFamilia02.displayTablaConTooltips(listaFamilia02Titulos);
            }

            function crearTablaF03() {
                tablaFamilia03 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaFamilia03'));
                tablaFamilia03 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaFamilia03'));
                tablaFamilia03.addColumna('0', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col0")%>'); // ID
                tablaFamilia03.addColumna('20', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col1")%>'); // Orden
                tablaFamilia03.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col2")%>'); // Tipo Gasto
                tablaFamilia03.addColumna('200', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col3")%>'); // Proveedor
                tablaFamilia03.addColumna('125', 'left', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col4")%>'); // Num Factura
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col5")%>'); // Fecha Emision
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col6")%>'); // Fecha Pago
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col7")%>'); // Base
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col17")%>'); // Base Validado
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col8")%>'); // I.V.A. %
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col18")%>'); // I.V.A. %  Validado
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col9")%>'); // IVA
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col19")%>'); // IVA Validado
                //tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col10")%>'); // Exento
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col12")%>'); // Prorrata IVA %                   
                tablaFamilia03.addColumna('65', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col20")%>'); // Prorrata IVA % Validado
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col11")%>'); // Total
                tablaFamilia03.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col13")%>'); // Validada
                tablaFamilia03.addColumna('40', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col14")%>'); // IVA subvencionable  
                tablaFamilia03.addColumna('65', 'right', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col15")%>'); //  Imp validado
                tablaFamilia03.addColumna('50', 'center', '<%=meLanbide90I18n.getMensaje(idiomaUsuario, "label.facturas.tabla.col16")%>'); // motivo NO validada

                tablaFamilia03.displayCabecera = true;
                tablaFamilia03.height = '400';
                tablaFamilia03.lineas = listaFamilia03Tabla;
                tablaFamilia03.displayTablaConTooltips(listaFamilia03Titulos);
            }

            function recargarTabla(facturas, familia) {
                var fila;
                var listaFacturas = new Array();
                var listaTablaFacturas = new Array();
                var listaFacturasTool = new Array();

                var descFamilia;

                var baseTabla = 0;
                var ivaTabla = 0;
                var totalTabla = 0;
                var valiTabla = 0;

                var elementoBase;
                var elementoIva;
                var elementoTotal;
                var elementoVali;

                for (var i = 0; i < facturas.length; i++) {
                    fila = facturas[i];
                    if (<%=idiomaUsuario%> == 4) {
                        descFamilia = fila.descFamiliaEus;
                    } else {
                        descFamilia = fila.descFamiliaCas;
                    }
                    listaFacturas[i] = [
                        fila.id,
                        fila.familia, descFamilia,
                        fila.numOrden,
                        fila.tipoGasto, fila.descTipoGasto,
                        fila.proveedor,
                        fila.numFactura,
                        fila.fecEmisionStr,
                        fila.fecPagoStr,
                        formatNumero(fila.importeBase),
                        formatNumero(fila.importeBaseValidado),
                        fila.porcentajeIva, fila.descPorcentajeIva,
                        fila.porcentajeIvaValidado, fila.descPorcentajeIvaValidado,
                        formatNumero(fila.importeIva),
                        formatNumero(fila.importeIvaValidado),
                        //fila.exento,
                        formatNumero(fila.prorrata),
                        formatNumero(fila.prorrataValidado),
                        formatNumero(fila.importeTotal),
                        fila.validada, fila.descValidada,
                        fila.ivaSub, fila.descIvaSub,
                        formatNumero(fila.importeVali),
                        fila.motNoVal, fila.descMotNoVal
                    ];

                    listaTablaFacturas[i] = [
                        fila.id,
                        fila.numOrden,
                        fila.tipoGasto,
                        fila.proveedor,
                        fila.numFactura,
                        fila.fecEmisionStr,
                        fila.fecPagoStr,
                        formatNumero(fila.importeBase) + ' \u20ac',
                        formatNumero(fila.importeBaseValidado) + ' \u20ac',
                        (fila.descPorcentajeIva != undefined ? fila.descPorcentajeIva : ''),
                        (fila.descPorcentajeIvaValidado != undefined ? fila.descPorcentajeIvaValidado : ''),
                        formatNumero(fila.importeIva) + ' \u20ac',
                        formatNumero(fila.importeIvaValidado) + ' \u20ac',
                        //fila.exento,
                        formatNumero(fila.prorrata),
                        formatNumero(fila.prorrataValidado),
                        formatNumero(fila.importeTotal) + ' \u20ac',
                        fila.descValidada,
                        fila.descIvaSub,
                        formatNumero(fila.importeVali) + ' \u20ac',
                        (fila.motNoVal != undefined ? fila.motNoVal : '')
                    ];

                    listaFacturasTool[i] = [
                        fila.id,
                        fila.numOrden,
                        fila.descTipoGasto,
                        fila.proveedor,
                        fila.numFactura,
                        fila.fecEmisionStr,
                        fila.fecPagoStr,
                        formatNumero(fila.importeBase) + ' \u20ac',
                        formatNumero(fila.importeBaseValidado) + ' \u20ac',
                        (fila.descPorcentajeIva != undefined ? fila.descPorcentajeIva : ''),
                        (fila.descPorcentajeIvaValidado != undefined ? fila.descPorcentajeIvaValidado : ''),
                        formatNumero(fila.importeIva) + ' \u20ac',
                        formatNumero(fila.importeIvaValidado) + ' \u20ac',
                        //fila.exento,
                        formatNumero(fila.prorrata),
                        formatNumero(fila.prorrataValidado),
                        formatNumero(fila.importeTotal) + ' \u20ac',
                        fila.descValidada,
                        fila.descIvaSub,
                        formatNumero(fila.importeVali) + ' \u20ac',
                        (fila.descMotNoVal != undefined ? fila.descMotNoVal : '')
                    ];
                    // suma importes
                    baseTabla += fila.importeBase;
                    ivaTabla += fila.importeIva;
                    totalTabla += fila.importeTotal;
                    valiTabla += fila.importeVali;
                }

                switch (familia) {
                    case 1:
                        listaFamilia01 = listaFacturas;
                        listaFamilia01Tabla = listaTablaFacturas;
                        listaFamilia01Titulos = listaFacturasTool;
                        crearTablaF01();
                        elementoBase = "base01";
                        elementoIva = "iva01";
                        elementoTotal = "total01";
                        elementoVali = "vali01";
                        totalBase01 = baseTabla;
                        totalIva01 = ivaTabla;
                        totalTotal01 = totalTabla;
                        totalVali01 = valiTabla;
                        break;

                    case 2:
                        listaFamilia02 = listaFacturas;
                        listaFamilia02Tabla = listaTablaFacturas;
                        listaFamilia02Titulos = listaFacturasTool;
                        crearTablaF02();
                        elementoBase = "base02";
                        elementoIva = "iva02";
                        elementoTotal = "total02";
                        elementoVali = "vali02";
                        totalBase02 = baseTabla;
                        totalIva02 = ivaTabla;
                        totalTotal02 = totalTabla;
                        totalVali02 = valiTabla;
                        break;

                    case 3:
                        listaFamilia03 = listaFacturas;
                        listaFamilia03Tabla = listaTablaFacturas;
                        listaFamilia03Titulos = listaFacturasTool;
                        crearTablaF03();
                        elementoBase = "base03";
                        elementoIva = "iva03";
                        elementoTotal = "total03";
                        elementoVali = "vali03";
                        totalBase03 = baseTabla;
                        totalIva03 = ivaTabla;
                        totalTotal03 = totalTabla;
                        totalVali03 = valiTabla;
                        break;

                    default:
                        jsp_alerta("A", '<%=meLanbide90I18n.getMensaje(idiomaUsuario,"error.noTabla")%>');
                        break;
                }
                document.getElementById(elementoBase).value = formatNumero(baseTabla) + ' \u20ac';
                document.getElementById(elementoIva).value = formatNumero(ivaTabla) + ' \u20ac';
                document.getElementById(elementoTotal).value = formatNumero(totalTabla) + ' \u20ac';
                document.getElementById(elementoVali).value = formatNumero(valiTabla) + ' \u20ac';
                calcularTotalesExpediente();
            }

            function calcularTotalesExpediente() {
                totalBaseExp = totalBase01 + totalBase02 + totalBase03;
                totalIvaExp = totalIva01 + totalIva02 + totalIva03;
                totalTotalExp = totalTotal01 + totalTotal02 + totalTotal03;
                totalValiExp = totalVali01 + totalVali02 + totalVali03;
                pintarTotalExpediente();
            }

            function pintarTotalesFamilias() {
                document.getElementById("base01").value = formatNumero(totalBase01) + ' \u20ac';
                document.getElementById("iva01").value = formatNumero(totalIva01) + ' \u20ac';
                document.getElementById("total01").value = formatNumero(totalTotal01) + ' \u20ac';
                document.getElementById("vali01").value = formatNumero(totalVali01) + ' \u20ac';

                document.getElementById("base02").value = formatNumero(totalBase02) + ' \u20ac';
                document.getElementById("iva02").value = formatNumero(totalIva02) + ' \u20ac';
                document.getElementById("total02").value = formatNumero(totalTotal02) + ' \u20ac';
                document.getElementById("vali02").value = formatNumero(totalVali02) + ' \u20ac';

                document.getElementById("base03").value = formatNumero(totalBase03) + ' \u20ac';
                document.getElementById("iva03").value = formatNumero(totalIva03) + ' \u20ac';
                document.getElementById("total03").value = formatNumero(totalTotal03) + ' \u20ac';
                document.getElementById("vali03").value = formatNumero(totalVali03) + ' \u20ac';

                calcularTotalesExpediente();
            }

            function pintarTotalExpediente() {
                document.getElementById("baseTotal").value = formatNumero(totalBaseExp) + ' \u20ac';
                document.getElementById("ivaTotal").value = formatNumero(totalIvaExp) + ' \u20ac';
                document.getElementById("totalTotal").value = formatNumero(totalTotalExp) + ' \u20ac';
                document.getElementById("valiTotal").value = formatNumero(totalValiExp) + ' \u20ac';
            }

            function recargarDatosExpediente() {
                document.forms[0].opcion.value = "cargarPestTram";
                document.forms[0].target = "mainFrame";
                document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
                document.forms[0].submit();
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage902" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana902"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaFacturas")%></h2>
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage902"));</script>
            <h2 class="legendTema" align="center" id="pestanaFact" style="margin-top: 15px;"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.tituloFacturas")%></h2>
            <div id="divGeneral">
                <fieldset id="facturas" name="facturas" style="width:98%; border: double">
                    <legend class="legendTema" align="center" id="titModalidad"></legend>
                    <!-- familia01 -->
                    <fieldset id="familia01" name="familia01" style="margin-top: 15px;">
                        <legend class="legendTema" align="left" id="titFamilia01"></legend>
                        <div id="listaFamilia01" align="center"></div>
                        <div id="capaFactura01" class="botonera">
                            <input type="button" id="btnNuevaFactura01" name="btnNuevaFactura01" class="botonGeneral"  value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="nuevaFactura(1);">
                            <input type="button" id="btnModificarFactura01" name="btnModificarFactura01" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="modificarFactura(1);">
                            <input type="button" id="btnEliminarFactura01" name="btnEliminarFactura01" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="eliminarFactura(1);">                                              
                        </div>
                        <div class="lineaFormulario" style="margin-top: 15px;">
                            <div style="width: 55%; text-align: right; float: left;padding-top: 5px;" class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalFamilia")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="base01"  name="base01" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="iva01" name="iva01" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>  
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="total01" name="total01" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>
                            <div style="width: 8%; text-align: right; float: left;padding-top: 5px;"class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalValidado")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="vali01" name="vali01" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 5%; text-align: right; float: left;"></div>
                        </div>
                    </fieldset>
                    <!-- familia02 -->
                    <fieldset id="familia02" name="familia02" style="margin-top: 15px;">
                        <legend class="legendTema" align="left" id="titFamilia02"></legend>
                        <div id="listaFamilia02" align="center"></div>
                        <div id="capaFactura02" class="botonera">
                            <input type="button" id="btnNuevaFactura02" name="btnNuevaFactura02" class="botonGeneral"  value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="nuevaFactura(2);">
                            <input type="button" id="btnModificarFactura02" name="btnModificarFactura02" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="modificarFactura(2);">
                            <input type="button" id="btnEliminarFactura02" name="btnEliminarFactura02" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="eliminarFactura(2);">
                        </div>
                        <div class="lineaFormulario" style="margin-top: 15px;">
                            <div style="width: 55%; text-align: right; float: left;padding-top: 5px;" class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalFamilia")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="base02"  name="base02" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="iva02" name="iva02" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>  
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="total02" name="total02" type="text" size="9" class="inputTexto total" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 8%; text-align: right; float: left;padding-top: 5px;"class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalValidado")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="vali02" name="vali02" type="text" size="9" class="inputTexto total" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 5%; text-align: right; float: left;"></div>
                        </div><br>

                    </fieldset>
                    <!-- familia03 -->
                    <fieldset id="familia03" name="familia03" style="margin-top: 15px;">
                        <legend class="legendTema" align="left" id="titFamilia03"></legend>
                        <div id="listaFamilia03" align="center"></div>
                        <div id="capaFactura03" class="botonera">
                            <input type="button" id="btnNuevaFactura03" name="btnNuevaFactura03" class="botonGeneral"  value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="nuevaFactura(3);">
                            <input type="button" id="btnModificarFactura" name="btnModificarFactura03" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="modificarFactura(3);">
                            <input type="button" id="btnEliminarFactura03" name="btnEliminarFactura03" class="botonGeneral" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="eliminarFactura(3);">
                        </div> 
                        <div class="lineaFormulario" style="margin-top: 15px;">
                            <div style="width: 55%; text-align: right; float: left;padding-top: 5px;" class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalFamilia")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="base03"  name="base03" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="iva03" name="iva03" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>  
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="total03" name="total03" type="text"class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 8%; text-align: right; float: left;padding-top: 5px;"class="total etiqueta"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalValidado")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="vali03" name="vali03" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 5%; text-align: right; float: left;"></div>
                        </div> <br>

                    </fieldset>  
                    <!-- total facturas -->
                    <fieldset id="totalFacturas" name="totalFacturas" style="margin-top: 15px; border-color: black;">
                        <div class="lineaFormulario">
                            <div class="botonera" style="width: 40%;float: left;">
                                <input type="button" id="btnVolcarDatos" name="btnVolcarDatos" class="botonMasLargo"  value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.volcar")%>" onclick="volcarDatos();">
                            </div>
                            <div style="width: 15%; text-align: right; float: left;padding-top: 5px;" class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalFacturas")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="baseTotal"  name="baseTotal" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div >
                                    <input id="ivaTotal" name="ivaTotal" type="text" class="inputTexto total" size="9" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div>  
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="totalTotal" name="totalTotal" type="text" size="9" class="inputTexto total" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                            <div style="width: 8%; text-align: right; float: left;padding-top: 5px;"class="etiqueta total"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"label.totalValidado")%></div>
                            <div style="width: 8%; text-align: right; float: left;">
                                <div>
                                    <input id="valiTotal" name="valiTotal" type="text" size="9" class="inputTexto total" style="text-align:right; color: black;" disabled/>
                                </div>
                            </div> 
                        </div>

                    </fieldset> 
                    <div class="botonera">
                        <input type="button" id="btnexportarFactura" name="btnexportarFactura" class="botonMasLargo" style="margin-top: 20px;" value="<%=meLanbide90I18n.getMensaje(idiomaUsuario, "btn.exportar")%>" onclick="pulsarExportarExcelListaFacturas();">
                    </div>  
                    <!-- mensajeVacio -->
                    <fieldset id="mensajeVacio" name="mensajeVacio" style="margin-top: 15px; border-color: red; display: none;">
                        <div class="lineaFormulario">
                            <legend class="legendRojo" align="center" id="sinFacturas"><%=meLanbide90I18n.getMensaje(idiomaUsuario,"msg.sinFacturas")%></legend>
                        </div>
                    </fieldset>
                </fieldset>
            </div> 

            <script  type="text/javascript">
                document.getElementById("titModalidad").innerText = modalidad;

                var tablaFamilia01;
                var listaFamilia01 = new Array();
                var listaFamilia01Tabla = new Array();
                var listaFamilia01Titulos = new Array();

                var tablaFamilia02;
                var listaFamilia02 = new Array();
                var listaFamilia02Tabla = new Array();
                var listaFamilia02Titulos = new Array();

                var tablaFamilia03;
                var listaFamilia03 = new Array();
                var listaFamilia03Tabla = new Array();
                var listaFamilia03Titulos = new Array();

                <%
                    // datos de las familias
                    FamiliaSolicitadaVO famVO = null;
                    List<FamiliaSolicitadaVO> ListaFam = null;
                
                    if(request.getAttribute("listaFamilias")!=null) {
                        ListaFam = (List<FamiliaSolicitadaVO>)request.getAttribute("listaFamilias");
                    }
                
                    if (ListaFam!= null && ListaFam.size() >0) {
                                
                        //  facturas por familias
                        FacturaVO facVO = null;
                        List<FacturaVO> List01 = null;
                        List<FacturaVO> List02 = null;
                        List<FacturaVO> List03 = null;
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String familiaTitu = "";
                        String base = "";
           
                //             tablaFamilia01
            
                        if(request.getAttribute("listaFacturas01")!=null) {
                            List01 = (List<FacturaVO>)request.getAttribute("listaFacturas01");
                   
                            if (List01!= null && List01.size() >0) {                                             
                                for (int indice=0;indice<List01.size();indice++) {
                                    facVO = List01.get(indice);

                                    String Familia = "";
                                    if(facVO.getFamilia()!=null) {
                                        Familia = facVO.getFamilia();
                                    }
                                    String DescFamilia = "";      
                                    if(idiomaUsuario==4){
                                        DescFamilia=facVO.getDescFamiliaEus();
                                    } else {
                                        DescFamilia=facVO.getDescFamiliaCas();
                                    }                                

                                    String DescTipoGasto="";
                                    if(facVO.getDescTipoGasto()!=null) {
                                        DescTipoGasto = facVO.getDescTipoGasto();
                                    }                    

                                    String FecEmision="";
                                    if(facVO.getFecEmision()!=null) {
                                        FecEmision = dateFormat.format(facVO.getFecEmision());
                                    }                    
                                    String FecPago="";
                                    if(facVO.getFecPago()!=null) {
                                        FecPago = dateFormat.format(facVO.getFecPago());
                                    }             
                                    
                                    String Validada = "";
                                    if(facVO.getValidada()!=null) {
                                        Validada = facVO.getValidada();
                                    }   
                                    String DescValidada="";
                                    if(facVO.getDescValidada()!=null) {
                                        DescValidada = facVO.getDescValidada();
                                    }
                                    
                                    String IvaSub = "";
                                    if(facVO.getIvaSub()!=null) {
                                        IvaSub = facVO.getIvaSub();
                                    }                                    
                                    String DescIvaSub="";
                                    if(facVO.getDescIvaSub()!=null) {
                                        DescIvaSub = facVO.getDescIvaSub();
                                    }
                                    
                                    String MotNoVal = "";
                                    if(facVO.getMotNoVal()!=null) {
                                        MotNoVal = facVO.getMotNoVal();
                                    }                                    
                                    String DescMotNoVal="";
                                    if(facVO.getDescMotNoVal()!=null) {
                                        DescMotNoVal = facVO.getDescMotNoVal();
                                    }

                                    String porcentajeIva = "";
                                    if(facVO.getPorcentajeIva()!=null) {
                                        porcentajeIva = facVO.getPorcentajeIva();
                                        
                                    }                                    
                                    String descPorcentajeIva="";
                                    if(facVO.getDescPorcentajeIva()!=null && !facVO.getDescPorcentajeIva().equals("")) {
                                        descPorcentajeIva = facVO.getDescPorcentajeIva();
                                    }

                                    String porcentajeIvaValidado = "";
                                    if(facVO.getPorcentajeIvaValidado()!=null) {
                                        porcentajeIvaValidado = facVO.getPorcentajeIvaValidado();
                                    }
                                    String descPorcentajeIvaValidado="";
                                    if(facVO.getDescPorcentajeIvaValidado()!=null && !facVO.getDescPorcentajeIvaValidado().equals("")) {
                                        descPorcentajeIvaValidado = facVO.getDescPorcentajeIva();
                                    }

                                    
                                    String exento = "";
                                    if(facVO.getExento()!=null) {
                                        exento = facVO.getExento();
                                    }    
                                    
                                    String prorrata = "";
                                    if(facVO.getProrrata()!=null) {
                                        prorrata = facVO.getProrrata().toString();
                                    }

                                    String prorrataValidado = "";
                                    if(facVO.getProrrataValidado()!=null) {
                                        prorrataValidado = facVO.getProrrataValidado().toString();
                                    }

                                    familiaTitu = DescFamilia;
                %>
                listaFamilia01[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getFamilia()%>', '<%=DescFamilia%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>', '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>',
                    '<%=porcentajeIva%>', '<%=descPorcentajeIva%>',
                    '<%=porcentajeIvaValidado%>', '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>',
                    '<%=facVO.getValidada()%>', '<%=DescValidada%>',
                    '<%=IvaSub%>', '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>',
                    '<%=MotNoVal%>', '<%=DescMotNoVal%>'
                ];

                listaFamilia01Tabla[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=MotNoVal%>'
                ];

                listaFamilia01Titulos[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=DescMotNoVal%>'
                ];
                <%
                                }// for            
                %>
                document.getElementById("titFamilia01").innerText = '<%=familiaTitu%>';
                document.getElementById('familia01').style.display = 'block';
                document.getElementById('totalFacturas').style.display = 'block';

                totalBase01 = <%=ListaFam.get(0).getBaseTotal()%>;
                totalIva01 = <%=ListaFam.get(0).getIvaTotal()%>;
                totalTotal01 = <%=ListaFam.get(0).getImporteTotal()%>;
                totalVali01 = <%=ListaFam.get(0).getValidadoTotal()%>;
                <%
                                } // if List                       
                        }

    //             tablaFamilia02
                        if(request.getAttribute("listaFacturas02")!=null) {
                            List02 = (List<FacturaVO>)request.getAttribute("listaFacturas02");        
                    
                            if (List02!= null && List02.size() >0){   
                                for (int indice=0;indice<List02.size();indice++) {
                                facVO = List02.get(indice);

                                    String Familia = "";
                                    if(facVO.getFamilia()!=null) {
                                        Familia = facVO.getFamilia();
                                    }
                                    String DescFamilia = "";      
                                    if(idiomaUsuario==4){
                                        DescFamilia=facVO.getDescFamiliaEus();
                                    } else {
                                        DescFamilia=facVO.getDescFamiliaCas();
                                    }                                

                                    String DescTipoGasto="";
                                    if(facVO.getDescTipoGasto()!=null) {
                                        DescTipoGasto = facVO.getDescTipoGasto();                                       
                                    }                    

                                    String FecEmision="";
                                    if(facVO.getFecEmision()!=null) {
                                        FecEmision = dateFormat.format(facVO.getFecEmision());
                                    }                    
                                    String FecPago="";
                                    if(facVO.getFecPago()!=null) {
                                        FecPago = dateFormat.format(facVO.getFecPago());
                                    }                                                 

                                    String Validada = "";
                                    if(facVO.getValidada()!=null) {
                                        Validada = facVO.getValidada();
                                    }   
                                    String DescValidada="";
                                    if(facVO.getDescValidada()!=null) {
                                        DescValidada = facVO.getDescValidada();
                                    }
                                    
                                    String IvaSub = "";
                                    if(facVO.getIvaSub()!=null) {
                                        IvaSub = facVO.getIvaSub();
                                    }                                    
                                    String DescIvaSub="";
                                    if(facVO.getDescIvaSub()!=null) {
                                        DescIvaSub = facVO.getDescIvaSub();
                                    }
                                    
                                    String MotNoVal = "";
                                    if(facVO.getMotNoVal()!=null) {
                                        MotNoVal = facVO.getMotNoVal();
                                    }                                    
                                    String DescMotNoVal="";
                                    if(facVO.getDescMotNoVal()!=null) {
                                        DescMotNoVal = facVO.getDescMotNoVal();
                                    }

                                    String porcentajeIva = "";
                                    if(facVO.getPorcentajeIva()!=null) {
                                        porcentajeIva = facVO.getPorcentajeIva();
                                    }                                    
                                    String descPorcentajeIva="";
                                    if(facVO.getDescPorcentajeIva()!=null) {
                                        descPorcentajeIva = facVO.getDescPorcentajeIva();
                                    }

                                    String porcentajeIvaValidado = "";
                                    if(facVO.getPorcentajeIvaValidado()!=null) {
                                        porcentajeIvaValidado = facVO.getPorcentajeIvaValidado();
                                    }
                                    String descPorcentajeIvaValidado="";
                                    if(facVO.getDescPorcentajeIvaValidado()!=null && !facVO.getDescPorcentajeIvaValidado().equals("")) {
                                        descPorcentajeIvaValidado = facVO.getDescPorcentajeIva();
                                    }
                                    
                                    String exento = "";
                                    if(facVO.getExento()!=null) {
                                        exento = facVO.getExento();
                                    }    
                                    
                                    String prorrata = "";
                                    if(facVO.getProrrata()!=null) {
                                        prorrata = facVO.getProrrata().toString();
                                    }

                                    String prorrataValidado = "";
                                    if(facVO.getProrrataValidado()!=null) {
                                        prorrataValidado = facVO.getProrrataValidado().toString();
                                    }

                                    familiaTitu = DescFamilia;
                %>
                listaFamilia02[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getFamilia()%>', '<%=DescFamilia%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>', '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>',
                    '<%=porcentajeIva%>', '<%=descPorcentajeIva%>',
                    '<%=porcentajeIvaValidado%>', '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>',
                    '<%=facVO.getValidada()%>', '<%=DescValidada%>',
                    '<%=IvaSub%>', '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>',
                    '<%=MotNoVal%>', '<%=DescMotNoVal%>'
                ];
                listaFamilia02Tabla[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=MotNoVal%>'
                ];

                listaFamilia02Titulos[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=DescMotNoVal%>'
                ];
                <%
                                }// for
                %>
                document.getElementById("titFamilia02").innerText = '<%=familiaTitu%>';
                document.getElementById('familia02').style.display = 'block';

                totalBase02 = <%=ListaFam.get(1).getBaseTotal()%>;
                totalIva02 = <%=ListaFam.get(1).getIvaTotal()%>;
                totalTotal02 = <%=ListaFam.get(1).getImporteTotal()%>;
                totalVali02 = <%=ListaFam.get(1).getValidadoTotal()%>;
                <%
                            } // if List
                        }

                //             tablaFamilia03
                        if(request.getAttribute("listaFacturas03")!=null) {
                            List03 = (List<FacturaVO>)request.getAttribute("listaFacturas03");

                            if (List03!= null && List03.size() >0){
                                for (int indice=0;indice<List03.size();indice++) {
                                facVO = List03.get(indice);

                                    String Familia = "";
                                    if(facVO.getFamilia()!=null) {
                                        Familia = facVO.getFamilia();
                                    }
                                    String DescFamilia = "";      
                                    if(idiomaUsuario==4){
                                        DescFamilia=facVO.getDescFamiliaEus();
                                    } else {
                                        DescFamilia=facVO.getDescFamiliaCas();
                                    }                                

                                    String DescTipoGasto="";
                                    if(facVO.getDescTipoGasto()!=null) {
                                        DescTipoGasto = facVO.getDescTipoGasto();
                                    }                    

                                    String FecEmision="";
                                    if(facVO.getFecEmision()!=null) {
                                        FecEmision = dateFormat.format(facVO.getFecEmision());
                                    }                    
                                    String FecPago="";
                                    if(facVO.getFecPago()!=null) {
                                        FecPago = dateFormat.format(facVO.getFecPago());
                                    }                                                 

                                    String Validada = "";
                                    if(facVO.getValidada()!=null) {
                                        Validada = facVO.getValidada();
                                    }   
                                    String DescValidada="";
                                    if(facVO.getDescValidada()!=null) {
                                        DescValidada = facVO.getDescValidada();
                                    }
                                    
                                    String IvaSub = "";
                                    if(facVO.getIvaSub()!=null) {
                                        IvaSub = facVO.getIvaSub();
                                    }                                    
                                    String DescIvaSub="";
                                    if(facVO.getDescIvaSub()!=null) {
                                        DescIvaSub = facVO.getDescIvaSub();
                                    }
                                    
                                    String MotNoVal = "";
                                    if(facVO.getMotNoVal()!=null) {
                                        MotNoVal = facVO.getMotNoVal();
                                    }                                    
                                    String DescMotNoVal="";
                                    if(facVO.getDescMotNoVal()!=null) {
                                        DescMotNoVal = facVO.getDescMotNoVal();
                                    }

                                    String porcentajeIva = "";
                                    if(facVO.getPorcentajeIva()!=null) {
                                        porcentajeIva = facVO.getPorcentajeIva();
                                    }                                    
                                    String descPorcentajeIva="";
                                    if(facVO.getDescPorcentajeIva()!=null) {
                                        descPorcentajeIva = facVO.getDescPorcentajeIva();
                                    }                                    

                                    String porcentajeIvaValidado = "";
                                    if(facVO.getPorcentajeIvaValidado()!=null) {
                                        porcentajeIvaValidado = facVO.getPorcentajeIvaValidado();
                                    }
                                    String descPorcentajeIvaValidado="";
                                    if(facVO.getDescPorcentajeIvaValidado()!=null && !facVO.getDescPorcentajeIvaValidado().equals("")) {
                                        descPorcentajeIvaValidado = facVO.getDescPorcentajeIva();
                                    }

                                    String exento = "";
                                    if(facVO.getExento()!=null) {
                                        exento = facVO.getExento();
                                    }    
                                    
                                    String prorrata = "";
                                    if(facVO.getProrrata()!=null) {
                                        prorrata = facVO.getProrrata().toString();
                                    }

                                    String prorrataValidado = "";
                                    if(facVO.getProrrataValidado()!=null) {
                                        prorrataValidado = facVO.getProrrataValidado().toString();
                                    }

                                    
                                    familiaTitu = DescFamilia;
                %>
                listaFamilia03[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getFamilia()%>', '<%=DescFamilia%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>', '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>',
                    '<%=porcentajeIva%>', '<%=descPorcentajeIva%>',
                    '<%=porcentajeIvaValidado%>', '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>',
                    '<%=facVO.getValidada()%>', '<%=DescValidada%>',
                    '<%=IvaSub%>', '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>',
                    '<%=MotNoVal%>', '<%=DescMotNoVal%>',
                    '<%=IvaSub%>', '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>',
                    '<%=MotNoVal%>', '<%=DescMotNoVal%>'
                ];

                listaFamilia03Tabla[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=facVO.getTipoGasto()%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=MotNoVal%>'
                ];

                listaFamilia03Titulos[<%=indice%>] = [
                    '<%=facVO.getId()%>',
                    '<%=facVO.getNumOrden()%>',
                    '<%=DescTipoGasto%>',
                    '<%=facVO.getProveedor()%>',
                    '<%=facVO.getNumFactura()%>',
                    '<%=FecEmision%>',
                    '<%=FecPago%>',
                    '<%=formateador.format(facVO.getImporteBase())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteBaseValidado())%>' + ' \u20ac',
                    '<%=descPorcentajeIva%>',
                    '<%=descPorcentajeIvaValidado%>',
                    '<%=formateador.format(facVO.getImporteIva())%>' + ' \u20ac',
                    '<%=formateador.format(facVO.getImporteIvaValidado())%>' + ' \u20ac',
                    //'<%=exento%>',
                    '<%=prorrata%>',
                    '<%=prorrataValidado%>',
                    '<%=formateador.format(facVO.getImporteTotal())%>' + ' \u20ac',
                    '<%=DescValidada%>',
                    '<%=DescIvaSub%>',
                    '<%=formateador.format(facVO.getImporteVali())%>' + ' \u20ac',
                    '<%=DescMotNoVal%>'
                ];
                <%
                                }// for
                %>
                document.getElementById("titFamilia03").innerText = '<%=familiaTitu%>';
                document.getElementById('familia03').style.display = 'block';

                totalBase03 = <%=ListaFam.get(2).getBaseTotal()%>;
                totalIva03 = <%=ListaFam.get(2).getIvaTotal()%>;
                totalTotal03 = <%=ListaFam.get(2).getImporteTotal()%>;
                totalVali03 = <%=ListaFam.get(2).getValidadoTotal()%>;
                <%
                            } // if List
                        }
                %>
                crearTablas();
                pintarTotalesFamilias();
                <%
                    } else {
                %>
                document.getElementById('mensajeVacio').style.display = 'block';
                document.getElementById('btnexportarFactura').style.display = 'none';
                <%
                    }
                %>
                //var numExpediente = '<%=numExpediente%>';
                //const d = new Date();
                //let year = d.getFullYear();

                //let yearExpediente = numExpediente.split("/")[0];
                //console.log("Año expediente=" + yearExpediente);
                //if (Number.parseInt(yearExpediente) === year) {
                //    document.getElementById('capaFactura01').style.display = "none";
                //     document.getElementById('capaFactura02').style.display = "none";
                //    document.getElementById('capaFactura03').style.display = "none";
                //}
            </script>
    </body>
</html>
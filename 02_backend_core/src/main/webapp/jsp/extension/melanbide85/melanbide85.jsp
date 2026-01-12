<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.i18n.MeLanbide85I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConstantesMeLanbide85"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
        int idiomaUsuario = 1;
        if(request.getParameter("idioma") != null) {
            try {
                idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
            } catch(Exception ex) {
            }
        }

        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 5;
        String css = "";
        if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
        }

        MeLanbide85I18n meLanbide85I18n = MeLanbide85I18n.getInstance();
        MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
        String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide85/melanbide85.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide85/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide85/IkerUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            var parametrosLLamadaM85 = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE85'
                , operacion: null
                , tipo: 0
                , numero: null
                , documento: null
            };

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
                }
            }

            function pulsarDatosCVIntermediacion() {
                pleaseWait('on');

                var dataParameter = $.extend({}, parametrosLLamadaM85);

                dataParameter.numero = document.forms[0].numero.value;
                dataParameter.control = new Date().getTime();
                dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
                dataParameter.documento = '1';

                var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

                $.ajax({
                    type: 'POST',
                    url: urlBaseLlamada,
                    data: dataParameter,
                    success: function (respuesta) {
                        console.log("pulsarDatosCVIntermediacion respuesta = " + respuesta);
                        if (respuesta !== null) {
                            pleaseWait('off');
                            if (respuesta == "0") {
                                actualizarPestana();
                            } else if (respuesta == "1") {
                                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableCV")%>');
                            } else if (respuesta == "-2") {
                                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                            }
                        } else {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                            pleaseWait('off');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                        pleaseWait('off');
                    },
                    async: true
                });
            }

            function pulsarCertificadoDemanda() {
                pleaseWait('on');

                var dataParameter = $.extend({}, parametrosLLamadaM85);

                dataParameter.numero = document.forms[0].numero.value;
                dataParameter.control = new Date().getTime();
                dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
                dataParameter.documento = '4';

                var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

                $.ajax({
                    type: 'POST',
                    url: urlBaseLlamada,
                    data: dataParameter,
                    success: function (respuesta) {
                        console.log("pulsarCertificadoDemanda respuesta = " + respuesta);
                        if (respuesta !== null) {
                            pleaseWait('off');
                            if (respuesta == "0") {
                                actualizarPestana();
                            } else if (respuesta == "1") {
                                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableDemand")%>');
                            } else if (respuesta == "-2") {
                                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                            }
                        } else {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                            pleaseWait('off');
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                        pleaseWait('off');
                    },
                    async: true
                });
            }

            function obtainDocumentFile(nameFile) {
//                console.log("obtainDocumentFile nameFile = " + nameFile);
//                console.log("obtainDocumentFile #urlBaseLlamadaM85 = " + $("#urlBaseLlamadaM85").val());

                var dataParameter = $.extend({}, parametrosLLamadaM85);
                dataParameter.numExp = document.forms[0].numero.value;
                dataParameter.nameFile = nameFile;
                dataParameter.control = new Date().getTime();
                dataParameter.operacion = 'obtainDocumentFile';
                window.open($("#urlBaseLlamadaM85").val() + "?" + $.param(dataParameter), "_blank");
            }

            function pulsarNuevaContratacion() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE85&operacion=cargarNuevaContratacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 900, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaContrataciones(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarContratacion() {
                if (tablaContrataciones.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE85&operacion=cargarModificarContratacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0], 900, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaContrataciones(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarContratacion() {
                console.log("pulsarEliminarContratacion: col0=" + listaContrataciones[tablaContrataciones.selectedIndex][25]);
                console.log("pulsarEliminarContratacion: col1=" + listaContrataciones[tablaContrataciones.selectedIndex][26]);
                console.log("pulsarEliminarContratacion: col2=" + listaContrataciones[tablaContrataciones.selectedIndex][27]);
                console.log("pulsarEliminarContratacion: col3=" + listaContrataciones[tablaContrataciones.selectedIndex][28]);

                if (tablaContrataciones.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoIKER');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE85&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0] +
                                '&docCV=' + listaContrataciones[tablaContrataciones.selectedIndex][25] +
                                '&docDemanda=' + listaContrataciones[tablaContrataciones.selectedIndex][27];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminarContratacion
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoIKER');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

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
                    if (extension != 'xml') {
                        var resultado = jsp_alerta('A', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                        return false;
                    }
                    var resultado = jsp_alerta('', '<%=meLanbide85I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE85&operacion=procesarXML&tipo=0&numero=<%=numExpediente%>';
                        document.forms[0].action = url + '?' + parametros;
                        document.forms[0].enctype = 'multipart/form-data';
                        document.forms[0].encoding = 'multipart/form-data';
                        document.forms[0].method = 'POST';
                        document.forms[0].target = 'uploadFrameCarga';
                        document.forms[0].submit();
                    }
                    return true;
                } else {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                    return false;
                }
            }

            function iniciarTablaContrataciones() {
                tablaContrataciones = new FixedColumnTable(document.getElementById('listaContrataciones'), 1350, 1400, 'listaContrataciones');

                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.numPuesto")%>", "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.numPuesto")%>", 'Number');
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.denomPuesto1")%>");
                tablaContrataciones.addColumna('180', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.titulacion1")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.municipioCT1")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato1")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.grupoCotiz1")%>");
                tablaContrataciones.addColumna('100', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costeSalarial1")%>");
                tablaContrataciones.addColumna('100', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costeEpis1")%>");
                tablaContrataciones.addColumna('100', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.subvSolicitada1")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empVerde1")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empDigital1")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empGeneral1")%>");

                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.nOferta2")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.nombre2")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido12")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido22")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.dniNie2")%>");

                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.cv2")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaCv2")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.demanda2")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaDemanda2")%>");

                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.sexo2")%>");
                tablaContrataciones.addColumna('180', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.titulacion2")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.denomPuesto2")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.municipioCT2")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.grupoCotiz2")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato2")%>");
                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaNacimiento2")%>");
                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaInicio2")%>");
                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.edad2")%>");
                tablaContrataciones.addColumna('70', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.retribucionBruta2")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empVerde2")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empDigital2")%>");
                tablaContrataciones.addColumna('100', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.empGeneral2")%>");

                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.nombre3")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido13")%>");
                tablaContrataciones.addColumna('150', 'left', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido23")%>");
                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.dniNie3")%>");

                tablaContrataciones.addColumna('100', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato3")%>");
                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaInicio3")%>");
                tablaContrataciones.addColumna('70', 'center', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaFin3")%>");
                tablaContrataciones.addColumna('70', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costeSalarial3")%>");
                tablaContrataciones.addColumna('70', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costesSS3")%>");
                tablaContrataciones.addColumna('100', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costeEpis3")%>");
                tablaContrataciones.addColumna('70', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.costeTotalReal")%>");
                tablaContrataciones.addColumna('70', 'right', "<%=meLanbide85I18n.getMensaje(idiomaUsuario,"filaContratacion.subvConcedidaLan3")%>");
                // 4.270

                tablaContrataciones.numColumnasFijas = 2;

                for (var cont = 0; cont < listaContratacionesTabla.length; cont++) {
                    tablaContrataciones.addFilaConFormato(listaContratacionesTabla[cont], listaContratacionesTabla_titulos[cont], listaContratacionesTabla_estilos[cont]);
                }

                tablaContrataciones.displayCabecera = true;
                tablaContrataciones.height = 360;

                tablaContrataciones.altoCabecera = 60;
                tablaContrataciones.scrollWidth = 4800;
                tablaContrataciones.dblClkFunction = 'dblClckTablaContrataciones';
                tablaContrataciones.displayTabla();
                tablaContrataciones.pack();

            }

            function checkCampoVacio(campo) {
                return (campo != undefined ? campo : "");
            }

            function recargarTablaContrataciones(contrataciones) {
//                console.log("recargarTablaContrataciones: contrataciones.length = " + contrataciones.length);
                var fila;
                listaContrataciones = new Array();
                listaContratacionesTabla = new Array();
                listaContratacionesTabla_titulos = new Array();
                for (var i = 0; i < contrataciones.length; i++) {
//                    console.log("recargarListaContrataciones: Iteración " + i);
//                    console.log("recargarListaContrataciones: contrataciones[" + i + "]=" + contrataciones[i]);
                    fila = contrataciones[i];
                    listaContrataciones[i] = [fila.id,
                        fila.numpuesto,
                        fila.denompuesto1,
                        fila.titulacion1,
                        fila.municipioct1,
                        fila.durcontrato1,
                        fila.desdurcontrato1,
                        fila.grupocotiz1,
                        fila.desgrupocotiz1,
                        fila.costesalarial1,
                        fila.costeepis1,
                        fila.subvsolicitada1,
                        fila.empverde1,
                        fila.desempverde1,
                        fila.empdigit1,
                        fila.desempdigit1,
                        fila.empgen1,
                        fila.desempgen1,
                        fila.numExp2,
                        fila.numPuesto2,
                        fila.noferta2,
                        fila.nombre2,
                        fila.apellido12,
                        fila.apellido22,
                        fila.dninie2,
                        fila.cv2,
                        fila.fechaCv2Str,
                        fila.demanda2,
                        fila.fechaDemanda2Str,
                        fila.sexo2,
                        fila.dessexo2,
                        fila.titulacion2,
                        fila.denompuesto2,
                        fila.municipioct2,
                        fila.grupocotiz2,
                        fila.desgrupocotiz2,
                        fila.durcontrato2,
                        fila.desdurcontrato2,
                        fila.fechanacimiento2,
                        fila.fecNacStr2,
                        fila.fechaInicio2fec,
                        fila.fecIniStr2,
                        fila.edad,
                        fila.retribucionbruta2,
                        fila.empverde2,
                        fila.desempverde2,
                        fila.empdigit2,
                        fila.desempdigit2,
                        fila.empgen2,
                        fila.desempgen2,
                        fila.numExp3,
                        fila.numPuesto3,
                        fila.nombre3,
                        fila.apellido13,
                        fila.apellido23,
                        fila.dninie3,
                        fila.durcontrato3,
                        fila.desdurcontrato3,
                        fila.fechainicio3,
                        fila.fecIniStr3,
                        fila.fechaFin3,
                        fila.fecFinStr3,
                        fila.costesalarial3,
                        fila.costesss3,
                        fila.costeepis3,
                        fila.costetotalreal3,
                        fila.subvconcedidalan3];
//                    console.log("recargarTablaContrataciones listaContrataciones[" + i + "].length=" + listaContrataciones[i].length);
                    listaContratacionesTabla[i] = [fila.numpuesto,
                        fila.denompuesto1,
                        fila.titulacion1,
                        fila.municipioct1,
                        fila.desdurcontrato1,
                        fila.desgrupocotiz1,
                        fila.costesalarial1,
                        fila.costeepis1,
                        fila.subvsolicitada1,
                        fila.desempverde1,
                        fila.desempdigit1,
                        fila.desempgen1,
                        checkCampoVacio(fila.noferta2),
                        checkCampoVacio(fila.nombre2),
                        checkCampoVacio(fila.apellido12),
                        checkCampoVacio(fila.apellido22),
                        checkCampoVacio(fila.dninie2),
                        showLinkDocument(fila.cv2),
                        checkCampoVacio(fila.fechaCv2Str),
                        showLinkDocument(fila.demanda2),
                        checkCampoVacio(fila.fechaDemanda2Str),
                        checkCampoVacio(fila.dessexo2),
                        checkCampoVacio(fila.titulacion2),
                        checkCampoVacio(fila.denompuesto2),
                        checkCampoVacio(fila.municipioct2),
                        checkCampoVacio(fila.desgrupocotiz2),
                        checkCampoVacio(fila.desdurcontrato2),
                        checkCampoVacio(fila.fecNacStr2),
                        checkCampoVacio(fila.fecIniStr2),
                        checkCampoVacio(fila.edad),
                        checkCampoVacio(fila.retribucionbruta2),
                        checkCampoVacio(fila.desempverde2),
                        checkCampoVacio(fila.desempdigit2),
                        checkCampoVacio(fila.desempgen2),
                        checkCampoVacio(fila.nombre3),
                        checkCampoVacio(fila.apellido13),
                        checkCampoVacio(fila.apellido23),
                        checkCampoVacio(fila.dninie3),
                        checkCampoVacio(fila.desdurcontrato3),
                        checkCampoVacio(fila.fecIniStr3),
                        checkCampoVacio(fila.fecFinStr3),
                        checkCampoVacio(fila.costesalarial3),
                        checkCampoVacio(fila.costesss3),
                        checkCampoVacio(fila.costeepis3),
                        checkCampoVacio(fila.costetotalreal3),
                        checkCampoVacio(fila.subvconcedidalan3)];
//                    console.log("recargarTablaContrataciones listaContratacionesTabla[" + i + "].length=" + listaContratacionesTabla[i].length);
                    listaContratacionesTabla_titulos[i] = [fila.numpuesto,
                        fila.denompuesto1,
                        fila.titulacion1,
                        fila.municipioct1,
                        fila.desdurcontrato1,
                        fila.desgrupocotiz1,
                        fila.costesalarial1,
                        fila.costeepis1,
                        fila.subvsolicitada1,
                        fila.desempverde1,
                        fila.desempdigit1,
                        fila.desempgen1,
                        checkCampoVacio(fila.noferta2),
                        checkCampoVacio(fila.nombre2),
                        checkCampoVacio(fila.apellido12),
                        checkCampoVacio(fila.apellido22),
                        checkCampoVacio(fila.dninie2),
                        checkCampoVacio(fila.cv2), 
                        checkCampoVacio(fila.fechaCv2Str),
                        checkCampoVacio(fila.demanda2),
                        checkCampoVacio(fila.fechaDemanda2Str),
                        checkCampoVacio(fila.dessexo2),
                        checkCampoVacio(fila.titulacion2),
                        checkCampoVacio(fila.denompuesto2),
                        checkCampoVacio(fila.municipioct2),
                        checkCampoVacio(fila.desgrupocotiz2),
                        checkCampoVacio(fila.desdurcontrato2),
                        checkCampoVacio(fila.fecNacStr2),
                        checkCampoVacio(fila.fecIniStr2),
                        checkCampoVacio(fila.edad),
                        checkCampoVacio(fila.retribucionbruta2),
                        checkCampoVacio(fila.desempverde2),
                        checkCampoVacio(fila.desempdigit2),
                        checkCampoVacio(fila.desempgen2),
                        checkCampoVacio(fila.nombre3),
                        checkCampoVacio(fila.apellido13),
                        checkCampoVacio(fila.apellido23),
                        checkCampoVacio(fila.dninie3),
                        checkCampoVacio(fila.desdurcontrato3),
                        checkCampoVacio(fila.fecIniStr3),
                        checkCampoVacio(fila.fecFinStr3),
                        checkCampoVacio(fila.costesalarial3),
                        checkCampoVacio(fila.costesss3),
                        checkCampoVacio(fila.costeepis3),
                        checkCampoVacio(fila.costetotalreal3),
                        checkCampoVacio(fila.subvconcedidalan3)];
//                    console.log("recargarTablaContrataciones listaContratacionesTabla_titulos[" + i + "].length=" + listaContratacionesTabla_titulos[i].length);
                }
//                console.log("recargarTablaContrataciones: Chegou ata aquí, Aleluia!!");
                iniciarTablaContrataciones();
            }
            
            function showLinkDocument(nameDoc) {
               if (nameDoc != undefined && nameDoc != "")
                   return "<a href=javascript:obtainDocumentFile('" + nameDoc + "')>" + nameDoc + "</a>" ;
               else 
                   return "";
            }

            function actualizarPestana() {
                limpiarFormulario();
                var parametros = 'tarea=preparar&modulo=MELANBIDE85&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestana,
                        error: mostrarErrorPeticion
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            function dblClckTablaContrataciones(rowID, tableName) {
                pulsarModificarContratacion();
            }

// FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var contrataciones = datos.tabla.lista;
                    //if (contrataciones.length > 0) {
                    elementoVisible('off', 'barraProgresoIKER');
                    recargarTablaContrataciones(contrataciones);
                    //} else {
                    //mostrarErrorPeticion();
                    //}
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarContratacion() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoIKER');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide85I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

            function limpiarFormulario() {
                document.getElementById('fichero_xml').value = "";
            }
        </script>

    </head>

    <body class="bandaBody">
        <input type="hidden" id="urlBaseLlamadaM85" name="urlBaseLlamadaM85" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
        <div class="tab-page" id="tabPage851" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana851"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage851"));</script>
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
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <br>
                <div id="divGeneral">     
                    <div id="listaContrataciones"  align="center"></div>
                </div>
                <br>
                <div class="legendRojo" id="comentario1"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"legend.comentario1")%></div>
                <div class="legendRojo" id="comentario2"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"legend.comentario2")%></div>
                <div class="legendRojo" id="comentario3"><%=meLanbide85I18n.getMensaje(idiomaUsuario,"legend.comentario3")%></div>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevaContratacion" name="btnNuevaContratacion" class="botonGeneral"  value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaContratacion();">
                    <input type="button" id="btnModificarContratacion" name="btnModificarContratacion" class="botonGeneral" value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                    <input type="button" id="btnEliminarContratacion" name="btnEliminarContratacion"   class="botonGeneral" value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnCargarRegistros" name="btnCargarRegistros" class="botonMasLargo" value="<%=meLanbide85I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXML();">
                    <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();" >
                    </input>
                    <input type="button" id="botonCertificadoDemanda" name="botonCertificadoDemanda" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();" >
                    </input>
                </div>                       
            </div>  
        </div>
        <iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
        <script  type="text/javascript">
            //Tabla Contrataciones
            var tablaContrataciones;
            var listaContrataciones = new Array();
            var listaContratacionesTabla = new Array();
            var listaContratacionesTabla_titulos = new Array();
            var listaContratacionesTabla_estilos = new Array();

            <%  		
               FilaContratacionVO objectVO = null;
               List<FilaContratacionVO> List = null;
               if(request.getAttribute("listaContrataciones")!=null){
                   List = (List<FilaContratacionVO>)request.getAttribute("listaContrataciones");
               }													
               if (List!= null && List.size() >0){
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                   for (int indice=0;indice<List.size();indice++)
                   {
                       objectVO = List.get(indice);
                       // CONTRATACION
                        String numpuesto = "";
                        if(objectVO.getNumpuesto()!=null){
                           numpuesto=String.valueOf(objectVO.getNumpuesto());
                        }		   
                        String denompuesto1 = "";
                        if(objectVO.getDenompuesto1()!=null){
                            denompuesto1=objectVO.getDenompuesto1();
                        }
                        String titulacion1 = "";
                        if(objectVO.getTitulacion1()!=null){
                                titulacion1=objectVO.getTitulacion1();
                        }
                        String municipioct1 = "";
                        if(objectVO.getMunicipioct1()!=null){
                                municipioct1=objectVO.getMunicipioct1();
                        }
                        String durcontrato1 = "";
                        if(objectVO.getDurcontrato1()!=null){
                            durcontrato1=objectVO.getDurcontrato1();
                        }
                        String desdurcontrato1 = "";
                        if(objectVO.getDesdurcontrato1()!=null){
                            desdurcontrato1=objectVO.getDesdurcontrato1();
                        }
                        String grupocotiz1 = "";
                        if(objectVO.getGrupocotiz1()!=null){
                            grupocotiz1=objectVO.getGrupocotiz1();
                        }
                        String desgrupocotiz1 = "";
                        if(objectVO.getDesgrupocotiz1()!=null){
                            desgrupocotiz1=objectVO.getDesgrupocotiz1();
                        }
                        String costesalarial1 = "";
                        if(objectVO.getCostesalarial1()!=null){
                            costesalarial1=String.valueOf(objectVO.getCostesalarial1().toString().replace(".",","));
                        }
                        String costeepis1 = "";
                        if(objectVO.getCosteepis1()!=null){
                            costeepis1=String.valueOf(objectVO.getCosteepis1().toString().replace(".",","));
                        }
                        String subvsolicitada1 = "";
                        if(objectVO.getSubvsolicitada1()!=null){
                           subvsolicitada1=String.valueOf(objectVO.getSubvsolicitada1().toString().replace(".",","));
                        }
                        String empverde1 = "";
                        if(objectVO.getEmpverde1()!=null){
                            empverde1=objectVO.getEmpverde1();
                        }
                        String desempverde1 = "";
                        if(objectVO.getDesempverde1()!=null){
                            desempverde1=objectVO.getDesempverde1();
                        }
                        String empdigit1 = "";
                        if(objectVO.getEmpdigit1()!=null){
                            empdigit1=objectVO.getEmpdigit1();
                        }
                        String desempdigit1 = "";
                        if(objectVO.getDesempdigit1()!=null){
                            desempdigit1=objectVO.getDesempdigit1();
                        }
                        String empgen1 = "";
                        if(objectVO.getEmpgen1()!=null){
                            empgen1=objectVO.getEmpgen1();
                        }
                        String desempgen1 = "";
                        if(objectVO.getDesempgen1()!=null){
                            desempgen1=objectVO.getDesempgen1();
                        }
 // CONTRATACION_INI
                        String numExp2  = "";
                        if(objectVO.getNumExp2()!=null){
                            numExp2=objectVO.getNumExp2();
                        }
                        String numPuesto2  = "";
                        if(objectVO.getNumPuesto2()!=null){
                            numPuesto2=String.valueOf(objectVO.getNumPuesto2());
                        }
                        String noferta2  = "";
                        if(objectVO.getNoferta2()!=null){
                            noferta2=objectVO.getNoferta2();
                        }
                        String nombre2  = "";
                        if(objectVO.getNombre2()!=null){
                            nombre2=objectVO.getNombre2();
                        }
                        String apellido12  = "";
                        if(objectVO.getApellido12 ()!=null){
                            apellido12=objectVO.getApellido12();
                        }
                        String apellido22  = "";
                        if(objectVO.getApellido22()!=null){
                            apellido22=objectVO.getApellido22();
                        }
                        String dninie2  = "";
                        if(objectVO.getDninie2()!=null){
                            dninie2=objectVO.getDninie2();
                        }
                        
                        String cv2 = "";
                        if (objectVO.getCv2() != null) {
                            cv2 = objectVO.getCv2();
                        }
                        String fechaCv2 = "";
                        if (objectVO.getFechaCv2() != null) {
                            fechaCv2 = dateFormat.format(objectVO.getFechaCv2());
                        }
                        String demanda2 = "";
                        if (objectVO.getDemanda2() != null) {
                            demanda2 = objectVO.getDemanda2();
                        }
                        String fechaDemanda2 = "";
                        if (objectVO.getFechaDemanda2() != null) {
                            fechaDemanda2 = dateFormat.format(objectVO.getFechaDemanda2());
                        }
                        
                        String sexo2  = "";
                        if(objectVO.getSexo2()!=null){
                            sexo2=objectVO.getSexo2();
                        }
                        String dessexo2  = "";
                        if(objectVO.getDessexo2()!=null){
                            dessexo2=objectVO.getDessexo2();
                        }
                        String titulacion2  = "";
                        if(objectVO.getTitulacion2()!=null){
                            titulacion2=objectVO.getTitulacion2();
                        }
                        String denompuesto2  = "";
                        if(objectVO.getDenompuesto2()!=null){
                            denompuesto2=objectVO.getDenompuesto2();
                        }
                        String municipioct2  = "";
                        if(objectVO.getMunicipioct2()!=null){
                            municipioct2=objectVO.getMunicipioct2();
                        }
                        String grupocotiz2  = "";
                        if(objectVO.getGrupocotiz2()!=null){
                            grupocotiz2=objectVO.getGrupocotiz2();
                        }
                        String desgrupocotiz2  = "";
                        if(objectVO.getDesgrupocotiz2()!=null){
                            desgrupocotiz2=objectVO.getDesgrupocotiz2();
                        }
                        String durcontrato2  = "";
                        if(objectVO.getDurcontrato2()!=null){
                            durcontrato2=objectVO.getDurcontrato2();
                        }
                        String desdurcontrato2  = "";
                        if(objectVO.getDesdurcontrato2()!=null){
                            desdurcontrato2=objectVO.getDesdurcontrato2();
                        }
                        String fechanacimiento2  = "";
                        if(objectVO.getFechanacimiento2()!=null){
                            fechanacimiento2=dateFormat.format(objectVO.getFechanacimiento2());                
                        }
                        String fechainicio2  = "";
                        if(objectVO.getFechainicio2()!=null){
                            fechainicio2=dateFormat.format(objectVO.getFechainicio2());                
                        }
                        String edad  = "";
                        if(objectVO.getEdad()!=null){
                            edad=String.valueOf(objectVO.getEdad());
                        }
                        String retribucionbruta2  = "";
                        if(objectVO.getRetribucionbruta2()!=null){
                           retribucionbruta2=String.valueOf(objectVO.getRetribucionbruta2().toString().replace(".",","));
                        }
                        String empverde2 = "";
                        if(objectVO.getEmpverde2()!=null){
                            empverde2=objectVO.getEmpverde2();
                        }
                        String desempverde2 = "";
                        if(objectVO.getDesempverde2()!=null){
                            desempverde2=objectVO.getDesempverde2();
                        }
                        String empdigit2 = "";
                        if(objectVO.getEmpdigit2()!=null){
                            empdigit2=objectVO.getEmpdigit2();
                        }
                        String desempdigit2 = "";
                        if(objectVO.getDesempdigit2()!=null){
                            desempdigit2=objectVO.getDesempdigit2();
                        }
                        String empgen2 = "";
                        if(objectVO.getEmpgen2()!=null){
                            empgen2=objectVO.getEmpgen2();
                        }
                        String desempgen2 = "";
                        if(objectVO.getDesempgen2()!=null){
                            desempgen2=objectVO.getDesempgen2();
                        }

                        // CONTRATACION_FIN
                        String numExp3  = "";
                        if(objectVO.getNumExp3()!=null){
                            numExp3=objectVO.getNumExp3();
                        }
                        String numPuesto3  = "";
                        if(objectVO.getNumPuesto3()!=null){
                            numPuesto3=String.valueOf(objectVO.getNumPuesto3());
                        }
                        String nombre3  = "";
                        if(objectVO.getNombre3()!=null){
                            nombre3=objectVO.getNombre3();
                        }
                        String apellido13  = "";
                        if(objectVO.getApellido13 ()!=null){
                            apellido13=objectVO.getApellido13();
                        }
                        String apellido23  = "";
                        if(objectVO.getApellido23()!=null){
                            apellido23=objectVO.getApellido23();
                        }
                        String dninie3  = "";
                        if(objectVO.getDninie3()!=null){
                            dninie3=objectVO.getDninie3();
                        }                        
                        String durcontrato3  = "";
                        if(objectVO.getDurcontrato3()!=null){
                            durcontrato3=objectVO.getDurcontrato3();
                        }
                        String desdurcontrato3  = "";
                        if(objectVO.getDesdurcontrato3()!=null){
                            desdurcontrato3=objectVO.getDesdurcontrato3();
                        }
                        String fechainicio3  = "";
                        if(objectVO.getFechainicio3()!=null){
                            fechainicio3=dateFormat.format(objectVO.getFechainicio3());                
                        }
                        String fechafin3  = "";
                        if(objectVO.getFechafin3()!=null){
                            fechafin3=dateFormat.format(objectVO.getFechafin3());                
                        }
                        String costesalarial3  = "";
                        if(objectVO.getCostesalarial3()!=null){
                           costesalarial3=String.valueOf(objectVO.getCostesalarial3().toString().replace(".",","));
                        }
                        String costesss3  = "";
                        if(objectVO.getCostesss3()!=null){
                           costesss3=String.valueOf(objectVO.getCostesss3().toString().replace(".",","));
                        }
                        String costeepis3  = "";
                        if(objectVO.getCosteepis3()!=null){
                           costeepis3=String.valueOf(objectVO.getCosteepis3().toString().replace(".",","));
                        }
                        String costetotalreal3  = "";
                        if(objectVO.getCostetotalreal3()!=null){
                           costetotalreal3=String.valueOf(objectVO.getCostetotalreal3().toString().replace(".",","));
                        }
                        String subvconcedidalan3  = "";
                        if(objectVO.getSubvconcedidalan3()!=null){
                           subvconcedidalan3=String.valueOf(objectVO.getSubvconcedidalan3().toString().replace(".",","));
                        }                               
            %>
            var docCv2 = "<%=cv2%>";
            var docDemanda2 = "<%=demanda2%>";
            listaContrataciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numpuesto%>', '<%=denompuesto1%>', '<%=titulacion1%>', '<%=municipioct1%>', '<%=durcontrato1%>', '<%=desdurcontrato1%>', '<%=grupocotiz1%>', '<%=desgrupocotiz1%>', '<%=costesalarial1%>', '<%=costeepis1%>', '<%=subvsolicitada1%>', '<%=empverde1%>', '<%=desempverde1%>', '<%=empdigit1%>', '<%=desempdigit1%>', '<%=empgen1%>', '<%=desempgen1%>',
                '<%=numExp2%>', '<%=numPuesto2%>', '<%=noferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dninie2%>', '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>', '<%=sexo2%>', '<%=dessexo2%>', '<%=titulacion2%>', '<%=denompuesto2%>', '<%=municipioct2%>', '<%=grupocotiz2%>', '<%=desgrupocotiz2%>', '<%=durcontrato2%>', '<%=desdurcontrato2%>', '<%=fechanacimiento2%>', '<%=fechainicio2%>', '<%=edad%>', '<%=retribucionbruta2%>', '<%=empverde2%>', '<%=desempverde2%>', '<%=empdigit2%>', '<%=desempdigit2%>', '<%=empgen2%>', '<%=desempgen2%>',
                '<%=numExp3%>', '<%=numPuesto3%>', '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dninie3%>', '<%=durcontrato3%>', '<%=desdurcontrato3%>', '<%=fechainicio3%>', '<%=fechafin3%>', '<%=costesalarial3%>', '<%=costesss3%>', '<%=costeepis3%>', '<%=costetotalreal3%>', '<%=subvconcedidalan3%>'
            ];
//            console.log("jsp listaContrataciones[<%=indice%>].length=" + listaContrataciones[<%=indice%>].length);
            // Lo que se ve
            listaContratacionesTabla[<%=indice%>] = ['<%=numpuesto%>', '<%=denompuesto1%>', '<%=titulacion1%>', '<%=municipioct1%>', '<%=desdurcontrato1%>', '<%=desgrupocotiz1%>', '<%=costesalarial1%>', '<%=costeepis1%>', '<%=subvsolicitada1%>', '<%=desempverde1%>', '<%=desempdigit1%>', '<%=desempgen1%>',
                '<%=noferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dninie2%>', '<a href="javascript:obtainDocumentFile(\'<%=cv2%>\')"><%=cv2%></a>', '<%=fechaCv2%>', '<a href="javascript:obtainDocumentFile(\'<%=demanda2%>\')"><%=demanda2%></a>', '<%=fechaDemanda2%>', '<%=dessexo2%>', '<%=titulacion2%>', '<%=denompuesto2%>', '<%=municipioct2%>', '<%=desgrupocotiz2%>', '<%=desdurcontrato2%>', '<%=fechanacimiento2%>', '<%=fechainicio2%>', '<%=edad%>', '<%=retribucionbruta2%>', '<%=desempverde2%>', '<%=desempdigit2%>', '<%=desempgen2%>',
                '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dninie3%>', '<%=desdurcontrato3%>', '<%=fechainicio3%>', '<%=fechafin3%>', '<%=costesalarial3%>', '<%=costesss3%>', '<%=costeepis3%>', '<%=costetotalreal3%>', '<%=subvconcedidalan3%>'
            ];
//            console.log("jsp listaContratacionesTabla[<%=indice%>].length=" + listaContratacionesTabla[<%=indice%>].length);
            // Tooltip
            listaContratacionesTabla_titulos[<%=indice%>] = ['<%=numpuesto%>', '<%=denompuesto1%>', '<%=titulacion1%>', '<%=municipioct1%>', '<%=desdurcontrato1%>', '<%=desgrupocotiz1%>', '<%=costesalarial1%>', '<%=costeepis1%>', '<%=subvsolicitada1%>', '<%=desempverde1%>', '<%=desempdigit1%>', '<%=desempgen1%>',
                '<%=noferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dninie2%>', '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>', '<%=dessexo2%>', '<%=titulacion2%>', '<%=denompuesto2%>', '<%=municipioct2%>', '<%=desgrupocotiz2%>', '<%=desdurcontrato2%>', '<%=fechanacimiento2%>', '<%=fechainicio2%>', '<%=edad%>', '<%=retribucionbruta2%>', '<%=desempverde2%>', '<%=desempdigit2%>', '<%=desempgen2%>',
                '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dninie3%>', '<%=desdurcontrato3%>', '<%=fechainicio3%>', '<%=fechafin3%>', '<%=costesalarial3%>', '<%=costesss3%>', '<%=costeepis3%>', '<%=costetotalreal3%>', '<%=subvconcedidalan3%>'
            ];
//            console.log("jsp listaContratacionesTabla_titulos[<%=indice%>].length=" + listaContratacionesTabla_titulos[<%=indice%>].length);
            <%
                   }// for
               }// if
            %>
            iniciarTablaContrataciones();

        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>
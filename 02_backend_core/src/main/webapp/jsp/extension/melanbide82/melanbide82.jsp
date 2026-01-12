<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.i18n.MeLanbide82I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConstantesMeLanbide82"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<head>
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
        MeLanbide82I18n meLanbide82I18n = MeLanbide82I18n.getInstance();
        MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();        
        String numExpediente = (String)request.getAttribute("numExp");
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide82/melanbide82.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide82/FixedColumnsTable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide82/GelUtils.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

        var parametrosLLamadaM82 = {
            tarea: 'preparar'
            , modulo: 'MELANBIDE82'
            , operacion: null
            , tipo: 0
            , numero: null
            , documento: null
        };
        
        var parametrosLLamadaM67 = {
            tarea: 'preparar'
            , modulo: 'MELANBIDE67'
            , operacion: null
            , tipo: 0
            , numero: null
            , documento: null
        };
        

        function pulsarNuevaContratacion() {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE82&operacion=cargarNuevaContratacion&tipo=0&numExp=<%=numExpediente%>&nuevo=1', 900, 1400, 'si', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaContrataciones(result);
                    }
                }
            });
        }

        function pulsarModificarContratacion() {
            if (tablaContrataciones.selectedIndex != -1) {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE82&operacion=cargarModificarContratacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0], 900, 1400, 'si', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaContrataciones(result);
                        }
                    }
                });
            } else {
                jsp_alerta('A', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarEliminarContratacion() {
            console.log("pulsarEliminarContratacion id=" + listaContrataciones[tablaContrataciones.selectedIndex][0] +
                    ", " + listaContrataciones[tablaContrataciones.selectedIndex][16] +
                    ", " + listaContrataciones[tablaContrataciones.selectedIndex][18]);
            if (tablaContrataciones.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE82&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0] +
                            '&docCV=' + listaContrataciones[tablaContrataciones.selectedIndex][16] +
                            '&fechaCV=' + listaContrataciones[tablaContrataciones.selectedIndex][17] +
                            '&docDemanda=' + listaContrataciones[tablaContrataciones.selectedIndex][18] +
                            '&fechaDemanda=' + listaContrataciones[tablaContrataciones.selectedIndex][19];
                    realizarPeticionAjax(parametros);
                }
            } else {
                jsp_alerta('A', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
     
        function pulsarExportarExcellContratacion() {
            var parametros = "";
             // Genera la URL sin depender de una fila seleccionada
                parametros = '?tarea=preparar&modulo=MELANBIDE82&operacion=generarExcelGEL&tipo=0&numExp=<%=numExpediente%>';
    
            // Abre una nueva ventana con los parámetros de exportación
                window.open(url + parametros, "_blank");
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
                    var resultado = jsp_alerta('A', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                    return false;
                }
                var resultado = jsp_alerta('', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE82&operacion=procesarXML&tipo=0&numero=<%=numExpediente%>';
                    document.forms[0].action = url + '?' + parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].encoding = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    document.forms[0].target = 'uploadFrameCarga';
                    document.forms[0].submit();
                }
                return true;
            } else {
                jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                return false;
            }
        }

        function dblClckTablaContrataciones(rowID, tableName) {
            pulsarModificarContratacion();
        }

        function iniciarTablaContrataciones() {
            tablaContrataciones = new FixedColumnTable(document.getElementById('listaContrataciones'), 1350, 1400, 'listaContrataciones');

            tablaContrataciones.addColumna('50', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.prioridad")%>", "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.prioridad")%>", 'Number');
            tablaContrataciones.addColumna('400', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.denomPuesto")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.nivelCualificacion")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.modContrato")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.durContrato")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.grupoCotiz")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.costeSalarial")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContrataciones.subvSolicitada")%>");
//7
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.municipio")%>");
            tablaContrataciones.addColumna('75', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.nombre")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.apellido1")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.apellido2")%>");
            tablaContrataciones.addColumna('80', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.dniNie")%>");

            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.cv2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.fechaCv2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.demanda2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.fechaDemanda2")%>");
// 16
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.fechaNacimiento")%>");
            tablaContrataciones.addColumna('50', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.sexo")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.nivelCualificacion")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.puestoTrabajo")%>");
            tablaContrataciones.addColumna('75', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.nOferta")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.grupoCotiz")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.durContrato")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.fechaInicio")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.edad")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.retribucionBruta")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesInicio.sistGrantiaJuve2")%>");
//27
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.municipio")%>");

            tablaContrataciones.addColumna('75', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.nombre")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.apellido1")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.apellido2")%>");
            tablaContrataciones.addColumna('80', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.dniNie")%>");
            tablaContrataciones.addColumna('50', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.sexo")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.grupoCotiz")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.durContrato")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.fechaInicio")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.fechaFin")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.retribucionBruta")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.costeSalarial")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.costesSs")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.indemFinContrato")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.costeTotalReal")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.subvConcedida")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide82I18n.getMensaje(idiomaUsuario,"tablaContratacionesFin.sistGrantiaJuve3")%>");
//44
//
            // 4.015
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
 

        function recargarTablaContrataciones(contrataciones) {
            var fila;
            listaContrataciones = new Array();
            listaContratacionesTabla = new Array();
            listaContratacionesTabla_titulos = new Array();

            for (var i = 0; i < contrataciones.length - 1; i++) {
                fila = contrataciones[i + 1];

                listaContrataciones[i] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10]
                            , fila[11], fila[12], fila[13], fila[14], fila[15]
                            , fila[16], fila[17], fila[18], fila[19]
                            , fila[20], fila[22], fila[24], fila[25], fila[26], fila[28]
                            , fila[30], fila[31], fila[32], fila[33], fila[36], fila[37], fila[38], fila[39], fila[40], fila[42]
                            , fila[44], fila[46], fila[47], fila[48], fila[49], fila[50]
                ];
//                console.log("listaContratacines[15]=" + listaContrataciones[i][16]);
//                console.log("listaContratacines[17]=" + listaContrataciones[i][18]);
//                console.log("recargarTablaContrataciones listaContrataciones[" + i + "].length=" + listaContrataciones[i].length);
                listaContratacionesTabla[i] = [fila[1], fila[2], fila[4], fila[5], fila[7], fila[8], fila[9], fila[10]
                            , fila[11], fila[12], fila[13], fila[14], fila[15]
                            , '<a href="javascript:obtainDocumentFile(\'' + fila[16] + '\')">' + fila[16] + '</a>', fila[17], '<a href="javascript:obtainDocumentFile(\'' + fila[18] + '\')">' + fila[18] + '</a>', fila[19]
                            , fila[20], fila[22], fila[24], fila[25], fila[26], fila[28]
                            , fila[30], fila[31], fila[32], fila[33], fila[35], fila[36], fila[37], fila[38], fila[39], fila[40], fila[42]
                            , fila[44], fila[46], fila[47], fila[48], fila[49]
                            , fila[50], fila[51], fila[52], fila[53], fila[54], fila[56]
                ];
                
//                for (let j = 0; j < listaContratacionesTabla[i].length; j++) {
//                    console.log ("listaContratacionesTabla[" + i + "][" + j + "]=" + listaContratacionesTabla[i][j]);
//                }
//                for (let j = 0; j < fila.length; j++) {
//                    console.log ("fila[" + j + "]=" + fila[j]);
//                }
//                console.log("recargarTablaContrataciones listaContratacionesTabla[" + i + "].length=" + listaContratacionesTabla[i].length);
//                listaContratacionesTabla_titulos[i] = [fila[1], fila[2], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10]
//                            , fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[18], fila[20], fila[22], fila[24], fila[26], fila[27], fila[28], fila[29]
//                            , fila[30], fila[31], fila[32], fila[33], fila[34], fila[36], fila[38], fila[40], fila[41], fila[42], fila[44], fila[45], fila[46], fila[47], fila[48]
//                ];
                listaContratacionesTabla_titulos[i] = [fila[1], fila[2], fila[4], fila[5], fila[7], fila[8], fila[9], fila[10]
                            , fila[11], fila[12], fila[13], fila[14], fila[15]
                            , fila[16], fila[17], fila[18], fila[19]
                            , fila[20], fila[22], fila[24], fila[25], fila[26], fila[28]
                            , fila[30], fila[31], fila[32], fila[33], fila[34], fila[35], fila[36], fila[37], fila[38], fila[39], fila[40], fila[42]
                            , fila[44], fila[46], fila[47], fila[48], fila[49]
                            , fila[50], fila[51], fila[52], fila[53], fila[54], fila[56]
                ];                
//                  console.log("recargarTablaContrataciones listaContratacionesTabla_titulos[" + i + "].length=" + listaContratacionesTabla_titulos[i].length);                
            }
            iniciarTablaContrataciones();
        }

        function limpiarFormulario() {
            document.getElementById('fichero_xml').value = '';
//            var fileupload = $('#fichero_xml');
//            fileupload.replaceWith(fileupload.val('').clone(true));
        }

        function actualizarPestana() {
            limpiarFormulario();
            var parametros = 'tarea=preparar&modulo=MELANBIDE82&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
            realizarPeticionAjax(parametros);
        }

        function realizarPeticionAjax(parametros) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            if (ajax != null) {
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)

                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var listaContratacionesNueva = extraerListaContrataciones(nodos);
                    var codigoOperacion = listaContratacionesNueva[0];
                    if (codigoOperacion == "0") {
                        recargarTablaContrataciones(listaContratacionesNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.sinDatos")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        }

        function pulsarDatosCVIntermediacion() {
            pleaseWait('on');

            var dataParameter = $.extend({}, parametrosLLamadaM82);

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
                    console.log("recuperarDatosCVoDemandaIntermediacion respuesta = " + respuesta);
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

            var dataParameter = $.extend({}, parametrosLLamadaM82);

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
                    console.log("recuperarDatosCVoDemandaIntermediacion respuesta = " + respuesta);
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
        
    function pulsarDatosIntermediacion() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM82);

        dataParameter.numero = document.forms[0].numero.value;
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'recuperarDatosIntermediacionExterno';

        var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (respuesta) {
                console.log("recuperarDatosIntermediacionExterno respuesta = " + respuesta);
                if (respuesta !== null) {
                    pleaseWait('off');

                    if (respuesta == "0") {
                        actualizarPestana();
                    } else if (respuesta == "1") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNIF")%>');
                    } else if (respuesta == "2") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNumOferta")%>');
                    } else if (respuesta == "3") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNIF_noNumOferta")%>');
                    } else if (respuesta == "4") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noCorrespondencia")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            },
            async: true
        });
    }        

        function obtainDocumentFile(nameFile, tipo) {
//            console.log("obtainDocumentFile nameFile = " + nameFile);
//            console.log("obtainDocumentFile #urlBaseLlamadaM82 = " + $("#urlBaseLlamadaM82").val());

            var dataParameter = $.extend({}, parametrosLLamadaM82);
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.nameFile = nameFile;
            dataParameter.tipo = tipo;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'obtainDocumentFile';
            window.open($("#urlBaseLlamadaM82").val() + "?" + $.param(dataParameter), "_blank");
        }
    </script> 
</head>
<body class="bandaBody">
    <input type="hidden" id="urlBaseLlamadaM82" name="urlBaseLlamadaM82" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>        
    <div class="tab-page" id="tabPage821" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana821"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage821"));</script>
        <div id="barraProgresoGEL" style="visibility: hidden;">
            <div class="contenedorHidepage">
                <div class="textoHide">
                    <span>
                        <%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                    </span>
                </div>
                <div class="imagenHide">
                    <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                </div>
            </div>
        </div>
        <br/>
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
        <div>    
            <br>
            <div id="divGeneral">     
                <div id="listaContrataciones"  align="center"></div>
            </div>
            <br><br>
            <div class="legendRojo" id="comentario1"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"legend.comentario1")%></div>
            <div class="legendRojo" id="comentario2"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"legend.comentario2")%></div>
            <div class="legendRojo" id="comentario3"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"legend.comentario3")%></div>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnNuevaContratacion" name="btnNuevaContratacion" class="botonGeneral"  value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaContratacion();">
                <input type="button" id="btnModificarContratacion" name="btnModificarContratacion" class="botonGeneral" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                <input type="button" id="btnEliminarContratacion" name="btnEliminarContratacion"   class="botonGeneral" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">
                <input type="button" id="btnpulsarExportarExcell" name="btnpulsarExportarExcell"   class="botonLargo" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarExcellContratacion();">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnCargarRegistros" name="btnCargarRegistros" class="botonMasLargo" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXML();">
                <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type= "button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();" >
                </input>
                <input type= "button" id="botonCertificadoDemanda" name="botonCertificadoDemanda" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();" >
                </input>                
                <input type= "button" id="botonDatosIntermediacion" name="botonDatosIntermediacion" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario,"btn.sistGrantiaJuv")%>" onclick="pulsarDatosIntermediacion();" >
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
                String prioridad="-";
                if(objectVO.getPrioridad()!=null && !"".equals(objectVO.getPrioridad())){
                    prioridad=Integer.toString(objectVO.getPrioridad());
                }                    
                String denomPuesto="-";
                if(objectVO.getDenomPuesto()!=null){
                    denomPuesto=objectVO.getDenomPuesto();
                }
                String nivelCualificacion="-";
                if(objectVO.getNivelCualificacion()!=null){
                    nivelCualificacion=objectVO.getNivelCualificacion();
                }
                String descNivel = "-";
                if(objectVO.getDescNivelcualificacion()!=null){
                    descNivel=objectVO.getDescNivelcualificacion();
                }                    
                String modContrato="-";
                if(objectVO.getModContrato()!=null){
                    modContrato=objectVO.getModContrato();
                }
                String durContrato="-";
                if(objectVO.getDurContrato()!=null && !"".equals(objectVO.getDurContrato())){
                    durContrato=Integer.toString(objectVO.getDurContrato());
                }
                String descDurContrato="-";
                if(objectVO.getDescDurContrato()!=null && !"".equals(objectVO.getDescDurContrato())){
                    descDurContrato=objectVO.getDescDurContrato();
                }
                String grupoCotiz="-";
                if(objectVO.getDescGrupoCotiz()!=null){
                    String descripcion = objectVO.getDescGrupoCotiz();                        
                    String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide82.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide82.FICHERO_PROPIEDADES);
                    String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                        if(idiomaUsuario==ConstantesMeLanbide82.CODIGO_IDIOMA_EUSKERA){
                            descripcion=descripcionDobleIdioma[1];
                        }else{
                            // Cogemos la primera posicion que deberia ser castellano
                            descripcion=descripcionDobleIdioma[0];
                        }
                    }
                    grupoCotiz = descripcion;
                }
                String costeSalarial="-";
                if(objectVO.getCostesalarial()!=null){
                    costeSalarial=String.valueOf((objectVO.getCostesalarial().toString()).replace(".",","));
                }
                String subvSolicitada="-";
                if(objectVO.getSubvsolicitada()!=null){
                    subvSolicitada=String.valueOf((objectVO.getSubvsolicitada().toString()).replace(".",","));
                }  
                // CONTRATACION_INICIO
                String municipio2="-";
                if(objectVO.getMunicipioInicio()!=null){
                    municipio2=objectVO.getMunicipioInicio();
                }                   
                String nombre2="-";
                if(objectVO.getNombreInicio()!=null){
                    nombre2=objectVO.getNombreInicio();
                }                    
                String apellido12="-";
                if(objectVO.getApellido1Inicio()!=null){
                    apellido12=objectVO.getApellido1Inicio();
                }                    
                String apellido22="-";
                if(objectVO.getApellido2Inicio()!=null){
                    apellido22=objectVO.getApellido2Inicio();
                }                    
                String dniNie2="-";
                if(objectVO.getDninieInicio()!=null){
                    dniNie2=objectVO.getDninieInicio();
                }   
                String cv2 = "";
                if (objectVO.getCv2() != null){
                    cv2 = objectVO.getCv2();
                }   
                String fechaCv2  = "-";
                if (objectVO.getFechaCv2() != null){
                    fechaCv2 = dateFormat.format(objectVO.getFechaCv2());                
                }                
                String demanda2 = "";
                if (objectVO.getDemanda2() != null){
                    demanda2 = objectVO.getDemanda2();
                }   
                String fechaDemanda2  = "-";
                if (objectVO.getFechaDemanda2() != null){
                    fechaDemanda2 = dateFormat.format(objectVO.getFechaDemanda2());                
                }                  
                String fechaNacimiento2  = "-";
                if(objectVO.getFechanacimientoInicio()!=null){
                    fechaNacimiento2=dateFormat.format(objectVO.getFechanacimientoInicio());                
                }
                String sexo2="-";
                if(objectVO.getSexoInicio()!=null){
                    sexo2=objectVO.getSexoInicio();
                }
                String descSexo2  = "-";
                if(objectVO.getDescSexoInicio()!=null){
                    descSexo2=objectVO.getDescSexoInicio();
                }                       
                String nivelCualificacion2="-";
                if(objectVO.getNivelcualificacionInicio()!=null){
                    nivelCualificacion2=objectVO.getNivelcualificacionInicio();
                }
                String descNivel2 = "-";
                if(objectVO.getDescNivelcualificacionInicio()!=null){
                    descNivel2=objectVO.getDescNivelcualificacionInicio();
                }                    
                String puestoTrabajo2="-";
                if(objectVO.getPuestotrabajoInicio()!=null){
                    puestoTrabajo2=objectVO.getPuestotrabajoInicio();
                }  
                String nOferta2="-";
                if(objectVO.getNofertaInicio()!=null){
                    nOferta2=objectVO.getNofertaInicio();
                }                    
                String grupoCotiz2="-";
                if(objectVO.getGrupocotizInicio()!=null){
                   grupoCotiz2 = objectVO.getGrupocotizInicio();
                }
                String descGrupoCotiz2 = "-";
                if(objectVO.getDescGrupocotizInicio()!=null){
                   descGrupoCotiz2 = objectVO.getDescGrupocotizInicio();
                }                    
                String durContrato2="-";
                if(objectVO.getDurcontratoInicio()!=null && !"".equals(objectVO.getDurcontratoInicio())){
                    durContrato2=Integer.toString(objectVO.getDurcontratoInicio());
                }
                String descDurContrato2="-";
                if(objectVO.getDescDurContratoInicio()!=null && !"".equals(objectVO.getDescDurContratoInicio())){
                    descDurContrato2=objectVO.getDescDurContratoInicio();
                }
                String fechaInicio2  = "-";
                if(objectVO.getFechainicioInicio()!=null){
                    fechaInicio2=dateFormat.format(objectVO.getFechainicioInicio());                
                }                    
                String edad2="-";
                if(objectVO.getEdadInicio()!=null && !"".equals(objectVO.getEdadInicio())){
                    edad2=Integer.toString(objectVO.getEdadInicio());
                }  
                String retribucionBruta2="-";
                if(objectVO.getRetribucionbrutaInicio()!=null){
                    retribucionBruta2=String.valueOf((objectVO.getRetribucionbrutaInicio().toString()).replace(".",","));
                }                    
                // CONTRATACION_FIN                   
                String municipio3="-";
                if(objectVO.getMunicipioFin()!=null){
                    municipio3=objectVO.getMunicipioFin();
                }    
                String nombre3="-";
                if(objectVO.getNombreFin()!=null){
                    nombre3=objectVO.getNombreFin();
                } 
                String apellido13="-";
                if(objectVO.getApellido1Fin()!=null){
                    apellido13=objectVO.getApellido1Fin();
                }   
                String apellido23="-";
                if(objectVO.getApellido2Fin()!=null){
                    apellido23=objectVO.getApellido2Fin();
                }  
                String dniNie3="-";
                if(objectVO.getDninieFin()!=null){
                    dniNie3=objectVO.getDninieFin();
                } 
                String sexo3="-";
                if(objectVO.getSexoFin()!=null){
                    sexo3=objectVO.getSexoFin();
                }
                String descSexo3="-";
                if(objectVO.getDescSexoFin()!=null){
                    descSexo3=objectVO.getDescSexoFin();
                }                   
                String grupoCotiz3="-";
                if(objectVO.getGrupocotizFin()!=null){
                    grupoCotiz3=objectVO.getGrupocotizFin();
                }
                String descGrupoCotiz3 = "-";
                if(objectVO.getDescGrupocotizFin()!=null){
                   descGrupoCotiz3 = objectVO.getDescGrupocotizFin();
                }                    
                String durContrato3="-";
                if(objectVO.getDurcontratoFin()!=null && !"".equals(objectVO.getDurcontratoFin())){
                    durContrato3=Integer.toString(objectVO.getDurcontratoFin());
                }
                String descDurContrato3="-";
                if(objectVO.getDescDurContratoFin()!=null && !"".equals(objectVO.getDescDurContratoFin())){
                    descDurContrato3=objectVO.getDescDurContratoFin();
                }
                String fechaInicio3  = "-";
                if(objectVO.getFechainicioFin()!=null){
                    fechaInicio3=dateFormat.format(objectVO.getFechainicioFin());                
                }                                      
                String fechaFin3  = "-";
                if(objectVO.getFechafinFin()!=null){
                    fechaFin3=dateFormat.format(objectVO.getFechafinFin());                
                }                                        
                String retribucionBruta3="-";
                if(objectVO.getRetribucionbrutaFin()!=null){
                    retribucionBruta3=String.valueOf((objectVO.getRetribucionbrutaFin().toString()).replace(".",","));
                }   
                String costeSalarial3="-";
                if(objectVO.getCostesalarialFin()!=null){
                    costeSalarial3=String.valueOf((objectVO.getCostesalarialFin().toString()).replace(".",","));
                }  
                String costesSs3="-";
                if(objectVO.getCostesssFin()!=null){
                    costesSs3=String.valueOf((objectVO.getCostesssFin().toString()).replace(".",","));
                }
                String indemFinContrato3="-";
                if(objectVO.getIndemfincontratoFin()!=null){
                    indemFinContrato3=String.valueOf((objectVO.getIndemfincontratoFin().toString()).replace(".",","));
                }   
                String costeTotalReal3="-";
                if(objectVO.getCostetotalrealFin()!=null){
                    costeTotalReal3=String.valueOf((objectVO.getCostetotalrealFin().toString()).replace(".",","));
                } 
                String subvConcedida3="-";
                if(objectVO.getSubvconcedidalanFin()!=null){
                    subvConcedida3=String.valueOf((objectVO.getSubvconcedidalanFin().toString()).replace(".",","));
                }
                String sistGrantiaJuve2="-";
                if(objectVO.getSistGrantiaJuveIni()!=null && !objectVO.getSistGrantiaJuveIni().equals("F")){
                    sistGrantiaJuve2= objectVO.getSistGrantiaJuveIni();
                }
                String descSistGrantiaJuve2="-";
                if(objectVO.getDescSistGrantiaJuveIni()!=null && !objectVO.getDescSistGrantiaJuveIni().equals("F")){
                    descSistGrantiaJuve2= objectVO.getDescSistGrantiaJuveIni();
                    final String  []labelSistGrantiaJuve2 = descSistGrantiaJuve2.split("\\|");
                    descSistGrantiaJuve2 = idiomaUsuario == 1 ? labelSistGrantiaJuve2[0] : labelSistGrantiaJuve2[1];
                }                
                String sistGrantiaJuve3="-";
                if(objectVO.getSistGrantiaJuveFin()!=null && !objectVO.getSistGrantiaJuveFin().equals("F")){
                    sistGrantiaJuve3=objectVO.getSistGrantiaJuveFin();
                }
                String descSistGrantiaJuve3="-";
                if(objectVO.getDescSistGrantiaJuveFin()!=null && !objectVO.getDescSistGrantiaJuveFin().equals("F")){
                    descSistGrantiaJuve3=objectVO.getDescSistGrantiaJuveFin();
                    final String  []labelSistGrantiaJuve3 = descSistGrantiaJuve3.split("\\|");
                    descSistGrantiaJuve3 = idiomaUsuario == 1 ? labelSistGrantiaJuve3[0] : labelSistGrantiaJuve3[1];                    
                }


        %>
        listaContrataciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=prioridad%>', '<%=denomPuesto%>', '<%=descNivel%>', '<%=nivelCualificacion%>', '<%=modContrato%>', '<%=durContrato%>', '<%=descDurContrato%>', '<%=grupoCotiz%>', '<%=costeSalarial%>', '<%=subvSolicitada%>'
                    , '<%=municipio2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>'
                    , '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>'
                    , '<%=fechaNacimiento2%>', '<%=sexo2%>', '<%=descSexo2%>', '<%=nivelCualificacion2%>', '<%=descNivel2%>', '<%=puestoTrabajo2%>', '<%=nOferta2%>', '<%=grupoCotiz2%>', '<%=descGrupoCotiz2%>', '<%=durContrato2%>', '<%=descDurContrato2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>'
                    , '<%=sistGrantiaJuve2%>', '<%=descSistGrantiaJuve2%>'
                    , '<%=municipio3%>', '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=sexo3%>', '<%=descSexo3%>', '<%=grupoCotiz3%>', '<%=descGrupoCotiz2%>', '<%=durContrato3%>', '<%=descDurContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=retribucionBruta3%>', '<%=costeSalarial3%>', '<%=costesSs3%>', '<%=indemFinContrato3%>', '<%=costeTotalReal3%>', '<%=subvConcedida3%>'
                    , '<%=sistGrantiaJuve3%>', '<%=descSistGrantiaJuve3%>'                    
        ];
//        console.log("cargarPestana listaContrataciones[<%=indice%>].length=" + listaContrataciones[<%=indice%>].length);
        listaContratacionesTabla[<%=indice%>] = ['<%=prioridad%>', '<%=denomPuesto%>', '<%=descNivel%>', '<%=modContrato%>', '<%=descDurContrato%>', '<%=grupoCotiz%>', '<%=costeSalarial%>', '<%=subvSolicitada%>'
                    , '<%=municipio2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>'
                    , '<a href="javascript:obtainDocumentFile(\'<%=cv2%>\')"><%=cv2%></a>', '<%=fechaCv2%>', '<a href="javascript:obtainDocumentFile(\'<%=demanda2%>\')"><%=demanda2%></a>', '<%=fechaDemanda2%>'
                    , '<%=fechaNacimiento2%>', '<%=descSexo2%>', '<%=descNivel2%>', '<%=puestoTrabajo2%>', '<%=nOferta2%>', '<%=descGrupoCotiz2%>', '<%=descDurContrato2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>'
                    , '<%=descSistGrantiaJuve2%>'                               
                    , '<%=municipio3%>', '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=descSexo3%>', '<%=descGrupoCotiz3%>', '<%=descDurContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=retribucionBruta3%>', '<%=costeSalarial3%>', '<%=costesSs3%>', '<%=indemFinContrato3%>', '<%=costeTotalReal3%>', '<%=subvConcedida3%>'
                    , '<%=descSistGrantiaJuve3%>'               
        ];
        listaContratacionesTabla_titulos[<%=indice%>] = ['<%=prioridad%>', '<%=denomPuesto%>', '<%=descNivel%>', '<%=modContrato%>', '<%=descDurContrato%>', '<%=grupoCotiz%>', '<%=costeSalarial%>', '<%=subvSolicitada%>'
                    , '<%=municipio2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>'
                    , '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>'
                    , '<%=fechaNacimiento2%>', '<%=descSexo2%>', '<%=descNivel2%>', '<%=puestoTrabajo2%>', '<%=nOferta2%>', '<%=descGrupoCotiz2%>', '<%=descDurContrato2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>'
                    , '<%=descSistGrantiaJuve2%>'                      
                    , '<%=municipio3%>', '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=descSexo3%>', '<%=descGrupoCotiz3%>', '<%=descDurContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=retribucionBruta3%>', '<%=costeSalarial3%>', '<%=costesSs3%>', '<%=indemFinContrato3%>', '<%=costeTotalReal3%>', '<%=subvConcedida3%>'
                    , '<%=descSistGrantiaJuve3%>'                     
        ];
//        console.log("cargarPestana listaContratacionesTabla_titulos[<%=indice%>].length=" + listaContratacionesTabla_titulos[<%=indice%>].length);
        <%
               }// for
           }// if
        %>
        iniciarTablaContrataciones();
        
        
    </script>
    <div id="popupcalendar" class="text"></div>                
</body>

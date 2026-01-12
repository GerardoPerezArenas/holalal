<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.i18n.MeLanbide86I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConstantesMeLanbide86"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.enums.GeneratedDocuments"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        if(request.getParameter("idioma") != null)
        {
            try
            {
                idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
            }
            catch(Exception ex)
            {}
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

        //Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide86I18n meLanbide86I18n = MeLanbide86I18n.getInstance();
        MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
        String numExpediente = (String)request.getAttribute("numExp");
        
        String cv = GeneratedDocuments.CV.getCode();
        String demanda = GeneratedDocuments.DEMANDA.getCode();
        
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide86/melanbide86.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide86/FixedColumnsTable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide86/IkerUtils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

        var parametrosLLamadaM86 = {
            tarea: 'preparar'
            , modulo: 'MELANBIDE86'
            , operacion: null
            , tipo: 0
            , numero: null
            , documento: null
        };

        function pulsarNuevaContratacion() {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE86&operacion=cargarNuevaContratacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 900, 1200, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaContrataciones(result[1]);
                    }
                }
            });
        }

        function pulsarModificarContratacion() {
            if (tablaContrataciones.selectedIndex != -1) {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE86&operacion=cargarModificarContratacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0], 900, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaContrataciones(result[1]);
                        }
                    }
                });
            } else {
                jsp_alerta('A', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarEliminarContratacion() {
            console.log("pulsarEliminarContratacion tablaContrataciones.selectedIndex = " +
                    tablaContrataciones.selectedIndex);
            if (tablaContrataciones.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    elementoVisible('on', 'barraProgresoIKER');
                    var parametros = 'tarea=preparar&modulo=MELANBIDE86&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0] +
                            '&docCV=' + listaContrataciones[tablaContrataciones.selectedIndex][20] +
                            '&fechaCV=' + listaContrataciones[tablaContrataciones.selectedIndex][21] +
                            '&docDemanda=' + listaContrataciones[tablaContrataciones.selectedIndex][22] +
                            '&fechaDemanda=' + listaContrataciones[tablaContrataciones.selectedIndex][23];     
                    console.log("pulsarEliminarContratacion: " + parametros);
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
                jsp_alerta('A', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
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
                    var resultado = jsp_alerta('A', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                    return false;
                }
                var resultado = jsp_alerta('', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE86&operacion=procesarXML&tipo=0&numero=<%=numExpediente%>';
                    document.forms[0].action = url + '?' + parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].encoding = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    document.forms[0].target = 'uploadFrameCarga';
                    document.forms[0].submit();
                }
                return true;
            } else {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                return false;
            }
        }

        function iniciarTablaContrataciones() {
            tablaContrataciones = new FixedColumnTable(document.getElementById('listaContrataciones'), 1350, 1400, 'listaContrataciones');

            tablaContrataciones.addColumna('50', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.numPuesto")%>", "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.numPuesto")%>", 'Number');
            tablaContrataciones.addColumna('400', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.denomPuesto")%>");
            tablaContrataciones.addColumna('200', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.descActDes1")%>");
            tablaContrataciones.addColumna('180', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.titulacion1")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.tipoCont1")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato1")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.grupoCotiz1")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.costeSalarial1")%>");
            tablaContrataciones.addColumna('100', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.subvSolicitada1")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.cainVinn1")%>");

            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.nOferta2")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.nombre2")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido12")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido22")%>");
            tablaContrataciones.addColumna('80', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.dniNie2")%>");

            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.cv2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaCv2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.demanda2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaDemanda2")%>");

            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.sexo2")%>");
            tablaContrataciones.addColumna('400', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.denomPuesto2")%>");
            tablaContrataciones.addColumna('200', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.actDes2")%>");
            tablaContrataciones.addColumna('180', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.titulacion2")%>");
            tablaContrataciones.addColumna('100', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.tipoCont2")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato2")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.grupoCotiz2")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaNacimiento2")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaInicio2")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.edad2")%>");
            tablaContrataciones.addColumna('70', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.retribucionBruta2")%>");
            tablaContrataciones.addColumna('100', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.cainVinn2")%>");

            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.nombre3")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido13")%>");
            tablaContrataciones.addColumna('150', 'left', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.apellido23")%>");
            tablaContrataciones.addColumna('80', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.dniNie3")%>");
            tablaContrataciones.addColumna('60', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.durContrato3")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaInicio3")%>");
            tablaContrataciones.addColumna('70', 'center', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.fechaFin3")%>");
            tablaContrataciones.addColumna('70', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.costeSalarial3")%>");
            tablaContrataciones.addColumna('70', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.costesSS3")%>");
            tablaContrataciones.addColumna('70', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.costeTotalReal")%>");
            tablaContrataciones.addColumna('70', 'right', "<%=meLanbide86I18n.getMensaje(idiomaUsuario,"filaContratacion.subvConcedidaLan3")%>");
            // 4.770
            tablaContrataciones.numColumnasFijas = 2;

            for (var cont = 0; cont < listaContratacionesTabla.length; cont++) {
                tablaContrataciones.addFilaConFormato(listaContratacionesTabla[cont], listaContratacionesTabla_titulos[cont], listaContratacionesTabla_estilos[cont]);
            }

            tablaContrataciones.displayCabecera = true;
            tablaContrataciones.height = 360;

            tablaContrataciones.altoCabecera = 60;
            tablaContrataciones.scrollWidth = 5200;
            tablaContrataciones.dblClkFunction = 'dblClckTablaContrataciones';
            tablaContrataciones.displayTabla();
            tablaContrataciones.pack();
        }

        function recargarTablaContrataciones(contrataciones) {
            var fila;
            listaContrataciones = new Array();
            listaContratacionesTabla = new Array();
            listaContratacionesTabla_titulos = new Array();
            for (var i = 0; i < contrataciones.length; i++) {
                fila = contrataciones[i];
                listaContrataciones[i] = [
                    fila.id,
                    fila.numPuesto,
                    fila.denomPuesto,
                    fila.actDes1,
                    fila.descActDes1,
                    fila.titulacion1,
                    fila.tipoCont1,
                    fila.durContrato1,
                    fila.grupoCotiz1,
                    fila.descGrupoCotiz1,
                    fila.costeSalarial1,
                    fila.subvSolicitada1,
                    fila.cainVinn1,
                    fila.numExp2,
                    fila.numPuesto2,
                    fila.nOferta2,
                    fila.nombre2,
                    fila.apellido12,
                    fila.apellido22,
                    fila.dniNie2,
                    fila.cv2,
                    fila.fechaCv2Str,
                    fila.demanda2,
                    fila.fechaDemanda2Str,
                    fila.sexo2,
                    fila.descSexo2,
                    fila.denomPuesto2,
                    fila.actDes2,
                    fila.descActDes2,
                    fila.titulacion2,
                    fila.tipoCont2,
                    fila.durContrato2,
                    fila.grupoCotiz2,
                    fila.descGrupoCotiz2,
                    fila.fechaNacimiento2,
                    fila.fecNacStr2,
                    fila.fechaInicio2fec,
                    fila.fecIniStr2,
                    fila.edad2,
                    fila.retribucionBruta2,
                    fila.cainVinn2,
                    fila.numExp3,
                    fila.numPuesto3,
                    fila.nombre3,
                    fila.apellido13,
                    fila.apellido23,
                    fila.dniNie3,
                    fila.durContrato3,
                    fila.fechaInicio3,
                    fila.fecIniStr3,
                    fila.fechaFin3,
                    fila.fecFinStr3,
                    fila.costeSalarial3,
                    fila.costesSS3,
                    fila.costeTotalReal,
                    fila.subvConcedidaLan3
                ];
                listaContratacionesTabla[i] = [
                    (fila.numPuesto != undefined ? fila.numPuesto : "-"),
                    (fila.denomPuesto != undefined ? fila.denomPuesto : "-"),
                    (fila.descActDes1 != undefined ? fila.descActDes1 : "-"),
                    (fila.titulacion1 != undefined ? fila.titulacion1 : "-"),
                    (fila.tipoCont1 != undefined ? fila.tipoCont1 : "-"),
                    (fila.durContrato1 != undefined ? fila.durContrato1 : "-"),
                    (fila.descGrupoCotiz1 != undefined ? fila.descGrupoCotiz1 : "-"),
                    (fila.costeSalarial1 != undefined ? fila.costeSalarial1 : "-"),
                    (fila.subvSolicitada1 != undefined ? fila.subvSolicitada1 : "-"),
                    (fila.cainVinn1 != undefined ? fila.cainVinn1 : "-"),
                    (fila.nOferta2 != undefined ? fila.nOferta2 : "-"),
                    (fila.nombre2 != undefined ? fila.nombre2 : "-"),
                    (fila.apellido12 != undefined ? fila.apellido12 : "-"),
                    (fila.apellido22 != undefined ? fila.apellido22 : "-"),
                    (fila.dniNie2 != undefined ? fila.dniNie2 : "-"),

                    (fila.cv2 != undefined ? '<a href="javascript:obtainDocumentFile(\'' + fila.cv2 + '\')">' + fila.cv2 + '</a>' : ""),
                    (fila.fechaCv2Str != undefined ? fila.fechaCv2Str : "-"),
                    (fila.demanda2 != undefined ? '<a href="javascript:obtainDocumentFile(\'' + fila.demanda2 + '\')">' + fila.demanda2 + '</a>' : ""),
                    (fila.fechaDemanda2Str != undefined ? fila.fechaDemanda2Str : "-"),

                    (fila.descSexo2 != undefined ? fila.descSexo2 : "-"),
                    (fila.denomPuesto2 != undefined ? fila.denomPuesto2 : "-"),
                    (fila.descActDes2 != undefined ? fila.descActDes2 : "-"),
                    (fila.titulacion2 != undefined ? fila.titulacion2 : "-"),
                    (fila.tipoCont2 != undefined ? fila.tipoCont2 : "-"),
                    (fila.durContrato2 != undefined ? fila.durContrato2 : "-"),
                    (fila.descGrupoCotiz2 != undefined ? fila.descGrupoCotiz2 : "-"),
                    (fila.fecNacStr2 != undefined ? fila.fecNacStr2 : "-"),
                    (fila.fecIniStr2 != undefined ? fila.fecIniStr2 : "-"),
                    (fila.edad2 != undefined ? fila.edad2 : "-"),
                    (fila.retribucionBruta2 != undefined ? fila.retribucionBruta2 : "-"),
                    (fila.cainVinn2 != undefined ? fila.cainVinn2 : "-"),
                    (fila.nombre3 != undefined ? fila.nombre3 : "-"),
                    (fila.apellido13 != undefined ? fila.apellido13 : "-"),
                    (fila.apellido23 != undefined ? fila.apellido23 : "-"),
                    (fila.dniNie3 != undefined ? fila.dniNie3 : "-"),
                    (fila.durContrato3 != undefined ? fila.durContrato3 : "-"),
                    (fila.fecIniStr3 != undefined ? fila.fecIniStr3 : "-"),
                    (fila.fecFinStr3 != undefined ? fila.fecFinStr3 : "-"),
                    (fila.costeSalarial3 != undefined ? fila.costeSalarial3 : "-"),
                    (fila.costesSS3 != undefined ? fila.costesSS3 : "-"),
                    (fila.costeTotalReal != undefined ? fila.costeTotalReal : "-"),
                    (fila.subvConcedidaLan3 != undefined ? fila.subvConcedidaLan3 : "-")
                ];
                listaContratacionesTabla_titulos[i] = [
                    (fila.numPuesto != undefined ? fila.numPuesto : "-"),
                    (fila.denomPuesto != undefined ? fila.denomPuesto : "-"),
                    (fila.descActDes1 != undefined ? fila.descActDes1 : "-"),
                    (fila.titulacion1 != undefined ? fila.titulacion1 : "-"),
                    (fila.tipoCont1 != undefined ? fila.tipoCont1 : "-"),
                    (fila.durContrato1 != undefined ? fila.durContrato1 : "-"),
                    (fila.descGrupoCotiz1 != undefined ? fila.descGrupoCotiz1 : "-"),
                    (fila.costeSalarial1 != undefined ? fila.costeSalarial1 : "-"),
                    (fila.subvSolicitada1 != undefined ? fila.subvSolicitada1 : "-"),
                    (fila.cainVinn1 != undefined ? fila.cainVinn1 : "-"),
                    (fila.nOferta2 != undefined ? fila.nOferta2 : "-"),
                    (fila.nombre2 != undefined ? fila.nombre2 : "-"),
                    (fila.apellido12 != undefined ? fila.apellido12 : "-"),
                    (fila.apellido22 != undefined ? fila.apellido22 : "-"),
                    (fila.dniNie2 != undefined ? fila.dniNie2 : "-"),

                    (fila.cv2 != undefined ? fila.cv2 : ""),
                    (fila.fechaCv2Str != undefined ? fila.fechaCv2Str : "-"),
                    (fila.demanda2 != undefined ? fila.demanda2 : ""),
                    (fila.fechaDemanda2Str != undefined ? fila.fechaDemanda2Str : "-"),

                    (fila.descSexo2 != undefined ? fila.descSexo2 : "-"),
                    (fila.denomPuesto2 != undefined ? fila.denomPuesto2 : "-"),
                    (fila.descActDes2 != undefined ? fila.descActDes2 : "-"),
                    (fila.titulacion2 != undefined ? fila.titulacion2 : "-"),
                    (fila.tipoCont2 != undefined ? fila.tipoCont2 : "-"),
                    (fila.durContrato2 != undefined ? fila.durContrato2 : "-"),
                    (fila.descGrupoCotiz2 != undefined ? fila.descGrupoCotiz2 : "-"),
                    (fila.fecNacStr2 != undefined ? fila.fecNacStr2 : "-"),
                    (fila.fecIniStr2 != undefined ? fila.fecIniStr2 : "-"),
                    (fila.edad2 != undefined ? fila.edad2 : "-"),
                    (fila.retribucionBruta2 != undefined ? fila.retribucionBruta2 : "-"),
                    (fila.cainVinn2 != undefined ? fila.cainVinn2 : "-"),
                    (fila.nombre3 != undefined ? fila.nombre3 : "-"),
                    (fila.apellido13 != undefined ? fila.apellido13 : "-"),
                    (fila.apellido23 != undefined ? fila.apellido23 : "-"),
                    (fila.dniNie3 != undefined ? fila.dniNie3 : "-"),
                    (fila.durContrato3 != undefined ? fila.durContrato3 : "-"),
                    (fila.fecIniStr3 != undefined ? fila.fecIniStr3 : "-"),
                    (fila.fecFinStr3 != undefined ? fila.fecFinStr3 : "-"),
                    (fila.costeSalarial3 != undefined ? fila.costeSalarial3 : "-"),
                    (fila.costesSS3 != undefined ? fila.costesSS3 : "-"),
                    (fila.costeTotalReal != undefined ? fila.costeTotalReal : "-"),
                    (fila.subvConcedidaLan3 != undefined ? fila.subvConcedidaLan3 : "-")
                ];
            }
            iniciarTablaContrataciones();
        }

        function actualizarPestana() {
            limpiarFormulario();
            var parametros = 'tarea=preparar&modulo=MELANBIDE86&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
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
                elementoVisible('off', 'barraProgresoIKER');
                recargarTablaContrataciones(contrataciones);
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
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
            } else if (codigo == "2") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
            } else if (codigo == "3") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
            } else if (codigo == "4") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
            } else if (codigo == "5") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
            } else if (codigo == "6") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
            } else if (codigo == "-1") {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
            } else {
                jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
            }
        }

        function limpiarFormulario() {
            document.getElementById('fichero_xml').value = "";
        }

        function pulsarDatosCVIntermediacion() {
            pleaseWait('on');

            var dataParameter = $.extend({}, parametrosLLamadaM86);

            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
            dataParameter.documento = '<%=cv%>';

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

            var dataParameter = $.extend({}, parametrosLLamadaM86);

            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
            dataParameter.documento = '<%=demanda%>';

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

        function obtainDocumentFile(nameFile) {
//            console.log("obtainDocumentFile nameFile = " + nameFile);
//            console.log("obtainDocumentFile #urlBaseLlamadaM86 = " + $("#urlBaseLlamadaM86").val());

            var dataParameter = $.extend({}, parametrosLLamadaM86);
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.nameFile = nameFile;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'obtainDocumentFile';
            window.open($("#urlBaseLlamadaM86").val() + "?" + $.param(dataParameter), "_blank");
        }
    </script> 
</head>
<body class="bandaBody">
    <input type="hidden" id="urlBaseLlamadaM86" name="urlBaseLlamadaM86" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>            
    <div class="tab-page" id="tabPage861" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana861"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage861"));</script>
        <div id="barraProgresoIKER" style="visibility: hidden;">
            <div class="contenedorHidepage">
                <div class="textoHide">
                    <span>
                        <%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                    </span>
                </div>
                <div class="imagenHide">
                    <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                </div>
            </div>
        </div>
        <br/>
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
        <div>    
            <br>
            <div id="divGeneral">     
                <div id="listaContrataciones"  align="center"></div>
            </div>
            <br>
            <div class="legendRojo" id="comentario1"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"legend.comentario1")%></div>
            <div class="legendRojo" id="comentario2"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"legend.comentario2")%></div>
            <div class="legendRojo" id="comentario3"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"legend.comentario3")%></div>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnNuevaContratacion" name="btnNuevaContratacion" class="botonGeneral"  value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaContratacion();">
                <input type="button" id="btnModificarContratacion" name="btnModificarContratacion" class="botonGeneral" value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                <input type="button" id="btnEliminarContratacion" name="btnEliminarContratacion"   class="botonGeneral" value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnCargarRegistros" name="btnCargarRegistros" class="botonMasLargo" value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXML();">
                <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type= "button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" class="botonGeneral" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();" >
                </input>
                <input type= "button" id="botonCertificadoDemanda" name="botonCertificadoDemanda" class="botonMasLargo" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();" >
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
                    String numPuesto = "-";
                    if(objectVO.getNumPuesto()!=null){
                       numPuesto=String.valueOf(objectVO.getNumPuesto());
                    }		   
                    String denomPuesto = "-";
                    if(objectVO.getDenomPuesto()!=null){
                        denomPuesto=objectVO.getDenomPuesto();
                    }			
                    String actDes1 = "-";
                    if(objectVO.getActDes1()!=null){
                            actDes1=objectVO.getActDes1();
                    }
                    String descActDes1 = "-";
                    if(objectVO.getDescActDes1()!=null){
                        descActDes1=objectVO.getDescActDes1();
                    }
                    String titulacion1 = "-";
                    if(objectVO.getTitulacion1()!=null){
                            titulacion1=objectVO.getTitulacion1();
                    }
                    String tipoCont1 = "-";
                    if(objectVO.getTipoCont1()!=null){
                        tipoCont1=objectVO.getTipoCont1();
                    }
                    String durContrato1 = "-";
                    if(objectVO.getDurContrato1()!=null){
                        durContrato1=String.valueOf(objectVO.getDurContrato1());
                    }
                    String grupoCotiz1 = "-";
                    if(objectVO.getGrupoCotiz1()!=null){
                        grupoCotiz1=objectVO.getGrupoCotiz1();
                    }
                    String descGrupoCotiz1 = "-";
                    if(objectVO.getDescGrupoCotiz1()!=null){
                        descGrupoCotiz1=objectVO.getDescGrupoCotiz1();
                    }
                    String costeSalarial1 = "-";
                    if(objectVO.getCosteSalarial1()!=null){
                        costeSalarial1=String.valueOf(objectVO.getCosteSalarial1().toString().replace(".",","));
                    }
                    String subvSolicitada1 = "-";
                    if(objectVO.getSubvSolicitada1()!=null){
                       subvSolicitada1=String.valueOf(objectVO.getSubvSolicitada1().toString().replace(".",","));
                    }
                    String cainVinn1 = "-";
                    if(objectVO.getCainVinn1()!=null){
                        cainVinn1=objectVO.getCainVinn1();
                    }
// CONTRATACION_INI
                    String numExp2  = "-";
                    if(objectVO.getNumExp2()!=null){
                        numExp2=objectVO.getNumExp2();
                    }
                    String numPuesto2  = "-";
                    if(objectVO.getNumPuesto2()!=null){
                        numPuesto2=String.valueOf(objectVO.getNumPuesto2());
                    }
                    String nOferta2  = "-";
                    if(objectVO.getnOferta2()!=null){
                        nOferta2=objectVO.getnOferta2();
                    }
                    String nombre2  = "-";
                    if(objectVO.getNombre2()!=null){
                        nombre2=objectVO.getNombre2();
                    }
                    String apellido12  = "-";
                    if(objectVO.getApellido12 ()!=null){
                        apellido12=objectVO.getApellido12();
                    }
                    String apellido22  = "-";
                    if(objectVO.getApellido22()!=null){
                        apellido22=objectVO.getApellido22();
                    }
                    String dniNie2  = "-";
                    if(objectVO.getDniNie2()!=null){
                        dniNie2=objectVO.getDniNie2();
                    }
                    
                    String cv2  = "";
                    if(objectVO.getCv2() != null){
                        cv2 = objectVO.getCv2();
                    }
                    String fechaCv2  = "-";
                    if(objectVO.getFechaCv2() != null){
                        fechaCv2 = dateFormat.format(objectVO.getFechaCv2());
                    }
                    String demanda2  = "";
                    if(objectVO.getDemanda2() != null){
                        demanda2 = objectVO.getDemanda2();
                    }
                    String fechaDemanda2  = "-";
                    if(objectVO.getFechaDemanda2() != null){
                        fechaDemanda2 = dateFormat.format(objectVO.getFechaDemanda2());
                    }
                    
                    String sexo2  = "-";
                    if(objectVO.getSexo2()!=null){
                        sexo2=objectVO.getSexo2();
                    }
                    String descSexo2  = "-";
                    if(objectVO.getDescSexo2()!=null){
                        descSexo2=objectVO.getDescSexo2();
                    }
                    String denomPuesto2  = "-";
                    if(objectVO.getDenomPuesto2()!=null){
                        denomPuesto2=objectVO.getDenomPuesto2();
                    }
                    String actDes2  = "-";
                    if(objectVO.getActDes2()!=null){
                        actDes2=objectVO.getActDes2();
                    }
                    String descActDes2  = "-";
                    if(objectVO.getDescActDes2()!=null){
                        descActDes2=objectVO.getDescActDes2();
                    }
                    String titulacion2  = "-";
                    if(objectVO.getTitulacion2()!=null){
                        titulacion2=objectVO.getTitulacion2();
                    }
                    String tipoCont2  = "-";
                    if(objectVO.getTipoCont2()!=null){
                        tipoCont2=objectVO.getTipoCont2();
                    }
                    String durContrato2  = "-";
                    if(objectVO.getDurContrato2()!=null){
                        durContrato2=String.valueOf(objectVO.getDurContrato2());
                    }
                    String grupoCotiz2  = "-";
                    if(objectVO.getGrupoCotiz2()!=null){
                        grupoCotiz2=objectVO.getGrupoCotiz2();
                    }
                    String descGrupoCotiz2  = "-";
                    if(objectVO.getDescGrupoCotiz2()!=null){
                        descGrupoCotiz2=objectVO.getDescGrupoCotiz2();
                    }
                    String fechaNacimiento2  = "-";
                    if(objectVO.getFechaNacimiento2()!=null){
                        fechaNacimiento2=dateFormat.format(objectVO.getFechaNacimiento2());                
                    }
                    String fechaInicio2  = "-";
                    if(objectVO.getFechaInicio2()!=null){
                        fechaInicio2=dateFormat.format(objectVO.getFechaInicio2());                
                    }
                    String edad2  = "-";
                    if(objectVO.getEdad2()!=null){
                        edad2=String.valueOf(objectVO.getEdad2());
                    }
                    String retribucionBruta2  = "-";
                    if(objectVO.getRetribucionBruta2()!=null){
                       retribucionBruta2=String.valueOf(objectVO.getRetribucionBruta2().toString().replace(".",","));
                    }
                    String cainVinn2  = "-";
                    if(objectVO.getCainVinn2()!=null){
                        cainVinn2=objectVO.getCainVinn2();
                    }

                    // CONTRATACION_FIN
                    String numExp3  = "-";
                    if(objectVO.getNumExp3()!=null){
                        numExp3=objectVO.getNumExp3();
                    }
                    String numPuesto3  = "-";
                    if(objectVO.getNumPuesto3()!=null){
                        numPuesto3=String.valueOf(objectVO.getNumPuesto3());
                    }
                    String nombre3  = "-";
                    if(objectVO.getNombre3()!=null){
                        nombre3=objectVO.getNombre3();
                    }
                    String apellido13  = "-";
                    if(objectVO.getApellido13 ()!=null){
                        apellido13=objectVO.getApellido13();
                    }
                    String apellido23  = "-";
                    if(objectVO.getApellido23()!=null){
                        apellido23=objectVO.getApellido23();
                    }
                    String dniNie3  = "-";
                    if(objectVO.getDniNie3()!=null){
                        dniNie3=objectVO.getDniNie3();
                    }
                    String durContrato3  = "-";
                    if(objectVO.getDurContrato3()!=null){
                        durContrato3=String.valueOf(objectVO.getDurContrato3());
                    }
                    String fechaInicio3  = "-";
                    if(objectVO.getFechaInicio3()!=null){
                        fechaInicio3=dateFormat.format(objectVO.getFechaInicio3());                
                    }
                    String fechaFin3  = "-";
                    if(objectVO.getFechaFin3()!=null){
                        fechaFin3=dateFormat.format(objectVO.getFechaFin3());                
                    }
                    String costeSalarial3  = "-";
                    if(objectVO.getCosteSalarial3()!=null){
                       costeSalarial3=String.valueOf(objectVO.getCosteSalarial3().toString().replace(".",","));
                    }
                    String costesSS3  = "-";
                    if(objectVO.getCostesSS3()!=null){
                       costesSS3=String.valueOf(objectVO.getCostesSS3().toString().replace(".",","));
                    }
                    String costeTotalReal  = "-";
                    if(objectVO.getCosteTotalReal()!=null){
                       costeTotalReal=String.valueOf(objectVO.getCosteTotalReal().toString().replace(".",","));
                    }
                    String subvConcedidaLan3  = "-";
                    if(objectVO.getSubvConcedidaLan3()!=null){
                       subvConcedidaLan3=String.valueOf(objectVO.getSubvConcedidaLan3().toString().replace(".",","));
                    }                               
        %>
        listaContrataciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numPuesto%>', '<%=denomPuesto%>', '<%=actDes1%>', '<%=descActDes1%>', '<%=titulacion1%>', '<%=tipoCont1%>', '<%=durContrato1%>', '<%=grupoCotiz1%>', '<%=descGrupoCotiz1%>', '<%=costeSalarial1%>', '<%=subvSolicitada1%>', '<%=cainVinn1%>',
            '<%=numExp2%>', '<%=numPuesto2%>', '<%=nOferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>',
            '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>',
            '<%=sexo2%>', '<%=descSexo2%>', '<%=denomPuesto2%>', '<%=actDes2%>', '<%=descActDes2%>', '<%=titulacion2%>', '<%=tipoCont2%>', '<%=durContrato2%>', '<%=grupoCotiz2%>', '<%=descGrupoCotiz2%>', '<%=fechaNacimiento2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>', '<%=cainVinn2%>',
            '<%=numExp3%>', '<%=numPuesto3%>', '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=durContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=costeSalarial3%>', '<%=costesSS3%>', '<%=costeTotalReal%>', '<%=subvConcedidaLan3%>'
        ];
        listaContratacionesTabla[<%=indice%>] = ['<%=numPuesto%>', '<%=denomPuesto%>', '<%=descActDes1%>', '<%=titulacion1%>', '<%=tipoCont1%>', '<%=durContrato1%>', '<%=descGrupoCotiz1%>', '<%=costeSalarial1%>', '<%=subvSolicitada1%>', '<%=cainVinn1%>',
            '<%=nOferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>',
            '<a href="javascript:obtainDocumentFile(\'<%=cv2%>\')"><%=cv2%></a>', '<%=fechaCv2%>', '<a href="javascript:obtainDocumentFile(\'<%=demanda2%>\')"><%=demanda2%></a>', '<%=fechaDemanda2%>',
            '<%=descSexo2%>', '<%=denomPuesto2%>', '<%=descActDes2%>', '<%=titulacion2%>', '<%=tipoCont2%>', '<%=durContrato2%>', '<%=descGrupoCotiz2%>', '<%=fechaNacimiento2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>', '<%=cainVinn2%>',
            '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=durContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=costeSalarial3%>', '<%=costesSS3%>', '<%=costeTotalReal%>', '<%=subvConcedidaLan3%>'
        ];
        listaContratacionesTabla_titulos[<%=indice%>] = ['<%=numPuesto%>', '<%=denomPuesto%>', '<%=descActDes1%>', '<%=titulacion1%>', '<%=tipoCont1%>', '<%=durContrato1%>', '<%=descGrupoCotiz1%>', '<%=costeSalarial1%>', '<%=subvSolicitada1%>', '<%=cainVinn1%>',
            '<%=nOferta2%>', '<%=nombre2%>', '<%=apellido12%>', '<%=apellido22%>', '<%=dniNie2%>',
            '<%=cv2%>', '<%=fechaCv2%>', '<%=demanda2%>', '<%=fechaDemanda2%>',
            '<%=descSexo2%>', '<%=denomPuesto2%>', '<%=descActDes2%>', '<%=titulacion2%>', '<%=tipoCont2%>', '<%=durContrato2%>', '<%=descGrupoCotiz2%>', '<%=fechaNacimiento2%>', '<%=fechaInicio2%>', '<%=edad2%>', '<%=retribucionBruta2%>', '<%=cainVinn2%>',
            '<%=nombre3%>', '<%=apellido13%>', '<%=apellido23%>', '<%=dniNie3%>', '<%=durContrato3%>', '<%=fechaInicio3%>', '<%=fechaFin3%>', '<%=costeSalarial3%>', '<%=costesSS3%>', '<%=costeTotalReal%>', '<%=subvConcedidaLan3%>'
        ];
        <%
               }// for
           }// if
        %>
        iniciarTablaContrataciones();
    </script>
    <div id="popupcalendar" class="text"></div>                
</body>



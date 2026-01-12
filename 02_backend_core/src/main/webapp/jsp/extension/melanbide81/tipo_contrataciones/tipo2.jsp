<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo2VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.io.ObjectInputStream" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <%
             UsuarioValueObject usuarioVO = new UsuarioValueObject();
             int idiomaUsuario = 1;
             int apl = 5;
             String css = "";
             try {
                 if (session.getAttribute("usuario") != null) {
                     usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                     apl = usuarioVO.getAppCod();
                     idiomaUsuario = usuarioVO.getIdioma();
                     css = usuarioVO.getCss();
                 }
              
             }catch(Exception ex) {
             }
           
             //Clase para internacionalizar los mensajes de la aplicación.
             MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
             MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();  
             final String tabla2 = ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES);   
             final String tipoDocCv2 = es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(
                     ConstantesMeLanbide67.DOCUMENTO_CV, 
                     ConstantesMeLanbide67.FICHERO_PROPIEDADES);
             final String tipoDocDemanda2 = es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(
                     ConstantesMeLanbide67.DOCUMENTO_DEMANDA, 
                     ConstantesMeLanbide67.FICHERO_PROPIEDADES);             
             String numExpediente = (String)request.getAttribute("numExp");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var mensajeValidacion = '';
            var tablaTipo2;
            var divListaTipo2 = new Array();
            var listaTipo2Tabla = new Array();
            var listaTipo2Tabla_titulos = new Array();
            var listaTipo2Tabla_estilos = new Array();
            let tabla2 = '<%=tabla2%>';

            function pulsarNuevoTipo2() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarNuevoTipo2&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 900, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
//                            recargarTablaTipos2(result[1]);
                            recargarDatosExpediente();
                        }
                    }
                });
            }

            function pulsarModificarTipo2() {
                if (tablaTipo2.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarModificarTipo2&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + divListaTipo2[tablaTipo2.selectedIndex][0], 900, 800, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
//                                recargarTablaTipos2(result[1]);
                                recargarDatosExpediente();
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarTipo2() {
                if (tablaTipo2.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoLPEEL');

                        var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=eliminarTipo2&tipo=0&numExp=<%=numExpediente%>&id=' + divListaTipo2[tablaTipo2.selectedIndex][0];

                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana2, // Usar la misma función que melanbide84
                                error: mostrarErrorEliminarTipo2
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoLPEEL');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }
            function pulsarExportarExcellAporEmpresas() {
                var parametros = "";
                parametros = '?tarea=preparar&modulo=MELANBIDE81&operacion=generarExcelAporContratacionesEmpresas&tipo=0&numExp=<%=numExpediente%>';
                window.open(url + parametros, "_blank");

            }

            function crearTablaTipo2() {
                tablaTipo2 = new FixedColumnTable(document.getElementById('divListaTipo2'), 1500, 1500, 'divListaTipo2');

                tablaTipo2.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.entidadLocalBeneficiaria")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.empresacontratante")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.CIF")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.CCC")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.nombre")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.apellido1")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.apellido2")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.DNI")%>");
                tablaTipo2.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.cv2")%>");
                tablaTipo2.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaCv2")%>");
                tablaTipo2.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.demanda2")%>");
                tablaTipo2.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaDemanda2")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.NAF")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fecnacimiento")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.sexo")%>");
                tablaTipo2.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.grupoDeCotizacion")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.tipocontrato")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fechaInicioContrato")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fechaFinContrato")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.porcJorn")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.duracionDelContrato")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.edad")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.municipioDeResidencia")%>");
                tablaTipo2.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeSalarial")%>");
                tablaTipo2.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeSeguridadSocial")%>");
                tablaTipo2.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeTotal")%>");
                tablaTipo2.addColumna('100', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.colectivo")%>");
                tablaTipo2.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.inscritaDesempleada")%>");
                tablaTipo2.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.certInterventor")%>");
                tablaTipo2.addColumna('150', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.subvencionConcedida")%>");
                tablaTipo2.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.priPago")%>");
                tablaTipo2.addColumna('150', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.subLiquidada")%>");
                tablaTipo2.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.segunPago")%>");
                tablaTipo2.addColumna('250', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.observaciones")%>");

                for (var cont = 0; cont < listaTipo2Tabla.length; cont++) {
                    tablaTipo2.addFilaConFormato(listaTipo2Tabla[cont], listaTipo2Tabla_titulos[cont], listaTipo2Tabla_estilos[cont]);
                }
                tablaTipo2.displayCabecera = true;
                tablaTipo2.height = 360;

                tablaTipo2.altoCabecera = 60;
                tablaTipo2.scrollWidth = 4800;
                tablaTipo2.dblClkFunction = 'dblClckTablaTipo2';
                tablaTipo2.lineas = listaTipo2Tabla;
                tablaTipo2.displayTabla();
                tablaTipo2.pack();
            }

            function recargarTablaTipos2(tipos2) {
                var fila;
                divListaTipo2 = new Array();
                listaTipo2Tabla = new Array();
                listaTipo2Tabla_titulos = new Array();
                listaTipo2Tabla_estilos = new Array();

                for (var i = 0; i < tipos2.length; i++) {
                    fila = tipos2[i];
                    let lineaCv = '<a href="javascript:obtainDocumentFile(\'' + fila.id + '\', \'' + fila.nombreFicheroCv + '\', \'' + tipoDocCv + '\', \'' + tabla2 + '\')">' + fila.nombreFicheroCv + '</a>';
                    let lineaDemanda = '<a href="javascript:obtainDocumentFile(\'' + fila.id + '\', \'' + fila.nombreFicheroDemanda + '\', \'' + tipoDocDemanda + '\', \'' + tabla2 + '\')">' + fila.nombreFicheroDemanda + '</a>';

                    divListaTipo2[i] = [fila.id, fila.entbene, fila.empcontra, fila.cif, fila.ccc, fila.nombre, fila.apellido1, fila.apellido2, fila.dni, fila.nombreFicheroCv, fila.fechaCvStr, fila.nombreFicheroDemanda, fila.fechaDemandaStr, fila.naf, fila.fecnacimiento, fila.sexo, fila.descSexo, fila.grupocot, fila.descGrupocot, fila.tipocontrato, fila.descTipocontrato, fila.fecIni, fila.fecfin, fila.porcJorn, fila.durcontrato, fila.edad, fila.municipio, fila.costesal, fila.costess, fila.costetotal, fila.colectivo, fila.descColectivo, fila.inscrita, fila.descInscrita, fila.certinter, fila.subconcedida, fila.pago1, fila.subliquidada, fila.pago2, fila.observaciones];

                    listaTipo2Tabla[i] = [fila.entbene, fila.empcontra, fila.cif, fila.ccc, fila.nombre, fila.apellido1, fila.apellido2, fila.dni, lineaCv, fila.fechaCvStr, lineaDemanda, fila.fechaDemandaStr, fila.naf, fila.fecnacimientoStr, fila.descSexo,
                        fila.descGrupocot, fila.descTipocontrato, fila.fecIniStr, fila.fecfinStr, fila.porcJorn, fila.durcontrato, fila.edad, fila.municipio, formatNumero(fila.costesal) + ' \u20ac', formatNumero(fila.costess) + ' \u20ac', formatNumero(fila.costetotal) + ' \u20ac',
                        (fila.descColectivo != undefined ? fila.descColectivo : "-"), (fila.descInscrita != undefined ? fila.descInscrita : "-"), (fila.certinter != undefined ? fila.certinter : "-"), (fila.subconcedida != undefined ? formatNumero(fila.subconcedida) + ' \u20ac' : "-"),
                        (fila.pago1 != undefined ? formatNumero(fila.pago1) + ' \u20ac' : "-"), (fila.subliquidada != undefined ? formatNumero(fila.subliquidada) + ' \u20ac' : "-"), (fila.pago2 != undefined ? formatNumero(fila.pago2) + ' \u20ac' : "-"), (fila.observaciones != undefined ? fila.observaciones : "-")];

                    listaTipo2Tabla_titulos[i] = [fila.entbene, fila.empcontra, fila.cif, fila.ccc, fila.nombre, fila.apellido1, fila.apellido2, fila.dni, fila.nombreFicheroCv, fila.fechaCvStr, fila.nombreFicheroDemanda, fila.fechaDemandaStr, fila.naf, fila.fecnacimientoStr, fila.descSexo,
                        fila.descGrupocot, fila.descTipocontrato, fila.fecIniStr, fila.fecfinStr, fila.porcJorn, fila.durcontrato, fila.edad, fila.municipio, formatNumero(fila.costesal) + ' \u20ac', formatNumero(fila.costess) + ' \u20ac', formatNumero(fila.costetotal) + ' \u20ac',
                        (fila.descColectivo != undefined ? fila.descColectivo : "-"), (fila.descInscrita != undefined ? fila.descInscrita : "-"), (fila.certinter != undefined ? fila.certinter : "-"), (fila.subconcedida != undefined ? formatNumero(fila.subconcedida) + ' \u20ac' : "-"),
                        (fila.pago1 != undefined ? formatNumero(fila.pago1) + ' \u20ac' : "-"), (fila.subliquidada != undefined ? formatNumero(fila.subliquidada) + ' \u20ac' : "-"), (fila.pago2 != undefined ? formatNumero(fila.pago2) + ' \u20ac' : "-"), (fila.observaciones != undefined ? fila.observaciones : "-")];

                    listaTipo2Tabla_estilos[i] = [];
                }

                // Actualizar la tabla existente sin recrearla (igual que melanbide84 y tipo1)
                tablaTipo2.lineas = listaTipo2Tabla;
                tablaTipo2.displayTabla();
            }

            function dblClckTablaTipo2(rowID, tableName) {
                pulsarModificarTipo2();
            }

            function pulsarCargarXMLTipo2() {
                var hayFicheroSeleccionado = false;
                if (document.getElementById('fichero_xml2').files) {
                    if (document.getElementById('fichero_xml2').files[0]) {
                        hayFicheroSeleccionado = true;
                    }
                } else if (document.getElementById('fichero_xml2').value != '') {
                    hayFicheroSeleccionado = true;
                }
                if (hayFicheroSeleccionado) {
                    var extension = document.getElementById('fichero_xml2').value.split('.').pop().toLowerCase();
                    if (extension != 'xml') {
                        var resultado = jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                        return false;
                    }
                    var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=procesarXMLTipo2&tipo=0&numero=<%=numExpediente%>';
                        document.forms[0].action = url + '?' + parametros;
                        document.forms[0].enctype = 'multipart/form-data';
                        document.forms[0].encoding = 'multipart/form-data';
                        document.forms[0].method = 'POST';
                        document.forms[0].target = 'uploadFrameCarga2';
                        document.forms[0].submit();
                    }
                    return true;
                } else {
                    jsp_alerta("A", '<%=meLanbide81I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                    return false;
                }
            }
            function actualizarPestanaTipo2() {
                elementoVisible('on', 'barraProgresoLPEEL');
                var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=actualizarPestanaTipo2&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestana2,
                        error: mostrarErrorActualizarPestana2
                    });
                } catch (Err) {
                    elementoVisible('off', 'barraProgresoLPEEL');
                    mostrarErrorPeticion();
                }
            }

            function actualizarTablaTipo2() {
                try {
                    actualizarPestanaTipo2();
                    limpiarFormularioTipo2();
                } catch (err) {
                }
            }

            function limpiarFormularioTipo2() {
                document.getElementById('fichero_xml2').value = "";
            }
            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana2(ajaxResult) {
                elementoVisible('off', 'barraProgresoLPEEL');

                try {
                    var datos = JSON.parse(ajaxResult);
                    var codigoOperacion = datos.tabla.codigoOperacion;

                    // Convertir a string para comparación robusta
                    var codigo = String(codigoOperacion);

                    if (codigo == "0" || codigo == "5") {
                        // Código 0 = datos OK, Código 5 = sin registros (pero operación exitosa)
                        var tipos2 = datos.tabla.lista || [];
                        recargarTablaTipos2(tipos2); // Recarga tabla (vacía o con datos)

                        // NO mostrar mensaje de error cuando no hay registros
                    } else {
                        // Error real de BBDD u operación
                        mostrarErrorPeticion(codigoOperacion);
                    }
                } catch (err) {
                    console.error('Error en procesarRespuestaActualizarPestana2:', err);
                    mostrarErrorPeticion();
                }
            }

            function mostrarErrorEliminarTipo2() {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion("6");
            }
            function mostrarErrorActualizarPestana2() {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion();
            }

        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage81-4" style="height:520px; width: 100%;">   
            <br/>
            <h2 class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario, "label.titulo.pestanaAportacionContrataciones2")%></h2>
            <br/>
            <div style="clear: both;">
                <div id="divListaTipo2"  name="divListaTipo2" align="center" style="margin: 5px;"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoTipo2" name="btnNuevoTipo2" class="botonGeneral"  value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoTipo2();">
                <input type="button" id="btnModificarTipo2" name="btnModificarTipo2" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTipo2();">
                <input type="button" id="btnEliminarTipo2" name="btnEliminarTipo2"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipo2();">
                <input type="button" id="btnpulsarExportarExcell" name="btnpulsarExportarExcell" class="botonLargo" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.exportar")%>" onclick="pulsarExportarExcellAporEmpresas();">
            </div>  
            <br>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnCargarRegistros2" name="btnCargarRegistros2" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXMLTipo2();">
                <input type="file"  name="fichero_xml2" id="fichero_xml2" class="inputTexto" size="60" accept=".xml">
            </div>
            <br><!-- comment -->
            <div class="botonera" style="text-align: center;">
                <input type= "button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();" >
                </input>
                <input type= "button" id="botonCertificadoDemanda" name="botonCertificadoDemanda" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();" >
                </input>                           
            </div>                    
            <iframe id="uploadFrameCarga2" name="uploadFrameCarga2" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
        </div>         
        <script type="text/javascript">
            <% 
                 
            Tipo2VO tipo2VO = null;
            List<Tipo2VO> List = null;
            if(request.getAttribute("divListaTipo2")!=null){
                List = (List<Tipo2VO>)request.getAttribute("divListaTipo2");
            }													
            if (List!= null && List.size() >0){
                for (int indice=0;indice<List.size();indice++){
                    tipo2VO = List.get(indice);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 
                    String entbene = "";
                    if(tipo2VO.getEntbene()!=null){
                       entbene=tipo2VO.getEntbene();
                    }
                       
                    String empcontra = "";
                    if(tipo2VO.getEmpcontra()!=null){
                       empcontra=tipo2VO.getEmpcontra();
                    }
                        
                    String cif = "";
                    if(tipo2VO.getCif()!=null){
                        cif=tipo2VO.getCif();
                    }
                    String ccc = "";
                    if(tipo2VO.getCcc()!=null){
                       ccc=tipo2VO.getCcc();
                    }                                          
                    
                    String nombreCompleto="";
                    String nombre = "";
                    if(tipo2VO.getNombre()!=null){
                       nombre=tipo2VO.getNombre();
                    }
                    String apellido1 = "";
                    if(tipo2VO.getApellido1()!=null){
                       apellido1=tipo2VO.getApellido1();
                    }
                    
                    String apellido2 = "";
                    if(tipo2VO.getApellido2()!=null){
                       apellido2=tipo2VO.getApellido2();
                    }
                        
                    String dni = "";
                    if(tipo2VO.getDni()!=null){
                       dni=tipo2VO.getDni();
                    }
                    
                    String naf = "";
                    if(tipo2VO.getNaf()!=null){
                       naf=tipo2VO.getNaf();
                    }
                    
                    String nombreFicheroCv = "";
                    if (tipo2VO.getNombreFicheroCv() != null) {
                        nombreFicheroCv = tipo2VO.getNombreFicheroCv();
                    }

                    String fechaCv = "";
                    if(tipo2VO.getFechaCv() != null){
                        fechaCv = dateFormat.format(tipo2VO.getFechaCv());
                    }

                    String nombreFicheroDemanda = "";
                    if (tipo2VO.getNombreFicheroDemanda() != null) {
                        nombreFicheroDemanda = tipo2VO.getNombreFicheroDemanda().replaceAll("[\"'<>]", "").split("\\s+")[0];
                    }

                    String fechaDemanda = "";
                    if(tipo2VO.getFechaDemanda() != null){
                        fechaDemanda = dateFormat.format(tipo2VO.getFechaDemanda());
                    }  
                    
                    String fecnacimiento="";
                    if(tipo2VO.getFecnacimiento()!=null){
                        fecnacimiento=dateFormat.format(tipo2VO.getFecnacimiento());
                    }else{
                        fecnacimiento="-";
                    }
                        nombreFicheroDemanda = tipo2VO.getNombreFicheroDemanda();
                    String sexo="";
                    if(tipo2VO.getDescSexo()!=null){
                        String descripcion = tipo2VO.getDescSexo();
                        
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
                        String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                        if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                            if(idiomaUsuario==ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO){
                                descripcion=descripcionDobleIdioma[1];
                            }else{
                                // Cogemos la primera posición que debería ser castellano
                                descripcion=descripcionDobleIdioma[0];
                            }
                        }
                        sexo = descripcion;
                    }else{
                        sexo="-";
                    }
                    String grupocot="";
                    if(tipo2VO.getDescGrupocot()!=null){
                        String descripcion = tipo2VO.getDescGrupocot();
                        
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
                        String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                        if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                            if(idiomaUsuario==ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO){
                                descripcion=descripcionDobleIdioma[1];
                            }else{
                                // Cogemos la primera posición que debería ser castellano
                                descripcion=descripcionDobleIdioma[0];
                            }
                        }
                        grupocot = descripcion;
                    }else{
                        grupocot="-";
                    }
                       String tipocontrato="";
                    if(tipo2VO.getDescTipocontrato()!=null){
                        String descripcion = tipo2VO.getDescTipocontrato();
               
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
                        String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                        if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                            if(idiomaUsuario==ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO){
                                descripcion=descripcionDobleIdioma[1];
                            }else{
                                // Cogemos la primera posición que debería ser castellano
                                descripcion=descripcionDobleIdioma[0];
                            }
                        }
                        tipocontrato = descripcion;
                    }else{
                        tipocontrato="-";
                    }

                    
                    String fecinicio="";
                    if(tipo2VO.getFecinicio()!=null){
                        fecinicio=dateFormat.format(tipo2VO.getFecinicio());
                    }else{
                        fecinicio="-";
                    }

                      String fecfin="";
                    if(tipo2VO.getFecfin()!=null){
                        fecfin=dateFormat.format(tipo2VO.getFecfin());
                    }else{
                        fecfin="-";
                    }                                         

                                            
                    String porcJorn="";
                    if(tipo2VO.getPorcJorn()!=null){
                        porcJorn=String.valueOf(tipo2VO.getPorcJorn().toString().replace(".",","));
                    }
                                       
                    String durcontrato = "";
                    if(tipo2VO.getDurcontrato()!=null){
                    durcontrato = tipo2VO.getDurcontrato();
                       
                    }
                     
                    String edad="";
                    if(tipo2VO.getEdad()!=null && !"".equals(tipo2VO.getEdad())){
                        edad=Integer.toString(tipo2VO.getEdad());
                    }else{
                        edad="-";
                    }
                    
                    String municipio = "";
                    if(tipo2VO.getMunicipio()!=null){
                       municipio=tipo2VO.getMunicipio();
                    }
                    
                    String costesal = "";
                    if(tipo2VO.getCostesal()!=null){
                       costesal=String.valueOf(tipo2VO.getCostesal().toString().replace(".",","));
                    }
                    
                    String costess = "";
                    if(tipo2VO.getCostess()!=null){
                        costess=String.valueOf(tipo2VO.getCostess().toString().replace(".",","));
                    }
                   
                    String costetotal = "";
                    if(tipo2VO.getCostetotal()!=null){
                        costetotal=String.valueOf(tipo2VO.getCostetotal().toString().replace(".",","));
                    }
                             String colectivo="";
                    if(tipo2VO.getDescColectivo()!=null){
                        String descripcion = tipo2VO.getDescColectivo();
                      
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
                        String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                        if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                            if(idiomaUsuario==ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO){
                                descripcion=descripcionDobleIdioma[1];
                            }else{
                                // Cogemos la primera posición que debería ser castellano
                                descripcion=descripcionDobleIdioma[0];
                            }
                        }
                        colectivo = descripcion;
                    }else{
                        colectivo="-";
                    }
               
                 
                    String inscrita="";
                    if(tipo2VO.getDescInscrita()!=null){
                        String descripcion = tipo2VO.getDescInscrita();
                        
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
                        String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                        if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                            if(idiomaUsuario==ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO){
                                descripcion=descripcionDobleIdioma[1];
                            }else{
                                // Cogemos la primera posición que debería ser castellano
                                descripcion=descripcionDobleIdioma[0];
                            }
                        }
                        inscrita = descripcion;
                    }else{
                        inscrita="-";
                    }
                    
                    String certinter = "";
                    if(tipo2VO.getCertinter()!=null){
                       certinter=tipo2VO.getCertinter();
                    }
                    
                    String subconcedida = "";
                    if(tipo2VO.getSubconcedida()!=null){
                       subconcedida=String.valueOf(tipo2VO.getSubconcedida());

                    }                       
                                                                 
                    String pago1="";
                    if(tipo2VO.getPago1()!=null){
                        pago1=String.valueOf(tipo2VO.getPago1());
                    }
                    
                    String subliquidada = "";
                   if(tipo2VO.getSubliquidada()!=null){
                       subliquidada=String.valueOf(tipo2VO.getSubliquidada());
                    }
                    
                    String pago2="";
                    if(tipo2VO.getPago2()!=null){
                        pago2=String.valueOf(tipo2VO.getPago2());
                    }
                  
                    String observaciones = "";
                    if(tipo2VO.getObservaciones()!=null){
                       observaciones=tipo2VO.getObservaciones().replaceAll("\\n", " ");
                    }                                      
                
                                 
            %>
            divListaTipo2[<%=indice%>] = ['<%=tipo2VO.getId()%>', '<%=entbene%>', '<%=empcontra%>', '<%=cif%>', '<%=ccc%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dni%>', '<%=nombreFicheroCv%>', '<%=fechaCv%>', '<%=nombreFicheroDemanda%>', '<%=fechaDemanda%>', '<%=naf%>', '<%=fecnacimiento%>', '<%=sexo%>', '<%=grupocot%>', '<%=tipocontrato%>', '<%=fecinicio%>', '<%=fecfin%>', '<%=porcJorn%>', '<%=durcontrato%>', '<%=edad%>', '<%=municipio%>', '<%=costesal%>', '<%=costess%>', '<%=costetotal%>', '<%=colectivo%>', '<%=inscrita%>', '<%=certinter%>', '<%=subconcedida%>', '<%=pago1%>', '<%=subliquidada%>', '<%=pago2%>', '<%=observaciones%>'];
            listaTipo2Tabla[<%=indice%>] = ['<%=entbene%>', '<%=empcontra%>', '<%=cif%>', '<%=ccc%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dni%>', '<%=nombreFicheroCv%>', '<%=fechaCv%>', '<%=nombreFicheroDemanda%>', '<%=fechaDemanda%>', '<%=naf%>', '<%=fecnacimiento%>', '<%=sexo%>', '<%=grupocot%>', '<%=tipocontrato%>', '<%=fecinicio%>', '<%=fecfin%>', '<%=porcJorn%>', '<%=durcontrato%>', '<%=edad%>', '<%=municipio%>', '<%=costesal%>' + ' \u20ac', '<%=costess%>' + ' \u20ac', '<%=costetotal%>' + ' \u20ac', '<%=colectivo%>', '<%=inscrita%>', '<%=certinter%>', '<%=subconcedida%>' + ' \u20ac', '<%=pago1%>' + ' \u20ac', '<%=subliquidada%>' + ' \u20ac', '<%=pago2%>' + ' \u20ac', '<%=observaciones%>'];
            listaTipo2Tabla_titulos[<%=indice%>] = ['<%=entbene%>', '<%=empcontra%>', '<%=cif%>', '<%=ccc%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dni%>', '<%=nombreFicheroCv%>', '<%=fechaCv%>', '<%=nombreFicheroDemanda%>', '<%=fechaDemanda%>', '<%=naf%>', '<%=fecnacimiento%>', '<%=sexo%>', '<%=grupocot%>', '<%=tipocontrato%>', '<%=fecinicio%>', '<%=fecfin%>', '<%=porcJorn%>', '<%=durcontrato%>', '<%=edad%>', '<%=municipio%>', '<%=costesal%>' + ' \u20ac', '<%=costess%>' + ' \u20ac', '<%=costetotal%>' + ' \u20ac', '<%=colectivo%>', '<%=inscrita%>', '<%=certinter%>', '<%=subconcedida%>' + ' \u20ac', '<%=pago1%>' + ' \u20ac', '<%=subliquidada%>' + ' \u20ac', '<%=pago2%>' + ' \u20ac', '<%=observaciones%>'];
            <%
                   } 
               } 
            %>
            crearTablaTipo2();
        </script>    
        <div id="popupcalendar" class="text"></div> 
    </body>
</html>


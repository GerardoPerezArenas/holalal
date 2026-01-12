<%--
    Document   : justificaSeguimientosPreparador23
    Created on : 03-feb-2025, 16:00:03
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusSeguimientosECA23VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();

            if (session.getAttribute("usuario") != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuario.getAppCod();
                idiomaUsuario = usuario.getIdioma();
                css = usuario.getCss();
            }

        //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
            int numSeguimientosPreparador = (Integer)request.getAttribute("numSeguimientosPreparador");
            String dniPreparador = (String)request.getAttribute("dniPreparador");
            String totalSegPreparadorVal  = (String)request.getAttribute("totalSegPreparadorVal");
            Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");

            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var numSegPreparador = <%=numSeguimientosPreparador%>;
            var dniPreparador = '<%=dniPreparador%>';
            var totSeguimientos = '<%=totalSegPreparadorVal%>';

            var maxSeguimientosJ;

            function crearTablaValSeguimientos() {
                tablaValSeguimientos = new FixedColumnTable(document.getElementById('listaValSegumientos'), 1400, 1450, 'listaValSegumientos');

                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.tabla.dni")%>');// dni
                tablaValSeguimientos.addColumna('240', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nombreAp")%>'); // nombre + apellidos
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.sexo")%>'); // sexo
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fNacimiento")%>'); // fNacimiento
                tablaValSeguimientos.addColumna('100', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoDisc")%>'); // tipoDisc
                tablaValSeguimientos.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.grado")%>'); // grado
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.colectivo")%>'); // colectivo
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoContrato")%>'); // tipoContrato
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>'); // jornada
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fInicio")%>'); // fInicio
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fFin")%>'); // fFin
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoEdadSexo")%>'); // tipoEdadSexo
                tablaValSeguimientos.addColumna('200', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.empresa")%>'); // empresa
                tablaValSeguimientos.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nifEmpresa")%>'); // nif empresa
                tablaValSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.cnae")%>'); // cnae
                tablaValSeguimientos.addColumna('100', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>'); //
                //                             1355
                //for (var cont = 0; cont < listaValSeguimientosTabla.length; cont++) {
                //    tablaValSeguimientos.addFila(listaValSeguimientosTabla[cont], listaValSeguimientosTitulos[cont]);
                //}
                tablaValSeguimientos.displayCabecera = true;
                tablaValSeguimientos.height = 300;
                tablaValSeguimientos.lineas = listaValSeguimientosTabla;
                tablaValSeguimientos.numColumnasFijas = 2;
                tablaValSeguimientos.altoCabecera = 50;
                tablaValSeguimientos.dblClkFunction = 'dblClckTablaSeguimientosdValidacion';
                tablaValSeguimientos.displayTabla();
                tablaValSeguimientos.pack();
            }

            function dblClckTablaSeguimientosdValidacion(rowID, tableName) {
                pulsarModificarSeguimientoValidacionEca();
            }

// Chapuza con pedigri.
function lanzarPopUpModal( url, theHeight, theWidth, scrollable, resizable,despois) {
    if (url) {
        var h=640;
        var w=480;
        var s="no";
        var r="no";
        if (theHeight) h = theHeight;
        if (theWidth) w = theWidth;
        var t=( (screen.height)?((screen.height-h)/2):(0) );
        var l=( (screen.width)?((screen.width-w)/2):(0) );
        if (scrollable!=null) s = scrollable;
        if (resizable!=null) r = resizable;
        abrirXanelaAuxiliar(url, null,
	'top='+t+',left='+l+',width='+w+',height='+h+',status=no,resizable='+r+'scrollbars='+s,despois);
    }
}

            function pulsarNuevoSeguimientoValidacionEca() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoInsertValSeguimiento23&nuevo=1&tipo=0&nuevo=1&numExp=<%=numExpediente%>' +
                    '&nifPreparador=<%=dniPreparador%>', 600, 804, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaValSeguimientos(result[1]);
                        }
                    }
                });
            }

            function pulsarEliminarSeguimientoValidacionEca() {
                if (tablaValSeguimientos.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarValSeguimiento23&nuevo=0&tipo=0&numExp=<%=numExpediente%>&id=' +
                            listaValSeguimientos[tablaValSeguimientos.selectedIndex][0] + '&nifPreparador=<%=dniPreparador%>';

                        console.log("pulsarEliminarSeguimientoValidacionEca: " + parametros);
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaAltaModificacion,
                                error: mostrarErrorEliminarSeguimiento
                            });
                        } catch (Err) {
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarModificarSeguimientoValidacionEca() {
                if (tablaValSeguimientos.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoUpdateValSeguimiento23&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' +
                        listaValSeguimientos[tablaValSeguimientos.selectedIndex][0] + '&nifPreparador=<%=dniPreparador%>' , 600, 804, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaValSeguimientos(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                //console.log("procesarRespuestaAltaModificacion ajaxResult=" + ajaxResult.toString());

                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var seguimientos = datos.tabla.lista;
                    var respuesta = new Array();
                    if (seguimientos.length > 0) {
                        respuesta[0] = "0";
                    }
                    respuesta[1] = seguimientos;
                    recargarTablaValSeguimientos(respuesta[1]);
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorEliminarSeguimiento() {
                mostrarError(6);
            }

            function mostrarError(codigo) {
                //elementoVisible('off', 'barraProgresoECA');
                console.log("mostrarError codigo=" + codigo);
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                }
            }

    function recargarTablaValSeguimientos(seguimientos) {
        var fila, descSexo, descTipoDisc, descColectivo, descTipoContrato, descTipoEdadSexo;
        var descCnae;
        listaValSeguimientos = [];
        listaValSeguimientosTabla = [];
        listaValSeguimientosTitulos = [];
        for (var i = 0 ; i < seguimientos.length ; i++) {
            fila = seguimientos[i];
            if (<%=idiomaUsuario%> == 4) {
                descCnae  = fila.descCnaeE;
            }
            else {
                descCnae = fila.descCnaeC;
            }
            listaValSeguimientos[i] = [fila.id, fila.dni, fila.nombre, fila.apellido1, fila.apellido2, fila.sexo, fila.descSexo,
                                        fila.fNacimientoStr, fila.tipoDisc, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descColectivo,
                                        fila.tipoContrato, fila.descTipoContrato, fila.jornada, fila.fIniciostr, fila.fFinStr,
                                        fila.tipoEdadSexo, fila.descTipoEdadSexo, fila.empresa,
                                        fila.nifEmpresa, fila.cnae, fila.descCnae, fila.nifPreparador, fila.importeSegui];
            listaValSeguimientosTabla[i] = [fila.dni, fila.nombre + " " + fila.apellido1 + " " + fila.apellido2, fila.descSexo,
                                            fila.fNacimientoStr, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descTipoContrato,
                                            fila.jornada, fila.fIniciostr, fila.fFinStr, fila.descTipoEdadSexo, fila.empresa, fila.nifEmpresa,
                                            descCnae, fila.importeSegui + ' \u20ac'];
            listaValSeguimientosTitulos[i] = [fila.dni, fila.nombre + " " + fila.apellido1 + " " + fila.apellido2, fila.descSexo,
                                            fila.fNacimientoStr, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descTipoContrato,
                                            fila.jornada, fila.fIniciostr, fila.fFinStr, fila.descTipoEdadSexo, fila.empresa, fila.nifEmpresa,
                                            descCnae, fila.importeSegui + ' \u20ac'];
        }
        crearTablaValSeguimientos();
    }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage3523-1" style="height:520px; width: 100%;">
            <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
                <span id="subtituloValSegumientos"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.seguimientos.tituloPagina")%></span>
            </div>
            <fieldset id="segumientosPrepVal23" name="segumientosPrepVal23">
                <legend class="legendAzul" id="pestanaValSegPrep" style="text-transform: uppercase;text-align: center;"></legend>
                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 30%; float: left; padding-left: 17%;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.numSeguimientos")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="numSegPrepECA23" name="numSegPrepECA23" size="3" maxlength="3" style="text-align: center;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.tabla.importeJus")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="totSegPreparadorVal" name="totSegPreparadorVal" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                </div>


                <div>
                    <div id="listaValSegumientos" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                </div>

				<div class="lineaFormulario" style="padding-top: 5px; width:40%;margin:0px;margin:auto;" align="center">
					<div style="width: 25%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnGuardarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnInsertar")%>" onClick="pulsarNuevoSeguimientoValidacionEca();">
					</div>
					<div style="width: 25%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnModificarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnModificar")%>" onClick="pulsarModificarSeguimientoValidacionEca();">
					</div>
					<div style="width: 25%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnEliminarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnEliminar")%>" onClick="pulsarEliminarSeguimientoValidacionEca();">
					</div>
					<div style="width: 25%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();">
					</div>
				</div>
            </fieldset>

        </div>
        <script type="text/javascript">
            document.getElementById('pestanaValSegPrep').innerHTML = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.preparadores.titulo")%>' + ' - ' + dniPreparador;
            document.getElementById('numSegPrepECA23').value = numSegPreparador.toString();
            document.getElementById('totSegPreparadorVal').value = formatearNumero(totSeguimientos) + ' \u20ac';

            var tablaValSeguimientos;
            var listaValSeguimientos = new Array();
            var listaValSeguimientosTabla = new Array();
            var listaValSeguimientosTitulos = new Array();
            <%
                JusSeguimientosECA23VO segui = null;
                List<JusSeguimientosECA23VO> ListaSegui = null;
                if(request.getAttribute("listaSegPreparador")!=null) {
                    ListaSegui = (List<JusSeguimientosECA23VO>)request.getAttribute("listaSegPreparador");
                }
                if (ListaSegui!= null && ListaSegui.size() > 0) {
                    for (int indice=0;indice<ListaSegui.size();indice++) {
                        segui = ListaSegui.get(indice);

                        String ape2 = "";
                        if (segui.getApellido2() != null) {
                            ape2 =segui.getApellido2();
                        }

                        String fNacimiento = "";
                        if(segui.getfNacimiento()!=null){
                            fNacimiento=dateFormat.format(segui.getfNacimiento());
                        }

                        String grado = "-";
                        if (segui.getGrado() != null) {
                            grado = formateador.format(segui.getGrado());
                        }

                        String jornada = "-";
                        if (segui.getJornada() != null) {
                            jornada = formateador.format(segui.getJornada());
                        }

                        String fInicio = "-";
                        if(segui.getfInicio()!=null){
                            fInicio=dateFormat.format(segui.getfInicio());
                        }

                        String fFin = "-";
                        if(segui.getfFin()!=null){
                            fFin=dateFormat.format(segui.getfFin());
                        }

                        String descCnae = "-";
                        if(idiomaUsuario==4) {
                            descCnae = segui.getDescCnaeE();
                        } else {
                            descCnae = segui.getDescCnaeC();
                        }

                        String importe = "";
                        if (segui.getImporteSegui() != null) {
                            importe = formateador.format(segui.getImporteSegui());
                        }
            %>
            listaValSeguimientos[<%=indice%>] = ['<%=segui.getId()%>', '<%=segui.getNifPreparador()%>', '<%=segui.getDni()%>', '<%=segui.getNombre()%>', '<%=segui.getApellido1()%>', '<%=ape2%>', '<%=segui.getSexo()%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getTipoDisc()%>', '<%=segui.getDescTipoDisc()%>', '<%=grado%>', '<%=segui.getColectivo()%>', '<%=segui.getDescColectivo()%>', '<%=segui.getTipoContrato()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getTipoEdadSexo()%>', '<%=segui.getDescTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=segui.getCnae()%>', '<%=descCnae%>', '<%=importe%>'];
            listaValSeguimientosTabla[<%=indice%>] = ['<%=segui.getDni()%>', '<%=segui.getNombre()%>' + ' ' + '<%=segui.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getDescTipoDisc()%>', '<%=grado%>', '<%=segui.getColectivo()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getDescTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=descCnae%>', '<%=importe%>' + ' \u20ac']; //18
            listaValSeguimientosTitulos[<%=indice%>] = ['<%=segui.getDni()%>', '<%=segui.getNombre()%>' + ' ' + '<%=segui.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getDescTipoDisc()%>', '<%=grado%>', '<%=segui.getColectivo()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getDescTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=descCnae%>', '<%=importe%>' + ' \u20ac']; //18
            <%
                    }
                }

                if (importesConfiguracion!= null) {
            %>
            maxSeguimientosJ = <%=importesConfiguracion.getMaximoSeguimientos()%>;
            <%
                }
            %>
            crearTablaValSeguimientos();
        </script>
    </body>
</html>
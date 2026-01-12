
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO"%>
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
    String numExpediente = request.getParameter("numero");
    String totalInserciones = (String)request.getAttribute("totalInserciones");

    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String barraSeparadoraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide35.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide35.FICHERO_PROPIEDADES);

  //  totalInserciones = formateador.format(totalInserciones);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
    var mensajeValidacion = '';
    var totalInserciones = <%=totalInserciones%>;

    function pulsarNuevoInsercionValidacionEca() {
        console.log("pulsarNuevoInsercionValidacionEca " + url);
        lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoInsertValInsercion23&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 650, 780, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaValInserciones(result[1]);
                }
            }
        });
    }

    function pulsarModificarInsercionValidacionEca() {
            console.log("pulsarModificarInsercionValidacionEca " + url);
        if (tablaValInserciones.selectedIndex != -1) {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoUpdateValInsercion23&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaValInserciones[tablaValInserciones.selectedIndex][0], 650, 780,'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaValInserciones(result[1]);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

        function dblClckTablaInsercionesVal(rowID, tableName) {
            pulsarModificarInsercionValidacionEca();
        }


    function pulsarEliminarInsercionValidacionEca() {
            console.log("pulsarEliminarInsercionValidacionEca tablaValInserciones.selectedIndex = " +
                    tablaValInserciones.selectedIndex);
            if (tablaValInserciones.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    console.log("pulsarEliminarInsercionValidacionEca: 1");
                    var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarValInsercion23&tipo=0&numExp=<%=numExpediente%>&id=' +
                        listaValInserciones[tablaValInserciones.selectedIndex][0];
                    console.log("pulsarEliminarInsercionValidacionEca: 2");
                    console.log("pulsarEliminarInsercionValidacionEca: " + parametros);
                    console.log("pulsarEliminarInsercionValidacionEca: 3");
                    try {
                                            console.log("pulsarEliminarInsercionValidacionEca: 4 " + url);
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaInsercionAltaModificacion,
                            error: mostrarErrorEliminarInsercion
                        });
                    } catch (Err) {
                                        console.log("pulsarEliminarInsercionValidacionEca: Error");
                        mostrarErrorPeticion();
                    }
                }
            } else {
                                      console.log("pulsarEliminarInsercionValidacionEca: Else");
                jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaInsercionAltaModificacion(ajaxResult) {
                console.log("procesarRespuestaInsercionAltaModificacion ajaxResult=" + ajaxResult.toString());

                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var inserciones = datos.tabla.lista;
                    var respuesta = new Array();
                    if (inserciones.length > 0) {
                        respuesta[0] = "0";
                    }
                    respuesta[1] = inserciones;
                    recargarTablaValInserciones(respuesta[1]);
                } else {
                    mostrarErrorIns(codigoOperacion);
                }
            }

            function mostrarErrorEliminarInsercion() {
                mostrarErrorIns(6);
            }

            function mostrarErrorIns(codigo) {
                //elementoVisible('off', 'barraProgresoECA');
                console.log("mostrarErrorIns codigo=" + codigo);
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

    function crearTablaValInserciones() {
        console.log("crearTablaValInserciones: Inicio");
        //tablaValInserciones = new FixedColumnTable(document.getElementById('listaValInserciones'), 1600, 1650, 'listaValInserciones');
        tablaValInserciones = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaValInserciones'));
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dni")%>'); // dni
        tablaValInserciones.addColumna('240', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nombreAp")%>'); // nombre + apellidos
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.sexo")%>'); // sexo
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fNacimiento")%>'); // fNacimiento
        tablaValInserciones.addColumna('100', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoDisc")%>'); // tipoDisc
        tablaValInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.grado")%>'); // grado
        tablaValInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.colectivo")%>'); // colectivo
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoContrato")%>'); // tipoContrato
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>'); // jornada
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fInicio")%>'); // fInicio
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fFin")%>'); // fFin
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoEdadSexo")%>'); // tipoEdadSexo
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dias")%>'); // dias
        tablaValInserciones.addColumna('40', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.edad")%>'); // edad
        tablaValInserciones.addColumna('200', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.empresa")%>'); // empresa
        tablaValInserciones.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nifEmpresa")%>'); // nif empresa
        tablaValInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.cnae")%>'); // cnae
        tablaValInserciones.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dniPreparador")%>'); //
        tablaValInserciones.addColumna('100', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>'); //

        tablaValInserciones.displayCabecera = true;
        tablaValInserciones.height = 400;
        tablaValInserciones.numColumnasFijas = 2;
        tablaValInserciones.altoCabecera = 50;

        tablaValInserciones.lineas = listaValInsercionesTabla;
        tablaValInserciones.displayTablaConTooltips(listaValInsercionesTitulos);
        tablaValInserciones.dblClkFunction = 'dblClckTablaInsercionesVal';
        tablaValInserciones.displayTabla();
    }

    function recargarTablaValInserciones(inserciones) {
        var fila, descSexo, descTipoDisc, descColectivo, descTipoContrato, descTipoEdadSexo;
        var descCnae;
        listaValInserciones = [];
        listaValInsercionesTabla = [];
        listaValInsercionesTitulos = [];
        for (var i = 0 ; i < inserciones.length ; i++) {
            fila = inserciones[i];
            if (<%=idiomaUsuario%> == 4) {
                descCnae  = fila.descCnaeE;
            }
            else {
                descCnae = fila.descCnaeC;
            }
            listaValInserciones[i] = [fila.id, fila.dni, fila.nombre, fila.apellido1, fila.apellido2, fila.sexo, fila.descSexo,
                                        fila.fNacimientoStr, fila.tipoDisc, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descColectivo,
                                        fila.tipoContrato, fila.descTipoContrato, fila.jornada, fila.fIniciostr, fila.fFinStr,
                                        fila.tipoEdadSexo, fila.descTipoEdadSexo, fila.dias, fila.edad, fila.empresa,
                                        fila.nifEmpresa, fila.cnae, fila.descCnae, fila.nifPreparador, fila.importeInser];
            listaValInsercionesTabla[i] = [fila.dni, fila.nombre + " " + fila.apellido1 + " " + fila.apellido2, fila.descSexo,
                                            fila.fNacimientoStr, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descTipoContrato,
                                            fila.jornada, fila.fIniciostr, fila.fFinStr, fila.descTipoEdadSexo, fila.dias, fila.edad, fila.empresa, fila.nifEmpresa,
                                            descCnae, fila.nifPreparador, fila.importeInser + ' \u20ac'];
            listaValInsercionesTitulos[i] = [fila.dni, fila.nombre + " " + fila.apellido1 + " " + fila.apellido2, fila.descSexo,
                                            fila.fNacimientoStr, fila.descTipoDisc, fila.grado, fila.colectivo, fila.descTipoContrato,
                                            fila.jornada, fila.fIniciostr, fila.fFinStr, fila.descTipoEdadSexo, fila.dias, fila.edad, fila.empresa, fila.nifEmpresa,
                                            descCnae, fila.nifPreparador, fila.importeInser + ' \u20ac'];
        }

        crearTablaValInserciones();
    }
</script>
<body>
    <div class="tab-page" id="tabPage3522" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloValInsercion"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.inserciones.tituloPagina")%></span>
        </div>
        <fieldset id="insercionesVal23" name="insercionesVal23">
            <div>
                <div id="listaValInserciones" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
				<div class="lineaFormulario" style="padding-top: 5px; width:20%;margin:0px;margin:auto;" align="center">
					<div style="width: 33%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnGuardarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnInsertar")%>" onClick="pulsarNuevoInsercionValidacionEca();">
					</div>
					<div style="width: 33%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnModificarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnModificar")%>" onClick="pulsarModificarInsercionValidacionEca();">
					</div>
					<div style="width: 33%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnEliminarValInsercion" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnEliminar")%>" onClick="pulsarEliminarInsercionValidacionEca();">
					</div>
				</div>
            </div>
        </fieldset>
    </div>
    <script  type="text/javascript">
        var tablaValInserciones;
        var listaValInserciones = new Array();
        var listaValInsercionesTabla = new Array();
        var listaValInsercionesTitulos = new Array();
        <%
            JusInsercionesECA23VO insercion = null;
            List<JusInsercionesECA23VO> ListaInser = null;
            if(request.getAttribute("listaInsercionesValidacion")!=null) {
                ListaInser = (List<JusInsercionesECA23VO>)request.getAttribute("listaInsercionesValidacion");
            }

            if (ListaInser!= null && ListaInser.size() > 0) {
                for (int indice=0;indice<ListaInser.size();indice++) {
                    insercion = ListaInser.get(indice);

                    String ape2 = "";
                    if (insercion.getApellido2() != null) {
                        ape2 =insercion.getApellido2();
                    }

                    String fNacimiento = "";
                    if(insercion.getfNacimiento()!=null){
                        fNacimiento=dateFormat.format(insercion.getfNacimiento());
                    }

                    String grado = "-";
                    if (insercion.getGrado() != null) {
                        grado = formateador.format(insercion.getGrado());
                    }

                    String jornada = "-";
                    if (insercion.getJornada() != null) {
                        jornada = formateador.format(insercion.getJornada());
                    }


                    String fInicio = "-";
                    if(insercion.getfInicio()!=null){
                        fInicio=dateFormat.format(insercion.getfInicio());
                    }

                    String fFin = "-";
                    if(insercion.getfFin()!=null){
                        fFin=dateFormat.format(insercion.getfFin());
                    }

                    String descCnae = "-";
                    if(idiomaUsuario==4) {
                        descCnae = insercion.getDescCnaeE();
                    } else {
                        descCnae = insercion.getDescCnaeC();
                    }

                    String dias = "";
                    if (insercion.getDias() != null && !insercion.getDias().toString().equals("") && !insercion.getDias().toString().equals("0")) {
                        dias = Integer.toString(insercion.getDias());
                    }

                    String edad = "";
                    if (insercion.getEdad() != null) {
                        edad = Integer.toString(insercion.getEdad());
                    }

                    String importe = "";
                    if (insercion.getImporteInser() != null) {
                        importe = formateador.format(insercion.getImporteInser());
                    }
        %>
        listaValInserciones[<%=indice%>] = ['<%=insercion.getId()%>', '<%=insercion.getDni()%>', '<%=insercion.getNombre()%>', '<%=insercion.getApellido1()%>', '<%=ape2%>', '<%=insercion.getSexo()%>', '<%=insercion.getDescSexo()%>',
            '<%=insercion.getfNacimientoStr()%>', '<%=insercion.getTipoDisc()%>', '<%=insercion.getDescTipoDisc()%>', '<%=grado%>', '<%=insercion.getColectivo()%>', '<%=insercion.getDescColectivo()%>', '<%=insercion.getTipoContrato()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=insercion.getfIniciostr()%>', '<%=insercion.getfFinStr()%>', '<%=insercion.getTipoEdadSexo()%>', '<%=insercion.getDescTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=insercion.getCnae()%>', '<%=descCnae%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>'];
        listaValInsercionesTabla[<%=indice%>] = ['<%=insercion.getDni()%>', '<%=insercion.getNombre()%>' + ' ' + '<%=insercion.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=insercion.getDescSexo()%>',
            '<%=insercion.getfNacimientoStr()%>', '<%=insercion.getDescTipoDisc()%>', '<%=grado%>', '<%=insercion.getColectivo()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=insercion.getfIniciostr()%>', '<%=insercion.getfFinStr()%>', '<%=insercion.getDescTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=descCnae%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>' + ' \u20ac']; // 19 columnas
        listaValInsercionesTitulos[<%=indice%>] = ['<%=insercion.getDni()%>', '<%=insercion.getNombre()%>' + ' ' + '<%=insercion.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=insercion.getDescSexo()%>',
            '<%=insercion.getfNacimientoStr()%>', '<%=insercion.getDescTipoDisc()%>', '<%=grado%>', '<%=insercion.getColectivo()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=insercion.getfIniciostr()%>', '<%=insercion.getfFinStr()%>', '<%=insercion.getDescTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=descCnae%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>' + ' \u20ac']; // 19 columnas
        <%
                }
            }
        %>
        crearTablaValInserciones();
    </script>
</body>
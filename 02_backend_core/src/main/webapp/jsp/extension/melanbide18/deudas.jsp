<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.i18n.MeLanbide18I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.vo.DeudaZorkuVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConstantesMeLanbide18"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
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

            MeLanbide18I18n meLanbide18I18n = MeLanbide18I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
            String fraccTodas = (String)request.getAttribute("fraccionarTodas");

            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide18/melanbide18.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide18/utils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>        

        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var fraccionarTodas = '<%=fraccTodas%>';

            function iniciarTablaDeudas() {
                
                tablaDeudas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaDeudas'));

                tablaDeudas.addColumna('300', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.numLiquidacion")%>");
                tablaDeudas.addColumna('300', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.numDocumento")%>");
                tablaDeudas.addColumna('300', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.expediente")%>");                
                tablaDeudas.addColumna('300', 'right', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.importeDeuda")%>");
                tablaDeudas.addColumna('300', 'right', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.importePendiente")%>");
                tablaDeudas.addColumna('300', 'right', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.importeCobrado")%>");
                tablaDeudas.addColumna('200', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.tipoPago")%>");
                tablaDeudas.addColumna('200', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.estadoDeuda")%>");
                tablaDeudas.addColumna('150', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.periodo")%>");
                tablaDeudas.addColumna('300', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.fechaLimitePago")%>");               
                tablaDeudas.addColumna('200', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudas.guardar")%>");

                tablaDeudas.displayCabecera = true;
                tablaDeudas.height = 360;
            
            }
            
            function pulsarCerrar() {
                cerrarVentana();
            }
            
            function pulsarGuardar() {
                let indiceListaDeudas, indiceIdsLiquidaciones;
                let parametros = 'tarea=preparar&modulo=MELANBIDE18&operacion=crearNuevaDeudaFracc&tipo=0&numExp=<%=numExpediente%>&idsLiquidaciones=';
                const idsLiquidaciones = [];
                indiceIdsLiquidaciones = 0;
                
                let algunaSeleccionada = false;
                for (indiceListaDeudas = 0; indiceListaDeudas < listaDeudas.length; indiceListaDeudas++) {
                    if (document.getElementById("chkDeuda" + indiceListaDeudas).checked == true) {
                        algunaSeleccionada = true;
                        break;
                    }
                }
                if (algunaSeleccionada == false) {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.noSeHaSecciolandoDeuda")%>');
                    return;
                }

                for (indiceListaDeudas = 0; indiceListaDeudas < listaDeudas.length; indiceListaDeudas++) {
                    if (document.getElementById("chkDeuda" + indiceListaDeudas).checked == true) {
                        idsLiquidaciones[indiceIdsLiquidaciones] = listaDeudas[indiceListaDeudas][0];
                        indiceIdsLiquidaciones++;
                    }
                        
                }
                console.log("idsLiquidaciones = " + idsLiquidaciones);
                for (indiceIdsLiquidaciones = 0; indiceIdsLiquidaciones < idsLiquidaciones.length; indiceIdsLiquidaciones++ ) {
                    if (indiceIdsLiquidaciones < idsLiquidaciones.length - 1) {
                        parametros += idsLiquidaciones[indiceIdsLiquidaciones] + ',';     
                    } else {
                        parametros += idsLiquidaciones[indiceIdsLiquidaciones]; 
                    }   
                }

                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        beforeSend: function () {
                            pleaseWaitSinFrame('on');
                        },
                        success: procesarRespuestaAltaModificacion,
                        error: mostrarErrorAltaModificacion
                    });
                } catch (Err) {
                    pleaseWaitSinFrame('off');
                    mostrarError();
                }                
            }
            
            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                pleaseWaitSinFrame('off');
                var datos = JSON.parse(ajaxResult);
                console.log("procesarRespuestaAltaModificacion: datos = " + datos);                
                console.log("procesarRespuestaAltaModificacion: datos.tabla.codigoOperacion = " + datos.tabla.codigoOperacion);                
                console.log("procesarRespuestaAltaModificacion: datos.tabla.listaDeudasFracc = " + datos.tabla.listaDeudasFracc);                

                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.listaDeudasFracc;
                    if (result.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = result;
                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
//                        cerrarVentana();
                    } else {
                        mostrarError("5");
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaModificacion() {
                mostrarError("8");
            }

            function mostrarError(codigo) {
                pleaseWaitSinFrame('off');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "9") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorDeudaExistente")%>');                    
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }            
        </script>
    </head>
    <body class="bandaBody" onload="pleaseWaitSinFrame('off');">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=meLanbide18I18n.getMensaje(idiomaUsuario, "msg.procesando")%>'/>
        </jsp:include>
        <div class="contenidoPantalla">            
            <form>    
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <h1 class="legendAzul" id="pestanaDeudasZorku"><%=meLanbide18I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaZorku")%></h1>
                <div id="divGeneral" style="margin:30px">     
                    <div id="listaDeudas" align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">               
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide18I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="pulsarCerrar();">
                        <input type="button" id="btnGuardar" name="btnGuardar" class="botonMasLargo" value="<%=meLanbide18I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="pulsarGuardar();">                    
                </div>
                    <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
                        <label id="mensajeErrorFracc" name="mensajeErrorFracc" class="legendRojo"></label>
            </div>  
                </div>
            </form> 


        <script  type="text/javascript">
            //Tabla deudas
            var tablaDeudas;
            var listaDeudas = new Array();
            var listaDeudasTabla = new Array();
            var listaDeudasTabla_titulos = new Array();

            <% 		
                   DeudaZorkuVO objectVO = null;
                   List<DeudaZorkuVO> listaDeudas = null;
               if(request.getAttribute("listaDeudas") != null){
                       listaDeudas = (List<DeudaZorkuVO>)request.getAttribute("listaDeudas");
               }													
               if (listaDeudas!= null && listaDeudas.size() >0){
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                   for (int indice=0;indice<listaDeudas.size();indice++)
                   {
                        objectVO = listaDeudas.get(indice);
                                             
                        String numDocumento = "-";
                        if(objectVO.getNumDocumento() != null){
                            numDocumento = objectVO.getNumDocumento().replaceAll("\\n", " ");
                        }
                        String expediente = "-";
                        if(objectVO.getExpediente() != null){
                            expediente = objectVO.getExpediente().replaceAll("\\n", " ");                            
                        }
                        String importeDeuda = "-";
                        if(objectVO.getImporteDeuda() != null){
                                importeDeuda = formateador.format(objectVO.getImporteDeuda());
                        }
                        String importePendiente = "-";
                        if(objectVO.getImportePendiente() != null){
                                importePendiente = formateador.format(objectVO.getImportePendiente());
                        }
                        String importeCobrado = "-";
                        if(objectVO.getImporteCobrado() != null){
                                importeCobrado = formateador.format(objectVO.getImporteCobrado());
                        }
                        String codTipoPago = "-";
                        if(objectVO.getCodTipoPago() != null){
                            codTipoPago = String.valueOf(objectVO.getCodTipoPago().toString());
                        }                        
                        String tipoPago = "-";
                        if(objectVO.getTipoPago() != null){
                                tipoPago = objectVO.getTipoPago();
                        }                             
                        String codEstadoDeuda = "-";
                        if(objectVO.getCodEstadoDeuda() != null){
                            codEstadoDeuda = String.valueOf(objectVO.getCodEstadoDeuda().toString());
                        }                        
                        String estadoDeuda = "-";
                        if(objectVO.getEstadoDeuda() != null){
                                estadoDeuda = objectVO.getEstadoDeuda();
                        }                        
                        String periodo = "-";
                        if(objectVO.getPeriodo() != null){
                                periodo = objectVO.getPeriodo();
                        }                             
                        String fechaLimitePago = "-";
                        if(objectVO.getFechaLimitePago()!=null){
                            fechaLimitePago = dateFormat.format(objectVO.getFechaLimitePago());
                        }      
            %>
 
            listaDeudas[<%=indice%>] = ['<%=objectVO.getNumLiquidacion()%>', '<%=numDocumento%>', '<%=expediente%>', '<%=importeDeuda%>', '<%=importePendiente%>', '<%=importeCobrado%>', '<%=codTipoPago%>', '<%=tipoPago%>', '<%=codEstadoDeuda%>', '<%=estadoDeuda%>', '<%=periodo%>', '<%=fechaLimitePago%>', '<input type="checkbox" id="chkDeuda<%=indice%>">'];
                listaDeudasTabla[<%=indice%>] = ['<%=objectVO.getNumLiquidacion()%>', '<%=numDocumento%>', '<%=expediente%>', '<%=importeDeuda%>' + ' \u20ac', '<%=importePendiente%>' + ' \u20ac', '<%=importeCobrado%>' + ' \u20ac', '<%=tipoPago%>', '<%=estadoDeuda%>', '<%=periodo%>', '<%=fechaLimitePago%>', '<input type="checkbox" id="chkDeuda<%=indice%>">'];
                listaDeudasTabla_titulos[<%=indice%>] = ['<%=objectVO.getNumLiquidacion()%>', '<%=numDocumento%>', '<%=expediente%>', '<%=importeDeuda%>' + ' \u20ac', '<%=importePendiente%>' + ' \u20ac', '<%=importeCobrado%>' + ' \u20ac', '<%=tipoPago%>', '<%=estadoDeuda%>', '<%=periodo%>', '<%=fechaLimitePago%>', '<input type="checkbox" id="chkDeuda<%=indice%>">'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaDeudas();
            tablaDeudas.lineas= listaDeudasTabla;
            tablaDeudas.displayTabla();

                if (fraccionarTodas == 'S') {
                    deshabilitarBotonLargo(document.forms[0].btnGuardar);
                    document.getElementById('mensajeErrorFracc').innerHTML = "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"msg.fraccionarTodas")%>";

                }
        </script>
        <div id="popupcalendar" class="text"></div>                
        </div>  
    </body>
</html>
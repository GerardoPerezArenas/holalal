<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.i18n.MeLanbide18I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.vo.FilaDeudaFraccVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConstantesMeLanbide18"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide18/melanbide18.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide18/utils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';
            var parametrosLlamada = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE18'
                , operacion: null
                , tipo: 0
                , numExp: '<%=numExpediente%>'
            };
            
            function pulsarEliminarDeudaFracc() {
                if (tablaDeudas.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide18I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE18&operacion=eliminarDeudaFracc&tipo=0&id=' + listaDeudasFracc[tablaDeudas.selectedIndex][0] + '&numExp=<%=numExpediente%>';
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                beforeSend: function () {
                                    pleaseWait('on');
                                },
                                success: procesarRespuestaEliminarDeuda,
                                error: function () {
                                    mostrarErrorPeticion("6");
                                }
                            });
                        } catch (Err) {
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide18I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }            

            function iniciarTablaDeudasFracc() {
                
                tablaDeudas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaDeudasFracc'));

                tablaDeudas.addColumna('0', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudasFracc.id")%>");
                tablaDeudas.addColumna('0', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudasFracc.numExpediente")%>");
                tablaDeudas.addColumna('50', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudasFracc.numExpedienteDeuda")%>");
                tablaDeudas.addColumna('50', 'center', "<%=meLanbide18I18n.getMensaje(idiomaUsuario,"tablaDeudasFracc.importeDeuda")%>");
                
                tablaDeudas.displayCabecera = true;
                tablaDeudas.height = 360;
            }

            function recargarTablaDeudasFracc(result) {
                var fila;
                listaDeudasFracc = new Array();
                listaDeudasFraccTabla = new Array();
                listaDeudasFraccTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaDeudasFracc[i] = [fila.id, fila.numExp, fila.numExpDeuda, formatNumero(fila.deudaImporte)];
                    listaDeudasFraccTabla[i] = [fila.id, fila.numExp, fila.numExpDeuda, formatNumero(fila.deudaImporte) + ' \u20ac'];
                    listaDeudasFraccTabla_titulos[i] = [fila.id, fila.numExp, fila.numExpDeuda, formatNumero(fila.deudaImporte) + ' \u20ac'];
                }
                iniciarTablaDeudasFracc();
                tablaDeudas.lineas= listaDeudasFraccTabla;
//                tablaDeudas.displayTabla();
                tablaDeudas.displayTablaConTooltips(listaDeudasFraccTabla_titulos);
            }

            function pulsarConsultarDeudas() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE18&operacion=consultarDeudasZorku&tipo=0&numExp=<%=numExpediente%>', 500, 1300, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaDeudasFracc(result[1]);
                        }
                    }
                });
            }

            function pulsarFraccionarDeudas() {
                //    deshabilitarBotonLargo(document.forms[0].btnFraccionarDeudas);
                document.getElementById('mensajeError').innerHTML = "";
                var dataParameter = $.extend({}, parametrosLlamada);
                dataParameter.operacion = 'fraccionarDeudas';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: dataParameter,
                        beforeSend: function () {
                            pleaseWait('on');
                        },
                        success: procesarRespuestaFraccionar,
                        error: function () {
                            mostrarErrorPeticion("-1");
                        }
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            function procesarRespuestaEliminarDeuda(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    pleaseWait('off');
                    var result = datos.tabla.lista;
                    recargarTablaDeudasFracc(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function procesarRespuestaFraccionar(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"msg.fraccionarOK")%>');
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorPeticion(codigo) {
                pleaseWait('off');
                var msgtitle = '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
                switch (codigo) {
                    case '-1':
                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                        break;
                    case '1':
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                        break;
                    case '2':
                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                        break;
                    case '3':
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                        break;
                    case '4':
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                        break;
                    case '5':
                    jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                        break;
                    case '6':
                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                        break;
//                    case '10':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.10")%>', msgtitle);
//                        break;
//                    case '11':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.11")%>', msgtitle);
//                        break;
//                    case '12':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.12")%>', msgtitle);
//                        break;
//                    case '13':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.13")%>', msgtitle);
//                        break;
//                    case '14':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.14")%>', msgtitle);
//                        break;
//                    case '15':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.15")%>', msgtitle);
//                        break;
//                    case '16':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.16")%>', msgtitle);
//                        break;
//                    case '17':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.17")%>', msgtitle);
//                        break;
//                    case '18':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.18")%>', msgtitle);
//                        break;
//                    case '19':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.19")%>', msgtitle);
//                        break;
//                    case '20':
//                        jsp_alerta("A", '<%=meLanbide18I18n.getMensaje(idiomaUsuario,"error.fraccionar.20")%>', msgtitle);
//                        break;
                    default:
                        jsp_alerta("A", codigo, msgtitle);
                        break;
                }
                //       habilitarBotonLargo(document.forms[0].btnFraccionarDeudas);
                document.getElementById('mensajeError').innerHTML = mensajeValidacion;
            }

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                pleaseWait('off');
            }" >
        <div class="tab-page" id="tabPage182" style="width: 100%;">
            <h2 class="tab" id="pestana182"><%=meLanbide18I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage182"));</script>
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide18I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <div id="divGeneral" style="width: 60%; margin: 0 auto;">  
                    <div id="listaDeudasFracc"></div>
                </div>  
                <div class="botonera" style="text-align: center;margin-top:20px">                                 
                    <input type="button" id="btnEliminarDeudaFracc" name="btnEliminarDeudaFracc" class="botonGeneral" value="<%=meLanbide18I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarDeudaFracc();">                                        
                    <input type="button" id="btnConsultarDeudas" name="btnConsultarDeudas" class="botonLargo" value="<%=meLanbide18I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarConsultarDeudas();">  
                </div>
                <div class="botonera" style="visibility: hidden;text-align: center;margin-top:20px">           
                    <input type="button" id="btnFraccionarDeudas" name="btnFraccionarDeudas" class="botonMasLargo" value="<%=meLanbide18I18n.getMensaje(idiomaUsuario, "btn.fraccionar")%>" onclick="pulsarFraccionarDeudas();">  
                </div>                
                </div>                
            <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
                <label id="mensajeError" name="mensajeError" class="legendRojo"></label>
            </div>  
        </div>

        <script  type="text/javascript">
            //Tabla Deudas
            var tablaDeudas;
            var listaDeudasFracc = new Array();
            var listaDeudasFraccTabla = new Array();
            var listaDeudasFraccTabla_titulos = new Array();

            <% 		
               FilaDeudaFraccVO objectVO = null;
               List<FilaDeudaFraccVO> listaDeudas = null;
               if(request.getAttribute("listaDeudasFracc") != null){
                   listaDeudas = (List<FilaDeudaFraccVO>)request.getAttribute("listaDeudasFracc");
               }													
               if (listaDeudas != null && listaDeudas.size() >0){
                   for (int indice=0;indice < listaDeudas.size();indice++)
                   {
                        objectVO = listaDeudas.get(indice);
                        
                        String id = "-";
                        if(objectVO.getId() != null){
                            id = objectVO.getId().toString();
                        }
                        String numExpedienteTabla = "-";
                        if(objectVO.getNumExp() != null){
                            numExpedienteTabla = objectVO.getNumExp().replaceAll("\\n", " ");                            
                        }
                        String numExpedienteDeuda = "-";
                        if(objectVO.getNumExpDeuda() != null){
                            numExpedienteDeuda = objectVO.getNumExpDeuda().replaceAll("\\n", " "); 
                        }
                        String deudaImporte = "-";
                        if(objectVO.getDeudaImporte() != null){
                            deudaImporte = String.valueOf((objectVO.getDeudaImporte().toString()).replace(".",","));
                        }
      
            %>
            listaDeudasFracc[<%=indice%>] = ['<%=id%>', '<%=numExpedienteTabla%>', '<%=numExpedienteDeuda%>', '<%=deudaImporte%>'];
            listaDeudasFraccTabla[<%=indice%>] = ['<%=id%>', '<%=numExpedienteTabla%>', '<%=numExpedienteDeuda%>', '<%=formateador.format(objectVO.getDeudaImporte())%>' + ' \u20ac'];
            listaDeudasFraccTabla_titulos[<%=indice%>] = ['<%=id%>', '<%=numExpedienteTabla%>', '<%=numExpedienteDeuda%>', '<%=formateador.format(objectVO.getDeudaImporte())%>' + ' \u20ac'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaDeudasFracc();
            tablaDeudas.lineas= listaDeudasFraccTabla;
//            tablaDeudas.displayTabla();
            tablaDeudas.displayTablaConTooltips(listaDeudasFraccTabla_titulos);
        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>
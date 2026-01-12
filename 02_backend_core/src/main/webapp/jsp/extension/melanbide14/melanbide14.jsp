<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.i18n.MeLanbide14I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
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

            MeLanbide14I18n meLanbide14I18n = MeLanbide14I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
             DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide14/melanbide14.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide14/ExtensionUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevo() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE14&operacion=cargarNuevaOperacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTabla(result[1]);
                        }
                    }
                });
            }

            function pulsarModificar() {
                if (tabla.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE14&operacion=cargarModificarOperacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + lista[tabla.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTabla(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminar() {
                if (tabla.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        pleaseWait('on');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE14&operacion=eliminarOperacion&tipo=0&numExp=<%=numExpediente%>&id=' + lista[tabla.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminar,
                                error: mostrarErrorEliminar
                            });
                        } catch (Err) {
                            pleaseWait('off');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTabla() {
                tabla = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('lista'));

                tabla.addColumna('0', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
                tabla.addColumna('0', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.num_exp")%>");
                tabla.addColumna('50', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.numoper")%>");
                tabla.addColumna('80', 'left', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.nombreoper")%>");
                tabla.addColumna('80', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.prioobj")%>");
                tabla.addColumna('120', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.linact1")%>");
                tabla.addColumna('120', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.linact2")%>");
                tabla.addColumna('120', 'center', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.linact3")%>");
                tabla.addColumna('50', 'right', "<%=meLanbide14I18n.getMensaje(idiomaUsuario,"tabla.impoper")%>");

                tabla.displayCabecera = true;
                tabla.height = 360;
                tabla.lineas = listaTabla;
                tabla.displayTabla();

            }

            function recargarTabla(operaciones) {
                var fila;
                lista = new Array();
                listaTabla = new Array();
                listaTabla_titulos = new Array();
                for (var i = 0; i < operaciones.length; i++) {
                    fila = operaciones[i];
                    lista[i] = [fila.id, fila.numExp, fila.numOper, fila.nombreOper, fila.desPrio, fila.desLin1, fila.desLin2, fila.desLin3, fila.impOper];
                    listaTabla[i] = [fila.id, fila.numExp, fila.numOper, fila.nombreOper, fila.desPrio, fila.desLin1, fila.desLin2, fila.desLin3, formatNumero(fila.impOper) + ' \u20ac'];
                    listaTabla_titulos[i] = [fila.id, fila.numExp, fila.numOper, fila.nombreOper, fila.desPrio, fila.desLin1, fila.desLin2, fila.desLin3, formatNumero(fila.impOper) + ' \u20ac'];
                }
                iniciarTabla();
            }


// FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaEliminar(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var operaciones = datos.tabla.lista;
                    pleaseWait('off');
                    recargarTabla(operaciones);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminar() {
                mostrarErrorPeticion("6");
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                pleaseWait('off');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

        </script>        
    </head>    
    <body class="bandaBody">       
        <div class="tab-page" id="tabPage141" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana141"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage141"));</script> 
            <h2 class="legendAzul" id="pestanaPrinc" style="padding-top: 15px;"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <br>
                <div id="divGeneral">     
                    <div id="lista"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevo" name="btnNuevo" class="botonGeneral"  value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevo();">
                    <input type="button" id="btnModificar" name="btnModificar" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificar();">
                    <input type="button" id="btnEliminar" name="btnEliminar"   class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminar();">
                </div>
            </div>  
        </div>
        <script  type="text/javascript">
            //Tabla
            var tabla;
            var lista = new Array();
            var listaTabla = new Array();
            var listaTabla_titulos = new Array();

            <%  		
               OperacionVO objectVO = null;
               List<OperacionVO> operaciones = null;
               if(request.getAttribute("listaOperaciones")!=null){
                   operaciones = (List<OperacionVO>)request.getAttribute("listaOperaciones");
               }													
               if (operaciones != null && operaciones.size() > 0){
                   for (int indice=0;indice < operaciones.size();indice++){
                        objectVO = operaciones.get(indice);
               
                        String id = "";
                        if(objectVO.getId() != null){
                           id = String.valueOf(objectVO.getId());
                        }
                        
                        String numExp = "";
                        if(objectVO.getNumExp() != null){
                            numExp = objectVO.getNumExp();
                        }
                        
                        String numOper = "";
                        if(objectVO.getNumOper() != null){
                            numOper = String.valueOf(objectVO.getNumOper());
                        }
                        
                        String nombreOper = "";
                        if(objectVO.getNombreOper() != null){
                            nombreOper = objectVO.getNombreOper();
                        }
                        
                        String prio = "";
                        if(objectVO.getDesPrio() != null){
                                prio = objectVO.getDesPrio();
                        }
                        
                        String lin1 = "";
                        if(objectVO.getLin1() != null){
                                lin1 = objectVO.getDesLin1();
                        }
                        
                        String lin2 = "";
                        if(objectVO.getLin2() != null){
                                lin2 = objectVO.getDesLin2();
                        }
                        
                        String lin3 = "";
                        if(objectVO.getLin3() != null){
                                lin3 = objectVO.getDesLin3();
                        }
                        
                        String impOper = "";
                        if(objectVO.getImpOper() != null){
                                impOper = formateador.format(objectVO.getImpOper());
                        }

                                         
            %>
            lista[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numExp%>', '<%=numOper%>', '<%=nombreOper%>', '<%=prio%>', '<%=lin1%>', '<%=lin2%>', '<%=lin3%>', '<%=impOper%>'];
            listaTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numExp%>', '<%=numOper%>', '<%=nombreOper%>', '<%=prio%>', '<%=lin1%>', '<%=lin2%>', '<%=lin3%>', '<%=impOper%>' + ' \u20ac'];
            listaTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numExp%>', '<%=numOper%>', '<%=nombreOper%>', '<%=prio%>', '<%=lin1%>', '<%=lin2%>', '<%=lin3%>', '<%=impOper%>' + ' \u20ac'];
            <%
                   }// for
               }// if
            %>
            iniciarTabla();

        </script>
        <div id="popupcalendar" class="text"></div>   
    </body>
</html>
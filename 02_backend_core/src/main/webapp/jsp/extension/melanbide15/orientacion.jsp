<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.vo.OrientacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.io.ObjectInputStream" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
            MeLanbide15I18n meLanbide15I18n = MeLanbide15I18n.getInstance();
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide15/melanbide15.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide15/CatpUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var mensajeValidacion = '';
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';



            function pulsarNuevaOrientacion() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE15&operacion=cargarNuevaOrientacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 450, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaOrientacion(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarOrientacion() {
                var indice = tablaOrientacion.selectedIndex;

                if (indice != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE15&operacion=cargarModificarOrientacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaOrientacion[indice][0], 450, 800, 'si', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaOrientacion(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarOrientacion() {
                var indice = tablaOrientacion.selectedIndex;

                if (indice != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        pleaseWait('on'); // Mostrar el indicador
                        var parametros = 'tarea=preparar&modulo=MELANBIDE15&operacion=eliminarOrientacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaOrientacion[indice][0];

                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: function (ajaxResult) {
                                    pleaseWait('off'); // Ocultar el indicador
                                    procesarRespuestaActualizarPestanaOrientacion(ajaxResult);
                                },
                                error: function () {
                                    pleaseWait('off'); // Ocultar el indicador
                                    mostrarErrorPeticion("6");
                                }
                            });
                        } catch (Err) {
                            pleaseWait('off');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }



            function crearTablaOrientacion() {
                tablaOrientacion = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaOrientacion'));


                tablaOrientacion.addColumna('0', 'center', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.id")%>");

                tablaOrientacion.addColumna('100', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.dninie")%>");
                tablaOrientacion.addColumna('150', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.identificacion")%>");
                tablaOrientacion.addColumna('75', 'center', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.horasOrientacion")%>");
                tablaOrientacion.addColumna('150', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.subvencion")%>");

                tablaOrientacion.displayCabecera = true;
                tablaOrientacion.height = 360;



            }

            function recargarTablaOrientacion(result) {
                var fila;
                listaOrientacion = [];
                listaOrientacionTabla = [];
                listaOrientacionTabla_titulos = [];

                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaOrientacion[i] = [fila.id, fila.descDniOri, fila.numIdenOri, fila.horasOri, fila.subvencionOri + ' \u20AC'];
                    listaOrientacionTabla[i] = [fila.id, fila.descDniOri, fila.numIdenOri, fila.horasOri, fila.subvencionOri + ' \u20AC'];
                }
                crearTablaOrientacion();
                tablaOrientacion.lineas = listaOrientacionTabla;
                tablaOrientacion.displayTabla();
            }

            function actualizarPestana() {
                var parametros = 'tarea=preparar&modulo=MELANBIDE15&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestanaOrientacion,
                        error: mostrarErrorPeticion
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestanaOrientacion(ajaxResult) {
                try {
                    var datos = JSON.parse(ajaxResult);
                    if (datos.tabla.codigoOperacion == "0") {
                        recargarTablaOrientacion(datos.tabla.lista); // Actualizar la tabla con los nuevos datos
                    } else {
                        mostrarErrorPeticion(datos.tabla.codigoOperacion); // Mostrar un mensaje de error si la operación falla
                    }
                } catch (e) {
                    console.error("Error procesando respuesta:", e);
                    mostrarErrorPeticion("2"); // Error genérico
                }
            }
            function mostrarErrorEliminarOrientacion() {
                mostrarErrorPeticion("6");
            }





            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                pleaseWait('off');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }






        </script>
    </head>
    <body class="bandaBody">

        <div class="tab-page" id="tabPage15-5" style="height:520px; width: 100%;"> 
            <br/>
            <h2 class="legendAzul"><%=meLanbide15I18n.getMensaje(idiomaUsuario, "label.titulo.orientacion")%></h2>
            <br/>
            <div style="clear: both;">
                <div id="listaOrientacion"  name="listaOrientacion" align="center" style="margin: 5px;"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoOrientacion" name="btnNuevoOrientacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaOrientacion();">
                <input type="button" id="btnModificarOrientacion" name="btnModificarOrientacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarOrientacion();">
                <input type="button" id="btnEliminarOrientacion" name="btnEliminarOrientacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarOrientacion();">

            </div> 

        </div>

        <script type="text/javascript">

            var tablaOrientacion;
            var listaOrientacion = new Array();
            var listaOrientacionTabla = new Array();
            var listaOrientacionTabla_titulos = new Array();

            <%
                
                OrientacionVO objectVO = null;
                List<OrientacionVO> lista = null;

                if (request.getAttribute("listaOrientacion") != null) {
                    lista = (List<OrientacionVO>) request.getAttribute("listaOrientacion");
                }

                // Validar si la lista tiene elementos
                if (lista != null && lista.size() > 0) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                for (int indice = 0; indice < lista.size(); indice++) {
                    objectVO = lista.get(indice);

                // Asignar dniNie con manejo de idiomas
                String dniOri = "";
                        if(objectVO.getDescDniOri()!=null){
                            String descripcion=objectVO.getDescDniOri();
                            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide15.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide15.FICHERO_PROPIEDADES);
                            String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide15.CODIGO_IDIOMA_EUSKERA){
                                    descripcion=descripcionDobleIdioma[1];
                                }else{
                                    // Cogemos la primera posición que debería ser castellano
                                    descripcion=descripcionDobleIdioma[0];
                                }
                            }
                            dniOri = descripcion;
                        }else{
                            dniOri="-";
                        }
                // Asignar otros campos
            
                    String numIdenOri = "";
                    if(objectVO.getNumIdenOri()!=null){
                       numIdenOri=objectVO.getNumIdenOri();
                    }
               
                    String horasOri="";
                    if(objectVO.getHorasOri()!=null){
                        horasOri=formateador.format(objectVO.getHorasOri());
                    } 
                    String subvencionOri="";
                    if(objectVO.getSubvencionOri()!=null){
                        subvencionOri=formateador.format(objectVO.getSubvencionOri());
                    }
               
            // Agregar las filas a las listas de JavaScript
            %>

            listaOrientacion[<%=indice%>] = ['<%= objectVO.getId() %>', '<%= dniOri %>', '<%= numIdenOri %>', '<%= horasOri %>', '<%= subvencionOri %>' + ' \u20AC'];
            listaOrientacionTabla[<%=indice%>] = ['<%= objectVO.getId() %>', '<%= dniOri %>', '<%= numIdenOri %>', '<%= horasOri %>', '<%= subvencionOri %> ' + ' \u20AC'];

            <%
                }
            }
            %>



            crearTablaOrientacion();
            tablaOrientacion.lineas = listaOrientacionTabla;
            tablaOrientacion.displayTabla();
        </script>    
        <div id="popupcalendar" class="text"></div> 
    </body>
</html>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO"%>
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
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator('.');
            simbolo.setGroupingSeparator(',');
            DecimalFormat formateador = new DecimalFormat("###,##0.##", simbolo);

            
            
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



            function pulsarNuevaContratacion() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE15&operacion=cargarNuevaContratacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaContratacion(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarContratacion() {
                var indice = tablaContratacion.selectedIndex;
                if (indice != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE15&operacion=cargarModificarContratacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContratacion[indice][0], 500, 800, 'si', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaContratacion(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarContratacion() {
                var indice = tablaContratacion.selectedIndex;
                if (indice != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide15I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        pleaseWait('on'); // Mostrar el indicador

                        var parametros = 'tarea=preparar&modulo=MELANBIDE15&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaContratacion[indice][0];

                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: function (ajaxResult) {
                                    pleaseWait('off'); // Ocultar el indicador
                                    procesarRespuestaActualizarPestanaContratacion(ajaxResult);
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
            function crearTablaContratacion() {
                tablaContratacion = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaContratacion'));


                tablaContratacion.addColumna('0', 'center', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.id")%>");

                tablaContratacion.addColumna('50', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.dninie")%>");
                tablaContratacion.addColumna('100', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.identificacion")%>");
                tablaContratacion.addColumna('100', 'center', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.fechaIni")%>");
                tablaContratacion.addColumna('100', 'center', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.fechaFin")%>");
                tablaContratacion.addColumna('75', 'left', "<%=meLanbide15I18n.getMensaje(idiomaUsuario,"tabla.subvencion")%>");


                tablaContratacion.displayCabecera = true;
                tablaContratacion.height = 360;



            }
            function recargarTablaContratacion(result) {
                var fila;
                listaContratacion = [];
                listaContratacionTabla = [];
                listaContratacionTabla_titulos = [];

                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaContratacion[i] = [fila.id, fila.descDniCon, fila.numIdenCon, fila.fecIniConStr, fila.fecFinConStr, fila.subvencionCon + ' \u20AC'];
                    listaContratacionTabla[i] = [fila.id, fila.descDniCon, fila.numIdenCon, fila.fecIniConStr, fila.fecFinConStr, fila.subvencionCon + ' \u20AC'];
                }
                crearTablaContratacion();
                tablaContratacion.lineas = listaContratacionTabla;
                tablaContratacion.displayTabla();
            }
            function actualizarPestana() {
                var parametros = 'tarea=preparar&modulo=MELANBIDE15&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestanaContratacion,
                        error: mostrarErrorPeticion
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestanaContratacion(ajaxResult) {
                try {
                    var datos = JSON.parse(ajaxResult);
                    if (datos.tabla.codigoOperacion == "0") {
                        recargarTablaContratacion(datos.tabla.lista); // Actualizar la tabla con los nuevos datos
                    } else {
                        mostrarErrorPeticion(datos.tabla.codigoOperacion); // Mostrar un mensaje de error si la operación falla
                    }
                } catch (e) {
                    console.error("Error procesando respuesta:", e);
                    mostrarErrorPeticion("2"); // Error genérico
                }
            }

            function mostrarErrorEliminarContratacion() {
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

        <div class="tab-page" id="tabPage15-6" style="height:520px; width: 100%;"> 
            <br/>
            <h2 class="legendAzul"><%=meLanbide15I18n.getMensaje(idiomaUsuario, "label.titulo.contratacion")%></h2>
            <br/>
            <div style="clear: both;">
                <div id="listaContratacion"  name="listaContratacion" align="center" style="margin: 5px;"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoContratacion" name="btnNuevoContratacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaContratacion();">
                <input type="button" id="btnModificarContratacion" name="btnModificarContratacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                <input type="button" id="btnEliminarContratacion" name="btnEliminarContratacion" class="botonGeneral" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">

            </div> 

        </div>

        <script type="text/javascript">

            var tablaContratacion;
            var listaContratacion = new Array();
            var listaContratacionTabla = new Array();
            var listaContratacionTabla_titulos = new Array();

            <%
                ContratacionVO objectVO = null;
                List<ContratacionVO> lista = null;

                // Verificar si existe el atributo listaIdentidad
                if (request.getAttribute("listaContratacion") != null) {
                    lista = (List<ContratacionVO>) request.getAttribute("listaContratacion");
                }

                // Validar que la lista no sea nula y tenga elementos
                if (lista != null && lista.size() > 0) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Iterar sobre la lista
                for (int indice = 0; indice < lista.size(); indice++) {
                    objectVO = lista.get(indice);

                // Asignar dniNie con manejo de idiomas
                String dniCon = "";
                        if(objectVO.getDescDniCon()!=null){
                            String descripcion=objectVO.getDescDniCon();

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
                            dniCon = descripcion;
                        }else{
                            dniCon="-";
                        }
                 String numIdenCon = "";
                    if(objectVO.getNumIdenCon()!=null){
                       numIdenCon=objectVO.getNumIdenCon();
                    }
                    
                   
                    
                String fecIniConStr = "-";
                if (objectVO.getFecIniCon() != null) {
                    fecIniConStr = dateFormat.format(objectVO.getFecIniCon());
                }    
                    
                    
              String fecFinConStr = "-";
                if (objectVO.getFecFinCon() != null) {
                    fecFinConStr = dateFormat.format(objectVO.getFecFinCon());
                }

                  String subvencionCon="";
                    if(objectVO.getSubvencionCon()!=null){
                        subvencionCon=formateador.format(objectVO.getSubvencionCon());
                    }
               
            %>
            listaContratacion     [<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dniCon%>', '<%=numIdenCon%>', '<%=fecIniConStr%>', '<%=fecFinConStr%>', '<%=subvencionCon%>' + ' \u20AC'];
            listaContratacionTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dniCon%>', '<%=numIdenCon%>', '<%=fecIniConStr%>', '<%=fecFinConStr%>', '<%=subvencionCon%>' + ' \u20AC'];
            <%
                        } // Fin del for
                    } // Fin del if lista
            %>


            crearTablaContratacion();
            tablaContratacion.lineas = listaContratacionTabla;
            tablaContratacion.displayTabla();
        </script>    
        <div id="popupcalendar" class="text"></div> 
    </body>
</html>
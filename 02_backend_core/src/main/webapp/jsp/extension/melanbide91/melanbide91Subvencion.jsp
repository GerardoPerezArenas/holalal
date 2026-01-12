<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.i18n.MeLanbide91I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>

<html>
    <head>
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

            MeLanbide91I18n meLanbide91I18n = MeLanbide91I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide91/melanbide91.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide91/GoUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var pestaña;
     
            function pulsarNuevoSubvenciones() {               
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE91&operacion=cargarNuevoAccesoSubvencion&tipo=0&nuevo=1&numExp=<%=numExpediente%>&numero=<%=numExpediente%>', 500, 1100, 'no', 'no', function(result){
                    if (result != undefined){								
                        if (result[0] == '0') {
                            recargarTabla(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarSubvenciones() {
                var indice = tablaSubvenSolic.selectedIndex;
                if (indice != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE91&operacion=cargarModificarAccesoSubvencion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&numero=<%=numExpediente%>&id=' + lista[indice][0], 500, 1100, 'no', 'no', function(result){
                            if (result != undefined){								
                                if (result[0] == '0') {
                                    recargarTabla(result[1]);
                                }
                            }
                    });
                }
                else {
                    jsp_alerta('A', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarSubvenciones() {
                var indice = tablaSubvenSolic.selectedIndex;
                if (indice != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar2")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoGO');
                        console.log('<%=numExpediente%>');
                        console.log(lista[indice][0]);
                        var parametros = 'tarea=preparar&modulo=MELANBIDE91&operacion=eliminarAccesoSubvencion&tipo=0&numExp=<%=numExpediente%>&id=' + lista[indice][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminar
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoGO');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTabla(){
                tablaSubvenSolic = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaSub'));

                tablaSubvenSolic.addColumna('0', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.id")%>");                
                tablaSubvenSolic.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.tipodatos")%>");
                tablaSubvenSolic.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.tipo")%>");
                tablaSubvenSolic.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.fecha")%>");
                tablaSubvenSolic.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.destino")%>");
                tablaSubvenSolic.addColumna('100', 'center', "<%=meLanbide91I18n.getMensaje(idiomaUsuario,"tabla.coste")%>");
                
                tablaSubvenSolic.displayCabecera = true;
                tablaSubvenSolic.height = 360;
                
                tablaSubvenSolic.lineas = lista;
                tablaSubvenSolic.displayTabla();
           
            }
   
            function recargarTabla(AccesoSubvencion) {
                var fila;
                lista = new Array();
                listaTabla = new Array();
                
                for (var i = 0; i < AccesoSubvencion.length; i++) {
                    fila = AccesoSubvencion[i];
                    lista[i] =              [fila.id,fila.destipoDatos,fila.tipo,fila.fechaStr,fila.destino,fila.coste];                    
                    listaTabla[i] =         [fila.destipoDatos,fila.tipo,fila.fecha,fila.fechaStr,fila.destino,fila.coste];   
                }
                iniciarTabla();
            }
            
            function actualizarPestana() {
                var parametros = 'tarea=preparar&modulo=MELANBIDE91&operacion=actualizarPestanaSubvenciones&tipo=0&numExp=<%=numExpediente%>';
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

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var AccesoSubvencion = datos.tabla.lista;
                        elementoVisible('off', 'barraProgresoGO');
                        recargarTabla(AccesoSubvencion);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminar() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }
            
            function selectRow(indiceSeleccionadoTabla, objetoTabla){            
                   if(objetoTabla!=null && objetoTabla!=undefined && objetoTabla.tabla!=null &&objetoTabla.tabla!=undefined
                       ){
                   if(objetoTabla.tabla.selectedIndex==indiceSeleccionadoTabla){
                       objetoTabla.tabla.selectedIndex=-1;
                       objetoTabla.tabla.desactivaRow(indiceSeleccionadoTabla);
                   }else{
                       if(objetoTabla.tabla.selectedIndex!= -1){
                           objetoTabla.tabla.desactivaRow(objetoTabla.tabla.selectedIndex);
                       }
                       objetoTabla.tabla.selectedIndex=indiceSeleccionadoTabla;
                       objetoTabla.tabla.activaRow(indiceSeleccionadoTabla);
                   }
                   
               }
            }
        </script>
        
    </head>
    
    <body class="bandaBody">
        <div class="tab-page" id="tabPage912" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana912"><%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaSubvencion")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage912"));</script>
            <div id="barraProgresoGO" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <br/>
            <h2 class="legendAzul" id="pestanaPrincSub"><%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalSubvencion")%></h2>
            <div>    
                <br>
                <div id="divGeneralSub">     
                    <div id="listaSub"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoSub" name="btnNuevoSub" class="botonGeneral"  value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoSubvenciones();">
                    <input type="button" id="btnModificarSub" name="btnModificarSub" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarSubvenciones();">
                    <input type="button" id="btnEliminarSub" name="btnEliminarSub"   class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarSubvenciones();">
                </div>
            </div>  
        </div>
                
        <script  type="text/javascript">
            //Tabla
            var tabla;
            var lista = new Array();
            var listaTabla = new Array();
            

            <%  		
               SubvenSolicVO objectVO = null;
               List<SubvenSolicVO> List = null;
               if(request.getAttribute("listaSubvenciones")!=null){
                   List = (List<SubvenSolicVO>)request.getAttribute("listaSubvenciones");
               }													
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);
               
                        String tipodatos = "TIPODATOS";
                        if(objectVO.getTipoDatos()!=null){
                           tipodatos=objectVO.getTipoDatos();
                        }
                        String tipodatosDesc = "";
                        if(objectVO.getDesTipoDatos()!=null){
                           tipodatosDesc=objectVO.getDesTipoDatos();
                        }
                        
                        String tipo = "TIPO";
                        if(objectVO.getTipo()!=null){
                           tipo=objectVO.getTipo();
                        }
                        
                        Date fecha= null;
                        if(objectVO.getFecha()!=null){
                           fecha=objectVO.getFecha();
                        }
                        
                        String fechaStr= null;
                        if(objectVO.getFechaStr()!=null){
                           fechaStr=objectVO.getFechaStr();
                        }
                        
                        String destino = "DESTINO";
                        if(objectVO.getDestino()!=null){
                           destino= objectVO.getDestino();
                        }
                        
                        String coste = "";
                        if(objectVO.getCoste()!=null){
                            coste=String.valueOf(objectVO.getCoste()).toString().replace(".",",");
                           
                        }
                        
                        
               
            %>
                
            lista[<%=indice%>] = ['<%=objectVO.getId()%>','<%=tipodatosDesc%>', '<%=tipo%>', '<%=fechaStr%>','<%=destino%>', '<%=coste%>'];
            listaTabla[<%=indice%>] = ['<%=objectVO.getId()%>','<%=tipodatosDesc%>', '<%=tipo%>', '<%=fecha%>','<%=destino%>', '<%=coste%>'];
            
            <%
                   }// for
               }// if
            %>
            iniciarTabla();

        </script>
       
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>
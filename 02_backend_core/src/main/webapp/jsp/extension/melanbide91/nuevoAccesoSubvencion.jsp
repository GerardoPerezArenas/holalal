<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.i18n.MeLanbide91I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            SubvenSolicVO datModif = new SubvenSolicVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            
            MeLanbide91I18n meLanbide91I18n = MeLanbide91I18n.getInstance();

            numExpediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try
                {
                    if (session != null) 
                    {
                        if (usuario != null) 
                        {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                }
                catch(Exception ex)
                {
                }

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (SubvenSolicVO)request.getAttribute("datModif");    
                }
            }
            catch(Exception ex)
            {
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide91/melanbide91.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide91/GoUtils.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide91/GoUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';
            
          
            function guardarDatos() {
                // alert("guardando datos");
                if (validarDatos()) {
                    //alert("entra en validar");
                    elementoVisible('on', 'barraProgresoGO');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE91&operacion=crearNuevoAccesoSubvencion&tipo=0";
                       //alert(parametros);
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE91&operacion=modificarAccesoSubvencion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                       
                    }
                    parametros += '&numExp=<%=numExpediente%>' + '&numero=<%=numExpediente%>'
                            + "&tipodatos=" + document.getElementById('tipoDatos').value
                            + "&tipoHtml=" + document.getElementById('tipo').value
                            + "&fecha=" + document.getElementById('fecha').value
                            + "&destino=" + document.getElementById('destino').value
                            + "&coste=" + document.getElementById('coste').value
                            
                            ;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaAcceso,
                            error: mostrarErrorAltaAcceso
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoGO');
                        mostrarError();
                    }
                } else {
                     //alert("no entra en validar");
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var campo = document.getElementById('tipoDatos').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.tipodatos")%>';
                    return false;
                }
                campo = document.getElementById('tipo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.tipo")%>';                        
                    return false;
                }
                campo = document.getElementById('fecha').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.fecha")%>';
                    return false;
                } 
                campo = document.getElementById('destino').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.destino")%>';                        
                    return false;
                }
                campo = document.getElementById('coste').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.coste")%>';
                    return false;
                   }else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.coste.errNumerico")%>';
                    return false;
                }
                return true;
            }
            
            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaAcceso(ajaxResult) {
                elementoVisible('off', 'barraProgresoGO');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var accesos = datos.tabla.lista;
                    if (accesos.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = accesos;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError(5);
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaAcceso() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarError(codigo);
            }

            function mostrarError(codigo) {
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide91I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }
            
      
        
        </script>
    </head>
    <body  class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoGO');
            }" >
        <div class="contenidoPantalla">
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
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">      
                            <%=nuevo != null && nuevo=="1" ? meLanbide91I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso2"):meLanbide91I18n.getMensaje(idiomaUsuario,"label.modifAcceso")%>
                        </span>
                    </div>
                        
                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.tipoDatos")%>
                        </div>
                      
                        <div>
                            <select name="tipoDatos" id="tipoDatos" class="inputTexto">
                                <option value=""> <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.texto.selecionar.opcion")%></option>
                                <c:forEach items="${listaTIPODATOS}" var="tipoDato" varStatus="contador">
                                    <option value="<c:out value="${tipoDato.codigo}"/>" title="<c:out value="${tipoDato.descripcion}"/>"><c:out value="${tipoDato.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>    
                    </div>
                            
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.tipo")%>
                        </div>
                         <div>
                            <div style="float: left;">
                                <input id="tipo" name="tipo" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getTipo() != null ? datModif.getTipo() : ""%>"/>
                            </div>
                        </div>
                     
                    </div>
                                                            
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.fecha")%>
                        </div>
                       
                        <div>
                            <input type="text" id="fecha" class="inputTxtFechaLanbide" value="<%=datModif != null && datModif.getFechaStr() != null ? datModif.getFechaStr() : ""%>" placeholder="dd/mm/yyyy"
                                   maxlength="10" name="fecha" size="10"
                                   onkeypress = "return permiteSoloFormatoFechas(event);"
                                   onfocus="javascript:this.select();"
                                   onblur = "validarFormatoFecha(this);"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalfecha(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calfecha" name="calfecha" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                            </A>
                        </div>
                     
                    </div>
                    
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.destino")%>
                        </div>
                         <div>
                            <div style="float: left;">
                                <input id="destino" name="destino" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getDestino() != null ? datModif.getDestino() : ""%>"/>
                            </div>
                        </div>
                     
                    </div>
                   
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.coste")%>
                        </div>
                         <div>
                            <div style="float: left;">
                                <input id="coste" name="coste" type="text" class="inputTexto" size="22" maxlength="10" 
                                value="<%=datModif != null && datModif.getCoste() != null ? datModif.getCoste().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>
                     
                    </div>
                    
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; margin-top: 25px; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
     <script type="text/javascript">           
            if("0"=='<%=nuevo%>'){
                var tipoDatoModificando = '<%=datModif != null && datModif.getTipoDatos() != null ? datModif.getTipoDatos() : ""%>';
                 document.getElementById('tipoDatos').value=tipoDatoModificando;                
            }
    </script>
</html> 

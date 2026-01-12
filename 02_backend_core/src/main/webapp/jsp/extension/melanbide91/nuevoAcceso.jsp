<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.i18n.MeLanbide91I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <div style="width: 10%"> 
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            ContrGenVO datModif = new ContrGenVO();
      
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
                    datModif = (ContrGenVO)request.getAttribute("datModif");    
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


            function guardarDatosContratacion() {
                if (validarDatosContratacion()) {
                    elementoVisible('on', 'barraProgresoGO');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE91&operacion=crearNuevoAcceso&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE91&operacion=modificarAcceso&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";

                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&nombre=" + document.getElementById('nombre').value
                            + "&apellido1=" + document.getElementById('apellido1').value
                            + "&apellido2=" + document.getElementById('apellido2').value
                            + "&sexo=" + document.getElementById('listSexo').value
                            + "&dni=" + document.getElementById('dni').value
                            + "&psiquica=" + document.getElementById('psiquica').value
                            + "&fisica=" + document.getElementById('fisica').value
                            + "&sensorial=" + document.getElementById('sensorial').value
                            + "&fecIni=" + document.getElementById('fecIni').value
                            + "&jornada=" + document.getElementById('listaJornada').value
                            + "&porcParcial=" + document.getElementById('porcParcial').value
                            ;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaAcceso,
                            error: mostrarErrorAltaAccesoContratacion
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoGO');
                        mostrarErrorContratacion();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatosContratacion() {
                mensajeValidacion = "";
                var campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }
                var campo = document.getElementById('apellido1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }
                var campo = document.getElementById('apellido2').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.apellido2")%>';
                    return false;
                }               
                
                campo = document.getElementById('dni').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.dninie")%>';
                    return false;
                   }else if (!validarDniNie(document.getElementById('dni'))) {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.dninie.errNumerico")%>';
                    return false;
                }               
                campo = document.getElementById('listSexo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                    return false;
                }
                
                campo = document.getElementById('psiquica').value;
                 if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.psiquica.errNumerico")%>';
                        return false;
                    }
                }
                
                campo = document.getElementById('fisica').value;
                if (campo != null && campo != '') {
                     if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.fisica.errNumerico")%>';
                        return false;
                    }
                }
                
                campo = document.getElementById('sensorial').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.sensorial.errNumerico")%>';
                        return false;
                    }
                }
                
                campo = document.getElementById('fecIni').value;
                if (campo.value != null && campo.value != '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.fecIni")%>';
                    return false;
                }
                
               campo = document.getElementById('listaJornada').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.listaJornada")%>';
                    return false;
                }
                campo = document.getElementById('porcParcial').value;
                if (campo != null && campo != '') {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.porcParcial")%>';
                    if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                    mensajeValidacion = '<%=meLanbide91I18n.getMensaje(idiomaUsuario, "msg.porcParcial.errNumerico")%>';
                    return false;
                    }
                }
                
                return true;
               
            }
            
            
            function cancelarContratacion() {
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
                        mostrarErrorContratacion(5);
                    }
                } else {
                    mostrarErrorContratacion(codigoOperacion);
                }
            }

            function mostrarErrorAltaAccesoContratacion() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarErrorContratacion(codigo);
            }

            function mostrarErrorContratacion(codigo) {
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
    <body class="bandaBody box" onload="javascript:{
                elementoVisible('off', 'barraProgresoGO');
            }" >
        <div style="width: 100%" class="contenidoPantalla">
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
                            <%=nuevo != null && nuevo=="1" ? meLanbide91I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso"):meLanbide91I18n.getMensaje(idiomaUsuario,"label.modifAcceso")%>
                        </span>
                    </div>

                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="nombre" name="nombre" type="text" class="inputTexto" size="60" maxlength="60" 
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="apellido1" name="apellido1" type="text" class="inputTexto" size="60" maxlength="60" 
                                       value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="apellido2" name="apellido2" type="text" class="inputTexto" size="60" maxlength="60" 
                                       value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.dni")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="dni" name="dni" type="text" class="inputTexto" size="22" maxlength="22" 
                                       value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>                       
                        <div>
                            <select name="listSexo" id="listSexo" class="inputTexto">
                                <option value=""> <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.texto.selecionar.opcion")%></option>
                                <c:forEach items="${listaSINO}" var="tipoSexo" varStatus="contador">
                                    <option value="<c:out value="${tipoSexo.codigo}"/>" title="<c:out value="${tipoSexo.descripcion}"/>"><c:out value="${tipoSexo.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        
                    </div>
                    

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.psiquica")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="psiquica" name="psiquica" type="text" class="inputTexto" size="18" maxlength="18" 
                                           value="<%=datModif != null && datModif.getPsiquica() != null ? datModif.getPsiquica().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>

                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.fisica")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="fisica" name="fisica" type="text" class="inputTexto" size="18" maxlength="18" 
                                           value="<%=datModif != null && datModif.getFisica() != null ? datModif.getFisica().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>

                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.sensorial")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="sensorial" name="sensorial" type="text" class="inputTexto" size="18" maxlength="18" 
                                           value="<%=datModif != null && datModif.getSensorial() != null ? datModif.getSensorial().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>

                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.fecIni")%>
                        </div>
                       
                        <div>
                            <input type="text" id="fecIni" class="inputTxtFechaLanbide" value="<%=datModif != null && datModif.getFecIniStr() != null ? datModif.getFecIniStr() : ""%>" placeholder="dd/mm/yyyy"
                                   maxlength="10" name="fecIni" size="10"
                                   onkeypress = "return permiteSoloFormatoFechas(event);"
                                   onfocus="javascript:this.select();"
                                   onblur = "validarFormatoFecha(this);"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalfecIni(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calfecIni" name="calfecIni" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                            </A>
                        </div>

                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.jornada")%>
                        </div>
                            <div>
                            <select name="listaJornada" id="listaJornada" class="inputTexto">
                                <option value="">  <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.texto.selecionar.opcion")%></option>
                                <c:forEach items="${listaJORNADA}" var="tipoJornada" varStatus="contador">
                                    <option value="<c:out value="${tipoJornada.codigo}"/>" title="<c:out value="${tipoJornada.descripcion}"/>"><c:out value="${tipoJornada.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>

                    </div>

                    <br><br>
                   <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide91I18n.getMensaje(idiomaUsuario,"label.porcParcial")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                    <input id="porcParcial" name="pocParcial" type="text" class="inputTexto" size="18" maxlength="18" 
                                           value="<%=datModif != null && datModif.getPorcParcial() != null ? datModif.getPorcParcial().toString().replace(".",",") : ""%>"/>
                            </div>
                        </div>

                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosContratacion();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide91I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelarContratacion();"/>
                        </div>
                    </div>
                </div>
            </form>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>

     <script type="text/javascript">           
            if("0"=='<%=nuevo%>'){
                var tipoJORNADAModificando = '<%=datModif != null && datModif.getJornada() != null ? datModif.getJornada() : ""%>';
                 document.getElementById('listaJornada').value=tipoJORNADAModificando;                
            }
            if("0"=='<%=nuevo%>'){
                var tipoSEXOModificando = '<%=datModif != null && datModif.getSexo() != null ? datModif.getSexo() : ""%>';
                 document.getElementById('listSexo').value=tipoSEXOModificando;                
            }
    </script>
<div/>    
</html> 

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios.ServiciosVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            ServiciosVO datModif = new ServiciosVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
            
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
                numExpediente    = request.getParameter("numero");
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (ServiciosVO)request.getAttribute("datModif");
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide50/RgcfmUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var mensajeValidacion = '';

            function guardarDatos() {
                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=crearServicio&tipo=0&numero=<%=numExpediente%>"
                                + "&descripcion=" + document.getElementById('descripcion').value
                                + "&ubicacion=" + document.getElementById('ubicacion').value
                                + "&superficie=" + document.getElementById('superficie').value;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarServicio&tipo=0&numero=<%=numExpediente%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&descripcion=" + document.getElementById('descripcion').value
                                + "&ubicacion=" + document.getElementById('ubicacion').value
                                + "&superficie=" + document.getElementById('superficie').value;
                    }
                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        ajax.send(parametros);
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            var xmlDoc = null;
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async = "false";
                                xmlDoc.loadXML(text);
                            } else {
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var listaServicios = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaServicios[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[0] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "SER_DESC") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "SER_UBIC") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "SER_SUPE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[3].toString();
                                            tex = tex.replace(".", ",");
                                            fila[3] = tex;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    }
                                }
                                listaServicios[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(listaServicios);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;
                var descripcion = document.getElementById('descripcion').value;
                if (descripcion == null || descripcion == '') {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "servicios.msg.todosCamposOblig")%>';
                    return false;
                } else {
                    if (!compruebaTamanoCampo(document.getElementById('descripcion'), 500)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                        return false;
                    }
                }

                var ubicacion = document.getElementById('ubicacion').value;
                if (ubicacion == null || ubicacion == '') {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "servicios.msg.ubicacion")%>';
                    return false;
                } else {
                    if (!compruebaTamanoCampo(document.getElementById('ubicacion'), 500)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                        return false;
                    }
                }

                var superficie = document.getElementById('superficie').value;
                if (superficie == null || superficie == '') {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "servicios.msg.superficie")%>';
                    return false;
                } else
                {
                    if (!validarNumericoDecimalPrecisionRgcfm(document.getElementById('superficie'), 6, 2)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "servicios.msg.superficie.errDecimal")%>';
                        return false;
                    }
                }
                return correcto;
            }

        </script>
    </head>
    <body class="bandaBody">
        <div class="contenidoPantalla">
            <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.servicios.nuevoAseoServicio")%>
                        </span>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.servicios.descripcion")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="4" cols="61" id="descripcion" name="descripcion" maxlength="500" onblur="compruebaTamanoCampo(this, 500)"><%=datModif != null && datModif.getDescripcion() != null ? datModif.getDescripcion()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.servicios.ubicacionPlano")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="4" cols="61" id="ubicacion" name="ubicacion" maxlength="500" onblur="compruebaTamanoCampo(this, 500)"><%=datModif != null && datModif.getUbicacion() != null ? datModif.getUbicacion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.servicios.superficie")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="superficie" name="superficie" type="text" class="inputTexto" size="15" maxlength="7" 
                                       value="<%=datModif != null && datModif.getSuperficie() != null ? datModif.getSuperficie().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="botonera">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();">
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
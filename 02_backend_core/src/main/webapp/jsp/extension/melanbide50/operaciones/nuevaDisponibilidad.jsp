<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            DisponibilidadVO datModif = new DisponibilidadVO();
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
                    datModif = (DisponibilidadVO)request.getAttribute("datModif");
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
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarDisponibilidad&tipo=0&numero=<%=numExpediente%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                + "&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp(): ""%>"
                                + "&propiedadce=" + document.getElementById('propiedadce').value
                                + "&situados=" + document.getElementById('situados').value
                                + "&supaulas=" + document.getElementById('supaulas').value
                                + "&suptaller=" + document.getElementById('suptaller').value
                                + "&supaulastaller=" + document.getElementById('supaulastaller').value
                                + "&supcampoprac=" + document.getElementById('supcampoprac').value;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarDisponibilidad&tipo=0&numero=<%=numExpediente%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                + "&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp(): ""%>"
                                + "&propiedadce=" + document.getElementById('propiedadce').value
                                + "&situados=" + document.getElementById('situados').value
                                + "&supaulas=" + document.getElementById('supaulas').value
                                + "&suptaller=" + document.getElementById('suptaller').value
                                + "&supaulastaller=" + document.getElementById('supaulastaller').value
                                + "&supcampoprac=" + document.getElementById('supcampoprac').value;
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
                        var listaNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaNueva[j] = codigoOperacion;
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
                                    } else if (hijosFila[cont].nodeName == "ID_ESPSOL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_NUM") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_CODCP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_PRCE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_SIT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_AUL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_TAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_AUTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DRE_CAPR") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    }
                                }
                                listaNueva[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(listaNueva);
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

                if (!compruebaTamanoCampo(document.getElementById('propiedadce'), 500)) {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                    return false;
                }
                if (!compruebaTamanoCampo(document.getElementById('situados'), 500)) {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                    return false;
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
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.modifDisponibilidad")%>
                        </span>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.cpespecialidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="codcp" name="codcp" type="text" class="inputTexto" readonly="true" size="15" maxlength="8" 
                                       value="<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp()  : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.propiedadce")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="2" cols="65" id="propiedadce" name="propiedadce" maxlength="500" onblur="compruebaTamanoCampo(this, 500)"><%=datModif != null && datModif.getPropiedadCedidos() != null ? datModif.getPropiedadCedidos()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.situados")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="2" cols="65" id="situados" name="situados" maxlength="500" onblur="compruebaTamanoCampo(this, 500)"><%=datModif != null && datModif.getSituados() != null ? datModif.getSituados()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supaulas")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="supaulas" name="supaulas" type="text" class="inputTexto" size="30" maxlength="200" 
                                       value="<%=datModif != null && datModif.getSupAulas() != null ? datModif.getSupAulas().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.suptaller")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="suptaller" name="suptaller" type="text" class="inputTexto" size="30" maxlength="200" 
                                       value="<%=datModif != null && datModif.getSupTaller() != null ? datModif.getSupTaller().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supaulastaller")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="supaulastaller" name="supaulastaller" type="text" class="inputTexto" size="30" maxlength="200" 
                                       value="<%=datModif != null && datModif.getSupAulaTaller() != null ? datModif.getSupAulaTaller().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supcampoprac")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="supcampoprac" name="supcampoprac" type="text" class="inputTexto" size="30" maxlength="200" 
                                       value="<%=datModif != null && datModif.getSupCampoPract() != null ? datModif.getSupCampoPract().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
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


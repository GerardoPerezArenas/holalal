<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            SMIVO datModif = new SMIVO();

            String codOrganizacion = "";
            String codMotivoVisita = "";
            String nuevo = "";
            String expediente = "";
            String fecha = "";

            String apellidosS = (String)request.getAttribute("apellidosS");
            String numLineaS = (String)request.getAttribute("numLineaS");

            String smiDia = (String)request.getAttribute("smiDia");
            double smiDiaDecimal = Double.parseDouble(smiDia);
        
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            //ConstantesMeLanbide58 _constantesMeLanbide58 = ConstantesMeLanbide58();

            expediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        if (usuario != null) {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                } catch(Exception ex) {
                }

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                
                if(request.getAttribute("datModif") != null) {
                    datModif = (SMIVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formatoFecha.format(datModif.getFecha());
                }
            } catch(Exception ex) {
            }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            var comboListaCausaIncidencia;
            var listaCodigosCausaIncidencia = new Array();
            var listaDescripcionesCausaIncidencia = new Array();

            function buscaCodigoCausaIncidencia(codCausaIncidencia) {
                comboListaCausaIncidencia.buscaCodigo(codCausaIncidencia);
            }

            function cargarDatosCausaIncidencia() {
                var codCausaIncidenciaSeleccionado = document.getElementById("codListaCausaIncidencia").value;
                buscaCodigoCausaIncidencia(codCausaIncidenciaSeleccionado);
            }

            function rellenardatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoCausaIncidencia('<%=datModif.getCausaIncidencia() != null ? datModif.getCausaIncidencia() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function guardarDatos() {
                if (validarDatos() && validarDatosNumericosVacios()) {

                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var ape = "";
                    if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                        nombre = "";
                    } else {
                        nombre = document.getElementById('nombre').value.replace(/\'/g, "''");
                    }

                    if (document.getElementById('apellidos').value == null || document.getElementById('apellidos').value == '') {
                        ape = "";
                    } else {
                        ape = document.getElementById('apellidos').value.replace(/\'/g, "''");
                    }

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoSMI&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + '&nombre=' + nombre
                                + '&apellidos=' + ape
                                + "&codCausaIncidencia=" + document.getElementById('codListaCausaIncidencia').value
                                + "&numDiasSinIncidencias=" + document.getElementById('numDiasSinIncidencias').value
                                + "&numDiasIncidencia=" + document.getElementById('numDiasIncidencia').value
                                + "&importeSubvencion=" + document.getElementById('importeSubvencion').value
                                + "&fecha=" + document.getElementById('fecha').value
                                //   + "&codPeriodo=" + document.getElementById('codListaPeriodo').value
                                //   + "&fechaPeriodo=" + document.getElementById('fechaPeriodo').value
                                + "&porcJornada=" + document.getElementById('porcJornada').value
                                + "&porcReduccion=" + document.getElementById('porcReduccion').value
                                + "&observaciones=" + document.getElementById('observaciones').value
                                ;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarSMI&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&expediente=<%=datModif != null && datModif.getNumExp() != null ? datModif.getNumExp().toString() : ""%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + '&nombre=' + nombre
                                + '&apellidos=' + ape
                                + "&codCausaIncidencia=" + document.getElementById('codListaCausaIncidencia').value
                                + "&numDiasSinIncidencias=" + document.getElementById('numDiasSinIncidencias').value
                                + "&numDiasIncidencia=" + document.getElementById('numDiasIncidencia').value
                                + "&importeSubvencion=" + document.getElementById('importeSubvencion').value
                                + "&fecha=" + document.getElementById('fecha').value
                                //  + "&codPeriodo=" + document.getElementById('codListaPeriodo').value
                                //  + "&fechaPeriodo=" + document.getElementById('fechaPeriodo').value
                                + "&porcJornada=" + document.getElementById('porcJornada').value
                                + "&porcReduccion=" + document.getElementById('porcReduccion').value
                                + "&observaciones=" + document.getElementById('observaciones').value
                                + "&apellidosS=" + "<%=apellidosS%>"
                                + "&numLineaS=" + "<%=numLineaS%>"
                                + "&porcOriginal=<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString(): ""%>"
                                ;
                    }
                    var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaModificarSMI")%>');
                    if (resultado == 1) {
                        try {
                            ajax.open("POST", baseUrl, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
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
                            var lista = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    lista[j] = codigoOperacion;
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
                                        } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUMDIASSININCIDENCIAS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[4].toString();
                                                tex = tex.replace(".", ",");
                                                fila[4] = tex;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUMDIASINCIDENCIA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[5].toString();
                                                tex = tex.replace(".", ",");
                                                fila[5] = tex;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "CAUSAINCIDENCIA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECHA") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[8].toString();
                                                tex = tex.replace(".", ",");
                                                fila[8] = tex;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PORCREDUCCION") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[9].toString();
                                                tex = tex.replace(".", ",");
                                                fila[9] = tex;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTESUBVENCION") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[10].toString();
                                                tex = tex.replace(".", ",");
                                                fila[10] = tex;
                                            } else {
                                                fila[10] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTERECALCULO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[11].toString();
                                                tex = tex.replace(".", ",");
                                                fila[11] = tex;
                                            } else {
                                                fila[11] = '-';
                                            }
                                        }  else if (hijosFila[cont].nodeName == "OBSERVACIONES") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[12].toString();
                                                fila[12] = tex;
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[13] = '-';
                                            }
                                        }
                                    }// for elementos de la fila
                                    lista[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                //jsp_alerta("A",'Correcto');
                                self.parent.opener.retornoXanelaAuxiliar(lista);
                                cerrarVentana();
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else if (codigoOperacion == "5") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.recalculoSMI")%>');
                            } else if (codigoOperacion == "6") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.notFoundSMI")%>');
                            } else if (codigoOperacion == "7") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarRecalculoSMI")%>');
                            } else if (codigoOperacion == "8") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.noExisteC")%>');
                            } else if (codigoOperacion == "9") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.porcentaje")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                            alert(Err);
                        }//try-catch

                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function validarDatosRecalculoVacios() {
                mensajeValidacion = "";
                var correcto = true;

                if (document.getElementById('numDiasSinIncidencias').value == null || document.getElementById('numDiasSinIncidencias').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasSinIncidencias")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('numDiasSinIncidencias').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasSinIncidencias.errNumerico")%>';
                        return false;
                    }
                }

                if (document.getElementById('porcJornada').value == null || document.getElementById('porcJornada').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcJornada').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcJornada').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errRango")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('porcReduccion').value == null || document.getElementById('porcReduccion').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcReduccion').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcJornada').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion.errRango")%>';
                            return false;
                        }
                    }
                }

                return correcto;
            }

            function validarDatosNumericosVacios() {
                mensajeValidacion = "";
                var correcto = true;

                if (document.getElementById('numLinea').value == null || document.getElementById('numLinea').value == '') {
                } else {
                    if (Trim(document.getElementById('numLinea').value) != '') {
                        if (/^([0-9])*$/.test(document.getElementById('numLinea').value)) {
                        } else {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                            return false;
                        }
                    }
                }

                if ((document.getElementById('numDiasSinIncidencias').value == null || document.getElementById('numDiasSinIncidencias').value == '') && (document.getElementById('numDiasIncidencia').value == null || document.getElementById('numDiasIncidencia').value == '')) {
                    var causa = document.getElementById('codListaCausaIncidencia').value;
                    if (document.getElementById('dni_nif').value != "99999999" && document.getElementById('dni_nif').value != "88888888" && causa != "ATR" && causa != "INC") {
                        var nuevo = "<%=nuevo%>";
                        if (nuevo != null && nuevo == "1") {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.diasObligatorios")%>';
                            return false;
                        }
                    }
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('numDiasSinIncidencias').value, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasSinIncidencias.errNumerico")%>';
                        return false;
                    }
                }

                if (document.getElementById('numDiasIncidencia').value == null || document.getElementById('numDiasIncidencia').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('numDiasIncidencia').value, 6, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasIncidencia.errNumerico")%>';
                        return false;
                    }
                }

                if (document.getElementById('importeSubvencion').value == null || document.getElementById('importeSubvencion').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('importeSubvencion').value, 7, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.importeSubvencion.errNumericoPre")%>';
                        return false;
                    }
                }

                if (document.getElementById('porcJornada').value == null || document.getElementById('porcJornada').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcJornada').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcJornada').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errRango")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('porcReduccion').value == null || document.getElementById('porcReduccion').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcReduccion').value, 5, 2)) {

                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcReduccion').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion.errRango")%>';
                            return false;
                        } else if (!validarPorcReduccion(document.getElementById('porcReduccion').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion.errValor")%>';
                            return false;
                        }
                    }
                }

                return correcto;
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;
                var campo;
                var numLinea = document.getElementById('numLinea').value;

                var doc = document.getElementById('dni_nif');
                if (doc.value == null || doc.value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                    return false;
                }
                if (!compruebaTamanoCampo(doc.value, 8)) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                    return false;
                }
                if (doc.value != "99999999" && doc.value != "88888888") {
                    if (!validarNif(doc)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                if (numLinea == null || numLinea == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                    //return false;
                } else {
                    if (!validarNumerico(document.getElementById('numLinea'))) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                        return false;
                    }
                    if (doc.value != "99999999" && doc.value != "88888888") { // 88888888 y 99999999 corresponde a lineas genericas no tienen nombre
                        campo = document.getElementById('nombre').value;
                        if (campo == null || campo == '') {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                            return false;
                        }
                        campo = document.getElementById('apellidos').value;
                        if (campo == null || campo == '') {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                            return false;
                        }
                    }
                }

                // si tiene NUM_DIAS_SIN obligatorios IMPORTE - POR JORNADA excluyente NUM_DIAS_INCIDENCIA
                campo = document.getElementById('numDiasSinIncidencias').value;
                if (campo == null || campo == '') {
                    // si no tiene dias SIN pero es generico, es obligatorio el importe
                    if (document.getElementById('dni_nif').value == "99999999" || document.getElementById('dni_nif').value == "88888888") {
                        campo = document.getElementById('importeSubvencion').value;
                        if (campo == null || campo == '') {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.importeSubvencion")%>';
                            return false;
                        }
                    }
                } else {
                    // con dias negativos No obligatorio importe
                    if (document.getElementById('numDiasSinIncidencias').value >= 0) {
                        campo = document.getElementById('importeSubvencion').value;
                        if (campo == null || campo == '') {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.importeSubvencion")%>';
                            return false;
                        }
                        if (document.getElementById('dni_nif').value != "99999999" && document.getElementById('dni_nif').value != "88888888") {
                            campo = document.getElementById('porcJornada').value;
                            if (campo == null || campo == '') {
                                mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada")%>';
                                return false;
                            }
                        }

                    }

                    campo = document.getElementById('numDiasIncidencia').value;
                    if (campo != null && campo != '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasIncidenciaNo")%>';
                        return false;
                    }
                }

                // si tiene NUM_DIAS INCIDENCIA obligatorios CAUSA % Reducc excluyente NUM_DIAS_SIN - IMPORTE
                campo = document.getElementById('numDiasIncidencia').value;
                if (campo == null || campo == '') {
                } else {
                    campo = document.getElementById('numDiasSinIncidencias').value;
                    if (campo != null && campo != '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numDiasSinIncidenciasNo")%>';
                        return false;
                    }
                    campo = document.getElementById('importeSubvencion').value;
                    if (campo != null && campo != '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.importeSubvencionNo")%>';
                        return false;
                    }
                    campo = document.getElementById('codListaCausaIncidencia').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.causaIncidencia")%>';
                        return false;
                    }
                }
                // en lineas genericas NO obligatorio POR_REDUCCION
                if (document.getElementById('dni_nif').value != "88888888" && document.getElementById('dni_nif').value != "99999999") {
                    campo = document.getElementById('porcReduccion').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcReduccion")%>';
                        return false;
                    }
                }
                return correcto;
            }

            function validarPorcReduccion(porcentaje) {
                try {
                    if (porcentaje == '15,00' || porcentaje == '15,0' || porcentaje == '15' || porcentaje == '25,00' || porcentaje == '25,0' || porcentaje == '25' || porcentaje == '0,00' || porcentaje == '0,0' || porcentaje == '0') {
                        return true;
                    } else {
                        return false;
                    }
                } catch (err) {
                    return false;
                }
            }

            function mostrarCalFechaAB(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecha").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecha', null, null, null, '', 'calFecha', '', null, null, null, null, null, null, null, null, evento);
            }

            function desbloquearCampos() {
                document.getElementById('dni_nif').disabled = false;
                document.getElementById('nombre').disabled = false;
                document.getElementById('apellidos').disabled = false;
                document.getElementById('importeSubvencion').disabled = false;
            }
        </script>   
    </head>
    <body>
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="width: 94%; clear: both; text-align: center; font-size: 14px;">
                        <span id="subtitulo"></span>
                    </div>                     
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 70%; float: left;">                        
                            <input id="numLinea" name="numLinea" type="text" class="inputTexto" size="10" maxlength="5" 
                                   value="<%=datModif != null && datModif.getNumLinea() != null ? datModif.getNumLinea() : ""%>" disabled/>                        
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.dniSMI")%>
                        </div>
                        <div style="width: 70%; float: left;">                        
                            <input id="dni_nif" name="dni_nif" type="text" class="inputTextoObligatorio" size="12" maxlength="10" onkeyup="xAMayusculas(this);"
                                   value="<%=datModif != null && datModif.getNif() != null ? datModif.getNif() : ""%>" disabled="true"
                                   title=" 88888888 para líneas con incremento.&#10 99999999 para líneas sin incremento."/>                        
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="50" 
                                   value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>" disabled="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="apellidos" name="apellidos" type="text" class="inputTextoObligatorio" size="30" maxlength="50" 
                                   value="<%=datModif != null && datModif.getApellidos() != null ? datModif.getApellidos() : ""%>" disabled="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numDiasSinIncidencias")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="numDiasSinIncidencias" name="numDiasSinIncidencias" type="text" class="inputTexto" size="10" maxlength="5" 
                                   value="<%=datModif != null && datModif.getNumDiasSinIncidencias() != null ? datModif.getNumDiasSinIncidencias().toString().replaceAll("\\.", ","): ""%>" 
                                   title="Días a subvencionar." onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numDiasIncidencia")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="numDiasIncidencia" name="numDiasIncidencia" type="text" class="inputTexto" size="10" maxlength="5" onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getNumDiasIncidencia() != null ? datModif.getNumDiasIncidencia().toString().replaceAll("\\.", ","): ""%>"
                                   title="Días sin subvención."  onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div class="etiqueta" style="width: 30%; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.causaIncidencia")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input type="text" name="codListaCausaIncidencia" id="codListaCausaIncidencia" size="4" maxlength="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaCausaIncidencia"  id="descListaCausaIncidencia" size="60" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaCausaIncidencia" name="anchorListaCausaIncidencia">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.importeSubvencion")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="importeSubvencion" name="importeSubvencion" type="text" class="inputTexto" size="10" maxlength="15" onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getImporteSolicitado() != null ? datModif.getImporteSolicitado().toString().replaceAll("\\.", ","): ""%>" disabled="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.importeSubvencionCalculo")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <div style="width: 15%; float: left;">
                                <input id="importeSubvencionCalculo" name="importeSubvencionCalculo" type="text" class="inputTexto" size="10" maxlength="15" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getImporteRecalculo() != null ? datModif.getImporteRecalculo().toString().replaceAll("\\.", ","): ""%>" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fecha")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input type="text" class="inputTxtFecha" 
                                   id="fecha" name="fecha"
                                   maxlength="10"  size="10"
                                   value="<%=fecha%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaAB(event);
                                    return false;" style="text-decoration: none;">
                                <IMG style="border: none" height="17" id="calFecha" name="calFecha" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.porcJornada")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="porcJornada" name="porcJornada" type="text" class="inputTexto" size="10" maxlength="5" title=""
                                   value="<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.porcReduccion")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <input id="porcReduccion" name="porcReduccion" type="text" class="inputTextoObligatorio" size="10" maxlength="5" title="VALORES ADMITIDOS:&#10 0, 15% o 25%"
                                   value="<%=datModif != null && datModif.getPorcReduccion() != null ? datModif.getPorcReduccion().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 20px">
                        <div style="width: 30%; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.observaciones")%>
                        </div>
                        <div style="width: 70%; float: left;">
                            <textarea name="observaciones" class="inputTexto" id="observaciones" maxLength="500" rows="6" cols="80"><%=datModif != null && datModif.getObservaciones() != null ? datModif.getObservaciones().toString().replaceAll("nnn","\n"): ""%></textarea>                            
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /*desplegable causa incidencia*/
                listaCodigosCausaIncidencia[0] = "";
                listaDescripcionesCausaIncidencia[0] = "";
                contador = 0;

                <logic:iterate id="causaIncidencia" name="listaCausaIncidencia" scope="request">
                listaCodigosCausaIncidencia[contador] = ['<bean:write name="causaIncidencia" property="des_val_cod" />'];
                listaDescripcionesCausaIncidencia[contador] = ['<bean:write name="causaIncidencia" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaCausaIncidencia = new Combo("ListaCausaIncidencia");
                comboListaCausaIncidencia.addItems(listaCodigosCausaIncidencia, listaDescripcionesCausaIncidencia);
                comboListaCausaIncidencia.change = cargarDatosCausaIncidencia;

                var nuevo = "<%=nuevo%>";
                if (nuevo == 0) {
                    rellenardatModificar();
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.modificar")%>";
                } else {
                    desbloquearCampos();
                    var linea = "<%=numLineaS%>";
                    document.getElementById('numLinea').value = linea;
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevo")%>";
                }

            </script>
        </div>
    </body>
</html>
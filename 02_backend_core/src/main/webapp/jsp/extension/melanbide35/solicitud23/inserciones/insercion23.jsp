<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaInsercionesECA23VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="java.math.*"%>
<%@page import="java.text.*"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    
    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }
    
//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();    
    String numExpediente    = request.getParameter("numero");
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);

    Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");
    EcaSolicitud23VO solicitud = (EcaSolicitud23VO)request.getAttribute("SolicitudECA23");
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
    

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript">
    var maxSeguimientos;
    var importeSeguimientos;
    var mensajeValidacion;

    function pulsarNuevoInsercionSolicitudEca() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoInsercionSolicitud23&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control=' + control.getTime(), 600, 850, 'no', 'no', function (result) {
            recarcargarTablaInserciones();
            actualizarImportesEca23();
        });
    }

    function pulsarEliminarInsercionSolicitudEca() {
        if (tablaInserciones.selectedIndex != -1) {
            if (jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>') == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var control = new Date();
                var lista = new Array();
                var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarInsercionSolicitud23&tipo=0&numero=<%=numExpediente%>&idInserc=' + listaInserciones[tablaInserciones.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", baseUrl, false);
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
                    var codigoOperacion = nodos[0].firstChild.textContent;
                    if (codigoOperacion == "0") {
                        recarcargarTablaInserciones();
                        actualizarImportesEca23();
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }//if
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }
            }
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function crearTablaInserciones() {
        tablaInserciones = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaInserciones'));
        tablaInserciones.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col0")%>'); // ID
        tablaInserciones.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col1")%>'); // num exp
        tablaInserciones.addColumna('250', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col2")%>'); // tipo disc
        tablaInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col3")%>'); // tipo sexo/edad
        tablaInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col4")%>'); // num personas
        tablaInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col5")%>'); // % trabajo
        tablaInserciones.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col6")%>'); // importe 1 año
        tablaInserciones.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.inserciones.tabla.col7")%>'); // importe

        tablaInserciones.displayCabecera = true;
        tablaInserciones.height = 300;
    }

    function actualizaPestanaSolicitud() {
        recarcargarTablaInserciones();
        recargarSeguimientosEca23();
        actualizarImportesEca23();
    }

    function recarcargarTablaInserciones() {
        tablaInserciones;
        listaInserciones = new Array();
        listaInsercionesTabla = new Array();
        var ajax = getXMLHttpRequest();
        var fila;
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=recargarInsercionSolicitud23&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
        try {
            ajax.open("POST", baseUrl, false);
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
            var result = extraerListadoInsercionesSolicitudEca(nodos);
            crearTablaInserciones();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaInserciones[i - 1] = fila;
                listaInsercionesTabla[i - 1] = fila;
            }
            tablaInserciones.lineas = listaInsercionesTabla;
            tablaInserciones.displayTabla();
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }

    }

    function extraerListadoInsercionesSolicitudEca(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaInserciones = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaInserciones[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
            else if (hijos[j].nodeName == "FILA") {
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for (var cont = 0; cont < hijosFila.length; cont++) {
                    if (hijosFila[cont].nodeName == "ID") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[0] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[0] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMEROEXPEDIENTE") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[1] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[1] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "TIPODISCAPACIDAD") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[2] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[2] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "TIPOSEXOEDAD") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[3] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[3] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMEROPERSONAS") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[4] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "PORCENTAJETRABAJO") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[5] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[5] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPORTECAL1ANO") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[6] = formatearNumero(nodoCampo.childNodes[0].childNodes[0].nodeValue);
                        } else {
                            fila[6] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPORTESOLICITUD") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[7] = formatearNumero(nodoCampo.childNodes[0].childNodes[0].nodeValue);
                        } else {
                            fila[7] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPORTETOTAL") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            valor = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                            document.getElementById('importeSolicitudInserciones').value = formatearNumero(valor);
                        }
                    } else if (hijosFila[cont].nodeName == "NUMEROPERSONASTOTAL") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            valor = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                            document.getElementById('totalPersonasInserciones').value = valor;
                            reemplazarPuntosEca(document.getElementById('totalPersonasInserciones'));
                        }
                    } else if (hijosFila[cont].nodeName == "TOTALIMPORTECAL1ANO") {
						nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            valor = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                            document.getElementById('totalImporteSolicitudUnAno').value = formatearNumero(valor);
                        }
                    }
                }
            }
            listaInserciones[j] = fila;
            fila = new Array();
        }
        return listaInserciones;
    }

    function calcularTotal() {
        if (validarImportes()) {
            var totIns = convertirANumero(document.getElementById('importeSolicitudInserciones').value);
            var totSeg = convertirANumero(document.getElementById('importeSolicitudSeguimiento').value);
            document.getElementById('totalSolicitud').value = formatearNumero(totIns + totSeg)+ ' \u20ac';
        } else {
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function calcularImporteSeguimiento() {
        var correcto = true;


        if (validarSeguimientos()) {
            if (parseInt(document.getElementById('numeroMujeres').value) + parseInt(document.getElementById('numeroHombres').value) > maxSeguimientos) {
                document.getElementById('numeroMujeres').value = '';
                document.getElementById('numeroHombres').value = '';
                document.getElementById('importeSolicitudSeguimiento').value = '';
                document.getElementById('totalPersonasSeguimiento').value = '';
                jsp_alerta("A", 'El numero maximo de seguimientos es ' + maxSeguimientos);
                correcto = false;
            }

            if (correcto) {
                document.getElementById('totalPersonasSeguimiento').value = parseInt(document.getElementById('numeroMujeres').value) + parseInt(document.getElementById('numeroHombres').value);
                document.getElementById('importeSolicitudSeguimiento').value = (parseInt(document.getElementById('numeroMujeres').value) * importeSeguimientos) + (parseInt(document.getElementById('numeroHombres').value) * importeSeguimientos);
                document.getElementById('importeSolicitudSeguimiento').value = formatearNumero(document.getElementById('importeSolicitudSeguimiento').value);
            }

        } else {
            jsp_alerta("A", mensajeValidacion);
        }

    }

    function validarImportes() {
        var correcto = true;
        if (document.getElementById('importeSolicitudInserciones').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('importeSolicitudInserciones'), 8, 2)) {
                    correcto = false;
                }
            } catch (err) {
                correcto = false;
            }
        } else {
            correcto = false;
        }
        if (document.getElementById('importeSolicitudSeguimiento').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('importeSolicitudSeguimiento'), 8, 2)) {
                    correcto = false;
                }
            } catch (err) {
                correcto = false;
            }
        } else {
            correcto = false;
        }
        return correcto;
    }

    function validarSeguimientos() {
        var correcto = true;
        mensajeValidacion = '';

        if (document.getElementById('numeroMujeres').value != '') {
            try {
                if (!validarNumericoEntero(document.getElementById('numeroMujeres'))) {
                    correcto = false;
                    document.getElementById('numeroMujeres').style.border = '1px solid red';
                    if (mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.seguimiento.numeroMujeres")%>';
                } else {
                    document.getElementById('numeroMujeres').style.border = '1px solid #ccc;';
                }
            } catch (err) {
                correcto = false;
                document.getElementById('numeroMujeres').style.border = '1px solid red';
                if (mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.seguimiento.numeroMujeres")%>';
            }
        } else {
            correcto = false;
            if (mensajeValidacion == '')
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.seguimiento.numeroMujeres")%>';
        }
        if (document.getElementById('numeroHombres').value != '') {
            try {
                if (!validarNumericoEntero(document.getElementById('numeroHombres'))) {
                    correcto = false;
                    document.getElementById('numeroHombres').style.border = '1px solid red';
                    if (mensajeValidacion == '') {
                        mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.solicitud.seg1HIncorrecto"), anoExpediente)%>';
                    }
                } else {
                    document.getElementById('numeroHombres').style.border = '1px solid #ccc;';

                }
            } catch (err) {
                correcto = false;
                document.getElementById('numeroHombres').style.border = '1px solid red';
                if (mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.seguimiento.numeroHombres")%>';
            }
        } else {
            correcto = false;
            if (mensajeValidacion == '')
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.seguimiento.numeroHombres")%>';
        }
        return correcto;
    }

    function guardarSeguimientosEca23() {
        if (validarSeguimientos()) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarSeguimientoSolicitud23&tipo=0&numero=<%=numExpediente%>'
                    + '&numeroExpediente=<%=numExpediente%>'
                    + '&numeroMujeres=' + parseInt(document.getElementById('numeroMujeres').value)
                    + '&numeroHombres=' + parseInt(document.getElementById('numeroHombres').value)
                    + '&importeSeguimiento=' + document.getElementById('importeSolicitudSeguimiento').value
                    + '&control=' + control.getTime();
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
                var codigoOperacion = nodos[0].firstChild.textContent;

                //for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", msgValidacion);
                } else if (codigoOperacion == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifPreparadorRepetido")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }
            recargarSeguimientosEca23();
            actualizarImportesEca23();
        } else {
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }

    function recargarSeguimientosEca23() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=recargarSolicitud23&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
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
            extraerSeguimientosEca23(nodos);
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }
    }

    function extraerSeguimientosEca23(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var valor = 0;

        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
            if (hijos[j].nodeName == "MUJERESSEG") {
                valor = hijos[j].childNodes[0].childNodes[0].nodeValue;
                document.getElementById('numeroMujeres').value = valor;
            }
            if (hijos[j].nodeName == "HOMBRESSEG") {
                valor = hijos[j].childNodes[0].childNodes[0].nodeValue;
                document.getElementById('numeroHombres').value = valor;
            }
            if (hijos[j].nodeName == "TOTALNUMEROPERSONAS") {
                valor = hijos[j].childNodes[0].childNodes[0].nodeValue;
                document.getElementById('totalPersonasSeguimiento').value = valor;
            }
            if (hijos[j].nodeName == "IMPORTESOLICSEG") {
                valor = hijos[j].childNodes[0].childNodes[0].nodeValue;
                document.getElementById('importeSolicitudSeguimiento').value = formatearNumero(valor);
            }
        }
    }

    function actualizarImportesEca23() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=actualizarImportesEca23&tipo=0&numero=<%=numExpediente%>'
                + '&control=' + control.getTime();
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
            var codigoOperacion = nodos[0].firstChild.textContent;

            //for(j=0;hijos!=null && j<hijos.length;j++)
            if (codigoOperacion == "0") {
                extraerImporteEca23(nodos);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            } else {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
    }

    function extraerImporteEca23(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var valor = 0;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "TOTAL") {
                valor = hijos[j].childNodes[0].nodeValue;
                document.getElementById('totalSolicitud').value = formatearNumero(valor)+ ' \u20ac';
            }
        }
    }
    
    function callFromTableToEca(rowID, tableName) {
    }
</script>
<body>
    <div class="tab-page" id="tabPage3510" style="height:520px; width: 98%;">   
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloInsercion"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.objetivosInsercion")%></span>
        </div>    
        <fieldset id="insercionesSol23" name="insercionesSol23">
            <h2 class="legendAzul" id="legendSegimiento" style="text-transform: uppercase;width: 90%;text-align: center;">
                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.inserciones")%>
            </h2>
            <div>
                <div id="listaInserciones" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            </div>
            <div class="lineaFormulario" style="/*margin-top: 15px;*/ width: 98%" >
                <div style="width: 40%; text-align: right; float: left;" class="etiqueta">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud23.inserciones.totalPersonas")%>
                </div>
                <div style="width: 10%; text-align: right; float: left;">
                    <input disabled type="text" id="totalPersonasInserciones" name="totalPersonasInserciones" size="10" maxlength="10" class="inputTexto" style="text-align: center"/>
                </div>
                <div style="width: 15%; text-align: right; float: left;"  class="etiqueta">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud23.inserciones.totalimporte1ano")%>
                </div>
                <div style="width: 10%; text-align: right; float: left;">
                    <input disabled type="text" id="totalImporteSolicitudUnAno" name="totalImporteSolicitudUnAno" size="10" maxlength="10" style="text-align: right" class="inputTexto"/>
                </div>
                <div style="width: 15%; text-align: right; float: left;"  class="etiqueta">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud23.inserciones.importe")%>
                </div>
                <div style="width: 10%; text-align: right; float: left;">
                    <input disabled type="text" id="importeSolicitudInserciones" name="importeSolicitudInserciones" size="10" maxlength="10" style="text-align: right" class="inputTexto"/>
                </div>
            </div>
            <div class="botonera" style="text-align: center; margin-top: 15px; padding: 20px;">
                <input  type="button" id="btnNuevoInsercion" name="btnNuevoInsercion" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.inserciones.nuevo")%>" onclick="pulsarNuevoInsercionSolicitudEca();">
                <input  type="button" id="btnBorrarInsercion" name="btnBorrarInsercion" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.inserciones.eliminar")%>" onclick="pulsarEliminarInsercionSolicitudEca();">
            </div>
        </fieldset>
        <fieldset id="seguimientosSol23" name="seguimientosSol23">
            <h2 class="legendAzul" id="legendSegimiento" style="text-transform: uppercase;width: 90%;text-align: center;">
                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.seguimiento")%>
            </h2>
            <fieldset style="padding-left: 25%;">
                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 20%; float: left;" class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.seguimientos.hombres")%>
                    </div>
                    <div style="width: 20%;  float: left;">
                        <input  onfocusout="calcularImporteSeguimiento()" type="text" id="numeroHombres" name="numeroHombres" size="10" maxlength="10" style="text-align: center;" value="" class="inputTextoObligarorio"/>
                    </div>
                    <div style="width: 20%; float: left;" class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.seguimientos.totalPersonas")%>
                    </div>
                    <div style="width: 20%; float: left;">
                        <input disabled type="text" id="totalPersonasSeguimiento" name="totalPersonasSeguimiento" size="10" maxlength="10"  value="" style="text-align: center;" class="inputTexto"/>
                    </div>
                </div> 
                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 20%; float: left;" class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.seguimientos.mujeres")%>
                    </div>
                    <div style="width: 20%; float: left;">
                        <input  onfocusout="calcularImporteSeguimiento()" type="text" id="numeroMujeres" name="numeroMujeres" size="10" maxlength="10" style="text-align: center;" value="" class="inputTextoObligarorio"/>
                    </div>

                    <div style="width: 20%; float: left;" class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.seguimientos.importe")%>
                    </div>
                    <div style="width: 20%; float: left;">
                        <input disabled type="text" id="importeSolicitudSeguimiento" name="importeSolicitudSeguimiento" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto"/>
                    </div>
                </div>
            </fieldset>
            <div class="botonera" style="padding-top: 20px;">
                <input type="button" id="btnGuardarSeg" name="btnGuardarSeg" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.seguimientos.guardar")%>" onclick="guardarSeguimientosEca23();">
            </div>  
        </fieldset>
        <fieldset id="resumenSol23" name="resumenSol23">
            <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
                <span id="subtituloResumen"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.importe")%></span>
            </div>
            <div style=" padding-left: 33%; width: 33%">

                <div class="lineaFormulario">
                    <div style="width: 50%; float: left;" class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.total")%>
                    </div>
                    <div style="padding-left: 20px; float: left;">
                        <input  disabled type="text" id="totalSolicitud" name="totalSolicitud" size="10" maxlength="10" style="text-align: right;" value="<%=solicitud.getCuantiaSolicitada()%>" class="inputTexto"/>
                    </div>
                </div>

            </div>
        </fieldset>
    </div>
    <script  type="text/javascript">
        <%
            if (solicitud != null){
        %>
        document.getElementById('totalPersonasInserciones').value = '<%=solicitud.getTotalNumeroPersonasInsercion() != null ? solicitud.getTotalNumeroPersonasInsercion().toString() : "" %>';
        document.getElementById('importeSolicitudInserciones').value = '<%=solicitud.getTotalImporteInserciones() != null ?  formateador.format(solicitud.getTotalImporteInserciones()) : "" %> ';
        document.getElementById('totalImporteSolicitudUnAno').value = '<%=solicitud.getTotalImporteCalculadoUnAnio() != null ? formateador.format(solicitud.getTotalImporteCalculadoUnAnio()) : "" %>';

        reemplazarPuntosEca(document.getElementById('totalPersonasInserciones'));
        //  reemplazarPuntosEca(document.getElementById('importeSolicitudInserciones'));
        document.getElementById('numeroHombres').value = '<%=solicitud.getHombresSeguimiento() != null ? solicitud.getHombresSeguimiento().toString() : "" %>';
        document.getElementById('numeroMujeres').value = '<%=solicitud.getMujeresSeguimiento() != null ? solicitud.getMujeresSeguimiento().toString() : "" %>';
        document.getElementById('totalPersonasSeguimiento').value = '<%=solicitud.getTotalNumeroPersonas() != null ? solicitud.getTotalNumeroPersonas().toString() : "" %>';
        document.getElementById('importeSolicitudSeguimiento').value = '<%=solicitud.getImporteSolicitadoSeguimiento() != null ? formateador.format(solicitud.getImporteSolicitadoSeguimiento()) : "" %>';
        document.getElementById('totalSolicitud').value = '<%=solicitud.getCuantiaSolicitada() != null ? formateador.format(solicitud.getCuantiaSolicitada()) : "" %>'+ ' \u20ac';
        <%            
                }
            if (importesConfiguracion!= null) {
        %>
        maxSeguimientos = <%=importesConfiguracion.getMaximoSeguimientos()%>;
        importeSeguimientos =<%=importesConfiguracion.getImporteSeguimientos()%>;
        <%            
            }
        %>
        var tablaInserciones;
        var listaInserciones = new Array();
        var listaInsercionesTabla = new Array();

        crearTablaInserciones();

        <%
            List<FilaInsercionesECA23VO> solList = (List<FilaInsercionesECA23VO>)request.getAttribute("listaInsercionesSolicitud");
            FilaInsercionesECA23VO act = null;
            if (solList!= null && solList.size() >0) {
                for (int i = 0; i <solList.size(); i++) {
                    act = solList.get(i);
        %>
        listaInserciones[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getTipoDiscapacidad()%>', '<%=act.getTipoSexoEdad()%>',
            '<%=act.getNumeroPersonas()%>', '<%=act.getPorcentajeTrabajo()%>', '<%=act.getImporteCalculadoUnAnio()%>', '<%=act.getImporteSolicitado()%>'];
        listaInsercionesTabla[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getTipoDiscapacidad()%>', '<%=act.getTipoSexoEdad()%>',
            '<%=act.getNumeroPersonas()%>', '<%=act.getPorcentajeTrabajo()%>', '<%=formateador.format(act.getImporteCalculadoUnAnio())%>'+ ' \u20ac', '<%=formateador.format(act.getImporteSolicitado())%>'+ ' \u20ac'];
        <%            
                }
            }
        %>
        tablaInserciones.lineas = listaInsercionesTabla;
        tablaInserciones.displayTabla();
    </script>
</body>
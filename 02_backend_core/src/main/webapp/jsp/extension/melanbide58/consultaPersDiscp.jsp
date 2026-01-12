<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DiscapacitadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();          
            String dniCeesc = "";
            String dniP = "";
            String nombreP = "";
            String apeP = "";
            String tipoDiscP = "";
            String gradoP = "";
            String severaP = "";
            String codOrganizacion = "";
            String expediente = "";
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
                Config m_Config = ConfigServiceHelper.getConfig("common");
                String statusBar = m_Config.getString("JSP.StatusBar");
                codOrganizacion  = request.getParameter("codOrganizacionModulo"); 
                expediente = (String)request.getAttribute("expediente");
                if (request.getAttribute("dniLimpio")!= null){
                    dniP = (String)request.getAttribute("dniLimpio");
                    dniCeesc = (String)request.getAttribute("dniCeesc");
                    
                }                
            }
            catch(Exception ex) {
            }
          
            String tituloPagina = meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.tituloPagina");                  
        %>
        <title><%=tituloPagina%></title>
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>    
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            var mensajeValidacion = '';

            var idSeleccionada = 0;
            var cambioPendiente = false;

            var comboListaTipoDiscapacidad;
            var listaCodigosTipoDiscapacidad = new Array();
            var listaDescripcionesTipoDiscapacidad = new Array();

            var comboListaEsSevera;
            var listaCodigosEsSevera = new Array();
            var listaDescripcionesEsSevera = new Array();

            var comboListaValidez;
            var listaCodigosValidez = new Array();
            var listaDescripcionesValidez = new Array();

            var comboListaTerritorio;
            var listaCodigosTerritorio = new Array();
            var listaDescripcionesTerritorio = new Array();

            var comboListaTipoDiscapacidadMan;
            var listaCodigosTipoDiscapacidadMan = new Array();
            var listaDescripcionesTipoDiscapacidadMan = new Array();

            var comboListaEsSeveraMan;
            var listaCodigosEsSeveraMan = new Array();
            var listaDescripcionesEsSeveraMan = new Array();

            var comboListaValidezMan;
            var listaCodigosValidezMan = new Array();
            var listaDescripcionesValidezMan = new Array();


            function buscaCodigoTipoDiscapacidad(codTipoDiscapacidad) {
                comboListaTipoDiscapacidad.buscaCodigo(codTipoDiscapacidad);
            }

            function cargarDatosTipoDiscapacidad() {
                var codTipoDiscapacidadSeleccionado = document.getElementById("codListaTipoDiscapacidad").value;
                buscaCodigoTipoDiscapacidad(codTipoDiscapacidadSeleccionado);
            }

            function buscaCodigoEsSevera(codEsSevera) {
                comboListaEsSevera.buscaCodigo(codEsSevera);
            }

            function cargarDatosEsSevera() {
                var codTipoEsSeveraSeleccionado = document.getElementById("codListaEsSevera").value;
                buscaCodigoEsSevera(codTipoEsSeveraSeleccionado);
            }

            function buscaCodigoValidez(codValidez) {
                comboListaValidez.buscaCodigo(codValidez);
            }

            function cargarDatosValidez() {
                var codTipoValidezSeleccionado = document.getElementById("codListaValidez").value;
                buscaCodigoValidez(codTipoValidezSeleccionado);
            }

            function buscaCodigoTerritorio(codTerritorio) {
                comboListaTerritorio.buscaCodigo(codTerritorio);
            }

            function cargarDatosTerritorio() {
                var codTerritorioSeleccionado = document.getElementById("codListaTerritorio").value;
                buscaCodigoTerritorio(codTerritorioSeleccionado);
            }

            function buscaCodigoTipoDiscapacidadMan(codTipoDiscapacidadMan) {
                comboListaTipoDiscapacidadMan.buscaCodigo(codTipoDiscapacidadMan);
            }

            function cargarDatosTipoDiscapacidadMan() {
                var codTipoDiscapacidadSeleccionadoMan = document.getElementById("codListaTipoDiscapacidadMan").value;
                buscaCodigoTipoDiscapacidadMan(codTipoDiscapacidadSeleccionadoMan);
            }

            function buscaCodigoEsSeveraMan(codEsSeveraMan) {
                comboListaEsSeveraMan.buscaCodigo(codEsSeveraMan);
            }

            function cargarDatosEsSeveraMan() {
                var codTipoEsSeveraSeleccionadoMan = document.getElementById("codListaEsSeveraMan").value;
                buscaCodigoEsSeveraMan(codTipoEsSeveraSeleccionadoMan);
            }

            function buscaCodigoValidezMan(codValidezMan) {
                comboListaValidezMan.buscaCodigo(codValidezMan);
            }

            function cargarDatosValidezMan() {
                var codTipoValidezSeleccionadoMan = document.getElementById("codListaValidezMan").value;
                buscaCodigoValidezMan(codTipoValidezSeleccionadoMan);
                comprobarValidezMan();
                comprobarOid();
            }


            function comprobarValidezMan() {
                var estado = 'off';
                var codigoVal = document.getElementById("codListaValidezMan").value;
                if (codigoVal == "T") {
                    estado = 'on';
                } else {
                    document.getElementById("fechaCaducidad").value = '';
                }
                elementoVisible(estado, 'caducidadCertificado');
            }

            function comprobarOid() {
                if (document.getElementById('oidCertificado').value != '') {
                    document.getElementById('btnDescargarCertificado').disabled = false;
                } else {
                    document.getElementById('btnDescargarCertificado').disabled = true;
                }
            }

            function buscar() {
                mensajeValidacion = "";
                if (validarCamposBusqueda()) {
                    //  document.getElementById('msgBuscando').style.display = "inline";
                    elementoVisible('on', 'barraProgresoPersDiscp');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarPersonaDiscp&tipo=0'
                            + '&dni=' + document.getElementById('txtDni').value
                            + '&nombre=' + document.getElementById('txtNombre').value
                            + '&apellidos=' + document.getElementById('txtApellidos').value
                            + '&tipoD=' + document.getElementById('codListaTipoDiscapacidad').value
                            + '&grado=' + document.getElementById('txtGrado').value
                            + '&severa=' + document.getElementById('codListaEsSevera').value
                            + '&validez=' + document.getElementById('codListaValidez').value
                            + '&centro=' + document.getElementById('txtCentro').value
                            + '&territorio=' + document.getElementById('codListaTerritorio').value
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
                                    } else if (hijosFila[cont].nodeName == "DNI") {
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
                                    } else if (hijosFila[cont].nodeName == "TIPODISC") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESCTIPODISC") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PORCDISC") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[6].toString();
                                            tex = tex.replace(".", ",");
                                            fila[6] = tex;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHEMISION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHRESOLUCION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "VALIDEZ") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESCVALIDEZ") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHCADUCIDAD") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "SEVERA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESCSEVERA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHVALIDACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[14] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHBAJA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[15] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "OID_CERTIFICADO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[16] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NOMBRE_CERTIFICADO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[17] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CENTRO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[18] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TERRITORIO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[19] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESC_TERRITORIO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[20] = '-';
                                        }
                                    }
                                }
                                lista[j] = fila;
                                fila = new Array();
                            }
                        }
                        if (codigoOperacion == "0") {
                            //jsp_alerta("A",'Correcto');
                            recargarTablaBusqueda(lista);
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarPersonaDisc")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.modificarAccesoSeleccionado")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.grabandoCompleto")%>');
                        } else if (codigoOperacion == "5") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.validando")%>');
                        } else if (codigoOperacion == "6") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.grabandoBaja")%>');
                        } else if (codigoOperacion == "7") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.anioVacio")%>');
                        } else if (codigoOperacion == "8") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.recalculoSMI")%>');
                        } else if (codigoOperacion == "9") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.notFoundSMI")%>');
                        } else if (codigoOperacion == "10") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarRecalculoSMI")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }

                    } catch (Err) {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
                elementoVisible('off', 'barraProgresoPersDiscp');
            }

            function modificar() {
                if (tablaResultados.selectedIndex != -1) {
                    limpiarFormulario();
                    limpiarMantenimiento();
                    rellenarMantenimiento();
                    document.getElementById("txtDniMan").disabled = true;
                    cambioPendiente = true;
                    //    elementoVisible('on', 'fieldsetMantenimiento');
                } else {
                    jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function guardar() {
                mensajeValidacion = "";
                if (validarCamposMantenimiento()) {
                    elementoVisible('on', 'barraProgresoPersDiscp');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var nombre = document.getElementById('txtNombreMan').value.replace(/\'/g, "''");
                    var ape = document.getElementById('txtApellidosMan').value.replace(/\'/g, "''");
                    var numexp = '<%=expediente%>';

                    parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarDiscapacitado&tipo=0"
                            + "&id=" + idSeleccionada
                            + "&dni=" + document.getElementById('txtDniMan').value
                            + '&nombre=' + nombre
                            + '&apellidos=' + ape
                            + '&tipoD=' + document.getElementById('codListaTipoDiscapacidadMan').value
//                            + '&subtipo=' + document.getElementById('codListaSubtipoMan').value
                            + '&grado=' + document.getElementById('txtGradoMan').value
                            + '&fecEmision=' + document.getElementById('fechaEmision').value
                            + '&fecResolucion=' + document.getElementById('fechaResolucion').value
                            + '&validez=' + document.getElementById('codListaValidezMan').value
                            + '&fecCaducidad=' + document.getElementById('fechaCaducidad').value
                            + '&severa=' + document.getElementById('codListaEsSeveraMan').value
                            + '&fecValidacion=' + document.getElementById('fechaValidacion').value
                            + '&fecBaja=' + document.getElementById('fechaBaja').value
                            + '&oid=' + document.getElementById('oidCertificado').value
                            + '&nombreCertificado=' + document.getElementById('nombreCertificado').value
                            + '&expediente=<%=expediente%>'
                            ;

                    try {
                        ajax.open("POST", baseUrl, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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

                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                lista[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                        }
                        elementoVisible('off', 'barraProgresoPersDiscp');
                        if (codigoOperacion == "0") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.guardadoOK")%>');
                            cambioPendiente = false;
                            // limpiarMantenimiento();
                            buscar();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function pulsarCargarCertificado() {
                var hayFicheroSeleccionado = false;
                if (idSeleccionada == 0 && cambioPendiente) {
                    var resultado = jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.registrarPersona")%>');
                    return false;
                }

                if (document.getElementById('certificado_pdf').files) {
                    if (document.getElementById('certificado_pdf').files[0]) {
                        hayFicheroSeleccionado = true;
                    }
                } else if (document.getElementById('certificado_pdf').value != '') {
                    hayFicheroSeleccionado = true;
                }

                if (hayFicheroSeleccionado) {
                    var extension = document.getElementById('certificado_pdf').value.split('.').pop().toLowerCase();
                    if (extension != 'pdf') {
                        var resultado = jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                        return false;
                    }
                    if (!validarCaracteresCertificado(document.getElementById('certificado_pdf').value)) {
                        var resultado = jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tamanoCertificado")%>');
                        return false;
                    }
                    var niPersona = document.getElementById('txtDniMan').value;
                    if (!niPersona || niPersona == '') {
                        var resultado = jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>');
                        return false;
                    }
                    var importar;
                    if (document.getElementById('oidCertificado').value != '') {
                        importar = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                    } else {
                        importar = 1;
                    }

                    if (importar == 1) {
                        elementoVisible('on', 'barraProgresoPersDiscp');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=grabarCertificado&tipo=0&idPersona=' + niPersona + '&idRegistro=' + idSeleccionada + '&expediente=<%=expediente%>';
                        document.forms[0].action = baseUrl + '?' + parametros;
                        document.forms[0].enctype = 'multipart/form-data';
                        document.forms[0].encoding = 'multipart/form-data';
                        document.forms[0].method = 'POST';
                        document.forms[0].target = 'uploadFrameCarga';
                        document.forms[0].submit();
                        comprobarOid();
                        limpiarFormulario();
                        cambioPendiente = true;
                        elementoVisible('off', 'barraProgresoPersDiscp');
                    }
                    return false;
                } else {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                    return false;
                }
            }

            function pulsarDescargarCertificado() {
                if (document.getElementById('oidCertificado').value != '') {
                    var niPersona = document.getElementById('txtDniMan').value;
                    if (!niPersona || niPersona == '') {
                        var resultado = jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>');
                        return false;
                    } else {
                        if (!validarNif(document.getElementById('txtDniMan'))) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                            return false;
                        }
                    }
                    elementoVisible('on', 'barraProgresoPersDiscp');
                    var control = new Date();
                    window.open('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=descargarCertificado&tipo=0&nuevo=0&oidCertificado='
                            + document.getElementById('oidCertificado').value
                            + '&control=' + control.getTime(), 'ventana1',
                            'left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes');
                    elementoVisible('off', 'barraProgresoPersDiscp');
                    return false;
                } else {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.sinCertificado")%>');
                    return false;
                }
            }

            function actualizarCertificado() {
                try {
                    limpiarFormulario();
                    comprobarOid();
                    buscar();
                    //      cambioPendiente = false;
                } catch (err) {
                }
                elementoVisible('off', 'barraProgresoPersDiscp');
            }

            function rellenaOid(idDocumento) {
                document.getElementById('oidCertificado').value = idDocumento;
            }

            function rellenarNombreCert(nomDocumento) {
                document.getElementById('nombreCertificado').value = nomDocumento;
            }

            function crearTabla() {
                tablaResultados = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaResultados'), 1170);
                tablaResultados.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col0")%>"); // id Registro
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col1")%>"); // dni
                tablaResultados.addColumna('80', 'left', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col2")%>"); // nombre
                tablaResultados.addColumna('80', 'left', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col3")%>"); // apellidos
                tablaResultados.addColumna('45', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col4")%>"); // tipoD
                tablaResultados.addColumna('60', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col5")%>"); // grado
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col6")%>"); // Fecha emisión
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col7")%>"); // Fecha Resolución
                tablaResultados.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col8")%>"); // Validez
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col9")%>"); // Fecha caducidad
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col10")%>"); // severa
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col11")%>"); // Fecha validación
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col12")%>"); // Fecha de baja
                tablaResultados.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col13")%>"); // OID
                tablaResultados.addColumna('90', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col14")%>"); // nombre Doc
                tablaResultados.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col16")%>"); // centro
                tablaResultados.addColumna('40', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"consPersDiscp.tablaResultados.col17")%>"); // territorio

                tablaResultados.displayCabecera = true;
                tablaResultados.height = 200;
            }

            function recargarTablaBusqueda(result) {
                var fila;
                listaResultados = new Array();
                listaResultadosTabla = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    listaResultados[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20]];
                    listaResultadosTabla[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[6], fila[7], fila[8], fila[9], fila[11], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19]];
                }
                crearTabla();
                tablaResultados.lineas = listaResultadosTabla;
                tablaResultados.displayTabla();
                document.getElementById('lblNumResultados').innerText = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.lblResultados1")%> ' + (result.length - 1) + ' <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.lblResultados2")%>';
            }

            function limpiarTablaBusqueda() {
                try {
                    document.getElementById('lblNumResultados').innerText = '';
                    tablaResultados.lineas = new Array();
                    tablaResultados.displayTabla();
                } catch (Err) {
                    alert("Err.description: " + Err.description);
                }
            }

            function rellenarPersonaBusqueda() {
                document.getElementById('txtDni').value = '<%=dniP%>';
            }

            function limpiarBuscador() {
                document.getElementById('txtDni').value = '';
                document.getElementById('txtNombre').value = '';
                document.getElementById('txtApellidos').value = '';
                buscaCodigoTipoDiscapacidad("");
                document.getElementById('txtGrado').value = '';
                buscaCodigoEsSevera("");
                buscaCodigoValidez("");
                document.getElementById('txtCentro').value = '';
                buscaCodigoTerritorio("");
                limpiarTablaBusqueda();
                limpiarMantenimiento();
            }

            function rellenarMantenimiento() {
                idSeleccionada = listaResultados[tablaResultados.selectedIndex][0];
                //  jsp_alerta('A', idSeleccionada);
                document.getElementById('txtDniMan').value = listaResultados[tablaResultados.selectedIndex][1];
                document.getElementById('txtNombreMan').value = listaResultados[tablaResultados.selectedIndex][3];
                document.getElementById('txtApellidosMan').value = listaResultados[tablaResultados.selectedIndex][2];
                var codigo;
                codigo = listaResultados[tablaResultados.selectedIndex][4];
                buscaCodigoTipoDiscapacidadMan(codigo);

                document.getElementById('txtGradoMan').value = listaResultados[tablaResultados.selectedIndex][6];
                codigo = listaResultados[tablaResultados.selectedIndex][12];
                buscaCodigoEsSeveraMan(codigo);
                if (listaResultados[tablaResultados.selectedIndex][7] != "-") {
                    document.getElementById('fechaEmision').value = listaResultados[tablaResultados.selectedIndex][7];
                }
                if (listaResultados[tablaResultados.selectedIndex][8] != "-") {
                    document.getElementById('fechaResolucion').value = listaResultados[tablaResultados.selectedIndex][8];
                }
                codigo = listaResultados[tablaResultados.selectedIndex][9];
                buscaCodigoValidezMan(codigo);
                if (listaResultados[tablaResultados.selectedIndex][11] != "-") {
                    document.getElementById('fechaCaducidad').value = listaResultados[tablaResultados.selectedIndex][11];
                }
                if (listaResultados[tablaResultados.selectedIndex][14] != "-") {
                    document.getElementById('fechaValidacion').value = listaResultados[tablaResultados.selectedIndex][14];
                }
                if (listaResultados[tablaResultados.selectedIndex][15] != "-") {
                    document.getElementById('fechaBaja').value = listaResultados[tablaResultados.selectedIndex][15];
                }
                if (listaResultados[tablaResultados.selectedIndex][16] != "-") {
                    document.getElementById('oidCertificado').value = listaResultados[tablaResultados.selectedIndex][16];
                }
                if (listaResultados[tablaResultados.selectedIndex][17] != "-") {
                    document.getElementById('nombreCertificado').value = listaResultados[tablaResultados.selectedIndex][17];
                }
                comprobarOid();
            }

            function limpiarMantenimiento() {
                document.getElementById('txtDniMan').value = "";
                document.getElementById('txtNombreMan').value = "";
                document.getElementById('txtApellidosMan').value = "";
                buscaCodigoTipoDiscapacidadMan("");
                document.getElementById('txtGradoMan').value = "";
                buscaCodigoEsSeveraMan("");
                document.getElementById('fechaEmision').value = "";
                document.getElementById('fechaResolucion').value = "";
                buscaCodigoValidezMan("");
                document.getElementById('fechaCaducidad').value = "";
                document.getElementById('fechaValidacion').value = "";
                document.getElementById('fechaBaja').value = "";
                document.getElementById('oidCertificado').value = "";
                document.getElementById('nombreCertificado').value = "";
                comprobarOid();
            }

            function limpiarFormulario() {
                $fileupload = $('#certificado_pdf');
                $fileupload.replaceWith($fileupload.clone(true));
            }

            function pulsarExportarConsultaDiscp() {
                elementoVisible('on', 'barraProgresoPersDiscp');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = '';
                parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelPersDiscp&tipo=0&numExp=<%=expediente%>'
                        + '&dni=' + document.getElementById('txtDni').value
                        + '&nombre=' + document.getElementById('txtNombre').value
                        + '&apellidos=' + document.getElementById('txtApellidos').value
                        + '&tipoD=' + document.getElementById('codListaTipoDiscapacidad').value
                        + '&grado=' + document.getElementById('txtGrado').value
                        + '&severa=' + document.getElementById('codListaEsSevera').value
                        + '&validez=' + document.getElementById('codListaValidez').value
                        + '&centro=' + document.getElementById('txtCentro').value
                        + '&territorio=' + document.getElementById('codListaTerritorio').value;
                window.open(url + parametros, "_blank");
                elementoVisible('off', 'barraProgresoPersDiscp');
            }

            function aceptar() {
                if (cambioPendiente) {
                    var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCambioPendiente")%>');
                    if (resultado == 1) {
                        self.parent.opener.retornoXanelaAuxiliar("0");
                        cerrarVentana();
                    }
                } else {
                    self.parent.opener.retornoXanelaAuxiliar("0");
                    cerrarVentana();
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    self.parent.opener.retornoXanelaAuxiliar("1");
                    cerrarVentana();
                }
            }

            function validarCamposBusqueda() {
                mensajeValidacion = "";
                var correcto = true;
                if (document.getElementById('txtNombreMan').value) {

                }
                if (document.getElementById('txtApellidosMan').value) {
                    if (!validarTresCaracteresApellido(document.getElementById('txtApellidosMan').value)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellidos.errLongitud")%>';
                        return false;
                    }
                }
                if (document.getElementById('txtGradoMan').value) {
                    if (!validarNumericoDecimalPrecision(document.getElementById('txtGradoMan').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else if (!validarNumericoPorcentajeCien(document.getElementById('txtGradoMan').value)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                        return false;
                    }

                }
                if (document.getElementById('txtCentro').value) {
                    if (!validarTresCaracteresApellido(document.getElementById('txtCentro').value)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.centro.errLongitud")%>';
                        return false;
                    }
                }
                return correcto;
            }

            function validarCamposMantenimiento() {
                mensajeValidacion = "";

                if (!document.getElementById('txtDniMan').value) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                } else if (!validarNif(document.getElementById('txtDniMan'))) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                    return false;
                }

                if (!document.getElementById('txtNombreMan').value) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }

                if (!document.getElementById('txtApellidosMan').value) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                } else if (!validarTresCaracteresApellido(document.getElementById('txtApellidosMan').value)) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellidos.errLongitud")%>';
                    return false;
                }

                if (!document.getElementById('codListaTipoDiscapacidadMan').value || document.getElementById('codListaTipoDiscapacidadMan').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoDis")%>';
                    return false;
                } else if (!document.getElementById('codListaTipoDiscapacidadMan').value == 'PA' && !document.getElementById('codListaTipoDiscapacidadMan').value == 'PG' && !document.getElementById('codListaTipoDiscapacidadMan').value == 'PT') {
                    if (!document.getElementById('txtGradoMan').value || document.getElementById('txtGradoMan').value == '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado")%>';
                        return false;
                    } else if (!validarNumericoDecimalPrecision(document.getElementById('txtGradoMan').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else if (!validarNumericoPorcentajeCien(document.getElementById('txtGradoMan').value)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                        return false;
                    }
                }

                if (!document.getElementById('fechaEmision').value || document.getElementById('fechaEmision').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaEmision")%>';
                    return false;
                }

                if (!document.getElementById('fechaResolucion').value || document.getElementById('fechaResolucion').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaResolucion")%>';
                    return false;
                }

                if (!document.getElementById('codListaValidezMan').value || document.getElementById('codListaValidezMan').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.validezObligatorio")%>';
                    return false;
                } else if (document.getElementById('codListaValidezMan').value == 'T') {
                    if (!document.getElementById('fechaCaducidad').value || document.getElementById('fechaCaducidad').value == '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaCaducidad")%>';
                        return false;
                    }
                }

                if (!document.getElementById('fechaValidacion').value || document.getElementById('fechaValidacion').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaValidacion")%>';
                    return false;
                }
                return true;
            }

            function reemplazarPuntos(campo) {
                try {
                    var valor = campo.value;
                    if (valor != null && valor != '') {
                        valor = valor.replace(/\./g, ',');
                        campo.value = valor;
                    }
                } catch (err) {
                }
            }

            function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide

            function validarCaracteresCertificado(certificado) {
                try {
                    var numCarac;
                    if (certificado == null || certificado == "") {
                        return true;
                    } else {
                        if (Trim(certificado) != "") {
                            numCarac = certificado.length;
                            if (numCarac > 84) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarTresCaracteresApellido(apellido) {
                try {
                    var numCarac;
                    if (apellido == null || apellido == "") {
                        return true;
                    } else {
                        if (Trim(apellido) != "") {
                            numCarac = apellido.length;
                            if (numCarac < 3) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                } catch (err) {
                    return false;
                }
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFechaEmision(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaEmision").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaEmision', null, null, null, '', 'calFechaEmision', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaResol(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaResol").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaResolucion', null, null, null, '', 'calFechaResol', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaCaducidad(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaCaducidad").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaCaducidad', null, null, null, '', 'calFechaCaducidad', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaValidacion(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaValidacion").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaValidacion', null, null, null, '', 'calFechaValidacion', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaBaja(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaBaja").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaBaja', null, null, null, '', 'calFechaBaja', '', null, null, null, null, null, null, null, null, evento);
            }

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoPersDiscp');
            }" >
        <div class="contenidoPantalla">
            <form>
                <div id="barraProgresoPersDiscp" class="barraProgreso">
                    <div class="contenedorHidepage">
                        <div class="textoHide">
                            <span>
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                            </span>
                        </div>
                        <div class="imagenHide" style="height:152px; width: 275px;">
                            <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                        </div>
                    </div>
                </div>
                <fieldset id="fieldsetCriterios" name="fieldsetCriterios" style="width: 98%;" class="contenidoBusqueda">
                    <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.criterios")%></legend>
                    <br>
                    <div class="lineaFormulario" style="text-align: right;">
                        <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.criterios.personal")%></legend>
                        <div style="float: left; width: 62px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.dni")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="9" size="9" id="txtDni" name="txtDni" class="inputTexto"  onkeyup="xAMayusculas(this);"/>
                        </div>
                        <div style="float: left; width: 50px; margin-left: 30px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.nombre")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="500" size="30" id="txtNombre" name="txtNombre" class="inputTexto" />
                        </div>
                        <div style="float: left; width: 52px; margin-left: 30px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.apellidos")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="500" size="50" id="txtApellidos" name="txtApellidos" class="inputTexto" />
                        </div>
                    </div>
                    <br>
                    <br>
                    <div class="lineaFormulario" style="text-align: right;">
                        <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.criterios.discapacidad")%></legend>
                        <br>
                        <div style="float: left; width: 20px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.tipo")%>
                        </div>                        
                        <div style="width: auto; margin-left: 10px; float: left;">
                            <input type="text" name="codListaTipoDiscapacidad" id="codListaTipoDiscapacidad" size="4" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaTipoDiscapacidad"  id="descListaTipoDiscapacidad" size="30" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaTipoConD" name="anchorListaTipoDiscapacidad">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                        <div style="float: left; width: 30px; margin-left: 20px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.grado")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="10" size="8" id="txtGrado" name="txtGrado" class="inputTexto"   onchange="reemplazarPuntos(this);"/>
                        </div>
                        <div style="float: left; width: 50px; margin-left: 20px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.severa")%>
                        </div>
                        <div style="width: auto; margin-left: 10px; float: left;">
                            <input type="text" name="codListaEsSevera" id="codListaEsSevera" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaEsSevera"  id="descListaEsSevera" size="4" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaEsSevera" name="anchorListaEsSevera">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                        <div style="float: left; width: 50px; margin-left: 20px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.validez")%>
                        </div>
                        <div style="width: auto; margin-left: 10px; float: left;">
                            <input type="text" name="codListaValidez" id="codListaValidez" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaValidez"  id="descListaValidez" size="25" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaValidez" name="anchorListaValidez">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <br>
                    <br>
                    <br>
                    <div class="lineaFormulario" style="text-align: right;">
                        <div style="float: left; width: 120px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.centro")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="10" size="10" id="txtCentro" name="txtCentro" class="inputTexto" />
                        </div>
                    </div>
                    <div style="float: left; width: 120px; margin-left: 20px; text-align: left;">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.criterios.territorio")%>
                    </div>
                    <div style="width: auto; margin-left: 10px; float: left;">
                        <input type="text" name="codListaTerritorio" id="codListaTerritorio" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                        <input type="text" name="descListaTerritorio"  id="descListaTerritorio" size="25" class="inputTexto" readonly="true" value="" />
                        <a href="" id="anchorListaTerritorio" name="anchorListaTerritorio">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                  name="botonAplicacion" style="cursor:pointer;"></span>
                        </a>
                    </div> 
                    <br>
                    <div class="botoneraBusqueda" style="padding-top: 30px; text-align: center; ">
                        <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="buscar();">
                        <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.limpiar")%>" onclick="limpiarBuscador();">
                    </div>
                </fieldset>
                <fieldset id="fieldsetResultados" name="fieldsetResultados" style="width: 98%;">
                    <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.resultados")%></legend>
                    <div class="lineaFormulario" style="text-align: right;">
                        <label id="lblNumResultados"></label>
                    </div>                
                    <div id="divGeneral"> 
                        <div id="listaResultados"  class="tablaPers" style="padding: 5px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                    </div>
                    <div class="botonera" style="padding-top: 30px; text-align: center; " >
                        <input type="button" id="btnDetalle" name="btnDetalle" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificar();">
                        <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarConsultaDiscp();"  >
                    </div>
                </fieldset>
                <fieldset id="fieldsetMantenimiento" name="fieldsetMantenimiento" style="width: 98%;">
                    <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.mantenimiento")%></legend>
                    <fieldset id="fieldsetMantenimientoPersona" name="fieldsetMantenimientoPersona" style="width: 98%">
                        <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.criterios.personal")%></legend>
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 50px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.dni")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="9" size="9" id="txtDniMan" name="txtDniMan" class="inputTextoObligatorio" disabled="true"  onkeyup="xAMayusculas(this);"/>
                            </div>
                            <div style="float: left; width: 50px; margin-left: 30px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.nombre")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="500" size="30" id="txtNombreMan" name="txtNombreMan" class="inputTextoObligatorio" disabled="true"/>
                            </div>
                            <div style="float: left; width: 52px; margin-left: 30px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.apellidos")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="500" size="50" id="txtApellidosMan" name="txtApellidosMan" class="inputTextoObligatorio" disabled="true"/>
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 120px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.tipo")%>
                            </div>                        
                            <div style="width: auto; margin-left: 10px; float: left;">
                                <input type="text" name="codListaTipoDiscapacidadMan" id="codListaTipoDiscapacidadMan" size="4" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoDiscapacidadMan"  id="descListaTipoDiscapacidadMan" size="30" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaTipoDiscapacidadMan" name="anchorListaTipoDiscapacidadMan">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>

                        </div>
                        <br>
                        <br>
                        <br>
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 100px;text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.grado")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="10" size="8" id="txtGradoMan" name="txtGradoMan" class="inputTextoObligatorio"   onchange="reemplazarPuntos(this);"/>
                            </div>
                            <div style="float: left; width: 50px; margin-left: 30px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.severa")%>
                            </div>
                            <div style="width: auto; margin-left: 10px; float: left;">
                                <input type="text" name="codListaEsSeveraMan" id="codListaEsSeveraMan" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaEsSeveraMan"  id="descListaEsSeveraMan" size="4" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaEsSeveraMan" name="anchorListaEsSeveraMan">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="fieldsetMantenimientoCertificado" name="fieldsetMantenimientoCertificado" style="width: 98%">
                        <legend class="legendAzul"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"legend.consPersDiscp.mantenimiento.certificado")%></legend>  
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 140px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.fechaEmision")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFechaObligatorio" 
                                           id="fechaEmision" name="fechaEmision"
                                           maxlength="10"  size="10"
                                           value=""
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                           onblur = "javascript:return comprobarFechaLanbide(this);"
                                           onfocus="javascript:this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaEmision()(event);
                                            return false;" style="text-decoration:none;"> 
                                        <IMG style="border: 0px solid windowframe" height="17" id="calFechaEmision" name="calFechaEmision" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </div>
                            <div style="float: left; width: 140px; margin-left: 20px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.fechaResolucion")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFechaObligatorio" 
                                           id="fechaResolucion" name="fechaResolucion"
                                           maxlength="10"  size="10"
                                           value=""
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                           onblur = "javascript:return comprobarFechaLanbide(this);"
                                           onfocus="javascript:this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaResol()(event);
                                            return false;" style="text-decoration:none;"> 
                                        <IMG style="border: 0px solid none" height="17" id="calFechaResol" name="calFechaResol" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 50px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.validez")%>
                            </div>
                            <div style="width: auto; margin-left: 10px; float: left;">
                                <input type="text" name="codListaValidezMan" id="codListaValidezMan" size="2" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaValidezMan"  id="descListaValidezMan" size="25" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaValidezMan" name="anchorListaValidezMan">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"  name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div id="caducidadCertificado" name="caducidadCertificado" >
                                <div style="float: left; width: 120px; margin-left: 20px; text-align: left;">
                                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.fechaCaducidad")%>
                                </div>
                                <div style="width: 200px; float: left;">
                                    <div style="float: left;">
                                        <input type="text" class="inputTxtFecha" 
                                               id="fechaCaducidad" name="fechaCaducidad"
                                               maxlength="10"  size="10"
                                               value=""
                                               onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                               onblur = "javascript:return comprobarFechaLanbide(this);"
                                               onfocus="javascript:this.select();"/>
                                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaCaducidad()(event);
                                                return false;" style="text-decoration:none;"> 
                                            <IMG style="border: 0px solid" height="17" id="calFechaCaducidad" name="calFechaCaducidad" border="0" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                        </A>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>
                        <br>
                        <br>
                        <div class="lineaFormulario" style="text-align: right;">
                            <div style="float: left; width: 40px; text-align: left;">
                                <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.certificado")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="40" size="20" id="oidCertificado" name="oidCertificado" class="inputTexto" disabled="true"/>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="40" size="20" id="nombreCertificado" name="nombreCertificado" class="inputTexto" disabled="true"/>
                            </div>
                        </div>
                        <br>
                        <br>
                        <div class="botonera" style="padding-top: 15px; text-align: left;">
                            <input type="file"  name="certificado_pdf" id="certificado_pdf" class="inputTexto" size="60" accept=".pdf">
                            <input type="button" id="btnCargarCertificado" name="btnCargarCertificado" class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.cargarCertificado")%>" onclick="pulsarCargarCertificado();">
                            <input type="button" id="btnDescargarCertificado" name="btnDescargarCertificado" class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.descargar")%>" onclick="pulsarDescargarCertificado();" disabled="true">
                        </div>  
                    </fieldset>
                    <div class="lineaFormulario" style="text-align: right;">
                        <div style="float: left; width: 180px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.fechaValidacion")%>
                        </div>
                        <div style="width: 200px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaValidacion" name="fechaValidacion"
                                       maxlength="10"  size="10"
                                       value=""
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaValidacion()(event);
                                        return false;" style="text-decoration:none;"> 
                                    <IMG style="border: 0px solid" height="17" id="calFechaValidacion" name="calFechaValidacion" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                        <div style="float: left; width: 100px;margin-left: 20px; text-align: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.mantenimiento.fechaBaja")%>
                        </div>
                        <div style="width: 300px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaBaja" name="fechaBaja"
                                       maxlength="10"  size="10"
                                       value=""
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaBaja()(event);
                                        return false;" style="text-decoration:none;"> 
                                    <IMG style="border: 0px solid" height="17" id="calFechaBaja" name="calFechaBaja" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <div class="botonera" style="padding-top: 30px; text-align: center; " >
                        <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.guadar")%>" onclick="guardar();">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="aceptar();">
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    </div>
                </fieldset>           
                <iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
            </form>       

            <script type="text/javascript">
                /* DESPLEGABLES */
                /* busqueda */
                /*  TIPO DISCP*/
                listaCodigosTipoDiscapacidad[0] = "";
                listaDescripcionesTipoDiscapacidad[0] = "";
                contador = 0;
                <logic:iterate id="tipoDisc" name="listaTipoDiscapacidad" scope="request">
                listaCodigosTipoDiscapacidad[contador] = ['<bean:write name="tipoDisc" property="des_val_cod" />'];
                listaDescripcionesTipoDiscapacidad[contador] = ['<bean:write name="tipoDisc" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoDiscapacidad = new Combo("ListaTipoDiscapacidad");
                comboListaTipoDiscapacidad.addItems(listaCodigosTipoDiscapacidad, listaDescripcionesTipoDiscapacidad);
                comboListaTipoDiscapacidad.change = cargarDatosTipoDiscapacidad;

                /* ES SEVERA*/
                listaCodigosEsSevera[0] = "";
                listaDescripcionesEsSevera[0] = "";
                contador = 0;
                <logic:iterate id="esSevera" name="listaEsSevera" scope="request">
                listaCodigosEsSevera[contador] = ['<bean:write name="esSevera" property="des_val_cod" />'];
                listaDescripcionesEsSevera[contador] = ['<bean:write name="esSevera" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaEsSevera = new Combo("ListaEsSevera");
                comboListaEsSevera.addItems(listaCodigosEsSevera, listaDescripcionesEsSevera);
                comboListaEsSevera.change = cargarDatosEsSevera;

                /* VALIDEZ */
                listaCodigosValidez[0] = "";
                listaDescripcionesValidez[0] = "";
                contador = 0;
                <logic:iterate id="validez" name="listaValidezCertificado" scope="request">
                listaCodigosValidez[contador] = ['<bean:write name="validez" property="des_val_cod" />'];
                listaDescripcionesValidez[contador] = ['<bean:write name="validez" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaValidez = new Combo("ListaValidez");
                comboListaValidez.addItems(listaCodigosValidez, listaDescripcionesValidez);
                comboListaValidez.change = cargarDatosValidez;

                /* TERRITORIO */
                listaCodigosTerritorio[0] = "";
                listaDescripcionesTerritorio[0] = "";
                contador = 0;
                <logic:iterate id="territorio" name="listaTerritorio" scope="request">
                listaCodigosTerritorio[contador] = ['<bean:write name="territorio" property="des_val_cod" />'];
                listaDescripcionesTerritorio[contador] = ['<bean:write name="territorio" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTerritorio = new Combo("ListaTerritorio");
                comboListaTerritorio.addItems(listaCodigosTerritorio, listaDescripcionesTerritorio);
                comboListaTerritorio.chamge = cargarDatosTerritorio;

                /* mantenimiento */
                /*  TIPO DISCP*/
                listaCodigosTipoDiscapacidadMan[0] = "";
                listaDescripcionesTipoDiscapacidadMan[0] = "";
                contador = 0;
                <logic:iterate id="tipoDiscMan" name="listaTipoDiscapacidad" scope="request">
                listaCodigosTipoDiscapacidadMan[contador] = ['<bean:write name="tipoDiscMan" property="des_val_cod" />'];
                listaDescripcionesTipoDiscapacidadMan[contador] = ['<bean:write name="tipoDiscMan" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoDiscapacidadMan = new Combo("ListaTipoDiscapacidadMan");
                comboListaTipoDiscapacidadMan.addItems(listaCodigosTipoDiscapacidadMan, listaDescripcionesTipoDiscapacidadMan);
                comboListaTipoDiscapacidadMan.change = cargarDatosTipoDiscapacidadMan;

                /* ES SEVERA */
                listaCodigosEsSeveraMan[0] = "";
                listaDescripcionesEsSeveraMan[0] = "";
                contador = 0;
                <logic:iterate id="esSeveraMan" name="listaEsSevera" scope="request">
                listaCodigosEsSeveraMan[contador] = ['<bean:write name="esSeveraMan" property="des_val_cod" />'];
                listaDescripcionesEsSeveraMan[contador] = ['<bean:write name="esSeveraMan" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaEsSeveraMan = new Combo("ListaEsSeveraMan");
                comboListaEsSeveraMan.addItems(listaCodigosEsSeveraMan, listaDescripcionesEsSeveraMan);
                comboListaEsSeveraMan.change = cargarDatosEsSeveraMan;

                /* VALIDEZ */
                listaCodigosValidezMan[0] = "";
                listaDescripcionesValidezMan[0] = "";
                contador = 0;
                <logic:iterate id="validezMan" name="listaValidezCertificado" scope="request">
                listaCodigosValidezMan[contador] = ['<bean:write name="validezMan" property="des_val_cod" />'];
                listaDescripcionesValidezMan[contador] = ['<bean:write name="validezMan" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaValidezMan = new Combo("ListaValidezMan");
                comboListaValidezMan.addItems(listaCodigosValidezMan, listaDescripcionesValidezMan);
                comboListaValidezMan.change = cargarDatosValidezMan;

                var vdni = '<%=dniP%>';
                if (vdni != null) {
                    rellenarPersonaBusqueda();
                }
                comprobarValidezMan();
                comprobarOid();

                // tabla Resultados
                var tablaResultados;
                var listaResultados = new Array();
                var listaResultadosTabla = new Array();

                crearTabla();
                <%
                        DiscapacitadoVO objectVO = null;
                        List<DiscapacitadoVO> List = null;
                        if(request.getAttribute("datosPersona")!=null){
                            List = (List<DiscapacitadoVO>)request.getAttribute("datosPersona");
                        }
                        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                        if (List!= null && List.size() >0){ 
                            for (int indice=0;indice<List.size();indice++) {
                                objectVO = List.get(indice);
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String Dni="";
                                if(objectVO.getDni()!=null) {
                                    Dni=objectVO.getDni();
                                }
                                String Apellidos="";
                                if(objectVO.getApellidos()!=null) {
                                    Apellidos=objectVO.getApellidos();
                                }
                                String Nombre="";
                                if(objectVO.getNombre()!=null) {
                                    Nombre=objectVO.getNombre();
                                }
                                String TipoD="";
                                if(objectVO.getTipoDisc()!=null) {
                                    TipoD=objectVO.getTipoDisc();
                                }
                                String DescTipoD="";
                                if(objectVO.getDescTipoDisc()!=null) {
                                    String descripcion = objectVO.getDescTipoDisc();                  
                                    String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                        if(idiomaUsuario==4){
                                            descripcion=descripcionDobleIdioma[1];
                                        }else{
                                            // Cogemos la primera posicion que deberia ser castellano
                                            descripcion=descripcionDobleIdioma[0];
                                        }
                                    }
                                    DescTipoD=descripcion;
                                   // DescTipoD=objectVO.getDescTipoDisc();
                                }
                       
                                String Grado="";
                                if(objectVO.getPorcDisc()!=null){
                                   Grado=String.valueOf((objectVO.getPorcDisc().toString()).replace(".",","));
                                }else{
                                   Grado="-";
                                }
                                String FechEmis="";
                                if(objectVO.getFechEmision()!=null){
                                   FechEmis=dateFormat.format(objectVO.getFechEmision());
                                }else{
                                   FechEmis="-";
                                }
                                String FechReso="";
                                if(objectVO.getFechResolucion()!=null){
                                   FechReso=dateFormat.format(objectVO.getFechResolucion());
                                }else{
                                   FechReso="-";
                                }
                                String FechCadu="";
                                if(objectVO.getFechCaducidad()!=null){
                                   FechCadu=dateFormat.format(objectVO.getFechCaducidad());
                                }else{
                                   FechCadu="-";
                                }
                                String FechVali="";
                                if(objectVO.getFechValidacion()!=null){
                                   FechVali=dateFormat.format(objectVO.getFechValidacion());
                                }else{
                                   FechVali="-";
                                }
                                String FechBaja="";
                                if(objectVO.getFechBaja()!=null){
                                   FechBaja=dateFormat.format(objectVO.getFechBaja());
                                }else{
                                   FechBaja="-";
                                }
                                String Validez="";
                                if(objectVO.getValidez()!=null) {
                                    Validez=objectVO.getValidez();
                                }
                                String DescValidez="";
                                if(objectVO.getDescValidez()!=null) {
                                    String descripcion = objectVO.getDescValidez();                  
                                    String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                        if(idiomaUsuario==4){
                                            descripcion=descripcionDobleIdioma[1];
                                        }else{
                                            // Cogemos la primera posicion que deberia ser castellano
                                            descripcion=descripcionDobleIdioma[0];
                                        }
                                    }
                                    DescValidez=descripcion;
                                }
                                String EsSevera="";
                                if(objectVO.getDiscSevera()!=null) {
                                    EsSevera=objectVO.getDiscSevera();
                                }
                                String DescEsSevera="";
                                if(objectVO.getDescDiscSevera()!=null) {
                                    String descripcion = objectVO.getDescDiscSevera();                  
                                    String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                        if(idiomaUsuario==4){
                                            descripcion=descripcionDobleIdioma[1];
                                        }else{
                                            // Cogemos la primera posicion que deberia ser castellano
                                            descripcion=descripcionDobleIdioma[0];
                                        }
                                    }
                                    DescEsSevera=descripcion;
                                }
                                String Oid="";
                                if(objectVO.getOidCertificado()!=null) {
                                    Oid=objectVO.getOidCertificado();
                                }
                                String NomCert="";
                                if(objectVO.getNombreCertificado()!=null) {
                                    NomCert=objectVO.getNombreCertificado();
                                }
                                String Centro="";
                                if(objectVO.getCentro()!=null) {
                                    Centro=objectVO.getCentro();
                                }
                                String Territorio="";
                                if(objectVO.getTerritorio()!=null) {
                                    Territorio=objectVO.getTerritorio();
                                }
                                String DescTerritorio="";
                                if(objectVO.getDescTerritorio()!=null) {
                                    String descripcion = objectVO.getDescTerritorio();                  
                                    String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                                    if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                        if(idiomaUsuario==4){
                                            descripcion=descripcionDobleIdioma[1];
                                        }else{
                                            // Cogemos la primera posicion que deberia ser castellano
                                            descripcion=descripcionDobleIdioma[0];
                                        }
                                    }
                                    DescTerritorio=descripcion;
                                }
                %>
                listaResultados[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=Dni%>', '<%=Apellidos%>', '<%=Nombre%>', '<%=TipoD%>',
                    '<%=DescTipoD%>', '<%=Grado%>', '<%=FechEmis%>', '<%=FechReso%>', '<%=Validez%>', '<%=DescValidez%>',
                    '<%=FechCadu%>', '<%=EsSevera%>', '<%=DescEsSevera%>', '<%=FechVali%>', '<%=FechBaja%>',
                    '<%=Oid%>', '<%=NomCert%>', '<%=Centro%>', '<%=Territorio%>', '<%=DescTerritorio%>'];
                listaResultadosTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=Dni%>', '<%=Nombre%>', '<%=Apellidos%>',
                    '<%=TipoD%>', '<%=Grado%>', '<%=FechEmis%>', '<%=FechReso%>', '<%=Validez%>',
                    '<%=FechCadu%>', '<%=DescEsSevera%>', '<%=FechVali%>', '<%=FechBaja%>',
                    '<%=Oid%>', '<%=NomCert%>', '<%=Centro%>', '<%=Territorio%>'];
                <%                                               
                            }
                        } 
                %>
                tablaResultados.lineas = listaResultadosTabla;
                tablaResultados.displayTabla();
                document.getElementById('lblNumResultados').innerText = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.lblResultados1")%> ' + (listaResultadosTabla.length) + ' <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.consPersDiscp.lblResultados2")%>';
            </script>
        </div>
    </body>
</html>
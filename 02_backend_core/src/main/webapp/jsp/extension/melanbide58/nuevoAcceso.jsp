<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO" %>
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
            PlantillaVO datModif = new PlantillaVO();
            PlantillaVO objectVO = new PlantillaVO();

            String codOrganizacion = "";
            String codMotivoVisita = "";
            String nuevo = "";
            String fecha = "";
            String fechaIV="";
            String fechaFV="";
            String expediente = "";
            String numLineaC = (String)request.getAttribute("numLineaC");

        
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            //ConstantesMeLanbide58 _constantesMeLanbide58 = ConstantesMeLanbide58();
            String numLineaS = (String)request.getAttribute("numLineaS");
            expediente = (String)request.getAttribute("numExp");
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
                    datModif = (PlantillaVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formatoFecha.format(datModif.getFecNaci());
                    if (datModif.getFecCertificado()!=null){
                        fechaIV = formatoFecha.format(datModif.getFecCertificado());
                    }
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            var nuevo = "<%=nuevo%>";
            var titulo;
            var mensajeValidacion = '';

            var comboListaSexo;
            var listaCodigosSexo = new Array();
            var listaDescripcionesSexo = new Array();

            var comboListaTipoDis;
            var listaCodigosTipoDis = new Array();
            var listaDescripcionesTipoDis = new Array();

            var comboListaCodContrato;
            var listaCodigosCodContrato = new Array();
            var listaDescripcionesCodContrato = new Array();

            var comboListaSevero;
            var listaCodigosSevero = new Array();
            var listaDescripcionesSevero = new Array();

            var comboListaValidado;
            var listaCodigosValidado = new Array();
            var listaDescripcionesValidado = new Array();

            function buscaCodigoSexo(codSexo) {
                comboListaSexo.buscaCodigo(codSexo);
            }

            function cargarDatosSexo() {
                var codSexoSeleccionado = document.getElementById("codListaSexo").value;
                buscaCodigoSexo(codSexoSeleccionado);
            }

            function buscaCodigoTipoDis(codTipoDis) {
                comboListaTipoDis.buscaCodigo(codTipoDis);
            }

            function cargarDatosTipoDis() {
                var codTipoDisSeleccionado = document.getElementById("codListaTipoDis").value;
                buscaCodigoTipoDis(codTipoDisSeleccionado);
                comprobarDiscapacidad(codTipoDisSeleccionado);
            }

            function buscaCodigoCodContrato(codCodContrato) {
                comboListaCodContrato.buscaCodigo(codCodContrato);
            }

            function cargarDatosCodContrato() {
                var codCodContratoSeleccionado = document.getElementById("codListaCodContrato").value;
                buscaCodigoCodContrato(codCodContratoSeleccionado);
            }

            function buscaCodigoSevero(codListaSevero) {
                comboListaSevero.buscaCodigo(codListaSevero);
            }

            function cargarDatosSevero() {
                var codSeveroSeleccionado = document.getElementById("codListaSevero").value;
                buscaCodigoSevero(codSeveroSeleccionado);
            }

            function buscaCodigoValidado(codListaValidado) {
                comboListaValidado.buscaCodigo(codListaValidado);
            }

            function cargarDatosValidado() {
                var codValidadoSeleccionado = document.getElementById("codListaValidado").value;
                buscaCodigoValidado(codValidadoSeleccionado);
            }

            function comprobarDiscapacidad(codigo) {
                if (codigo == 'P' || codigo == 'PA' || codigo == 'PG' || codigo == 'PT') {
                    document.getElementById('grado').disabled = true;
                } else {
                    document.getElementById('grado').disabled = false;
                }

            }
//            function comprobarContrato(codigo) {
//                if (esParcial(codigo)) {
//                    document.getElementById('porcJornada').disabled = false;
//                } else {
//                    document.getElementById('porcJornada').value = '100';
//                    document.getElementById('porcJornada').disabled = true;
//                }
//            }

            function esParcial(codigo) {
                if (codigo == '200' || codigo == '209' || codigo == '230' || codigo == '239' || codigo == '250'
                        || codigo == '289' || codigo == '339' || codigo == '501' || codigo == '502' || codigo == '510'
                        || codigo == '520' || codigo == '530' || codigo == '540' || codigo == '541' || codigo == '550') {
                    return true;
                } else {
                    return false;
                }
            }


            function rellenardatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                    buscaCodigoTipoDis('<%=datModif.getTipoDis() != null ? datModif.getTipoDis() : ""%>');
                    buscaCodigoCodContrato('<%=datModif.getCodContrato() != null ? datModif.getCodContrato() : ""%>');
                    buscaCodigoSevero('<%=datModif.getDiscSevera()!= null ? datModif.getDiscSevera() : ""%>');
                    buscaCodigoValidado('<%=datModif.getDiscValidada() != null ? datModif.getDiscValidada() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }


            function guardarDatos() {

                if (validarDatos()) {

                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var ape = "";
                    var severaEmp = "-";
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

                    var llamar = false;

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoAnexoC&tipo=0";
                        llamar = true;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarAnexoC&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&porcOriginal=<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString(): ""%>";

                        var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaModificarPlantilla")%>');
                        if (resultado == 1) {
                            llamar = true;
                        }
                    }
                    parametros += "&numLinea=" + document.getElementById('numLinea').value
                            + "&expediente=<%=expediente%>"
                            + "&dni_nif=" + document.getElementById('dni_nif').value
                            + '&nombre=' + nombre
                            + '&apellidos=' + ape
                            + "&codSexo=" + document.getElementById('codListaSexo').value
                            + "&fechaNac=" + document.getElementById('meLanbide58Fecha').value
                            + "&numSS=" + document.getElementById('numSS').value
                            + "&fecCert=" + document.getElementById('fecInicioVigencia').value
                            + "&tipoDis=" + document.getElementById('codListaTipoDis').value
                            + "&grado=" + document.getElementById('grado').value
                            + "&severo=" + document.getElementById('codListaSevero').value
                            + "&validado=" + document.getElementById('codListaValidado').value
                            + "&codCodContrato=" + document.getElementById('codListaCodContrato').value
//                            + "&porcJornada=" + document.getElementById('porcJornada').value
                            ;
                    if (llamar) {
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
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var codigoIncompleto = null;
                            var lista = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    lista[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                                else if (hijos[j].nodeName == "CODIGO_INCOMPLETO") {
                                    codigoIncompleto = hijos[j].childNodes[0].nodeValue;
                                    lista[j] = codigoIncompleto;
                                } else if (hijos[j].nodeName == "FILA") {
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
                                        } else if (hijosFila[cont].nodeName == "NIFCIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "SEXO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECNACI") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUMSS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECCERTIFICADO") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPODIS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "GRADO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[10].toString();
                                                tex = tex.replace(".", ",");
                                                fila[10] = tex;
                                            } else {
                                                fila[10] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DES_DISC_SEVERA_EMP") {//11
                                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[11] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DES_DISC_VALIDADA") {//12
                                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[12] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "CODCONTRATO") {//13
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[13] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DATOS_PENDIENTES") { //14
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[14] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUEVA_ALTA") {//15
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[15] = '-';
                                            }
                                        }
                                    }
                                    lista[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                //jsp_alerta("A",'Correcto');
                                if (codigoIncompleto != "0") {
                                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.personaIncompletaA")%>' + codigoIncompleto + ' ' + '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.personaIncompletaB")%>');
                                }
                                self.parent.opener.retornoXanelaAuxiliar(lista);
                                cerrarVentana();
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else if (codigoOperacion == "4") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.anioVacio")%>');
                            } else if (codigoOperacion == "5") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.recalculoSMI")%>');
                            } else if (codigoOperacion == "6") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.notFoundSMI")%>');
                            } else if (codigoOperacion == "7") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarRecalculoSMI")%>');
                            } else if (codigoOperacion == "8") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.insertarPersonaDiscapacitada")%>');
                            } else if (codigoOperacion == "9") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarPersonaDisc")%>');
                            } else if (codigoOperacion == "10") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.modificarAccesoSeleccionado")%>');
                            } else if (codigoOperacion == "11") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizaJornadaB")%>');
                            } else if (codigoOperacion == "12") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarPersonaDisc")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                        }//try-catch
                    }// if (llamar)

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


            function validarDatos() {
                mensajeValidacion = "";

                var campo = document.getElementById('meLanbide58Fecha').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                campo = document.getElementById('fecInicioVigencia').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                campo = document.getElementById('dni_nif');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                } else if (!validarNif(campo)) {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                    return false;
                }

                campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                    return false;
                }

                campo = document.getElementById('numLinea').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                    return false;
                } else {
                    if (!validarNumerico(document.getElementById('numLinea'))) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                        return false;
                    }
                }

                campo = document.getElementById('apellidos').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }

                campo = document.getElementById('codListaSexo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                    return false;
                }

                campo = document.getElementById('codListaTipoDis').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoDis")%>';
                    return false;
                } else if (campo != 'PA' && campo != 'PG' && campo != 'PT') {
                    campo = document.getElementById('grado').value;
                    if (campo == null || campo == '') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado")%>';
                        return false;
                    } else if (!validarNumericoDecimalPrecision(campo, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else if (!validarNumericoPorcentajeCien(campo)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                        return false;
                    }
                }

                campo = document.getElementById('codListaCodContrato').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoCon")%>';
                    return false;
                }
               
                return true;
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

            //Funcion para el calendario de fecha 
            function mostrarCalFecha(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calMeLanbide58Fecha").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'meLanbide58Fecha', null, null, null, '', 'calMeLanbide58Fecha', '', null, null, null, null, null, null, null, null, evento);

            }//mostrarCalFechaAdqui

            function mostrarCalFechaIVigen(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecInicioVigencia").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecInicioVigencia', null, null, null, '', 'calFecInicioVigencia', '', null, null, null, null, null, null, null, null, evento);

            }
            function mostrarCalFechaFVigen(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecFinVigencia").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecFinVigencia', null, null, null, '', 'calFecFinVigencia', '', null, null, null, null, null, null, null, null, evento);

            }

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }

            function desbloquearCampos() {
                document.getElementById('dni_nif').disabled = false;
                document.getElementById('nombre').disabled = false;
                document.getElementById('apellidos').disabled = false;
            }
        </script>
    </head>
    <body>
        <div class="contenidoPantalla">
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span id="subtitulo"></span>
                </div>                        
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="numLinea" name="numLinea" type="text" class="inputTextoObligatorio" size="10" maxlength="5" disabled
                                   value="<%=datModif != null && datModif.getNumLinea() != null ? datModif.getNumLinea() : ""%>"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.dni_nif")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="dni_nif" name="dni_nif" type="text" class="inputTextoObligatorio" size="12" maxlength="9"
                                   onkeyup="xAMayusculas(this);" disabled
                                   value="<%=datModif != null && datModif.getNif_Dni() != null ? datModif.getNif_Dni() : ""%>"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="50" disabled
                                   value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="apellidos" name="apellidos" type="text" class="inputTextoObligatorio" size="30" maxlength="50" disabled
                                   value="<%=datModif != null && datModif.getApellidos() != null ? datModif.getApellidos() : ""%>"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div class="etiqueta" style="width: 190px; float: left;">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                    </div>
                    <div>
                        <div>
                            <input type="text" name="codListaSexo" id="codListaSexo" size="4"  maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaSexo"  id="descListaSexo" size="15" class="inputComboObligatorio" readonly="true" value="" />
                            <a href="" id="anchorListaSexo" name="anchorListaSexo">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.FechaNacimiento")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input type="text" class="inputTxtFechaObligatorio" 
                                   id="meLanbide58Fecha" name="meLanbide58Fecha"
                                   maxlength="10"  size="10"
                                   value="<%=fecha%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecha(event);
                                    return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG style="border:none" height="17" id="calMeLanbide58Fecha" name="calMeLanbide58Fecha" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numSS")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="numSS" name="numSS" type="text" class="inputTexto" size="15" maxlength="15" 
                                   value="<%=datModif != null && datModif.getNumSS() != null ? datModif.getNumSS() : ""%>"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;text-align: left;" class="etiqueta" >
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaCertif")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input type="text" class="inputTxtFechaObligatorio" 
                               id="fecInicioVigencia" name="fecInicioVigencia"
                               maxlength="10"  size="10"
                               value="<%=fechaIV%>"
                               onkeyup = "return SoloCaracteresFechaLanbide(this);"
                               onblur = "javascript:return comprobarFechaLanbide(this);"
                               onfocus="javascript:this.select();"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIVigen(event);
                                return false;" style="text-decoration:none;"> 
                            <IMG style="border: none" height="17" id="calFecInicioVigencia" name="calFecInicioVigencia" border="0" 
                                 src="<c:url value='/images/calendario/icono.gif'/>"> 
                        </A>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div class="etiqueta" style="width: 190px; float: left;">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoDiscapacidad")%>
                    </div>
                    <div>
                        <div>
                            <input type="text" name="codListaTipoDis" id="codListaTipoDis" size="4"  maxlength="4" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaTipoDis"  id="descListaTipoDis" size="45" class="inputComboObligatorio" readonly="true" value="" />
                            <a href="" id="anchorListaTipoDis" name="anchorListaTipoDis">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.gradoDiscapacidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="grado" name="grado" type="text" class="inputTextoObligatorio" size="5" maxlength="5" 
                                   value="<%=datModif != null && datModif.getGrado() != null ? datModif.getGrado().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div class="etiqueta" style="width: 190px; float: left;">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.codContrato")%>
                    </div>
                    <div>
                        <div>
                            <input type="text" name="codListaCodContrato" id="codListaCodContrato" size="4"  maxlength="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaCodContrato"  id="descListaCodContrato" size="60" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaCodContrato" name="anchorListaCodContrato">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                </div>                    
                
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.severa")%>
                    </div>                     
                    <div>
                        <div>
                            <input type="text" name="codListaSevero" id="codListaSevero" size="4"  maxlength="1" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaSevero"  id="descListaSevero" size="10" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaValidado" name="anchorListaSevero">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>                       
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.severaLan")%>
                    </div>
                    <div>
                        <div>
                            <input type="text" name="codListaValidado" id="codListaValidado" size="4" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaValidado"  id="descListaValidado" size="10" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaValidado" name="anchorListaValidado">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>                       
                </div>
                <div class="lineaFormulario" style="width: 100%; float: left; margin-bottom: 10px">
                    <div class="botonera" style="text-align: center;">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                    </div>
                </div>
            </div>
            <script type="text/javascript">

                /*desplegable sexo*/
                listaCodigosSexo[0] = "";
                listaDescripcionesSexo[0] = "";
                contador = 0;

                <logic:iterate id="sexo" name="listaSexo" scope="request">
                listaCodigosSexo[contador] = ['<bean:write name="sexo" property="des_val_cod" />'];
                listaDescripcionesSexo[contador] = ['<bean:write name="sexo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSexo = new Combo("ListaSexo");
                comboListaSexo.addItems(listaCodigosSexo, listaDescripcionesSexo);
                comboListaSexo.change = cargarDatosSexo;

                /*desplegable tipo discapacidad*/
                listaCodigosTipoDis[0] = "";
                listaDescripcionesTipoDis[0] = "";
                contador = 0;

                <logic:iterate id="tipoDis" name="listaTipoDis" scope="request">
                listaCodigosTipoDis[contador] = ['<bean:write name="tipoDis" property="des_val_cod" />'];
                listaDescripcionesTipoDis[contador] = ['<bean:write name="tipoDis" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoDis = new Combo("ListaTipoDis");
                comboListaTipoDis.addItems(listaCodigosTipoDis, listaDescripcionesTipoDis);
                comboListaTipoDis.change = cargarDatosTipoDis;

                /*desplegables tipo contrato*/
                listaCodigosCodContrato[0] = "";
                listaDescripcionesCodContrato[0] = "";
                contador = 0;

                <logic:iterate id="codContrato" name="listaCodContrato" scope="request">
                listaCodigosCodContrato[contador] = ['<bean:write name="codContrato" property="des_val_cod" />'];
                listaDescripcionesCodContrato[contador] = ['<bean:write name="codContrato" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaCodContrato = new Combo("ListaCodContrato");
                comboListaCodContrato.addItems(listaCodigosCodContrato, listaDescripcionesCodContrato);
                comboListaCodContrato.change = cargarDatosCodContrato;

                /*desplegable SEVERO*/
                listaCodigosSevero[0] = "";
                listaDescripcionesSevero[0] = "";
                contador = 0;

                <logic:iterate id="severo" name="listaSiNo" scope="request">
                listaCodigosSevero[contador] = ['<bean:write name="severo" property="des_val_cod" />'];
                listaDescripcionesSevero[contador] = ['<bean:write name="severo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSevero = new Combo("ListaSevero");
                comboListaSevero.addItems(listaCodigosSevero, listaDescripcionesSevero);
                comboListaSevero.change = cargarDatosSevero;

                /*desplegable VALIDADO*/
                listaCodigosValidado[0] = "";
                listaDescripcionesValidado[0] = "";
                contador = 0;

                <logic:iterate id="validado" name="listaSiNo" scope="request">
                listaCodigosValidado[contador] = ['<bean:write name="validado" property="des_val_cod" />'];
                listaDescripcionesValidado[contador] = ['<bean:write name="validado" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaValidado = new Combo("ListaValidado");
                comboListaValidado.addItems(listaCodigosValidado, listaDescripcionesValidado);
                comboListaValidado.change = cargarDatosValidado;

                if (nuevo == 0) {
                    rellenardatModificar();
                    document.getElementById("dni_nif").disabled = true;
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.modificarC")%>";
                } else {
                    desbloquearCampos();
                    var linea = "<%=numLineaC%>";
                    document.getElementById('numLinea').value = linea;
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso")%>";
                }

            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

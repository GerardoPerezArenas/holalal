<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MeLanbideConvocatorias" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = 1;
    if(sIdioma!=null && sIdioma!="")
        idiomaUsuario=Integer.parseInt(sIdioma);
 int apl = 5;
 String css = "";
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    String [] datosExp = numExpediente!=null ? numExpediente.split("/"):null;
    int ejercicioExpediente = (datosExp!=null && datosExp.length>0 ?  Integer.valueOf(datosExp[0]) : 2018);  // A partir de 2018 son los cambios

    EntidadVO entidad = (EntidadVO) request.getAttribute("entidad");
    int ext_ter = 0; //entidad.getExtTer();
    int ext_nvr = 0; //entidad.getExtNvr();
    MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias) request.getAttribute("convocatoriaActiva");
    int convActiva = (convocatoriaActiva != null && convocatoriaActiva.getDecretoCodigo()!= null ? convocatoriaActiva.getId(): 4); // Por defecto convocatorias CONV_ANTE-2021

    
    List<FilaAsociacionVO> asociaciones = (List<FilaAsociacionVO>)request.getAttribute("asociaciones");

%>
<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/oriGestionDatosEntidad.js"></script>

<script type="text/javascript">
    var ejercicioExpedienteJS = <%=ejercicioExpediente%>;
    var anchoTabla = ejercicioExpedienteJS < 2018 ? 850 : 970;

    function pulsarAltaAsociacionOri14() {
        var control = new Date();

        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarNuevaAsociacionORI&tipo=0&numero=<%=numExpediente%>&codigoEntidad=' + document.getElementById('codEntidadOri14').value + '&nombreEntidad=' + document.getElementById('nombreAsociacion').value + '&control=' + control.getTime(), 350, 650, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaEntidadesOri14(result);
                    limpiar();
                    refrescarPestanasORI14(1);
                }
            }
        });
    }

    function pulsarModificarAsociacionOri14() {
        var fila;
        if (tabEntidadOri14.selectedIndex != -1) {
            fila = tabEntidadOri14.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if (document.forms[0].modoConsulta.value == "si") {
                opcion = 'consultar';
            } else {
                opcion = 'modificar';
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarModificarAsociacion&tipo=0&numero=<%=numExpediente%>&opcion=' + opcion + '&codigoEntidad=' + document.getElementById('codEntidadOri14').value + '&nombreEntidad=' + document.getElementById('nombreAsociacion').value + '&codAsociacion=' + listaEntidadesOri14[fila][1] + '&control=' + control.getTime(), 350, 650, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaEntidadesOri14(result);
                        refrescarPestanasORI14(1);
                    }
                }
            });


        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarAsociacionOri14() {


        if (tabEntidadOri14.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {

                document.getElementById('msgCargandoEntidadOri14').style.display = "none";
                document.getElementById('msgEliminandoEntidadOri14').style.display = "inline";
                //barraProgresoOri14('on', 'barraProgresoEntidadesSolicitudOri14');
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var listaEntidades = new Array();
                parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarAsociacion&tipo=0&numero=<%=numExpediente%>&codEntidad=' + listaEntidadesOri14[tabEntidadOri14.selectedIndex][0] + '&codAsociacion=' + listaEntidadesOri14[tabEntidadOri14.selectedIndex][1] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", url, false);
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
                    listaEntidades = extraerListadoEntidadesOri14(nodos);
                    recargarTablaEntidadesOri14(listaEntidades);
                    var codigoOperacion = listaEntidades[0];
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        refrescarPestanasORI14(1);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
        //barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
    }

    function recargarTablaEntidadesOri14(result) {
        document.getElementById('codEntidadOri14').value = result[1];
        var fila;
        listaEntidadesOri14 = new Array();
        listaEntidadesOri14Tabla = new Array();
        listaEntidadesOri14Tabla_titulos = new Array();
        listaEntidadesOri14Tabla_estilos = new Array();

        for (var i = 2; i < result.length; i++) {
            fila = result[i];
            //listaEntidadesOri14[i-2] = fila; //NO FUNCIONA IE9
            if (ejercicioExpedienteJS < 2018) {
                listaEntidadesOri14[i - 2] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
                listaEntidadesOri14Tabla[i - 2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
                listaEntidadesOri14Tabla_titulos[i - 2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
            } else {
                listaEntidadesOri14[i - 2] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
                listaEntidadesOri14Tabla[i - 2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
                listaEntidadesOri14Tabla_titulos[i - 2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            }
        }

        inicializarTablaEntidadOri14();

    }

    function extraerListadoEntidadesOri14(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaEntidades = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        var codigoEntidad;

        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaEntidades[j] = codigoOperacion;
            } else if (hijos[j].nodeName == "CODIGO_ENTIDAD") {
                codigoEntidad = hijos[j].childNodes[0].nodeValue;
                listaEntidades[j] = codigoEntidad;
            } else if (hijos[j].nodeName == "ASOCIACION") {
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for (var cont = 0; cont < hijosFila.length; cont++) {
                    if (hijosFila[cont].nodeName == "COD_ENTIDAD") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                        } else {
                            fila[0] = '-';
                        }
                    }
                    if (hijosFila[cont].nodeName == "COD_ASOCIACION") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                        } else {
                            fila[1] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "CIF") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                        } else {
                            fila[2] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                        } else {
                            fila[3] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "SUPRAMUN") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[4] == '1') {
                                fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[4] == '0') {
                                fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "ADM_LOCAL") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[5] == '1') {
                                fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[5] == '0') {
                                fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[5] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "CENTROFP_PUB") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[6] == '1') {
                                fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[6] == '0') {
                                fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[6] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "CENTROFP_PRIV") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[7] == '1') {
                                fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[7] == '0') {
                                fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[7] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "ORI_ENT_AGENCOLOC") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[8] == '1') {
                                fila[8] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[8] == '0') {
                                fila[8] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[8] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "SINANIMOLUCRO") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                            if (fila[9] == '1') {
                                fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                            } else if (fila[9] == '0') {
                                fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                            }
                        } else {
                            fila[9] = '-';
                        }
                    }
                }
                listaEntidades[j] = fila;
                fila = new Array();
            }
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaEntidades;
    }

    function dblClckTablaEntidadesOri14(rowID, tableName) {
        //pulsarModificarEntidadOri14();
    }

    function cambiaEntidad() {
        var esAsociacion = (document.getElementById('esAsociacionS').checked ? "S" : (document.getElementById('esAsociacionN').checked ? "N" : ""));
        if ("S" == esAsociacion) {
            document.getElementById('supramunS').disabled = true;
            document.getElementById('supramunN').disabled = true;
            document.getElementById('admLocalS').disabled = true;
            document.getElementById('admLocalN').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            document.getElementById('agenciaColocacionN').disabled = true;
            if (document.getElementById('sinAnimoLucroS') != null)
                document.getElementById('sinAnimoLucroS').disabled = true;
            if (document.getElementById('sinAnimoLucroN') != null)
                document.getElementById('sinAnimoLucroN').disabled = true;

            /*document.getElementById('supramunS').checked = false;
             document.getElementById('supramunN').checked = false;
             document.getElementById('admLocalS').checked = false;
             document.getElementById('admLocalN').checked = false;
             document.getElementById('centrofpPubS').checked = false;
             document.getElementById('centrofpPubN').checked = false;
             document.getElementById('centrofpPrivS').checked = false;
             document.getElementById('centrofpPrivN').checked = false;*/

            document.getElementById('nombreAsociacion').disabled = false;

            document.getElementById('fieldsetListadoEntidades').style.display = 'inline';
            document.getElementById('fieldsetEntidad').style.display = 'none';
        } else if ("N" == esAsociacion) {
            document.getElementById('supramunS').disabled = false;
            document.getElementById('supramunN').disabled = false;
            document.getElementById('admLocalS').disabled = false;
            document.getElementById('admLocalN').disabled = false;
            document.getElementById('centrofpPubS').disabled = false;
            document.getElementById('centrofpPubN').disabled = false;
            document.getElementById('centrofpPrivS').disabled = false;
            document.getElementById('centrofpPrivN').disabled = false;
            document.getElementById('agenciaColocacionS').disabled = false;
            document.getElementById('agenciaColocacionN').disabled = false;
            if (document.getElementById('sinAnimoLucroS') != null)
                document.getElementById('sinAnimoLucroS').disabled = false;
            if (document.getElementById('sinAnimoLucroN') != null)
                document.getElementById('sinAnimoLucroN').disabled = false;

            //document.getElementById('nombreAsociacion').value = '';
            document.getElementById('nombreAsociacion').disabled = true;

            document.getElementById('fieldsetListadoEntidades').style.display = 'none';
            document.getElementById('fieldsetEntidad').style.display = 'inline';
        } else {
            document.getElementById('supramunS').disabled = true;
            document.getElementById('supramunN').disabled = true;
            document.getElementById('admLocalS').disabled = true;
            document.getElementById('admLocalN').disabled = true;
            document.getElementById('centrofpPubS').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPrivS').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            document.getElementById('agenciaColocacionS').disabled = true;
            document.getElementById('agenciaColocacionN').disabled = true;
            if (document.getElementById('sinAnimoLucroS') != null)
                document.getElementById('sinAnimoLucroS').disabled = false;
            if (document.getElementById('sinAnimoLucroN') != null)
                document.getElementById('sinAnimoLucroN').disabled = false;

            document.getElementById('supramunS').checked = false;
            document.getElementById('supramunN').checked = false;
            document.getElementById('admLocalS').checked = false;
            document.getElementById('admLocalN').checked = false;
            document.getElementById('centrofpPubS').checked = false;
            document.getElementById('centrofpPubN').checked = false;
            document.getElementById('centrofpPrivS').checked = false;
            document.getElementById('centrofpPrivN').checked = false;
            document.getElementById('agenciaColocacionS').checked = false;
            document.getElementById('agenciaColocacionN').checked = false;
            if (document.getElementById('sinAnimoLucroS') != null)
                document.getElementById('sinAnimoLucroS').checked = false;
            if (document.getElementById('sinAnimoLucroN') != null)
                document.getElementById('sinAnimoLucroN').checked = false;
            document.getElementById('nombreAsociacion').value = '';
            document.getElementById('nombreAsociacion').disabled = true;

            document.getElementById('fieldsetListadoEntidades').style.display = 'none';
            document.getElementById('fieldsetEntidad').style.display = 'none';
        }
    }

    function validarDatosOri14() {
        mensajeValidacion = '';
        var correcto = true;

        if (document.getElementById('esAsociacionN').checked) {
            var valor = document.getElementById('cif').value;
            if (!comprobarCaracteresEspecialesOri14(valor)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.caracteresNoPermitidos")%>';
                return false;
            } else if (!validarCIFOri14(valor) && !validarNifOri14(valor) && !validarNieOri14(valor)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.cifIncorrecto")%>';
                return false;
            }

            valor = document.getElementById('nombre').value;
            if (!comprobarCaracteresEspecialesOri14(valor)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.nombre.caracteresNoPermitidos")%>';
                return false;
            }
            if (<%=convActiva%> > 4) {
                valor = document.getElementById('numAgenciaColocacion').value;
                if (!comprobarCaracteresEspecialesOri14(valor)) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.numAgencia.caracteresNoPermitidos2")%>';
                    return false;
                }
                valor = document.getElementById('numAgenciaColocacionVal').value;
                if (!comprobarCaracteresEspecialesOri14(valor)) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.numAgencia.caracteresNoPermitidos2")%>';
                    return false;
                }
            }

            if (!document.getElementById('supramunS').checked && !document.getElementById('supramunN').checked) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.supramun.obligatorio")%>';
                return false;
            }

            if (!document.getElementById('admLocalS').checked && !document.getElementById('admLocalN').checked) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.admLocal.obligatorio")%>';
                return false;
            }

            if (!document.getElementById('centrofpPubS').checked && !document.getElementById('centrofpPubN').checked) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPub.obligatorio")%>';
                return false;
            }

            if (!document.getElementById('centrofpPrivS').checked && !document.getElementById('centrofpPrivN').checked) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPriv.obligatorio")%>';
                return false;
            }
            if (ejercicioExpedienteJS >= 2018) {
                if (document.getElementById('sinAnimoLucroS') != null && !document.getElementById('sinAnimoLucroS').checked && document.getElementById('sinAnimoLucroN') != null && !document.getElementById('sinAnimoLucroN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.sinanimolucro.obligatorio")%>';
                    return false;
                }
            }
            if (<%=convActiva%> > 4) {
                if (document.getElementById('planIgualdadS')!= null && !document.getElementById('planIgualdadS').checked && document.getElementById('planIgualdadN')!= null &&  !document.getElementById('planIgualdadN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.planIgualdad.obligatorio")%>';
                    return false;
                }
                if (document.getElementById('agenciaColocacionS').checked) {
                    if (document.getElementById('numAgenciaColocacion').value == '') {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.numAgenciaColocacion.obligatorio")%>';
                        return false;
                    }
                }

                if (document.getElementById('certificadoCalidadS') != null && !document.getElementById('certificadoCalidadS').checked && document.getElementById('certificadoCalidadN')!= null && !document.getElementById('certificadoCalidadN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.certificadoCalidad.obligatorio")%>';
                    return false;
                }

                if (document.getElementById('agenciaColocacionValS').checked) {
                    if (document.getElementById('numAgenciaColocacionVal').value == '') {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.numAgenciaColocacion.obligatorio")%>';
                        return false;
                    }
                }

            }
        }

        //if (!validarNumericoOri14(document.getElementById('trayectoriaVal'), 2)) {
        //    mensajeValidacion = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.trayectoriaValIncorrecta"), 2)%>';
        //    return false;
      //  }

        return correcto;
    }

    function guardarDatosOri14() {

        if (validarDatosOri14()) {
            document.getElementById('msgGuardandoDatos').style.display = "inline";
            barraProgresoOri14('on', 'barraProgresoEntidadesSolicitudOri14');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();

            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarDatosOri14&tipo=0&numero=<%=numExpediente%>'
                    + '&codEntidad=' + document.getElementById('codEntidadOri14').value
                    + '&asociacion=' + (document.getElementById('esAsociacionS').checked ? 1 : document.getElementById('esAsociacionN').checked ? 0 : '')
                    + '&cif=' + document.getElementById('cif').value
                    + '&nombre=' + document.getElementById('nombre').value
                    + '&ext_ter=' + '<%=entidad.getExtTer()%>'
                    + '&ext_nvr=' + '<%=entidad.getExtNvr()%>'
                    + '&supramun=' + (document.getElementById('supramunS').checked ? 1 : document.getElementById('supramunN').checked ? 0 : '')
                    + '&admLocal=' + (document.getElementById('admLocalS').checked ? 1 : document.getElementById('admLocalN').checked ? 0 : '')
                    + '&centrofpPub=' + (document.getElementById('centrofpPubS').checked ? 1 : document.getElementById('centrofpPubN').checked ? 0 : '')
                    + '&agenciaColocacion=' + (document.getElementById('agenciaColocacionS').checked ? 1 : document.getElementById('agenciaColocacionN').checked ? 0 : '')
                    + '&agenciaColocacionVal=' + (document.getElementById('agenciaColocacionValS').checked ? 1 : document.getElementById('agenciaColocacionValN').checked ? 0 : '');
            if (<%=convActiva%> > 4) {
                parametros += '&numAgenciaColocacion=' + document.getElementById('numAgenciaColocacion').value;
                parametros += '&numAgenciaColocacionVal=' + document.getElementById('numAgenciaColocacionVal').value;
            }
            parametros += '&centrofpPriv=' + (document.getElementById('centrofpPrivS').checked ? 1 : document.getElementById('centrofpPrivN').checked ? 0 : '')
                    + '&sinAnimoLucro=' + (document.getElementById('sinAnimoLucroS') != null && document.getElementById('sinAnimoLucroS').checked ? 1 : document.getElementById('sinAnimoLucroN') != null && document.getElementById('sinAnimoLucroN').checked ? 0 : '')
                    + '&nombreAsociacion=' + document.getElementById('nombreAsociacion').value
                    + '&aceptaMas=' + (document.getElementById('aceptaMasS').checked ? 1 : document.getElementById('aceptaMasN').checked ? 0 : '')
                    + '&segundosLocalesAmbito=' + (document.getElementById('segundosLocalesAmbitoS').checked ? 1 : document.getElementById('segundosLocalesAmbitoN').checked ? 0 : '')
                    + '&supramunVal=' + (document.getElementById('supramunValS').checked ? 1 : document.getElementById('supramunValN').checked ? 0 : '')
                    + '&admLocalVal=' + (document.getElementById('admLocalValS').checked ? 1 : document.getElementById('admLocalValN').checked ? 0 : '')
                    + '&centrofpPubVal=' + (document.getElementById('centrofpPubValS').checked ? 1 : document.getElementById('centrofpPubValN').checked ? 0 : '')
                    + '&centrofpPrivVal=' + (document.getElementById('centrofpPrivValS').checked ? 1 : document.getElementById('centrofpPrivValN').checked ? 0 : '')
                    + '&sinAnimoLucroVal=' + (document.getElementById('sinAnimoLucroValS') != null && document.getElementById('sinAnimoLucroValS').checked ? 1 : document.getElementById('sinAnimoLucroValN') != null && document.getElementById('sinAnimoLucroValN').checked ? 0 : '');
            if (<%=convActiva%> > 4) {
                parametros += '&planIgualdad=' + (document.getElementById('planIgualdadS') != null && document.getElementById('planIgualdadS').checked ? 1 : document.getElementById('planIgualdadN') != null && document.getElementById('planIgualdadN').checked ? 0 : '');
                parametros += '&certificadoCalidad=' + (document.getElementById('certificadoCalidadS') != null && document.getElementById('certificadoCalidadS').checked ? 1 : document.getElementById('certificadoCalidadN') != null && document.getElementById('certificadoCalidadN').checked ? 0 : '');
                parametros += '&planIgualdadVal=' + (document.getElementById('planIgualdadValS') != null && document.getElementById('planIgualdadValS').checked ? 1 : document.getElementById('planIgualdadValN') != null && document.getElementById('planIgualdadValN').checked ? 0 : '');
                parametros += '&certificadoCalidadVal=' + (document.getElementById('certificadoCalidadValS') != null && document.getElementById('certificadoCalidadValS').checked ? 1 : document.getElementById('certificadoCalidadValN') != null && document.getElementById('certificadoCalidadValN').checked ? 0 : '');
                parametros += '&oriEntTrayectoria='+(document.getElementById('totalMeses')!==null ? document.getElementById('totalMeses').value :"");
                parametros += '&oriEntTrayectoriaVal='+(document.getElementById('totalMesesVal')!==null ? document.getElementById('totalMesesVal').value :"");
                var tempNumber  = 0.0;
                tempNumber = $("#totalMesesVal").val()!= null && $("#totalMesesVal").val()!= undefined && $("#totalMesesVal").val()!="" ? (parseFloat($("#totalMesesVal").val().replace(",","."))*0.2).toFixed(2) : null;
                document.getElementById('trayectoriaVal').value=(tempNumber!=null ? String(tempNumber).replace(".",",") : "");
                // Nuevos campos convocaroria
                parametros += '&sujetoDerPublico='+(document.getElementById('sujetoDerPublicoS')!=null && document.getElementById('sujetoDerPublicoS') != undefined && document.getElementById('sujetoDerPublicoS').checked ? 1 : (document.getElementById('sujetoDerPublicoN')!= null && document.getElementById('sujetoDerPublicoN')!= undefined &&  document.getElementById('sujetoDerPublicoN').checked) ? 0 : null);
                parametros += '&compIgualdadOpcion='+getValorSeleccionadoCompromisoIgualdad();
                parametros +='&certificadoCalidadLista='+getValorSeleccionadoCertificadoCalidad();
                // Datos validados
                parametros +='&entSujetaDerPublValidado='+(document.getElementById('entSujetaDerPublSValidado')!=null && document.getElementById('entSujetaDerPublSValidado') != undefined && document.getElementById('entSujetaDerPublSValidado').checked ? 1 : (document.getElementById('entSujetaDerPublNValidado')!= null && document.getElementById('entSujetaDerPublNValidado')!= undefined &&  document.getElementById('entSujetaDerPublNValidado').checked) ? 0 : null);
                parametros +='&compromisoIgualdadValidado='+getValorSeleccionadoCompromisoIgualdadValidado();
                parametros += '&certificadoCalidadListaValidado='+getValorSeleccionadoCertificadoCalidadValidado();
            }
            parametros += '&trayectoriaVal=' + document.getElementById('trayectoriaVal').value
                    + '&control=' + control.getTime();




            try {

                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                //var formData = new FormData(document.getElementById('formContrato'));
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
                var listaEntidades = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var nodoCampo;
                var j;

                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        listaEntidades[j] = codigoOperacion;
                    } else if (hijos[j].nodeName == "CODIGO_ENTIDAD") {
                        document.getElementById('codEntidadOri14').value = hijos[j].childNodes[0].nodeValue;
                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)

                if (codigoOperacion == "0") {
                    barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    limpiar();
                    refrescarPestanasORI14(1);
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch

            barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
        } else {
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }



    function inicio() {
        if (ejercicioExpedienteJS >= 2018) {
            //A partir de este anio no hay lista ENTIDADES
            document.getElementById('esAsociacionS').disabled = 'true';
            document.getElementById('esAsociacionN').disabled = 'true';
            document.getElementById('esAsociacionS').readOnly = 'true';
            document.getElementById('esAsociacionN').readOnly = 'true';
            document.getElementById('divEsAsociacion').style.display = 'none';
            document.getElementById('esAsociacionN').checked = true;
            document.getElementById('esAsociacionS').checked = false;
        } else
            document.getElementById('divEsAsociacion').style.display = 'block';

    <%
        if (entidad != null) 
        {
    %>

        document.getElementById('codEntidadOri14').value = '<%=entidad.getOriEntCod()%>';
        document.getElementById('nombreAsociacion').value = '<%=entidad.getOriEntNom() != null ? entidad.getOriEntNom() : ""%>';
    <%      if(convActiva > 4){ %>
        document.getElementById('numAgenciaColocacion').value = '<%=entidad.getNumAgenciaColocacion() != null ? entidad.getNumAgenciaColocacion() : ""%>';
        document.getElementById('numAgenciaColocacionVal').value = '<%=entidad.getNumAgenciaColocacionVal() != null ? entidad.getNumAgenciaColocacionVal() : ""%>';

    <%      }
               

            if(entidad.getOriEntAsociacion() != null)
            {
                if(entidad.getOriEntAsociacion().equals(1))
                {
    %>
        document.getElementById('esAsociacionS').checked = 'true';
    <%
                }
                else if(entidad.getOriEntAsociacion().equals(0))
                {
                        
    %>
        document.getElementById('esAsociacionN').checked = 'true';
    <%
                    if(asociaciones != null && asociaciones.size() > 0)
                    {
                        FilaAsociacionVO asoc = asociaciones.get(0);
    %>
        document.getElementById('cif').value = '<%=asoc.getCif() != null ? asoc.getCif() : ""%>';
        document.getElementById('nombre').value = '<%=asoc.getNombre() != null ? asoc.getNombre() : ""%>';
    <%
                        if(asoc.getSupramun() != null)
                        {
                            if(asoc.getSupramun().equals("0"))
                            {
    %>
        document.getElementById('supramunN').checked = 'true';
    <%
                            }
                            else if(asoc.getSupramun().equals("1"))
                            {
    %>
        document.getElementById('supramunS').checked = 'true';
    <%
                            }
                        }
                        if(asoc.getAdmLocal() != null)
                        {
                            if(asoc.getAdmLocal().equals("0"))
                            {
    %>
        document.getElementById('admLocalN').checked = 'true';
    <%
                            }
                            else if(asoc.getAdmLocal().equals("1"))
                            {
    %>
        document.getElementById('admLocalS').checked = 'true';
    <%
                            }
                        }
                        if(asoc.getCentrofpPub() != null)
                        {
                            if(asoc.getCentrofpPub().equals("0"))
                            {
    %>
        document.getElementById('centrofpPubN').checked = 'true';
    <%
                            }
                            else if(asoc.getCentrofpPub().equals("1"))
                            {
    %>
        document.getElementById('centrofpPubS').checked = 'true';
    <%
                            }
                        }
                        if(asoc.getCentrofpPriv() != null)
                        {
                            if(asoc.getCentrofpPriv().equals("0"))
                            {
    %>
        document.getElementById('centrofpPrivN').checked = 'true';
    <%
                            }
                            else if(asoc.getCentrofpPriv().equals("1"))
                            {
    %>
        document.getElementById('centrofpPrivS').checked = 'true';
    <%
                            }
                        }
                        if(asoc.getAgenciaColocacion() != null)
                        {
                            if(asoc.getAgenciaColocacion().equals("0"))
                            {
    %>
        document.getElementById('agenciaColocacionN').checked = 'true';
    <%
                            }
                            else if(asoc.getAgenciaColocacion().equals("1"))
                            {
    %>
        document.getElementById('agenciaColocacionS').checked = 'true';
    <%
                            }
                        }
                        if(asoc.getSinAnimoLucro() != null)
                        {
                            if(asoc.getSinAnimoLucro().equals("0"))
                            {
    %>
        if (ejercicioExpedienteJS >= 2018)
            document.getElementById('sinAnimoLucroN').checked = 'true';
    <%
                            }
                            else if(asoc.getSinAnimoLucro().equals("1"))
                            {
    %>
        if (ejercicioExpedienteJS >= 2018)
            document.getElementById('sinAnimoLucroS').checked = 'true';
    <%
                            }
                        }
                    }
                }
            }else{
               // En el caso de que no este cumplimentado el campo esAsociacion, intento cargar si los hay, datos del nombre y cif de la entidad NO ASOCIACION  metida en la request
               // como una asociacion.
               if(asociaciones != null && asociaciones.size() > 0)
                {
                    FilaAsociacionVO asoc = asociaciones.get(0);
    %>
        document.getElementById('cif').value = '<%=asoc.getCif() != null ? asoc.getCif() : ""%>';
        document.getElementById('nombre').value = '<%=asoc.getNombre() != null ? asoc.getNombre() : ""%>';
    <%
}
}
                
if(entidad.getOriEntSupramunVal() != null)
{
if(entidad.getOriEntSupramunVal().equals("0"))
{
    %>
        document.getElementById('supramunValN').checked = 'true';
    <%                
                }
                else if(entidad.getOriEntSupramunVal().equals("1"))
                {
    %>
        document.getElementById('supramunValS').checked = 'true';
    <%        
                }
            }
                
            if(entidad.getOriEntAdmLocalVal() != null)
            {
                if(entidad.getOriEntAdmLocalVal().equals("0"))
                {
    %>
        document.getElementById('admLocalValN').checked = 'true';
    <%          
                }
                else if(entidad.getOriEntAdmLocalVal().equals("1"))
                {
    %>
        document.getElementById('admLocalValS').checked = 'true';
    <%        
                }
            }
                
            if(entidad.getOriExpCentrofpPubVal() != null)
            {
                if(entidad.getOriExpCentrofpPubVal().equals("0"))
                {
    %>
        document.getElementById('centrofpPubValN').checked = 'true';
    <%        
                }
                else if(entidad.getOriExpCentrofpPubVal().equals("1"))
                {
    %>
        document.getElementById('centrofpPubValS').checked = 'true';
    <%        
                }
            }
                
            if(entidad.getOriExpCentrofpPrivVal() != null)
            {
                if(entidad.getOriExpCentrofpPrivVal().equals("0"))
                {
    %>
        document.getElementById('centrofpPrivValN').checked = 'true';
    <%        
                }
                else if(entidad.getOriExpCentrofpPrivVal().equals("1"))
                {
    %>
        document.getElementById('centrofpPrivValS').checked = 'true';
    <%        
                }
            }

            if(entidad.getAgenciaColocacionVal() != null)
            {
                if(entidad.getAgenciaColocacionVal().equals("0"))
                {
    %>
        document.getElementById('agenciaColocacionValN').checked = 'true';
    <%        
                }
                else if(entidad.getAgenciaColocacionVal().equals("1"))
                {
    %>
        document.getElementById('agenciaColocacionValS').checked = 'true';
    <%        
                }
            }
            if(entidad.getSinAnimoLucroVal() != null)
            {
                if(entidad.getSinAnimoLucroVal() == 0)
                {
    %>
        if (ejercicioExpedienteJS >= 2018)
            document.getElementById('sinAnimoLucroValN').checked = 'true';
    <%        
                }
                else if(entidad.getSinAnimoLucroVal() == 1)
                {
    %>
        if (ejercicioExpedienteJS >= 2018)
            document.getElementById('sinAnimoLucroValS').checked = 'true';
    <%        
                }
            }
           if(convActiva > 4 ){
            if(entidad.getPlanIgualdad() != null)
            {
                if(entidad.getPlanIgualdad() == 0)
                {
    %>
        document.getElementById('planIgualdadN').checked = 'true';
    <%        
                }
                else if(entidad.getPlanIgualdad() == 1)
                {
    %>
        document.getElementById('planIgualdadS').checked = 'true';
    <%        
                }
            }
            if(entidad.getPlanIgualdadVal() != null)
            {
                if(entidad.getPlanIgualdadVal() == 0)
                {
    %>
        document.getElementById('planIgualdadValN').checked = 'true';
    <%        
                }
                else if(entidad.getPlanIgualdadVal() == 1)
                {
    %>
        document.getElementById('planIgualdadValS').checked = 'true';
    <%        
                }
            }
            if(entidad.getCertificadoCalidad() != null)
            {
                if(entidad.getCertificadoCalidad() == 0)
                {
    %>
        document.getElementById('certificadoCalidadN').checked = 'true';
    <%        
                }
                else if(entidad.getCertificadoCalidad() == 1)
                {
    %>
        document.getElementById('certificadoCalidadS').checked = 'true';
    <%        
                }
            }
            if(entidad.getCertificadoCalidadVal() != null)
            {
                if(entidad.getCertificadoCalidadVal() == 0)
                {
    %>
        document.getElementById('certificadoCalidadValN').checked = 'true';
    <%        
                }
                else if(entidad.getCertificadoCalidadVal() == 1)
                {
    %>
        document.getElementById('certificadoCalidadValS').checked = 'true';
    <%        
                }
            }
            }
            if(entidad.getOriEntAceptaMas() != null)
            {
                if(entidad.getOriEntAceptaMas() == 0)
                {
    %>
        document.getElementById('aceptaMasN').checked = 'true';
    <%       
                }
                else if(entidad.getOriEntAceptaMas() == 1)
                {
    %>
        document.getElementById('aceptaMasS').checked = 'true';
    <%       
                }
            }

            if(entidad.getSegundosLocalesAmbito() != null)
            {
                if(entidad.getSegundosLocalesAmbito() == 0)
                {
    %>
        document.getElementById('segundosLocalesAmbitoN').checked = 'true';
    <%       
                }
                else if(entidad.getSegundosLocalesAmbito() == 1)
                {
    %>
        document.getElementById('segundosLocalesAmbitoS').checked = 'true';
    <%       
                }
            }
             if(convActiva > 4){
                if(entidad.getOriValoracionTrayectoria() != null && !entidad.getOriValoracionTrayectoria().equals("NULL"))
                {
    %>
        document.getElementById('trayectoriaVal').value = '<%=entidad.getOriValoracionTrayectoria()%>';
    <%
                } else {%>
        document.getElementById('trayectoriaVal').value = '0';
    <%          } 
            } else {%>
        document.getElementById('trayectoriaVal').value ='<%=entidad.getOriEntTrayectoriaVal()%>';
            <%}
        }
    %>

        cambiaEntidad();
        inicializarTablaEntidadOri14();
        limpiar();
        //Si es una carga nueva, relleno el nombre de la asociacion, que se ha limpiado en cambiaEntidad al no esatr seleccionado ningun checck en EsAoviacion
    <%
        if (entidad != null && entidad.getOriEntAsociacion()== null 
                && asociaciones != null && asociaciones.size() > 0 ) 
        {
    %>
        if (document.getElementById('nombreAsociacion') != null) {
            document.getElementById('nombreAsociacion').value = (document.getElementById('nombre') != null ? document.getElementById('nombre').value : "");
        }
    <%
        }
    %>
    }


    function limpiar() {
        if (!document.getElementById('esAsociacionS').checked) {
            document.getElementById('nombreAsociacion').value = '';
            listaEntidadesOri14Tabla = new Array();
            listaEntidadesOri14Tabla_titulos = new Array();
            listaEntidadesOri14Tabla_estilos = new Array();
            inicializarTablaEntidadOri14();
        } else if (!document.getElementById('esAsociacionN').checked) {
            document.getElementById('cif').value = '';
            document.getElementById('nombre').value = '';
            document.getElementById('supramunS').checked = false;
            document.getElementById('supramunN').checked = false;
            document.getElementById('admLocalS').checked = false;
            document.getElementById('admLocalN').checked = false;
            document.getElementById('centrofpPubS').checked = false;
            document.getElementById('centrofpPubN').checked = false;
            document.getElementById('centrofpPrivS').checked = false;
            document.getElementById('centrofpPrivN').checked = false;
            document.getElementById('agenciaColocacionS').checked = false;
            document.getElementById('agenciaColocacionN').checked = false;
            document.getElementById('numAgenciaColocacion').value = '';
            if (document.getElementById('sinAnimoLucroS') != null)
                document.getElementById('sinAnimoLucroS').checked = false;
            if (document.getElementById('sinAnimoLucroN') != null)
                document.getElementById('sinAnimoLucroN').checked = false;
            if (document.getElementById('planIgualdadS') != null)
                document.getElementById('planIgualdadS').checked = false;
            if (document.getElementById('planIgualdadN') != null)
                document.getElementById('planIgualdadN').checked = false;
            if (document.getElementById('certificadoCalidadS') != null)
                document.getElementById('certificadoCalidadS').checked = false;
            if (document.getElementById('certificadoCalidadN') != null)
                document.getElementById('certificadoCalidadN').checked = false;
            document.getElementById('numAgenciaColocacionVal').value = '';
            if (document.getElementById('planIgualdadValS') != null)
                document.getElementById('planIgualdadValS').checked = false;
            if (document.getElementById('planIgualdadValN') != null)
                document.getElementById('planIgualdadValN').checked = false;
            if (document.getElementById('certificadoCalidadValS') != null)
                document.getElementById('certificadoCalidadValS').checked = false;
            if (document.getElementById('certificadoCalidadValN') != null)
                document.getElementById('certificadoCalidadValN').checked = false;
        }
    }

    function inicializarTablaEntidadOri14() {
        tabEntidadOri14 = new FixedColumnTable(document.getElementById('entidadesOri14'), anchoTabla, 922, 'entidadesOri14');

        tabEntidadOri14.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1.title")%>");
        tabEntidadOri14.addColumna('268', 'left', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2.title")%>");
        tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3.title")%>");
        tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4.title")%>");
        tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5.title")%>");
        tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6.title")%>");
        tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7.title")%>");
        if (ejercicioExpedienteJS >= 2018)
            tabEntidadOri14.addColumna('120', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8.title")%>");

        tabEntidadOri14.numColumnasFijas = 0;

        for (var cont = 0; cont < listaEntidadesOri14Tabla.length; cont++) {
            tabEntidadOri14.addFilaConFormato(listaEntidadesOri14Tabla[cont], listaEntidadesOri14Tabla_titulos[cont], listaEntidadesOri14Tabla_estilos[cont])
        }

        tabEntidadOri14.displayCabecera = true;
        tabEntidadOri14.height = 156;

        tabEntidadOri14.altoCabecera = 30;

        tabEntidadOri14.scrollWidth = 1000;

        tabEntidadOri14.dblClkFunction = 'dblClckTablaEntidadesOri14';

        tabEntidadOri14.displayTabla();

        tabEntidadOri14.pack();
    }

    function refrescarPestanaEntidad() {
        //Se actualizan los datos de la pestaña
    }

    function cambioCentrofpEntidadOri14(id) {
        if (id == 'centrofpPubS') {
            document.getElementById('centrofpPrivN').checked = true;
        } else if (id == 'centrofpPrivS') {
            document.getElementById('centrofpPubN').checked = true;
        }
    }

    function habilitarNumAgencia(id) {
        document.getElementById('numAgenciaColocacion').disabled = false;
        if (document.getElementById('agenciaColocacionS').checked) {
            document.getElementById('numAgenciaColocacion').disabled = false;
        }
        if (document.getElementById('agenciaColocacionN').checked) {
            document.getElementById('numAgenciaColocacion').disabled = true;
            document.getElementById('numAgenciaColocacion').value = '';
        }
    }

    function habilitarNumAgenciaVal(id) {
        document.getElementById('numAgenciaColocacionVal').disabled = false;
        if (document.getElementById('agenciaColocacionValS').checked) {
            document.getElementById('numAgenciaColocacionVal').disabled = false;
        }
        if (document.getElementById('agenciaColocacionValN').checked) {
            document.getElementById('numAgenciaColocacionVal').disabled = true;
            document.getElementById('numAgenciaColocacionVal').value = '';
        }
    }
</script>

<body>
    <div id="barraProgresoEntidadesSolicitudOri14" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoEntidadOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoEntidadOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide"></td>
                                        <td style="width:5%;height:20%;"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" style="height:10%" ></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div style="clear: both;">
        <div class="lineaFormulario" id="divEsAsociacion"> 
            <div style="float: left; width: 120px;">
                <label for="esAsociacion"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.asociacion")%></label>
            </div>
            <div style="width: auto; float: left; margin-left: 10px;" id="esAsociacion">
                <div style="float: left;">
                    <input type="radio" name="esAsociacion" id="esAsociacionS" value="S" onClick="cambiaEntidad()">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="esAsociacion" id="esAsociacionN" value="N" onClick="cambiaEntidad()">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
        </div>
        <br />  
        <fieldset id="fieldsetEntidad" name="fieldsetEntidad" style="height: auto;">
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.datosEntidad")%></legend>
            <div class="lineaFormulario">
                <div style="float: left; width: 60px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.cif")%>
                </div>
                <div>
                    <input type="text" maxlength="15" size="20" id="cif" name="cif" value="" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="float: left; width: 60px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.nombre")%>
                </div>
                <div>
                    <input type="text" maxlength="500" size="60" id="nombre" name="nombre" value="" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.supramun")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="supramun" id="supramunS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="supramun" id="supramunN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.admlocal")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="admLocal" id="admLocalS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="admLocal" id="admLocalN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPub")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="centrofpPub" id="centrofpPubS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="centrofpPub" id="centrofpPubN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPriv")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="centrofpPriv" id="centrofpPrivS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="centrofpPriv" id="centrofpPrivN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.agenciaColocacion")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="agenciaColocacion" id="agenciaColocacionS" value="S" onclick="habilitarNumAgencia(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="agenciaColocacion" id="agenciaColocacionN" value="N" onclick="habilitarNumAgencia(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <% if(convActiva > 4){ %>
            <div class="lineaFormulario">
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.numAgenciaColocacion")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <input type="text" maxlength="50" size="25" id="numAgenciaColocacion" name="numAgenciaColocacion" value="" class="inputTexto"/>
                </div>
            </div>
            <% } %>
            <% if(ejercicioExpediente>=2018){ %>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 350px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.sinanimolucro")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <input type="radio" name="sinAnimoLucro" id="sinAnimoLucroS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="sinAnimoLucro" id="sinAnimoLucroN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
            <% } %>

            <c:if test="${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id > 4}">
                <c:choose>
                    <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id < 30}">
                        <div class="lineaFormulario">
                            <div style="float: left; width: 350px; margin-left: 30px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.planIgualdad")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="planIgualdad" id="planIgualdadS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="planIgualdad" id="planIgualdadN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 350px; margin-left: 30px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.certificadoCalidad")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="certificadoCalidad" id="certificadoCalidadS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="certificadoCalidad" id="certificadoCalidadN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="lineaFormulario">
                             <div style="float: left; width: 350px; margin-left: 30px;" id="lblSujetoDerPublico">
                                 <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.sujeto.derecho.publico")%>
                             </div>
                             <div style="width: auto; float: left; margin-left: 10px;">
                                 <div style="float: left;">
                                     <input type="radio" name="sujetoDerPublico" id="sujetoDerPublicoS" value="S" <c:if test="${entidad!=null && entidad.entSujetaDerPubl!=null && entidad.entSujetaDerPubl==1}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                     <input type="radio" name="sujetoDerPublico" id="sujetoDerPublicoN" value="N" <c:if test="${entidad!=null && entidad.entSujetaDerPubl!=null && entidad.entSujetaDerPubl==0}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                 </div>
                             </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 350px; margin-left: 30px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%>
                            </div>
                            <div style="width: 80%; float: left; margin-left: 40px;">
                                <div style="float: left;">
                                    <table style="text-align:justify;" id="tableCompromisoIgualdad">
                                        <c:forEach items="${listaCompromisoIgualdad}" var="compromisoIgualdad" varStatus="contadorCI">
                                            <tr>
                                                <td style="padding:5px;"><input type="radio" name="compromisoIgualdad" id="compromisoIgualdad_<c:out value="${compromisoIgualdad.id}"/>" value="<c:out value="${compromisoIgualdad.id}"/>"
                                                <c:if test="${entidad!=null && entidad.compIgualdadOpcion!=null && entidad.compIgualdadOpcion==compromisoIgualdad.id}">checked</c:if>/></td>
                                                <td><span><c:out value = "${compromisoIgualdad.label}"/></span></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 350px; margin-left: 30px;" id="lblCertificadoCalidad">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.certificadoCalidad")%>
                            </div>
                            <div style="width: 80%; float: left; margin-left: 40px;">
                                <div style="float: left;">
                                    <table style="text-align:justify;" id="tableCertificadoCalidad">
                                        <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidad" varStatus="contadorCC">
                                            <tr>
                                                <td style="padding:5px;"><input type="checkbox" name="certificadoCalidad_<c:out value="${certificadoCalidad.id}"/>" id="certificadoCalidad_<c:out value="${certificadoCalidad.id}"/>" value="<c:out value="${certificadoCalidad.id}"/>"/></td>
                                                <td><span><c:out value = "${certificadoCalidad.label}"/></span></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </fieldset>
        <br />
        <fieldset id="fieldsetListadoEntidades" name="fieldsetListadoEntidades">
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.listaAsociaciones")%></legend>
            <div class="lineaFormulario">
                <div style="float: left; width: fit-content; margin-left: 20px">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.nombreAsociacion")%>
                </div>
                <div style="margin-left: 5px">
                    <input style="margin-left: 5px" type="text" maxlength="80" size="60" id="nombreAsociacion" name="nombreAsociacion" value="" class="inputTexto"/>
                </div>
            </div>
            <div style="clear: both;">
                <div>
                    <div id="entidadesOri14" name="entidadesOri14" style="font-size: 11px" ></div>
                    <div class="botonera" id="botoneraEntidad" name="botoneraEntidad">
                        <input type="button" id="btnNuevaEntidad" name="btnNuevaEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaAsociacionOri14();">
                        <input type="button" id="btnModificarEntidad" name="btnModificarEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAsociacionOri14();">
                        <input type="button" id="btnEliminarEntidad" name="btnEliminarEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAsociacionOri14();">
                    </div>
                </div>
            </div>
        </fieldset>           
        <br />
        <fieldset id="fieldsetAceptaMas" name="fieldsetAceptaMas" >
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.aceptaMas")%></legend>
            <div class="lineaFormulario">
                <div class="col" style="float: left; width: 564px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.aceptaMas")%>
                </div>
                <div style="float: left; margin-left: 25px;">
                    <input type="radio" name="aceptaMas" id="aceptaMasS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="aceptaMas" id="aceptaMasN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
        </fieldset>
        <fieldset id="fieldsetsegundosLocalesAmbito" name="fieldsetsegundosLocalesAmbito" >
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.ofrecenSegundosLocales")%></legend>
            <div class="lineaFormulario">
                <div class="col"  style="float: left; width: 564px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.ofrecenSegundosLocales")%>
                </div>
                <div style="float: left; margin-left: 25px;">
                    <input type="radio" name="segundosLocalesAmbito" id="segundosLocalesAmbitoS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="segundosLocalesAmbito" id="segundosLocalesAmbitoN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
        </fieldset>
        <fieldset id="fieldsetValidacion" class="jumbotron" style="border:2px solid #17a2b8; margin:20px;" name="fieldsetValidacion" >
            <legend class="lineaFormularioFont badge badge-info"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.validacion")%></legend>
            <div class="lineaFormularioPL10">
                <div style="float: left; width: 240px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.supramun")%>
                </div>
                <div style="float: left;">  <!--disabled="true" readonly="true" -->
                    <input type="radio" name="supramunVal" id="supramunValS" value="S" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="supramunVal" id="supramunValN" value="N" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div style="float: left; width: 240px; margin-left: 100px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.admlocal")%>
                </div>
                <div style="float: left;">
                    <input type="radio" name="admLocalVal" id="admLocalValS" value="S" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="admLocalVal" id="admLocalValN" value="N" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
            <div class="lineaFormularioPL10">
                <div style="float: left; width: 240px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPub")%>
                </div>
                <div style="float: left;">
                    <input type="radio" name="centrofpPubVal" id="centrofpPubValS" value="S" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="centrofpPubVal" id="centrofpPubValN" value="N" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div style="float: left; width: 240px; margin-left: 100px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPriv")%>
                </div>
                <div style="float: left;">
                    <input type="radio" name="centrofpPrivVal" id="centrofpPrivValS" value="S" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="centrofpPrivVal" id="centrofpPrivValN" value="N" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>             
            </div>                        
            <div class="lineaFormularioPL10">
                <div style="float: left; width: 240px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.agenciaColocacion")%>
                </div>
                <div style="float: left;">
                    <input type="radio" name="agenciaColocacionVal" id="agenciaColocacionValS" value="S" onclick="habilitarNumAgenciaVal(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="agenciaColocacionVal" id="agenciaColocacionValN" value="N" onclick="habilitarNumAgenciaVal(this.id);">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <c:if test="${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id > 4}">
                    <div style="float: left; width: 240px; margin-left: 100px;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.numAgenciaColocacion")%>
                    </div>
                    <div style="width: auto; float: left;">
                        <input type="text" maxlength="50" size="25" id="numAgenciaColocacionVal" name="numAgenciaColocacionVal" value="" class="inputTexto"/>
                    </div>
                </c:if>
            </div>
            <% if(ejercicioExpediente>=2018){ %>
                <div class="lineaFormularioPL10">
                    <div style="float: left; width: 240px;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.sinanimolucro")%>
                    </div>
                    <div style="float: left;">
                        <input type="radio" name="sinAnimoLucroVal" id="sinAnimoLucroValS" value="S" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="sinAnimoLucroVal" id="sinAnimoLucroValN" value="N" >&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            <% }%>
            <c:if test="${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id > 4}">
                <c:choose>
                    <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id < 30}">
                        <div class="lineaFormularioPL10">
                            <div style="float: left; width: 240px; margin-left: 100px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.planIgualdad")%>
                            </div>
                            <div style="width: auto; float: left; ">
                                <div style="float: left;">
                                    <input type="radio" name="planIgualdadVal" id="planIgualdadValS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="planIgualdadVal" id="planIgualdadValN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormularioPL10">
                            <div style="float: left; width: 240px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.certificadoCalidad")%>
                            </div>
                            <div style="width: auto; float: left; ">
                                <div style="float: left;">
                                    <input type="radio" name="certificadoCalidadVal" id="certificadoCalidadValS" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="certificadoCalidadVal" id="certificadoCalidadValN" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormularioPL10">
                            <div style="float: left; width: 240px; margin-left: 100px; margin-right: 10px;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.trayectoria21")%>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="lineaFormularioPL10">
                            <div class="container-fluid">
                                <div class="row">
                                    <div style="float: left; width: 240px;">
                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.sujeto.derecho.publico")%>
                                    </div>
                                    <div style="float: left;">
                                        <input type="radio" name="entSujetaDerPublValidado" id="entSujetaDerPublSValidado" value="S" <c:if test="${entidad!=null && entidad.entSujetaDerPublVal!=null && entidad.entSujetaDerPublVal>0}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                        <input type="radio" name="entSujetaDerPublValidado" id="entSujetaDerPublNValidado" value="N" <c:if test="${entidad!=null && entidad.entSujetaDerPublVal !=null && entidad.entSujetaDerPublVal==0}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormularioPL10">
                            <div class="container-fluid">
                                <div class="row">
                                    <div style="float: left; width: 240px;">
                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%>
                                    </div>
                                    <div style="float: left;">
                                        <input type="radio" name="compIgualdadOpcionValidado" id="compIgualdadOpcionSValidado" value="S" <c:if test="${entidad!=null && entidad.compIgualdadOpcionVal!=null && entidad.compIgualdadOpcionVal>0}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                        <input type="radio" name="compIgualdadOpcionValidado" id="compIgualdadOpcionNValidado" value="N" <c:if test="${entidad!=null && entidad.compIgualdadOpcionVal !=null && entidad.compIgualdadOpcionVal==0}">checked</c:if>>&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormularioPL10">
                            <div id="lblCertificadoCalidadVal">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.certificadoCalidad")%>
                            </div>
                        </div>
                        <div class="lineaFormularioPL10">
                            <div class="container-fluid">
                                <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidadVOP" varStatus="contadorCCVOP">
                                    <div class="row">
                                        <div class="col-md-5">
                                            <span><c:out value = "${certificadoCalidadVOP.label}"/></span>
                                        </div>
                                        <div class="col-md-5" style="align-self:center;" id="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.id}"/>">
                                            <input type="radio" name="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.id}"/>" id="certificadoCalidadOpcionValTS_<c:out value = "${certificadoCalidadVOP.id}"/>" value="S">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                            <input type="radio" name="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.id}"/>" id="certificadoCalidadOpcionValTN_<c:out value = "${certificadoCalidadVOP.id}"/>" value="N">&nbsp;<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div class="lineaFormularioPL10">
                <div class="lineaFormulario">
                    <div style="float: left; width: 240px;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.trayectoria")%>
                    </div>
                    <div style="float: left;">
                        <input type="text" name="trayectoriaVal" id="trayectoriaVal" value="" maxlength="5" size="5" class="inputTexto" style="text-align:right;" disabled/>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
    <div style="clear: both; margin-top: 15px;">
        <div class="botonera" style="text-align: right;width: 95%; margin-bottom: 15px;">
            <input type="button" id="btnGuardarEntidadOri14" name="btnGuardarEntidadOri14" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatosOri14();">
        </div>
    </div>
    <input type="hidden" id="codEntidadOri14" name="codEntidadOri14" value=""/>
</body>

<script type="text/javascript">
    var tabEntidadOri14;
    var listaEntidadesOri14 = new Array();
    var listaEntidadesOri14Tabla = new Array();
    var listaEntidadesOri14Tabla_titulos = new Array();
    var listaEntidadesOri14Tabla_estilos = new Array();

    var supramun;
    var admLocal;
    var centrofpPub;
    var centrofpPriv;
    var agenciaColocacion;
    var sinAnimoLucro;

    <%
        if(entidad != null && entidad.getOriEntAsociacion() != null && entidad.getOriEntAsociacion().equals(1))
        {
            if(asociaciones != null && asociaciones.size() > 0)
            {
                FilaAsociacionVO fila = null;
                for(int i = 0; i < asociaciones.size(); i++)
                {
                    fila = asociaciones.get(i);
    %>

    supramun = '<%=fila.getSupramun() != null ? (fila.getSupramun().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    admLocal = '<%=fila.getAdmLocal() != null ? (fila.getAdmLocal().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    centrofpPub = '<%=fila.getCentrofpPub() != null ? (fila.getCentrofpPub().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    centrofpPriv = '<%=fila.getCentrofpPriv() != null ? (fila.getCentrofpPriv().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    agenciaColocacion = '<%=fila.getAgenciaColocacion() != null ? (fila.getAgenciaColocacion().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    sinAnimoLucro = '<%=fila.getSinAnimoLucro() != null ? (fila.getSinAnimoLucro().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
    if (ejercicioExpedienteJS < 2018) {
        listaEntidadesOri14[<%=i%>] = ['<%=fila.getCodEntidad()%>', '<%=fila.getCodAsociacion()%>', '<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion];
        listaEntidadesOri14Tabla[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion];
        listaEntidadesOri14Tabla_titulos[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion];
    } else {
        listaEntidadesOri14[<%=i%>] = ['<%=fila.getCodEntidad()%>', '<%=fila.getCodAsociacion()%>', '<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion, sinAnimoLucro];
        listaEntidadesOri14Tabla[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion, sinAnimoLucro];
        listaEntidadesOri14Tabla_titulos[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv, agenciaColocacion, sinAnimoLucro];
    }
    <%
                }
            }
        }
    %>

    inicio();
</script>

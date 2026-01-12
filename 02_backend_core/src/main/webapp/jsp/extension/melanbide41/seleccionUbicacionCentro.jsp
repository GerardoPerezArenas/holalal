<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConfigurationParameter" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
   int idiomaUsuario = 1;
   int apl = 5;
   String css = "";
   if(request.getParameter("idioma") != null){
       try
       {
           idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
       }
       catch(Exception ex)
       {}
   }

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
   catch(Exception ex){
   }

   //Clase para internacionalizar los mensajes de la aplicación.            
   MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();

   Config m_Config = ConfigServiceHelper.getConfig("common");
   String statusBar = m_Config.getString("JSP.StatusBar");
   String nombreModulo     = request.getParameter("nombreModulo");
   String codOrganizacion  = request.getParameter("codOrganizacionModulo");
   String numExpediente    = request.getParameter("numero");

   String codigoCampoCodigoCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO","MELANBIDE41");
   String codigoCampoCodigoUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION","MELANBIDE41");
   String codigoCampoCodigoCenso = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOSILCOI","MELANBIDE41");
   String codigoCampoCodigoCensoLanbide = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOLANBIDE","MELANBIDE41");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<div class="tab-page" id="tabPage900" style="height:380px; width: 100%;">
    <h2 class="tab" id="pestana900"><%=meLanbide41I18n.getMensaje(idiomaUsuario, "label.seleccionUbicacionCentros.tituloPestana")%></h2>
    <script type="text/javascript">tp1_p900 = tp1.addTabPage(document.getElementById("tabPage900"));</script>
    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide41I18n.getMensaje(idiomaUsuario, "listaUbicaciones.legend.titulo")%></label>
    <div id="listaUbicacionesCentro" name="listaUbicacionesCentro" align="center"></div>
    <div class="botonera">
        <br/>
        <input type="button" id="btnConsultar" name="btnConsultar" class="botonMasLargo"  value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.consultarUbicaciones")%>" onclick="pulsarConsultarUbicaciones();"/>
        <input type="button" id="btnSeleccionar" name="btnSeleccionar" class="botonMasLargo"  value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.seleccionarUbicacion")%>" onclick="pulsarSeleccionarUbicacion();"/>
    </div>     
</div>
<script type="text/javascript">
    // Variable que contiene el código de centro
    var codigoCenso;
    var codigoCentro;
    var codigoCensoLanbide;
    var ubicaciones = new Array();
    var campoCodigoCentro = "<%=codigoCampoCodigoCentro%>";
    var campoCodigoUbicacion = "<%=codigoCampoCodigoUbicacion%>";
    var campoCodigoCenso = "<%=codigoCampoCodigoCenso%>";
    var campoCodigoCensoLanbide = "<%=codigoCampoCodigoCensoLanbide%>";

    // Se crea la tabla de búsqueda de ubicaciones del centro        
    var tabUbicaciones = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaUbicacionesCentro'), 930);
    tabUbicaciones.addColumna('60', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna1")%>");
    tabUbicaciones.addColumna('150', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna2")%>");
    tabUbicaciones.addColumna('30', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna3")%>");
    tabUbicaciones.addColumna('150', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna4")%>");
    tabUbicaciones.addColumna('44', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna5")%>");
    tabUbicaciones.addColumna('50', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna6")%>");
    tabUbicaciones.addColumna('45', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna7")%>");
    tabUbicaciones.addColumna('45', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna8")%>");
    tabUbicaciones.addColumna('40', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna9")%>");
    tabUbicaciones.addColumna('90', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna10")%>");
    tabUbicaciones.addColumna('90', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna11")%>");
    tabUbicaciones.addColumna('60', 'center', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"ubicacionesCentro.tablaUbicaciones.columna12")%>");

    tabUbicaciones.displayCabecera = true;
    tabUbicaciones.height = 250;
    tabUbicaciones.displayTabla();

    function pulsarConsultarUbicaciones() {

        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=consultarUbicaciones&tipo=0&numero=<%=numExpediente%>";
        try {
            limpiarTablaUbicaciones();
            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            var xmlDoc = null;
            if (ajax.readyState == 4 && ajax.status == 200) {

                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    // En IE el XML viene en responseText y no en la propiedad responseXML
                    var text = ajax.responseText;
                    xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                    xmlDoc.async = "false";
                    xmlDoc.loadXML(text);

                } else {
                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                    xmlDoc = ajax.responseXML;
                }
            }

            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var nodoFila;
            var hijosFila;

            var indice = 0;

            for (j = 0; hijos != null && j < hijos.length; j++) {
                if (hijos[j].nodeName == "STATUS") {
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                } else
                if (hijos[j].nodeName == "CODCENTRO") {
                    codigoCentro = hijos[j].childNodes[0].nodeValue;
                } else
                if (hijos[j].nodeName == "CODCENSO") {
                    codigoCenso = hijos[j].childNodes[0].nodeValue;
                    if (codigoCenso == "null") {
                        codigoCenso = "";
                    }
                } else
                if (hijos[j].nodeName == "CODCENSOLANBIDE") {
                    codigoCensoLanbide = hijos[j].childNodes[0].nodeValue;
                } else
                if (hijos[j].nodeName == "UBICACIONES") {

                    // Se recuperan las ubicaciones
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;

                    for (var cont = 0; cont < hijosFila.length; cont++) {
                        var ubicacion = hijosFila[cont];

                        if (ubicacion.nodeName != null && ubicacion.nodeName == "UBICACION") {
                            var datosUbicacion = ubicacion.childNodes;

                            var listaUbicacion = new Array();
                            for (var z = 0; datosUbicacion != null && z < datosUbicacion.length; z++) {

                                if (datosUbicacion[z].nodeName == "CODUBICACION") {
                                    listaUbicacion[0] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "NOMBREUBICACION") {
                                    listaUbicacion[1] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "TIPOCALLE") {
                                    listaUbicacion[2] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "NOMBRECALLE") {
                                    listaUbicacion[3] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "NUMEROCALLE") {
                                    listaUbicacion[4] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "BIS") {
                                    listaUbicacion[5] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "ESCALERA") {
                                    listaUbicacion[6] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "PISO") {
                                    listaUbicacion[7] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "PUERTA") {
                                    listaUbicacion[8] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "DESCPROVINCIA") {
                                    listaUbicacion[9] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "DESCMUNICIPIO") {
                                    listaUbicacion[10] = datosUbicacion[z].childNodes[0].nodeValue;
                                } else
                                if (datosUbicacion[z].nodeName == "CODPOSTAL") {
                                    listaUbicacion[11] = datosUbicacion[z].childNodes[0].nodeValue;
                                }
                            }// for
                            ubicaciones[indice] = listaUbicacion;
                            indice++;
                        }//if                            
                    }// for
                }
            }//for


            if (codigoOperacion == "0") {
                actualizarTablaUbicaciones(ubicaciones);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.tecnico.recuperar.documento.interesado")%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.expediente.sin.interesado.roldefecto")%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.tecnico.recuperar.codigo.centro")%>');
            } else if (codigoOperacion == "4") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.codigo.centro")%>');
            } else if (codigoOperacion == "5") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.tecnico.recuperar.ubicaciones")%>');
            } else if (codigoOperacion == "6") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.centro.sin.ubicaciones")%>');
            } else if (codigoOperacion == "7") {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.tecnico.ejecucion.consultarUbicaciones")%>');
            } else
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
        }
    }

    function pulsarSeleccionarUbicacion() {
        var index = tabUbicaciones.selectedIndex;
        if (index == -1) {
            jsp_alerta("A", "<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.no.seleccionubicacion")%>");
        } else
        if (codigoCentro == null || codigoCentro == "") {
            jsp_alerta("A", "<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.grabar.ubicacion.centro.desconocido")%>");

        } else {
            var lista = ubicaciones[index];
            var codigoUbicacion = lista[0];
            var codTramite = document.forms[0].codTramite.value;
            var ocuTramite = document.forms[0].ocurrenciaTramite.value;
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=grabarUbicacionSeleccionada&tipo=1&numero=<%=numExpediente%>&codigoCentro=" + codigoCentro + "&codigoUbicacion=" + codigoUbicacion + "&codTramite=" + codTramite + "&ocuTramite=" + ocuTramite;

            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);
                var xmlDoc = null;
                if (ajax.readyState == 4 && ajax.status == 200) {
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }
                }

                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;

                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "STATUS") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    } else
                    if (hijos[j].nodeName == "CODUBIC") {
                        codigoCenso = "";
                        if (hijos[j].childNodes.length > 0) {
                            codigoCenso = hijos[j].childNodes[0].nodeValue;
                            if (codigoCenso == "null") {
                                codigoCenso = "";
                            }
                        }
                    } else
                    if (hijos[j].nodeName == "CODCENSOLANBIDE") {
                        if (hijos[j].childNodes.length > 0) {
                            codigoCensoLanbide = hijos[j].childNodes[0].nodeValue;
                        } else
                            codigoCensoLanbide = "";
                    }
                }//for

                if (codigoOperacion == "0") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"grabacion.centro.ubicaciones.correcto")%>');
                    actualizarValoresCamposSuplementarios(codigoCentro, codigoUbicacion, codigoCenso, codigoCensoLanbide);
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.codigos.campos.centro.ubicacion")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.codigos.campos.centro.configuracion")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.codigos.campos.ubicacion.configuracion")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.grabar.codigo.centro.campo")%>');
                } else if (codigoOperacion == "5") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.grabar.codigo.ubicacion.campo")%>');
                } else if (codigoOperacion == "6") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.tecnico.grabar.codigo.ubicacion")%>');
                } else if (codigoOperacion == "7") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.campos.silicoi")%>');
                } else if (codigoOperacion == "8") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.campos.corr")%>');
                } else if (codigoOperacion == "9") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.campos.corrServicio")%>');
                } else if (codigoOperacion == "10") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.campos.corrSubservicio")%>');
                } else if (codigoOperacion == "11") {
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.recuperar.campos.lanbide")%>');
                } else
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }
        }
    }

    function actualizarTablaUbicaciones(ubicaciones) {
        try {
            tabUbicaciones.lineas = ubicaciones;
            tabUbicaciones.displayTabla();
        } catch (Err) {
            alert("Err.description: " + Err.description);
        }
    }

    function limpiarTablaUbicaciones() {
        try {
            tabUbicaciones.lineas = new Array();
            tabUbicaciones.displayTabla();
        } catch (Err) {
            alert("Err.description: " + Err.description);
        }
    }

    function actualizarValoresCamposSuplementarios(codCentro, codUbicacion, codCenso, codCensoLanbide) {
        var codTramite = document.forms[0].codTramite.value;
        var ocuTramite = document.forms[0].ocurrenciaTramite.value;

        try {
            eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_" + campoCodigoCentro + ".value='" + codCentro + "'");
            eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_" + campoCodigoUbicacion + ".value='" + codUbicacion + "'");
            eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_" + campoCodigoCenso + ".value='" + codCenso + "'");
            eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_" + campoCodigoCensoLanbide + ".value='" + codCensoLanbide + "'");

        } catch (Err) {
            alert("actualizarValoresCamposSuplementarios : " + Err.description);
        }
    }
</script>
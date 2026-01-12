<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosPeriodoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.i18n.MeLanbide01I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.MeLanbide01ValidatorResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter"
             type="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter" />
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }

    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma): idioma);

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo = request.getParameter("nombreModulo");
    
    String codError   = (String) request.getAttribute("error");
    
    String porcentaje = (String)request.getAttribute("PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS");
    String codCampoImporteSubvencion = (String)request.getAttribute("CODIGO_CAMPO_SUPLEMENTARIO_IMPORTE_SUBVENCION");
    String codCampoTotalDiasConcedidos = (String)request.getAttribute("CODIGO_CAMPO_SUPLEMENTARIO_TOTAL_DIAS_CONCEDIDOS");
    
    DatosCalculoVO calculo = (DatosCalculoVO)request.getAttribute("datos_calculo");
    double porcentajeGrabado = calculo.getDescuento();
    ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
    String totalNumDias = new String();
    String gastoTotal = new String();
    int descuento = 0;
    double totalGastosConDescuento = 0;
    String diasRestantes = new String();
    
    if(calculo!=null){
        if(calculo.getPeriodos() != null){
            periodos = calculo.getPeriodos();
            totalNumDias = calculo.getSumaTotalDiasPeriodos();
            gastoTotal = calculo.getImporteSubvencionado();
            descuento  = calculo.getDescuento();
            totalGastosConDescuento = calculo.getTotalConDescuento();
            diasRestantes = calculo.getDiasRestantesSubvencionables();
        }
    }
    
    String urlCausantesSubvencion=(String) request.getAttribute("urlCausantesSubvencion");
    String urlHistorialSubvencion=(String) request.getAttribute("urlHistorialSubvencion");
    Melanbide01Decreto melanbide01Decreto = (Melanbide01Decreto)request.getAttribute("melanbide01Decreto");
    MeLanbide01I18n meLanbide01I18n = MeLanbide01I18n.getInstance();
    
    List<MeLanbide01ValidatorResult> validacionesAlarmasCONCM  = (ArrayList<MeLanbide01ValidatorResult>)request.getAttribute("validacionesAlarmasCONCM");
    Gson gson = new Gson();
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    gsonB.serializeNulls();  
    gson=gsonB.create();
    String validacionesAlarmasCONCMJsonString = gson.toJson(validacionesAlarmasCONCM);    
    String limiteReglaMinimis=(String)request.getAttribute("limiteReglaMinimis");
    String impTotalRecibidoEmpreReglaMinimis=(String)request.getAttribute("impTotalRecibidoEmpreReglaMinimis");
%>

<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/meLanbide01/lanbide.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide01/lanbide.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide01/gestionConciliacionSubvencion.js"></script>

<!-- Modal Para Gestion de Mensajes -->
<jsp:include page="/jsp/extension/melanbide01/modal/melanbide01ModalMensajes.jsp" flush="true">
    <jsp:param name="idiomaUsuarioModal" value="<%=usuarioVO.getIdioma()%>" />
    <jsp:param name="codOrganizacionModal" value="<%=usuarioVO.getOrgCod()%>" />
    <jsp:param name="idUsuarioModal" value="<%=usuarioVO.getIdUsuario()%>" />
    <jsp:param name="aplModal" value="<%=usuarioVO.getAppCod()%>" />
    <jsp:param name="cssModal" value="<%=usuarioVO.getCss()%>" />
    <jsp:param name="appContextPathModal" value="<%=request.getContextPath()%>" />
</jsp:include>

<script type="text/javascript">

    var nombreModulo = "<%=request.getParameter("nombreModulo")%>";
    var numExpediente = "<%=request.getParameter("numero")%>";
    var operacion = "<%=request.getParameter("operacionProceso")%>";
    var codOrganizacion = "<%=request.getParameter("codOrganizacionModulo")%>";
    var tipo = "<%=request.getParameter("tipo")%>";
    var ejercicio = "<%=request.getParameter("ejercicio")%>";
    var codProcedimiento = "<%=request.getParameter("codProcedimiento")%>";
    var contadorParamsConfig = 0;
    //Mantiene en todo momento los valores de la tabla
    var registros = new Array();
    var codError = <%=codError%>
    var porcentaje = <%=porcentaje%>
    var descuento = <%=descuento%>
    var totalSinDescuento = <%=gastoTotal%>;
    var totalConDescuento = 0;

    var codCampoImporteSubvencion = "<%=codCampoImporteSubvencion%>";
    var codCampoTotalDiasConcedidos = "<%=codCampoTotalDiasConcedidos%>";

    function comprobarFechaLanbide(inputFecha) {
        var formato = 'dd/mm/yyyy';
        if (Trim(inputFecha.value) != '') {
            var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
            if (!D[0]) {
                jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_FECHA_NO_VALIDA_" + idiomaUsuario,nombreModulo)%>');
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

    //Recupera los datos de las cajas de texto y comprueba que la fecha de inicio sea posterior a la de fin, ademas comprueba tambien
    //que los datos obligatorios esten cubiertos
    function pulsarAlta() {
        var fechaDesde = document.getElementById("meLanbide01FechaDesde").value;
        var fechaHasta = document.getElementById("meLanbide01FechaHasta").value;
        var porcSubvenc = document.getElementById("meLanbide01PorcSubvenc").value;
        var numDias = document.getElementById("meLanbide01NumDias").value;
        var baseCotiz = document.getElementById("meLanbide01BaseCotizacion").value;
        var bonificacion = document.getElementById("meLanbide01Bonificacion").value;
        var gasto = document.getElementById("meLanbide01Gasto").value;
        var porcJornRealizada = $("#porcJornRealizada").val();
        var porcJornSutitucion = $("#porcJornSustitucion").val();

        if (fechaDesde != "" && fechaHasta != "" && numDias != "" && baseCotiz != "" && bonificacion != "") {
            var mensaje = '<%=configuracion.getParameter("MSG_ERROR_INTERVALO_FECHA_INCORRECTO_" + idiomaUsuario,nombreModulo)%>';
            var fecCorrecta = validarFechaAnteriorLanbide(fechaDesde, fechaHasta, mensaje);
            if (fecCorrecta) {
                comprobarInsercion();
                actualizarCampoOculto();
            }//if(fecCorrecta)
        } else {
            jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_CAMPOS_OBLIGATORIOS_" + idiomaUsuario,nombreModulo)%>');
        }//if(fechaDesde!="" && fechaHasta!="" && jornada!="" && numDias!="" && cotizacionCC!="" && baseCotizacion!="" && importe!="")
    }//pulsarAlta

    //Recupera los datos de las cajas de texto y comprueba que la fecha de inicio sea posterior a la de fin, ademas comprueba tambien
    //que los datos obligatorios esten cubiertos
    function pulsarModificar() {
        var fechaDesde = document.getElementById("meLanbide01FechaDesde").value;
        var fechaHasta = document.getElementById("meLanbide01FechaHasta").value;
        var porcSubvenc = document.getElementById("meLanbide01PorcSubvenc").value;
        var numDias = document.getElementById("meLanbide01NumDias").value;
        var baseCotiz = document.getElementById("meLanbide01BaseCotizacion").value;
        var bonificacion = document.getElementById("meLanbide01Bonificacion").value;
        var gasto = document.getElementById("meLanbide01Gasto").value;
        var porcJornRealizada = $("#porcJornRealizada").val();
        var porcJornSutitucion = $("#porcJornSustitucion").val();

        try {
            var indice = tabConciliacion.selectedIndex;
            if (indice == -1) {
                jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_SELECCION_PERIODO_" + idiomaUsuario,nombreModulo)%>');
            } else {
                if (fechaDesde != "" && fechaHasta != "" && porcSubvenc != "" && numDias != "" && baseCotiz != "" && bonificacion != "") {
                    var mensaje = '<%=configuracion.getParameter("MSG_ERROR_INTERVALO_FECHA_INCORRECTO_" + idiomaUsuario,nombreModulo)%>';
                    var fecCorrecta = validarFechaAnteriorLanbide(fechaDesde, fechaHasta, mensaje);
                    if (fecCorrecta) {
                        comprobarModificacion(indice);
                    }//if(fecCorrecta)
                }//if(fechaDesde!="" && fechaHasta!="" && porcSubvenc!="" && numDias!="" && baseCotiz!="" && bonificacion!="" && gasto!="")
                else {
                    jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_CAMPOS_OBLIGATORIOS_" + idiomaUsuario,nombreModulo)%>');
                }//else
            }//if(indice==-1)
        } catch (Err) {
            jsp_alerta("A", "error: " + Err.message);
        }
    }//pulsarModificar

    //Elimina la fila seleccionada de la tabla de periodos.
    function pulsarEliminar() {
        var indice = tabConciliacion.selectedIndex;
        if (indice != -1) {
            limpiarr();
            eliminar(indice);
            actualizarCampoOculto();
        } else {
            jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_SELECCION_PERIODO_" + idiomaUsuario,nombreModulo) %>');
        }//if(indice!=-1)
    }//pulsarEliminar



    //Llama via AJAX a una funcion de la clase del modulo que se encarga de grabar los datos de los periodos y actualizar los
    //campos suplementarios implicados
    function pulsarGrabar() {

        var fechaDesde = document.getElementById("meLanbide01FechaDesde").value;
        var fechaHasta = document.getElementById("meLanbide01FechaHasta").value;
        var porcSubvenc = document.getElementById("meLanbide01PorcSubvenc").value;
        var numDias = document.getElementById("meLanbide01NumDias").value;
        var baseCotiz = document.getElementById("meLanbide01BaseCotizacion").value;
        var bonificacion = document.getElementById("meLanbide01Bonificacion").value;
//        if(fechaDesde!="" && fechaHasta!="" && porcSubvenc!="" && numDias!="" && baseCotiz!="" && bonificacion!=""){
        var ajax = getXMLHttpRequest();
        var campoOculto = document.getElementById("meLanbide01Registros").value;
        var descuento = document.getElementById("descuento").value;
        var numTotalDiasConcedidos = document.getElementById("divTotalNumDias").innerHTML;

        var cadena = new String(totalConDescuento);

        if (!comprobarDatosCubiertos()) {
            jsp_alerta("A", '<%=configuracion.getParameter("21_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
        } else {
            if (ajax != null) {

                pleaseWait('on');

                var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                var parametros = "&tarea=procesar&operacion=grabarDatos&modulo=" + nombreModulo + "&codOrganizacion=" + codOrganizacion
                        + "&numero=" + numExpediente + "&tipo=0&periodos=" + campoOculto + "&descuento=" + descuento + "&totalConDescuento=" + formatNumeroDecimal(cadena)
                        + "&numTotalDiasConcedidos=" + numTotalDiasConcedidos;

                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);
                try {
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

                    pleaseWait('off');
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var informacionAdicional = null;
                    var diasRestantes = null;

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;

                        } else if (hijos[j].nodeName == "INFORMACION_ADICIONAL") {
                            try {
                                informacionAdicional = hijos[j].childNodes[0].nodeValue;
                            } catch (ex) {
                            }
                        }
                    }



                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=configuracion.getParameter("OPERACION_GRABAR_CORRECTA_" + idiomaUsuario,nombreModulo)%>');

                        try {
                            var aux = formatNumeroDecimal(cadena);
                            aux = aux.replace(".", "");

                            //                                var cargados = false;
                            //                                if(!estanCargadosCamposSuplementarios()){                                             
                            //                                    if(cargarCamposSuplementarios()){
                            //                                        cargados = true;
                            //                                        
                            //                                    }
                            //                                }else{
                            //                                     cargados=true;
                            //                                }                                

                            if (estanCargadosCamposSuplementarios()) {
                                document.getElementById(codCampoImporteSubvencion).value = aux;
                                // Se recupera el total de días concedidos
                                var totalDiasConcedidos = document.getElementById("divTotalNumDias").innerHTML;
                                document.getElementById(codCampoTotalDiasConcedidos).value = totalDiasConcedidos;
                            }
                            //document.getElementById(codCampoImporteSubvencion).value=aux;                                
                            // Se recupera el total de días concedidos
                            //var totalDiasConcedidos = document.getElementById("divTotalNumDias").innerHTML;                                
                            //document.getElementById(codCampoTotalDiasConcedidos).value = totalDiasConcedidos;

                        } catch (err) {
                            jsp_alerta("A", "Error al almacenar la subvención total en el campo suplementario : " + err.message);
                        }

                    } else if (codigoOperacion == "9") {
                        jsp_alerta("A", '<%=configuracion.getParameter("9_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                    } else if (codigoOperacion == "11") {
                        jsp_alerta("A", '<%=configuracion.getParameter("11_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                    } else if (codigoOperacion == "15") {
                        jsp_alerta("A", '<%=configuracion.getParameter("OPERACION_GRABAR_CORRECTA_" + idiomaUsuario,nombreModulo)%>');
                        mostrarMensajeError('<%=configuracion.getParameter("15_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                    } else if (codigoOperacion == "25") {
                        jsp_alerta("A", '<%=configuracion.getParameter("MSG_ERROR_DIAS_INTERESADO_ACTIVIDADSUBVENCIONABLE_EXCEDIDOS_" + idiomaUsuario,nombreModulo)%>' + ' ' + informacionAdicional);
                    } else if (codigoOperacion == "27") {
                        ocultarDiasRestantes();
                        mostrarMensajeError('<%=configuracion.getParameter("15_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                    } else if (codigoOperacion == "30") {
                        mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                    }//if  
                } catch (Err) {
                    pleaseWait('off');
                    jsp_alerta("A", "Error.descripcion: " + Err.message);
                }//try-catch
            }//if(ajax!=null)
        }//if(fechaDesde!="" && fechaHasta!="" && porcSubvenc!="" && numDias!="" && baseCotiz!="" && bonificacion!="")
//         }else{
//            jsp_alerta("A",'<%=configuracion.getParameter("MSG_ERROR_CAMPOS_OBLIGATORIOS_" + idiomaUsuario,nombreModulo)%>');
//         }
    }//pulsarGrabar

    //Llama via AJAX a una funcion que se encarga de realizar las comprobaciones necesarias y eliminar si todo es correcto la fila seleccionada
    //de la tabla de periodos
    function eliminar(indice) {
        var campoOculto = document.getElementById("meLanbide01Registros").value;
        var indiceSeleccionado = indice;
        var ajax = getXMLHttpRequest();
        var nodos = "";
        if (ajax != null) {
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=eliminarPeriodo&modulo=" + nombreModulo + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpediente + "&tipo=0&periodos=" + campoOculto + "&indiceSeleccionado=" + indiceSeleccionado;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try {
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
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)

                if (codigoOperacion == "0") {
                    desbloquearFormulario();
                    deshabilitarFormulario(false);
                    ocultarMensajeError();
                    tabConciliacion.lineas = new Array();
                    tabConciliacion.displayTabla();
                    registros = new Array();

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "TOTAL_DIAS") {
                            totalDias = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "GASTO_TOTAL") {
                            gastoTotal = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "DIAS_SUBVENCIONABLES_RESTANTES") {
                            diasRestantes = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "PERIODOS") {
                            var listaUnidades = xmlDoc.getElementsByTagName("PERIODOS");
                            var nodosUnidades = listaUnidades[0];
                            var hijosUnidades = nodosUnidades.childNodes;
                            for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                                var inicioPeriodo = "";
                                var finPeriodo = "";
                                var numDias = "";
                                var baseCotizacion = "";
                                var bonificacion = "";
                                var porcSubvenc = "";
                                var gasto = "";
                                var porcJornRealizada = "";
                                var porcJornSutitucion = "";
                                if (hijosUnidades[x].nodeName == "PERIODO") {
                                    var listaUnidades = xmlDoc.getElementsByTagName("PERIODO");
                                    for (x = 0; listaUnidades != null && x < listaUnidades.length; x++) {
                                        var unidad = listaUnidades[x].childNodes;
                                        for (y = 0; unidad != null && y < unidad.length; y++) {
                                            if (unidad[y].nodeName == "INICIO_PERIODO") {
                                                inicioPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "FIN_PERIODO") {
                                                finPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "NUM_DIAS") {
                                                numDias = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BASE_COTIZACION") {
                                                baseCotizacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BONIFICACION") {
                                                bonificacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_SUBVENC") {
                                                porcSubvenc = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "GASTO") {
                                                gasto = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_REALIZADA") {
                                                porcJornRealizada = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_SUSTITUCION") {
                                                porcJornSutitucion = unidad[y].childNodes[0].nodeValue;
                                            }//if
                                        }//for(y=0;unidad!=null && y<unidad.length;y++)
                                        registros[x] = [inicioPeriodo, finPeriodo, porcSubvenc, numDias, baseCotizacion, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
                                    }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                                }//if(hijosUnidades[x].nodeName == "PERIODO")
                            }//for(x=0;hijosUnidades!=null && x<hijosUnidades.length;x++)
                        }//
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    tabConciliacion.lineas = copiarRegistrosMostrarEnTabla(registros);
                    tabConciliacion.displayTabla();
                    actualizarCampoOculto();
                    mostrarTotalDias(totalDias);
                    mostrarImporteTotal(gastoTotal);
                    totalSinDescuento = gastoTotal;
                    recalcularTotalConDescuento();
                    mostrarDiasRestantes(diasRestantes);

                } else if (codigoOperacion == "12") {
                    mostrarMensajeError('<%=configuracion.getParameter("12_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "13") {
                    mostrarMensajeError('<%=configuracion.getParameter("13_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "27") {
                    mostrarMensajeError('<%=configuracion.getParameter("27_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "30") {
                    mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                }//if(codigoOperacion)

                // Validamos de Nuevo los datos del Expediente funcion en js gestionConciliacionSubvencion
                //Se mostrara la validacion en el Modal y el resultado de esta operacion si hay  en el Div de errores.
                validarControlAlarmasCONCM();

            } catch (Err) {
                jsp_alerta("A", "Error.descripcion: " + Err.message);
            }//try-catch
        }//if(ajax!=null)
    }//eliminar

    //Llama via AJAX a una funcion que comprueba los datos y si todo es correcto añade una fila a la tabla de periodos con los datos
    //cubiertos en las cajas de texto.
    function comprobarInsercion() {
        var campoOculto = document.getElementById("meLanbide01Registros").value;
        var nuevoPeriodo = generarCampoOcultoParaComprobacion();
        var totalDias = "";
        var gastoTotal = "";
        var diasRestantes = "";
        var nodos = null;
        var ajax = getXMLHttpRequest();
        if (ajax != null) {
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=comprobarDatosAnhadir&modulo=" + nombreModulo + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpediente + "&tipo=0&periodos=" + campoOculto + "&nuevoPeriodo=" + nuevoPeriodo;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try {
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
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }
                }

                if (codigoOperacion == "0") {
                    desbloquearFormulario();
                    deshabilitarFormulario(false);
                    ocultarMensajeError();
                    tabConciliacion.lineas = new Array();
                    tabConciliacion.displayTabla();
                    registros = new Array();

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "TOTAL_DIAS") {
                            totalDias = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "DIAS_SUBVENCIONABLES_RESTANTES") {
                            diasRestantes = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "GASTO_TOTAL") {
                            gastoTotal = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "PERIODOS") {
                            var listaUnidades = xmlDoc.getElementsByTagName("PERIODOS");
                            var nodosUnidades = listaUnidades[0];
                            var hijosUnidades = nodosUnidades.childNodes;
                            for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                                var inicioPeriodo = "";
                                var finPeriodo = "";
                                var numDias = "";
                                var baseCotizacion = "";
                                var bonificacion = "";
                                var porcSubvenc = "";
                                var gasto = "";
                                var porcJornRealizada = "";
                                var porcJornSutitucion = "";
                                if (hijosUnidades[x].nodeName == "PERIODO") {
                                    var listaUnidades = xmlDoc.getElementsByTagName("PERIODO");
                                    for (x = 0; listaUnidades != null && x < listaUnidades.length; x++) {
                                        var unidad = listaUnidades[x].childNodes;
                                        for (y = 0; unidad != null && y < unidad.length; y++) {
                                            if (unidad[y].nodeName == "INICIO_PERIODO") {
                                                inicioPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "FIN_PERIODO") {
                                                finPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "NUM_DIAS") {
                                                numDias = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BASE_COTIZACION") {
                                                baseCotizacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BONIFICACION") {
                                                bonificacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_SUBVENC") {
                                                porcSubvenc = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "GASTO") {
                                                gasto = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_REALIZADA") {
                                                porcJornRealizada = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_SUSTITUCION") {
                                                porcJornSutitucion = unidad[y].childNodes[0].nodeValue;
                                            }//if
                                        }//for(y=0;unidad!=null && y<unidad.length;y++)
                                        registros[x] = [inicioPeriodo, finPeriodo, porcSubvenc, numDias, baseCotizacion, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
                                    }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                                }//if(hijosUnidades[x].nodeName == "PERIODO")
                            }//for(x=0;hijosUnidades!=null && x<hijosUnidades.length;x++)
                        }//
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    limpiarr();
                    tabConciliacion.lineas = copiarRegistrosMostrarEnTabla(registros);
                    tabConciliacion.displayTabla();
                    actualizarCampoOculto();
                    mostrarTotalDias(totalDias);
                    mostrarImporteTotal(gastoTotal);
                    totalSinDescuento = gastoTotal;
                    recalcularTotalConDescuento();
                    mostrarDiasRestantes(diasRestantes);

                } else if (codigoOperacion == "6") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    mostrarMensajeError('<%=configuracion.getParameter("6_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "7") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    mostrarMensajeError('<%=configuracion.getParameter("7_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "8") {
                    bloquearFormulario();
                    mostrarMensajeError('<%=configuracion.getParameter("8_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "14") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    mostrarMensajeError('<%=configuracion.getParameter("14_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "25") {
                    mostrarMensajeError('<%=configuracion.getParameter("25_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "27") {
                    mostrarMensajeError('<%=configuracion.getParameter("27_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "30") {
                    mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                }//if(codigoOperacion) 

                // Validamos de Nuevo los datos del Expediente funcion en js gestionConciliacionSubvencion
                //Se mostrara la validacion en el Modal y el resultado de esta operacion si hay  en el Div de errores.
                validarControlAlarmasCONCM();

            } catch (Err) {
                jsp_alerta("A", "Error.descripcion: " + Err.message);
            }//try-catch
        }//if(ajax!=null)
    }//comprobarInsercion

    //Llama via AJAX a una función que comprueba que los datos modificados para una fila de la tabla de los periodos son correctos y si es
    //asi modifica la fila seleccionada con los datos introducidos en las cajas de texto
    function comprobarModificacion(indice) {
        var campoOculto = document.getElementById("meLanbide01Registros").value;
        var nuevoPeriodo = generarCampoOcultoParaComprobacion();
        var indiceSeleccionado = indice;
        var totalDias = "";
        var gastoTotal = "";
        var nodos = null;
        var diasRestantes = "";
        var ajax = getXMLHttpRequest();


        if (ajax != null) {

            pleaseWait('on');
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=comprobarDatosModificar&modulo=" + nombreModulo + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpediente + "&tipo=0&periodos=" + campoOculto + "&nuevoPeriodo=" + nuevoPeriodo + "&indiceSeleccionado="
                    + indiceSeleccionado;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try {
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

                pleaseWait('off');
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)

                if (codigoOperacion == "0") {
                    desbloquearFormulario();
                    deshabilitarFormulario(false);
                    ocultarMensajeError();
                    tabConciliacion.lineas = new Array();
                    tabConciliacion.displayTabla();
                    registros = new Array();

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "TOTAL_DIAS") {
                            totalDias = hijos[j].childNodes[0].nodeValue;
                        }/**
                         else if(hijos[j].nodeName=="DIAS_SUBVENCIONABLES_RESTANTES"){
                         diasRestantes = hijos[j].childNodes[0].nodeValue;
                         }*/
                        else if (hijos[j].nodeName == "GASTO_TOTAL") {
                            gastoTotal = hijos[j].childNodes[0].nodeValue;
                        } else if (hijos[j].nodeName == "PERIODOS") {
                            var listaUnidades = xmlDoc.getElementsByTagName("PERIODOS");
                            var nodosUnidades = listaUnidades[0];
                            var hijosUnidades = nodosUnidades.childNodes;
                            for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                                var inicioPeriodo = "";
                                var finPeriodo = "";
                                var numDias = "";
                                var baseCotizacion = "";
                                var bonificacion = "";
                                var porcSubvenc = "";
                                var gasto = "";
                                var porcJornRealizada = "";
                                var porcJornSutitucion = "";
                                if (hijosUnidades[x].nodeName == "PERIODO") {
                                    var listaUnidades = xmlDoc.getElementsByTagName("PERIODO");
                                    for (x = 0; listaUnidades != null && x < listaUnidades.length; x++) {
                                        var unidad = listaUnidades[x].childNodes;
                                        for (y = 0; unidad != null && y < unidad.length; y++) {
                                            if (unidad[y].nodeName == "INICIO_PERIODO") {
                                                inicioPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "FIN_PERIODO") {
                                                finPeriodo = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "NUM_DIAS") {
                                                numDias = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BASE_COTIZACION") {
                                                baseCotizacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "BONIFICACION") {
                                                bonificacion = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_SUBVENC") {
                                                porcSubvenc = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "GASTO") {
                                                gasto = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_REALIZADA") {
                                                porcJornRealizada = unidad[y].childNodes[0].nodeValue;
                                            } else if (unidad[y].nodeName == "PORC_JORN_SUSTITUCION") {
                                                porcJornSutitucion = unidad[y].childNodes[0].nodeValue;
                                            }//if
                                        }//for(y=0;unidad!=null && y<unidad.length;y++)
                                        registros[x] = [inicioPeriodo, finPeriodo, porcSubvenc, numDias, baseCotizacion, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
                                        inicioPeriodo = "";
                                        finPeriodo = "";
                                        numDias = "";
                                        baseCotizacion = "";
                                        bonificacion = "";
                                        porcSubvenc = "";
                                        gasto = "";
                                        porcJornRealizada = "";
                                        porcJornSutitucion = "";
                                    }
                                }
                            }
                        }
                    }

                    tabConciliacion.lineas = copiarRegistrosMostrarEnTabla(registros);
                    tabConciliacion.displayTabla();
                    actualizarCampoOculto();
                    mostrarTotalDias(totalDias);
                    mostrarImporteTotal(gastoTotal);
                    totalSinDescuento = gastoTotal;
                    recalcularTotalConDescuento();
                    //mostrarDiasRestantes(diasRestantes);
                    limpiarr();
                } else if (codigoOperacion == "6") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    mostrarMensajeError('<%=configuracion.getParameter("6_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "7") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    mostrarMensajeError('<%=configuracion.getParameter("7_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "27") {
                    mostrarMensajeError('<%=configuracion.getParameter("27_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "30") {
                    mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                }//if(codigoOperacion) 

                // Validamos de Nuevo los datos del Expediente funcion en js gestionConciliacionSubvencion
                //Se mostrara la validacion en el Modal y el resultado de esta operacion si hay  en el Div de errores.
                validarControlAlarmasCONCM();

            } catch (Err) {
                pleaseWait('off');
                jsp_alerta("A", "Error.descripcion: " + Err.message);
            }//try-catch
        }//if(ajax!=null)
    }//comprobarModificacion

    //Genera un string con un formato similar al del campo oculto para enviar en las comprobaciones con los datos de las cajas de texto
    function generarCampoOcultoParaComprobacion() {
        var SEPARADOR_ELEMENTO = "#@#";
        var fechaDesde = document.getElementById("meLanbide01FechaDesde").value;
        var fechaHasta = document.getElementById("meLanbide01FechaHasta").value;
        var porcSubvenc = document.getElementById("meLanbide01PorcSubvenc").value;
        var numDias = document.getElementById("meLanbide01NumDias").value;
        var baseCotiz = document.getElementById("meLanbide01BaseCotizacion").value;
        var bonificacion = document.getElementById("meLanbide01Bonificacion").value;
        var gasto = document.getElementById("meLanbide01Gasto").value;
        var porcJornRealizada = ($("#porcJornRealizada") != null && $("#porcJornRealizada") != undefined && $("#porcJornRealizada").length > 0 ? formatNumeroFromInputToBD($("#porcJornRealizada")[0]) : "");
        var porcJornSutitucion = ($("#porcJornSustitucion") != null && $("#porcJornSustitucion") != undefined && $("#porcJornSustitucion").length > 0 ? formatNumeroFromInputToBD($("#porcJornSustitucion")[0]) : "");

        baseCotiz = baseCotiz.replace(",", ".");
        bonificacion = bonificacion.replace(",", ".");

        //Le concatenamos los nuevos datos.
        var mensaje = fechaDesde + SEPARADOR_ELEMENTO + fechaHasta + SEPARADOR_ELEMENTO + porcSubvenc + SEPARADOR_ELEMENTO +
                numDias + SEPARADOR_ELEMENTO + baseCotiz + SEPARADOR_ELEMENTO + bonificacion + SEPARADOR_ELEMENTO + gasto + SEPARADOR_ELEMENTO + porcJornRealizada + SEPARADOR_ELEMENTO + porcJornSutitucion;

        return mensaje;
    }//generarCampoOcultoParaComprobacion


    //Limpia las cajas de texto y setea el estado de los botones.
    function limpiarr() {
        document.getElementById("meLanbide01FechaDesde").value = "";
        document.getElementById("meLanbide01FechaHasta").value = "";
        if (document.getElementById("meLanbide01PorcSubvenc") !== null)
            document.getElementById("meLanbide01PorcSubvenc").value = "";
        document.getElementById("meLanbide01NumDias").value = "";
        document.getElementById("meLanbide01BaseCotizacion").value = "";
        document.getElementById("meLanbide01Bonificacion").value = "";
        document.getElementById("meLanbide01Gasto").value = "";
        $("#porcJornRealizada").val("");
        $("#porcJornSustitucion").val("");

        document.getElementById("meLanbide01FechaDesde").disabled = false;
        document.getElementById("meLanbide01FechaHasta").disabled = false;
        if (document.getElementById("meLanbide01PorcSubvenc") !== null)
            document.getElementById("meLanbide01PorcSubvenc").disabled = false;
        document.getElementById("meLanbide01NumDias").disabled = false;
        document.getElementById("meLanbide01BaseCotizacion").disabled = false;
        document.getElementById("meLanbide01Bonificacion").disabled = false;
        document.getElementById("meLanbide01Gasto").disabled = false;
        $("#porcJornRealizada").removeAttr("disabled");
        $("#porcJornSustitucion").removeAttr("disabled");

        document.getElementById("btnMeAlta").disabled = false;
        document.getElementById("btnMeModificar").disabled = false;
        document.getElementById("btnMeEliminar").disabled = false;
        document.getElementById("btnMeGrabar").disabled = false;
        document.getElementById('btnMeAlta').style.color = '#FFFFFF';
        document.getElementById('btnMeModificar').style.color = '#FFFFFF';
        document.getElementById('btnMeEliminar').style.color = '#FFFFFF';
        document.getElementById('btnMeGrabar').style.color = '#FFFFFF';

        habilitarImagenCal("meLanbide01CalFechaDesde", true);
        habilitarImagenCal("meLanbide01CalFechaHasta", true);

        ocultarMensajeError();
    }//limpiar

    //Actualiza el campo oculto que se usa para saber los datos de la tabla de periodos
    function actualizarCampoOculto() {
        //var SEPARADOR_REGISTRO ="@#@";
        var SEPARADOR_REGISTRO = "POP";
        var SEPARADOR_ELEMENTO = "#@#";

        var mensaje = "";
        for (i = 0; i < registros.length; i++) {
            var reg = registros[i];
            mensaje = mensaje + reg[0] + SEPARADOR_ELEMENTO + reg[1] + SEPARADOR_ELEMENTO + reg[2] + SEPARADOR_ELEMENTO
                    + reg[3] + SEPARADOR_ELEMENTO + reg[4] + SEPARADOR_ELEMENTO + reg[5] + SEPARADOR_ELEMENTO + reg[6] + SEPARADOR_ELEMENTO + reg[7] + SEPARADOR_ELEMENTO + reg[8];
            if (registros.length - i > 1)
                mensaje = mensaje + SEPARADOR_REGISTRO;
        }//for(i=0;i<registros.length;i++)
        document.getElementById("meLanbide01Registros").value = mensaje;
    }//actualizarCampoOculto

    //Comprueba que todos los datos de la tabla esten cubiertos
    function comprobarDatosCubiertos() {
        for (i = 0; i < registros.length; i++) {
            var reg = registros[i];
            if (reg[0] == null || reg[0] == "") {
                return false;
            }//if(reg[0] == null || reg[0] == "")
            if (reg[1] == null || reg[1] == "") {
                return false;
            }//if(reg[1] == null || reg[1] == "")
            if (reg[2] == null || reg[2] == "") {
                return false;
            }//if(reg[2] == null || reg[2] == "")
            if (reg[3] == null || reg[3] == "") {
                return false;
            }//if(reg[3] == null || reg[3] == "")
            if (reg[4] == null || reg[4] == "") {
                return false;
            }//if(reg[4] == null || reg[4] == "")
            if (reg[5] == null || reg[5] == "") {
                return false;
            }//if(reg[5] == null || reg[5] == "")
            if (reg[7] == null || reg[7] == "") {
                return false;
            }
            if (reg[8] == null || reg[8] == "") {
                return false;
            }
        }//for(i=0;i<registros.length;i++)
        return true;
    }//comprobarDatosCubiertos

    //Funcion que carga en las cajas de texto los datos de la fila seleccionada en la tabla de periodos
    function copiarRegistros() {
        var registro = registros[0];

        for (var i = 1; i < registros.length; i++) {
            var nuevoRegistro = new Array();
            var regSel = registros[i];
            var fechaDesde = regSel[0];
            var fechaHasta = regSel[1];
            //var porcSubvenc = regSel[2];
            var porcSubvenc = registro[2];
            var numDias = regSel[3];
            var baseCotizacion = registro[4];
            var bonificacion = registro[5];
            var gasto = registro[6];

            var porcJornRealizada = registro[7];
            var porcJornSutitucion = registro[8];

            nuevoRegistro = [fechaDesde, fechaHasta, porcSubvenc, numDias, baseCotizacion, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
            registros[i] = nuevoRegistro;
        }


        var registrosTabla = new Array();
        for (i = 0; i < registros.length; i++) {
            registrosTabla[i] = [registros[i][0], registros[i][1], formatNumeroDecimal(registros[i][2]), registros[i][3], formatNumeroDecimal(registros[i][4]), formatNumeroDecimal(registros[i][5]), formatNumeroDecimal(registros[i][6]), formatNumeroDecimal(registros[i][7]), formatNumeroDecimal(registros[i][8])];
        }

        tabConciliacion.lineas = registrosTabla;
        tabConciliacion.displayTabla();
        actualizarCampoOculto();

        recalcularGastoTotal();
    }


    function recalcularGastoTotal() {
        var total = 0;
        for (i = 0; i < registros.length; i++) {
            var gasto = parseFloat(registros[i][6]);
            total = total + gasto;
        }

        totalSinDescuento = total;
        mostrarImporteTotal(new String(total));
        recalcularTotalConDescuento();

    }


</script>

<div class="tab-page" id="tabPage100" style="height:500px;width:940px;">    
    <h2 class="tab" id="pestana8"> <%=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_" + idiomaUsuario,nombreModulo) %> </h2>
    <script type="text/javascript">tp1_p8 = tp1.addTabPage(document.getElementById("tabPage100"));</script>
    <div style="clear: both;">
        <span class="badge badge-warning" id="decretoExpediente">
            <%=melanbide01Decreto!=null && melanbide01Decreto.getDecretoDescripcion() != null && !melanbide01Decreto.getDecretoDescripcion().isEmpty() ? 
                    meLanbide01I18n.getMensaje(idioma, "label.texto.decreto.expediente") + " " + 
                    melanbide01Decreto.getDecretoDescripcion() 
                    : meLanbide01I18n.getMensaje(idioma, "label.texto.decreto.expediente.noPeriodos.noDecreto")%>
        </span><br/>
        <div id="reglaMinimisInfo" style="float: right;">
            <table>
                <tr>
                    <td>
                        <span class="badge badge-info">
                            <%=meLanbide01I18n.getMensaje(idioma, "lable.texto.regla.minimis.info.limite")%>
                        </span>
                    </td>
                    <td>
                        <span class="badge badge-info" style="float: right;">
                            <%=limiteReglaMinimis%>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="badge badge-info">
                            <%=meLanbide01I18n.getMensaje(idioma, "lable.texto.regla.minimis.info.importe.empresa")%>
                        </span>
                    </td>
                    <td>
                        <span class="badge badge-info" style="float: right;" id="impTotalRecibidoEmpreReglaMinimis">
                            <%=impTotalRecibidoEmpreReglaMinimis%>
                        </span>
                    </td>
                </tr>
            </table>
        </div>
        <br/>
        <div id="divValidacionesAlarmasExpte" name="divValidacionesAlarmasExpte" class="alert alert-danger alert-dismissible fade show" role="alert" style="float: left;width: 100%;display: none;"></div>
        <div id="divErroresOperacionesM01" name="divErroresOperacionesM01" class="alert alert-danger alert-dismissible fade show" role="alert" style="float: left;width: 100%;display: none;"></div>
        <br/>
        <div id="tab-panel-01" class="tab-pane" style="float: left;" align="center"></div>
        <script type="text/javascript">
            var tp01_concm = new WebFXTabPane(document.getElementById("tab-panel-01"));
            tp01_concm.selectedIndex = 0;
        </script> 
        <div class="tab-page" id="tabPage01_datosCalculo" style="height: 480px;">
            <h2 class="tab" id="pestana01_datosCalculo"><%=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_" + idiomaUsuario,nombreModulo) %></h2>
            <script type="text/javascript">tp01_concm.addTabPage(document.getElementById("tabPage01_datosCalculo"));</script>
            <div style="clear: both; padding-top: 14px;">
                <table width="100%" border="0">
                    <tr><td></td></tr>  
                    <tr style="padding-left:10px">
                        <td align="left">
                            <table border="0" width="100%">
                                <tr height="5">
                                    <!-- separacion-->
                                    <td height="5" colspan="4"></td>
                                </tr>
                                <tr>
                                    <td width="100%" align="center" colspan="4">
                                        <!--form action="/PeticionModuloIntegracion.do" method="POST"-->
                                        <input type="hidden" name="meLanbide01Registros" id="meLanbide01Registros" value=""/>
                                        <!-- CONTENIDO DE LA PANTALLA -->
                                        <table border="0">
                                            <tr>
                                                <td colspan="7" align="left" colspan="2">
                                                    <div id="mensajes" name="mensajes" class="mensajes" style="text-align:left;"/>
                                                </td>
                                            </tr>                          
                                            <tr>
                                                <%-- Tabla con los periodos de conciliacion --%>
                                                <!--td id="tabConciliacion" name="tabConciliacion"></td-->
                                                <td>
                                                    <div id="tabConciliacion" style="padding: 5px; width:930px; height: 250px; text-align: center; margin:0px;margin-top:0px;" align="center">
                                                    </div>
                                                </td>
                                                <td style="vertical-align: top">
                                                    <br/>
                                                    <input type="button" class="botonGeneralLanbide" 
                                                           id="btnMeGrabar" name="btnMeGrabar"   
                                                           value="<%=configuracion.getParameter("ETIQUETA_BOTON_COPIAR_" 
                                                    + idiomaUsuario,nombreModulo)%>" 
                                                           onclick="javascript:copiarRegistros();"/>
                                                </td>
                                            </tr>
                                        </table>
                                        <table border="0">
                                            <!------------>
                                            <tr>
                                                <td class="bordeInferiorCelda">
                                                    <table border="0" width="100%">
                                                        <tr>
                                                            <td align="left" style="width: 80px">
                                                                <input type="text" id="meLanbide01FechaDesde" class="inputTxtFechaLanbide" value=""
                                                                       maxlength="10" name="meLanbide01FechaDesde" size="10"
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                            </td>
                                                            <td align="left" style="width:40px">
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaDesde(event);
                                                                        return false;" onblur="ocultarCalendarioOnBlur(event);
                                                                                return false;" style="text-decoration:none;">
                                                                    <IMG style="border: 0px solid" height="17" id="meLanbide01CalFechaDesde" name="meLanbide01CalFechaDesde" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                                                                </A>

                                                            <td align="left" style="width: 80px">
                                                                <input type="text" id="meLanbide01FechaHasta" class="inputTxtFechaLanbide" value=""
                                                                       maxlength="10" name="meLanbide01FechaHasta" size="10"
                                                                       title="" onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"/>
                                                            </td>
                                                            <td align="left" style="width:40px">
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaHasta(event);
                                                                        return false;" onblur="ocultarCalendarioOnBlur(event);
                                                                                return false;" style="text-decoration:none;">
                                                                    <IMG style="border: 0px solid" height="17" id="meLanbide01CalFechaHasta" name="meLanbide01CalFechaHasta" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                                                                </A>
                                                            </td>
                                                            <td align="left" style="width: 100px">
                                                                <input type="text" class="inputTextoLanbide" name="meLanbide01PorcSubvenc" 
                                                                       id="meLanbide01PorcSubvenc" size="8"  maxlength="15"
                                                                       style ="" onkeyup="return SoloDigitosNumericosDecimalesLanbide(this);"/>
                                                            </td>
                                                            <td align="left" style="width: 70px">
                                                                <input type="text" class="inputTextoLanbide" name="meLanbide01NumDias" 
                                                                       id="meLanbide01NumDias" value="" size="8"  maxlength="2" />
                                                            </td>
                                                            <td align="left" style="width: 120px">
                                                                <input type="text" class="inputTextoLanbide" name="meLanbide01BaseCotizacion" 
                                                                       id="meLanbide01BaseCotizacion" value=""  size="15" onkeyup="return SoloDigitosNumericosDecimalesLanbide(this);"/>
                                                            </td>
                                                            <td align="left" style="width: 120px">
                                                                <input type="text" class="inputTextoLanbide" name="meLanbide01Bonificacion" 
                                                                       id="meLanbide01Bonificacion" value="" size="15" onkeyup="return SoloDigitosNumericosDecimalesLanbide(this);"/>
                                                            </td>
                                                            <td align="left" style="width: 120px">
                                                                <input type="text" class="inputTextoLanbide" name="meLanbide01Gasto" 
                                                                       id="meLanbide01Gasto" value="" size="12" readonly="true"
                                                                       style ="visibility: hidden"/>
                                                            </td>
                                                            <td align="left" style="width: 120px">
                                                                <input type="text" class="inputTextoLanbide" name="porcJornRealizada" 
                                                                       id="porcJornRealizada" value="" size="15" 
                                                                       onkeypress="return permiteSoloNumeroY2Decimales(event);" 
                                                                       onfocusout="formatInputAsNumero(this);"/>
                                                            </td>
                                                            <td align="left" style="width: 120px">
                                                                <input type="text" class="inputTextoLanbide" name="porcJornSustitucion" 
                                                                       id="porcJornSustitucion" value="" size="15" 
                                                                       onkeypress="return permiteSoloNumeroY2Decimales(event);" 
                                                                       onfocusout="formatInputAsNumero(this);"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td style="vertical-align: top">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </tr>
                                            <!------------>
                                            <tr>
                                                <td colspan="7">
                                                    <table border="0" width="100%">
                                                        <tr>
                                                            <td align="right" colspan="3" style="width:320px;">
                                                                <span class="cabeceraConciliacionIdioma">
                                                                    <b>
                                                                        <%=configuracion.getParameter("ETIQUETA_TOTAL_" + idiomaUsuario,nombreModulo)%>
                                                                    </b>
                                                                </span>
                                                            </td>
                                                            <td class="totales" align="left" width="70px;">
                                                                <div id="divTotalNumDias" name="divTotalNumDias" class="totales">&nbsp;</div>                                                                                    
                                                            </td>
                                                            <td align="right" width="120px;">
                                                                &nbsp;
                                                            </td>
                                                            <td align="right" width="120px;" class="totales">
                                                                <strong>
                                                                    <span class="cabeceraConciliacionIdioma">                                                        
                                                                        <%=configuracion.getParameter("ETIQUETA_GASTO_TOTAL_" + idiomaUsuario,nombreModulo)%>
                                                                </strong>
                                                            </td>
                                                            <td  width="126px;" align="left">
                                                                <div id="divTotalImporte" name="divTotalImporte" class="totales" >&nbsp;</div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="3" style="width:320px;">
                                                                <span class="cabeceraConciliacionIdioma">
                                                                    <b>
                                                                        <%=configuracion.getParameter("ETIQUETA_DIAS_RESTANTES_" + idiomaUsuario,nombreModulo)%>
                                                                    </b>
                                                                </span>
                                                            </td>
                                                            <td class="totales" align="left" width="70px;">
                                                                <div id="divDiasRestantes" name="divDiasRestantes" class="totales">&nbsp;</div>
                                                            </td>
                                                            <td align="right" width="120px;">
                                                                &nbsp;
                                                            </td>
                                                            <td align="right" width="120px;" >
                                                                <strong>
                                                                    <span class="cabeceraConciliacionIdioma">                                                           
                                                                        <%=configuracion.getParameter("ETIQUETA_PORCENTAJE_DESCUENTO_" + idiomaUsuario,nombreModulo)%>
                                                                    </span>
                                                                </strong>
                                                            </td>
                                                            <td  width="126px;" align="left">
                                                                <input type="text" name="descuento" id="descuento" size="6" class="inputTextoLanbideBlanco" value="<%=porcentaje%>" onkeyup="recalcularTotalConDescuento();
                                                                        return SoloDigitosNumericos(this);"/>                                                                                                               
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="3" style="width:320px;">
                                                                &nbsp;
                                                            </td>
                                                            <td class="totales" align="right" width="70px;">
                                                                &nbsp;
                                                            </td>
                                                            <td align="right" width="120px;">
                                                                &nbsp;
                                                            </td>
                                                            <td align="right" width="120px;" >
                                                                <strong>
                                                                    <span class="cabeceraConciliacionIdioma">                                                           
                                                                        <%=configuracion.getParameter("ETIQUETA_GASTO_DESCUENTO_" + idiomaUsuario,nombreModulo)%>
                                                                    </span>
                                                                </strong>
                                                            </td>
                                                            <td  width="126px;" align="left">
                                                                <div id="divTotalImporteConDescuento" name="divTotalImporteConDescuento" class="totales" >&nbsp;</div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td style="vertical-align: top">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="right" colspan="7">                                     
                                                    <%-- Futura fila para total subvencionado --%>
                                                </td>
                                                <td style="vertical-align: top">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="7"><span class="cabeceraConciliacionIdioma">&nbsp;</span></td>
                                                <td style="vertical-align: top">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                </td>
                                            </tr>
                                            <!--
                                            <tr>
                                                <td colspan="7"><span class="cabeceraConciliacionIdioma">&nbsp;</span></td>
                                            </tr>
                                            -->
                                            <tr>
                                                <td colspan="7" align="center">
                                                    <table border="0" width="70%" cellspacing="2" cellpadding="0">
                                                        <tr>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeAlta" name="btnMeAlta" 
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_ALTA_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:pulsarAlta();"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeModificar" name="btnMeModificar" 
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_MODIFICAR_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:pulsarModificar();"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeEliminar" name="btnMeEliminar" 
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_ELIMINAR_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:pulsarEliminar();"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeLimpiar" name="btnMeLimpiar" 
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_LIMPIAR_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:limpiarr();"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeRecalcular" name="btnMeRecalcular" 
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_RECALCULAR_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:pulsarRecalcular();"
                                                                       style="display: none"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" class="botonGeneralLanbide" 
                                                                       id="btnMeGrabar" name="btnMeGrabar"   
                                                                       value="<%=configuracion.getParameter("ETIQUETA_BOTON_GUARDAR_" 
                                                                            + idiomaUsuario,nombreModulo)%>" 
                                                                       onclick="javascript:pulsarGrabar();"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>                            
                                            <tr>
                                                <td colspan="7" align="center">
                                                    <div id="mensajeError" name="mensajeError" class="mensajeError"/>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- CONTENIDO DE LA PANTALLA --->
                                        <!--/form-->                                          
                                    </td>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="tab-page" id="tabPage01_causantesSubv" style="height: 480px;">
            <h2 class="tab" id="pestana01_causantesSubv"><%=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_CAUSANTES_SUBV_" + idiomaUsuario,nombreModulo) %></h2>
            <script type="text/javascript">tp01_concm.addTabPage(document.getElementById("tabPage01_causantesSubv"));</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlCausantesSubvencion%>" flush="true"/>
            </div>
        </div>
        <div class="tab-page" id="tabPage01_historialSubv" style="height: 480px;">
            <h2 class="tab" id="pestana01_historialSubv"><%=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_HISTORIAL_SUBV_" + idiomaUsuario,nombreModulo) %></h2>
            <script type="text/javascript">tp01_concm.addTabPage(document.getElementById("tabPage01_historialSubv"));</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlHistorialSubvencion%>" flush="true"/>
            </div>
        </div>
    </div>

    <!-- Elementos Html para control desde fichero JS-->
    <input type="hidden" id="validacionesAlarmasExpte" value=""/>
    <input type="hidden" id="refrescarValidacionesAlarmasExpte" value="0"/>
    <script>document.getElementById("validacionesAlarmasExpte").value = JSON.stringify(<%=validacionesAlarmasCONCMJsonString%>, function (key, value) {
            return value == null ? "" : value;
        });</script>

</div>    
<script type="text/javascript">

    //Iniciamos la tabla para los periodos
    var tabConciliacion;
    //if(document.all) tabConciliacion = new Tabla(document.all.tabConciliacion);
    //else 
    tabConciliacion = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tabConciliacion'), 1164);
    tabConciliacion.addColumna('110', 'center', '<%=configuracion.getParameter("ETIQUETA_DESDE_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('110', 'center', '<%=configuracion.getParameter("ETIQUETA_HASTA_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('100', 'center', '<%=configuracion.getParameter("ETIQUETA_PORC_SUBVEN_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('70', 'right', '<%=configuracion.getParameter("ETIQUETA_NUMDIAS_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('120', 'right', '<%=configuracion.getParameter("ETIQUETA_BASE_COTIZACION_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('120', 'right', '<%=configuracion.getParameter("ETIQUETA_BONIFICACION_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('120', 'right', '<%=configuracion.getParameter("ETIQUETA_GASTO_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('120', 'right', '<%=configuracion.getParameter("ETIQUETA_PORC_JORN_REALIZADA_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.addColumna('120', 'right', '<%=configuracion.getParameter("ETIQUETA_PORC_JORN_SUSTITUCION_" + idiomaUsuario,nombreModulo)%>');
    tabConciliacion.displayCabecera = true;
    tabConciliacion.height = 200;
    tabConciliacion.displayTabla();
    tabConciliacion.displayDatos = modificarPeriodo;
    //tabConciliacion.displayDatos = modificarPeriodo;

    cargarPeriodos();

    //Funcion que carga la tabla con los periodos y la informacion recibida al iniciar la JSP
    function cargarPeriodos() {
        //Primero comprobamos que el codigo de error indique que esta todo correcto

        desbloquearFormulario();
        deshabilitarFormulario(false);
        var indice = 0;
    <%for(int x=0;x<periodos.size();x++){
                DatosPeriodoVO dato = periodos.get(x);%>
        registros[indice] = ['<%=dato.getFechaInicioAsString()%>', '<%=dato.getFechaFinAsString()%>', '<%=dato.getPorcSubven()%>',
            '<%=dato.getNumDias()%>', '<%=dato.getBaseCotizacion()%>', '<%=dato.getBonificacion()%>', '<%=dato.getGasto()%>', '<%=(dato.getPorcJornRealizada()!=null?dato.getPorcJornRealizada():"")%>', '<%=(dato.getPorcJornSustitucion()!=null?dato.getPorcJornSustitucion():"")%>'];
        indice++;
    <%}%>//for

        tabConciliacion.lineas = copiarRegistrosMostrarEnTabla(registros);
        tabConciliacion.displayTabla();
        if (registros.length > 0) {
            actualizarCampoOculto();
            var totalNumDias = '<%=totalNumDias%>';
            var gastoTotal = '<%=gastoTotal%>';
            var sDescuento = '<%=descuento%>';
            var diasRestantes = '<%=diasRestantes%>';
            var totalGastosConDescuento = '<%=totalGastosConDescuento%>';

            totalConDescuento = totalGastosConDescuento;

            if (totalNumDias == "null") {
                mostrarTotalDias('');
            } else {
                mostrarTotalDias('<%=totalNumDias%>');
            }

            if (gastoTotal == "null")
                mostrarImporteTotal('');
            else
                mostrarImporteTotal('<%=gastoTotal%>');

            /** preuba **/
            if (sDescuento == null)
                mostrarDescuento('');
            else
                mostrarDescuento(sDescuento);

            if (totalGastosConDescuento == null)
                mostrarTotalGastosConDescuento('');
            else
                mostrarTotalGastosConDescuento(totalGastosConDescuento);
            /**/

            if (diasRestantes == null) {
                mostrarDiasRestantes('');
            } else {
                mostrarDiasRestantes(diasRestantes);
            }//if(diasRestantes == null)
        } else {
            ocultarTotalDias();
            ocultarImporteTotal();
            ocultarDiasRestantes();
        }//if(registros.length>0)

        if (codError != 0) {
            //deshabilitarFormulario(true); 
            if (codError == 1)
                mostrarMensajeError('<%=configuracion.getParameter("1_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 1)
                mostrarMensajeError('<%=configuracion.getParameter("2_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 3)
                mostrarMensajeError('<%=configuracion.getParameter("3_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 6)
                mostrarMensajeError('<%=configuracion.getParameter("6_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 7)
                mostrarMensajeError('<%=configuracion.getParameter("7_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 8)
                mostrarMensajeError('<%=configuracion.getParameter("8_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 9)
                mostrarMensajeError('<%=configuracion.getParameter("9_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 11)
                mostrarMensajeError('<%=configuracion.getParameter("11_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 12)
                mostrarMensajeError('<%=configuracion.getParameter("12_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 13)
                mostrarMensajeError('<%=configuracion.getParameter("13_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 14)
                mostrarMensajeError('<%=configuracion.getParameter("14_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 15)
                mostrarMensajeError('<%=configuracion.getParameter("15_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 16)
                mostrarMensajeError('<%=configuracion.getParameter("16_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 17)
                mostrarMensajeError('<%=configuracion.getParameter("17_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 18)
                mostrarMensajeError('<%=configuracion.getParameter("18_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 19)
                mostrarMensajeError('<%=configuracion.getParameter("19_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 20)
                mostrarMensajeError('<%=configuracion.getParameter("20_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 21)
                mostrarMensajeError('<%=configuracion.getParameter("21_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 22)
                mostrarMensajeError('<%=configuracion.getParameter("22_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 23)
                mostrarMensajeError('<%=configuracion.getParameter("23_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 24)
                mostrarMensajeError('<%=configuracion.getParameter("24_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 25)
                mostrarMensajeError('<%=configuracion.getParameter("25_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 26)
                mostrarMensajeError('<%=configuracion.getParameter("26_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 28)
                mostrarMensajeError('<%=configuracion.getParameter("28_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            else
            if (codError == 29) {
                mostrarMensajeError('<%=configuracion.getParameter("29_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            } else
            if (codError == 30) {
                mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
            }

        }

    }//cargarPeriodos

    //Modifica y formatea los datos contenidos en el array para la tabla de periodos
    function copiarRegistrosMostrarEnTabla(lista) {
        // Se modifican los campos del array para formatear los campos numéricos decimales
        var aux = new Array();


        for (j = 0; lista != null && j < lista.length; j++) {
            var fechaInicio = lista[j][0];
            var fechaFin = lista[j][1];
            var porcSubven = (lista[j][2] != null && lista[j][2] != "" ? formatNumeroDecimal(lista[j][2]) : "");
            var numDias = lista[j][3];
            var base = "";
            var bonificacion = "";
            var gasto = "";
            var porcJornRealizada = "";
            var porcJornSutitucion = "";

            if (lista[j][4] != null && lista[j][4] != "")
                base = formatNumeroDecimal(lista[j][4]);

            if (lista[j][5] != null && lista[j][5] != "")
                bonificacion = formatNumeroDecimal(lista[j][5]);

            if (lista[j][6] != null && lista[j][6] != "") {
                gasto = formatNumeroDecimal(lista[j][6]);
            }

            if (lista[j][7] != null && lista[j][7] != "") {
                porcJornRealizada = formatNumero(lista[j][7]);
            }
            if (lista[j][8] != null && lista[j][8] != "") {
                porcJornSutitucion = formatNumero(lista[j][8]);
            }

            aux[j] = [fechaInicio, fechaFin, porcSubven, numDias, base, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
        }//for

        return aux;
    }//copiarRegistrosMostrarEnTabla



    function convertirPeriodosParametro() {
        var salida = "";
        var separador = '@M01@';

        for (i = 0; i < registros.length; i++) {

            var fechaInicio = registros[i][0];
            var fechaFin = registros[i][1];
            var porcentaje = registros[i][2];
            var numDias = registros[i][3];
            var baseCotizacion = registros[i][4];
            var bonificacion = registros[i][5];
            var gasto = registros[i][6];
            var porcJornRealizada = registros[i][7];
            var porcJornSutitucion = registros[i][8];

            var fila = fechaInicio + "#" + fechaFin + "#" + porcentaje + "#" + numDias + "#" + baseCotizacion + "#" + bonificacion + "#" + gasto + "#" + porcJornRealizada + "#" + porcJornSutitucion;
            salida = salida + fila;
            if (registros.length - i > 0) {
                salida = salida + escape(separador);
            }
        }
        return salida;
    }

    //Funcion que se desencadena al modificar los datos del expediente y que recarga la tabla con los nuevos periodos
    //en caso de que se hayan modificado los datos suplementarios del expediente
    function recargarDatosPagina() {
        var totalDias = "";
        var gastoTotal = "";
        var nodos = null;
        var ajax = getXMLHttpRequest();

        var parametrosPeriodos = convertirPeriodosParametro();
        if (ajax != null) {
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=recargarDatosPagina&modulo=" + nombreModulo + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpediente + "&tipo=0" + "&periodos=" + parametrosPeriodos;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try {
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
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                ocultarTotalDias();
                ocultarImporteTotal();

                //if(codigoOperacion=="0"){
                tabConciliacion.lineas = new Array();
                tabConciliacion.displayTabla();

                desbloquearFormulario();
                deshabilitarFormulario(false);
                ocultarMensajeError();
                registros = new Array();
                var gastoTotal = "";
                var diasRestantes = "";

                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "GASTO_TOTAL") {
                        try {
                            if (hijos[j].childNodes[0] != null)
                                gastoTotal = hijos[j].childNodes[0].nodeValue;
                            else
                                gastoTotal = "";
                        } catch (err) {

                        }

                    } else if (hijos[j].nodeName == "TOTAL_DIAS") {
                        totalDias = hijos[j].childNodes[0].nodeValue;
                    } else if (hijos[j].nodeName == "DIAS_SUBVENCIONABLES_RESTANTES") {
                        diasRestantes = hijos[j].childNodes[0].nodeValue;
                    } else if (hijos[j].nodeName == "PERIODOS") {

                        var listaUnidades = xmlDoc.getElementsByTagName("PERIODOS");
                        var nodosUnidades = listaUnidades[0];
                        var hijosUnidades = nodosUnidades.childNodes;
                        for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                            var inicioPeriodo = "";
                            var finPeriodo = "";
                            var numDias = "";
                            var baseCotizacion = "";
                            var bonificacion = "";
                            var porcSubvenc = "";
                            var gasto = "";
                            var porcJornRealizada = "";
                            var porcJornSutitucion = "";
                            if (hijosUnidades[x].nodeName == "PERIODO") {

                                var listaUnidades = xmlDoc.getElementsByTagName("PERIODO");
                                for (x = 0; listaUnidades != null && x < listaUnidades.length; x++) {
                                    var unidad = listaUnidades[x].childNodes;
                                    for (y = 0; unidad != null && y < unidad.length; y++) {
                                        if (unidad[y].nodeName == "INICIO_PERIODO") {
                                            inicioPeriodo = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "FIN_PERIODO") {
                                            finPeriodo = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "NUM_DIAS") {
                                            numDias = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "BASE_COTIZACION") {
                                            baseCotizacion = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "BONIFICACION") {
                                            bonificacion = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "PORC_SUBVENC") {
                                            porcSubvenc = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "GASTO") {
                                            gasto = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "PORC_JORN_REALIZADA") {
                                            porcJornRealizada = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "PORC_JORN_SUSTITUCION") {
                                            porcJornSutitucion = unidad[y].childNodes[0].nodeValue;
                                        }

                                    }
                                    registros[x] = [inicioPeriodo, finPeriodo, porcSubvenc, numDias, baseCotizacion, bonificacion, gasto, porcJornRealizada, porcJornSutitucion];
                                    baseCotizacion = "";
                                    bonificacion = "";
                                    porcSubvenc = "";
                                    gasto = "";
                                    porcJornRealizada = "";
                                    porcJornSutitucion = "";
                                }
                            }
                        }
                    }
                }

                tabConciliacion.lineas = copiarRegistrosMostrarEnTabla(registros);
                tabConciliacion.displayTabla();
                actualizarCampoOculto();
                mostrarTotalDias(totalDias);
                mostrarImporteTotal(gastoTotal);
                mostrarDiasRestantes(diasRestantes);
                totalSinDescuento = gastoTotal;
                recalcularTotalConDescuento();

                // Validamos de Nuevo los datos del Expediente funcion en js gestionConciliacionSubvencion
                //Se mostrara la validacion en el Modaly el resultado de esta operacion en el Div de errores.
                actualizarValorImportesEmpresaReglaMinimis();
                validarControlAlarmasCONCM();

                if (codigoOperacion == "1") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("1_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "2") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("2_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "3") {
                    desbloquearFormulario();
                    deshabilitarFormulario(true);
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("3_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "8") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("8_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "16") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("16_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "17") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("17_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "18") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("18_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "19") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("19_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "20") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("20_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "22") {
                    bloquearFormulario();
                    ocultarDiasRestantes();
                    mostrarMensajeError('<%=configuracion.getParameter("21_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "0000") {
                    //Todo correcto y sin actualizaciones
                }//if(codigoOperacion)   
                else if (codigoOperacion == "26") {
                    bloquearFormulario();
                    mostrarDiasRestantes(diasRestantes);
                    mostrarMensajeError('<%=configuracion.getParameter("26_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "28") {
                    //bloquearFormulario();                    
                    mostrarMensajeError('<%=configuracion.getParameter("28_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "29") {
                    //bloquearFormulario();                    
                    mostrarMensajeError('<%=configuracion.getParameter("29_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                } else if (codigoOperacion == "30") {
                    //bloquearFormulario();                    
                    mostrarMensajeError('<%=configuracion.getParameter("30_MSG_ERROR_" + idiomaUsuario,nombreModulo)%>');
                }

            } catch (Err) {
                jsp_alerta("A", "Error.descripcion: " + Err.message);
            }//try-catch
        }//if(ajax!=null)
    }//recargarDatosPagina

    //Funcion que carga en un div de la JSP el mensaje de error que le pasemos como parametro
    function mostrarMensajeError(msg) {
        //document.getElementById("mensajeError").innerHTML = msg;
        mostrarMensajeErrorModuloEnDiv(msg, true);
    }//mostrarMensajeError

    //Oculta el posible mensaje de error que se pudiera estar mostrando
    function ocultarMensajeError() {
        //document.getElementById("mensajeError").innerHTML = "";
        limpiarOcultarMensajeErrorModuloEnDiv();
    }//ocultarMensajeError

    //Funcion que carga en las cajas de texto los datos de la fila seleccionada en la tabla de periodos
    function modificarPeriodo() {
        var index = tabConciliacion.selectedIndex;
        if (index != -1) {
            var registro = registros[index];
            document.getElementById("meLanbide01FechaDesde").value = registro[0];
            document.getElementById("meLanbide01FechaHasta").value = registro[1];
            var porcentajeSubvencion = registro[2];
            porcentajeSubvencion = porcentajeSubvencion.replace(".", ",");
            document.getElementById("meLanbide01PorcSubvenc").value = porcentajeSubvencion;
            document.getElementById("meLanbide01NumDias").value = registro[3];
            var baseCotizacion = registro[4].replace(".", ",");
            document.getElementById("meLanbide01BaseCotizacion").value = baseCotizacion;
            var bonificacion = registro[5].replace(".", ",");
            if (bonificacion == null || bonificacion == "") {
                document.getElementById("meLanbide01Bonificacion").value = 0;
            } else {
                document.getElementById("meLanbide01Bonificacion").value = bonificacion;
            }
            var gasto = registro[6].replace(".", ",");
            document.getElementById("meLanbide01Gasto").value = gasto;

            var porcJornRealizada = formatNumero(registro[7]);
            $("#porcJornRealizada").val(porcJornRealizada);
            var porcJornSutitucion = formatNumero(registro[8]);
            $("#porcJornSustitucion").val(porcJornSutitucion);

            /** original
             if(index == 0){
             bloquearParaPrimerRegistro();
             }else if(index == registros.length-1){
             bloquearParaUltimoRegistro();
             }else{
             bloquearParaRegistroIntermedio();
             }//if(index == 0)
             **/
        }//if(index!=-1)
    }//modificarPeriodo

    //En caso de que seleccionemos el primer registro de la tabla de periodos esta funcion impedira que modifiquemos la fecha de fin
    //para que sea consistente con la del siguiente periodo
    function bloquearParaPrimerRegistro() {
        document.getElementById("meLanbide01FechaHasta").disabled = true;
        deshabilitarImagenCal("meLanbide01CalFechaHasta", true);

        document.getElementById("meLanbide01FechaDesde").disabled = false;
        deshabilitarImagenCal("meLanbide01CalFechaDesde", false);
    }//bloquearParaPrimerRegistro

    //En caso de que seleccionemos el ultimo registro de la tabla de periodos esta funcion impedira que modifiquemos la echa de inicio
    //para que sea consistente con la del periodo anterior
    function bloquearParaUltimoRegistro() {
        document.getElementById("meLanbide01FechaDesde").disabled = true;
        deshabilitarImagenCal("meLanbide01CalFechaDesde", true);

        document.getElementById("meLanbide01FechaHasta").disabled = false;
        deshabilitarImagenCal("meLanbide01CalFechaHasta", false);
    }//bloquearParaUltimoRegistro

    //En caso de que seleccionemos un registro intermedio de la tabla de periodos esta funcion impedira que modifiquemos las fechas
    //para que sean coherentes con el periodo anterior y el siguiente
    function bloquearParaRegistroIntermedio() {
        document.getElementById("meLanbide01FechaHasta").disabled = true;
        deshabilitarImagenCal("meLanbide01CalFechaHasta", true);
        document.getElementById("meLanbide01FechaDesde").disabled = true;
        deshabilitarImagenCal("meLanbide01CalFechaDesde", true);
    }//bloquearParaRegistroIntermedio

    //Devuelve un objeto XMLHttpRequest para su uso con las funciones AJAX
    function getXMLHttpRequest() {
        var aVersions = ["MSXML2.XMLHttp.5.0", "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp", "Microsoft.XMLHttp"];
        if (window.XMLHttpRequest) {
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                } catch (error) {
                    //no necesitamos hacer nada especial
                }//try-catch
            }//for (var i = 0; i < aVersions.length; i++)
        }//if
    }//getXMLHttpRequest

    //Funcion para el calendario de fecha desde
    function mostrarCalFechaDesde(event) {
        if (window.event)
            event = window.event;
        if (document.getElementById("meLanbide01CalFechaDesde").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'meLanbide01FechaDesde', null, null, null, '', 'meLanbide01CalFechaDesde', '', null, null, null, null, null, null, null, '', event);
    }//mostrarCalFechaDesde

    //Funcion para el calendario de fecha hasta
    function mostrarCalFechaHasta(event) {
        if (window.event)
            event = window.event;
        if (document.getElementById("meLanbide01CalFechaHasta").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'meLanbide01FechaHasta', null, null, null, '', 'meLanbide01CalFechaHasta', '', null, null, null, null, null, null, null, '', event);
    }//mostrarCalFechaHasta

    //Funcion que deshabilita el formulario en funcion del flag que reciba como parametro
    function deshabilitarFormulario(flag) {
        if (flag) {
            document.getElementById("btnMeAlta").disabled = true;
            document.getElementById("btnMeModificar").disabled = true;
            document.getElementById("btnMeEliminar").disabled = true;
            document.getElementById("btnMeGrabar").disabled = true;

            document.getElementById('btnMeAlta').style.color = '#CCCCCC';
            document.getElementById('btnMeModificar').style.color = '#CCCCCC';
            document.getElementById('btnMeEliminar').style.color = '#CCCCCC';
            document.getElementById('btnMeGrabar').style.color = '#CCCCCC';

            document.getElementById("meLanbide01FechaDesde").disabled = true;
            document.getElementById("meLanbide01FechaHasta").disabled = true;
            document.getElementById("meLanbide01PorcSubvenc").disabled = true;
            document.getElementById("meLanbide01NumDias").disabled = true;
            document.getElementById("meLanbide01BaseCotizacion").disabled = true;
            document.getElementById("meLanbide01Bonificacion").disabled = true;
            document.getElementById("meLanbide01Gasto").disabled = true;
            $("#porcJornRealizada").attr("disabled", "disabled");
            $("#porcJornSustitucion").attr("disabled", "disabled");

            deshabilitarImagenCal("meLanbide01CalFechaDesde", true);
            deshabilitarImagenCal("meLanbide01CalFechaHasta", true);
        } else {
            document.getElementById("btnMeAlta").disabled = false;
            document.getElementById("btnMeModificar").disabled = false;
            document.getElementById("btnMeEliminar").disabled = false;
            document.getElementById("btnMeGrabar").disabled = false;
            document.getElementById('btnMeAlta').style.color = '#FFFFFF';
            document.getElementById('btnMeModificar').style.color = '#FFFFFF';
            document.getElementById('btnMeEliminar').style.color = '#FFFFFF';
            document.getElementById('btnMeGrabar').style.color = '#FFFFFF';

            document.getElementById("meLanbide01FechaDesde").disabled = false;
            document.getElementById("meLanbide01FechaHasta").disabled = false;
            if (document.getElementById("meLanbide01PorcSubvenc") !== null)
                document.getElementById("meLanbide01PorcSubvenc").disabled = false;
            document.getElementById("meLanbide01NumDias").disabled = false;
            document.getElementById("meLanbide01BaseCotizacion").disabled = false;
            document.getElementById("meLanbide01Bonificacion").disabled = false;
            document.getElementById("meLanbide01Gasto").disabled = false;
            $("#porcJornRealizada").removeAttr("disabled");
            $("#porcJornSustitucion").removeAttr("disabled");

            habilitarImagenCal("meLanbide01CalFechaDesde", true);
            habilitarImagenCal("meLanbide01CalFechaHasta", true);
        }//if(flag){
    }// deshabilitarFormulario

    //Bloquea todos los campos y botones del formulario
    function bloquearFormulario() {
        document.getElementById("btnMeAlta").disabled = true;
        document.getElementById("btnMeModificar").disabled = true;
        document.getElementById("btnMeEliminar").disabled = true;
        document.getElementById("btnMeGrabar").disabled = true;
        document.getElementById("btnMeLimpiar").disabled = true;
        document.getElementById("btnMeRecalcular").disabled = true;

        document.getElementById('btnMeAlta').style.color = '#CCCCCC';
        document.getElementById('btnMeModificar').style.color = '#CCCCCC';
        document.getElementById('btnMeEliminar').style.color = '#CCCCCC';
        document.getElementById('btnMeGrabar').style.color = '#CCCCCC';
        document.getElementById('btnMeLimpiar').style.color = '#CCCCCC';
        document.getElementById('btnMeRecalcular').style.color = '#CCCCCC';

        document.getElementById("meLanbide01FechaDesde").disabled = true;
        document.getElementById("meLanbide01FechaHasta").disabled = true;
        document.getElementById("meLanbide01PorcSubvenc").disabled = true;
        document.getElementById("meLanbide01NumDias").disabled = true;
        document.getElementById("meLanbide01BaseCotizacion").disabled = true;
        document.getElementById("meLanbide01Bonificacion").disabled = true;
        document.getElementById("meLanbide01Gasto").disabled = true;
        $("#porcJornRealizada").attr("disabled", "disabled");
        $("#porcJornSustitucion").attr("disabled", "disabled");

        deshabilitarImagenCal("meLanbide01CalFechaDesde", true);
        deshabilitarImagenCal("meLanbide01CalFechaHasta", true);
    }//bloquearFormulario

    //Desbloquea todos los campos y botones del formulario
    function desbloquearFormulario() {
        document.getElementById("btnMeAlta").disabled = false;
        document.getElementById("btnMeModificar").disabled = false;
        document.getElementById("btnMeEliminar").disabled = false;
        document.getElementById("btnMeGrabar").disabled = false;
        document.getElementById("btnMeLimpiar").disabled = false;
        document.getElementById("btnMeRecalcular").disabled = false;

        document.getElementById('btnMeAlta').style.color = '#FFFFFF';
        document.getElementById('btnMeModificar').style.color = '#FFFFFF';
        document.getElementById('btnMeEliminar').style.color = '#FFFFFF';
        document.getElementById('btnMeGrabar').style.color = '#FFFFFF';
        document.getElementById('btnMeLimpiar').style.color = '#FFFFFF';
        document.getElementById('btnMeRecalcular').style.color = '#FFFFFF';

        document.getElementById("meLanbide01FechaDesde").disabled = false;
        document.getElementById("meLanbide01FechaHasta").disabled = false;
        if (document.getElementById("meLanbide01PorcSubvenc") !== null)
            document.getElementById("meLanbide01PorcSubvenc").disabled = false;
        document.getElementById("meLanbide01NumDias").disabled = false;
        document.getElementById("meLanbide01BaseCotizacion").disabled = false;
        document.getElementById("meLanbide01Bonificacion").disabled = false;
        document.getElementById("meLanbide01Gasto").disabled = false;
        $("#porcJornRealizada").removeAttr("disabled");
        $("#porcJornSustitucion").removeAttr("disabled");

        habilitarImagenCal("meLanbide01CalFechaDesde", true);
        habilitarImagenCal("meLanbide01CalFechaHasta", true);
    }//desbloquearFormulario

    //Funcion que muestra en un div el total de dias que le pases como parametro
    function mostrarDiasRestantes(diasRestantes) {
        document.getElementById("divDiasRestantes").innerHTML = diasRestantes;
    }//mostrarDiasRestantes

    //Oculta el total de dias restantes subvencionables
    function ocultarDiasRestantes() {
        document.getElementById("divDiasRestantes").innerHTML = "";
    }//ocultarDiasRestantes

    //Funcion que muestra en un div el total de dias que le pases como parametro
    function mostrarTotalDias(totalNumDias) {
        document.getElementById("divTotalNumDias").innerHTML = totalNumDias;
    }//mostrarTotalDias

    //Oculta el total de dias 
    function ocultarTotalDias() {
        document.getElementById("divTotalNumDias").innerHTML = "";
    }//ocultarTotalDias

    //Funcion que muestra en un div el total del importe subvencionado
    function mostrarImporteTotal(totalImporte) {
        if (totalImporte == '') {
            document.getElementById("divTotalImporte").innerHTML = '';
        } else {
            document.getElementById("divTotalImporte").innerHTML = formatNumeroDecimal(totalImporte);
        }//if(totalImporte == '')
    }//mostrarImporteTotal


    // Función que muestra en el div correspondiente el gasto total menos el descuento que se le aplica
    function mostrarTotalGastosConDescuento(gasto) {
        document.getElementById("divTotalImporteConDescuento").innerHTML = formatNumeroDecimal(gasto);
    }

    function recalcularTotalConDescuento() {
        var descuento = document.forms[0].descuento.value;

        var fDescuento = parseFloat(descuento);
        var apDescuento = (parseFloat(totalSinDescuento) * fDescuento) / 100;
        var totalRestante = totalSinDescuento - apDescuento;
        totalConDescuento = totalRestante;
        var cadena = new String(totalRestante);
        document.getElementById("divTotalImporteConDescuento").innerHTML = formatNumeroDecimal(cadena);

    }

    //Oculta el total del importe subvencionado
    function ocultarImporteTotal() {
        document.getElementById("divTotalImporte").innerHTML = "";
    }//ocultarImporteTotal



    // Función que muestra en el div correspondiente el gasto total menos el descuento que se le aplica
    function mostrarDescuento(descuento) {
        document.forms[0].descuento.value = descuento;
    }


</script>
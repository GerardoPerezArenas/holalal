<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO" %>
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = sIdioma!=null && sIdioma!= "" ? Integer.parseInt(sIdioma) : 1;
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

    String codEntidad = (String)request.getAttribute("codEntidad") != null ? (String)request.getAttribute("codEntidad") : "";
    String cifEntidad = (String)request.getAttribute("cifEntidad") != null ? (String)request.getAttribute("cifEntidad") : "";
    String nombreEntidad = (String)request.getAttribute("nombreEntidad") != null ? (String)request.getAttribute("nombreEntidad") : "";
    String nombreCompleto = (cifEntidad != null ? cifEntidad + " - " : "") + nombreEntidad; 
    EntidadVO entidad = (EntidadVO) request.getAttribute("entidad");
    List<OriTrayectoriaEntidadVO> listaResumen = (List<OriTrayectoriaEntidadVO>)request.getAttribute("listaResumen");
    String totalMesesNoSolapadoValidado = (String)request.getAttribute("totalMesesNoSolapadoValidado");
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/TablaNuevaORI.js"></script>
<script type="text/javascript">
    var baseUrl = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';

    function recargarTablaResumen(result) {
        var total = 0;
        var codOperacion = result != null ? result[0] : null;
        var filaResumen;
        if (codOperacion != null) {
            listaResumen = new Array();
            listaResumenTabla = new Array();
            for (var i = 1; i < result.length; i++) {
                filaResumen = result[i];
                listaResumen[i - 1] = [filaResumen[0], filaResumen[1], filaResumen[2], filaResumen[3], filaResumen[4], filaResumen[5], filaResumen[6], filaResumen[7], filaResumen[8], filaResumen[9], filaResumen[10], filaResumen[11], filaResumen[12], filaResumen[13], filaResumen[14]];
                if (filaResumen[1] == 2) {
                    if (filaResumen[5] != '-') {
                        if (filaResumen[5] == 1) {
                            listaResumenTabla[i - 1] = [filaResumen[4], filaResumen[5], filaResumen[6], filaResumen[7], filaResumen[8], (filaResumen[9] != null && filaResumen[9] != "null" ? filaResumen[9] : "-"), (filaResumen[10] != null && filaResumen[10] != "null" ? filaResumen[10] : "-"), (filaResumen[11] != null && filaResumen[11] != "null" ? filaResumen[11] : "-"), (filaResumen[12] != null && filaResumen[12] != "null" ? filaResumen[12] : "-")];
                            total += parseInt(filaResumen[8]);
                        } else {
                            listaResumenTabla[i - 1] = ['<span style="color:red">' + filaResumen[4] + '</span>', filaResumen[5], filaResumen[6], filaResumen[7], filaResumen[8], (filaResumen[9] != null && filaResumen[9] != "null" ? filaResumen[9] : "-"), (filaResumen[10] != null && filaResumen[10] != "null" ? filaResumen[10] : "-"), (filaResumen[11] != null && filaResumen[11] != "null" ? filaResumen[11] : "-"), (filaResumen[12] != null && filaResumen[12] != "null" ? filaResumen[12] : "-")];
                        }
                    } else {
                        listaResumenTabla[i - 1] = ['<span style="color:red">' + filaResumen[4] + '</span>', filaResumen[5], filaResumen[6], filaResumen[7], filaResumen[8], (filaResumen[9] != null && filaResumen[9] != "null" ? filaResumen[9] : "-"), (filaResumen[10] != null && filaResumen[10] != "null" ? filaResumen[10] : "-"), (filaResumen[11] != null && filaResumen[11] != "null" ? filaResumen[11] : "-"), (filaResumen[12] != null && filaResumen[12] != "null" ? filaResumen[12] : "-")];
                    }
                } else {
                    listaResumenTabla[i - 1] = [filaResumen[4], filaResumen[5], filaResumen[6], filaResumen[7], filaResumen[8], (filaResumen[9] != null && filaResumen[9] != "null" ? filaResumen[9] : "-"), (filaResumen[10] != null && filaResumen[10] != "null" ? filaResumen[10] : "-"), (filaResumen[11] != null && filaResumen[11] != "null" ? filaResumen[11] : "-"), (filaResumen[12] != null && filaResumen[12] != "null" ? filaResumen[12] : "-")];
                    total += parseInt(filaResumen[8]);
                }
            }
            crearTablaResumen();
            tablaResumen.lineas = listaResumenTabla;
            tablaResumen.displayCabecera = true;
            tablaResumen.height = 250;
            tablaResumen.displayTabla();
            //document.getElementById('totalMeses').value = total;
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
        if($("#totalMesesVal").val()!=null && $("#totalMesesVal").val()!=undefined && $("#totalMesesVal").val()!="" ){
            $("#inputMensajeMesesValidadosNoGuardados").val("1");
        }else{
            $("#inputMensajeMesesValidadosNoGuardados").val("0");
        }
        ocultarMostrarMensajeMesesValidadosNoGuardados();
    }

    function crearTablaResumen() {
        tablaResumen = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaResumen'), 1200, 25);
        tablaResumen.addColumna('600', 'left', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.programa")%>");
        tablaResumen.addColumna('0', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.experiencia")%>");
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%>", 'Date');
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%>", 'Date');
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%>", 'Number');
        tablaResumen.addColumna('0', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.experienciaVal")%>");
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>", 'Date');
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>", 'Date');
        tablaResumen.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%>");
    }

    function refrescarPestanaResumen() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=refrescarPestanaResumen&tipo=0&numero=<%=numExpediente%>&codEntidad=<%=codEntidad%>';
        try {
            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var listaResumenNueva = extraerListaResumen(nodos);
            var codigoOperacion = listaResumenNueva[0];
            if (codigoOperacion == "0") {
                recargarTablaResumen(listaResumenNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.resumen"))%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.resumen"))%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.resumen"))%>');
            } else {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.resumen"))%>');
            }

        } catch (Err) {
            jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.resumen"))%>');
        }//try-catch
    }

    function extraerListaResumen(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaDatosRespuesta = new Array();
        var filaResumen = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaDatosRespuesta[j] = codigoOperacion;
            } else if (hijos[j].nodeName == "TRAYECTORIAS") {
                var nodoFila1 = hijos[j];
                var hijosFila1 = nodoFila1.childNodes;
                for (var cont1 = 0; cont1 < hijosFila1.length; cont1++) {
                    nodoFila = hijosFila1[cont1];
                    hijosFila = nodoFila.childNodes;
                    for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos ACTIVIDAD
                        if (hijosFila[cont].nodeName == "ID") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[0] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[0] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTGRUPO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[1] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[1] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTSUBGRPRE") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[2] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[2] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYCODIGOENTIDAD") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[3] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[3] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYDESCRIPCION") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[4] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[4] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIA") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[5] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[5] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[6] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[6] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFIN") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[7] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[7] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESES") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[8] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[8] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIAVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[9] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[9] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIOVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[10] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[10] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFINVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[11] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[11] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESESVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[12] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[12] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[13] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[13] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_NOM") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                filaResumen[14] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                filaResumen[14] = '-';
                            }
                        }
                    }// nodo TRAYECTORIA
                    listaDatosRespuesta[cont1 + 1] = filaResumen;
                    filaResumen = new Array();
                } //for(var cont1 = 0; cont1 < hijosFila1.length; cont1++)
            }// if trayectoriaS

        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaDatosRespuesta;
    }

    function copiarMeses() {
        if (document.getElementById("totalMeses").value != null) {
            document.getElementById("totalMesesVal").value = document.getElementById("totalMeses").value;
            document.getElementById('msgEliminandoTrayectoria').style.display = "none";
            document.getElementById('msgCopiandoTrayectoria').style.display = "inline";
            validarMeses();
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.trayectoria21.faltanMeses")%>');
        }
    }

    function validarMeses() {
    if (document.getElementById("totalMesesVal").value != null) {
        var mesesSol = document.getElementById("totalMeses").value;
        var mesesVal = document.getElementById("totalMesesVal").value;
        
        var urlParam = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarMesesValidados&tipo=0&numero=<%=numExpediente%>&mesesValidados=' + mesesVal +'&mesesSolicitud' + mesesSol;
        try {
            $.ajax({
                url: baseUrl,
                type: 'POST',
                async: true,
                data: urlParam,
                beforeSend: antesDeLlamar,
                success: procesarRespuestaValidar,
                error: mostrarMensajeRespuestaResumen
            });
        } catch (Err) {
            mostrarMensajeRespuestaResumen();
        }
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.trayectoria21.faltanMeses")%>');
        }
    }

    function antesDeLlamar() {
        elementoVisible('on', 'barraProgresoTrayectoria');
    }
    function procesarRespuestaValidar(result) {
        elementoVisible('off', 'barraProgresoTrayectoria');
        if (result) {
            var datos = JSON.parse(result);
            datos = datos.tabla;
            if (datos.error == "0") {   
                refrescarPestanaResumen();
                refrescarPestanasORI14(99);
                // Actualizar el valor de puntos por trayectoria segun lo guardado
                if($("#totalMesesVal").val()!=null && $("#totalMesesVal").val()!= undefined && $("#totalMesesVal").val()!=""){
                    var tempNumber  = 0.0;
                    tempNumber = $("#totalMesesVal").val()!= null && $("#totalMesesVal").val()!= undefined && $("#totalMesesVal").val()!="" ? (parseFloat($("#totalMesesVal").val().replace(",","."))*0.2).toFixed(2) : null;
                    $('#trayectoriaVal').val(tempNumber!=null ? String(tempNumber).replace(".",",") : "");
                }
            }
            mostrarMensajeRespuestaResumen(parseInt(datos.error));
        } else {
            mostrarMensajeRespuestaResumen();
        }
    }

    function mostrarMensajeRespuestaResumen(codigo) {
        var msgtitle = "ERROR EN EL PROCESO";
        var mensaje;
        switch (codigo) {
            case 0:
                msgtitle = "PROCESO CORRECTO";
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                break;
            case 1:
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>';
                break;
            case 2:
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                break;
            case 3:
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>';
                break;
            case 4:
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.trayectoria.errorGuardarTray")%>';
                break;
            default:
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
        }
        botones(false);
        elementoVisible('off', 'barraProgresoTrayectoria');
        jsp_alerta("A", mensaje, msgtitle);
    }
    
    function ocultarMostrarMensajeMesesValidadosNoGuardados(){
        if($("#inputMensajeMesesValidadosNoGuardados").val()==1){
            $("#textoMensajeMesesValidadosNoGuardados").hide();
        }else{
            $("#textoMensajeMesesValidadosNoGuardados").show();
        }
    }

</script>
<body>
    <fieldset id="fieldsetResumen" style="height: auto; margin-top: 20px; "> 
        <div>
            <div id="datosEntidad" style="text-align: center; background-color: #4B95D3; height: 20px;"title="<%=nombreEntidad%>">
                <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;"align="center">
                    <%=nombreCompleto%>
                </div>
            </div>
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; margin-top: 10px;"> <!--463px-->                   
                <label class="legendAzul" align="center"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria21.resumen.textoLargo")%></label>
                <div id="divTablaResumen" align="center">
                    <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: auto;">
                        <div id="listaResumen" style="padding: 5px; width:98%; font-size: 13px; height: auto; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div> 
                        <div class="lineaFormulario" style="float: right; width: 40%;">                            
                            <div style="width: 120px; float: left;" class="etiqueta">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.total")%>
                            </div> 
                            <div style="width: 40px; float: left;">
                                <div style="float: left;">
                                    <input id="totalMeses" name="totalMeses" type="text" size="6" class="inputTexto" value="<c:out value="${totalMesesNoSolapado}"/>" disabled style="text-align: center;"/>
                                </div>
                            </div>                            
                            <div style="width: 200px; float: left; padding-left: 20px;" class="etiqueta">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.totalVal")%>
                            </div> 
                            <div style="width: 40px; float: left;">
                                <div style="float: left;">
                                    <b>
                                        <input id="totalMesesVal" name="totalMesesVal" type="text" size="6" class="inputTexto" value='<%=entidad!=null && entidad.getOriEntTrayectoriaVal()!=null ? String.valueOf(entidad.getOriEntTrayectoriaVal()).replace(".",",") :totalMesesNoSolapadoValidado%>' style="text-align: center;" onkeypress="return SoloDecimales(event);" />
                                        <input id="inputMensajeMesesValidadosNoGuardados" name="inputMensajeMesesValidadosNoGuardados" type="hidden" value="<%=entidad!=null && entidad.getOriEntTrayectoriaVal()!=null ? 1 : 0 %>" />
                                        <span id="textoMensajeMesesValidadosNoGuardados"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.trayectoria21.mesesValidados.noGuardados")%></span>
                                    </b>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario"  id="botonesMeses" name="botonesMeses" style="float: right; width: 33%; text-align: center; margin-bottom: 10px">
                            <input type="button" id="btnCopiarMeses" name="btnCopiarMeses" class="botonMasLargo" style="margin-top: 5px" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiar.meses")%>" onclick="copiarMeses()"/> 
                            <input type="button" id="btnValidarMeses" name="btnValidarMeses" class="botonMasLargo" style="margin-top: 5px" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.validar.meses")%>" onclick="validarMeses()"/> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>

</body>
<script type="text/javascript">
//  -----------------  RESUMEN
    var tablaResumen;
    var listaResumen = new Array();
    var listaResumenTabla = new Array();
    var total = 0;
    crearTablaResumen();
    <%
        OriTrayectoriaEntidadVO filaResumen= null;
        if(listaResumen!=null){
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0;i <listaResumen.size();i++)
            {
                filaResumen = listaResumen.get(i);
                String experiencia = (filaResumen.getTieneExperiencia() != null ? filaResumen.getTieneExperiencia().toString() : "-");
                String experienciaVal = (filaResumen.getTieneExperienciaVal() != null ? filaResumen.getTieneExperienciaVal().toString() : "-");
                String fecIni="";
                if (filaResumen.getFechaInicio()!=null) {
                    fecIni=formatoFecha.format(filaResumen.getFechaInicio());
                } else {
                    fecIni="-";
                }
                String fecIniVal="";
                if (filaResumen.getFechaInicioVal()!=null) {
                    fecIniVal=formatoFecha.format(filaResumen.getFechaInicioVal());
                } else {
                    fecIniVal="-";
                }
                String fecFin="";
                if (filaResumen.getFechaFin()!=null) {
                    fecFin=formatoFecha.format(filaResumen.getFechaFin());
                } else {
                    fecFin="-";
                }
                String fecFinVal="";
                if (filaResumen.getFechaFinVal()!=null) {
                    fecFinVal=formatoFecha.format(filaResumen.getFechaFinVal());
                } else {
                    fecFinVal="-";
                }
    %>
    listaResumen[<%=i%>] = ['<%=filaResumen.getIdTrayEntidad()%>', '<%=filaResumen.getIdConActGrupo()%>', '<%=filaResumen.getIdConActSubgrupo()%>', '<%=filaResumen.getCodEntidad()%>', '<%=filaResumen.getDescActividad()%>', '<%=experiencia%>', '<%=fecIni%>', '<%=fecFin%>', '<%=filaResumen.getNumMeses()%>', '<%=experienciaVal%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=filaResumen.getNumMesesVal()%>', '<%=filaResumen.getCifEntidad()%>', '<%=filaResumen.getNombreEntidad()%>'];

    if (<%=filaResumen.getIdConActGrupo()%> == 2) {
        if (<%=filaResumen.getTieneExperiencia()%> != null) {
            if (<%=filaResumen.getTieneExperiencia()%> == 1) {
                listaResumenTabla[<%=i%>] = ['<%=filaResumen.getDescActividad()%>', '<%=experiencia%>', '<%=fecIni%>', '<%=fecFin%>', '<%=filaResumen.getNumMeses()%>', '<%=experienciaVal%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(filaResumen.getNumMesesVal() !=null ? filaResumen.getNumMesesVal() : "-")%>'];
                total += <%=filaResumen.getNumMeses()%>;
            } else { // no tiene experiencia
                listaResumenTabla[<%=i%>] = ["<span style='color:red'>" + '<%=filaResumen.getDescActividad()%>' + "</span>", '<%=experiencia%>', '<%=fecIni%>', '<%=fecFin%>', '<%=filaResumen.getNumMeses()%>', '<%=experienciaVal%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(filaResumen.getNumMesesVal() !=null ? filaResumen.getNumMesesVal() : "-")%>'];
            }
        } else {  // no tiene el dato de experiencia
            listaResumenTabla[<%=i%>] = ["<span style='color:red'>" + '<%=filaResumen.getDescActividad()%>' + "</span>", '<%=experiencia%>', '<%=fecIni%>', '<%=fecFin%>', '<%=filaResumen.getNumMeses()%>', '<%=experienciaVal%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(filaResumen.getNumMesesVal() !=null ? filaResumen.getNumMesesVal() : "-")%>'];
        }
    } else {
        listaResumenTabla[<%=i%>] = ['<%=filaResumen.getDescActividad()%>', '<%=experiencia%>', '<%=fecIni%>', '<%=fecFin%>', '<%=filaResumen.getNumMeses()%>', '<%=experienciaVal%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(filaResumen.getNumMesesVal() !=null ? filaResumen.getNumMesesVal() : "-")%>'];
        total += <%=filaResumen.getNumMeses()%>;
    }
    <%
            }
        }
    %>
    tablaResumen.lineas = listaResumenTabla;
    tablaResumen.displayCabecera = true;
    tablaResumen.height = 250;
    tablaResumen.displayTabla();
    //document.getElementById('totalMeses').value = total;
    ocultarMostrarMensajeMesesValidadosNoGuardados();
</script>

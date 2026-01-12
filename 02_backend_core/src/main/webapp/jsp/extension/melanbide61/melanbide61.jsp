<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.PersonaContratoRenovacionPlantillaVO"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaContratoRenovacionPlantillaVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    
    int idiomaUsuario = 1;    
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
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide61/melanbide61.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript">
    var nombreModuloLicitacion = "<%=nombreModulo%>";
    var codOrganizacion = "<%=codOrganizacion%>";
    var numExpRenovacion = "<%=numExpediente%>";

    function actualizaContratos(listaContratos) {
        listaContratos = new Array();
        for (i = 0; i < listaContratos.length; i++) {
            listaContratosTabla[i] = [listaContratos[i][0], listaContratos[i][1], listaContratos[i][2], listaContratos[i][3], listaContratos[i][4]];
        }
        tabContratos.lineas = listaContratosTabla;
        tabContratos.displayTabla();
    }

    function barraProgresoContratos(valor) {
        if (valor == 'on') {
            document.getElementById('barraProgresoContratos').style.visibility = 'inherit';
        } else if (valor == 'off') {
            document.getElementById('barraProgresoContratos').style.visibility = 'hidden';
        }
    }

    function pulsarAltaContrato() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE61&operacion=cargarNuevoContrato&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime(), 900, 1200, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTabla(result);
                }
            }
        });
    }

    // Elimina un contrato de renovacion asociado al expediente.
    function pulsarEliminarContrato() {

        if (tabContratos.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
    <%
        mensajeProgreso = meLanbide61I18n.getMensaje(idiomaUsuario, "msg.procesoEliminarContrato");
    %>
                barraProgresoContratos('on');

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE61&operacion=eliminarContrato&tipo=0&numero=<%=numExpediente%>&idCon=' + listaContratos[tabContratos.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                    var fecha20rel = null;
                    var listaContratosNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaContratosNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                        else if (hijos[j].nodeName == "FECHA20") {
                            fecha20rel = hijos[j].childNodes[0].nodeValue;
                            if (fecha20rel == "-")
                                fecha20rel = "";
                            listaContratosNueva[j] = fecha20rel;
                        }//if(hijos[j].nodeName=="FECHA20")
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    /*if(hijosFila[cont].nodeName=="ID"){
                                     fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="DNICONTRATADO"){
                                     fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="CONTRATADO"){
                                     fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="DNIADICIONAL"){
                                     fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="ADICIONAL"){
                                     fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="DNISUSTITUIDO"){
                                     fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                     }
                                     else if(hijosFila[cont].nodeName=="SUSTITUIDO"){
                                     fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                     }*/
                                    fila[cont] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[cont] = '';
                                }
                            }
                            listaContratosNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTabla(listaContratosNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    alert(Err.description);
                }//try-catch

                barraProgresoContratos('off');
            }
        } else {
            jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTabla(result) {
        var fila;
        var fecha20 = result[1];
        listaContratos = new Array();
        listaContratosTabla = new Array();
        for (var i = 1; i < result.length - 1; i++) {
            fila = result[i + 1];
            //listaContratos[i-1] = fila;//NO FUNCIONA EN ie9
            listaContratos[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
            listaContratosTabla[i - 1] = [fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
        }
        tabContratos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('contratosReple'), 905);
        tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col1")%>");
        tabContratos.addColumna('195', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col2")%>");
        tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col3")%>");
        tabContratos.addColumna('195', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col4")%>");
        tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col5")%>");
        tabContratos.addColumna('178', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col6")%>");

        tabContratos.displayCabecera = true;
        tabContratos.height = 390;
        tabContratos.lineas = listaContratosTabla;
        tabContratos.displayTabla();

        /////////////////////

        try {


            var cargados = false;
            if (!estanCargadosCamposSuplementarios()) {
                if (cargarCamposSuplementarios()) {
                    cargados = true;

                }
            } else {
                cargados = true;
            }

            if (cargados) {
                document.getElementById("FEC20MESESREL").value = fecha20;
            }

        } catch (err) {
            alert("Error al almacenar la fecha20 en el campo suplementario : " + err.description);
        }


//////////////////////

        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('contratosReple');
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[5].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[5].style.width = '100%';
            } catch (err) {

            }
        }
    }

    // Carga los datos de un contrato de renovacion para poder editarlo
    function pulsarModificarContrato(event) {
        var idTabla = document.getElementById('contratosReple').children[0].children[1].children[0].children[0].id;
        var fila;
        if (window.event) { //IE
            if (window.event.srcElement.tagName == 'TD') {
                if (window.event.srcElement.parentElement.parentElement.parentElement.id == idTabla) {
                    fila = window.event.srcElement.parentElement.rowIndex;
                }
            } else if (window.event.srcElement.id == 'btnModificarContrato') {
                fila = tabContratos.selectedIndex;
            }
        } else { // FF
            if (event.target.tagName == 'TD') {
                if (event.target.parentElement.parentElement.parentElement.id == idTabla) {
                    fila = event.target.parentNode.rowIndex;
                }
            } else if (window.event.srcElement.id == 'btnModificarContrato') {
                fila = tabContratos.selectedIndex;
            }
        }

        if (fila >= 0 && fila < listaContratos.length) {
            var control = new Date();
            var cerrado = '';
            if (document.forms[0].modoConsulta.value == 'si') {
                cerrado = '1';
            } else {
                cerrado = '0';
            }
            if (tabContratos.selectedIndex != -1) {
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE61&operacion=cargarModificarContrato&tipo=0&numero=<%=numExpediente%>&idCon=' + listaContratos[tabContratos.selectedIndex][0] + '&cerrado=' + cerrado + '&control=' + control.getTime(), 900, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTabla(result);
                        }
                    }
                });
            } else {
                jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        } else if (tabContratos.selectedIndex == -1) {
            jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }

    }
</script>

<body>
    <div id="barraProgresoContratos" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px" >
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidePage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span><%=mensajeProgreso%></span>
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
   <div class="tab-page" id="tabPage291" style="width: 100%;">
        <h2 class="tab" id="pestana291"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.listadoContratos.title")%></h2>
        <script type="text/javascript">tp1_p291 = tp1.addTabPage(document.getElementById("tabPage291"));</script>
       <div>
            <div>
                <fieldset style="width: 97.5%;">
                    <legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario, "label.listadoContratos")%></legend>
                    <div id="contratosReple" align="center"></div>
                    <div style="width: 100%; text-align: center;">
                        <input type="button" id="btnNuevoContrato" name="btnNuevoContrato" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaContrato();">
                        <input type="button" id="btnModificarContrato" name="btnModificarContrato" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContrato();">
                        <input type="button" id="btnEliminarContrato" name="btnEliminarContrato" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContrato();">
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabContratos;
    var listaContratos = new Array();
    var listaContratosTabla = new Array();

    tabContratos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('contratosReple'), 905);
    tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col1")%>");
    tabContratos.addColumna('195', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col2")%>");
    tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col3")%>");
    tabContratos.addColumna('195', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col4")%>");
    tabContratos.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col5")%>");
    tabContratos.addColumna('178', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"contratos.tabla.col6")%>");

    tabContratos.displayCabecera = true;
    tabContratos.height = 390;
    <%  		
        FilaContratoRenovacionPlantillaVO vo = null;
        List<FilaContratoRenovacionPlantillaVO> contList = (List<FilaContratoRenovacionPlantillaVO>)request.getAttribute("contratosRenovacionPlantilla");													
        if (contList!= null && contList.size() >0){
            for (int indiceNot=0;indiceNot<contList.size();indiceNot++)
            {
                vo = contList.get(indiceNot);
                
    %>
    listaContratos[<%=indiceNot%>] = ["<%=vo.getNumContrato()%>", "<%=vo.getDni2()%>", "<%=vo.getNomApe2()%>", "<%=vo.getDni3()%>", "<%=vo.getNomApe3()%>", "<%=vo.getDni1()%>", "<%=vo.getNomApe1()%>"];
    listaContratosTabla[<%=indiceNot%>] = ["<%=vo.getDni2().toUpperCase()%>", "<%=vo.getNomApe2().toUpperCase()%>", "<%=vo.getDni3().toUpperCase()%>", "<%=vo.getNomApe3().toUpperCase()%>", "<%=vo.getDni1().toUpperCase()%>", "<%=vo.getNomApe1().toUpperCase()%>"];
    <%
            }// for
        }// if
    %>
    tabContratos.lineas = listaContratosTabla;
    tabContratos.displayTabla();
//    if(navigator.appName.indexOf("Internet Explorer")!=-1){
//        try{
//            var div = document.getElementById('contratos');
//            div.children[0].children[0].children[0].children[0].children[0].children[0].children[5].style.width = '100%';
//            div.children[0].children[1].children[0].children[0].children[0].children[5].style.width = '100%';
//        }catch(err){
//            
//        }
//    }

    document.getElementById('contratosReple').children[0].children[1].children[0].children[0].ondblclick = function (event) {
        pulsarModificarContrato(event);
    };
</script>

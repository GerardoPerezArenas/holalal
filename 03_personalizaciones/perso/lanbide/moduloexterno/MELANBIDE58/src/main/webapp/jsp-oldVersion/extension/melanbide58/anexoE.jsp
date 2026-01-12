<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.ContratacionVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null)
    {
        try
        {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }
    //nuevas
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
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>

<script type="text/javascript">

    function crearTabla() {
        tablaContrataciones = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaContrataciones'));
        tablaContrataciones.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col1")%>");
        tablaContrataciones.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col2")%>");
        tablaContrataciones.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col3")%>");
        tablaContrataciones.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col4")%>");
        tablaContrataciones.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col5")%>");
        tablaContrataciones.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col6")%>");
        tablaContrataciones.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"contrataciones.tablaContrataciones.col7")%>");

        tablaContrataciones.displayCabecera = true;
        tablaContrataciones.height = '300';
    }


    function pulsarLimpiarContrataciones() {
        document.getElementById("numLineaEBusqueda").value = "";
        document.getElementById("nifEBusqueda").value = "";
        pulsarBuscarContrataciones();
    }

    function recargarTablaContrataciones(result) {
        var fila;
        listaContrataciones = new Array();
        listaContratacionesTabla = new Array();

        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            listaContrataciones[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
            listaContratacionesTabla[i - 1] = [fila[0], fila[1], fila[2], fila[4], fila[5], fila[3], fila[6]];
        }
        crearTabla();
        tablaContrataciones.lineas = listaContratacionesTabla;
        tablaContrataciones.displayTabla();
    }

    function pulsarNuevoAltaContrataciones() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoAltaE&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaContrataciones(result);
                }
            }
        });
    }

    function pulsarEliminarAltaContrataciones() {
        if (tablaContrataciones.selectedIndex != -1) {
            var resultado = jsp_alerta_Mediana('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0] + '&control=' + control.getTime();
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
                                } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DNI") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECALTA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECBAJA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CLAVECONTRATO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }// for elementos de la fila
                    }
                    if (codigoOperacion == "0") {
                        recargarTablaContrataciones(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarBuscarContrataciones() {
        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLineaEBusqueda').value)) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=buscarContratacion&tipo=0&numExp=<%=numExpediente%>&numLineaE=" + document.getElementById('numLineaEBusqueda').value + "&nifE=" + document.getElementById('nifEBusqueda').value + '&control=' + control.getTime();

            try {
                ajax.open("POST", url, false);
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
                            } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[1] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "DNI") {
                                if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[2] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[3] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "FECALTA") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[4] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "FECBAJA") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[5] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "CLAVECONTRATO") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                } else {
                                    fila[6] = '-';
                                }
                            }
                        }
                        listaNueva[j] = fila;
                        fila = new Array();
                    }// for elementos de la fila
                }
                if (codigoOperacion == "0") {
                    recargarTablaContrataciones(listaNueva);
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

            }//try-catch
        } else {
            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
            jsp_alerta("A", mensajeValidacion);
        }
    }
    
    function pulsarModificarAltaContrataciones(){
        if (tablaContrataciones.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarAltaE&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0] + '&control=' + control.getTime(), 700, 920, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaContrataciones(result);
                    }
                }
            });

        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function validarNumericoVacio(numero) {
        try {
            if (numero == null || numero == '') {
                return true;
            } else {
                if (Trim(numero) != '') {
                    return /^([0-9])*$/.test(numero);
                }
            }
        } catch (err) {
            return false;
        }
    }
    
    function pulsarExportarAnexoAltaContrataciones(){
        var control = new Date();
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelContratacion&tipo=0&numExp=<%=numExpediente%>&control=' + control.getTime();
        window.open(url + parametros, "_blank");
    }

</script>
<div class="tab-page" id="tabPage360" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana360"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoE")%></h2>
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage360"));</script>
    <br/>
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tituloPestanaAnexoE")%></h2>
    <div>
        <div>
            <div class="lineaFormulario">
                <div class="lineaFormulario">
                    <div style="width: 120px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="numLineaEBusqueda" name="numLineaE" type="text" class="inputTexto" size="15" maxlength="5"/>
                        </div>
                    </div><div style="width: 70px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nif")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="nifEBusqueda" name="nifE" type="text" class="inputTexto" size="15" maxlength="9" 
                                   value=""/>
                        </div>
                    </div>
                </div>

            </div>
            <div class="botonera" style="text-align: right;">
                <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarContrataciones();">
                <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarContrataciones();">
            </div>
        </div>
        <br>
        <div id="divGeneral">
            <div id="listaContrataciones" align ="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevoAltaE" name="btnNuevoAltaE" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoAltaContrataciones();">
            <input type="button" id="btnModificarAltaE" name="btnModificarAltaE" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAltaContrataciones();">
            <input type="button" id="btnEliminarAltaE" name="btnEliminarAltaE"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAltaContrataciones();">
            <input type="button" id="btnExportarE" name="btnExportarE"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoAltaContrataciones();">
        </div>
    </div>
</div>
<script type="text/javascript">

    var tablaContrataciones;
    var listaContrataciones = new Array();
    var listaContratacionesTabla = new Array();

    crearTabla();

    <%
        ContratacionVO objectVO = null;
        List<ContratacionVO> lst = null;
        if(request.getAttribute("lstContrataciones")!=null){
            lst = (List<ContratacionVO>)request.getAttribute("lstContrataciones");
        }
        if(lst!= null && lst.size() >0){
            for(int indice=0;indice<lst.size();indice++){
                objectVO = lst.get(indice);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String numLinea="";
                if(objectVO.getNumLinea()!=null && !"".equals(objectVO.getNumLinea())){
                    numLinea=Integer.toString(objectVO.getNumLinea());

                }else{
                    numLinea="-";
                }
                
                String nif="";
                if(objectVO.getNif()!=null){
                    nif=objectVO.getNif();
                }else{
                    nif="-";
                }
                
                String fechaAlta="";
                if(objectVO.getFechaAlta()!=null){
                    fechaAlta=dateFormat.format(objectVO.getFechaAlta());
                }else{
                    fechaAlta="-";
                }
                
                String fechaBaja="";
                if(objectVO.getFechaBaja()!=null){
                    fechaBaja=dateFormat.format(objectVO.getFechaBaja());
                }else{
                    fechaBaja="-";
                }
                
                String porcJornada="";
                if (objectVO.getPorcJornada()!=null) {
                    porcJornada=String.valueOf((objectVO.getPorcJornada().toString()).replace(".",","));
                }else{
                    porcJornada ="-";
                }  
                
                String claveContrato="";
                if(objectVO.getClaveContrato()!=null && !"".equals(objectVO.getClaveContrato())){
                    claveContrato=Integer.toString(objectVO.getClaveContrato());

                }else{
                    claveContrato="-";
                }
                
    %>
    listaContrataciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', '<%=fechaAlta%>', '<%=fechaBaja%>', '<%=porcJornada%>', '<%=claveContrato%>'];
    listaContratacionesTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', '<%=fechaAlta%>', '<%=fechaBaja%>', '<%=porcJornada%>', '<%=claveContrato%>'];
    <%
                
            }//for
        }//if

    %>
    tablaContrataciones.lineas = listaContratacionesTabla;
    tablaContrataciones.displayTabla();

</script>
<div id="popupcalendar" class="text"></div>

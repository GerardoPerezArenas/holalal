<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>


<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.i18n.MeLanbide75I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConstantesMeLanbide75"%>
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
    MeLanbide75I18n meLanbide75I18n = MeLanbide75I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<script type="text/javascript">

    function pulsarNuevoAcceso() {
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE75&operacion=cargarNuevoAcceso&tipo=0&numExp=<%=numExpediente%>&nuevo=1', 680, 800, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaAccesos(result);
                }
            }
        });

    }

    function pulsarModificarAcceso() {
        if (tabaAccesos.selectedIndex != -1) {
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE75&operacion=cargarModificarAcceso&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaAccesos[tabaAccesos.selectedIndex][0], 680, 800, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaAccesos(result);
                    }
                }
            });

        } else {
            jsp_alerta('A', '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarAnexoC() {
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE75&operacion=generarExcelAnexoC&tipo=0&numExp=<%=numExpediente%>';

        window.open(url + parametros, "_blank");
    }

    function pulsarEliminarAcceso() {
        if (tabaAccesos.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                parametros = 'tarea=preparar&modulo=MELANBIDE75&operacion=eliminarAcceso&tipo=0&numExp=<%=numExpediente%>&id=' + listaAccesos[tabaAccesos.selectedIndex][0];
                try {
                    ajax.open("POST", url, false);
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
                                }
                                /*else if (hijosFila[cont].nodeName == "CNAE") {
                                 if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                 fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                 }
                                 else {
                                 fila[1] = '-';
                                 }
                                 }*/
                                else if (hijosFila[cont].nodeName == "DENOMPUESTO") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NIFCIF") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "GRADO") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[4].toString();
                                        tex = tex.replace(".", ",");
                                        fila[4] = tex;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOCONTRATO") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRATODESDE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CONTRATOHASTA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TOTALDIAS") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaAccesos(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTablaAccesos(result) {
        var fila;
        listaAccesos = new Array();
        listaAccesosTabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            //listaAccesos[i - 1] = fila;//NO FUNCIONA IE9
            listaAccesos[i - 1] = [fila[0], fila[1], fila[2], fila[3],
                fila[4], fila[5], fila[6], fila[7], fila[8]];
            listaAccesosTabla[i - 1] = [fila[0], fila[1], fila[2], fila[3],
                fila[4], fila[5], fila[6], fila[7], fila[8]];
        }

        tabaAccesos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaAccesos'), 1350);

        tabaAccesos.addColumna('0', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col1")%>");
        tabaAccesos.addColumna('270', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col3")%>");
        tabaAccesos.addColumna('240', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col4")%>");
        tabaAccesos.addColumna('70', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col5")%>");
        tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col6")%>");
        tabaAccesos.addColumna('270', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col7")%>");
        tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col8")%>");
        tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col9")%>");
        tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col10")%>");

        tabaAccesos.displayCabecera = true;
        tabaAccesos.height = 300;
        tabaAccesos.lineas = listaAccesosTabla;
        tabaAccesos.displayTabla();

        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('listaAccesos');
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            } catch (err) {

            }
        }
    }

</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide75/melanbide75.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->
<div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana352"><%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
    <br/>
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div>    
        <br>
        <div id="divGeneral">     
            <div id="listaAccesos"  align="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevoAcceso" name="btnNuevoAcceso" class="botonGeneral"  value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoAcceso();">
            <input type="button" id="btnModificarAcceso" name="btnModificarAcceso" class="botonGeneral" value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAcceso();">
            <input type="button" id="btnEliminarAcceso" name="btnEliminarAcceso"   class="botonGeneral" value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAcceso();">
            <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoC();">
        </div>
    </div>  
</div>
<script  type="text/javascript">
    //Tabla Accesos
    var tabaAccesos;
    var listaAccesos = new Array();
    var listaAccesosTabla = new Array();

    //right - left - center
    tabaAccesos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaAccesos'), 1350);

    tabaAccesos.addColumna('0', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col1")%>");
    tabaAccesos.addColumna('270', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col3")%>");
    tabaAccesos.addColumna('240', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col4")%>");
    tabaAccesos.addColumna('70', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col5")%>");
    tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col6")%>");
    tabaAccesos.addColumna('270', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col7")%>");
    tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col8")%>");
    tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col9")%>");
    tabaAccesos.addColumna('75', 'center', "<%=meLanbide75I18n.getMensaje(idiomaUsuario,"puesto.tablaPuestos.col10")%>");

    tabaAccesos.displayCabecera = true;
    tabaAccesos.height = 300;


    <%  		
       ControlAccesoVO objectVO = null;
       List<ControlAccesoVO> List = null;
       if(request.getAttribute("listaAccesos")!=null){
           List = (List<ControlAccesoVO>)request.getAttribute("listaAccesos");
       }													
       if (List!= null && List.size() >0){
           for (int indice=0;indice<List.size();indice++)
           {
               objectVO = List.get(indice);
               DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    
               /*String cnae="";
               if(objectVO.getCnae()!=null){
                   cnae=objectVO.getCnae();
               }else{
                   cnae="-";
               }*/
                    
               String puesto="";
               if(objectVO.getPuesto()!=null){
                   puesto=objectVO.getPuesto();
               }else{
                   puesto="-";
               }
                    
               String nombre="";
               if(objectVO.getNombre()!=null){
                   nombre=objectVO.getNombre();
               }else{
                   nombre="-";
               }
                    
               String nif="";
               if(objectVO.getNif()!=null){
                   nif=objectVO.getNif();
               }else{
                   nif="-";
               }
                    
               String porDisc="";
               if(objectVO.getPorDisc()!=null){
                   porDisc=String.valueOf((objectVO.getPorDisc().toString()).replace(".",","));
               }else{
                   porDisc="-";
               }
                    
               String tipoCon="";
               if(objectVO.getDesTipoCon()!=null){
                   String descripcion = objectVO.getDesTipoCon();
                        
                   String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide75.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide75.FICHERO_PROPIEDADES);
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==ConstantesMeLanbide75.CODIGO_IDIOMA_EUSKERA){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           // Cogemos la primera posicion que deberia ser castellano
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                        
                   tipoCon = descripcion;
               }else{
                   tipoCon="-";
               }
                    
               String conDesde="";
               if(objectVO.getConDesde()!=null){
                   conDesde=dateFormat.format(objectVO.getConDesde());
                
               }else{
                   conDesde="-";
               }

               String conHasta="";
               if(objectVO.getConHasta()!=null){
                   conHasta=dateFormat.format(objectVO.getConHasta());
                
               }else{
                   conHasta="-";
               }

               String totalDias="";
               if(objectVO.getTotalDias()!=null && !"".equals(objectVO.getTotalDias())){
                   totalDias=Integer.toString(objectVO.getTotalDias());
               }else{
                   totalDias="-";
               }

    %>
    listaAccesos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=puesto%>', "<%=nombre%>",
        '<%=nif%>', '<%=porDisc%>', '<%=tipoCon%>', '<%=conDesde%>', '<%=conHasta%>', '<%=totalDias%>'];
    listaAccesosTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=puesto%>', "<%=nombre%>",
        '<%=nif%>', '<%=porDisc%>', '<%=tipoCon%>', '<%=conDesde%>', '<%=conHasta%>', '<%=totalDias%>'];
    <%
           }// for
       }// if
    %>

    tabaAccesos.lineas = listaAccesosTabla;
    tabaAccesos.displayTabla();

    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
        try {
            var div = document.getElementById('listaAccesos');
            if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
            } else {
                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                div.children[0].children[1].children[0].children[0].style.width = '100%';
            }
        } catch (err) {

        }
    }
</script>
<div id="popupcalendar" class="text"></div>                


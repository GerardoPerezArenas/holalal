<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
           int idiomaUsuario = 1;
int apl = 5;
String css = "";
           if(request.getParameter("idioma") != null)
           {
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
           catch(Exception ex)
           {

           }
           //Clase para internacionalizar los mensajes de la aplicación.
           MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();

           Config m_Config = ConfigServiceHelper.getConfig("common");
           String statusBar = m_Config.getString("JSP.StatusBar");
           String nombreModulo     = request.getParameter("nombreModulo");
           String codOrganizacion  = request.getParameter("codOrganizacionModulo");
           String numExpediente    = request.getParameter("numero");

           EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
           if(request.getAttribute("datoEspecialidad") != null)
           {
               datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
           }

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
    </head>
    <body>		
        <div class="tab-page" id="tabPage413" style="height:420px; width: 90%;">
            <!--<script type="text/javascript">tp1_p411 = tp1.addTabPage( document.getElementById( "tabPage411" ) );</script>-->
            <div style="clear: both;">
                <label class="legendAzul" style="text-align: center; position: relative; left: 18%; "><%=meLanbide41I18n.getMensaje(idiomaUsuario, "dotacion.legend.titulo")%></label>
                <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 420px;">     <!--onscroll="deshabilitarRadios();"-->
                    <div id="listaDotacion"  align="center"></div>
                    <div class="botonera">
                        <input type="button" id="btnNuevaDotacion" name="btnNuevaDotacion" class="botonGeneral"  value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaDotacion();">
                        <input type="button" id="btnEliminarDotacion" name="btnEliminarDotacion"   class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarDotacion();">
                        <input type="button" id="btnModificarDotacion" name="btnModificarDotacion" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarDotacion();">
                    </div>
                </div>
            </div>
        </div>
        <!--Script Ejecucion Elementos Pagina-->
        <script type="text/javascript">
            //Tabla Especialidades
            var tabDotacion;
            var listaDotacion = new Array();
            var listaDotacionTabla = new Array();

            tabDotacion = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaDotacion'), 820);
            tabDotacion.addColumna('100', 'right', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col1")%>");
            tabDotacion.addColumna('550', 'left', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col2")%>");
            tabDotacion.addColumna('150', 'right', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col3")%>");

            tabDotacion.displayCabecera = true;
            tabDotacion.height = 100;

            <%  		
                DotacionVO objectVO = null;
                List<DotacionVO> List = (List<DotacionVO>)request.getAttribute("listaDotacion");													
                if (List!= null && List.size() >0){
                    for (int indice=0;indice<List.size();indice++)
                    {
                        objectVO = List.get(indice);
                        //SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        //String fechaAdqui = formatoFecha.format(objectVO.getFechaAdq());
                        String fechaAdqui = objectVO.getFechaAdq();

            %>
            listaDotacionTabla[<%=indice%>] = ['<%=objectVO.getCantidad()%>', '<%=objectVO.getDenominacionET().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>', '<%=fechaAdqui%>'];
            listaDotacion[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=objectVO.getCantidad()%>', '<%=objectVO.getDenominacionET().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>', '<%=fechaAdqui%>'];

            <%
                    }// for
                }// if
            %>

            tabDotacion.lineas = listaDotacionTabla;
            tabDotacion.displayTabla();


            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                try {
                    var div = document.getElementById('listaDotacion');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                } catch (err) {

                }
            }
        </script>
        <!-- Script Con Funciones-->
        <script type="text/javascript">

            function pulsarAltaDotacion() {
                var control = new Date();
                var result = null;
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarNuevaDotacion&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime() + '&idEpsol=<%=datoEspecialidad.getId()%>', 350, 900, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaDotacion(result);
                        }
                    }
                });

            }

            function pulsarModificarDotacion() {
                if (tabDotacion.selectedIndex != -1) {
                    var control = new Date();
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarModifDotacion&tipo=0&numero=<%=numExpediente%>&id=' + listaDotacion[tabDotacion.selectedIndex][0] + '&control=' + control.getTime() + '&idEpsol=<%=datoEspecialidad.getId()%>', 350, 900, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaDotacion(result);
                            }
                        }
                    });

                } else {
                    jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarDotacion() {
                if (tabDotacion.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE41&operacion=eliminarDotacion&tipo=0&numero=<%=numExpediente%>&id=' + listaDotacion[tabDotacion.selectedIndex][0] + '&control=' + control.getTime()
                                + '&idEpsol=<%=datoEspecialidad.getId()%>';
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
                                        } else if (hijosFila[cont].nodeName == "DOT_NUM") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DOT_CANT") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DOT_DET") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DOT_FAD") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        }
                                        if (hijosFila[cont].nodeName == "ID_ESPSOL") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaDotacion(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function recargarTablaDotacion(result) {
                var fila;
                listaDotacion = new Array();
                listaDotacionTabla = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    listaDotacion[i - 1] = [fila[0], fila[1], fila[2], fila[3]];
                    var date = fila[4];
                    var newdate = date.split("-").reverse().join("/");
                    listaDotacionTabla[i - 1] = [fila[2], fila[3].replace("\n\r", "<br>").replace("\r", "<br>").replace("\n", "<br>"), newdate];
                }
                tabDotacion = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaDotacion'), 820);
                tabDotacion.addColumna('100', 'right', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col1")%>");
                tabDotacion.addColumna('550', 'left', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col2")%>");
                tabDotacion.addColumna('150', 'right', "<%= meLanbide41I18n.getMensaje(idiomaUsuario,"dotacion.tablaDotacion.col3")%>");

                tabDotacion.displayCabecera = true;
                tabDotacion.height = 100;
                tabDotacion.lineas = listaDotacionTabla;
                tabDotacion.displayTabla();

                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    try {
                        var div = document.getElementById('listaDotacion');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    } catch (err) {

                    }
                }
            }

        </script>    
    </body>
</html>
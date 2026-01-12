<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.Iterator" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayOtroProgramaVO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

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
  
    List<OriTrayOtroProgramaVO> listaTrayectoriaOtrosProg = (List<OriTrayOtroProgramaVO>)request.getAttribute("listaTrayectoriaOtrosProg");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/TablaNuevaORI.js"></script>

<script type="text/javascript">
    function anadirTrayectoriaOtroPrograma() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoriaOtrosProgramas&tipo=0&modoDatos=0&numero=<%=numExpediente%>&control=' + control.getTime(), 500, 980, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == 0) {
                    recargarTablaTrayectoriaOtrosProgramas(result);
                }
            }
        });

    }
    function modificarTrayectoriaOtroPrograma() {

        var control = new Date();
        var result = null;
        var idTray = "";
        var codEntidad = "";
        if (tabTrayectoriaOtrosProgramas.selectedIndex != -1) {
            idTray = listaTrayectoriaOtrosProgramas[tabTrayectoriaOtrosProgramas.selectedIndex][0];
            codEntidad = listaTrayectoriaOtrosProgramas[tabTrayectoriaOtrosProgramas.selectedIndex][3];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoriaOtrosProgramas&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTray=' + idTray + '&codEntidad=' + codEntidad + '&control=' + control.getTime(), 350, 980, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == 0) {
                        recargarTablaTrayectoriaOtrosProgramas(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    function eliminarTrayectoriaOtroPrograma() {

        var control = new Date();
        var result = null;
        var codColectivo = "";
        var idTray = "";
        var codEntidad = "";
        if (tabTrayectoriaOtrosProgramas.selectedIndex != -1) {

            idTray = listaTrayectoriaOtrosProgramas[tabTrayectoriaOtrosProgramas.selectedIndex][0];
            codEntidad = listaTrayectoriaOtrosProgramas[tabTrayectoriaOtrosProgramas.selectedIndex][3];
            var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarTrayectoriaOtrosProgramas&tipo=0&numero=<%=numExpediente%>'
                        + '&idTray=' + idTray
                        + '&codEntidad=' + codEntidad
                        + '&idiomaUsuario=<%=idiomaUsuario%>'
                        + '&control=' + control.getTime();
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
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaDatosRespuesta = new Array();
                    var fila = new Array();
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
                                for (var cont = 0; cont < hijosFila.length; cont++) {  // recorremos los nodos TRAYECTORIA
                                    if (hijosFila[cont].nodeName == "ORI_OTRPRO_ID") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[0] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_EXP_EJE") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_NUMEXP") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_COD_ENTIDAD") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROGRAMA") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROG_EJE") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_DURACION") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_ENT_NOM") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    }else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROG_EJE_VALID") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    }else if (hijosFila[cont].nodeName == "ORI_OTRPRO_DURACION_VALID") {
                                        nodoCampo = hijosFila[cont];
                                        if (nodoCampo.childNodes.length > 0) {
                                            fila[10] = nodoCampo.childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    }

                                }// fin nodo trayectoria
                                listaDatosRespuesta[cont1 + 1] = fila;
                                fila = new Array();
                            }//for(var cont1)
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        if (listaDatosRespuesta != null && listaDatosRespuesta != undefined) {
                            if (listaDatosRespuesta[0] == '0') {
                                recargarTablaTrayectoriaOtrosProgramas(listaDatosRespuesta);
                            }
                        }
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
                }
            }

        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }

    }
    function recargarTablaTrayectoriaOtrosProgramas(result) {
        //TODO
        var codOperacion = result != null ? result[0] : null;
        var fila;
        if (codOperacion != null) {
            listaTrayectoriaOtrosProgramas = new Array();
            listaTrayectoriaOtrosProgramasTabla = new Array();
            // Creamos tabla Trampa porque al seleccionar fila hace tab[0] y los id creados en el script NuevaTabla entre la tabla trámites y esta es el mismo tabla0
            tabTrayectoriaOtrosProgramas = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaTrayectoriaOtrosProgramas'), 1105, 25);
            //tabTrayectoriaOtrosProgramas.addColumna('250', 'left', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.entidad")%>");
            tabTrayectoriaOtrosProgramas.addColumna('450', 'left', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.programa")%>");
            tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio")%>");
            tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.duracion")%>");
            tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio.validado")%>");
            tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.duracion.validada")%>");
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaTrayectoriaOtrosProgramas[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8],fila[9],fila[10]];
                listaTrayectoriaOtrosProgramasTabla[i - 1] = [fila[4], fila[5], fila[6],(fila[9]!=null && fila[9]!="null"?fila[9] :"-"),(fila[10]!=null && fila[10]!="null"?fila[10] :"-")];
                //fila[10], 
            }
            tabTrayectoriaOtrosProgramas.lineas = listaTrayectoriaOtrosProgramasTabla;
            tabTrayectoriaOtrosProgramas.displayCabecera = true;
            tabTrayectoriaOtrosProgramas.height = 250;
            tabTrayectoriaOtrosProgramas.displayTabla();
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
    }
</script>
<body class="contenidoPantalla" id="bodyOriTrayectoriaOtrosProgramas">
    <fieldset id="fieldsetTrayectoriaOP" style="height: 350px; margin-top: 20px; ">
       <!-- <label class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria.otrosProgramas.textoLargo")%></label>-->
        <div id="divTablaTrayectoriaOtroProg" align="center">
            <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                <div id="listaTrayectoriaOtrosProgramas" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirTrayectoriaOP" name="botonAnadirTrayectoriaOP" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirTrayectoriaOtroPrograma();"/>
                    <input type="button" id="botonModificarTrayectoriaOP" name="botonModificarTrayectoriaOP" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarTrayectoriaOtroPrograma();"/>
                    <input type="button" id="botonEliminarTrayectoriaOP" name="botonEliminarTrayectoriaOP" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarTrayectoriaOtroPrograma();"/>              
                </div>
            </div>
        </div>
    </fieldset>
</body>

<script type="text/javascript">
    var tabTrayectoriaOtrosProgramas;
    var listaTrayectoriaOtrosProgramas = new Array();
    var listaTrayectoriaOtrosProgramasTabla = new Array();

    tabTrayectoriaOtrosProgramas = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaTrayectoriaOtrosProgramas'), 1105, 25);
    //tabTrayectoriaOtrosProgramas.addColumna('250', 'left', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.entidad")%>");
    tabTrayectoriaOtrosProgramas.addColumna('450', 'left', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.programa")%>");
    tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio")%>");
    tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.duracion")%>");
    tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio.validado")%>");
    tabTrayectoriaOtrosProgramas.addColumna('100', 'center', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.duracion.validada")%>");

    <%
        OriTrayOtroProgramaVO fila = null;
        if (listaTrayectoriaOtrosProg!=null){
            for(int i=0;i<listaTrayectoriaOtrosProg.size();i++){
                fila = listaTrayectoriaOtrosProg.get(i);
    %>
    listaTrayectoriaOtrosProgramas[<%=i%>] = ['<%=fila.getCodIdOtroPrograma()%>', '<%=fila.getEjercicio()%>', '<%=fila.getNumExpediente()%>', '<%=fila.getCodEntidad()%>', '<%=fila.getPrograma()%>', '<%=fila.getAnioPrograma()%>', '<%=fila.getDuracion()%>', '<%=fila.getCifEntidad()%>', '<%=fila.getNombreEntidad()%>','<%=fila.getAnioProgramaVal()%>','<%=fila.getDuracionVal()%>'];
    listaTrayectoriaOtrosProgramasTabla [<%=i%>] = ['<%=fila.getPrograma()%>', '<%=fila.getAnioPrograma()%>', '<%=fila.getDuracion()%>','<%=(fila.getAnioProgramaVal() != null ? fila.getAnioProgramaVal() : "-")%>', '<%=(fila.getDuracionVal() !=null ? fila.getDuracionVal() : "-")%>'];
    //'< %=fila.getNombreEntidad()%>'

    <%
            }
        }
    %>
    tabTrayectoriaOtrosProgramas.lineas = listaTrayectoriaOtrosProgramasTabla;
    tabTrayectoriaOtrosProgramas.displayCabecera = true;
    tabTrayectoriaOtrosProgramas.height = 250;
    tabTrayectoriaOtrosProgramas.displayTabla();
</script>

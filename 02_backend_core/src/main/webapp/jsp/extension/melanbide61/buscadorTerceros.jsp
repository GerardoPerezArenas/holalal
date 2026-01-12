<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO"%>

<html>
    <head> 
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
            
            String codOrganizacion  = request.getParameter("tipo");
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';

            function barraProgresoBusqueda(valor) {
                if (valor == 'on') {
                    document.getElementById('barraProgresoBusqueda').style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById('barraProgresoBusqueda').style.visibility = 'hidden';
                }
            }

            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                ];

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
                        }
                    }
                }
            }

            function validarCriteriosBusqueda() {
                var doc = document.getElementById('txtDniBusq').value;
                var nom = document.getElementById('txtNomBusq').value;
                var ape1 = document.getElementById('txtApe1Busq').value;
                var ape2 = document.getElementById('txtApe2Busq').value;

                if ((doc == '') &&
                        ((nom == '' && ape1 == '' && ape2 == '')
                                ||
                                (nom == '' && ape1 == '')
                                ||
                                (nom == '' && ape2 == '')
                                ||
                                (ape1 = '' && ape2 == ''))
                        ) {
                    return false;
                } else {
                    if ((doc != '') && (doc.length < 6))
                        return false;
                    else
                        return true;
                }
            }

            function buscar() {
                if (validarCriteriosBusqueda()) {
                    barraProgresoBusqueda('on');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "tarea=preparar&modulo=MELANBIDE61&operacion=busquedaTerceros&tipo=0"
                            + "&dni=" + document.getElementById('txtDniBusq').value + "&nom=" + document.getElementById('txtNomBusq').value + "&ape1=" + document.getElementById('txtApe1Busq').value + "&ape2=" + document.getElementById('txtApe2Busq').value;
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
                        var listaTerceros = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else if (hijosFila[cont].nodeName == "DNI") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else if (hijosFila[cont].nodeName == "NOMBREAPELLIDOS") {
                                        var nom = "";
                                        try {
                                            nom = hijosFila[cont].childNodes[0].childNodes[0].nodeValue;
                                            fila[2] = nom;
                                        } catch (err) {

                                        }
                                        var ape1 = "";
                                        try {
                                            ape1 = hijosFila[cont].childNodes[1].childNodes[0].nodeValue;
                                            fila[3] = ape1;
                                        } catch (err) {

                                        }
                                        var ape2 = "";
                                        try {
                                            ape2 = hijosFila[cont].childNodes[2].childNodes[0].nodeValue;
                                            fila[4] = ape2;
                                        } catch (err) {

                                        }
                                        var nomApe = '';
                                        nomApe = nom != null ? nom : "";
                                        nomApe += ape1 != null && ape1 != "" ? (nomApe != null && nomApe != "" ? " " : "") : "";
                                        nomApe += ape1 != null ? ape1 : "";
                                        nomApe += ape2 != null && ape2 != "" ? (nomApe != null && nomApe != "" ? " " : "") : "";
                                        nomApe += ape2 != null ? ape2 : "";
                                        fila[5] = nomApe;
                                    } else if (hijosFila[cont].nodeName == "SEXO") {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else if (hijosFila[cont].nodeName == "FNACI") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                }
                                listaTerceros[j - 1] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            listaResultados = new Array();
                            listaResultadosTabla = new Array();
                            for (var cont = 0; cont < listaTerceros.length; cont++) {
                                listaResultados[cont] = [listaTerceros[cont][0], listaTerceros[cont][1], listaTerceros[cont][2], listaTerceros[cont][3], listaTerceros[cont][4], listaTerceros[cont][6], listaTerceros[cont][7]];
                                listaResultadosTabla[cont] = [listaTerceros[cont][1], listaTerceros[cont][5], listaTerceros[cont][6], listaTerceros[cont][7]];
                            }
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch


                    tabResultados = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('resultados'), 530);
                    tabResultados.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col1")%>");
                    tabResultados.addColumna('417', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col2")%>");
                    tabResultados.addColumna('417', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col3")%>");
                    tabResultados.addColumna('417', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col4")%>");
                    tabResultados.displayCabecera = true;
                    tabResultados.height = 220;
                    tabResultados.lineas = listaResultadosTabla;
                    tabResultados.displayTabla();

                    barraProgresoBusqueda('off');
                } else {
                    jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.critBusquedaInsuficientes")%>');
                }
            }

            function limpiar() {
                document.getElementById('txtDniBusq').value = '';
                document.getElementById('txtNomBusq').value = '';
                document.getElementById('txtApe1Busq').value = '';
                document.getElementById('txtApe2Busq').value = '';
            }

            function aceptar() {
                if (tabResultados.selectedIndex != -1) {
                    self.parent.opener.retornoXanelaAuxiliar(listaResultados[tabResultados.selectedIndex]);
                    cerrarVentana();
                } else {
                    jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function cerrarVentana() {
                if (navigator.appName == "Microsoft Internet Explorer") {
                    window.parent.window.opener = null;
                    window.parent.window.close();
                } else if (navigator.appName == "Netscape") {
                    top.window.opener = top;
                    top.window.open('', '_parent', '');
                    top.window.close();
                } else {
                    window.close();
                }
            }

        </script>
    </head>
    <body class="contenidoPantalla" style="width: 98%;">
        <form> 
            <div id="barraProgresoBusqueda" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidePage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span><%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.realizandoBusq")%></span>
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
                <fieldset>
                    <!--legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.personaContratada")%></legend-->
                    <div class="lineaFormulario">
                        <div style="float: left; width: 10%;" class="etiqueta">
                            <span> <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dni")%></span>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" size="25" maxlength="25" class="inputTexto" id="txtDniBusq" name="txtDniBusq"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 10%;" class="etiqueta">
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" size="75" maxlength="80" class="inputTexto" id="txtNomBusq" name="txtNomBusq"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width:  10%;" class="etiqueta">
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" size="75" maxlength="100" class="inputTexto" id="txtApe1Busq" name="txtApe1Busq"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 10%;" class="etiqueta">
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" size="75" maxlength="100"  class="inputTexto" id="txtApe2Busq" name="txtApe2Busq"/>
                        </div>
                    </div> 
                    <div style="width: 100%; text-align: center; padding-top: 5px;">
                        <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="Buscar" onclick="buscar();">
                        <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="Limpiar" onclick="limpiar();">
                    </div>
                </fieldset>
                <div id="resultados" align="center"></div>
                <div style="width: 100%; text-align: center; padding-top: 5px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>   

        <script type="text/javascript">
            var tabResultados;
            var listaResultados = new Array();
            var listaResultadosTabla = new Array();

            tabResultados = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('resultados'), 530);
            tabResultados.addColumna('100', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col1")%>");
            tabResultados.addColumna('417', 'left', "<%= meLanbide61I18n.getMensaje(idiomaUsuario,"busqTerceros.tabla.col2")%>");
            tabResultados.displayCabecera = true;
            tabResultados.height = 220;
            tabResultados.lineas = listaResultadosTabla;
            tabResultados.displayTabla();
        </script>
    </body>
</html>

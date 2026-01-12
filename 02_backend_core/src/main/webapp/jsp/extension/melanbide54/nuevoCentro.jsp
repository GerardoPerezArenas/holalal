<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.i18n.MeLanbide54I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.TerritorioHVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.MunicipioVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.util.ConstantesMeLanbide54" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            CentroVO datModif = new CentroVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide54I18n meLanbide54I18n = MeLanbide54I18n.getInstance();
            
            int idiomaUsuario = 1;
            try
            {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try
                {
                    if (session != null) 
                    {
                        if (usuario != null) 
                        {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                        }
                    }
                }
                catch(Exception ex)
                {

                }
                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                numExpediente    = request.getParameter("numero");
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (CentroVO)request.getAttribute("datModif");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var mensajeValidacion = '';

            var comboListaTh;
            var comboListaMuni;
            var listaCodigosTh = new Array();
            var listaDescripcionesTh = new Array();
            var listaCodigosMuni = new Array();
            var listaDescripcionesMuni = new Array();

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
                } else {
                    return null;
                }
            }

            function guardarDatos() {
                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE54&operacion=crearCentro&tipo=0&numero=<%=numExpediente%>"
                                + "&codTh=" + document.getElementById('codListaTh').value
                                + "&desCodTh=" + document.getElementById('descListaTh').value
                                + "&codMun=" + document.getElementById('codListaMuni').value
                                + "&desCodMun=" + document.getElementById('descListaMuni').value
                                + "&calle=" + document.getElementById('calle').value
                                + "&portal=" + document.getElementById('portal').value
                                + "&piso=" + document.getElementById('piso').value
                                + "&letra=" + document.getElementById('letra').value
                                + "&cp=" + document.getElementById('cp').value
                                + "&telef=" + document.getElementById('telef').value
                                + "&email=" + document.getElementById('email').value
                                + "&existe=";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE54&operacion=modificarCentro&tipo=0&numero=<%=numExpediente%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&codTh=" + document.getElementById('codListaTh').value
                                + "&desCodTh=" + document.getElementById('descListaTh').value
                                + "&codMun=" + document.getElementById('codListaMuni').value
                                + "&desCodMun=" + document.getElementById('descListaMuni').value
                                + "&calle=" + document.getElementById('calle').value
                                + "&portal=" + document.getElementById('portal').value
                                + "&piso=" + document.getElementById('piso').value
                                + "&letra=" + document.getElementById('letra').value
                                + "&cp=" + document.getElementById('cp').value
                                + "&telef=" + document.getElementById('telef').value
                                + "&email=" + document.getElementById('email').value
                                + "&existe=";
                    }
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
                        var lista = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                lista[j] = codigoOperacion;
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
                                    } else if (hijosFila[cont].nodeName == "EXP_NUM") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CODTH") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESCODTH") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CODMUN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESCODMUN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CALLE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PORTAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PISO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "LETRA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TELEF") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "EMAIL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '';
                                        }
                                    }
                                }
                                lista[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }


            function cancelar() {
                var resultado = 1; //jsp_alerta('','<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
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

            function reemplazarPuntos(campo) {
                try {
                    var valor = campo.value;
                    if (valor != null && valor != '') {
                        valor = valor.replace(/\./g, ',');
                        campo.value = valor;
                    }
                } catch (err) {
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                var codMod = document.getElementById('codListaTh').value;
                if (codMod == null || codMod == '') {
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.codigoModOblig")%>';
                    return false;
                }

                var denominacion = document.getElementById('codListaMuni').value;
                if (denominacion == null || denominacion == '') {
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                    return false;
                }
                return correcto;
            }

            function buscaCodigoTh(codTh) {
                comboListaTh.buscaCodigo(codTh);
            }

            function buscaCodigoMuni(codTh) {
                comboListaMuni.buscaCodigo(codTh);
            }

            function cargarDatosTh() {
                var codThSeleccionado = document.getElementById("codListaTh").value;
                buscaCodigoTh(codThSeleccionado);
            }

            function cargarDatosMuni() {
                var codThSeleccionado = document.getElementById("codListaMuni").value;
                buscaCodigoMuni(codThSeleccionado);
            }

            function rellenardatEspModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTh('<%=datModif.getCodTH() != null ? datModif.getCodTH() : ""%>');
                    buscaCodigoMuni('<%=datModif.getCodMun() != null ? datModif.getCodMun() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function cargarMunicipios() {
                var codProvincia = document.getElementById('codListaTh').value;

                if (codProvincia != null && codProvincia != '') {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE54&operacion=cargarMunicipios&tipo=0&numero=<%=numExpediente%>&codProvincia=' + codProvincia + '&control=' + control.getTime();
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
                        var listaAmbitos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var contAmbitos = 0;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "ITEM_AMBITO") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[0] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "LABEL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    }
                                }
                                listaAmbitos[contAmbitos] = fila;
                                contAmbitos++;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            comboListaMuni = new Combo("ListaMuni");

                            for (var i = 0; i < listaAmbitos.length; i++) {
                                listaCodigosMuni[i] = listaAmbitos[i][0];
                                listaDescripcionesMuni[i] = listaAmbitos[i][1];
                            }
                            comboListaMuni.addItems(listaCodigosMuni, listaDescripcionesMuni);
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {
                        alert(Err);
                    }//try-catch
                }
            }
        </script>                
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide54/melanbide54.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    </head>        
    <body>
        <div class="contenidoPantalla">
            <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.centro")%>
                    </span>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div class="etiqueta"   style="width: 190px; float: left;" >
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.territorioHistorico")%>
                    </div>
                    <div style="float:left; margin-left: 2px">
                        <input type="text" name="codListaTh" id="codListaTh" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                        <input type="text" name="descListaTh"  id="descListaTh" size="70" class="inputTexto" readonly="true" value=""/>
                        <a href="" id="anchorListaTh" name="anchorListaTh">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                 name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div class="etiqueta"  style="width: 190px; float: left;" >
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.municipio")%>
                    </div>
                    <div style="float:left; margin-left: 2px">
                        <input type="text" name="codListaMuni" id="codListaMuni" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                        <input type="text" name="descListaMuni"  id="descListaMuni" size="70" class="inputTexto" readonly="true" value=""/>
                        <a href="" id="anchorListaMuni" name="anchorListaMuni">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion2"
                                 name="botonAplicacion2" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.calle")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="calle" name="calle" type="text" class="inputTexto" size="90" maxlength="500" 
                               value="<%=datModif != null && datModif.getCalle() != null ? datModif.getCalle()  : ""%>"/>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.portal")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="portal" name="portal" type="text" class="inputTexto" size="15" maxlength="4" 
                               value="<%=datModif != null && datModif.getPortal() != null ? datModif.getPortal().toString()  : ""%>" />
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.piso")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="piso" name="piso" type="text" class="inputTexto" size="15" maxlength="4" 
                                   value="<%=datModif != null && datModif.getPiso() != null ? datModif.getPiso().toString()  : ""%>" />
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.letra")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="letra" name="letra" type="text" class="inputTexto" size="15" maxlength="4" 
                                   value="<%=datModif != null && datModif.getLetra() != null ? datModif.getLetra().toString()  : ""%>" />
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.cp")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="cp" name="cp" type="text" class="inputTexto" size="15" maxlength="9" 
                                   value="<%=datModif != null && datModif.getCp() != null ? datModif.getCp().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.telef")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="telef" name="telef" type="text" class="inputTexto" size="15" maxlength="12" 
                                   value="<%=datModif != null && datModif.getTlef() != null ? datModif.getTlef().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="margin-left: 4px">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.email")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="email" name="email" type="text" class="inputTexto" size="50" maxlength="50" 
                                   value="<%=datModif != null && datModif.getEmail() != null ? datModif.getEmail().toString()  : ""%>" />
                        </div>
                    </div>
                </div>
                <br><br><br><br>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </form>
        </div>
        <script type="text/javascript">
            listaCodigosTh[0] = "";
            listaDescripcionesTh[0] = "";

            /* Lista con los territorios históricos */
            var contador = 0;

            <logic:iterate  id="th" name="listaTh" scope="request">
            listaCodigosTh[contador] = ['<bean:write name="th" property="id" />'];
            listaDescripcionesTh[contador] = ['<bean:write name="th" property="descripcion" />'];
            contador++;
            </logic:iterate>
            var comboListaTh = new Combo("ListaTh");
            comboListaTh.addItems(listaCodigosTh, listaDescripcionesTh);
            comboListaTh.change = cargarDatosTh;

            listaCodigosMuni[0] = "";
            listaDescripcionesMuni[0] = "";

            /* Lista con los municipios */
            contador = 0;
            comboListaTh.change = cargarMunicipios;

            var nuevo = "<%=nuevo%>";
            if (nuevo == 0) {
                rellenardatEspModificar();
            }
        </script>                  
    </body>
</html>
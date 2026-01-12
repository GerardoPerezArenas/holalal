<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConfigurationParameter" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>


<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaTipDocLanbideVO datModif = new FilaTipDocLanbideVO();
            
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
            
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            
            String id = request.getParameter("id");
            String codTipDoc = request.getParameter("codTipDoc"); // Obtiene el valor de codTipDoc

            /*String codTipDoc = request.getParameter("codTipDoc");
            String tipDocCas = request.getParameter("tipDocCas");
            String tipDocEus = request.getParameter("tipDocEus");
            String tipDocCasL = "";
            if (request.getParameter("tipDocCasL") != null)
                tipDocCasL = request.getParameter("tipDocCasL");
            String tipDocEusL = "";
            if (request.getParameter("tipDocEusL") != null)
                tipDocEusL = request.getParameter("tipDocEusL");*/
            
            if(request.getAttribute("datModif") != null)
            {
                datModif = (FilaTipDocLanbideVO)request.getAttribute("datModif");
            }
            
            
            String codDokusi  = request.getParameter("tipDocDokusi");

            String tituloPagina = "";
            if(id != null) {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.titulo.modifTipDoc");
            }
            else
            {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocLanbide.titulo.nuevoTipDoc");
            }
        %>
        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/tabUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/InputMask.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
    </head>
    <body id="cuerpoNuevoTipDoc" style="text-align: left; height: auto;">
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: auto;">
            <form  id="formNuevoTipDoc">
                <fieldset id="fieldsetDatosTipDoc" name="fieldsetDatosTipDoc" style="width: auto;">
                    <legend class="legendAzul"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.TipDocLanbide.datosTipDoc")%></legend>

                    <div class="lineaFormulario" id="divId">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <!-- < % =meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.Id")%>-->
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="hidden" id="id" name="id" type="text" class="inputTexto" size="5" maxlength="5" 
                                       value="" />
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <!--< %=meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocLanbide.datosTipDoc.TipDoc")%>-->
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <!-- Campo deshabilitado para que el usuario no lo edite -->
                                <input id="codTipDoc" name="codTipDoc" type="hidden" class="inputTexto" size="5" maxlength="5" 
                                       value="" />
                            </div>
                        </div>
                    </div> 
                    <div class="lineaFormulario">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.descTipDocCas")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="tipDocCas" name="tipDocCas" type="text" class="inputTexto" size="117" maxlength="150" 
                                       value="" style="text-transform: none; margin-right: auto;" />
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.descTipDocEus")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="tipDocEus" name="tipDocEus" type="text" class="inputTexto" size="117" maxlength="150" 
                                       value="" style="text-transform: none; margin-right: auto;" />
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.descTipDocCasL")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea id="tipDocCasL" name="tipDocCasL" rows="7" cols="102" class="inputTexto" style="text-transform: none;" ><%=datModif != null && datModif.getTipDocLanbide_es_L() != null ? datModif.getTipDocLanbide_es_L().replaceAll("nnn", "\n") : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 190px; margin-left: 20px; float: left;" class="etiqueta">
                            <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.descTipDocEusL")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea id="tipDocEusL" name="tipDocEusL" rows="7" cols="102" class="inputTexto" style="text-transform: none;" ><%=datModif != null && datModif.getTipDocLanbide_eu_L() != null ? datModif.getTipDocLanbide_eu_L().replaceAll("nnn", "\n") : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">                    
                        <div style="width: auto; margin-left: 20px;">
                            <div class="etiqueta" style="width: 190px; float: left;">
                                <%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.datosTipDoc.TipDocDokusi")%>
                            </div>
                            <div>
                                <!-- combo de Tipos documentales Dokusi -->
                                <input type="text" name="codListaTipDokusi" id="codListaTipDokusi" maxlength="50" size="25" class="inputTextoMinus" value="" />
                                <input type="text" name="descListaTipDokusi" id="descListaTipDokusi" size="70" class="inputTexto" readonly="true" value="" /> 
                                <a href="" id="anchorListaTipDokusi" name="anchorListaTipDokusi">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipDokusi"
                                         name="botonTipDokusi" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                    </div>
                                         
                    <div class="lineaFormulario">                    
                        <div style="width: auto; margin-left: 20px;">
                            <div class="etiqueta" style="width: 190px; float: left;">
                                <!--   < % =meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.datosTipDoc.Grupo")%>-->
                            </div>
                            <div>
                                <!-- combo de Grupos de Tipos Documentales -->
                                <input type="hidden" name="codListaGrupo" id="codListaGrupo" maxlength="8" size="25" class="inputTextoMinus" value="GRP001" />
                                <input type="hidden" name="descListaGrupo" id="descListaGrupo" size="70" class="inputTexto" readonly="true" value="" /> 
                                <a href="" id="anchorListaGrupo" name="anchorListaGrupo">


                                </a>
                            </div>
                        </div>
                    </div>    
                                         
                </fieldset>               
                <br/><br/><br/>
                <div class="botonera" style="padding: auto">
                    <input type="button" id="btnGuardarTipDoc" name="btnGuardarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                    <input type="button" id="btnCancelarTipDoc" name="btnCancelarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>  
            </form>

            <script type="text/javascript">
                
                var mensajeValidacion = '';
                
                var listaCodigosTipDokusi = new Array();
                var listaDescripcionesTipDokusi = new Array();
                
                listaCodigosTipDokusi[0] = "";
                listaDescripcionesTipDokusi[0] = "";
                
                contador = 0;

                <logic:iterate id="numTipdokusi" name="listaTipDokusi" scope="request">
                    listaCodigosTipDokusi[contador] = ['<bean:write name="numTipdokusi" property="codDokusi" />'];
                    listaDescripcionesTipDokusi[contador] = '<bean:write name="numTipdokusi" property="desDokusi" />';
                    contador++;
                </logic:iterate>

                var comboListaTipDokusi = new Combo("ListaTipDokusi");
                comboListaTipDokusi.addItems(listaCodigosTipDokusi, listaDescripcionesTipDokusi);
                
                
                //Grupos de Tipos Documentales________________________________________________________________________________________
                var listaCodigosGrupo = new Array();
                var listaDescripcionesGrupo = new Array();
                var listaDescripcionesGrupoEU = new Array();
                
                listaCodigosGrupo[0] = "";
                listaDescripcionesGrupo[0] = "";
                listaDescripcionesGrupoEU[0] = "";
                
                cont = 0;

                <logic:iterate id="numGrupo" name="listaGruposTipDoc" scope="request">
                    listaCodigosGrupo[cont] = ['<bean:write name="numGrupo" property="codGrupo" />'];
                    listaDescripcionesGrupo[cont] = '<bean:write name="numGrupo" property="descGrupo_es" />';
                    listaDescripcionesGrupoEU[cont] = '<bean:write name="numGrupo" property="descGrupo_eu" />';
                    cont++;
                </logic:iterate>
                
                //idioma
                if ('<%=idiomaUsuario%>' == 4){
                    listaDescripcionesGrupo = listaDescripcionesGrupoEU;
                }
                
                var comboListaGrupo = new Combo("ListaGrupo");
                comboListaGrupo.addItems(listaCodigosGrupo, listaDescripcionesGrupo);
                //Grupos de Tipos Documentales_________________________________________________________________________________________
                
                
                if (<%=id%> != null && <%=id%> != 0) {
                    pintarDatos();
                    document.getElementById("id").disabled = true;
                    document.getElementById("tipDocCas").focus();
                } else {
                    document.getElementById("divId").style.display = "none";
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
                    } else {
                        return null;
                    }
                }

                function pintarDatos() {

                    document.getElementById('id').value = '<%=datModif != null && datModif.getId() != 0 ? datModif.getId() : ""%>';
                    document.getElementById('codTipDoc').value = '<%=datModif != null && datModif.getTipDocID() != 0 ? datModif.getTipDocID() : ""%>';
                    document.getElementById('tipDocCas').value = '<%=datModif != null && datModif.getTipDocLanbide_es() != null ? datModif.getTipDocLanbide_es() : ""%>';
                    document.getElementById('tipDocEus').value = '<%=datModif != null && datModif.getTipDocLanbide_eu() != null ? datModif.getTipDocLanbide_eu() : ""%>';
                    document.getElementById('codListaTipDokusi').value = '<%=request.getParameter("tipDocDokusi") != null ? request.getParameter("tipDocDokusi") : "" %>';
                    document.getElementById('descListaTipDokusi').value = '<%=request.getParameter("tipDokusi") != null ? request.getParameter("tipDokusi").toUpperCase() : "" %>';
                    
                    var codGrupo = '<%=datModif != null && datModif.getCodGrupo() != null ? datModif.getCodGrupo() : ""%>';
                    document.getElementById('codListaGrupo').value = codGrupo;
                    
                    for (var i=0; i < listaCodigosGrupo.length; i++){
                        if (listaCodigosGrupo[i] == codGrupo){
                            document.getElementById('descListaGrupo').value = listaDescripcionesGrupo[i];
                            break;
                        }
                            
                    }
                    
                   
                    resizeForFF();
                }
                
                function resizeForFF() {
                    //if (navigator.appName.indexOf("Internet Explorer") == -1) {
                        document.getElementById('cuerpoNuevoTipDoc').style.width = '99%';
                        document.getElementById('divCuerpo').style.width = '98%';
                        document.getElementById('fieldsetDatosTipDoc').style.width = '98%';
                    //}
                }

                function cancelar() {
                    var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                    if (resultado == 1) {
                        cerrarVentana();
                    }
                }

                function cerrarVentana() {
                    if (navigator.appName == 'Microsoft Internet Explorer') {
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

                function guardar() {
                    if (validarDatos()) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        
                        if (<%=id%> == null) {
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarTipDoc&tipo=0'
                                    + '&codTipDoc=' + document.getElementById('codTipDoc').value
                                    //+ '&tipDocCas=' + escape(document.getElementById('tipDocCas').value)
                                    + '&tipDocCas=' + document.getElementById('tipDocCas').value
                                    + '&tipDocEus=' + document.getElementById('tipDocEus').value
                                    + '&tipDocCasL=' + document.getElementById('tipDocCasL').value
                                    + '&tipDocEusL=' + document.getElementById('tipDocEusL').value
                                    + '&tipDocDokusi=' + document.getElementById('codListaTipDokusi').value
                                    + '&codGrupo=' + document.getElementById('codListaGrupo').value
                                   
                //+ '&todosProcedimientos=' + document.getElementById('chkAnadirProcedimientos').checked
                            ;
                        } else {
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=modificarTipDoc&tipo=0'
                                    + '&id=' + document.getElementById('id').value
                                    + '&codTipDoc=' + document.getElementById('codTipDoc').value
                                    //+ '&tipDocCas=' + escape(document.getElementById('tipDocCas').value)
                                    + '&tipDocCas=' + document.getElementById('tipDocCas').value
                                    + '&tipDocEus=' + document.getElementById('tipDocEus').value
                                    + '&tipDocCasL=' + document.getElementById('tipDocCasL').value
                                    + '&tipDocEusL=' + document.getElementById('tipDocEusL').value
                                    + '&tipDocDokusi=' + document.getElementById('codListaTipDokusi').value
                                    + '&codGrupo=' + document.getElementById('codListaGrupo').value
                            ;
                        }
                        try {
                            ajax.open("POST", url, false);
                            //ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
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
                            var listaTipDoc = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            var nodoCampo;
                            var j;
                            
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaTipDoc[j] = codigoOperacion;
                                } else if (hijos[j].nodeName == "FILA") {
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
                                        if (hijosFila[cont].nodeName == "COD_TIPDOC") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPDOC_ES") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPDOC_EU") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPDOC_ES_L") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPDOC_EU_L") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPDOC_DOKUSI") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIENE_METADATO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DESHABILITADO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "GRUPO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        }
                                        
                                    }
                                    listaTipDoc[j] = fila;
                                    fila = new Array();
                                }
                            }
                            if (codigoOperacion == "0") {
                                self.parent.opener.retornoXanelaAuxiliar(listaTipDoc);
                  
                                if (<%=datModif.getTipDocID()%> == null) {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocGuardadoOK")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocModificadoOK")%>');
                                }
                                cerrarVentana();
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else if (codigoOperacion == "4") {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.tipDocDuplicado")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                        }
                    } else {
                        //jsp_alerta("A", escape(mensajeValidacion));
                        jsp_alerta("A", mensajeValidacion);
                    }
                }

                function validarDatos() {
                    mensajeValidacion = '';
                    var correcto = true;
                    try {
                        if (!validarDatosTipDoc()) {
                            correcto = false;
                        }
                    } catch (err) {
                        correcto = false;
                        if (mensajeValidacion == '') {
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                        }
                    }
                    return correcto;
                }
                
                function validarDatosTipDoc() {
                    var correcto = true;
    var mensajeValidacion = "";

                    // Código de Tipo documental
                    var cTipDoc = document.getElementById('codTipDoc').value;
    if (cTipDoc != null && cTipDoc !== '') {
                        if (!validarNumerico(cTipDoc)) {
            if (mensajeValidacion === '') {
                                mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.codTipDocNoNumerico")%>';
                            }
                            document.getElementById('codTipDoc').style.border = '1px solid red';
                            correcto = false;
                        } else {
                            document.getElementById('codTipDoc').removeAttribute("style");
                        }
                    } else {
        document.getElementById('codTipDoc').removeAttribute("style");
    }

    // Función para validar caracteres prohibidos en las descripciones
    function contieneCaracteresProhibidos(texto) {
        return /[<>]/.test(texto); // Comprueba si hay < o >
                    }

                    // Descripción en castellano del Tipo documental
                    if (correcto) {
                        var ctipDocCas = document.getElementById('tipDocCas').value;
        if (ctipDocCas == null || ctipDocCas === '') {
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipDocCasVacio")%>';
                            correcto = false;
        } else if (contieneCaracteresProhibidos(ctipDocCas)) {
            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipcaracteres")%>';
            document.getElementById('tipDocCas').style.border = '1px solid red';
            correcto = false;
        } else {
            document.getElementById('tipDocCas').removeAttribute("style");
                        }
                    }

                    // Descripción en euskera del Tipo documental
                    if (correcto) {
                        var ctipDocEus = document.getElementById('tipDocEus').value;
        if (ctipDocEus == null || ctipDocEus === '') {
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipDocEusVacio")%>';
                            correcto = false;
        } else if (contieneCaracteresProhibidos(ctipDocEus)) {
            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipcaracteres")%>';  "La descripción en euskera no puede contener los caracteres < o >.";
            document.getElementById('tipDocEus').style.border = '1px solid red';
            correcto = false;
        } else {
            document.getElementById('tipDocEus').removeAttribute("style");
                        }
                    }

    // Validación de Tipo documental DOKUSI
                    if (correcto) {
                        var cTipDokusi = document.getElementById('codListaTipDokusi').value;
        if (cTipDokusi != null && cTipDokusi !== '') {
                            if (!existeCodigoCombo(cTipDokusi, listaCodigosTipDokusi)) {
                                correcto = false;
                                document.getElementById('codListaTipDokusi').style.border = '1px solid red';
                                document.getElementById('descListaTipDokusi').style.border = '1px solid red';
                if (mensajeValidacion === '') {
                                    mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDokusi.datosDokusi.codDokusiNoExiste")%>';
                                }
                            } else {
                                document.getElementById('codListaTipDokusi').removeAttribute("style");
                                document.getElementById('descListaTipDokusi').removeAttribute("style");
                            }
                        } else {
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipDocDokusiVacio")%>';
                            correcto = false;
                        }
                    }
                    
    // Validación de Grupo
                    if (correcto) {
                        var cGrupo = document.getElementById('codListaGrupo').value;
        if (cGrupo != null && cGrupo !== '') {
                            if (!existeCodigoCombo(cGrupo, listaCodigosGrupo)) {
                                correcto = false;
                                document.getElementById('codListaGrupo').style.border = '1px solid red';
                                document.getElementById('descListaGrupo').style.border = '1px solid red';
                if (mensajeValidacion === '') {
                                    mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.codGrupoNoExiste")%>';
                                }
                            } else {
                                document.getElementById('codListaGrupo').removeAttribute("style");
                                document.getElementById('descListaGrupo').removeAttribute("style");
                            }
                        } else {
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.grupoVacio")%>';
                            correcto = false;
                        }
                    }

    // Validación de las descripciones largas en castellano y euskera
                    if (correcto) {
                        var ctipDocCasL = document.getElementById('tipDocCasL').value;
        if (ctipDocCasL == null || ctipDocCasL === '') {
                            document.getElementById('tipDocCasL').value = " ";
        } else if (contieneCaracteresProhibidos(ctipDocCasL)) {
            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipcaracteres")%>';
            document.getElementById('tipDocCasL').style.border = '1px solid red';
            correcto = false;
        } else {
            document.getElementById('tipDocCasL').removeAttribute("style");
                        }
                        var ctipDocEusL = document.getElementById('tipDocEusL').value;
        if (ctipDocEusL == null || ctipDocEusL === '') {
                            document.getElementById('tipDocEusL').value = " ";
        } else if (contieneCaracteresProhibidos(ctipDocEusL)) {
            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipcaracteres")%>';
            document.getElementById('tipDocEusL').style.border = '1px solid red';
            correcto = false;
        } else {
            document.getElementById('tipDocEusL').removeAttribute("style");
                        }
                    }

    // Si hubo un mensaje de validación, mostrar alerta
    if (!correcto) {
        jsp_alerta("A", mensajeValidacion);
    }

                    return correcto;
                }

                function existeCodigoCombo(seleccionado, listaCodigos) {
                    if (seleccionado != undefined && seleccionado != null && listaCodigos != undefined && listaCodigos != null) {
                        if (validarNumerico(seleccionado)) {
                            var encontrado = false;
                            var i = 0;
                            while (!encontrado && i < listaCodigos.length) {
                                if (listaCodigos[i] == seleccionado) {
                                    encontrado = true;
                                } else {
                                    i++;
                                }
                            }
                            return encontrado;
                        } else {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            </script>
        </div>
    </body>
</html>


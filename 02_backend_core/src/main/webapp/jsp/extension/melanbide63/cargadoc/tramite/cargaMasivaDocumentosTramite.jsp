<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide63.i18n.MeLanbide63I18n" %>
<head>
    <%
        int idiomaUsuario = 0;
        int codOrganizacion = 0;
        int idUsuario=0;
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
                    codOrganizacion  = usuario.getOrgCod();
                    idUsuario=usuario.getIdUsuario();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
        }
        catch(Exception ex)
        {
        
        }
        MeLanbide63I18n meLanbide63I18n = MeLanbide63I18n.getInstance();
        String numExpediente    = request.getParameter("numero");
    %>
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    

    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

    <script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    
    <script type="text/javascript">
        var msgValidacion = '';
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
        
        function validaDatosDescarga(){
            var datosOK=true;
            msgValidacion='';
            if(document.getElementById('txtNumTarea')!=null && document.getElementById('txtNumTarea').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtNumTarea').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }else if(document.getElementById('txtCodProcedimientoDes')!=null && document.getElementById('txtCodProcedimientoDes').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodProcedimientoDes').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }
            /*else if(document.getElementById('txtCodInternoTramiteDes')!=null && document.getElementById('txtCodInternoTramiteDes').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodInternoTramiteDes').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }else if(document.getElementById('txtCodPlantillaDes')!=null && document.getElementById('txtCodPlantillaDes').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodPlantillaDes').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }*/
            return datosOK;
        }
        
        function validaDatos(){
            var datosOK=true;
            msgValidacion='';
            if(document.getElementById('txtCodProcedimiento')!=null && document.getElementById('txtCodProcedimiento').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodProcedimiento').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }else if(document.getElementById('txtCodInternoTramite')!=null && document.getElementById('txtCodInternoTramite').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodInternoTramite').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }else if(document.getElementById('txtCodPlantilla')!=null && document.getElementById('txtCodPlantilla').value=="" ){
                datosOK=false;
                msgValidacion= document.getElementById('txtCodPlantilla').title + ' <%=meLanbide63I18n.getMensaje(idiomaUsuario, "validacion.campo.obligatorio")%>';
            }
            return datosOK;
        }
        
        function ejecutarDescargaMasivaResol() {
            pleaseWait('on');
            var ajax = getXMLHttpRequest();
            var nodos = "";
            if(validaDatosDescarga()){
                if (ajax != null) {
                var control = new Date();
                var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                var parametros = '?tarea=preparar&modulo=MELANBIDE63&operacion=descargaMasivaRegexlanResolucionesDsdURL&tipo=0&control=' + control.getTime()
                + '&numTarea='+(document.getElementById('txtNumTarea')!=null?document.getElementById('txtNumTarea').value:'')
                + '&codigoProcIndicadoInterfaz='+(document.getElementById('txtCodProcedimientoDes')!=null?document.getElementById('txtCodProcedimientoDes').value:'')
                + '&codigoInteTramIndicaInterf='+(document.getElementById('txtCodInternoTramiteDes')!=null?document.getElementById('txtCodInternoTramiteDes').value:'')
                + '&codigoPlantilla='+(document.getElementById('txtCodPlantillaDes')!=null?document.getElementById('txtCodPlantillaDes').value:'')
                + '&urlDocumentosResol='+(document.getElementById('txturlDocumentosResolDes')!=null?document.getElementById('txturlDocumentosResolDes').value:'')
                ;
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var descOperacion = null;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                        if (hijos[j].nodeName == "DETALLE_OPERACION") {
                            descOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    jsp_alerta("A", codigoOperacion + "\n - " + descOperacion);
                } catch (Err) {
                    jsp_alerta("A", "Error General en java script -  Descripcion: " + Err.description);
                }//try-catch
            }//if(ajax!=null)
            
            }else{ // if validaDatos
                jsp_alerta("A", msgValidacion);
            }
            pleaseWait('off');
        }

        function ejecutarCargaMasivaResol() {
            pleaseWait('on');
            var ajax = getXMLHttpRequest();
            var nodos = "";
            if(validaDatos()){
                if (ajax != null) {
                var control = new Date();
                var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                var parametros = '?tarea=preparar&modulo=MELANBIDE63&operacion=cargaMasivaRegexlanResolucionesDsdURL&tipo=0&control=' + control.getTime()
                + '&codigoProcIndicadoInterfaz='+(document.getElementById('txtCodProcedimiento')!=null?document.getElementById('txtCodProcedimiento').value:'')
                + '&codigoInteTramIndicaInterf='+(document.getElementById('txtCodInternoTramite')!=null?document.getElementById('txtCodInternoTramite').value:'')
                + '&codigoPlantilla='+(document.getElementById('txtCodPlantilla')!=null?document.getElementById('txtCodPlantilla').value:'')
                + '&urlDocumentosResol='+(document.getElementById('txturlDocumentosResol')!=null?document.getElementById('txturlDocumentosResol').value:'')
                + '&eliminarDocServidor='+(document.getElementById('checkEliminarDocServidor')!=null && document.getElementById('checkEliminarDocServidor').checked ? '1' : '0')
                ;
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                                    var descOperacion = null;
                                    for (j = 0; hijos != null && j < hijos.length; j++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        }
                                        if (hijos[j].nodeName == "DETALLE_OPERACION") {
                                            descOperacion = hijos[j].childNodes[0].nodeValue;
                                        }
                                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                                    jsp_alerta("A", codigoOperacion + "\n - " + descOperacion);
                                } catch (Err) {
                                    jsp_alerta("A", "Error General en java script -  Descripcion: " + Err.description);
                                }//try-catch
                            }//if(ajax!=null)
            
            }else{ // if validaDatos
                jsp_alerta("A", msgValidacion);
            }
            pleaseWait('off');
         }
        function ejecutarDeshacerCargaMasivaResol() {
            pleaseWait('on');
            var ajax = getXMLHttpRequest();
            var nodos = "";
            if(validaDatos()){
                if (ajax != null) {
                var control = new Date();
                var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                var parametros = '?tarea=preparar&modulo=MELANBIDE63&operacion=deshacerCargaMasivaRegexlanResolucionesDsdURL&tipo=0&control=' + control.getTime()
                + '&codigoProcIndicadoInterfaz='+(document.getElementById('txtCodProcedimiento')!=null?document.getElementById('txtCodProcedimiento').value:'')
                + '&codigoInteTramIndicaInterf='+(document.getElementById('txtCodInternoTramite')!=null?document.getElementById('txtCodInternoTramite').value:'')
                + '&codigoPlantilla='+(document.getElementById('txtCodPlantilla')!=null?document.getElementById('txtCodPlantilla').value:'')
                + '&urlDocumentosResol='+(document.getElementById('txturlDocumentosResol')!=null?document.getElementById('txturlDocumentosResol').value:'')
                + '&eliminarDocServidor='+(document.getElementById('checkEliminarDocServidor')!=null && document.getElementById('checkEliminarDocServidor').checked ? '1' : '0')
                ;
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                                    var descOperacion = null;
                                    for (j = 0; hijos != null && j < hijos.length; j++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        }
                                        if (hijos[j].nodeName == "DETALLE_OPERACION") {
                                            descOperacion = hijos[j].childNodes[0].nodeValue;
                                        }
                                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                                    jsp_alerta("A", codigoOperacion + "\n - " + descOperacion);
                                } catch (Err) {
                                    jsp_alerta("A", "Error General en java script -  Descripcion: " + Err.description);
                                }//try-catch
                            }//if(ajax!=null)
            }else{ // if validaDatos
                jsp_alerta("A", msgValidacion);
            }
            pleaseWait('off');
         }
    </script>
</head>
<body>
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div style="width: 80%">
        <table cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td style="padding-left: 15px" class="txttitblanco"> <%=meLanbide63I18n.getMensaje(idiomaUsuario, "legend.titulo.principal")%></td>
            </tr>
            <tr>
                <td valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                    <div id="contenidoProc" class="cuadroFondoBlanco" style="overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                        <fieldset style="float: left; padding-left: 10px; padding-right: 10px;">
                            <legend class="etiqueta" align="center"><%=meLanbide63I18n.getMensaje(idiomaUsuario, "legend.opciones.descarga")%></legend>
                            <table>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.descarga.masiva.tarea")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="text" id="txtNumTarea" name="txtNumTarea" class="inputTextoObligatorio" maxlength="6" size="6" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.tarea")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.procedimiento")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="text" id="txtCodProcedimientoDes" name="txtCodProcedimientoDes" class="inputTextoObligatorio" maxlength="10" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.procedimiento")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.codigointerno.tramite")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="number" id="txtCodInternoTramiteDes" name="txtCodInternoTramiteDes" class="inputTextoObligatorio" maxlength="3" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.codigointerno.tramite")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.codigo.plantilla")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="number" id="txtCodPlantillaDes" name="txtCodPlantillaDes" class="inputTextoObligatorio" maxlength="3" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.codigo.plantilla")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.url.documentos.servidor")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="text" id="txturlDocumentosResolDes" name="txturlDocumentosResolDes" class="inputTexto" maxlength="1000" size="100" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.url.documentos.servidor")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div>
                                            <label id="lblTextoExplicacionURL" style="font-size: 9px">
                                                <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.url.notas")%>
                                            </label>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td style="width: 80%">
                                <div class="lineaFormulario">
                                    <div style="text-align: center; margin-left: 20px;">
                                        <div style="text-align: center">
                                            <input type="button" id="btnDescargaMasivaDocResol" name="btnDescargaMasivaDocResol" class="botonGeneral" value="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "btn.descargar")%>" onclick="ejecutarDescargaMasivaResol();">
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                   </table>
                </td>
            </tr>
            <tr>
                <td valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                    <div id="contenidoProc" class="cuadroFondoBlanco" style="overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                        <fieldset style="float: left; padding-left: 10px; padding-right: 10px;">
                            <legend class="etiqueta" align="center"><%=meLanbide63I18n.getMensaje(idiomaUsuario, "legend.opciones")%></legend>
                            <table>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.procedimiento")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="text" id="txtCodProcedimiento" name="txtCodProcedimiento" class="inputTextoObligatorio" maxlength="10" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.procedimiento")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.codigointerno.tramite")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="number" id="txtCodInternoTramite" name="txtCodInternoTramite" class="inputTextoObligatorio" maxlength="3" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.codigointerno.tramite")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.codigo.plantilla")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="number" id="txtCodPlantilla" name="txtCodPlantilla" class="inputTextoObligatorio" maxlength="3" size="5" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.codigo.plantilla")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.url.documentos.servidor")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="text" id="txturlDocumentosResol" name="txturlDocumentosResol" class="inputTexto" maxlength="1000" size="100" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.txt.carga.masiva.url.documentos.servidor")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div>
                                            <label id="lblTextoExplicacionURL" style="font-size: 9px">
                                                <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.url.notas")%>
                                            </label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 30%">
                                        <div style="float: left; text-align: left;" class="etiqueta">
                                            <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.elimnar.documentos")%>
                                        </div>
                                    </td>
                                    <td>
                                        <div style="float: left;">
                                            <div style="float: left;">
                                                <input type="checkbox" id="checkEliminarDocServidor" name="checkEliminarDocServidor" title="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "title.chx.carga.masiva.eliminardoc.servidor")%>"/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </fieldset>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td style="width: 80%">
                                <div class="lineaFormulario">
                                    <div style="text-align: center">
                                        <div style="text-align: center">
                                            <input type="button" id="btnCargaMasivaDocResol" name="btnCargaMasivaDocResol" class="botonGeneral" value="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "btn.ejecutar")%>" onclick="ejecutarCargaMasivaResol();">
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td style="width: 20%">
                                <div class="lineaFormulario">
                                    <div style="text-align: right">
                                        <div style="text-align: right">
                                            <input type="button" id="btnDeshacerCargaMasivaDocResol" name="btnDeshacerCargaMasivaDocResol" class="botonGeneral" value="<%=meLanbide63I18n.getMensaje(idiomaUsuario, "btn.deshacerCarga")%>" onclick="ejecutarDeshacerCargaMasivaResol();">
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 80%">
                            </td>
                            <td style="width: 20%; text-align: right">
                                <fieldset>
                                    <label id="lblTextoNotaDeshacerCarga" style="font-size: 9px">
                                        <%=meLanbide63I18n.getMensaje(idiomaUsuario, "label.carga.masiva.deshacer.carga")%>
                                    </label>
                                </fieldset>
                            </td>
                        </tr>
                   </table>
                </td>
            </tr>
        </table>
    </div>
</body>
<script type="text/javascript">
        pleaseWait('off');
</script>

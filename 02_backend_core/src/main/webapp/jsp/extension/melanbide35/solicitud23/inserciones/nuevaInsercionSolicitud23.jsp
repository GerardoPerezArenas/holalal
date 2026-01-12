<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    try {
        if (session != null) {
            if (usuario != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    } catch(Exception ex) {        
    }
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicacion.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    
    
    Boolean consulta = false;
    String tituloPagina = "";
    if(consulta) {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.consultaPreparador.tituloPagina");
    } else {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nuevoPreparador.tituloPagina");        
    }
       
    Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");

    
 // lista tipo sexoEdad
    List<SelectItem> listaTiposSexoEdad = new ArrayList<SelectItem>();
    if(request.getAttribute("lstTipoSexo") != null){
        listaTiposSexoEdad = (List<SelectItem>)request.getAttribute("lstTipoSexo");
    }
        
    String lcodTipoSexoEdad = "";
    String ldescTipoSexoEdad = "";    

    if (listaTiposSexoEdad != null && listaTiposSexoEdad.size() > 0) {
        int i;
        SelectItem tc = null;
        for (i = 0; i < listaTiposSexoEdad.size() - 1; i++) {
            tc = (SelectItem) listaTiposSexoEdad.get(i);
            lcodTipoSexoEdad += "\"" + tc.getId().toString() + "\",";
            ldescTipoSexoEdad += "\"" + escape(tc.getLabel()) + "\",";
        }
        tc = (SelectItem) listaTiposSexoEdad.get(i);
        lcodTipoSexoEdad += "\"" + tc.getId().toString() + "\"";
        ldescTipoSexoEdad += "\"" + escape(tc.getLabel()) + "\"";
    }

    // lista tipo discapacidad
    List<SelectItem> listaTiposDiscapacidad = new ArrayList<SelectItem>();
    if(request.getAttribute("lstTipoDiscapacidad") != null) {
        listaTiposDiscapacidad = (List<SelectItem>)request.getAttribute("lstTipoDiscapacidad");
    }

    String lcodTipoDiscapacidad = "";
    String ldescTipoDiscapacidad = "";    

    if (listaTiposDiscapacidad != null && listaTiposDiscapacidad.size() > 0) {
      int i;
      SelectItem tc = null;
      for (i = 0; i < listaTiposDiscapacidad.size() - 1; i++) {
          tc = (SelectItem) listaTiposDiscapacidad.get(i);
          lcodTipoDiscapacidad += "\"" + tc.getId().toString() + "\",";
          ldescTipoDiscapacidad += "\"" + escape(tc.getLabel()) + "\",";
        }
      tc = (SelectItem) listaTiposDiscapacidad.get(i);
      lcodTipoDiscapacidad += "\"" + tc.getId().toString() + "\"";
      ldescTipoDiscapacidad += "\"" + escape(tc.getLabel()) + "\"";
    }


%>
<title><%=tituloPagina%></title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>


<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">

    <%!
        // Funcion para escapar strings para javascript
        private String escape(String str) 
        {
            return StringEscapeUtils.escapeJavaScript(str);
        }
    %>
    //LISTAS DE VALORES PARA LOS COMBOS
    var codTipoSexoEdad = [<%=lcodTipoSexoEdad%>];
    var descTipoSexoEdad = [<%=ldescTipoSexoEdad%>];

    var codTipoDiscapacidad = [<%=lcodTipoDiscapacidad%>];
    var descTipoDiscapacidad = [<%=ldescTipoDiscapacidad%>];

    var mensajeValidacion = '';
    var nuevo = true;

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

    function inicio() {
        comboTipoSexoEdad = new Combo('TipoSexoEdad');
        comboTipoDiscapacidad = new Combo('TipoDiscapacidad');
        cargarCombos();
    }

    function cargarCombos() {
        comboTipoSexoEdad.addItems(codTipoSexoEdad, descTipoSexoEdad);
        comboTipoDiscapacidad.addItems(codTipoDiscapacidad, descTipoDiscapacidad);
    }

    function cancelar() {
        var resultado = jsp_alerta("", "<%=meLanbide35I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
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

    function guardar() {
        calcularImporteInsercion();
        if (validarDatos()) {

            document.getElementById('msgGuardandoDatos').style.display = "inline";
            barraProgresoEca('on', 'barraProgresoNuevoPreparadorSolicitud');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarInsercionSolicitud23&tipo=0&numero=<%=numExpediente%>'
                    + '&numeroExpediente=<%=numExpediente%>'
                    + '&tipoDiscapacidad=' + document.getElementById('codTipoDiscapacidad').value
                    + '&tipoSexoEdad=' + document.getElementById('codTipoSexoEdad').value
                    + '&numeroPersonas=' + convertirANumero(document.getElementById('numeroPersonas').value)
                    + '&porcentajeTrabajo=' + convertirANumero(document.getElementById('porcentajeTrabajo').value)
                    + '&importeSolicitud=' + document.getElementById('importeSolicitud').value
                    + '&importeSolicitudUnAno=' + document.getElementById('importeSolicitudUnAno').value
                    + '&control=' + control.getTime();
            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
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
                var codigoOperacion = null;
                var msgValidacion = '';
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var codigoOperacion = nodos[0].firstChild.textContent;

                //for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    self.parent.opener.retornoXanelaAuxiliar();
                    barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    cerrarVentana();
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", msgValidacion);
                } else if (codigoOperacion == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifPreparadorRepetido")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch

            barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
            cerrarVentana();
        } else {
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }

    function calcularImporteInsercion() {
        var correcto = true;
        document.getElementById('importeSolicitud').value = '';
        if (document.getElementById('numeroPersonas').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('numeroPersonas'), 8, 2)) {
                    correcto = false;
                }
            } catch (err) {
                correcto = false;
            }
        } else {
            correcto = false;
        }


        if (document.getElementById('porcentajeTrabajo').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('porcentajeTrabajo'), 8, 2)) {
                    correcto = false;
                }
            } catch (err) {
                correcto = false;
            }
        } else {
            correcto = false;
        }

        if (document.getElementById('codTipoDiscapacidad').value == '') {
            correcto = false;
        }

        if (document.getElementById('codTipoSexoEdad').value == '') {
            correcto = false;
        }

        if (correcto) {

            var discapacidad = document.getElementById('codTipoDiscapacidad').value;
            var edad = document.getElementById('codTipoSexoEdad').value;
            var importeColectivo = '';
            if (discapacidad == "A") {
                if (edad == "H") {
                    importeColectivo = <%=importesConfiguracion.getcAh()%>;
                }
                if (edad == "H45") {
                    importeColectivo = <%=importesConfiguracion.getcAh45()%>;
                }
                if (edad == "M") {
                    importeColectivo = <%=importesConfiguracion.getcAm()%>;
                }
            }
            if (discapacidad == "B") {
                if (edad == "H") {
                    importeColectivo = <%=importesConfiguracion.getcBh()%>;
                }
                if (edad == "H45") {
                    importeColectivo = <%=importesConfiguracion.getcBh45()%>;
                }
                if (edad == "M") {
                    importeColectivo = <%=importesConfiguracion.getcBm()%>;
                }
            }
            if (discapacidad == "C") {
                if (edad == "H") {
                    importeColectivo = <%=importesConfiguracion.getcCh()%>;
                }
                if (edad == "H45") {
                    importeColectivo = <%=importesConfiguracion.getcCh45()%>;
                }
                if (edad == "M") {
                    importeColectivo = <%=importesConfiguracion.getcCm()%>;
                }
            }
            if (discapacidad == "D") {
                if (edad == "H") {
                    importeColectivo = <%=importesConfiguracion.getcDh()%>;
                }
                if (edad == "H45") {
                    importeColectivo = <%=importesConfiguracion.getcDh45()%>;
                }
                if (edad == "M") {
                    importeColectivo = <%=importesConfiguracion.getcDm()%>;
                }
            }

            //Si el bigdecimal aun esta a null es que no ha llegado bien el dato
            if (importeColectivo == '') {
                document.getElementById('importeSolicitud').value = '';
            }

            document.getElementById('importeSolicitud').value = ((importeColectivo * (document.getElementById('porcentajeTrabajo').value).replace(",", ".")) / 100) * document.getElementById('numeroPersonas').value;
            document.getElementById('importeSolicitud').value = Number(((document.getElementById('importeSolicitud').value))).toFixed(2).replace(".", ",");

        } else {
            document.getElementById('importeSolicitud').value = '';
        }

    }

    function validarDatos() {
        var correcto = true;
        mensajeValidacion = '';

        if (document.getElementById('codTipoDiscapacidad').value == '') {
            correcto = false;
            document.getElementById('codTipoDiscapacidad').style.border = '1px solid red';
        }

        if (document.getElementById('codTipoSexoEdad').value == '') {
            correcto = false;
            document.getElementById('codTipoSexoEdad').style.border = '1px solid red';
        }

        if (document.getElementById('numeroPersonas').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('numeroPersonas'), 8, 2)) {
                    correcto = false;
                    document.getElementById('numeroPersonas').style.border = '1px solid red';
                } else {
                    document.getElementById('numeroPersonas').removeAttribute('style');
                }
            } catch (err) {
                correcto = false;
                document.getElementById('numeroPersonas').style.border = '1px solid red';
            }
        } else {
            document.getElementById('numeroPersonas').style.border = '1px solid red';
        }

        if (document.getElementById('porcentajeTrabajo').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('porcentajeTrabajo'), 8, 2)) {
                    correcto = false;
                    document.getElementById('porcentajeTrabajo').style.border = '1px solid red';
                } else {
                    document.getElementById('porcentajeTrabajo').removeAttribute('style');
                }
            } catch (err) {
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
            }
        } else {
            document.getElementById('porcentajeTrabajo').style.border = '1px solid red';
        }

        if (document.getElementById('importeSolicitudUnAno').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('importeSolicitudUnAno'), 8, 2)) {
                    correcto = false;
                    document.getElementById('importeSolicitudUnAno').style.border = '1px solid red';
                } else {
                    document.getElementById('importeSolicitudUnAno').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('importeSolicitudUnAno').value));
                }
            } catch (err) {
                correcto = false;
                document.getElementById('importeSolicitudUnAno').style.border = '1px solid red';
            }
        } else {
            document.getElementById('importeSolicitudUnAno').style.border = '1px solid red';
        }


        if (document.getElementById('importeSolicitud').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('importeSolicitud'), 8, 2)) {
                    correcto = false;
                    document.getElementById('importeSolicitud').style.border = '1px solid red';
                } else {
                    document.getElementById('importeSolicitud').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('importeSolicitud').value));
                }
            } catch (err) {
                correcto = false;
                document.getElementById('importeSolicitud').style.border = '1px solid red';
            }
        } else {
            document.getElementById('importeSolicitud').style.border = '1px solid red';
        }
        return correcto;
    }

</script>
<body onload="inicio();" class="contenidoPantalla">
    <form>
        <div id="barraProgresoNuevoPreparadorSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgGuardandoDatos">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                </span>
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
        <div style="width: 100%; padding: 10px; text-align: left;">
            <div class="sub3titulo" style="text-align: left;width: 95%;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.datosInsercion")%>
                </span>
            </div>
            <fieldset style="width: 94%;">    
                <!--<legend>Preparador</legend>-->
                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.tipoDiscapacidad")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input id="codTipoDiscapacidad" name="codTipoDiscapacidad" type="text" class="inputComboObligatorio" size="2" maxlength="1" 
                               onkeyup="xAMayusculas(this);" onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descTipoDiscapacidad" name="descTipoDiscapacidad" type="text" class="inputComboObligatorio" size="22" readonly >
                        <a id="anchorTipoDiscapacidad" name="anchorTipoDiscapacidad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoDiscapacidad" name="botonTipoDiscapacidad" border="0" width="14" height="14" style="cursor:pointer;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <BR><BR>
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.tipoEdadSexo")%><font color="red">*</font>
                    </div>
                    <div style="float: left;">
                        <input id="codTipoSexoEdad" name="codTipoSexoEdad" type="text" class="inputComboObligatorio" size="2" maxlength="3" 
                               onkeyup="xAMayusculas(this);" onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descTipoSexoEdad" name="descTipoSexoEdad" type="text" class="inputComboObligatorio" size="22" readonly >
                        <a id="anchorTipoSexoEdad" name="anchorTipoSexoEdad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoSexoEdad" name="botonTipoSexoEdad" border="0" width="14" height="14" style="cursor:pointer;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                </div>

                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.numeroPersonas")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input onfocusout="calcularImporteInsercion()" type="text" id="numeroPersonas" name="numeroPersonas" size="10" maxlength="10" class="inputTextoObligarorio"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <br><br>
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.procentajeTrabajo")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input onfocusout="calcularImporteInsercion()" type="text" id="porcentajeTrabajo" name="porcentajeTrabajo" size="10" maxlength="10" class="inputTextoObligarorio"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.importeUnAnio")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input disabled type="text" id="importeSolicitud" name="importeSolicitud" size="10" maxlength="10" class="inputTexto textoNumerico"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.inserciones.importeSolicitud")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input type="text" id="importeSolicitudUnAno" name="importeSolicitudUnAno" size="10" maxlength="10" class="inputTexto textoNumerico"/>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="botonera" style="padding-top: 20px;">
            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardar();">
            <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
</body>


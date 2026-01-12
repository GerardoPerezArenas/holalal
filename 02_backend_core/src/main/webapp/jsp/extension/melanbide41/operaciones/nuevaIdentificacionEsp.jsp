<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            IdentificacionEspVO datModif = new IdentificacionEspVO();
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();
            
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
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
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
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
                    datModif = (IdentificacionEspVO)request.getAttribute("datModif");
                }
                if(request.getAttribute("datoEspecialidad") != null)
                {
                    datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
                }
                datModif.setIdEspSol(datoEspecialidad.getId());
                datModif.setCodCp(datoEspecialidad.getCodCP());
                datModif.setDenomEsp(datoEspecialidad.getDenominacion());
            }
            catch(Exception ex)
            {
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
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var mensajeValidacion = '';

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
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=crearIdentificacionEsp&tipo=0&numero=<%=numExpediente%>"
                                + "&nuevo=" + nuevo
                                + "&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                + "&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp().toString() : ""%>"
                                + "&denomesp=<%=datModif != null && datModif.getDenomEsp() != null ? datModif.getDenomEsp().toString() : ""%>"
                                + "&horas=" + document.getElementById('horas').value
                                + "&alumnos=" + document.getElementById('alumnos').value
                                + "&alumnosAut=" + document.getElementById('alumnosAut').value
                                + "&certpro=" + convertirSINOnumero(document.getElementsByName('certpro'))
                                + "&realdecregu=" + document.getElementById('realdecregu').value
                                + "&boefecpub=" + document.getElementById('boefecpub').value
                                + "&descripadapt=" + document.getElementById('descripadapt').value
                                + "&observadapt=" + document.getElementById('observadapt').value;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=crearIdentificacionEsp&tipo=0&numero=<%=numExpediente%>"
                                + "&nuevo=" + nuevo
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                + "&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp().toString() : ""%>"
                                + "&denomesp=<%=datModif != null && datModif.getDenomEsp() != null ? datModif.getDenomEsp().toString() : ""%>"
                                + "&horas=" + document.getElementById('horas').value
                                + "&alumnos=" + document.getElementById('alumnos').value
                                + "&alumnosAut=" + document.getElementById('alumnosAut').value
                                + "&certpro=" + convertirSINOnumero(document.getElementsByName('certpro'))
                                + "&realdecregu=" + document.getElementById('realdecregu').value
                                + "&boefecpub=" + document.getElementById('boefecpub').value
                                + "&descripadapt=" + document.getElementById('descripadapt').value
                                + "&observadapt=" + document.getElementById('observadapt').value;
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
                        var listaIdentificacionEsp = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaIdentificacionEsp[j] = codigoOperacion;
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
                                    } else if (hijosFila[cont].nodeName == "ID_ESPSOL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_NUM") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_CODESP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_DENESP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_HORAS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[5].toString();
                                            tex = tex.replace(".", ",");
                                            fila[5] = tex;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_ALUM") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[6].toString();
                                            tex = tex.replace(".", ",");
                                            fila[6] = tex;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_CERTP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_RDER") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_BOEFP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_DESADAP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_OBSADAP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IDE_ALUM_AUT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[6].toString();
                                            tex = tex.replace(".", ",");
                                            fila[12] = tex;
                                        } else {
                                            fila[12] = '-';
                                        }
                                    }
                                }
                                listaIdentificacionEsp[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"msg.datosGuardados")%>');
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function convertirSINOnumero(sino) {
                try {
                    for (var i = 0; i < sino.length; i++) {
                        if (sino[i].value == '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.no")%>' && sino[i].checked == true)
                            return 1;
                        if (sino[i].value == '<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.si")%>' && sino[i].checked == true)
                            return 0;
                    }
                } catch (err) {
                    return null;
                }
                return null;
            }

            function validarNumerico(numero) {
                try {
                    if (Trim(numero.value) != '') {
                        return /^([0-9])*$/.test(numero.value);
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarNumericoDecimal(numero) {
                try {
                    if (Trim(numero.value) != '') {
                        return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
                try {
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if (Trim(numero.value) != '') {
                        var valor = numero.value;
                        var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
                        var regex = new RegExp(pattern);
                        var result = regex.test(valor);
                        return result;
                    } else {
                        return true;
                    }
                } catch (err) {
                    alert(err);
                    return false;
                }
            }

            function cancelar() {
                var resultado = 1; //jsp_alerta('','<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function volver() {
                var resultado = 1; //jsp_alerta('','<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaVolver")%>');
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

            function comprobarCaracteresEspeciales(texto) {
                //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
                var iChars = "&'<>|^\\%";   // / quitado para carga automatica de trayectorias 30052014
                for (var i = 0; i < texto.length; i++) {
                    if (iChars.indexOf(texto.charAt(i)) != -1) {
                        return false;
                    }
                }
                return true;
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;
                var horas = document.getElementById('horas');
                if (horas != null && horas != '') {
                    if (!validarNumericoDecimalPrecision(horas, 8, 2)) {
                        mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "identificacionesp.msg.horas.errDecimal")%>';
                        return false;
                    }
                }

                var alumnos = document.getElementById('alumnos');
                if (alumnos != null && alumnos.value != '') {
                    if (!validarNumerico(alumnos)) {
                        mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.errorCampNumerico")%>';
                        return false;
                    }
                }
                return correcto;
            }

        </script>
    </head>
    <body class="bandaBody">
        <div >
            <!--form-->  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.nuevaIdentificacionEsp")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.cpespecialidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codcp" name="codcp" type="text" style="background:#dddddd" class="inputTexto" size="15" maxlength="12" readonly="true"
                                   value="<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp(): ""%>" />
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.denomespecialidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="denomesp" name="denomesp" style="background:#dddddd" type="text" class="inputTexto" size="90" maxlength="100" readonly="true"
                               value="<%=datModif != null && datModif.getDenomEsp() != null ? datModif.getDenomEsp(): ""%>" />
                    </div>
                </div>
                <br>
                <div class="lineaFormulario">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.horas")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="horas" name="horas" type="text" class="inputTexto" size="15" maxlength="9" 
                                       value="<%=datModif != null && datModif.getHoras() != null ? datModif.getHoras().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.alumnos")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="alumnos" name="alumnos" type="text" class="inputTexto" size="15" maxlength="6" 
                                       value="<%=datModif != null && datModif.getAlumnos() != null ? datModif.getAlumnos() : ""%>" />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="lineaFormulario">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.alumnosaut")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="alumnosAut" name="alumnosAut" type="text" class="inputTexto" size="15" maxlength="6" 
                                       value="<%=datModif != null && datModif.getAlumnosAut() != null ? datModif.getAlumnosAut() : ""%>" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.certpro")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.si")%>
                                <input id="certprosi" name="certpro" type="radio" class="inputTexto"   
                                       value="<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.si")%>" />
                                <br>
                            </div>
                            <div style="float: left;">
                                <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.no")%>
                                <input id="certprono" name="certpro" type="radio" class="inputTexto" 
                                       value="<%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.no")%>" />
                                <br>
                            </div>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario" style="display: none;">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.realdecreg")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="realdecregu" name="realdecregu" type="text" class="inputTexto" size="50" maxlength="200" 
                                       value="<%=datModif != null && datModif.getRealDecRegu() != null ? datModif.getRealDecRegu(): ""%>" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="display: none;">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta" >
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.boefecpub")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="boefecpub" name="boefecpub" type="text" class="inputTexto" size="50" maxlength="200" 
                                       value="<%=datModif != null && datModif.getBoeFecPub() != null ? datModif.getBoeFecPub(): ""%>" />
                            </div>
                        </div>
                    </div>
                </div>
                <br><br>
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px; display: none;">
                    <span>
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.tituloadaptabilidaddiscapacitados")%>
                    </span>
                </div>
                <br>    
                <div class="lineaFormulario" style="display: none;">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.descripcionadaptabilidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="5" cols="90" id="descripadapt" name="descripadapt" maxlength="500"><%=datModif != null && datModif.getDescripAdapt() != null ? datModif.getDescripAdapt()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="display: none;">
                    <div>
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.identificacionesp.observacionesadaptabilidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="5" cols="90" id="observadapt" name="observadapt" maxlength="500"><%=datModif != null && datModif.getObservAdapt() != null ? datModif.getObservAdapt()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="botonera">
                    <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" style="display: none;" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="volver();">
                </div>
            </div>
            <!--/form-->
        </div>
        <script type="text/javascript">
            selectRadioSiNo();
            function selectRadioSiNo() {
                var checked = '<%=datModif.getCertPro()%>';
                if (checked == 0)
                    document.getElementById('certprosi').checked = true;
                else if (checked == 1)
                    document.getElementById('certprono').checked = true;
                else {
                    document.getElementById('certprono').checked = false;
                    document.getElementById('certprono').checked = false;
                }
            }
        </script>
    </body>
</html>


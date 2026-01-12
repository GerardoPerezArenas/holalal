<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriUbicValoracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MeLanbideConvocatorias"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
    <head>
        <%
            OriUbicValoracionVO ubicModif = new OriUbicValoracionVO();
            String codOrganizacion = "";
            String numExpediente = "";
            int anionumExpediente = 0;
            String nuevo = "1";
            String codEntidad = "";
            String convocatoria = "";
            int mesesSol = 0;
            int mesesVal = 0;
            int convActiva = 0;
            MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            OriUbicValoracionVO ubicVal = new OriUbicValoracionVO();
            Integer ejercicio    = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            String codProcedimiento    = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
            MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
            String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
            String idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? String.valueOf(convocatoriaActiva.getId()) : "");
        
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
                anionumExpediente    = numExpediente != null && numExpediente != "" ? Integer.parseInt(numExpediente.substring(0,4)):0;
                //convocatoria = (String)request.getAttribute("nuevaCon");
                //nuevaCon = (convocatoria!=null?Integer.parseInt(convocatoria):-1);
                convActiva = (convocatoriaActiva != null && convocatoriaActiva.getId()!= null ? convocatoriaActiva.getId(): 4); // Por defecto convocatorias CONV_ANTE-2021
                codEntidad = request.getParameter("codEntidad");

                if(request.getAttribute("ubicVal") != null)
                {
                    ubicModif = (OriUbicValoracionVO)request.getAttribute("ubicVal");
                }
            }
            catch(Exception ex)
            {
                
            }
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/oriGestionValidarValorarUbicacion.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

        <script type="text/javascript">


            var mensajeValidacion = '';
            var anioNumExpeJavaScript = "<%=anionumExpediente%>";
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
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    parametros = "tarea=preparar&modulo=MELANBIDE47&operacion=valorarAmbitoORI&tipo=0&numero=<%=numExpediente%>&nuevaCon=<%=convActiva%>"
                            + "&codEntidad=<%=codEntidad != null ? codEntidad : ""%>"
                            + "&idUbic=<%=ubicModif != null && ubicModif.getOriOrientUbicCod() != null ? ubicModif.getOriOrientUbicCod().toString() : ""%>"
                            + "&despachos=" + (document.getElementById('numDespachosValid') != null && document.getElementById('numDespachosValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
                            + "&aulaGrupal=" + (document.getElementById('aulaGrupalValid') != null && document.getElementById('aulaGrupalValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
                            + "&trayVal=" + document.getElementById('trayectoriaValor').value
                            + "&ubicVal=" + document.getElementById('ubicacionValor').value
                            + "&despVal=" + (document.getElementById('despachosValor') != null ? document.getElementById('despachosValor').value : "")
                            + "&aulaVal=" + (document.getElementById('aulasValor') != null ? document.getElementById('aulasValor').value : "")
                            + "&puntuacion=" + document.getElementById('totalValoracion').value
                            + "&observaciones=" + escape(document.getElementById('observaciones').value)
                            + "&disp1EspaAdicional=" + (document.getElementById('disp1EspaAdicionalValid') != null && document.getElementById('disp1EspaAdicionalValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
                            + "&dispEspaOrdeIntWifi=" + (document.getElementById('dispEspaOrdeIntWifiValid') != null && document.getElementById('dispEspaOrdeIntWifiValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
                            + "&disp1EspaAdicionalValor=" + (document.getElementById('disp1EspaAdicionalValor') != null ? document.getElementById('disp1EspaAdicionalValor').value : "")
                            + "&dispEspaOrdeIntWifiValor=" + (document.getElementById('dispEspaOrdeIntWifiValor') != null ? document.getElementById('dispEspaOrdeIntWifiValor').value : "")
                            + "&localPreviamenteAprobadoVAL=" + (document.getElementById('localPreviamenteAprobadoVAL') != null && document.getElementById('localPreviamenteAprobadoVAL').checked ? "1" : "0")
                            + "&mantenimientoRequisitosLPAVAL=" + (document.getElementById('mantenimientoRequisitosLPAVAL') != null && document.getElementById('mantenimientoRequisitosLPAVAL').checked ? "1" : "0")
                            + "&localPreviamenteAprobadoValoracion=" + (document.getElementById('localPreviamenteAprobadoValoracion') != null ? document.getElementById('localPreviamenteAprobadoValoracion').value : "")
                            + "&mantenimientoRequisitosLPAValoracion=" + (document.getElementById('mantenimientoRequisitosLPAValoracion') != null ? document.getElementById('mantenimientoRequisitosLPAValoracion').value : "")
                            ;
                    if (<%=convActiva%> > 4) {
                        parametros += "&planIgualdad=" + (document.getElementById('dispPlanIgualdad') != null && document.getElementById('dispPlanIgualdad').checked ? 1 : 0)
                                + "&certCalidad=" + (document.getElementById('dispCertCalidad') != null && document.getElementById('dispCertCalidad').checked ? 1 : 0)
                                + "&planIgualdadVal=" + (document.getElementById('dispPlanIgualdadValid') != null && document.getElementById('dispPlanIgualdadValid').checked ? 1 : 0)
                                + "&certCalidadVal=" + (document.getElementById('dispCertCalidadValid') != null && document.getElementById('dispCertCalidadValid').checked ? 1 : 0)
                                + "&planIgualdadValoracion=" + (document.getElementById('planIgualdadValoracion') != null ? document.getElementById('planIgualdadValoracion').value : "")
                                + "&certCalidadValoracion=" + (document.getElementById('certCalidadValoracion') != null ? document.getElementById('certCalidadValoracion').value : "")
                                ;
                    }
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
                        var listaUbicaciones = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaUbicaciones[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                          
                            else if (hijos[j].nodeName == "CODIGO_ENTIDAD") {
                                listaUbicaciones[j] = hijos[j].childNodes[0].nodeValue;
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
                                    } else if (hijosFila[cont].nodeName == "PROVINCIA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "AMBITO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "MUNICIPIO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DIRECCION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "COD_POSTAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "HORAS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESPACHOS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "AULAGRUPAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "VALORACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TOTAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "HORASADJ") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TELEFIJO_UBICACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESPACIOADICIONA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESPHERRABUSQEMP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[14] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_LOCALPREVAPRO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[15] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_MATENREQ_LPA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[16] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_ORIENT_NUMERO_UBICACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[17] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_ORIENT_PISO_UBICACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[18] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_ORIENT_LETRA_UBICACION") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[19] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_PLANIGUALDAD") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[20] = '';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ORI_CERTCALIDAD") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[21] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[21] = '';
                                        }
                                    }
                                }
                                listaUbicaciones[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar(listaUbicaciones);
                            jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";

                var correcto = true;



                var observaciones = document.getElementById('observaciones').value;
                if (!comprobarCaracteresEspecialesOri14(observaciones)) {
                    document.getElementById('observaciones').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.valorarAmbito.observacionesCaracteresEspeciales")%>';
                    return false;
                } else {
                    document.getElementById('observaciones').removeAttribute("style");
                }

                return correcto;
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

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

            function inicio() {


                var traySol = '<%=ubicModif != null && ubicModif.getOriEntTrayectoria() != null ? ubicModif.getOriEntTrayectoria() : "0"%>';
                document.getElementById('trayectoriaSol').value = traySol;

                var trayVal = '<%=ubicModif != null && ubicModif.getOriEntTrayectoriaVal() != null ? String.valueOf(ubicModif.getOriEntTrayectoriaVal()).replace(".",",") : "0"%>';
                document.getElementById('trayectoriaValid').value = trayVal;



                var ubicSol = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacion() != null ? ubicModif.getOriUbicPuntuacion() : "0"%>';
                document.getElementById('ubicacionSol').value = ubicSol;

                var ubicVal = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacionVal() != null ? ubicModif.getOriUbicPuntuacionVal() : "0"%>';
                document.getElementById('ubicacionValid').value = ubicVal;

            <%
                if(anionumExpediente<2017){
                    if(ubicModif != null && ubicModif.getOriOrientDespachos() != null && ubicModif.getOriOrientDespachos().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
            %>
                document.getElementById('numDespachosSol').checked = true;
            <%
                    }
            %>

            <%
                    if(ubicModif != null && ubicModif.getOriOrientDespachosValidados() != null && ubicModif.getOriOrientDespachosValidados().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
            %>
                document.getElementById('numDespachosValid').checked = true;
            <%
                    }
            %>

            <%
                    if(ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
            %>
                document.getElementById('aulaGrupalSol').checked = true;
            <%
                    }
            %>

            <%
                    if(ubicModif != null && ubicModif.getOriOrientAulaGrupalValidada() != null && ubicModif.getOriOrientAulaGrupalValidada().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
            %>
                document.getElementById('aulaGrupalValid').checked = true;
            <%
                    }
                } else {
                    if(ubicModif != null && ubicModif.getOriOrientUbicaEspacioAdicional() != null && ubicModif.getOriOrientUbicaEspacioAdicional().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
            %>
                document.getElementById('disp1EspaAdicional').checked = true;
            <%
                        }
            %>

            <%
                        if(ubicModif != null && ubicModif.getOriOrientUbicaEspacioAdicionalVal() != null && ubicModif.getOriOrientUbicaEspacioAdicionalVal().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                        {
            %>
                document.getElementById('disp1EspaAdicionalValid').checked = true;
            <%
                        }
            %>

            <%
                        if(ubicModif != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo() != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                        {
            %>
                document.getElementById('dispEspaOrdeIntWifi').checked = true;
            <%
                        }
            %>

            <%
                        if(ubicModif != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal() != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                        {
            %>
                document.getElementById('dispEspaOrdeIntWifiValid').checked = true;
            <%
                        }
            %>

            <%
                    if(anionumExpediente>=2018){
                        if(ubicModif != null && ubicModif.getOriCELocalPreviamenteAprobado() != null && ubicModif.getOriCELocalPreviamenteAprobado()==1)
                            {
            %>
                if (document.getElementById('localPreviamenteAprobado') != null)
                    document.getElementById('localPreviamenteAprobado').checked = true;
            <%
                            }
            %>
            <%
                            if(ubicModif != null && ubicModif.getOriCEMantenimientoRequisitosLPA() != null && ubicModif.getOriCEMantenimientoRequisitosLPA()==1)
                            {
            %>
                if (document.getElementById('mantenimientoRequisitosLPA') != null)
                    document.getElementById('mantenimientoRequisitosLPA').checked = true;
            <%
                            }
                        if(ubicModif != null && ubicModif.getOriCELocalPreviamenteAprobadoVAL() != null && ubicModif.getOriCELocalPreviamenteAprobadoVAL()==1)
                            {
            %>
                if (document.getElementById('localPreviamenteAprobadoVAL') != null)
                    document.getElementById('localPreviamenteAprobadoVAL').checked = true;
            <%
                            }
            %>
            <%
                            if(ubicModif != null && ubicModif.getOriCEMantenimientoRequisitosLPAVAL() != null && ubicModif.getOriCEMantenimientoRequisitosLPAVAL()==1)
                            {
            %>
                if (document.getElementById('mantenimientoRequisitosLPAVAL') != null)
                    document.getElementById('mantenimientoRequisitosLPAVAL').checked = true;
            <%
                            }
                    }
            %>
            <%
                    if (convActiva > 4){
                        if(ubicModif != null && ubicModif.getOriEntPlanIgualdad() != null && ubicModif.getOriEntPlanIgualdad()==1)
                            {
            %>
                if (document.getElementById('dispPlanIgualdad') != null)
                    document.getElementById('dispPlanIgualdad').checked = true;
            <%
                            }
                        if(ubicModif != null && ubicModif.getOriEntPlanIgualdadVal() != null && ubicModif.getOriEntPlanIgualdadVal()==1) 
                            {
            %>
                if (document.getElementById('dispPlanIgualdadValid') != null)
                    document.getElementById('dispPlanIgualdadValid').checked = true;
            <%
                            }
if(ubicModif != null && ubicModif.getOriEntCertCalidad() != null && ubicModif.getOriEntCertCalidad()==1)
                            {
            %>
                if (document.getElementById('dispCertCalidad') != null)
                    document.getElementById('dispCertCalidad').checked = true;
            <%
                            }
                        if(ubicModif != null && ubicModif.getOriEntCertCalidadVal() != null && ubicModif.getOriEntCertCalidadVal()==1) 
                            {
            %>
                if (document.getElementById('dispCertCalidadValid') != null)
                    document.getElementById('dispCertCalidadValid').checked = true;
            <%
                            }
                    }
            %>

            <%       
                }%>

                // Pasamos estas llamadas al JS 16/10/2023
                //rellenarVacios();
                //calcularValoracion();
            }
        </script>
    </head>
    <div class="contenidoPantalla" style="overflow-y: scroll;">  <!--onload="inicio();" -->
        <div style="width: 100%; padding: 10px; text-align: left;">
            <div class="tituloAzul" style="clear: both; text-align: left;" >
                <span>
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.datosUbicacion")%>
                </span>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.territorioHistorico")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getDescProvincia() != null ? ubicModif.getDescProvincia() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.ambito")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getDescAmbito() != null ? ubicModif.getDescAmbito() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.municipio")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getDescMunicipio() != null ? ubicModif.getDescMunicipio() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.direccion")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getDireccion() != null ? ubicModif.getDireccion() : ""%>
                        <%=ubicModif != null && ubicModif.getDireccionNumero() != null ? " " + ubicModif.getDireccionNumero() : ""%>
                        <%=ubicModif != null && ubicModif.getDireccionPiso() != null ? " " + ubicModif.getDireccionPiso() : ""%>
                        <%=ubicModif != null && ubicModif.getDireccionLetra() != null ? " " + ubicModif.getDireccionLetra() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.codPostal")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getCodPostal() != null ? ubicModif.getCodPostal() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 170px; float: left;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.bloquesSolicitados")%>
                </div>
                <div style="width: 255px; float: left;">
                    <div style="float: left;">
                        <%=ubicModif != null && ubicModif.getHorasSolic() != null ? ubicModif.getHorasSolic() : ""%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div class="form-row">
                    <div class="col-sm-6"></div>
                    <div class="col-sm-2 formControlCentrado">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.solicitud")%>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.validado")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div class="form-row">
                    <% if(convActiva > 4){ %>
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.meses")%>
                        </div>
                    <% } else { %>
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.trayectoria")%>
                        </div>
                    <% } %>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="trayectoriaSol" name="trayectoriaSol" type="text" class="inputTexto inputTextRight" size="5" maxlength="5" value="" disabled="disabled"/>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="trayectoriaValid" name="trayectoriaValid" type="text" class="inputTexto inputTextRight" size="5" maxlength="5" value="" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div class="form-row">
                    <div class="col-sm-6">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.ubicacion")%>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="ubicacionSol" name="ubicacionSol" type="text" class="inputTexto inputTextRight" size="5" maxlength="5" value="" disabled="disabled"/>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="ubicacionValid" name="ubicacionValid" type="text" class="inputTexto inputTextRight" size="5" maxlength="5" value="" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <% if(anionumExpediente<2017){ %>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispEspComp")%>
                        </div>
                         <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="numDespachosSol" name="numDespachosSol"  disabled="true" readonly="true"></input>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="numDespachosValid" name="numDespachosValid" onclick="calcularValoracion();"></input>
                        </div>
                    </div>
                </div>
                 <div class="lineaFormulario">
                        <div class="form-row">
                            <div class="col-sm-6">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispAulaGrupal")%>
                            </div>
                            <div class="col-sm-2 formControlCentrado">
                                <input type="checkBox" id="aulaGrupalSol" name="aulaGrupalSol"  disabled="true" readonly="true"></input>
                            </div>
                            <div class="col-sm-2 formControlCentrado">
                                <input type="checkBox" id="aulaGrupalValid" name="aulaGrupalValid" onclick="calcularValoracion();"></input>
                            </div>
                        </div>
                </div>
            <% } else{ %>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.disp1EspaAdicional")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="disp1EspaAdicional" name="disp1EspaAdicional"  disabled="true" readonly="true"></input>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="disp1EspaAdicionalValid" name="disp1EspaAdicionalValid" onclick="calcularValoracion();"></input>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispEspaOrdeIntWifi")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="dispEspaOrdeIntWifi" name="dispEspaOrdeIntWifi"  disabled="true" readonly="true"></input>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="dispEspaOrdeIntWifiValid" name="dispEspaOrdeIntWifiValid" onclick="calcularValoracion();"></input>
                        </div>
                    </div>
                </div>
            <% }%>
            <% if(anionumExpediente>=2018){ %>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.nuevoAmbito.localpreviamenteaprobado")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="localPreviamenteAprobado" name="localPreviamenteAprobado"  disabled="true" readonly="true"></input>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <div style="dispay:none;">
                                <input type="checkBox" id="localPreviamenteAprobadoVAL" name="localPreviamenteAprobadoVAL" onclick="calcularValoracion();"></input>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.nuevoAmbito.mantienerequisitosLPA")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input type="checkBox" id="mantenimientoRequisitosLPA" name="mantenimientoRequisitosLPA"  disabled="true" readonly="true"></input>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <div style="dispay:none;">
                                <input type="checkBox" id="mantenimientoRequisitosLPAVAL" name="mantenimientoRequisitosLPAVAL" onclick="calcularValoracion();"></input>
                            </div>
                        </div>
                    </div>
                </div>

                <c:if test="${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id > 4}">
                    <c:choose>
                        <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='ORI' && convocatoriaActiva.id < 30}">
                            <div class="lineaFormulario">
                                 <div class="form-row">
                                    <div class="col-sm-6">
                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispPlanIgualdad")%>
                                    </div>
                                    <div class="col-sm-2 formControlCentrado">
                                        <input type="checkBox" id="dispPlanIgualdad" name="dispPlanIgualdad"  disabled="true" readonly="true" />
                                    </div>
                                    <div class="col-sm-2 formControlCentrado">
                                        <input type="checkBox" id="dispPlanIgualdadValid" name="dispPlanIgualdadValid"  disabled="true" readonly="true" />
                                    </div>
                                 </div>
                            </div>
                            <div class="lineaFormulario">
                                <div class="form-row">
                                    <div class="col-sm-6">
                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispCertCalidad")%>
                                    </div>
                                    <div class="col-sm-2 formControlCentrado">
                                        <input type="checkBox" id="dispCertCalidad" name="dispCertCalidad"  disabled="true" readonly="true"></input>
                                    </div>
                                    <div class="col-sm-2 formControlCentrado">
                                        <input type="checkBox" id="dispCertCalidadValid" name="dispCertCalidadValid" disabled="true" readonly="true" />
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="lineaFormulario">
                                <div class="form-row" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.compromiso.igualdad")%>">
                                    <div class="col-sm-6"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%></div>
                                    <div class="col-sm-2 formControlCentrado" style="text-align: center;">
                                        <input class="form-control " type="text" id="compromisoIgualdadGenero" readonly disabled
                                            title="<c:if test="${ubicVal!=null}"><c:out value="${ubicVal.compIgualdadOpcionLiteral}"></c:out></c:if>"
                                            value="<c:if test="${ubicVal!=null}"><c:out value="${ubicVal.compIgualdadOpcionLiteral}"></c:out></c:if>"/>
                                    </div>
                                    <div class="col-sm-2 formControlCentrado" style="text-align: center;">
                                        <input class="form-control" type="checkbox" id="validarCompromisoIgualdadGenero"
                                            value="<c:if test="${ubicVal!=null && ubicVal.compIgualdadOpcionVal!=null && ubicVal.compIgualdadOpcionVal>0}"><c:out value="${ubicVal.compIgualdadOpcionVal}"></c:out></c:if>"
                                            readonly disabled <c:if test="${ubicVal!=null && ubicVal.compIgualdadOpcionVal!=null && ubicVal.compIgualdadOpcionVal>0}">checked</c:if>/>
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div class="form-row" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.certificados.calidad")%>">
                                    <div class="col-sm-6"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%></div>
                                    <div class="col-sm-2 formControlCentrado"></div>
                                    <div class="col-sm-2 formControlCentrado"></div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidad" varStatus="contadorCC">
                                    <div class="form-row" title="<c:out value = "${certificadoCalidad.label}"/>">
                                        <div class="col-sm-6"><div style="margin-left:15px;"><li><c:out value = "${certificadoCalidad.label}"/></li></div></div>
                                        <div class="col-sm-2 formControlCentrado" style="text-align: center;align-self:center;"><input class="form-control formControlCentrado" type="checkbox" id="certificadoCalidad_<c:out value="${certificadoCalidad.id}"/>" readonly disabled value="<c:out value="${certificadoCalidad.id}"/>"/></div>
                                        <div class="col-sm-2 formControlCentrado" style="text-align: center;align-self:center;"><input class="form-control formControlCentrado" type="checkbox" id="validarCertificadoCalidad_<c:out value="${certificadoCalidad.id}"/>" readonly disabled/></div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            <%}%>
            <div class="lineaFormularioFont badge badge-info" style="text-align: center; margin:20px 0px 20px 0px;">
                <span>
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.valoracion")%>
                </span>
            </div>
            <div class="lineaFormulario">
                <div class="form-row">
                    <% if(convActiva > 4){ %>
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.trayectoria21")%>
                        </div>
                    <% } else { %>
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.trayectoria")%>
                        </div>
                    <% } %>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="trayectoriaValor" name="trayectoriaValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                               value="" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div class="form-row">
                    <div class="col-sm-6">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.ubicacion")%>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="ubicacionValor" name="ubicacionValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                               value="" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <% if(anionumExpediente<2017){ %>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.despachosExtra")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="despachosValor" name="despachosValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                     <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.aulasExtra")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="aulasValor" name="aulasValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
            <% } else{%>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.disp1EspaAdicional")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="disp1EspaAdicionalValor" name="disp1EspaAdicionalValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispEspaOrdeIntWifi")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="dispEspaOrdeIntWifiValor" name="dispEspaOrdeIntWifiValor" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
            <% }%>
            <% if(anionumExpediente>=2018){ %>
                <div class="lineaFormulario" style="dispay:none;">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.nuevoAmbito.localpreviamenteaprobado")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="localPreviamenteAprobadoValoracion" name="localPreviamenteAprobadoValoracion" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario" style="dispay:none;">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.nuevoAmbito.mantienerequisitosLPA")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="mantenimientoRequisitosLPAValoracion" name="mantenimientoRequisitosLPAValoracion" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
            <% }
            if (convActiva > 4){ %>
                <div class="lineaFormulario">
                    <div class="form-row">
                        <div class="col-sm-6">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispPlanIgualdad")%>
                        </div>
                        <div class="col-sm-2 formControlCentrado">
                            <input id="planIgualdadValoracion" name="planIgualdadValoracion" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                   value="<c:if test="${ubicVal!=null}"><c:out value="${ubicVal.oriEntPlanIgualdadValoracion}"></c:out></c:if>" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                    <div class="lineaFormulario">
                        <div class="form-row">
                            <div class="col-sm-6">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispCertCalidad")%>
                            </div>
                            <div class="col-sm-2 formControlCentrado">
                                <input id="certCalidadValoracion" name="certCalidadValoracion" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                                       value="" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
            <% }%>
            <div class="lineaFormulario">
                <div class="form-row">
                    <div class="col-sm-6">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.total")%>
                    </div>
                    <div class="col-sm-2 formControlCentrado">
                        <input id="totalValoracion" name="totalValoracion" type="text" class="inputTexto inputTextRight" size="5" maxlength="5"
                               value="" disabled="disabled"/>
                    </div>
                </div>
            </div>
            <div class="tituloAzul" style="clear: both; text-align: left;">
                <span>
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"ori.label.observaciones")%>
                </span>
            </div>
            <div>
                <textarea rows="4" cols="50" id="observaciones" name="observaciones" maxlength="500"><%=ubicModif != null && ubicModif.getOriOrientObservaciones() != null && !ubicModif.getOriOrientObservaciones().equals("") ? ubicModif.getOriOrientObservaciones() : ""%></textarea>
            </div>
            &nbsp
            <div class="botonera">
                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();">
                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
            </div>
        </div>
        <!-- Datos comunes trabajar desde JS -->
        <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
        <input type="hidden" id="ejercicio" name="ejercicio" value="<%=ejercicio%>"/>
        <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
        <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
        <input type="hidden" id="urlBaseLlamadaM47" name="urlBaseLlamadaM47" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
        <input type="hidden" id="codigoConvocatoriaExpediente" name="codigoConvocatoriaExpediente" value="<%=codigoConvocatoriaExpediente%>"/>
        <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>
        <input type="hidden" id="idBDEntidad" name="idBDEntidad" value="<%=codEntidad%>"/>
        <input type="hidden" id="idBDOriUbicValoracionVO" name="idBDOriUbicValoracionVO" value="<c:if test="${ubicVal!=null && ubicVal.oriOrientUbicCod!=null}"><c:out value="${ubicVal.oriOrientUbicCod}"/></c:if>"/>
    </div>
    </div>
    <script type="text/javascript">
        inicio();
    </script>
</html>

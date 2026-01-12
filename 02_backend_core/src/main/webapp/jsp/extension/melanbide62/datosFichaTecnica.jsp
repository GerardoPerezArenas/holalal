<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoTramiteVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoExpedienteVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide62.i18n.MeLanbide62I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide62.util.ConstantesMeLanbide62" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String sIdioma = request.getParameter("idioma");
            int idiomaUsuario = Integer.parseInt(sIdioma);
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
            MeLanbide62I18n meLanbide62I18n = MeLanbide62I18n.getInstance();
    
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide62/melanbide62.css"/>
        <style type="text/css">
            <!--
            p.sencillo, div.sencillo {
                margin-top: -8px;
            }
            -->
        </style>


        <script type="text/javascript">
            var tabListaPagos;
            var listaPagos = new Array();
            var listaPagosTabla = new Array();

            <%  		
                InfoTramiteVO ocurrencia = null;        
        
                InfoExpedienteVO datosExp = (InfoExpedienteVO) request.getAttribute("datosFicha");
                ArrayList<InfoTramiteVO> datosFicha = (ArrayList<InfoTramiteVO>) datosExp.getListaTramites();
                if (datosFicha!= null && datosFicha.size() >0){
                    int iter = 0;
                    for (int i = 0; i <datosFicha.size(); i++) {
                        ocurrencia = datosFicha.get(i);
                
            %>
            listaPagos[<%=i%>] = [
                '<%=datosExp.getNumExpediente()%>', '<%=ocurrencia.getCodTramite()%>',
                '<%=ocurrencia.getOcurrencia()%>', '<%=ocurrencia.getfSolPagoStr()%>', '<%=ocurrencia.getfDesdePagoStr()%>',
                '<%=ocurrencia.getfHastaPagoStr()%>', '<%=ocurrencia.getImportePago()%>', '<%=ocurrencia.getRefIkusPago()%>'
            ];
            listaPagosTabla[<%=i%>] = [
                '<%=ocurrencia.getOcurrencia()%>', '<%=ocurrencia.getfSolPagoStr()%>', '<%=ocurrencia.getfDesdePagoStr()%>',
                '<%=ocurrencia.getfHastaPagoStr()%>', '<%=ocurrencia.getDiasSolPagoStr()%>',
                '<%=ocurrencia.getImportePagoFormat()%>', '<%=ocurrencia.getRefIkusPago()%>'
            ];
            <%
                        iter++;
                    }// for
            %>
            listaPagosTabla[<%=iter%>] = ['', '', '', '<b><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.etiquetaTotal")%>:</b>', '<b><%=datosExp.getDiasTotalSolicitud()%></b>', '', ''];
            <%
                }// if
            %>

            function pulsarGrabarObs() {
                var ajax = getXMLHttpRequest();
                var valor = document.forms[0].observ.value;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = 'tarea=preparar&modulo=MELANBIDE62&operacion=grabarCampoObservaciones&tipo=0'
                        + '&expediente=<%=numExpediente%>&organizacion=<%=codOrganizacion%>&observaciones=' + valor;

                // Deshabilito el botón antes de enviar la petición al servidor
                deshabilitarGeneral([document.forms[0].cmdGrabarObs]);
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var codigoOperacion = ajax.responseText;
                        // Habilito el botón cuando el servidor ha terminado
                        habilitarGeneral([document.forms[0].cmdGrabarObs]);
                        mostrarMensaje(codigoOperacion);
                    } else {//if (ajax.readyState==4 && ajax.status==200) 
                        // Habilito el botón cuando el servidor ha terminado
                        habilitarGeneral([document.forms[0].cmdGrabarObs]);
                        mostrarMensaje("3");
                    }
                } catch (Err) {
                    // Habilito el botón si ha habido un error de comunicación cliente-servidor
                    habilitarGeneral([document.forms[0].cmdGrabarObs]);
                    mostrarMensaje("3");
                }//try-catch
            }

            function mostrarMensaje(codigo) {
                if (codigo == "0") {
                    jsp_alerta("A", '<%=meLanbide62I18n.getMensaje(idiomaUsuario,"mens.operacionSucc")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide62I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide62I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide62I18n.getMensaje(idiomaUsuario,"error.operacionFail")%>');
                }
            }

            //Funcion que se desencadena al modificar los datos del expediente y que recarga los datos de la pestaña
            function recargarDatosFichaTecnica() {
                var ajax = getXMLHttpRequest();

                if (ajax != null) {
                    var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                    var parametros = 'tarea=preparar&modulo=MELANBIDE62&operacion=recargarDatosFichaTecnica&tipo=0'
                            + '&expediente=<%=numExpediente%>&organizacion=<%=codOrganizacion%>';

                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
                    try {
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            var text = ajax.responseText;
                            var datos = JSON.parse(text);
                            if (datos.codProcedimiento != "") {
                                // obtenemos la lista de trámites y si hay, los recorremos para construir la tabla con los datos de cada trámite
                                var datosTram = datos.listaTramites;
                                var diasSolTotal = 0;
                                listaPagos = new Array();
                                listaPagosTabla = new Array();
                                if (datosTram.length > 0) {
                                    var index;
                                    for (index = 0; index < datosTram.length; index++) {
                                        var datoTram = datosTram[index];
                                        listaPagos[index] = [
                                            datos.numExpediente, datoTram.codTramite, datoTram.ocurrencia, datoTram.fSolPago,
                                            datoTram.fDesdePago, datoTram.fHastaPago, datoTram.importePago, datoTram.refIkusPago
                                        ];
                                        var diasSolicitud = datoTram.diasSolicitud;
                                        diasSolTotal += diasSolicitud;
                                        if (datoTram.fDesdePago == null || datoTram.fHastaPago == null)
                                            diasSolicitud = "-";
                                        listaPagosTabla[index] = [
                                            datoTram.ocurrencia, datoTram.fSolPagoStr, datoTram.fDesdePagoStr, datoTram.fHastaPagoStr,
                                            diasSolicitud, datoTram.importePagoFormat, datoTram.refIkusPago
                                        ];
                                    }
                                    listaPagosTabla[index] = ['', '', '', '<b><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.etiquetaTotal")%>:</b>', '<b>' + diasSolTotal + '</b>', '', ''];
                                }
                                tabListaPagos.lineas = listaPagosTabla;
                                tabListaPagos.displayTabla();
                                // Hasta aquí para construir la tabla de trámites, ahora asignamos datos a los otros campos de la pantalla
                                document.forms[0].diassepe.value = datos.diasSepe;
                                document.forms[0].restadias.value = (datos.diasSepe - diasSolTotal);
                                if (datos.fichaTecObserv == null || datos.fichaTecObserv == 'undfined' || datos.fichaTecObserv == "")
                                    document.forms[0].observ.value = "";
                                else
                                    document.forms[0].observ.value = datos.fichaTecObserv;

                                document.getElementById('avisoValidacion').innerHTML = datos.mensajeValidaciones;

                                // Reflejamos los camibios en FECFINSEPE ( = FECINISEPE + DIASSEPE)
                                var campos = datos.camposSuplementarios;
                                if (document.forms[0].FECFINSEPE)
                                    document.forms[0].FECFINSEPE.value = campos.FECFINSEPE;
                            } else
                                mostrarMensaje("3");
                        }//if (ajax.readyState==4 && ajax.status==200)
                    } catch (Err) {
                        alert("Error.descripcion: " + Err.description);
                    }//try-catch
                }//if(ajax!=null)
            }//recargarDatosPagina
        </script>
    </head>
    <body>
        <div class="tab-page" id="tabPage621" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana621"><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
            <script type="text/javascript">var tp1_p621 = tp1.addTabPage(document.getElementById("tabPage621"));</script>
            <div style="clear: both;">
                <div id="listaPagos" style="padding: 5px; text-align: center; margin:0px;margin-top:0px;" align="center"><!--width:930px; height: 300px;-->
                </div>
                <br/>
                <div style="text-align: left; margin-left: 50px" class="etiqueta">
                    <p class="sencillo">
                        <label><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.etiqueta1")%>:</label>
                        <input style="vertical-align: middle; border: 0;" id="diassepe" name="diassepe" type="text" class="etiqueta" size="20" value="<%=datosExp.getDiasSepe()%>" readonly="true"/>
                    </p>
                    <p class="sencillo">
                        <label><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.etiqueta2")%>:</label>
                        <input style="vertical-align: middle; border: 0;" id="restadias" name="restadias" type="text" class="etiqueta" size="20" value="<%=datosExp.getDiasRestantes()%>" readonly="true"/>
                    </p>
                    <p class="sencillo">
                        <label><%=meLanbide62I18n.getMensaje(idiomaUsuario,"label.etiqueta3")%>:</label>
                    </p>
                    <p class="sencillo">
                        <textarea style="width: 820px; border: 1px solid #d9d9d9; margin-left: 5px;" class="etiqueta" id="observ" name="observ" rows="7"><%=datosExp.getFichaTecObserv()%></textarea>
                        <input style="float: right; margin: 5px 50px 0 0;" type="button" class="botonLargo" value="<%=meLanbide62I18n.getMensaje(idiomaUsuario,"button.etiquetaGrabar")%>" name="cmdGrabarObs" onclick="pulsarGrabarObs();">
                    </p>
                    <div id="avisoValidacion" class="sencillo" style="color: red; font-weight: bold">
                        <%=(String) request.getAttribute("mensajeValidacion")%>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        tabListaPagos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaPagos'));
        tabListaPagos.addColumna('100', 'left', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col1")%>');
        tabListaPagos.addColumna('130', 'left', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col2")%>');
        tabListaPagos.addColumna('105', 'ledt', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col3")%>');
        tabListaPagos.addColumna('105', 'left', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col4")%>');
        tabListaPagos.addColumna('90', 'left', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col7")%>');
        tabListaPagos.addColumna('100', 'rigth', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col5")%>');
        tabListaPagos.addColumna('160', 'left', '<%=meLanbide62I18n.getMensaje(idiomaUsuario, "label.listaPagos.col6")%>');

        tabListaPagos.height = '250';
        tabListaPagos.displayCabecera = true;
        tabListaPagos.lineas = listaPagosTabla;
        tabListaPagos.displayTabla();
    </script>
</html>
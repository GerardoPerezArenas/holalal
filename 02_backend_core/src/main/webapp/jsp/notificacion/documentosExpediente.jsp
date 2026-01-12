<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.flexia.notificacion.form.NotificacionForm" %>
<%@page import="es.altia.flexia.notificacion.vo.NotificacionVO" %>
<%@page import="es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO" %>

<%@ page import="org.apache.logging.log4j.Logger"%>
<%@ page import="org.apache.logging.log4j.LogManager"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>
    <head>
    <%
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma=1;
        int apl=4;
        int codOrg = 0;
        int codEnt = 1;
        String css = "";
        NotificacionForm formulario = null;
        NotificacionVO notificacion = null;

        if (session.getAttribute("usuario") != null) {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
            codOrg = usuario.getOrgCod();
            css=usuario.getCss();

            formulario = (NotificacionForm) session.getAttribute("notificacionForm");
            notificacion = (NotificacionVO) session.getAttribute("notificacionVO");
        }
        int codNotif= formulario.getCodNotificacion();
        int codTram = formulario.getCodigoTramite();
        int ocuTram = formulario.getOcurrenciaTramite();
        String numExpediente = formulario.getNumExpediente();
    %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
            <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script type="text/javascript">
            var codNoti = <%=codNotif%>;
            var tramite = <%=codTram%>;
            var ocurrencia = <%=ocuTram%>;
            var org = <%=codOrg%>;
            var numExpediente = '<%=numExpediente%>';
            var listaExpedienteSelec='';
            var listaTramiteSelec='';
            var listaExternosSelec = '';

            function crearTablas() {
                tablaSupExpediente = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaSupExpediente'));
                tablaSupExpediente.addColumna('50','center',"");
                tablaSupExpediente.addColumna('360','left',"<%= descriptor.getDescripcion("etiq_NomFichero")%>");
                tablaSupExpediente.addColumna('75','center',"<%= descriptor.getDescripcion("etiq_tipFichero")%>");
                tablaSupExpediente.displayCabecera = true;
                tablaSupExpediente.lineas = listaSupExpedienteTabla;
                tablaSupExpediente.displayTabla();

                tablaSupTramite = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaSupTramite'));
                tablaSupTramite.addColumna('50','center',"");
                tablaSupTramite.addColumna('360','left',"<%= descriptor.getDescripcion("etiq_NomFichero")%>");
                tablaSupTramite.addColumna('75','center',"<%= descriptor.getDescripcion("etiq_tipFichero")%>");
                tablaSupTramite.displayCabecera = true;
                tablaSupTramite.lineas = listaSupTramiteTabla;
                tablaSupTramite.displayTabla();

                tablaOtros = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaOtros'));
                tablaOtros.addColumna('50','center',"");
                tablaOtros.addColumna('360','left',"<%= descriptor.getDescripcion("etiq_NomFichero")%>");
                tablaOtros.addColumna('75','center',"<%= descriptor.getDescripcion("etiq_tipFichero")%>");
                tablaOtros.displayCabecera = true;
                tablaOtros.lineas = listaOtrosTabla;
                tablaOtros.displayTabla();
            }

            function pulsarAnexar() {
                listaExpedienteSelec = '';
                listaTramiteSelec = "";
                listaExternosSelec = "";
                var haySeleccionados = false;

                for (i=0; i < listaSupExpedienteTabla.length; i++)  {
                    var cajaAdjunto = "supExpediente" + i;
                    console.log(cajaAdjunto);
                    if (eval("document.forms[0]." + cajaAdjunto + ".checked") == true) {
                        listaExpedienteSelec += listaSupExpediente[i][3] + '_';
                        haySeleccionados = true;
                    }
                }

                for (i=0; i < listaSupTramiteTabla.length; i++)  {
                    var cajaAdjunto = "supTram" + i;
                    console.log(cajaAdjunto);
                    if (eval("document.forms[0]." + cajaAdjunto + ".checked") == true) {
                        listaTramiteSelec += listaSupTramite[i][3] + '_';
                        haySeleccionados = true;
                    }
                }

                for (i=0; i < listaOtrosTabla.length; i++)  {
                    var cajaAdjunto = "externo" + i;
                    if (eval("document.forms[0]." + cajaAdjunto + ".checked") == true) {
                        listaExternosSelec += listaOtros[i][3] + '_';
                        haySeleccionados = true;
                    }
                }

                if (haySeleccionados) {
                    var parametros = "&codMunicipio=" + org + "&numero=" + numExpediente + "&codTramite=" + tramite + "&ocurrenciaTramite=" + ocurrencia +
                                    "&codNotificacion=" + codNoti + "&listaExpedienteSelec=" + listaExpedienteSelec + "&listaTramiteSelec=" + listaTramiteSelec + "&listaExternosSelec=" + listaExternosSelec;
                    var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=seleccionarDocumentosExpediente" + parametros;

                    self.parent.opener.retornoXanelaAuxiliar(parametros);
                } else {
                    jsp_alerta("A","<%= descriptor.getDescripcion("msgFichNotifNoSeleccionado")%>");
                }

            }


            function pulsarCancelar() {
                self.parent.parent.opener.retornoXanelaAuxiliar();
            }

        </script>
   </head>
    <body class="bandaBody">
        <html:form action="/Notificacion.do" method="post" enctype="multipart/form-data">
            <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiqDocsExp")%></div>
            <div class="contenidoPantalla">
                <div id="divGeneral" style="margin:30px">
                    <div class="sub3titulo" style="clear: both; text-align: left;height:18px; width: 97%;"><%=descriptor.getDescripcion("etiqDocsExp")%></div>
                    <div id="listaSupExpediente" align="center"></div>
                    <div class="sub3titulo" style="clear: both; text-align: left;height:18px; width: 97%;margin-top: 30px;"><%=descriptor.getDescripcion("etiqDocsTramites")%></div>
                    <div id="listaSupTramite" align="center"></div>
                    <div class="sub3titulo" style="clear: both; text-align: left;height:18px; width: 97%;margin-top: 30px;"><%=descriptor.getDescripcion("etiqDocExternos")%></div>
                    <div id="listaOtros" align="center"></div>
                </div>

                <div style="text-align: center;margin-top:20px">
                     <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAnexar")%>" onclick="pulsarAnexar();">
                     <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" onclick="pulsarCancelar();">
                </div>
            </div>
        </html:form>
        <script type="text/javascript">
            var tablaSupExpediente;
            var listaSupExpediente = new Array();
            var listaSupExpedienteTabla = new Array();
            var j=0;
            <%
                AdjuntoNotificacionVO supExpediente = null;
                List<AdjuntoNotificacionVO> listaSupExpediente = null;
                if(request.getAttribute("ListaDocsExpediente") != null) {
                   listaSupExpediente = (List<AdjuntoNotificacionVO>)request.getAttribute("ListaDocsExpediente");
                }
                if (listaSupExpediente != null && listaSupExpediente.size() > 0) {
                    for (int indice=0; indice<listaSupExpediente.size(); indice++) {
                        supExpediente = listaSupExpediente.get(indice);
                        String tipo = "-";
                        if (supExpediente.getContentType()!=null) {
                            tipo = supExpediente.getContentType();
                            if (tipo.equals("application/pdf")) {
                                tipo = "PDF";
                            } else {
                                tipo = "Excell";
                            }
                        }
            %>
                        listaSupExpediente[<%=indice%>]=["<input type='checkbox' name='supExpediente"+<%=indice%>+"'>",'<%=supExpediente.getNombre()%>','<%=supExpediente.getContentType()%>','<%=supExpediente.getOidDokusi()%>'];
                        listaSupExpedienteTabla[<%=indice%>]=["<input type='checkbox' name='supExpediente"+<%=indice%>+"'>",'<%=supExpediente.getNombre()%>','<%=tipo%>'];
            <%
                    } // for
                } // if
            %>

            var tablaSupTramite;
            var listaSupTramite = new Array();
            var listaSupTramiteTabla = new Array();
            var j=0;
            <%
                AdjuntoNotificacionVO supTram = null;
                List<AdjuntoNotificacionVO> listaSupTramite = null;
                if(request.getAttribute("ListaDocsTramite") != null) {
                   listaSupTramite = (List<AdjuntoNotificacionVO>)request.getAttribute("ListaDocsTramite");
                }
                if (listaSupTramite != null && listaSupTramite.size() > 0) {
                    for (int indice=0; indice<listaSupTramite.size(); indice++) {
                        supTram = listaSupTramite.get(indice);
                        String tipo = "-";
                        if (supTram.getContentType()!=null) {
                            tipo = supTram.getContentType();
                            if (tipo.equals("application/pdf")) {
                                tipo = "PDF";
                            } else {
                                tipo = "Excell";
                            }
                        }
            %>
                        listaSupTramite[<%=indice%>]=["<input type='checkbox' name='supTram"+<%=indice%>+"'>",'<%=supTram.getNombre()%>','<%=supTram.getContentType()%>','<%=supTram.getOidDokusi()%>'];
                        listaSupTramiteTabla[<%=indice%>]=["<input type='checkbox' name='supTram"+<%=indice%>+"'>",'<%=supTram.getNombre()%>','<%=tipo%>'];
            <%
                    } // for
                } // if
            %>

            var tablaOtros;
            var listaOtros = new Array();
            var listaOtrosTabla = new Array();
            <%
                AdjuntoNotificacionVO externo = null;
                List<AdjuntoNotificacionVO> listaExternos = null;
                if(request.getAttribute("ListaDocsExternos") != null) {
                   listaExternos = (List<AdjuntoNotificacionVO>)request.getAttribute("ListaDocsExternos");
                }
                if (listaExternos != null && listaExternos.size() > 0) {
                    for (int indice=0; indice<listaExternos.size(); indice++) {
                        externo = listaExternos.get(indice);
                        String tipo = "-";
                        if (externo.getContentType() != null) {
                            tipo = externo.getContentType();
                            if (tipo.equals("application/pdf")) {
                                tipo = "PDF";
                            } else {
                                tipo = "Excell";
                            }
                        }
            %>
                       listaOtros[<%=indice%>]=["<input type='checkbox' name='externo"+<%=indice%>+"'>",'<%=externo.getNombre()%>','<%=externo.getContentType()%>','<%=externo.getOidDokusi()%>'];
                       listaOtrosTabla[<%=indice%>]=["<input type='checkbox' name='externo"+<%=indice%>+"'>",'<%=externo.getNombre()%>','<%=tipo%>'];

            <%
                    } // for
                } // if
            %>
            crearTablas();
        </script>
    </body>
</html:html>
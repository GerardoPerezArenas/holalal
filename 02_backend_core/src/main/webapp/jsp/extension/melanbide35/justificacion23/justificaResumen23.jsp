<%-- 
    Document   : justificaResumen23
    Created on : 21-ene-2025, 11:06:11
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    
    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }
    
//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<script type="text/javascript">
    var totalInsercionesRes = 0;
    var totalSeguimientosRes = 0;
    var totalEcaRes = 0;
    var totalValidadoRes = 0;
    var numPreparadores = 0;
    var numSeguimientos = 0;

    function crearTablaJusResumen() {
        tablaJusResumen = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaJusResumen'));
        tablaJusResumen.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.ID")%>'); // ID
        tablaJusResumen.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.totalInser")%>'); //
        tablaJusResumen.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.totalSegui")%>'); //
        tablaJusResumen.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeEca")%>'); //
        tablaJusResumen.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeValidado")%>'); //

        tablaJusResumen.displayCabecera = true;
        tablaJusResumen.height = 300;
        tablaJusResumen.lineas = listaJusResumenTabla;
        tablaJusResumen.displayTablaConTooltips(listaJusResumenTitulos);
    }
    function recargarTablaJusResumen() {}
</script>
<body>
    <div class="tab-page" id="tabPage3521" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloJusResumen"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.tituloPagina")%></span>
        </div>   
        <fieldset id="resumenJus23" name="resumenJus23">
            <h2 class="legendAzul" id="legendJusResumen" style="text-transform: uppercase;width: 90%;text-align: center;">
                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.tituloPestana")%>
            </h2>
            <div>
                <div id="listaJusResumen" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            </div>
            <fieldset id="totalesJus23" name="totalesJus23" style="margin-top: 15px; border-color: black;">
                <legend class="legendTema" align="center" id="titTotalesJus"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.total")%></legend>
                <div>
                    
                </div>
            </fieldset>
        </fieldset>
    </div>
    <script type="text/javascript">
        var tablaJusResumen;
        var listaJusResumen = new Array();
        var listaJusResumenTabla = new Array();
        var listaJusResumenTitulos = new Array();
        <%
            JustificacionECA23VO justif = null;
            List<JustificacionECA23VO> ListaJusti = null;
            if(request.getAttribute("listaResumenJustificacion")!=null) {
                ListaJusti = (List<JustificacionECA23VO>)request.getAttribute("listaResumenJustificacion");
            }
            
            if (ListaJusti!= null && ListaJusti.size() > 0) { 
                for (int indice=0;indice<ListaJusti.size();indice++) {
                    justif = ListaJusti.get(indice);
                    
                    String totalInser = "";
                    if (justif.getTotalInserciones() != null) {
                        totalInser = formateador.format(justif.getTotalInserciones());
                    }
                    
                    String totalSegui = "";
                    if (justif.getTotalSeguimientos() != null) {
                        totalSegui = formateador.format(justif.getTotalSeguimientos());
                    }
                    
                    String importeTotal = "";
                    if (justif.getImporteEca() != null) {
                        importeTotal = formateador.format(justif.getImporteEca());
                    }
                    
                    String importeValidado = "";
                    if (justif.getImporteValidado() != null) {
                        importeValidado = formateador.format(justif.getImporteValidado());
                    }
        %>
        listaJusResumen[<%=indice%>] = ['<%=justif.getId()%>', '<%=totalInser%>', '<%=totalSegui%>', '<%=importeTotal%>', '<%=importeValidado%>'];
        listaJusResumenTabla[<%=indice%>] = ['<%=justif.getId()%>', '<%=totalInser%>', '<%=totalSegui%>', '<%=importeTotal%>', '<%=importeValidado%>'];
        listaJusResumenTitulos[<%=indice%>] = ['<%=justif.getId()%>', '<%=totalInser%>', '<%=totalSegui%>', '<%=importeTotal%>', '<%=importeValidado%>'];
        <%
                }
            }
        %>
        crearTablaJusResumen();
    </script>
</body>
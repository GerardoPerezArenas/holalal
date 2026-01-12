<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03ReportVO" %>
<%@page import="java.util.ArrayList" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
    
    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    String cargarDatosPDF = (String) request.getAttribute("cargarDatosPDF");
    
    
%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide03/melanbide03.css'/>">

<script type="text/javascript">
    
    var nombresReports = new Array();
    var tipoDocumentoReports = new Array();
    var fechaReports = new Array();
    var listaReportsTabla = new Array();
    
    <% if(cargarDatosPDF.equalsIgnoreCase("S")){ %>
        function cargarDatosPDF(){
            
            var cont = 0;
            <logic:iterate id="report" name="listaReports" scope="request">
                nombresReports[cont] = ['<bean:write name="report" property="nombre" />'];
                tipoDocumentoReports[cont] = ['<bean:write name="report" property="mimeType" />'];
                fechaReports[cont] = ['<bean:write name="report" property="fechaString"/>'];
                listaReportsTabla[cont] = ['',nombresReports[cont],tipoDocumentoReports[cont],fechaReports[cont],''];
                cont++;
            </logic:iterate>
            
            tabDocumentosApa.lineas = listaReportsTabla;
            tabDocumentosApa.displayTabla();
        }//cargarDatosPDF
    <%}%>
        
    function verDocumento(){
        if(tabDocumentosApa.selectedIndex != -1) {
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var nomDoc = listaReportsTabla[tabDocumentosApa.selectedIndex][1];
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "?tarea=procesar&operacion=getReport" + "&modulo=" + escape(nombreModuloLicitacion) + "&codOrganizacion=" + escape(codOrganizacion)
                + "&numero=" + escape(numExpLicitacion) + "&tipo=0&nomDoc=" + nomDoc;
            window.open(url+parametros);
        }else{
            jsp_alerta('A','<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.noReportSelect")%>');
        }//if(tabDocumentosApa.selectedIndex != -1) 
    }//verDocumento
</script>

<div class="tab-page" id="tabPage101" style="height:290px; width: 100%;">
    <h2 class="tab" id="pestana101"><%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.cabeceraPDF")%></h2>
    <script type="text/javascript">tp1_p101 = tp1.addTabPage( document.getElementById( "tabPage101" ) );</script>
    <form action="/PeticionModuloIntegracion.do" method="POST">
        <table width="100%" border="0">
            <tr style="padding-left:10px">
                <td align="center">
                    <table border="0" width="95%" cellspacing="2" cellpadding="2" align="center" class="contenidoPestanha">
                        <tr style="padding-left:10px">
                            <td align="center">
                                <table border="0" width="95%" cellspacing="2" cellpadding="2" align="center" class="contenidoPestanha">
                                    <tr>
                                        <td class="sub3titulo">
                                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.certificadosGenerados")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                                                <tr>
                                                    <td>
                                                        <div id="tabDocumentosApa" class="text" align="left"></div>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="right">
                                                        <input type= "button" class="botonLargo" 
                                                               value="<%=meLanbide03I18n.getMensaje(idiomaUsuario,"boton.verDocumento")%>" 
                                                               name="cmdVerDoc" id="cmdVerDoc" onclick="verDocumento();">
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">  
    //Tabla de documentos APA
    var tabDocumentosApa;
    if(document.all) tabDocumentosApa = new Tabla(document.all.tabDocumentosApa);
    else tabDocumentosApa = new Tabla(document.getElementById('tabDocumentosApa'));
    tabDocumentosApa.addColumna('0','left','');
    tabDocumentosApa.addColumna('582','left','<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.nombreDocumento")%>'); //Nombre
    tabDocumentosApa.addColumna('150','center','<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.tipoDocumento")%>'); //Tipo documento
    tabDocumentosApa.addColumna('150','center','<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.fechaAlta")%>'); //fecha alta
    tabDocumentosApa.addColumna('0','left','');
    tabDocumentosApa.displayCabecera=true;
    tabDocumentosApa.height = 220;
    tabDocumentosApa.displayTabla();
    
    <% if(cargarDatosPDF.equalsIgnoreCase("S")){ %>
        cargarDatosPDF();
    <%}%>
</script>
        
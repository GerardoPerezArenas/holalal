<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.vo.Melanbide03ExpedientesVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide03/melanbide03.css"/>       
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var provinciaCambiada = false;
            var ambitoCambiado = false;

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
                //cargarCombos();
                //cargarCodigosCombos();
                //cargarDescripcionesCombos();*/
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
        </script>

    </head>
    <body onload="inicio();" class="contenidoPantalla">
        <div style="width: 100%; padding: 10px; text-align: left;">
            <div class="tituloAzul" style="clear: both; text-align: left;">
                <span>

                </span>
            </div>
            <div id="tablaMotivo">  
            </div>

        </div>
    </body>
</html>
<script type="text/javascript">
    //Tabla 
    var tabUbicacionesCE;
    var listaUbicacionesCE = new Array();
    var listaUbicacionesCETabla = new Array();

    tabUbicacionesCE = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaMotivo'));
    tabUbicacionesCE.addColumna('200', 'left', "Número expediente");
    tabUbicacionesCE.addColumna('388', 'left', "Motivo exención");

    tabUbicacionesCE.displayCabecera = true;
    tabUbicacionesCE.height = 100;

    <%
        List<Melanbide03ExpedientesVO> ListaExp = new ArrayList<Melanbide03ExpedientesVO>();
        ListaExp = (List<Melanbide03ExpedientesVO>)request.getAttribute("listadoExpedientes");
        if (ListaExp != null && ListaExp.size() > 0) 
        {
            int i;
            Melanbide03ExpedientesVO si = null;
            for (i = 0; i < ListaExp.size() ; i++) 
            {
                si = ListaExp.get(i);
    %>
    listaUbicacionesCETabla[<%=i%>] = ['<%=si.getCodExpediente()%>', '<%=si.getDesMotivo()%>'];
    <%
            }
        }                    
    %>
    tabUbicacionesCE.lineas = listaUbicacionesCETabla;
    tabUbicacionesCE.displayTabla();
</script>
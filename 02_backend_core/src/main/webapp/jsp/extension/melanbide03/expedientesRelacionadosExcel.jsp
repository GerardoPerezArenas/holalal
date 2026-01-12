<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>

<html>
    <head>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>::: Expedientes Relacionados :::</title>
        <%@ include file="/jsp/plantillas/Metas.jsp" %>
        <%
            String COMMA = ",";
            int idioma=1;
            int apl=4;
            String codUsu = "";
            
            UsuarioValueObject usuario=new UsuarioValueObject();
            Log _log = LogFactory.getLog(this.getClass());
            if (session!=null){
              if (usuario!=null) {
                usuario = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                        int cUsu = usuario.getIdUsuario();
                codUsu = Integer.toString(cUsu);
              }
            }
        %>

        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
        <!-- Beans -->
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript">
            function incio() {

                var datosExpedienteAPA = new Array();
                var lineasExpedienteAPA = new Array();
            <%              
                List<ImpresionExpedientesLanbideValueObject> relacionExpedientesAPA = (List<ImpresionExpedientesLanbideValueObject>) request.getAttribute("lista_expedientes_documento");
                int numRelacionExpedientesAPA = 0;
                int i = 0;
                if ( relacionExpedientesAPA != null ) numRelacionExpedientesAPA = relacionExpedientesAPA.size();

            %>
                var j = 0;
                var contador = 0;
                var seleccionado = "false";
            <%
                ImpresionExpedientesLanbideValueObject expedienteAPA = null;
                for(i=0;i<numRelacionExpedientesAPA;i++)
                {
                    expedienteAPA = (ImpresionExpedientesLanbideValueObject)relacionExpedientesAPA.get(i);
            %>

                datosExpedienteAPA[j] = ['<%=(String)expedienteAPA.getNumExpediente()%>', '<%=(String)expedienteAPA.getInteresados()%>', '<%=(String)expedienteAPA.getNumRegistroInicio()%>', '<%=(String)expedienteAPA.getFechaPresentacionRegistroInicio()%>'];
                lineasExpedienteAPA[j] = datosExpedienteAPA[j];
                j++;
                contador++;
            <%
                }
            %>

                tablaExpedientes.lineas = lineasExpedienteAPA;
                refresca(tablaExpedientes);
            }
        </script>

        <style type="text/css">
            TR.rojo TD {
                background-color:red;
                color:white;
            }
            TR.gris TD {
                background-color:white;
                color:#a5a5a5;
            }
        </style>
    </head>
    <body class="bandaBody" onload="javascript:incio();">
        <script type="text/javascript">

            function pulsarSalir() {
                top.close();
            }
        </script>    

        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiqListadoExpedientes")%></div>
        <div class="contenidoPantalla">
            <table class="contenidoPestanha">                                                
                <tr>
                    <td colspan="2">                                                        
                        <div id="divTablaExpedientes"></div>
                    </td>
                </tr>  
            </table>                                
            <div class="botoneraPrincipal">
                <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onclick="pulsarSalir();">
            </div>   
        </div>   
    </body>

    <script type="text/javascript">

        var tablaExpedientes;
        tablaExpedientes = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divTablaExpedientes'));
        tablaExpedientes.addColumna('180', 'center', '<%=descriptor.getDescripcion("gEtiqExpGeneradoFichero")%>');
        tablaExpedientes.addColumna('220', 'center', '<%=descriptor.getDescripcion("gEtiqSolicitante")%>');
        tablaExpedientes.addColumna('120', 'center', '<%=descriptor.getDescripcion("gEtiqAnotRegistro")%>');
        tablaExpedientes.addColumna('120', 'center', '<%=descriptor.getDescripcion("gEtiqFecPresRegistro")%>');
        tablaExpedientes.height = 150;
        tablaExpedientes.displayCabecera = true;

        function refresca(tablaCampoSup) {
            tablaCampoSup.displayTabla();
        }

    </script>
</html>
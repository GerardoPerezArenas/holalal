<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaDocumentoContableVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide61/melanbide61.css'/>">

<script type="text/javascript">
</script>

<body>
    <!--div class="tab-page" id="tabPage292" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana292"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.listadoDocumentos.title")%></h2>
        <script type="text/javascript">tp1_p292 = tp1.addTabPage( document.getElementById( "tabPage292" ) );</script>
        <div style="clear: both;">            
            <fieldset style="width: 97.5%; padding-top: 10px; margin-top: 10px;">
                <legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario, "label.listadoDocumentos")%></legend>
                <div id="docContables" style="width:910px; height: 450px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
            </fieldset>
        </div>
    </div-->
</body>
<script type="text/javascript">    
    var tabDocumentosContables;
    var listaDocumentosContables = new Array();
    var listaDocumentosContablesTabla = new Array();

    tabDocumentosContables = new Tabla(document.getElementById('docContables'), 905);
    tabDocumentosContables.addColumna('160','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col1")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col2")%>");                                                            
    tabDocumentosContables.addColumna('100','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col3")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col4")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col5")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col6")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col7")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col8")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide61I18n.getMensaje(idiomaUsuario,"docContables.tabla.col9")%>");

    tabDocumentosContables.displayCabecera=true;
    tabDocumentosContables.height = 390;
    <%  		
        FilaDocumentoContableVO vo = null;
        List<FilaDocumentoContableVO> listaDocumentosContables = (List<FilaDocumentoContableVO>)request.getAttribute("listaDocContables");													
        if (listaDocumentosContables != null && listaDocumentosContables.size() >0){
            for (int i = 0; i < listaDocumentosContables.size(); i++)
            {
                vo = listaDocumentosContables.get(i);
                
    %>
        listaDocumentosContables[<%=i%>] = ['<%=vo.getRefIntervencion()%>', '<%=vo.getTipoDoc()%>', '<%=vo.getImporte()%>', '<%=vo.getfDocumento()%>', '<%=vo.getfTramitacion()%>', '<%=vo.getfMaqueta()%>', '<%=vo.getNumMaqueta()%>', '<%=vo.getfContab()%>', '<%=vo.getNumApunte()%>'];
        listaDocumentosContablesTabla[<%=i%>] = ['<%=vo.getRefIntervencion()%>', '<%=vo.getTipoDoc()%>', '<%=vo.getImporte()%>', '<%=vo.getfDocumento()%>', '<%=vo.getfTramitacion()%>', '<%=vo.getfMaqueta()%>', '<%=vo.getNumMaqueta()%>', '<%=vo.getfContab()%>', '<%=vo.getNumApunte()%>'];
    <%
            }// for
        }// if
    %>
    tabDocumentosContables.lineas=listaDocumentosContablesTabla;
    tabDocumentosContables.displayTabla();
</script>
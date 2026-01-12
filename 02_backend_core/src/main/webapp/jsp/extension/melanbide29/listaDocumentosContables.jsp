<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.i18n.MeLanbide29I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaDocumentoContableVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

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
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide29I18n meLanbide29I18n = MeLanbide29I18n.getInstance();
    
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
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide29/melanbide29.css'/>">

<script type="text/javascript">
</script>

<body>
    <div class="tab-page" id="tabPage292" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana292"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.listadoDocumentos.title")%></h2>
        <script type="text/javascript">tp1_p292 = tp1.addTabPage( document.getElementById( "tabPage292" ) );</script>
        <div style="clear: both;">            
            <fieldset style="width: 97.5%; padding-top: 10px; margin-top: 10px;">
                <legend class="legendAzul"><%=meLanbide29I18n.getMensaje(idiomaUsuario, "label.listadoDocumentos")%></legend>
                <div id="docContables" align="center"></div>
            </fieldset>
        </div>
    </div>
</body>
<script type="text/javascript">    
    var tabDocumentosContables;
    var listaDocumentosContables = new Array();
    var listaDocumentosContablesTabla = new Array();

    tabDocumentosContables = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('docContables'), 905);
    tabDocumentosContables.addColumna('160','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col1")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col2")%>");                                                            
    tabDocumentosContables.addColumna('100','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col3")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col4")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col5")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col6")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col7")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col8")%>");
    tabDocumentosContables.addColumna('85','right',"<%= meLanbide29I18n.getMensaje(idiomaUsuario,"docContables.tabla.col9")%>");

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

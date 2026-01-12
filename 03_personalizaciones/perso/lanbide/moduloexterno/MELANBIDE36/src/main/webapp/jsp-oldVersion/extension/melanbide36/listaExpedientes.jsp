<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.i18n.MeLanbide36I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.util.MeLanbide36Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
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
    MeLanbide36I18n meLanbide36I18n = MeLanbide36I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    int totDias = 0;
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide36/melanbide36.css'/>">



<script type="text/javascript">
    var tabListaExpedientesRel;
    var listalistaExpedientesRel = new Array();
    var listalistaExpedientesRelTabla = new Array();
    
    <%  		
        FilaExpedienteVO act = null;
        
        
        List<FilaExpedienteVO> expRel = (List<FilaExpedienteVO>)request.getAttribute("listaExpedientesRelacionados");
        if (expRel!= null && expRel.size() >0){
            for (int i = 0; i <expRel.size(); i++)
            {
                act = expRel.get(i);
                totDias += act.getNumDias();
                
    %>
        listalistaExpedientesRel[<%=i%>] = ['<%=act.getNumExpedienteP29()%>', '<%=act.getNumExpediente()%>', '<%=MeLanbide36Utils.integerToFormattedString(act.getNumDias())%>', '<%=MeLanbide36Utils.doubleToFormattedString(act.getImporteAbonado())%>', '<%=act.getSituacion()%>', '<%=act.getTipoSubvencion()%>', '<%=act.getFecNacPersDepen()%>', '<%=act.getFeIni()%>', '<%=act.getFeFin()%>'];
        listalistaExpedientesRelTabla[<%=i%>] = ['<%=act.getNumExpedienteP29()%>', '<%=act.getNumExpediente()%>', '<%=MeLanbide36Utils.integerToFormattedString(act.getNumDias())%>', '<%=MeLanbide36Utils.doubleToFormattedString(act.getImporteAbonado())%>', '<%=act.getSituacion()%>', '<%=act.getTipoSubvencion()%>', '<%=act.getFecNacPersDepen()%>', '<%=act.getFeIni()%>', '<%=act.getFeFin()%>'];
    <%
            }// for
        }// if
    %>
    
</script>
<body>
    <div class="tab-page" id="tabPage361" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana361"><%=meLanbide36I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">var tp1_p361 = tp1.addTabPage( document.getElementById( "tabPage361" ) );</script>
        <div style="clear: both;">
            <div id="listaExpedientesRel" style="padding: 5px; width:930px; height: 450px; text-align: center; margin:0px;margin-top:0px;" align="center">
            </div>
            <div style="clear: both; float: left; width: 930px; text-align: right;">
                <%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.totalDias")%>
                <input type="text" value="<%=MeLanbide36Utils.integerToFormattedString(totDias)%>" size="13" style="margin-left: 5px; text-align: right;" disabled="true"/>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
    
    
    tabListaExpedientesRel = new Tabla(document.getElementById('listaExpedientesRel'), 924);
    tabListaExpedientesRel.addColumna('160','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col1")%>');
    tabListaExpedientesRel.addColumna('140','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col2")%>');
    tabListaExpedientesRel.addColumna('60','right','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col3")%>');
    tabListaExpedientesRel.addColumna('70','right','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col4")%>');
    tabListaExpedientesRel.addColumna('65','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col5")%>');
    tabListaExpedientesRel.addColumna('165','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col6")%>');
    tabListaExpedientesRel.addColumna('70','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col7")%>');
    tabListaExpedientesRel.addColumna('70','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col8")%>');
    tabListaExpedientesRel.addColumna('70','left','<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col9")%>');
    
    tabListaExpedientesRel.lineas=listalistaExpedientesRelTabla;
    
    tabListaExpedientesRel.height = '410';
    
    tabListaExpedientesRel.displayCabecera=true;
    
    tabListaExpedientesRel.displayTabla();
</script>
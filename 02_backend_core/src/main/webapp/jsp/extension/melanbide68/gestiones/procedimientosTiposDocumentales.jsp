<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ProcedimientoVO" %>
<%@page import="java.util.List" %>

<!doctype html>
<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            int idiomaUsuario = 0;
            int apl = 5;
            int codOrganizacion = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");      

            String codProc     = request.getParameter("codProc");
            
            String tituloPagina = "";
            tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.titulo.procedimientoTipoDocuLanbide");
        %>
        <title><%=tituloPagina%></title>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
                <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/JavaScriptUtil.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript">
            function crearTabla() {
                tabProTipDocLanbide = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaProcedimientos'));    
                tabProTipDocLanbide.addColumna('20', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.proDocLanbide.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.proDocLanbide.tabla.col1.title")%>");
                tabProTipDocLanbide.addColumna('100', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.proDocLanbide.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.proDocLanbide.tabla.col2.title")%>");

                tabProTipDocLanbide.displayCabecera = true;
                tabProTipDocLanbide.height = '300';
            }
        </script> 
    </head>
    <body id="cuerpoProcedimientosDocumentales" style="text-align: left;" >
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
            <fieldset id="fieldsetDatosTipDoc" name="fieldsetDatosTipDoc" style="width: 99%;">
                <legend class="legendAzul"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.TipDocLanbide.popupProcedimientos")%></legend>
                <div id="divGeneral" >     
                    <div id="listaProcedimientos" align="center"></div>
                </div> 
            </fieldset>
            <div class="contenidoPantalla">
                <!-- botones debajo de la tabla --->
                <div class="botonera" >
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.cerrar")%>" onclick="cerrarVentana()">
                </div>   
            </div>
        </div>
        <script  type="text/javascript">
            var tabProTipDocLanbide;
            var listaProcedimientos = new Array();
            var listaProcedimientosTabla = new Array();

            crearTabla();
            <% 
            ProcedimientoVO objectVO = null;
            List<ProcedimientoVO> List = null;
            if(request.getAttribute("lstProcedimientos")!=null){
                List = (List<ProcedimientoVO>)request.getAttribute("lstProcedimientos");               
            }	
            
            if (List!= null && List.size() >0){             
                for (int indice=0;indice<List.size();indice++)
                {
                    objectVO = List.get(indice);                
            %>
            listaProcedimientos[<%=indice%>] = ['<%=objectVO.getCodProc()%>', '<%=objectVO.getDescProc()%>'];
            listaProcedimientosTabla[<%=indice%>] = ['<%=objectVO.getCodProc()%>', '<%=objectVO.getDescProc()%>'];
            <%
                }// for
            }// if
            
            %>
                
            tabProTipDocLanbide.lineas = listaProcedimientos;
            tabProTipDocLanbide.displayTablaConTooltips(listaProcedimientosTabla);
            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                try {
                    var div = document.getElementById('listaProcedimientos');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                } catch (err) {
                }
            }

            function cerrarVentana() {
                if (navigator.appName == 'Microsoft Internet Explorer') {
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
    </div>
</body>
</html>
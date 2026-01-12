<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46Utils" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmeOfertaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConstantesMeLanbide46" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <%
        
            
            int idiomaUsuario = 1;
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
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");

            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
            
            String res = request.getParameter("res");
            String error = request.getParameter("error");
    
            String tituloPagina = "";
            
            String fecRes = null;
            String fecNotifRes = null;
            String pConRes = null;
            String impConRes = null;
            String impPPagRes = null;
            String impSPagRes = null;
            String fecPresRes = null;
            String numPRen = null;
            String impPueRen = null;
            
            if(res != null && !res.equals(""))
            {
                if(res.equals("I"))
                {
                    //Es resolucion inicial
                    
                    tituloPagina = meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.titulo.resInicial");
                    
                    fecRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_FEC_RES);
                    fecNotifRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_NOT_RES);
                    pConRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_PUESTOS_CONC);
                    impConRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_CONC);
                    impPPagRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_PRIMER_PAG);
                    impSPagRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_SEG_PAGO);
                    fecPresRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_FEC_RESOL);
                    numPRen = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_NUM_PUESTO);
                    impPueRen = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_RENUN);
                }
                else if(res.equals("M"))
                {
                    //Es resolucion modificatoria
                    
                    tituloPagina = meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.titulo.resModif");
                    
                    fecRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_FEC_RES);
                    fecNotifRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_NOT_RES);
                    pConRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_CONC);
                    impConRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_CONC);
                    impPPagRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_PRI_PAG);
                    impSPagRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_SEG_PAG);
                    fecPresRes = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_FEC_RENUN);
                    numPRen = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_REN);
                    impPueRen = (String)request.getAttribute(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_REN);
                }
            }
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide46/melanbide46.css"/>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
        <script type="text/javascript">
            function inicio(){
                document.getElementById('fechaRes').value = '<%=fecRes != null ? fecRes : ""%>';
                document.getElementById('fechaNotifRes').value = '<%=fecNotifRes != null ? fecNotifRes : ""%>';
                document.getElementById('pConceRes').value = '<%=pConRes != null ? pConRes : ""%>';
                document.getElementById('impConceRes').value = '<%=impConRes != null ? impConRes : ""%>';
                document.getElementById('impPPagRes').value = '<%=impPPagRes != null ? impPPagRes : ""%>';
                document.getElementById('impSPagRes').value = '<%=impSPagRes != null ? impSPagRes : ""%>';
                document.getElementById('fechaPresRen').value = '<%=fecPresRes != null ? fecPresRes : ""%>';
                document.getElementById('numPRen').value = '<%=numPRen != null ? numPRen : ""%>';
                document.getElementById('impPRen').value = '<%=impPueRen != null ? impPueRen : ""%>';
                
                document.getElementById('msgError').innerText = '<%=error != null ? error : ""%>';
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                 }else{
                     window.close(); 
                 } 
            }
        </script>
    </head>
    <body id="cuerpoNuevaSolicitud" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo" style="overflow-y: auto; padding: 10px;">
            <form  id="formNuevaOferta" enctype="multipart/form-data">
                
                <fieldset  style="padding: 10px;">
                    <legend class="legendAzul"><%=tituloPagina%></legend>
                    <div id="msgError">
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.fechaRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="fechaRes" name="fechaRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.fechaNotifRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="fechaNotifRes" name="fechaNorifRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.pConceRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="pConceRes" name="pConceRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.impConceRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="impConceRes" name="impConceRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.impPPagRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="impPPagRes" name="impPPagRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.impSPagRes")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="impSPagRes" name="impSPagRes" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.fechaPresRen")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="fechaPresRen" name="fechaPresRen" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.numPRen")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="numPRen" name="numPRen" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 200px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosRes.impPRen")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="15" id="impPRen" name="impPRen" value="" class="inputTexto readOnly" readonly="true"/>
                        </div>
                    </div>
                </fieldset>
                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();">
                </div>
            </form>
        </div>
        <script type="text/javascript"> 
            inicio();
        </script>
    </body>
</html>
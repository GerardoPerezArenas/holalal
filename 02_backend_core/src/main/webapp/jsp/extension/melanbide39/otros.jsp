<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <%
            int idiomaUsuario = 1;
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
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            
            String tituloPagina = meLanbide39I18n.getMensaje(idiomaUsuario, "label.solicitud.titulo.nuevaSolicitud");
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>

        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        
        <script type="text/javascript">
            function inicio(){
                
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
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }


            function mostrarCalFechaEnvioCandidatosOferta(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaEnvioCandidatosOferta').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaEnvioCandidatosOferta',null,null,null,'','calfechaEnvioCandidatosOferta','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaNacContratado(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaNacContratado').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaNacContratado',null,null,null,'','calfechaNacContratado','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaIniContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaIniContrato').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaIniContrato',null,null,null,'','calfechaIniContrato','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaFinContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaFinContrato').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaFinContrato',null,null,null,'','calfechaFinContrato','',null,null,null,null,null,null,null,null,evento);
            }
        
            function cambioValorCheck(check){
                if(check.checked){
                    check.value="S";
                }
                else{
                    check.value="N";
                }
            }
        </script>
    </head>
    <body id="cuerpoNuevaSolicitud" style="margin: 10px; text-align: left;" class="contenidoPantalla">
        <form  id="formNuevaSolicitud">
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosPuesto")%></legend>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.puesto")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input type="text" maxlength="15" size="10" id="codPuesto" name="codPuesto" value=""/>
                    </div>
                    <div style="float: left;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.pais")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input id="codPaisPuesto" name="codPaisPuesto" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descPaisPuesto" name="descPaisPuesto" type="text" class="inputTexto" size="25" readonly >
                         <a id="anchorPaisPuesto" name="anchorPaisPuesto" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto" name="botonPaisPuesto" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.ciudad")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input type="text" maxlength="200" size="58" id="txtCiudadPuesto" name="txtCiudadPuesto" value=""/>
                    </div>
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.departamento")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input type="text" maxlength="200" size="45" id="txtDepartamentoPuesto" name="txtDepartamentoPuesto" value=""/>
                    </div>
                </div>
                <div class="lineaFormulario">        
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.titulacion")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codTitulacionPuesto" name="codTitulacionPuesto" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descTitulacionPuesto" name="descTitulacionPuesto" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorTitulacionPuesto" name="anchorTitulacionPuesto" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTitulacionPuesto" name="botonTitulacionPuesto" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div class="lineaFormulario">      
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.funciones")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <textarea rows="4" cols="99" id="funcionesPuesto" name="funcionesPuesto" maxlength="500"></textarea>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.idioma")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codIdiomaPuesto1" name="codIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descIdiomaPuesto1" name="descIdiomaPuesto1" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorIdiomaPuesto1" name="anchorIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto1" name="botonIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.nivelIdioma")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input id="codNivelIdiomaPuesto1" name="codNivelIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descNivelIdiomaPuesto1" name="descNivelIdiomaPuesto1" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorNivelIdiomaPuesto1" name="anchorNivelIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto1" name="botonNivelIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.idioma")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codIdiomaPuesto2" name="codIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descIdiomaPuesto2" name="descIdiomaPuesto2" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorIdiomaPuesto2" name="anchorIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto2" name="botonIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosPuesto.nivelIdioma")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input id="codNivelIdiomaPuesto2" name="codNivelIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descNivelIdiomaPuesto2" name="descNivelIdiomaPuesto2" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorNivelIdiomaPuesto2" name="anchorNivelIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto2" name="botonNivelIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosSolicitables")%></legend>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.pais")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codPaisSolicitables" name="codPaisSolicitables" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descPaisSolicitables" name="descPaisSolicitables" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorPaisSolicitables" name="anchorPaisSolicitables" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisSolicitables" name="botonPaisSolicitables" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 100px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.grupoCot")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codGrupoSolicitables" name="codGrupoSolicitables" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descGrupoSolicitables" name="descGrupoSolicitables" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorGrupoSolicitables" name="anchorGrupoSolicitables" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupoSolicitables" name="botonGrupoSolicitables" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 150px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.salario")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input type="text" maxlength="15" size="6" id="txtSalarioSolicitables" name="txtSalarioSolicitables" value=""/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.dietas")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="6" id="txtDietasSolicitables" name="txtDietasSolicitables" value=""/>
                    </div>
                    <div style="float: left; width: 100px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.meses")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="6" id="txtMesesSolicitables" name="txtMesesSolicitables" value=""/>
                    </div>
                    <div style="float: left; width: 150px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.tramSeguros")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input type="text" maxlength="15" size="6" id="txtSegurosSolicitables" name="txtSegurosSolicitables" value=""/>
                    </div>
                </div>
                <div class="lineaFormulario" style="text-align: right;">
                    <div style="float: right; padding-right: 31px; width: 273px;">
                        <div style="float: left; width: 150px; text-align: left; padding-left: 40px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSolicitables.impSolic")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="6" id="txtImporteSolicitables" name="txtImporteSolicitables" value="" disabled="true"/>
                        </div>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosSubvencion")%></legend>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSubvencion.otrasAyudas")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="6" id="txtOtrasAyudasSubvencion" name="txtOtrasAyudasSubvencion" value=""/>
                    </div>
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosSubvencion.impSubvencion")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input type="text" maxlength="15" size="6" id="txtImpSubvencion" name="txtImpSubvencion" value="" disabled="true"/>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosOferta")%></legend>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.numOferta")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="3" id="txtNumOferta" name="txtNumOferta" value=""/>
                    </div>
                    <div style="float: left; width: 120px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.ofiGestion")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input id="codOfiGestionOferta" name="codOfiGestionOferta" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descOfiGestionOferta" name="descOfiGestionOferta" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorOfiGestionOferta" name="anchorOfiGestionOferta" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOfiGestionOferta" name="botonOfiGestionOferta" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.precandidatos")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="3" id="txtPrecandidatosOferta" name="txtPrecandidatosOferta" value=""/>
                    </div>
                    <div style="float: left; width: 120px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.fecEnvioCandidatos")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaEnvioCandidatosOferta" name="fechaEnvioCandidatosOferta" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" value=""/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaEnvioCandidatosOferta(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calfechaEnvioCandidatosOferta" name="calfechaEnvioCandidatosOferta" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.fecEnvioCandidatos")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                        </A>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <input style="margin-right: 2px" type="checkBox" id="checkDifusionOferta" name="checkDifusionOferta" value="" onclick="cambioValorCheck(this);"></input>
                    <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.difuOferta")%>
                    <input style="margin-right: 2px" type="checkBox" id="checkContratacionOferta" name="checkContratacionOferta" value="" onclick="cambioValorCheck(this);"></input>
                    <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.contratacion")%>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosOferta.listaCandidatos")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <textarea rows="10" cols="40" id="listaCandidatosOferta" name="listaCandidatosOferta" maxlength="5000"></textarea>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <input type="button" id="btnExaminarCandidatosOferta" name="btnExaminarCandidatosOferta" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.examinar")%>...">
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosContratado")%></legend>
                <div class="lineaFormulario">     
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContratado.nif")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codNifContratado" name="codNifContratado" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descNifContratado" name="descNifContratado" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorNifContratado" name="anchorNifContratado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNifContratado" name="botonNifContratado" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="15" size="10" id="txtNifOferta" name="txtNifOferta" value=""/>
                    </div>
                </div>
                <div class="lineaFormulario"> 
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContratado.nomApel")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" maxlength="200" size="100" id="txtNomApel" name="txtNomApel" value=""/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContratado.fechaNac")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacContratado" name="fechaNacContratado" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" value=""/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacContratado(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calfechaNacContratado" name="calfechaNacContratado" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContratado.fechaNac")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                        </A>
                    </div>
                    <div style="width: 200px; float: left;">
                        <div style="float: left;">
                            <input type="radio" name="radioIniSust" id="radioInicial" value="I"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "label.solicitud.datosContratado.inicial")%>
                            <input type="radio" name="radioIniSust" id="radioSustituto" value="S"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "label.solicitud.datosContratado.sustituto")%>
                        </div>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosContrato")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.fecIni")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 75px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaIniContrato" name="fechaIniContrato" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaIniContrato(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaIniContrato" name="calfechaIniContrato" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.fecIni")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.fecFin")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinContrato" name="fechaFinContrato" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinContrato(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaFinContrato" name="calfechaFinContrato" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.fecFin")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.grupoCot")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 65px;">
                            <input id="codGrupoContrato" name="codGrupoContrato" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descGrupoContrato" name="descGrupoContrato" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorGrupoContrato" name="anchorGrupoContrato" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupoContrato" name="botonGrupoContrato" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <input style="margin-right: 2px" type="checkBox" id="checkConvenioContrato" name="checkConvenioContrato" value="" onclick="cambioValorCheck(this);"></input>
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.convenio")%>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="11" size="15" id="txtSalarioContrato" name="txtSalarioContrato" value=""/>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.dietas")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="11" size="15" id="txtDietasContrato" name="txtDietasContrato" value=""/>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosContrato.coste")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="15" id="txtCosteContrato" name="txtCosteContrato" value="" disabled="true"/>
                        </div>
                    </div>
            </fieldset>
            <fieldset  style="width: 95.5%;">
                <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.solicitud.datosBaja")%></legend> 
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosBaja.causa")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codCausaBaja" name="codCausaBaja" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descCausaBaja" name="descCausaBaja" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorCausaBaja" name="anchorCausaBaja" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCausaBaja" name="botonCausaBaja" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosBaja.fecha")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                        <input id="codFechaBaja" name="codFechaBaja" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descFechaBaja" name="descFechaBaja" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorFechaBaja" name="anchorFechaBaja" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonFechaBaja" name="botonFechaBaja" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 90px;">
                        <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.datosBaja.listaCandidatos")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <textarea rows="10" cols="40" id="listaCandidatosBaja" name="listaCandidatosBaja" maxlength="5000"></textarea>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="width: auto; float: left; clear: both;">
                            <input type="button" id="btnExaminarCandidatosBaja" name="btnExaminarCandidatosBaja" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.examinar")%>...">
                        </div>
                        <div style="width: auto; float: left; clear: both; margin-top: 10px;">
                            <input type="button" id="btnAltaCandidatosBaja" name="btnAltaCandidatosBaja" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.altaSustituto")%>">
                        </div>
                    </div>
                </div>
            </fieldset>
            <div class="botonera">
                <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="crearContrato();">
                <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>
        </form>
        <script type="text/javascript">
            //Persona contratada
            var comboPaisPuesto = new Combo('PaisPuesto');
            var comboTitulacionPuesto = new Combo('TitulacionPuesto');
            var comboIdiomaPuesto1 = new Combo('IdiomaPuesto1');
            var comboIdiomaPuesto2 = new Combo('NivelIdiomaPuesto1');
            var comboIdiomaPuesto2 = new Combo('IdiomaPuesto2');
            var comboIdiomaPuesto2 = new Combo('NivelIdiomaPuesto2');
            
            
            
            var comboPaisSolicitables = new Combo('PaisSolicitables');
            var comboGrupoSolicitables = new Combo('GrupoSolicitables');
            
            var comboOfiGestionOferta = new Combo('OfiGestionOferta');
            
            var comboNifContratado = new Combo('NifContratado');
            
            
            var comboGrupoContrato = new Combo('GrupoContrato');
            
            var comboCausaBaja = new Combo('CausaBaja');
            var comboFechaBaja = new Combo('FechaBaja');
            
            
            inicio();
        </script>
    </body>
</html>

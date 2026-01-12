<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide06.i18n.MeLanbide06I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06Constantes"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06ConfigurationParameter"
             type="es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06ConfigurationParameter" />

<script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>

<html:html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    
<%
    
  MeLanbide06I18n meLanbide06I18n = MeLanbide06I18n.getInstance();  
     
  int idioma=1;
  int apl=1;
  int munic = 0;
  int entidad=0;
  int organizacion=0;
  String descripEnt="";
  String descripOrg="";
  String css="";
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
          css=usuario.getCss();
          entidad=usuario.getEntCod();
          organizacion=usuario.getOrgCod();
          descripEnt=usuario.getEnt();
          descripOrg=usuario.getOrg();
        } 
  }

  //Textos formulario
  String eliminarGA = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.msgEliminarGA");
  String asuntoGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.asunto");
  String contenidoGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.contenido");
  String errorEliminar = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorEliminar");
  String emailIncorrecto = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.emailIncorrecto");
  String errorModifConfig = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorModifConfig");
  String camposOblig = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.camposOblig");
  String errorMostrarTabla = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorMostrarTabla");
  String errorAddConfig = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorAddConfig");        
  String errorGetUor = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorGetUor");
  String errorGetCodpro = meLanbide06I18n.getMensaje(idioma,"msg.formularioGestionAvisos.errorGetCodpro");
  String buscarGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.buscarGA");
  String anteriorGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.anteriorGA");
  String siguienteGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.siguienteGA");
  String mosFilasPagGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.mosFilasPagGA");
  String msgNoResultBusqGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.msgNoResultBusqGA");
  String mosPagDePagsGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.mosPagDePagsGA");
  String noRegDispGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.noRegDispGA");
  String filtrDeTotalGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.filtrDeTotalGA");
  String primeroGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.primeroGA");
  String ultimoGA = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.ultimoGA");
  String btnEtiquetas = meLanbide06I18n.getMensaje(idioma,"btn.formularioGestionAvisos.etiquetas");     
          
  //Combos  
  Gson gson;
  GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
  
  List<SelectItem> listaProcedimientosGA = (ArrayList<SelectItem>)request.getAttribute("listaProcedimientos");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listProcedimientosGA = gson.toJson(listaProcedimientosGA);
  
  List<SelectItem> listaUorsGA = (ArrayList<SelectItem>)request.getAttribute("listaUors");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listUorsGA = gson.toJson(listaUorsGA);
  
  List<SelectItem> listaAsuntosGA = (ArrayList<SelectItem>)request.getAttribute("listaAsuntos");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listAsuntosGA = gson.toJson(listaAsuntosGA);
  
  List<SelectItem> listaEventosGA = (ArrayList<SelectItem>)request.getAttribute("listaEventos");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listEventosGA = gson.toJson(listaEventosGA);
  
  List<SelectItem> listaEntidadesGA = (ArrayList<SelectItem>)request.getAttribute("listaEntidades");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listEntidadesGA = gson.toJson(listaEntidadesGA);
  
  List<SelectItem> listaOrganizacionesGA = (ArrayList<SelectItem>)request.getAttribute("listaOrganizaciones");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listOrganizacionesGA = gson.toJson(listaOrganizacionesGA);
  
%>    
    
<!-- Estilos -->

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide06/gestionAvisos/gestionAvisos.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide06/gestionAvisos/gestionAvisos.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />    
    
<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />

<TITLE>::: ADMINISTRACION  Grupo:::</TITLE>
  
</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off');}">

<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
        
<html:form action="/administracion/UsuariosGrupos.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codEntidad" />
<html:hidden  property="nombreEntidad" />
<input type="hidden" id="organizacion" value="<%=organizacion%>"/>
<input type="hidden" id="entidad" value="<%=entidad%>"/>
<input type="hidden" id="descripOrg" value="<%=descripOrg%>"/>
<input type="hidden" id="descripEnt" value="<%=descripEnt%>"/>

<!-- Elementos Html para control desde fichero JS-->

<input type="hidden" id="gestionAvisos_descriptor_buscar" value="<%=buscarGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_anterior" value="<%=anteriorGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_siguiente" value="<%=siguienteGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_mosFilasPag" value="<%=mosFilasPagGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_msgNoResultBusq" value="<%=msgNoResultBusqGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_mosPagDePags" value="<%=mosPagDePagsGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_noRegDisp" value="<%=noRegDispGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_filtrDeTotal" value="<%=filtrDeTotalGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_primero" value="<%=primeroGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_ultimo" value="<%=ultimoGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_evento" value="<%=descriptor.getDescripcion("etiq_evento")%>"/>
<input type="hidden" id="gestionAvisos_descriptor_procedimiento" value="<%=descriptor.getDescripcion("gEtiqProc")%>"/>
<input type="hidden" id="gestionAvisos_descriptor_mailUOR" value="<%=descriptor.getDescripcion("etiqMailUOR")%>"/>
<input type="hidden" id="gestionAvisos_descriptor_asunto" value="<%=asuntoGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_contenido" value="<%=contenidoGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_eliminarGA" value="<%=eliminarGA%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_eliminar" value="<%=errorEliminar%>"/>
<input type="hidden" id="gestionAvisos_descriptor_email_incorrecto" value="<%=emailIncorrecto%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_modif_config" value="<%=errorModifConfig%>"/>
<input type="hidden" id="gestionAvisos_descriptor_campos_oblig" value="<%=camposOblig%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_mostrar_tabla" value="<%=errorMostrarTabla%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_add_config" value="<%=errorAddConfig%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_get_uor" value="<%=errorGetUor%>"/>
<input type="hidden" id="gestionAvisos_descriptor_error_get_codpro" value="<%=errorGetCodpro%>"/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_conf_sist_avi_email")%></div>
<div class="contenidoPantalla">
    
    <input type="hidden" id="listaOrganizacionesGestionAvisos" value=""/>
    <script>document.getElementById("listaOrganizacionesGestionAvisos").value = JSON.stringify(<%=listOrganizacionesGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <input type="hidden" id="listaEntidadesGestionAvisos" value=""/>
    <script>document.getElementById("listaEntidadesGestionAvisos").value = JSON.stringify(<%=listEntidadesGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <input type="hidden" id="listaProcedimientosGestionAvisos" value=""/>
    <script>document.getElementById("listaProcedimientosGestionAvisos").value = JSON.stringify(<%=listProcedimientosGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <input type="hidden" id="listaUorsGestionAvisos" value=""/>
    <script>document.getElementById("listaUorsGestionAvisos").value = JSON.stringify(<%=listUorsGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <input type="hidden" id="listaAsuntosGestionAvisos" value=""/>
    <script>document.getElementById("listaAsuntosGestionAvisos").value = JSON.stringify(<%=listAsuntosGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <input type="hidden" id="listaEventosGestionAvisos" value=""/>
    <script>document.getElementById("listaEventosGestionAvisos").value = JSON.stringify(<%=listEventosGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    
    <TABLE id ="tablaDatosGral">
        <tr>
            <td style="width: 13%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
            <td class="columnP">
                <input type="text" id="codOrganizaciones" class="inputTextoObligatorio" name="codOrganizaciones" size="2" onkeypress="javascript:return SoloDigitosNumericosLanbide(event);" readonly/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descOrganizaciones" style="width:300px;height:17px" readonly/>
                       <A href="" id="anchorOrganizaciones" name="anchorOrganizaciones" style="pointer-events: none;">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizaciones" name="botonOrganizaciones"></span>
                </A>
            </td>
            <td style="width: 12%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Entidad")%>:</td>
            <td class="columnP">
                <input type="text" id="codEntidades" class="inputTextoObligatorio" name="codEntidades" size="2"
                       onkeypress="javascript:return SoloDigitosNumericosLanbide(event);" readonly/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descEntidades" style="width:300px;height:17px" readonly/>
                       <A href="" id="anchorEntidades" name="anchorEntidades" style="pointer-events: none;">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEntidades" name="botonEntidades"></span>
                </A>
            </td>
        </tr>
        </table>
        <TABLE id ="tablaDatosGral">
            <tr>    
            <td style="width: 13%" class="etiqueta"><%=meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.asuntoRegistro")%>:</td>
            <td class="columnP">
                <input type="text" id="codAsunto" class="inputTextoObligatorio" name="codAsunto" size="9"/>
                <input type="text" id="descAsunto" class="inputTextoObligatorio" name="descAsunto" style="width:762px;height:17px;pointer-events: none;" readonly/>
                       <A href="" id="anchorAsunto" name="anchorAsunto">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAsunto" name="botonAsunto"></span>
                </A>
            </td>
        </table>     
        <TABLE id ="tablaDatosGral">
            <tr>
                <td style="width: 13%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</td>
                <td class="columnP">
                    <input type="text" id="codProcedimiento" class="inputTextoObligatorio" name="codProcedimiento" size="9" style="pointer-events: none;"/>
                    <input type="text" id="descProcedimiento" class="inputTextoObligatorio" name="descProcedimiento" style="width:762px;height:17px; pointer-events: none;" readonly/>
                           <A href="" id="anchorProcedimiento" name="anchorProcedimiento" style="pointer-events: none;">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimiento" name="botonProcedimiento" style="pointer-events: none;"></span>
                    </A>
                </td>
            </tr>
        </table>
        <TABLE id ="tablaDatosGral">
         <tr>
            <td style="width: 13%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_unidOrg")%>:</td>
            <td class="columnP">
                <input type="text" id="codUOR" class="inputTextoObligatorio" name="codUOR" size="9" style="pointer-events: none;"/>
                <input type="text" id="descUOR" class="inputTextoObligatorio" name="descUOR" style="width:762px;height:17px;pointer-events: none;" readonly/>
                       <A href="" id="anchorUOR" name="anchorUOR" style="pointer-events: none;">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUOR" name="botonUOR"></span>
                </A>
            </td>
        </tr>
        </table>
        <TABLE id ="tablaDatosGral">    
            <tr>
                <td style="width: 13%" class="etiqueta"><%=descriptor.getDescripcion("etiq_evento")%>:</td>
                <td class="columnP">
                    <input type="text" id="codEvento" class="inputTextoObligatorio" name="codEvento" size="9""/>
                    <input type="text" id="descEvento" class="inputTextoObligatorio" name="descEvento" style="width:762px;height:17px;pointer-events: none;" readonly/>
                           <A href="" id="anchorUOR" name="anchorEvento">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEvento" name="botonEvento"></span>
                    </A>
                </td>
            </tr>
        </table>    
        <TABLE id ="tablaDatosGral">    
            <tr>
                <td style="width: 13.38%;" class="etiqueta"><%=meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.direccionEmail")%>:</td>
                <td class="columnP">
                    <input style="width:850px;height:17px" type="text" id="desEmail" maxlength="150" class="inputTextoObligatorio" name="desEmail" size="2"/>
                </td>
            </tr>  
        </table>            
        <TABLE id ="tablaDatosGral">    
            <tr> 
                <td style="width: 13.38%;" class="etiqueta"><%=meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.asuntoEmail")%>:</td>
                <td class="columnP">
                    <input style="width:850px;height:17px" type="text" id="desAsunto" maxlength="150" class="inputTextoObligatorio" name="desAsunto" size="2"/>
                </td>
            </tr>  
        </table>
        <TABLE id ="tablaDatosGral">    
            <tr> 
                <td style="width: 13.38%" class="etiqueta"><%=meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.contenidoEmail")%>:</td>
                <td class="columnP">
                    <textarea style="width:850px; resize: none;" type="text" id="desContenido" maxlength="500" class="inputTextoObligatorio" name="desContenido"></textarea>
                </td>
            </tr>  
        </table>    
        <TABLE id ="tablaDatosGral">    
            <tr> 
                <td style="width: 13%;" class="etiqueta"><%=meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.checkBox")%>:</td>
                <td class="columnP">
                    <input type="checkBox" id="checkConfActiva" name="checkConfActiva" class="inputTextoObligatorio" value="" onclick="cambioValorCheck(this);">    
                    </input>
                </td>
            </tr>  
        </table>       
        <TABLE id ="tablaDatosGral">         
            <tr>
                <td align="left" id="tablaGestionAvisos"></td>
            </tr>
        </table>
        <div class="botoneraPrincipal"> 
                <INPUT type= "button" id="btnAnadir" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAnadir")%> name="cmdAceptar"  onClick="pulsarAnadirGA();">
                <INPUT type= "button" id="btnModificar" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdAceptar"  onClick="pulsarModificarGA();">
                <INPUT type= "button" id="btnEliminar" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdAceptar"  onClick="pulsarEliminarGA();">
                <INPUT type= "button" id="btnEtiquetas" class="botonGeneral" value="<%=meLanbide06I18n.getMensaje(idioma,"btn.formularioGestionAvisos.etiquetas")%>" name="cmdEtiquetas"  onClick="pulsarEtiquetasGA();">
                <INPUT type= "button" id="btnSalir" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalirGA();">
        </div>        
</div>        

</html:form>

</BODY>

</html:html>
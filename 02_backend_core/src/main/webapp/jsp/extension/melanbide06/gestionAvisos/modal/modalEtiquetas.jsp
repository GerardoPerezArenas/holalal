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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
    
<%
    
  MeLanbide06I18n meLanbide06I18n = MeLanbide06I18n.getInstance();  
  Gson gson;
  GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
  
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
  
  String codigoEtiq = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.codigoEtiq");
  String descEtiq = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.descEtiq");   
  String titModalEtiquetas = meLanbide06I18n.getMensaje(idioma,"label.formularioGestionAvisos.titModalEtiquetas");
  
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
  
  List<SelectItem> listaEtiquetasGA = (ArrayList<SelectItem>)request.getAttribute("listaEtiquetas");
  gson = new Gson();
  gsonB.serializeNulls();  
  gson=gsonB.create();
  String listEtiquetasGA = gson.toJson(listaEtiquetasGA);
  
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

<body class="bandaBody" onload="javascript:{ pleaseWait('on');}">


<html:form action="/administracion/UsuariosGrupos.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codEntidad" />
<html:hidden  property="nombreEntidad" />
<input type="hidden" id="organizacion" value="<%=organizacion%>"/>
<input type="hidden" id="entidad" value="<%=entidad%>"/>
<input type="hidden" id="descripOrg" value="<%=descripOrg%>"/>
<input type="hidden" id="descripEnt" value="<%=descripEnt%>"/>
<input type="hidden" id="codigoEtiq" value="<%=codigoEtiq%>"/>
<input type="hidden" id="descEtiq" value="<%=descEtiq%>"/>
<input type="hidden" id="titModalEtiquetas" value="<%=titModalEtiquetas%>"/>

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

<div id="tituloModalEtiquetas" class="txttitblanco"></div> 
<div class="contenidoPantalla">
     
   <input type="hidden" id="listaEtiquetasGestionAvisos" value=""/>
    <script>document.getElementById("listaEtiquetasGestionAvisos").value = JSON.stringify(<%=listEtiquetasGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script> 
    
    <TABLE id ="tablaDatosGral">         
        <tr>
            <td align="left" id="tablaEtiquetas"></td>
        </tr>
    </table>
    <div class="botoneraPrincipal" style="float: right;"> 
            <INPUT type= "button" id="btnSalir" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="cerrarVentanaGA();">
    </div>        
</div>        

</html:form>

</BODY>

</html:html>
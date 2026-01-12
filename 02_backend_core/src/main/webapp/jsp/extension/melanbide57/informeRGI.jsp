<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide57.i18n.MeLanbide57I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

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
        {}
    

    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide57I18n meLanbide57I18n = MeLanbide57I18n.getInstance();
    
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
    
%>
<script type="text/javascript">
    var mensajeValidacion="";
    function pulsarInforme(){
        if (validarFiltrosBusqueda()) {
            try{
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                var parametros = "?tarea=preparar&modulo=MELANBIDE57&operacion=descargarInformeRGI&tipo=0";

                //document.forms[0].target = "ocultoPendientesAPA";
                document.forms[0].action = url + parametros;       
                document.forms[0].submit();        
            }catch(err){
                alert('<%=meLanbide57I18n.getMensaje(idiomaUsuario, "error.abrirDocumento")%>');
            }
        }
        else {
            jsp_alerta('A',mensajeValidacion);
        }
    }//pulsarInforme
    

</script>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide57/melanbide57.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/lanbide.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/JavaScriptUtil.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/Parsers.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/InputMask.js"></script>
<!--en PRE sale mensaje de contenido no seguro, LO QUITAMOS-->
<!-- Eventos onKeyPress compatibilidad firefox  -->
<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->

<body class="bandaBody">        <!--inicializar();-->
    <br/>
    <form action="/PeticionModuloIntegracion.do" method="POST">
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalRGI")%></h2>
        <div class="contenidoPantalla">
            <div style="clear: both;">
                <div id="div_label" class="sub3titulo" style=" text-align: center;">
                    <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide57I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
                </div>
                <br/>
                <div class="separador"></div>
                
                <table style="width: 100%;"> 		                 
                    <tbody>
                        <tr>
                            <td class="etiqueta" style="width: 50%; text-align: right; padding-right: 1%;">
                                <%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.meLanbide57FechaDesde")%>
                            </td>
                            <td style="width: 50%; text-align: right;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="meLanbide57FechaDesde" name="meLanbide57FechaDesde"
                                           maxlength="10"  size="10"
                                           value=""
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                           onblur = "javascript:return comprobarFechaLanbide(this);"
                                           onfocus="javascript:this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaDesde(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                        <IMG style="border: 0px solid none" height="17" id="calMeLanbide57FechaDesde" name="calMeLanbide57FechaDesde" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="etiqueta" style="width: 50%; text-align: right; padding-right: 1%;">
                                <%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.meLanbide57FechaHasta")%>
                            </td>
                            <td style="width: 50%; text-align: right;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="meLanbide57FechaHasta" name="meLanbide57FechaHasta"
                                           maxlength="10"  size="10"
                                           value=""
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                           onblur = "javascript:return comprobarFechaLanbide(this);"
                                           onfocus="javascript:this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaHasta(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                        <IMG style="border: 0px solid none" height="17" id="calMeLanbide57FechaHasta" name="calMeLanbide57FechaHasta" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                                    
                <div class="separador"></div>
                <div class="botonera" style="text-align: center; margin-top: 3%" >
                        <input type="button" id="btnInforme" name="btnInforme" class="botonGeneral"  value="<%=meLanbide57I18n.getMensaje(idiomaUsuario, "btn.informeRGI")%>" onclick="pulsarInforme();">
                </div>
            </div>
        </div>
    </form>
    <!-- Script Con Funciones-->
    <script type="text/javascript">
            
        function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value)!='') {
                  var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                  if (!D[0]){
                    jsp_alerta("A","<%=meLanbide57I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                    document.getElementById(inputFecha.name).focus();
                    document.getElementById(inputFecha.name).select();
                    return false;
                  } else {
                    inputFecha.value = D[1];
                    return true;
                  }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide
            
        function mostrarCalFechaDesde(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide57FechaDesde").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide57FechaDesde',null,null,null,'','calMeLanbide57FechaDesde','',null,null,null,null,null,null,null, null,evento);        
        }
        
        function mostrarCalFechaHasta(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide57FechaHasta").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide57FechaHasta',null,null,null,'','calMeLanbide57FechaHasta','',null,null,null,null,null,null,null, null,evento);        
        }

        function validarFiltrosBusqueda(){
            if(document.getElementById("meLanbide57FechaDesde").value=="" 
                || document.getElementById("meLanbide57FechaHasta").value==""){
                mensajeValidacion='<%=meLanbide57I18n.getMensaje(idiomaUsuario, "msg.busqueda.filtrosoblig")%>';
                return false;
            }
            if(document.getElementById("meLanbide57FechaDesde").value!="" && document.getElementById("meLanbide57FechaHasta").value!=""){
                var fechaentrada = document.getElementById("meLanbide57FechaDesde").value.split("/");
                var fechasalida = document.getElementById("meLanbide57FechaHasta").value.split("/");
                fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                if(fechaentrada>fechasalida){
                    mensajeValidacion='<%=meLanbide57I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechas")%>';
                    document.getElementById("meLanbide57FechaDesde").select();
                    return false;
                }
                
            }
            return true;
        }
        
    </script>
    
    <div id="popupcalendar" class="text"></div>                
</body>


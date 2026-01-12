<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null)
    {
        try
        {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }

    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide53I18n meLanbide57I18n = MeLanbide53I18n.getInstance();
    
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
    
%>
<script type="text/javascript">
    var mensajeValidacion="";
    function pulsarInformeInterno(){
        if (validarFiltrosBusqueda()) {
            try{
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
               var parametros = "?tarea=preparar&modulo=MELANBIDE53&operacion=descargarInformeDetalle&tipo=0";
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
    }//pulsarInformeInterno
    function pulsarInformeGeneral(){
        if (validarFiltrosBusqueda()) {
            try{
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                
                 var parametros = "?tarea=preparar&modulo=MELANBIDE53&operacion=descargarInformeInterno&tipo=0";

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
    }//pulsarInformeGeneral
    

</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide57/melanbide57.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/lanbide.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/JavaScriptUtil.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/Parsers.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/InputMask.js"></script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>

<body class="bandaBody" onload="javascript:{if (window.top.principal.frames[0]&&window.top.principal.frames['menu'].Go)
        window.top.principal.frames['menu'].Go();}">        <!--inicializar();-->
    <br/>
    <form action="/PeticionModuloIntegracion.do" method="POST">
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal2")%></h2>
        <div>
            <div style="clear: both;">
                <div id="div_label" class="sub3titulo" style=" text-align: center;">
                    <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide57I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
                </div>
                <br/>
                <div class="separador"></div>
                <div class="wrapper_1">
                    <div class="container_1">
                        <label class="etiqueta" style="float: left;margin-right: 1%;">
                            <%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.meLanbide57FechaDesde")%>
                        </label>
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
                        <div class="separador"></div>
                        <label class="etiqueta" style="float: left;margin-right: 1%;">
                            <%=meLanbide57I18n.getMensaje(idiomaUsuario,"label.meLanbide57FechaHasta")%>
                        </label>
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
                    </div>
                </div>
                <div class="separador"></div>
                <div class="botonera" style="text-align: center; margin-top: 5%" >
                        <input type="button" id="btnInformeInterno" name="btnInformeInterno" class="botonGeneral"  value="<%=meLanbide57I18n.getMensaje(idiomaUsuario, "btn.informeInterno")%>" onclick="pulsarInformeInterno();">
                        <input type="button" id="btnInformeGeneral" name="btnInformeGeneral" class="botonGeneral" value="<%=meLanbide57I18n.getMensaje(idiomaUsuario, "btn.informeGeneral")%>" onclick="pulsarInformeGeneral();">
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


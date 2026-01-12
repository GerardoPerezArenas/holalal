<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <%
            MeLanbide37I18n meLanbide37I18n = MeLanbide37I18n.getInstance();
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex)
                {
                    
                }
            }
               
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
    
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide37/melanbide37.css"/>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
              
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript">
            var msgValidacion = '';
            
            function aceptar(){
                document.getElementById('msgAceptandoDatos').style.display="inline";
                var listaDatos = new Array(); 
                listaDatos[0]=document.getElementById('codigoCP').value;
                listaDatos[1]=document.getElementById('fecSoliIni').value;
                listaDatos[2]=document.getElementById('fecSoliFin').value;
                /*var fila = new Array();
                fila[0]=document.getElementById('codTipoAcreditacion').value;
                fila[1]=document.getElementById('codValoracion').value;
                fila[2]=document.getElementById('codigoCP').value;
                fila[3]=document.getElementById('fecSoliIni').value;
                fila[4]=document.getElementById('fecSoliFin').value;
                listaDatos[1]=fila;*/
                window.returnValue = listaDatos;
                cerrarVentana();
                }
            
            function cancelar(){
                cerrarVentana();
            }
            
            function cerrarVentana(){
                if(navigator.appName=="Microsoft Internet Explorer") { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                }else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                }else{
                     window.close(); 
                } 
            }
                
            function mostrarCalFechaInicio(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFecSoliIni").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fecSoliIni',null,null,null,'','calFecSoliIni','',null,null,null,null,null,null,null,null,evento);
            }

            function mostrarCalFecSoliFin(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFecSoliFin").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fecSoliFin',null,null,null,'','calFecSoliFin','',null,null,null,null,null,null,null,null,evento);
            }
              
        </script>
    </head>
    <body>
        <form>
            <div id="barraProgresoListadoCP" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgAceptandoDatos">
                                                    <%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="width:5%;height:20%;"></td>
                                            <td class="imagenHide"></td>
                                            <td style="width:5%;height:20%;"></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" style="height:10%" ></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
            
            <div style="width: 100%; padding: 10px;">
                <div class="lineaFormulario">
                    <div style="width: 150px; float: left;">
                        <%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarCodigoCP")%>
                    </div>
                    <div style="width: 400px; float: left;">
                        <input id="codigoCP" name="codigoCP" type="text" class="inputTexto" size="40"/>                         
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 169px; float: left;">
                        <%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarFecSoliIni")%>
                    </div>
                    <div style="width: 131px; float: left;">
                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecSoliIni" name="fecSoliIni" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                        <IMG style="border: 0" height="17" id="calFecSoliIni" name="calFecSoliIni" alt="<%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                    </A>                                
                    </div>
                    <div style="width: 155px; float: left;">
                        <%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarFecSoliFin")%>
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecSoliFin" name="fechaFin" onkeyup="javascript: return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A id="btnFecSoliFin" href="javascript:calClick(event);" onclick="mostrarCalFecSoliFin(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFecSoliFin" name="calFecSoliFin" alt="<%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarFecSoliIni")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                </div>
                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
                <div style="width: 100%; padding: 10px;">
                    <label id="msgLabel" style="color:red;"></label>
                </div>
            </div>
        </form>    
        <div id="popupcalendar" class="text"></div>
    </body>    
</html>
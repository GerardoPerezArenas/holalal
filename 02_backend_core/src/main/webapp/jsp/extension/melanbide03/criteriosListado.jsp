<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
           MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide03/melanbide03.css"/>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var msgValidacion = '';

            function aceptar() {
                document.getElementById('msgAceptandoDatos').style.display = "inline";
                var listaDatos = new Array();
                listaDatos[0] = '0';//codoperacion 
                var fila = new Array();
                fila[0] = document.getElementById('numExp').value;

                listaDatos[1] = fila;
                //window.returnValue = listaDatos;
                self.parent.opener.retornoXanelaAuxiliar(listaDatos);
                cerrarVentana();
            }

            function cancelar() {
                cerrarVentana();
            }

            function cerrarVentana() {
                if (navigator.appName == "Microsoft Internet Explorer") {
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
                                                        <%=meLanbide03I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                <!--<div class="lineaFormulario">
                    <div style="width: 150px; float: left;">
                        <//%=meLanbide03I18n.getMensaje(idiomaUsuario,"msg.solicitarTipoAcreditacion")%>
                    </div>
                    <div style="width: 400px; float: left;">
                        <input id="codTipoAcreditacion" name="codTipoAcreditacion" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descTipoAcreditacion" name="descTipoAcreditacion" type="text" class="inputTexto" size="40" readonly >
                         <a id="anchorTipoAcreditacion" name="anchorTipoAcreditacion" href=""><img src="<//%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoAcreditacion" name="botonTipoAcreditacion" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                </div>-->

                <div class="lineaFormulario">
                    <div style="width: 150px; float: left;">
                        <%=meLanbide03I18n.getMensaje(idiomaUsuario,"msg.solicitarNumExp")%>
                    </div>
                    <div style="width: 400px; float: left;">
                        <input id="numExp" name="numExp" type="text" class="inputTexto" size="40"/>                         
                    </div>
                </div>

                <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
                <div style="width: 100%; padding: 10px;">
                    <label id="msgLabel" style="color:red;"></label>
                </div>
            </div>
        </form>    
    </body>

</html>
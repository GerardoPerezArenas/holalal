<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.vo.comun.SelectItem" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
			
                        int idioma=1;
            int apl=4;
            String codUsu = "";
            
            UsuarioValueObject usuario=new UsuarioValueObject();            
            if (session!=null){
              if (usuario!=null) {
                usuario = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                        int cUsu = usuario.getIdUsuario();
                codUsu = Integer.toString(cUsu);
              }
            }		
               
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
    
            //listado Tipoacreditacion
            List<SelectItem> listaTipoAcreditacion = new ArrayList<SelectItem>();
            if(request.getAttribute("lstTipoAcreditacion") != null)
                listaTipoAcreditacion = (List<SelectItem>)request.getAttribute("lstTipoAcreditacion");

            String lcodTipoAcreditacion = "";
            String ldescTipoAcreditacion = "";

            if (listaTipoAcreditacion != null && listaTipoAcreditacion.size() > 0) 
            {
                int i;
                SelectItem tc = null;                
                for (i = 0; i < listaTipoAcreditacion.size() - 1; i++) 
                {
                    tc = (SelectItem) listaTipoAcreditacion.get(i);
                    lcodTipoAcreditacion += "\"" + tc.getId().toString() + "\",";                   
                    ldescTipoAcreditacion += "\"" + tc.getLabel() + "\",";
                }
                tc = (SelectItem) listaTipoAcreditacion.get(i);
                lcodTipoAcreditacion += "\"" + tc.getId().toString() + "\"";
                ldescTipoAcreditacion += "\"" + tc.getLabel() + "\"";
            }
            
            
            
            List<SelectItem> listaValoracion = new ArrayList<SelectItem>();
            if(request.getAttribute("lstValoracion") != null)
                listaValoracion = (List<SelectItem>)request.getAttribute("lstValoracion");
            String lcodValoracion = "";
            String ldescValoracion = "";

            if (listaValoracion != null && listaValoracion.size() > 0) 
            {
                int i;
                SelectItem tc = null;                
                for (i = 0; i < listaValoracion.size() - 1; i++) 
                {
                    tc = (SelectItem) listaValoracion.get(i);
                    lcodValoracion += "\"" + tc.getId().toString() + "\",";                   
                    ldescValoracion += "\"" + tc.getLabel() + "\",";
                }
                tc = (SelectItem) listaValoracion.get(i);
                lcodValoracion += "\"" + tc.getId().toString() + "\"";
                ldescValoracion += "\"" + tc.getLabel() + "\"";
            } 
        %>
        <!-- Beans -->
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide37/melanbide37.css"/>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript">
            var msgValidacion = '';

            var codTipoAcreditacion = [<%=lcodTipoAcreditacion%>];
            var descTipoAcreditacion = [<%=ldescTipoAcreditacion%>];
            var codValoracion = [<%=lcodValoracion%>];
            var descValoracion = [<%=ldescValoracion%>];

            function aceptar() {
                document.getElementById('msgAceptandoDatos').style.display = "inline";
                var listaDatos = new Array();
                listaDatos[0] = '0';//codoperacion 
                var fila = new Array();
                fila[0] = document.getElementById('codTipoAcreditacion').value;
                fila[1] = document.getElementById('codValoracion').value;
                fila[2] = document.getElementById('codigoCP').value;
                fila[3] = document.getElementById('fecSoliIni').value;
                fila[4] = document.getElementById('fecSoliFin').value;
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

            function inicio() {
                comboTipoAcreditacion = new Combo('TipoAcreditacion');
                comboValoracion = new Combo('Valoracion');
                cargarCombos();
            }

            function cargarCombos() {
                comboTipoAcreditacion.addItems(codTipoAcreditacion, descTipoAcreditacion);
                comboValoracion.addItems(codValoracion, descValoracion);
            }

            function mostrarCalFechaInicio(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecSoliIni").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecSoliIni', null, null, null, '', 'calFecSoliIni', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFecSoliFin(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecSoliFin").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecSoliFin', null, null, null, '', 'calFecSoliFin', '', null, null, null, null, null, null, null, null, evento);
            }

        </script>
    </head>
    <body onload="inicio();">
        <form>
            <div class="contenidoPantalla">
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
                <legend style="color:red;">En los listados se recupera un máximo de 50.000 registros.</legend>
                <div style="width: 100%; padding: 10px;">               
                    <div class="lineaFormulario">
                        <div style="width: 150px; float: left;">
                            <%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarTipoAcreditacion")%>
                        </div>
                        <div style="width: 400px; float: left;">
                            <input id="codTipoAcreditacion" name="codTipoAcreditacion" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descTipoAcreditacion" name="descTipoAcreditacion" type="text" class="inputTexto" size="40" readonly >
                            <a id="anchorTipoAcreditacion" name="anchorTipoAcreditacion" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoAcreditacion" name="botonTipoAcreditacion" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 150px; float: left;">
                            <%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.solicitarValoracion")%>
                        </div>
                        <div style="width: 400px; float: left;">
                            <input id="codValoracion" name="codValoracion" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descValoracion" name="descValoracion" type="text" class="inputTexto" size="40" readonly >
                            <a id="anchorValoracion" name="anchorValoracion" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonValoracion" name="botonValoracion" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>  
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
            </div>
        </form>    
        <div id="popupcalendar" class="text"></div>
    </body>
    <script type="text/javascript">
        var comboTipoAcreditacion;
        var comboValoracion;
    </script>
</html>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.MyHttpSessionListener"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.SessionInfo"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.InformacionSistemaForm"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Gestion Errores</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int numeroTotalRegistros = 0;
            String iniciaModulo = "";
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            
            if (request.getAttribute("numeroTotalRegistros") != null) {
                numeroTotalRegistros = (Integer)request.getAttribute("numeroTotalRegistros");
            }
            if (request.getAttribute("iniciaModulo") != null) {
                iniciaModulo = (String)request.getAttribute("iniciaModulo");
            }

            MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
            String numMaxLineas = ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES);
            if(numMaxLineas == null || numMaxLineas=="")
                numMaxLineas="15";
       %>
       
       <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
       </script>
       
       <script type="text/javascript">
            //paginación
           /* Variables Para paginacion tabla de errores */
            var enlacesPaginaE  = 10;
            var lineasPaginaE   = '<%=numMaxLineas%>';
            var paginaActualE   = 1;
            var paginaInferiorE = 1;
            var paginaSuperiorE = enlacesPaginaE;
            var inicioE = 0;
            var finE    = 0;
            var numeroTotalRegistros='<%=numeroTotalRegistros%>';
            var numeroPaginasE=Math.ceil(numeroTotalRegistros/lineasPaginaE);
            if (numeroPaginasE < enlacesPaginaE) paginaSuperiorE= numeroPaginasE;
            var listaSelErr = new Array();
            var listaSelErrOriginal = new Array();
            var iniciaModulo = '<%=iniciaModulo%>';
            // Fin variables paginacion tabla errores 
            
            function inicializaLista(numeroPaginaE){
                //tableObject=tabAud;
                //var j = 0;
                //var jE = 0;
                paginaActualE = numeroPaginaE;
                listaPE = new Array();

                inicioE =0;
                finE = lineasPaginaE;
                listaPE = listaSelErr;

                tablaErrores.lineas=listaPE;
                refrescaDisplayTablaErrors();
                
                domlay('enlacesListIdErros',1,0,0,enlacesListErrors());

            }
            
            function enlacesListErrors() {
                var htmlString = " ";
                numeroPaginasE = Math.ceil(numeroTotalRegistros /lineasPaginaE);

                if (numeroPaginasE > 1) {
                  htmlString += '<table class="fondoNavegacion" cellpadding="2" cellspacing="0" align="center"><tr>'
                  if (paginaActualE > 1) {
                    htmlString += '<td width="35" class="botonNavegacion">';
                    htmlString += '<a href="javascript:irPrimeraPaginaE();" class="linkNavegacion" target="_self">';
                    htmlString += ' |<< ';
                    htmlString += '</a></td>';
                    htmlString += '<td width="5"></td>';
                    htmlString += '<td width="35" class="botonNavegacion">';
                    htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasAnterioresE('+ eval(paginaActualE) + ')" target="_self">';
                    htmlString += ' << ';
                    htmlString += '</a></td>';
                    } else htmlString += '<td width="75">&nbsp;</td>';

                  htmlString += '</td><td align="center" style="width:250px">';

                  for(var i=paginaInferiorE-1; i < paginaSuperiorE; i++){
                      if ((i+1) == paginaActualE)
                        htmlString += '<span class="indiceNavSelected">'+ (i+1) + '</span>&nbsp;&nbsp;';
                      else
                        htmlString += '<a class="indiceNavegacion" href="javascript:cargaPagE('+ eval(i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
                  }

                  if (paginaSuperiorE < numeroPaginasE){
                    htmlString += '</td><td width="35" class="botonNavegacion">';
                    htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasSiguientesE('+ eval(eval(paginaActualE))+ ')" target="_self">';
                    htmlString += ' >> ';
                    htmlString += '</a></td>';
                    htmlString += '<td width="5""></td>';
                    htmlString += '<td width="35" class="botonNavegacion">';
                    htmlString += '<a href="javascript:irUltimaPaginaE();" class="linkNavegacion" target="_self">';
                    htmlString += ' >>| ';
                    htmlString += '</a></td>';
                  } else htmlString += '</td><td width="70"></td>';
                  htmlString += '</tr></table>';
                }

                var registroInferiorE = ((paginaActualE - 1) * lineasPaginaE) + 1;
                var registroSuperiorE = (paginaActualE * lineasPaginaE);
                if (paginaActualE == numeroPaginasE)
                  registroSuperiorE = numeroTotalRegistros ;
                if (listaSelErr.length > 0)
                  htmlString += '<center><font class="textoSuelto"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.etiquetaEnlace1")%>&nbsp;' + registroInferiorE + '&nbsp;a&nbsp;' + registroSuperiorE + '&nbsp;de&nbsp;' + numeroTotalRegistros + '&nbsp;<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.etiquetaEnlace2")%>.</font></center>'
                else
                  htmlString += '<center><font class="textoSuelto">&nbsp;' + numeroTotalRegistros  + '&nbsp;<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.etiquetaEnlace2")%>.</font></center>'

                return (htmlString);
            }
            
            function refrescaDisplayTablaErrors() {
                tablaErrores.displayTabla();
            }
            
            function cargaPagE(numeroPaginaE){
                paginaActualE = numeroPaginaE; 
                if(iniciaModulo!="1")
                    pulsarBusquedaFiltr();
                listaSelErr = new Array();
                for(var i = 0; i < listaSelErrOriginal.length; i++){
                    listaSelErr[i] = listaSelErrOriginal[i];
                }
                //numeroTotalRegistros=listaSelErrOriginal.length;

                inicializaLista(numeroPaginaE);
            }
            
            function calcularLimitesE(numeroPaginaE) {
                if(numeroPaginaE > paginaInferiorE +(enlacesPaginaE-1)) {
                  paginaInferiorE = numeroPaginaE;
                }
                var enlacePaginaE = Math.ceil(numeroPaginaE/enlacesPaginaE);
                var ultimaPantalla = Math.ceil(numeroPaginasE/enlacesPaginaE);
                var valorMaxPaginaE = 0;
                if(enlacePaginaE == 0) valorMaxPaginaE = enlacesPaginaE;
                else valorMaxPaginaE = enlacePaginaE * enlacesPaginaE;
                if(numeroPaginasE < valorMaxPaginaE && enlacePaginaE == ultimaPantalla) paginaSuperiorE = numeroPaginasE;
                else paginaSuperiorE = (numeroPaginaE-1) + enlacesPaginaE;
            }

            function irNPaginasAnterioresE(pagActualE){
                var incremento = enlacesPaginaE + (pagActualE - paginaInferiorE) ;
                  if (paginaInferiorE-1 <= 0)
                              pagActualE = 1;
                      else  pagActualE -= incremento;
                paginaInferiorE = pagActualE;
                paginaSuperiorE = paginaInferiorE + enlacesPaginaE-1;

                calcularLimitesE(pagActualE);
                cargaPagE(pagActualE);
            }

            function irNPaginasSiguientesE(pagActualE){
                pagActualE = parseInt(pagActualE);
                var incremento = paginaSuperiorE +1 - pagActualE;
                if (pagActualE + incremento > numeroPaginasE)
                    pagActualE = Math.ceil(numeroTotalRegistros/lineasPaginaE); // Ultima
                else {
                  pagActualE +=  incremento;
                  pagInferiorE = pagActualE;
                  if (paginaInferiorE + enlacesPaginaE > numeroPaginasE)
                      paginaSuperiorE=numeroPaginasE;
                  else paginaSuperiorE=paginaInferiorE+enlacesPaginaE-1;
                }

                calcularLimitesE(pagActualE);
                cargaPagE(pagActualE);
            }

            function irUltimaPaginaE() {
                paginaActualE   = Math.ceil(numeroTotalRegistros/lineasPaginaE);
                paginaInferiorE = 1;
                if (numeroPaginasE <= enlacesPaginaE)
                    paginaSuperiorE = numeroPaginasE;
                else {
                  paginaSuperiorE = enlacesPaginaE;
                  while (paginaActualE > paginaSuperiorE) {
                    paginaInferiorE = paginaSuperiorE +1;
                    if (numeroPaginasE > paginaInferiorE-1+enlacesPaginaE)
                      paginaSuperiorE = paginaInferiorE-1+enlacesPaginaE;
                    else paginaSuperiorE = numeroPaginasE;
                   }
                }
                cargaPagE(paginaActualE)
            }

            function irPrimeraPaginaE() {
                paginaActualE   = 1;
                paginaInferiorE = 1;
                if (numeroPaginasE <= enlacesPaginaE)
                    paginaSuperiorE = numeroPaginasE;
                  else paginaSuperiorE = enlacesPaginaE;
                cargaPagE(paginaActualE)
            }
            
            function buscar(){
                iniciaModulo = "0";
                paginaActualE = 1;
                cargaPagE(paginaActualE);
            }
            
            function buscaCodListaTipo (codListaTipo){
                comboTipo.buscaCodigo(codListaTipo);
            }
            function buscaCodListaCritico (codListaCritico){
                comboCritico.buscaCodigo(codListaCritico);
            }
            //fin paginación
            
            function tipo(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codListaTipo") != null){
                    var codigo = document.getElementById("codListaTipo").value;
                    buscaCodListaTipo(codigo);
                }
            }
            function critico(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codListaCritico") != null){
                    var codigo = document.getElementById("codListaCritico").value;
                    buscaCodListaCritico(codigo);
                }
            }
            var listaCritico = new Array();
            var listaCodigosCritico = new Array();
            listaCodigosCritico[0] = 1;
            listaCodigosCritico[1] = 2;
            listaCodigosCritico[2] = 3;
            listaCodigosCritico[3] = 4;
            listaCritico[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.bloq")%>';
            listaCritico[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.grave")%>';
            listaCritico[2] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.leve")%>';
            listaCritico[3] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.inf")%>';

            var listaTipo = new Array();
            var listaCodigosTipo = new Array();
            listaCodigosTipo[0] = 1;
            listaCodigosTipo[1] = 2;
            listaTipo[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.sist")%>';
            listaTipo[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.func")%>';
       </script>
           
           

        <!-- Estilos -->
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
       
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <!--script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script-->
        
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/lanbide.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/InputMask.js"></script>

        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>
        
    </head>
    <body class="bandaBody" >
        <form>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalId")%></h2>
            <div>
                <div style="clear: both;">
                    <div id="div_label" class="sub3titulo" style=" text-align: center;">
                        <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.busquedaId")%></label>
                    </div>
                    <br/>
                    <div>
                        <table id="tablaBusqueda">
                            <tbody>
                                <tr>
                                    <td style="width: 20%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.id.fk")%>
                                           
                                        </label>                            
                                    </td>
                                    <td>
                                        <input id="idError" name="idError" type="text" class="inputTextoM53" size="25" maxlength="25">
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 20%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.descripcionErrorCorta")%>
                                        </label>                            
                                    </td>
                                    <td>
                                        <input id="desc" name="desc" type="text" class="inputTextoM53" size="100" maxlength="100">
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="lineaFormulario">
                                            <div style="width: 20%; float: left;" class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.tipoError")%>
                                            </div>
                                            <div>
                                                <input type="text" name="codListaTipo" id="codListaTipo" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                                <input type="text" name="descListaTipo"  id="descListaTipo" size="45" class="inputTexto" readonly="true" value="" />
                                                <a href="" id="anchorListaTipo" name="anchorListaTipo">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="lineaFormulario">
                                            <div style="width: 20%; float: left;" class="etiqueta" >
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.criticError")%>
                                            </div>
                                            <div>
                                                <input type="text" name="codListaCritico" id="codListaCritico" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                                <input type="text" name="descListaCritico"  id="descListaCritico" size="45" class="inputTexto" readonly="true" value="" />
                                                <a href="" id="anchorListaCritico" name="anchorListaCritico">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
    <!--                                <td style="width: 15%"></td>-->
                                    <td style="text-align: center;" colspan="2">
                                        <input type="button" id="botonBusqueda" name="botonBusqueda" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBusquedaFiltr();">
    <!--                                    <IMG  id="botonBusqueda"  name="botonBusqueda"  style="CURSOR: hand; BORDER-TOP: 0px; BORDER-RIGHT: 0px; BORDER-BOTTOM: 0px; BORDER-LEFT: 0px; margin-right: 5px; " 
                                              src="/flexia/images/prismaticos.gif" onclick="pulsarBusquedaFiltrando()">-->
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style="clear: both;">
                    <div>
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.tablaId")%></label>
                        </div>    
                           
                        <div id="divGeneral" style="overflow-y: auto; overflow-x: auto; height: 390px;">     
                            <div id="listaErrores" style="padding: 5px; width:900px; height: 380px; text-align: center; overflow-x:auto; overflow-y:auto;margin:0px;margin-top:0px;" align="center"></div>
                        </div>
                        
                        <br/>
                        <div id="enlacesListIdErros" STYLE="width:100%; clear: both;">
                        </div>
                        <br/>
                        <br/>
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnNuevoAcceso" name="btnNuevoAcceso" class="botonGeneral"  value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoError();">
                            <input type="button" id="btnActualizarError" name="btnActualizarError" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarError();">
                            <input type="button" id="btnActualizarError" name="btnActualizarError" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarError();">
                            <input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();"/>   
                        </div>
                    </div>
                </div>
            </div>

            <!--Script Ejecucion Elementos Pagina   legendAzul-->
            <script type="text/javascript">
                
                //Combos
                var comboCritico = new Combo("ListaCritico");
                comboCritico.addItems(listaCodigosCritico, listaCritico);
                comboCritico.change = critico;
                
                
                var comboTipo = new Combo("ListaTipo");
                comboTipo.addItems(listaCodigosTipo, listaTipo);
                comboTipo.change = tipo;
              
                
                //Tabla Errores
                var tablaErrores;
                var listaErrores = new Array();
                var listaErroresTabla = new Array();

                //right - left - center
                tablaErrores = new Tabla(document.getElementById('listaErrores'), 1400);
                tablaErrores.addColumna('160','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col1")%>");
                tablaErrores.addColumna('300','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col2")%>");
                tablaErrores.addColumna('540','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col3")%>");
                tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col4")%>");
                tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col5")%>");
                //tablaErrores.addColumna('100','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col6")%>");
                //tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col7")%>");

                tablaErrores.displayCabecera=true;
                tablaErrores.height = 600;
              
                <%  		
                    DetalleErrorVO objectVO = new DetalleErrorVO();
                    List<DetalleErrorVO> _list = null;
                    if(request.getAttribute("ListErrores")!=null){
                        _list = (List<DetalleErrorVO>)request.getAttribute("ListErrores");
                    }													
                    if (_list!= null && _list.size() >0){
                        for (int indice=0;indice<_list.size();indice++)
                        {
                            objectVO = _list.get(indice);
                %>
                var descC = '<%=objectVO.getDescripcionCorta()%>';
                var desc = '<%=objectVO.getDescripcion()%>';
                var desTipo = '<%=objectVO.getDesTipo()%>';
                var desCritico = '<%=objectVO.getDesCritico()%>';
                    listaErrores[<%=indice%>] = ['<%=objectVO.getId()%>', descC,desc, desTipo, desCritico];
                    listaErroresTabla[<%=indice%>] = ['<%=objectVO.getId()%>', descC, desc, desTipo, desCritico];
                <%
                        }// for
                    }// if
                %>
                listaSelErrOriginal = listaErroresTabla;
                tablaErrores.lineas=listaErroresTabla;
                tablaErrores.displayTabla();
                //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                //            pulsarModificarError(event);
                if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                    try{
                        var div = document.getElementById('listaErrores');
                        //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                            div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].children[0].style.width = '100%';
                        }else{
                            div.children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].style.width = '100%';
                        }
                    }
                    catch (err) {

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">
                var mensajeValidacion="";
                function getXMLHttpRequest() {
                    var aVersions = ["MSXML2.XMLHttp.5.0",
                        "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                        "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                    ];

                    if (window.XMLHttpRequest) {
                        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                        return new XMLHttpRequest();
                    } else if (window.ActiveXObject) {
                        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                        for (var i = 0; i < aVersions.length; i++) {
                            try {
                                var oXmlHttp = new ActiveXObject(aVersions[i]);
                                return oXmlHttp;
                            } catch (error) {
                                //no necesitamos hacer nada especial
                            }
                        }
                    } else {
                        return null;
                    }
                }

                function comprobarFechaLanbide(inputFecha) {
                    var formato = 'dd/mm/yyyy';
                    if (Trim(inputFecha.value)!='') {
                      var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                      if (!D[0]){
                        jsp_alerta("A","<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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

                function mostrarCalFechaE(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_busqE").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_busqE',null,null,null,'','calMeLanbide53Fecha_busqE','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaE

                function mostrarCalFechaS(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_busqS").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_busqS',null,null,null,'','calMeLanbide53Fecha_busqS','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaS

                function mostrarCalFechaRevE(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_RevbusqE").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_RevbusqE',null,null,null,'','calMeLanbide53Fecha_RevbusqE','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaRevE

                function mostrarCalFechaRevF(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_RevbusqF").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_RevbusqF',null,null,null,'','calMeLanbide53Fecha_RevbusqF','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaRevF

                function formatearHora(control,evento){  
                    evento = (evento) ? evento : window.event;  
                    var charCode = (evento.which) ? evento.which : evento.keyCode;  

                    var ignore = evento.altKey || evento.ctrlKey || inArray(charCode, JST_IGNORED_KEY_CODES);
                    if (!ignore) {
                        var range = getInputSelectionRange(control);
                        if (range != null && range[0] != range[1]) {
                            replaceSelection(this, "");
                        }
                    }

                    if (!ignore) {  //charCode >= 48 && charCode <= 56 && control.value.length < 8
                        var i = control.value.length;  
                        var texto = "";  
                        if (i == 2 || i == 5) {
                            texto = control.value+":";  
                            control.value = texto;  
                        }  
                        return true;  
                    }  
                    return false;  
                }

                function comprobarMaskaraHora(control){
                    if(control!=null){
                        if(control.value==""){
                            return true;
                        }
                        var texto = control.value.split(':');
                        if(texto.length!=3){
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                            control.focus();
                            control.select();
                            return false;
                        }
                        if(!(texto[0]>=0 && texto[0]<=24)){
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                        }else{
                            if(texto[0].length<2){
                                texto[0]= '0'+texto[0];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }
                        }
                        if(!(texto[1]>=0 && texto[1]<=59) || !(texto[2]>=0 && texto[2]<=59)){
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                        }else{
                            if(texto[1].length<2){
                                texto[1]= '0'+texto[1];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }
                            if(texto[2].length<2){
                                texto[2]= '0'+texto[2];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }

                        }
                    }
                    else{
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                    }
                }

                function pulsarNuevoError() {
                    var control = new Date();
                    var result = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarNuevoError&tipo=0&nuevo=1&control='+control.getTime(), 760, 1100, 'no', 'no');
                    } else {
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarNuevoError&tipo=0&nuevo=1&control='+control.getTime(), 760, 1100, 'no', 'no');
                    }
                    if (result != undefined) {
                        if (result[0] == '0') {
                            //recargarTablaErrores(result);
                            cargaPagE(1);
                        }
                    }
                }

                function pulsarModificarError() {
                    if (tablaErrores.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarPantallaDetallesIDError&tipo=0&visible=true&nuevo=0&idError=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime(), 760, 1100, 'no', 'yes');
                        } else {
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarPantallaDetallesIDError&tipo=0&visible=true&nuevo=0&idError=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime(), 760, 1100, 'no', 'yes');
                        }
                        if (result != undefined) {
                            if (result[0] == '0') {
                                //recargarTablaErrores(result);
                                cargaPagE(1);
                            }
                        }
                    }
                    else {
                        jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }
                
                function pulsarEliminarError() {
                    if (tablaErrores.selectedIndex != -1) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=eliminarIdError&tipo=0&idError='+listaErrores[tablaErrores.selectedIndex][0]+'&control='+control.getTime();
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                            //var formData = new FormData(document.getElementById('formContrato'));
                            ajax.send(parametros);
                            if (ajax.readyState==4 && ajax.status==200){
                                var xmlDoc = null;
                                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                                    // En IE el XML viene en responseText y no en la propiedad responseXML
                                    var text = ajax.responseText;
                                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async="false";
                                    xmlDoc.loadXML(text);
                                }else{
                                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    xmlDoc = ajax.responseXML;
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var listaNueva = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            var nodoCampo;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaNueva[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                else if (hijos[j].nodeName == "FILA") { 
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                            listaNueva[j] = codigoOperacion;
                                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                        else if (hijos[j].nodeName == "FILA") {
                                            nodoFila = hijos[j];
                                            hijosFila = nodoFila.childNodes;
                                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                                if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[0] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[1] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[2] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "DES_TIPO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[3] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "DES_CRITICO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[4] = '-';
                                                    }
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                //recargarTablaErrores(listaNueva);
                                cargaPagE(1);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
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
                function pulsarObtenerDocs() {
                    if (tablaErrores.selectedIndex != -1) {
                        var control = new Date();
                        //var result = null;
                        var oidSolicitud=listaErrores[tablaErrores.selectedIndex][8];
                        var codProcedimiento=listaErrores[tablaErrores.selectedIndex][6];
                        if(validarDatosDescargaDocumentos(oidSolicitud,codProcedimiento)){
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                //result = 
                                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarObtenerDocumentos&tipo=0&nuevo=0&oidSolicitud=' + oidSolicitud + '&codProcedimiento=' + codProcedimiento + '&control=' + control.getTime(), 350, 800, 'no', 'no');
                            } else {
                                //result = 
                                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarObtenerDocumentos&tipo=0&nuevo=0&oidSolicitud=' + oidSolicitud + '&codProcedimiento=' + codProcedimiento + '&control=' + control.getTime(), 350, 800, 'no', 'no');
                            }
    //                        if (result != undefined) {
    //                            if (result[0] == '0') {
    //                                recargarTablaErrores(result);
    //                            }
    //                        }
                        }else{
                            jsp_alerta('A',mensajeValidacion);
                        }
                    }else {
                        jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }


                function recargarTablaErrores(result) {
                    var fila;
                    listaErrores = new Array();
                    listaErroresTabla = new Array();
                    for (var i = 1; i < result.length; i++) {
                        fila = result[i];
                        listaErrores[i - 1] = [fila[0],fila[1],fila[2],fila[3], fila[4]];
                               
                        listaErroresTabla[i - 1] = [fila[0],fila[1],fila[2],fila[3], fila[4]];
                    }
                    tablaErrores = new Tabla(document.getElementById('listaErrores'), 1400);
                    tablaErrores.addColumna('160','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col1")%>");
                    tablaErrores.addColumna('300','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col2")%>");
                    tablaErrores.addColumna('540','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col3")%>");
                    tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col4")%>");
                    tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col5")%>");
                 

                    tablaErrores.displayCabecera=true;
                    tablaErrores.height = 600;
                    listaSelErrOriginal = listaErroresTabla;
                    tablaErrores.lineas = listaErroresTabla;
                    tablaErrores.displayTabla();
                    //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                    //        pulsarModificarError(event);
                    if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                        try{
                            var div = document.getElementById('listaErrores');
                            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                            }else{
                                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].style.width = '100%';
                            }
                        }
                        catch (err) {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errorGen")%>' + ' - ' + err.message );
                        }
                    }
                }

                function pulsarBusquedaFiltr(){
                    if (true) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=busquedaIdenError&tipo=0&control=" + control.getTime()
                                        + "&idError="+ document.getElementById('idError').value
                                        + "&descripcion="+ document.getElementById('desc').value
                                        + "&codTipo="+ document.getElementById('codListaTipo').value
                                        + "&codCritico="+ document.getElementById('codListaCritico').value
                                        + "&paginaActualE="+ paginaActualE
                                        + "&lineasPaginaE="+ lineasPaginaE
                        ;
                        try {
                            ajax.open("POST", url, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                            //var formData = new FormData(document.getElementById('formContrato'));
                            ajax.send(parametros);
                            if (ajax.readyState == 4 && ajax.status == 200) {
                                var xmlDoc = null;
                                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                    // En IE el XML viene en responseText y no en la propiedad responseXML
                                    var text = ajax.responseText;
                                    xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async = "false";
                                    xmlDoc.loadXML(text);
                                } else {
                                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    xmlDoc = ajax.responseXML;
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var listaNueva = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaNueva[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                else if (hijos[j].nodeName == "FILA") {
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if (hijos[j].nodeName == "FILA") {
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for (var cont = 0; cont < hijosFila.length; cont++) {
                                            if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "DES_TIPO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "DES_CRITICO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[4] = '-';
                                                }
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if (codigoOperacion == "0") {
                                    recargarTablaErrores(listaNueva);
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }
                            }//if(
                        }
                        catch (Err) {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                        }//try-catch
                    }
                    else {
                        jsp_alerta('A',mensajeValidacion);
                    }
                }
                
                function prepararFiltroChecked(idElement){
                    if(document.getElementById(idElement)!=null && document.getElementById(idElement).checked){
                        return '<%=ConstantesMeLanbide53.BOOLEAN_TRUE%>'; 
                    }else 
                        return "";
                }
                
                function validarFiltrosBusqueda(){
                    var result = true;
                    if(document.getElementById("meLanbide53Fecha_busqE").value!="" && document.getElementById("meLanbide53Fecha_busqS").value!="")
                    {
                        var fechaentrada = document.getElementById("meLanbide53Fecha_busqE").value.split("/");
                        var fechasalida = document.getElementById("meLanbide53Fecha_busqS").value.split("/");
                        fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                        fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                        if(fechaentrada>fechasalida){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechaerror")%>';
                            document.getElementById("meLanbide53Fecha_busqE").select();
                            return false;
                        }

                    }
                    if(document.getElementById("hora_entrada_busq").value!="" && document.getElementById("hora_entrada_busqF").value!="")
                    {
                        var horaentrada = document.getElementById("hora_entrada_busq").value;
                        var horaentradaF = document.getElementById("hora_entrada_busqF").value;
                        if(horaentrada>horaentradaF){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervaloshora")%>';
                            document.getElementById("hora_entrada_busq").select();
                            return false;
                        }

                    }
                    if(document.getElementById("meLanbide53Fecha_RevbusqE").value!="" && document.getElementById("meLanbide53Fecha_RevbusqF").value!="")
                    {
                        var fechaentrada = document.getElementById("meLanbide53Fecha_RevbusqE").value.split("/");
                        var fechasalida = document.getElementById("meLanbide53Fecha_RevbusqF").value.split("/");
                        fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                        fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                        if(fechaentrada>fechasalida){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfecharevision")%>';
                            document.getElementById("meLanbide53Fecha_RevbusqE").select();
                            return false;
                        }

                    }
                    return result;
                }
                
                function validarDatosDescargaDocumentos(oidSolicitud, procedimiento){
                    var result = true;
                    var _oidSolicitud = oidSolicitud;
                    var _procedimiento = procedimiento;
                    if(_oidSolicitud != null && _oidSolicitud!="" && _oidSolicitud!="null" )
                    {
                        if(_procedimiento!=null && _procedimiento!="" && _procedimiento!="null"){
                            return true;
                        }else{
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinprocedimiento")%>';
                            return false;
                        }
                    }else{
                        mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinoid")%>';
                         return false;
                    }
                    return result;
                }
                
                
                
                if(iniciaModulo=="1"){ 
                    cargaPagE(1);
                    iniciaModulo="0";
                }
            </script>
        </form>
        <div id="popupcalendar" class="text"></div>   
    </body>
</html>
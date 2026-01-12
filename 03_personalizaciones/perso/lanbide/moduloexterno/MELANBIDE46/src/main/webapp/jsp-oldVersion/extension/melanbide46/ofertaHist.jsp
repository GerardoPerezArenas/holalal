<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaOfertaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
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
    String mensajeProgreso = "";
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide46/melanbide46.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript">   
    
    function dblClckTablaOfertasCme(rowID,tableName){
        pulsarModificarOfertaCme();
    }
    
    function pulsarModificarOfertaCme(){
        var fila;
        if(tabOfertasCme.selectedIndex != -1) {
            fila = tabOfertasCme.selectedIndex;
            var control = new Date();
            var result = null;
            var nodos = null;
            var opcion = '';
            opcion = 'consultar';
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCme[fila][0]+'&idPuesto='+listaOfertasCme[fila][1]+'&control='+control.getTime(),560,980,'yes','no');
            }else{
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCme[fila][0]+'&idPuesto='+listaOfertasCme[fila][1]+'&control='+control.getTime(),620,980,'yes','no');
            }
            if (nodos != undefined){
                result = extraerListadoOfertasCme(nodos);
                if(result != undefined){
                    if(result[0] == '0'){
                        recargarTablaOfertasCme(result);
                    }
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function recargarTablaOfertasCme(result){
        var fila;
        listaOfertasCme = new Array();
        listaOfertasCmeTabla = new Array();
        listaOfertasCmeTabla_titulos = new Array();
        listaOfertasCmeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCme(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaOfertasCme[i-2] = fila;
            listaOfertasCmeTabla[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            listaOfertasCmeTabla_titulos[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
        }

        tabOfertasCme = new FixedColumnTable(document.getElementById('ofertasCme'), 850, 876, 'ofertasCme');
        tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
        tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
        tabOfertasCme.addColumna('250','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
        tabOfertasCme.addColumna('80','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
        tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
        tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
        tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
        tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     

        tabOfertasCme.numColumnasFijas = 2;

        tabOfertasCme.displayCabecera=true;
    
        for(var cont = 0; cont < listaOfertasCmeTabla.length; cont++){
            tabOfertasCme.addFilaConFormato(listaOfertasCmeTabla[cont], listaOfertasCmeTabla_titulos[cont], listaOfertasCmeTabla_estilos[cont])
        }

        tabOfertasCme.height = '146';

        tabOfertasCme.altoCabecera = 50;

        tabOfertasCme.scrollWidth = 1200;

        tabOfertasCme.dblClkFunction = 'dblClckTablaOfertasCme';

        tabOfertasCme.displayTabla();

        tabOfertasCme.pack();
        
        actualizarOtrasPestanas('oferta');
    }
</script>
<body>
    <div id="barraProgresoPuestosOfertaCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">

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
    <div style="clear: both;">
        <div id="ofertasCme" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <input type="button" id="btnModificarOfertaCme" name="btnModificarOfertaCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarModificarOfertaCme();">
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabOfertasCme;
    var listaOfertasCme = new Array();
    var listaOfertasCmeTabla = new Array();
    var listaOfertasCmeTabla_titulos = new Array();
    var listaOfertasCmeTabla_estilos = new Array();

    tabOfertasCme = new FixedColumnTable(document.getElementById('ofertasCme'), 850, 876, 'ofertasCme');
    tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
    tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
    tabOfertasCme.addColumna('250','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
    tabOfertasCme.addColumna('80','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
    tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
    tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
    tabOfertasCme.addColumna('60','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
    tabOfertasCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     
    
    tabOfertasCme.numColumnasFijas = 2;

    tabOfertasCme.displayCabecera=true;
    
    <%  		
        FilaOfertaVO oferta = null;
        List<FilaOfertaVO> listaOfertas = (List<FilaOfertaVO>)request.getAttribute("ofertas");													
        if (listaOfertas != null && listaOfertas.size() >0){
            for(int i = 0;i < listaOfertas.size();i++)
            {
                oferta = listaOfertas.get(i);
    %>
        listaOfertasCme[<%=i%>] = ['<%=oferta.getIdOferta()%>', '<%=oferta.getCodPuesto()%>', '<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCmeTabla[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCmeTabla_titulos[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaOfertasCmeTabla.length; cont++){
        tabOfertasCme.addFilaConFormato(listaOfertasCmeTabla[cont], listaOfertasCmeTabla_titulos[cont], listaOfertasCmeTabla_estilos[cont])
    }
    
    tabOfertasCme.height = '146';
    
    tabOfertasCme.altoCabecera = 50;
    
    tabOfertasCme.scrollWidth = 1200;
    
    tabOfertasCme.dblClkFunction = 'dblClckTablaOfertasCme';
    
    tabOfertasCme.displayTabla();
    
    tabOfertasCme.pack();
</script>
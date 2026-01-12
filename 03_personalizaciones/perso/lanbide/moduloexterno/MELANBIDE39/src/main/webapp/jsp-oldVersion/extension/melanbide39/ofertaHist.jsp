<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaOfertaVO" %>
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
    MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide39/melanbide39.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript">   
    
    function dblClckTablaOfertasCpe(rowID,tableName){
        pulsarModificarOfertaCpe();
    }
    
    function pulsarModificarOfertaCpe(){
        var fila;
        if(tabOfertasCpe.selectedIndex != -1) {
            fila = tabOfertasCpe.selectedIndex;
            var control = new Date();
            var result = null;
            var nodos = null;
            var opcion = '';
            opcion = 'consultar';
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCpe[fila][0]+'&idPuesto='+listaOfertasCpe[fila][1]+'&control='+control.getTime(),560,980,'yes','no');
            }else{
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCpe[fila][0]+'&idPuesto='+listaOfertasCpe[fila][1]+'&control='+control.getTime(),620,980,'yes','no');
            }
            if (nodos != undefined){
                result = extraerListadoOfertasCpe(nodos);
                if(result != undefined){
                    if(result[0] == '0'){
                        recargarTablaOfertasCpe(result);
                    }
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function recargarTablaOfertasCpe(result){
        var fila;
        listaOfertasCpe = new Array();
        listaOfertasCpeTabla = new Array();
        listaOfertasCpeTabla_titulos = new Array();
        listaOfertasCpeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCpe(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaOfertasCpe[i-2] = fila;
            listaOfertasCpeTabla[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            listaOfertasCpeTabla_titulos[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
        }

        tabOfertasCpe = new FixedColumnTable(document.getElementById('ofertasCpe'), 850, 876, 'ofertasCpe');
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
        tabOfertasCpe.addColumna('250','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
        tabOfertasCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     

        tabOfertasCpe.numColumnasFijas = 2;

        tabOfertasCpe.displayCabecera=true;
    
        for(var cont = 0; cont < listaOfertasCpeTabla.length; cont++){
            tabOfertasCpe.addFilaConFormato(listaOfertasCpeTabla[cont], listaOfertasCpeTabla_titulos[cont], listaOfertasCpeTabla_estilos[cont])
        }

        tabOfertasCpe.height = '146';

        tabOfertasCpe.altoCabecera = 50;

        tabOfertasCpe.scrollWidth = 1200;

        tabOfertasCpe.dblClkFunction = 'dblClckTablaOfertasCpe';

        tabOfertasCpe.displayTabla();

        tabOfertasCpe.pack();
        
        actualizarOtrasPestanas('oferta');
    }
</script>
<body>
    <div id="barraProgresoPuestosOfertaCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
        <div id="ofertasCpe" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <input type="button" id="btnModificarOfertaCpe" name="btnModificarOfertaCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarModificarOfertaCpe();">
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabOfertasCpe;
    var listaOfertasCpe = new Array();
    var listaOfertasCpeTabla = new Array();
    var listaOfertasCpeTabla_titulos = new Array();
    var listaOfertasCpeTabla_estilos = new Array();

    tabOfertasCpe = new FixedColumnTable(document.getElementById('ofertasCpe'), 850, 876, 'ofertasCpe');
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
    tabOfertasCpe.addColumna('250','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
    tabOfertasCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     
    
    tabOfertasCpe.numColumnasFijas = 2;

    tabOfertasCpe.displayCabecera=true;
    
    <%  		
        FilaOfertaVO oferta = null;
        List<FilaOfertaVO> listaOfertas = (List<FilaOfertaVO>)request.getAttribute("ofertas");													
        if (listaOfertas != null && listaOfertas.size() >0){
            for(int i = 0;i < listaOfertas.size();i++)
            {
                oferta = listaOfertas.get(i);
    %>
        listaOfertasCpe[<%=i%>] = ['<%=oferta.getIdOferta()%>', '<%=oferta.getCodPuesto()%>', '<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCpeTabla[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCpeTabla_titulos[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaOfertasCpeTabla.length; cont++){
        tabOfertasCpe.addFilaConFormato(listaOfertasCpeTabla[cont], listaOfertasCpeTabla_titulos[cont], listaOfertasCpeTabla_estilos[cont])
    }
    
    tabOfertasCpe.height = '146';
    
    tabOfertasCpe.altoCabecera = 50;
    
    tabOfertasCpe.scrollWidth = 1200;
    
    tabOfertasCpe.dblClkFunction = 'dblClckTablaOfertasCpe';
    
    tabOfertasCpe.displayTabla();
    
    tabOfertasCpe.pack();
</script>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaJustificacionVO" %>
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
    MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide59/melanbide59.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">   
    
    function recargarTablaJustificacionesCpe(result){
        var fila;
        tabJustifCpe = new Array();
        listaJustifCpeTabla = new Array();
        listaJustifCpeTabla_titulos = new Array();
        listaJustifCpeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCpe(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaJustifCpe[i-2] = fila;
            listaJustifCpeTabla[i-2] = [fila[3], fila[4], fila[5], fila[6]];
            listaJustifCpeTabla_titulos[i-2] = [fila[3], fila[4], fila[5], fila[6]];
        }

        tabJustifCpe = new FixedColumnTable(document.getElementById('justificacionesCpe'), 850, 876, 'justificacionesCpe');
        tabJustifCpe.addColumna('418','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
        tabJustifCpe.addColumna('130','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
        tabJustifCpe.addColumna('130','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
        tabJustifCpe.addColumna('170','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");   
        //tabJustifCpe.addColumna('200','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col5")%>");         

        tabJustifCpe.numColumnasFijas = 0;

        tabJustifCpe.displayCabecera=true;
    
        for(var cont = 0; cont < listaJustifCpeTabla.length; cont++){
            tabJustifCpe.addFilaConFormato(listaJustifCpeTabla[cont], listaJustifCpeTabla_titulos[cont], listaJustifCpeTabla_estilos[cont])
        }

        tabJustifCpe.height = '146';

        tabJustifCpe.altoCabecera = 50;

        tabJustifCpe.scrollWidth = 1200;

        tabJustifCpe.dblClkFunction = 'dblClckTablaJustifCpe';

        tabJustifCpe.displayTabla();

        tabJustifCpe.pack();
    }
    
    function dblClckTablaJustifCpe(rowID,tableName){
        pulsarModificarJustifCpe();
    }
    
    function pulsarModificarJustifCpe(){
        var fila;
        if(tabJustifCpe.selectedIndex != -1) {
            fila = tabJustifCpe.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = 'consultar';
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCpe[fila][0]+'&idPuesto='+listaJustifCpe[fila][1]+'&control='+control.getTime(),740,980,'yes','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCpe[fila][0]+'&idPuesto='+listaJustifCpe[fila][1]+'&control='+control.getTime(),800,980,'yes','no');
            }
            if (result != undefined){
                //recargarTablaJustificacionesCpe(result);
                recargarCalculosCpe(result);
                actualizarPestanaJustificacion();
            }
        }else{
                jsp_alerta('A', '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
</script>
<body>
    <div id="barraProgresoPuestosJustifCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
        <div id="justificacionesCpe" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <input type="button" id="btnModificarJustifCpe" name="btnModificarJustifCpe" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarModificarJustifCpe();">
        </div>
    </div>
</body>
<script type="text/javascript">
    var tabJustifCpe;
    var listaJustifCpe = new Array();
    var listaJustifCpeTabla = new Array();
    var listaJustifCpeTabla_titulos = new Array();
    var listaJustifCpeTabla_estilos = new Array();

    tabJustifCpe = new FixedColumnTable(document.getElementById('justificacionesCpe'), 850, 876, 'justificacionesCpe');
    tabJustifCpe.addColumna('418','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
    tabJustifCpe.addColumna('130','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
    tabJustifCpe.addColumna('130','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
    tabJustifCpe.addColumna('170','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");     
    //tabJustifCpe.addColumna('200','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col5")%>");     
    
    tabJustifCpe.numColumnasFijas = 0;

    tabJustifCpe.displayCabecera=true;
    
    <%  		
        FilaJustificacionVO justif = null;
        List<FilaJustificacionVO> listaJustif = (List<FilaJustificacionVO>)request.getAttribute("justificaciones");													
        if (listaJustif != null && listaJustif.size() >0){
            for(int i = 0;i < listaJustif.size();i++)
            {
                justif = listaJustif.get(i);
    %>
        listaJustifCpe[<%=i%>] = ['<%=justif.getIdJustificacion()%>','<%=justif.getCodPuesto()%>', <%=justif.getIdOferta()%>,'<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>', '<%=justif.getEstado()%>'];
        listaJustifCpeTabla[<%=i%>] = ['<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>'];
        listaJustifCpeTabla_titulos[<%=i%>] = ['<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaJustifCpeTabla.length; cont++){
        tabJustifCpe.addFilaConFormato(listaJustifCpeTabla[cont], listaJustifCpeTabla_titulos[cont], listaJustifCpeTabla_estilos[cont])
    }
    
    tabJustifCpe.height = '146';
    
    tabJustifCpe.altoCabecera = 50;
    
    tabJustifCpe.scrollWidth = 850;
    
    tabJustifCpe.dblClkFunction = 'dblClckTablaJustifCpe';
    
    tabJustifCpe.displayTabla();
    
    tabJustifCpe.pack();
</script>
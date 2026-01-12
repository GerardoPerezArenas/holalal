<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaJustificacionVO" %>
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
    
    function recargarTablaJustificacionesCme(result){
        var fila;
        tabJustifCme = new Array();
        listaJustifCmeTabla = new Array();
        listaJustifCmeTabla_titulos = new Array();
        listaJustifCmeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCme(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaJustifCme[i-2] = fila;
            listaJustifCmeTabla[i-2] = [fila[3], fila[4], fila[5], fila[6]];
            listaJustifCmeTabla_titulos[i-2] = [fila[3], fila[4], fila[5], fila[6]];
        }

        tabJustifCme = new FixedColumnTable(document.getElementById('justificacionesCme'), 850, 876, 'justificacionesCme');
        tabJustifCme.addColumna('418','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
        tabJustifCme.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
        tabJustifCme.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
        tabJustifCme.addColumna('170','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");   
        //tabJustifCme.addColumna('200','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col5")%>");         

        tabJustifCme.numColumnasFijas = 0;

        tabJustifCme.displayCabecera=true;
    
        for(var cont = 0; cont < listaJustifCmeTabla.length; cont++){
            tabJustifCme.addFilaConFormato(listaJustifCmeTabla[cont], listaJustifCmeTabla_titulos[cont], listaJustifCmeTabla_estilos[cont])
        }

        tabJustifCme.height = '146';

        tabJustifCme.altoCabecera = 50;

        tabJustifCme.scrollWidth = 1200;

        tabJustifCme.dblClkFunction = 'dblClckTablaJustifCme';

        tabJustifCme.displayTabla();

        tabJustifCme.pack();
    }
    
    function dblClckTablaJustifCme(rowID,tableName){
        pulsarModificarJustifCme();
    }
    
    function pulsarModificarJustifCme(){
        var fila;
        if(tabJustifCme.selectedIndex != -1) {
            fila = tabJustifCme.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = 'consultar';
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCme[fila][0]+'&idPuesto='+listaJustifCme[fila][1]+'&control='+control.getTime(),740,980,'yes','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCme[fila][0]+'&idPuesto='+listaJustifCme[fila][1]+'&control='+control.getTime(),800,980,'yes','no');
            }
            if (result != undefined){
                //recargarTablaJustificacionesCme(result);
                recargarCalculosCme(result);
                actualizarPestanaJustificacion();
            }
        }else{
                jsp_alerta('A', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
</script>
<body>
    <div id="barraProgresoPuestosJustifCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
        <div id="justificacionesCme" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <input type="button" id="btnModificarJustifCme" name="btnModificarJustifCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarModificarJustifCme();">
        </div>
    </div>
</body>
<script type="text/javascript">
    var tabJustifCme;
    var listaJustifCme = new Array();
    var listaJustifCmeTabla = new Array();
    var listaJustifCmeTabla_titulos = new Array();
    var listaJustifCmeTabla_estilos = new Array();

    tabJustifCme = new FixedColumnTable(document.getElementById('justificacionesCme'), 850, 876, 'justificacionesCme');
    tabJustifCme.addColumna('418','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
    tabJustifCme.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
    tabJustifCme.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
    tabJustifCme.addColumna('170','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");     
    //tabJustifCme.addColumna('200','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col5")%>");     
    
    tabJustifCme.numColumnasFijas = 0;

    tabJustifCme.displayCabecera=true;
    
    <%  		
        FilaJustificacionVO justif = null;
        List<FilaJustificacionVO> listaJustif = (List<FilaJustificacionVO>)request.getAttribute("justificaciones");													
        if (listaJustif != null && listaJustif.size() >0){
            for(int i = 0;i < listaJustif.size();i++)
            {
                justif = listaJustif.get(i);
    %>
        listaJustifCme[<%=i%>] = ['<%=justif.getIdJustificacion()%>','<%=justif.getCodPuesto()%>', <%=justif.getIdOferta()%>,'<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>', '<%=justif.getEstado()%>'];
        listaJustifCmeTabla[<%=i%>] = ['<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>'];
        listaJustifCmeTabla_titulos[<%=i%>] = ['<%=justif.getDescPuesto()%>', '<%=justif.getImpSolic()%>', '<%=justif.getImpJustif()%>', '<%=justif.getNumContrataciones()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaJustifCmeTabla.length; cont++){
        tabJustifCme.addFilaConFormato(listaJustifCmeTabla[cont], listaJustifCmeTabla_titulos[cont], listaJustifCmeTabla_estilos[cont])
    }
    
    tabJustifCme.height = '146';
    
    tabJustifCme.altoCabecera = 50;
    
    tabJustifCme.scrollWidth = 850;
    
    tabJustifCme.dblClkFunction = 'dblClckTablaJustifCme';
    
    tabJustifCme.displayTabla();
    
    tabJustifCme.pack();
</script>
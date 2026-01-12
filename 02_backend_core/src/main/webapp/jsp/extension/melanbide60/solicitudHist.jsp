<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.i18n.MeLanbide60I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaPuestoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
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
    {

    }
    //Clase para internacionalizar los mensajes de la aplicacion.
    MeLanbide60I18n meLanbide60I18n = MeLanbide60I18n.getInstance();

    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide60/melanbide60.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide60/FixedColumnsTable.js"></script>
<script type="text/javascript">   
  function dblClckTablaPuestosCme(rowID,tableName){
        pulsarModificarPuestoCme();
    }
    function pulsarModificarPuestoCme(){
        var fila;
        if(tabPuestosCme.selectedIndex != -1) {
            fila = tabPuestosCme.selectedIndex;
            var control = new Date();
            var opcion = '';
            opcion = 'consultar';
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE60&operacion=cargarModificarPuesto&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPuesto='+listaPuestosCme[fila][0]+'&control='+control.getTime(),600,980,'yes','no', function(result){
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaPuestosCme(result);
                }
            }
			});
        }else{
                jsp_alerta('A', '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function recargarTablaPuestosCme(result){
        var fila;
        listaPuestosCme = new Array();
        listaPuestosCmeTabla = new Array();
        listaPuestosCmeTabla_titulos = new Array();
        listaPuestosCmeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCme(fila);
        var titulas;
        
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            
            titulas = fila[4][0] != null && fila[4][0] != '' ? fila[4][0].toUpperCase() : '';
            titulas += fila[4][1] != null && fila[4][1] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[4][1] != null && fila[4][1] != '' ? fila[4][1].toUpperCase() : '';
            titulas += fila[4][2] != null && fila[4][2] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[4][2] != null && fila[4][2] != '' ? fila[4][2].toUpperCase() : '';
            
            if(titulas == null || titulas == ''){
                    titulas = "-";
                }
                if(titulas.length > 16){
                    if(fila[4][0] != null && fila[4][0] != ''){
                        titulas = fila[4][0].toUpperCase();
                    }else if(fila[4][1] != null && fila[4][1] != ''){
                        titulas = fila[4][1].toUpperCase();
                    }else if(fila[4][2] != null && fila[4][2] != ''){
                        titulas = fila[4][2].toUpperCase();
                    }
                    if(titulas.length > 16){
                        titulas = titulas.substring(0, 16);
                    }
                    titulas = titulas+"...";
                }
            
            listaPuestosCme[i-2] = fila;
            listaPuestosCmeTabla[i-2] = [fila[1], fila[2], fila[3], titulas, fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
            listaPuestosCmeTabla_titulos[i-2] = [fila[1], fila[2], fila[3], getListAsTextCme(fila[4]), fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
        }

        //tabPuestosCme = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('puestosCme'), 890);
        tabPuestosCme = new FixedColumnTable(document.getElementById('puestosCme'), 850, 876, 'puestosCme');
        tabPuestosCme.addColumna('180','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
        tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
        tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");    
        tabPuestosCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
        tabPuestosCme.addColumna('140','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
        tabPuestosCme.addColumna('80','right',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");   
        tabPuestosCme.addColumna('100','right',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");  
        tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
        tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     
        tabPuestosCme.addColumna('200','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10.title")%>");     

        tabPuestosCme.numColumnasFijas = 1;
        
        for(var cont = 0; cont < listaPuestosCmeTabla.length; cont++){
            tabPuestosCme.addFilaConFormato(listaPuestosCmeTabla[cont], listaPuestosCmeTabla_titulos[cont], listaPuestosCmeTabla_estilos[cont])
        }

        tabPuestosCme.displayCabecera=true;
        tabPuestosCme.height = 146;
    
        tabPuestosCme.altoCabecera = 50;

        tabPuestosCme.scrollWidth = 1330;

        tabPuestosCme.dblClkFunction = 'dblClckTablaPuestosCme';

        tabPuestosCme.displayTabla();

        tabPuestosCme.pack();
        
        actualizarOtrasPestanas('justif');
    }
</script>
<body>
    <div id="barraProgresoPuestosSolicitudCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
        <div>
            <div id="puestosCme" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        </div>
        <div class="botonera">
                <input type="button" id="btnModificarPuestoCme" name="btnModificarPuestoCme" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarModificarPuestoCme();">
            </div>
    </div>
</body>

<script type="text/javascript">   
 
   var tabPuestosCme;
    var listaPuestosCme = new Array();
    var listaPuestosCmeTabla = new Array();
    var listaPuestosCmeTabla_titulos = new Array();
    var listaPuestosCmeTabla_estilos = new Array();

    //tabPuestosCme = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('puestosCme'), 890);

    tabPuestosCme = new FixedColumnTable(document.getElementById('puestosCme'), 850, 876, 'puestosCme');
    tabPuestosCme.addColumna('180','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
    tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
    tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");    
    tabPuestosCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
    tabPuestosCme.addColumna('140','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
    tabPuestosCme.addColumna('80','right',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");   
    tabPuestosCme.addColumna('100','right',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");   
    tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
    tabPuestosCme.addColumna('130','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     
    tabPuestosCme.addColumna('200','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10")%>", "<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10.title")%>");     
    
    tabPuestosCme.numColumnasFijas = 1;

    tabPuestosCme.displayCabecera=true;
    
    var listaTitulaciones = new Array();
    
    <%  		
        FilaPuestoVO voP = null;
        List<FilaPuestoVO> listaPuestos = (List<FilaPuestoVO>)request.getAttribute("puestos");													
        if (listaPuestos != null && listaPuestos.size() >0){
            for(int i = 0;i < listaPuestos.size();i++)
            {
                voP = listaPuestos.get(i);
                
                
                String titulacion = voP.getTitulacion1() != null && !voP.getTitulacion1().equals("") ? voP.getTitulacion1().toUpperCase() : "";
                titulacion += voP.getTitulacion2() != null && !voP.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? "<br/>" : "") : "";
                titulacion += voP.getTitulacion2() != null && !voP.getTitulacion2().equals("") ? voP.getTitulacion2().toUpperCase() : "";
                titulacion += voP.getTitulacion3() != null && !voP.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? "<br/>" : "") : "";
                titulacion += voP.getTitulacion3() != null && !voP.getTitulacion3().equals("") ? voP.getTitulacion3().toUpperCase() : "";
                
                if(titulacion == null || titulacion.equals("")){
                    titulacion = "-";
                }
                if(titulacion.length() > 16){
                    if(voP.getTitulacion1() != null && !voP.getTitulacion1().equals("")){
                        titulacion = voP.getTitulacion1().toUpperCase() ;
                    }
                    else if(voP.getTitulacion2() != null && !voP.getTitulacion2().equals("")){
                        titulacion = voP.getTitulacion2().toUpperCase() ;
                    }
                    else if(voP.getTitulacion3() != null && !voP.getTitulacion3().equals("")){
                        titulacion = voP.getTitulacion3().toUpperCase() ;
                    }
                    if(titulacion.length() > 16){
                        titulacion = titulacion.substring(0, 16);
                    }
                    titulacion = titulacion+"...";
                }
    %>
        listaTitulaciones = new Array();
        listaTitulaciones[0] = '<%=voP.getTitulacion1()%>';
        listaTitulaciones[1] = '<%=voP.getTitulacion2()%>';
        listaTitulaciones[2] = '<%=voP.getTitulacion3()%>';
        listaPuestosCme[<%=i%>] = ['<%=voP.getCodPuesto()%>','<%=voP.getDescPuesto()%>','<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCmeTabla[<%=i%>] = ['<%=voP.getDescPuesto()%>','<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCmeTabla_titulos[<%=i%>] = ['<%=voP.getDescPuesto()%>','<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', getListAsTextCme(listaTitulaciones), '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaPuestosCmeTabla.length; cont++){
        tabPuestosCme.addFilaConFormato(listaPuestosCmeTabla[cont], listaPuestosCmeTabla_titulos[cont], listaPuestosCmeTabla_estilos[cont])
    }
    
    tabPuestosCme.height = '146';
    
    tabPuestosCme.altoCabecera = 50;
    
    tabPuestosCme.scrollWidth = 1330;
    
    tabPuestosCme.dblClkFunction = 'dblClckTablaPuestosCme';
    
    tabPuestosCme.displayTabla();
    
    tabPuestosCme.pack();
</script>

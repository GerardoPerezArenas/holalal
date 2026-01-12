<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaJustificacionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
    
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
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide59/melanbide59.css'/>">

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide59/FixedColumnsTable2.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">   
    function pulsarModificarJustifCpe(){
        var fila;
        if(tabJustifCpe.selectedIndex != -1) {
            fila = tabJustifCpe.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCpe[fila][0]+'&idPuesto='+listaJustifCpe[fila][1]+'&control='+control.getTime(),740,980,'yes','no',function(result){
                                if (result != undefined){
                                    recargarCalculosCpe(result);
                                    actualizarPestanaJustificacion();
                                }
			});
            }else{
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarModificarJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaJustifCpe[fila][0]+'&idPuesto='+listaJustifCpe[fila][1]+'&control='+control.getTime(),800,980,'yes','no',function(result){
            if (result != undefined){
                recargarCalculosCpe(result);
                actualizarPestanaJustificacion();
            }
			});
            }
        }else{
                jsp_alerta('A', '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
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
            //listaJustifCpe[i-2] = fila;
			listaJustifCpe[i-2] =[fila[0],fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7]];
            listaJustifCpeTabla[i-2] = [fila[3], fila[4], fila[5], fila[6]];
            listaJustifCpeTabla_titulos[i-2] = [fila[3], fila[4], fila[5], fila[6]];
        }

        tabJustifCpe = new FixedColumnTable2(document.getElementById('justificacionesCpe'), 677, 876, 'justificacionesCpe');
        tabJustifCpe.addColumna('200','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
        tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
        tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
        tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");   
        //tabJustifCpe.addColumna('200','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col5")%>");         

        tabJustifCpe.numColumnasFijas = 0;

        tabJustifCpe.displayCabecera=true;
    
        for(var cont = 0; cont < listaJustifCpeTabla.length; cont++){
            tabJustifCpe.addFilaConFormato(listaJustifCpeTabla[cont], listaJustifCpeTabla_titulos[cont], listaJustifCpeTabla_estilos[cont])
        }

        tabJustifCpe.height = '146';

        tabJustifCpe.altoCabecera = 50;

        tabJustifCpe.scrollWidth = 677;

        tabJustifCpe.dblClkFunction = 'dblClckTablaJustifCpe';

        tabJustifCpe.displayTabla();

        tabJustifCpe.pack();
        
        actualizarOtrasPestanas('resumen');
    }
    
    function dblClckTablaJustifCpe(rowID,tableName){
        pulsarModificarJustifCpe();
    }
    
    function actualizarPestanaJustificacion(){
        try{
            var nodos = this.getListaJustificacionesCpe();
            var result = this.extraerListadoJustificacionesCpe(nodos);
            this.recargarTablaJustificacionesCpe(result);
        }catch(err){
            
        }
    }
    
    function getListaJustificacionesCpe(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE59&operacion=getListaJustificacionesNoDenegadasPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
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
        }
        catch(err){
            alert(err);
            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
    function extraerListadoJustificacionesCpe(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaJustif = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCampo;
            var nodoCalculos;
            var hijosCalculos;
            var j;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaJustif[j] = codigoOperacion;
                }                  
                else if(hijos[j].nodeName=="CALCULOS"){
                    nodoCalculos = hijos[j];
                    hijosCalculos = nodoCalculos.childNodes;
                    for(var cont = 0; cont < hijosCalculos.length; cont++){
                        if(hijosCalculos[cont].nodeName=="IMP_SOL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_CONV"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PREV_CON"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_CON"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_JUS"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REN"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG_2"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REI"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_SOLIC"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_CONCE"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="MINIMIS_SOLIC"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[11] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="MINIMIS_CONCE"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[12] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_DESP"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[13] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[13] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_BAJA"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[14] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[14] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="CONCEDIDO_REAL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[15] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[15] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="PAGADO_REAL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[16] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[16] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="PAGADO_REAL_2"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[17] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[17] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_NO_JUS"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[18] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[18] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REN_RES"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[19] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[19] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG_RES"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[20] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[20] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_JUS2"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[21] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[21] = '-';
                            }
                        }
                        
                    }
                    listaJustif[j] = fila; 

                    fila = new Array();  
                }else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID_JUSTIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="COD_PUESTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ID_OFERTA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESC_PUESTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMP_SOLIC"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMP_JUSTIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NUM_CONTRATACIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ESTADO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                    }
                    listaJustif[j] = fila;
                    fila = new Array();   
                }
        } 
        return listaJustif;
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
                                            <span id="msgCargandoPuestoJustifCpe">
                                                <%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoJustifCpe">
                                                <%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
    <div style="clear: both;">
        <div id="justificacionesCpe" style="padding: 5px; width:51.9%; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <!--<input type="button" id="btnNuevaJustificacionCpe" name="btnNuevaJustificacionCpe" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaJustifCpe();">-->
            <input type="button" id="btnModificarJustifCpe" name="btnModificarJustifCpe" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarJustifCpe();">
            <!--<input type="button" id="btnEliminaJustifCpe" name="btnEliminaJustifCpe" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarJustifCpe();">-->
        </div>
    </div>
</body>
<script type="text/javascript">
    var tabJustifCpe;
    var listaJustifCpe = new Array();
    var listaJustifCpeTabla = new Array();
    var listaJustifCpeTabla_titulos = new Array();
    var listaJustifCpeTabla_estilos = new Array();

    tabJustifCpe = new FixedColumnTable2(document.getElementById('justificacionesCpe'), 677, 876, 'justificacionesCpe');
    tabJustifCpe.addColumna('200','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col1")%>");
    tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col2")%>");    
    tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col3")%>");   
    tabJustifCpe.addColumna('150','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.justif.col4")%>");     
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
    
    tabJustifCpe.scrollWidth = 677;
    
    tabJustifCpe.dblClkFunction = 'dblClckTablaJustifCpe';
    
    tabJustifCpe.displayTabla();
    
    tabJustifCpe.pack();
</script>

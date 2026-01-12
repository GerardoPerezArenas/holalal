<%@page import="es.altia.flexia.integracion.moduloexterno.meikus.vo.ConvocatoriaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<%
    int idioma=1;
    int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if(usuario!=null){
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }//if(usuario!=null)
    }//if (session!=null)

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacion");
    String nombreModulo     = request.getParameter("nombreModulo");
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter"
type="es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter" />

<SCRIPT type="text/javascript">
  
    var listaConvocatorias = new Array();
    var nombreModulo = "MEIKUS01";
  
    function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0", "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp", "Microsoft.XMLHttp"];
        if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for(var i = 0; i < aVersions.length; i++){
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    }catch (error) {
                    //no necesitamos hacer nada especial
                    }
            }//for(var i = 0; i < aVersions.length; i++)
        }//if
    }//getXMLHttpRequest
    
    function pulsarAñadirConvocatoria() {
        //var nCS = "altaDesdeProcedimiento";
        var argumentos = new Array();
            argumentos[0] = '<%=codProcedimiento%>';
            argumentos[1] = '<%=codOrganizacion%>';

        <%--var source = "<%=request.getContextPath() %>/sge/PeticionModuloIntegracion.do?opcion=inicio&nCS=/>"+nCS;--%>
        var oReturnValue = window.showModalDialog('<%=request.getContextPath()%>/jsp/extension/meikus01/altaConvocatoria.jsp',argumentos,'dialogWidth:850px;dialogHeight:310px;status:'+ '<%=statusBar%>' + ';');

        //obtener los datos del popup
        if((oReturnValue.convocatoria != '') && (oReturnValue.plurianualidades != '') && (oReturnValue.justificacion != '') 
            && (oReturnValue.numPagos != '') && (oReturnValue.numAnos != '')){
            var lista = new Array();
            lista = 
                [oReturnValue.convocatoria,oReturnValue.plurianualidades,oReturnValue.numAnos,oReturnValue.justificacion,oReturnValue.numPagos];
            altaConvocatoria(lista);
        }else{
            if (oReturnValue.convocatoriaExiste != ''){
                jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
            }//if (oReturnValue.convocatoriaExiste != '')
            if (oReturnValue.camposConvocatoriaVacios != ''){
                jsp_alerta('A','<%=descriptor.getDescripcion("msjObligTodos")%>');
            }//if (oReturnValue.camposConvocatoriaVacios != '')
            if (oReturnValue.errorInsertarConvocatori != ''){
                jsp_alerta("A",'<%=configuracion.getParameter("MSG_ERROR_ALTA_CONVOCATORIA_INCORRECTA_" + idioma,nombreModulo)%>');
            }//if (oReturnValue.errorInsertarConvocatori != '')
        }/*if((oReturnValue.convocatoria != '') && (oReturnValue.plurianualidades != '') && (oReturnValue.justificacion != '') 
            && (oReturnValue.numPagos != '') && (oReturnValue.numAnos != ''))*/
    }//pulsarAñadirConvocatoria
    
    function altaConvocatoria(lista) {
        var lineas = tabConvocatorias.lineas;
            listaConvocatorias = tabConvocatorias.lineas;
        for (i=0; i < lineas.length; i++) {
        }
        listaConvocatorias[i]=[lista[0],lista[1],lista[2],lista[3],lista[4]];
        tabConvocatorias.lineas=listaConvocatorias;
        refresConvocatorias();
    }//altaConvocatoria

    function pulsarEliminarConvocatoria() {
        codProcedimiento = '<%=codProcedimiento%>';
        codOrganizacion = '<%=codOrganizacion%>';
        listaConvocatorias = tabConvocatorias.lineas;
     
        if (tabConvocatorias.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
            var eliminado = tabConvocatorias.selectedIndex;
            //registro a eliminar
            var regEliminar = new Array();
            regEliminar = tabConvocatorias.lineas[eliminado];
            convocatoria = regEliminar[0];
            plurianualidades = regEliminar[1];
            numAnos = regEliminar[2];
            justificacion = regEliminar[3];
            numPagos = regEliminar[4];
            var ajax = getXMLHttpRequest();
            
            if(ajax!=null){
                var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
                var parametros = "&tarea=procesar&operacion=procesarEliminarConvocatoria&modulo=MEIKUS01&tipo=2&codOrganizacion=" + codOrganizacion + "&codProcedimiento="+ codProcedimiento + "&convocatoria=" + convocatoria + "&plurianualidades=" + plurianualidades + "&numAnos=" + numAnos + "&justificacion=" + justificacion + "&numPagos=" + numPagos;
                    ajax.open("POST",url,false);
                    ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded;  charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
            }//if(ajax!=null)
            listaAuxConvocatoria = new Array();
            var j=0;
            for(i=0; i<listaConvocatorias.length; i++){
                if(i != eliminado){
                    listaAuxConvocatoria[j++] = listaConvocatorias[i];
                }//if(i != eliminado)
            }//for(i=0; i<listaConvocatorias.length; i++)
            listaConvocatorias = listaAuxConvocatoria;
            tabConvocatorias.lineas=listaConvocatorias;
            tabConvocatorias.displayTabla();
        }//if (tabConvocatorias.selectedIndex == -1) 
    }//pulsarEliminarConvocatoria
    
    function pulsarVerConvocatoria(){
        var argumentos = new Array();
        codProcedimiento = '<%=codProcedimiento%>';
        codOrganizacion = '<%=codOrganizacion%>';
            argumentos[0] = '<%=codProcedimiento%>';
            argumentos[1] = '<%=codOrganizacion%>';
        listaConvocatorias = tabConvocatorias.lineas;
        if (tabConvocatorias.selectedIndex == -1) {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else {
            var verConvocatoria = tabConvocatorias.selectedIndex;
            //registro a eliminar
            var regVerConvocatoria = new Array();
            regVerConvocatoria = tabConvocatorias.lineas[verConvocatoria];
            argumentos[2] = regVerConvocatoria[0];
            argumentos[3] = regVerConvocatoria[1];
            argumentos[4] = regVerConvocatoria[2];
            argumentos[5] = regVerConvocatoria[3];
            argumentos[6] = regVerConvocatoria[4];
            var oReturnValue = window.showModalDialog('<%=request.getContextPath()%>/jsp/extension/meikus01/verConvocatoria.jsp',argumentos,'dialogWidth:850px;dialogHeight:310px;status:'+ '<%=statusBar%>' + ';');
        }//if (tabConvocatorias.selectedIndex == -1)
    }//pulsarVerConvocatoria

</SCRIPT>

<div class="tab-page" id="tabPage300" style="height:350px;width:950px;">
    <h2 class="tab">Listado de convocatorias</h2>
    <script type="text/javascript">tp1_p300 = tp1.addTabPage( document.getElementById( "tabPage300" ) );</script>
    <table cellpadding="0px" cellspacing="0px" border="0px"  width="100%">
        <tr align="left">
            <td colspan="2" align="center">
                <table border="0" align="left" width="80%">
                    <tr>
                        <td class="etiqueta" style="width:28%;">
                            &nbsp;
                        </td>    
                        <td valign="top" id="tablaConvocatorias" class="columnP">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="center" valign="middle">
                <table width="300px" height="100%" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td>
                            <input type="button" class="botonGeneral" value="Alta" name="cmdAltaDoc" onClick="pulsarAñadirConvocatoria();">
                        </td>
                        <td style="width: 1px">
                        </td>
                        <td>
                            <input type="button" class="botonGeneral" value="Ver" name="cmdModificarDoc" onClick="pulsarVerConvocatoria();">
                        </td>
                        <td style="width: 2px">
                        </td>
                        <td>
                            <input type="button" class="botonGeneral" value="Eliminar" name="cmdEliminarDoc" onClick="pulsarEliminarConvocatoria();">
                        </td>
                        <td style="width: 2px">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right" class="etiqueta">
                &nbsp;
                <div id="divPorcentaje" class="etiqueta"></div>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                &nbsp;
            </td>
        </tr>
        <tr>
            <td colspan="2">
                &nbsp;
            </td>
        </tr>
    </table>
</div>

<script type="text/javascript">

    var tabConvocatorias;
    if(document.all) tabConvocatorias = new Tabla(document.all.tablaConvocatorias);
    else tabConvocatorias = new Tabla(document.getElementById('tablaConvocatorias'));
    tabConvocatorias.addColumna('150','center',"Convocatoria");
    tabConvocatorias.addColumna('150','center',"Plurianualidades");
    tabConvocatorias.addColumna('150','center',"Años");
    tabConvocatorias.addColumna('150','center',"Justificación");
    tabConvocatorias.addColumna('300','left',"Número de pagos");

    tabConvocatorias.height = 300;
    tabConvocatorias.displayCabecera=true;
    var lineas = new Array();
    var j=0;
    var cadPlurianualidades;
    var cadJustificacion;
    <%
       
       Vector relacionConvocatorias = (Vector) request.getAttribute("RelacionConvocatorias");
       for(int i=0;i<relacionConvocatorias.size();i++)
       {
           ConvocatoriaVO convocatoriaVO = new ConvocatoriaVO();
           convocatoriaVO=(ConvocatoriaVO)relacionConvocatorias.get(i);

        %>
        var convocatoria='<%=convocatoriaVO.getConvocatoria()%>';
        var plurianualidades='<%=convocatoriaVO.getPlurianualidades()%>';
        if (plurianualidades == 1){
           cadPlurianualidades ="SI";
        }else{
            cadPlurianualidades ="NO";
        }
        var numAnos='<%=convocatoriaVO.getNumAnos()%>';
        var justificacion='<%=convocatoriaVO.getJustificacion()%>';
        if (justificacion == 1){
           cadJustificacion ="SI";
        }else{
            cadJustificacion ="NO";
        }
        var numPagos='<%=convocatoriaVO.getNumPagos()%>';
        lineas[j]=[convocatoria,cadPlurianualidades,numAnos,cadJustificacion,numPagos];
            j=j+1;
     <%}%>

	tabConvocatorias.lineas = lineas;
	tabConvocatorias.displayTabla();
function refresConvocatorias() {
  tabConvocatorias.displayTabla();
}

</script>
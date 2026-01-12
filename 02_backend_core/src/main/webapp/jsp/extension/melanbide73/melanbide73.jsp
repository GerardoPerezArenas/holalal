<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide73.i18n.MeLanbide73I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadTramitadoraVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConstantesMeLanbide73"%>

<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
     String permiso="";
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null){
        try{
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex){
        
        }
    }
    //nuevas
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 5;
    String css = "";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css = usuarioVO.getCss();
    }
    
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide73I18n meLanbide73I18n = MeLanbide73I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
        Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
 
    
 permiso = (String)request.getAttribute("permiso");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide73/melanbide73.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide73/JavaScriptUtil.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/gestionInformes/tpls/WindowTemplate.jsp"></script>
<script type="text/javascript">

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

    function cambioUniTram() {

        if (tablaUnidades.selectedIndex != -1) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var control = new Date();
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var parametros = 'tarea=preparar&modulo=MELANBIDE73&operacion=cambiarUnidadTramitadora&tipo=0&nuevo=0&numExp=<%=numExpediente%>'
                    + '&id=' + listaUnidades[tablaUnidades.selectedIndex][1];

            try {

                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
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

                for (var j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                }//for(j=0;hijos!=null && j<hijos.length;j++)														
                if (codigoOperacion == "0") {
                    jsp_alerta('A', "Se ha modificado la unidad tramitadora del expediente " + '<%=numExpediente%>' + " a la unidad  " + listaUnidades[tablaUnidades.selectedIndex][1]);
                }
            } catch (Err) {

            }//try-catch

            volverPendientes();
        } else {
            jsp_alerta('A', '<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }//if-else 
    }// cambioUnidadTram

    function volverPendientes() {
        var control = new Date();
        try {
            var url = '<%=request.getContextPath()%>/sge/Tramitacion.do';
            var parametros = '?opcion=expedientesPendientes';
            document.forms[0].action = url + parametros;
            document.forms[0].submit();
        } catch (err) {
            alert('<%=meLanbide73I18n.getMensaje(idiomaUsuario,"msg.recargaPendientes")%>');
        }

    }// volverPendientes

    function dblClickTablaUnidades(rowID, tableName) {
        cambioUniTram();

    }//dblClickTablaUnidades


</script>

<div class="tab-page" id="tabPage731" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana731"><%=meLanbide73I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2> 
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage731"));</script>
    <br>
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide73I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div>
        <br>
        <div id="divGeneral">     
            <div id="listaUnidades" align="center"></div>
            <div id="btnCambioUni" align="center">
                <input type="button" class="botonLargo"
                       value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.cambiarUnidad")%>"
                       name="cmdCambioUni" id="cmdCambioUni" onclick="cambioUniTram();">
            </div>
        </div>
        <br><br>
    </div>  
</div>
<script type="text/javascript">

    // tabla unidades del expediente
    var tablaUnidades;
    var listaUnidades = new Array();
    var listaUnidadesTabla = new Array();
    var permiso = '<%=permiso%>';
    if (permiso == "1") {
        document.getElementById('cmdCambioUni').disabled = false;
    }
    tablaUnidades = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaUnidades'), 300);
    tablaUnidades.addColumna('50', 'left', "<%=meLanbide73I18n.getMensaje(idiomaUsuario,"tabla.tablaUnidades.col1")%>");
    tablaUnidades.addColumna('250', 'left', "<%=meLanbide73I18n.getMensaje(idiomaUsuario,"tabla.tablaUnidades.col2")%>");

    tablaUnidades.displayCabecera = true;
    tablaUnidades.height = 220;
    tablaUnidades.dblClkFunction = 'dblClickTablaUnidades';

    <%
      UnidadTramitadoraVO objectVO = new UnidadTramitadoraVO();
      List<UnidadTramitadoraVO> _list = null;
      if(request.getAttribute("listaUnidadesRgi")!=null){
          _list =(List<UnidadTramitadoraVO>)request.getAttribute("listaUnidadesRgi");
      }
      if(_list!=null && _list.size()>0){
          for (int indice=0;indice<_list.size();indice++){
              objectVO = _list.get(indice);
    %>

    listaUnidades[<%=indice%>] = ['<%=objectVO.getCodVisible()%>', '<%=objectVO.getNomUnidad()%>'];
    listaUnidadesTabla[<%=indice%>] = ['<%=objectVO.getCodVisible()%>', '<%=objectVO.getNomUnidad()%>'];

    <%
          }//for
      }//if
    %>

    tablaUnidades.lineas = listaUnidadesTabla;
    tablaUnidades.displayTablaConTooltips(listaUnidades);

    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
        try {
            var div = document.getElementById('listaUnidades');
            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
            } else {
                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                div.children[0].children[1].children[0].children[0].style.width = '100%';
            }
        } catch (err) {
        }
    }


</script>

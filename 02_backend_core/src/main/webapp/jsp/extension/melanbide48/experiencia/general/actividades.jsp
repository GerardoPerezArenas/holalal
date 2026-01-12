<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayActividadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    
    List<ColecTrayActividadVO> listaTrayGenralActividades = (List<ColecTrayActividadVO>)request.getAttribute("listaTrayGenralActividades");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>"/>

<script type="text/javascript"> 
    
    function anadirTrayectoriaGeneralActividad(){
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarAltaEdicionTrayGenActividades&tipo=0&modoDatos=0&numero=<%=numExpediente%>&control='+control.getTime(),500,980,'no','no',function(result){
        	if (result != undefined){
				if(result[0] == '0'){
                                    recargarTablaTrayGenActividades(result);
                                }
			}
		});
    }
    
    function modificarTrayectoriaGeneralActividades(){
        //TODO
        var control = new Date();
        var result = null;
        var idTray = "";
        var codEntidad="";
        if((tabTrayectoriaActividades.selectedIndex != -1)) {
            idTray=listaTrayectoriaActividades[tabTrayectoriaActividades.selectedIndex][0];
            codEntidad=listaTrayectoriaActividades[tabTrayectoriaActividades.selectedIndex][4];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarAltaEdicionTrayGenActividades&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTray='+idTray+'&codEntidad='+codEntidad+'&control='+control.getTime(),350,980,'no','no',function(result){
                    if (result != undefined){
                                    if(result[0] == '0'){
                                        recargarTablaTrayGenActividades(result);
                                    }
                            }
                    });
        }else{
            jsp_alerta('A', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function eliminarTrayectoriaGeneralActividades(){
        //TODO
        var control = new Date();
        var result = null;
        var idTray="";
        var codEntidad="";
        if((tabTrayectoriaActividades.selectedIndex != -1)) {
            idTray=listaTrayectoriaActividades[tabTrayectoriaActividades.selectedIndex][0];
            codEntidad=listaTrayectoriaActividades[tabTrayectoriaActividades.selectedIndex][4];
            var resultado = jsp_alerta('', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=eliminarTrayectoriaGeneralActividades&tipo=0&numero=<%=numExpediente%>'
                    +'&idTray='+idTray
                    +'&codEntidad='+codEntidad
                    +'&idiomaUsuario=<%=idiomaUsuario%>'
                    +'&control='+control.getTime();
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
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaDatosRespuesta = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaDatosRespuesta[j] = codigoOperacion;
                        }else if(hijos[j].nodeName=="TRAYECTORIAS"){
                            var nodoFila1 = hijos[j];
                            var hijosFila1 = nodoFila1.childNodes;
                            for(var cont1 = 0; cont1 < hijosFila1.length; cont1++){
                                nodoFila = hijosFila1[cont1];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos TRAYECTORIA
                                        if (hijosFila[cont].nodeName == "COLEC_ACTIV_COD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[0] = '-';
                                            }
                                        }
                                        if (hijosFila[cont].nodeName == "COLEC_ACTIV_TIPO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[1] = '-';
                                            }
                                        }
                                        else if (hijosFila[cont].nodeName == "COLEC_ACTIV_EXP_EJE") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[2] = '-';
                                            }
                                        }
                                        else if (hijosFila[cont].nodeName == "COLEC_ACTIV_NUMEXP") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ACTIV_COD_ENTIDAD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ACTIV_DESC_SERVPUB") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ACTIV_INICIO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ACTIV_FIN") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ENT_CIF") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ENT_NOMBRE") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        }
                                    }
                                    listaDatosRespuesta[cont1 + 1] = fila;
                                    fila = new Array();
                                }

                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            if (listaDatosRespuesta!=null && listaDatosRespuesta!= undefined){
                                        if(listaDatosRespuesta[0] == '0'){
                                            recargarTablaTrayGenActividades(listaDatosRespuesta);
                                        }
                                }
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch (Err) {
                        jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }
            }
        }else{
            jsp_alerta('A', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function recargarTablaTrayGenActividades(result){
        //TODO
        var codOperacion = result!=null?result[0]:null;
        var fila;
        if(codOperacion!=null){
            listaTrayectoriaActividades = new Array();
            listaTrayectoriaActividadesTabla = new Array();
             // Creamos tabla Trampa porque al seleccionar fila hace tab[0] y los id creados en el script NuevaTabla entre la tabla trámites y esta es el mismo tabla0
            
            tabTrayectoriaActividades = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriaActividades'), 905,25);
            tabTrayectoriaActividades.addColumna('250','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
            tabTrayectoriaActividades.addColumna('450','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.actividades")%>");
            tabTrayectoriaActividades.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%>");   
            tabTrayectoriaActividades.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%>");   
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaTrayectoriaActividades[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9]]; 
                listaTrayectoriaActividadesTabla[i-1] = [fila[9], fila[5],fila[6],fila[7]];
            }
            tabTrayectoriaActividades.lineas = listaTrayectoriaActividadesTabla;
            tabTrayectoriaActividades.displayCabecera=true;
            tabTrayectoriaActividades.height = 250;
            tabTrayectoriaActividades.displayTabla();
        }else{
            alert('Datos NO Guardados Correctamente..!!');
        }
    }

</script>

<body class="contenidoPantalla" id="bodyColecTrayectoriaActividades">
    <fieldset id="fieldsetTrayGenACt" style="height: 350px; margin-top: 20px; ">
        <!--<legend class="legendAzul">< %=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.experiencia.previa.general.Actividades.textoLargo")%></legend>-->
        <label class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.experiencia.previa.general.Actividades.textoLargo")%></label>
        <div id="divTablaTrayectoriaGenActividades" align="center">
            <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                <div id="listaTrayectoriaActividades" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>    
                <div class="botonera">
                    <input type="button" id="botonAnadirTrayGenAct" name="botonAnadirTrayGenAct" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirTrayectoriaGeneralActividad();"/>
                    <input type="button" id="botonModificarTrayGenAct" name="botonModificarTrayGenAct" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarTrayectoriaGeneralActividades();"/>
                    <input type="button" id="botonEliminarTrayGenAct" name="botonEliminarTrayGenAct" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarTrayectoriaGeneralActividades();"/>
                </div>
            </div>
        </div>
    </fieldset>
</body>

<script type="text/javascript">   
    var tabTrayectoriaActividades;
    var listaTrayectoriaActividades = new Array();
    var listaTrayectoriaActividadesTabla = new Array();
    
    
//    tabTrayectoriaActividades = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriaActividades'),905,25);
//    var g = new Array();
//    g[0]=0;
//    recargarTablaTrayGenActividades(g);

    tabTrayectoriaActividades = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriaActividades'),905,25);
    tabTrayectoriaActividades.addColumna('250','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
    tabTrayectoriaActividades.addColumna('450','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.actividades")%>");
    tabTrayectoriaActividades.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%>");   
    tabTrayectoriaActividades.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%>");   

    <%
        ColecTrayActividadVO fila = null;
        if(listaTrayGenralActividades!=null){
            for(int i = 0; i < listaTrayGenralActividades.size(); i++)
            {
                fila = listaTrayGenralActividades.get(i);
    %>
                listaTrayectoriaActividades[<%=i%>] = ['<%=fila.getCodIdActividad()%>', '<%=fila.getTipoActividad()%>','<%=fila.getEjercicio()%>','<%=fila.getNumExpediente()%>','<%=fila.getCodEntidad()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDesActividadyServPublEmp())%>','<%=MeLanbide48Utils.formatearFecha_ddmmyyyy(fila.getFechaInicio())%>','<%=MeLanbide48Utils.formatearFecha_ddmmyyyy(fila.getFechaFin())%>','<%=fila.getCifEntidad()%>','<%=fila.getNombreEntidad()%>'];
                listaTrayectoriaActividadesTabla[<%=i%>] = ['<%=fila.getNombreEntidad()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDesActividadyServPublEmp())%>','<%=MeLanbide48Utils.formatearFecha_ddmmyyyy(fila.getFechaInicio())%>','<%=MeLanbide48Utils.formatearFecha_ddmmyyyy(fila.getFechaFin())%>'];
    <%
            }
        }
    %>
    
    tabTrayectoriaActividades.lineas = listaTrayectoriaActividadesTabla;
    tabTrayectoriaActividades.displayCabecera=true;
    tabTrayectoriaActividades.height = 250;
    tabTrayectoriaActividades.displayTabla();
    //establecerNroElementosxPageTable('listaTrayectoriaActividades','25');
    
</script>
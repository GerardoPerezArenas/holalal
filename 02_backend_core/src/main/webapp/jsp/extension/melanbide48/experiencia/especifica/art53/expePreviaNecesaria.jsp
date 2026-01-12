<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaTrayEspVO"%>
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
    
    
    List<FilaTrayEspVO> trayectoriasCol1 = (List<FilaTrayEspVO>)request.getAttribute("trayectoriasCol1");
    List<FilaTrayEspVO> trayectoriasCol2 = (List<FilaTrayEspVO>)request.getAttribute("trayectoriasCol2");
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
    
    function anadirTrayectoriaEspecial(_colectivo){
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarNuevaActividadTrayectoria&tipo=0&colectivo='+_colectivo+'&numero=<%=numExpediente%>&control='+control.getTime(),500,980,'no','no',function(result){
        	if (result != undefined){
				if(result[0] == '0'){
                                    recargarTablaTrayEspecifica(result,_colectivo);
                                }
			}
		});
    }
    
    function modificarTrayectoriaEspecial(_colectivo){
        //TODO
        var control = new Date();
        var result = null;
        var idTrayEsp = "";
        var codEntidad="";
        if((_colectivo==1 && tabTrayectoriasCol1.selectedIndex != -1)
                || (_colectivo==2 && tabTrayectoriasCol2.selectedIndex != -1)) {
            if(_colectivo==1){
                idTrayEsp=listaTrayectoriasCol1[tabTrayectoriasCol1.selectedIndex][0];
                codEntidad=listaTrayectoriasCol1[tabTrayectoriasCol1.selectedIndex][1];
            }else if(_colectivo==2){
                idTrayEsp=listaTrayectoriasCol2[tabTrayectoriasCol2.selectedIndex][0];
                codEntidad=listaTrayectoriasCol2[tabTrayectoriasCol2.selectedIndex][1];
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarNuevaActividadTrayectoria&tipo=0&modoDatos=1&colectivo='+_colectivo+'&numero=<%=numExpediente%>&idTrayEsp='+idTrayEsp+'&codEntidad='+codEntidad+'&control='+control.getTime(),350,980,'no','no',function(result){
                    if (result != undefined){
                                    if(result[0] == '0'){
                                        recargarTablaTrayEspecifica(result,_colectivo);
                                    }
                            }
                    });
        }else{
            jsp_alerta('A', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function eliminarTrayectoriaEspecial(_colectivo){
        //TODO
        var control = new Date();
        var result = null;
        var selectedRow=false;
        var codColectivo="";
        var idTrayEsp="";
        var codEntidad="";
        if((_colectivo==1 && tabTrayectoriasCol1.selectedIndex != -1)) {
            idTrayEsp=listaTrayectoriasCol1[tabTrayectoriasCol1.selectedIndex][0];
            codEntidad=listaTrayectoriasCol1[tabTrayectoriasCol1.selectedIndex][1];
            codColectivo=listaTrayectoriasCol1[tabTrayectoriasCol1.selectedIndex][2];
            selectedRow=true;
        }else if((_colectivo==2 && tabTrayectoriasCol2.selectedIndex != -1)){
            idTrayEsp=listaTrayectoriasCol2[tabTrayectoriasCol2.selectedIndex][0];
            codEntidad=listaTrayectoriasCol2[tabTrayectoriasCol2.selectedIndex][1];
            codColectivo=listaTrayectoriasCol2[tabTrayectoriasCol2.selectedIndex][2];
            selectedRow=true;
        }
        if(selectedRow){
            var resultado = jsp_alerta('', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();

                parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=eliminarTrayectoriaEspecifica&tipo=0&numero=<%=numExpediente%>'
                    +'&codColectivo='+codColectivo
                    +'&idTrayEsp='+idTrayEsp
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
                                        if (hijosFila[cont].nodeName == "COLEC_COD_TRAY_ESP") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[0] = '-';
                                            }
                                        }
                                        if (hijosFila[cont].nodeName == "COLEC_COD_ENTIDAD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[1] = '-';
                                            }
                                        }
                                        else if (hijosFila[cont].nodeName == "COLEC_COLECTIVO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else {
                                                fila[2] = '-';
                                            }
                                        }
                                        else if (hijosFila[cont].nodeName == "COLEC_NOMBRE_ADM") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_DESC_ACTIVIDAD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ENT_CIF") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_ENT_NOMBRE") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_VALIDADA") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_NUMEXP") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "COLEC_EXP_EJE") {
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
                                            recargarTablaTrayEspecifica(listaDatosRespuesta,_colectivo);
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
    
    function obtenerListaTrayCol1(){
        //TODO
    }
    
    function recargarTablaTrayEspecifica(result,_colectivo){
        //TODO
        if(_colectivo!=null && _colectivo!=""){
            if(_colectivo==1){
                recargarTablaTrayEspecificaC1(result)
            }else if(_colectivo==2){
                recargarTablaTrayEspecificaC2(result)
            }
        }
    }
    function recargarTablaTrayEspecificaC1(result){
        //TODO
        var codOperacion = result!=null?result[0]:null;
        var fila;
        if(codOperacion!=null){
            listaTrayectoriasCol1 = new Array();
            listaTrayectoriasCol1Tabla = new Array();
             // Creamos tabla Trampa porque al seleccionar fila hace tab[0] y los id creados en el script NuevaTabla entre la tabla trámites y esta es el mismo tabla0
            
            tabTrayectoriasCol1 = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriasCol1'), 905,25); 
            tabTrayectoriasCol1.addColumna('200','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
            tabTrayectoriasCol1.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.nombreAdm")%>");
            tabTrayectoriasCol1.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.descActividad")%>");   
            //tabTrayectoriasCol1.addColumna('370','left',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.validada")%>");     
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaTrayectoriasCol1[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9]]; 
                listaTrayectoriasCol1Tabla[i-1] = [fila[6], fila[3],fila[4]];
            }
            tabTrayectoriasCol1.lineas = listaTrayectoriasCol1Tabla;
            tabTrayectoriasCol1.displayCabecera=true;
            tabTrayectoriasCol1.height = 250;
            tabTrayectoriasCol1.displayTabla();
        }else{
            alert('Datos no Guardados Correctamente..!!');
        }
    }
    function recargarTablaTrayEspecificaC2(result){
        //TODO
        var codOperacion = result!=null?result[0]:null;
        var fila;
        if(codOperacion!=null){
            listaTrayectoriasCol2 = new Array();
            listaTrayectoriasCol2Tabla = new Array();

            tabTrayectoriasCol2 = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriasCol2'), 905,25); //'< %=descriptor.getDescripcion("mosFilasPag")%>'
            tabTrayectoriasCol2.addColumna('200','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
            tabTrayectoriasCol2.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.nombreAdm")%>");
            tabTrayectoriasCol2.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.descActividad")%>");   
            //tabTrayectoriasCol2.addColumna('370','left',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.validada")%>");     
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaTrayectoriasCol2[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9]]; 
                listaTrayectoriasCol2Tabla[i-1] = [fila[6], fila[3],fila[4]];
            }
            tabTrayectoriasCol2.lineas = listaTrayectoriasCol2Tabla;
            tabTrayectoriasCol2.displayCabecera=true;
            tabTrayectoriasCol2.height = 250;
            tabTrayectoriasCol2.displayTabla();
        }else{
            alert('Datos no Guardados Correctamente..!!');
        }
    }

</script>

<body class="contenidoPantalla" id="bodyColecTrayectoriaEspecial">
    <fieldset id="fieldsetTrayCol1" style="height: 350px; margin-top: 20px; ">
        <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.experiencia.previa.especifica.colectivo1")%></legend>
        <div id="divTablaTrayectoriaCol1Padre" align="center">
            <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                <div id="listaTrayectoriasCol1" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>    
                <div class="botonera">
                    <input type="button" id="botonAnadirCol1" name="botonAnadirCol1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirTrayectoriaEspecial(1);"/>
                    <input type="button" id="botonModificarCol1" name="botonModificarCol1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarTrayectoriaEspecial(1);"/>
                    <input type="button" id="botonEliminarCol1" name="botonEliminarCol1" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarTrayectoriaEspecial(1);"/>
                </div>
            </div>
        </div>
    </fieldset>
    <div style="clear: both;">
        <fieldset id="fieldsetTrayCol2" style="height: 350px; margin-top: 20px;">
            <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.experiencia.previa.especifica.colectivo2")%></legend>
            <div align="center">
                <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                    <div id="listaTrayectoriasCol2" style="padding: 5px; width:98%; font-size: 13px;height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>    
                    <div class="botonera">
                        <input type="button" id="botonAnadirCol2" name="botonAnadirCol2" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirTrayectoriaEspecial(2);"/>
                        <input type="button" id="botonModificarCol2" name="botonModificarCol2" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarTrayectoriaEspecial(2);"/>
                        <input type="button" id="botonEliminarCol2" name="botonEliminarCol2" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarTrayectoriaEspecial(2);"/>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
</body>

<script type="text/javascript">   
    //Tabla Colectivo 1
    var tabTrayectoriasCol1;
    var listaTrayectoriasCol1 = new Array();
    var listaTrayectoriasCol1Tabla = new Array();
    

    //Creamos una tabla Ficticia para activar la seleccion de Filas
//    tabTrayectoriasCol1 = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriasCol1'),905,25); //'< %=descriptor.getDescripcion("mosFilasPag")%>'
//    var g = new Array();
//    g[0]=0;
//    recargarTablaTrayEspecificaC1(g);
    // Ñapa porque hay pendiente un revision en el script de felxia TablaNueva.js
    // Esto esta ocasionando que se vean dos opciones de busqueda y paginacion en Tabla de entidades
    
    //Creamos latabla real
    tabTrayectoriasCol1 = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriasCol1'),905,25);  //'< %=descriptor.getDescripcion("mosFilasPag")%>'
    tabTrayectoriasCol1.addColumna('200','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
    tabTrayectoriasCol1.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.nombreAdm")%>");
    tabTrayectoriasCol1.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.descActividad")%>");   
    //tabTrayectoriasCol1.addColumna('370','left',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.validada")%>");     

    <%
        FilaTrayEspVO fila = null;
        if(trayectoriasCol1!=null){
            for(int i = 0; i < trayectoriasCol1.size(); i++)
            {
                fila = trayectoriasCol1.get(i);
    %>
                listaTrayectoriasCol1[<%=i%>] = ['<%=fila.getCodTray()%>', '<%=fila.getCodEntidad()%>','<%=fila.getColectivo()%>','<%=fila.getNombreAdm()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDescAct())%>','<%=fila.getCifEntidad()%>','<%=fila.getNomEntidad()%>','<%=fila.getValidada()%>','<%=fila.getNumExpediente()%>','<%=fila.getEjercicio()%>'];
                listaTrayectoriasCol1Tabla[<%=i%>] = ['<%=fila.getNomEntidad()%>', '<%=fila.getNombreAdm()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDescAct())%>']; //'< %=fila.getValidada() == 1 ? meLanbide48I18n.getMensaje(idiomaUsuario,"label.si") : meLanbide48I18n.getMensaje(idiomaUsuario,"label.no")%>'
    <%
            }
        }
    %>
    
    tabTrayectoriasCol1.lineas = listaTrayectoriasCol1Tabla;
    tabTrayectoriasCol1.displayCabecera=true;
    tabTrayectoriasCol1.height = 250;
    tabTrayectoriasCol1.displayTabla();
    
    //Tabla Colectivo 2
    var tabTrayectoriasCol2;
    var listaTrayectoriasCol2 = new Array();
    var listaTrayectoriasCol2Tabla = new Array();
    // NO funciona igual que en la tabla 1, hay que hacer como si se recargara la tabla para que funcione
    // Hay qu revisar codigo flexia
    //var g = new Array();
    //g[0]=0;
    //recargarTablaTrayEspecificaC2(g);
    
    tabTrayectoriasCol2 = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriasCol2'),905,25); //'< %=descriptor.getDescripcion("mosFilasPag")%>'
    tabTrayectoriasCol2.addColumna('200','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>");
    tabTrayectoriasCol2.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.nombreAdm")%>");
    tabTrayectoriasCol2.addColumna('350','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.descActividad")%>");   
    //tabTrayectoriasCol2.addColumna('370','left',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.validada")%>");     

    <%
        fila = null;
        if(trayectoriasCol2!=null){
            for(int i = 0; i < trayectoriasCol2.size(); i++)
            {
                fila = trayectoriasCol2.get(i);
        %>
                listaTrayectoriasCol2[<%=i%>] = ['<%=fila.getCodTray()%>', '<%=fila.getCodEntidad()%>','<%=fila.getColectivo()%>','<%=fila.getNombreAdm()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDescAct())%>','<%=fila.getCifEntidad()%>','<%=fila.getNomEntidad()%>','<%=fila.getValidada()%>','<%=fila.getNumExpediente()%>','<%=fila.getEjercicio()%>'];
                listaTrayectoriasCol2Tabla[<%=i%>] = ['<%=fila.getNomEntidad()%>', '<%=fila.getNombreAdm()%>','<%=MeLanbide48Utils.escapeNewLineToSimpleSpace(fila.getDescAct())%>']; //'< %=fila.getValidada() == 1 ? meLanbide48I18n.getMensaje(idiomaUsuario,"label.si") : meLanbide48I18n.getMensaje(idiomaUsuario,"label.no")%>'
        <%
            }
        }
    %>
    
    tabTrayectoriasCol2.lineas = listaTrayectoriasCol2Tabla;
    tabTrayectoriasCol2.displayCabecera=true;
    tabTrayectoriasCol2.height = 250;
    tabTrayectoriasCol2.displayTabla();
    //inicio();
    
    
</script>
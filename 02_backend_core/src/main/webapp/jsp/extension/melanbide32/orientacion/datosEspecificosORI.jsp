<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaUbicOrientacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaTrayectoriaOrientacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    EntidadVO entidad = (EntidadVO)request.getAttribute("entidad");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<script type="text/javascript">
        var mensajeValidacion = "";
        
        function pulsarAltaUbicacionORI(){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                	lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaSolicitudORI&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),320,820,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaUbicaciones(result);								
								}
							}
						});
            }else{
                	lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaSolicitudORI&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),320,820,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaUbicaciones(result);								
								}
							}
						});
            }            
        }
        
        function pulsarModificarUbicacionORI(){
            if(tabUbicacionesOri.selectedIndex != -1) {
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifSolicitudORI&tipo=0&numero=<%=numExpediente%>&idUbic='+listaUbicacionesOri[tabUbicacionesOri.selectedIndex][0]+'&control='+control.getTime(),320,820,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaUbicaciones(result);								
								}
							}
						});
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifSolicitudORI&tipo=0&numero=<%=numExpediente%>&idUbic='+listaUbicacionesOri[tabUbicacionesOri.selectedIndex][0]+'&control='+control.getTime(),320,820,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaUbicaciones(result);								
								}
							}
						});
                }                
            } 
            else {
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarEliminarUbicacionORI(){

            if(tabUbicacionesOri.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=eliminarSolicitudORI&tipo=0&numero=<%=numExpediente%>&idUbic='+listaUbicacionesOri[tabUbicacionesOri.selectedIndex][0]+'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                        var listaUbicacionesNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaUbicacionesNueva[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PROVINCIA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MUNICIPIO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DIRECCION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORAS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESPACHOS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="VALORACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TOTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORASADJ"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                }
                                listaUbicacionesNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            recargarTablaUbicaciones(listaUbicacionesNueva);
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarValorarUbicacionORI(){
            if(tabUbicacionesOri.selectedIndex != -1) {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarValorarSolicitudORI&tipo=0&numero=<%=numExpediente%>&idUbic='+listaUbicacionesOri[tabUbicacionesOri.selectedIndex][0]+'&control='+control.getTime(),600,500,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaUbicaciones(result);								
								}
							}  
						});              
            }
            else{
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarAltaTrayectoriaORI(){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),240,800,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaTrayectoria(result);								
								}
							}
						});
            }else{
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),240,800,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaTrayectoria(result);								
								}
							}
						});
            }            
        }
        
        function pulsarModificarTrayectoriaORI(){
            if(tabTrayectoriaOri.selectedIndex != -1) {
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&idTray='+listaTrayectoriaOri[tabTrayectoriaOri.selectedIndex][0]+'&control='+control.getTime(),240,800,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaTrayectoria(result);								
								}
							}
						});
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&idTray='+listaTrayectoriaOri[tabTrayectoriaOri.selectedIndex][0]+'&control='+control.getTime(),240,800,'no','no', function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaTrayectoria(result);								
								}
							}
						});
                }                
            } 
            else {
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        
        function pulsarEliminarTrayectoriaORI(){

            if(tabTrayectoriaOri.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=eliminarTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&idTray='+listaTrayectoriaOri[tabTrayectoriaOri.selectedIndex][0]+'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                        var listaTrayectoriaNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaTrayectoriaNueva[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESCRIPCION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DURACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORGANISMO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                }
                                listaTrayectoriaNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            recargarTablaTrayectoria(listaTrayectoriaNueva);
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function recargarTablaUbicaciones(result){
            var fila;
            listaUbicacionesOri = new Array();
            listaUbicacionesOriTabla = new Array();
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaUbicacionesOri[i-1] = fila;
                listaUbicacionesOriTabla[i-1] = [fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10]];
            }
            tabUbicacionesOri = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaUbicacionesOri'), 874);
            tabUbicacionesOri.addColumna('72','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col1")%>");
            tabUbicacionesOri.addColumna('75','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col2")%>");                                                            
            tabUbicacionesOri.addColumna('75','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col3")%>");
            tabUbicacionesOri.addColumna('192','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col4")%>");
            tabUbicacionesOri.addColumna('50','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col5")%>");
            tabUbicacionesOri.addColumna('70','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col6")%>");
            tabUbicacionesOri.addColumna('80','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col7")%>");
            tabUbicacionesOri.addColumna('80','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col8")%>");
            tabUbicacionesOri.addColumna('40','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col9")%>");
            tabUbicacionesOri.addColumna('79','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col10")%>");

            tabUbicacionesOri.displayCabecera=true;
            tabUbicacionesOri.height = 100;
            tabUbicacionesOri.lineas=listaUbicacionesOriTabla;
            tabUbicacionesOri.displayTabla();
            
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('listaUbicacionesOri');
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[7].style.backgroundColor = 'forestgreen';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[8].style.backgroundColor = 'forestgreen';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[9].style.backgroundColor = 'FF9966';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[9].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[9].style.width = '100%';
                }
                catch(err){

                }
            }
        }
        
        function recargarTablaTrayectoria(result){
            var fila;
            listaTrayectoriaOri = new Array();
            listaTrayectoriaOriTabla = new Array();
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaTrayectoriaOri[i-1] = fila;
                listaTrayectoriaOriTabla[i-1] = [fila[1], fila[2], fila[3]];
            }
            tabTrayectoriaOri = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriaOri'), 874);
            tabTrayectoriaOri.addColumna('391','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col1")%>");
            tabTrayectoriaOri.addColumna('74','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col2")%>");                                                            
            tabTrayectoriaOri.addColumna('391','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col3")%>");

            tabTrayectoriaOri.displayCabecera=true;
            tabTrayectoriaOri.height = 60;
            tabTrayectoriaOri.lineas=listaTrayectoriaOriTabla;
            
            calcularTrayectoriaEntidad();
            
            tabTrayectoriaOri.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('listaTrayectoriaOri');
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[2].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[2].style.width = '100%';    
                }
                catch(err){
                }
            }
        }
        
        function calcularTrayectoriaEntidad(){
            var totalAnos = 0.0;
            var anosAct;
            for(var i = 0; i < listaTrayectoriaOri.length; i++){
                anosAct = 0.0;
                try{
                    anosAct = parseFloat(listaTrayectoriaOri[i][2].replace(/\,/,"."));
                    if(!isNaN(anosAct)){
                        totalAnos += anosAct;
                    }
                }catch(err){
                    
                }
            }
            document.getElementById('anosTrayEnt').value = totalAnos;
            reemplazarPuntosORI(document.getElementById('anosTrayEnt'));
        }
        
        function guardarTrayectoriaValEntidad(){
            if(validarDatosVal()){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=guardarAnosValEntidadORI&tipo=0&numero=<%=numExpediente%>&anosVal='+document.getElementById('anosTrayEntVal').value+'&nomEnt='+document.getElementById('nombreEntORI').value+'&control='+control.getTime();
                try{
                    ajax.open("POST",url,false);
                    ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var listaUbicacionesNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }  else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PROVINCIA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MUNICIPIO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DIRECCION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORAS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESPACHOS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="VALORACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TOTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORASADJ"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                }
                                listaUbicacionesNueva[j] = fila;
                                fila = new Array();
                            }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if(codigoOperacion=="0"){
                        recargarTablaUbicaciones(listaUbicacionesNueva);
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else if(codigoOperacion=="4"){
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                    }else{
                        jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }else{
                jsp_alerta("A", mensajeValidacion);
            }
        }
        
        function validarDatosVal(){
            mensajeValidacion = "";
            
            var nombreEntidad = document.getElementById('nombreEntORI').value;
            if(nombreEntidad == null || nombreEntidad == ''){
                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.nomEntidadObligatorio")%>';
                document.getElementById('nombreEntORI').style.border = '1px solid red';
                return false;
            }else{
                if(!comprobarCaracteresEspecialesORI(nombreEntidad)){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.nomEntidadCaracteresEspeciales")%>';   
                    document.getElementById('nombreEntORI').style.border = '1px solid red';
                    return false;
                }else{
                    mensajeValidacion = '';
                    document.getElementById('nombreEntORI').removeAttribute("style");
                }
            }
            
            var anosVal = document.getElementById('anosTrayEntVal').value;
//            if(anosVal == null || anosVal == ''){
//                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValObligatorio")%>';
//                document.getElementById('anosTrayEntVal').style.border = '1px solid red';
//                return false;
//            }
            
            var correcto = true;
            if(anosVal != null && anosVal != ''){
                if(!validarNumerico(document.getElementById('anosTrayEntVal').value)){
                    document.getElementById('anosTrayEntVal').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var anos = parseInt(document.getElementById('anosTrayEntVal').value);
                        if(anos < 0){
                            document.getElementById('anosTrayEntVal').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValNegativo")%>';
                            correcto = false;
                        }else{
                            try
                            {
                                var minValor = parseInt('<%=ConfigurationParameter.getParameter("minAnosValTray", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>');
                                var maxValor = parseInt('<%=ConfigurationParameter.getParameter("maxAnosValTray", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS)%>');
                            
                                if(anos < minValor || anos > maxValor){
                                    document.getElementById('anosTrayEntVal').style.border = '1px solid red';
                                    if(mensajeValidacion == '')
                                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValFueraRango1")%>'+' '+minValor+' '+'<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValFueraRango2")%>'+' '+maxValor;
                                    correcto = false;
                                }else{
                                    document.getElementById('anosTrayEntVal').removeAttribute("style");
                                }
                            }catch(err){
                                document.getElementById('anosTrayEntVal').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValIncorrecto")%>';
                                correcto = false;
                            }
                        }
                    }
                    catch(err){
                        document.getElementById('anosTrayEntVal').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.anosValIncorrecto")%>';
                        correcto = false;
                    }
                }
            }
            return correcto;
        }
            
        function validarNumerico(numero){
            return !isNaN(numero);
            /*try{
                if (Trim(numero.value)!='') {
                    return /^([0-9])*$/.test(numero.value);
                }
            }
            catch(err){
                return false;
            }*/
        }
    
        function cambioNombreEntidadORI(){
            try{
                var nuevoValor = document.getElementById('nombreEntORI').value;
                var nombreEntidadORI = document.getElementById('nombreEntCE');
                if(nombreEntidadORI != null){
                    nombreEntidadORI.value = nuevoValor;
                }
            }catch(err){

            }
        }
            
        function comprobarCaracteresEspecialesORI(texto){
            //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
            var iChars = "&'<>|^/\\%";
            for (var i = 0; i < texto.length; i++) {
                if (iChars.indexOf(texto.charAt(i)) != -1) {
                    return false;
                }
            }
            return true;
        }
        
            function reemplazarPuntosORI(campo){
                try{
                    var valor = campo.value;
                    if(valor != null && valor != ''){
                        valor = valor.replace(/\./g,',');
                        campo.value = valor;
                    }
                }
                catch(err){
                }
            }
</script>
<body>
    <div class="tab-page" id="tabPage321" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana321"><%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p321 = tp1.addTabPage( document.getElementById( "tabPage321" ) );</script>
        <div style="clear: both;">
            <fieldset style="width: 97.5%;">
                <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.legend.orientacion")%></legend>
                <div class="tituloAzul">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.solicHorasOrientacion")%>
                    </span>
                </div>
                <div style="width: 120px; float: left;">
                    <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.nomEnt")%>
                </div>
                <div style="width: 575px; float: left;">
                    <input id="nombreEntORI" name="nombreEntORI" type="text" class="inputTexto" size="92" maxlength="80" value="<%=entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : ""%>" onchange="cambioNombreEntidadORI();">
                </div>
                <div id="listaUbicacionesOri" class="tablaPers" align="center"></div>
                <div class="botonera">
                    <input type="button" id="btnNuevaSolicHorasOri" name="btnNuevaSolicHorasOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.nuevo")%>" onclick="pulsarAltaUbicacionORI();">
                    <input type="button" id="btnEliminarSolicHorasOri" name="btnEliminarSolicHorasOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.eliminar")%>" onclick="pulsarEliminarUbicacionORI();">
                    <input type="button" id="btnModificarSolicHorasOri" name="btnModificarSolicHorasOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.modificar")%>" onclick="pulsarModificarUbicacionORI();">
                    <input type="button" id="btnValorarSolicHorasOri" name="btnValorarSolicHorasOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.valorar")%>" onclick="pulsarValorarUbicacionORI();">
                </div>
                <div class="tituloAzul">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.trayectoriaEntidadAnos")%>
                    </span>
                    <input type="text" id="anosTrayEnt" name="anosTrayEnt" size="2" maxlength="2" disabled="true"/>
                    <input type="text" id="anosTrayEntVal" name="anosTrayEntVal" size="2" maxlength="2" class="inputTexto" value="<%=entidad != null && entidad.getOriEntTrayectoriaVal() != null ? entidad.getOriEntTrayectoriaVal() : ""%>"/>
                    <span style="color: forestgreen;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.validados")%>
                    </span>
                </div>
                <div id="listaTrayectoriaOri" align="center"></div>
                <div class="botonera">
                    <input type="button" id="btnNuevaTrayectoriaOri" name="btnNuevaTrayectoriaOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.nuevo")%>" onclick="pulsarAltaTrayectoriaORI();">
                    <input type="button" id="btnEliminarTrayectoriaOri" name="btnEliminarTrayectoriaOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.eliminar")%>" onclick="pulsarEliminarTrayectoriaORI();">
                    <input type="button" id="btnModificarTrayectoriaOri" name="btnModificarTrayectoriaOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.modificar")%>" onclick="pulsarModificarTrayectoriaORI();">
                </div>
            </fieldset>    
            <div class="botonera" style="padding-top: 20px;">
                <input type="button" id="btnGuardarOri" name="btnGuardarOri" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.guardar")%>" onclick="guardarTrayectoriaValEntidad();">
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    //Tabla solicitud de horas de orientacion
    var tabUbicacionesOri;
    var listaUbicacionesOri = new Array();
    var listaUbicacionesOriTabla = new Array();

    tabUbicacionesOri = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaUbicacionesOri'), 874);
    tabUbicacionesOri.addColumna('72','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col1")%>");
    tabUbicacionesOri.addColumna('75','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col2")%>");                                                            
    tabUbicacionesOri.addColumna('75','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col3")%>");
    tabUbicacionesOri.addColumna('192','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col4")%>");
    tabUbicacionesOri.addColumna('50','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col5")%>");
    tabUbicacionesOri.addColumna('70','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col6")%>");
    tabUbicacionesOri.addColumna('80','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col7")%>");
    tabUbicacionesOri.addColumna('80','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col8")%>");
    tabUbicacionesOri.addColumna('40','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col9")%>");
    tabUbicacionesOri.addColumna('79','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaSolicHoras.col10")%>");

    tabUbicacionesOri.displayCabecera=true;
    tabUbicacionesOri.height = 100;
    <%  		
        FilaUbicOrientacionVO voS = null;
        List<FilaUbicOrientacionVO> solList = (List<FilaUbicOrientacionVO>)request.getAttribute("listaUbicOrientacion");													
        if (solList != null && solList.size() >0){
            for (int indiceSol=0;indiceSol<solList.size();indiceSol++)
            {
                voS = solList.get(indiceSol);
                
    %>
        listaUbicacionesOri[<%=indiceSol%>] = ['<%=voS.getCodigoUbic()%>','<%=voS.getProvincia()%>','<%=voS.getAmbito()%>','<%=voS.getMunicipio()%>','<%=voS.getDireccion()%>', '<%=voS.getHoras()%>', '<%=voS.getDespachos()%>','<%=voS.getAulaGrupal()%>','<%=voS.getValoracion()%>','<%=voS.getTotal()%>','<%=voS.getHorasAdj()%>'];
        listaUbicacionesOriTabla[<%=indiceSol%>] = ['<%=voS.getProvincia()%>','<%=voS.getAmbito()%>','<%=voS.getMunicipio()%>','<%=voS.getDireccion()%>', '<%=voS.getHoras()%>', '<%=voS.getDespachos()%>','<%=voS.getAulaGrupal()%>','<%=voS.getValoracion()%>','<%=voS.getTotal()%>','<%=voS.getHorasAdj()%>'];
    <%
            }// for
        }// if
    %>
    tabUbicacionesOri.lineas=listaUbicacionesOriTabla;
    tabUbicacionesOri.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('listaUbicacionesOri');
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[7].style.backgroundColor = 'forestgreen';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[8].style.backgroundColor = 'forestgreen';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[9].style.backgroundColor = 'FF9966';
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[9].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[9].style.width = '100%';
                    }
                catch(err){
                }
            }
    
    //Tabla Trayectoria de la entidad
    var tabTrayectoriaOri;
    var listaTrayectoriaOri = new Array();
    var listaTrayectoriaOriTabla = new Array();
    
    tabTrayectoriaOri = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTrayectoriaOri'), 874);
    tabTrayectoriaOri.addColumna('391','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col1")%>");
    tabTrayectoriaOri.addColumna('74','right',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col2")%>");                                                            
    tabTrayectoriaOri.addColumna('391','left',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ori.tablaTrayectoria.col3")%>");
    
    tabTrayectoriaOri.displayCabecera=true;
    tabTrayectoriaOri.height = 60;
    
    <%  		
        FilaTrayectoriaOrientacionVO voT = null;
        List<FilaTrayectoriaOrientacionVO> traList = (List<FilaTrayectoriaOrientacionVO>)request.getAttribute("listaTrayectoriaOrientacion");													
        if (traList!= null && traList.size() >0){
            for (int indiceTra=0;indiceTra<traList.size();indiceTra++)
            {
                voT = traList.get(indiceTra);
                
    %>
        listaTrayectoriaOri[<%=indiceTra%>] = ['<%=voT.getOriOritrayCod()%>','<%=voT.getDescTrayectoria()%>','<%=voT.getDuracionServicio()%>','<%=voT.getOrganismo()%>'];
        listaTrayectoriaOriTabla[<%=indiceTra%>] = ['<%=voT.getDescTrayectoria()%>','<%=voT.getDuracionServicio()%>','<%=voT.getOrganismo()%>'];
    <%
            }// for
        }// if
    %>
        
    tabTrayectoriaOri.lineas=listaTrayectoriaOriTabla;
    
    calcularTrayectoriaEntidad();
    
    tabTrayectoriaOri.displayTabla();
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        try{
            var div = document.getElementById('listaTrayectoriaOri');
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[2].style.width = '100%';
            div.children[0].children[1].children[0].children[0].children[0].children[2].style.width = '100%';
        }
        catch(err){
        }
    }
</script>

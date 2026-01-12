<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaUbicOrientacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaTrayectoriaOrientacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    EntidadVO entidad = (EntidadVO)request.getAttribute("entidad");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>

<script type="text/javascript">
        var mensajeValidacion = "";
        
        function pulsarAltaUbicacionORI(){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarNuevoAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&control='+control.getTime(),280,820,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarNuevoAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&control='+control.getTime(),300,850,'no','no');
            }
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaUbicaciones(result);
                }
            }
        }
        
        function pulsarModificarUbicacionORI(){
            if(tabUbicacionesOri14.selectedIndex != -1) {
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarModifAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&idUbic='+listaUbicacionesOri14[tabUbicacionesOri14.selectedIndex][0]+'&control='+control.getTime(),280,820,'no','no');
                }else{
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarModifAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&idUbic='+listaUbicacionesOri14[tabUbicacionesOri14.selectedIndex][0]+'&control='+control.getTime(),300,850,'no','no');
                }
                if (result != undefined){
                    if(result[0] == '0'){
                        recargarTablaUbicaciones(result);
                    }
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarEliminarUbicacionORI(){

            if(tabUbicacionesOri14.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&idUbic='+listaUbicacionesOri14[tabUbicacionesOri14.selectedIndex][0]+'&control='+control.getTime();
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
                            else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                                listaUbicacionesNueva[j] = hijos[j].childNodes[0].nodeValue;
                            }                   
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
                                    else if(hijosFila[cont].nodeName=="COD_POSTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORAS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESPACHOS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="VALORACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TOTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORASADJ"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[11] = '-';
                                        }
                                    }
                                }
                                listaUbicacionesNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            recargarTablaUbicaciones(listaUbicacionesNueva);
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarValorarUbicacionORI(){
            if(tabUbicacionesOri14.selectedIndex != -1) {
                var control = new Date();
                var result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarValorarAmbitoORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&idUbic='+listaUbicacionesOri14[tabUbicacionesOri14.selectedIndex][0]+'&control='+control.getTime(),580,475,'no','no');
                if (result != undefined){
                    if(result[0] == '0'){
                        recargarTablaUbicaciones(result);
                    }
                }
            }
            else{
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function recargarTablaUbicaciones(result){
            try{
                document.getElementById('codEntidadOri14').value = result[1];
            }catch(err){
                
            }
            var fila;
            listaUbicacionesOri14 = new Array();
            listaUbicacionesOri14Tabla = new Array();
            for(var i = 2;i< result.length; i++){
                fila = result[i];
                listaUbicacionesOri14[i-2] = fila;
                listaUbicacionesOri14Tabla[i-2] = [fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11]];
                listaUbicacionesOri14Tabla_titulos[i-2] = [fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11]];
            }
    
            inicializarTabla();
        }
        
        function validarDatosAmbitos(){
            mensajeValidacion = "";
            return correcto;
        }
    
        function inicializarTabla(){
            tabUbicacionesOri14 = new FixedColumnTable(document.getElementById('listaUbicacionesOri14'), 850, 876, 'listaUbicacionesOri14');

            tabUbicacionesOri14.addColumna('82','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col1")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col1.title")%>");
            tabUbicacionesOri14.addColumna('75','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col2")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col2.title")%>");
            tabUbicacionesOri14.addColumna('100','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col3")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col3.title")%>");
            tabUbicacionesOri14.addColumna('250','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col4")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col4.title")%>");
            tabUbicacionesOri14.addColumna('70','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col5")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col5.title")%>");
            tabUbicacionesOri14.addColumna('80','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col6")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col6.title")%>");
            tabUbicacionesOri14.addColumna('160','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col7")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col7.title")%>");
            tabUbicacionesOri14.addColumna('150','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col8")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col8.title")%>");
            tabUbicacionesOri14.addColumna('100','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col9")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col9.title")%>");
            tabUbicacionesOri14.addColumna('89','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col10")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col10.title")%>");
            tabUbicacionesOri14.addColumna('100','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col11")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.solicHoras.col11.title")%>");

            tabUbicacionesOri14.numColumnasFijas = 3;

            for(var cont = 0; cont < listaUbicacionesOri14Tabla.length; cont++){
                tabUbicacionesOri14.addFilaConFormato(listaUbicacionesOri14Tabla[cont], listaUbicacionesOri14Tabla_titulos[cont], listaUbicacionesOri14Tabla_estilos[cont])
            }

            tabUbicacionesOri14.displayCabecera=true;
            tabUbicacionesOri14.height = 360;

            tabUbicacionesOri14.altoCabecera = 30;

            tabUbicacionesOri14.scrollWidth = 1256;

            tabUbicacionesOri14.dblClkFunction = 'dblClckTablaUbicacionesOri14';

            tabUbicacionesOri14.displayTabla();

            tabUbicacionesOri14.pack();
        }
    
        function dblClckTablaUbicacionesOri14(rowID,tableName){
            pulsarModificarUbicacionORI();
        }
    
        function refrescarPestanaAmbitos(){
            //Se actualizan los datos de la pestaña
                    
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=getListaAmbitosORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&control='+control.getTime();
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
                var listaUbicacionesNueva = extraerListaAmbitosORI14(nodos);
                var codigoOperacion = listaUbicacionesNueva[0];
                if(codigoOperacion=="0"){
                    recargarTablaUbicaciones(listaUbicacionesNueva);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else{
                        jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
            }//try-catch
        }
        
        function extraerListaAmbitosORI14(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaUbicaciones = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaUbicaciones[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                    listaUbicaciones[j] = hijos[j].childNodes[0].nodeValue;
                }                   
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
                        else if(hijosFila[cont].nodeName=="COD_POSTAL"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESPACHOS"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VALORACION"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TOTAL"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORASADJ"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[11] = '-';
                            }
                        }
                    }
                    listaUbicaciones[j] = fila;
                    fila = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            return listaUbicaciones;
        }
</script>
<body>
    <div id="barraProgresoAmbitosOri14" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoAmbitosOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoAmbitosOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
        <fieldset>
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "legend.ambitos.ambitosSolic")%></legend>
            <div id="listaUbicacionesOri14" class="tablaPers" style="padding: 5px; width:920px; height: 420px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
            <div class="botonera">
                <input type="button" id="btnNuevaSolicHorasOri" name="btnNuevaSolicHorasOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaUbicacionORI();">
                <input type="button" id="btnModificarSolicHorasOri" name="btnModificarSolicHorasOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarUbicacionORI();">
                <input type="button" id="btnEliminarSolicHorasOri" name="btnEliminarSolicHorasOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarUbicacionORI();">
                <input type="button" id="btnValorarSolicHorasOri" name="btnValorarSolicHorasOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.valorar")%>" onclick="pulsarValorarUbicacionORI();">
            </div>
        </fieldset>  
    </div>
</body>

<script type="text/javascript">
    //Tabla solicitud de horas de orientacion
    var tabUbicacionesOri14;
    var listaUbicacionesOri14 = new Array();
    var listaUbicacionesOri14Tabla = new Array();
    var listaUbicacionesOri14Tabla_titulos = new Array();
    var listaUbicacionesOri14Tabla_estilos = new Array();
    
    <%  		
        FilaUbicOrientacionVO voS = null;
        List<FilaUbicOrientacionVO> solList = (List<FilaUbicOrientacionVO>)request.getAttribute("listaUbicOrientacion");													
        if (solList != null && solList.size() >0){
            for (int indiceSol=0;indiceSol<solList.size();indiceSol++)
            {
                voS = solList.get(indiceSol);
                
    %>
        listaUbicacionesOri14[<%=indiceSol%>] = ['<%=voS.getCodigoUbic()%>','<%=voS.getProvincia()%>','<%=voS.getAmbito()%>','<%=voS.getMunicipio()%>','<%=voS.getDireccion()%>', '<%=voS.getCodPostal()%>', '<%=voS.getHoras()%>', '<%=voS.getDespachos()%>','<%=voS.getAulaGrupal()%>','<%=voS.getValoracion()%>','<%=voS.getTotal()%>','<%=voS.getHorasAdj()%>'];
        listaUbicacionesOri14Tabla[<%=indiceSol%>] = ['<%=voS.getProvincia()%>','<%=voS.getAmbito()%>','<%=voS.getMunicipio()%>','<%=voS.getDireccion()%>', '<%=voS.getCodPostal()%>', '<%=voS.getHoras()%>', '<%=voS.getDespachos()%>','<%=voS.getAulaGrupal()%>','<%=voS.getValoracion()%>','<%=voS.getTotal()%>','<%=voS.getHorasAdj()%>'];
        listaUbicacionesOri14Tabla_titulos[<%=indiceSol%>] = ['<%=voS.getProvincia()%>','<%=voS.getAmbito()%>','<%=voS.getMunicipio()%>','<%=voS.getDireccion()%>', '<%=voS.getCodPostal()%>', '<%=voS.getHoras()%>', '<%=voS.getDespachos()%>','<%=voS.getAulaGrupal()%>','<%=voS.getValoracion()%>','<%=voS.getTotal()%>','<%=voS.getHorasAdj()%>'];
    <%
            }// for
        }// if
    %>
    
    inicializarTabla();
</script>
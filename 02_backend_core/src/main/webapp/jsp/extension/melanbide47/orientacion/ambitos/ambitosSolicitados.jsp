<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaOriAmbitoSolicitadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriAmbitoSolicitadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = 1;
    if(sIdioma!=null && sIdioma!="")
        idiomaUsuario=Integer.parseInt(sIdioma);
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
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    int anionumExpediente =0;
    anionumExpediente = numExpediente != null ? Integer.parseInt(numExpediente.substring(0,4)):0;
    List<FilaOriAmbitoSolicitadoVO> solList = (List<FilaOriAmbitoSolicitadoVO>)request.getAttribute("listaAmbitosSolicitados");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>

<script type="text/javascript">
        var mensajeValidacion = "";
        function pulsarAltaAmbitoSolicitadoORI(){
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionAmbitoSolicitadoORI&tipo=0&esAmbiSolEdicion=0&numero=<%=numExpediente%>&control='+control.getTime(),600,1000,'si','no', function(result){
				if (result != undefined){
					if(result[0] == '0'){
						recargarTablaAmbitosSolicitadosORI(result);
                                                refrescarPestanaAmbitos();
					}
				}
			});
            
        }
        
        function pulsarModificarAmbitoSolicitadoORI(){
            if(tabAmbitosSolicitadosORI.selectedIndex != -1) {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionAmbitoSolicitadoORI&tipo=0&esAmbiSolEdicion=1&numero=<%=numExpediente%>&idAmbitoSolicitado='+listaAmbitosSolicitadoORI[tabAmbitosSolicitadosORI.selectedIndex][0]+'&control='+control.getTime(),600,1000,'si','no', function(result){
					if (result != undefined){
						if(result[0] == '0'){
							recargarTablaAmbitosSolicitadosORI(result);
                                                        refrescarPestanaAmbitos();
						}
					}
				});
            } 
            else {
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarEliminarAmbitoSolicitadoORI(){

            if(tabAmbitosSolicitadosORI.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarAmbitoSolicitadoORI&tipo=0&numero=<%=numExpediente%>&idAmbitoSolicitado='+listaAmbitosSolicitadoORI[tabAmbitosSolicitadosORI.selectedIndex][0]+'&control='+control.getTime();
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
                        var listaAmbitosSolicitadosNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaAmbitosSolicitadosNueva[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ORI_AMB_SOL_COD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NUM_EXP"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_BLOQUES"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_UBIC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS_DESC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO_DESC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                }
                                listaAmbitosSolicitadosNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            recargarTablaAmbitosSolicitadosORI(listaAmbitosSolicitadosNueva);
                            refrescarPestanaAmbitos();
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
        
        function recargarTablaAmbitosSolicitadosORI(result){
            var fila;
            listaAmbitosSolicitadoORI = new Array();
            listaAmbitosSolicitadoORITabla = new Array();
            listaAmbitosSolicitadoORITabla_titulos = new Array();
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaAmbitosSolicitadoORI[i-1] = [fila[0], fila[1], fila[2], fila[3], fila[4],fila[5],fila[6],fila[7]];
                listaAmbitosSolicitadoORITabla[i-1] = [fila[6], fila[7],fila[4], fila[5]];
                listaAmbitosSolicitadoORITabla_titulos[i-1] = [fila[6], fila[7],fila[4], fila[5]];
                listaAmbitosSolicitadoORITabla_estilos[i-1] = [fila[6], fila[7],fila[4], fila[5]];
            }
            inicializarTablaAmbitosSolicitadosORI();
            
            //tabAmbitosSolicitadosORI.lineas=listaAmbitosSolicitadoORITabla;
            //tabAmbitosSolicitadosORI.displayTabla();
        }

        function inicializarTablaAmbitosSolicitadosORI(){
            
            tabAmbitosSolicitadosORI = new FixedColumnTable(document.getElementById('listaAmbitosSolicitadoORI'), 730, 1025, 'listaAmbitosSolicitadoORI');
            //tabAmbitosSolicitadosORI = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaAmbitosSolicitadoORI'));        

            tabAmbitosSolicitadosORI.addColumna('200','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col1")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col1.title")%>");
            tabAmbitosSolicitadosORI.addColumna('200','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col2")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col2.title")%>");
            tabAmbitosSolicitadosORI.addColumna('150','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col3")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col3.title")%>");
            tabAmbitosSolicitadosORI.addColumna('150','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col4")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col4.title")%>");
            
            //tabAmbitosSolicitadosORI.numColumnasFijas = 3;

            for(var cont = 0; cont < listaAmbitosSolicitadoORITabla.length; cont++){
                tabAmbitosSolicitadosORI.addFilaConFormato(listaAmbitosSolicitadoORITabla[cont], listaAmbitosSolicitadoORITabla_titulos[cont], listaAmbitosSolicitadoORITabla_estilos[cont])
            }

            tabAmbitosSolicitadosORI.displayCabecera=true;
            tabAmbitosSolicitadosORI.height = 250;
            tabAmbitosSolicitadosORI.altoCabecera = 40;
            //tabAmbitosSolicitadosORI.scrollWidth = 600;
            tabAmbitosSolicitadosORI.dblClkFunction = 'dblClckTabAmbitosSolicitadosORI';
            //tabAmbitosSolicitadosORI.lineas=listaAmbitosSolicitadoORITabla;
            tabAmbitosSolicitadosORI.displayTabla();

            tabAmbitosSolicitadosORI.pack();
        }
    
        function dblClckTabAmbitosSolicitadosORI(rowID,tableName){
            pulsarModificarAmbitoSolicitadoORI();
        }
    
        function refrescarPestanaAmbitosSolicitadosORI(){
            //Se actualizan los datos de la pestaña
                    
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=getListaAmbitosSolicitadosORI&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                var listaAmbitosSolicitadosNueva = extraerListaAmbitosSolicitadosORI(nodos);
                var codigoOperacion = listaAmbitosSolicitadosNueva[0];
                if(codigoOperacion=="0"){
                    recargarTablaAmbitosSolicitadosORI(listaAmbitosSolicitadosNueva);
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
        
        function extraerListaAmbitosSolicitadosORI(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaAmbitosSolicitados = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaAmbitosSolicitados[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ORI_AMB_SOL_COD"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NUM_EXP"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_BLOQUES"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_UBIC"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS_DESC"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO_DESC"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                    }
                    listaAmbitosSolicitados[j] = fila;
                    fila = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            return listaAmbitosSolicitados;
        }
</script>
<div id="body">
    <div style="clear: both;">
        <fieldset>
            <h3><legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "legend.ambitos.ambitosSolic")%></legend></h3>
            <div id="listaAmbitosSolicitadoORI" class="tablaPers" style="font-size: 11px"></div>
            <div class="botonera">
                <input type="button" id="btnNuevoAmbitoSolicitadoOri" name="btnNuevoAmbitoSolicitadoOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaAmbitoSolicitadoORI();" />
                <input type="button" id="btnModificarAmbitoSolicitadoOri" name="btnModificarAmbitoSolicitadoOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAmbitoSolicitadoORI();" />
                <input type="button" id="btnEliminarAmbitoSolicitadoOri" name="btnEliminarAmbitoSolicitadoOri" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAmbitoSolicitadoORI();" />
            </div>
        </fieldset>  
    </div>
</div>

<script type="text/javascript">
    //Tabla solicitud de horas de orientacion
    var tabAmbitosSolicitadosORI;
    var listaAmbitosSolicitadoORI = new Array();
    var listaAmbitosSolicitadoORITabla = new Array();
    var listaAmbitosSolicitadoORITabla_titulos = new Array();
    var listaAmbitosSolicitadoORITabla_estilos = new Array();
    <%  		
        FilaOriAmbitoSolicitadoVO voS = null;
        if (solList != null && solList.size() >0){
            for (int indiceSol=0;indiceSol<solList.size();indiceSol++)
            {
                voS = solList.get(indiceSol);
     %>
            listaAmbitosSolicitadoORI[<%=indiceSol%>] = ['<%=voS.getOriAmbSolCod()%>','<%=voS.getOriAmbSolNumExp()%>','<%=voS.getOriAmbSolTerHis()%>','<%=voS.getOriAmbSolAmbito()%>','<%=voS.getOriAmbSolNroBloques()%>', '<%=voS.getOriAmbSolNroUbic()%>','<%=voS.getOriAmbSolTerHisDesc()%>','<%=voS.getOriAmbSolAmbitoDesc()%>'];
            listaAmbitosSolicitadoORITabla[<%=indiceSol%>] = ['<%=voS.getOriAmbSolTerHisDesc()%>','<%=voS.getOriAmbSolAmbitoDesc()%>','<%=voS.getOriAmbSolNroBloques()%>', '<%=voS.getOriAmbSolNroUbic()%>'];
            listaAmbitosSolicitadoORITabla_titulos[<%=indiceSol%>] = ['<%=voS.getOriAmbSolTerHisDesc()%>','<%=voS.getOriAmbSolAmbitoDesc()%>','<%=voS.getOriAmbSolNroBloques()%>', '<%=voS.getOriAmbSolNroUbic()%>'];
    <%
            }// for
        }// if
    %>
    
    inicializarTablaAmbitosSolicitadosORI();
    //tabAmbitosSolicitadosORI.lineas=listaAmbitosSolicitadoORITabla;
    //tabAmbitosSolicitadosORI.displayTabla();
</script>

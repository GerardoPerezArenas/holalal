<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaAmbitoSolicitadoCempVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoSolicitadoCempVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>
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
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    int anionumExpediente =0;
    anionumExpediente = numExpediente != null ? Integer.parseInt(numExpediente.substring(0,4)):0;
    List<FilaAmbitoSolicitadoCempVO> solList = (List<FilaAmbitoSolicitadoCempVO>)request.getAttribute("listaAmbitosSolicitadosCEMP");
    EntidadVO entidad = (EntidadVO)request.getAttribute("entidad");
    Long codEntidadCEMP = (entidad!=null && entidad.getOriEntCod()!=null ? entidad.getOriEntCod() : 0);
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>

<script type="text/javascript">
        var mensajeValidacion = "";

        function pulsarAltaAmbitoSolicitadoCEMP(){
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarAltaEdicionAmbitoSolicitadoCEMP&tipo=0&esAmbiSolEdicion=0&numero=<%=numExpediente%>'
                              +'&oriAmbCeEntCod=<%=codEntidadCEMP%>&control='+control.getTime(),600,1000,'si','no', function(result){
				if (result != undefined){
					if(result[0] == '0'){
						recargarTablaAmbitosSolicitadosCEMP(result);
					}
				}
			});
            
        }
        
        function pulsarModificarAmbitoSolicitadoCEMP(){
            if(tabAmbitosSolicitadosCEMP.selectedIndex != -1) {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarAltaEdicionAmbitoSolicitadoCEMP&tipo=0&esAmbiSolEdicion=1&numero=<%=numExpediente%>'
                                    +'&oriAmbCeEntCod=<%=codEntidadCEMP%>&idAmbitoSolicitado='+listaAmbitosSolicitadoCEMP[tabAmbitosSolicitadosCEMP.selectedIndex][0]+'&control='+control.getTime(),600,1000,'si','no', function(result){
					if (result != undefined){
						if(result[0] == '0'){
							recargarTablaAmbitosSolicitadosCEMP(result);
						}
					}
				});
            } 
            else {
                jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function pulsarEliminarAmbitoSolicitadoCEMP(){

            if(tabAmbitosSolicitadosCEMP.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=eliminarAmbitoSolicitadoCEMP&tipo=0&numero=<%=numExpediente%>&oriAmbCeCodId='+listaAmbitosSolicitadoCEMP[tabAmbitosSolicitadosCEMP.selectedIndex][0]+'&control='+control.getTime();
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
                                    if(hijosFila[cont].nodeName=="ORI_AMB_CE_COD_ID"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_COD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_ENT_COD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_ANOCONV"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_TIPO_AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_NUMCE"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NUM_EXP"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_CE_TIPO_AMBITO_DESC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                }
                                listaAmbitosSolicitadosNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            recargarTablaAmbitosSolicitadosCEMP(listaAmbitosSolicitadosNueva);
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
        
        function recargarTablaAmbitosSolicitadosCEMP(result){
            var fila;
            listaAmbitosSolicitadoCEMP = new Array();
            listaAmbitosSolicitadoCEMPTabla = new Array();
            listaAmbitosSolicitadoCEMPTabla_titulos = new Array();
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaAmbitosSolicitadoCEMP[i-1] = [fila[0], fila[1], fila[2], fila[3], fila[4],fila[5],fila[6],fila[7]];
                listaAmbitosSolicitadoCEMPTabla[i-1] = [fila[8], fila[4],fila[6]!="null"?fila[6]:"-"];
                //listaAmbitosSolicitadoCEMPTabla_titulos[i-1] = [fila[6], fila[7],fila[4], fila[5]];
                //listaAmbitosSolicitadoCEMPTabla_estilos[i-1] = [fila[6], fila[7],fila[4], fila[5]];
            }
            inicializarTablaAmbitosSolicitadosCEMP();
            
            tabAmbitosSolicitadosCEMP.lineas=listaAmbitosSolicitadoCEMPTabla;
            tabAmbitosSolicitadosCEMP.displayTabla();
        }

        function inicializarTablaAmbitosSolicitadosCEMP(){
            
            //tabAmbitosSolicitadosCEMP = new FixedColumnTable(document.getElementById('listaAmbitosSolicitadoCEMP'), 730, 1025, 'listaAmbitosSolicitadoCEMP');
            tabAmbitosSolicitadosCEMP = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaAmbitosSolicitadoCEMP'));        

            tabAmbitosSolicitadosCEMP.addColumna('200','center',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.ambitos.tabla.ambitos.solicitados.col1")%>", "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col1.title")%>");
            tabAmbitosSolicitadosCEMP.addColumna('200','center',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.ambitos.tabla.ambitos.solicitados.col2")%>", "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col2.title")%>");
            tabAmbitosSolicitadosCEMP.addColumna('150','center',"<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.ambitos.tabla.ambitos.solicitados.col3")%>", "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"label.ambitos.tabla.ambitos.solicitados.col3.title")%>");
            
            //tabAmbitosSolicitadosCEMP.numColumnasFijas = 3;

            //for(var cont = 0; cont < listaAmbitosSolicitadoCEMPTabla.length; cont++){
            //    tabAmbitosSolicitadosCEMP.addFilaConFormato(listaAmbitosSolicitadoCEMPTabla[cont], listaAmbitosSolicitadoCEMPTabla_titulos[cont], listaAmbitosSolicitadoCEMPTabla_estilos[cont])
            //}

            tabAmbitosSolicitadosCEMP.displayCabecera=true;
            tabAmbitosSolicitadosCEMP.height = 250;
            //tabAmbitosSolicitadosCEMP.altoCabecera = 40;
            //tabAmbitosSolicitadosCEMP.scrollWidth = 600;
            tabAmbitosSolicitadosCEMP.dblClkFunction = 'dblClckTabAmbitosSolicitadosCEMP';
            tabAmbitosSolicitadosCEMP.lineas=listaAmbitosSolicitadoCEMPTabla;
            //tabAmbitosSolicitadosCEMP.displayTabla();

            //tabAmbitosSolicitadosCEMP.pack();
        }
    
        function dblClckTabAmbitosSolicitadosCEMP(rowID,tableName){
            pulsarModificarAmbitoSolicitadoCEMP();
        }
    
        function refrescarPestanaAmbitosSolicitadosCEMP(){
            //Se actualizan los datos de la pestaña
                    
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=getListaAmbitosSolicitadosCEMP&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                var listaAmbitosSolicitadosNueva = extraerListaAmbitosSolicitadosCEMP(nodos);
                var codigoOperacion = listaAmbitosSolicitadosNueva[0];
                if(codigoOperacion=="0"){
                    recargarTablaAmbitosSolicitadosCEMP(listaAmbitosSolicitadosNueva);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=String.format(meLanbide32I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide32I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=String.format(meLanbide32I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide32I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=String.format(meLanbide32I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide32I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }else{
                        jsp_alerta("A",'<%=String.format(meLanbide32I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide32I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=String.format(meLanbide32I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide32I18n.getMensaje(idiomaUsuario, "label.ambitos.tituloPestana"))%>');
            }//try-catch
        }
        
        function extraerListaAmbitosSolicitadosCEMP(nodos){
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
                        if(hijosFila[cont].nodeName=="ORI_AMB_CE_COD_ID"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_COD"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_ENT_COD"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_ANOCONV"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_AMBITO"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_TIPO_AMBITO"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_NUMCE"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NUM_EXP"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ORI_AMB_CE_TIPO_AMBITO_DESC"){
                            if(hijosFila[cont].childNodes.length > 0){
                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
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
            <h3><legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "legend.ambitos.ambitosSolic")%></legend></h3>
            <div id="listaAmbitosSolicitadoCEMP" class="tablaPers" style="font-size: 11px"></div>
            <div class="botonera">
                <input type="button" id="btnNuevoAmbitoSolicitadoCemp" name="btnNuevoAmbitoSolicitadoCemp" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.nuevo")%>" onclick="pulsarAltaAmbitoSolicitadoCEMP();" />
                <input type="button" id="btnModificarAmbitoSolicitadoCemp" name="btnModificarAmbitoSolicitadoCemp" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.modificar")%>" onclick="pulsarModificarAmbitoSolicitadoCEMP();" />
                <input type="button" id="btnEliminarAmbitoSolicitadoCemp" name="btnEliminarAmbitoSolicitadoCemp" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.eliminar")%>" onclick="pulsarEliminarAmbitoSolicitadoCEMP();" />
            </div>
        </fieldset>  
    </div>
</div>

<script type="text/javascript">
    //Tabla solicitud de horas de orientacion
    var tabAmbitosSolicitadosCEMP;
    var listaAmbitosSolicitadoCEMP = new Array();
    var listaAmbitosSolicitadoCEMPTabla = new Array();
    var listaAmbitosSolicitadoCEMPTabla_titulos = new Array();
    var listaAmbitosSolicitadoCEMPTabla_estilos = new Array();
    <%  		
        FilaAmbitoSolicitadoCempVO voS = null;
        if (solList != null && solList.size() >0){
            for (int indiceSol=0;indiceSol<solList.size();indiceSol++)
            {
                voS = solList.get(indiceSol);
     %>
            listaAmbitosSolicitadoCEMP[<%=indiceSol%>] = ['<%=voS.getOriAmbCeCodId()%>','<%=voS.getOriAmbCeCod()%>','<%=voS.getOriAmbCeEntCod()%>','<%=voS.getOriAmbCeAnoconv()%>','<%=voS.getOriAmbCeAmbito()%>', '<%=voS.getOriAmbCeTipoAmbito()%>','<%=voS.getOriAmbCeNumce()%>','<%=voS.getOriAmbSolNumExp()%>'];
            listaAmbitosSolicitadoCEMPTabla[<%=indiceSol%>] = ['<%= voS.getOriAmbCeTipoAmbitoDesc()!=null && voS.getOriAmbCeTipoAmbitoDesc() != "null" ? voS.getOriAmbCeTipoAmbitoDesc() : "-" %>','<%=voS.getOriAmbCeAmbito()!=null && voS.getOriAmbCeAmbito()!="null" ? voS.getOriAmbCeAmbito() : "-"%>','<%=voS.getOriAmbCeNumce()!=null ? voS.getOriAmbCeNumce():"-"%>'];
            //listaAmbitosSolicitadoCEMPTabla_titulos[<%=indiceSol%>] = ['<%=voS.getOriAmbCeTipoAmbitoDesc()%>','<%=voS.getOriAmbCeAmbito()%>','<%=voS.getOriAmbCeNumce()%>'];
    <%
            }// for
        }// if
    %>
    
    inicializarTablaAmbitosSolicitadosCEMP();
    tabAmbitosSolicitadosCEMP.lineas=listaAmbitosSolicitadoCEMPTabla;
    tabAmbitosSolicitadosCEMP.displayTabla();
</script>

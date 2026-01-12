<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResumenVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.*" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>

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
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    EcaResumenVO datosResumen = (EcaResumenVO)request.getAttribute("datosResumen");
    
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
    
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    GeneralValueObject expedienteVO = null;
    String modoConsulta = "no";
    if(expForm != null)
    {
        expedienteVO = expForm.getExpedienteVO();
        if(expedienteVO != null)
        {
            modoConsulta = (String) expedienteVO.getAtributo("modoConsulta");
            if(modoConsulta != null)
            {
                modoConsulta = modoConsulta.toLowerCase();
            }
            else
            {
                modoConsulta = "no";
            }
        }
    }
%>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

<script type="text/javascript">         
    var mensajeValidacion = '';
    
    function actualizarDatosResumen(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=actualizarDatosResumen&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            var mensaje = null;
            for(j=0;hijos!=null && j<hijos.length;j++){                  
                if(hijos[j].nodeName=="MENSAJE"){
                    mensaje = hijos[j].childNodes[0].nodeValue;
                }       
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(mensaje != null && mensaje != ''){
                if(mensaje.toUpperCase() == '<%=ConstantesMeLanbide44.OK%>'){
                    jsp_alerta("A", '<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.resumen.actualizarDatos.ok")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.resumen.errorActualizarDatos")%>:'+'<br/><br/>'+mensaje);
                }
            }else{
                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.resumen.actualizarDatos.error.desconocido")%>');
            }
        }
        catch(err){
            alert(err);
        }//try-catch
    }
    
    function refrescarPantallaResumen(){
        try{
            var datos = getDatosResumenEca();
            recargarDatosResumenEca(datos);            
        }catch(err){
        }
        barraProgresoEca('off', 'barraProgresoResumenEca');
    }
    
    function getDatosResumenEca(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var datos = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getDatosResumenEca&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            datos = extraerDatosResumenEca(nodos);
        }
        catch(Err){
            datos[0] = "1";
        }//try-catch
        return datos;
    }
    
    
    
    function extraerDatosResumenEca(nodos){
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var datos = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
            
        for(j=0;hijos!=null && j<hijos.length;j++){
            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                datos[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")    
            else{
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                if(hijos[j].nodeName=="VALORES_SOLICITADO"){
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="NUM_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="NUM_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="GASTOS_GENERALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="FL_OTRAS_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PUB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PRIV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="TOT_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '';
                            }
                        }
                    }
                    datos[1] = fila;
                }//if(hijos[j].nodeName=="VALORES_SOLICITADO")                      
                else if(hijos[j].nodeName=="VALORES_CONCEDIDO"){
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="NUM_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="NUM_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="GASTOS_GENERALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PUB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PRIV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="TOT_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '';
                            }
                        }
                    }
                    datos[2] = fila;
                }//if(hijos[j].nodeName=="VALORES_CONCEDIDO")                      
                else if(hijos[j].nodeName=="VALORES_JUSTIFICADO"){
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="NUM_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="NUM_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="GASTOS_GENERALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PUB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PRIV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="TOT_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '';
                            }
                        }
                    }
                    datos[3] = fila;
                }//if(hijos[j].nodeName=="VALORES_JUSTIFICADO")                      
                else if(hijos[j].nodeName=="VALORES_PAGAR"){
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="NUM_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PROS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="NUM_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="IMP_PREP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="GASTOS_GENERALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PUB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="OTRAS_SUBV_PRIV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="TOT_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '';
                            }
                        }else if(hijosFila[cont].nodeName=="TOPE_GASTOS_GENERALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '';
                            }
                        }
                    }
                    datos[4] = fila;
                }
                fila = new Array();
            }
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return datos;
    }
    
    function recargarDatosResumenEca(datos){
            if(datos)
            {
                var columna;
                
                //COLUMNA SOLICITADO
                columna = datos[1];
                
                if(columna[5] != '')
                {
                    document.getElementById('radioOtrasAyudasSResumen').checked = true;
                    document.getElementById('radioOtrasAyudasNResumen').checked = false;
                    document.getElementById('impOrgPublicosResumen').style.display = 'table-row';
                    document.getElementById('impOrgPrivadosResumen').style.display = 'table-row';
                    document.getElementById('impOrgPublicosConcResumen').style.display = 'table-row';
                    document.getElementById('impOrgPrivadosConcResumen').style.display = 'table-row';
                }
                else
                {
                    document.getElementById('radioOtrasAyudasSResumen').checked = false;
                    document.getElementById('radioOtrasAyudasNResumen').checked = true;
                    document.getElementById('impOrgPublicosResumen').style.display = 'none';
                    document.getElementById('impOrgPrivadosResumen').style.display = 'none';
                    document.getElementById('impOrgPublicosConcResumen').style.display = 'none';
                    document.getElementById('impOrgPrivadosConcResumen').style.display = 'none';
                }
                
                document.getElementById('prospectoresSolicNumResumen').value = columna[0];
                document.getElementById('prospectoresSolicSolResumen').value = columna[1];
                document.getElementById('preparadoresSolicNumResumen').value = columna[2];
                document.getElementById('preparadoresSolicSolResumen').value = columna[3];
                document.getElementById('gastosGeneralesSolicResumen').value = columna[4];
                document.getElementById('impOrgPublicosResumen').value = columna[6];
                document.getElementById('impOrgPrivadosResumen').value = columna[7];
                document.getElementById('totSolResumen').value = columna[8];

                //COLUMNA CONCEDIDO
                columna = datos[2];
                
                document.getElementById('prospectoresAnexCargaNumResumen').value = columna[0];
                document.getElementById('prospectoresAnexCargaSolResumen').value = columna[1];
                document.getElementById('preparadoresAnexCargaNumResumen').value = columna[2];
                document.getElementById('preparadoresAnexCargaSolResumen').value = columna[3];
                document.getElementById('gastosGeneralesAnexCargaResumen').value = columna[4];
                document.getElementById('impOrgPublicosConcResumen').value = columna[5];
                document.getElementById('impOrgPrivadosConcResumen').value = columna[6];
                document.getElementById('totConcResumen').value = columna[7];

                //COLUMNA JUSTIFICADO
                columna = datos[3];
                
                document.getElementById('prospectoresAnexNumResumen').value = columna[0];
                document.getElementById('prospectoresAnexSolResumen').value = columna[1];
                document.getElementById('preparadoresAnexNumResumen').value = columna[2];
                document.getElementById('preparadoresAnexSolResumen').value = columna[3];
                document.getElementById('gastosGeneralesAnexResumen').value = columna[4];
                document.getElementById('impOrgPublicosJusResumen').value = columna[5];
                document.getElementById('impOrgPrivadosJusResumen').value = columna[6];
                document.getElementById('totJusResumen').value = columna[7];

                //COLUMNA PAGADO
                columna = datos[4];
                
                document.getElementById('prospectoresPagadoNumResumen').value = columna[0];
                document.getElementById('prospectoresPagadoImpResumen').value = columna[1];
                document.getElementById('preparadoresPagadoNumResumen').value = columna[2];
                document.getElementById('preparadoresPagadoImpResumen').value = columna[3];
                document.getElementById('gastosGeneralesPagadoResumen').value = columna[4];
                document.getElementById('impOrgPublicosPagResumen').value = columna[5];
                document.getElementById('impOrgPrivadosPagResumen').value = columna[6];
                document.getElementById('totPagadoResumen').value = columna[7];

        }
        
        //calcularTotSubvResumen();
        
        
        //FormatNumber(document.getElementById('totJusResumen').value, 8, 2, document.getElementById('totJusResumen').id);
        
        activarModoConsulta();
		ajustarDecimalesImportesResumen(true);
    }
    
    function cargarDetalleProspectoresResumen(){
        var control = new Date();   
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarDetalleProspectoresResumen&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),480,922,'no','no');
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarDetalleProspectoresResumen&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),500,952,'no','no');
        }
        
    }
    
    function cargarDetallePreparadoresResumen(){
        var control = new Date();   
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarDetallePreparadoresResumen&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),480,1100,'no','no');
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarDetallePreparadoresResumen&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),500,1130,'no','no');
        }
        
    }
    
    function guardarDatosResumen(){
        if(validarResumenEca()){
            barraProgresoEca('on', 'barraProgresoSolicitudEca');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            var gastos = document.getElementById('gastosGeneralesPagadoResumen').value;
            var tope='0';
            <%
                if(datosResumen != null)
                {
            %>
                    tope = '<%=datosResumen.getTopeGastosGenerales_pag()%>';
            <% } %>
            gastos = gastos.replace('.', '');                
            gastos = gastos.replace(/,/g, '.');
            if(gastos > tope)
            {
                jsp_alerta("A", "Los gastos superan el porcentaje estimado");
            }
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=guardarDatosResumen&tipo=0&numero=<%=numExpediente%>'
                        +'&subvPriv='+convertirANumero(document.getElementById('impOrgPrivadosJusResumen').value)
                        +'&subvPub='+convertirANumero(document.getElementById('impOrgPublicosJusResumen').value)
                        +'&totSubv='+convertirANumero(document.getElementById('totJusResumen').value)
                        +'&gastos='+convertirANumero(document.getElementById('gastosGeneralesPagadoResumen').value)
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
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }  
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                barraProgresoEca('off', 'barraProgresoSolicitudEca');
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    mostrarPestanasSolicitudEca(1);
                    actualizarOtrasPestanasEca();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.noSolic")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
        }else{
            jsp_alerta("A", mensajeValidacion);
        }
    }
    
    function validarResumenEca(){
        mensajeValidacion = '';
        var correcto = true;
        if(document.getElementById('impOrgPublicosJusResumen').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPublicosJusResumen'), 10, 2)){
                    correcto = false;
                    document.getElementById('impOrgPublicosJusResumen').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.resumen.impPubJusIncorrecto")%>';
                }else{
                    document.getElementById('impOrgPublicosJusResumen').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('impOrgPublicosJusResumen').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.resumen.impPubJusIncorrecto")%>';
            }
        }
        
        
        if(document.getElementById('impOrgPrivadosJusResumen').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('impOrgPrivadosJusResumen'), 10, 2)){
                    correcto = false;
                    document.getElementById('impOrgPrivadosJusResumen').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.resumen.impPrivJusIncorrecto")%>';
                }else{
                    document.getElementById('impOrgPrivadosJusResumen').removeAttribute('style');
                }
            }catch(err){
                correcto = false;
                document.getElementById('impOrgPrivadosJusResumen').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.resumen.impPrivJusIncorrecto")%>';
            }
        }
        return correcto;
    }
    
    function calcularTotSubvResumen(){
        copiarValores();
        calcularTotSubvResumenJustificado();
        calcularTotSubvResumenSubvencionable();
    }
    
    function calcularTotSubvResumenJustificado(){
        var impPros = document.getElementById('prospectoresAnexSolResumen');
        var impPrep = document.getElementById('preparadoresAnexSolResumen');
        var gastos = document.getElementById('gastosGeneralesAnexResumen');
        var impPub = document.getElementById('impOrgPublicosJusResumen');
        var impPriv = document.getElementById('impOrgPrivadosJusResumen');
       
        var result = 0.0;
        var valor = null;
        
        if(impPros.value != '' && validarNumericoDecimalEca(impPros, 8, 2)){
            try{
                valor = impPros.value;
                valor = convertirANumero(valor);
                result = parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPrep.value != '' && validarNumericoDecimalEca(impPrep, 8, 2)){
            try{
                valor = impPrep.value;
                valor = convertirANumero(valor);
                result += parseFloat(valor);
            }catch(err){
                
            }
        }
        if(gastos.value != '' && validarNumericoDecimalEca(gastos, 8, 2)){
            try{
                valor = gastos.value;
                valor = convertirANumero(valor);
                result += parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPub.value != '' && validarNumericoDecimalEca(impPub, 20, 2)){
            try{                
                valor = impPub.value;
                valor = convertirANumero(valor);
                result -= parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPriv.value != '' && validarNumericoDecimalEca(impPriv, 20, 2)){
            try{                
                valor = impPriv.value;
                valor = convertirANumero(valor);
                result -= parseFloat(valor);
            }catch(err){
                
            }
        }
        
        try{
            if (isFinite(result)){

                result = result.toFixed(2);
                if(result < 0){
                    result = 0;
                }
                document.getElementById('totJusResumen').value = result;
                reemplazarPuntosEca(document.getElementById('totJusResumen'));
            }
        }catch(err){

        }
    }

    function calcularTotSubvResumenSubvencionable(){
        var impPros = document.getElementById('prospectoresPagadoImpResumen');
        var impPrep = document.getElementById('preparadoresPagadoImpResumen');
        var gastos = document.getElementById('gastosGeneralesPagadoResumen');
        var impPub = document.getElementById('impOrgPublicosPagResumen');
        var impPriv = document.getElementById('impOrgPrivadosPagResumen');
       
        var result = 0.0;
        var valor = null;
        
        if(impPros.value != '' && validarNumericoDecimalEca(impPros, 8, 2)){
            try{
                valor = impPros.value;
                valor = convertirANumero(valor);
                result = parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPrep.value != '' && validarNumericoDecimalEca(impPrep, 8, 2)){
            try{
                valor = impPrep.value;
                valor = convertirANumero(valor);
                result += parseFloat(valor);
            }catch(err){
                
            }
        }
        if(gastos.value != '' && validarNumericoDecimalEca(gastos, 8, 2)){
            try{
                valor = gastos.value;
                valor = convertirANumero(valor);
                result += parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPub.value != '' && validarNumericoDecimalEca(impPub, 20, 2)){
            try{                
                valor = impPub.value;
                valor = convertirANumero(valor);
                result -= parseFloat(valor);
            }catch(err){
                
            }
        }
        if(impPriv.value != '' && validarNumericoDecimalEca(impPriv, 20, 2)){
            try{                
                valor = impPriv.value;
                valor = convertirANumero(valor);
                result -= parseFloat(valor);
            }catch(err){
                
            }
        }
        
        try{
            if (isFinite(result)){

                result = result.toFixed(2);
                if(result < 0){
                    result = 0;
                }
                document.getElementById('totPagadoResumen').value = result;
                reemplazarPuntosEca(document.getElementById('totPagadoResumen'));
            }
        }catch(err){

        }
    }
            
    function ajustarDecimalesImportesResumen(refrescando){
        //refrescando indica si se llama desde la funcion de refrescar la pantalla o desde la carga inicial.
        //si se llama desde la funcion de refrescar, habra que formatear todos los campos
        var campo;
        
        if(refrescando){
            campo = document.getElementById('prospectoresSolicSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('preparadoresSolicSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('gastosGeneralesSolicResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPublicosResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPrivadosResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('totSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('prospectoresAnexCargaSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('preparadoresAnexCargaSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('gastosGeneralesAnexCargaResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPublicosConcResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPrivadosConcResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('totConcResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('prospectoresAnexSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('preparadoresAnexSolResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('gastosGeneralesAnexResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('prospectoresPagadoImpResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('preparadoresPagadoImpResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('gastosGeneralesPagadoResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            /*reemplazarPuntosEca(document.getElementById('impOrgPublicosJusResumen'));
            reemplazarPuntosEca(document.getElementById('impOrgPrivadosJusResumen'));
            reemplazarPuntosEca(document.getElementById('totJusResumen'));
            reemplazarPuntosEca(document.getElementById('impOrgPublicosPagResumen'));
            reemplazarPuntosEca(document.getElementById('impOrgPrivadosPagResumen'));
            reemplazarPuntosEca(document.getElementById('totPagadoResumen'));*/
            
            
            
            campo = document.getElementById('impOrgPublicosJusResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPrivadosJusResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('totJusResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPublicosPagResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('impOrgPrivadosPagResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('totPagadoResumen');
            reemplazarPuntosEca(campo);
            ajustarDecimalesCampo(campo, 2);
        }

        campo = document.getElementById('impOrgPublicosJusResumen');
        ajustarDecimalesCampo(campo, 2);

        campo = document.getElementById('impOrgPrivadosJusResumen');
        ajustarDecimalesCampo(campo, 2);

        //reemplazarPuntosEca(document.getElementById('totJusResumen'));
        //FormatNumber(document.getElementById('totJusResumen').value, 8, 2, document.getElementById('totJusResumen').id);

        campo = document.getElementById('totJusResumen');
        ajustarDecimalesCampo(campo, 2);

        campo = document.getElementById('impOrgPublicosPagResumen');
        ajustarDecimalesCampo(campo, 2);

        campo = document.getElementById('impOrgPrivadosPagResumen');
        ajustarDecimalesCampo(campo, 2);

        //reemplazarPuntosEca(document.getElementById('totPagadoResumen'));
        //FormatNumber(document.getElementById('totPagadoResumen').value, 8, 2, document.getElementById('totPagadoResumen').id);

        campo = document.getElementById('totPagadoResumen');
        ajustarDecimalesCampo(campo, 2);
    }
    
    function copiarValores(){
        document.getElementById('impOrgPublicosPagResumen').value = document.getElementById('impOrgPublicosJusResumen').value;
        document.getElementById('impOrgPrivadosPagResumen').value = document.getElementById('impOrgPrivadosJusResumen').value;
        //document.getElementById('totPagadoResumen').value = document.getElementById('totJusResumen').value;
    }
    
    
    
    function activarModoConsulta(){
        <%
            if(modoConsulta != null && modoConsulta.equalsIgnoreCase("si"))
            {
        %>
                document.getElementById('impOrgPublicosJusResumen').disabled = true;
                document.getElementById('impOrgPublicosJusResumen').readOnly = true;
                document.getElementById('impOrgPrivadosJusResumen').disabled = true;
                document.getElementById('impOrgPrivadosJusResumen').readOnly = true;
        <%
            }
        %>
    }
</script>

<body class="contenidoPantalla etiqueta">
    <div class="tab-page" id="tabPage353" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana353"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage353" ) );</script>
        <div style="clear: both;">
            <div id="barraProgresoResumenEca" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px" height="100%">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span id="msgGuardandoDatosResumen">
                                                        <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
            <div style="width: 920px; float: left; text-align: left; clear: both; height: 414px; overflow-x: hidden; overflow-y: auto; border: 1px solid #999999" onscroll="deshabilitarRadiosSolicitudEca();">
                <table style="width: 910px; font-size: 12px; text-align: center; border-collapse: collapse;" cellpadding="1px">
                    <tr>
                        <td width="28%" ></td>
                        <td width="6%" ></td>
                        <td width="6%" ></td>
                        <td width="5%" ></td>
                        <td width="6%" ></td>
                        <td width="6%" ></td>
                        <td width="5%" ></td>
                        <td width="6%" ></td>
                        <td width="6%" ></td>
                        <td width="5%" ></td>
                        <td width="6%" ></td>
                        <td width="6%" ></td>
                        <td width="5%" ></td>
                        <td width="4%" ></td>
                    </tr>



                    <tr>
                        <td colspan="13" style="text-align: left;" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.actuacionSubvencionable")%></td>
                    </tr>
                    <tr>
                        <td colspan="10">
                            <div class="separadorTabla"></div>
                        </td>
                    </tr>
                    <tr class="negrita">
                        <td rowspan="2" style="text-align: left;" >&nbsp;</td>
                        <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.solicitado")%></td>
                        <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.concedido")%></td>
                        <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.justificadoObjetivos")%></td>
                        <td colspan="3" class="sub3titulo"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.subvencionable")%></td>
                    </tr>
                    <tr class="negrita">
                        <!--td style="text-align: left;"></td-->
                        <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.numero")%></td>
                        <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.solicitud.importe")%></td>               
                        <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.numero")%></td>
                        <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.solicitud.importe")%></td>                
                        <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.numero")%></td>
                        <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.importe")%></td>              
                        <td class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.numero")%></td>
                        <td colspan="2" class="xCabecera"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.importe")%></td>

                    </tr>
                   <tr>
                        <td colspan="4" style="border-right: 1px solid #1766A7;"></td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3">
                            <div class="separadorTabla"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.nProspectores")%></td>
                        <td><input id="prospectoresSolicNumResumen" name="prospectoresSolicNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="4"></td>
                        <td  colspan="2" style="border-right: 1px solid #1766A7;">
                            <input id="prospectoresSolicSolResumen" name="prospectoresSolicSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11">
                        </td>
                        <td><input id="prospectoresAnexCargaNumResumen" name="prospectoresAnexCargaNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="prospectoresAnexCargaSolResumen" name="prospectoresAnexCargaSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><input id="prospectoresAnexNumResumen" name="prospectoresAnexNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="prospectoresAnexSolResumen" name="prospectoresAnexSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><input id="prospectoresPagadoNumResumen" name="prospectoresPagadoNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2"><input id="prospectoresPagadoImpResumen" name="prospectoresPagadoImpResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><a href="#" onclick="cargarDetalleProspectoresResumen()"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle")%></a></td>
                    </tr>
                    <tr>
                        <td style=" text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.nPreparadores")%></td>
                        <td><input id="preparadoresSolicNumResumen" name="preparadoresSolicNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="4"></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;">
                            <input id="preparadoresSolicSolResumen" name="preparadoresSolicSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11">
                        </td>
                        <td><input id="preparadoresAnexCargaNumResumen" name="preparadoresAnexCargaNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="preparadoresAnexCargaSolResumen" name="preparadoresAnexCargaSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><input id="preparadoresAnexNumResumen" name="preparadoresAnexNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="preparadoresAnexSolResumen" name="preparadoresAnexSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><input id="preparadoresPagadoNumResumen" name="preparadoresPagadoNumResumen" type="text" size="3" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td colspan="2"><input id="preparadoresPagadoImpResumen" name="preparadoresPagadoImpResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td><a href="#" onclick="cargarDetallePreparadoresResumen();"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.detalle")%></a></td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.gastosGeneralesAdm")%></td>
                        <td></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;">
                            <input id="gastosGeneralesSolicResumen" name="gastosGeneralesSolicResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                        <td></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="gastosGeneralesAnexCargaResumen" name="gastosGeneralesAnexCargaResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="gastosGeneralesAnexResumen" name="gastosGeneralesAnexResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td></td>
                        <td colspan="2"><input id="gastosGeneralesPagadoResumen" name="gastosGeneralesPagadoResumen" type="text" size="10" class="inputTexto textoNumerico" onkeyup="calcularTotSubvResumen();FormatNumber(this.value, 8, 2, this.id);"
                                                                                        onblur="calcularTotSubvResumen();ajustarDecimalesImportesResumen(false);"></td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.otrasSubvenciones")%></td>
                        <td colspan="3" style="text-align: left; border-right: 1px solid #1766A7;">
                            <table style="width: 100%; font-size: 12px;text-align: center;">
                                <tr>
                                    <input type="radio" name="radioOtrasAyudasSResumen" id="radioOtrasAyudasSResumen" class="readOnly" style="background-color: white;" readOnly="readonly" value="S"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.si").toUpperCase()%>
                                    <input type="radio" name="radioOtrasAyudasNResumen" id="radioOtrasAyudasNResumen" class="readOnly" style="background-color: white;" readOnly="readonly" value="N"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.no").toUpperCase()%>
                                </tr>
                            </table>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                    </tr>
                    <tr id="filaOtrosOrganismos1Resumen" class="negrita">
                        <td style=" text-align: left;"></td>
                        <td></td>

                        <td colspan="2" style="border-right: 1px solid #1766A7;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.general.importe").toUpperCase()%></td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;"></td>
                    </tr>
                    <tr id="filaOtrosOrganismos2Resumen">
                        <td style="text-align: left; padding-left: 45px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.cantOrgPublicos")%></td>
                        <td ></td>                
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPublicosResumen" name="impOrgPublicosResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                        <td>&nbsp;</td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPublicosConcResumen" name="impOrgPublicosConcResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                        <td>&nbsp;</td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPublicosJusResumen" name="impOrgPublicosJusResumen" type="text" size="10" class="inputTexto textoNumerico" maxlength="11"
                                                                                        onkeyup="calcularTotSubvResumen();FormatNumber(this.value, 8, 2, this.id);"
                                                                                        onblur="calcularTotSubvResumen();ajustarDecimalesImportesResumen(false);"></td>
                        <td>&nbsp;</td>
                        <td colspan="2"><input id="impOrgPublicosPagResumen" name="impOrgPublicosPagResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                    </tr>
                    <tr id="filaOtrosOrganismos3Resumen">
                        <td style="text-align: left; padding-left: 45px;"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.cantOrgPrivados")%></td>
                        <td ></td>                
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPrivadosResumen" name="impOrgPrivadosResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                        <td>&nbsp;</td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPrivadosConcResumen" name="impOrgPrivadosConcResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                        <td>&nbsp;</td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="impOrgPrivadosJusResumen" name="impOrgPrivadosJusResumen" type="text" size="10" class="inputTexto textoNumerico" maxlength="11"
                                                                                        onkeyup="calcularTotSubvResumen();FormatNumber(this.value, 8, 2, this.id);"
                                                                                        onblur="calcularTotSubvResumen();ajustarDecimalesImportesResumen(false);"/></td>
                        <td>&nbsp;</td>
                        <td colspan="2"><input id="impOrgPrivadosPagResumen" name="impOrgPrivadosPagResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly" maxlength="11"></td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                        <td colspan="3" style="border-right: 1px solid #1766A7;">
                            <div class="separadorTabla"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style=" text-align: left;" class="negrita"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.resumen.totSubSolicitada")%></td>
                        <td ></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="totSolResumen" name="totSolResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" maxlength="11" readOnly="readonly"></td>
                        <td></td>
                        <td colspan="2"  style="border-right: 1px solid #1766A7;"><input id="totConcResumen" name="totConcResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td ></td>
                        <td colspan="2" style="border-right: 1px solid #1766A7;"><input id="totJusResumen" name="totJusResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                        <td ></td>
                        <td colspan="2"><input id="totPagadoResumen" name="totPagadoResumen" type="text" size="10" class="inputTexto textoNumerico readOnly" readOnly="readonly"></td>
                    </tr>
                    <tr>
                        <td colspan="10">
                            <div class="separadorTabla"></div>
                        </td>
                    </tr>

                </table>
            </div>                                        
            <div class="botonera" style="height: 50px; padding-top: 20px;">
                <input type="button" id="btnGuardarResumen" name="btnGuardarResumen" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.guardar")%>" onclick="guardarDatosResumen();">
                <input type="button" id="btnActualizarResumen" name="btnActualizarResumen" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.solicitud.resumen.actualizarResumen")%>" onclick="actualizarDatosResumen();">
            </div> 
        </div>
    </div>
</body>

<script type="text/javascript">

            //document.getElementById('radioOtrasAyudasSResumen').checked = true;
            //document.getElementById('filaOtrosOrganismos1Resumen').style.display = 'table-row';
            //document.getElementById('filaOtrosOrganismos2Resumen').style.display = 'table-row';
            //document.getElementById('filaOtrosOrganismos3Resumen').style.display = 'table-row';
            
            <%
                if(datosResumen != null)
                {
            %>
                    //OTRAS SUBVENCIONES
                    <%
                        if(datosResumen.getOtrasSubv() != null && datosResumen.getOtrasSubv() == true)
                        {
                    %>
                            document.getElementById('radioOtrasAyudasSResumen').checked = true;
                            document.getElementById('impOrgPublicosResumen').style.display = 'table-row';
                            document.getElementById('impOrgPrivadosResumen').style.display = 'table-row';
                            document.getElementById('impOrgPublicosConcResumen').style.display = 'table-row';
                            document.getElementById('impOrgPrivadosConcResumen').style.display = 'table-row';
                    <%
                        }
                        else
                        {
                    %>
                            document.getElementById('radioOtrasAyudasNResumen').checked = true;
                            document.getElementById('impOrgPublicosResumen').style.display = 'none';
                            document.getElementById('impOrgPrivadosResumen').style.display = 'none';
                            document.getElementById('impOrgPublicosConcResumen').style.display = 'none';
                            document.getElementById('impOrgPrivadosConcResumen').style.display = 'none';
                    <%
                        }
                    %>
                    
                    //COLUMNA SOLICITADO
                    document.getElementById('prospectoresSolicNumResumen').value = '<%=datosResumen.getNumeroProspectores_solic() != null ? datosResumen.getNumeroProspectores_solic().intValue() : ""%>';
                    document.getElementById('prospectoresSolicSolResumen').value = '<%=datosResumen.getImporteProspectores_solic() != null ? formateador.format(datosResumen.getImporteProspectores_solic()) : ""%>';
                    document.getElementById('preparadoresSolicNumResumen').value = '<%=datosResumen.getNumeroPreparadores_solic() != null ? datosResumen.getNumeroPreparadores_solic().intValue() : ""%>';
                    document.getElementById('preparadoresSolicSolResumen').value = '<%=datosResumen.getImportePreparadores_solic() != null ? formateador.format(datosResumen.getImportePreparadores_solic()) : ""%>';
                    document.getElementById('gastosGeneralesSolicResumen').value = '<%=datosResumen.getGastosGenerales_solic() != null ? formateador.format(datosResumen.getGastosGenerales_solic()) : ""%>';
                    document.getElementById('impOrgPublicosResumen').value = '<%=datosResumen.getSubvOrgPublicos_solic() != null ? formateador.format(datosResumen.getSubvOrgPublicos_solic()) : ""%>';
                    document.getElementById('impOrgPrivadosResumen').value = '<%=datosResumen.getSubvOrgPrivados_solic() != null ? formateador.format(datosResumen.getSubvOrgPrivados_solic()) : ""%>';
                    document.getElementById('totSolResumen').value = '<%=datosResumen.getTotSubv_solic() != null ? formateador.format(datosResumen.getTotSubv_solic().floatValue()) : ""%>';
                    
                    //COLUMNA CONCEDIDO
                    document.getElementById('prospectoresAnexCargaNumResumen').value = '<%=datosResumen.getNumeroProspectores_conc() != null ? datosResumen.getNumeroProspectores_conc().intValue() : ""%>';
                    document.getElementById('prospectoresAnexCargaSolResumen').value = '<%=datosResumen.getImporteProspectores_conc() != null ? formateador.format(datosResumen.getImporteProspectores_conc()) : ""%>';
                    document.getElementById('preparadoresAnexCargaNumResumen').value = '<%=datosResumen.getNumeroPreparadores_conc() != null ? datosResumen.getNumeroPreparadores_conc().intValue() : ""%>';
                    document.getElementById('preparadoresAnexCargaSolResumen').value = '<%=datosResumen.getImportePreparadores_conc() != null ? formateador.format(datosResumen.getImportePreparadores_conc()) : ""%>';
                    document.getElementById('gastosGeneralesAnexCargaResumen').value = '<%=datosResumen.getGastosGenerales_conc() != null ? formateador.format(datosResumen.getGastosGenerales_conc()) : ""%>';
                    document.getElementById('impOrgPublicosConcResumen').value = '<%=datosResumen.getSubvOrgPublicos_conc() != null ? formateador.format(datosResumen.getSubvOrgPublicos_conc()) : ""%>';
                    document.getElementById('impOrgPrivadosConcResumen').value = '<%=datosResumen.getSubvOrgPrivados_conc() != null ? formateador.format(datosResumen.getSubvOrgPrivados_conc()) : ""%>';
                    document.getElementById('totConcResumen').value = '<%=datosResumen.getTotSubv_conc() != null ? formateador.format(datosResumen.getTotSubv_conc().floatValue()) : ""%>';
                    
                    //COLUMNA JUSTIFICADO
                    document.getElementById('prospectoresAnexNumResumen').value = '<%=datosResumen.getNumeroProspectores_justif() != null ? datosResumen.getNumeroProspectores_justif().intValue() : ""%>';
                    document.getElementById('prospectoresAnexSolResumen').value = '<%=datosResumen.getImporteProspectores_justif() != null ? formateador.format(datosResumen.getImporteProspectores_justif()) : ""%>';
                    document.getElementById('preparadoresAnexNumResumen').value = '<%=datosResumen.getNumeroPreparadores_justif() != null ? datosResumen.getNumeroPreparadores_justif().intValue() : ""%>';
                    document.getElementById('preparadoresAnexSolResumen').value = '<%=datosResumen.getImportePreparadores_justif() != null ? formateador.format(datosResumen.getImportePreparadores_justif()) : ""%>';
                    document.getElementById('gastosGeneralesAnexResumen').value = '<%=datosResumen.getGastosGenerales_justif() != null ? formateador.format(datosResumen.getGastosGenerales_justif()) : ""%>';
                    document.getElementById('impOrgPublicosJusResumen').value = '<%=datosResumen.getSubvOrgPublicos_justif() != null ? formateador.format(datosResumen.getSubvOrgPublicos_justif()) : ""%>';
                    document.getElementById('impOrgPrivadosJusResumen').value = '<%=datosResumen.getSubvOrgPrivados_justif() != null ? formateador.format(datosResumen.getSubvOrgPrivados_justif()) : ""%>';
                    document.getElementById('totJusResumen').value = '<%=datosResumen.getTotSubv_justif() != null ? formateador.format(datosResumen.getTotSubv_justif()) : ""%>';
                    
                    //COLUMNA PAGADO
                    document.getElementById('prospectoresPagadoNumResumen').value = '<%=datosResumen.getNumeroProspectores_pag() != null ? datosResumen.getNumeroProspectores_pag().intValue() : ""%>';
                    document.getElementById('prospectoresPagadoImpResumen').value = '<%=datosResumen.getImporteProspectores_pag() != null ? formateador.format(datosResumen.getImporteProspectores_pag()) : ""%>';
                    document.getElementById('preparadoresPagadoNumResumen').value = '<%=datosResumen.getNumeroPreparadores_pag() != null ? datosResumen.getNumeroPreparadores_pag().intValue() : ""%>';
                    document.getElementById('preparadoresPagadoImpResumen').value = '<%=datosResumen.getImportePreparadores_pag() != null ? formateador.format(datosResumen.getImportePreparadores_pag()) : ""%>';
                    document.getElementById('gastosGeneralesPagadoResumen').value = '<%=datosResumen.getGastosGenerales_pag() != null ? formateador.format(datosResumen.getGastosGenerales_pag()) : ""%>';
                    document.getElementById('impOrgPublicosPagResumen').value = '<%=datosResumen.getSubvOrgPublicos_pag() != null ? formateador.format(datosResumen.getSubvOrgPublicos_pag()) : ""%>';
                    document.getElementById('impOrgPrivadosPagResumen').value = '<%=datosResumen.getSubvOrgPrivados_pag() != null ? formateador.format(datosResumen.getSubvOrgPrivados_pag()) : ""%>';
                    document.getElementById('totPagadoResumen').value = '<%=datosResumen.getTotSubv_pag() != null ? formateador.format(datosResumen.getTotSubv_pag()) : ""%>';
                   
            <%
                }
            %>
        
        calcularTotSubvResumen();
        
        ajustarDecimalesImportesResumen(false);
        
        
        FormatNumber(document.getElementById('totJusResumen').value, 8, 2, document.getElementById('totJusResumen').id);
        
        activarModoConsulta();
</script>
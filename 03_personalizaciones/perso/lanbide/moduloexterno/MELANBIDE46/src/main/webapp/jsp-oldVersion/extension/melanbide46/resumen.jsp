<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaResumenVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
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
//prueba
    MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide46/melanbide46.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">  
        function recargarTablaResumenCme(result){
        var fila;
        tabResumenCpe = new Array();
        listaResumenCpeTabla = new Array();
        listaResumenCpeTabla_titulos = new Array();
        listaResumenCpeTabla_estilos = new Array();
        fila = result[1];
        
        //recargarCalculosCpe(fila);
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            listaResumenCpe[i-1] = fila;
            listaResumenCpeTabla[i-1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10]
            , fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20]
            , fila[21], fila[22], fila[23], fila[24], fila[25], fila[26],  fila[27],  fila[28], fila[29], fila[30], fila[31]
        , fila[32], fila[33]/*, fila[34], fila[35],  fila[36], fila[37], fila[38]*/];
            listaResumenCpeTabla_titulos[i-1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10]
            , fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20]
            , fila[21], fila[22], fila[23], fila[24], fila[25], fila[26],  fila[27],  fila[28], fila[29], fila[30], fila[31]
        , fila[32], fila[33]/*, fila[34], fila[35],  fila[36], fila[37], fila[38]*/];
        }

        tabResumenCpe = new FixedColumnTable(document.getElementById('resumenesCpe'), 850, 876, 'resumenesCpe');
        tabResumenCpe.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col1")%>");
    tabResumenCpe.addColumna('100','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col2")%>");    
    tabResumenCpe.addColumna('120','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col3")%>");   
    //tabResumenCpe.addColumna('230','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col4")%>");   
    tabResumenCpe.addColumna('230','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col5")%>");   
    tabResumenCpe.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col6")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col7")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col8")%>");  
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col35")%>");  
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col36")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col9")%>");       
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col10")%>");     
    tabResumenCpe.addColumna('160','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col11")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col12")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col13")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col14")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col15")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col16")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col17")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col18")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col19")%>");    
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col20")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col21")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col22")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col23")%>");    
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col24")%>");    
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col25")%>");     
    tabResumenCpe.addColumna('200','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col26")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col27")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col28")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col29")%>");     
    //tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col30")%>");   
    tabResumenCpe.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col31")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col32")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col33")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col34")%>");   
    /*tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col37")%>");   
    tabResumenCpe.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col38")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col39")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col40")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col41")%>")*/;    

        tabResumenCpe.numColumnasFijas = 1;

        tabResumenCpe.displayCabecera=true;
    
        for(var cont = 0; cont < listaResumenCpeTabla.length; cont++){
            tabResumenCpe.addFilaConFormato(listaResumenCpeTabla[cont], listaResumenCpeTabla_titulos[cont], listaResumenCpeTabla_estilos[cont])
        }

        tabResumenCpe.height = '146';
    
    tabResumenCpe.altoCabecera = 50;
    
    tabResumenCpe.scrollWidth = 5372;
    
    tabResumenCpe.displayTabla();
    
    tabResumenCpe.pack();
    }
    
    function actualizarPestanaResumen(){
        try{
            var nodos = this.getListaResumenCme();
            var result = this.extraerListadoResumenCme(nodos);
            this.recargarTablaResumenCme(result);
        }catch(err){
            
        }
    }
    
    function getListaResumenCme(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=getListaResumenPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
    function extraerListadoResumenCme(nodos){
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
                
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){        
                
                        if(hijosFila[cont].nodeName=="NOM_APE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DNI"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PAIS_SOLICITADO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RANGO_EDAD_CONCE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RANGO_EDAD_CONTRA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MESES_CONTRATO"){
                            nodoCampo = hijosFila[cont]; //alert("meses"+nodoCampo.childNodes[0].nodeValue);
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMP_MAX_SUBV"){
                            nodoCampo = hijosFila[cont]; //alert("meses"+nodoCampo.childNodes[0].nodeValue);
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MINORACION"){
                            nodoCampo = hijosFila[cont]; //alert("meses"+nodoCampo.childNodes[0].nodeValue);
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPROTE-MINO"){
                            nodoCampo = hijosFila[cont]; //alert("meses"+nodoCampo.childNodes[0].nodeValue);
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FEC_INI"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FEC_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TOTAL_DIAS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[11] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="BC_PERIODO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[12] = '-';
                            }
                        }         
                        else if(hijosFila[cont].nodeName=="COEFICIENTE_TGSS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[13] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[13] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SS_EMPRESA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[14] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[14] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="BC_AT"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[15] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[15] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COEFICIENTE_FOGASA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[16] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[16] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COEFICIENTE_AT"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[17] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[17] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FOGASA_AT_EMPRESA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[18] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[18] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PORC_EPSV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[19] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[19] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="APORT_EPSV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[20] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[20] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TOTAL_EMPRESA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[21] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[21] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SALARIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[22] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[22] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SUBV_MINORADA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[23] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[23] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MAXIMO_PERIODO_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[24] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[24] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="MINO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[25] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[25] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SUBVFINAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[26] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[26] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DIETAS_PERIODO_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[27] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[27] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DIETAS_CONCEDIDAS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[28] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[28] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MAXIMO_DIETAS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[29] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[29] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISADO_ABONADO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[30] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[30] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISADO_CONCEDIDO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[31] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[31] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISADO_SUBV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[32] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[32] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTE_SUBVENCIONABLE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[33] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[33] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RESOLINICIAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[34] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[34] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PRIMERPAGO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[35] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[35] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SEGUNDOPAGO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[36] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[36] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="REINTEGRO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[37] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[37] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="D"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[38] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[38] = '-';
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
    <div id="barraProgresoPuestosResumenCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoResumenCpe">
                                                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoResumenCpe">
                                                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
        <div id="resumenesCpe" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <!--<input type="button" id="btnNuevaOfertaCpe" name="btnNuevaOfertaCpe" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaOfertaCpe();">-->
            <!--<input type="button" id="btnModificarOfertaCpe" name="btnModificarOfertaCpe" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarOfertaCpe();">-->
            <!--<input type="button" id="btnEliminaOfertaCpe" name="btnEliminaOfertaCpe" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarOfertaCpe();">-->
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabResumenCpe;
    var listaResumenCpe = new Array();
    var listaResumenCpeTabla = new Array();
    var listaResumenCpeTabla_titulos = new Array();
    var listaResumenCpeTabla_estilos = new Array();
    
    tabResumenCpe = new FixedColumnTable(document.getElementById('resumenesCpe'), 850, 876, 'resumenesCpe');
    tabResumenCpe.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col1")%>");
    tabResumenCpe.addColumna('100','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col2")%>");    
    tabResumenCpe.addColumna('120','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col3")%>");   
    //tabResumenCpe.addColumna('230','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col4")%>");   
    tabResumenCpe.addColumna('230','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col5")%>");   
    tabResumenCpe.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col6")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col7")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col8")%>");  
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col35")%>");  
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col36")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col9")%>");       
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col10")%>");     
    tabResumenCpe.addColumna('160','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col11")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col12")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col13")%>");   
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col14")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col15")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col16")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col17")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col18")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col19")%>");    
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col20")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col21")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col22")%>");     
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col23")%>");    
    tabResumenCpe.addColumna('130','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col24")%>");    
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col25")%>");     
    tabResumenCpe.addColumna('200','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col26")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col27")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col28")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col29")%>");     
    //tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col30")%>");   
    tabResumenCpe.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col31")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col32")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col33")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col34")%>");   
    /*tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col37")%>");   
    tabResumenCpe.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col38")%>");     
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col39")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col40")%>");
    tabResumenCpe.addColumna('140','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.resumen.col41")%>");  */        

    tabResumenCpe.numColumnasFijas = 1;

    tabResumenCpe.displayCabecera=true;
    
    
    <%  		
        FilaResumenVO filaResumen = null;
        List<FilaResumenVO> listaResumen = (List<FilaResumenVO>)request.getAttribute("resumen");													
        if (listaResumen != null && listaResumen.size() >0){
            for(int i = 0;i < listaResumen.size();i++)
            {
                filaResumen = listaResumen.get(i);
    %>
        listaResumenCpe[<%=i%>] = ['<%=filaResumen.getNomApe()%>', '<%=filaResumen.getDni()%>', '<%=filaResumen.getPaisSolicitado()%>', 
        '<%=filaResumen.getRangoEdadConce()%>', '<%=filaResumen.getRangoEdadContra()%>',  
        '<%=filaResumen.getMesesContrato()%>', '<%=filaResumen.getImporteConcedido()%>', '<%=filaResumen.getMinoracion()%>', 
        '<%=filaResumen.getImporteConceMinora()%>', '<%=filaResumen.getFecIni()%>', '<%=filaResumen.getFecFin()%>', 
        '<%=filaResumen.getTotalDias()%>', '<%=filaResumen.getBcPeriodo()%>','<%=filaResumen.getCoeficienteTGSS()%>', 
        '<%=filaResumen.getSsEmpresa()%>', '<%=filaResumen.getBcAT()%>', '<%=filaResumen.getCoeficienteFogasa()%>',
        '<%=filaResumen.getCoefiecienteAT()%>', '<%=filaResumen.getFogasaATEmpresa()%>', '<%=filaResumen.getPorcEPSV()%>', 
        '<%=filaResumen.getAportEPSV()%>', '<%=filaResumen.getTotalEmpresa()%>', '<%=filaResumen.getCostePeriodoSubv()%>',
        '<%=filaResumen.getImporteMaxSubv()%>', '<%=filaResumen.getMaximoPeriodoSubv()%>','<%=filaResumen.getContratoBonif()%>', 
        '<%=filaResumen.getSubvFinal()%>', '<%=filaResumen.getDietasPeriodoSubv()%>', '<%=filaResumen.getDietasConcedidas()%>'
        , '<%=filaResumen.getMaximoDietas()%>', '<%=filaResumen.getVisadoAbonado()%>', '<%=filaResumen.getVisadoConcedido()%>', 
        '<%=filaResumen.getVisadoSubv()%>', '<%=filaResumen.getCosteSubvecionable()%>'/*, '<%=filaResumen.getResolInical()%>'
        , '<%=filaResumen.getPrimerPago()%>', '<%=filaResumen.getSegundoPago()%>', '<%=filaResumen.getReintegro()%>'
        , '<%=filaResumen.getD()%>'*/];
        
        listaResumenCpeTabla[<%=i%>] = ['<%=filaResumen.getNomApe()%>', '<%=filaResumen.getDni()%>', '<%=filaResumen.getPaisSolicitado()%>', 
        '<%=filaResumen.getRangoEdadConce()%>', '<%=filaResumen.getRangoEdadContra()%>',  
        '<%=filaResumen.getMesesContrato()%>', '<%=filaResumen.getImporteConcedido()%>', '<%=filaResumen.getMinoracion()%>', 
        '<%=filaResumen.getImporteConceMinora()%>', '<%=filaResumen.getFecIni()%>', '<%=filaResumen.getFecFin()%>', 
        '<%=filaResumen.getTotalDias()%>', '<%=filaResumen.getBcPeriodo()%>','<%=filaResumen.getCoeficienteTGSS()%>', 
        '<%=filaResumen.getSsEmpresa()%>', '<%=filaResumen.getBcAT()%>', '<%=filaResumen.getCoeficienteFogasa()%>',
        '<%=filaResumen.getCoefiecienteAT()%>', '<%=filaResumen.getFogasaATEmpresa()%>', '<%=filaResumen.getPorcEPSV()%>', 
        '<%=filaResumen.getAportEPSV()%>', '<%=filaResumen.getTotalEmpresa()%>', '<%=filaResumen.getCostePeriodoSubv()%>',
        '<%=filaResumen.getImporteMaxSubv()%>', '<%=filaResumen.getMaximoPeriodoSubv()%>','<%=filaResumen.getContratoBonif()%>', 
        '<%=filaResumen.getSubvFinal()%>', '<%=filaResumen.getDietasPeriodoSubv()%>', '<%=filaResumen.getDietasConcedidas()%>'
        , '<%=filaResumen.getMaximoDietas()%>', '<%=filaResumen.getVisadoAbonado()%>', '<%=filaResumen.getVisadoConcedido()%>', 
        '<%=filaResumen.getVisadoSubv()%>', '<%=filaResumen.getCosteSubvecionable()%>'/*, '<%=filaResumen.getResolInical()%>'
        , '<%=filaResumen.getPrimerPago()%>', '<%=filaResumen.getSegundoPago()%>', '<%=filaResumen.getReintegro()%>'
        , '<%=filaResumen.getD()%>'*/];
        listaResumenCpeTabla_titulos[<%=i%>] = ['<%=filaResumen.getNomApe()%>', '<%=filaResumen.getDni()%>', '<%=filaResumen.getPaisSolicitado()%>', 
        '<%=filaResumen.getRangoEdadConce()%>', '<%=filaResumen.getRangoEdadContra()%>',  
        '<%=filaResumen.getMesesContrato()%>', '<%=filaResumen.getImporteConcedido()%>', '<%=filaResumen.getMinoracion()%>', 
        '<%=filaResumen.getImporteConceMinora()%>', '<%=filaResumen.getFecIni()%>', '<%=filaResumen.getFecFin()%>', 
        '<%=filaResumen.getTotalDias()%>', '<%=filaResumen.getBcPeriodo()%>','<%=filaResumen.getCoeficienteTGSS()%>', 
        '<%=filaResumen.getSsEmpresa()%>', '<%=filaResumen.getBcAT()%>', '<%=filaResumen.getCoeficienteFogasa()%>',
        '<%=filaResumen.getCoefiecienteAT()%>', '<%=filaResumen.getFogasaATEmpresa()%>', '<%=filaResumen.getPorcEPSV()%>', 
        '<%=filaResumen.getAportEPSV()%>', '<%=filaResumen.getTotalEmpresa()%>', '<%=filaResumen.getCostePeriodoSubv()%>',
        '<%=filaResumen.getImporteMaxSubv()%>', '<%=filaResumen.getMaximoPeriodoSubv()%>','<%=filaResumen.getContratoBonif()%>', 
        '<%=filaResumen.getSubvFinal()%>', '<%=filaResumen.getDietasPeriodoSubv()%>', '<%=filaResumen.getDietasConcedidas()%>'
        , '<%=filaResumen.getMaximoDietas()%>', '<%=filaResumen.getVisadoAbonado()%>', '<%=filaResumen.getVisadoConcedido()%>', 
        '<%=filaResumen.getVisadoSubv()%>', '<%=filaResumen.getCosteSubvecionable()%>'/*, '<%=filaResumen.getResolInical()%>'
        , '<%=filaResumen.getPrimerPago()%>', '<%=filaResumen.getSegundoPago()%>', '<%=filaResumen.getReintegro()%>'
        , '<%=filaResumen.getD()%>'*/];
        
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaResumenCpeTabla.length; cont++){
        tabResumenCpe.addFilaConFormato(listaResumenCpeTabla[cont], listaResumenCpeTabla_titulos[cont], listaResumenCpeTabla_estilos[cont])
    }
    
    tabResumenCpe.height = '146';
    
    tabResumenCpe.altoCabecera = 50;
    
    tabResumenCpe.scrollWidth = 5372;
    
    tabResumenCpe.displayTabla();
    
    tabResumenCpe.pack();
</script>
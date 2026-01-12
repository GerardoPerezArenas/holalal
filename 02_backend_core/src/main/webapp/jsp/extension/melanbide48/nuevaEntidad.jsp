<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadEnAgrupacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

       <%
            int idiomaUsuario = 1;
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
            MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            
            ColecEntidadEnAgrupacionVO entidadModif = (ColecEntidadEnAgrupacionVO)request.getAttribute("entidadModif");
            String descTit1 = (String)request.getAttribute("descTit1");
            String descTit2 = (String)request.getAttribute("descTit2");
            String descTit3 = (String)request.getAttribute("descTit3");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    
    
            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.titulo.consultaEntidad");
            }
            else
            {
                if(entidadModif != null)
                {
                    tituloPagina = meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.titulo.modifEntidad");
                }
                else
                {
                    tituloPagina = meLanbide48I18n.getMensaje(idiomaUsuario, "label.entidad.titulo.nuevaEntidad");
                }
            }
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide48/melanbide48.css"/>
                <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
    
            function getXMLHttpRequest(){
                var aVersions = [ "MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                if (window.XMLHttpRequest){
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                }else if (window.ActiveXObject){
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                    }
                }else{
                    return null;
                }
            }
            
            function inicio(){
                <%
                if(entidadModif != null)
                {
                    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide48.FORMATO_FECHA);
                %>
                        //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                    nuevo = false;
                    
                    //Datos entidad
                    document.getElementById('codEntidadPadre').value = '<%=entidadModif.getCodEntidadPadreAgrup() != null ? entidadModif.getCodEntidadPadreAgrup() : "" %>';
                    document.getElementById('codEntidad').value = '<%=entidadModif.getCodEntidad() != null ? entidadModif.getCodEntidad() : "" %>';
                    document.getElementById('cif').value = '<%=entidadModif.getCif() != null ? entidadModif.getCif(): "" %>';
                    document.getElementById('nombre').value = '<%=entidadModif.getNombre() != null ? entidadModif.getNombre(): "" %>';
//                    var porcentajeRea = '< %=entidadModif.getPorcentaCompromisoRealizacion() != null ? entidadModif.getPorcentaCompromisoRealizacion(): "" %>';
//                    porcentajeRea=reemplazarTextoColec(porcentajeRea,'.',',');
//                    document.getElementById('porcentajeComproRealiza').value = porcentajeRea;
                    
                    <%
                    if(entidadModif.getCentroEspEmpTH() != null && entidadModif.getCentroEspEmpTH().equals(1))
                    {
                    %>
                        document.getElementById('centroEspecialS').checked = 'true';                                
                    <%
                    }
                    else if(entidadModif.getCentroEspEmpTH() != null && entidadModif.getCentroEspEmpTH().equals(0))
                    {
                    %>
                        document.getElementById('centroEspecialN').checked = 'true'; 
                    <%
                    }
                    %>
                    <%
                    if(entidadModif.getParticipanteMayorCentEcpEmpTH() != null && entidadModif.getParticipanteMayorCentEcpEmpTH().equals(1))
                    {
                    %>
                        document.getElementById('centroEspecialMayoritarioS').checked = 'true';                                
                    <%
                    }
                    else if(entidadModif.getParticipanteMayorCentEcpEmpTH() != null && entidadModif.getParticipanteMayorCentEcpEmpTH().equals(0))
                    {
                    %>
                        document.getElementById('centroEspecialMayoritarioN').checked = 'true'; 
                    <%
                    }
                    %>

                    <%
                    if(entidadModif.getEmpresaInsercionTH() != null && entidadModif.getEmpresaInsercionTH().equals(1))
                    {
                    %>
                        document.getElementById('centroInsercionS').checked = 'true';                                
                    <%
                    }
                    else if(entidadModif.getEmpresaInsercionTH() != null && entidadModif.getEmpresaInsercionTH().equals(0))
                    {
                    %>
                        document.getElementById('centroInsercionN').checked = 'true'; 
                    <%
                    }
                    %>
                            
                    <%
                    if(entidadModif.getPromotorEmpInsercionTH() != null && entidadModif.getPromotorEmpInsercionTH().equals(1))
                    {
                    %>
                        document.getElementById('centroInsercionPromotorS').checked = 'true';                                
                    <%
                    }
                    else if(entidadModif.getPromotorEmpInsercionTH() != null && entidadModif.getPromotorEmpInsercionTH().equals(0))
                    {
                    %>
                        document.getElementById('centroInsercionPromotorN').checked = 'true'; 
                    <%
                    }
                    %>
                    <%
                    if(entidadModif.getPlanIgualdad() != null && entidadModif.getPlanIgualdad().equals(1))
                    {
                    %>
                        if(document.getElementById('planIgualdadS')!=null && document.getElementById('planIgualdadS')!= undefined)
                            document.getElementById('planIgualdadS').checked = 'true';
                    <%
                    }
                    else if(entidadModif.getPlanIgualdad() != null && entidadModif.getPlanIgualdad().equals(0))
                    {
                    %>
                        if(document.getElementById('planIgualdadN')!=null && document.getElementById('planIgualdadN')!= undefined)
                            document.getElementById('planIgualdadN').checked = 'true';
                    <%
                    }
                    %>
                    <%
                    if(entidadModif.getCertificadoCalidad() != null && entidadModif.getCertificadoCalidad().equals(1))
                    {
                    %>
                        if(document.getElementById('certificadoCalidadS')!=null && document.getElementById('certificadoCalidadS')!= undefined){
                            document.getElementById('certificadoCalidadS').checked = 'true';
                        }
                    <%
                    }
                    else if(entidadModif.getCertificadoCalidad() != null && entidadModif.getCertificadoCalidad().equals(0))
                    {
                    %>
                        if(document.getElementById('certificadoCalidadN')!=null && document.getElementById('certificadoCalidadN')!= undefined){
                            document.getElementById('certificadoCalidadN').checked = 'true';
                        }
                    <%
                    }
                    %>
                <%
                }
                else
                {
                %>
                    //document.getElementById('codResultadoResumen').value = '< %=ConstantesMeLanbide48.CODIGO_RESULTADO_NO_EVALUADO%>';
                <%
                }

                if(consulta == true)
                {
                %>
                    //Deshabilito todos los campos
                    
                    //Datos entidad
                    document.getElementById('codEntidadPadre').readOnly = true;
                    document.getElementById('codEntidadPadre').className = 'inputTexto readOnly';
                    document.getElementById('codEntidad').readOnly = true;
                    document.getElementById('codEntidad').className = 'inputTexto readOnly';
                    document.getElementById('cif').readOnly = true;
                    document.getElementById('cif').className = 'inputTexto readOnly';
                    document.getElementById('nombre').readOnly = true;
                    document.getElementById('nombre').className = 'inputTexto readOnly';
//                    document.getElementById('porcentajeComproRealiza').readOnly = true;
//                    document.getElementById('porcentajeComproRealiza').className = 'inputTexto readOnly';
                    document.getElementById('centroEspecialS').readOnly = true;
                    document.getElementById('centroEspecialN').readOnly = true;
                    document.getElementById('centroEspecialMayoritarioS').readOnly = true;
                    document.getElementById('centroEspecialMayoritarioSN').readOnly = true;
                    document.getElementById('centroInsercionS').readOnly = true;
                    document.getElementById('centroInsercionN').readOnly = true;
                    document.getElementById('centroInsercionPromotorS').readOnly = true;
                    document.getElementById('centroInsercionPromotorN').readOnly = true;
                    if(document.getElementById('planIgualdadS')!=null && document.getElementById('planIgualdadS')!= undefined)
                        document.getElementById('planIgualdadS').readOnly = true;
                    if(document.getElementById('planIgualdadN')!=null && document.getElementById('planIgualdadN')!= undefined)
                        document.getElementById('planIgualdadN').readOnly = true;
                    if(document.getElementById('certificadoCalidadS')!=null && document.getElementById('certificadoCalidadS')!= undefined)
                        document.getElementById('certificadoCalidadS').readOnly = true;
                    if(document.getElementById('certificadoCalidadN')!=null && document.getElementById('certificadoCalidadN')!= undefined)
                        document.getElementById('certificadoCalidadN').readOnly = true;
                <%
                }
                %>
                   
                resizeForFF();
            }
        
            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    if(document.getElementById('cuerpoNuevaEntidad')!=null)
                        document.getElementById('cuerpoNuevaEntidad').style.width = '99%';
                    if(document.getElementById('divCuerpo')!=null)
                        document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosEntidad').style.width = '98%';
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                 }else{
                     window.close(); 
                 } 
            }
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }
            
                function guardar(){
                if(validarDatos()){
                    //document.getElementById('msgGuardandoDatos').style.display="inline";
                    //barraProgresoColec('on', 'barraProgresoNuevaEntidad');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    //var porcentajeComp = reemplazarTextoColec(document.getElementById('porcentajeComproRealiza').value,',','.'); 
                    parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarEntidadAsociada&tipo=0&numero=<%=numExpediente%>'
                        +'&codEntidadPadre=<%=entidadModif != null && entidadModif.getCodEntidadPadreAgrup()!=null ? entidadModif.getCodEntidadPadreAgrup() : ""%>'
                        +'&codEntidad=<%=entidadModif != null && entidadModif.getCodEntidad() != null ? entidadModif.getCodEntidad() : ""%>'
                        +'&cif='+document.getElementById('cif').value
                        +'&nombre='+escape(document.getElementById('nombre').value)
                        //+'&porcentajeCompro='+porcentajeComp
                        +'&centroEspecialEmpTH='+(document.getElementById('centroEspecialS').checked ? 1 : document.getElementById('centroEspecialN').checked ? 0 : null)
                        +'&particiMayorCentEspeEmpTH='+(document.getElementById('centroEspecialMayoritarioS').checked ? 1 : document.getElementById('centroEspecialMayoritarioN').checked ? 0 : null)
                        +'&empresaInsercionTH='+(document.getElementById('centroInsercionS').checked ? 1 : document.getElementById('centroInsercionN').checked ? 0 : null)
                        +'&promotoraEmprInsercionTH='+(document.getElementById('centroInsercionPromotorS').checked ? 1 : document.getElementById('centroInsercionPromotorN').checked ? 0 : null)
                        +'&planIgualdad='+(document.getElementById('planIgualdadS') !=null && document.getElementById('planIgualdadS') != undefined && document.getElementById('planIgualdadS').checked ? 1 : (document.getElementById('planIgualdadN') != null && document.getElementById('planIgualdadN')!= undefined && document.getElementById('planIgualdadN').checked) ? 0 : null)
                        +'&certificadoCalidad='+( document.getElementById('certificadoCalidadS') != null && document.getElementById('certificadoCalidadS')!= undefined && document.getElementById('certificadoCalidadS').checked ? 1 : (document.getElementById('certificadoCalidadN') != null && document.getElementById('certificadoCalidadN')!= undefined && document.getElementById('certificadoCalidadN').checked) ? 0 : null)
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
                    var listaEntidades = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaEntidades[j] = codigoOperacion;
                        }else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="COLEC_ENT_COD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[0] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_AGRUP_COD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[1] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_NUMEXP"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[2] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_TIPO_CIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[3] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_CIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[4] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_NOMBRE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[5] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_CENTESPEMPTH"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[6] == '1') {
                                           fila[6] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[6] == '0') {
                                           fila[6] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[6] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_PARTMAYCEETH"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[7] == '1') {
                                           fila[7] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[7] == '0') {
                                           fila[7] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[7] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_EMPINSERCIONTH"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[8] == '1') {
                                           fila[8] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[8] == '0') {
                                           fila[8] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[8] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_PROMOEMPINSTH"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[9] == '1') {
                                           fila[9] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[9] == '0') {
                                           fila[9] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[9] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLEC_ENT_PORCEN_COMPROM_REALI"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[10] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[10] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PLANIGUALDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        var valor = nodoCampo.childNodes[0].nodeValue;
                                        if (valor === '1') {
                                           fila[11] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (valor === '0') {
                                           fila[11] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[11] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CERTIFICADOCALIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        var valor = nodoCampo.childNodes[0].nodeValue;
                                        if (valor === '1') {
                                           fila[12] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (valor === '0') {
                                           fila[12] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[12] = '-';
                                    }
                                }
                            }
                            listaEntidades[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //window.returnValue =  listaEntidades;
                            self.parent.opener.retornoXanelaAuxiliar(listaEntidades);
                            //barraProgresoColec('off', 'barraProgresoNuevaEntidad');
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.entidad.guardadaOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch

                    //barraProgresoColec('off', 'barraProgresoNuevaEntidad');
                }else{
                    jsp_alerta("A", mensajeValidacion);
                }
            }
            
            function validarDatos(){
                mensajeValidacion = '';
                var correcto = true;
                try{
                    if(!validarDatosEntidad()){
                        correcto = false;
                    }
                }catch(err){
                    correcto = false;
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }
            
            function validarDatosEntidad(){
                var correcto = true;
                <%
                if(entidadModif != null)
                {
                %>
                    var codEntidadPadre = document.getElementById('codEntidadPadre').value;
                    if(codEntidadPadre == null || codEntidadPadre == ''){
                        document.getElementById('codEntidadPadre').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.codEntidadVacio")%>';
                        return false;
                    }
                <%
                }
                %>
                if (document.getElementById('cif')!=null && (document.getElementById('cif').value=="" || document.getElementById('cif').value==null || document.getElementById('cif').value==undefined)) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.cif.obligatorio")%>';
                        return false;
                }else{
                    if(!validarCIF(document.getElementById('cif').value)){
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.cif.incorrecto")%>';
                        return false;
                    }
                }
                if (document.getElementById('nombre')!=null && (document.getElementById('nombre').value=="" || document.getElementById('nombre').value==null || document.getElementById('nombre').value==undefined)) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.nombre.obligatorio")%>';
                        return false;
                }
                if (!document.getElementById('centroEspecialS').checked && !document.getElementById('centroEspecialN').checked) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.centroEspecial.obligatorio")%>';
                        return false;
                }
                if (!document.getElementById('centroEspecialMayoritarioS').checked && !document.getElementById('centroEspecialMayoritarioN').checked) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.centroEspecial.mayoritario.obligatorio")%>';
                        return false;
                }
                
                if (!document.getElementById('centroInsercionS').checked && !document.getElementById('centroInsercionN').checked) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.centroInsercion.obligatorio")%>';
                        return false;
                }
                if (!document.getElementById('centroInsercionPromotorS').checked && !document.getElementById('centroInsercionPromotorN').checked) {
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.centroInsercion.promotor.obligatorio")%>';
                        return false;
                }
                if(document.getElementById('planIgualdadS')!=null && document.getElementById('planIgualdadS')!=undefined && document.getElementById('planIgualdadN')!=null && document.getElementById('planIgualdadN')!=undefined ){
                    if (!document.getElementById('planIgualdadS').checked && !document.getElementById('planIgualdadN').checked) {
                            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.campo.obligatorio")%>'
                            + " : " + $("#lblPlanIgualdad").text();
                            return false;
                    }
                }

                if(document.getElementById('certificadoCalidadS')!=null && document.getElementById('certificadoCalidadS')!= undefined && document.getElementById('certificadoCalidadN')!=null && document.getElementById('certificadoCalidadN')!= undefined){
                    if (!document.getElementById('certificadoCalidadS').checked && !document.getElementById('certificadoCalidadN').checked) {
                            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.campo.obligatorio")%>'
                            + " : " + $("#lblCertificadoCalidad").text();
                            return false;
                    }
                }
                return correcto;
            }
            
        </script>
<div id="cuerpoNuevaSolicitud" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo" style="overflow-y: auto; padding: 10px;">
<!--        <form  id="formNuevaEntidad">  -->
                <fieldset id="fieldsetDatosEntidad" name="fieldsetDatosEntidad" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.datosEntidad")%></legend>
                    <div class="lineaFormulario" style="display: none">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.entidadPadre")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="5" id="codEntidadPadre" name="codEntidadPadre" value="" class="inputTexto readOnly" disabled="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="display: none">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.entidad")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="5" id="codEntidad" name="codEntidad" value="" class="inputTexto readOnly" disabled="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.cif")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right">
                                <input type="text" maxlength="15" size="15px" id="cif" name="cif" value="" class="inputTexto"/>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.nombre")%>
                        </div>
                        <div style="width: 100px; margin-left: 10px; float: left">
                            <div style="float: right">
                                <input type="text" maxlength="200" size="80" id="nombre" name="nombre" value="" class="inputTexto"/>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="centroEspecial" id="centroEspecialS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centroEspecial" id="centroEspecialN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial.participante.mayor")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="centroEspecialMayoritario" id="centroEspecialMayoritarioS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centroEspecialMayoritario" id="centroEspecialMayoritarioN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="centroInsercion" id="centroInsercionS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centroInsercion" id="centroInsercionN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 500px;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion.promotor")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="centroInsercionPromotor" id="centroInsercionPromotorS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centroInsercionPromotor" id="centroInsercionPromotorN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 500px;" id="lblPlanIgualdad">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.plan.igualdad")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="planIgualdad" id="planIgualdadS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="planIgualdad" id="planIgualdadN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 500px;" id="lblCertificadoCalidad">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%>
                        </div>
                        <div style="width: 100px; float: left; margin-left: 10px;">
                            <div style="float: right;">
                                <input type="radio" name="certificadoCalidad" id="certificadoCalidadS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="certificadoCalidad" id="certificadoCalidadN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="botonera" style="margin-top: 25px;">
                    <input type="button" id="btnGuardarEntidad" name="btnGuardarEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                    <input type="button" id="btnCancelarEntidad" name="btnCancelarEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
        <!--</form>-->
        </div>
</div>		
<script type="text/javascript">
 inicio();
</script>
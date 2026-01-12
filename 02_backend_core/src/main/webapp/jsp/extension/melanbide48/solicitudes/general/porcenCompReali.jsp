<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecComproRealizacionVO" %>

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
    
    List<ColecComproRealizacionVO> listaDatosEntiComproReali = (List<ColecComproRealizacionVO>)request.getAttribute("listaDatosEntiComproReali");
    List<ColecComproRealizacionVO> listaDatosEntiComproRealiValorPorcen = (List<ColecComproRealizacionVO>)request.getAttribute("listaDatosEntiComproRealiValorPorcen");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>


<script type="text/javascript"> 
    var mensajeValidacion = '';
    // Trabajaremos con arrays en el javascripts pq al recargar desde pestaña entidad
    // NO podemos actualizar mediante ajax la parte de List<ColecComproRealizacionVO>
    var listaEntidades = new Array();
    var listaEntidadesDatosCampos = new Array();
    var filaDatos = new Array();
    var contador = 0;
    <% for (ColecComproRealizacionVO datosVali : listaDatosEntiComproReali){
    %>
        
        filaDatos[0]='<%=(datosVali.getCodigoID()!=null?datosVali.getCodigoID():"")%>';
        filaDatos[1]='<%=(datosVali.getEjercicio()!=null?datosVali.getEjercicio():"")%>';
        filaDatos[2]='<%=(datosVali.getNumExpediente()!=null?datosVali.getNumExpediente():"")%>';
        filaDatos[3]='<%=(datosVali.getCodigoEntidad()!=null?datosVali.getCodigoEntidad():"")%>';
        filaDatos[4]='<%=(datosVali.getColectivo()!=null?datosVali.getColectivo():"")%>';
        var _th = ""+'<%=(datosVali.getTerritorioHistorico()!=null?datosVali.getTerritorioHistorico():"")%>';
        _th = (_th.length==1?("0"+_th):_th);
        filaDatos[5]=_th;
        var compRealEnt = '<%=(datosVali.getPorcentajeCompReal()!=null?datosVali.getPorcentajeCompReal():"")%>';
        compRealEnt=reemplazarTextoColec(compRealEnt,'.',',');
        filaDatos[6]=(compRealEnt!='null'?compRealEnt:'');
        filaDatos[7]='<%=(datosVali.getNombreEntidad()!=null?datosVali.getNombreEntidad():"")%>';
        listaEntidades[contador]=filaDatos;
        filaDatos= new Array();
        contador++;
    <%
       } // for de lista de entidades
    %>
        //reinciamos el contador    
        contador=0;
    <%
        for (ColecComproRealizacionVO datosVali : listaDatosEntiComproRealiValorPorcen){
    %>
        filaDatos[0]='<%=(datosVali.getCodigoID()!=null?datosVali.getCodigoID():"")%>';
        filaDatos[1]='<%=(datosVali.getEjercicio()!=null?datosVali.getEjercicio():"")%>';
        filaDatos[2]='<%=(datosVali.getNumExpediente()!=null?datosVali.getNumExpediente():"")%>';
        filaDatos[3]='<%=(datosVali.getCodigoEntidad()!=null?datosVali.getCodigoEntidad():"")%>';
        filaDatos[4]='<%=(datosVali.getColectivo()!=null?datosVali.getColectivo():"")%>';
        var _th = ""+'<%=(datosVali.getTerritorioHistorico()!=null?datosVali.getTerritorioHistorico():"")%>';
        _th = (_th.length==1?("0"+_th):_th);
        filaDatos[5]=_th;
        var compRealEnt = '<%=(datosVali.getPorcentajeCompReal()!=null?datosVali.getPorcentajeCompReal():"")%>';
        compRealEnt=reemplazarTextoColec(compRealEnt,'.',',');
        filaDatos[6]=(compRealEnt!='null'?compRealEnt:'');
        filaDatos[7]='<%=(datosVali.getNombreEntidad()!=null?datosVali.getNombreEntidad():"")%>';
        listaEntidadesDatosCampos[contador]=filaDatos;
        filaDatos= new Array();
        contador++;
    <%
       }// for lista de datos porcentajes para textbox
    %>
    function guardarDatosComproRealxColeTH(){
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var ajax = getXMLHttpRequest();
        if(validarDatosCompRealxColTH()){
            var parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarDatosComproRealxColeTH&tipo=0&numero=<%=numExpediente%>'
                +prepParamGuardarCompRealxColTH()
                +'&control='+control.getTime()
                ;
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
                var nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var nodoSolicitud;
                var hijosSolicitud;
                var fila = new Array();
                var nodoCampo;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="SOLICITUD"){
                        nodoSolicitud = hijos[j];
                        hijosSolicitud = nodoSolicitud.childNodes;
                        for(var cont = 0; cont < hijosSolicitud.length; cont++){
                            if(hijosSolicitud[cont].nodeName=="ID_SOLICITUD"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[0] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }
                        }
                        recargarPestanaGeneral(fila);
                    }
                }
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
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
                }
            }catch(err){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }
        }else{
            jsp_alerta("A",escape(mensajeValidacion));
        }
    }
    
    function recargarPestanaDatosCompRealxColeTH(){
        refrescarEstructuraTablaPestanaCompRealxColeTH();
        refrescarDatosPorcentajesCompRealxColeTH();
    }
    
    function refrescarEstructuraTablaPestanaCompRealxColeTH(){
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var ajax = getXMLHttpRequest();
            var parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=refrescarPestanaCompRealxColeTH&tipo=0&tipoRecarga=1&numero=<%=numExpediente%>'
                +'&control='+control.getTime()
                ;
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
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
                var nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var nodoSolicitud;
                var hijosSolicitud;
                var fila = new Array();
                var listafila = new Array();
                var nodoCampo;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="FILA"){
                        nodoSolicitud = hijos[j];
                        hijosSolicitud = nodoSolicitud.childNodes;
                        for(var cont = 0; cont < hijosSolicitud.length; cont++){
                            if(hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COD_ID"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[0] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_EJERCICIO"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[1] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_NUM_EXP"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[2] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[2] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COD_ENTIDAD"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[3] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[3] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COLECTIVO"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[4] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[4] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_TTHH"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[5] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[5] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_PORCENT"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[6] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[6] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_NOMBRE_ENTIDAD"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[7] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[7] = '-';
                                }
                            }
                        }
                        listafila[j-1]=fila;
                        fila = new Array();
                    }
                }
                if(codigoOperacion=="0"){
                    //jsp_alerta("A",'< %=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    actualizaHtmlEstructuraTabla(listafila);
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
                        }
                    } catch (err) {
                        jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }
    }
    function refrescarDatosPorcentajesCompRealxColeTH(){
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var ajax = getXMLHttpRequest();
            var parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=refrescarPestanaCompRealxColeTH&tipo=0&tipoRecarga=2&numero=<%=numExpediente%>'
                +'&control='+control.getTime()
                ;
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
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
                var nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var nodoSolicitud;
                var hijosSolicitud;
                var fila = new Array();
                var listafila = new Array();
                var nodoCampo;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="FILA"){
                        nodoSolicitud = hijos[j];
                        hijosSolicitud = nodoSolicitud.childNodes;
                        for(var cont = 0; cont < hijosSolicitud.length; cont++){
                            if(hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COD_ID"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[0] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_EJERCICIO"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[1] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_NUM_EXP"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[2] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[2] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COD_ENTIDAD"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[3] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[3] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_COLECTIVO"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[4] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[4] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_TTHH"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[5] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[5] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_COMPREAL_PORCENT"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[6] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[6] = '-';
                                }
                            }else if (hijosSolicitud[cont].nodeName=="COLEC_NOMBRE_ENTIDAD"){
                                nodoCampo = hijosSolicitud[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[7] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[7] = '-';
                                }
                            }
                        }
                        listafila[j-1]=fila;
                        fila = new Array();
                    }
                }
                if(codigoOperacion=="0"){
                    //jsp_alerta("A",'< %=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    actualizaValoresPorcentajesTabla(listafila);
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
                        }
                    } catch (err) {
                        jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }
    }
    
    function actualizaHtmlEstructuraTabla(listafila){
        var htmlNew = '<table class="tablaNormal" style="width: 100%; height: 80%; overflow-y: scroll;">';
        if(listafila!=null && listafila.length>0){
            listaEntidades=listafila;
            for(var i=0;i<listafila.length;i++){
        htmlNew += '<tr>'
                +'      <td style="width: 28%; text-align: left">'
                +        listafila[i][7]//< %=(datos.getNombreEntidad()!=null?datos.getNombreEntidad():"-")%>
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col1_01_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col1_20_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col1_48_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col2_01_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'       </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col2_20_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col2_48_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col3_01_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col3_20_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col3_48_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col4_01_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col4_20_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'        <td style="width: 6%;">'
                +'            <input type="text" id="txtPCR_Col4_48_'+listafila[i][3]+'" class="textoNumerico" maxlength="6" size="3">'
                +'        </td>'
                +'    </tr>'
                ;
            } // Cierra For
        }else{
            htmlNew+='<tr>'
                    +'   <td colspan="13">'
                    +'        No hay registro de entidades para mostrar.'
                    +'    </td>'
                    +'</tr>'
                    ;
            }
         htmlNew += '</table>';
         document.getElementById("cuerpoT").innerHTML=htmlNew;
    }
    
    function validarDatosCompRealxColTH(){
        var nombreTxtBase = 'txtPCR_Col';
        var nombreTxtComodin = '_';
        var provincias = new Array('01','20','48');
        // Recorremos los 4 colectivos
        for(var x=1;x<=4;x++){
            var nombreTxt=nombreTxtBase+x+nombreTxtComodin;
            //Recorremos las Provincias
            for(var y=0;y<provincias.length;y++){
                nombreTxt += provincias[y]+nombreTxtComodin;
                //Recorremos las entidades y comprobamos los campos
                //< %for(ColecComproRealizacionVO datosVali : listaDatosEntiComproReali){
                for(var i=0;i<listaEntidades.length;i++){
                //% >
                //nombreTxt+='< %=datosVali.getCodigoEntidad()%>';
                if(document.getElementById(nombreTxt+listaEntidades[i][3])!=null){
                    if(!validarNumericoDecimalColec(document.getElementById(nombreTxt+listaEntidades[i][3]),5,2)){
                        //document.getElementById(nombreTxt).style.border = '1px solid red';
                        document.getElementById(nombreTxt+listaEntidades[i][3]).focus();
                        document.getElementById(nombreTxt+listaEntidades[i][3]).select();
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.porcentaje.compromiso.Decimal")%>';
                        return false;
                    }
                    var porcentajeCompro=document.getElementById(nombreTxt+listaEntidades[i][3]).value;
                    porcentajeCompro=reemplazarTextoColec(porcentajeCompro,',','.');
                    if(new Number(porcentajeCompro)>100.00){
                        document.getElementById(nombreTxt+listaEntidades[i][3]).focus();
                        document.getElementById(nombreTxt+listaEntidades[i][3]).select();
                        mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.porcentaje.compromiso.Decimal")%>';
                        return false;
                    }
                }
                //< %
                } // Cierra For Entidades
                //% >
               //Reseteamos nombre element para que avance a la siguiente provinvia al continua el LOOP.
                nombreTxt=nombreTxtBase+x+nombreTxtComodin;
            } // For Provincias
        } // For Colectivos
        return true;
    }
    
    function prepParamGuardarCompRealxColTH(){
        var nombreTxtBase = 'txtPCR_Col';
        var nombreTxtComodin = '_';
        var provincias = new Array('01','20','48');
        var textoParametros='';
        var codEntidades ='';
        // Recorremos los 4 colectivos
        for(var x=1;x<=4;x++){
            var nombreTxt=nombreTxtBase+x+nombreTxtComodin;
            //Recorremos las Provincias
            for(var y=0;y<provincias.length;y++){
                nombreTxt += provincias[y]+nombreTxtComodin;
                //Recorremos las entidades y comprobamos los campos
               //< %
                //for(ColecComproRealizacionVO datosVali : listaDatosEntiComproReali){
                for(var i=0;i<listaEntidades.length;i++){
                //% >
                if(document.getElementById(nombreTxt+listaEntidades[i][3])!=null){  //'< %=datosVali.getCodigoEntidad()%>'
                    textoParametros+='&'+nombreTxt+listaEntidades[i][3]+'='+ reemplazarTextoColec(document.getElementById(nombreTxt+listaEntidades[i][3]).value,',','.');
                    if(codEntidades=='')
                        codEntidades+=listaEntidades[i][3];
                    else{
                        if(codEntidades.indexOf(listaEntidades[i][3]) === -1){
                            codEntidades+= ','+listaEntidades[i][3];
                        }
                    }
                }
                //< %
                } // Cierra For Entidades
                //% >
               //Reseteamos nombre element para que avance a la siguiente provinvia al continua el LOOP.
                nombreTxt=nombreTxtBase+x+nombreTxtComodin;
            } // For Provincias
        } // For Colectivos
        textoParametros+='&codEntidades='+codEntidades;
        return textoParametros;
    }
    
    function inicioPCR(){
        var nombreTxtBase = 'txtPCR_Col';
        var nombreTxtComodin = '_';
        <%if(listaDatosEntiComproRealiValorPorcen!=null){
            for(ColecComproRealizacionVO datosVali : listaDatosEntiComproRealiValorPorcen){
        %>
                // Formamos el Nombre del elemento a buscar para asignar el Nombre:
                var nombreElement = nombreTxtBase+'<%=datosVali.getColectivo()%>'+nombreTxtComodin;
                var th = ""+'<%=datosVali.getTerritorioHistorico()%>';
                th = (th.length==1?("0"+th):th);
                nombreElement+=(th+nombreTxtComodin)+'<%=datosVali.getCodigoEntidad()%>';
               if(document.getElementById(nombreElement)!=null){
                    document.getElementById(nombreElement).value=reemplazarTextoColec('<%=datosVali.getPorcentajeCompReal()!=null?datosVali.getPorcentajeCompReal():""%>','.',',');
                }else{
                    //alert("Cuadro de texto no encontrado" + nombreElement);
                }
        <%
            }// Cierra For Entidades
        } // Cierra if lista null
        else{
        %>
                //alert("listaDatosEntiComproRealiValorPorcen  ha llegado a null")
        <%
        }
        %>
    }
    
    function actualizaValoresPorcentajesTabla(listafila){
        if(listafila!=null){
            listaEntidadesDatosCampos=listafila;
            var nombreTxtBase = 'txtPCR_Col';
            var nombreTxtComodin = '_';
            for(var i=0; i<listafila.length;i++){
                    // Formamos el Nombre del elemento a buscar para asignar el Nombre:
                    var nombreElement = nombreTxtBase+listafila[i][4]+nombreTxtComodin;
                    var th = ""+listafila[i][5];
                    th = (th.length==1?("0"+th):th);
                    nombreElement+=(th+nombreTxtComodin)+listafila[i][3];
                   if(document.getElementById(nombreElement)!=null){
                        var valor=reemplazarTextoColec(listafila[i][6],'.',',');
                        document.getElementById(nombreElement).value=(valor!='null'?valor:'');
                    }else{
                        //alert("Cuadro de texto no encontrado" + nombreElement);
                    }
                }// Cierra For Entidades
            } // Cierra if lista null
            else{
                    //alert("listaDatosEntiComproRealiValorPorcen  ha llegado a null")
            }
    }
</script>
<style>    
    #tablaPorcen th{
        font-size: 11px !important;        
    }
   #tablaPorcen td{
        font-size: 11px !important;        
    }
</style>
<div>
    <div id="contenedorT">
        <div id="cabeceraT">
            <table class="tablaNormal" id="tablaPorcen" style="width: 100%;">
                <!--<tbody>-->
                    <tr>
                        <th style="width: 28%;"  rowspan="2">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.entidad")%>
                        </th>
                        <th colspan="3">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colectivo1")%>
                        </th>
                        <th colspan="3">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colectivo2")%>
                        </th>
                        <th colspan="3">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colectivo3")%>
                        </th>
                        <th colspan="3">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colectivo4")%>
                        </th>
                    </tr>
                    <tr>
<!--                        <td style="width: 70px;">
                        </td>-->
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colAraba")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colGipuzkoa")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colBizkaia")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colAraba")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colGipuzkoa")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colBizkaia")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colAraba")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colGipuzkoa")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colBizkaia")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colAraba")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colGipuzkoa")%>
                        </th>
                        <th style="width: 6%;">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.general.tabla.colBizkaia")%>
                        </th>
                    </tr>
                <!--</tbody>-->
            </table>
        </div>
        <div id="cuerpoT">
            <table class="tablaNormal" id="tablaPorcen" style="width: 100%; height: 80%; overflow-y: scroll;">
                <!--<tbody>-->
                    <%
                        if(listaDatosEntiComproReali!=null && listaDatosEntiComproReali.size()>0){
                            for(ColecComproRealizacionVO datos : listaDatosEntiComproReali){
                    %>
                    <tr>
                        <td style="width: 28%; text-align: left">
                            <%=(datos.getNombreEntidad()!=null?datos.getNombreEntidad():"-")%>
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col1_01_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col1_20_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col1_48_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col2_01_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col2_20_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col2_48_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col3_01_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col3_20_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col3_48_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col4_01_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col4_20_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                        <td style="width: 6%;">
                            <input type="text" id="txtPCR_Col4_48_<%=(datos.getCodigoEntidad()!=null?datos.getCodigoEntidad():0)%>" class="textoNumerico" maxlength="6" size="3">
                        </td>
                    </tr>
                    <%
                        } // Cierra For
                        }else{
                    %>
                    <tr>
                        <td colspan="13">
                            No hay registro de entidades para mostrar.
                        </td>
                    </tr>
                    <%
                        }
                    %>
                <!--</tbody>-->
            </table>
        </div>
    </div>
    <div class="botonera">
          <input type="button" id="btnGuardarDatCompReal" name="btnGuardarDatCompReal" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarDatosComproRealxColeTH();">
    </div>
</div>    
<script type="text/javascript">   
    inicioPCR();
</script>
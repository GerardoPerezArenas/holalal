<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaPreparadorJustificacionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
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
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    Integer anoExpediente = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
    
    //String nombreTabla = "solicitudes";
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

<script type="text/javascript">
    
    function cargarFicheroSeguimientosPrepEca(){
        
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_seguimientos_jus').files){
            if(document.getElementById('fichero_seguimientos_jus').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_seguimientos_jus').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){    
            var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarJustificacion")%>');
            if (resultado == 1){        
                document.getElementById('msgImportandoSeguimientos').style.display='inline';
                document.getElementById('msgEliminandoSeguimientos').style.display='none';
                barraProgresoEca('on', 'barraProgresoPreparadoresJustificacion');

                document.getElementById('opcionescargains').style.display = 'none';
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarInsercionesPrep').disabled = false;
                document.getElementById('opcionescargaseg').style.display = 'none';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarSegumientosPrep').disabled = false;

                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=procesarExcelSeguimientosPrep&tipo=0&numero=<%=numExpediente%>&tiposeg=0&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameSeguimientosPrep';
                document.forms[0].submit();
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }
    
    function cargarFicheroInsercionesPrepEca(){
        
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_inserciones_jus').files){
            if(document.getElementById('fichero_inserciones_jus').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_inserciones_jus').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){      
            var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarJustificacion")%>');
            if (resultado == 1){      
                document.getElementById('msgImportandoSeguimientos').style.display='inline';
                document.getElementById('msgEliminandoSeguimientos').style.display='none';
                barraProgresoEca('on', 'barraProgresoPreparadoresJustificacion');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=procesarExcelSeguimientosPrep&tipo=0&numero=<%=numExpediente%>&tiposeg=1&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameSeguimientosPrep';
                document.forms[0].submit();
                document.getElementById('opcionescargains').style.display = 'none';
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarInsercionesPrep').disabled = false;
                document.getElementById('opcionescargaseg').style.display = 'none';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarSegumientosPrep').disabled = false;
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }

    function uploadProgress(evt) {
        if (evt.lengthComputable) {
            var percentComplete = Math.round(evt.loaded * 100 / evt.total);
            document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
            document.getElementById('prog').value = percentComplete;
        }
        else {
            document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
    }

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        //alert(evt.target.responseText);
    }

    function uploadFailed(evt) {
        //alert("There was an error attempting to upload the file.");
    }

    function uploadCanceled(evt) {
        //alert("The upload has been canceled by the user or the browser dropped the connection.");
    }
    
    function getListaPreparadoresJustificacionEca(){        
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        var listaPreparadores = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getListaPreparadoresJustificacion&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            listaPreparadores = extraerListadoPreparadoresJustificacionEca(nodos);
        }
        catch(err){
            //alert(err);
        }//try-catch
        return listaPreparadores;
    }
    
    function recargarTablaPreparadoresJustificacionEca(result, actualizarOtrasPestanas){
      
        var fila;
        var haySustitutos = false;
        listaPreparadores_jus = new Array();
        listaPreparadores_jusTabla = new Array();        
        listaPreparadores_jusTabla_titulos = new Array();
        listaPreparadores_jusTabla_estilos = new Array();
        var listaEstilosFila;
        var lstEstilosEnlaceFila;
           try{
        var camposErrores = new Array();         
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            //listaPreparadores_jus[i-1] = fila;  //NO FUNCIONA EN IE9          
            listaPreparadores_jus[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                            fila[10],fila[11],fila[12],fila[13],fila[14],fila[15],fila[16],fila[17],fila[18],fila[19],
                                            fila[20],fila[21],fila[22],fila[23],fila[24],fila[25],fila[26],fila[27],fila[28],fila[29],fila[30],fila[31],fila[32],fila[33],fila[34],fila[35],fila[36]];
            listaPreparadores_jusTabla_titulos[i-1] = [getListAsText(fila[36]), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], 
                fila[11], 
                '<a href=\'javascript:cargarSeguimientosPrepEca('+fila[0]+');\'>'+fila[12]+'</a>', 
                fila[13], 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1,0)\'>'+fila[14]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1,1)\'>'+fila[15]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1)\'>'+fila[16]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2,0)\'>'+fila[17]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2,1)\'>'+fila[18]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2)\'>'+fila[19]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3,0)\'>'+fila[20]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3,1)\'>'+fila[21]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3)\'>'+fila[22]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\'>'+fila[23]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\'>'+fila[24]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'>'+fila[25]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\'>'+fila[26]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\'>'+fila[27]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'>'+fila[28]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\'>'+fila[29]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\'>'+fila[30]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'>'+fila[31]+'</a>', 
                fila[32], fila[33], fila[34]];
            
            inicializarErroresCamposPrepJus();
            lstEstilosEnlaceFila =new Array();
            for (var j = 0; j < fila[35].length; j++){
                lstEstilosEnlaceFila[j]= 'color: black;';
            }
            if(fila[36] != undefined && fila[36].length > 0){
                listaEstilosFila = new Array();
                camposErrores = fila[38];
                for(var j = 0; j < camposErrores.length; j++){
                    if(camposErrores[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                        listaEstilosFila[j] = 'color: red;';
                        lstEstilosEnlaceFila[j]= 'color: red;';
                    }else{
                        listaEstilosFila[j] = 'color: black;';
                        lstEstilosEnlaceFila[j]= 'color: black;';
                    }
                }
                
                listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = 'color: red;';
                listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
                
                listaPreparadores_jusTabla_estilos[i-1] = listaEstilosFila;
            }
            
            if(fila[35] != undefined && fila[35] == '1'){
                haySustitutos = true;
                if(listaEstilosFila == undefined || listaEstilosFila == null){
                    listaEstilosFila = new Array();
                }
                var estilo;
                if(listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>];
                }else{
                    estilo = '';
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = estilo;
                if(listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>];
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = estilo;
                
                listaPreparadores_jusTabla_estilos[i-1] = listaEstilosFila;
            }
            
            listaPreparadores_jusTabla[i-1] = [fila[36] != undefined && fila[36].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide44/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10],
                fila[11],
                '<a href=\'javascript:cargarSeguimientosPrepEca('+fila[0]+');\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>]+'</a>', 
                fila[13], 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>]+'\'>' +fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 2)\'   style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 3)\'   style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'   style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'   style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,0)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4,1)\' style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>]+'</a>', 
                '<a href=\'javascript:cargarInsercionesPrepEca('+fila[0]+', 4)\'   style=\''+lstEstilosEnlaceFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>]+'\'>'+fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>]+'</a>', 
            fila[32], fila[33], fila[34]];    
            
            listaEstilosFila = new Array();
        }
        }catch(Err){
                
            }

    tabPreparadores_jus = new FixedColumnTable(document.getElementById('listaPreparadores_jus'), 850, 876, 'listaPreparadores_jus');
    tabPreparadores_jus.addColumna('30','center','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
    //if(haySustitutos == true){
        tabPreparadores_jus.addColumna('140','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>');
    //}else{
    //    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>');
    //}
    tabPreparadores_jus.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2")%>');
    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4")%>');
    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5")%>');
    tabPreparadores_jus.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.preparadores.tabla.coltipoCont")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6")%>');
    tabPreparadores_jus.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8")%>');
    tabPreparadores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9")%>');
    tabPreparadores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col10")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11")%>');
    tabPreparadores_jus.addColumna('130','right','<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12"), anoExpediente)%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28")%>');
 tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col30")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col31")%>');
     tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col32")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col33")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col34")%>');
    
        tabPreparadores_jus.numColumnasFijas = 3;

        //tabPreparadores_jus.lineas=listaPreparadores_jusTabla;
        
         for(var cont = 0; cont < listaPreparadores_jusTabla.length; cont++){
            tabPreparadores_jus.addFilaConFormato(listaPreparadores_jusTabla[cont], listaPreparadores_jusTabla_titulos[cont], listaPreparadores_jusTabla_estilos[cont])
        }

        tabPreparadores_jus.height = '300';

        tabPreparadores_jus.altoCabecera = 50;
        
        tabPreparadores_jus.dblClkFunction = 'dblClckTablaPreparadoresJustificacion';

        tabPreparadores_jus.displayTabla();

        tabPreparadores_jus.pack();
         
        if(actualizarOtrasPestanas){ 
            actualizarOtrasPestanasEca(5);
        }
    }
    
    function actualizarTablaPreparadoresJustificacionEca(actualizarOtrasPestanas){
       
        try{
       
            var result = getListaPreparadoresJustificacionEca();
            recargarTablaPreparadoresJustificacionEca(result, actualizarOtrasPestanas);            
        }catch(err){
        }
        barraProgresoEca('off', 'barraProgresoPreparadoresJustificacion');
    }
   
    function pulsarNuevoPreparadorJustificacionEca(){
        if(tabPreparadores_jus.selectedIndex != -1) {
            if(listaPreparadores_jus[tabPreparadores_jus.selectedIndex][35] == undefined || listaPreparadores_jus[tabPreparadores_jus.selectedIndex][35] == null || listaPreparadores_jus[tabPreparadores_jus.selectedIndex][35] != '1')
            {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoPreparadorJustificacion&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&idPrepSustituir='+listaPreparadores_jus[tabPreparadores_jus.selectedIndex][0]+'&control='+control.getTime(),430,840,'no','no', function(result){
                	if (result != undefined){
						if(result[0] == '0'){
							recargarTablaPreparadoresJustificacionEca(result, true);
						}
					}
				});
            }else{
                jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.sustitutoSustNoPermitido")%>');
            }
       } 
        else {
            jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
     
    function pulsarModifPreparadorJustificacionEca(){                
        if(tabPreparadores_jus.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoPreparadorJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPrepModificar='+listaPreparadores_jus[tabPreparadores_jus.selectedIndex][0]+'&control='+control.getTime(),430,840,'no','no', function(result){
                if (result != undefined){
					if(result[0] == '0'){
						recargarTablaPreparadoresJustificacionEca(result, true);
					}
				}
			});
        } 
        else {
            jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarPreparadorJustificacionEca(){
        
            if(tabPreparadores_jus.selectedIndex != -1) {
                var resultado = 0;
                if(preparadorTieneSeguimientosInserciones(tabPreparadores_jus.selectedIndex)){
                    resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarPrepSegIns")%>');
                }else{
                    resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarPrep")%>');
                }
                
                
                if (resultado == 1){
                    
                    document.getElementById('msgImportandoSeguimientos').style.display="none";
                    document.getElementById('msgEliminandoSeguimientos').style.display="inline";
                    barraProgresoEca('on', 'barraProgresoPreparadoresJustificacion');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPreparadores = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=eliminarPreparadorJustificacion&tipo=0&numero=<%=numExpediente%>&idPrep='+listaPreparadores_jus[tabPreparadores_jus.selectedIndex][0]+'&control='+control.getTime();
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
                        listaPreparadores = extraerListadoPreparadoresJustificacionEca(nodos);
                        recargarTablaPreparadoresJustificacionEca(listaPreparadores, true);
                        barraProgresoEca('off', 'barraProgresoPreparadoresJustificacion');
                        var codigoOperacion = listaPreparadores[0];
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function extraerListadoPreparadoresJustificacionEca(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaPreparadores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoErrores;
            var errores;
            var nodoCampo;
            var j;
            
            inicializarErroresCamposPrepJus();
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPreparadores[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){                                
                                 fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                 fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }    
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }
                        /*else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }*/
                        else if(hijosFila[cont].nodeName=="TIPO_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NUM_SEG_ANTERIORES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2H"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3H"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4M"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = '-';
                            }
                        }
                        
                        
                        else if(hijosFila[cont].nodeName=="C5H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C5M"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C5TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>] = '-';
                            }
                        }
                        
                        
                        else if(hijosFila[cont].nodeName=="C6H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C6M"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C6TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SEGUIMIENTOS_INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ERRORES"){
                            listaErrores = new Array();
                            nodoErrores = hijosFila[cont];
                            errores = nodoErrores.childNodes;
                            for(var contE = 0; contE < errores.length; contE++){
                                if(errores[contE].nodeName=="ERROR"){
                                    if(errores[contE].childNodes.length > 0){
                                        listaErrores[contE] = errores[contE].childNodes[0].nodeValue;
                                    }
                                }
                            }
                            fila[36] = listaErrores;
                        }else if(hijosFila[cont].nodeName=="ES_SUSTITUTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[35] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[35] = '0';
                            }
                        }
                    }
                    fila[38] = camposErrores;
                    listaPreparadores[j] = fila;
                    fila = new Array();
                    camposErrores = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            return listaPreparadores;
    }
   
    function dblClckTablaPreparadoresJustificacion(rowID,tableName){
        pulsarModifPreparadorJustificacionEca();
    }
    
    function mostrarBotonCargarFicheroSeguimientosJus(){
        if(document.getElementById('fichero_seguimientos_jus').files.length > 0){
            document.getElementById('btnCargarFicheroSeguimientosJus').disabled = false;
            document.getElementById('btnCargarFicheroSeguimientosJus').className = 'botonMasLargo';
        }else{
            document.getElementById('btnCargarFicheroSeguimientosJus').disabled = true;
            document.getElementById('btnCargarFicheroSeguimientosJus').className = 'botonMasLargoDeshabilitado';
        }
    }
    
    function mostrarBotonCargarFicheroInsercionesJus(){
        if(document.getElementById('fichero_inserciones_jus').files.length > 0){
            document.getElementById('btnCargarFicheroInsercionesJus').disabled = false;
            document.getElementById('btnCargarFicheroInsercionesJus').className = 'botonMasLargo';
        }else{
            document.getElementById('btnCargarFicheroInsercionesJus').disabled = true;
            document.getElementById('btnCargarFicheroInsercionesJus').className = 'botonMasLargoDeshabilitado';
        }
    }
    
    function mostrarOpcionesCargaSeguimiento(){      
           if (document.getElementById('btnCargarSegumientosPrep').disabled){
                document.getElementById('opcionescargaseg').style.display = 'none';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarSegumientosPrep').disabled = false;
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarInsercionesPrep').disabled = false;
           }else {
               document.getElementById('opcionescargaseg').style.display = 'block';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargoDeshabilitado';
                document.getElementById('btnCargarSegumientosPrep').disabled = true;
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargoDeshabilitado';
                document.getElementById('btnCargarInsercionesPrep').disabled = true;
           }
        
    }
    
    function mostrarOpcionesCargaInsercion(){      
           if (document.getElementById('btnCargarInsercionesPrep').disabled){
                document.getElementById('opcionescargains').style.display = 'none';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarSegumientosPrep').disabled = false;
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargo';
                document.getElementById('btnCargarInsercionesPrep').disabled = false;
           }else {
               document.getElementById('opcionescargains').style.display = 'block';
                document.getElementById('btnCargarSegumientosPrep').className = 'botonMasLargoDeshabilitado';
                document.getElementById('btnCargarSegumientosPrep').disabled = true;
                document.getElementById('btnCargarInsercionesPrep').className = 'botonMasLargoDeshabilitado';
                document.getElementById('btnCargarInsercionesPrep').disabled = true;
           }
        
    }
    function cargarSeguimientosPrepEca(preparador){        
        var control = new Date();
        var result = null;    
        var opcion = '';
        if(document.forms[0].modoConsulta.value == "si"){
            opcion = 'consultar';
        }else{
            opcion = 'modificar';
        }   
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarSeguimientos&tipo=0&numero=<%=numExpediente%>&idPrep='+preparador+'&tiposeg=0&opcion='+opcion+'&control='+control.getTime(),430,820,'no','no', function(result){
        	if(result != undefined && result == true){
				var lista = getListaPreparadoresJustificacionEca();
				recargarTablaPreparadoresJustificacionEca(lista, true)
			}
        });    
    }
    function cargarInsercionesPrepEca(preparador, colectivo, sexo){   
    if (sexo==null) sexo="";
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarSeguimientos&tipo=0&numero=<%=numExpediente%>&idPrep='+preparador+'&colectivo='+colectivo+'&sexo='+sexo+'&tiposeg=1&control='+control.getTime(),430,820,'no','no', function(result){
			if(result != undefined && result == true){
				var lista = getListaPreparadoresJustificacionEca();
				recargarTablaPreparadoresJustificacionEca(lista, true)
			}
		});
    }
    function cargarTodosSeguimientosPrep(){        
        var control = new Date();
        var result = null;    
        var opcion = '';
        if(document.forms[0].modoConsulta.value == "si"){
            opcion = 'consultar';
        }else{
            opcion = 'modificar';
        }
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarSeguimientos&tipo=0&numero=<%=numExpediente%>&tiposeg=0&opcion='+opcion+'&control='+control.getTime(),580,920,'no','no', function(result){
			if(result != undefined && result == true){
				var lista = getListaPreparadoresJustificacionEca();
				recargarTablaPreparadoresJustificacionEca(lista, true)
			}
        });
    }
    function cargarTodasInsercionesPrep(){   
       var control = new Date();
        var result = null;    
        var opcion = '';
        if(document.forms[0].modoConsulta.value == "si"){
            opcion = 'consultar';
        }else{
            opcion = 'modificar';
        }  
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarSeguimientos&tipo=0&numero=<%=numExpediente%>&tiposeg=1&opcion='+opcion+'&control='+control.getTime(),590,900,'no','no', function(result){
			if(result != undefined && result == true){
				var lista = getListaPreparadoresJustificacionEca();
				recargarTablaPreparadoresJustificacionEca(lista, true)
			}
       });
    }
    
    function inicializarErroresCamposPrepJus(){
        camposErrores = new Array();
        for(var i = 0; i < <%=FilaPreparadorJustificacionVO.NUM_CAMPOS_FILA%>; i++){
            camposErrores[i] = '<%=ConstantesMeLanbide44.FALSO%>';
        }
    }
    
    function habilitarBotonesListados(){
        document.getElementById('btnListadoSegumientosPrep').disabled = false;
        document.getElementById('btnListadoInsercionesPrep').disabled = false;
    }
    
    function preparadorTieneSeguimientosInserciones(selectedIndex){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        var listaPreparadores = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getNumeroSeguimientosInsercionesPreparador&tipo=0&numero=<%=numExpediente%>&idPrep='+listaPreparadores_jus[selectedIndex][0]+'&control='+control.getTime();
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
            var listaPreparadores = new Array();
            var seguimientos = 0;
            var inserciones = 0;
            var j;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPreparadores[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="SEGUIMIENTOS"){
                    seguimientos = hijos[j].childNodes[0].nodeValue
                }                   
                else if(hijos[j].nodeName=="INSERCIONES"){
                    inserciones = hijos[j].childNodes[0].nodeValue
                }
            }
            var codigoOperacion = listaPreparadores[0];
            if(codigoOperacion=="0"){
                var total = parseInt(seguimientos) + parseInt(inserciones);
                if(total > 0){
                    return true;
                }else{
                    return false;
                }
            }else if(codigoOperacion=="1"){
                return false;
            }
        }catch(err){
            
        }
    }
    
    function pulsarCopiarPreparadores(){
        var resultado = 0;
        resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaCopiarPreparadores")%>');
        
        if (resultado == 1){    
            //alert("copiar");
                    
            document.getElementById('msgCopiandoPreparadores').style.display="inline";
            document.getElementById('msgEliminandoSeguimientos').style.display="none";
            document.getElementById('msgImportandoSeguimientos').style.display="inline";
            barraProgresoEca('on', 'barraProgresoPreparadoresJustificacion');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            var listaPreparadores = new Array();
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=copiarPreparadores&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                        listaPreparadores = extraerListadoPreparadoresJustificacionEca(nodos);
                        recargarTablaPreparadoresJustificacionEca(listaPreparadores, true);
                        barraProgresoEca('off', 'barraProgresoPreparadoresJustificacion');
                        var codigoOperacion = listaPreparadores[0];
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.registrosCopiadosOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                        }else if(codigoOperacion=="-1"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.noPreparadoresCopiar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                    }//try-catch
                }
            else {
                jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
</script>
<body>
    <div style="width: 100%; float: left; text-align: left; clear: both; height: 100%; overflow-x: hidden; overflow-y: auto;">
        <div id="barraProgresoPreparadoresJustificacion" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgImportandoSeguimientos">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                                </span>
                                                <span id="msgEliminandoSeguimientos">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
                                                </span>
                                                <span id="msgCopiandoPreparadores">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.copiandoDatosPrep")%>
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
            <input type="button" id="btnCopiarPreparadores" name="btnCopiarPreparadores" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.CopiarPreparadores")%>" style="margin:2px" onClick="pulsarCopiarPreparadores();">
            
            <div id="listaPreparadores_jus" style="padding: 5px; width:98%; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            
                  
            <div class="botonera">
                <input type="button" id="btnNuevoPreparadorJus" name="btnNuevoPreparadorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.sustituir")%>" onclick="pulsarNuevoPreparadorJustificacionEca();">
                <input type="button" id="btnModificarPreparadorJus" name="btnModificarPreparadorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifPreparadorJustificacionEca();" >
                <input type="button" id="btnEliminarPreparadorJus" name="btnEliminarPreparadorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarPreparadorJustificacionEca();">
            </div>
            <div style="width: 20%; float: left; padding-top: 10px;" onmouseover="habilitarBotonesListados();">
                <input type="button" id="btnCargarSegumientosPrep" name="btnCargarSegumientosPrep" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.cargaSeguimientos")%>" onclick="mostrarOpcionesCargaSeguimiento();" style="margin: 2px;">
                <input type="button" id="btnCargarInsercionesPrep" name="btnCargarInsercionesPrep" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.cargainserciones")%>" onclick="mostrarOpcionesCargaInsercion();" style="margin: 2px;">
                <input type="button" id="btnListadoSegumientosPrep" name="btnListadoSegumientosPrep" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.listadoSeguimientos")%>" onclick="cargarTodosSeguimientosPrep();" style="margin: 2px;">
                <input type="button" id="btnListadoInsercionesPrep" name="btnListadoInsercionesPrep" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.listadoInserciones")%>" onclick="cargarTodasInsercionesPrep();" style="margin: 2px;">
            </div>
            <div style="width: 78%; text-align: right; height: 110px; float: right; padding-top: 10px;">
                <fieldset id="opcionescargaseg" style="display: none; width: 100%; height: 100px; padding-top: 24px;">
                    <div style="text-align: right; clear: both; margin-bottom: 10px;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.cargarFicheroSeguimientos")%>:&nbsp;
                        <input type="file"  name="fichero_seguimientos_jus" id="fichero_seguimientos_jus" class="inputTexto" size="57" accept=".xls">
                        <a href="#" onclick="descargarPlantillaEca('plantilla_justificacion_seg.xls');"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.descargarPlantilla")%></a>
                    </div>
                    <div class="botonera">
                        <input type="button" id="btnCargarFicheroSeguimientosJus" name="btnCargarFicheroSeguimientosJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%>" onclick="cargarFicheroSeguimientosPrepEca();">
                        <input type="button" id="btnCancelarFicheroSeguimientosJus" name="btnCancelarFicheroSeguimientosJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cancelar")%>" onclick="mostrarOpcionesCargaSeguimiento();" >
                    </div>
                </fieldset>
                <fieldset id="opcionescargains" style="display: none; width: 100%; height: 100px; padding-top: 24px;">
                    <div style="text-align: right; clear: both; margin-bottom: 10px;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.cargarFicheroInserciones")%>:&nbsp;
                        <input type="file"  name="fichero_inserciones_jus" id="fichero_inserciones_jus" class="inputTexto" size="57" accept=".xls">
                        <a href="#" onclick="descargarPlantillaEca('plantilla_justificacion_ins.xls');"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.descargarPlantilla")%></a>
                    </div>
                    <div class="botonera"> 
                        <input type="button" id="btnCargarFicheroInsercionesJus" name="btnCargarFicheroInsercionesJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%>" onclick="cargarFicheroInsercionesPrepEca();">
                        <input type="button" id="btnCancelarFicheroInsercionesJus" name="btnCancelarFicheroInsercionesJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cancelar")%>" onclick="mostrarOpcionesCargaInsercion();">
                    </div>

                    <div style="display:none">
                        Esto se debe dejar, porque si no los botones de arriba se van al final de la pantalla
                    </div>
                </fieldset>
            </div>
        </div>
    </div>

    <iframe id="uploadFrameSeguimientosPrep" name="uploadFrameSeguimientosPrep" height="0" width="0" 
    frameborder="0" scrolling="yes"></iframe>  
</body>

<script type="text/javascript">
    var tabPreparadores_jus;
    var listaPreparadores_jus = new Array();
    var listaPreparadores_jusTabla = new Array();
    var listaPreparadores_jusTabla_titulos = new Array();
    var listaPreparadores_jusTabla_estilos = new Array();
    var camposErrores = new Array();
    var haySustitutos = false;
    
    <%  		
        FilaPreparadorJustificacionVO act = null;
        
        List<FilaPreparadorJustificacionVO> solList = (List<FilaPreparadorJustificacionVO>)request.getAttribute("listaPreparadoresJustificacion");
        if (solList!= null && solList.size() >0){
            for (int i = 0; i <solList.size(); i++)
            {
                act = solList.get(i);
                
    %>
        listaPreparadores_jus[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getTipoContrato()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getNumSegAnteriores()%>', '<%=act.getImporte()%>', '<%=act.getC1h()%>', '<%=act.getC1m()%>', '<%=act.getC1Total()%>', 
            '<%=act.getC2h()%>', '<%=act.getC2m()%>', '<%=act.getC2Total()%>', '<%=act.getC3h()%>', '<%=act.getC3m()%>', '<%=act.getC3Total()%>', 
            '<%=act.getC4h()%>', '<%=act.getC4m()%>', '<%=act.getC4Total()%>','<%=act.getC5h()%>', '<%=act.getC5m()%>', '<%=act.getC5Total()%>','<%=act.getC6h()%>', '<%=act.getC6m()%>', '<%=act.getC6Total()%>', '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>'];
         
        listaPreparadores_jus[<%=i%>][36] = '<%=act.esSustituto() ? "1" : "0"%>';
         
        var arrayErrores = new Array();
        <%
            if(act.getErrores() != null)
            {
                for(int contE = 0; contE < act.getErrores().size(); contE++)
                {
        %>
                    arrayErrores[<%=contE%>] = '<%=act.getErrores().get(contE)%>';
        <%
                }
            }
        %>
            
        var erroresStr = '';
        erroresStr = getListAsText(arrayErrores);
        
        listaPreparadores_jusTabla_titulos[<%=i%>] =  [erroresStr, '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>',  '<%=act.getTipoContrato()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getNumSegAnteriores()%>', '<%=act.getImporte()%>', '<%=act.getC1h()%>', '<%=act.getC1m()%>', '<%=act.getC1Total()%>', 
            '<%=act.getC2h()%>', '<%=act.getC2m()%>', '<%=act.getC2Total()%>', '<%=act.getC3h()%>', '<%=act.getC3m()%>', '<%=act.getC3Total()%>', 
            '<%=act.getC4h()%>', '<%=act.getC4m()%>', '<%=act.getC4Total()%>','<%=act.getC5h()%>', '<%=act.getC5m()%>', '<%=act.getC5Total()%>','<%=act.getC6h()%>', '<%=act.getC6m()%>', '<%=act.getC6Total()%>', '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>'];
         
        
        
        inicializarErroresCamposPrepJus();
        
        <%
            String errorAct = "";
            for(int posCE = 0; posCE < FilaPreparadorJustificacionVO.NUM_CAMPOS_FILA; posCE++)
            {
        %>
                    camposErrores[<%=posCE%>] = '<%=act.getErrorCampo(posCE)%>';
        <%    
            }
        %>
            var estilosFila = new Array();
            for(var cont = 0; cont < camposErrores.length; cont++){
                if(camposErrores[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                    estilosFila[cont] = 'color: red;';
                }else{
                    estilosFila[cont] = 'color: black;';
                }
            }
            
        <%
            if(act.getErrores() != null && act.getErrores().size() > 0)
            {
        %>
            estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = 'color: red;';
            estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
        <%
            }
        %>  
            
        <%
            if(act.esSustituto() != null && act.esSustituto() == true)
            {
        %>
            haySustitutos = true;
            estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
            estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
        <%
            }
        %>     
            
            listaPreparadores_jusTabla_estilos[<%=i%>] = estilosFila;
        
        
        
        listaPreparadores_jusTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>','<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>',  '<%=act.getTipoContrato()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>',             
            '<a href=\'javascript:cargarSeguimientosPrepEca(<%=act.getId() %>);\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>]+'\'><%=act.getNumSegAnteriores()%></a>', 
            '<%=act.getImporte()%>',
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 1,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>]+'\'><%=act.getC1h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 1,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>]+'\'><%=act.getC1m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>]+'\'><%=act.getC1Total()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 2,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>]+'\'><%=act.getC2h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 2,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>]+'\'><%=act.getC2m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 2)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>]+'\'><%=act.getC2Total()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 3,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>]+'\'><%=act.getC3h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 3,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>]+'\'><%=act.getC3m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 3)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>]+'\'><%=act.getC3Total()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 4,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>]+'\'><%=act.getC4h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 4,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>]+'\'><%=act.getC4m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 4)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>]+'\'><%=act.getC4Total()%></a>', 
                        '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 5,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5H%>]+'\'><%=act.getC5h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 5,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5M%>]+'\'><%=act.getC5m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 5)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL%>]+'\'><%=act.getC5Total()%></a>', 
                        '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 6,0)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6H%>]+'\'><%=act.getC6h()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 6,1)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6M%>]+'\'><%=act.getC6m()%></a>', 
            '<a href=\'javascript:cargarInsercionesPrepEca(<%=act.getId() %>, 6)\' style=\''+estilosFila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL%>]+'\'><%=act.getC6Total()%></a>', 
            '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>'];
    
    
        
    <%
            }// for
        }// if
    %>
    
    tabPreparadores_jus = new FixedColumnTable(document.getElementById('listaPreparadores_jus'), 850, 876, 'listaPreparadores_jus');
    tabPreparadores_jus.addColumna('30','center','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
    //if(haySustitutos == true){
        tabPreparadores_jus.addColumna('140','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>');
    //}else{
    //    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>');
    //}
    tabPreparadores_jus.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2")%>');
    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4")%>');
    tabPreparadores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5")%>');
    tabPreparadores_jus.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.preparadores.tabla.coltipoCont")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6")%>');
    tabPreparadores_jus.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8")%>');
    tabPreparadores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9")%>');
    tabPreparadores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col10")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11")%>');
    tabPreparadores_jus.addColumna('130','right','<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12"), anoExpediente)%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col30")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col31")%>');
    tabPreparadores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col32")%>');
    tabPreparadores_jus.addColumna('120','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col33")%>');
    tabPreparadores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col34")%>');
    tabPreparadores_jus.numColumnasFijas = 3;
    
    //tabPreparadores_jus.lineas=listaPreparadores_jusTabla;
     for(var cont = 0; cont < listaPreparadores_jusTabla.length; cont++){
        tabPreparadores_jus.addFilaConFormato(listaPreparadores_jusTabla[cont], listaPreparadores_jusTabla_titulos[cont], listaPreparadores_jusTabla_estilos[cont])
    }
    
    tabPreparadores_jus.height = '300';
    
    tabPreparadores_jus.altoCabecera = 50;
    
    tabPreparadores_jus.dblClkFunction = 'dblClckTablaPreparadoresJustificacion';
    
    tabPreparadores_jus.displayTabla();
    
    tabPreparadores_jus.pack();
    
    
</script>

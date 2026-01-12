<!--%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %-->
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.procesos.FilaAuditoriaProcesosVO" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 0;
 int apl = 5;
 String css = "";
    int codOrganizacion = 0;
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
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
    var msgValidacion = '';
            
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
    
    function inicializar(){
        window.focus();
    }
    
    function ejecutarResolucionProvisionalSolicitudEca(){
        
    }
    
    function generarDocumentacionResolucionSolicitudEca(){
        
    }
    
    function consolidarSolicitudEca(){
        
    }
    
    function deshacerConsolidacionSolicitudEca(){
        
    }
    
    //JAVASCRIPT DE LA TABLA AUDITORIA
    
    var enlacesPaginaE  = 10;
    var lineasPaginaE   = 12;
    var paginaActualE   = 1;
    var paginaInferiorE = 1;
    var paginaSuperiorE = enlacesPaginaE;
    var listaPE = new Array();
    var listaSelE = new Array();
    var listaSelEOriginal = new Array();
    var inicioE = 0;
    var finE    = 0;
    var numRelacionAuditorias = 0;
    var numeroPaginasE=Math.ceil(numRelacionAuditorias /lineasPaginaE);
    if (numeroPaginasE < enlacesPaginaE) 
        paginaSuperiorE= numeroPaginasE;
    
    function cargarComboProcesos(){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=cargarComboProcesos&tipo=0&control='+control.getTime();
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
                var listaProcesos = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var contProcesos = 0;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                         
                    else if(hijos[j].nodeName=="SELECT_ITEM"){
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
                            else if(hijosFila[cont].nodeName=="LABEL"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }
                        }
                        listaProcesos[contProcesos] = fila;
                        contProcesos++;
                        fila = new Array();
                    }  
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    document.getElementById('codProceso').value = '';
                    document.getElementById('descProceso').value = '';
                    comboProcesos = new Combo("Proceso");
                    codProcesos = new Array();
                    descProcesos = new Array();
                    for(var i = 0; i < listaProcesos.length; i++){
                        codProcesos[i] = listaProcesos[i][0];
                        descProcesos[i] = listaProcesos[i][1];
                    }
                    comboProcesos.addItems(codProcesos, descProcesos);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
    }
    
    function mostrarCalFechaDesde(){
        if(window.event) 
        evento = window.event;
        if (document.getElementById("calFechaDesde").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaDesde',null,null,null,'','calFechaDesde','',null,null,null,null,null,null,null,null,evento);
    }
    
    function mostrarCalFechaHasta(){
        if(window.event) 
        evento = window.event;
        if (document.getElementById("calFechaHasta").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaHasta',null,null,null,'','calFechaHasta','',null,null,null,null,null,null,null,null,evento);
    }
    
    function buscar(){
        inicio = false;
        paginaActualE = 1;
        cargaPaginaE(paginaActualE);
    }
    
    function limpiarFiltro(){
        document.getElementById('nomApellidos').value = '';
        document.getElementById('fechaDesde').value = '';
        document.getElementById('fechaHasta').value = '';
        document.getElementById('codProceso').value = '';
        document.getElementById('descProceso').value = '';
    }

    function cargaPaginaE(numeroPaginaE){
        paginaActualE = numeroPaginaE;
        filtrarAuditorias();
        listaSelE = new Array();
        for(var i = 0; i < listaSelEOriginal.length; i++){
            listaSelE[i] = listaSelEOriginal[i];
        }

        inicializaLista(numeroPaginaE);
    }
    
    function inicializaLista(numeroPaginaE){
        tableObject=tabAud;
        var j = 0;
        var jE = 0;

        paginaActualE = numeroPaginaE;
        listaPE = new Array();

        inicioE =0;
        finE = lineasPaginaE;
        listaPE = listaSelE;

        tabAud.lineas=listaPE;
        refrescaExpedientes();
        domlay('enlaceAud',1,0,0,enlacesAud());

      }
      
      

      function enlacesAud() {
        var htmlString = " ";
        numeroPaginasE = Math.ceil(numRelacionAuditorias /lineasPaginaE);
        /*if (numeroPaginasE < enlacesPaginaE) 
            paginaSuperiorE= numeroPaginasE;*/

      if (numeroPaginasE > 1) {
        htmlString += '<table class="fondoNavegacion" cellpadding="2" cellspacing="0" align="center"><tr>'
        if (paginaActualE > 1) {
          if(paginaInferiorE > enlacesPaginaE){
            htmlString += '<td width="35" class="botonNavegacion">';
            htmlString += '<a href="javascript:irPrimeraPaginaE();" class="linkNavegacion" target="_self">';
            htmlString += ' |<< ';
            htmlString += '</a></td>';
            htmlString += '<td width="5"></td>';
            htmlString += '<td width="35" class="botonNavegacion">';
            htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasAnterioresE('+ eval(paginaActualE) + ')" target="_self">';
            htmlString += ' << ';
            htmlString += '</a></td>';
          } else htmlString += '<td width="75">&nbsp;</td>';
        }else htmlString += '<td width="75">&nbsp;</td>';
        htmlString += '</td><td align="center" width="400">';

        for(var i=paginaInferiorE-1; i < paginaSuperiorE; i++){
            if ((i+1) == paginaActualE)
              htmlString += '<span class="indiceNavSelected">'+ (i+1) + '</span>&nbsp;&nbsp;';
            else
              htmlString += '<a class="indiceNavegacion" href="javascript:cargaPaginaE('+ eval(i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
        }

        if (paginaSuperiorE < numeroPaginasE){
          htmlString += '</td><td width="35" class="botonNavegacion">';
          htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasSiguientesE('+ eval(eval(paginaActualE))+ ')" target="_self">';
          htmlString += ' >> ';
          htmlString += '</a></td>';
          htmlString += '<td width="5"></td>';
          htmlString += '<td width="35" class="botonNavegacion">';
          htmlString += '<a href="javascript:irUltimaPaginaE();" class="linkNavegacion" target="_self">';
          htmlString += ' >>| ';
          htmlString += '</a></td>';
        } else htmlString += '</td><td width="70"></td>';
        htmlString += '</tr></table>';
      }

      var registroInferiorE = ((paginaActualE - 1) * lineasPaginaE) + 1;
      var registroSuperiorE = (paginaActualE * lineasPaginaE);
      if (paginaActualE == numeroPaginasE)
        registroSuperiorE = numRelacionAuditorias;
      if (listaSelE.length > 0)
        htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferiorE + '&nbsp;a&nbsp;' + registroSuperiorE + '&nbsp;de&nbsp;' + numRelacionAuditorias + '&nbsp;encontrados.</font></center>'
      else
        htmlString += '<center><font class="textoSuelto">&nbsp;' + numRelacionAuditorias  + '&nbsp;encontrados.</font></center>'

      return (htmlString);
    }
    
    function filtrarAuditorias(){
        if(validarDatosFiltro()){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            var nomApellidos = document.getElementById('nomApellidos').value;
            var feDesde = document.getElementById('fechaDesde').value;
            var feHasta = document.getElementById('fechaHasta').value;
            var codProc = document.getElementById('codProceso').value;
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=filtrarAuditoriaProcesos&tipo=0&nomApellidos='+nomApellidos
                +'&feDesde='+feDesde+'&feHasta='+feHasta+'&codProc='+codProc+'&pagAct='+paginaActualE+'&maxFilas='+lineasPaginaE+'&control='+control.getTime();
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
                var listaRegistros = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var contFilas = 0;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                    else if(hijos[j].nodeName=="TOTAL_REGISTROS"){
                        try{
                            numRelacionAuditorias = parseInt(hijos[j].childNodes[0].nodeValue);
                        }catch(err){

                        }
                    }
                    else if(hijos[j].nodeName=="REGISTRO"){
                        nodoFila = hijos[j];
                        hijosFila = nodoFila.childNodes;
                        for(var cont = 0; cont < hijosFila.length; cont++){
                            if(hijosFila[cont].nodeName=="NOMAPELLIDOS"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="PROCESO"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="FECHA"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[2] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="RESULTADO"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[3] = '-';
                                }
                            }
                        }
                        listaRegistros[contFilas] = fila;
                        contFilas++;
                        fila = new Array();
                    }       
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    listaSelEOriginal = new Array();
                    for(var i = 0; i < listaRegistros.length; i++){
                        listaSelEOriginal[i] = listaRegistros[i];
                    }


                    if(inicio == false){
                        paginaSuperiorE = enlacesPaginaE;
                        var np = Math.ceil(numRelacionAuditorias /lineasPaginaE);
                        if(paginaSuperiorE > np)
                            paginaSuperiorE = np;
                        inicio = true;
                    }

                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
    }
    

    function calcularLimitesE(numeroPaginaE) {
      if(numeroPaginaE > paginaInferiorE +(enlacesPaginaE-1)) {
        paginaInferiorE = numeroPaginaE;
      }
      var enlacePaginaE = Math.ceil(numeroPaginaE/enlacesPaginaE);
      var ultimaPantalla = Math.ceil(numeroPaginasE/enlacesPaginaE);
      var valorMaxPaginaE = 0;
      if(enlacePaginaE == 0) valorMaxPaginaE = enlacesPaginaE;
      else valorMaxPaginaE = enlacePaginaE * enlacesPaginaE;
      if(numeroPaginasE < valorMaxPaginaE && enlacePaginaE == ultimaPantalla) paginaSuperiorE = numeroPaginasE;
      else paginaSuperiorE = (numeroPaginaE-1) + enlacesPaginaE;
    }

    function irNPaginasAnterioresE(pagActualE){
      var incremento = enlacesPaginaE + (pagActualE - paginaInferiorE) ;
        if (paginaInferiorE-1 <= 0)
                    pagActualE = 1;
            else  pagActualE -= incremento;
      paginaInferiorE = pagActualE;
      paginaSuperiorE = paginaInferiorE + enlacesPaginaE-1;

      calcularLimitesE(pagActualE);
      cargaPaginaE(pagActualE);
    }

    function irNPaginasSiguientesE(pagActualE){
      pagActualE = parseInt(pagActualE);
      var incremento = paginaSuperiorE +1 - pagActualE;
      if (pagActualE + incremento > numeroPaginasE)
          pagActualE = Math.ceil(numRelacionAuditorias/lineasPaginaE); // Ultima
      else {
        pagActualE +=  incremento;
        pagInferiorE = pagActualE;
        if (paginaInferiorE + enlacesPaginaE > numeroPaginasE)
            paginaSuperiorE=numeroPaginasE;
        else paginaSuperiorE=paginaInferiorE+enlacesPaginaE-1;
      }

      calcularLimitesE(pagActualE);
      cargaPaginaE(pagActualE);
    }

    function irUltimaPaginaE() {
      paginaActualE   = Math.ceil(numRelacionAuditorias/lineasPaginaE);
      paginaInferiorE = 1;
      if (numeroPaginasE <= enlacesPaginaE)
          paginaSuperiorE = numeroPaginasE;
      else {
        paginaSuperiorE = enlacesPaginaE;
        while (paginaActualE > paginaSuperiorE) {
          paginaInferiorE = paginaSuperiorE +1;
          if (numeroPaginasE > paginaInferiorE-1+enlacesPaginaE)
            paginaSuperiorE = paginaInferiorE-1+enlacesPaginaE;
          else paginaSuperiorE = numeroPaginasE;
         }
      }
      cargaPaginaE(paginaActualE)
    }

    function irPrimeraPaginaE() {
      paginaActualE   = 1;
      paginaInferiorE = 1;
      if (numeroPaginasE <= enlacesPaginaE)
          paginaSuperiorE = numeroPaginasE;
        else paginaSuperiorE = enlacesPaginaE;
      cargaPaginaE(paginaActualE)
    }
    
    function validarDatosFiltro(){
        var txtDesde = document.getElementById('fechaDesde').value;
        var txtHasta = document.getElementById('fechaHasta').value;
        
        var hayFechaDesde = false;
        var hayFechaHasta = false;
        if(txtDesde != null && txtDesde != ''){
            if(!ValidarFechaConFormato(document.forms[0],document.getElementById('fechaDesde'))){
                jsp_alerta("A","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaDesdeIncorrecto")%>");
                return false;
            }else{
                hayFechaDesde = true;
            }
        }
        
        if(txtHasta != null && txtHasta != ''){
            if(!ValidarFechaConFormato(document.forms[0],document.getElementById('fechaHasta'))){
                jsp_alerta("A","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaHastaIncorrecto")%>");
                return false;
            }else{
                hayFechaHasta = true;
            }
        }
        
        var correcto = true;
        if(hayFechaDesde && hayFechaHasta){
            var array_fecha_desde = txtDesde.split("/");
            var array_fecha_hasta = txtHasta.split("/");
            var dia_desde = array_fecha_desde[0];
            var mes_desde = array_fecha_desde[1];
            var ano_desde = array_fecha_desde[2];
            var dia_hasta = array_fecha_hasta[0];
            var mes_hasta = array_fecha_hasta[1];
            var ano_hasta = array_fecha_hasta[2];
            
            var desde = new Date(ano_desde, mes_desde-1, dia_desde, 0, 0, 0, 0);
            var hasta = new Date(ano_hasta, mes_hasta-1, dia_hasta, 0, 0, 0, 0);
            var n1 = desde.getTime();
            var n2 = hasta.getTime();
            var result = n2 - n1;
            if(result < 0){
                jsp_alerta("A","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaDesdeAnteriorHasta")%>");
                correcto = false;
            }
        }
        return correcto;
    }
    
    function solicitarAno(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,200,'no','no',function(){					
					});
        }else{
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,250,'no','no',function(){					
					});
        }
        return result;
    }
    
    function solicitarAnoYFormato(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAnoYFormato.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,200,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAnoYFormato.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,250,'no','no');
        }
        return result;
    }
    
    
    
    function ejecutarListadoDesglose(){  
        var control = new Date();      
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAnoYFormato.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,200,'no','no', function(datos){
                if(datos != null && datos != undefined && datos.length == 2){
                var ano = datos[0];
                var formato = datos[1];
                    if(ano != null && ano != undefined && ano != ''){
                        if(formato != null && formato != undefined && formato != ''){
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = '?tarea=preparar&modulo=MELANBIDE35&operacion=generarInformeDesglose&tipo=0&ano='+ano+'&formato='+formato+'&control='+control.getTime();
                            window.open(url+parametros, "_blank");
                        }
                     }
                }
            });
        inicio = false;
        cargaPaginaE(paginaActualE);
    }
    
    function ejecutarListadoProyectos(){  
        var control = new Date();      
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide35/procesos/solicitarAnoYFormato.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),120,200,'no','no', function(datos){
                if(datos != null && datos != undefined && datos.length == 2){
                var ano = datos[0];
                var formato = datos[1];
                    if(ano != null && ano != undefined && ano != ''){
                        if(formato != null && formato != undefined && formato != ''){
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = '?tarea=preparar&modulo=MELANBIDE35&operacion=generarInformeProyectos&tipo=0&ano='+ano+'&formato='+formato+'&control='+control.getTime();
                            window.open(url+parametros, "_blank");
                        }
                     }
                }
            });
        inicio = false;
        cargaPaginaE(paginaActualE);
    }
</script>
<body class="bandaBody" onload="inicializar();">
    <form id="formProcesos">
        <div class="tab-page" id="tabPage323" style="height:550px; width: 100%;">
            <table width="100%" style="height:100px;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "tit_procesos")%></td>
                </tr>
                <tr>
                    <td class="separadorTituloPantalla"></td>
                </tr>
                <tr>
                    <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                        <div id="contenidoProc" class="cuadroFondoBlanco" style="width:970px; height: 550px; overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                            <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                <legend class="legendAzul"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.legend.solicitudEca")%></legend>
                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.RPSE")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnRPSE" name="btnRPSE" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarResolucionProvisionalSolicitudEca();">
                                        </div>
                                    </div>
                                </div>

                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.DRSE")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnDRSE" name="btnDRSE" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="generarDocumentacionResolucionSolicitudEca();">
                                        </div>
                                    </div>
                                </div>

                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.CSE")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnCSE" name="btnCSE" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="consolidarSolicitudEca();">
                                        </div>
                                    </div>
                                </div>

                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.DCSE")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnDCSE" name="btnDCSE" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="deshacerConsolidacionSolicitudEca();">
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                            <div style="clear: both;"></div>
                            <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                <legend class="legendAzul"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.legend.otros")%></legend>
                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.LD")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnLD" name="btnLD" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="ejecutarListadoDesglose();">
                                        </div>
                                    </div>
                                </div>
                                <div class="lineaFormularioFont">
                                    <div style="width: 300px; float: left; text-align: left;">
                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.label.LP")%>
                                    </div>
                                    <div style="width: 100px; float: left;">
                                        <div style="float: left;">
                                            <input type="button" id="btnLP" name="btnLP" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="ejecutarListadoProyectos();">
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                                <div style="clear: both; float: left; width: 100%; padding-top: 10px;">
                                    <fieldset style="width: 97%; float: left; padding-top: 10px;">
                                        <legend class="legendAzul"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.legend.auditoriaProcesos")%></legend>
                                        <div style="clear: both; float: left; width: 100%;">
                                            <table style="width: 100%; font-size: 12px;">
                                                <tr>
                                                    <td>
                                                        <b><u><%=meLanbide35I18n.getMensaje(idiomaUsuario,"proc.label.criteriosBusq")%></u></b>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"proc.label.nomApellidos")%>
                                                        <input id="nomApellidos" name="nomApellidos" type="text" class="inputTexto" size="52" maxlength="40" />
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"proc.label.fDesde")%>
                                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaDesde" name="fechaDesde" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" />
                                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaDesde(event);return false;" style="text-decoration:none; padding-right: 10px;" >
                                                            <IMG style="border: 0" height="17" id="calFechaDesde" name="calFechaDesde" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                                        </A>
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"proc.label.fHasta")%>
                                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaHasta" name="fechaHasta" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" />
                                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaHasta(event);return false;" style="text-decoration:none; padding-right: 10px;" >
                                                            <IMG style="border: 0" height="17" id="calFechaHasta" name="calFechaHasta" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                                        </A>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"proc.label.proceso")%>
                                                        <input id="codProceso" name="codProceso" type="text" class="inputTexto" size="6" maxlength="2" 
                                                               onkeypress="javascript:return SoloDigitosConsulta(event);" >
                                                        <input id="descProceso" name="descProceso" type="text" class="inputTexto" size="53" readonly>
                                                        <a id="anchorProceso" name="anchorProceso" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProceso" name="botonProceso" style="cursor:hand;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div class="botonera">
                                                            <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.buscar")%>" onclick="buscar();">
                                                            <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "proc.btn.limpiar")%>" onclick="limpiarFiltro();">
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div style="clear: both; float: left; width: 100%;">
                                            <table style="width: 100%;">
                                                <tr>
                                                    <td align="left" id="tablaAuditoriaProcesosEca"/>
                                                </tr>
                                            </table>
                                        </div>
                                        <div id="enlaceAud" STYLE="width:100%; clear: both;">
                                        </div>
                                    </fieldset>
                                </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div id="popupcalendar" class="text"></div>
    </form>
</body>
<script type="text/javascript">
// JAVASCRIPT DE LA TABLA AUDITORIA
   
   
    var comboProcesos = new Combo("Proceso");
    var codProcesos = new Array();
    var descProcesos = new Array();

    var tabAud = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAuditoriaProcesosEca'), 930);
    //TOTAL ANCHO: 928
    tabAud.addColumna('245','left','<%= meLanbide35I18n.getMensaje(idiomaUsuario, "proc.auditoria.nombre")%>');
    tabAud.addColumna('250','left','<%= meLanbide35I18n.getMensaje(idiomaUsuario, "proc.auditoria.proceso")%>');
    tabAud.addColumna('165','center','<%= meLanbide35I18n.getMensaje(idiomaUsuario, "proc.auditoria.fecha")%>');
    tabAud.addColumna('244','center','<%= meLanbide35I18n.getMensaje(idiomaUsuario, "proc.auditoria.resultado")%>');
    tabAud.displayCabecera=true;
    tabAud.height = 125;

    var tableObject=tabAud;
    var inicio = false;
    
    cargarComboProcesos();
    
    cargaPaginaE(1);

    function refrescaExpedientes() {
      tabAud.displayTabla();
    }

    tabAud.displayDatos = pintaDatosExpedientes;

    function pintaDatosExpedientes() {
      tableObject = tabAud;
    }
</script>

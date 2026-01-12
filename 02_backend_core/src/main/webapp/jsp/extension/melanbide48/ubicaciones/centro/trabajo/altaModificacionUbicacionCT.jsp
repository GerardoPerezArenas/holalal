<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCentrosVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicacionesCTVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaColecCentrosVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

        <%
            int idiomaUsuario = 1;
            int codOrganizacion  = 0;

            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion = usuario.getOrgCod();
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
            String numExpediente    = request.getParameter("numero");
            Integer ejercicio    = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            String codProcedimiento    = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
            String tituloPagina = meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.colectivo.nuevoCentro.tituloPagina.nuevoCentro");
            String colectivo = (String)request.getAttribute("colectivo") != null ? (String)request.getAttribute("colectivo") : "";
            String modoDatos = (String)request.getAttribute("modoDatos");
            MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
            String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
            String idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? String.valueOf(convocatoriaActiva.getId()) : "");
   
            
            //LISTAS PARA LOS COMBOS
            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            List<SelectItem> listaColectivos = new ArrayList<SelectItem>();
            List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
            List<SelectItem> listaComarcas = new ArrayList<SelectItem>();
            List<SelectItem> listaMunicipios = new ArrayList<SelectItem>();
            ColecUbicacionesCTVO ubicacionModif = new ColecUbicacionesCTVO();
            
            String lcodEntidades = "";
            String ldescEntidades = "";
            String lcodColectivos = "";
            String ldescColectivos = "";
            String lcodAmbitos = "";
            String ldescAmbitos = "";
            String lcodComarcas = "";
            String ldescComarcas = "";
            String lcodMunicipios = "";
            String ldescMunicipios = "";
            
            if(request.getAttribute("ubicacionModif") != null)
                ubicacionModif = (ColecUbicacionesCTVO)request.getAttribute("ubicacionModif");
            String vieneDatosUbicaRequest = ubicacionModif!=null?"1":"0";
            
            if(request.getAttribute("listaEntidades") != null)
                listaEntidades = (List<SelectItem>)request.getAttribute("listaEntidades");

            if(request.getAttribute("listaColectivos") != null)
                listaColectivos = (List<SelectItem>)request.getAttribute("listaColectivos");
            
            if(request.getAttribute("listaAmbitos") != null)
                listaAmbitos = (List<SelectItem>)request.getAttribute("listaAmbitos");
            
            if(request.getAttribute("listaComarcas") != null)
                listaComarcas = (List<SelectItem>)request.getAttribute("listaComarcas");
            
            if(request.getAttribute("listaMunicipios") != null)
                listaMunicipios = (List<SelectItem>)request.getAttribute("listaMunicipios");
            
            //Entidades
            if (listaEntidades != null && listaEntidades.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaEntidades.size() - 1; i++) 
                {
                    si = (SelectItem) listaEntidades.get(i);
                    lcodEntidades += "\"" + si.getCodigo() + "\",";
                    ldescEntidades += "\"" + escape(si.getDescripcion()) + "\",";
                }
                si = (SelectItem) listaEntidades.get(i);
                lcodEntidades += "\"" + si.getCodigo() + "\"";
                ldescEntidades += "\"" + escape(si.getDescripcion()) + "\"";
            }
            // Colectivos
            if (listaColectivos != null && listaColectivos.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaColectivos.size() - 1; i++) 
                {
                    si = (SelectItem) listaColectivos.get(i);
                    lcodColectivos += "\"" + si.getCodigo() + "\",";
                    ldescColectivos += "\"" + escape(si.getDescripcion()) + "\",";
                }
                si = (SelectItem) listaColectivos.get(i);
                lcodColectivos += "\"" + si.getCodigo() + "\"";
                ldescColectivos += "\"" + escape(si.getDescripcion()) + "\"";
            }
            
            // Ambitos
            if (listaAmbitos != null && listaAmbitos.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaAmbitos.size() - 1; i++) 
                {
                    si = (SelectItem) listaAmbitos.get(i);
                    lcodAmbitos += "\"" + si.getCodigo() + "\",";
                    ldescAmbitos += "\"" + escape(si.getDescripcion()) + "\",";
                }
                si = (SelectItem) listaAmbitos.get(i);
                lcodAmbitos += "\"" + si.getCodigo() + "\"";
                ldescAmbitos += "\"" + escape(si.getDescripcion()) + "\"";
            }
            
            // Comarcas
            if (listaComarcas != null && listaComarcas.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaComarcas.size() - 1; i++) 
                {
                    si = (SelectItem) listaComarcas.get(i);
                    lcodComarcas += "\"" + si.getCodigo() + "\",";
                    ldescComarcas += "\"" + escape(si.getDescripcion()) + "\",";
                }
                si = (SelectItem) listaComarcas.get(i);
                lcodComarcas += "\"" + si.getCodigo() + "\"";
                ldescComarcas += "\"" + escape(si.getDescripcion()) + "\"";
            }

            //Municipios
            if (listaMunicipios != null && listaMunicipios.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaMunicipios.size() - 1; i++) 
                {
                    si = (SelectItem) listaMunicipios.get(i);
                    lcodMunicipios += "\"" + si.getCodigo() + "\",";
                    ldescMunicipios += "\"" + escape(si.getDescripcion()) + "\",";
                }
                si = (SelectItem) listaMunicipios.get(i);
                lcodMunicipios += "\"" + si.getCodigo() + "\"";
                ldescMunicipios += "\"" + escape(si.getDescripcion()) + "\"";
            }

            List<FilaColecCentrosVO> centros = (List<FilaColecCentrosVO>)request.getAttribute("listaCentros");
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide48/melanbide48.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionUbicacionesCT.js"></script>
        
        <script type="text/javascript">
            var anio = <%=ejercicio%>;
            var idiomaUsuarioJS = <%=idiomaUsuario%>;
            var mensajeValidacion = '';
            var mensajeVisible = false;
            var nuevo = true;
            var repetidos = false;
            
            var codEntidades = [<%=lcodEntidades%>];
            var descEntidades = [<%=ldescEntidades%>];
            
            var codColectivos = [<%=lcodColectivos%>];
            var descColectivos = [<%=ldescColectivos%>];
            
            var codAmbitos = [<%=lcodAmbitos%>];
            var descAmbitos = [<%=ldescAmbitos%>];
            
            var codComarcas = [<%=lcodComarcas%>];
            var descComarcas = [<%=ldescComarcas%>];
            
            var codMunicipios = [<%=lcodMunicipios%>];
            var descMunicipios = [<%=ldescMunicipios%>];
            
    
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
                cargarCombos();
                cargarCodigosCombos();
                if("1"=='<%=modoDatos%>' && "1"=='<%=vieneDatosUbicaRequest%>'){
                    // Cargamos los items para que no de error de codigo no existe al pulsar cuando esta relleno
                    comboComarca.addItems(codComarcas,descComarcas);
                    comboMunicipio.addItems(codMunicipios,descMunicipios);
                }
                cargarDescripcionesCombos();
                //Deshabilitar Ambito si es Colectivo 1/2
                if("CONV_ANTE-2021"!==$("#codigoConvocatoriaExpediente").val()){
                    var codColectivo=document.getElementById('codColectivo').value;
                    if((codColectivo==1 || codColectivo==2) && document.getElementById('codComarca').value!='' && document.getElementById('descComarca').value!=''){
                        document.getElementById('codComarca').disabled = true;
                        document.getElementById('descComarca').disabled = true;
                        document.getElementById('anchorComarca').style.display = "none";
                    }else{
                        document.getElementById('codComarca').disabled = false;
                        document.getElementById('descComarca').disabled = false;
                        document.getElementById('anchorComarca').style.display = "inline";
                    }
                }                
            }
            
            function cargarCombos() {
                comboEntidad.addItems(codEntidades, descEntidades);
                comboColectivo.addItems(codColectivos, descColectivos);
                comboAmbito.addItems(codAmbitos, descAmbitos);
            }
            
            function cargarCodigosCombos(){
                document.getElementById('codEntidad').value = '<%=ubicacionModif != null && ubicacionModif.getCodEntidad()!=null ? ubicacionModif.getCodEntidad() : ""%>';
                document.getElementById('codColectivo').value = '<%=ubicacionModif != null && ubicacionModif.getCodTipoColectivo()!=null ? ubicacionModif.getCodTipoColectivo() : ""%>';
                document.getElementById('codAmbito').value = '<%=ubicacionModif != null && ubicacionModif.getTerritorioHist() !=null? ubicacionModif.getTerritorioHist() : ""%>';
                if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
                    document.getElementById('codComarca').value = '<%=ubicacionModif != null && ubicacionModif.getComarca() !=null ? ubicacionModif.getComarca() : ""%>';
                }else{
                    document.getElementById('codComarca').value = '<%=ubicacionModif != null && ubicacionModif.getFkIdAmbitoSolicitado() !=null ? ubicacionModif.getFkIdAmbitoSolicitado() : ""%>';
                }
                document.getElementById('codMunicipio').value = '<%=ubicacionModif != null && ubicacionModif.getMunicipio()!=null ? ubicacionModif.getMunicipio() : ""%>';
            }
            
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                
                //Entidad
                codigo = "<%=ubicacionModif != null && ubicacionModif.getCodEntidad()!=null ? ubicacionModif.getCodEntidad() : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codEntidades.length; i++)
                    {
                        codAct = codEntidades[i];
                        if(codAct == codigo)
                        {
                            desc = descEntidades[i];
                        }
                    }
                }
                document.getElementById('descEntidad').value = desc;
                //Colectivo
                codigo = "<%=ubicacionModif != null && ubicacionModif.getCodTipoColectivo()!=null ? ubicacionModif.getCodTipoColectivo() : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codColectivos.length; i++)
                    {
                        codAct = codColectivos[i];
                        if(codAct == codigo)
                        {
                            desc = descColectivos[i];
                        }
                    }
                }
                document.getElementById('descColectivo').value = desc;
                //Ambito
                codigo = "<%=ubicacionModif != null && ubicacionModif.getTerritorioHist()!=null ? ubicacionModif.getTerritorioHist() : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codAmbitos.length; i++)
                    {
                        codAct = codAmbitos[i];
                        if(codAct == codigo)
                        {
                            desc = descAmbitos[i];
                        }
                    }
                }
                document.getElementById('descAmbito').value = desc;
                //Comarca
                if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
                    codigo = "<%=ubicacionModif != null && ubicacionModif.getComarca()!=null ? ubicacionModif.getComarca() : ""%>";
                }else{
                    codigo = "<%=ubicacionModif != null && ubicacionModif.getFkIdAmbitoSolicitado()!=null ? ubicacionModif.getFkIdAmbitoSolicitado() : ""%>";
                }
                
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codComarcas.length; i++)
                    {
                        codAct = codComarcas[i];
                        if(codAct == codigo)
                        {
                            desc = descComarcas[i];
                        }
                    }
                }
                document.getElementById('descComarca').value = desc;
                //Municipio
               codigo = "<%=ubicacionModif != null && ubicacionModif.getMunicipio()!=null  ? ubicacionModif.getMunicipio() : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codMunicipios.length; i++)
                    {
                        codAct = codMunicipios[i];
                        if(codAct == codigo)
                        {
                            desc = descMunicipios[i];
                        }
                    }
                }
                document.getElementById('descMunicipio').value = desc;
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
            
            function guardarDatosUbicacionCentTra(){
                      if(validarDatos()){
                        //TODO
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var idTray = '<%=ubicacionModif != null && ubicacionModif.getCodId()!=null ? ubicacionModif.getCodId() : ""%>';
                        parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarUbicacionCT&tipo=0&numero=<%=numExpediente%>'
                                    +'&idTray='+idTray
                                    +'&codEntidad='+document.getElementById('codEntidad').value
                                    +'&codColectivo='+document.getElementById('codColectivo').value
                                    +'&codAmbito='+document.getElementById('codAmbito').value
                                    +'&codComarca='+document.getElementById('codComarca').value
                                    +'&codMunicipio='+document.getElementById('codMunicipio').value
                                    +'&direccion='+escape(document.getElementById('direccionCT').value)
                                    +'&codigoPostal='+document.getElementById('codigoPostalCT').value
                                    +'&telefono='+document.getElementById('telefonoCT').value
                                    +'&localPrevAprobado='+(document.getElementById('localPrevAprobado')!=null && document.getElementById('localPrevAprobado').checked ? 1 : 0)
                                    +'&disponeEspacioComplWifi='+(document.getElementById('disponeEspacioComplWifi')!=null && document.getElementById('disponeEspacioComplWifi').checked ? 1 : 0)
                                    +'&mantieneRequLocalApro='+(document.getElementById('mantieneRequLocalApro')!=null && document.getElementById('mantieneRequLocalApro').checked ? 1 
                                                                                : document.getElementById('localPrevAprobado')!=null && document.getElementById('localPrevAprobado').checked ? 0 : "")
                            ;
                            parametros += '&control='+control.getTime();
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");       
                            //var formData = new FormData(document.getElementById('formContrato'));
                            ajax.send(parametros);
                            if (ajax.readyState==4 && ajax.status==200){
                                var xmlDoc = null;
                                if((navigator.appName.indexOf("Internet Explorer")!=-1)){
                                    // En IE el XML viene en responseText y no en la propiedad responseXML
                                    var text = ajax.responseText;
                                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async="false";
                                    xmlDoc.loadXML(text);
                                }else{
                                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    if(ajax.responseXML!=null){
                                        xmlDoc = ajax.responseXML;
                                    }else if(ajax.responseText!=null){
                                            var text = ajax.responseText;
                                            xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                            xmlDoc.async="false";
                                            xmlDoc.loadXML(text);
                                    }
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var centros = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;

                            var nodoCentro;
                            var hijosCentro;
                            var contCentros = 0;

                            for(var j=0;hijos!=null && j<hijos.length;j++){
                                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    centros[j]=codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                                else if(hijos[j].nodeName=="UBICACIONES"){
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for(var cont = 0; cont < hijosFila.length; cont++){
                                        if(hijosFila[cont].nodeName=="UBICACION"){
                                            nodoCentro = hijosFila[cont];
                                            hijosCentro = nodoCentro.childNodes;
                                            for(var cont2 = 0; cont2 < hijosCentro.length; cont2++){
                                                if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_COD"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[0] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[0] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_NUMEXP"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[1] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[1] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_TIPO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[2] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[2] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_CODENTIDAD"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[3] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[3] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_TERRITORIO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[4] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[4] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_NROCOMARCA"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[5] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[5] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_MUNICIPIO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[6] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[6] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_LOCALIDAD"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[7] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[7] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_DIRECCION"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[8] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[8] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_PORTAL_DIR"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[9] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[9] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_PISO_DIR"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[10] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[10] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_LETRA_DIR"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[11] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[11] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_CODPOSTAL"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[12] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[12] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_TELEFONO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[13] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[13] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_ENT_CIF"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[14] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[14] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="COLEC_ENT_NOMBRE"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[15] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[15] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="DESC_TERRITORIO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[16] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[16] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="DESC_COMARCA"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[17] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[17] = '-';
                                                    }
                                                }else if(hijosCentro[cont2].nodeName=="DESC_MUNICPIO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[18] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[18] = '-';
                                                    }
                                                }
                                                else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_LOCALPREVAPRO"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[19] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[19] = '-';
                                                    }
                                                }
                                                else if(hijosCentro[cont2].nodeName=="COLEC_UBIC_CT_MATENREQ_LPA"){
                                                    if(hijosCentro[cont2].childNodes.length > 0){
                                                        fila[20] = hijosCentro[cont2].childNodes[0].nodeValue;
                                                    }
                                                    else{
                                                        fila[20] = '-';
                                                    }
                                                }
                                            }
                                            centros[contCentros+j] = fila;
                                            contCentros++;
                                            fila = new Array();
                                        }
                                    }
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if(codigoOperacion=="0"){
                                self.parent.opener.retornoXanelaAuxiliar(centros);
                                jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                                cerrarVentana();
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        }catch(err){

                        }
                    }else{
                        jsp_alerta("A", mensajeValidacion);
                    }
            }
            
            function validarDatos(){
                //TODO
                var retorno = true;
                if((document.getElementById('codEntidad').value==null || document.getElementById('codEntidad').value=='')
                   ||(document.getElementById('codColectivo').value==null || document.getElementById('codColectivo').value=='')
                   ||(document.getElementById('codAmbito').value==null || document.getElementById('codAmbito').value=='')
                   ||(document.getElementById('codComarca').value==null || document.getElementById('codComarca').value=='')
                   ||(document.getElementById('codMunicipio').value==null || document.getElementById('codMunicipio').value=='')
                   ||(document.getElementById('direccionCT').value==null || document.getElementById('direccionCT').value=='')
                   ||(document.getElementById('codigoPostalCT').value==null || document.getElementById('codigoPostalCT').value=='')
                   ||(document.getElementById('telefonoCT').value==null || document.getElementById('telefonoCT').value=='')
                )
                {
                    mensajeValidacion='<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
                    return false;
                }else{
                    if(document.getElementById('direccionCT').value!=null && document.getElementById('direccionCT').value!=''){
                        retorno=comprobarDireccion(document.getElementById('direccionCT').value)
                    }
                }
                return retorno;
            }
          
            function cargarMunicipiosPorComarca(){
                var comarcaCod = document.getElementById('codComarca').value;
//                if(comarcaCod != ''){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=cargarMunicipiosPorComarca&tipo=0&numero=<%=numExpediente%>&codComarca='+comarcaCod+'&codColectivo='+document.getElementById('codColectivo').value+'&control='+control.getTime()
                                +'&codProcedimiento='+$("#codigoProcedimiento").val()+'&codOrganizacion='+$("#codigoOrganizacion").val()
                                +'&territorioHistorico='+$("#codAmbito").val()
                                ;
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
                        var listaMunicipios = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var contMunicipios= 0;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){  
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="MUNICIPIO"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="CODIGO"){
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
                                }
                                listaMunicipios[contMunicipios] = fila;
                                contMunicipios++;
                                fila = new Array();
                            }     
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            comboMunicipio= new Combo("Municipio");
                            codMunicipios = new Array();
                            descMunicipios = new Array();
                            for(var i = 0; i < listaMunicipios.length; i++){
                                codMunicipios[i] = listaMunicipios[i][0];
                                descMunicipios[i] = listaMunicipios[i][1];
                            }
                            comboMunicipio.addItems(codMunicipios, descMunicipios);
                            document.getElementById('codMunicipio').disabled = false;
                            document.getElementById('codMunicipio').value = '';
                            document.getElementById('descMunicipio').disabled = false;
                            document.getElementById('descMunicipio').value = '';
                            document.getElementById('anchorMunicipio').style.display = "inline";
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
//                }else{
//                    document.getElementById('codMunicipio').disabled = false;
//                            document.getElementById('codMunicipio').value = '';
//                            document.getElementById('descMunicipio').disabled = false;
//                            document.getElementById('descMunicipio').value = '';
//                            document.getElementById('anchorMunicipio').style.display = "none";
//                }
            }
            function cargarComarcasPorAmbito(){
                var codAmbito = document.getElementById('codAmbito').value;
                var codColectivo = document.getElementById('codColectivo').value;
                if(codAmbito != '' && codColectivo!=""){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
                        parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=cargarComarcasPorAmbito&tipo=0&numero=<%=numExpediente%>&codAmbito='+codAmbito+'&control='+control.getTime()
                        +'&codProcedimiento='+$("#codigoProcedimiento").val()+'&codOrganizacion='+$("#codigoOrganizacion").val();
                    }else{
                        parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=cargarComarcasPorAmbito&tipo=0&numero=<%=numExpediente%>&codAmbito='+codAmbito+'&control='+control.getTime()
                        +'&codProcedimiento='+$("#codigoProcedimiento").val()+'&codOrganizacion='+$("#codigoOrganizacion").val()
                        +'&idBDConvocatoriaExpediente='+$("#idBDConvocatoriaExpediente").val()+'&codigoConvocatoriaExpediente='+$("#codigoConvocatoriaExpediente").val()+'&codColectivo='+$("#codColectivo").val()
                        ;
                    }
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
                        var listaComarcas = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var contComarcas = 0;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){  
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="COMARCA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="CODIGO"){
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
                                }
                                listaComarcas[contComarcas] = fila;
                                contComarcas++;
                                fila = new Array();
                            }     
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            comboComarca = new Combo("Comarca");
                            comboComarca.change = cargarMunicipiosPorComarca;
                            codComarcas = new Array();
                            descComarcas = new Array();
                            for(var i = 0; i < listaComarcas.length; i++){
                                codComarcas[i] = listaComarcas[i][0];
                                descComarcas[i] = listaComarcas[i][1];
                            }
                            comboComarca.addItems(codComarcas, descComarcas);
                            if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
                                document.getElementById('codComarca').disabled = false;
                                document.getElementById('codComarca').value = '';
                                document.getElementById('descComarca').disabled = false;
                                document.getElementById('descComarca').value = '';
                                document.getElementById('anchorComarca').style.display = "inline";
                            }else{
                                if((codColectivo==1 || codColectivo==2) && (codComarcas.length===1 && descComarcas.length===1)){
                                    document.getElementById('codComarca').disabled = true;
                                    document.getElementById('codComarca').value = codComarcas[0];
                                    document.getElementById('descComarca').disabled = true;
                                    document.getElementById('descComarca').value = descComarcas[0];
                                    document.getElementById('anchorComarca').style.display = "none";
                                }else{
                                    document.getElementById('codComarca').disabled = false;
                                    document.getElementById('codComarca').value = '';
                                    document.getElementById('descComarca').disabled = false;
                                    document.getElementById('descComarca').value = '';
                                    document.getElementById('anchorComarca').style.display = "inline";
                                }
                            }
                            cargarMunicipiosPorComarca();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }else{
                    document.getElementById('codComarca').disabled = true;
                    document.getElementById('codComarca').value = '';
                    document.getElementById('descComarca').disabled = true;
                    document.getElementById('descComarca').value = '';
                    document.getElementById('anchorComarca').style.display = "none";
                    var mensaje=(idiomaUsuarioJS==4?"Adierazi kolektiboa eta lurralde historikoa eremuak kargatzeko.":"Indique Colectivo y Territorio Historico para cargar Ambitos.");
                    jsp_alerta("A",mensaje);
                }
            }
            
            function cerrar(){
                window.close();
            }
            
            function confirmarSalida(){
                return '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.confirmarSalida")%>';
            }
            
            
            function eventosCheck(lanzadorEvento){
                if(anio>=2018){
                    if(document.getElementById('localPrevAprobado') != null && document.getElementById('localPrevAprobado') != undefined
                            && document.getElementById('localPrevAprobado').checked){
                        document.getElementById('rowMantieneRequLocalApro').style.display="block";
                        document.getElementById('textoNotaObserMantieneRequLocalApro').style.display="block";
                        document.getElementById('mantieneRequLocalApro').checked=true;
                        document.getElementById('mantieneRequLocalApro').readonly=true;
                        document.getElementById('mantieneRequLocalApro').disabled=true;
                    }else{
                        document.getElementById('textoNotaObserMantieneRequLocalApro').style.display="none";
                        document.getElementById('rowMantieneRequLocalApro').style.display="none";
                        document.getElementById('mantieneRequLocalApro').checked=false;
                    }
                    if(document.getElementById('disponeEspacioComplWifi') != null && document.getElementById('disponeEspacioComplWifi') != undefined
                            && document.getElementById('disponeEspacioComplWifi').checked){
                        document.getElementById('textoNotaObserDisponeEspacioComplWifi').style.display="block";
                    }else{
                        document.getElementById('textoNotaObserDisponeEspacioComplWifi').style.display="none";
                    }
                }
            }
        </script>
    <div id="cuerpoNuevaUbicacionCTo" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo" style="overflow-y: auto; padding: 1px;">
            <!--<form  id="formNuevaUbicacionCT">-->
                <div style="width: 98.5%; float: left; clear: both; margin-left: 5px; margin-right: 5px;">
                    <div style="width: 98.5%; clear: both; margin-bottom: 10px;" class="txttitblanco"><%=tituloPagina%></div>
                    <div style="width: 50%; float: left; display: inline;">
                        <fieldset id="fieldsetDatosCentro" name="fieldsetDatosCentro" style="width: auto;">
                            <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.ubicacion")%></legend>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px;">
                                <table>
                                    <tr>
                                        <td style="width: 40px;">
                                <label for="codEntidad"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.entidad")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input id="codEntidad" name="codEntidad" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descEntidad" name="descEntidad" type="text" class="inputTexto" size="50" readonly>
                                <a id="anchorEntidad" name="anchorEntidad" href="" style="display: none;"><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEntidad" name="botonEntidad" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                        </td>
                                    </tr>
<!--                            </div>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px;">-->
                                    <tr>
                                        <td style="width: 40px;">
                                <label for="codColectivo"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.colectivo")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input id="codColectivo" name="codColectivo" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descColectivo" name="descColectivo" type="text" class="inputTexto" size="50" readonly>
                                <a id="anchorColectivo" name="anchorColectivo" href="" style="display: none;"><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonColectivo" name="botonColectivo" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                        </td>
                                    </tr>
<!--                            </div>

                            <div class="lineaFormulario" style="margin-top: 19px;padding-left: 5px; padding-right: 5px;">-->
                                    <tr>
                                        <td style="width: 40px;">
                                <label><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.ambito")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input id="codAmbito" name="codAmbito" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;"
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descAmbito" name="descAmbito" type="text" class="inputTexto" size="50" readonly>
                                <a id="anchorAmbito" name="anchorAmbito" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAmbito" name="botonAmbito" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                        </td>
                                    </tr>
<!--                            </div>
                            <div class="lineaFormulario" style="margin-top: 19px;padding-left: 5px; padding-right: 5px;">-->
                                    <tr>
                                        <td style="width: 40px;">
                                <label for="codComarca"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.comarca")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input id="codComarca" name="codComarca" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;"
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descComarca" name="descComarca" type="text" class="inputTexto" size="50" readonly>
                                <a id="anchorComarca" name="anchorComarca" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonComarca" name="botonComarca" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                        </td>
                                    </tr>
<!--                            </div>
                            <div class="lineaFormulario" style="margin-top: 19px;padding-left: 5px; padding-right: 5px;">-->
                                    <tr>
                                        <td style="width: 40px;">
                                <label for="codMunicipio"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.municipio")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input id="codMunicipio" name="codMunicipio" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;"
                                       onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descMunicipio" name="descMunicipio" type="text" class="inputTexto" size="50" readonly>
                                <a id="anchorMunicipio" name="anchorMunicipio" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonMunicipio" name="botonMunicipio" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </fieldset>
                    </div>
                    <div style="width: 45%; float: right; display: inline; margin-right: 10px">
                        <fieldset id="fieldsetDireccionCentro" name="fieldsetDireccionCentro" style="width: auto;">
                            <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.direccion")%></legend>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px;">
                                <table>
                                    <tr>
                                        <td style="width: 65px;">
                                            <label for="direccionCT"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.direccion")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                            <input type="text" id="direccionCT" name="direccionCT" class="inputTexto" size="50" maxlength="200" value="<%=ubicacionModif != null && ubicacionModif.getDireccion()!=null ? ubicacionModif.getDireccion() : ""%>"/>
                                            <br /><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.nuevoCentro.tabla.direcciones.num")%><input type="number" id="direccionPortal" name="direccionPortal" class="inputTexto" size="10" maxlength="10" value="<%=ubicacionModif != null && ubicacionModif.getDireccionPortal()!=null ? ubicacionModif.getDireccionPortal() : ""%>"/>
                                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.direccion.piso")%><input type="text" id="direccionPiso" name="direccionPiso" class="inputTexto" size="10" maxlength="10" value="<%=ubicacionModif != null && ubicacionModif.getDireccionPiso()!=null ? ubicacionModif.getDireccionPiso() : ""%>"/>
                                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.direccion.letra")%><input type="text" id="direccionLetra" name="direccionLetra" class="inputTexto" size="10" maxlength="10" value="<%=ubicacionModif != null && ubicacionModif.getDireccionLetra()!=null ? ubicacionModif.getDireccionLetra() : ""%>"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 65px;">
<!--                            </div>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px;">-->
                                <label for="codigoPostalCT"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.codigo.postal")%></label><font color="red">*</font>
                                        </td>
                                        <td>
                                <input type="text" id="codigoPostalCT" name="codigoPostalCT" class="inputTexto" size="15" maxlength="5" value="<%=ubicacionModif != null && ubicacionModif.getCodigoPostal()!=null? ubicacionModif.getCodigoPostal() : ""%>"/>
                                        </td>
                                    </tr>
                                    <tr id="filaTelefono">
                                        <td style="width: 65px;">
<!--                            </div>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px;">-->
                                <label for="telefonoCT" ><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.telefono")%></label>
                                        </td>
                                        <td>
                                <input type="text" id="telefonoCT" name="telefonoCT" class="inputTexto" size="15" maxlength="12" value="<%=ubicacionModif != null && ubicacionModif.getTelefono()!=null ? ubicacionModif.getTelefono() : ""%>"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </fieldset>
                    </div>
                    
                    <div class="lineaFormulario" id="divOtrosDatos" style="<%=(ejercicio<2018?"display:none;":"")%>margin-top: 10px;">
                        <fieldset id="fieldsetOtrosDatos" name="fieldsetOtrosDatos" style="width: auto;">
                            <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos")%></legend>
                            <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px;">
                                <table>
                                    <tr>
                                        <td style="width: 750; table-layout:fixed;">
                                            <label for="localPrevAprobado"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.localpreviaaprobado")%></label><span> *</span>
                                        </td>
                                        <td style="text-align: center;">
                                            <input type="checkbox" id="localPrevAprobado" <%=(ubicacionModif != null && ubicacionModif.getLocalesPreviamenteAprobados()!=null && ubicacionModif.getLocalesPreviamenteAprobados() == 1 ? "checked" : "")%> size="2" onclick="eventosCheck()"/>
                                        </td>
                                    </tr>
                                    <tr id="rowMantieneRequLocalApro">
                                        <td style="width: 750;">
                                            <label for="mantieneRequLocalApro"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.mantenimientoreqlocales")%></label><span> *</span>
                                        </td>
                                        <td style="text-align: center;">
                                            <input type="checkbox" id="mantieneRequLocalApro" <%=(ubicacionModif != null && ubicacionModif.getMantieneRequisitosLocalesAprob()!=null && ubicacionModif.getMantieneRequisitosLocalesAprob() == 1 ? "checked" : "")%> size="2"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 750; table-layout:fixed;">
                                            <label for="disponeEspacioComplWifi"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.disponeEspacioComplWifi")%></label><span> **</span>
                                        </td>
                                        <td style="text-align: center;">
                                            <input type="checkbox" id="disponeEspacioComplWifi" <%=(ubicacionModif != null && ubicacionModif.getDisponeEspacioComplWifi()!=null && ubicacionModif.getDisponeEspacioComplWifi() == 1 ? "checked" : "")%> size="2" onclick="eventosCheck()"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="lineaFormulario" id="textoNotaObserMantieneRequLocalApro" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px; text-align: justify">
                                <font color="red">
                                <ul style="width: 80%;">
                                    <!--<li style="list-style-type: none" >* <%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos.nota1")%>
                                        <ul style="text-align: justify">
                                            <li><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos.nota2")%></li>
                                            <li><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos.nota3")%></li>
                                        </ul>
                                    </li>-->    
                                    <li style="list-style-type: none" >(*) <%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos.nota4")%>
                                    </li>    
                                </ul> 
                                </font>
                            </div>
                           <div class="lineaFormulario" id="textoNotaObserDisponeEspacioComplWifi" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px; text-align: justify">
                                <font color="red">
                                <ul style="width: 80%;">
                                    <li style="list-style-type: none" >(**) <%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.ubicaciones.centro.trabajo.otrosdatos.nota5")%>
                                    </li>    
                                </ul> 
                                </font>
                            </div>
                        </fieldset>
                    </div>                    
                </div>
                <div class="botonera" style="alignment-adjust: central">
                   <input type="button" id="btnAnadir" name="btnAnadir" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" > <!--onclick="guardarDatosUbicacionCentTra();" Pasado a JS-->
                </div>
            <!--</form>-->
        </div>
                <!-- Variables a leer en el JS en la apestana-->
                <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="S"/>
                <input type="hidden" id="codigoConvocatoriaExpediente" name="codigoConvocatoriaExpediente" value="<%=codigoConvocatoriaExpediente%>"/>
                <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>
                <input type="hidden" id="idiomaUsuario" name="idiomaUsuario" value="<%=idiomaUsuario%>"/>
                <input type="hidden" id="ejercicio" name="ejercicio" value="<%=ejercicio%>"/>
                <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
                <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
                <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
                <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
                <input type="hidden" id="codId" value="<%=ubicacionModif != null && ubicacionModif.getCodId()!=null ? ubicacionModif.getCodId() : ""%>"/>
                <input type="hidden" id="msg_generico_campos_obligatorios" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>"/>
                <input type="hidden" id="msg_nuevoCentro_direcciones_dirCaracteresNoPermitidos" value="<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevoCentro.direcciones.dirCaracteresNoPermitidos")%>"/>
                <input type="hidden" id="msg_nuevoCentro_direcciones_dirExcedeLongitud" value="<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevoCentro.direcciones.dirExcedeLongitud")%>"/>
	</div>	
	<script type="text/javascript">
		var comboEntidad = new Combo("Entidad");
		
		var comboColectivo = new Combo("Colectivo");
		comboColectivo.change = cargarComarcasPorAmbito;
		var comboAmbito = new Combo("Ambito");
		comboAmbito.change = cargarComarcasPorAmbito;
		var comboComarca = new Combo("Comarca");
		var comboMunicipio = new Combo("Municipio");
		comboComarca.change = cargarMunicipiosPorComarca;
		eventosCheck();
		inicio();
		
	</script>
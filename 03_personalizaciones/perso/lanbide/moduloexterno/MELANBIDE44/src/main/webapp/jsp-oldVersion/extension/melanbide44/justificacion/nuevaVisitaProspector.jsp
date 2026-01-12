<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaVisProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.SelectItem" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>
<%@page import="java.util.*" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
    <head>
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

            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);

            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            String numExpediente    = request.getParameter("numero");   
            
            Integer anoExpediente = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
            
            String prosvis    = request.getParameter("prospector");
            if (prosvis==null || prosvis.equals("null")) prosvis = "";
            String nifpros = (String) request.getAttribute("nifpros");
            EcaVisProspectoresVO visModif = (EcaVisProspectoresVO)request.getAttribute("visitaModif");  
            if (visModif !=null) {
                if (prosvis==null || prosvis.equals("null")|| prosvis.equals("")) prosvis=visModif.getJusProspectoresCod().toString();
            }                
            session.removeAttribute("visitaModif");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

    
            // lista sector
            List<SelectItem> listaSector = new ArrayList<SelectItem>();
            if(request.getAttribute("lstSector") != null)
                listaSector = (List<SelectItem>)request.getAttribute("lstSector");

            String lcodSector = "";
            String ldescSector = "";

            if (listaSector != null && listaSector.size() > 0) 
            {
                int i;
                SelectItem ts = null;
                for (i = 0; i < listaSector.size() - 1; i++) 
                {
                    ts = (SelectItem) listaSector.get(i);
                    lcodSector += "\"" + ts.getId().toString() + "\",";
                    ldescSector += "\"" + escape(ts.getLabel()) + "\",";
                }
                ts = (SelectItem) listaSector.get(i);
                lcodSector += "\"" + ts.getId().toString() + "\"";
                ldescSector += "\"" + escape(ts.getLabel()) + "\"";
            }
            
            List<SelectItem> listaProvincia = new ArrayList<SelectItem>();
            if(request.getAttribute("lstProvincia") != null)
                listaProvincia = (List<SelectItem>)request.getAttribute("lstProvincia");

            String lcodProvincia = "";
            String ldescProvincia = "";

            if (listaProvincia != null && listaProvincia.size() > 0) 
            {
                int i;
                SelectItem tp = null;
                for (i = 0; i < listaProvincia.size() - 1; i++) 
                {
                    tp = (SelectItem) listaProvincia.get(i);
                    lcodProvincia += "\"" + tp.getId().toString() + "\",";
                    ldescProvincia += "\"" + escape(tp.getLabel()) + "\",";
                }
                tp = (SelectItem) listaProvincia.get(i);
                lcodProvincia += "\"" + tp.getId().toString() + "\"";
                ldescProvincia += "\"" + escape(tp.getLabel()) + "\"";
            }
            
            List<SelectItem> listaLismi = new ArrayList<SelectItem>();
            if(request.getAttribute("lstCumpleLismi") != null)
                listaLismi = (List<SelectItem>)request.getAttribute("lstCumpleLismi");

            String lcodLismi = "";
            String ldescLismi = "";

            if (listaLismi != null && listaLismi.size() > 0) 
            {
                int i;
                SelectItem tl = null;
                for (i = 0; i < listaLismi.size() - 1; i++) 
                {
                    tl = (SelectItem) listaLismi.get(i);
                    lcodLismi += "\"" + tl.getId().toString() + "\",";
                    ldescLismi += "\"" + escape(tl.getLabel()) + "\",";
                }
                tl = (SelectItem) listaLismi.get(i);
                lcodLismi += "\"" + tl.getId().toString() + "\"";
                ldescLismi += "\"" + escape(tl.getLabel()) + "\"";
            }
            
            List<SelectItem> listaResultado = new ArrayList<SelectItem>();
            if(request.getAttribute("lstResultado") != null)
                listaResultado = (List<SelectItem>)request.getAttribute("lstResultado");

            String lcodResultado = "";
            String ldescResultado = "";

            if (listaResultado != null && listaResultado.size() > 0) 
            {
                int i;
                SelectItem tr = null;
                for (i = 0; i < listaResultado.size() - 1; i++) 
                {
                    tr = (SelectItem) listaResultado.get(i);
                    lcodResultado += "\"" + tr.getId().toString() + "\",";
                    ldescResultado += "\"" + escape(tr.getLabel()) + "\",";
                }
                tr = (SelectItem) listaResultado.get(i);
                lcodResultado += "\"" + tr.getId().toString() + "\"";
                ldescResultado += "\"" + escape(tr.getLabel()) + "\"";
            }

            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.consultaVisita.tituloPagina");
            }
            else
            {
                if(visModif != null)
                {
                    tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.modifVisita.tituloPagina");
                }
                else
                {
                    tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.nuevaVisita.tituloPagina");
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
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

        <script type="text/javascript">

            var nuevo = true;
            var mensajeValidacion = '';
    
            //LISTAS DE VALORES PARA LOS COMBOS
            var codSector = [<%=lcodSector%>];
            var descSector = [<%=ldescSector%>];
            
            var codProvincia = [<%=lcodProvincia%>];
            var descProvincia = [<%=ldescProvincia%>];
            
            var codLismi = [<%=lcodLismi%>];
            var descLismi = [<%=ldescLismi%>];
            
            var codResultado = [<%=lcodResultado%>];
            var descResultado = [<%=ldescResultado%>];

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
                try{            
                    <%

                    if(visModif != null)
                    { 
                    %> nuevo = false;           
                        document.getElementById('fecVisita').value = '<%=visModif.getFecVisita() != null ? format.format(visModif.getFecVisita()) : "" %>';                         
                        document.getElementById('cif').value = '<%=visModif.getCif() != null ? visModif.getCif().toUpperCase() : "" %>';
                        document.getElementById('empresa').value = '<%=visModif.getEmpresa() != null ? visModif.getEmpresa().toUpperCase() : "" %>';
                        document.getElementById('direccion').value = '<%=visModif.getDireccion() != null ? visModif.getDireccion().toUpperCase() : "" %>';
                        document.getElementById('cpostal').value = '<%=visModif.getCpostal() != null ? visModif.getCpostal() : "" %>';               
                        document.getElementById('localidad').value = '<%=visModif.getLocalidad() != null ? visModif.getLocalidad().toUpperCase() : "" %>';               
                        document.getElementById('email').value = '<%=visModif.getMail() != null ? visModif.getMail().toUpperCase() : "" %>';               
                        document.getElementById('telefono').value = '<%=visModif.getTelefono() != null ? visModif.getTelefono() : "" %>';                       
                        document.getElementById('numTrab').value = '<%=visModif.getNumTrab() != null ? visModif.getNumTrab() : "" %>';               
                        document.getElementById('numTrabDisc').value = '<%=visModif.getNumTrabDisc() != null ? visModif.getNumTrabDisc() : "" %>';
                        document.getElementById('personaContacto').value = '<%=visModif.getPersContacto() != null ? visModif.getPersContacto().toUpperCase() : "" %>';
                        document.getElementById('puesto').value = '<%=visModif.getPuesto() != null ? visModif.getPuesto().toUpperCase() : "" %>';               
                        document.getElementById('observaciones').value = '<%=visModif.getObservaciones() != null ? visModif.getObservaciones().toUpperCase() : "" %>';
                        document.getElementById('nifProspector').value = '<%=visModif.getNifProspector() != null ? visModif.getNifProspector().toUpperCase() : "" %>';
                        document.getElementById('codSector').value = '<%=visModif.getSector() != null ? visModif.getSector() : "" %>';
                        document.getElementById('codProvincia').value = '<%=visModif.getProvincia() != null ? visModif.getProvincia() : "" %>';
                        document.getElementById('codLismi').value = '<%=visModif.getCumpleLismi() != null ? visModif.getCumpleLismi() : "" %>';
                        document.getElementById('codResultado').value = '<%=visModif.getResultadoFinal() != null ? visModif.getResultadoFinal() : "" %>';
                        
                        document.getElementById('nifProspector').disabled = true;
                    <%
                    } else {
                        if ( nifpros !=null) { %>
                             document.getElementById('nifProspector').value = '<%=nifpros %>';
                             document.getElementById('nifProspector').disabled = true;
                        <% } 
                    }
                    %>


                    <%
                        if(consulta == true)
                        {
                    %>
                            //Deshabilito todos los campos
                            document.getElementById('cif').disabled = true;
                            document.getElementById('empresa').disabled = true;
                            document.getElementById('direccion').disabled = true;
                            document.getElementById('cpostal').disabled = true;              
                            document.getElementById('localidad').disabled = true;               
                            document.getElementById('email').disabled = true;               
                            document.getElementById('telefono').disabled = true;       
                            document.getElementById('numTrab').disabled = true;             
                            document.getElementById('numTrabDisc').disabled = true;
                            document.getElementById('descProvincia').disabled = true;             
                            document.getElementById('codProvincia').disabled = true;    
                            document.getElementById('codSector').disabled = true;              
                            document.getElementById('descSector').disabled = true;    
                            document.getElementById('codLismi').disabled = true;        
                            document.getElementById('descLismi').disabled = true;        
                            document.getElementById('codResultado').disabled = true;       
                            document.getElementById('descResultado').disabled = true;  
                            document.getElementById('personaContacto').disabled = true;
                            document.getElementById('puesto').disabled = true;
                            document.getElementById('observaciones').disabled = true;
                            document.getElementById('nifProspector').disabled = true;
                            document.getElementById('fecVisita').disabled = true;
                            document.getElementById('observaciones').disabled = true;
                            document.getElementById('observaciones').className = 'inputTexto readOnly';
                            
                            document.getElementById('botonSector').style.display = 'none';
                            document.getElementById('botonProvincia').style.display = 'none';
                            document.getElementById('botonLismi').style.display = 'none';
                            document.getElementById('botonResultado').style.display = 'none';
                            document.getElementById('calFecVisita').style.display = 'none';

                            document.getElementById('btnAceptar').style.display = 'none';
                            document.getElementById('btnCancelar').style.display = 'none';
                            document.getElementById('btnCerrar').style.display = 'inline';                   

                    <%
                        }
                        else
                        {
                    %>  
                            comboSector = new Combo('Sector');
                            comboProvincia = new Combo('Provincia');
                            comboLismi = new Combo('Lismi');
                            comboResultado = new Combo('Resultado');
                            cargarCombos();
                    <%
                        }
                    %> 
                  
                    FormatNumber(document.getElementById('numTrab').value, 6, 0, document.getElementById('numTrab').id);
                    FormatNumber(document.getElementById('numTrabDisc').value, 6, 0, document.getElementById('numTrabDisc').id);
                    cargarDescripcionesCombos();

                }catch(err){

                }
            }
    
            function cargarCombos(){
                comboSector.addItems(codSector, descSector);
                comboProvincia.addItems(codProvincia, descProvincia);
                comboLismi.addItems(codLismi, descLismi);
                comboResultado.addItems(codResultado, descResultado);
            }
        
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                //Sector
                codigo = '<%=visModif != null && visModif.getSector() != null ? visModif.getSector() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codSector.length && !encontrado)
                    {
                        codAct = codSector[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descSector[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descSector').value = desc;

                //Provincia
                codigo = '<%=visModif != null && visModif.getProvincia() != null ? visModif.getProvincia() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codProvincia.length && !encontrado)
                    {
                        codAct = codProvincia[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descProvincia[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descProvincia').value = desc;

                //Lismi
                codigo = '<%=visModif != null && visModif.getCumpleLismi() != null ? visModif.getCumpleLismi() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codLismi.length && !encontrado)
                    {
                        codAct = codLismi[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descLismi[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descLismi').value = desc;

                //Resultado final
                codigo = '<%=visModif != null && visModif.getResultadoFinal() != null ? visModif.getResultadoFinal() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codResultado.length && !encontrado)
                    {
                        codAct = codResultado[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descResultado[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descResultado').value = desc;
            }

            function mostrarCalFechaVisita(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFecVisita").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fecVisita',null,null,null,'','calFecVisita','',null,null,null,null,null,null,null,null,evento);
            }

            function cancelar(){
                var resultado = jsp_alerta("","<%=meLanbide44I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
                if (resultado == 1){
                    cerrarVentana();
                }
            }

            function cerrarVentana(){
              if(navigator.appName=="Microsoft Internet Explorer") { 
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

            function barraProgresoVis(valor, idBarra) {
            if(valor=='on'){
                document.getElementById(idBarra).style.visibility = 'inherit';
            }
            else if(valor=='off'){
                document.getElementById(idBarra).style.visibility = 'hidden';
            }
        }

           function guardarVisita(){
                if(validarDatosVisita()){

                    document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoVis('on', 'barraProgresoNuevaVisitaProspector');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=guardarVisitaProspector&tipo=0&numero=<%=numExpediente%>'
                        +'&idPros=<%=visModif != null && visModif.getJusProspectoresCod() != null ? visModif.getJusProspectoresCod() : ""%>'
                        +'&idVis=<%=visModif != null && visModif.getVisProspectoresCod() != null ? visModif.getVisProspectoresCod() : ""%>'
                        +'&cif='+escape(document.getElementById('cif').value)
                        +'&empresa='+escape(document.getElementById('empresa').value)
                        +'&sector='+document.getElementById('codSector').value
                        +'&direccion='+escape(document.getElementById('direccion').value)
                        +'&cpostal='+document.getElementById('cpostal').value               
                        +'&localidad='+escape(document.getElementById('localidad').value)
                        +'&provincia='+document.getElementById('codProvincia').value
                        +'&personaContacto='+escape(document.getElementById('personaContacto').value)
                        +'&puesto='+escape(document.getElementById('puesto').value)
                        +'&email='+escape(document.getElementById('email').value)
                        +'&telefono='+document.getElementById('telefono').value
                        +'&numtrab='+document.getElementById('numTrab').value
                        +'&numtrabdisc='+document.getElementById('numTrabDisc').value
                        +'&fecVisita='+document.getElementById('fecVisita').value
                        +'&cumple='+document.getElementById('codLismi').value
                        +'&resultado='+document.getElementById('codResultado').value
                        +'&observaciones='+escape(document.getElementById('observaciones').value)
                        +'&nifProspector='+document.getElementById('nifProspector').value               
                        +'&prosvis='+'<%=prosvis %>'              
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


                    var listaErrores = new Array();           
                    var nodoErrores;
                    var nodoCampo;
                    var errores;           
                    var camposErrores;            

                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaProspectores = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var j;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaProspectores[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            camposErrores = new Array();
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="ID"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[0] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[0] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = '-';
                                    }
                                }      
                                else if(hijosFila[cont].nodeName=="EMPRESA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_VISITA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = '-';
                                    }                            
                                }
                                else if(hijosFila[cont].nodeName=="SECTOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = '-';
                                    }   
                                }
                                else if(hijosFila[cont].nodeName=="DIRECCION"){
                                     nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = '-';
                                    }   
                                }
                                else if(hijosFila[cont].nodeName=="CPOSTAL"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="LOCALIDAD"){
                                   nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="PROVINCIA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="PERS_CONTACTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="PUESTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="EMAIL"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = '-';
                                    } 
                                }else if(hijosFila[cont].nodeName=="TELEFONO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="NIF_PROSPECTOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = '-';
                                    } 
                                }                        
                                else if(hijosFila[cont].nodeName=="NUMTRAB"){
                                   nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="NUMTRABDISC"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="CUMPLELISMI"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="RESULTADO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = '-';
                                    } 
                                }else if(hijosFila[cont].nodeName=="ERRORES"){
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
                                    fila[19] = listaErrores;
                                }

                            }
                            fila[20] = camposErrores;
                            listaProspectores[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                        if(codigoOperacion=="0"){
                            window.returnValue =  listaProspectores;
                            barraProgresoVis('off', 'barraProgresoNuevaVisitaProspector');
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else if(codigoOperacion=="5"){
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.NifProspectoIncorrecto")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch

                    barraProgresoVis('off', 'barraProgresoNuevaVisitaProspector');
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
            }

            function validarDatosVisita(){
                mensajeValidacion = '';
                var correcto = true;
                
                //Primero comprobamos que se hayan introducido todos los datos obligatorios
                
                //empresa
                var empresa = document.getElementById('empresa').value;
                if(empresa == null || empresa == ''){
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.todosCamposAsteriscoOblig")%>';
                    return false;
                }  
                
                //sector
                var sector = document.getElementById('codSector').value;
                if(sector == null || sector == ''){
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.todosCamposAsteriscoOblig")%>';
                    return false;
                }
                
                //Resultado final
                var result = document.getElementById('codResultado').value;
                if(result == null || result == ''){
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.todosCamposAsteriscoOblig")%>';
                    return false;
                }
                
                //Fecha visita
                var fec = document.getElementById('fecVisita').value;
                if(fec == null || fec == ''){
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.todosCamposAsteriscoOblig")%>';
                    return false;
                }

                //Nif prospector
                var nifProspector = document.getElementById('nifProspector').value;
                if(nifProspector == null || nifProspector == ''){
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.todosCamposAsteriscoOblig")%>';
                    return false;
                } 
                
                //ahora se comprueba el formato de los campos
                
                //CIF
                var cif = document.getElementById('cif').value
                if(cif != null && cif != ''){
                    if(cif != 'AS/G/14961/2010' && cif != 'J01467364' && cif != 'J16266326' && cif != 'J01467364'){
                        if(!validarCif(document.getElementById('cif'))){
                            //no es cif miramos si nif (autonomo)
                             if(!validarNIFEca(document.getElementById('cif'))){
                                 document.getElementById('cif').style.border = '1px solid red';
                                 if(mensajeValidacion == '')
                                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.cifIncorrecto")%>';
                                 correcto = false;
                             }else{
                                 document.getElementById('cif').removeAttribute("style");
                             }
                        }else{
                            document.getElementById('cif').removeAttribute("style");
                        } 
                    }
                }
                
                //Empresa
                if(!comprobarCaracteresEspecialesEca(empresa)){
                    document.getElementById('empresa').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.empresaCaracteresEspeciales")%>';
                    correcto = false;
                }else{
                    document.getElementById('empresa').removeAttribute("style");
                }
                
                //Sector
                if(!existeCodigoCombo(sector, codSector)){
                    correcto = false;
                    document.getElementById('codSector').style.border = '1px solid red';
                    document.getElementById('descSector').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.codigoSectorNoExiste")%>';
                }else{
                    document.getElementById('codSector').removeAttribute("style");
                    document.getElementById('descSector').removeAttribute("style");
                }
                
                //direccion
                var direccion = document.getElementById('direccion').value;
                if(direccion != null && direccion != ''){
                    if(!comprobarCaracteresEspecialesEca(direccion)){
                        correcto = false;
                        document.getElementById('direccion').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.direccionCaracteresEspeciales")%>';
                    }else{
                        document.getElementById('direccion').removeAttribute("style");
                    }
                }
                
                //C. Postal
                var cp = document.getElementById('cpostal').value;
                if(cp != null && cp != ''){
                    try{
                        if(!validarNumericoEca(document.getElementById('cpostal'))){
                            correcto = false;
                            document.getElementById('cpostal').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.cpostalIncorrecto")%>';
                        }else{
                            var n = parseInt(document.getElementById('cpostal').value);
                            if(n < 0){
                                correcto = false;
                                document.getElementById('cpostal').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.cpostalIncorrecto")%>';
                            }else{
                                document.getElementById('cpostal').removeAttribute('style');
                            }
                        }                    
                    }catch(err){
                        correcto = false;
                        document.getElementById('cpostal').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.cpostalIncorrecto")%>';
                    }
                }
                
                //Localidad
                var localidad = document.getElementById('localidad').value;
                if(localidad != null && localidad != ''){
                    if(!comprobarCaracteresEspecialesEca(localidad)){
                        correcto = false;
                        document.getElementById('localidad').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.localidadCaracteresEspeciales")%>';
                    }else{
                        document.getElementById('localidad').removeAttribute("style");
                    }
                }
                
                //Provincia
                var provincia = document.getElementById('codProvincia').value;
                if(provincia != null && provincia != ''){
                    if(!existeCodigoCombo(provincia, codProvincia)){
                        correcto = false;
                        document.getElementById('codProvincia').style.border = '1px solid red';
                        document.getElementById('descProvincia').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.codigoProvinciaNoExiste")%>';
                    }else{
                        document.getElementById('codProvincia').removeAttribute("style");
                        document.getElementById('descProvincia').removeAttribute("style");
                    }
                }
                
                //Persona contacto   
                var personaContacto = document.getElementById('personaContacto').value;
                if(personaContacto != null && personaContacto != ''){
                    if(!comprobarCaracteresEspecialesEca(personaContacto)){
                        correcto = false;
                        document.getElementById('personaContacto').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.perscontCaracteresEspeciales")%>';
                    }else{
                        document.getElementById('personaContacto').removeAttribute("style");
                    }
                }
                
                //Cargo/puesto
                var puesto = document.getElementById('puesto').value;
                if(puesto != null && puesto != ''){
                    if(!comprobarCaracteresEspecialesEca(puesto)){
                        correcto = false;
                        document.getElementById('puesto').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.puestoCaracteresEspeciales")%>';
                    }else{
                        document.getElementById('puesto').removeAttribute("style");
                    }
                }
                
                //Email
                var email = document.getElementById('email').value;
                if(email != null && email != ''){
                    try{
                        if(!comprobarCaracteresEspecialesEca(email)){
                            correcto = false;
                            document.getElementById('email').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.emailCaracteresEspeciales")%>';
                        }else{
                            if(!validarEmailEca(email)){
                                correcto = false;
                                document.getElementById('email').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.emailIncorrecto")%>';
                            }else{
                                document.getElementById('email').removeAttribute('style');
                            }   
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('email').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.emailIncorrecto")%>';
                    }
                }
                
                //telefono
                var telefono = document.getElementById('telefono').value;
                if(telefono != null && telefono != ''){
                    try{
                        if(!validarNumericoEca(document.getElementById('telefono'))){
                            correcto = false;
                            document.getElementById('telefono').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.telefonoIncorrecto")%>';
                        }else{
                            var n = parseInt(document.getElementById('telefono').value);
                            if(n < 0){
                                correcto = false;
                                document.getElementById('telefono').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.telefonoIncorrecto")%>';
                            }else{
                                document.getElementById('telefono').removeAttribute('style');
                            }
                        }                    
                    }catch(err){
                        correcto = false;
                        document.getElementById('telefono').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.telefonoIncorrecto")%>';
                    }
                }
                
                //Num Trab
                var numTrab = document.getElementById('numTrab').value;
                if(numTrab != null && numTrab != ''){
                    try{
                        if(!validarNumericoEca(document.getElementById('numTrab'))){
                            correcto = false;
                            document.getElementById('numTrab').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabIncorrecto")%>';
                        }else{
                            var n = parseInt(document.getElementById('numTrab').value);
                            if(n < 0){
                                correcto = false;
                                document.getElementById('numTrab').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabIncorrecto")%>';
                            }else{
                                document.getElementById('numTrab').removeAttribute('style');
                            }
                        }                    
                    }catch(err){
                        correcto = false;
                        document.getElementById('numTrab').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabIncorrecto")%>';
                    }
                }
                
                //Num Trab Disc
                var numTrabDisc = document.getElementById('numTrabDisc').value;
                if(numTrabDisc != null && numTrabDisc != ''){
                    try{
                        if(!validarNumericoEca(document.getElementById('numTrabDisc'))){
                            correcto = false;
                            document.getElementById('numTrabDisc').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabdiscIncorrecto")%>';
                        }else{
                            var n = parseInt(document.getElementById('numTrabDisc').value);
                            if(n < 0){
                                correcto = false;
                                document.getElementById('numTrabDisc').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabdiscIncorrecto")%>';
                            }else{
                                document.getElementById('numTrabDisc').removeAttribute('style');
                            }
                        }                    
                    }catch(err){
                        correcto = false;
                        document.getElementById('numTrabDisc').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.numtrabdiscIncorrecto")%>';
                    }
                }
                
                //Cumple LISMI
                var lismi = document.getElementById('codLismi').value;
                if(lismi != null && lismi != ''){
                    if(!existeCodigoCombo(lismi, codLismi)){
                        correcto = false;
                        document.getElementById('codLismi').style.border = '1px solid red';
                        document.getElementById('descLismi').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.codigoLismiNoExiste")%>';
                    }else{
                        document.getElementById('codLismi').removeAttribute("style");
                        document.getElementById('descLismi').removeAttribute("style");
                    }
                }
                
                //Resultado final
                if(!existeCodigoCombo(result, codResultado)){
                    correcto = false;
                    document.getElementById('codResultado').style.border = '1px solid red';
                    document.getElementById('descResultado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.codigoResultadoNoExiste")%>';
                }else{
                    document.getElementById('codResultado').removeAttribute("style");
                    document.getElementById('descResultado').removeAttribute("style");
                }
                
                //Fecha visita
                if(!validarFechaEca(document.forms[0], document.getElementById('fecVisita'))){
                    document.getElementById('fecVisita').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.justificacion.visita.fechaVisitaIncorrecto")%>';
                    correcto = false;
                }else{ 
                    //La fecha de visita debe pertenecer al año del expediente.
                    var feVisita = document.getElementById('fecVisita').value;
                    if(feVisita != null && feVisita != ''){
                        var aVisita = feVisita.split('/');
                        var anoVisita = parseInt(aVisita[2]);
                        var anoExp = parseInt('<%=anoExpediente != null ? anoExpediente : -1%>');
                        if(anoVisita != anoExp){
                            correcto = false;
                            document.getElementById('fecVisita').style.border = '1px solid red';
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.justificacion.visita.fechaVisita")%>';
                        }else{
                            document.getElementById('fecVisita').removeAttribute('style');
                        }
                    }else{
                        document.getElementById('fecVisita').removeAttribute('style');
                    }
                }
                    
                //Observaciones
                var obs = document.getElementById('observaciones').value;
                if(obs != null && obs != ''){
                    if(!comprobarCaracteresEspecialesEca(obs)){
                        correcto = false;
                        document.getElementById('observaciones').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.observacionesCaracteresEspeciales")%>';
                    }else{
                        if(obs.length > 200){
                            correcto = false;
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.justificacion.visita.observacionesDemasiadoLargo")%>';
                        }else{
                            document.getElementById('observaciones').removeAttribute("style");
                        }
                    }
                }
                
                //Nif prospector
                if(!validarCif(document.getElementById('nifProspector'))){
                    //no es cif miramos si nif (autonomo)
                     if(!validarNIFEca(document.getElementById('nifProspector'))){
                         document.getElementById('nifProspector').style.border = '1px solid red';
                         if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.justificacion.visita.nifIncorrectoPros")%>';
                         correcto = false;
                     }else{
                         document.getElementById('nifProspector').removeAttribute("style");
                     }
                }else{
                    document.getElementById('nifProspector').removeAttribute("style");
                } 
                
                return correcto;
            }
            
            function existeCodigoCombo(seleccionado, listaCodigos){
                if(seleccionado != undefined && seleccionado != null && listaCodigos != undefined && listaCodigos != null){
                    if(trim(seleccionado) != ''){
                        var encontrado = false;
                        var i = 0;
                        while(!encontrado && i < listaCodigos.length){
                            if(listaCodigos[i] == seleccionado){
                                encontrado = true;
                            }else{
                                i++;
                            }
                        }
                        return encontrado;
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }

        </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla etiqueta">
        <form>
            <div id="barraProgresoNuevaVisitaProspector" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px" height="100%">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span id="msgGuardandoDatos">
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
            <div style="width: 100%; padding: 10px; text-align: left;">
                <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
                <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.datosVisita")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 36px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.cif")%>
                    </div>
                    <div style="width: 125px; float: left;">
                        <input type="text" id="cif" name="cif" size="15" maxlength="15" class="inputTexto"/>
                    </div>
                    <div style="width: 100px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.empresa")%><font color="red">*</font>
                    </div>
                    <div style="width: 200px; float: left;">
                        <input type="text" id="empresa" name="empresa" size="83" maxlength="200" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 94px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.sector")%><font color="red">*</font>
                    </div>
                    <div style="width: 130px; float: left;">                    
                        <div style="width: 220px; float: left;">
                            <input id="codSector" name="codSector" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descSector" name="descSector" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorSector" name="anchorSector" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonSector" name="botonSector" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.direccion")%>
                    </div>
                    <div style="width: 540px; float: left;">
                        <input type="text" id="direccion" name="direccion" size="83" maxlength="200" class="inputTexto"/>
                    </div>
                    <div style="width: 90px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.cPostal")%>
                    </div>
                    <div style="width: 80px; float: left;">
                        <input type="text" id="cpostal" name="cpostal" size="8" maxlength="5" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">

                     <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.localidad")%>
                    </div>
                    <div style="width: 350px; float: left;">
                        <input type="text" id="localidad" name="localidad" size="50" maxlength="100" class="inputTexto"/>
                    </div>
                    <div style="width: 90px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.provincia")%>
                    </div>
                    <div style="width: 100px; float: left;">                    
                        <div style="width: 220px; float: left;">
                            <input id="codProvincia" name="codProvincia" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descProvincia" name="descProvincia" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorProvincia" name="anchorProvincia" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonProvincia" name="botonProvincia" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>                
                </div>
               <div class="lineaFormulario">
                    <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.personaContacto")%>
                    </div>
                    <div style="width: 350px; float: left;">
                        <input type="text" id="personaContacto" name="personaContacto" size="50" maxlength="100" class="inputTexto"/>
                    </div>
                    <div style="width: 90px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.cargo")%>
                    </div>
                    <div style="width: 80px; float: left;">
                        <input type="text" id="puesto" name="puesto" size="40" maxlength="100" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.email")%>
                    </div>
                    <div style="width: 540px; float: left;">
                        <input type="text" id="email" name="email" size="83" maxlength="100" class="inputTexto"/>
                    </div>
                    <div style="width: 74px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.telefono")%>
                    </div>
                    <div style="width: 80px; float: left;">
                        <input type="text" id="telefono" name="telefono" size="11" maxlength="9" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.numtrab")%>
                    </div>
                    <div style="width: 240px; float: left;">
                        <input type="text" id="numTrab" name="numTrab" size="6" maxlength="100" class="inputTexto"
                               onkeyup="FormatNumber(this.value, 6, 0, this.id);"/>
                    </div>
                    <div style="width: 180px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.numtrabdisc")%>
                    </div>
                    <div style="width: 80px; float: left;">
                        <input type="text" id="numTrabDisc" name="numTrabDisc" size="10" maxlength="6" class="inputTexto"
                               onkeyup="FormatNumber(this.value, 6, 0, this.id);"/>
                    </div>
                </div>
                <div class="lineaFormulario"> 
                     <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.cumple")%>
                    </div>
                    <div style="width: 240px; float: left;">                    
                        <div style="width: 220px; float: left;">
                            <input id="codLismi" name="codLismi" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descLismi" name="descLismi" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorLismi" name="anchorLismi" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonLismi" name="botonLismi" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div> 
                    <div style="width: 100px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.resultado")%><font color="red">*</font>
                    </div>
                    <div style="width: 240px; float: left;">                      
                        <div style="width: 220px; float: left;">
                            <input id="codResultado" name="codResultado" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descResultado" name="descResultado" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorResultado" name="anchorResultado" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonResultado" name="botonResultado" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                       </div> 
                </div>

                <div class="lineaFormulario">                         
                    <div style="width: 80px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.fecvisita")%><font color="red">*</font>
                    </div>
                    <div style="width: 240px; float: left;">
                         <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecVisita" name="fecVisita" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaVisita(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFecVisita" name="calFecVisita" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.fecvisita")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>   
                     <div style="width: 100px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.observaciones")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <textarea type="text" id="observaciones" name="observaciones" cols="56" class="inputTexto" ></textarea>
                    </div>    
                </div>
                <div class="lineaFormulario">
                    <div style="width: 130px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.nifProspector")%><font color="red">*</font>
                    </div>
                    <div style="width: 101px; float: left;">
                        <input type="text" id="nifProspector" name="nifProspector" size="10" maxlength="10" class="inputTexto" />
                    </div>
                </div> 
               <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardarVisita();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
            </div>
        </form>
        <div id="popupcalendar" class="text"></div>

        <script type="text/javascript">
            var comboSector;
            var comboProvincia;
            var comboLismi;
            var comboResultado;

        </script>
    </body>
</html>
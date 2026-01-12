<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaSegPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaInsPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
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

        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);

        //Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
        String numExpediente    = request.getParameter("numero");
        String tiposeg    = request.getParameter("tiposeg");
        String colectivosegs    = request.getParameter("colectivo");
        if (colectivosegs==null ||colectivosegs.equals("null")) colectivosegs="";
        String sexosegs    = request.getParameter("sexo");
        if (sexosegs==null || sexosegs.equals("null")) sexosegs = "";
        String prepsegs    = request.getParameter("preparador");
        if (prepsegs==null || prepsegs.equals("null")) prepsegs = "";
        String nifprep = (String) request.getAttribute("nifprep");
        EcaSegPreparadoresVO segModif = (EcaSegPreparadoresVO)request.getAttribute("seguimientoModif");    
        if (segModif !=null) {tiposeg=segModif.getTipo().toString();
            if (prepsegs==null || prepsegs.equals("null")) prepsegs=segModif.getJusPreparadoresCod().toString();
        }
        session.removeAttribute("seguimientoModif");
        Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;


        // lista tipo contrato
        List<SelectItem> listaTiposContratos = new ArrayList<SelectItem>();
        if(request.getAttribute("lstTipoContrato") != null)
            listaTiposContratos = (List<SelectItem>)request.getAttribute("lstTipoContrato");

        // lista tipo discapacidad
        List<SelectItem> listaTiposDisc = new ArrayList<SelectItem>();
        if(request.getAttribute("lstTipoDiscapacidad") != null)
            listaTiposDisc = (List<SelectItem>)request.getAttribute("lstTipoDiscapacidad");

        // lista gravedad
        List<SelectItem> listaGravedad = new ArrayList<SelectItem>();
        if(request.getAttribute("lstGravedad") != null)
            listaGravedad = (List<SelectItem>)request.getAttribute("lstGravedad");

        String lcodTipoContrato = "";
        String ldescTipoContrato = "";

        String lcodTipoDisc = "";
        String ldescTipoDisc = "";

        String lcodGravedad = "";
        String ldescGravedad = "";

        if (listaTiposContratos != null && listaTiposContratos.size() > 0) 
        {
            int i;
            SelectItem tc = null;
            for (i = 0; i < listaTiposContratos.size() - 1; i++) 
            {
                tc = (SelectItem) listaTiposContratos.get(i);
                lcodTipoContrato += "\"" + tc.getId().toString() + "\",";
                ldescTipoContrato += "\"" + escape(tc.getLabel()) + "\",";
            }
            tc = (SelectItem) listaTiposContratos.get(i);
            lcodTipoContrato += "\"" + tc.getId().toString() + "\"";
            ldescTipoContrato += "\"" + escape(tc.getLabel()) + "\"";
        }

        if (listaTiposDisc != null && listaTiposDisc.size() > 0) 
        {
            int i;
            SelectItem td = null;
            for (i = 0; i < listaTiposDisc.size() - 1; i++) 
            {
                td = (SelectItem) listaTiposDisc.get(i);
                lcodTipoDisc += "\"" + td.getId().toString() + "\",";
                ldescTipoDisc += "\"" + escape(td.getLabel()) + "\",";
            }
            td = (SelectItem) listaTiposDisc.get(i);
            lcodTipoDisc += "\"" + td.getId().toString() + "\"";
            ldescTipoDisc += "\"" + escape(td.getLabel()) + "\"";
        }

        if (listaGravedad != null && listaGravedad.size() > 0) 
        {
            int i;
            SelectItem tg = null;
            for (i = 0; i < listaGravedad.size() - 1; i++) 
            {
                tg = (SelectItem) listaGravedad.get(i);
                lcodGravedad += "\"" + tg.getId().toString() + "\",";
                ldescGravedad += "\"" + escape(tg.getLabel()) + "\",";
            }
            tg = (SelectItem) listaGravedad.get(i);
            lcodGravedad += "\"" + tg.getId().toString() + "\"";
            ldescGravedad += "\"" + escape(tg.getLabel()) + "\"";
        }

        String tituloPagina = "";
        if(consulta)
        {
            if(tiposeg != null && tiposeg.equals("0"))
            {
                tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.consultaSegPrep.tituloPagina");
            }
            else
            {
                tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.consultaInsPrep.tituloPagina");
            }
        }
        else
        {
            if(segModif != null)
            {
                if(tiposeg != null && tiposeg.equals("0"))
                {
                    tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.modifSegPrep.tituloPagina");
                }
                else
                {
                    tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.modifInsPrep.tituloPagina");
                }
            }
            else
            {
                if(tiposeg != null && tiposeg.equals("0"))
                {
                    tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.nuevoSegPrep.tituloPagina");
                }
                else
                {
                    tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.nuevoInsPrep.tituloPagina");
                }
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
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

    <script type="text/javascript">

        var nuevo = true;
        var mensajeValidacion = '';
        
        var colectivos = new Array();

        //LISTAS DE VALORES PARA LOS COMBOS
        var codTipoContrato = [<%=lcodTipoContrato%>];
        var descTipoContrato = [<%=ldescTipoContrato%>];

        var codTipoDisc = [<%=lcodTipoDisc%>];
        var descTipoDisc = [<%=ldescTipoDisc%>];

        var codGravedad = [<%=lcodGravedad%>];
        var descGravedad = [<%=ldescGravedad%>];

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
                String[] colectivos = ConstantesMeLanbide35.COMBINACIONES_COLECTIVO;
                for(int i = 0; i < colectivos.length; i++)
                {
                %>
                    colectivos[<%=i%>] = '<%=colectivos[i]%>';
                <%
                }
                %>
                <%
                if(segModif != null)
                { 
                %> nuevo = false;                        
                    document.getElementById('nif').value = '<%=segModif.getNif() != null ? segModif.getNif().toUpperCase() : "" %>';
                    document.getElementById('nomApel').value = '<%=segModif.getNombre() != null ? segModif.getNombre().toUpperCase() : "" %>';
                    document.getElementById('fechaInicio').value = '<%=segModif.getFecIni() != null ? format.format(segModif.getFecIni()) : "" %>';
                    document.getElementById('fechaFin').value = '<%=segModif.getFecFin() != null ? format.format(segModif.getFecFin()) : "" %>';               
                    document.getElementById('horasContrato').value = '<%=segModif.getHorasCont() != null ? segModif.getHorasCont() : "" %>';               
                    document.getElementById('fecNacimiento').value = '<%=segModif.getFecNacimiento() != null ? format.format(segModif.getFecNacimiento()) : "" %>';               


                    <%
                    if(segModif.getSexo() != null && segModif.getSexo().equals(ConstantesMeLanbide35.CODIGOS_SEXO.MUJER))
                    {
                    %>
                        document.getElementById('sexo1').checked = true;
                    <%
                    }
                    else if(segModif.getSexo() != null && segModif.getSexo().equals(ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE))
                    {
                    %>
                        document.getElementById('sexo0').checked = true;
                    <%
                    }
                    %>

                    document.getElementById('porcJorn').value = '<%=segModif.getPorcJornada() != null ? segModif.getPorcJornada() : "" %>';               
                    document.getElementById('empresa').value = '<%=segModif.getEmpresa() != null ? segModif.getEmpresa().toUpperCase() : "" %>';               
                    //document.getElementById('persContacto').value = '<%//=segModif.getNomPersContacto() != null ? segModif.getNomPersContacto().toUpperCase() : "" %>';
                    /*<% if (tiposeg.equals("0")) { %>
                    document.getElementById('fecSeguimiento').value = '<%=segModif.getFecSeguimiento() != null ? format.format(segModif.getFecSeguimiento()) : "" %>'; 
                    <% } %>*/
                    
                    document.getElementById('nifPreparador').value = '<%=segModif.getNifPreparador() != null ? segModif.getNifPreparador().toUpperCase() : "" %>';
                    document.getElementById('nifPreparador').disabled = true;
                    document.getElementById('codTipoContrato').value = '<%=segModif.getTipoContrato() != null ? segModif.getTipoContrato() : ""%>';
                    document.getElementById('codTipoDisc').value = '<%=segModif.getTipoDiscapacidad() != null ? segModif.getTipoDiscapacidad() : ""%>';
                    document.getElementById('codGravedad').value = '<%=segModif.getGravedad() != null ? segModif.getGravedad() : ""%>';
                <%
                } else {
                    if ( nifprep !=null) { %>
                         document.getElementById('nifPreparador').value = '<%=nifprep %>';
                         document.getElementById('nifPreparador').disabled = true;
                    <% } 
                }
                %>


                <%
                    if(consulta == true)
                    {
                %>
                        //Deshabilito todos los campos
                        document.getElementById('nif').disabled = true;
                        document.getElementById('nomApel').disabled = true;
                        document.getElementById('fechaInicio').disabled = true;
                        document.getElementById('fechaFin').disabled = true;              
                        document.getElementById('horasContrato').disabled = true;               
                        document.getElementById('fecNacimiento').disabled = true;               
                        document.getElementById('sexo0').disabled = true;       
                        document.getElementById('sexo1').disabled = true;    
                        document.getElementById('codTipoDisc').disabled = true;             
                        document.getElementById('descTipoDisc').disabled = true;             
                        document.getElementById('codGravedad').disabled = true;
                        document.getElementById('descGravedad').disabled = true;
                        document.getElementById('codTipoContrato').disabled = true;             
                        document.getElementById('descTipoContrato').disabled = true;             
                        document.getElementById('porcJorn').disabled = true;              
                        document.getElementById('empresa').disabled = true;        
                        //document.getElementById('persContacto').disabled = true;
                        document.getElementById('nifPreparador').disabled = true;
                        /*<% if (tiposeg.equals("0")) { %>
                        document.getElementById('fecSeguimiento').disabled = true;
                        <% } %>*/
                        document.getElementById('observaciones').disabled = true;
                        document.getElementById('observaciones').className = 'readOnly';

                        document.getElementById('botonTipoDisc').style.display = 'none';
                        document.getElementById('botonGravedad').style.display = 'none';
                        document.getElementById('botonTipoContrato').style.display = 'none';
                        document.getElementById('calFecNacimiento').style.display = 'none';
                        document.getElementById('calFechaInicio').style.display = 'none';
                        document.getElementById('calFechaFin').style.display = 'none';
                         /*<% if (tiposeg.equals("0")) { %>
                        document.getElementById('calFecSeguimiento').style.display = 'none';
                        <% } %>*/
                        document.getElementById('btnAceptar').style.display = 'none';
                        document.getElementById('btnCancelar').style.display = 'none';
                        document.getElementById('btnCerrar').style.display = 'inline';
                        document.getElementById('btnFechaFin').style.display = 'none';
                        document.getElementById('btnFechaInicio').style.display = 'none';
                <%
                    }
                    else
                    {
                %>  
                        comboTipoContrato = new Combo('TipoContrato');
                        comboTipoDisc = new Combo('TipoDisc');
                        comboGravedad = new Combo('Gravedad');
                        cargarCombos();
                <%
                    }
                %>


                  reemplazarPuntosEca(document.getElementById('horasContrato'));
                  reemplazarPuntosEca(document.getElementById('porcJorn'));

                  FormatNumber(document.getElementById('horasContrato').value, 8, 2, document.getElementById('horasContrato').id);
                  FormatNumber(document.getElementById('porcJorn').value, 5, 2, document.getElementById('porcJorn').id);
                  
                  cargarDescripcionesCombos();
                  ajustarDecimalesImportes();
            }catch(err){

            }
        }

        function cargarCombos(){
            comboTipoContrato.addItems(codTipoContrato, descTipoContrato);
            comboTipoDisc.addItems(codTipoDisc, descTipoDisc);
            comboGravedad.addItems(codGravedad, descGravedad);
        }

        function cargarDescripcionesCombos(){
            var desc = "";
            var codAct = "";
            var codigo = "";

            //Tipo contrato
            codigo = '<%=segModif != null && segModif.getTipoContrato() != null ? segModif.getTipoContrato() : ""%>';
            desc = '';
            var encontrado = false;
            var i = 0; 
            if(codigo != null && codigo != '')
            {
                while(i<codTipoContrato.length && !encontrado)
                {
                    codAct = codTipoContrato[i];
                    if(codAct == codigo)
                    {
                        encontrado = true;
                        desc = descTipoContrato[i];
                    }else{
                        i++;
                    }
                }
            }
            document.getElementById('descTipoContrato').value = desc;

            //Tipo discapacidad
            codigo = '<%=segModif != null && segModif.getTipoDiscapacidad() != null ? segModif.getTipoDiscapacidad() : ""%>';
            desc = '';
            var encontrado = false;
            var i = 0; 
            if(codigo != null && codigo != '')
            {
                while(i<codTipoDisc.length && !encontrado)
                {
                    codAct = codTipoDisc[i];
                    if(codAct == codigo)
                    {
                        encontrado = true;
                        desc = descTipoDisc[i];
                    }else{
                        i++;
                    }
                }
            }
            document.getElementById('descTipoDisc').value = desc;

            //Gravedad
            codigo = '<%=segModif != null && segModif.getGravedad() != null ? segModif.getGravedad() : ""%>';
            desc = '';
            var encontrado = false;
            var i = 0; 
            if(codigo != null && codigo != '')
            {
                while(i<codGravedad.length && !encontrado)
                {
                    codAct = codGravedad[i];
                    if(codAct == codigo)
                    {
                        encontrado = true;
                        desc = descGravedad[i];
                    }else{
                        i++;
                    }
                }
            }
            document.getElementById('descGravedad').value = desc;
        }

        function mostrarCalFechaInicio(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFechaInicio").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
        }

        function mostrarCalFechaFin(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFechaFin").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
        }

        function mostrarCalFechaNacimiento(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFecNacimiento").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fecNacimiento',null,null,null,'','calFecNacimiento','',null,null,null,null,null,null,null,null,evento);
        }

        /*function mostrarCalFechaSeguimiento(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFecSeguimiento").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fecSeguimiento',null,null,null,'','calFecSeguimiento','',null,null,null,null,null,null,null,null,evento);
        }*/

        function cancelar(){
            var resultado = jsp_alerta("","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
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

        function barraProgresoSeg(valor, idBarra) {
        if(valor=='on'){
            document.getElementById(idBarra).style.visibility = 'inherit';
        }
        else if(valor=='off'){
            document.getElementById(idBarra).style.visibility = 'hidden';
        }
    }

        function guardarSeguimiento(){
            if(validarDatosSeg()){
                
                var encontrado = false;
                var combColectivo = document.getElementById('codTipoDisc').value + document.getElementById('codGravedad').value;
                for(var i = 0; i < colectivos.length; i++){
                    if(combColectivo == colectivos[i]){
                        encontrado = true;
                    }
                }
                var resultado = 1;
                if(!encontrado){
                    resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.colectivoNoDefinido")%>');
                }
                
                if(resultado == 1){

                    document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoSeg('on', 'barraProgresoNuevoSeguimientoPreparador');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();

                    var sexo;
                    if(document.getElementById('sexo0').checked)
                        sexo = document.getElementById('sexo0').value;
                    else if(document.getElementById('sexo1').checked)
                        sexo = document.getElementById('sexo1').value;

                    parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>'
                        //+'&idPrep=<%=segModif != null && segModif.getJusPreparadoresCod() != null ? segModif.getJusPreparadoresCod() : ""%>'
                        //+'&idPrep=<%=prepsegs%>'
                        +'&idPrep=<%=segModif != null && segModif.getJusPreparadoresCod() != null ? segModif.getJusPreparadoresCod() : prepsegs%>'
                        +'&idSeg=<%=segModif != null && segModif.getSegPreparadoresCod() != null ? segModif.getSegPreparadoresCod() : ""%>'
                        +'&nif='+escape(document.getElementById('nif').value)
                        +'&nomApel='+escape(document.getElementById('nomApel').value)
                        +'&feIni='+document.getElementById('fechaInicio').value
                        +'&feFin='+document.getElementById('fechaFin').value
                        +'&horasCont='+convertirANumero(document.getElementById('horasContrato').value)               
                        +'&fecNacimiento='+document.getElementById('fecNacimiento').value
                        +'&sexo='+sexo
                        +'&tipoDiscap='+document.getElementById('codTipoDisc').value
                        +'&gravedad='+document.getElementById('codGravedad').value
                        +'&tipoCont='+document.getElementById('codTipoContrato').value
                        +'&porcJorn='+convertirANumero(document.getElementById('porcJorn').value)
                        +'&empresa='+escape(document.getElementById('empresa').value)
                       // +'&perscont='+escape(document.getElementById('persContacto').value)
                         /*<% if (tiposeg.equals("0")) { %>
                            parametros = parametros +'&fecSeguimiento='+document.getElementById('fecSeguimiento').value
                        <% } %>*/
                           parametros = parametros +'&observaciones='+escape(document.getElementById('observaciones').value)
                        +'&nifPreparador='+document.getElementById('nifPreparador').value
                        +'&tiposeg='+'<%=tiposeg %>'
                        +'&prepsegs='+'<%=prepsegs %>'
                        +'&sexosegs='+'<%=sexosegs %>'
                        +'&colectivosegs='+'<%=colectivosegs %>'
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
                    var msgValidacion = '';
                    var listaPreparadores = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var j;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaPreparadores[j] = codigoOperacion;
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
                                else if(hijosFila[cont].nodeName=="NIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF:FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF:FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF:FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = '-';
                                    }
                                }      
                                else if(hijosFila[cont].nodeName=="NOMBRE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NOMBREAPEL:FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NOMBREAPEL:FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NOMBREAPEL:FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                                    }
                                }
                                <% if (tiposeg.equals("0")){ %>
                                     if(hijosFila[cont].nodeName=="FECHA_SEGUIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = '-';
                                    }                            
                                }
                                <% } %>
                                if(hijosFila[cont].nodeName=="FECHA_NACIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = '-';
                                    }   
                                }
                                else if(hijosFila[cont].nodeName=="SEXO"){
                                     nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_SEXO:FilaInsPreparadoresVO.POS_CAMPO_SEXO %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_SEXO:FilaInsPreparadoresVO.POS_CAMPO_SEXO %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_SEXO:FilaInsPreparadoresVO.POS_CAMPO_SEXO %>] = '-';
                                    }   
                                }
                                else if(hijosFila[cont].nodeName=="COLECTIVO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_SEXO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_DISCAPACIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD:FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD:FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD:FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD %>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="GRAVEDAD"){
                                   nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD:FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD:FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD:FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD %>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO:FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO  %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO:FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO:FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO %>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="PORC_JORNADA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_PORCJORN:FilaInsPreparadoresVO.POS_CAMPO_PORCJORN %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_PORCJORN:FilaInsPreparadoresVO.POS_CAMPO_PORCJORN %>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_PORCJORN:FilaInsPreparadoresVO.POS_CAMPO_PORCJORN%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO %>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO:FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO %>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN:FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN:FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN:FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="NIF_PREPARADOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR:FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR:FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR:FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="EMPRESA"){
                                   nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_EMPRESA:FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_EMPRESA:FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_EMPRESA:FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = '-';
                                    } 
                                }
                               /* else if(hijosFila[cont].nodeName=="PERS_CONTACTO"){
                                   nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%//=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_CONTACTO:FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%//=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_CONTACTO:FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%//=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_CONTACTO:FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = '-';
                                    } 
                                }*/
                                else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES:FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES:FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES:FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES:FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES:FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=tiposeg.equals("0")?FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES:FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = '-';
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
                                    <% if (tiposeg.equals("0")) {%>
                                        fila[17] = listaErrores;
                                    <% }else  { %>fila[17] = listaErrores;<% } %>
                                }

                            }
                            <% if (tiposeg.equals("0")) { %>
                                fila[18] = camposErrores;
                            <% } else {%>fila[18] = camposErrores;
                            <% } %>
                            listaPreparadores[j] = fila;
                            fila = new Array();
                        }else if(hijos[j].nodeName=="VALIDACION"){                            
                            msgValidacion = hijos[j].childNodes[0].nodeValue;
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                        if(codigoOperacion=="0"){
                            window.returnValue =  listaPreparadores;
                            barraProgresoSeg('off', 'barraProgresoNuevoSeguimientoPreparador');
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",msgValidacion);
                        }else if(codigoOperacion=="5"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.NifPreparadorNoEncontrado")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch

                    barraProgresoSeg('off', 'barraProgresoNuevoSeguimientoPreparador');
                }
            }else{
                jsp_alerta("A", escape(mensajeValidacion));
            }
        }

        function validarDatosSeg(){
            var nif = document.getElementById('nif').value;
            if(nif == null || nif == ''){
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nifVacio")%>';
                return false;
            }
            var nomApel = document.getElementById('nomApel').value;
            if(nomApel == null || nomApel == ''){
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nomApelVacio")%>';
                return false;
            }

            var nifPreparador = document.getElementById('nifPreparador').value;
            if(nifPreparador == null || nifPreparador == ''){
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nifVacio")%>';
                return false;
            }

            var correcto = true;
            mensajeValidacion = '';
                
                if(!validarNIFEca(document.getElementById('nif'))){
                    document.getElementById('nif').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        var letra = calcularLetraNifEca(document.getElementById('nif').value);
                        if(letra != ''){
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nifIncorrecto")%>'+' '+'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.letraNifDeberiaSer")%>'+letra;
                        }else{
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nifIncorrecto")%>';
                        }
                    }
                    correcto = false;
                }else{
                    document.getElementById('nif').removeAttribute("style");
                }
                  

            if(!comprobarCaracteresEspecialesEca(nomApel)){
                document.getElementById('nomApel').style.border = '1px solid red';
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.nomApelCaracteresEspeciales")%>';
                return false;
            }else{
                document.getElementById('nomApel').removeAttribute("style");
            }

            if(nuevo){
                if(document.getElementById('fechaInicio').value == ''){
                    document.getElementById('fechaInicio').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaIniIncorrecto")%>';
                    correcto = false;
                }
                /*if(document.getElementById('fechaFin').value == ''){
                    document.getElementById('fechaFin').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaFinIncorrecto")%>';
                    correcto = false;
                }*/

                <%--if(!validarFechaEca(document.forms[0], document.getElementById('fechaInicio'))){
                    document.getElementById('fechaInicio').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaIniIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaInicio').removeAttribute('style');
                }

                if(!validarFechaEca(document.forms[0], document.getElementById('fechaFin'))){
                    document.getElementById('fechaFin').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaFinIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaFin').removeAttribute('style');
                }--%>
                if(correcto){
                    var feIni = document.getElementById('fechaInicio').value;
                    var feFin = document.getElementById('fechaFin').value;
                    if(feIni != '' && feFin != ''){
                        var array_fecha_ini = feIni.split('/');
                        var diaIni = array_fecha_ini[0];
                        var mesIni = array_fecha_ini[1];
                        var anoIni = array_fecha_ini[2];
                        var dIni = new Date(anoIni, mesIni-1, diaIni, 0, 0, 0, 0);

                        var array_fecha_fin = feFin.split('/');
                        var diaFin = array_fecha_fin[0];
                        var mesFin = array_fecha_fin[1];
                        var anoFin = array_fecha_fin[2];
                        var dFin = new Date(anoFin, mesFin-1, diaFin, 0, 0, 0, 0);


                        var n1 = dFin.getTime();
                        var n2 = dIni.getTime();
                        var result = n1 - n2;
                        if(result < 0){
                            document.getElementById('fechaInicio').style.border = '1px solid red';
                            document.getElementById('fechaFin').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaIniPosteriorFin")%>';
                            correcto = false;
                        }
                        else{ //fecinicio contrato anterior al año del expediente
                            var ejercicio ='<%=MeLanbide35Utils.getEjercicioDeExpediente(numExpediente)%>';
                            <% if (tiposeg.equals("0")) { %>
                                if(anoIni >= ejercicio){
                                    correcto = false;
                                    document.getElementById('fechaInicio').style.border = '1px solid red';
                                    if(mensajeValidacion == '')
                                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.preparadores.fechaIniAnoIncorrecto")%>';
                                }else{
                                    document.getElementById('fechaInicio').removeAttribute('style');
                                }  
                            <% }else { %>
                                if(anoIni != ejercicio){
                                    correcto = false;
                                    document.getElementById('fechaInicio').style.border = '1px solid red';
                                    if(mensajeValidacion == '')
                                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.preparadores.fechaIniAnoIncorrectoIns")%>';
                                }else{
                                    document.getElementById('fechaInicio').removeAttribute('style');
                                }  
                            <% } %>
                        }
                    }
                }
                /*<% if (tiposeg.equals("0")) { %>
                if(document.getElementById('fecSeguimiento').value == ''){
                    document.getElementById('fecSeguimiento').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaSeguimientoIncorrecto")%>';
                    correcto = false;
                }
                <% } %>*/
                if(document.getElementById('fecNacimiento').value == ''){
                    document.getElementById('fecNacimiento').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.fechaNacimientoIncorrecto")%>';
                    correcto = false;
                }
            }
            //porcentaje jornada 
            var porc = 0.00;    
            if(document.getElementById('porcJorn').value != ''){
                try{
                    if(!validarNumericoDecimalEca(document.getElementById('porcJorn'), 5, 2)){
                        correcto = false;
                        document.getElementById('porcJorn').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.porcJornadaIncorrecto")%>';
                        }
                    }else{
                        document.getElementById('porcJorn').removeAttribute('style');
                        porc = parseFloat(convertirANumero(document.getElementById('porcJorn').value));
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('porcJorn').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.porcJornadaIncorrecto")%>';
                }
            }else if (nuevo) {
                     correcto = false;
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.porcJornadaIncorrecto")%>';
            }

            //Horas contrato        
            var hc = 0.0;    
            if(document.getElementById('horasContrato').value != ''){
                try{
                    if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                        correcto = false;
                        document.getElementById('horasContrato').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                    }else{
                        document.getElementById('horasContrato').removeAttribute('style');
                        hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }
            }else if (nuevo) {
                    correcto = false;
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.horasContratoIncorrecto")%>';
            }

            //sexo
            if (document.getElementById('sexo0').checked == false && document.getElementById('sexo1').checked == false){
                 correcto = false;
                 document.getElementById('sexo0').style.border = '1px solid red';
                 document.getElementById('sexo1').style.border = '1px solid red';
                 if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.sexoIncorrecto")%>';
            }

            //tipo discapacidad
            if (nuevo){
                if(document.getElementById('codTipoDisc').value == ''){
                     correcto = false;
                     document.getElementById('codTipoDisc').style.border = '1px solid red';
                     if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.tipoDiscapacidadVacio")%>';

                }else document.getElementById('codTipoDisc').removeAttribute("style");
            }

            //tipo gravedad
            if (nuevo){
                if(document.getElementById('codGravedad').value == ''){
                     correcto = false;
                     document.getElementById('codGravedad').style.border = '1px solid red';
                     if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.tipoGravedadVacio")%>';

                }else document.getElementById('codGravedad').removeAttribute("style");
            }

            //tipo contrato
            if (nuevo){
                if(document.getElementById('codTipoContrato').value == ''){
                     correcto = false;
                     document.getElementById('codTipoContrato').style.border = '1px solid red';
                     if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.tipoContratoVacio")%>';

                }else document.getElementById('codTipoContrato').removeAttribute("style");
            }        

            //empresa    
            if (nuevo){
                if(document.getElementById('empresa').value == null || document.getElementById('empresa').value == ''){
                    document.getElementById('empresa').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.empresaVacio")%>';
                    correcto = false;
                }
                if(!comprobarCaracteresEspecialesEca(document.getElementById('empresa').value)){
                    document.getElementById('empresa').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.seguimiento.empresaCaracteresEspeciales")%>';
                    correcto = false;
                }else{
                    document.getElementById('empresa').removeAttribute("style");
                }
            }


            return correcto;

            }
            
        function ajustarDecimalesImportes(){
                                
            var campo;
            campo = document.getElementById('porcJorn');
            ajustarDecimalesCampo(campo, 2);
            
            campo = document.getElementById('horasContrato');
            ajustarDecimalesCampo(campo, 2);
            
            /*var f;
            var v;

            //% Jornada
            v = document.getElementById('porcJorn').value;
            v = convertirANumero(v);
            f = parseFloat(v);
            f = ajustarDecimalesEca(f, 2);
            if(!isNaN(f)){
                document.getElementById('porcJorn').value = f;
                reemplazarPuntosEca(document.getElementById('porcJorn'));
                document.getElementById('porcJorn').removeAttribute("style");
            }

            //% Jornada
            v = document.getElementById('horasContrato').value;
            v = convertirANumero(v);
            f = parseFloat(v);
            f = ajustarDecimalesEca(f, 2);
            if(!isNaN(f)){
                document.getElementById('horasContrato').value = f;
                reemplazarPuntosEca(document.getElementById('horasContrato'));
                document.getElementById('horasContrato').removeAttribute("style");
            }*/
        }

    </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla etiqueta">
        <form>
            <div id="barraProgresoNuevoSeguimientoPreparador" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                        <%=tiposeg.equals("0")?meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.datosSeguimiento"):meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.datosInsercion")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 36px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.nif")%><font color="red">*</font>
                    </div>
                    <div style="width: 101px; float: left;">
                        <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto"/>
                    </div>
                    <div style="width: 140px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.nomApel")%><font color="red">*</font>
                    </div>
                    <div style="width: 520px; float: left;">
                        <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fecnacimiento")%><font color="red">*</font>
                    </div>
                    <div style="width: 125px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecNacimiento" name="fecNacimiento" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimiento(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFecNacimiento" name="calFecNacimiento" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fecnacimiento")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                    <div style="width: 58px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.sexo")%><font color="red">*</font>
                    </div>
                    <div style="width: 140px; float: left;">
                        <input type="radio" id="sexo0" name="sexo" value="0" class="inputTexto"/> <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimiento.sexo0")%>
                        <input type="radio" id="sexo1" name="sexo" value="1" class="inputTexto"/> <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimiento.sexo1")%>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 130px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.tipoDiscapacidad")%><font color="red">*</font>
                    </div>
                    <div style="width: 210px; float: left;">   
                        <input id="codTipoDisc" name="codTipoDisc" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descTipoDisc" name="descTipoDisc" type="text" class="inputTexto" size="22" readonly >
                         <a id="anchorTipoDisc" name="anchorTipoDisc" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoDisc" name="botonTipoDisc" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                     <div style="width: 96px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.gravedad")%><font color="red">*</font>
                    </div>
                    <div style="width: 210px; float: left;">
                        <input id="codGravedad" name="codGravedad" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descGravedad" name="descGravedad" type="text" class="inputTexto" size="22" readonly >
                         <a id="anchorGravedad" name="anchorGravedad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonGravedad" name="botonGravedad" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 130px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.tipoContrato")%><font color="red">*</font>
                    </div>
                    <div style="width: 210px; float: left;">
                        <input id="codTipoContrato" name="codTipoContrato" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                        <input id="descTipoContrato" name="descTipoContrato" type="text" class="inputTexto" size="22" readonly >
                         <a id="anchorTipoContrato" name="anchorTipoContrato" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipoContrato" name="botonTipoContrato" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div>
                    <div style="width: 96px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.porcjornada")%><font color="red">*</font>
                    </div>
                    <div style="width: 210px; float: left;">
                        <input type="text" id="porcJorn" name="porcJorn" size="5" maxlength="5" class="inputTexto textoNumerico" 
                              onkeyup="FormatNumber(this.value, 5, 2, this.id);" onblur="ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fechaInicio")%><font color="red">*</font>
                    </div>
                    <div style="width: 248px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaInicio" name="calFechaInicio" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fechaFin")%><!--<font color="red">*</font>-->
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFin" name="fechaFin" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaFin(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaFin" name="calFechaFin" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                </div>
                <div class="lineaFormulario">                         
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.horas")%><font color="red">*</font>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="11" class="inputTexto textoNumerico" 
                               onkeyup="FormatNumber(this.value, 8, 2, this.id);" onblur="ajustarDecimalesImportes();"/>
                    </div>                
                </div>     
                <div class="lineaFormulario">                         
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.empresa")%><font color="red">*</font>
                    </div>
                    <div style="width: 240px; float: left;">
                        <input type="text" id="empresa" name="empresa" size="35" maxlength="200" class="inputTexto" />
                    </div>   
                     <!--div style="width: 100px; float: left;">
                        <%//=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.persContacto")%>
                    </div>
                    <div style="width: 100px; float: left;">
                        <input type="text" id="persContacto" name="persContacto" size="56" maxlength="200" class="inputTexto" />
                    </div-->    
                </div>
                <!--<div class="lineaFormulario">                         
                     <% if (tiposeg.equals("0")) { %>
                    <div style="width: 94px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fecseguimiento")%><font color="red">*</font>
                    </div>                    
                    <div style="width: 240px; float: left;">
                         <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecSeguimiento" name="fecSeguimiento" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaSeguimiento(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFecSeguimiento" name="calFechaFin" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.fecseguimiento")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>  
                         <% } %>   
                </div>-->
                <div class="lineaFormulario">
                    <div style="width: 93px; float: left; margin-right: 1px;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.nifPreparador")%><font color="red">*</font>
                    </div>
                    <div style="width: 240px; float: left;">
                        <input type="text" id="nifPreparador" name="nifPreparador" size="10" maxlength="10" class="inputTexto" />
                    </div>
                     <div style="width: 100px; float: left;">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.seguimientos.observaciones")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <textarea type="text" id="observaciones" name="observaciones" cols="56" class="inputTexto" rows="4"><%=segModif != null && segModif.getObservaciones() != null ? segModif.getObservaciones().toUpperCase() : "" %></textarea>
                    </div> 
                </div> 
               <div class="botonera" style="padding-top: 20px;">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardarSeguimiento();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
            </div>
        </form>
        <div id="popupcalendar" class="text"></div>

    <script type="text/javascript">
        var comboTipoContrato;    
        var comboTipoDisc;    
        var comboGravedad;
    </script>
    </body>
</html>
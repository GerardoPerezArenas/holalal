<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.i18n.MeLanbide29I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.vo.ContratoRenovacionPlantillaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide29.vo.PersonaContratoRenovacionPlantillaVO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            int idiomaUsuario = 1;
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
            //Clase para internacionalizar los mensajes de la aplicaci�n.
            MeLanbide29I18n meLanbide29I18n = MeLanbide29I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            String fechaNacimiento;
            String nuevo = (String)request.getAttribute("nuevo");

            //LISTAS PARA LOS COMBOS
            List<ValorCampoDesplegableModuloIntegracionVO> listaEstudios = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaEstudios") != null)
                listaEstudios = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaEstudios");
            List<ValorCampoDesplegableModuloIntegracionVO> listaColectivos = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaColectivos") != null)
                listaColectivos = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaColectivos");
            List<ValorCampoDesplegableModuloIntegracionVO> listaTipConDur = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaTipConDur") != null)
                listaTipConDur = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaTipConDur");
            List<ValorCampoDesplegableModuloIntegracionVO> listaTipoJornada = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaTipoJornada") != null)
                listaTipoJornada = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaTipoJornada");
            List<ValorCampoDesplegableModuloIntegracionVO> listaCNOE = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaCNOE") != null)
                listaCNOE = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaCNOE");

            
            String lcodEstudios = "";
            String ldescEstudios = "";
            String lcodColectivos = "";
            String ldescColectivos = "";
            String lcodTipConDur = "";
            String ldescTipConDur = "";
            String lcodTipoJornada = "";
            String ldescTipoJornada = "";
            String lcodCNOE = "";
            String ldescCNOE = "";
            String mensajeProgreso = "";
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            
            // Estudios
            if (listaEstudios != null && listaEstudios.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO est = null;
                for (i = 0; i < listaEstudios.size() - 1; i++) 
                {
                    est = (ValorCampoDesplegableModuloIntegracionVO) listaEstudios.get(i);
                    lcodEstudios += "\"" + est.getCodigo() + "\",";
                    ldescEstudios += "\"" + escape(est.getDescripcion()) + "\",";
                }
                est = (ValorCampoDesplegableModuloIntegracionVO) listaEstudios.get(i);
                lcodEstudios += "\"" + est.getCodigo() + "\"";
                ldescEstudios += "\"" + escape(est.getDescripcion()) + "\"";
            }

            //Colectivos
            if(listaColectivos != null && listaColectivos.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO col = null;
                for(i = 0; i < listaColectivos.size() - 1; i++)
                {
                    col = (ValorCampoDesplegableModuloIntegracionVO) listaColectivos.get(i);
                    lcodColectivos += "\"" + col.getCodigo() + "\",";
                    ldescColectivos += "\"" + escape(col.getDescripcion()) + "\",";
                }
                col = (ValorCampoDesplegableModuloIntegracionVO) listaColectivos.get(i);
                lcodColectivos += "\"" + col.getCodigo() + "\"";
                ldescColectivos += "\"" + escape(col.getDescripcion()) + "\"";
            }

            //Tip. Con. Dur.
            if(listaTipConDur != null && listaTipConDur.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO tip = null;
                for(i = 0; i < listaTipConDur.size() - 1; i++)
                {
                    tip = (ValorCampoDesplegableModuloIntegracionVO) listaTipConDur.get(i);
                    lcodTipConDur += "\"" + tip.getCodigo() + "\",";
                    ldescTipConDur += "\"" + escape(tip.getDescripcion()) + "\",";
                }
                tip = (ValorCampoDesplegableModuloIntegracionVO) listaTipConDur.get(i);
                lcodTipConDur += "\"" + tip.getCodigo() + "\"";
                ldescTipConDur += "\"" + escape(tip.getDescripcion()) + "\"";
            }

            //Tipo jornada
            if(listaTipoJornada != null && listaTipoJornada.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO jor = null;
                for(i = 0; i < listaTipoJornada.size() - 1; i++)
                {
                    jor = (ValorCampoDesplegableModuloIntegracionVO) listaTipoJornada.get(i);
                    lcodTipoJornada += "\"" + jor.getCodigo() + "\",";
                    ldescTipoJornada += "\"" + escape(jor.getDescripcion()) + "\",";
                }
                jor = (ValorCampoDesplegableModuloIntegracionVO) listaTipoJornada.get(i);
                lcodTipoJornada += "\"" + jor.getCodigo() + "\"";
                ldescTipoJornada += "\"" + escape(jor.getDescripcion()) + "\"";
            }

            //CNOE
            if(listaCNOE != null && listaCNOE.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO cnoe = null;
                for(i = 0; i < listaCNOE.size() - 1; i++)
                {
                    cnoe = (ValorCampoDesplegableModuloIntegracionVO) listaCNOE.get(i);
                    lcodCNOE += "\"" + cnoe.getCodigo() + "\",";
                    ldescCNOE += "\"" + escape(cnoe.getDescripcion()) + "\",";
                }
                cnoe = (ValorCampoDesplegableModuloIntegracionVO) listaCNOE.get(i);
                lcodCNOE += "\"" + cnoe.getCodigo() + "\"";
                ldescCNOE += "\"" + escape(cnoe.getDescripcion()) + "\"";
            }

            //Si es para modificar un contrato, cargo los datos
            ContratoRenovacionPlantillaVO contrato = new ContratoRenovacionPlantillaVO();
            PersonaContratoRenovacionPlantillaVO p1 = new PersonaContratoRenovacionPlantillaVO();
            PersonaContratoRenovacionPlantillaVO p2 = new PersonaContratoRenovacionPlantillaVO();
            PersonaContratoRenovacionPlantillaVO p3 = new PersonaContratoRenovacionPlantillaVO();
            if(request.getAttribute("contratoModif") != null)
            {
                contrato = (ContratoRenovacionPlantillaVO)request.getAttribute("contratoModif");
                if(contrato != null)
                {
                    List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
                    PersonaContratoRenovacionPlantillaVO act;
                    for(int i = 0; i < personas.size(); i++)
                    {
                        act = personas.get(i);
                        if(act.getCodTipoPersona() != null)
                        {
                            switch(act.getCodTipoPersona())
                            {
                                case 1:
                                    p1 = act;
                                    break;
                                case 2:
                                    p2 = act;
                                    break;
                                case 3:
                                    p3 = act;
                                    break;
                            }
                        }
                    }
                }
            }

            Boolean cerrado = request.getAttribute("cerrado") != null ? (Boolean)request.getAttribute("cerrado") : false;

            String tituloPagina = "";
            if(cerrado)
            {
                tituloPagina = meLanbide29I18n.getMensaje(idiomaUsuario,"label.tituloPagina.consultaContrato");
            }
            else
            {
                if(contrato != null)
                {
                    tituloPagina = meLanbide29I18n.getMensaje(idiomaUsuario,"label.tituloPagina.modifContrato");
                }
                else
                {
                    tituloPagina = meLanbide29I18n.getMensaje(idiomaUsuario,"label.tituloPagina.nuevoContrato");
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
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide29/melanbide29.css'/>">

        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>


        <script type="text/javascript">
            // LISTAS DE VALORES
            var codEstudios = [<%=lcodEstudios%>];
            var descEstudios = [<%=ldescEstudios%>];
            var codColectivos = [<%=lcodColectivos%>];
            var descColectivos = [<%=ldescColectivos%>];
            var codTipConDur = [<%=lcodTipConDur%>];
            var descTipConDur = [<%=ldescTipConDur%>];
            var codTipoJornada = [<%=lcodTipoJornada%>];
            var descTipoJornada = [<%=ldescTipoJornada%>];
            var codCNOE = [<%=lcodCNOE%>];
            var descCNOE = [<%=ldescCNOE%>];
            //Mensaje para las validaciones
            var mensajeError = "";
        
        function mostrarBusqueda(tipo){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide29/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),800,600,'no','no',function(result){
                    if (result != undefined){
                        if(tipo == 'PC'){
                            document.getElementById('idTerPC').value = result[0];
                            document.getElementById('txtDniPC').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPC').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PC').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PC').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                        else if(tipo == 'PA'){
                            document.getElementById('idTerPA').value = result[0];
                            document.getElementById('txtDniPA').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPA').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PA').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PA').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                        else if(tipo == 'PS'){
                            document.getElementById('idTerPS').value = result[0];
                            document.getElementById('txtDniPS').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPS').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PS').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PS').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                    }
                });
            }else{
                lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide29/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),650,600,'no','no',function(result){
                    if (result != undefined){
                        if(tipo == 'PC'){
                            document.getElementById('idTerPC').value = result[0];
                            document.getElementById('txtDniPC').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPC').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PC').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PC').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                        else if(tipo == 'PA'){
                            document.getElementById('idTerPA').value = result[0];
                            document.getElementById('txtDniPA').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPA').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PA').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PA').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                        else if(tipo == 'PS'){
                            document.getElementById('idTerPS').value = result[0];
                            document.getElementById('txtDniPS').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                            document.getElementById('txtNomPS').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                            document.getElementById('txtApe1PS').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                            document.getElementById('txtApe2PS').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        }
                    }
                });
            }
            
        }


            function mostrarCalFechaNacimientoPC(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaNacPC").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaNacimientoPC',null,null,null,'','calFechaNacPC','',null,null,null,null,null,null,null,'calcularEdadPC()',evento);
            }
            
            function mostrarCalFechaAltaPC(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaAltaPC").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaAltaPC',null,null,null,'','calFechaAltaPC','',null,null,null,null,null,null,null,'calcularDuracionContrato()',evento);
            }
            
            function mostrarCalFechaFinPC(evento){
                if(window.event)
                    evento = window.event;
                if(document.getElementById("calFechaFinPC").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]','txtFFinContratoPC',null,null,null,'','calFechaFinPC','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaNacimientoPA(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaNacPA").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaNacimientoPA',null,null,null,'','calFechaNacPA','',null,null,null,null,null,null,null,'calcularEdadPA()',evento);
            }
            
            function mostrarCalFechaAltaPA(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaAltaPA").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaAltaPA',null,null,null,'','calFechaAltaPA','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaNacimientoPS(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaNacPS").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaNacimientoPS',null,null,null,'','calFechaNacPS','',null,null,null,null,null,null,null,'calcularEdadPS()',evento);
            }
            
            function mostrarCalFechaBajaPS(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaBajaPS").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaBajaPS',null,null,null,'','calFechaBajaPS','',null,null,null,null,null,null,null,'calcularDuracionContrato()',evento);
            }

            function comprobarFecha(inputFecha) {
                if (Trim(inputFecha.value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                        jsp_alerta("A","<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
                        return false;
                    }
                }
                return true;
            }
            
            function comprobarFechaNac(inputFecha) {
                if (Trim(inputFecha.value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                        jsp_alerta("A","<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
                        return false;
                    }
                    else{
                        //La fecha de nacimiento no puede ser mayor que la actual
                        var array_fecha = inputFecha.value.split("/");
                        if(array_fecha.length == 3)
                        {
                            var dia = array_fecha[0];
                            var mes = array_fecha[1];
                            var ano = array_fecha[2];
                            var today = new Date();
                            var d = new Date(ano, mes-1, dia, 0, 0, 0, 0);
                            var n1 = today.getTime();
                            var n2 = d.getTime();
                            var result = n1 - n2;
                            /*if(n2 <= 0){
                                jsp_alerta("A","<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacPosterior")%>");
                                return false;
                            }*/
                            if(result < 0){
                                jsp_alerta("A","<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacPosterior")%>");
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
            
            function comprobarNumerico(numero){
                try{
                    if (Trim(numero.value)!='') {
                        return /^([0-9])*$/.test(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
              }
              
            function comprobarSexo(campo){
                var sexo = Trim(campo.value);
                sexo = sexo.toUpperCase();
                if(sexo != null && sexo != ''){
                    if(sexo != 'H' && sexo != 'M'){
                        campo.value = '';
                    }
                    else{
                        campo.value = sexo;
                    }
                }
            }
            
            function calcularEdadPC(){
                if(comprobarFechaNac(document.getElementById('fechaNacimientoPC'))){
                    var txtOutput = 'txtEdadPC';
                    var value = document.getElementById('fechaNacimientoPC').value;
                    calcularEdad(value, txtOutput);
                }
                else{
                    document.getElementById('txtEdadPC').value = '';
                }
            }
            
            function calcularEdadPA(){
                if(comprobarFechaNac(document.getElementById('fechaNacimientoPA'))){
                    var txtOutput = 'txtEdadPA';
                    var value = document.getElementById('fechaNacimientoPA').value;
                    calcularEdad(value, txtOutput);
                }
                else{
                    document.getElementById('txtEdadPA').value = '';
                }
            }
            
            function calcularEdadPS(){
                if(comprobarFechaNac(document.getElementById('fechaNacimientoPS'))){
                    var txtOutput = 'txtEdadPS';
                    var value = document.getElementById('fechaNacimientoPS').value;
                    calcularEdad(value, txtOutput);
                }
                else{
                    document.getElementById('txtEdadPS').value = '';
                }
            }
            
            function calcularEdad(feNac, txtOutput){
            try
            {
                if(feNac != "")
                {
                    var array_fecha = feNac.split("/");
                    if(array_fecha.length == 3)
                    {
                        var dia = array_fecha[0];
                        var mes = array_fecha[1];
                        var ano = array_fecha[2];

                        var fechaHoy = new Date();
                        var ahoraDia = fechaHoy.getDate();
                        var ahoraMes = fechaHoy.getMonth();
                        var ahoraAno = fechaHoy.getYear();

                        edad = (ahoraAno + 1900) - ano;

                        if(ahoraMes < (mes - 1)){
                            edad--;
                        }
                        if(((mes - 1) == ahoraMes) && (ahoraDia < dia)) {
                            edad--;
                        }
                        if(edad > 1900){
                            edad -= 1900;
                        }
                        document.getElementById(txtOutput).value = edad;
                    }
                }
            }catch(err){
                
            }
        }
        
        function calcularDuracionContrato(){
            //Se calcula restando la fecha de alta de la persona contratada y la fecha de baja de la persona sustituida
            var feAlta = document.getElementById('fechaAltaPC').value;
            var feBaja = document.getElementById('fechaBajaPS').value;
            if(feAlta != null && feAlta != '' && feBaja != null && feBaja != ''){
                var aAlta = feAlta.split('/');
                var aBaja = feBaja.split('/');
                var dAlta = new Date(aAlta[2], aAlta[1], aAlta[0]);
                var dBaja = new Date(aBaja[2], aBaja[1], aBaja[0]);
                var numMeses;

                var diferencia = (dBaja.getTime() - dAlta.getTime());

                if(diferencia < 0){
                    jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fAltaAntFBaja")%>');
                }

                var anos = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 365));
                diferencia -= anos * (1000 * 60 * 60 * 24 * 365);
                var meses = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 30.4375));

                numMeses = anos*12 + meses;
                if(!isNaN(numMeses) && numMeses != null && numMeses >= 0 && numMeses <= 99999)
                    document.getElementById('txtDuracionContratoPC').value = numMeses;
                else
                    document.getElementById('txtDuracionContratoPC').value = '';
            }
            else{
                document.getElementById('txtDuracionContratoPC').value = '';
            }
        }
            
            function inicio(){
                
            <%mensajeProgreso = meLanbide29I18n.getMensaje(idiomaUsuario, "msg.cargarDatos");%>
            barraProgresoContrato('on', 'barraProgresoContratoCargar');
                
                cargarCombos();
                cargarDescripcionesCombos();
                var nuevo = "<%=nuevo%>";
                if(nuevo != null && nuevo == "1"){
                    document.getElementById('divBtnBuscarPC').style.display = 'inline';
                    document.getElementById('divBtnBuscarPA').style.display = 'inline';
                    document.getElementById('divBtnBuscarPS').style.display = 'inline';
                }
                else{
                    document.getElementById('divBtnBuscarPC').style.display = 'none';
                    document.getElementById('divBtnBuscarPA').style.display = 'none';
                    document.getElementById('divBtnBuscarPS').style.display = 'none';
                }
                var feNacPC = "<%=p2 != null && p2.getFeNac() != null ? formatter.format(p2.getFeNac()) : ""%>";
                calcularEdad(feNacPC, 'txtEdadPC');
                var feNacPA = "<%=p3 != null && p3.getFeNac() != null ? formatter.format(p3.getFeNac()) : ""%>";
                calcularEdad(feNacPA, 'txtEdadPA');
                var feNacPS = "<%=p1 != null && p1.getFeNac() != null ? formatter.format(p1.getFeNac()) : ""%>";
                calcularEdad(feNacPS, 'txtEdadPS');
                calcularDuracionContrato();
                
                
                <%
                    if(cerrado == true)
                    {
                %>
                        //Deshabilito todos los campos
                        
                        //Persona contratada
                        document.getElementById('txtNomPC').disabled = true;
                        document.getElementById('txtApe1PC').disabled = true;
                        document.getElementById('txtApe2PC').disabled = true;
                        document.getElementById('fechaNacimientoPC').disabled = true;
                        document.getElementById('checkMinusvalidoPC').disabled = true;
                        document.getElementById('checkInmigPC').disabled = true;
                        document.getElementById('codEstudiosPC').disabled = true;
                        document.getElementById('descEstudiosPC').disabled = true;
                        document.getElementById('codColectivoPC').disabled = true;
                        document.getElementById('descColectivoPC').disabled = true;
                        document.getElementById('txtMesesDesempleoPC').disabled = true;
                        document.getElementById('checkPLDPC').disabled = true;
                        document.getElementById('checkRMLPC').disabled = true;
                        document.getElementById('checkOtroPC').disabled = true;
                        document.getElementById('codTipConDurPC').disabled = true;
                        document.getElementById('descTipConDurPC').disabled = true;
                        document.getElementById('fechaAltaPC').disabled = true;
                        document.getElementById('codTipoJornadaPC').disabled = true;
                        document.getElementById('descTipoJornadaPC').disabled = true;
                        document.getElementById('codCNOEPC').disabled = true;
                        document.getElementById('descCNOEPC').disabled = true;
                        document.getElementById('calFechaNacPC').style.display = 'none';
                        document.getElementById('botonEstudiosPC').style.display = 'none';
                        document.getElementById('botonColectivoPC').style.display = 'none';
                        document.getElementById('botonTipConDurPC').style.display = 'none';
                        document.getElementById('calFechaAltaPC').style.display = 'none';
                        document.getElementById('botonTipoJornadaPC').style.display = 'none';
                        document.getElementById('botonCNOEPC').style.display = 'none';
                        

                        //Datos Contrato Adicional
                        document.getElementById('txtNomPA').disabled = true;
                        document.getElementById('txtApe1PA').disabled = true;
                        document.getElementById('txtApe2PA').disabled = true;
                        document.getElementById('fechaNacimientoPA').disabled = true;
                        document.getElementById('checkMinusvalidoPA').disabled = true;
                        document.getElementById('checkInmigPA').disabled = true;
                        document.getElementById('codEstudiosPA').disabled = true;
                        document.getElementById('descEstudiosPA').disabled = true;
                        document.getElementById('codColectivoPA').disabled = true;
                        document.getElementById('descColectivoPA').disabled = true;
                        document.getElementById('txtMesesDesempleoPA').disabled = true;
                        document.getElementById('codTipConDurPA').disabled = true;
                        document.getElementById('descTipConDurPA').disabled = true;
                        document.getElementById('fechaAltaPA').disabled = true;
                        document.getElementById('codTipoJornadaPA').disabled = true;
                        document.getElementById('descTipoJornadaPA').disabled = true;
                        document.getElementById('checkPLDPA').disabled = true;
                        document.getElementById('checkRMLPA').disabled = true;
                        document.getElementById('checkOtroPA').disabled = true;
                        document.getElementById('codCNOEPA').disabled = true;
                        document.getElementById('descCNOEPA').disabled = true;
                        document.getElementById('calFechaNacPA').style.display = 'none';
                        document.getElementById('botonEstudiosPA').style.display = 'none';
                        document.getElementById('botonColectivoPA').style.display = 'none';
                        document.getElementById('botonTipConDurPA').style.display = 'none';
                        document.getElementById('calFechaAltaPA').style.display = 'none';
                        document.getElementById('botonTipoJornadaPA').style.display = 'none';
                        document.getElementById('botonCNOEPA').style.display = 'none';
                        
                        //Persona sustituida
                        document.getElementById('txtNomPS').disabled = true;
                        document.getElementById('txtApe1PS').disabled = true;
                        document.getElementById('txtApe2PS').disabled = true;
                        document.getElementById('fechaNacimientoPS').disabled = true;
                        document.getElementById('txtSexoPS').disabled = true;
                        document.getElementById('fechaBajaPS').disabled = true;
                        document.getElementById('txtReduJorPS').disabled = true;
                        document.getElementById('codCNOEPS').disabled = true;
                        document.getElementById('descCNOEPS').disabled = true;
                        document.getElementById('calFechaNacPS').style.display = 'none';
                        document.getElementById('calFechaBajaPS').style.display = 'none';
                        document.getElementById('botonCNOEPS').style.display = 'none';
                        
                        //Botones
                        document.getElementById('btnGuardarContrato').style.display = 'none';
                        document.getElementById('btnCancelarContrato').style.display = 'none';
                        document.getElementById('btnCerrar').style.display = 'inline';
                <%
                    }
                    else
                    {
                %>  
                        document.getElementById('btnCerrar').style.display = 'none';
                <%
                    }
                %>
                
                barraProgresoContrato('off', 'barraProgresoContratoCargar');
            }

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
                }
             }
             
             function getFormData(){
                if(window.FormData == undefined)
                {
                    this.processData = true;
                    this.contentType = 'application/x-www-form-urlencoded';
                    this.append = function(name, value) {
                        this[name] = value == undefined ? "" : value;
                        return true;
                    }
                }
                else
                {
                    var formdata = new FormData();
                    formdata.processData = false;
                    formdata.contentType = false;
                    return formdata;
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
        
        function cargarDescripcionesCombos(){
            
            var desc = "";
            var codAct = "";
            var codigo = "";
            
            /**PERSONA CONTRATADA**/
            //Estudios
            codigo = "<%=p2 != null && p2.getNivelEstudios() != null ? String.valueOf(p2.getNivelEstudios()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codEstudios.length; i++)
                {
                    codAct = codEstudios[i];
                    if(codAct == codigo)
                    {
                        desc = descEstudios[i];
                    }
                }
            }
            document.getElementById('descEstudiosPC').value = desc;
            
            //Colectivo
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p2 != null && p2.getColectivo() != null ? String.valueOf(p2.getColectivo()) : ""%>";
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
            document.getElementById('descColectivoPC').value = desc;
            
            //Tip. Con. Dur.
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p2 != null && p2.getTipoContrato() != null ? String.valueOf(p2.getTipoContrato()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codTipConDur.length; i++)
                {
                    codAct = codTipConDur[i];
                    if(codAct == codigo)
                    {
                        desc = descTipConDur[i];
                    }
                }
            }
            document.getElementById('descTipConDurPC').value = desc;
            
            //Tipo Jornada
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p2 != null && p2.getTipoJornada() != null ? String.valueOf(p2.getTipoJornada()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codTipoJornada.length; i++)
                {
                    codAct = codTipoJornada[i];
                    if(codAct == codigo)
                    {
                        desc = descTipoJornada[i];
                    }
                }
            }
            document.getElementById('descTipoJornadaPC').value = desc;
            
            //CNOE
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p2 != null && p2.getCnoe() != null ? String.valueOf(p2.getCnoe()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codCNOE.length; i++)
                {
                    codAct = codCNOE[i];
                    if(codAct == codigo)
                    {
                        desc = descCNOE[i];
                    }
                }
            }
            document.getElementById('descCNOEPC').value = desc;
            
            /**PERSONA CONTRATO ADICIONAL**/
            //Estudios
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p3 != null && p3.getNivelEstudios() != null ? String.valueOf(p3.getNivelEstudios()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codEstudios.length; i++)
                {
                    codAct = codEstudios[i];
                    if(codAct == codigo)
                    {
                        desc = descEstudios[i];
                    }
                }
            }
            document.getElementById('descEstudiosPA').value = desc;
            
            //Colectivo
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p3 != null && p3.getColectivo() != null ? String.valueOf(p3.getColectivo()) : ""%>";
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
            document.getElementById('descColectivoPA').value = desc;
            
            //Tip. Con. Dur.
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p3 != null && p3.getTipoContrato() != null ? String.valueOf(p3.getTipoContrato()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codTipConDur.length; i++)
                {
                    codAct = codTipConDur[i];
                    if(codAct == codigo)
                    {
                        desc = descTipConDur[i];
                    }
                }
            }
            document.getElementById('descTipConDurPA').value = desc;
            
            //Tipo Jornada
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p3 != null && p3.getTipoJornada() != null ? String.valueOf(p3.getTipoJornada()) : ""%>;";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codTipoJornada.length; i++)
                {
                    codAct = codTipoJornada[i];
                    if(codAct == codigo)
                    {
                        desc = descTipoJornada[i];
                    }
                }
            }
            document.getElementById('descTipoJornadaPA').value = desc;
            
            //CNOE
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p3 != null && p3.getCnoe() != null ? String.valueOf(p3.getCnoe()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codCNOE.length; i++)
                {
                    codAct = codCNOE[i];
                    if(codAct == codigo)
                    {
                        desc = descCNOE[i];
                    }
                }
            }
            document.getElementById('descCNOEPA').value = desc;
            
            /**PERSONA SUSTITUIDA**/
            //CNOE
            desc = "";
            codAct = "";
            codigo = "";
            codigo = "<%=p1 != null && p1.getCnoe() != null ? String.valueOf(p1.getCnoe()) : ""%>";
            if(codigo != null && codigo != "")
            {
                for(var i=0; i<codCNOE.length; i++)
                {
                    codAct = codCNOE[i];
                    if(codAct == codigo)
                    {
                        desc = descCNOE[i];
                    }
                }
            }
            document.getElementById('descCNOEPS').value = desc;
        }

        function cargarCombos() {
            //Persona Contratada
            comboEstudiosPC.addItems(codEstudios, descEstudios);
            comboColectivoPC.addItems(codColectivos, descColectivos);
            comboTipConDurPC.addItems(codTipConDur, descTipConDur);
            comboTipoJornadaPC.addItems(codTipoJornada, descTipoJornada);
            comboCNOEPC.addItems(codCNOE, descCNOE);

            //Persona Contratada Adicional
            comboEstudiosPA.addItems(codEstudios, descEstudios);
            comboColectivoPA.addItems(codColectivos, descColectivos);
            comboTipConDurPA.addItems(codTipConDur, descTipConDur);
            comboTipoJornadaPA.addItems(codTipoJornada, descTipoJornada);
            comboCNOEPA.addItems(codCNOE, descCNOE);

            //Persona sustituida
            comboCNOEPS.addItems(codCNOE, descCNOE);
        }
        
        function barraProgresoContrato(valor, idBarra) {
            if(valor=='on'){
                document.getElementById(idBarra).style.visibility = 'inherit';
            }
            else if(valor=='off'){
                document.getElementById(idBarra).style.visibility = 'hidden';
            }
        }
        
        function validarDatos(){
            mensajeError = "";
            var correcto = true;
            if(hayNifsRepetidos())
                correcto = false;
            if(!validarDatosPersonaContratada())
                correcto = false;
            if(!validarDatosPersonaAdicional())
                correcto = false;
            if(!validarDatosPersonaSustituida())
                correcto = false;
            if(!validarDuracionContrato()){
                correcto = false;
            }
            return correcto;
        }
        
        function validarFecha(inputFecha)
        {
            if (Trim(inputFecha.value)!='') {
                if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                    return false;
                }
            }
            return true;
        }
        
        function reemplazarPuntos(campo){
            try{
                var valor = campo.value;
                if(valor != null && valor != ''){
                    valor = valor.replace(/\./g,',');
                    campo.value = valor;
                }
            }
            catch(err){
            }
        }
            
        function validarNumerico(numero){
            try{
                if (Trim(numero.value)!='') {
                    return /^([0-9])*$/.test(numero.value);
                }
            }
            catch(err){
                return false;
            }
          }	
          function validarNumericoDecimal(numero){
              try{
                  if(Trim(numero.value) != ''){
                      return /^([0-9])*[,]?[0-9]*$/.test(numero.value);
                  }
              }
              catch(err){
                  return false;
              }
          }
          
          function calcularLetraNIF (campo){
            var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';
            var letra;
            var aux;
            var posicion;
            var longitud;
            var correcto=true;
            var dni = campo.value;
            longitud=dni.length;
            aux=dni.substring(longitud-1).toUpperCase();
            if ((longitud>=8)&&(longitud<10)){
                if (isNaN(aux)){	
                    posicion = dni.substring(0,longitud-1) % 23;
                    letra=cadena.charAt(posicion);
                    if (isNaN(dni.substring(0,longitud-1))) {
                            return false;
                    }
                    if(aux!=letra) {
                            return false;
                    }
                }else{
                    posicion = dni.substring(0,longitud) % 23;
                    letra=cadena.charAt(posicion);
                    if (isNaN(dni.substring(0,longitud))) {
                            return false;
                    }
                    campo.value =dni+letra;
                    return true;
                }
            }else {
              return false;
            }
            campo.value=campo.value.toUpperCase();
            return correcto;
        }

        function validarNIF (campo) {
            if (!calcularLetraNIF(campo)) {
                return false;
            }
            return true;

        }
        
        function validarDatosPersonaContratada(){
            var correcto = true;
            //Datos obligatorios
            var dni = document.getElementById('txtDniPC').value;
            if(dni == null || dni == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var nombre = document.getElementById("txtNomPC").value;
            if(nombre == null || nombre == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var ape1 = document.getElementById("txtApe1PC").value;
            if(ape1 == null || ape1 == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            /*var ape2 = document.getElementById("txtApe2PC").value;
            if(ape2 == null ||ape2 == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }*/
            var feNac = document.getElementById("fechaNacimientoPC").value;
            if(feNac == null || feNac == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var feAlta = document.getElementById("fechaAltaPC").value;
            if(feAlta == null ||feAlta == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            //DNI
            if(!validarNIF(document.getElementById('txtDniPC'))){
                document.getElementById('txtDniPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPC').value)){
                    document.getElementById('txtDniPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtDniPC').removeAttribute("style");
                }
            }
            //Nombre
            if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPC').value)){
                document.getElementById('txtNomPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtNomPC').removeAttribute("style");
            }
            //Apellido 1
            if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PC').value)){
                document.getElementById('txtApe1PC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtApe1PC').removeAttribute("style");
            }
            //Apellido 2
            if(document.getElementById('txtApe2PC').value != null && document.getElementById('txtApe2PC').value != ""){
                if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PC').value)){
                    document.getElementById('txtApe2PC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtApe2PC').removeAttribute("style");
                }
            }
            //Fecha nacimiento
            if(!validarFecha(document.getElementById('fechaNacimientoPC'))){
                document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }
            else{
                var feNac = document.getElementById('fechaNacimientoPC').value;
                var array_fecha = feNac.split("/");
                var dia = array_fecha[0];
                var mes = array_fecha[1];
                var ano = array_fecha[2];
                var today = new Date();
                var d = new Date(ano, mes-1, dia, 0, 0, 0, 0);
                var n1 = today.getTime();
                var n2 = d.getTime();
                var result = n1 - n2;
                /*if(n2 <= 0){
                    document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }*/
                if(result < 0){
                    document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('fechaNacimientoPC').removeAttribute("style");
                }
            }
            //Nivel estudios
            var nivel = document.getElementById('codEstudiosPC').value;
            if(nivel != null && nivel != ""){
                var encontrado = false;
                var i = 0;
                while(i < codEstudios.length && !encontrado){
                    if(codEstudios[i] == nivel){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codEstudiosPC').style.border = '1px solid red';
                    document.getElementById('descEstudiosPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nivelEstudiosNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codEstudiosPC').removeAttribute("style");
                    document.getElementById('descEstudiosPC').removeAttribute("style");
                }
            }
            //Colectivo
            var col = document.getElementById('codColectivoPC').value;
            if(col != null && col != ""){
                var encontrado = false;
                var i = 0;
                while(i < codColectivos.length && !encontrado){
                    if(codColectivos[i] == col){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codColectivoPC').style.border = '1px solid red';
                    document.getElementById('descColectivoPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "colectivoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codColectivoPC').removeAttribute("style");
                    document.getElementById('descColectivoPC').removeAttribute("style");
                }
            }
            //Meses desempleo
            if(document.getElementById('txtMesesDesempleoPC').value != null && document.getElementById('txtMesesDesempleoPC').value != ''){
                if(!validarNumerico(document.getElementById('txtMesesDesempleoPC'))){
                    document.getElementById('txtMesesDesempleoPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    try{
                        var meses = parseInt(document.getElementById('txtMesesDesempleoPC').value);
                        if(meses < 0 || meses > 999){
                            document.getElementById('txtMesesDesempleoPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtMesesDesempleoPC').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtMesesDesempleoPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }
                }
            }
            //Tipo Contrato
            var tipo = document.getElementById('codTipConDurPC').value;
            if(tipo != null && tipo != ""){
                var encontrado = false;
                var i = 0;
                while(i < codTipConDur.length && !encontrado){
                    if(codTipConDur[i] == tipo){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codTipConDurPC').style.border = '1px solid red';
                    document.getElementById('descTipConDurPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "tipConDurNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codTipConDurPC').removeAttribute("style");
                    document.getElementById('descTipConDurPC').removeAttribute("style");
                }
            }
            //Fecha alta
            if(!validarFecha(document.getElementById('fechaAltaPC'))){
                document.getElementById('fechaAltaPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaAltaNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }
            else{
                document.getElementById('fechaAltaPC').removeAttribute("style");
            }
            //Tipo Jornada
            var tipoJor = document.getElementById('codTipoJornadaPC').value;
            if(tipoJor != null && tipoJor != ""){
                var encontrado = false;
                var i = 0;
                while(i < codTipoJornada.length && !encontrado){
                    if(codTipoJornada[i] == tipoJor){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codTipoJornadaPC').style.border = '1px solid red';
                    document.getElementById('descTipoJornadaPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "tipoJorNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codTipoJornadaPC').removeAttribute("style");
                    document.getElementById('descTipoJornadaPC').removeAttribute("style");
                }
            }
            //Fecha fin contrato
            if(!validarFecha(document.getElementById('txtFFinContratoPC'))){
                document.getElementById('txtFFinContratoPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaFinNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }
            else{
                document.getElementById('txtFFinContratoPC').removeAttribute("style");
            }
            //Retribucion
            if(document.getElementById('txtRetribucionPC').value != null && document.getElementById('txtRetribucionPC').value != ''){
                if(!validarNumericoDecimal(document.getElementById('txtRetribucionPC'))){
                    document.getElementById('txtRetribucionPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    try
                    {
                        var retrib = parseFloat(document.getElementById('txtRetribucionPC').value);
                        if(retrib >= 10000000000 || retrib < 0){
                            document.getElementById('txtRetribucionPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtRetribucionPC').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtRetribucionPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }
                }
            }
            //CNOE
            var cnoe = document.getElementById('codCNOEPC').value;
            if(cnoe != null && cnoe != ""){
                var encontrado = false;
                var i = 0;
                while(i < codCNOE.length && !encontrado){
                    if(codCNOE[i] == cnoe){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codCNOEPC').style.border = '1px solid red';
                    document.getElementById('descCNOEPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "cnoeNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codCNOEPC').removeAttribute("style");
                    document.getElementById('descCNOEPC').removeAttribute("style");
                }
            }
            //Duracion contrato
            if(document.getElementById('txtDuracionContratoPC').value != null && document.getElementById('txtDuracionContratoPC').value != ''){
                if(!validarNumerico(document.getElementById('txtDuracionContratoPC'))){
                    document.getElementById('txtDuracionContratoPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "durContratoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    try
                    {
                        var meses = parseInt(document.getElementById('txtDuracionContratoPC').value);
                        if(meses > 99999 || meses < 0){
                            document.getElementById('txtDuracionContratoPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "durContratoFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtDuracionContratoPC').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtDuracionContratoPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "durContratoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }
                }
            }
            //Imp. Subvencion
             if(document.getElementById('txtISubvencionPC').value != null && document.getElementById('txtISubvencionPC').value != ''){
                if(!validarNumericoDecimal(document.getElementById('txtISubvencionPC'))){
                    document.getElementById('txtISubvencionPC').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "impSubNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    try
                    {
                        var imp = parseFloat(document.getElementById('txtISubvencionPC').value);
                        if(imp >= 10000000000 || imp < 0){
                            document.getElementById('txtISubvencionPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "impFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtISubvencionPC').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtISubvencionPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "impSubNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }
                }
             }
            return correcto;
        }
        
        function validarDatosPersonaAdicional(){
            var idTerPA = document.getElementById('idTerPA').value;
            if(idTerPA == null || idTerPA == ''){
                return true;
            }
            var correcto = true;
            //DNI
            if(!validarNIF(document.getElementById('txtDniPA'))){
                document.getElementById('txtDniPA').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                correcto = false;
            }else{
                if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPA').value)){
                    document.getElementById('txtDniPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtDniPA').removeAttribute("style");
                }
            }
            //Nombre
            if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPA').value)){
                document.getElementById('txtNomPA').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtNomPA').removeAttribute("style");
            }
            //Apellido 1
            if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PA').value)){
                document.getElementById('txtApe1PA').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtApe1PA').removeAttribute("style");
            }
            //Apellido 2
            if(document.getElementById('txtApe2PA').value != null && document.getElementById('txtApe2PA').value != ""){
                if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PA').value)){
                    document.getElementById('txtApe2PA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtApe2PA').removeAttribute("style");
                }
            }
            //Fecha nacimiento
            if(!validarFecha(document.getElementById('fechaNacimientoPA'))){
                document.getElementById('fechaNacimientoPA').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                correcto = false;
            }
            else{
                var feNac = document.getElementById('fechaNacimientoPA').value;
                var array_fecha = feNac.split("/");
                var dia = array_fecha[0];
                var mes = array_fecha[1];
                var ano = array_fecha[2];
                var today = new Date();
                var d = new Date(ano, mes-1, dia, 0, 0, 0, 0);
                var n1 = today.getTime();
                var n2 = d.getTime();
                var result = n1 - n2;
                /*if(n2 <= 0){
                    document.getElementById('fechaNacimientoPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }*/
                if(result < 0){
                    document.getElementById('fechaNacimientoPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('fechaNacimientoPA').removeAttribute("style");
                }
            }
            //Nivel estudios
            var nivel = document.getElementById('codEstudiosPA').value;
            if(nivel != null && nivel != ""){
                var encontrado = false;
                var i = 0;
                while(i < codEstudios.length && !encontrado){
                    if(codEstudios[i] == nivel){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codEstudiosPA').style.border = '1px solid red';
                    document.getElementById('descEstudiosPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nivelEstudiosNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codEstudiosPA').removeAttribute("style");
                    document.getElementById('descEstudiosPA').removeAttribute("style");
                }
            }
            //Colectivo
            var col = document.getElementById('codColectivoPA').value;
            if(col != null && col != ""){
                var encontrado = false;
                var i = 0;
                while(i < codColectivos.length && !encontrado){
                    if(codColectivos[i] == col){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codColectivoPA').style.border = '1px solid red';
                    document.getElementById('descColectivoPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "colectivoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codColectivoPA').removeAttribute("style");
                    document.getElementById('descColectivoPA').removeAttribute("style");
                }
            }
            //Meses desempleo
            if(document.getElementById('txtMesesDesempleoPA').value != null && document.getElementById('txtMesesDesempleoPA').value != ''){
                if(!validarNumerico(document.getElementById('txtMesesDesempleoPA'))){
                    document.getElementById('txtMesesDesempleoPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    try{
                        var meses = parseInt(document.getElementById('txtMesesDesempleoPA').value);
                        if(meses < 0 || meses > 999){
                            ocument.getElementById('txtMesesDesempleoPA').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                            correcto = false;
                        }
                        else{
                            document.getElementById('txtMesesDesempleoPA').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtMesesDesempleoPA').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mesesDesempleoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                        correcto = false;
                    }
                }
            }
            //Tipo Contrato
            var tipo = document.getElementById('codTipConDurPA').value;
            if(tipo != null && tipo != ""){
                var encontrado = false;
                var i = 0;
                while(i < codTipConDur.length && !encontrado){
                    if(codTipConDur[i] == tipo){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codTipConDurPA').style.border = '1px solid red';
                    document.getElementById('descTipConDurPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "tipConDurNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codTipConDurPA').removeAttribute("style");
                    document.getElementById('descTipConDurPA').removeAttribute("style");
                }
            }
            //Fecha alta
            if(!validarFecha(document.getElementById('fechaAltaPA'))){
                document.getElementById('fechaAltaPA').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaAltaNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                correcto = false;
            }
            else{
                document.getElementById('fechaAltaPA').removeAttribute("style");
            }
            //Tipo Jornada
            var tipoJor = document.getElementById('codTipoJornadaPA').value;
            if(tipoJor != null && tipoJor != ""){
                var encontrado = false;
                var i = 0;
                while(i < codTipoJornada.length && !encontrado){
                    if(codTipoJornada[i] == tipoJor){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codTipoJornadaPA').style.border = '1px solid red';
                    document.getElementById('descTipoJornadaPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "tipoJorNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codTipoJornadaPA').removeAttribute("style");
                    document.getElementById('descTipoJornadaPA').removeAttribute("style");
                }
            }
            //Retribucion
            if(document.getElementById('txtRetribucionPA').value != null && document.getElementById('txtRetribucionPA').value != ''){
                if(!validarNumericoDecimal(document.getElementById('txtRetribucionPA'))){
                    document.getElementById('txtRetribucionPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }else{
                    try
                    {
                        var retrib = parseFloat(document.getElementById('txtRetribucionPA').value);
                        if(retrib >= 10000000000 || retrib < 0){
                            document.getElementById('txtRetribucionPA').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribFueraRango")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtRetribucionPA').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('txtRetribucionPA').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "retribNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                        correcto = false;
                    }
                }
            }
            //CNOE
            var cnoe = document.getElementById('codCNOEPA').value;
            if(cnoe != null && cnoe != ""){
                var encontrado = false;
                var i = 0;
                while(i < codCNOE.length && !encontrado){
                    if(codCNOE[i] == cnoe){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codCNOEPA').style.border = '1px solid red';
                    document.getElementById('descCNOEPA').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "cnoeNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codCNOEPA').removeAttribute("style");
                    document.getElementById('descCNOEPA').removeAttribute("style");
                }
            }
            return correcto;
        }
        
        function validarDatosPersonaSustituida(){
            var correcto = true;
            //Datos obligatorios
            var dni = document.getElementById('txtDniPS').value;
            if(dni == null || dni == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var nombre = document.getElementById("txtNomPS").value;
            if(nombre == null || nombre == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var ape1 = document.getElementById("txtApe1PS").value;
            if(ape1 == null || ape1 == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            /*var ape2 = document.getElementById("txtApe2PS").value;
            if(ape2 == null ||ape2 == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }*/
            var feNac = document.getElementById("fechaNacimientoPS").value;
            if(feNac == null || feNac == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }
            var sexo = document.getElementById("txtSexoPS").value;
            if(sexo == null || feNac == ""){
                mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false
            }
            //DNI
            if(!validarNIF(document.getElementById('txtDniPS'))){
                document.getElementById('txtDniPS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                correcto = false;
            }else{
                if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPS').value)){
                    document.getElementById('txtDniPS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtDniPS').removeAttribute("style");
                }
            }
            //Nombre
            if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPS').value)){
                document.getElementById('txtNomPS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtNomPS').removeAttribute("style");
            }
            //Apellido 1
            if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PS').value)){
                document.getElementById('txtApe1PS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }else{
                document.getElementById('txtApe1PS').removeAttribute("style");
            }
            //Apellido 2
            if(document.getElementById('txtApe2PS').value != null && document.getElementById('txtApe2PS').value != ""){
                if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PS').value)){
                    document.getElementById('txtApe2PS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }else{
                    document.getElementById('txtApe2PS').removeAttribute("style");
                }
            }
            //Fecha nacimiento
            if(!validarFecha(document.getElementById('fechaNacimientoPS'))){
                document.getElementById('fechaNacimientoPS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                correcto = false;
            }
            else{
                var feNac = document.getElementById('fechaNacimientoPS').value;
                var array_fecha = feNac.split("/");
                var dia = array_fecha[0];
                var mes = array_fecha[1];
                var ano = array_fecha[2];
                var today = new Date();
                var d = new Date(ano, mes-1, dia, 0, 0, 0, 0);
                var n1 = today.getTime();
                var n2 = d.getTime();
                var result = n1 - n2;
                /*if(n2 <= 0){
                    document.getElementById('fechaNacimientoPS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                    correcto = false;
                }*/
                if(result < 0){
                    document.getElementById('fechaNacimientoPS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('fechaNacimientoPS').removeAttribute("style");
                }
            }
            //Sexo
            var s = document.getElementById("txtSexoPS").value;
            if(s != "H" && s != "M"){
                document.getElementById('txtSexoPS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "sexoNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                correcto = false;
            }
            else{
                document.getElementById('txtSexoPS').removeAttribute("style");
            }
            //F. Baja Empresa
            if(!validarFecha(document.getElementById('fechaBajaPS'))){
                document.getElementById('fechaBajaPS').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fechaBajaNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                correcto = false;
            }
            else{
                document.getElementById('fechaBajaPS').removeAttribute("style");
            }
            //Reducci�n de jornada
            if(document.getElementById('txtReduJorPS').value != null && document.getElementById('txtReduJorPS').value != ''){
                if(!validarNumericoDecimal(document.getElementById('txtReduJorPS'))){
                    document.getElementById('txtReduJorPS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "porReduJorNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                    correcto = false;
                }
                else{
                    var valor = parseInt(document.getElementById('txtReduJorPS').value);
                    if(valor < 0 || valor > 100){
                        document.getElementById('txtReduJorPS').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "porReduJorNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                        correcto = false;
                    }
                    document.getElementById('txtReduJorPS').removeAttribute("style");
                }
            }
            //CNOE
            var cnoe = document.getElementById('codCNOEPS').value;
            if(cnoe != null && cnoe != ""){
                var encontrado = false;
                var i = 0;
                while(i < codCNOE.length && !encontrado){
                    if(codCNOE[i] == cnoe){
                        encontrado = true;
                    }
                    i++;
                }
                if(!encontrado){
                    document.getElementById('codCNOEPS').style.border = '1px solid red';
                    document.getElementById('descCNOEPS').style.border = '1px solid red';
                    if(mensajeError == "")
                        mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "cnoeNoVal")%>"+" '"+"<%=meLanbide29I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                    correcto = false;
                }
                else{
                    document.getElementById('codCNOEPS').removeAttribute("style");
                    document.getElementById('descCNOEPS').removeAttribute("style");
                }
            }
            return correcto;
        }
        
        function hayNifsRepetidos(){
            var nif1 = document.getElementById('txtDniPS').value;
            var nif2 = document.getElementById('txtDniPC').value;
            var nif3 = document.getElementById('txtDniPA').value;
            if(nif1 == nif2){
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nifsRepetidos12")%>";
                return true;
            }
            else if(nif1 == nif3){
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nifsRepetidos13")%>";
                return true;
            }
            else if(nif2 == nif3){
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "nifsRepetidos23")%>";
                return true;
            }
            return false;
        }
        
        function validarDuracionContrato(){
            var feAlta = document.getElementById('fechaAltaPC').value;
            var feBaja = document.getElementById('fechaBajaPS').value;
            
            var aAlta = feAlta.split('/');
            var aBaja = feBaja.split('/');
            var dAlta = new Date(aAlta[2], aAlta[1], aAlta[0]);
            var dBaja = new Date(aBaja[2], aBaja[1], aBaja[0]);
        
            var diferencia = (dBaja.getTime() - dAlta.getTime());

            if(diferencia < 0){
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide29I18n.getMensaje(idiomaUsuario, "fAltaAntFBaja")%>";
                return false;
            }
            return true;
        }
        
        function cambioValorCheck(check){
            if(check.checked){
                check.value="S";
            }
            else{
                check.value="N";
            }
        }

        function pulsarAceptar(){
            <%mensajeProgreso = meLanbide29I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos");%>
            barraProgresoContrato('on', 'barraProgresoContratoGuardar');
            if(validarDatos()){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var nuevo = "<%=nuevo%>";
                if(nuevo != null && nuevo == "1"){
                    parametros = "tarea=preparar&modulo=MELANBIDE29&operacion=crearContrato&tipo=0&numero=<%=numExpediente%>"
                    +"&idTerPS="+document.getElementById('idTerPS').value+"&txtDniPS="+document.getElementById('txtDniPS').value+"&txtNomPS="+escape(document.getElementById('txtNomPS').value)+"&txtApe1PS="+escape(document.getElementById('txtApe1PS').value)
                    +"&txtApe2PS="+escape(document.getElementById('txtApe2PS').value)+"&fechaNacimientoPS="+document.getElementById('fechaNacimientoPS').value
                    +"&txtSexoPS="+document.getElementById('txtSexoPS').value+"&fechaBajaPS="+document.getElementById('fechaBajaPS').value
                    +"&txtReduJorPS="+document.getElementById('txtReduJorPS').value+"&codCNOEPS="+document.getElementById('codCNOEPS').value
                    +"&idTerPC="+document.getElementById('idTerPC').value+"&txtDniPC="+document.getElementById('txtDniPC').value+"&txtNomPC="+escape(document.getElementById('txtNomPC').value)+"&txtApe1PC="+escape(document.getElementById('txtApe1PC').value)
                    +"&txtApe2PC="+escape(document.getElementById('txtApe2PC').value)+"&fechaNacimientoPC="+document.getElementById('fechaNacimientoPC').value+"&txtSexoPC="+document.getElementById('txtSexoPC').value
                    +"&checkMinusvalidoPC="+document.getElementById('checkMinusvalidoPC').value+"&checkInmigPC="+document.getElementById('checkInmigPC').value
                    +"&codEstudiosPC="+document.getElementById('codEstudiosPC').value+"&codColectivoPC="+document.getElementById('codColectivoPC').value
                    +"&txtMesesDesempleoPC="+document.getElementById('txtMesesDesempleoPC').value+"&checkPLDPC="+document.getElementById('checkPLDPC').value
                    +"&checkRMLPC="+document.getElementById('checkRMLPC').value+"&checkOtroPC="+document.getElementById('checkOtroPC').value+"&codTipConDurPC="
                    +document.getElementById('codTipConDurPC').value+"&fechaAltaPC="+document.getElementById('fechaAltaPC').value+"&codTipoJornadaPC="
                    +document.getElementById('codTipoJornadaPC').value+"&codCNOEPC="+document.getElementById('codCNOEPC').value+"&txtDuracionContratoPC="
                    +document.getElementById('txtDuracionContratoPC').value+"&fechaFinPC="+document.getElementById('txtFFinContratoPC').value+"&retribPC="+document.getElementById('txtRetribucionPC').value+"&subPC="+document.getElementById('txtISubvencionPC').value
                    +"&idTerPA="+document.getElementById('idTerPA').value+"&txtDniPA="+document.getElementById('txtDniPA').value+"&txtNomPA="+escape(document.getElementById('txtNomPA').value)+"&txtApe1PA="+escape(document.getElementById('txtApe1PA').value)+"&txtApe2PA="+escape(document.getElementById('txtApe2PA').value)+"&fechaNacimientoPA="
                    +document.getElementById('fechaNacimientoPA').value+"&txtSexoPA="+document.getElementById('txtSexoPA').value+"&checkMinusvalidoPA="+document.getElementById('checkMinusvalidoPA').value
                    +"&checkInmigPA="+document.getElementById('checkInmigPA').value+"&codEstudiosPA="+document.getElementById('codEstudiosPA').value
                    +"&codColectivoPA="+document.getElementById('codColectivoPA').value+"&txtMesesDesempleoPA="+document.getElementById('txtMesesDesempleoPA').value
                    +"&codTipConDurPA="+document.getElementById('codTipConDurPA').value+"&fechaAltaPA="+document.getElementById('fechaAltaPA').value
                    +"&codTipoJornadaPA="+document.getElementById('codTipoJornadaPA').value+"&checkPLDPA="+document.getElementById('checkPLDPA').value
                    +"&checkRMLPA="+document.getElementById('checkRMLPA').value+"&checkOtroPA="+document.getElementById('checkOtroPA').value
                    +"&retribPA="+document.getElementById('txtRetribucionPA').value+"&codCNOEPA="+document.getElementById('codCNOEPA').value;
                }
                else{
                    parametros = "tarea=preparar&modulo=MELANBIDE29&operacion=guardarContrato&tipo=0&numero=<%=numExpediente%>&idCon=<%=contrato.getNumContrato()%>"
                    + "&txtNomPS="+escape(document.getElementById('txtNomPS').value)+"&txtApe1PS="+escape(document.getElementById('txtApe1PS').value)
                    +"&txtApe2PS="+escape(document.getElementById('txtApe2PS').value)+"&fechaNacimientoPS="+document.getElementById('fechaNacimientoPS').value
                    +"&txtSexoPS="+document.getElementById('txtSexoPS').value+"&fechaBajaPS="+document.getElementById('fechaBajaPS').value
                    +"&txtReduJorPS="+document.getElementById('txtReduJorPS').value+"&codCNOEPS="+document.getElementById('codCNOEPS').value
                    +"&txtNomPC="+escape(document.getElementById('txtNomPC').value)+"&txtApe1PC="+escape(document.getElementById('txtApe1PC').value)
                    +"&txtApe2PC="+escape(document.getElementById('txtApe2PC').value)+"&fechaNacimientoPC="+document.getElementById('fechaNacimientoPC').value
                    +"&checkMinusvalidoPC="+document.getElementById('checkMinusvalidoPC').value+"&checkInmigPC="+document.getElementById('checkInmigPC').value
                    +"&codEstudiosPC="+document.getElementById('codEstudiosPC').value+"&codColectivoPC="+document.getElementById('codColectivoPC').value
                    +"&txtMesesDesempleoPC="+document.getElementById('txtMesesDesempleoPC').value+"&checkPLDPC="+document.getElementById('checkPLDPC').value
                    +"&checkRMLPC="+document.getElementById('checkRMLPC').value+"&checkOtroPC="+document.getElementById('checkOtroPC').value+"&codTipConDurPC="
                    +document.getElementById('codTipConDurPC').value+"&fechaAltaPC="+document.getElementById('fechaAltaPC').value+"&codTipoJornadaPC="
                    +document.getElementById('codTipoJornadaPC').value+"&codCNOEPC="+document.getElementById('codCNOEPC').value+"&txtDuracionContratoPC="
                    +document.getElementById('txtDuracionContratoPC').value+"&txtNomPA="+escape(document.getElementById('txtNomPA').value)+"&txtApe1PA="
                    +escape(document.getElementById('txtApe1PA').value)+"&txtApe2PA="+escape(document.getElementById('txtApe2PA').value)+"&fechaNacimientoPA="
                    +document.getElementById('fechaNacimientoPA').value+"&checkMinusvalidoPA="+document.getElementById('checkMinusvalidoPA').value
                    +"&checkInmigPA="+document.getElementById('checkInmigPA').value+"&codEstudiosPA="+document.getElementById('codEstudiosPA').value
                    +"&codColectivoPA="+document.getElementById('codColectivoPA').value+"&txtMesesDesempleoPA="+document.getElementById('txtMesesDesempleoPA').value
                    +"&codTipConDurPA="+document.getElementById('codTipConDurPA').value+"&fechaAltaPA="+document.getElementById('fechaAltaPA').value
                    +"&codTipoJornadaPA="+document.getElementById('codTipoJornadaPA').value+"&checkPLDPA="+document.getElementById('checkPLDPA').value
                    +"&checkRMLPA="+document.getElementById('checkRMLPA').value+"&checkOtroPA="+document.getElementById('checkOtroPA').value+"&codCNOEPA="
                    +document.getElementById('codCNOEPA').value;
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
                    var listaContratos = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaContratos[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].childNodes.length > 0){
                                    /*if(hijosFila[cont].nodeName=="ID"){
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="DNICONTRATADO"){
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="CONTRATADO"){
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="DNIADICIONAL"){
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="ADICIONAL"){
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="DNISUSTITUIDO"){
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=="SUSTITUIDO"){
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    }*/
                                    fila[cont] = hijosFila[cont].childNodes[0].nodeValue;
                                }else{
                                    fila[cont] = '';
                                }
                            }
                            listaContratos[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if(codigoOperacion=="0"){
                        //jsp_alerta("A",'Correcto');
                        window.returnValue =  listaContratos;
                        cerrarVentana();
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else{
                            jsp_alerta("A",'<%=meLanbide29I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){
                    
                }//try-catch
                barraProgresoContrato('off', 'barraProgresoContratoGuardar');
            }
            else{
                barraProgresoContrato('off', 'barraProgresoContratoGuardar');
                jsp_alerta("A", mensajeError);
            }
        }
            
        function cancelar(){
            var resultado = jsp_alerta("","<%=meLanbide29I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
            if (resultado == 1){
		cerrarVentana();
            }
        }
            
        function comprobarCaracteresEspeciales(texto){
            var iChars = "&'<>|^/\\%";
            for (var i = 0; i < texto.length; i++) {
                if (iChars.indexOf(texto.charAt(i)) != -1) {
                    return false;
                }
            }
            return true;
        }
        </script>
    </head>
    <body onload="inicio();" id="cuerpoNuevoContratoRenovacion" style="margin: 10px; text-align: left; width: 98%;" class="contenidoPantalla">
        <div class="tab-page" id="tabPage100" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana291"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.listadoContratos.title")%></h2>
            <form action="<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE29&operacion=guardarContrato&tipo=0&numero=<%=numExpediente%>&idCon=<%=contrato.getNumContrato()%>" method="POST" id="formContrato">

                <div id="barraProgresoContratoCargar" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                        <span>
                                                            <%=meLanbide29I18n.getMensaje(idiomaUsuario, "msg.cargarDatos")%>
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
                <div id="barraProgresoContratoGuardar" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                        <span>
                                                            <%=meLanbide29I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                    <div>
                        <fieldset>
                            <legend><font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"legend.personaContratada")%></font></legend>
                            <div style="width: 100%; clear: both;">
                                <div style="float: left; width: 170px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.dni")%></font>
                                    <input type="text" size="16" maxlength="25" id="txtDniPC" name="txtDniPC" disabled="disabled" value="<%=p2 != null && p2.getNumDoc() != null ? p2.getNumDoc().toUpperCase() : ""%>"/>
                                </div>
                                <div style="float: left; width: 750px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nombreApel")%></font>
                                    <input type="text" size="29" maxlength="80" id="txtNomPC" name="txtNomPC" value="<%=p2 != null && p2.getNombre() != null ? p2.getNombre().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe1PC" name="txtApe1PC" value="<%=p2 != null && p2.getApellido1() != null ? p2.getApellido1().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe2PC" name="txtApe2PC" value="<%=p2 != null && p2.getApellido2() != null ? p2.getApellido2().toUpperCase() : ""%>"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px; float: left;" id="divBtnBuscarPC">
                                <input type="button" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PC');"/>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 233px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%></font>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPC" name="fechaNacimientoPC" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularEdadPC();" onfocus="this.select();" value="<%=p2 != null && p2.getFeNac() != null ? formatter.format(p2.getFeNac()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPC(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaNacPC" name="calFechaNacPC" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 100px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.edad")%>
                                    <input type="text" disabled size="3" id="txtEdadPC" name="txtEdadPC"/>
                                </div>
                                <div style="float: left; width: 130px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="1" size="2" id="txtSexoPC" name="txtSexoPC" value="<%=p2 != null && p2.getSexo() != null ? (p2.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                </div>
                                <div style="float: left; width: 175px;">
                                    <input style="margin-right: 2px" type="checkBox" id="checkMinusvalidoPC" name="checkMinusvalidoPC" value="<%=p2 != null && p2.getFlMinusvalido() != null && p2.getFlMinusvalido().equals("S") ? "S" : "N"%>" <%=p2 != null && p2.getFlMinusvalido() != null && p2.getFlMinusvalido().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.minusvalido")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkInmigPC" name="checkInmigPC" value="<%=p2 != null && p2.getFlInmigrante() != null && p2.getFlInmigrante().equals("S") ? "S" : "N"%>" <%=p2 != null && p2.getFlInmigrante() != null && p2.getFlInmigrante().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.inmig")%>
                                </div>
                                <div style="float: left; width: 282px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nEstudios")%>
                                    <input id="codEstudiosPC" name="codEstudiosPC" type="text" class="inputTexto" size="2" maxlength="3" 
                                           onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getNivelEstudios() != null ? String.valueOf(p2.getNivelEstudios()) : ""%>" >
                                    <input id="descEstudiosPC"	name="descEstudiosPC" type="text" class="inputTexto" size="20" readonly >
                                     <a id="anchorEstudiosPC" name="anchorEstudiosPC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstudiosPC" name="botonEstudiosPC" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 260px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.colectivo")%>
                                    <input id="codColectivoPC" name="codColectivoPC" type="text" class="inputTexto" size="2" maxlength="2"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getColectivo() != null ? String.valueOf(p2.getColectivo()) : ""%>">
                                     <input id="descColectivoPC" name="descColectivoPC" type="text" class="inputTexto" size="20" readonly>
                                     <a id="anchorColectivosPC" name="anchorColectivoPC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonColectivoPC" name="botonColectivoPC" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                                <div style="float: left; width: 180px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.mesesDesempleo")%>
                                    <input type="text" maxlength="3" size="3" id="txtMesesDesempleoPC" name="txtMesesDesempleoPC" value="<%=p2 != null && p2.getMesesDesempleo() != null ? String.valueOf(p2.getMesesDesempleo()) : ""%>"/>
                                </div>
                                <div style="float: left; width: 184px;">
                                    <input style="margin-right: 2px" type="checkBox" id="checkPLDPC" name="checkPLDPC" value="<%=p2 != null && p2.getFlPld() != null && p2.getFlPld().equals("S") ? "S" : "N"%>" <%=p2 != null && p2.getFlPld() != null && p2.getFlPld().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.PLD")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkRMLPC" name="checkRMLPC" value="<%=p2 != null && p2.getFlRml() != null && p2.getFlRml().equals("S") ? "S" : "N"%>" <%=p2 != null && p2.getFlRml() != null && p2.getFlRml().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.RML")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkOtroPC" name="checkOtroPC" value="<%=p2 != null && p2.getFlOtr() != null && p2.getFlOtr().equals("S") ? "S" : "N"%>" <%=p2 != null && p2.getFlOtr() != null && p2.getFlOtr().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.otro")%>
                                </div>
                                <div style="float: left; width: 290px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.tipConDurAbrev")%>
                                    <input id="codTipConDurPC" name="codTipConDurPC" type="text" class="inputTexto" size="2" maxlength="3"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getTipoContrato() != null ? String.valueOf(p2.getTipoContrato()) : ""%>">
                                     <input id="descTipConDurPC" name="descTipConDurPC" type="text" class="inputTexto" size="20" readonly>
                                     <a id="anchorTipConDurPC" name="anchorTipConDurPC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipConDurPC" name="botonTipConDurPC" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 170px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fAlta")%></font>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaAltaPC" name="fechaAltaPC" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onchange="calcularDuracionContrato();" onfocus="this.select();" value="<%=p2 != null && p2.getFeAlta() != null ? formatter.format(p2.getFeAlta()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaAltaPC(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaAltaPC" name="calFechaAltaPC" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fAlta")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 340px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.tipoJornada")%>
                                    <input id="codTipoJornadaPC" name="codTipoJornadaPC" type="text" class="inputTexto" size="2" maxlength="5"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getTipoJornada() != null ? String.valueOf(p2.getTipoJornada()) : ""%>">
                                     <input id="descTipoJornadaPC" name="descTipoJornadaPC" type="text" class="inputTexto" size="25" readonly>
                                     <a id="anchorTipoJornadaPC" name="anchorTipoJornadaPC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoJornadaPC" name="botonTipoJornadaPC" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                                <div style="float: left; width: 200px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fFinContrato")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="10" size="<%=nuevo != null && nuevo.equals("1") ? "6" : "10"%>" id="txtFFinContratoPC" name="txtFFinContratoPC" value="<%=p2 != null && p2.getFeFinContrato() != null ? formatter.format(p2.getFeFinContrato()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinPC(event);return false;" style="text-decoration:none;display: <%=nuevo != null && nuevo.equals("1") ? "inline" : "none"%>" >
                                        <IMG style="border: 0" height="17" id="calFechaFinPC" name="calFechaFinPC" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fFinContrato")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 204px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.retribucion")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="13" size="13" id="txtRetribucionPC" name="txtRetribucionPC" style="margin-left: 17px;" value="<%=p2 != null && p2.getRetSalarial() != null ? String.valueOf(p2.getRetSalarial()) : ""%>" onchange="reemplazarPuntos(this);"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 510px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.CNOE")%>
                                    <input id="codCNOEPC" name="codCNOEPC" type="text" class="inputTexto" size="2" maxlength="4"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getCnoe() != null ? String.valueOf(p2.getCnoe()) : ""%>">
                                     <input id="descCNOEPC" name="descCNOEPC" type="text" class="inputTexto" size="61" readonly>
                                     <a id="anchorCNOEPC" name="anchorCNOEPC" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCNOEPC" name="botonCNOEPC" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                                <div style="float: left; width: 200px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.duracionContrato")%>
                                    <input type="text" maxlength="5" size="6" id="txtDuracionContratoPC" name="txtDuracionContratoPC" disabled style="padding-left: 1px;" value="<%=p2 != null && p2.getDuracionContrato() != null ? String.valueOf(p2.getDuracionContrato()) : ""%>"/>
                                </div>
                                <div style="float: left; width: 204px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.ISubvencion")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="13" size="13" id="txtISubvencionPC" name="txtISubvencionPC" value="<%=p2 != null && p2.getImpSubvencion() != null ? String.valueOf(p2.getImpSubvencion()) : ""%>" onchange="reemplazarPuntos(this);"/>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend><%=meLanbide29I18n.getMensaje(idiomaUsuario,"legend.personaContratoAdicional")%></legend>
                            <div style="width: 100%; clear: both;">
                                <div style="float: left; width: 170px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.dni")%>
                                    <input type="text" size="16" maxlength="25" id="txtDniPA" name="txtDniPA" disabled="disabled" value="<%=p3 != null && p3.getNumDoc() != null ? p3.getNumDoc().toUpperCase() : ""%>"/>
                                </div>
                                <div style="float: left; width: 750px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nombreApel")%>
                                    <input type="text" size="29" maxlength="80" id="txtNomPA" name="txtNomPA" value="<%=p3 != null && p3.getNombre() != null ? p3.getNombre().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe1PA" name="txtApe1PA" value="<%=p3 != null && p3.getApellido1() != null ? p3.getApellido1().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe2PA" name="txtApe2PA" value="<%=p3 != null && p3.getApellido2() != null ? p3.getApellido2().toUpperCase() : ""%>"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;" id="divBtnBuscarPA">
                                <input type="button" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PA');"/>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 233px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPA" name="fechaNacimientoPA" onkeyup="return SoloCaracteresFecha(this);"  onblur="calcularEdadPA();" onfocus="this.select();" value="<%=p3 != null && p3.getFeNac() != null ? formatter.format(p3.getFeNac()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPA(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaNacPA" name="calFechaNacPA" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                    
                                    
                                <div style="float: left; width: 100px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.edad")%>
                                    <input type="text" disabled="disabled" size="3" id="txtEdadPA" name="txtEdadPA"/>
                                </div>
                                    
                                    
                                <div style="float: left; width: 130px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="1" size="2" id="txtSexoPA" name="txtSexoPA" value="<%=p3 != null && p3.getSexo() != null ? (p3.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                </div>
                                    
                                    
                                <div style="float: left; width: 175px;">
                                    <input style="margin-right: 2px" type="checkBox" id="checkMinusvalidoPA" name="checkMinusvalidoPA" value="<%=p3 != null && p3.getFlMinusvalido() != null && p3.getFlMinusvalido().equals("S") ? "S" : "N"%>" <%=p3 != null && p3.getFlMinusvalido() != null && p3.getFlMinusvalido().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.minusvalido")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkInmigPA" name="checkInmigPA" value="<%=p3 != null && p3.getFlInmigrante() != null && p3.getFlInmigrante().equals("S") ? "S" : "N"%>" <%=p3 != null && p3.getFlInmigrante() != null && p3.getFlInmigrante().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.inmig")%>
                                </div>
                                
                                
                                <div style="float: left; width: 282px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nEstudios")%>
                                    <input id="codEstudiosPA" name="codEstudiosPA" type="text" class="inputTexto" size="2" maxlength="3"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p3 != null && p3.getNivelEstudios() != null ? String.valueOf(p3.getNivelEstudios()) : ""%>" >
                                     <input id="descEstudiosPA"	name="descEstudiosPA" type="text" class="inputTexto" size="20" readonly>
                                     <a id="anchorEstudiosPA" name="anchorEstudiosPA" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstudiosPA" name="botonEstudiosPA" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 260px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.colectivo")%>
                                    <input id="codColectivoPA" name="codColectivoPA" type="text" class="inputTexto" size="2" maxlength="2"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p3 != null && p3.getColectivo() != null ? String.valueOf(p3.getColectivo()) : ""%>">
                                     <input id="descColectivoPA" name="descColectivoPA" type="text" class="inputTexto" size="20" readonly>
                                     <a id="anchorColectivosPA" name="anchorColectivoPA" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonColectivoPA" name="botonColectivoPA" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                                <div style="float: left; width: 364px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.mesesDesempleo")%>
                                    <input type="text" maxlength="3" size="3" id="txtMesesDesempleoPA" name="txtMesesDesempleoPA" value="<%=p3 != null && p3.getMesesDesempleo() != null ? String.valueOf(p3.getMesesDesempleo()) : ""%>"/>
                                </div>
                                <div style="float: left; width: 290px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.tipConDurAbrev")%>
                                    <input id="codTipConDurPA" name="codTipConDurPA" type="text" class="inputTexto" size="2" maxlength="3"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p3 != null && p3.getTipoContrato() != null ? String.valueOf(p3.getTipoContrato()) : ""%>">
                                     <input id="descTipConDurPA" name="descTipConDurPA" type="text" class="inputTexto" size="20" readonly>
                                     <a id="anchorTipConDurPA" name="anchorTipConDurPA" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipConDurPA" name="botonTipConDurPA" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 170px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fAlta")%>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaAltaPA" name="fechaAltaPA" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value="<%=p3 != null && p3.getFeAlta() != null ? formatter.format(p3.getFeAlta()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaAltaPA(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaAltaPA" name="calFechaAltaPA" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fAlta")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 340px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.tipoJornada")%>
                                    <input id="codTipoJornadaPA" name="codTipoJornadaPA" type="text" class="inputTexto" size="2" maxlength="5"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p3 != null && p3.getTipoJornada() != null ? String.valueOf(p3.getTipoJornada()) : ""%>">
                                     <input id="descTipoJornadaPA" name="descTipoJornadaPA" type="text" class="inputTexto" size="25" readonly>
                                     <a id="anchorTipoJornadaPA" name="anchorTipoJornadaPA" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoJornadaPA" name="botonTipoJornadaPA" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                                <div style="float: left; width: 195px;">
                                    <input style="margin-right: 2px" type="checkBox" id="checkPLDPA" name="checkPLDPA" value="<%=p3 != null && p3.getFlPld() != null && p3.getFlPld().equals("S") ? "S" : "N"%>" <%=p3 != null && p3.getFlPld() != null && p3.getFlPld().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.PLD")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkRMLPA" name="checkRMLPA" value="<%=p3 != null && p3.getFlRml() != null && p3.getFlRml().equals("S") ? "S" : "N"%>" <%=p3 != null && p3.getFlRml() != null && p3.getFlRml().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.RML")%>
                                    <input style="margin-right: 2px" type="checkBox" id="checkOtroPA" name="checkOtroPA" value="<%=p3 != null && p3.getFlOtr() != null && p3.getFlOtr().equals("S") ? "S" : "N"%>" <%=p3 != null && p3.getFlOtr() != null && p3.getFlOtr().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.otro")%>
                                </div>
                                <div style="float: left; width: 204px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.retribucion")%>
                                    <input type="text" <%=nuevo != null && nuevo.equals("1") ? "" : "disabled"%> maxlength="13" size="13" id="txtRetribucionPA" name="txtRetribucionPA" style="margin-left: 17px;" value="<%=p3 != null && p3.getRetSalarial() != null ? String.valueOf(p3.getRetSalarial()) : ""%>" onchange="reemplazarPuntos(this);"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 510px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.CNOE")%>
                                    <input id="codCNOEPA" name="codCNOEPA" type="text" class="inputTexto" size="2" maxlength="4"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p3 != null && p3.getCnoe() != null ? String.valueOf(p3.getCnoe()) : ""%>">
                                     <input id="descCNOEPA" name="descCNOEPA" type="text" class="inputTexto" size="61" readonly>
                                     <a id="anchorCNOEPA" name="anchorCNOEPA" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCNOEPA" name="botonCNOEPA" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend><font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"legend.personaSustituida")%></font></legend>
                            <div style="width: 100%; clear: both;">
                                <div style="float: left; width: 170px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.dni")%></font>
                                    <input type="text" size="16" maxlength="25" id="txtDniPS" name="txtDniPS" disabled="disabled" value="<%=p1 != null && p1.getNumDoc() != null ? p1.getNumDoc().toUpperCase() : ""%>"/>
                                </div>
                                <div style="float: left; width: 750px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.nombreApel")%></font>
                                    <input type="text" size="29" maxlength="80" id="txtNomPS" name="txtNomPS" value="<%=p1 != null && p1.getNombre() != null ? p1.getNombre().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe1PS" name="txtApe1PS" value="<%=p1 != null && p1.getApellido1() != null ? p1.getApellido1().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe2PS" name="txtApe2PS" value="<%=p1 != null && p1.getApellido2() != null ? p1.getApellido2().toUpperCase() : ""%>"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;" id="divBtnBuscarPS">
                                <input type="button" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PS');"/>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 233px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%></font>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPS" name="fechaNacimientoPS" onkeyup="return SoloCaracteresFecha(this);"  onblur="calcularEdadPS();" onfocus="this.select();" value="<%=p1 != null && p1.getFeNac() != null ? formatter.format(p1.getFeNac()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPS(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaNacPS" name="calFechaNacPS" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 100px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.edad")%>
                                    <input type="text" disabled="disabled" size="3" id="txtEdadPS" name="txtEdadPS"/>
                                </div>
                                <div style="float: left; width: 154px;">
                                    <font color="red"><%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.sexo")%></font>
                                    <input type="text" maxlength="1" size="2" id="txtSexoPS" name="txtSexoPS" value="<%=p1 != null && p1.getSexo() != null ? (p1.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                </div>
                                <div style="float: left; width: 236px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fBaja")%>
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaBajaPS" name="fechaBajaPS" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" onchange="calcularDuracionContrato();" value="<%=p1 != null && p1.getFeBaja() != null ? formatter.format(p1.getFeBaja()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaBajaPS(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaBajaPS" name="calFechaBajaPS" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.fBaja")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; width: 190px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.porReduJor")%>
                                    <input type="text" maxlength="6" size="2" id="txtReduJorPS" name="txtReduJorPS" value="<%=p1 != null && p1.getPorReduJor() != null ? String.valueOf(p1.getPorReduJor()) : ""%>" onchange="reemplazarPuntos(this);"/>
                                </div>
                            </div>
                            <div style="width: 100%; clear: both; padding-top: 5px;">
                                <div style="float: left; width: 510px;">
                                    <%=meLanbide29I18n.getMensaje(idiomaUsuario,"label.CNOE")%>
                                    <input id="codCNOEPS" name="codCNOEPS" type="text" class="inputTexto" size="2" maxlength="4"
                                            onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p1 != null && p1.getCnoe() != null ? String.valueOf(p1.getCnoe()) : ""%>">
                                     <input id="descCNOEPS" name="descCNOEPS" type="text" class="inputTexto" size="61" readonly>
                                     <a id="anchorCNOEPS" name="anchorCNOEPS" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCNOEPS" name="botonCNOEPS" style="cursor:hand;" alt="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide29I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                </div>
                            </div>
                        </fieldset>
                    </div>    
                    <div style="width: 100%; text-align: center; padding-top: 5px;">
                        <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="pulsarAceptar();">
                        <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                        <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide29I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();">
                    </div>
                </div>

            </form>
            <div id="popupcalendar" class="text"></div>
            <input type="hidden" id="idTerPC"/>
            <input type="hidden" id="idTerPA"/>
            <input type="hidden" id="idTerPS"/>
        </div>

        <script type="text/javascript">
            //Persona contratada
            var comboEstudiosPC = new Combo("EstudiosPC");
            var comboColectivoPC = new Combo("ColectivoPC");
            var comboTipConDurPC = new Combo("TipConDurPC");
            var comboTipoJornadaPC = new Combo("TipoJornadaPC");
            var comboCNOEPC = new Combo("CNOEPC");

            //Persona contratada adicional
            var comboEstudiosPA = new Combo("EstudiosPA");
            var comboColectivoPA = new Combo("ColectivoPA");
            var comboTipConDurPA = new Combo("TipConDurPA");
            var comboTipoJornadaPA = new Combo("TipoJornadaPA");
            var comboCNOEPA = new Combo("CNOEPA");
            
            //Persona sustituida
            var comboCNOEPS = new Combo("CNOEPS");
                                    
        </script>
    </body>
</html>

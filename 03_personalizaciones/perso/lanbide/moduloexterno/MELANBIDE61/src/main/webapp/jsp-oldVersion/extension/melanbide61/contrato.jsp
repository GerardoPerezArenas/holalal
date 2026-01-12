<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.PersonaContratoRenovacionPlantillaVO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
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
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();

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
            List<ValorCampoDesplegableModuloIntegracionVO> listaTipoCon = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaTipoCon") != null)
                listaTipoCon = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaTipoCon");
            List<ValorCampoDesplegableModuloIntegracionVO> listaColec2 = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaColec2") != null)
                listaColec2 = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaColec2");
            List<ValorCampoDesplegableModuloIntegracionVO> listaSitPrevia = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaSitPrevia") != null)
                listaSitPrevia = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaSitPrevia");

            
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
            String lcodTipoCon ="";
            String ldescTipoCon ="";
            String lcodColec2 = "";
            String ldescColec2 = "";
            String lcodSitPrevia ="";
            String ldescSitPrevia ="";
            String lcodSitPrevia2 ="";
            String ldescSitPrevia2 ="";
            
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

            // tipo contrato
            if (listaTipoCon != null && listaTipoCon.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO est = null;
                for (i = 0; i < listaTipoCon.size() - 1; i++) 
                {
                    est = (ValorCampoDesplegableModuloIntegracionVO) listaTipoCon.get(i);
                    lcodTipoCon += "\"" + est.getCodigo() + "\",";
                    ldescTipoCon += "\"" + escape(est.getDescripcion()) + "\",";
                }
                est = (ValorCampoDesplegableModuloIntegracionVO) listaTipoCon.get(i);
                lcodTipoCon += "\"" + est.getCodigo() + "\"";
                ldescTipoCon += "\"" + escape(est.getDescripcion()) + "\"";
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

            //Colectivos
            if(listaColec2 != null && listaColec2.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO col = null;
                for(i = 0; i < listaColec2.size() - 1; i++)
                {
                    col = (ValorCampoDesplegableModuloIntegracionVO) listaColec2.get(i);
                    lcodColec2 += "\"" + col.getCodigo() + "\",";
                    ldescColec2 += "\"" + escape(col.getDescripcion()) + "\",";
                }
                col = (ValorCampoDesplegableModuloIntegracionVO) listaColec2.get(i);
                lcodColec2 += "\"" + col.getCodigo() + "\"";
                ldescColec2 += "\"" + escape(col.getDescripcion()) + "\"";
            }

            //Situación previa
            if(listaSitPrevia != null && listaSitPrevia.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO col = null;
                for(i = 0; i < listaSitPrevia.size() - 1; i++)
                {
                    col = (ValorCampoDesplegableModuloIntegracionVO) listaSitPrevia.get(i);
                    lcodSitPrevia += "\"" + col.getCodigo() + "\",";
                    ldescSitPrevia += "\"" + escape(col.getDescripcion()) + "\",";
                }
                col = (ValorCampoDesplegableModuloIntegracionVO) listaSitPrevia.get(i);
                lcodSitPrevia += "\"" + col.getCodigo() + "\"";
                ldescSitPrevia += "\"" + escape(col.getDescripcion()) + "\"";
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
                tituloPagina = meLanbide61I18n.getMensaje(idiomaUsuario,"label.tituloPagina.consultaContrato");
            }
            else
            {
                if(contrato != null)
                {
                    tituloPagina = meLanbide61I18n.getMensaje(idiomaUsuario,"label.tituloPagina.modifContrato");
                }
                else
                {
                    tituloPagina = meLanbide61I18n.getMensaje(idiomaUsuario,"label.tituloPagina.nuevoContrato");
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide61/melanbide61.css"/>

        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide61/ecaUtils.js"></script>

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
            var codTipoCon = [<%=lcodTipoCon%>];
            var descTipoCon = [<%=ldescTipoCon%>];
            var codColec2 = [<%=lcodColec2%>];
            var descColec2 = [<%=ldescColec2%>];
            var codSitPrevia = [<%=lcodSitPrevia%>];
            var descSitPrevia = [<%=ldescSitPrevia%>];
            var codSitPrevia2 = [<%=lcodSitPrevia%>];
            var descSitPrevia2 = [<%=ldescSitPrevia%>];
            //Mensaje para las validaciones
            var mensajeError = "";
        
            function mostrarBusqueda(tipo){
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide61/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),500,600,'no','no');
                }else{
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide61/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),550,600,'no','no');
                }
                if (result != undefined){
                    if(tipo == 'PC'){
                        document.getElementById('idTerPC').value = result[0];
                        document.getElementById('txtDniPC').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                        document.getElementById('txtNomPC').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                        document.getElementById('txtApe1PC').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                        document.getElementById('txtApe2PC').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        document.getElementById('txtSexoPC').value = result[5] != undefined && result[5] != '' ? result[5] : '';
                        document.getElementById('fechaNacimientoPC').value = result[6] != undefined && result[6] != '' ? result[6] : '';
                        calcularEdadPC();
                    
                    }
                    else if(tipo == 'PA'){
                        document.getElementById('idTerPA').value = result[0];
                        document.getElementById('txtDniPA').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                        document.getElementById('txtNomPA').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                        document.getElementById('txtApe1PA').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                        document.getElementById('txtApe2PA').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        document.getElementById('txtSexoPA').value = result[5] != undefined && result[5] != '' ? result[5] : '';
                        document.getElementById('fechaNacimientoPA').value = result[6] != undefined && result[6] != '' ? result[6] : '';
                        calcularEdadPA();
                    }
                    else if(tipo == 'PS'){
                        document.getElementById('idTerPS').value = result[0];
                        document.getElementById('txtDniPS').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                        document.getElementById('txtNomPS').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                        document.getElementById('txtApe1PS').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                        document.getElementById('txtApe2PS').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                        document.getElementById('txtSexoPS').value = result[5] != undefined && result[5] != '' ? result[5] : '';
                        document.getElementById('fechaNacimientoPS').value = result[6] != undefined && result[6] != '' ? result[6] : '';
                        calcularEdadPS();
                    }
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
        
            function mostrarCalFechaBajaPA(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaBajaPA").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaBajaPA',null,null,null,'','calFechaBajaPA','',null,null,null,null,null,null,null,'',evento);
            }
        
            function mostrarCalFechaInConPre(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaInConPre").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaInConPre',null,null,null,'','calFechaInConPre','',null,null,null,null,null,null,null,"comprobarFechaInicioFin('fechaInConPre', 'fechaFinConPre');",evento);
            }
        
            function mostrarCalFechaFinConPre(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaFinConPre").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaFinConPre',null,null,null,'','calFechaFinConPre','',null,null,null,null,null,null,null,"comprobarFechaInicioFin('fechaInConPre', 'fechaFinConPre');",evento);
            }      
        
            function mostrarCalFechaInConPre2(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaInConPre2").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaInConPre2',null,null,null,'','calFechaInConPre2','',null,null,null,null,null,null,null,"comprobarFechaInicioFin('fechaInConPre2', 'fechaFinConPre2');",evento);
            }
        
            function mostrarCalFechaFinConPre2(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaFinConPre2").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaFinConPre2',null,null,null,'','calFechaFinConPre2','',null,null,null,null,null,null,null,"comprobarFechaInicioFin('fechaInConPre2', 'fechaFinConPre2');",evento);
            }
        
            function mostrarCalFechaInConAd(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaInConAd").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaInConAd',null,null,null,'','calFechaInConAd','',null,null,null,null,null,null,null,"calcularDiasContrato();comprobarFechaInicioFin('calFechaInConAd', 'fechaFinConAd')",evento);
            }
        
            function mostrarCalFechaFinConAd(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaFinConAd").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaFinConAd',null,null,null,'','calFechaFinConAd','',null,null,null,null,null,null,null,"calcularDiasContrato();comprobarFechaInicioFin('calFechaInConAd', 'fechaFinConAd')",evento);
            }
        
            function mostrarCalFechaConPC(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaConPC").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaConPC',null,null,null,'','calFechaConPC','',null,null,null,null,null,null,null,'calcularEdadPS();calcularEdadPC();calcularEdadPA();calcularDiasF()',evento);
            }
        
            function mostrarCalFechaPublica(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaPublica").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaPublica',null,null,null,'','calFechaPublica','',null,null,null,null,null,null,null,'',evento);
            }
        
            function mostrarCalFechaIniContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaIniContrato").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaIniContrato',null,null,null,'','calFechaIniContrato','',null,null,null,null,null,null,null,"comprobarFechaInicioFin('fechaIniContrato', 'fechaFinContrato');",evento);
            }
        
            function mostrarCalFechaFinContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calFechaFinContrato").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','fechaFinContrato',null,null,null,'','calFechaFinContrato','',null,null,null,null,null,null,null,"calcularDiasF();comprobarFechaInicioFin('fechaIniContrato', 'fechaFinContrato');",evento);
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
                        jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
                        return false;
                    }
                }
                return true;
            }

            function comprobarFechaNac(inputFecha) {
                if (Trim(inputFecha.value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                        jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
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
                            jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacPosterior")%>");
                            return false;
                        }*/
                            if(result < 0){
                                jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacPosterior")%>");
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        
            function comprobarFechaInicioFin(inputFechaI, inputFechaF) {
                if (Trim(document.getElementById(inputFechaI).value)!='' && Trim(document.getElementById(inputFechaF).value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],document.getElementById(inputFechaI))){
                        jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
                        return false;
                    }
                    else if(!ValidarFechaConFormato(document.forms[0],document.getElementById(inputFechaF))){
                        jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNoVal")%>");
                        return false;
                    }
                    else{ 
                        //La fecha de nacimiento no puede ser mayor que la actual
                        var array_fechaI = document.getElementById(inputFechaI).value.split("/");
                        var array_fechaF = document.getElementById(inputFechaF).value.split("/");
                        var dateStart=new Date(array_fechaI[2],(array_fechaI[1]-1),array_fechaI[0]);
                        var dateEnd=new Date(array_fechaF[2],(array_fechaF[1]-1),array_fechaF[0]);
                        if(dateStart>=dateEnd)
                        {
                            jsp_alerta("A","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaIncoherente")%>");
                            document.getElementById(inputFechaI).value = "";
                            document.getElementById(inputFechaF).value = "";
                            return false
                        }
                    }
                }
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
                    var fecha2 = document.getElementById('fechaConPC').value;
                    if (comprobarFecha(document.getElementById('fechaConPC')))
                        calcularEdad(value, txtOutput,fecha2);
                }
                else{
                    document.getElementById('txtEdadPC').value = '';
                }
            }

            function calcularEdadPA(){
                if(comprobarFechaNac(document.getElementById('fechaNacimientoPA'))){
                    var txtOutput = 'txtEdadPA';
                    var value = document.getElementById('fechaNacimientoPA').value;
                    var fecha2 = document.getElementById('fechaConPC').value;
                    if (comprobarFecha(document.getElementById('fechaConPC')))
                        calcularEdad(value, txtOutput,fecha2);
                }
                else{
                    document.getElementById('txtEdadPA').value = '';
                }
            }

            function calcularEdadPS(){                
                if(comprobarFechaNac(document.getElementById('fechaNacimientoPS')) && comprobarFecha(document.getElementById('fechaInConAd'))){
                    var txtOutput = 'txtEdadPS';
                    var value = document.getElementById('fechaNacimientoPS').value;
                    var fecha2 = document.getElementById('fechaInConAd').value;
                    //  if (comprobarFecha(document.getElementById('fechaInConAd')))
                    calcularEdad(value, txtOutput, fecha2);                       
                }
                else{
                    document.getElementById('txtEdadPS').value = '';
                }
            }


            function  restaFechas(f1,f2,txtOutput)
            {
                var aFecha1 = f1.split('/'); 
                var aFecha2 = f2.split('/'); 
                var fFecha1 = Date.UTC(aFecha1[2],aFecha1[1]-1,aFecha1[0]); 
                var fFecha2 = Date.UTC(aFecha2[2],aFecha2[1]-1,aFecha2[0]); 
                var dif = fFecha2 - fFecha1;
                var dias = Math.floor(dif / (1000 * 60 * 60 * 24)); 
                var anyos =Math.floor(dias/365);
                document.getElementById(txtOutput).value = anyos;               
            }

            function calcularEdad(feNac, txtOutput, fecha){
                try
                {
                    if(feNac != "")
                    { 
                        var dat1 = feNac.split('/'); 
                        var dat2 = fecha.split('/'); 
                        var anyos = parseInt(dat2[2]) - parseInt(dat1[2]);
                        var mes = parseInt(dat2[1],10) - parseInt(dat1[1],10);//forzar' la base a 10
                        if (mes < 0) {
                            anyos = anyos - 1;
                        } else if (mes == 0) {
                            var dia = parseInt(dat2[0],10) - parseInt(dat1[0],10);
                            if (dia < 0) {
                                anyos = anyos - 1;
                            }
                        }
                
                        document.getElementById(txtOutput).value = anyos;   
                    }
                }catch(err){

                }
            }
    
            function calcularDiasF(){
                var fecha1 = "";
                var fecha2 = "";
                var txtOutput = 'txtDiasF';
                if(comprobarFecha(document.getElementById('fechaConPC'))){
                    fecha1 = document.getElementById('fechaConPC').value;
                    if(comprobarFecha(document.getElementById('fechaFinContrato'))){                    
                        fecha2 = document.getElementById('fechaFinContrato').value;
                    }
                }
                if(fecha1 != "" && fecha2 != ""){
                    calcularDias(fecha1, fecha2, txtOutput)
                }
                else{
                    document.getElementById('txtDiasF').value = '';
                }
                var diasF = document.getElementById('txtDiasF').value;
                var diasC = document.getElementById('txtDiasContrato').value;
                if(diasF != "" && diasC != "")
                {
                    var f = parseInt(diasF);
                    var c = parseInt(diasC); 
                    if(c < f)
                        alert("El numero de días del contrato adicional es menor que el número de días para fin contrato");
                }
            }
        
            function calcularDiasContrato(){
                var fecha1 = "";
                var fecha2 = "";
                var txtOutput = 'txtDiasContrato';
                if(comprobarFecha(document.getElementById('fechaInConAd'))){
                    fecha1 = document.getElementById('fechaInConAd').value;
                    if(comprobarFecha(document.getElementById('fechaFinConAd'))){                    
                        fecha2 = document.getElementById('fechaFinConAd').value;
                    }
                }
                if(fecha1 != "" && fecha2 != ""){
                    calcularDias(fecha1, fecha2, txtOutput)
                }
                else{
                    document.getElementById('txtDiasContrato').value = '';
                }
                var diasF = document.getElementById('txtDiasF').value;
                var diasC = document.getElementById('txtDiasContrato').value;
                if(diasF != "" && diasC != "")
                {
                    var f = parseInt(diasF);
                    var c = parseInt(diasC); 
                    if(c < f)
                        alert("El numero de días del contrato adicional es menor que el número de días para fin contrato");
                }
            }
        
            function calcularDias(fechaInicial, fechaFinal, output)
            {
                //var fechaInicial=document.getElementById("fechaInicial").value;
                //var fechaFinal=document.getElementById("fechaFinal").value;
                var resultado="";
                var inicial=fechaInicial.split('/');
                var final=fechaFinal.split('/');
                // obtenemos las fechas en milisegundos
                var dateStart=new Date(inicial[2],(inicial[1]-1),inicial[0]);
                var dateEnd=new Date(final[2],(final[1]-1),final[0]);
                if(dateStart<=dateEnd)
                {
                    // la diferencia entre las dos fechas, la dividimos entre 86400 segundos
                    // que tiene un dia, y posteriormente entre 1000 ya que estamos
                    // trabajando con milisegundos.
                    resultado = Math.ceil((((dateEnd-dateStart)/86400)/1000))+1;
                    //alert("La diferencia es de "+(((dateEnd-dateStart)/86400)/1000)+" días");
                }else{
                    alert("La fecha inicial es posterior a la fecha final");
                }
                document.getElementById(output).value =resultado;
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
                        jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fAltaAntFBaja")%>');
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
            <%mensajeProgreso = meLanbide61I18n.getMensaje(idiomaUsuario, "msg.cargarDatos");%>
                    //barraProgresoContrato('on', 'barraProgresoContratoCargar');

                    cargarCombos(); 
                    cargarDescripcionesCombos();
                    var nuevo = "<%=nuevo%>";
                    if(nuevo != null && nuevo == "1"){
                        document.getElementById('divBtnBuscarPC').style.display = 'inline';
                        document.getElementById('divBtnBuscarPA').style.display = 'inline';
                        document.getElementById('divBtnBuscarPS').style.display = 'inline';
                    }
                    /*else{
                        document.getElementById('divBtnBuscarPC').style.display = 'none';
                        document.getElementById('divBtnBuscarPA').style.display = 'none';
                        document.getElementById('divBtnBuscarPS').style.display = 'none';
                        document.getElementById('txtDniPC').disabled = true;
                        document.getElementById('txtDniPA').disabled = true;
                        document.getElementById('txtDniPS').disabled = true;
                    }*/
                    var feNacPC = "<%=p2 != null && p2.getFeNac() != null ? formatter.format(p2.getFeNac()) : ""%>";
                    var feCont = document.getElementById('fechaConPC').value;//"<%=p2 != null && p2.getFechaIniContrato() != null ? formatter.format(p2.getFechaIniContrato()) : ""%>";
                    calcularEdad(feNacPC, 'txtEdadPC',feCont);
                    var feNacPA = "<%=p3 != null && p3.getFeNac() != null ? formatter.format(p3.getFeNac()) : ""%>";
                    calcularEdad(feNacPA, 'txtEdadPA',feCont);
                    var feNacPS = "<%=p1 != null && p1.getFeNac() != null ? formatter.format(p1.getFeNac()) : ""%>";
                    var feContAd = document.getElementById('fechaInConAd').value;//"<%=p2 != null && p2.getFecInConAd() != null ? formatter.format(p2.getFecInConAd()) : ""%>";
                    if (comprobarFecha(document.getElementById('fechaInConAd')))
                        calcularEdad(feNacPS, 'txtEdadPS',feContAd);
            
                    //calcularDuracionContrato();
            

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
                    //document.getElementById('txtSexoPS').disabled = true;
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
                    desc="";
                    codigo = "<%=p1 != null && p1.getNivelEstudios() != null ? String.valueOf(p1.getNivelEstudios()) : ""%>";
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
                    document.getElementById('descEstudiosPS').value = desc;

            
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
            
                    //contrato
                    desc = "";
                    codAct = "";
                    codigo = "";
                    codigo = "<%=p2 != null && p2.getTipoContrato() != null ? String.valueOf(p2.getTipoContrato()) : "IN"%>";
                    if(codigo != null && codigo != "")
                    {
                        for(var i=0; i<codTipoCon.length; i++)
                        {
                            codAct = codTipoCon[i];
                            if(codAct == codigo)
                            {
                                desc = descTipoCon[i];
                            }
                        }
                    }
                    document.getElementById('descTipoCon').value = desc;
            
                    desc = "";
                    codAct = "";
                    codigo = "";
                    codigo = "<%=p1 != null && p1.getColectivo() != null ? String.valueOf(p1.getColectivo()) : ""%>";
                    //alert("codigo: " + codigo);
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
                    document.getElementById('descColectivoPS').value = desc;
            
                    //situación previa
                    desc = "";
                    codAct = "";
                    codigo = "";
                    codigo = "<%=p2 != null && p2.getSitPrevia() != null ? String.valueOf(p2.getSitPrevia()) : ""%>";
                    //alert("codigo: " + codigo);
                    if(codigo != null && codigo != "")
                    {
                        for(var i=0; i<codSitPrevia.length; i++)
                        {
                            codAct = codSitPrevia[i];
                            if(codAct == codigo)
                            {
                                desc = descSitPrevia[i];
                            }
                        }
                    }
                    document.getElementById('descSitPrevia').value = desc;
            
                    desc = "";
                    codAct = "";
                    codigo = "";
                    codigo = "<%=p1 != null && p1.getSitPrevia() != null ? String.valueOf(p1.getSitPrevia()) : ""%>";
                    //alert("codigo: " + codigo);
                    if(codigo != null && codigo != "")
                    {
                        for(var i=0; i<codSitPrevia.length; i++)
                        {
                            codAct = codSitPrevia[i];
                            if(codAct == codigo)
                            {
                                desc = descSitPrevia[i];
                            }
                        }
                    }
                    document.getElementById('descSitPrevia2').value = desc;
                }

                function cargarCombos() { 
                    //Persona Contratada
                    comboEstudiosPC.addItems(codEstudios, descEstudios);
                    comboColectivoPC.addItems(codColectivos, descColectivos);
                    comboColectivoPS.addItems(codColec2, descColec2);
                    comboTipoCon.addItems(codTipoCon, descTipoCon);
                    comboEstudiosPS.addItems(codEstudios, descEstudios);
                    comboSitPrevia.addItems(codSitPrevia, descSitPrevia);
                    comboSitPrevia2.addItems(codSitPrevia, descSitPrevia);
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
                    //validaciones nuevos campos
                    var colecPC = document.getElementById('codColectivoPC').value;
                    var colecPS = document.getElementById('codColectivoPS').value;
                    var dias = document.getElementById('txtDias').value;
                    
                    if(hayNifsRepetidos())
                        correcto = false;
                    if (colecPC=="3" || colecPC=='4'){
                        if(!validarDatosPersonaContratada())
                            correcto = false;
                    }
                    if(!validarDatosPersonaAdicional())
                        correcto = false;
                    
                    if(!validarDatosPersonaRelevista())
                        correcto = false;
                   
            
                    if(colecPC == "1" && (dias == "" || dias == "0")){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "campoDias")%>";
                        return false;
                    }
                    var fecIni = document.getElementById('fechaIniContrato').value;
                    var fecFin = document.getElementById('fechaFinContrato').value;
                    if(colecPC == "" && colecPC != "1" && (fecIni =="" || fecFin =="")){                
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaContratoPrevio")%>";
                        return false;
                    }
                    //colec = document.getElementById('codColectivoPS').value;
                    //dias = document.getElementById('txtDiasI').value;
                    if(colecPC == "1" && (dias == "" || dias == "0")){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "campoDias")%>";
                        return false;
                    }  
                    
                    if (colecPS=="2"){//Obligatorio si código de 5.1 es 2
                        fecIni = document.getElementById('fechaInConPre2').value;
                        fecFin = document.getElementById('fechaFinConPre2').value;

                        if(/*colecPC !== "" && colecPC !== "1" && */(fecIni === "" || fecFin ==="")){                                    
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaContratoPrevioAd")%>";
                            return false;
                        }
                    }
                    
                    var diasI = document.getElementById('txtDiasI').value;
                    if (colecPS=="1" && diasI==""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "campoDias")%>";
                        return false;
                    }
                    
                    var diasF = document.getElementById('txtDiasF').value;
                    var diasC = document.getElementById('txtDiasContrato').value;
                    if(diasF != "" && diasC != "")
                    {
                        var f = parseInt(diasF);
                        var c = parseInt(diasC); 
                        if(c < f){
                            mensajeError = "El numero de días del contrato adicional es menor que el número de días para fin contrato";
                            return false;
                        }
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
                            if (dni.substring(0,1)=='X' || dni.substring(0,1)=='Y' ||dni.substring(0,1)=='Z' ){
                                /*   //nuevo
                               numero = dni.substr(0,dni.length-1);
                                numero = numero.replace('X', 0);
                                numero = numero.replace('Y', 1);
                                numero = numero.replace('Z', 2);
                                posicion = numero.substring(0,longitud-1) % 23;
                            letra=cadena.charAt(posicion);
                            if (isNaN(numero.substring(0,longitud-1))) {
                                return false;
                            }
                            if(aux!=letra) {
                                return false;
                            }*/
                                return true;
                            }else{
                               
                                posicion = dni.substring(0,longitud-1) % 23;
                                letra=cadena.charAt(posicion);
                                if (isNaN(dni.substring(0,longitud-1))) {
                                    return false;
                                }
                                if(aux!=letra) {
                                    return false;
                                }
                            }
                        }else{
                            if (dni.substring(0,1)=='X' || dni.substring(0,1)=='Y' ||dni.substring(0,1)=='Z' ){
                                /*   numero = dni.substr(0,dni.length-1);
                                numero = numero.replace('X', 0);
                                numero = numero.replace('Y', 1);
                                numero = numero.replace('Z', 2);
                                posicion = numero.substring(0,longitud) % 23;
                                letra=cadena.charAt(posicion);
                                if (isNaN(numero.substring(0,longitud))) {
                                    return false;
                                }
                                campo.value =campo.value+letra;
                                 */
                                //dejar nie --> validación mal hecha
                                return true;
                            }else {
                                posicion = dni.substring(0,longitud) % 23;
                                letra=cadena.charAt(posicion);
                                if (isNaN(dni.substring(0,longitud))) {
                                    return false;
                                }
                                campo.value =dni+letra;
                                return true;
                            }
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
        
                function validarDatosPersonaRelevista(){
                    var correcto = true;
                    //Datos obligatorios
                    var dni = document.getElementById('txtDniPC').value;
                    if(dni == null || dni == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    var nombre = document.getElementById("txtNomPC").value;
                    if(nombre == null || nombre == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    var ape1 = document.getElementById("txtApe1PC").value;
                    if(ape1 == null || ape1 == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    /*var ape2 = document.getElementById("txtApe2PC").value;
            if(ape2 == null ||ape2 == ""){
                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }*/
                    var feNac = document.getElementById("fechaNacimientoPC").value;
                    if(feNac == null || feNac == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    //DNI
                    if(!validarNIF(document.getElementById('txtDniPC'))){
                        document.getElementById('txtDniPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPC').value)){
                            document.getElementById('txtDniPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtDniPC').removeAttribute("style");
                        }
                    }
                    //Nombre
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPC').value)){
                        document.getElementById('txtNomPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtNomPC').removeAttribute("style");
                    }
                    //Apellido 1
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PC').value)){
                        document.getElementById('txtApe1PC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtApe1PC').removeAttribute("style");
                    }
                    //Apellido 2
                    if(document.getElementById('txtApe2PC').value != null && document.getElementById('txtApe2PC').value != ""){
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PC').value)){
                            document.getElementById('txtApe2PC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtApe2PC').removeAttribute("style");
                        }
                    }
                    //Fecha nacimiento
                    if(!validarFecha(document.getElementById('fechaNacimientoPC'))){
                        document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
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
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                    correcto = false;
                }*/
                        if(result < 0){
                            document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
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
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nivelEstudiosNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
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
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "colectivoNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }
                        else{
                            document.getElementById('codColectivoPC').removeAttribute("style");
                            document.getElementById('descColectivoPC').removeAttribute("style");
                        }
                    }    
                        
                    //situación previa obligatoria si codigo=2
                    if (col=="2"){
                        var encontrado = false;
                        var i = 0;
                        while(i < codSitPrevia.length && !encontrado){
                            if(codSitPrevia[i] == col){
                                encontrado = true;
                            }
                            i++;
                        }
                        if(!encontrado){
                            document.getElementById('codSitPrevia').style.border = '1px solid red';
                            document.getElementById('descSitPrevia').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "sitPreviaNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }
                        else{
                            document.getElementById('codSitPrevia').removeAttribute("style");
                            document.getElementById('descSitPrevia').removeAttribute("style");
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
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                        correcto = false;
                    }else{
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPA').value)){
                            document.getElementById('txtDniPA').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtDniPA').removeAttribute("style");
                        }
                    }
                    //Nombre
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPA').value)){
                        document.getElementById('txtNomPA').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtNomPA').removeAttribute("style");
                    }
                    //Apellido 1
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PA').value)){
                        document.getElementById('txtApe1PA').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtApe1PA').removeAttribute("style");
                    }
                    //Apellido 2
                    if(document.getElementById('txtApe2PA').value != null && document.getElementById('txtApe2PA').value != ""){
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PA').value)){
                            document.getElementById('txtApe2PA').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtApe2PA').removeAttribute("style");
                        }
                    }
                    //Fecha nacimiento
                    if(!validarFecha(document.getElementById('fechaNacimientoPA'))){
                        document.getElementById('fechaNacimientoPA').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
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
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                    correcto = false;
                }*/
                        if(result < 0){
                            document.getElementById('fechaNacimientoPA').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratoAdicional")%>"+"'";
                            correcto = false;
                        }
                        else{
                            document.getElementById('fechaNacimientoPA').removeAttribute("style");
                        }
                    }
                    return correcto;
                }
        
                //function validarDatosPersonaSustituida(){
                function validarDatosPersonaContratada(){
                    var correcto = true;
                    //Datos obligatorios
                    var dni = document.getElementById('txtDniPS').value;
                    if(dni == null || dni == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    var nombre = document.getElementById("txtNomPS").value;
                    if(nombre == null || nombre == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    var ape1 = document.getElementById("txtApe1PS").value;
                    if(ape1 == null || ape1 == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    /*var ape2 = document.getElementById("txtApe2PS").value;
            if(ape2 == null ||ape2 == ""){
                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                return false;
            }*/
                    var feNac = document.getElementById("fechaNacimientoPS").value;
                    if(feNac == null || feNac == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false;
                    }
                    var sexo = document.getElementById("txtSexoPS").value;
                    if(sexo == null || feNac == ""){
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "camposRojosOblig")%>";
                        return false
                    }
                    //DNI
                    if(!validarNIF(document.getElementById('txtDniPS'))){
                        document.getElementById('txtDniPS').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                        correcto = false;
                    }else{
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtDniPS').value)){
                            document.getElementById('txtDniPS').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtDniPS').removeAttribute("style");
                        }
                    }
                    //Nombre
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtNomPS').value)){
                        document.getElementById('txtNomPS').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtNomPS').removeAttribute("style");
                    }
                    //Apellido 1
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1PS').value)){
                        document.getElementById('txtApe1PS').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                        correcto = false;
                    }else{
                        document.getElementById('txtApe1PS').removeAttribute("style");
                    }
                    //Apellido 2
                    if(document.getElementById('txtApe2PS').value != null && document.getElementById('txtApe2PS').value != ""){
                        if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2PS').value)){
                            document.getElementById('txtApe2PS').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                            correcto = false;
                        }else{
                            document.getElementById('txtApe2PS').removeAttribute("style");
                        }
                    }
                    //Fecha nacimiento
                    if(!validarFecha(document.getElementById('fechaNacimientoPS'))){
                        document.getElementById('fechaNacimientoPS').style.border = '1px solid red';
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
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
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                    correcto = false;
                }*/
                        if(result < 0){
                            document.getElementById('fechaNacimientoPS').style.border = '1px solid red';
                            if(mensajeError == "")
                                mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
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
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "sexoNoVal")%>"+" '"+"<%=meLanbide61I18n.getMensaje(idiomaUsuario, "legend.personaSustituida")%>"+"'";
                        correcto = false;
                    }
                    else{
                        document.getElementById('txtSexoPS').removeAttribute("style");
                    }
                    
                    //FECHAS CONTRATACION
                    var fecIni = document.getElementById('fechaInConAd').value;
                    var fecFin = document.getElementById('fechaFinConAd').value;

                    if (fecIni === "" || fecFin ===""){  
                        mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fechaContratoAd")%>";
                        return false;
                    }
                    
                    
                    return correcto;
                }
        
                function hayNifsRepetidos(){
                    var nif1 = document.getElementById('txtDniPS').value;
                    var nif2 = document.getElementById('txtDniPC').value;
                    var nif3 = document.getElementById('txtDniPA').value;
                    if(nif1 == nif2){
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nifsRepetidos12")%>";
                        return true;
                    }
                    else if(nif1 == nif3){
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nifsRepetidos13")%>";
                        return true;
                    }
                    else if(nif2 == nif3){
                        if(mensajeError == "")
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "nifsRepetidos23")%>";
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
                            mensajeError = "<%=meLanbide61I18n.getMensaje(idiomaUsuario, "fAltaAntFBaja")%>";
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
            
        //COMPROBAR DNIS
        var dni2 = document.getElementById('txtDniPC').value;
        comprobarDNI(dni2);        
        var dni3 = document.getElementById('txtDniPS').value;
        comprobarDNI(dni3);
        <%mensajeProgreso = meLanbide61I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos");%>
                    barraProgresoContrato('on', 'barraProgresoContratoGuardar');
                   
                    //GRABAR SIEMPRE, AUNQUE HAYA ERRORES DE VALIDACIÓN
                    var errores=false;
                    if(!validarDatos()){
                        barraProgresoContrato('off', 'barraProgresoContratoGuardar');
                        jsp_alerta("A", mensajeError);
                        errores=true;
                    }
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var fondo = "N";
                    if(document.getElementById('fondo').checked)
                        fondo = "S";
                    if(nuevo != null && nuevo == "1"){
                        parametros = "tarea=preparar&modulo=MELANBIDE61&operacion=crearContrato&tipo=0&numero=<%=numExpediente%>"
                            +"&idTerPS="+document.getElementById('idTerPS').value+"&txtDniPS="+document.getElementById('txtDniPS').value+"&txtNomPS="+escape(document.getElementById('txtNomPS').value)+"&txtApe1PS="+escape(document.getElementById('txtApe1PS').value)
                            +"&txtApe2PS="+escape(document.getElementById('txtApe2PS').value)+"&fechaNacimientoPS="+document.getElementById('fechaNacimientoPS').value
                            +"&txtSexoPS="+document.getElementById('txtSexoPS').value+"&txtReduJorPA="+document.getElementById('txtReduJorPA').value
                            +"&codColectivoPS="+document.getElementById('codColectivoPS').value+"&fechaBajaPA="+document.getElementById('fechaBajaPA').value
                            +"&idTerPC="+document.getElementById('idTerPC').value+"&txtDniPC="+document.getElementById('txtDniPC').value+"&txtNomPC="+escape(document.getElementById('txtNomPC').value)+"&txtApe1PC="+escape(document.getElementById('txtApe1PC').value)
                            +"&txtApe2PC="+escape(document.getElementById('txtApe2PC').value)+"&fechaNacimientoPC="+document.getElementById('fechaNacimientoPC').value+"&txtSexoPC="+document.getElementById('txtSexoPC').value
                            +"&codEstudiosPC="+document.getElementById('codEstudiosPC').value+"&codColectivoPC="+document.getElementById('codColectivoPC').value
                            +"&idTerPA="+document.getElementById('idTerPA').value+"&txtDniPA="+document.getElementById('txtDniPA').value+"&txtNomPA="+escape(document.getElementById('txtNomPA').value)+"&txtApe1PA="+escape(document.getElementById('txtApe1PA').value)+"&txtApe2PA="+escape(document.getElementById('txtApe2PA').value)+"&fechaNacimientoPA="
                            +document.getElementById('fechaNacimientoPA').value+"&txtSexoPA="+document.getElementById('txtSexoPA').value
                            +"&codTipoCon="+document.getElementById('codTipoCon').value
                            +"&fechaConPC="+document.getElementById('fechaConPC').value
                            +"&txtRetribucionPC="+convertirANumero(document.getElementById('txtRetribucionPC').value)
                        //+"&fechaInConPre="+document.getElementById('fechaInConPre').value
                        //+"&fechaFinConPre="+document.getElementById('fechaFinConPre').value
                            +"&fechaInConPre2="+document.getElementById('fechaInConPre2').value
                            +"&fechaFinConPre2="+document.getElementById('fechaFinConPre2').value
                            +"&fechaInConAd="+document.getElementById('fechaInConAd').value
                            +"&fechaFinConAd="+document.getElementById('fechaFinConAd').value
                            +"&convenio="+document.getElementById('txtConvenio').value
                            +"&fechaPublica="+document.getElementById('fechaPublica').value
                            +"&dias="+document.getElementById('txtDias').value
                            +"&fechaIniContrato="+document.getElementById('fechaIniContrato').value
                            +"&fechaFinContrato="+document.getElementById('fechaFinContrato').value
                            +"&diasF="+document.getElementById('txtDiasF').value
                            +"&diasI="+document.getElementById('txtDiasI').value
                            +"&diasContrato="+document.getElementById('txtDiasContrato').value
                            +"&codEstudiosPS="+document.getElementById('codEstudiosPS').value
                            +"&codSitPrevia="+document.getElementById('codSitPrevia').value
                            +"&codSitPrevia2="+document.getElementById('codSitPrevia2').value
                            +"&fondo="+fondo;
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE61&operacion=guardarContrato&tipo=0&numero=<%=numExpediente%>&idCon=<%=contrato.getNumContrato()%>"
                            +"&txtDniPS="+document.getElementById('txtDniPS').value
                            +"&txtNomPS="+escape(document.getElementById('txtNomPS').value)+"&txtApe1PS="+escape(document.getElementById('txtApe1PS').value)
                            +"&txtApe2PS="+escape(document.getElementById('txtApe2PS').value)+"&fechaNacimientoPS="+document.getElementById('fechaNacimientoPS').value
                            +"&txtSexoPS="+document.getElementById('txtSexoPS').value+"&txtReduJorPA="+document.getElementById('txtReduJorPA').value
                            +"&fechaBajaPA="+document.getElementById('fechaBajaPA').value
                            +"&codColectivoPS="+document.getElementById('codColectivoPS').value
                            +"&txtDniPC="+document.getElementById('txtDniPC').value
                            +"&txtNomPC="+escape(document.getElementById('txtNomPC').value)+"&txtApe1PC="+escape(document.getElementById('txtApe1PC').value)
                            +"&txtApe2PC="+escape(document.getElementById('txtApe2PC').value)+"&fechaNacimientoPC="+document.getElementById('fechaNacimientoPC').value+"&txtSexoPC="+document.getElementById('txtSexoPC').value
                            +"&codEstudiosPC="+document.getElementById('codEstudiosPC').value+"&codColectivoPC="+document.getElementById('codColectivoPC').value
                            +"&txtDniPA="+document.getElementById('txtDniPA').value
                            +"&txtNomPA="+escape(document.getElementById('txtNomPA').value)+"&txtApe1PA="+escape(document.getElementById('txtApe1PA').value)+"&txtApe2PA="+escape(document.getElementById('txtApe2PA').value)+"&fechaNacimientoPA="
                            +document.getElementById('fechaNacimientoPA').value+"&txtSexoPA="+document.getElementById('txtSexoPA').value
                            +"&codTipoCon="+document.getElementById('codTipoCon').value
                            +"&fechaConPC="+document.getElementById('fechaConPC').value
                            +"&txtRetribucionPC="+convertirANumero(document.getElementById('txtRetribucionPC').value)
                        //+"&fechaInConPre="+document.getElementById('fechaInConPre').value
                        //+"&fechaFinConPre="+document.getElementById('fechaFinConPre').value
                            +"&fechaInConPre2="+document.getElementById('fechaInConPre2').value
                            +"&fechaFinConPre2="+document.getElementById('fechaFinConPre2').value
                            +"&fechaInConAd="+document.getElementById('fechaInConAd').value
                            +"&fechaFinConAd="+document.getElementById('fechaFinConAd').value
                            +"&convenio="+document.getElementById('txtConvenio').value
                            +"&fechaPublica="+document.getElementById('fechaPublica').value
                            +"&dias="+document.getElementById('txtDias').value
                            +"&fechaIniContrato="+document.getElementById('fechaIniContrato').value
                            +"&fechaFinContrato="+document.getElementById('fechaFinContrato').value
                            +"&diasF="+document.getElementById('txtDiasF').value
                            +"&diasI="+document.getElementById('txtDiasI').value
                            +"&diasContrato="+document.getElementById('txtDiasContrato').value
                            +"&codEstudiosPS="+document.getElementById('codEstudiosPS').value
                            +"&codSitPrevia="+document.getElementById('codSitPrevia').value
                            +"&codSitPrevia2="+document.getElementById('codSitPrevia2').value
                            +"&fondo="+fondo;
                        if(errores) 
                            parametros+="&errores=si";
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
                            jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                    
                    }//try-catch
                    barraProgresoContrato('off', 'barraProgresoContratoGuardar');
                }
                   
                
            
                function cancelar(){
                    var resultado = jsp_alerta("","<%=meLanbide61I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
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
                
                function comprobarDNI(dni){
                    var control = new Date();
                    var dni = dni;
                    var parametros = "tarea=preparar&modulo=MELANBIDE61&operacion=comprobarDNIEnExpedientes&tipo=0&numero=<%=numExpediente%>&dni="+dni+"&control='"+control.getTime();
                    realizarLlamada(parametros);
                }
                
                function realizarLlamada(parametros){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
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
                        var nodoValores;
                        var hijosValores;
                        for(var j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="VALORES"){
                                nodoValores = hijos[j];
                                hijosValores = nodoValores.childNodes;                 
                            }  
                            else if(hijos[j].nodeName=="MENSAJE"){
                                nodoValores = hijos[j];
                                hijosValores = nodoValores.childNodes;
                                if (hijosValores[0].nodeValue!="")
                                    alert(hijosValores[0].nodeValue);
                                //document.getElementById('lblMensaje').innerHTML = hijosValores[0].nodeValue;
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            document.getElementById('lblMensaje').className = "textoAzul";
                        }else if(codigoOperacion=="1"){
                            document.getElementById('lblMensaje').className = "textoRojo";
                        }else if(codigoOperacion=="2"){
                            document.getElementById('lblMensaje').className = "textoRojo";
                        }else if(codigoOperacion=="3"){
                            document.getElementById('lblMensaje').className = "textoRojo";
                        }else{
                    
                        }//if(
            
                        deshabilitarCampos();
                    }
                    catch(Err){

                    }//try-catch
                }
                
        </script>
    </head>
    <body onload="inicio();" id="cuerpoNuevoContratoRenovacion" style="margin: 10px; text-align: left; width: 98%;" class="contenidoPantalla">
        <div  class="contenidoPantalla">
            <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                <span>Renovación Plantilla</span>
            </div>
            <form action="<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE61&operacion=guardarContrato&tipo=0&numero=<%=numExpediente%>&idCon=<%=contrato.getNumContrato()%>" method="POST" id="formContrato">

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
                                                            <%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.cargarDatos")%>
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
                                                            <%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                        <fieldset style="border:1px solid grey">
                            <legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.personaContratoAdicional")%></legend>
                            <!-- DATOS PERSONA TRABAJADORA ACOJIDA A LA JUBILIDACIÓN -->
                            <div class="lineaFormulario">
                                <div style="float: left;padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dni")%>
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" size="16" maxlength="25" id="txtDniPA" name="txtDniPA" class="inputTexto" value="<%=p3 != null && p3.getNumDoc() != null ? p3.getNumDoc().toUpperCase() : ""%>"/>
                                </div>
                                <div style="float: left;padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nombreApel")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" size="29" maxlength="80" id="txtNomPA"  class="inputTexto" name="txtNomPA" value="<%=p3 != null && p3.getNombre() != null ? p3.getNombre().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe1PA" class="inputTexto" name="txtApe1PA" value="<%=p3 != null && p3.getApellido1() != null ? p3.getApellido1().toUpperCase() : ""%>"/>
                                    <input type="text" size="29" maxlength="100" id="txtApe2PA" class="inputTexto" name="txtApe2PA" value="<%=p3 != null && p3.getApellido2() != null ? p3.getApellido2().toUpperCase() : ""%>"/>
                                </div>
                            </div>
                            <div style="float: left; padding-left: 25px" id="divBtnBuscarPA">
                                <input type="button"class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PA');"/>
                            </div>
                            <div class="lineaFormulario">
                                <div style="float: left; padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPA" name="fechaNacimientoPA" onkeyup="return SoloCaracteresFecha(this);"  onblur="calcularEdadPA();" onfocus="this.select();" value="<%=p3 != null && p3.getFeNac() != null ? formatter.format(p3.getFeNac()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPA(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaNacPA" name="calFechaNacPA" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                                <div style="float: left; padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.edad")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" size="3" class="inputTexto" id="txtEdadPA"  name="txtEdadPA"/>
                                </div>
                                <div style="float: left; padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.sexo")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text"  maxlength="1" class="inputTexto" size="2" id="txtSexoPA" name="txtSexoPA" value="<%=p3 != null && p3.getSexo() != null ? (p3.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                </div>                                    
                                <div style="float: left; padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.porReduJor")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" maxlength="6" size="2" id="txtReduJorPA" name="txtReduJorPA" class="inputTexto" value="<%=p3 != null && p3.getPorReduJor() != null ? String.valueOf(p3.getPorReduJor()) : ""%>" onchange="reemplazarPuntos(this);"/>
                                </div>                                
                                <div style="float: left; padding-right: 10px;" class="etiqueta obligatorio">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fBaja")%>*
                                </div>
                                <div style="float: left; padding-right: 10px;">
                                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaBajaPA" class="inputTexto" name="fechaBajaPA" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" onchange="calcularDuracionContrato();" value="<%=p3 != null && p3.getFeBaja() != null ? formatter.format(p3.getFeBaja()) : ""%>"/>
                                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaBajaPA(event);return false;" style="text-decoration:none;" >
                                        <IMG style="border: 0" height="17" id="calFechaBajaPA" name="calFechaBajaPA" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fBaja")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                    </A>
                                </div>
                            </div>
                        </fieldset>
                        <br />

                        <fieldset style="border:1px solid grey">
                            <!-- CONTRATO DE RELEVO -->
                            <legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.contratoRelevo")%></legend>
                            <fieldset style="border:1px solid #C0C0C0"><legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.personaContratada")%></legend>
                                <!-- DATOS PERSONA RELEVISTA -->
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px" class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dni")%>*                                    
                                    </div>
                                    <div style="float: left;">
                                        <div style="float: left; padding-right: 10px">
                                            <input type="text" size="16" maxlength="25" id="txtDniPC" name="txtDniPC" class="inputTexto" value="<%=p2 != null && p2.getNumDoc() != null ? p2.getNumDoc().toUpperCase() : ""%>"/>
                                        </div>
                                    </div>
                                    <div style="float: left; padding-right: 10px" class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nombreApel")%>*
                                    </div>
                                    <div style="float: left; padding-right: 10px">
                                        <input type="text" size="29" maxlength="80" id="txtNomPC" name="txtNomPC"  class="inputTexto" value="<%=p2 != null && p2.getNombre() != null ? p2.getNombre().toUpperCase() : ""%>"/>
                                        <input type="text" size="29" maxlength="100" id="txtApe1PC" name="txtApe1PC" class="inputTexto" value="<%=p2 != null && p2.getApellido1() != null ? p2.getApellido1().toUpperCase() : ""%>"/>
                                        <input type="text" size="29" maxlength="100" id="txtApe2PC" name="txtApe2PC" class="inputTexto" value="<%=p2 != null && p2.getApellido2() != null ? p2.getApellido2().toUpperCase() : ""%>"/>
                                    </div>
                                </div>
                                <div style="float: left; padding-left: 25px" id="divBtnBuscarPC">
                                    <input type="button" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PC');"/>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px" class="etiqueta obligatorio" >
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>*
                                    </div>
                                    <div style="float: left;padding-right: 20px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPC" name="fechaNacimientoPC" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularEdadPC();" onfocus="this.select();" value="<%=p2 != null && p2.getFeNac() != null ? formatter.format(p2.getFeNac()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPC(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaNacPC" name="calFechaNacPC" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left;  padding-right: 10px;"  class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.edad")%>*
                                    </div>
                                    <div style="float: left;padding-right: 20px;">
                                        <input type="text" size="3" class="inputTexto" id="txtEdadPC" name="txtEdadPC"/>
                                    </div>
                                    <div style="float: left; padding-right: 10px;"  class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.sexo")%>*
                                    </div>
                                    <div  style="float: left;padding-right: 20px;">
                                        <input type="text"  maxlength="1" size="2" class="inputTexto"  id="txtSexoPC" name="txtSexoPC" value="<%=p2 != null && p2.getSexo() != null ? (p2.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                    </div>
                                    <div style="float: left;  padding-right: 10px" class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nEstudios")%>*
                                    </div>
                                    <div  style="float: left;padding-right: 10px;">
                                        <input id="codEstudiosPC" name="codEstudiosPC" type="text" class="inputTexto" size="2" maxlength="3" 
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getNivelEstudios() != null ? String.valueOf(p2.getNivelEstudios()) : ""%>" >
                                        <input id="descEstudiosPC"	name="descEstudiosPC" type="text" class="inputTexto" size="20" readonly >
                                        <a id="anchorEstudiosPC" name="anchorEstudiosPC" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEstudiosPC" name="botonEstudiosPC" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.codigo")%>*
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input id="codColectivoPC" name="codColectivoPC" type="text" class="inputTexto" size="2" maxlength="2"
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getColectivo() != null ? String.valueOf(p2.getColectivo()) : ""%>">
                                        <input id="descColectivoPC" name="descColectivoPC" type="text" class="inputTexto" size="100" readonly>
                                        <a id="anchorColectivosPC" name="anchorColectivoPC" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonColectivoPC" name="botonColectivoPC" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fondo")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="checkbox" id="fondo" name="fondo" value="S" <%=p2.getFondo() != null && p2.getFondo().equals("S")? "checked='checked'" :""%> >
                                    </div>
                                </div>
                                <div style="float: left;  padding-right: 10px" class="etiqueta">
                                    <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.sitPrevia")%>
                                </div>
                                <div  style="float: left;padding-right: 10px;">
                                    <input id="codSitPrevia" name="codSitPrevia" type="text" class="inputTexto" size="2" maxlength="3" 
                                           onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getSitPrevia() != null ? String.valueOf(p2.getSitPrevia()) : ""%>" >
                                    <input id="descSitPrevia"	name="descSitPrevia" type="text" class="inputTexto" size="30" readonly >
                                    <a id="anchorSitPrevia" name="anchorSitPrevia" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonSitPrevia" name="botonSitPrevia" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                </div>
                            </fieldset>
                            <fieldset style="border:1px solid #C0C0C0"><legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.datosContratoRelevo")%></legend>
                                <!-- DATOS CONTRATO DE RELEVO -->
                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>*
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input id="codTipoCon" name="codTipoCon" type="text" class="inputTexto" size="2" maxlength="2"
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p2 != null && p2.getTipoContrato() != null ? String.valueOf(p2.getTipoContrato()) : "IN"%>">
                                        <input id="descTipoCon" name="descTipoCon" type="text" class="inputTexto" size="20" readonly>
                                        <a id="anchorTipoCon" name="anchorTipoCon" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonColectivoPC" name="botonColectivoPC" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                    <div style="float: left;padding-right: 10px;" class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecInicioContrato")%>*
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" class="inputTexto" id="fechaConPC" name="fechaConPC" onblur="calcularDiasF();calcularEdadPC();calcularEdadPA(); " onkeyup="return SoloCaracteresFecha(this);"  onfocus="this.select();" value="<%=p2 != null && p2.getFeAlta() != null ? formatter.format(p2.getFeAlta()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaConPC(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaConPA" name="calFechaConPC" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecInicioContrato")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left; padding-right: 10px;"  class="etiqueta obligatorio">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.retribucionAnual")%>*
                                    </div>
                                    <div  style="float: left;padding-right: 20px;">
                                        <input type="text" maxlength="15" size="15" id="txtRetribucionPC"  class="inputTexto" name="txtRetribucionPC" 
                                               value="<%=p2 != null && p2.getRetSalarial() != null ? p2.getRetSalarial().toPlainString().replace(".",",") : ""%>" 
                                               onkeyup="FormatNumber(this.value, 8, 2, this.id);"
                                               onblur="ajustarDecimalesCampo(this, 2);"/>

                                    </div>
                                </div>

                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.acuerdo")%>
                                    </div>
                                    <div  style="float: left;padding-right: 20px;">
                                        <input type="text" maxlength="300" size="90" id="txtConvenio"  class="inputTexto" name="txtConvenio" value="<%=p2 != null && p2.getConvenio() != null ? p2.getConvenio() : ""%>" />
                                    </div>
                                </div>

                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fPublicacion")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" class="inputTexto" id="fechaPublica" name="fechaPublica" onkeyup="return SoloCaracteresFecha(this);"  onfocus="this.select();" value="<%=p2 != null && p2.getFechaPublica() != null ? formatter.format(p2.getFechaPublica()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaPublica(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaPublica" name="calFechaPublica" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fPublicacion")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dias")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" maxlength="5" size="4" id="txtDias"  class="inputTexto" name="txtDias" value="<%=p2 != null && p2.getDias() != null ? p2.getDias() : ""%>" />
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioContrato")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaIniContrato" name="fechaIniContrato" onkeyup="return SoloCaracteresFecha(this);" onchange="calcularDiasF();"  onblur="calcularDiasF();" onfocus="this.select();" value="<%=p2 != null && p2.getFechaIniContrato() != null ? formatter.format(p2.getFechaIniContrato()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaIniContrato(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaIniContrato" name="calFechaIniContrato" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioContrato")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinContrato")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinContrato" name="fechaFinContrato" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularDiasF()" onfocus="this.select();"  value="<%=p2 != null && p2.getFechaFinContrato() != null ? formatter.format(p2.getFechaFinContrato()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinContrato(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaFinContrato" name="calFechaFinContrato" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConPre")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.diasFin")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" maxlength="5" size="4" id="txtDiasF"  class="inputTexto" name="txtDiasF" value="<%=p2 != null && p2.getDiasF() != null ? p2.getDiasF() : ""%>" />
                                    </div>


                                </div>
                            </fieldset> 
                            <!-- PERSONA RELEVISTA MENOR...  -->
                    <!--fieldset style="border:1px solid #C0C0C0"><legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.personaRelMenor")%></legend>
                        <div class="lineaFormulario">
                            <div style="float: left; padding-right: 10px;" class="etiqueta">
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioConPre")%>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInConPre" class="inputTexto" name="fechaInConPre" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value="<%=p3 != null && p3.getFecInConPre() != null ? formatter.format(p3.getFecInConPre()) : ""%>"/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaInConPre(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calFechaInConPre" name="calFechaInConPre" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioConPre")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                            </A>
                        </div>
                        <div style="float: left; padding-right: 10px;" class="etiqueta">
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConPre")%>
                        </div>
                        <div style="float: left; padding-right: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinConPre" name="fechaFinConPre" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value="<%=p3 != null && p3.getFecFinConPre() != null ? formatter.format(p3.getFecFinConPre()) : ""%>"/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinConPre(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calFechaFinConPre" name="calFechaFinConPre" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConPre")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                            </A>
                        </div>
                    </div>
                </fieldset-->
                        </fieldset>
                        <br />


                        <!-- CONTRATO ADICIONAL -->
                        <fieldset style="border:1px solid grey"><legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.contratacionAd")%></legend>
                            <fieldset style="border:1px solid #C0C0C0"> <!-- DATOS PERSONA CONTRATADA -->
                                <legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.personaSustituida")%></legend>
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dni")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" size="16" maxlength="25" id="txtDniPS" name="txtDniPS" class="inputTexto" value="<%=p1 != null && p1.getNumDoc() != null ? p1.getNumDoc().toUpperCase() : ""%>"/>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nombreApel")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" size="29" maxlength="80" class="inputTexto" id="txtNomPS" name="txtNomPS" value="<%=p1 != null && p1.getNombre() != null ? p1.getNombre().toUpperCase() : ""%>"/>
                                        <input type="text" size="29" maxlength="100" class="inputTexto" id="txtApe1PS" name="txtApe1PS" value="<%=p1 != null && p1.getApellido1() != null ? p1.getApellido1().toUpperCase() : ""%>"/>
                                        <input type="text" size="29" maxlength="100" class="inputTexto" id="txtApe2PS" name="txtApe2PS" value="<%=p1 != null && p1.getApellido2() != null ? p1.getApellido2().toUpperCase() : ""%>"/>
                                    </div>
                                </div>
                                <div style="float: left; padding-left: 25px"  id="divBtnBuscarPS">
                                    <input type="button" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="mostrarBusqueda('PS');"/>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimientoPS" name="fechaNacimientoPS" onkeyup="return SoloCaracteresFecha(this);"  onblur="calcularEdadPS();" onfocus="this.select();" value="<%=p1 != null && p1.getFeNac() != null ? formatter.format(p1.getFeNac()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimientoPS(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaNacPS" name="calFechaNacPS" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.edad")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" size="3" class="inputTexto" id="txtEdadPS" name="txtEdadPS"/>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" maxlength="1" size="2" id="txtSexoPS" name="txtSexoPS" class="inputTexto" value="<%=p1 != null && p1.getSexo() != null ? (p1.getSexo() == 1 ? "H" : "M") : ""%>" onkeyup="comprobarSexo(this);"/>
                                    </div>
                                    <div style="float: left;  padding-right: 10px" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.nEstudios")%>
                                    </div>
                                    <div  style="float: left;padding-right: 10px;">
                                        <input id="codEstudiosPS" name="codEstudiosPS" type="text" class="inputTexto" size="2" maxlength="3" 
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p1 != null && p1.getNivelEstudios() != null ? String.valueOf(p1.getNivelEstudios()) : ""%>" >
                                        <input id="descEstudiosPS"	name="descEstudiosPS" type="text" class="inputTexto" size="20" readonly >
                                        <a id="anchorEstudiosPS" name="anchorEstudiosPS" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEstudiosPC" name="botonEstudiosPS" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                </div>
                                <div class="lineaFormulario">                                    
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.codigo")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input id="codColectivoPS" name="codColectivoPS" type="text" class="inputTexto" size="2" maxlength="2"
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p1 != null && p1.getColectivo() != null ? String.valueOf(p1.getColectivo()) : ""%>">
                                        <input id="descColectivoPS" name="descColectivoPS" type="text" class="inputTexto" size="100" readonly>
                                        <a id="anchorColectivosPS" name="anchorColectivoPS" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonColectivoPS" name="botonColectivoPS" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                </div>
                                <div class="lineaFormulario">                                   


                                    <div style="float: left;  padding-right: 10px" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.sitPrevia")%>
                                    </div>
                                    <div  style="float: left;padding-right: 10px;">
                                        <input id="codSitPrevia2" name="codSitPrevia2" type="text" class="inputTexto" size="2" maxlength="3" 
                                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="<%=p1 != null && p1.getSitPrevia() != null ? String.valueOf(p1.getSitPrevia()) : ""%>" >
                                        <input id="descSitPrevia2"	name="descSitPrevia2" type="text" class="inputTexto" size="30" readonly >
                                        <a id="anchorSitPrevia2" name="anchorSitPrevia2" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonSitPrevia2" name="botonSitPrevia2" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                                    </div>
                                </div>

                            </fieldset>
                            <fieldset style="border:1px solid #C0C0C0"><legend class="legendAzul"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"legend.datosContratoAd")%></legend>                               
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioConAd")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInConAd" name="fechaInConAd" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularDiasContrato();calcularEdadPS();" onfocus="this.select();" onchange="calcularDuracionContrato();" value="<%=p1 != null && p1.getFecInConAd() != null ? formatter.format(p1.getFecInConAd()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaInConAd(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaInConAd" name="calFechaInConAd" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioConAd")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConAd")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinConAd" name="fechaFinConAd" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularDiasContrato()" onfocus="this.select();" onchange="calcularDuracionContrato();" value="<%=p1 != null && p1.getFecFinConAd() != null ? formatter.format(p1.getFecFinConAd()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinConAd(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaFinConAd" name="calFechaFinConAd" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConAd")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                </div>
                                <div class="lineaFormulario">
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioContrato2")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInConPre2" class="inputTexto" name="fechaInConPre2" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value="<%=p3 != null && p3.getFecInConPre2() != null ? formatter.format(p3.getFecInConPre2()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaInConPre2(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaInConPre2" name="calFechaInConPre2" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecIncioConPre")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                    <div style="float: left; padding-right: 10px;" class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinContrato2")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinConPre2" name="fechaFinConPre2" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value="<%=p3 != null && p3.getFecFinConPre2() != null ? formatter.format(p3.getFecFinConPre2()) : ""%>"/>
                                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinConPre2(event);return false;" style="text-decoration:none;" >
                                            <IMG style="border: 0" height="17" id="calFechaFinConPre2" name="calFechaFinConPre2" alt="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.fecFinConPre")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                        </A>
                                    </div>
                                </div>
                                <div class="lineaFormulario">  
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.dias")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" maxlength="5" size="4" id="txtDiasI"  class="inputTexto" name="txtDiasI" value="<%=p1 != null && p1.getDiasI() != null ? p1.getDiasI() : ""%>" />
                                    </div>  
                                    <div style="float: left;padding-right: 10px;"  class="etiqueta">
                                        <%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.diasContrato")%>
                                    </div>
                                    <div style="float: left; padding-right: 10px;">
                                        <input type="text" maxlength="5" size="4" id="txtDiasContrato"  class="inputTexto" name="txtDiasContrato" value="<%=p1 != null && p1.getDiasContrato() != null ? p1.getDiasContrato() : ""%>" />
                                    </div>
                                </div>

                            </fieldset>
                        </fieldset>
                    </div>    
                    <div style="width: 100%; text-align: center; padding-top: 5px;">
                        <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="pulsarAceptar();">
                        <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                        <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();">
                    </div>
                </div>

            </form>
            <div id="popupcalendar" class="text"></div>
            <input type="hidden" id="idTerPC" value="<%=p2 != null && p2.getCodTercero() != null ? p2.getCodTercero() : ""%>"/>
            <input type="hidden" id="idTerPA" value="<%=p3 != null && p3.getCodTercero() != null ? p3.getCodTercero() : ""%>"/>
            <input type="hidden" id="idTerPS" value="<%=p1 != null && p1.getCodTercero() != null ? p1.getCodTercero() : ""%>"/>
        </div>

        <script type="text/javascript">
            //Persona contratada
            var comboEstudiosPC = new Combo("EstudiosPC");
            var comboEstudiosPS = new Combo("EstudiosPS");
            var comboColectivoPC = new Combo("ColectivoPC");
            var comboColectivoPS = new Combo("ColectivoPS");
            var comboTipoCon = new Combo("TipoCon");
            var comboSitPrevia = new Combo ("SitPrevia");
            var comboSitPrevia2 = new Combo ("SitPrevia2");
            
        </script>
    </body>
</html>
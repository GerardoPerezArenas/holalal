<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide38.i18n.MeLanbide38I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide38.util.ConstantesMeLanbide38" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
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
            MeLanbide38I18n meLanbide38I18n = MeLanbide38I18n.getInstance();

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
            List<ValorCampoDesplegableModuloIntegracionVO> listaTipoContrato = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaTipoContrato") != null)
                listaTipoContrato = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaTipoContrato");
            List<ValorCampoDesplegableModuloIntegracionVO> listaCProf = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaCatProf") != null)
                listaCProf = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaCatProf");
            
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
            String lcodTipoContrato = "";
            String ldescTipoContrato = "";
            String lcodCProf = "";
            String ldescCProf = "";
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

            //Tipo Contrato
            if(listaTipoContrato != null && listaTipoContrato.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO tipoContrato = null;
                for(i = 0; i < listaTipoContrato.size() - 1; i++)
                {
                    tipoContrato = (ValorCampoDesplegableModuloIntegracionVO) listaTipoContrato.get(i);
                    lcodTipoContrato += "\"" + tipoContrato.getCodigo() + "\",";
                    ldescTipoContrato += "\"" + escape(tipoContrato.getDescripcion()) + "\",";
                }
                tipoContrato = (ValorCampoDesplegableModuloIntegracionVO) listaTipoContrato.get(i);
                lcodTipoContrato += "\"" + tipoContrato.getCodigo() + "\"";
                ldescTipoContrato += "\"" + escape(tipoContrato.getDescripcion()) + "\"";
            }

            //Categoria profesional
            if(listaCProf != null && listaCProf.size() > 0)
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO catProf = null;
                for(i = 0; i < listaCProf.size() - 1; i++)
                {
                    catProf = (ValorCampoDesplegableModuloIntegracionVO) listaCProf.get(i);
                    lcodCProf += "\"" + catProf.getCodigo() + "\",";
                    ldescCProf += "\"" + escape(catProf.getDescripcion()) + "\",";
                }
                catProf = (ValorCampoDesplegableModuloIntegracionVO) listaCProf.get(i);
                lcodCProf += "\"" + catProf.getCodigo() + "\"";
                ldescCProf += "\"" + escape(catProf.getDescripcion()) + "\"";
            }

            ContratoVO contratoModif = (ContratoVO)request.getAttribute("contratoModif");

            Boolean cerrado = request.getAttribute("cerrado") != null ? (Boolean)request.getAttribute("cerrado") : false;
            String tituloPagina = "";
            if(cerrado)
            {
                tituloPagina = meLanbide38I18n.getMensaje(idiomaUsuario,"label.tituloPagina.consultaContrato");
            }
            else
            {
                if(contratoModif != null)
                {
                    tituloPagina = meLanbide38I18n.getMensaje(idiomaUsuario,"label.tituloPagina.modifContrato");
                }
                else
                {
                    tituloPagina = meLanbide38I18n.getMensaje(idiomaUsuario,"label.tituloPagina.nuevoContrato");
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide38/melanbide38.css"/>

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
            var codTipoContrato = [<%=lcodTipoContrato%>];
            var descTipoContrato = [<%=ldescTipoContrato%>];
            var codCProf = [<%=lcodCProf%>];
            var descCProf = [<%=ldescCProf%>];
            
            //Mensaje para las validaciones
            var msgValidacion = '';
            
            
            
            function inicio(){
                cargarCombos();
                cargarDescripcionesCombos();
                var nuevo = '<%=nuevo%>';
                if(nuevo != null && nuevo == '1'){
                    document.getElementById('divBtnBuscar').style.display = 'inline';
                }
                else{
                    document.getElementById('divBtnBuscar').style.display = 'none';
                }
                cargarDatosContratoModif();
                calcularEdadTrabajador();
                calcularCosteSalarialTotal();
                //calcularDuracContrato();
                if(navigator.appName.indexOf('Internet Explorer')==-1){
                    document.getElementById('cuerpoNuevoContrato').style.width = '98%';
                }
                
                <%
                    if(cerrado == true)
                    {
                %>
                        //Deshabilito todos los campos
                        document.getElementById('txtDni').disabled = true;
                        document.getElementById('txtNom').disabled = true;
                        document.getElementById('txtApe1').disabled = true;
                        document.getElementById('txtApe2').disabled = true;
                        document.getElementById('fechaNacimiento').disabled = true;
                        document.getElementById('txtSexo').disabled = true;
                        document.getElementById('txtMesesDesempleo').disabled = true;
                        document.getElementById('codEstudios').disabled = true;
                        document.getElementById('descEstudios').disabled = true;
                        document.getElementById('botonEstudios').style.display = 'none';
                        document.getElementById('checkMinusvalido').disabled = true;
                        document.getElementById('checkInmig').disabled = true;
                        document.getElementById('checkPLD').disabled = true;
                        document.getElementById('checkRML').disabled = true;
                        document.getElementById('checkOtro').disabled = true;
                        document.getElementById('calFechaNac').style.display = 'none';

                        //Datos del Contrato
                        document.getElementById('fechaAlta').disabled = true;
                        document.getElementById('txtDuracionContrato').disabled = true;
                        document.getElementById('codTipoContrato').disabled = true;
                        document.getElementById('descTipoContrato').disabled = true;
                        document.getElementById('botonTipoContrato').style.display = 'none';
                        document.getElementById('codTipoJornada').disabled = true;
                        document.getElementById('descTipoJornada').disabled = true;
                        document.getElementById('botonTipoJornada').style.display = 'none';
                        document.getElementById('txtPorJornada').disabled = true;
                        document.getElementById('codTipConDur').disabled = true;
                        document.getElementById('descTipConDur').disabled = true;
                        document.getElementById('botonTipConDur').style.display = 'none';
                        document.getElementById('salarioBruto').disabled = true;
                        document.getElementById('codCNOE').disabled = true;
                        document.getElementById('descCNOE').disabled = true; 
                        document.getElementById('botonCNOE').style.display = 'none';
                        document.getElementById('segSocial').disabled = true;
                        document.getElementById('codColectivo').disabled = true;
                        document.getElementById('descColectivo').disabled = true;
                        document.getElementById('botonColectivo').style.display = 'none';
                        document.getElementById('codCProf').disabled = true;
                        document.getElementById('descCProf').disabled = true;
                        document.getElementById('botonCProf').style.display = 'none';
                        document.getElementById('calFechaAlta').style.display = 'none';

                        //Importe Subvenci�n
                        document.getElementById('concedido').disabled = true;
                        document.getElementById('modifRes').disabled = true;
                        document.getElementById('recurso').disabled = true;
                        
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
            }
            
            function cargarDatosContratoModif(){
                //Trabajador
                document.getElementById('txtDni').value = '<%=contratoModif != null && contratoModif.getDoc() != null ? contratoModif.getDoc() : ""%>';
                document.getElementById('txtNom').value = '<%=contratoModif != null && contratoModif.getNom() != null ? contratoModif.getNom() : ""%>';
                document.getElementById('txtApe1').value = '<%=contratoModif != null && contratoModif.getAp1() != null ? contratoModif.getAp1() : ""%>';
                document.getElementById('txtApe2').value = '<%=contratoModif != null && contratoModif.getAp2() != null ? contratoModif.getAp2() : ""%>';
                document.getElementById('fechaNacimiento').value = '<%=contratoModif != null && contratoModif.getFna() != null ? formatter.format(contratoModif.getFna()) : ""%>';
                document.getElementById('txtSexo').value = '<%=contratoModif != null && contratoModif.getSex() != null ? (contratoModif.getSex() == 1 ? ConstantesMeLanbide38.SEXO_H : ConstantesMeLanbide38.SEXO_M) : ""%>';
                document.getElementById('txtMesesDesempleo').value = '<%=contratoModif != null && contratoModif.getMde() != null ? contratoModif.getMde() : ""%>';
                document.getElementById('codEstudios').value = '<%=contratoModif != null && contratoModif.getNes() != null ? contratoModif.getNes().toString() : ""%>';
                document.getElementById('checkMinusvalido').value = '<%=contratoModif != null && contratoModif.getMin() != null ? contratoModif.getMin() : "N"%>';
                document.getElementById('checkMinusvalido').checked = '<%=contratoModif != null && contratoModif.getMin() != null && contratoModif.getMin().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "true" : ""%>';
                document.getElementById('checkInmig').value = '<%=contratoModif != null && contratoModif.getInm() != null ? contratoModif.getInm() : "N"%>';
                document.getElementById('checkInmig').checked = '<%=contratoModif != null && contratoModif.getInm() != null && contratoModif.getInm().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "true" : ""%>';
                document.getElementById('checkPLD').value = '<%=contratoModif != null && contratoModif.getPld() != null ? contratoModif.getPld() : "N"%>';
                document.getElementById('checkPLD').checked = '<%=contratoModif != null && contratoModif.getPld() != null && contratoModif.getPld().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "true" : ""%>';
                document.getElementById('checkRML').value = '<%=contratoModif != null && contratoModif.getRml() != null ? contratoModif.getRml() : "N"%>';
                document.getElementById('checkRML').checked = '<%=contratoModif != null && contratoModif.getRml() != null && contratoModif.getRml().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "true" : ""%>';
                document.getElementById('checkOtro').value = '<%=contratoModif != null && contratoModif.getOtr() != null ? contratoModif.getOtr() : "N"%>';
                document.getElementById('checkOtro').checked = '<%=contratoModif != null && contratoModif.getOtr() != null && contratoModif.getOtr().equalsIgnoreCase(ConstantesMeLanbide38.SI) ? "true" : ""%>';
                
                //Datos del Contrato
                document.getElementById('fechaAlta').value = '<%=contratoModif != null && contratoModif.getFac() != null ? formatter.format(contratoModif.getFac()) : ""%>';
                document.getElementById('txtDuracionContrato').value = '<%=contratoModif != null && contratoModif.getDco() != null ? contratoModif.getDco().toString() : ""%>';
                document.getElementById('codTipoContrato').value = '<%=contratoModif != null && contratoModif.getTic() != null ? contratoModif.getTic().toString() : ""%>';
                document.getElementById('codTipoJornada').value = '<%=contratoModif != null && contratoModif.getTjo() != null ? contratoModif.getTjo() : ""%>';
                document.getElementById('txtPorJornada').value = '<%=contratoModif != null && contratoModif.getPjt() != null ? contratoModif.getPjt().toPlainString().replaceAll("\\.", ",") : ""%>';
                document.getElementById('codTipConDur').value = '<%=contratoModif != null && contratoModif.getTco() != null ? contratoModif.getTco().toString() : ""%>';
                document.getElementById('salarioBruto').value = '<%=contratoModif != null && contratoModif.getSal() != null ? contratoModif.getSal().toPlainString().replaceAll("\\.", ",") : ""%>';
                document.getElementById('codCNOE').value = '<%=contratoModif != null && contratoModif.getCnoe() != null ? contratoModif.getCnoe() : ""%>';
                document.getElementById('segSocial').value = '<%=contratoModif != null && contratoModif.getCss() != null ? contratoModif.getCss().toPlainString().replaceAll("\\.", ",") : ""%>';
                document.getElementById('codColectivo').value = '<%=contratoModif != null && contratoModif.getCol() != null ? contratoModif.getCol().toString() : ""%>';
                document.getElementById('codCProf').value = '<%=contratoModif != null && contratoModif.getcPro() != null ? contratoModif.getcPro().toString() : ""%>';
                
                //Importe Subvenci�n
                document.getElementById('concedido').value = '<%=contratoModif != null && contratoModif.getImp() != null ? contratoModif.getImp().toPlainString().replaceAll("\\.", ",") : ""%>';
                document.getElementById('modifRes').value = '<%=contratoModif != null && contratoModif.getImr() != null ? contratoModif.getImr().toPlainString().replaceAll("\\.", ",") : ""%>';
                document.getElementById('recurso').value = '<%=contratoModif != null && contratoModif.getIre() != null ? contratoModif.getIre().toPlainString().replaceAll("\\.", ",") : ""%>';
                
                //ID del tercero
                document.getElementById('idTer').value = '<%=contratoModif != null && contratoModif.getTerCod() != null ? contratoModif.getTerCod() : ""%>';
            }

            function cargarCombos() {
                comboEstudios.addItems(codEstudios, descEstudios);
                comboColectivo.addItems(codColectivos, descColectivos);
                comboTipConDur.addItems(codTipConDur, descTipConDur);
                comboTipoJornada.addItems(codTipoJornada, descTipoJornada);
                comboCNOE.addItems(codCNOE, descCNOE);
                comboTipoContrato.addItems(codTipoContrato, descTipoContrato);
                comboCProf.addItems(codCProf, descCProf);
            }
            
            function cargarDescripcionesCombos(){
            
                var desc = '';
                var codAct = '';
                var codigo = '';

                //Estudios
                codigo = '<%=contratoModif != null && contratoModif.getNes() != null ? String.valueOf(contratoModif.getNes()) : ""%>';
                if(codigo != null && codigo != '')
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
                document.getElementById('descEstudios').value = desc;

                //Tipo Contrato
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getTic() != null ? String.valueOf(contratoModif.getTic()) : ""%>';
                if(codigo != null && codigo != '')
                {
                    for(var i=0; i<codTipoContrato.length; i++)
                    {
                        codAct = codTipoContrato[i];
                        if(codAct == codigo)
                        {
                            desc = descTipoContrato[i];
                        }
                    }
                }
                document.getElementById('descTipoContrato').value = desc;
                
                //Tipo Jornada
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getTjo() != null ? String.valueOf(contratoModif.getTjo()) : ""%>';
                if(codigo != null && codigo != '')
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
                document.getElementById('descTipoJornada').value = desc;
            
                //Tip. Con. Dur.
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getTco() != null ? String.valueOf(contratoModif.getTco()) : ""%>';
                if(codigo != null && codigo != '')
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
                document.getElementById('descTipConDur').value = desc;
            
                //CNOE
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getCnoe() != null ? String.valueOf(contratoModif.getCnoe()) : ""%>';
                if(codigo != null && codigo != '')
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
                document.getElementById('descCNOE').value = desc;
            
                //Colectivo
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getCol() != null ? String.valueOf(contratoModif.getCol()) : ""%>';
                if(codigo != null && codigo != '')
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
            
                //Categoria profesional
                desc = '';
                codAct = '';
                codigo = '';
                codigo = '<%=contratoModif != null && contratoModif.getcPro() != null ? String.valueOf(contratoModif.getcPro()) : ""%>';
                if(codigo != null && codigo != '')
                {
                    for(var i=0; i<codCProf.length; i++)
                    {
                        codAct = codCProf[i];
                        if(codAct == codigo)
                        {
                            desc = descCProf[i];
                        }
                    }
                }
                document.getElementById('descCProf').value = desc;
            }
        
            function mostrarBusqueda(){
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf('Internet Explorer')!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide38/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),500,600,'no','no',function(result){
						if (result != undefined){
							document.getElementById('idTer').value = result[0];
							document.getElementById('txtDni').value = result[1] != undefined && result[1] != '' ? result[1] : '';
							document.getElementById('txtNom').value = result[2] != undefined && result[2] != '' ? result[2] : '';
							document.getElementById('txtApe1').value = result[3] != undefined && result[3] != '' ? result[3] : '';
							document.getElementById('txtApe2').value = result[4] != undefined && result[4] != '' ? result[4] : '';
						}
					});
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide38/buscadorTerceros.jsp?tipo=<%=codOrganizacion%>&control='+control.getTime(),550,600,'no','no',function(result){
                if (result != undefined){
                    document.getElementById('idTer').value = result[0];
                    document.getElementById('txtDni').value = result[1] != undefined && result[1] != '' ? result[1] : '';
                    document.getElementById('txtNom').value = result[2] != undefined && result[2] != '' ? result[2] : '';
                    document.getElementById('txtApe1').value = result[3] != undefined && result[3] != '' ? result[3] : '';
                    document.getElementById('txtApe2').value = result[4] != undefined && result[4] != '' ? result[4] : '';
                }
					});
                }
                
            }
            
            function calcularEdadTrabajador(){
                if(comprobarFechaNac(document.getElementById('fechaNacimiento'))){
                    var txtOutput = 'txtEdad';
                    var feNac = document.getElementById('fechaNacimiento').value;
                    if(feNac != ''){
                        var array_fecha = feNac.split('/');
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
                }
                else{
                    document.getElementById('txtEdad').value = '';
                }
            }
            
            
            
            function comprobarFechaNac(inputFecha) {
                if (Trim(inputFecha.value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                        jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');
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
                                jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNacPosterior")%>");
                                return false;
                            }*/
                            if(result < 0){
                                jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNacPosterior")%>');
                                return false;
                            }
                        }
                    }
                }
                return true;
            }


            function mostrarCalFechaNacimiento(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calFechaNac').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaNacimiento',null,null,null,'','calFechaNac','',null,null,null,null,null,null,null,'calcularEdadTrabajador()',evento);
            }
            
            function mostrarCalFechaAlta(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calFechaAlta').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaAlta',null,null,null,'','calFechaAlta','',null,null,null,null,null,null,null,null,evento);
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
        
            function cambioValorCheck(check){
                if(check.checked){
                    check.value='S';
                }
                else{
                    check.value='N';
                }
            }
            
            function crearContrato(){
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                    var parametros = '';
                    var nuevo = '<%=nuevo%>';
                    if(nuevo != null && nuevo == '1'){
                        parametros = 'tarea=preparar&modulo=MELANBIDE38&operacion=crearContrato&tipo=0&numero=<%=numExpediente%>'
                                     +'&idTer='+document.getElementById('idTer').value
                                     +'&dni='+document.getElementById('txtDni').value
                                     +'&nom='+escape(document.getElementById('txtNom').value)
                                     +'&ap1='+escape(document.getElementById('txtApe1').value)
                                     +'&ap2='+escape(document.getElementById('txtApe2').value)
                                     +'&fNac='+document.getElementById('fechaNacimiento').value
                                     +'&sexo='+document.getElementById('txtSexo').value
                                     +'&mesesDesempleo='+document.getElementById('txtMesesDesempleo').value
                                     +'&nEstudios='+document.getElementById('codEstudios').value
                                     +'&minus='+document.getElementById('checkMinusvalido').value
                                     +'&inmig='+document.getElementById('checkInmig').value
                                     +'&pld='+document.getElementById('checkPLD').value
                                     +'&rml='+document.getElementById('checkRML').value
                                     +'&otr='+document.getElementById('checkOtro').value
                                     +'&fAlta='+document.getElementById('fechaAlta').value
                                     +'&duracionContrato='+document.getElementById('txtDuracionContrato').value
                                     +'&tipoContrato='+document.getElementById('codTipoContrato').value
                                     +'&tipoJornada='+document.getElementById('codTipoJornada').value
                                     +'&porJornada='+document.getElementById('txtPorJornada').value
                                     +'&tipConDur='+document.getElementById('codTipConDur').value
                                     +'&salario='+document.getElementById('salarioBruto').value
                                     +'&cnoe='+document.getElementById('codCNOE').value
                                     +'&segSocial='+document.getElementById('segSocial').value
                                     +'&colectivo='+document.getElementById('codColectivo').value
                                     +'&concedido='+document.getElementById('concedido').value
                                     +'&modifRes='+document.getElementById('modifRes').value
                                     +'&recurso='+document.getElementById('recurso').value
                                     +'&cPro='+document.getElementById('codCProf').value;
                    }
                    else{
                        parametros = 'tarea=preparar&modulo=MELANBIDE38&operacion=modificarContrato&tipo=0&numero=<%=numExpediente%>'
                                     +'&idCon=<%=contratoModif != null && contratoModif.getnCon() != null ? contratoModif.getnCon() : ""%>'
                                     +'&idTer='+document.getElementById('idTer').value
                                     +'&dni='+document.getElementById('txtDni').value
                                     +'&nom='+escape(document.getElementById('txtNom').value)
                                     +'&ap1='+escape(document.getElementById('txtApe1').value)
                                     +'&ap2='+escape(document.getElementById('txtApe2').value)
                                     +'&fNac='+document.getElementById('fechaNacimiento').value
                                     +'&sexo='+document.getElementById('txtSexo').value
                                     +'&mesesDesempleo='+document.getElementById('txtMesesDesempleo').value
                                     +'&nEstudios='+document.getElementById('codEstudios').value
                                     +'&minus='+document.getElementById('checkMinusvalido').value
                                     +'&inmig='+document.getElementById('checkInmig').value
                                     +'&pld='+document.getElementById('checkPLD').value
                                     +'&rml='+document.getElementById('checkRML').value
                                     +'&otr='+document.getElementById('checkOtro').value
                                     +'&fAlta='+document.getElementById('fechaAlta').value
                                     +'&duracionContrato='+document.getElementById('txtDuracionContrato').value
                                     +'&tipoContrato='+document.getElementById('codTipoContrato').value
                                     +'&tipoJornada='+document.getElementById('codTipoJornada').value
                                     +'&porJornada='+document.getElementById('txtPorJornada').value
                                     +'&tipConDur='+document.getElementById('codTipConDur').value
                                     +'&salario='+document.getElementById('salarioBruto').value
                                     +'&cnoe='+document.getElementById('codCNOE').value
                                     +'&segSocial='+document.getElementById('segSocial').value
                                     +'&colectivo='+document.getElementById('codColectivo').value
                                     +'&concedido='+document.getElementById('concedido').value
                                     +'&modifRes='+document.getElementById('modifRes').value
                                     +'&recurso='+document.getElementById('recurso').value
                                     +'&cPro='+document.getElementById('codCProf').value;
                    }
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=ISO-8859-1');
                        ajax.setRequestHeader('Accept', 'text/xml, application/xml, text/plain');     
                        //var formData = new FormData(document.getElementById('formContrato'));
                        ajax.send(parametros);
                        if (ajax.readyState==4 && ajax.status==200){
                            var xmlDoc = null;
                            if(navigator.appName.indexOf('Internet Explorer')!=-1){
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc=new ActiveXObject('Microsoft.XMLDOM');
                                xmlDoc.async='false';
                                xmlDoc.loadXML(text);
                            }else{
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName('RESPUESTA');
                        var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var listaContratos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=='CODIGO_OPERACION'){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaContratos[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=='FILA'){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=='ID'){
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=='DNI'){
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else if(hijosFila[cont].nodeName=='NOMBREAPELLIDOS'){
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                }
                                listaContratos[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=='0'){
                            jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            self.parent.opener.retornoXanelaAuxiliar(  listaContratos);
                            cerrarVentana();
                        }else if(codigoOperacion=='1'){
                            jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=='2'){
                            jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=='3'){
                            jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                                jsp_alerta('A','<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }
                else{
                    jsp_alerta('A', msgValidacion);
                }
            }
            
            function validarDatos(){
                msgValidacion = "";
                var correcto = true;
                if(!validarDatosTrabajador())
                    correcto = false;
                if(!validarDatosContrato())
                    correcto = false;
                if(!validarImporteSubvencion())
                    correcto = false;
                return correcto;
            }
            
            function validarDatosTrabajador(){
                var correcto = true;
                //Datos obligatorios
                var dni = document.getElementById('txtDni').value;
                if(dni == null || dni == ''){
                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.camposRojosOblig")%>';
                    return false;
                }
                var nombre = document.getElementById('txtNom').value;
                if(nombre == null || nombre == ''){
                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.camposRojosOblig")%>';
                    return false;
                }
                var ape1 = document.getElementById('txtApe1').value;
                if(ape1 == null || ape1 == ''){
                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.camposRojosOblig")%>';
                    return false;
                }
                var feNac = document.getElementById('fechaNacimiento').value;
                if(feNac == null || feNac == ''){
                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.camposRojosOblig")%>';
                    return false;
                }
                var sexo = document.getElementById('txtSexo').value;
                if(sexo == null || sexo == ''){
                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.camposRojosOblig")%>';
                    return false;
                }
                //DNI
                document.getElementById('txtDni').removeAttribute('style');
                if(!validarNIF(document.getElementById('txtDni'))){
                    document.getElementById('txtDni').style.border = '1px solid red';
                    if(msgValidacion == '')
                        msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.dniNoVal")%>';
                    correcto = false;
                }else{
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtDni').value)){
                        document.getElementById('txtDni').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "dniCaracteresNoPermitidos")%>';
                        correcto = false;
                    }else{
                        document.getElementById('txtDni').removeAttribute('style');
                    }
                }
                //Nombre
                if(!comprobarCaracteresEspeciales(document.getElementById('txtNom').value)){
                    document.getElementById('txtNom').style.border = '1px solid red';
                    if(msgValidacion == '')
                        msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>';
                    correcto = false;
                }else{
                    document.getElementById('txtNom').removeAttribute('style');
                }
                //Apellido 1
                if(!comprobarCaracteresEspeciales(document.getElementById('txtApe1').value)){
                    document.getElementById('txtApe1').style.border = '1px solid red';
                    if(msgValidacion == '')
                        msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>';
                    correcto = false;
                }else{
                    document.getElementById('txtApe1').removeAttribute('style');
                }
                //Apellido 2
                if(document.getElementById('txtApe2').value != null && document.getElementById('txtApe2').value != ''){
                    if(!comprobarCaracteresEspeciales(document.getElementById('txtApe2').value)){
                        document.getElementById('txtApe2').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "nomApelCaracteresNoPermitidos")%>';
                        correcto = false;
                    }else{
                        document.getElementById('txtApe2').removeAttribute('style');
                    }
                }
                //Fecha nacimiento
                document.getElementById('fechaNacimiento').removeAttribute('style');
                if(!validarFecha(document.getElementById('fechaNacimiento'))){
                    document.getElementById('fechaNacimiento').style.border = '1px solid red';
                    if(msgValidacion == '')
                        msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNacNoVal")%>';
                    correcto = false;
                }
                else{
                    var feNac = document.getElementById('fechaNacimiento').value;
                    var array_fecha = feNac.split('/');
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
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNacNoVal")%>";
                        correcto = false;
                    }*/
                    if(result < 0){
                        document.getElementById('fechaNacimiento').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaNacNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('fechaNacimiento').removeAttribute('style');
                    }
                }
                //Meses desempleo
                document.getElementById('txtMesesDesempleo').removeAttribute('style');
                if(document.getElementById('txtMesesDesempleo').value != null && document.getElementById('txtMesesDesempleo').value != ''){
                    if(!validarNumerico(document.getElementById('txtMesesDesempleo'))){
                        document.getElementById('txtMesesDesempleo').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.mesesDesempleoNoVal")%>';
                        correcto = false;
                    }
                    else{
                        try{
                            var meses = parseInt(document.getElementById('txtMesesDesempleo').value);
                            if(meses < 0 || meses > 999){
                                document.getElementById('txtMesesDesempleo').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.mesesDesempleoFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('txtMesesDesempleo').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('txtMesesDesempleo').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.mesesDesempleoNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                //Nivel estudios
                document.getElementById('codEstudios').removeAttribute('style');
                document.getElementById('descEstudios').removeAttribute('style');
                var nivel = document.getElementById('codEstudios').value;
                if(nivel != null && nivel != ''){
                    var encontrado = false;
                    var i = 0;
                    while(i < codEstudios.length && !encontrado){
                        if(codEstudios[i] == nivel){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(!encontrado){
                        document.getElementById('codEstudios').style.border = '1px solid red';
                        document.getElementById('descEstudios').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.nivelEstudiosNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codEstudios').removeAttribute('style');
                        document.getElementById('descEstudios').removeAttribute('style');
                    }
                }
                return correcto;
            }
            
            function validarDatosContrato(){
            var correcto = true;
                //Fecha alta
                document.getElementById('fechaAlta').removeAttribute('style');
                if(!validarFecha(document.getElementById('fechaAlta'))){
                    document.getElementById('fechaAlta').style.border = '1px solid red';
                    if(msgValidacion == '')
                        msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.fechaAltaNoVal")%>';
                    correcto = false;
                }
                else{
                    document.getElementById('fechaAlta').removeAttribute('style');
                }
                
                //Duracion contrato
                document.getElementById('txtDuracionContrato').removeAttribute('style');
                if(document.getElementById('txtDuracionContrato').value != null && document.getElementById('txtDuracionContrato').value != ''){
                    if(!validarNumerico(document.getElementById('txtDuracionContrato'))){
                        document.getElementById('txtDuracionContrato').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.durContratoNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var meses = parseInt(document.getElementById('txtDuracionContrato').value);
                            if(meses > 99999 || meses < 0){
                                document.getElementById('txtDuracionContrato').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.durContratoFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('txtDuracionContrato').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('txtDuracionContrato').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.durContratoNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //Tipo Contrato
                document.getElementById('codTipoContrato').removeAttribute('style');
                document.getElementById('descTipoContrato').removeAttribute('style');
                var tipoContrato = document.getElementById('codTipoContrato').value;
                if(tipoContrato != null && tipoContrato != ''){
                    var encontrado = false;
                    var i = 0;
                    while(i < codTipoContrato.length && !encontrado){
                        if(codTipoContrato[i] == tipoContrato){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(!encontrado){
                        document.getElementById('codTipoContrato').style.border = '1px solid red';
                        document.getElementById('descTipoContrato').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.tipoContratoNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codTipoContrato').removeAttribute('style');
                        document.getElementById('descTipoContrato').removeAttribute('style');
                    }
                }
                
                //Tipo Jornada
                document.getElementById('codTipoJornada').removeAttribute('style');
                document.getElementById('descTipoJornada').removeAttribute('style');
                var tipoJor = document.getElementById('codTipoJornada').value;
                if(tipoJor != null && tipoJor != ''){
                    var encontrado = false;
                    var i = 0;
                    while(i < codTipoJornada.length && !encontrado){
                        if(codTipoJornada[i] == tipoJor){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(!encontrado){
                        document.getElementById('codTipoJornada').style.border = '1px solid red';
                        document.getElementById('descTipoJornada').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.tipoJorNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codTipoJornada').removeAttribute('style');
                        document.getElementById('descTipoJornada').removeAttribute('style');
                    }
                }
                
                //Porcentaje jornada
                document.getElementById('txtPorJornada').removeAttribute('style');
                if(document.getElementById('txtPorJornada').value != null && document.getElementById('txtPorJornada').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('txtPorJornada'), 5, 2)){
                        document.getElementById('txtPorJornada').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.porJornadaNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var porcentaje = parseFloat(document.getElementById('txtPorJornada').value);
                            if(porcentaje > 100 || porcentaje < 0){
                                document.getElementById('txtPorJornada').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.porJornadaFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('txtPorJornada').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('txtPorJornada').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.porJornadaNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //Tipo Contrato por duraci�n
                document.getElementById('codTipConDur').removeAttribute('style');
                document.getElementById('descTipConDur').removeAttribute('style');
                var tipo = document.getElementById('codTipConDur').value;
                if(tipo != null && tipo != ''){
                    var encontrado = false;
                    var i = 0;
                    while(i < codTipConDur.length && !encontrado){
                        if(codTipConDur[i] == tipo){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(!encontrado){
                        document.getElementById('codTipConDur').style.border = '1px solid red';
                        document.getElementById('descTipConDur').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.tipConDurNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codTipConDur').removeAttribute('style');
                        document.getElementById('descTipConDur').removeAttribute('style');
                    }
                }
                
                //Salario Bruto
                document.getElementById('salarioBruto').removeAttribute('style');
                if(document.getElementById('salarioBruto').value != null && document.getElementById('salarioBruto').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('salarioBruto'), 12, 2)){
                        document.getElementById('salarioBruto').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.salarioNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var retrib = parseFloat(document.getElementById('salarioBruto').value);
                            if(retrib >= 10000000000 || retrib < 0){
                                document.getElementById('salarioBruto').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.salarioFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('salarioBruto').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('salarioBruto').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.salarioNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //CNOE
                document.getElementById('codCNOE').removeAttribute('style');
                document.getElementById('descCNOE').removeAttribute('style');
                var cnoe = document.getElementById('codCNOE').value;
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
                        document.getElementById('codCNOE').style.border = '1px solid red';
                        document.getElementById('descCNOE').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.cnoeNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codCNOE').removeAttribute('style');
                        document.getElementById('descCNOE').removeAttribute('style');
                    }
                }
                
                //Seguridad Social
                document.getElementById('segSocial').removeAttribute('style');
                if(document.getElementById('segSocial').value != null && document.getElementById('segSocial').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('segSocial'), 12, 2)){
                        document.getElementById('segSocial').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.segSocialNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var retrib = parseFloat(document.getElementById('segSocial').value);
                            if(retrib >= 10000000000 || retrib < 0){
                                document.getElementById('segSocial').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.segSocialFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('segSocial').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('segSocial').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.segSocialNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //Colectivo
                document.getElementById('codColectivo').removeAttribute('style');
                document.getElementById('descColectivo').removeAttribute('style');
                var col = document.getElementById('codColectivo').value;
                if(col != null && col != ''){
                    var encontrado = false;
                    var i = 0;
                    while(i < codColectivos.length && !encontrado){
                        if(codColectivos[i] == col){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(!encontrado){
                        document.getElementById('codColectivo').style.border = '1px solid red';
                        document.getElementById('descColectivo').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.colectivoNoVal")%>';
                        correcto = false;
                    }
                    else{
                        document.getElementById('codColectivo').removeAttribute('style');
                        document.getElementById('descColectivo').removeAttribute('style');
                    }
                }
                return correcto;
            }
            
            function validarImporteSubvencion(){
                var correcto = true;
                //Concedido
                document.getElementById('concedido').removeAttribute('style');
                if(document.getElementById('concedido').value != null && document.getElementById('concedido').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('concedido'), 12, 2)){
                        document.getElementById('concedido').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.concedidoNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var retrib = parseFloat(document.getElementById('concedido').value);
                            if(retrib >= 10000000000 || retrib < 0){
                                document.getElementById('concedido').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.concedidoFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('concedido').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('concedido').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.concedidoNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //Modificacion Resolucion
                document.getElementById('modifRes').removeAttribute('style');
                if(document.getElementById('modifRes').value != null && document.getElementById('modifRes').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('modifRes'), 12, 2)){
                        document.getElementById('modifRes').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.modifResNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var retrib = parseFloat(document.getElementById('modifRes').value);
                            if(retrib >= 10000000000 || retrib < 0){
                                document.getElementById('modifRes').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.modifResFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('modifRes').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('modifRes').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.modifResNoVal")%>';
                            correcto = false;
                        }
                    }
                }
                
                //Recurso
                document.getElementById('recurso').removeAttribute('style');
                if(document.getElementById('recurso').value != null && document.getElementById('recurso').value != ''){
                    if(!validarNumericoDecimal(document.getElementById('recurso'), 12, 2)){
                        document.getElementById('recurso').style.border = '1px solid red';
                        if(msgValidacion == '')
                            msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.recursoResNoVal")%>';
                        correcto = false;
                    }else{
                        try
                        {
                            var retrib = parseFloat(document.getElementById('recurso').value);
                            if(retrib >= 10000000000 || retrib < 0){
                                document.getElementById('recurso').style.border = '1px solid red';
                                if(msgValidacion == '')
                                    msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.recursoResFueraRango")%>';
                                correcto = false;
                            }else{
                                document.getElementById('recurso').removeAttribute('style');
                            }
                        }
                        catch(err){
                            document.getElementById('recurso').style.border = '1px solid red';
                            if(msgValidacion == '')
                                msgValidacion = '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.recursoResNoVal")%>';
                            correcto = false;
                        }
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

            function validarNIF (campo) {
                if (!calcularLetraNIF(campo)) {
                    return false;
                }
                return true;

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
            
            function validarNumerico(numero){
                try{
                    if (Trim(numero.value)!='') {
                        return !isNaN(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
            }
            
            function validarNumericoDecimal(numero, longTotal, longParteDecimal){
                try{
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if(Trim(numero.value) != ''){
                        var valor = numero.value;
                        var pattern = '^[0-9]{1,'+longParteEntera+'}(,[0-9]{1,'+longParteDecimal+'})?$';
                        var regex = new RegExp(pattern);
                        //var result = valor.match(regex);
                        var result = regex.test(valor);
                        return result;
                        //return /^[0-]{1,}(,[0-9]{1,longParteDecimal})?$/.test(numero.value);
                    }else{
                        return true;
                    }
                }
                catch(err){
                    alert(err);
                    return false;
                }
            }
            
            function calcularCosteSalarialTotal(){
                var salarioBruto = 0.0;
                var segSocial = 0.0;
                var valor1 = document.getElementById('salarioBruto').value;
                var valor2 = document.getElementById('segSocial').value;
                try{
                    valor1 = valor1.replace(',', '.');
                    salarioBruto = parseFloat(valor1);
                }catch(err){
                    
                }
                
                try{
                    valor2 = valor2.replace(',', '.');
                    segSocial = parseFloat(valor2);
                }catch(err){
                    
                }
                if(isNaN(salarioBruto)){
                    salarioBruto = 0.0;
                }
                if(isNaN(segSocial)){
                    segSocial = 0.0;
                }
                
                var result = salarioBruto + segSocial;
                result = result * 100;
                result = Math.round(result);
                result = result / 100;
                document.getElementById('costeSalarial').value = result;
                reemplazarPuntos(document.getElementById('costeSalarial'));
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
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
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

            function getXMLHttpRequest(){
                var aVersions = [ 'MSXML2.XMLHttp.5.0',
                    'MSXML2.XMLHttp.4.0','MSXML2.XMLHttp.3.0',
                    'MSXML2.XMLHttp','Microsoft.XMLHttp'
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
            
        function comprobarCaracteresEspeciales(texto){
            var iChars = '&\'<>|^/\\%';
            for (var i = 0; i < texto.length; i++) {
                if (iChars.indexOf(texto.charAt(i)) != -1) {
                    return false;
                }
            }
            return true;
        }
        </script>
    </head>
    <body id="cuerpoNuevoContrato" style="margin: 10px; text-align: left;padding: 0 !important;" class="contenidoPantalla">
        <form  id="formNuevoContrato">
            <fieldset  style="width: 96.5%;">
                <legend><%=meLanbide38I18n.getMensaje(idiomaUsuario,"legend.trabajador")%></legend>
                <div style="width: 100%; clear: both;">
                    <div style="float: left; width: 170px;">
                        <font color="red"><%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.dni")%></font>
                        <input type="text" size="16" maxlength="25" id="txtDni" name="txtDni" disabled="disabled" value=""/>
                    </div>
                    <div style="float: left; width: 740px;">
                        <font color="red"><%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.nombreApel")%></font>
                        <input type="text" size="29" maxlength="80" id="txtNom" name="txtNom" value=""/>
                        <input type="text" size="29" maxlength="100" id="txtApe1" name="txtApe1" value=""/>
                        <input type="text" size="29" maxlength="100" id="txtApe2" name="txtApe2" value=""/>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;" id="divBtnBuscar">
                    <input type="button" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="mostrarBusqueda();"/>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 170px;">
                        <font color="red"><%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.fNac")%></font>
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacimiento" name="fechaNacimiento" onkeyup="return SoloCaracteresFecha(this);" onblur="calcularEdadTrabajador();" onfocus="this.select();" value=""/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacimiento(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaNac" name="calFechaNac" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                        </A>
                    </div>
                    <div style="float: left; width: 170px;">
                       <font color="red"><%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.sexo")%></font>
                        <input type="text" maxlength="1" size="2" id="txtSexo" name="txtSexo" value="" onkeyup="comprobarSexo(this);" style="margin-left: 27px;"/>
                    </div>
                    <div style="float: left; width: 245px;">
                       <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.mesesDesempleo")%>
                        <input type="text" maxlength="3" size="2" id="txtMesesDesempleo" name="txtMesesDesempleo" onchange="reemplazarPuntos(this);" value="" style="margin-left: 10px;"/>
                    </div>
                    <div style="float: left; width: 322px;">
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.nEstudios")%>
                        <input id="codEstudios" name="codEstudios" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descEstudios"	name="descEstudios" type="text" class="inputTexto" size="28" readonly >
                         <a id="anchorEstudios" name="anchorEstudios" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstudios" name="botonEstudios" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px; text-align: center;">
                    <div style="float: left; width: 20%; text-align: center;">
                        <input style="margin-right: 2px" type="checkBox" id="checkMinusvalido" name="checkMinusvalido" value="" onchange="cambioValorCheck(this);"></input>
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.minusvalido")%>
                    </div>
                    <div style="float: left; width: 20%; text-align: center;">
                        <input style="margin-right: 2px; margin-left: 30px;" type="checkBox" id="checkInmig" name="checkInmig" value="" onchange="cambioValorCheck(this);"></input>
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.inmig")%>
                    </div>
                    <div style="float: left; width: 20%; text-align: center;">
                        <input style="margin-right: 2px;margin-left: 30px;" type="checkBox" id="checkPLD" name="checkPLD" value="" onchange="cambioValorCheck(this);"></input>
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.PLD")%>
                    </div>
                    <div style="float: left; width: 20%; text-align: center;">
                        <input style="margin-right: 2px;margin-left: 30px;" type="checkBox" id="checkRML" name="checkRML" value="" onchange="cambioValorCheck(this);"></input>
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.RML")%>
                    </div>
                    <div style="float: left; width: 20%; text-align: center;">
                        <input style="margin-right: 2px;margin-left: 30px;" type="checkBox" id="checkOtro" name="checkOtro" value="" onchange="cambioValorCheck(this);"></input>
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.otro")%>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 96.5%;">
                <legend><%=meLanbide38I18n.getMensaje(idiomaUsuario,"legend.datosContrato")%></legend>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 330px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.fAlta")%>
                        </div>
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaAlta" name="fechaAlta" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFecha(this);" onfocus="this.select();" value=""/>
                        <A href="javascript:calClick(event);" onclick="mostrarCalFechaAlta(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaAlta" name="calFechaAlta" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.fAlta")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                    <div style="float: left; width: 300px;">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.edad")%>
                        </div>
                        <input type="text" disabled size="3" id="txtEdad" name="txtEdad"/>
                    </div>
                    <div style="float: left; width: 280px;">
                        <div style="float: left; width: 185px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.duracionContrato")%>
                        </div>
                        <input type="text" maxlength="5" size="6" id="txtDuracionContrato" name="txtDuracionContrato" style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 330px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                        </div>
                        <input id="codTipoContrato" name="codTipoContrato" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descTipoContrato" name="descTipoContrato" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorTipoContrato" name="anchorTipoContrato" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoContrato" name="botonTipoContrato" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 300px;">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.tipoJornada")%>
                        </div>
                        <input id="codTipoJornada" name="codTipoJornada" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descTipoJornada" name="descTipoJornada" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorTipoJornada" name="anchorTipoJornada" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoJornada" name="botonTipoJornada" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 280px;">
                        <div style="float: left; width: 185px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.porJornada")%>
                        </div>
                        <input type="text" maxlength="5" size="6" id="txtPorJornada" name="txtPorJornada" style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 630px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.tipConDurAbrev")%>
                        </div>
                        <input id="codTipConDur" name="codTipConDur" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descTipConDur" name="descTipConDur" type="text" class="inputTexto" size="20" readonly >
                         <a id="anchorTipConDur" name="anchorTipConDur" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipConDur" name="botonTipConDur" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 630px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.cnoe")%>
                        </div>
                        <input id="codCNOE" name="codCNOE" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descCNOE" name="descCNOE" type="text" class="inputTexto" size="70" readonly >
                         <a id="anchorCNOE" name="anchorCNOE" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCNOE" name="botonCNOE" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 280px;">
                        <div style="float: left; width: 185px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.salarioBruto")%>
                        </div>
                        <input type="text" maxlength="13" size="6" id="salarioBruto" name="salarioBruto" style="padding-left: 1px;" onchange="calcularCosteSalarialTotal(this);reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 630px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.colectivo")%>
                        </div>
                        <input id="codColectivo" name="codColectivo" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descColectivo" name="descColectivo" type="text" class="inputTexto" size="70" readonly >
                         <a id="anchorColectivo" name="anchorColectivo" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonColectivo" name="botonColectivo" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 280px;">
                        <div style="float: left; width: 185px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.segSocial")%>
                        </div>
                        <input type="text" maxlength="13" size="6" id="segSocial" name="segSocial" style="padding-left: 1px;" onchange="calcularCosteSalarialTotal(this);reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 630px;">
                        <div style="float: left; width: 120px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.cProf")%>
                        </div>
                        <input id="codCProf" name="codCProf" type="text" class="inputTexto" size="2" maxlength="3" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                        <input id="descCProf" name="descCProf" type="text" class="inputTexto" size="70" readonly >
                         <a id="anchorCProf" name="anchorCProf" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCProf" name="botonCProf" style="cursor:hand;" alt="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                    </div>
                    <div style="float: left; width: 280px;">
                        <div style="float: left; width: 185px;">
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.costeSalarial")%>
                        </div>
                        <input type="text" maxlength="13" size="6" id="costeSalarial" name="costeSalarial" disabled style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
            </fieldset>
            <fieldset  style="width: 96.5%;">
                <legend><%=meLanbide38I18n.getMensaje(idiomaUsuario,"legend.importe")%></legend>
                <div style="width: 100%; clear: both; padding-top: 5px;">
                    <div style="float: left; width: 33%; text-align: center;">
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.concedido")%>
                        <input type="text" maxlength="13" size="13" id="concedido" name="concedido" style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                    <div style="float: left; width: 33%; text-align: center;">
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.modifRes")%>
                        <input type="text" maxlength="13" size="13" id="modifRes" name="modifRes" style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                    <div style="float: left; width: 33%; text-align: center;">
                        <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.recurso")%>
                        <input type="text" maxlength="13" size="13" id="recurso" name="recurso" style="padding-left: 1px;" onchange="reemplazarPuntos(this);" value=""/>
                    </div>
                </div>
            </fieldset>
            <div style="width: 100%; text-align: center; padding-top: 5px;">
                <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="crearContrato();">
                <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();">
            </div>
        </form>
        <div id="popupcalendar" class="text"></div>
        <input type="hidden" id="idTer"/>
        <script type="text/javascript">
            //Persona contratada
            var comboEstudios = new Combo('Estudios');
            var comboColectivo = new Combo('Colectivo');
            var comboTipConDur = new Combo('TipConDur');
            var comboTipoJornada = new Combo('TipoJornada');
            var comboCNOE = new Combo('CNOE');
            var comboTipoContrato = new Combo('TipoContrato');
            var comboCProf = new Combo('CProf');
            
            inicio();
        </script>
    </body>
</html>
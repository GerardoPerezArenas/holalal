<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaPuestoTrabajoVO" %>
<%@page import="es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.Date" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        int apl = 5; // Pendiente de mirar
        int codOrganizacion = 0;
        String css = "";
        UsuarioValueObject usuario = new UsuarioValueObject();
        try {
            if (session != null)
            {
                if (usuario != null)
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuario.getAppCod();
                    idiomaUsuario = usuario.getIdioma();
                    codOrganizacion  = usuario.getOrgCod();
                    css = usuario.getCss();
                }
            }
        }
        catch(Exception ex){}
        //Clase para internacionalizar los mensajes de la aplicacion.
        MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
    
        String nombreModulo     = request.getParameter("nombreModulo");
        String numExpediente    = request.getParameter("numero");
    
        PersonaContratadaPuestoTrabajoVO personaPuestoVO = (PersonaContratadaPuestoTrabajoVO)request.getAttribute("personaContratadaPuestoTrabajo");
        
        // Depende de los desplegables que haya //
        List<GeneralComboVO> despDesempleado = null;
        if (personaPuestoVO.getDesempleado() != null) {
            despDesempleado = personaPuestoVO.getDesempleado().getDesplegable();
        }
        
        List<GeneralComboVO> despDesempleadoAnterior = null;
        if (personaPuestoVO.getDesempleadoAnterior() != null) {
            despDesempleadoAnterior = personaPuestoVO.getDesempleadoAnterior().getDesplegable();
        }
        
        List<GeneralComboVO> despExperiencia6M = null;
        if (personaPuestoVO.getExperiencia6M() != null) {
            despExperiencia6M = personaPuestoVO.getExperiencia6M().getDesplegable();
        }
        
        List<GeneralComboVO> despMuniPerCon = null;
        if (personaPuestoVO.getMuniPerCon() != null) {
            despMuniPerCon = personaPuestoVO.getMuniPerCon().getDesplegable();
        }
        
        List<GeneralComboVO> despSexoPerCont = null;
        if (personaPuestoVO.getSexoPerCont() != null) {
            despSexoPerCont = personaPuestoVO.getSexoPerCont().getDesplegable();
        }
        
        List<GeneralComboVO> despSistGarantiaJuve = null;
        if (personaPuestoVO.getSistGarantiaJuve() != null) {
            despSistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve().getDesplegable();
        }
        
        List<GeneralComboVO> despDuraContra = null;
        if (personaPuestoVO.getDuraContra() != null) {
            despDuraContra = personaPuestoVO.getDuraContra().getDesplegable();
        }

        List<GeneralComboVO> despGrupCotSS = null;
        if (personaPuestoVO.getGrupCotSS() != null) {
            despGrupCotSS = personaPuestoVO.getGrupCotSS().getDesplegable();
        }
        
        List<GeneralComboVO> despJorPuestra = null;
        if (personaPuestoVO.getJorPuestra() != null) {
            despJorPuestra = personaPuestoVO.getJorPuestra().getDesplegable();
        }        
        
        List<GeneralComboVO> despModContra = null;
        if (personaPuestoVO.getModContra() != null) {
            despModContra = personaPuestoVO.getModContra().getDesplegable();
        }        
        
        List<GeneralComboVO> despMujSubRepre = null;
        if (personaPuestoVO.getMujSubRepre() != null) {
            despMujSubRepre = personaPuestoVO.getMujSubRepre().getDesplegable();
        }        
        
        List<GeneralComboVO> despNomCodOcu = null;
        if (personaPuestoVO.getNomCodOcu() != null) {
            despNomCodOcu = personaPuestoVO.getNomCodOcu().getDesplegable();
        }
        
        List<GeneralComboVO> despSalarioMin = null;
        if (personaPuestoVO.getSalarioMin() != null) {
            despSalarioMin = personaPuestoVO.getSalarioMin().getDesplegable();
        }        
        
        List<GeneralComboVO> despTitPuestra = null;
        if (personaPuestoVO.getTitPuestra() != null) {
            despTitPuestra = personaPuestoVO.getTitPuestra().getDesplegable();
        }   
        
        //Combos
        Gson gson = new Gson();
        GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonB.serializeNulls();

        gson = gsonB.create();

        final String despDesempleadoGA = gson.toJson(despDesempleado);
        final String despDesempleadoAnteriorGA = gson.toJson(despDesempleadoAnterior);
        final String despExperiencia6MGA = gson.toJson(despExperiencia6M);
        final String despMuniPerConGA = gson.toJson(despMuniPerCon);
        final String despSexoPerContGA = gson.toJson(despSexoPerCont);
        final String despSistGarantiaJuveGA = gson.toJson(despSistGarantiaJuve);
        final String despDuraContraGA = gson.toJson(despDuraContra);
        final String despGrupCotSSGA = gson.toJson(despGrupCotSS);
        final String despJorPuestraGA = gson.toJson(despJorPuestra);
        final String despModContraGA = gson.toJson(despModContra);
        final String despMujSubRepreGA = gson.toJson(despMujSubRepre);
        final String despNomCodOcuGA = gson.toJson(despNomCodOcu);
        final String despSalarioMinGA = gson.toJson(despSalarioMin);
        final String despTitPuestraGA = gson.toJson(despTitPuestra);
        // Fin desplegables
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide67/pestanaPersonaContPuestoTrabajo.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/leaukUtils.js"></script>    

    <script type="text/javascript">
        var idiomaUsuario = <%=idiomaUsuario%>;

        <% String dniPerCon = personaPuestoVO.getDniPerCon() != null ? personaPuestoVO.getDniPerCon().getValor() : ""; %>
        var dniPerCon = '<%=dniPerCon != null ? dniPerCon.replace("null", "") : "" %>';

        <% String tDaDniPerCon = personaPuestoVO.getDniPerCon() != null ? personaPuestoVO.getDniPerCon().getPcaTda() : ""; %>
        var tDaDniPerCon = '<%=tDaDniPerCon != null ? tDaDniPerCon.replace("null", "") : "" %>';
        <% String tamDniPerCon = personaPuestoVO.getDniPerCon() != null ? personaPuestoVO.getDniPerCon().getPcaTam() : ""; %>
        var tamDniPerCon = '<%=tamDniPerCon != null ? tamDniPerCon.replace("null", "") : "" %>';
        <% String bloqDniPerCon = personaPuestoVO.getDniPerCon() != null ? personaPuestoVO.getDniPerCon().getPcaBloq() : ""; %>
        var bloqDniPerCon = '<%=bloqDniPerCon != null ? bloqDniPerCon.replace("null", "") : "" %>';
        <% String ocultoDniPerCon = personaPuestoVO.getDniPerCon() != null ? personaPuestoVO.getDniPerCon().getPcaOculto() : ""; %>
        var ocultoDniPerCon = '<%=ocultoDniPerCon != null ? ocultoDniPerCon.replace("null", "") : "" %>';

        <% String nomPerCon = personaPuestoVO.getNomPerCon() != null ? personaPuestoVO.getNomPerCon().getValor() : ""; %>
        var nomPerCon = '<%=nomPerCon != null ? nomPerCon.replace("null", "") : "" %>';

        <% String tDanomPerCon = personaPuestoVO.getNomPerCon() != null ? personaPuestoVO.getNomPerCon().getPcaTda() : ""; %>
        var tDanomPerCon = '<%=tDaDniPerCon != null ? tDaDniPerCon.replace("null", "") : "" %>';
        <% String tamNomPerCon = personaPuestoVO.getNomPerCon() != null ? personaPuestoVO.getNomPerCon().getPcaTam() : ""; %>
        var tamNomPerCon = '<%=tamDniPerCon != null ? tamDniPerCon.replace("null", "") : "" %>';
        <% String bloqNomPerCon = personaPuestoVO.getNomPerCon() != null ? personaPuestoVO.getNomPerCon().getPcaBloq() : ""; %>
        var bloqNomPerCon = '<%=bloqDniPerCon != null ? bloqDniPerCon.replace("null", "") : "" %>';
        <% String ocultoNomPerCon = personaPuestoVO.getNomPerCon() != null ? personaPuestoVO.getNomPerCon().getPcaOculto() : ""; %>
        var ocultoNomPerCon = '<%=ocultoNomPerCon != null ? ocultoNomPerCon.replace("null", "") : "" %>';

        <% String apel1PerCon = personaPuestoVO.getApel1PerCon() != null ? personaPuestoVO.getApel1PerCon().getValor() : ""; %>
        var apel1PerCon = '<%=apel1PerCon != null ? apel1PerCon.replace("null", "") : ""%>';

        <% String tDaApel1PerCon = personaPuestoVO.getApel1PerCon() != null ? personaPuestoVO.getApel1PerCon().getPcaTda() : ""; %>
        var tDaApel1PerCon = '<%=tDaApel1PerCon != null ? tDaApel1PerCon.replace("null", "") : "" %>';
        <% String tamApel1PerCon = personaPuestoVO.getApel1PerCon() != null ? personaPuestoVO.getApel1PerCon().getPcaTam() : ""; %>
        var tamApel1PerCon = '<%=tamApel1PerCon != null ? tamApel1PerCon.replace("null", "") : "" %>';
        <% String bloqApel1PerCon = personaPuestoVO.getApel1PerCon() != null ? personaPuestoVO.getApel1PerCon().getPcaBloq() : ""; %>
        var bloqApel1PerCon = '<%=bloqApel1PerCon != null ? bloqApel1PerCon.replace("null", "") : "" %>';
        <% String ocultoApel1PerCon = personaPuestoVO.getApel1PerCon() != null ? personaPuestoVO.getApel1PerCon().getPcaOculto() : ""; %>
        var ocultoApel1PerCon = '<%=ocultoApel1PerCon != null ? ocultoApel1PerCon.replace("null", "") : "" %>';

        <% String apel2PerCon = personaPuestoVO.getApel2PerCon() != null ? personaPuestoVO.getApel2PerCon().getValor() : ""; %>
        var apel2PerCon = '<%=apel2PerCon != null ? apel2PerCon.replace("null", "") : "" %>';

        <% String tDaApel2PerCon = personaPuestoVO.getApel2PerCon() != null ? personaPuestoVO.getApel2PerCon().getPcaTda() : ""; %>
        var tDaApel2PerCon = '<%=tDaApel2PerCon != null ? tDaApel2PerCon.replace("null", "") : "" %>';
        <% String tamApel2PerCon = personaPuestoVO.getApel2PerCon() != null ? personaPuestoVO.getApel2PerCon().getPcaTam() : ""; %>
        var tamApel2PerCon = '<%=tamApel2PerCon != null ? tamApel2PerCon.replace("null", "") : "" %>';
        <% String bloqApel2PerCon = personaPuestoVO.getApel2PerCon() != null ? personaPuestoVO.getApel2PerCon().getPcaBloq() : ""; %>
        var bloqApel2PerCon = '<%=bloqApel2PerCon != null ? bloqApel2PerCon.replace("null", "") : "" %>';
        <% String ocultoApel2PerCon = personaPuestoVO.getApel2PerCon() != null ? personaPuestoVO.getApel2PerCon().getPcaOculto() : ""; %>
        var ocultoApel2PerCon = '<%=ocultoApel2PerCon != null ? ocultoApel2PerCon.replace("null", "") : "" %>';

        <% String fecNacPerCon = personaPuestoVO.getFecNacPerCon() != null ? personaPuestoVO.getFecNacPerCon().getValor() : ""; %>
        var fecNacPerCon = '<%=fecNacPerCon != null ? fecNacPerCon.replace("null", "") : ""%>';

        <% String tDaFecNacPerCon = personaPuestoVO.getFecNacPerCon() != null ? personaPuestoVO.getFecNacPerCon().getPcaTda() : ""; %>
        var tDaFecNacPerCon = '<%=tDaFecNacPerCon != null ? tDaFecNacPerCon.replace("null", "") : "" %>';
        <% String tamFecNacPerCon = personaPuestoVO.getFecNacPerCon() != null ? personaPuestoVO.getFecNacPerCon().getPcaTam() : ""; %>
        var tamFecNacPerCon = '<%=tamFecNacPerCon != null ? tamFecNacPerCon.replace("null", "") : "" %>';
        <% String bloqFecNacPerCon = personaPuestoVO.getFecNacPerCon() != null ? personaPuestoVO.getFecNacPerCon().getPcaBloq() : ""; %>
        var bloqFecNacPerCon = '<%=bloqFecNacPerCon != null ? bloqFecNacPerCon.replace("null", "") : "" %>';
        <% String ocultoFecNacPerCon = personaPuestoVO.getFecNacPerCon() != null ? personaPuestoVO.getFecNacPerCon().getPcaOculto() : ""; %>
        var ocultoFecNacPerCon = '<%=ocultoFecNacPerCon != null ? ocultoFecNacPerCon.replace("null", "") : "" %>';

        <% String edadPerCon = personaPuestoVO.getEdadPerCon() != null ? personaPuestoVO.getEdadPerCon().getValor() : ""; %>
        var edadPerCon = '<%=edadPerCon != null ? edadPerCon.replace("null", "") : ""%>';

        <% String tDaEdadPerCon = personaPuestoVO.getEdadPerCon() != null ? personaPuestoVO.getEdadPerCon().getPcaTda() : ""; %>
        var tDaEdadPerCon = '<%=tDaEdadPerCon != null ? tDaEdadPerCon.replace("null", "") : "" %>';
        <% String tamEdadPerCon = personaPuestoVO.getEdadPerCon() != null ? personaPuestoVO.getEdadPerCon().getPcaTam() : ""; %>
        var tamEdadPerCon = '<%=tamEdadPerCon != null ? tamEdadPerCon.replace("null", "") : "" %>';
        <% String bloqEdadPerCon = personaPuestoVO.getEdadPerCon() != null ? personaPuestoVO.getEdadPerCon().getPcaBloq() : ""; %>
        var bloqEdadPerCon = '<%=bloqEdadPerCon != null ? bloqEdadPerCon.replace("null", "") : "" %>';
        <% String ocultoEdadPerCon = personaPuestoVO.getEdadPerCon() != null ? personaPuestoVO.getEdadPerCon().getPcaOculto() : ""; %>
        var ocultoEdadPerCon = '<%=ocultoEdadPerCon != null ? ocultoEdadPerCon.replace("null", "") : "" %>';

        <% String sexoPerCont = personaPuestoVO.getSexoPerCont() != null ? personaPuestoVO.getSexoPerCont().getValor() : ""; %>
        var sexoPerCont = '<%=sexoPerCont != null ? sexoPerCont.replace("null", "") : ""%>';

        <% String tDaSexoPerCont = personaPuestoVO.getSexoPerCont() != null ? personaPuestoVO.getSexoPerCont().getPcaTda() : ""; %>
        var tDaSexoPerCont = '<%=tDaSexoPerCont != null ? tDaSexoPerCont.replace("null", "") : "" %>';
        <% String tamSexoPerCont = personaPuestoVO.getSexoPerCont() != null ? personaPuestoVO.getSexoPerCont().getPcaTam() : ""; %>
        var tamSexoPerCont = '<%=tamSexoPerCont != null ? tamSexoPerCont.replace("null", "") : "" %>';
        <% String bloqSexoPerCont = personaPuestoVO.getSexoPerCont() != null ? personaPuestoVO.getSexoPerCont().getPcaBloq() : ""; %>
        var bloqSexoPerCont = '<%=bloqSexoPerCont != null ? bloqSexoPerCont.replace("null", "") : "" %>';
        <% String ocultoSexoPerCont = personaPuestoVO.getSexoPerCont() != null ? personaPuestoVO.getSexoPerCont().getPcaOculto() : ""; %>
        var ocultoSexoPerCont = '<%=ocultoSexoPerCont != null ? ocultoSexoPerCont.replace("null", "") : "" %>';

        <% String muniPerCon = personaPuestoVO.getMuniPerCon() != null ? personaPuestoVO.getMuniPerCon().getValor() : ""; %>
        var muniPerCon = '<%=muniPerCon != null ? muniPerCon.replace("null", "") : ""%>';

        <% String tDaMuniPerCon = personaPuestoVO.getMuniPerCon() != null ? personaPuestoVO.getMuniPerCon().getPcaTda() : ""; %>
        var tDaMuniPerCon = '<%=tDaMuniPerCon != null ? tDaMuniPerCon.replace("null", "") : "" %>';
        <% String tamMuniPerCon = personaPuestoVO.getMuniPerCon() != null ? personaPuestoVO.getMuniPerCon().getPcaTam() : ""; %>
        var tamMuniPerCon = '<%=tamMuniPerCon != null ? tamMuniPerCon.replace("null", "") : "" %>';
        <% String bloqMuniPerCon = personaPuestoVO.getMuniPerCon() != null ? personaPuestoVO.getMuniPerCon().getPcaBloq() : ""; %>
        var bloqMuniPerCon = '<%=bloqMuniPerCon != null ? bloqMuniPerCon.replace("null", "") : "" %>';
        <% String ocultoMuniPerCon = personaPuestoVO.getMuniPerCon() != null ? personaPuestoVO.getMuniPerCon().getPcaOculto() : ""; %>
        var ocultoMuniPerCon = '<%=ocultoMuniPerCon != null ? ocultoMuniPerCon.replace("null", "") : "" %>';

        <% String experiencia6M = personaPuestoVO.getExperiencia6M() != null ? personaPuestoVO.getExperiencia6M().getValor() : ""; %>
        var experiencia6M = '<%=experiencia6M != null ? experiencia6M.replace("null", "") : ""%>';

        <% String tDaExperiencia6M = personaPuestoVO.getExperiencia6M() != null ? personaPuestoVO.getExperiencia6M().getPcaTda() : ""; %>
        var tDaExperiencia6M = '<%=tDaExperiencia6M != null ? tDaExperiencia6M.replace("null", "") : "" %>';
        <% String tamExperiencia6M = personaPuestoVO.getExperiencia6M() != null ? personaPuestoVO.getExperiencia6M().getPcaTam() : ""; %>
        var tamExperiencia6M = '<%=tamExperiencia6M != null ? tamExperiencia6M.replace("null", "") : "" %>';
        <% String bloqExperiencia6M = personaPuestoVO.getExperiencia6M() != null ? personaPuestoVO.getExperiencia6M().getPcaBloq() : ""; %>
        var bloqExperiencia6M = '<%=bloqExperiencia6M != null ? bloqExperiencia6M.replace("null", "") : "" %>';
        <% String ocultoExperiencia6M = personaPuestoVO.getExperiencia6M() != null ? personaPuestoVO.getExperiencia6M().getPcaOculto() : ""; %>
        var ocultoExperiencia6M = '<%=ocultoExperiencia6M != null ? ocultoExperiencia6M.replace("null", "") : "" %>';

        <% String desempleadoAnterior = personaPuestoVO.getDesempleadoAnterior() != null ? personaPuestoVO.getDesempleadoAnterior().getValor() : ""; %>
        var desempleadoAnterior = '<%=desempleadoAnterior != null ? desempleadoAnterior.replace("null", "") : ""%>';

        <% String tDaDesempleadoAnterior = personaPuestoVO.getDesempleadoAnterior() != null ? personaPuestoVO.getDesempleadoAnterior().getPcaTda() : ""; %>
        var tDaDesempleadoAnterior = '<%=tDaDesempleadoAnterior != null ? tDaDesempleadoAnterior.replace("null", "") : "" %>';
        <% String tamDesempleadoAnterior = personaPuestoVO.getDesempleadoAnterior() != null ? personaPuestoVO.getDesempleadoAnterior().getPcaTam() : ""; %>
        var tamDesempleadoAnterior = '<%=tamDesempleadoAnterior != null ? tamDesempleadoAnterior.replace("null", "") : "" %>';
        <% String bloqDesempleadoAnterior = personaPuestoVO.getDesempleadoAnterior() != null ? personaPuestoVO.getDesempleadoAnterior().getPcaBloq() : ""; %>
        var bloqDesempleadoAnterior = '<%=bloqDesempleadoAnterior != null ? bloqDesempleadoAnterior.replace("null", "") : "" %>';
        <% String ocultoDesempleadoAnterior = personaPuestoVO.getDesempleadoAnterior() != null ? personaPuestoVO.getDesempleadoAnterior().getPcaOculto() : ""; %>
        var ocultoDesempleadoAnterior = '<%=ocultoDesempleadoAnterior != null ? ocultoDesempleadoAnterior.replace("null", "") : "" %>';

        <% String sistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve() != null ? personaPuestoVO.getSistGarantiaJuve().getValor() : ""; %>
        var sistGarantiaJuve = '<%=sistGarantiaJuve != null ? sistGarantiaJuve.replace("null", "") : ""%>';

        <% String tDaSistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve() != null ? personaPuestoVO.getSistGarantiaJuve().getPcaTda() : ""; %>
        var tDaSistGarantiaJuve = '<%=tDaSistGarantiaJuve != null ? tDaSistGarantiaJuve.replace("null", "") : "" %>';
        <% String tamSistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve() != null ? personaPuestoVO.getSistGarantiaJuve().getPcaTam() : ""; %>
        var tamSistGarantiaJuve = '<%=tamSistGarantiaJuve != null ? tamSistGarantiaJuve.replace("null", "") : "" %>';
        <% String bloqSistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve() != null ? personaPuestoVO.getSistGarantiaJuve().getPcaBloq() : ""; %>
        var bloqSistGarantiaJuve = '<%=bloqSistGarantiaJuve != null ? bloqSistGarantiaJuve.replace("null", "") : "" %>';
        <% String ocultoSistGarantiaJuve = personaPuestoVO.getSistGarantiaJuve() != null ? personaPuestoVO.getSistGarantiaJuve().getPcaOculto() : ""; %>
        var ocultoSistGarantiaJuve = '<%=ocultoSistGarantiaJuve != null ? ocultoSistGarantiaJuve.replace("null", "") : "" %>';

        <% String docCvInter = personaPuestoVO.getDocCvInter() != null ? personaPuestoVO.getDocCvInter().getValor() : ""; %>
        var docCvInter = '<%=docCvInter != null ? docCvInter.replace("null", "") : ""%>';

        <% String tDaDocCvInter = personaPuestoVO.getDocCvInter() != null ? personaPuestoVO.getDocCvInter().getPcaTda() : ""; %>
        var tDaDocCvInter = '<%=tDaDocCvInter != null ? tDaDocCvInter.replace("null", "") : "" %>';
        <% String tamDocCvInter = personaPuestoVO.getDocCvInter() != null ? personaPuestoVO.getDocCvInter().getPcaTam() : ""; %>
        var tamDocCvInter = '<%=tamDocCvInter != null ? tamDocCvInter.replace("null", "") : "" %>';
        <% String bloqDocCvInter = personaPuestoVO.getDocCvInter() != null ? personaPuestoVO.getDocCvInter().getPcaBloq() : ""; %>
        var bloqDocCvInter = '<%=bloqDocCvInter != null ? bloqDocCvInter.replace("null", "") : "" %>';
        <% String ocultoDocCvInter = personaPuestoVO.getDocCvInter() != null ? personaPuestoVO.getDocCvInter().getPcaOculto() : ""; %>
        var ocultoDocCvInter = '<%=ocultoDocCvInter != null ? ocultoDocCvInter.replace("null", "") : "" %>';

        <% String docDemInter = personaPuestoVO.getDocDemInter() != null ? personaPuestoVO.getDocDemInter().getValor() : ""; %>
        var docDemInter = '<%=docDemInter != null ? docDemInter.replace("null", "") : ""%>';

        <% String tDaDocDemInter = personaPuestoVO.getDocDemInter() != null ? personaPuestoVO.getDocDemInter().getPcaTda() : ""; %>
        var tDaDocDemInter = '<%=tDaDocDemInter != null ? tDaDocDemInter.replace("null", "") : "" %>';
        <% String tamDocDemInter = personaPuestoVO.getDocDemInter() != null ? personaPuestoVO.getDocDemInter().getPcaTam() : ""; %>
        var tamDocDemInter = '<%=tamDocDemInter != null ? tamDocDemInter.replace("null", "") : "" %>';
        <% String bloqDocDemInter = personaPuestoVO.getDocDemInter() != null ? personaPuestoVO.getDocDemInter().getPcaBloq() : ""; %>
        var bloqDocDemInter = '<%=bloqDocDemInter != null ? bloqDocDemInter.replace("null", "") : "" %>';
        <% String ocultoDocDemInter = personaPuestoVO.getDocDemInter() != null ? personaPuestoVO.getDocDemInter().getPcaOculto() : ""; %>
        var ocultoDocDemInter = '<%=ocultoDocDemInter != null ? ocultoDocDemInter.replace("null", "") : "" %>';

        <% String fechaDemInter = personaPuestoVO.getFechaDemInter() != null ? personaPuestoVO.getFechaDemInter().getValor() : ""; %>
        var fechaDemInter = '<%=fechaDemInter != null ? fechaDemInter.replace("null", "") : ""%>';

        <% String tDaFechaDemInter = personaPuestoVO.getFechaDemInter() != null ? personaPuestoVO.getFechaDemInter().getPcaTda() : ""; %>
        var tDaFechaDemInter = '<%=tDaFechaDemInter != null ? tDaFechaDemInter.replace("null", "") : "" %>';
        <% String tamFechaDemInter = personaPuestoVO.getFechaDemInter() != null ? personaPuestoVO.getFechaDemInter().getPcaTam() : ""; %>
        var tamFechaDemInter = '<%=tamFechaDemInter != null ? tamFechaDemInter.replace("null", "") : "" %>';
        <% String bloqFechaDemInter = personaPuestoVO.getFechaDemInter() != null ? personaPuestoVO.getFechaDemInter().getPcaBloq() : ""; %>
        var bloqFechaDemInter = '<%=bloqFechaDemInter != null ? bloqFechaDemInter.replace("null", "") : "" %>';
        <% String ocultoFechaDemInter = personaPuestoVO.getFechaDemInter() != null ? personaPuestoVO.getFechaDemInter().getPcaOculto() : ""; %>
        var ocultoFechaDemInter = '<%=ocultoFechaDemInter != null ? ocultoFechaDemInter.replace("null", "") : "" %>';

        <% String fechaCvInter = personaPuestoVO.getFechaCvInter() != null ? personaPuestoVO.getFechaCvInter().getValor() : ""; %>
        var fechaCvInter = '<%=fechaCvInter != null ? fechaCvInter.replace("null", "") : ""%>';

        <% String tDaFechaCvInter = personaPuestoVO.getFechaCvInter() != null ? personaPuestoVO.getFechaCvInter().getPcaTda() : ""; %>
        var tDaFechaCvInter = '<%=tDaFechaCvInter != null ? tDaFechaCvInter.replace("null", "") : "" %>';
        <% String tamFechaCvInter = personaPuestoVO.getFechaCvInter() != null ? personaPuestoVO.getFechaCvInter().getPcaTam() : ""; %>
        var tamFechaCvInter = '<%=tamFechaCvInter != null ? tamFechaCvInter.replace("null", "") : "" %>';
        <% String bloqFechaCvInter = personaPuestoVO.getFechaCvInter() != null ? personaPuestoVO.getFechaCvInter().getPcaBloq() : ""; %>
        var bloqFechaCvInter = '<%=bloqFechaCvInter != null ? bloqFechaCvInter.replace("null", "") : "" %>';
        <% String ocultoFechaCvInter = personaPuestoVO.getFechaCvInter() != null ? personaPuestoVO.getFechaCvInter().getPcaOculto() : ""; %>
        var ocultoFechaCvInter = '<%=ocultoFechaCvInter != null ? ocultoFechaCvInter.replace("null", "") : "" %>';

        <% String desempleado = personaPuestoVO.getDesempleado() != null ? personaPuestoVO.getDesempleado().getValor() : ""; %>
        var desempleado = '<%=desempleado != null ? desempleado.replace("null", "") : ""%>';

        <% String tDaDesempleado = personaPuestoVO.getDesempleado() != null ? personaPuestoVO.getDesempleado().getPcaTda() : ""; %>
        var tDaDesempleado = '<%=tDaDesempleado != null ? tDaDesempleado.replace("null", "") : "" %>';
        <% String tamDesempleado = personaPuestoVO.getDesempleado() != null ? personaPuestoVO.getDesempleado().getPcaTam() : ""; %>
        var tamDesempleado = '<%=tamDesempleado != null ? tamDesempleado.replace("null", "") : "" %>';
        <% String bloqDesempleado = personaPuestoVO.getDesempleado() != null ? personaPuestoVO.getDesempleado().getPcaBloq() : ""; %>
        var bloqDesempleado = '<%=bloqDesempleado != null ? bloqDesempleado.replace("null", "") : "" %>';
        <% String ocultoDesempleado = personaPuestoVO.getDesempleado() != null ? personaPuestoVO.getDesempleado().getPcaOculto() : ""; %>
        var ocultoDesempleado = '<%=ocultoDesempleado != null ? ocultoDesempleado.replace("null", "") : "" %>';

        <% String fecIniContr = personaPuestoVO.getFecIniContr() != null ? personaPuestoVO.getFecIniContr().getValor() : ""; %>
        var fecIniContr = '<%=fecIniContr != null ? fecIniContr.replace("null", "") : ""%>';

        <% String tDaFecIniContr = personaPuestoVO.getFecIniContr() != null ? personaPuestoVO.getFecIniContr().getPcaTda() : ""; %>
        var tDaFecIniContr = '<%=tDaFecIniContr != null ? tDaFecIniContr.replace("null", "") : "" %>';
        <% String tamFecIniContr = personaPuestoVO.getFecIniContr() != null ? personaPuestoVO.getFecIniContr().getPcaTam() : ""; %>
        var tamFecIniContr = '<%=tamFecIniContr != null ? tamFecIniContr.replace("null", "") : "" %>';
        <% String bloqFecIniContr = personaPuestoVO.getFecIniContr() != null ? personaPuestoVO.getFecIniContr().getPcaBloq() : ""; %>
        var bloqFecIniContr = '<%=bloqFecIniContr != null ? bloqFecIniContr.replace("null", "") : "" %>';
        <% String ocultoFecIniContr = personaPuestoVO.getFecIniContr() != null ? personaPuestoVO.getFecIniContr().getPcaOculto() : ""; %>
        var ocultoFecIniContr = '<%=ocultoFecIniContr != null ? ocultoFecIniContr.replace("null", "") : "" %>';

        <% String mujSubRepre = personaPuestoVO.getMujSubRepre() != null ? personaPuestoVO.getMujSubRepre().getValor() : ""; %>
        var mujSubRepre = '<%=mujSubRepre != null ? mujSubRepre.replace("null", "") : ""%>';

        <% String tDaMujSubRepre = personaPuestoVO.getMujSubRepre() != null ? personaPuestoVO.getMujSubRepre().getPcaTda() : ""; %>
        var tDaMujSubRepre = '<%=tDaMujSubRepre != null ? tDaMujSubRepre.replace("null", "") : "" %>';
        <% String tamMujSubRepre = personaPuestoVO.getMujSubRepre() != null ? personaPuestoVO.getMujSubRepre().getPcaTam() : ""; %>
        var tamMujSubRepre = '<%=tamMujSubRepre != null ? tamMujSubRepre.replace("null", "") : "" %>';
        <% String bloqMujSubRepre = personaPuestoVO.getMujSubRepre() != null ? personaPuestoVO.getMujSubRepre().getPcaBloq() : ""; %>
        var bloqMujSubRepre = '<%=bloqMujSubRepre != null ? bloqMujSubRepre.replace("null", "") : "" %>';
        <% String ocultoMujSubRepre = personaPuestoVO.getMujSubRepre() != null ? personaPuestoVO.getMujSubRepre().getPcaOculto() : ""; %>
        var ocultoMujSubRepre = '<%=ocultoMujSubRepre != null ? ocultoMujSubRepre.replace("null", "") : "" %>';

        <% String numCtaCotSS = personaPuestoVO.getNumCtaCotSS() != null ? personaPuestoVO.getNumCtaCotSS().getValor() : ""; %>
        var numCtaCotSS = '<%=numCtaCotSS != null ? numCtaCotSS.replace("null", "") : ""%>';

        <% String tDaNumCtaCotSS = personaPuestoVO.getNumCtaCotSS() != null ? personaPuestoVO.getNumCtaCotSS().getPcaTda() : ""; %>
        var tDaNumCtaCotSS = '<%=tDaNumCtaCotSS != null ? tDaNumCtaCotSS.replace("null", "") : "" %>';
        <% String tamNumCtaCotSS = personaPuestoVO.getNumCtaCotSS() != null ? personaPuestoVO.getNumCtaCotSS().getPcaTam() : ""; %>
        var tamNumCtaCotSS = '<%=tamNumCtaCotSS != null ? tamNumCtaCotSS.replace("null", "") : "" %>';
        <% String bloqNumCtaCotSS = personaPuestoVO.getNumCtaCotSS() != null ? personaPuestoVO.getNumCtaCotSS().getPcaBloq() : ""; %>
        var bloqNumCtaCotSS = '<%=bloqNumCtaCotSS != null ? bloqNumCtaCotSS.replace("null", "") : "" %>';
        <% String ocultoNumCtaCotSS = personaPuestoVO.getNumCtaCotSS() != null ? personaPuestoVO.getNumCtaCotSS().getPcaOculto() : ""; %>
        var ocultoNumCtaCotSS = '<%=ocultoNumCtaCotSS != null ? ocultoNumCtaCotSS.replace("null", "") : "" %>';

        <% String dirCenTrab = personaPuestoVO.getDirCenTrab() != null ? personaPuestoVO.getDirCenTrab().getValor() : ""; %>
        var dirCenTrab = '<%=dirCenTrab != null ? dirCenTrab.replace("null", "") : ""%>';

        <% String tDaDirCenTrab = personaPuestoVO.getDirCenTrab() != null ? personaPuestoVO.getDirCenTrab().getPcaTda() : ""; %>
        var tDaDirCenTrab = '<%=tDaDirCenTrab != null ? tDaDirCenTrab.replace("null", "") : "" %>';
        <% String tamDirCenTrab = personaPuestoVO.getDirCenTrab() != null ? personaPuestoVO.getDirCenTrab().getPcaTam() : ""; %>
        var tamDirCenTrab = '<%=tamDirCenTrab != null ? tamDirCenTrab.replace("null", "") : "" %>';
        <% String bloqDirCenTrab = personaPuestoVO.getDirCenTrab() != null ? personaPuestoVO.getDirCenTrab().getPcaBloq() : ""; %>
        var bloqDirCenTrab = '<%=bloqDirCenTrab != null ? bloqDirCenTrab.replace("null", "") : "" %>';
        <% String ocultoDirCenTrab = personaPuestoVO.getDirCenTrab() != null ? personaPuestoVO.getDirCenTrab().getPcaOculto() : ""; %>
        var ocultoDirCenTrab = '<%=ocultoDirCenTrab != null ? ocultoDirCenTrab.replace("null", "") : "" %>';

        <% String grupCotSS = personaPuestoVO.getGrupCotSS() != null ? personaPuestoVO.getGrupCotSS().getValor() : ""; %>
        var grupCotSS = '<%=grupCotSS != null ? grupCotSS.replace("null", "") : ""%>';

        <% String tDaGrupCotSS = personaPuestoVO.getGrupCotSS() != null ? personaPuestoVO.getGrupCotSS().getPcaTda() : ""; %>
        var tDaGrupCotSS = '<%=tDaGrupCotSS != null ? tDaGrupCotSS.replace("null", "") : "" %>';
        <% String tamGrupCotSS = personaPuestoVO.getGrupCotSS() != null ? personaPuestoVO.getGrupCotSS().getPcaTam() : ""; %>
        var tamGrupCotSS = '<%=tamGrupCotSS != null ? tamGrupCotSS.replace("null", "") : "" %>';
        <% String bloqGrupCotSS = personaPuestoVO.getGrupCotSS() != null ? personaPuestoVO.getGrupCotSS().getPcaBloq() : ""; %>
        var bloqGrupCotSS = '<%=bloqGrupCotSS != null ? bloqGrupCotSS.replace("null", "") : "" %>';
        <% String ocultoGrupCotSS = personaPuestoVO.getGrupCotSS() != null ? personaPuestoVO.getGrupCotSS().getPcaOculto() : ""; %>
        var ocultoGrupCotSS = '<%=ocultoGrupCotSS != null ? ocultoGrupCotSS.replace("null", "") : "" %>';

        <% String jorPuestra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getValor() : ""; %>
        var jorPuestra = '<%=jorPuestra != null ? jorPuestra.replace("null", "") : ""%>';

        <% String tDaJorPuestra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaTda() : ""; %>
        var tDaJorPuestra = '<%=tDaJorPuestra != null ? tDaJorPuestra.replace("null", "") : "" %>';
        <% String tamJorPuestra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaTam() : ""; %>
        var tamJorPuestra = '<%=tamJorPuestra != null ? tamJorPuestra.replace("null", "") : "" %>';
        <% String bloqJorPuestra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaBloq() : ""; %>
        var bloqJorPuestra = '<%=bloqJorPuestra != null ? bloqJorPuestra.replace("null", "") : "" %>';
        <% String ocultoJorPuestra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaOculto() : ""; %>
        var ocultoJorPuestra = '<%=ocultoJorPuestra != null ? ocultoJorPuestra.replace("null", "") : "" %>';

        <% String modContra = personaPuestoVO.getModContra() != null ? personaPuestoVO.getModContra().getValor() : ""; %>
        var modContra = '<%=modContra != null ? modContra.replace("null", "") : ""%>';

        <% String tDaModContra = personaPuestoVO.getModContra() != null ? personaPuestoVO.getModContra().getPcaTda() : ""; %>
        var tDaModContra = '<%=tDaModContra != null ? tDaModContra.replace("null", "") : "" %>';
        <% String tamModContra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaTam() : ""; %>
        var tamModContra = '<%=tamModContra != null ? tamModContra.replace("null", "") : "" %>';
        <% String bloqModContra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaBloq() : ""; %>
        var bloqModContra = '<%=bloqModContra != null ? bloqModContra.replace("null", "") : "" %>';
        <% String ocultoModContra = personaPuestoVO.getJorPuestra() != null ? personaPuestoVO.getJorPuestra().getPcaOculto() : ""; %>
        var ocultoModContra = '<%=ocultoModContra != null ? ocultoModContra.replace("null", "") : "" %>';

        <% String titPuestra = personaPuestoVO.getTitPuestra() != null ? personaPuestoVO.getTitPuestra().getValor() : ""; %>
        var titPuestra = '<%=titPuestra != null ? titPuestra.replace("null", "") : ""%>';

        <% String tDaTitPuestra = personaPuestoVO.getTitPuestra() != null ? personaPuestoVO.getTitPuestra().getPcaTda() : ""; %>
        var tDaTitPuestra = '<%=tDaTitPuestra != null ? tDaTitPuestra.replace("null", "") : "" %>';
        <% String tamTitPuestra = personaPuestoVO.getTitPuestra() != null ? personaPuestoVO.getTitPuestra().getPcaTam() : ""; %>
        var tamTitPuestra = '<%=tamTitPuestra != null ? tamTitPuestra.replace("null", "") : "" %>';
        <% String bloqTitPuestra = personaPuestoVO.getTitPuestra() != null ? personaPuestoVO.getTitPuestra().getPcaBloq() : ""; %>
        var bloqTitPuestra = '<%=bloqTitPuestra != null ? bloqTitPuestra.replace("null", "") : "" %>';
        <% String ocultoTitPuestra = personaPuestoVO.getTitPuestra() != null ? personaPuestoVO.getTitPuestra().getPcaOculto() : ""; %>
        var ocultoTitPuestra = '<%=ocultoTitPuestra != null ? ocultoTitPuestra.replace("null", "") : "" %>';

        <% String nomCodOcu = personaPuestoVO.getNomCodOcu() != null ? personaPuestoVO.getNomCodOcu().getValor() : ""; %>
        var nomCodOcu = '<%=nomCodOcu != null ? nomCodOcu.replace("null", "") : ""%>';

        <% String tDaNomCodOcu = personaPuestoVO.getNomCodOcu() != null ? personaPuestoVO.getNomCodOcu().getPcaTda() : ""; %>
        var tDaNomCodOcu = '<%=tDaNomCodOcu != null ? tDaNomCodOcu.replace("null", "") : "" %>';
        <% String tamNomCodOcu = personaPuestoVO.getNomCodOcu() != null ? personaPuestoVO.getNomCodOcu().getPcaTam() : ""; %>
        var tamNomCodOcu = '<%=tamNomCodOcu != null ? tamNomCodOcu.replace("null", "") : "" %>';
        <% String bloqNomCodOcu = personaPuestoVO.getNomCodOcu() != null ? personaPuestoVO.getNomCodOcu().getPcaBloq() : ""; %>
        var bloqNomCodOcu = '<%=bloqNomCodOcu != null ? bloqNomCodOcu.replace("null", "") : "" %>';
        <% String ocultoNomCodOcu = personaPuestoVO.getNomCodOcu() != null ? personaPuestoVO.getNomCodOcu().getPcaOculto() : ""; %>
        var ocultoNomCodOcu = '<%=ocultoNomCodOcu != null ? ocultoNomCodOcu.replace("null", "") : "" %>';

        <% String nomPuesTra = personaPuestoVO.getNomPuesTra() != null ? personaPuestoVO.getNomPuesTra().getValor() : ""; %>
        var nomPuesTra = '<%=nomPuesTra != null ? nomPuesTra.replace("null", "") : ""%>';

        <% String tDaNomPuesTra = personaPuestoVO.getNomPuesTra() != null ? personaPuestoVO.getNomPuesTra().getPcaTda() : ""; %>
        var tDaNomPuesTra = '<%=tDaNomPuesTra != null ? tDaNomPuesTra.replace("null", "") : "" %>';
        <% String tamNomPuesTra = personaPuestoVO.getNomPuesTra() != null ? personaPuestoVO.getNomPuesTra().getPcaTam() : ""; %>
        var tamNomPuesTra = '<%=tamNomPuesTra != null ? tamNomPuesTra.replace("null", "") : "" %>';
        <% String bloqNomPuesTra = personaPuestoVO.getNomPuesTra() != null ? personaPuestoVO.getNomPuesTra().getPcaBloq() : ""; %>
        var bloqNomPuesTra = '<%=bloqNomPuesTra != null ? bloqNomPuesTra.replace("null", "") : "" %>';
        <% String ocultoNomPuesTra = personaPuestoVO.getNomPuesTra() != null ? personaPuestoVO.getNomPuesTra().getPcaOculto() : ""; %>
        var ocultoNomPuesTra = '<%=ocultoNomPuesTra != null ? ocultoNomPuesTra.replace("null", "") : "" %>';

        <% String fecReFinContr = personaPuestoVO.getFecReFinContr() != null ? personaPuestoVO.getFecReFinContr().getValor() : ""; %>
        var fecReFinContr = '<%=fecReFinContr != null ? fecReFinContr.replace("null", "") : ""%>';

        <% String tDaFecReFinContr = personaPuestoVO.getFecReFinContr() != null ? personaPuestoVO.getFecReFinContr().getPcaTda() : ""; %>
        var tDaFecReFinContr = '<%=tDaFecReFinContr != null ? tDaFecReFinContr.replace("null", "") : "" %>';
        <% String tamFecReFinContr = personaPuestoVO.getFecReFinContr() != null ? personaPuestoVO.getFecReFinContr().getPcaTam() : ""; %>
        var tamFecReFinContr = '<%=tamFecReFinContr != null ? tamFecReFinContr.replace("null", "") : "" %>';
        <% String bloqFecReFinContr = personaPuestoVO.getFecReFinContr() != null ? personaPuestoVO.getFecReFinContr().getPcaBloq() : ""; %>
        var bloqFecReFinContr = '<%=bloqFecReFinContr != null ? bloqFecReFinContr.replace("null", "") : "" %>';
        <% String ocultoFecReFinContr = personaPuestoVO.getFecReFinContr() != null ? personaPuestoVO.getFecReFinContr().getPcaOculto() : ""; %>
        var ocultoFecReFinContr = '<%=ocultoFecReFinContr != null ? ocultoFecReFinContr.replace("null", "") : "" %>';

        <% String fecFinContr = personaPuestoVO.getFecFinContr() != null ? personaPuestoVO.getFecFinContr().getValor() : ""; %>
        var fecFinContr = '<%=fecFinContr != null ? fecFinContr.replace("null", "") : ""%>';

        <% String tDaFecFinContr = personaPuestoVO.getFecFinContr() != null ? personaPuestoVO.getFecFinContr().getPcaTda() : ""; %>
        var tDaFecFinContr = '<%=tDaFecFinContr != null ? tDaFecFinContr.replace("null", "") : "" %>';
        <% String tamFecFinContr = personaPuestoVO.getFecFinContr() != null ? personaPuestoVO.getFecFinContr().getPcaTam() : ""; %>
        var tamFecFinContr = '<%=tamFecFinContr != null ? tamFecFinContr.replace("null", "") : "" %>';
        <% String bloqFecFinContr = personaPuestoVO.getFecFinContr() != null ? personaPuestoVO.getFecFinContr().getPcaBloq() : ""; %>
        var bloqFecFinContr = '<%=bloqFecFinContr != null ? bloqFecFinContr.replace("null", "") : "" %>';
        <% String ocultoFecFinContr = personaPuestoVO.getFecFinContr() != null ? personaPuestoVO.getFecFinContr().getPcaOculto() : ""; %>
        var ocultoFecFinContr = '<%=ocultoFecFinContr != null ? ocultoFecFinContr.replace("null", "") : "" %>';

        <% String salarioMin = personaPuestoVO.getSalarioMin() != null ? personaPuestoVO.getSalarioMin().getValor() : ""; %>
        var salarioMin = '<%=salarioMin != null ? salarioMin.replace("null", "") : ""%>';

        <% String tDaSalarioMin = personaPuestoVO.getSalarioMin() != null ? personaPuestoVO.getSalarioMin().getPcaTda() : ""; %>
        var tDaSalarioMin = '<%=tDaSalarioMin != null ? tDaSalarioMin.replace("null", "") : "" %>';
        <% String tamSalarioMin = personaPuestoVO.getSalarioMin() != null ? personaPuestoVO.getSalarioMin().getPcaTam() : ""; %>
        var tamSalarioMin = '<%=tamSalarioMin != null ? tamSalarioMin.replace("null", "") : "" %>';
        <% String bloqSalarioMin = personaPuestoVO.getSalarioMin() != null ? personaPuestoVO.getSalarioMin().getPcaBloq() : ""; %>
        var bloqSalarioMin = '<%=bloqSalarioMin != null ? bloqSalarioMin.replace("null", "") : "" %>';
        <% String ocultoSalarioMin = personaPuestoVO.getSalarioMin() != null ? personaPuestoVO.getSalarioMin().getPcaOculto() : ""; %>
        var ocultoSalarioMin = '<%=ocultoSalarioMin != null ? ocultoSalarioMin.replace("null", "") : "" %>';

        <% String titulacionEspecifica = personaPuestoVO.getTitulacionEspecifica() != null ? personaPuestoVO.getTitulacionEspecifica().getValor() : ""; %>
        var titulacionEspecifica = '<%=titulacionEspecifica != null ? titulacionEspecifica.replace("null", "") : ""%>';

        <% String tDaTitulacionEspecifica = personaPuestoVO.getTitulacionEspecifica() != null ? personaPuestoVO.getTitulacionEspecifica().getPcaTda() : ""; %>
        var tDaTitulacionEspecifica = '<%=tDaTitulacionEspecifica != null ? tDaTitulacionEspecifica.replace("null", "") : "" %>';
        <% String tamTitulacionEspecifica = personaPuestoVO.getTitulacionEspecifica() != null ? personaPuestoVO.getTitulacionEspecifica().getPcaTam() : ""; %>
        var tamTitulacionEspecifica = '<%=tamTitulacionEspecifica != null ? tamTitulacionEspecifica.replace("null", "") : "" %>';
        <% String bloqTitulacionEspecifica = personaPuestoVO.getTitulacionEspecifica() != null ? personaPuestoVO.getTitulacionEspecifica().getPcaBloq() : ""; %>
        var bloqTitulacionEspecifica = '<%=bloqTitulacionEspecifica != null ? bloqTitulacionEspecifica.replace("null", "") : "" %>';
        <% String ocultoTitulacionEspecifica = personaPuestoVO.getTitulacionEspecifica() != null ? personaPuestoVO.getTitulacionEspecifica().getPcaOculto() : ""; %>
        var ocultoTitulacionEspecifica = '<%=ocultoTitulacionEspecifica != null ? ocultoTitulacionEspecifica.replace("null", "") : "" %>';

        <% String cosConReal = personaPuestoVO.getCosConReal() != null ? personaPuestoVO.getCosConReal().getValor() : ""; %>
        var cosConReal = '<%=cosConReal != null ? cosConReal.replaceAll("\\.", ",") : ""%>';

        <% String tDaCosConReal = personaPuestoVO.getCosConReal() != null ? personaPuestoVO.getCosConReal().getPcaTda() : ""; %>
        var tDaCosConReal = '<%=tDaCosConReal != null ? tDaCosConReal.replace("null", "") : "" %>';
        <% String tamCosConReal = personaPuestoVO.getCosConReal() != null ? personaPuestoVO.getCosConReal().getPcaTam() : ""; %>
        var tamCosConReal = '<%=tamCosConReal != null ? tamCosConReal.replace("null", "") : "" %>';
        <% String bloqCosConReal = personaPuestoVO.getCosConReal() != null ? personaPuestoVO.getCosConReal().getPcaBloq() : ""; %>
        var bloqCosConReal = '<%=bloqCosConReal != null ? bloqCosConReal.replace("null", "") : "" %>';
        <% String ocultoCosConReal = personaPuestoVO.getCosConReal() != null ? personaPuestoVO.getCosConReal().getPcaOculto() : ""; %>
        var ocultoCosConReal = '<%=ocultoCosConReal != null ? ocultoCosConReal.replace("null", "") : "" %>';

        <% String porcJorn = personaPuestoVO.getPorcJorn() != null ? personaPuestoVO.getPorcJorn().getValor() : ""; %>
        var porcJorn = '<%=porcJorn != null ? porcJorn.replace("null", "") : ""%>';

        <% String tDaPorcJorn = personaPuestoVO.getPorcJorn() != null ? personaPuestoVO.getPorcJorn().getPcaTda() : ""; %>
        var tDaPorcJorn = '<%=tDaPorcJorn != null ? tDaPorcJorn.replace("null", "") : "" %>';
        <% String tamPorcJorn = personaPuestoVO.getPorcJorn() != null ? personaPuestoVO.getPorcJorn().getPcaTam() : ""; %>
        var tamPorcJorn = '<%=tamPorcJorn != null ? tamPorcJorn.replace("null", "") : "" %>';
        <% String bloqPorcJorn = personaPuestoVO.getPorcJorn() != null ? personaPuestoVO.getPorcJorn().getPcaBloq() : ""; %>
        var bloqPorcJorn = '<%=bloqPorcJorn != null ? bloqPorcJorn.replace("null", "") : "" %>';
        <% String ocultoPorcJorn = personaPuestoVO.getPorcJorn() != null ? personaPuestoVO.getPorcJorn().getPcaOculto() : ""; %>
        var ocultoPorcJorn = '<%=ocultoPorcJorn != null ? ocultoPorcJorn.replace("null", "") : "" %>';

        <% String duraContra = personaPuestoVO.getDuraContra() != null ? personaPuestoVO.getDuraContra().getValor() : ""; %>
        var duraContra = '<%=duraContra != null ? duraContra.replace("null", "") : ""%>';

        <% String tDaDuraContra = personaPuestoVO.getDuraContra() != null ? personaPuestoVO.getDuraContra().getPcaTda() : ""; %>
        var tDaDuraContra = '<%=tDaDuraContra != null ? tDaDuraContra.replace("null", "") : "" %>';
        <% String tamDuraContra = personaPuestoVO.getDuraContra() != null ? personaPuestoVO.getDuraContra().getPcaTam() : ""; %>
        var tamDuraContra = '<%=tamDuraContra != null ? tamDuraContra.replace("null", "") : "" %>';
        <% String bloqDuraContra = personaPuestoVO.getDuraContra() != null ? personaPuestoVO.getDuraContra().getPcaBloq() : ""; %>
        var bloqDuraContra = '<%=bloqDuraContra != null ? bloqDuraContra.replace("null", "") : "" %>';
        <% String ocultoDuraContra = personaPuestoVO.getDuraContra() != null ? personaPuestoVO.getDuraContra().getPcaOculto() : ""; %>
        var ocultoDuraContra = '<%=ocultoDuraContra != null ? ocultoDuraContra.replace("null", "") : "" %>';

        var titleWindowPersonaContratada = '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.tituloPestana")%>';

        function cargarDatos() {
            console.log("cargarDatos: dniPerCon=" + dniPerCon + ", nomPerCon=" + nomPerCon + ", apel1PerCon=" + apel1PerCon + ", apel2PerCon=" + apel2PerCon + ", fecNacPerCon=" +
                    fecNacPerCon + ", edadPerCon=" + edadPerCon + ", sexoPerCont=" + sexoPerCont + ", muniPerCon=" + muniPerCon + ", experiencia6M=" +
                    experiencia6M + ", desempleadoAnterior=" + desempleadoAnterior + ", sistGarantiaJuve=" + sistGarantiaJuve + ", docCvInter=" + docCvInter + ", docDemInter=" +
                    docDemInter + ", fechaCvInter=" + fechaCvInter + ", idiomaUsuario=" + idiomaUsuario +
                    ", fechaDemInter=" + fechaDemInter + ", desempleado=" + desempleado + ", fecIniContr=" + fecIniContr +
                    ", mujSubRepre=" + mujSubRepre + ", numCtaCotSS=" + numCtaCotSS + ", dirCenTrab=" + dirCenTrab +
                    ", grupCotSS=" + grupCotSS + ", jorPuestra=" + jorPuestra +
                    ", modContra=" + modContra + ", titPuestra=" + titPuestra + ", nomCodOcu=" + nomCodOcu +
                    ", nomPuesTra=" + nomPuesTra + ", fecReFinContr=" + fecReFinContr + ", fecFinContr=" + fecFinContr +
                    ", salarioMin=" + salarioMin + ", titulacionEspecifica=" + titulacionEspecifica + ", cosConReal=" + cosConReal +
                    ", porcJorn=" + porcJorn + ", duraContra=" + duraContra);

            document.getElementById("divDniPerCon").style.visibility = ocultoDniPerCon == "SI" ? "hidden" : "visible";
            document.getElementById("dniPerCon").maxLength = tamDniPerCon;
            document.getElementById("dniPerCon").disabled = bloqDniPerCon == "SI" ? true : false;
            document.getElementById("dniPerCon").value = dniPerCon;

            document.getElementById("divNomPerCon").style.visibility = ocultoNomPerCon == "SI" ? "hidden" : "visible";
            document.getElementById("nomPerCon").maxLength = tamNomPerCon;
            document.getElementById("nomPerCon").disabled = bloqNomPerCon == "SI" ? true : false;
            document.getElementById("nomPerCon").value = nomPerCon;

            document.getElementById("divApel1PerCon").style.visibility = ocultoApel1PerCon == "SI" ? "hidden" : "visible";
            document.getElementById("apel1PerCon").maxLength = tamApel1PerCon;
            document.getElementById("apel1PerCon").disabled = bloqApel1PerCon == "SI" ? true : false;
            document.getElementById("apel1PerCon").value = apel1PerCon;

            document.getElementById("divApel2PerCon").style.visibility = ocultoApel2PerCon == "SI" ? "hidden" : "visible";
            document.getElementById("apel2PerCon").maxLength = tamApel2PerCon;
            document.getElementById("apel2PerCon").disabled = bloqApel2PerCon == "SI" ? true : false;
            document.getElementById("apel2PerCon").value = apel2PerCon;

            document.getElementById("divFecNacPerCon").style.visibility = ocultoFecNacPerCon == "SI" ? "hidden" : "visible";
            document.getElementById("fecNacPerCon").maxLength = tamFecNacPerCon;
            document.getElementById("fecNacPerCon").disabled = bloqFecNacPerCon == "SI" ? true : false;
            document.getElementById("fecNacPerCon").value = fecNacPerCon;

            document.getElementById("divEdadPerCon").style.visibility = ocultoEdadPerCon == "SI" ? "hidden" : "visible";
            document.getElementById("edadPerCon").maxLength = tamEdadPerCon;
            document.getElementById("edadPerCon").disabled = bloqEdadPerCon == "SI" ? true : false;
            document.getElementById("edadPerCon").value = edadPerCon;

            document.getElementById("divSexoPerCont").style.visibility = ocultoSexoPerCont == "SI" ? "hidden" : "visible";
            document.getElementById("descSexoPerCont").maxLength = tamSexoPerCont;
            document.getElementById("codSexoPerCont").disabled = true;//bloqSexoPerCont == "SI" ? true : false;
            document.getElementById("codSexoPerCont").value = sexoPerCont;

            document.getElementById("divMuniPerCon").style.visibility = ocultoMuniPerCon == "SI" ? "hidden" : "visible";
            document.getElementById("descMuniPerCon").maxLength = tamMuniPerCon;
            document.getElementById("codMuniPerCon").disabled = true; //bloqMuniPerCon == "SI" ? true : false;
            document.getElementById("codMuniPerCon").value = muniPerCon;

            document.getElementById("divExperiencia6M").style.visibility = ocultoExperiencia6M == "SI" ? "hidden" : "visible";
            document.getElementById("descExperiencia6M").maxLength = tamExperiencia6M;
            document.getElementById("codExperiencia6M").disabled = true; //bloqExperiencia6M == "SI" ? true : false;
            document.getElementById("codExperiencia6M").value = experiencia6M;

            document.getElementById("divDesempleado").style.visibility = ocultoDesempleado == "SI" ? "hidden" : "visible";
            document.getElementById("descDesempleado").maxLength = tamDesempleado;
            document.getElementById("codDesempleado").disabled = true; //bloqDesempleado == "SI" ? true : false;
            document.getElementById("codDesempleado").value = desempleado;

            document.getElementById("divDesempleadoAnterior").style.visibility = ocultoDesempleadoAnterior == "SI" ? "hidden" : "visible";
            document.getElementById("descDesempleadoAnterior").maxLength = tamDesempleadoAnterior;
            document.getElementById("codDesempleadoAnterior").disabled = true; //bloqDesempleadoAnterior == "SI" ? true : false;
            document.getElementById("codDesempleadoAnterior").value = desempleadoAnterior;

            document.getElementById("divSistGarantiaJuve").style.visibility = ocultoSistGarantiaJuve == "SI" ? "hidden" : "visible";
            document.getElementById("descSistGarantiaJuve").maxLength = tamSistGarantiaJuve;
            document.getElementById("codSistGarantiaJuve").disabled = true; //bloqSistGarantiaJuve == "SI" ? true : false;
            document.getElementById("codSistGarantiaJuve").value = sistGarantiaJuve;

            document.getElementById("divDocCvInter").style.visibility = ocultoDocCvInter == "SI" ? "hidden" : "visible";
//            document.getElementById("docCvInter").maxLength = tamDocCvInter;
//            document.getElementById("docCvInter").disabled = bloqDocCvInter == "SI" ? true : false;
//            document.getElementById("docCvInter").value = docCvInter;

            document.getElementById("divDocDemInter").style.visibility = ocultoDocDemInter == "SI" ? "hidden" : "visible";
//            document.getElementById("docDemInter").maxLength = tamDocDemInter;
//            document.getElementById("docDemInter").disabled = bloqDocDemInter == "SI" ? true : false;
//            document.getElementById("docDemInter").value = docDemInter;

            document.getElementById("divFechaDemInter").style.visibility = ocultoFechaDemInter == "SI" ? "hidden" : "visible";
            document.getElementById("fechaDemInter").maxLength = tamFechaDemInter;
            document.getElementById("fechaDemInter").disabled = bloqFechaDemInter == "SI" ? true : false;
            document.getElementById("fechaDemInter").value = fechaDemInter;

            document.getElementById("divFechaCvInter").style.visibility = ocultoFechaCvInter == "SI" ? "hidden" : "visible";
            document.getElementById("fechaCvInter").maxLength = tamFechaCvInter;
            document.getElementById("fechaCvInter").disabled = bloqFechaCvInter == "SI" ? true : false;
            document.getElementById("fechaCvInter").value = fechaCvInter;

            document.getElementById("divFecIniContr").style.visibility = ocultoFecIniContr == "SI" ? "hidden" : "visible";
            document.getElementById("fecIniContr").maxLength = tamFecIniContr;
            document.getElementById("fecIniContr").disabled = bloqFecIniContr == "SI" ? true : false;
            document.getElementById("fecIniContr").value = fecIniContr;

            console.log("ocultoMujSubRepre = " + ocultoMujSubRepre);
            document.getElementById("divMujSubRepre").style.visibility = ocultoMujSubRepre == "SI" ? "hidden" : "visible";
            document.getElementById("descMujSubRepre").maxLength = tamMujSubRepre;
            document.getElementById("codMujSubRepre").disabled = true; //bloqMujSubRepre == "SI" ? true : false;
            document.getElementById("codMujSubRepre").value = mujSubRepre;

            document.getElementById("divNumCtaCotSS").style.visibility = ocultoNumCtaCotSS == "SI" ? "hidden" : "visible";
            document.getElementById("numCtaCotSS").maxLength = tamNumCtaCotSS;
            document.getElementById("numCtaCotSS").disabled = bloqNumCtaCotSS == "SI" ? true : false;
            document.getElementById("numCtaCotSS").value = numCtaCotSS;

            document.getElementById("divDirCenTrab").style.visibility = ocultoDirCenTrab == "SI" ? "hidden" : "visible";
            document.getElementById("dirCenTrab").maxLength = tamDirCenTrab;
            document.getElementById("dirCenTrab").disabled = bloqDirCenTrab == "SI" ? true : false;
            document.getElementById("dirCenTrab").value = dirCenTrab;

            document.getElementById("divGrupCotSS").style.visibility = ocultoGrupCotSS == "SI" ? "hidden" : "visible";
            document.getElementById("descGrupCotSS").maxLength = tamGrupCotSS;
            document.getElementById("codGrupCotSS").disabled = true; //bloqGrupCotSS == "SI" ? true : false;
            document.getElementById("codGrupCotSS").value = grupCotSS;

            document.getElementById("divJorPuestra").style.visibility = ocultoJorPuestra == "SI" ? "hidden" : "visible";
            document.getElementById("descJorPuestra").maxLength = tamJorPuestra;
            document.getElementById("codJorPuestra").disabled = true; //bloqJorPuestra == "SI" ? true : false;
            document.getElementById("codJorPuestra").value = jorPuestra;

            document.getElementById("divModContra").style.visibility = ocultoModContra == "SI" ? "hidden" : "visible";
            document.getElementById("descModContra").maxLength = tamModContra;
            document.getElementById("codModContra").disabled = true; //bloqModContra == "SI" ? true : false;
            document.getElementById("codModContra").value = modContra;

            document.getElementById("divTitPuestra").style.visibility = ocultoTitPuestra == "SI" ? "hidden" : "visible";
            document.getElementById("descTitPuestra").maxLength = tamTitPuestra;
            document.getElementById("codTitPuestra").disabled = true; //bloqTitPuestra == "SI" ? true : false;
            document.getElementById("codTitPuestra").value = titPuestra;

            document.getElementById("divNomCodOcu").style.visibility = ocultoNomCodOcu == "SI" ? "hidden" : "visible";
            document.getElementById("descNomCodOcu").maxLength = tamNomCodOcu;
            document.getElementById("codNomCodOcu").disabled = true; //bloqNomCodOcu == "SI" ? true : false;
            document.getElementById("codNomCodOcu").value = nomCodOcu;

            document.getElementById("divNomPuesTra").style.visibility = ocultoNomPuesTra == "SI" ? "hidden" : "visible";
            document.getElementById("nomPuesTra").maxLength = tamNomPuesTra;
            document.getElementById("nomPuesTra").disabled = bloqNomPuesTra == "SI" ? true : false;
            document.getElementById("nomPuesTra").value = nomPuesTra;

            document.getElementById("divFecReFinContr").style.visibility = ocultoFecReFinContr == "SI" ? "hidden" : "visible";
            document.getElementById("fecReFinContr").maxLength = tamFecReFinContr;
            document.getElementById("fecReFinContr").disabled = bloqFecReFinContr == "SI" ? true : false;
            document.getElementById("fecReFinContr").value = fecReFinContr;

            document.getElementById("divFecFinContr").style.visibility = ocultoFecFinContr == "SI" ? "hidden" : "visible";
            document.getElementById("fecFinContr").maxLength = tamFecFinContr;
            document.getElementById("fecFinContr").disabled = bloqFecFinContr == "SI" ? true : false;
            document.getElementById("fecFinContr").value = fecFinContr;

            document.getElementById("divSalarioMin").style.visibility = ocultoSalarioMin == "SI" ? "hidden" : "visible";
            document.getElementById("descSalarioMin").value = tamSalarioMin;
            document.getElementById("codSalarioMin").disabled = true; //bloqSalarioMin == "SI" ? true : false;
            document.getElementById("codSalarioMin").value = salarioMin;

            document.getElementById("divTitulacionEspecifica").style.visibility = ocultoTitulacionEspecifica == "SI" ? "hidden" : "visible";
            document.getElementById("titulacionEspecifica").maxLength = tamTitulacionEspecifica;
            document.getElementById("titulacionEspecifica").disabled = bloqTitulacionEspecifica == "SI" ? true : false;
            document.getElementById("titulacionEspecifica").value = titulacionEspecifica;

            document.getElementById("divCosConReal").style.visibility = ocultoCosConReal == "SI" ? "hidden" : "visible";
            document.getElementById("cosConReal").maxLength = tamCosConReal;
            document.getElementById("cosConReal").disabled = bloqCosConReal == "SI" ? true : false;
            document.getElementById("cosConReal").value = cosConReal;

            document.getElementById("divPorcJorn").style.visibility = ocultoPorcJorn == "SI" ? "hidden" : "visible";
            document.getElementById("porcJorn").maxLength = tamPorcJorn;
            document.getElementById("porcJorn").disabled = bloqPorcJorn == "SI" ? true : false;
            document.getElementById("porcJorn").value = porcJorn;
            
            document.getElementById("divDuraContra").style.visibility = ocultoDuraContra == "SI" ? "hidden" : "visible";
            document.getElementById("descDuraContra").maxLength = tamDuraContra;
            document.getElementById("codDuraContra").disabled = true; //bloqDuraContra == "SI" ? true : false;
            document.getElementById("codDuraContra").value = duraContra;
        }

        function guardarDatosPersonaContratada() {
            pleaseWait('on');

            var parametrosLLamadaMelanbide67 = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE67'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, parametrosLLamadaMelanbide67);

            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            var codMun = document.forms[0].codMunicipio.value;
            var ejercicio = document.forms[0].ejercicio.value;
            var numero = document.forms[0].numero.value;
            var expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codMunicipio = document.forms[0].codMunicipio.value;
            dataParameter.ejercicio = document.forms[0].ejercicio.value;
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codProcedimiento = document.forms[0].codProcedimiento.value;
            dataParameter.dniPerCon = document.getElementById("dniPerCon").value;
            dataParameter.nomPerCon = document.getElementById("nomPerCon").value;
            dataParameter.apel1PerCon = document.getElementById("apel1PerCon").value;
            dataParameter.apel2PerCon = document.getElementById("apel2PerCon").value;
            dataParameter.fecNacPerCon = document.getElementById("fecNacPerCon").value;
            dataParameter.edadPerCon = document.getElementById("edadPerCon").value;
            dataParameter.sexoPerCont = document.getElementById("codSexoPerCont").value;
            dataParameter.muniPerCon = document.getElementById("codMuniPerCon").value;
            dataParameter.experiencia6M = document.getElementById("codExperiencia6M").value;
            dataParameter.desempleadoAnterior = document.getElementById("codDesempleadoAnterior").value;
            dataParameter.sistGarantiaJuve = document.getElementById("codSistGarantiaJuve").value;
            dataParameter.docCvInter = ""; //document.getElementById("docCvInter").value;
            dataParameter.docDemInter = ""; //document.getElementById("docDemInter").value;
            dataParameter.fechaDemInter = document.getElementById("fechaDemInter").value;
            dataParameter.fechaCvInter = document.getElementById("fechaCvInter").value;
            dataParameter.desempleado = document.getElementById("codDesempleado").value;
            dataParameter.fecIniContr = document.getElementById("fecIniContr").value;
            dataParameter.mujSubRepre = document.getElementById("codMujSubRepre").value;
            dataParameter.numCtaCotSS = document.getElementById("numCtaCotSS").value;
            dataParameter.dirCenTrab = document.getElementById("dirCenTrab").value;
            dataParameter.grupCotSS = document.getElementById("codGrupCotSS").value;
            dataParameter.jorPuestra = document.getElementById("codJorPuestra").value;
            dataParameter.modContra = document.getElementById("codModContra").value;
            dataParameter.titPuestra = document.getElementById("codTitPuestra").value;
            dataParameter.nomCodOcu = document.getElementById("codNomCodOcu").value;
            dataParameter.nomPuesTra = document.getElementById("nomPuesTra").value;
            dataParameter.fecReFinContr = document.getElementById("fecReFinContr").value;
            dataParameter.fecFinContr = document.getElementById("fecFinContr").value;
            dataParameter.salarioMin = document.getElementById("codSalarioMin").value;
            dataParameter.titulacionEspecifica = document.getElementById("titulacionEspecifica").value;
            dataParameter.cosConReal = document.getElementById("cosConReal").value;
            dataParameter.porcJorn = document.getElementById("porcJorn").value;
            dataParameter.duraContra = document.getElementById("codDuraContra").value;
            dataParameter.idiomaUsuario = idiomaUsuario;

            console.log("guardarDatosPersonaContratada dniPerCon=" + dataParameter.dniPerCon + ", nomPerCon=" + dataParameter.nomPerCon + ", apel1PerCon=" + dataParameter.apel1PerCon + ", apel2PerCon=" + dataParameter.apel2PerCon + ", fecNacPerCon=" +
                    dataParameter.fecNacPerCon + ", edadPerCon=" + dataParameter.edadPerCon + ", sexoPerCont=" + dataParameter.sexoPerCont + ", muniPerCon=" + dataParameter.muniPerCon + ", experiencia6M=" +
                    dataParameter.experiencia6M + ", desempleadoAnterior=" + dataParameter.desempleadoAnterior + ", sistGarantiaJuve=" + dataParameter.sistGarantiaJuve + ", docCvInter=" + dataParameter.docCvInter + ", docDemInter=" +
                    dataParameter.docDemInter + ", fechaCvInter=" + dataParameter.fechaCvInter + ", idiomaUsuario=" + dataParameter.idiomaUsuario +
                    ", fechaDemInter=" + dataParameter.fechaDemInter + ", desempleado=" + dataParameter.desempleado + ", fecIniContr=" + dataParameter.fecIniContr +
                    ", mujSubRepre=" + dataParameter.mujSubRepre + ", numCtaCotSS=" + dataParameter.numCtaCotSS + ", dirCenTrab=" + dataParameter.dirCenTrab +
                    ", grupCotSS=" + dataParameter.grupCotSS + ", jorPuestra=" + dataParameter.jorPuestra +
                    ", modContra=" + dataParameter.modContra + ", titPuestra=" + dataParameter.titPuestra + ", nomCodOcu=" + dataParameter.nomCodOcu +
                    ", nomPuesTra=" + dataParameter.nomPuesTra + ", fecReFinContr=" + dataParameter.fecReFinContr + ", fecFinContr=" + dataParameter.fecFinContr +
                    ", salarioMin=" + dataParameter.salarioMin + ", titulacionEspecifica=" + dataParameter.titulacionEspecifica + ", cosConReal=" + dataParameter.cosConReal +
                    ", porcJorn=" + dataParameter.porcJorn + ", duraContra=" + dataParameter.duraContra);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'guardarPersonaContratadaPuestoTrabajo';

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            console.log("guardarPersonaContratadaPuestoTrabajo");

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("guardarPersonaContratadaPuestoTrabajo response = " + response);
                    respuesta = JSON.parse(response);
                    console.log("guardarPersonaContratadaPuestoTrabajo respuesta = " + respuesta);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta == "0") {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.AddPersonaContratada")%>');
                            // recargarDatosExpediente(); // Los datos modificados sólo están en la nueva pestaña, por eso no hace falta recargar el expediente.
                        } else {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.AddPersonaContratada")%>');
                        }
                    } else {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.AddPersonaContratada")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.AddPersonaContratada")%>');
                },
                async: true
            });
        }

        function rellenaCombo(nombreLista, nombreCombo) {

//            console.log("rellenaCombo nombreLista=" + nombreLista + ", nombreCombo=" + nombreCombo);
            var listaJSON = JSON.parse($("#" + nombreLista).val());
            var variableCombo = new Combo(nombreCombo);
            //    variableCombo.change = hola;
            var lcodGA = new Array();
            var ldescGA = new Array();
            var descSeleccionada = "";

            if (listaJSON != null && listaJSON.length > 0) {
                listaJSON.forEach(function (entidad, index) {
                    lcodGA.push(entidad.codigo);
//                    console.log("rellenaCombo " + entidad.descripcion.includes("|"));
                    if (entidad.descripcion.includes("|")) {
                        if (idiomaUsuario == 1) {
                            ldescGA.push(entidad.descripcion.split("|")[0]);
                        } else if (idiomaUsuario == 4) {
                            ldescGA.push(entidad.descripcion.split("|")[1]);
                        }
                    } else
                        ldescGA.push(entidad.descripcion);
                    if (document.getElementById("cod" + nombreCombo).value == entidad.codigo) {
                        descSeleccionada = ldescGA[ldescGA.length - 1];
                    }
                });
                variableCombo.addItems(lcodGA, ldescGA);
                document.getElementById("desc" + nombreCombo).value = descSeleccionada;
            }
        }

        function update() {
            console.log("update resultado = " + document.getElementById("codENVIOEIKA").value);
        }

        function onlyNumberKey(evt, acum) {
            //        console.log ("onlyNumberKey " + acum + ", " + acum.indexOf(",") + ", " + acum.length);
            // Only ASCII character in that range allowed
            var ASCIICode = (evt.which) ? evt.which : evt.keyCode;
            if (ASCIICode >= 48 && ASCIICode <= 57) {
                if (acum.indexOf(",") == -1)
                    return true;
                else if (acum.indexOf(",") != -1 && acum.indexOf(",") >= acum.length - 2)
                    return true;
            } else if (ASCIICode == 44 && acum.indexOf(",") == -1)
                return true;

            return false;
        }

        function testValue(acum, txtId) {
            acum = acum.replace(",", ".");
            if (isNaN(acum)) {
                alert("Valor incorrecto: Debe de ser un número");
                document.getElementById(txtId).value = "";
                document.getElementById(txtId).focus();
            } else if (acum > maxValue) {
                alert("El número no puede ser superior a " + maxValue);
                acum /= 10;
                while (acum > maxValue)
                    acum /= 10;
                $("#" + txtId).val(Math.floor(acum));
            }
        }

        function recargarDatosExpediente() {
            pleaseWait('on');
            document.forms[0].opcion.value = "cargarPestTram";
            document.forms[0].target = "mainFrame";
            document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
        }

        function abreDocConURL(link) {
            window.open(link, "_blank");
        }
    </script>
</head>
<body>
    <!--<body>-->
    <input type="hidden" id="listaDesempleado" value=""/>
    <script>document.getElementById("listaDesempleado").value = JSON.stringify(<%=despDesempleadoGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaDesempleadoAnterior" value=""/>
    <script>document.getElementById("listaDesempleadoAnterior").value = JSON.stringify(<%=despDesempleadoAnteriorGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaExperiencia6M" value=""/>
    <script>document.getElementById("listaExperiencia6M").value = JSON.stringify(<%=despExperiencia6MGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaMuniPerCon" value=""/>
    <script>document.getElementById("listaMuniPerCon").value = JSON.stringify(<%=despMuniPerConGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaSexoPerCont" value=""/>
    <script>document.getElementById("listaSexoPerCont").value = JSON.stringify(<%=despSexoPerContGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaSistGarantiaJuve" value=""/>
    <script>document.getElementById("listaSistGarantiaJuve").value = JSON.stringify(<%=despSistGarantiaJuveGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaDuraContra" value=""/>
    <script>document.getElementById("listaDuraContra").value = JSON.stringify(<%=despDuraContraGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaGrupCotSS" value=""/>
    <script>document.getElementById("listaGrupCotSS").value = JSON.stringify(<%=despGrupCotSSGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaJorPuestra" value=""/>
    <script>document.getElementById("listaJorPuestra").value = JSON.stringify(<%=despJorPuestraGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaModContra" value=""/>
    <script>document.getElementById("listaModContra").value = JSON.stringify(<%=despModContraGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaMujSubRepre" value=""/>
    <script>document.getElementById("listaMujSubRepre").value = JSON.stringify(<%=despMujSubRepreGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaNomCodOcu" value=""/>
    <script>document.getElementById("listaNomCodOcu").value = JSON.stringify(<%=despNomCodOcuGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaSalarioMin" value=""/>
    <script>document.getElementById("listaSalarioMin").value = JSON.stringify(<%=despSalarioMinGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	

    <input type="hidden" id="listaTitPuestra" value=""/>
    <script>document.getElementById("listaTitPuestra").value = JSON.stringify(<%=despTitPuestraGA%>, function (key, value) {
            return value == null ? "" : value;
        });
    </script>	    
    <input type="hidden" id="urlBaseLlamadaM67" name="urlBaseLlamadaM67" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>   

    <div class="tab-page" id="tabDatosPersonaContratada" style="height:90%; width: 100%;">
        <h2 class="tab" id="pestanaDatosPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabDatosPersonaContratada"));</script>
        <div style="clear: both;">
            <br>
            <div class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                <span id="tituloVentanaPersonaContratada" style="width: 98%;"></span>
            </div>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 90%;">
                <form id="frmPersonaContratada">
                    <fieldset id="program" name="program">
                        <fieldset id="datosTrabajador" name="datosTrabajador">
                            <legend class="legendAzulPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.tituloAgrupacionDatosPersona")%></legend>
                            <div class="lineaFormulario">
                                <div id="divDniPerCon" name="divDniPerCon">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.dniPerCon")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="dniPerCon" name="dniPerCon" type="text" class="inputTexto" />
                                    </div>
                                </div>
                                <div id="divNomPerCon" name="divNomPerCon">    
                                    <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.nomPerCon")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="nomPerCon" name="nomPerCon" type="text" class="inputTexto" />
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div id="divApel1PerCon" name="divApel1PerCon">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.apel1PerCon")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="apel1PerCon" name="apel1PerCon" type="text" class="inputTexto" />
                                    </div>
                                </div>
                                <div id="divApel2PerCon" name="divApel2PerCon">
                                    <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.apel2PerCon")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="apel2PerCon" name="apel2PerCon" type="text" class="inputTexto" />
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div id="divFecNacPerCon" name="divFecNacPerCon">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.fecNacPerCon")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="fecNacPerCon" name="fecNacPerCon" type="date" class="inputTexto" />
                                    </div>
                                </div>
                                <div id="divEdadPerCon" name="divEdadPerCon">
                                    <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.edadPerCon")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="edadPerCon" name="edadPerCon" type="text" class="inputTexto" />
                                    </div>
                                </div>
                            </div>                                
                            <div class="lineaFormulario" id="divSexoPerCont" name="divSexoPerCont">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.sexoPerCont")%></div>
                                <div style="width: 300px;float: left;">
                                    <!--<input id="sexoPerCont" name="sexoPerCont" type="text" class="inputTexto" />-->
                                    <input type="text" name="codSexoPerCont" id="codSexoPerCont" size="5" class="inputTexto" value="">
                                    <input type="text" name="descSexoPerCont"  id="descSexoPerCont" size="65" class="inputTexto" style="width:100px" readonly="true" value="">
                                    <A href="" id="anchorSexoPerCont" name="anchorSexoPerCont">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSexoPerCont"
                                              name="botonSexoPerCont" style="cursor:hand;"></span>
                                    </A>		
                                </div>                        
                            </div>                                   
                            <div class="lineaFormulario" id="divMuniPerCon" name="divMuniPerCon">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.muniPerCon")%></div>
                                <div style="width: 400px;float: left;">
                                    <!--<input id="muniPerCon" name="muniPerCon" type="text" class="inputTexto" />-->
                                    <input type="text" name="codMuniPerCon" id="codMuniPerCon" size="5" class="inputTexto" value="">
                                    <input type="text" name="descMuniPerCon"  id="descMuniPerCon" size="65" class="inputTexto" style="width:300px" readonly="true" value="">
                                    <A href="" id="anchorMuniPerCon" name="anchorMuniPerCon">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMuniPerCon"
                                              name="botonMuniPerCon" style="cursor:hand;"></span>
                                    </A>	
                                </div>                        
                            </div>  
                            <div class="lineaFormulario">
                                <div id="divExperiencia6M" name="divExperiencia6M">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.experiencia6M")%></div>
                                    <div style="width: 400px;float: left;">
                                        <!--<input id="experiencia6M" name="experiencia6M" type="text" class="inputTexto" />-->
                                        <input type="text" name="codExperiencia6M" id="codExperiencia6M" size="5" class="inputTexto" value="">
                                        <input type="text" name="descExperiencia6M"  id="descExperiencia6M" size="65" class="inputTexto" style="width:50px" readonly="true" value="">
                                        <A href="" id="anchorExperiencia6M" name="anchorExperiencia6M">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonExperiencia6M"
                                                  name="botonExperiencia6M" style="cursor:hand;"></span>
                                        </A>                                          
                                    </div>
                                </div> 
                                <div id="divDesempleadoAnterior" name="divDesempleadoAnterior">
                                    <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.desempleadoAnterior")%></div>
                                    <div style="width: 400px;float: left;">
                                        <!--<input id="desempleadoAnterior" name="desempleadoAnterior" type="text" class="inputTexto" />--> 
                                        <input type="text" name="codDesempleadoAnterior" id="codDesempleadoAnterior" size="5" class="inputTexto" value="">
                                        <input type="text" name="descDesempleadoAnterior"  id="descDesempleadoAnterior" size="65" class="inputTexto" style="width:50px" readonly="true" value="">
                                        <A href="" id="anchorDesempleadoAnterior" name="anchorDesempleadoAnterior">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDesempleadoAnterior"
                                                  name="botonDesempleadoAnterior" style="cursor:hand;"></span>
                                        </A>                                           
                                    </div>   
                                </div>
                            </div>                                  
                            <div class="lineaFormulario">
                                <div id="divSistGarantiaJuve" name="divSistGarantiaJuve">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.sistGarantiaJuve")%></div>
                                    <div style="width: 400px;float: left;">
                                        <!--<input id="sistGarantiaJuve" name="sistGarantiaJuve" type="text" class="inputTexto" />-->
                                        <input type="text" name="codSistGarantiaJuve" id="codSistGarantiaJuve" size="5" class="inputTexto" value="">
                                        <input type="text" name="descSistGarantiaJuve"  id="descSistGarantiaJuve" size="65" class="inputTexto" style="width:50px" readonly="true" value="">
                                        <A href="" id="anchorSistGarantiaJuve" name="anchorSistGarantiaJuve">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSistGarantiaJuve"
                                                  name="botonSistGarantiaJuve" style="cursor:hand;"></span>
                                        </A>  
                                    </div>
                                </div>
                                <div id="divDesempleado" name="divDesempleado">
                                    <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.desempleado")%></div>
                                    <div style="width: 300px;float: left;">
                                        <!--<input id="desempleado" name="desempleado" type="text" class="inputTexto" />-->
                                        <input type="text" name="codDesempleado" id="codDesempleado" size="5" class="inputTexto" value="">
                                        <input type="text" name="descDesempleado"  id="descDesempleado" size="65" class="inputTexto" style="width:50px" readonly="true" value="">
                                        <A href="" id="anchorDesempleado" name="anchorDesempleado">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDesempleado"
                                                  name="botonDesempleado" style="cursor:hand;"></span>
                                        </A>                                        
                                    </div>
                                </div>
                            </div> 
                            <div class="lineaFormulario" id="divDocCvInter" name="divDocCvInter">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.docCvInter")%></div>
                                <div style="width: 300px;float: left;">
                                <!--   <a href="javascript:obtainDocumentFile('<%=docCvInter != null ? docCvInter : ""%>', 'DOCCVINTER')"><%=docCvInter != null ? docCvInter : ""%></a>   -->                                 
                                    <a href="javascript:abreDocConURL('<%=docCvInter != null ? docCvInter : ""%>')"><%=docCvInter != null ? docCvInter : ""%></a>
                                </div>                        
                            </div>                                
                            <div class="lineaFormulario" id="divFechaCvInter" name="divFechaCvInter">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.fechaCvInter")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="fechaCvInter" name="fechaCvInter" type="date" class="inputTexto" />
                                </div>                        
                            </div> 
                            <div class="lineaFormulario" id="divDocDemInter" name="divDocDemInter">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.docDemInter")%></div>
                                <div style="width: 300px;float: left;">
                                    <!--<input id="docDemInter" name="docDemInter" type="text" class="inputTexto" />-->
                                   <!-- <a href="javascript:obtainDocumentFile('<%=docDemInter != null ? docDemInter : ""%>', 'DOCDEMINTER')"><%=docDemInter != null ? docDemInter : ""%></a>  -->          
                                    <a href="javascript:abreDocConURL('<%=docDemInter != null ? docDemInter : ""%>')"><%=docDemInter != null ? docDemInter : ""%></a>
                                </div>
                                <div class="lineaFormulario" id="divFechaDemInter" name="divFechaDemInter">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.fechaDemInter")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="fechaDemInter" name="fechaDemInter" type="date" class="inputTexto" />
                                    </div>                        
                                </div>                                 
                        </fieldset>
                        <br>
                        <fieldset id="datosPuestoTrabajo" name="datosPuestoTrabajo">
                            <legend class="legendAzulPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.tituloAgrupacionPuestoTrabajo")%></legend>
                            <div class="lineaFormulario" id="divNomPuesTra" name="divNomPuestTra">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.nomPuesTra")%></div>
                                <div style="width: 700px;float: left;">
                                    <input id="nomPuesTra" name="nomPuesTra" type="text" class="inputTexto" />
                                </div>                            
                            </div>                             
                            <div class="lineaFormulario" id="divNomCodOcu" name="divNomCodOcu">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.nomCodOcu")%></div>
                                <div style="width: 700px;float: left;">
                                    <!--<input id="nomCodOcu" name="nomCodOcu" type="text" class="inputTextoReadOnly" // style="width: 225px;" />-->
                                    <input type="text" name="codNomCodOcu" id="codNomCodOcu" size="5" class="inputTexto" value="">                                    
                                    <input type="text" name="descNomCodOcu"  id="descNomCodOcu" size="65" class="inputTexto" style="width:400px" readonly="true" value="">
                                    <A href="" id="anchorNomCodOcu" name="anchorNomCodOcu">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNomCodOcu"
                                              name="botonNomCodOcu" style="cursor:hand;"></span>
                                    </A>                                       
                                </div>
                            </div> 
                            <div class="lineaFormulario" id="divTitPuestra" name="divTitPuestra">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.titPuestra")%></div>
                                <div style="width: 900px;float: left;">
                                    <!--<input id="titPuestra" name="titPuestra" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codTitPuestra" id="codTitPuestra" size="5" class="inputTexto" value="">
                                    <input type="text" name="descTitPuestra"  id="descTitPuestra" size="65" class="inputTexto" style="width:700px" readonly="true" value="">
                                    <A href="" id="anchorTitPuestra" name="anchorTitPuestra">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTitPuestra"
                                              name="botonTitPuestra" style="cursor:hand;"></span>
                                    </A>                                       
                                </div>
                            </div>                                 
                            <div class="lineaFormulario" id="divTitulacionEspecifica" name="divTitulacionEspecifica">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.titulacionEspecifica")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="titulacionEspecifica" name="titulacionEspecifica" type="text" class="inputTexto" style="width: 225px;" />
                                </div>
                            </div>                                  
                            <div class="lineaFormulario" id="divModContra" name="divModContra">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.modContra")%></div>
                                <div style="width: 700px;float: left;">
                                    <!--<input id="modContra" name="modContra" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codModContra" id="codModContra" size="5" class="inputTexto" value="">
                                    <input type="text" name="descModContra"  id="descModContra" size="65" class="inputTexto" style="width:400px" readonly="true" value="">
                                    <A href="" id="anchorModContra" name="anchorModContra">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonModContra"
                                              name="botonModContra" style="cursor:hand;"></span>
                                    </A>                                      
                                </div>
                            </div>
                            <div class="lineaFormulario" id="divDuraContra" name="divDuraContra">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.duraContra")%></div>
                                <div style="width: 700px;float: left;">
                                    <!--<input id="duraContra" name="duraContra" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codDuraContra" id="codDuraContra" size="5" class="inputTexto" value="">
                                    <input type="text" name="descDuraContra"  id="descDuraContra" size="65" class="inputTexto" style="width:300px" readonly="true" value="">
                                    <A href="" id="anchorDuraContra" name="anchorDuraContra">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDuraContra"
                                              name="botonDuraContra" style="cursor:hand;"></span>
                                    </A>                                      
                                </div>
                            </div>                                
                            <div class="lineaFormulario">
                                <div id="divJorPuestra" name="divJorPuestra">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.jorPuestra")%></div>
                                    <div style="width: 300px;float: left;">
                                        <!--<input id="jorPuestra" name="jorPuestra" type="text" class="inputTexto" style="width: 225px;" />-->
                                        <input type="text" name="codJorPuestra" id="codJorPuestra" size="5" class="inputTexto" value="">
                                        <input type="text" name="descJorPuestra"  id="descJorPuestra" size="65" class="inputTexto" style="width:200px" readonly="true" value="">
                                        <A href="" id="anchorJorPuestra" name="anchorJorPuestra">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonJorPuestra"
                                                  name="botonJorPuestra" style="cursor:hand;"></span>
                                        </A>                                            
                                    </div>
                                </div>
                                <div id="divPorcJorn" name="divPorcJorn">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.porcJorn")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="porcJorn" name="porcJorn" type="text" class="inputTexto" style="width: 225px;" />
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div id="divFecIniContr" name="divFecIniContr">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.fecIniContr")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="fecIniContr" name="fecIniContr" type="date" class="inputTexto" style="width: 225px;" />
                                    </div>
                                </div>
                                <div id="divFecFinContr" name="divFecFinContr">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.fecFinContr")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="fecFinContr" name="fecFinContr" type="date" class="inputTexto" style="width: 225px;" />
                                    </div>
                                </div>
                            </div>   
                            <div class="lineaFormulario" id="divFecReFinContr" name="divFecReFinContr">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.fecReFinContr")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="fecReFinContr" name="fecReFinContr" type="date" class="inputTexto" style="width: 225px;" />
                                </div>
                            </div>
                            <div class="lineaFormulario" id="divDirCenTrab" name="divDirCenTrab">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.dirCenTrab")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="dirCenTrab" name="dirCenTrab" type="text" class="inputTexto" style="width: 225px;" />
                                </div>
                            </div>                                  
                            <div class="lineaFormulario" id="divGrupCotSS" name="divGrupCotSS">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.grupCotSS")%></div>
                                <div style="width: 900px;float: left;">
                                    <!--<input id="grupCotSS" name="grupCotSS" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codGrupCotSS" id="codGrupCotSS" size="5" class="inputTexto" value="">
                                    <input type="text" name="descGrupCotSS"  id="descGrupCotSS" size="65" class="inputTexto" style="width:700px" readonly="true" value="">
                                    <A href="" id="anchorGrupCotSS" name="anchorGrupCotSS">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupCotSS"
                                              name="botonGrupCotSS" style="cursor:hand;"></span>
                                    </A>                                      
                                </div>
                            </div>                                
                            <div class="lineaFormulario">
                                <div id="divCosConReal" name="divCosConReal">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.cosConReal")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="cosConReal" name="cosConReal" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntosLak(this);"
                                        onkeyup="return soloNumerosComa(this);"
                                        value="<%=personaPuestoVO.getCosConReal() != null && personaPuestoVO.getCosConReal().getValor() != null ? personaPuestoVO.getCosConReal().getValor().replaceAll("\\.", ","): ""%>"/>                                        
                                    </div>
                                </div>                            
                                <div id="divNumCtaCotSS" name="divNumCtaCotSS">
                                    <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.numCtaCotSS")%></div>
                                    <div style="width: 300px;float: left;">
                                        <input id="numCtaCotSS" name="numCtaCotSS" type="text" class="inputTexto" style="width: 225px;" />
                                    </div>
                                </div>
                            </div>                                
                            <div class="lineaFormulario" id="divSalarioMin" name="divSalarioMin">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.salarioMin")%></div>
                                <div style="width: 900px;float: left;">
                                    <!--<input id="salarioMin" name="salarioMin" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codSalarioMin" id="codSalarioMin" size="5" class="inputTexto" value="">
                                    <input type="text" name="descSalarioMin"  id="descSalarioMin" size="65" class="inputTexto" style="width:700px" readonly="true" value="">
                                    <A href="" id="anchorSalarioMin" name="anchorSalarioMin">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSalarioMin"
                                              name="botonSalarioMin" style="cursor:hand;"></span>
                                    </A>                                      
                                </div>
                            </div>                                 
                            <div class="lineaFormulario" id="divMujSubRepre" name="divMujSubRepre">
                                <div class="etiquetaPasikus"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.personaContradaPuestoTrabajo.mujSubRepre")%></div>
                                <div style="width: 300px;float: left;">
                                    <!--<input id="mujSubRepre" name="mujSubRepre" type="text" class="inputTextoReadOnly" style="width: 225px;" />-->
                                    <input type="text" name="codMujSubRepre" id="codMujSubRepre" size="5" class="inputTexto" value="">
                                    <input type="text" name="descMujSubRepre"  id="descMujSubRepre" size="65" class="inputTexto" style="width:50px" readonly="true" value="">
                                    <A href="" id="anchorMujSubRepre" name="anchorMujSubRepre">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMujSubRepre"
                                              name="botonMujSubRepre" style="cursor:hand;"></span>
                                    </A>                                       
                                </div>
                            </div>                                    
                        </fieldset>
                        <br>
                        <fieldset id="grabarDatos" name="grabarDatos">
                            <div class="lineaFormulario">
                                <div style="margin:auto;width:20%">
                                    <input type="button" id="botonGrabarDatos" name="botonGrabarDatos" style="margin:auto;" value='<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.personaContradaPuestoTrabajo.botonGrabarDatos")%>' onclick="guardarDatosPersonaContratada();" ></input>
                                </div> 
                            </div>
                        </fieldset>
                    </fieldset> 
                </form>
            </div>
        </div>
        <!-- Es la capa m?s externa de la pesta?a -->
    </div>
</body>
<script type="text/javascript">
    cargarDatos();
    ///// Necesario para los combos
    rellenaCombo("listaDesempleado", "Desempleado");
    rellenaCombo("listaDesempleadoAnterior", "DesempleadoAnterior");
    rellenaCombo("listaExperiencia6M", "Experiencia6M");
    rellenaCombo("listaMuniPerCon", "MuniPerCon");
    rellenaCombo("listaSexoPerCont", "SexoPerCont");
    rellenaCombo("listaSistGarantiaJuve", "SistGarantiaJuve");

    rellenaCombo("listaDuraContra", "DuraContra");
    rellenaCombo("listaGrupCotSS", "GrupCotSS");
    rellenaCombo("listaJorPuestra", "JorPuestra");
    rellenaCombo("listaModContra", "ModContra");
    rellenaCombo("listaMujSubRepre", "MujSubRepre");
    rellenaCombo("listaNomCodOcu", "NomCodOcu");
    rellenaCombo("listaSalarioMin", "SalarioMin");
    rellenaCombo("listaTitPuestra", "TitPuestra");
    ///// Fin de los combos

    document.getElementById('tituloVentanaPersonaContratada').innerHTML = titleWindowPersonaContratada;

    function obtainDocumentFile(nameFile, codCampo) {
//            console.log("obtainDocumentFile nameFile = " + nameFile);
//            console.log("obtainDocumentFile #urlBaseLlamadaM82 = " + $("#urlBaseLlamadaM82").val());
        var parametrosLLamadaMelanbide67 = {
            tarea: 'preparar'
            , modulo: 'MELANBIDE67'
            , operacion: null
            , tipo: 0
            , numero: null
        };
        var dataParameter = $.extend({}
        , parametrosLLamadaMelanbide67);
        dataParameter.numero = document.forms[0].numero.value;
        dataParameter.nameFile = nameFile;
        dataParameter.ejercicio = document.forms[0].ejercicio.value;
        dataParameter.codCampo = codCampo;
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'obtainDocumentFile';
        window.open($("#urlBaseLlamadaM67").val() + "?" + $.param(dataParameter), "_blank");
    }

</script>

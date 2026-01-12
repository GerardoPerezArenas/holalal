<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.RegistroVidaLaboralVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.Date" %>


<%
    int idiomaUsuario = 1;
    int apl = 5; // Pendiente de mirar
    int codOrganizacion = 0;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
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
    
    List<RegistroVidaLaboralVO> registrosVidaLaboral = new ArrayList<RegistroVidaLaboralVO>();
    if (request.getAttribute("listaRegistrosWS") != null) {
        registrosVidaLaboral = (List<RegistroVidaLaboralVO>)request.getAttribute("listaRegistrosWS");
    }
    else {
        registrosVidaLaboral = (List<RegistroVidaLaboralVO>)request.getAttribute("listaRegistrosBD");    
    }
    
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

<script type="text/javascript">

    function obtenerDatosVidaLaboralIntermediacion() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM67);

        dataParameter.numero = document.forms[0].numero.value; // Número de expediente
        dataParameter.fechaDesdeCVL = $("#fechaDesde").val();
        dataParameter.fechaHastaCVL = $("#fechaHasta").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'consultaVidaLaboralCVLBatch';

        var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

        console.log("obtenerDatosVidaLaboralIntermediacion dataParameter.numero=" + dataParameter.numero +
                ", dataParameter.control=" + dataParameter.control +
                ", dataParameter.operacion=" + dataParameter.operacion +
                ", dataParameter.fechaDesdeCVL=" + dataParameter.fechaDesdeCVL +
                ", dataParameter.fechaHastaCVL=" + dataParameter.fechaHastaCVL);

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (respuesta) {
                console.log("consultaVidaLaboralCVLBatch respuesta = " + respuesta);
                if (respuesta !== null) {
                    pleaseWait('off');
                    if (respuesta == "0257") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceDatabiggerToday")%>');
                    }
                    else if (respuesta == "0252") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceBeginDataBefore5Years")%>');
                    }
                    else if (respuesta == "0231") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceIncorrectDocument")%>');
                    }                    
                    else if (respuesta == "0254") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceLittleInformacion")%>');
                    }                    
                    else if (respuesta == "-2") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceNotAvailable")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceNotAvailable")%>');
                    pleaseWait('off');
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceNotAvailable")%>');
                pleaseWait('off');
            },
            async: true
        });
    }
</script>

<body>
    <!--<body>-->
    <div class="tab-page" id="tabTablaVidaLaboral" style="height:90%; width: 100%;">
        <h2 class="tab" id="pestanaVidaLaboral"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabTablaVidaLaboral"));</script>
        <div style="clear: both;">
            <label class="legendAzul" style="text-align: center;"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.vidalaboral.tituloPestana")%></label>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 90%;">     <!--onscroll="deshabilitarRadios();"-->
                <div id="listaLaboral" style="padding: 5px; width: 98%; height: 50%; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
            </div>
        </div>
        <!-- Es la capa más externa de la pestaña -->
        <form  id="formBusqueda">
            <div style="clear: both;">
                <div style="text-align: left; margin-left: 50px; margin-top: 12px" class="etiqueta">
                    <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.fechaDesde")%></label>
                    <input type="date" id="fechaDesde" name="fechaDesde" />
                    <script>
                        var ahora = new Date();
                        var hace1Anho = ahora.setFullYear(ahora.getFullYear() - 1);
                        document.getElementById('fechaDesde').valueAsDate = new Date(hace1Anho);
                    </script>                        
                    <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.fechaHasta")%></label>
                    <input type="date" id="fechaHasta" name="fechaHasta" />      
                    <script>
                        document.getElementById('fechaHasta').valueAsDate = new Date();
                    </script>
                </div>
            </div> 

            <div style="clear: both;">
                <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.entidadColaboradora.titulo")%></label>
                </br>
                <div style="text-align: left; margin-left: 50px" class="etiqueta">
                    <input type="button" id="botonDatosVidaLaboralIntermediacion" name="botonDatosVidaLaboralIntermediacion" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosVidaLaboralIntermediacion")%>" onclick="obtenerDatosVidaLaboralIntermediacion();" >
                    </input>
                </div>
            </div>  
        </form>                
    </div>
    <script type="text/javascript">
        //Tabla vidaLaboral
        var tabLaboral;
        var listaLaboral = new Array();
        var listaLaboralTabla = new Array();


        tabLaboral = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaLaboral'), 820);
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col1")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col2")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col3")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col4")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col5")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col6")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col7")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col8")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col9")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col10")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col11")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col12")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col13")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col14")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col15")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col16")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbide67I18n.getMensaje(idiomaUsuario,"label.vidalaboral.col17")%>");
        tabLaboral.displayCabecera = true;
        tabLaboral.height = 100;

        <%  		
            RegistroVidaLaboralVO registroVO = null;											
            if (registrosVidaLaboral!= null && registrosVidaLaboral.size() >0){
                for (int indice=0 ; indice < registrosVidaLaboral.size() ; indice++)
                {
                    registroVO = registrosVidaLaboral.get(indice);

        %>
        <% String tipoDocumentacion = registroVO.getTipoDocumentacion(); %>
        var tipoDocumentacion = '<%=tipoDocumentacion.replace("null", "")%>';
        <% String documentacion = registroVO.getDocumentacion(); %>
        var documentacion = '<%=documentacion.replace("null", "")%>';
        <% Date fechaDesde = registroVO.getFechaDesde(); %>
        var fechaDesde = '<%=fechaDesde != null ? fechaDesde : ""%>';
        <% Date fechaHasta = registroVO.getFechaHasta(); %>
        var fechaHasta = '<%=fechaHasta != null ? fechaHasta : ""%>';
        <% String numeroAfiliacionL = registroVO.getNumeroAfiliacionL(); %>
        var numeroAfiliacionL = '<%=numeroAfiliacionL.replace("null", "")%>';
        <% Date fechaNacimiento = registroVO.getFechaNacimiento(); %>
        var fechaNacimiento = '<%=fechaNacimiento != null ? fechaNacimiento : ""%>';
        var resumenConplTotalDiasAlta = '<%=registroVO.getResumenConplTotalDiasAlta()%>';
        <% String regimen = registroVO.getRegimen(); %>
        var regimen = '<%=regimen.replace("null", "")%>';
        <% String codCuentaCot = registroVO.getCodCuentaCot(); %>
        var codCuentaCot = '<%=codCuentaCot.replace("null", "")%>';
        <% String provincia = registroVO.getProvincia(); %>
        var provincia = '<%=provincia.replace("null", "")%>';
        <% Date fechaAlta = registroVO.getFechaAlta(); %>
        var fechaAlta = '<%=fechaAlta != null ? fechaAlta : ""%>';
        <% Date fechaEfectos = registroVO.getFechaEfectos(); %>
        var fechaEfectos = '<%=fechaEfectos != null ? fechaEfectos : ""%>';
        <% Date fechaBaja = registroVO.getFechaBaja(); %>
        var fechaBaja = '<%=fechaBaja != null ? fechaBaja : ""%>';
        <% String contratoTrabajo = registroVO.getContratoTrabajo(); %>
        var contratoTrabajo = '<%=contratoTrabajo.replace("null", "")%>';
        <% String contratoTParcial = registroVO.getContratoTParcial(); %>
        var contratoTParcial = '<%=contratoTParcial.replace("null", "")%>';
        <% String grupoCotizacion = registroVO.getGrupoCotizacion(); %>
        var grupoCotizacion = '<%=grupoCotizacion.replace("null", "")%>';
        var diasAlta = '<%=registroVO.getDiasAlta()%>';

        listaLaboralTabla[<%=indice%>] = [tipoDocumentacion, documentacion, fechaDesde, fechaHasta,
            numeroAfiliacionL, fechaNacimiento, resumenConplTotalDiasAlta, regimen, codCuentaCot,
            provincia, fechaAlta, fechaEfectos, fechaBaja, contratoTrabajo, contratoTParcial,
            grupoCotizacion, diasAlta];

        <%
                }// for
            }// if
        %>

        tabLaboral.lineas = listaLaboralTabla;
        tabLaboral.displayTabla();
    </script> 
</body>

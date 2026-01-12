<%-- 
    Document   : justificaInsercion23
    Created on : 02-ene-2025, 13:55:08
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    
    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }
    
//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente = request.getParameter("numero");
    String totalInserciones = (String)request.getAttribute("totalInserciones");

    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String barraSeparadoraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide35.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide35.FICHERO_PROPIEDADES);
    
  //  totalInserciones = formateador.format(totalInserciones);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
<script type="text/javascript">
    var totalInserciones = <%=totalInserciones%>;

    function pulsarNuevoInsercionJustificacionEca() {
        lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoJusInsercion23&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaJusInserciones(result[1]);
                }
            }
        });
    }
    function pulsarModificarInsercionJustificacionEca() {
        alert("HOLA");
    }
    function pulsarEliminarInsercionJustificacionEca() {}

    function crearTablaJusInserciones() {
        tablaJusInserciones = new FixedColumnTable(document.getElementById('listaJusInserciones'), 1600, 1650, 'listaJusInserciones');

        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dni")%>'); // dni
        tablaJusInserciones.addColumna('240', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nombreAp")%>'); // nombre + apellidos
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.sexo")%>'); // sexo
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fNacimiento")%>'); // fNacimiento
        tablaJusInserciones.addColumna('100', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoDisc")%>'); // tipoDisc
        tablaJusInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.grado")%>'); // grado
        tablaJusInserciones.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.colectivo")%>'); // colectivo
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoContrato")%>'); // tipoContrato
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>'); // jornada
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fInicio")%>'); // fInicio
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fFin")%>'); // fFin
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoEdadSexo")%>'); // tipoEdadSexo
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dias")%>'); // dias
        tablaJusInserciones.addColumna('40', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.edad")%>'); // edad
        tablaJusInserciones.addColumna('200', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.empresa")%>'); // empresa
        tablaJusInserciones.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nifEmpresa")%>'); // nif empresa
        tablaJusInserciones.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.cnae")%>'); // cnae
        tablaJusInserciones.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dniPreparador")%>'); //
        tablaJusInserciones.addColumna('100', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>'); //
        //                             1590
        for (var cont = 0; cont < listaJusInsercionesTabla.length; cont++) {
            tablaJusInserciones.addFila(listaJusInsercionesTabla[cont], listaJusInsercionesTitulos[cont]);
        }
        tablaJusInserciones.displayCabecera = true;
        tablaJusInserciones.height = 400;
        //      tablaJusInserciones.lineas = listaJusInsercionesTabla;
//        tablaJusInserciones.displayTablaConTooltips(listaJusInsercionesTitulos);
        tablaJusInserciones.numColumnasFijas = 2;
        tablaJusInserciones.altoCabecera = 50;
        tablaJusInserciones.dblClkFunction = 'dblClckTablaPreparadoresJustificacion';
        tablaJusInserciones.displayTabla();
        tablaJusInserciones.pack();
    }
    function recargarTablaJusInserciones() {}

    function dblClckTablaPreparadoresJustificacion(rowID, tableName) {
        pulsarModificarInsercionJustificacionEca();
    }

</script>
<body>
    <div class="tab-page" id="tabPage3522" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloJusInsercion"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.inserciones.tituloPagina")%></span>
        </div>     
        <fieldset id="insercionesJus23" name="insercionesJus23">          
            <div>
                <div id="listaJusInserciones" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            </div>
            <div class="botonera" style="text-align: center; margin-top: 15px; visibility: hidden">
                <input  type="button" id="btnNuevoJusInsercion" name="btnNuevoJusInsercion" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.inserciones.nuevo")%>" onclick="pulsarNuevoInsercionJustificacionEca();">
                <input  type="button" id="btnBorrarJusInsercion" name="btnBorrarJusInsercion" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.inserciones.eliminar")%>" onclick="pulsarEliminarInsercionJustificacionEca();">         
            </div>
        </fieldset>
    </div>
    <script  type="text/javascript">
        var tablaJusInserciones;
        var listaJusInserciones = new Array();
        var listaJusInsercionesTabla = new Array();
        var listaJusInsercionesTitulos = new Array();
        <%
            JusInsercionesECA23VO insercion = null;
            List<JusInsercionesECA23VO> ListaInser = null;
            if(request.getAttribute("listaInsercionesJustificacion")!=null) {
                ListaInser = (List<JusInsercionesECA23VO>)request.getAttribute("listaInsercionesJustificacion");
            }
            
            if (ListaInser!= null && ListaInser.size() > 0) {                
                for (int indice=0;indice<ListaInser.size();indice++) {
                    insercion = ListaInser.get(indice);
                    
                    String ape2 = "";
                    if (insercion.getApellido2() != null) {
                        ape2 =insercion.getApellido2();
                    }

                    String fNacimiento = "";
                    if(insercion.getfNacimiento()!=null){
                        fNacimiento=dateFormat.format(insercion.getfNacimiento());
                    }
                                                          
                    String grado = "-";
                    if (insercion.getGrado() != null) {
                        grado = formateador.format(insercion.getGrado());
                    }
                    
                    String jornada = "-";
                    if (insercion.getJornada() != null) {
                        jornada = formateador.format(insercion.getJornada());
                    }
                    
                    String fInicio = "-";
                    if(insercion.getfInicio()!=null){
                        fInicio=dateFormat.format(insercion.getfInicio());
                    }
                    
                    String fFin = "-";
                    if(insercion.getfFin()!=null){
                        fFin=dateFormat.format(insercion.getfFin());
                    }
                    
                    String descCnae = "-";
                    if(idiomaUsuario==4) {
                        descCnae = insercion.getDescCnaeE();
                    } else {
                        descCnae = insercion.getDescCnaeC();
                    }
                    
                    String dias = "";
                    if (insercion.getDias() != null) {
                        dias = Integer.toString(insercion.getDias());
                    }
                    
                    String edad = "";
                    if (insercion.getEdad() != null) {
                        edad = Integer.toString(insercion.getEdad());
                    }
                    
                    String importe = "";
                    if (insercion.getImporteInser() != null) {
                        importe = formateador.format(insercion.getImporteInser());
                    }
        %>
        listaJusInserciones[<%=indice%>] = ['<%=insercion.getId()%>', '<%=insercion.getDni()%>', '<%=insercion.getNombre()%>', '<%=insercion.getApellido1()%>', '<%=ape2%>', '<%=insercion.getSexo()%>', '<%=insercion.getDescSexo()%>',
            '<%=fNacimiento%>', '<%=insercion.getTipoDisc()%>', '<%=insercion.getDescTipoDisc()%>', '<%=grado%>', '<%=insercion.getColectivo()%>', '<%=insercion.getDescColectivo()%>', '<%=insercion.getTipoContrato()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=insercion.getTipoEdadSexo()%>', '<%=insercion.getDescTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=insercion.getCnae()%>', '<%=descCnae%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>'];
        listaJusInsercionesTabla[<%=indice%>] = ['<%=insercion.getDni()%>', '<%=insercion.getNombre()%>' + ' ' + '<%=insercion.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=insercion.getDescSexo()%>',
            '<%=fNacimiento%>', '<%=insercion.getTipoDisc()%>', '<%=grado%>', '<%=insercion.getColectivo()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=insercion.getTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=insercion.getCnae()%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>' + ' \u20ac']; // 19 columnas
        listaJusInsercionesTitulos[<%=indice%>] = ['<%=insercion.getDni()%>', '<%=insercion.getNombre()%>' + ' ' + '<%=insercion.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=insercion.getDescSexo()%>',
            '<%=fNacimiento%>', '<%=insercion.getDescTipoDisc()%>', '<%=grado%>', '<%=insercion.getDescColectivo()%>', '<%=insercion.getDescTipoContrato()%>',
            '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=insercion.getDescTipoEdadSexo()%>', '<%=dias%>', '<%=edad%>', '<%=insercion.getEmpresa()%>', '<%=insercion.getNifEmpresa()%>',
            '<%=descCnae%>', '<%=insercion.getNifPreparador()%>', '<%=importe%>' + ' \u20ac']; // 19 columnas
        <%
                }
            }
        %>
        crearTablaJusInserciones();
    </script>
</body>
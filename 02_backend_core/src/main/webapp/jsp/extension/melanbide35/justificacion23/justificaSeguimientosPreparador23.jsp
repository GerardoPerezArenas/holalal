<%-- 
    Document   : justificaSeguimientosPreparador23
    Created on : 03-feb-2025, 16:00:03
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusSeguimientosECA23VO"%>
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
<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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
            int numSeguimientosPreparador = (Integer)request.getAttribute("numSeguimientosPreparador");
            String dniPreparador = (String)request.getAttribute("dniPreparador");
            String totalSegPreparadorJus  = (String)request.getAttribute("totalSegPreparadorJus");
            Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");

            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var numSegPreparador = <%=numSeguimientosPreparador%>;
            var dniPreparador = '<%=dniPreparador%>';
            var totSeguimientos = '<%=totalSegPreparadorJus%>';

            var maxSeguimientosJ;

            function crearTablaJusSeguimientos() {
                tablaJusSeguimientos = new FixedColumnTable(document.getElementById('listaJusSegumientos'), 1400, 1450, 'listaJusSegumientos');

                tablaJusSeguimientos.addColumna('65', 'center', "<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.tabla.dni")%>");// dni
                tablaJusSeguimientos.addColumna('240', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nombreAp")%>'); // nombre + apellidos
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.sexo")%>'); // sexo
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fNacimiento")%>'); // fNacimiento
                tablaJusSeguimientos.addColumna('100', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoDisc")%>'); // tipoDisc
                tablaJusSeguimientos.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.grado")%>'); // grado
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.colectivo")%>'); // colectivo
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoContrato")%>'); // tipoContrato
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>'); // jornada
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fInicio")%>'); // fInicio
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.fFin")%>'); // fFin
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.tipoEdadSexo")%>'); // tipoEdadSexo
                tablaJusSeguimientos.addColumna('200', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.empresa")%>'); // empresa
                tablaJusSeguimientos.addColumna('80', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.nifEmpresa")%>'); // nif empresa
                tablaJusSeguimientos.addColumna('65', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.cnae")%>'); // cnae
                tablaJusSeguimientos.addColumna('100', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>'); //
                //                             1355
                for (var cont = 0; cont < listaJusSeguimientosTabla.length; cont++) {
                    tablaJusSeguimientos.addFila(listaJusSeguimientosTabla[cont], listaJusSeguimientosTitulos[cont]);
                }
                tablaJusSeguimientos.displayCabecera = true;
                tablaJusSeguimientos.height = 300;
                tablaJusSeguimientos.lineas = listaJusSeguimientosTabla;
                tablaJusSeguimientos.numColumnasFijas = 2;
                tablaJusSeguimientos.altoCabecera = 50;
                tablaJusSeguimientos.dblClkFunction = 'dblClckTablaSeguimientosdJustificacion';
                tablaJusSeguimientos.displayTabla();
                tablaJusSeguimientos.pack();
            }

            function dblClckTablaSeguimientosdJustificacion(rowID, tableName) {
                pulsarModificarSeguimientoJustificacionEca();
            }
            function pulsarNuevoSeguimientoJustificacionEca() {}
            function pulsarEliminarSeguimientoJustificacionEca() {
                if (tablaJusSeguimientos.selectedIndex != -1) {
                } else {
                    jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarModificarSeguimientoJustificacionEca() {
                if (tablaJusSeguimientos.selectedIndex != -1) {
                } else {
                    jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage3523-1" style="height:520px; width: 100%;">  
            <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
                <span id="subtituloJusSegumientos"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.seguimientos.tituloPagina")%></span>
            </div>
            <fieldset id="segumientosPrepJus23" name="segumientosPrepJus23">
                <legend class="legendAzul" id="pestanaJusSegPrep" style="text-transform: uppercase;text-align: center;"></legend>
                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 30%; float: left; padding-left: 17%;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.numSeguimientos")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="numSegPrepECA23" name="numSegPrepECA23" size="3" maxlength="3" style="text-align: center;" value="" class="inputTexto total"/>
                        </div>
                    </div> 
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.tabla.importeJus")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="totSegPreparadorJus" name="totSegPreparadorJus" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div> 
                </div>


                <div>
                    <div id="listaJusSegumientos" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                </div>
                <div class="botonera" style="text-align: center; margin-top: 15px; visibility: hidden">
                </div>
            </fieldset>
            <div class="botonera" style="width: 100%; float: left;margin-top: 15px; text-align: center;">               
                <input type="button" id="btnVolver" name="btnVolver" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();">
            </div>
        </div>
        <script type="text/javascript">
            document.getElementById('pestanaJusSegPrep').innerHTML = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.preparadores.titulo")%>' + ' - ' + dniPreparador;
            document.getElementById('numSegPrepECA23').value = numSegPreparador.toString();
            document.getElementById('totSegPreparadorJus').value = formatearNumero(totSeguimientos) + ' \u20ac';

            var tablaJusSeguimientos;
            var listaJusSeguimientos = new Array();
            var listaJusSeguimientosTabla = new Array();
            var listaJusSeguimientosTitulos = new Array();
            <%
                JusSeguimientosECA23VO segui = null;
                List<JusSeguimientosECA23VO> ListaSegui = null;
                if(request.getAttribute("listaSegPreparador")!=null) {
                    ListaSegui = (List<JusSeguimientosECA23VO>)request.getAttribute("listaSegPreparador");
                }
                if (ListaSegui!= null && ListaSegui.size() > 0) {
                    for (int indice=0;indice<ListaSegui.size();indice++) {
                        segui = ListaSegui.get(indice);

                        String ape2 = "";
                        if (segui.getApellido2() != null) {
                            ape2 =segui.getApellido2();
                        }

                        String fNacimiento = "";
                        if(segui.getfNacimiento()!=null){
                            fNacimiento=dateFormat.format(segui.getfNacimiento());
                        }

                        String grado = "-";
                        if (segui.getGrado() != null) {
                            grado = formateador.format(segui.getGrado());
                        }

                        String jornada = "-";
                        if (segui.getJornada() != null) {
                            jornada = formateador.format(segui.getJornada());
                        }

                        String fInicio = "-";
                        if(segui.getfInicio()!=null){
                            fInicio=dateFormat.format(segui.getfInicio());
                        }

                        String fFin = "-";
                        if(segui.getfFin()!=null){
                            fFin=dateFormat.format(segui.getfFin());
                        }

                        String descCnae = "-";
                        if(idiomaUsuario==4) {
                            descCnae = segui.getDescCnaeE();
                        } else {
                            descCnae = segui.getDescCnaeC();
                        }

                        String importe = "";
                        if (segui.getImporteSegui() != null) {
                            importe = formateador.format(segui.getImporteSegui());
                        }
            %>
            listaJusSeguimientos[<%=indice%>] = ['<%=segui.getId()%>', '<%=segui.getNifPreparador()%>', '<%=segui.getDni()%>', '<%=segui.getNombre()%>', '<%=segui.getApellido1()%>', '<%=ape2%>', '<%=segui.getSexo()%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getTipoDisc()%>', '<%=segui.getDescTipoDisc()%>', '<%=grado%>', '<%=segui.getColectivo()%>', '<%=segui.getDescColectivo()%>', '<%=segui.getTipoContrato()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getTipoEdadSexo()%>', '<%=segui.getDescTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=segui.getCnae()%>', '<%=descCnae%>', '<%=importe%>'];
            listaJusSeguimientosTabla[<%=indice%>] = ['<%=segui.getDni()%>', '<%=segui.getNombre()%>' + ' ' + '<%=segui.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getTipoDisc()%>', '<%=grado%>', '<%=segui.getColectivo()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=segui.getCnae()%>', '<%=importe%>' + ' \u20ac']; //18
            listaJusSeguimientosTitulos[<%=indice%>] = ['<%=segui.getDni()%>', '<%=segui.getNombre()%>' + ' ' + '<%=segui.getApellido1()%>' + ' ' + '<%=ape2%>', '<%=segui.getDescSexo()%>',
                '<%=fNacimiento%>', '<%=segui.getDescTipoDisc()%>', '<%=grado%>', '<%=segui.getDescColectivo()%>', '<%=segui.getDescTipoContrato()%>',
                '<%=jornada%>', '<%=fInicio%>', '<%=fFin%>', '<%=segui.getDescTipoEdadSexo()%>', '<%=segui.getEmpresa()%>', '<%=segui.getNifEmpresa()%>', '<%=descCnae%>', '<%=importe%>' + ' \u20ac']; //18
            <%
                    }           
                }
                
                if (importesConfiguracion!= null) {
            %>
            maxSeguimientosJ = <%=importesConfiguracion.getMaximoSeguimientos()%>;
            <%            
                }
            %>
            crearTablaJusSeguimientos();
        </script>
    </body>
</html>
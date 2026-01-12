<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch(Exception ex) {
                }
            }

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idioma = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
            }

            MeLanbide12I18n meLanbide12I18n = MeLanbide12I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide12/melanbide12.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide12/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide12/PractUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevoL1EmpresaExterna() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarNuevoL1EmpresaExterna&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaL1EmpresaExterna(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarL1EmpresaExterna() {
                if (tablaL1EmpresaExterna.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarModificarL1EmpresaExterna&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaL1EmpresaExterna[tablaL1EmpresaExterna.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaL1EmpresaExterna(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarL1EmpresaExterna() {
                if (tablaL1EmpresaExterna.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoPRACT');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE12&operacion=eliminarL1EmpresaExterna&tipo=0&numExp=<%=numExpediente%>&id=' + listaL1EmpresaExterna[tablaL1EmpresaExterna.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarL1EmpresaExterna,
                                error: mostrarErrorEliminarL1EmpresaExterna
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoPRACT');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTablaL1EmpresaExterna() {
                tablaL1EmpresaExterna = new FixedColumnTable(document.getElementById('listaL1EmpresaExterna'), 1600, 1650, 'listaL1EmpresaExterna');

                tablaL1EmpresaExterna.addColumna('30', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.id")%>");
                tablaL1EmpresaExterna.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.cif")%>");
                tablaL1EmpresaExterna.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.denom_empr")%>");
                tablaL1EmpresaExterna.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.n_factura")%>");
                tablaL1EmpresaExterna.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.fec_emis")%>");
                tablaL1EmpresaExterna.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.fec_pago")%>");
                tablaL1EmpresaExterna.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.imp_base")%>");
                tablaL1EmpresaExterna.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.imp_iva")%>");
                tablaL1EmpresaExterna.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.imp_total")%>");
                tablaL1EmpresaExterna.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.personas")%>");
                tablaL1EmpresaExterna.addColumna('300', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.imp_persona_fact")%>");
                tablaL1EmpresaExterna.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaExterna.tablaL1EmpresaExterna.imp_solic")%>");

                for (var cont = 0; cont < listaL1EmpresaExternaTabla.length; cont++) {
                    tablaL1EmpresaExterna.addFilaConFormato(listaL1EmpresaExternaTabla[cont], listaL1EmpresaExternaTabla_titulos[cont], listaL1EmpresaExternaTabla_estilos[cont]);
                }

                tablaL1EmpresaExterna.displayCabecera = true;
                tablaL1EmpresaExterna.height = 360;

                tablaL1EmpresaExterna.altoCabecera = 40;
                tablaL1EmpresaExterna.scrollWidth = 5770;
                tablaL1EmpresaExterna.dblClkFunction = 'dblClckTablaL1EmpresaExterna';
                tablaL1EmpresaExterna.displayTabla();
                tablaL1EmpresaExterna.pack();

            }

            function recargarTablaL1EmpresaExterna(result) {
                var fila;
                listaL1EmpresaExterna = new Array();
                listaL1EmpresaExternaTabla = new Array();
                listaL1EmpresaExternaTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaL1EmpresaExterna[i] = [fila.id,fila.cif,fila.denomEmpr,fila.nFactura,fila.fecEmis,fila.fecEmisStr,fila.fecPago,fila.fecPagoStr,fila.impBase,fila.impIva,fila.impTotal,fila.personas,fila.impPersonaFact,fila.impSolic];
                    listaL1EmpresaExternaTabla[i] = [fila.id,fila.cif,fila.denomEmpr,fila.nFactura,fila.fecEmisStr,fila.fecPagoStr,fila.impBase,fila.impIva,fila.impTotal,fila.personas,fila.impPersonaFact,fila.impSolic];
                    listaL1EmpresaExternaTabla_titulos[i] = [fila.id,fila.cif,fila.denomEmpr,fila.nFactura,fila.fecEmisStr,fila.fecPagoStr,fila.impBase,fila.impIva,fila.impTotal,fila.personas,fila.impPersonaFact,fila.impSolic];
                }
                iniciarTablaL1EmpresaExterna();
            }


            function dblClckTablaL1EmpresaExterna(rowID, tableName) {
                pulsarModificarL1EmpresaExterna();
            }

            function procesarRespuestaEliminarL1EmpresaExterna(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    elementoVisible('off', 'barraProgresoPRACT');
                    recargarTablaL1EmpresaExterna(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarL1EmpresaExterna() {
                mostrarErrorPeticion("6");
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoPRACT');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage122" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana122"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.titulo.pestana.L1EmpresaExterna")%></h2>
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage122"));</script>
            <div id="barraProgresoPRACT" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <br/>
            <h2 class="legendTema" align="center" id="pestanaL1EmpresaExterna"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal.L1EmpresaExterna")%></h2>
            <div>
                <br>
                <div id="divGeneral">
                    <div id="listaL1EmpresaExterna"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoL1EmpresaExterna" name="btnNuevoL1EmpresaExterna" class="botonGeneral"  value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoL1EmpresaExterna();">
                    <input type="button" id="btnModificarL1EmpresaExterna" name="btnModificarL1EmpresaExterna" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarL1EmpresaExterna();">
                    <input type="button" id="btnEliminarL1EmpresaExterna" name="btnEliminarL1EmpresaExterna"   class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarL1EmpresaExterna();">
                </div>
            </div>
        </div>

        <script  type="text/javascript">
            //Tabla L1EmpresaExterna
            var tablaL1EmpresaExterna;
            var listaL1EmpresaExterna = new Array();
            var listaL1EmpresaExternaTabla = new Array();
            var listaL1EmpresaExternaTabla_titulos = new Array();
            var listaL1EmpresaExternaTabla_estilos = new Array();

            <%
               FilaL1EmpresaExternaVO objectVO = null;
               List<FilaL1EmpresaExternaVO> List = null;
               if(request.getAttribute("listaL1EmpresaExterna")!=null){
                   List = (List<FilaL1EmpresaExternaVO>)request.getAttribute("listaL1EmpresaExterna");
               }
               if (List!= null && List.size() >0){
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);

                        String cif="";
                        if(objectVO.getCif()!=null){
                            cif=objectVO.getCif().replaceAll("\\n", " ");
                        }else{
                            cif="-";
                        }
                        String denom_empr="";
                        if(objectVO.getDenomEmpr()!=null){
                            denom_empr=objectVO.getDenomEmpr().replaceAll("\\n", " ");
                        }else{
                            denom_empr="-";
                        }
                        String n_factura="";
                        if(objectVO.getnFactura()!=null){
                            n_factura=objectVO.getnFactura().replaceAll("\\n", " ");
                        }else{
                            n_factura="-";
                        }
                        String fec_emis="";
                        if(objectVO.getFecEmis()!=null){
                            fec_emis=dateFormat.format(objectVO.getFecEmis());
                        }else{
                            fec_emis="-";
                        }
                        String fec_pago="";
                        if(objectVO.getFecPago()!=null){
                            fec_pago=dateFormat.format(objectVO.getFecPago());
                        }else{
                            fec_pago="-";
                        }
                        String imp_base="";
                        if(objectVO.getImpBase()!=null){
                            imp_base=String.valueOf((objectVO.getImpBase().toString()).replace(".",","));
                        }else{
                            imp_base="-";
                        }
                        String imp_iva="";
                        if(objectVO.getImpIva()!=null){
                            imp_iva=String.valueOf((objectVO.getImpIva().toString()).replace(".",","));
                        }else{
                            imp_iva="-";
                        }
                        String imp_total="";
                        if(objectVO.getImpTotal()!=null){
                            imp_total=String.valueOf((objectVO.getImpTotal().toString()).replace(".",","));
                        }else{
                            imp_total="-";
                        }
                        String personas="";
                        if(objectVO.getPersonas()!=null){
                            personas=String.valueOf((objectVO.getPersonas().toString()).replace(".",","));
                        }else{
                            personas="-";
                        }
                        String imp_persona_fact="";
                        if(objectVO.getImpPersonaFact()!=null){
                            imp_persona_fact=String.valueOf((objectVO.getImpPersonaFact().toString()).replace(".",","));
                        }else{
                            imp_persona_fact="-";
                        }
                        String imp_solic="";
                        if(objectVO.getImpSolic()!=null){
                            imp_solic=String.valueOf((objectVO.getImpSolic().toString()).replace(".",","));
                        }else{
                            imp_solic="-";
                        }


            %>
            listaL1EmpresaExterna[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=cif%>', '<%=denom_empr%>', '<%=n_factura%>', '<%=fec_emis%>', '<%=fec_pago%>', '<%=imp_base%>', '<%=imp_iva%>', '<%=imp_total%>', '<%=personas%>', '<%=imp_persona_fact%>', '<%=imp_solic%>'];
            listaL1EmpresaExternaTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=cif%>', '<%=denom_empr%>', '<%=n_factura%>', '<%=fec_emis%>', '<%=fec_pago%>', '<%=imp_base%>', '<%=imp_iva%>', '<%=imp_total%>', '<%=personas%>', '<%=imp_persona_fact%>', '<%=imp_solic%>'];
            listaL1EmpresaExternaTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=cif%>', '<%=denom_empr%>', '<%=n_factura%>', '<%=fec_emis%>', '<%=fec_pago%>', '<%=imp_base%>', '<%=imp_iva%>', '<%=imp_total%>', '<%=personas%>', '<%=imp_persona_fact%>', '<%=imp_solic%>'];;
            <%
                   }// for
               }// if
            %>
            iniciarTablaL1EmpresaExterna();
        </script>
        <div id="popupcalendar" class="text"></div>
    </body>
</html>
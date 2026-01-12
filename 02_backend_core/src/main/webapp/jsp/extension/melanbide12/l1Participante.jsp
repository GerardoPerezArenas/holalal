<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO"%>
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

            function pulsarNuevoL1Participante() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarNuevoL1Participante&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaL1Participante(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarL1Participante() {
                if (tablaL1Participante.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarModificarL1Participante&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaL1Participante[tablaL1Participante.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaL1Participante(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarL1Participante() {
                if (tablaL1Participante.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoPRACT');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE12&operacion=eliminarL1Participante&tipo=0&numExp=<%=numExpediente%>&id=' + listaL1Participante[tablaL1Participante.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarL1Participante,
                                error: mostrarErrorEliminarL1Participante
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

            function iniciarTablaL1Participante() {
                tablaL1Participante = new FixedColumnTable(document.getElementById('listaL1Participante'), 1600, 1650, 'listaL1Participante');

                tablaL1Participante.addColumna('30', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.id")%>");
                tablaL1Participante.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.tipoDoc")%>");
                tablaL1Participante.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.doc")%>");
                tablaL1Participante.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.nombre")%>");
                tablaL1Participante.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.ape1")%>");
                tablaL1Participante.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.ape2")%>");
                tablaL1Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.nss")%>");
                tablaL1Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.cod_act_form")%>");
                tablaL1Participante.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.fec_ini_pract")%>");
                tablaL1Participante.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.fec_fin_pract")%>");
                tablaL1Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.cc_cot")%>");
                tablaL1Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.dias_cot")%>");
                tablaL1Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1Participante.tablaL1Participante.imp_solic")%>");

                for (var cont = 0; cont < listaL1ParticipanteTabla.length; cont++) {
                    tablaL1Participante.addFilaConFormato(listaL1ParticipanteTabla[cont], listaL1ParticipanteTabla_titulos[cont], listaL1ParticipanteTabla_estilos[cont]);
                }

                tablaL1Participante.displayCabecera = true;
                tablaL1Participante.height = 360;

                tablaL1Participante.altoCabecera = 40;
                tablaL1Participante.scrollWidth = 5770;
                tablaL1Participante.dblClkFunction = 'dblClckTablaL1Participante';
                tablaL1Participante.displayTabla();
                tablaL1Participante.pack();

            }

            function recargarTablaL1Participante(result) {
                var fila;
                listaL1Participante = new Array();
                listaL1ParticipanteTabla = new Array();
                listaL1ParticipanteTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];

                    listaL1Participante[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.nss,fila.codActForm,fila.fecIniPract,fila.fecIniPractStr,fila.fecFinPract,fila.fecFinPractStr,fila.ccCot,fila.diasCot,fila.impSolic];
                    listaL1ParticipanteTabla[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.nss,fila.codActForm,fila.fecIniPractStr,fila.fecFinPractStr,fila.ccCot,fila.diasCot,fila.impSolic];
                    listaL1ParticipanteTabla_titulos[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.nss,fila.codActForm,fila.fecIniPractStr,fila.fecFinPractStr,fila.ccCot,fila.diasCot,fila.impSolic];
                }
                iniciarTablaL1Participante();
            }


            function dblClckTablaL1Participante(rowID, tableName) {
                pulsarModificarL1Participante();
            }

            function procesarRespuestaEliminarL1Participante(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    elementoVisible('off', 'barraProgresoPRACT');
                    recargarTablaL1Participante(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarL1Participante() {
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
        <div class="tab-page" id="tabPage121" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana121"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.titulo.pestana.L1Participante")%></h2>
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage121"));</script>
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
            <h2 class="legendTema" align="center" id="pestanal1Participante"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal.L1Participante")%></h2>
            <div>
                <br>
                <div id="divGeneral">
                    <div id="listaL1Participante"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoL1Participante" name="btnNuevoL1Participante" class="botonGeneral"  value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoL1Participante();">
                    <input type="button" id="btnModificarL1Participante" name="btnModificarL1Participante" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarL1Participante();">
                    <input type="button" id="btnEliminarL1Participante" name="btnEliminarL1Participante"   class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarL1Participante();">
                </div>
            </div>
        </div>

        <script  type="text/javascript">
            //Tabla L1Participante
            var tablaL1Participante;
            var listaL1Participante = new Array();
            var listaL1ParticipanteTabla = new Array();
            var listaL1ParticipanteTabla_titulos = new Array();
            var listaL1ParticipanteTabla_estilos = new Array();

            <%
               FilaL1ParticipanteVO objectVO = null;
               List<FilaL1ParticipanteVO> List = null;
               if(request.getAttribute("listaL1Participante")!=null){
                   List = (List<FilaL1ParticipanteVO>)request.getAttribute("listaL1Participante");
               }
               if (List!= null && List.size() >0){
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);

                        String tipoDoc="";
                        if(objectVO.getDesTipoDoc()!=null){
                            String descripcion = objectVO.getDesTipoDoc();

                            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide12.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide12.FICHERO_PROPIEDADES);
                            String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide12.CODIGO_IDIOMA_EUSKERA){
                                    descripcion=descripcionDobleIdioma[1];
                                }else{
                                    // Cogemos la primera posición que debería ser castellano
                                    descripcion=descripcionDobleIdioma[0];
                                }
                            }
                            tipoDoc = descripcion;
                        }else{
                            tipoDoc="-";
                        }
                        String doc="";
                        if(objectVO.getDoc()!=null){
                            doc=objectVO.getDoc().replaceAll("\\n", " ");
                        }else{
                            doc="-";
                        }
                        String nombre="";
                        if(objectVO.getNombre()!=null){
                            nombre=objectVO.getNombre().replaceAll("\\n", " ");
                        }else{
                            nombre="-";
                        }
                        String ape1="";
                        if(objectVO.getApe1()!=null){
                            ape1=objectVO.getApe1().replaceAll("\\n", " ");
                        }else{
                            ape1="-";
                        }
                        String ape2="";
                        if(objectVO.getApe2()!=null){
                            ape2=objectVO.getApe2().replaceAll("\\n", " ");
                        }else{
                            ape2="-";
                        }
                        String nss="";
                        if(objectVO.getNss()!=null){
                            nss=objectVO.getNss().replaceAll("\\n", " ");
                        }else{
                            nss="-";
                        }
                        String cod_act_form="";
                        if(objectVO.getCodActForm()!=null){
                            cod_act_form=objectVO.getCodActForm().replaceAll("\\n", " ");
                        }else{
                            cod_act_form="-";
                        }
                        String fec_ini_pract="";
                        if(objectVO.getFecIniPract()!=null){
                            fec_ini_pract=dateFormat.format(objectVO.getFecIniPract());
                        }else{
                            fec_ini_pract="-";
                        }
                        String fec_fin_pract="";
                        if(objectVO.getFecFinPract()!=null){
                            fec_fin_pract=dateFormat.format(objectVO.getFecFinPract());
                        }else{
                            fec_fin_pract="-";
                        }
                        String cc_cot="";
                        if(objectVO.getCcCot()!=null){
                            cc_cot=objectVO.getCcCot().replaceAll("\\n", " ");
                        }else{
                            cc_cot="-";
                        }
                        String dias_cot="";
                        if(objectVO.getDiasCot()!=null){
                            dias_cot=String.valueOf((objectVO.getDiasCot().toString()).replace(".",","));
                        }else{
                            dias_cot="-";
                        }
                        String imp_solic="";
                        if(objectVO.getImpSolic()!=null){
                            imp_solic=String.valueOf((objectVO.getImpSolic().toString()).replace(".",","));
                        }else{
                            imp_solic="-";
                        }


            %>
            listaL1Participante[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=nss%>', '<%=cod_act_form%>', '<%=fec_ini_pract%>', '<%=fec_fin_pract%>', '<%=cc_cot%>', '<%=dias_cot%>', '<%=imp_solic%>'];
            listaL1ParticipanteTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=nss%>', '<%=cod_act_form%>', '<%=fec_ini_pract%>', '<%=fec_fin_pract%>', '<%=cc_cot%>', '<%=dias_cot%>', '<%=imp_solic%>'];
            listaL1ParticipanteTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=nss%>', '<%=cod_act_form%>', '<%=fec_ini_pract%>', '<%=fec_fin_pract%>', '<%=cc_cot%>', '<%=dias_cot%>', '<%=imp_solic%>'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaL1Participante();
        </script>
        <div id="popupcalendar" class="text"></div>
    </body>
</html>
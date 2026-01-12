<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL2ParticipanteVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
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

            function pulsarNuevoL2Participante() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarNuevoL2Participante&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaL2Participante(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarL2Participante() {
                if (tablaL2Participante.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarModificarL2Participante&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaL2Participante[tablaL2Participante.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaL2Participante(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarL2Participante() {
                if (tablaL2Participante.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoPRACT');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE12&operacion=eliminarL2Participante&tipo=0&numExp=<%=numExpediente%>&id=' + listaL2Participante[tablaL2Participante.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarL2Participante,
                                error: mostrarErrorEliminarL2Participante
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

            function iniciarTablaL2Participante() {
                tablaL2Participante = new FixedColumnTable(document.getElementById('listaL2Participante'), 1600, 1650, 'listaL2Participante');

                tablaL2Participante.addColumna('30', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.id")%>");
                tablaL2Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.tipoDoc")%>");
                tablaL2Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.doc")%>");
                tablaL2Participante.addColumna('300', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.nombre")%>");
                tablaL2Participante.addColumna('300', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.ape1")%>");
                tablaL2Participante.addColumna('300', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.ape2")%>");
                tablaL2Participante.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.cod_act_form")%>");
                tablaL2Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.horas_pract")%>");
                tablaL2Participante.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L2Participante.tablaL2Participante.imp_solic")%>");

                for (var cont = 0; cont < listaL2ParticipanteTabla.length; cont++) {
                    tablaL2Participante.addFilaConFormato(listaL2ParticipanteTabla[cont], listaL2ParticipanteTabla_titulos[cont], listaL2ParticipanteTabla_estilos[cont]);
                }

                tablaL2Participante.displayCabecera = true;
                tablaL2Participante.height = 360;

                tablaL2Participante.altoCabecera = 40;
                tablaL2Participante.scrollWidth = 5770;
                tablaL2Participante.dblClkFunction = 'dblClckTablaL2Participante';
                tablaL2Participante.displayTabla();
                tablaL2Participante.pack();

            }

            function recargarTablaL2Participante(result) {
                var fila;
                listaL2Participante = new Array();
                listaL2ParticipanteTabla = new Array();
                listaL2ParticipanteTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaL2Participante[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.codActForm,fila.horasPract,fila.impSolic];
                    listaL2ParticipanteTabla[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.codActForm,fila.horasPract,fila.impSolic];
                    listaL2ParticipanteTabla_titulos[i] = [fila.id,fila.desTipoDoc,fila.doc,fila.nombre,fila.ape1,fila.ape2,fila.codActForm,fila.horasPract,fila.impSolic];
                }
                iniciarTablaL2Participante();
            }


            function dblClckTablaL2Participante(rowID, tableName) {
                pulsarModificarL2Participante();
            }

            function procesarRespuestaEliminarL2Participante(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    elementoVisible('off', 'barraProgresoPRACT');
                    recargarTablaL2Participante(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarL2Participante() {
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
        <div class="tab-page" id="tabPage124" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana124"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.titulo.pestana.L2Participante")%></h2>
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage124"));</script>
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
            <h2 class="legendTema" align="center" id="pestanaL2Participante"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal.L2Participante")%></h2>
            <div>
                <br>
                <div id="divGeneral">
                    <div id="listaL2Participante"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoL2Participante" name="btnNuevoL2Participante" class="botonGeneral"  value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoL2Participante();">
                    <input type="button" id="btnModificarL2Participante" name="btnModificarL2Participante" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarL2Participante();">
                    <input type="button" id="btnEliminarL2ParticipanteL2Participante" name="btnEliminarL2Participante"   class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarL2Participante();">
                </div>
            </div>
        </div>

        <script  type="text/javascript">
            //Tabla L2Participante
            var tablaL2Participante;
            var listaL2Participante = new Array();
            var listaL2ParticipanteTabla = new Array();
            var listaL2ParticipanteTabla_titulos = new Array();
            var listaL2ParticipanteTabla_estilos = new Array();

            <%
               FilaL2ParticipanteVO objectVO = null;
               List<FilaL2ParticipanteVO> List = null;
               if(request.getAttribute("listaL2Participante")!=null){
                   List = (List<FilaL2ParticipanteVO>)request.getAttribute("listaL2Participante");
               }
               if (List!= null && List.size() >0){
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
                        String cod_act_form="";
                        if(objectVO.getCodActForm()!=null){
                            cod_act_form=objectVO.getCodActForm().replaceAll("\\n", " ");
                        }else{
                            cod_act_form="-";
                        }
                        String horas_pract="";
                        if(objectVO.getHorasPract()!=null){
                            horas_pract=String.valueOf((objectVO.getHorasPract().toString()).replace(".",","));
                        }else{
                            horas_pract="-";
                        }
                        String imp_solic="";
                        if(objectVO.getImpSolic()!=null){
                            imp_solic=String.valueOf((objectVO.getImpSolic().toString()).replace(".",","));
                        }else{
                            imp_solic="-";
                        }


            %>
            listaL2Participante[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=cod_act_form%>', '<%=horas_pract%>', '<%=imp_solic%>'];
            listaL2ParticipanteTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=cod_act_form%>', '<%=horas_pract%>', '<%=imp_solic%>'];
            listaL2ParticipanteTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipoDoc%>', '<%=doc%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=cod_act_form%>', '<%=horas_pract%>', '<%=imp_solic%>'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaL2Participante();
        </script>
        <div id="popupcalendar" class="text"></div>
    </body>
</html>
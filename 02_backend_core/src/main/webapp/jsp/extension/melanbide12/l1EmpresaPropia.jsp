<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO"%>
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

            function pulsarNuevoL1EmpresaPropia() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarNuevoL1EmpresaPropia&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaL1EmpresaPropia(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarL1EmpresaPropia() {
                if (tablaL1EmpresaPropia.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE12&operacion=cargarModificarL1EmpresaPropia&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaL1EmpresaPropia[tablaL1EmpresaPropia.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaL1EmpresaPropia(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarL1EmpresaPropia() {
                if (tablaL1EmpresaPropia.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoPRACT');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE12&operacion=eliminarL1EmpresaPropia&tipo=0&numExp=<%=numExpediente%>&id=' + listaL1EmpresaPropia[tablaL1EmpresaPropia.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarL1EmpresaPropia,
                                error: mostrarErrorEliminarL1EmpresaPropia
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

            function iniciarTablaL1EmpresaPropia() {
                tablaL1EmpresaPropia = new FixedColumnTable(document.getElementById('listaL1EmpresaPropia'), 1600, 1650, 'listaL1EmpresaPropia');

                tablaL1EmpresaPropia.addColumna('30', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.id")%>");
                tablaL1EmpresaPropia.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.dni")%>");
                tablaL1EmpresaPropia.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.nombre")%>");
                tablaL1EmpresaPropia.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.ape1")%>");
                tablaL1EmpresaPropia.addColumna('200', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.ape2")%>");
                tablaL1EmpresaPropia.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.retr_anual_bruta")%>");
                tablaL1EmpresaPropia.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.cc_cot_ss")%>");
                tablaL1EmpresaPropia.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.horas_lab_anual")%>");
                tablaL1EmpresaPropia.addColumna('100', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.horas_imput")%>");
                tablaL1EmpresaPropia.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.imp_gest")%>");
                tablaL1EmpresaPropia.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.person_pract")%>");
                tablaL1EmpresaPropia.addColumna('150', 'center', "<%=meLanbide12I18n.getMensaje(idiomaUsuario,"L1EmpresaPropia.tablaL1EmpresaPropia.imp_solic")%>");

                for (var cont = 0; cont < listaL1EmpresaPropiaTabla.length; cont++) {
                    tablaL1EmpresaPropia.addFilaConFormato(listaL1EmpresaPropiaTabla[cont], listaL1EmpresaPropiaTabla_titulos[cont], listaL1EmpresaPropiaTabla_estilos[cont]);
                }

                tablaL1EmpresaPropia.displayCabecera = true;
                tablaL1EmpresaPropia.height = 360;

                tablaL1EmpresaPropia.altoCabecera = 40;
                tablaL1EmpresaPropia.scrollWidth = 5770;
                tablaL1EmpresaPropia.dblClkFunction = 'dblClckTablaL1EmpresaPropia';
                tablaL1EmpresaPropia.displayTabla();
                tablaL1EmpresaPropia.pack();

            }

            function recargarTablaL1EmpresaPropia(result) {
                var fila;
                listaL1EmpresaPropia = new Array();
                listaL1EmpresaPropiaTabla = new Array();
                listaL1EmpresaPropiaTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaL1EmpresaPropia[i] = [fila.id,fila.dni,fila.nombre,fila.ape1,fila.ape2,fila.retrAnualBruta,fila.ccCotSs,fila.horasLabAnual,fila.horasImput,fila.impGest,fila.personPract,fila.impSolic];
                    listaL1EmpresaPropiaTabla[i] = [fila.id,fila.dni,fila.nombre,fila.ape1,fila.ape2,fila.retrAnualBruta,fila.ccCotSs,fila.horasLabAnual,fila.horasImput,fila.impGest,fila.personPract,fila.impSolic];
                    listaL1EmpresaPropiaTabla_titulos[i] = [fila.id,fila.dni,fila.nombre,fila.ape1,fila.ape2,fila.retrAnualBruta,fila.ccCotSs,fila.horasLabAnual,fila.horasImput,fila.impGest,fila.personPract,fila.impSolic];
                }
                iniciarTablaL1EmpresaPropia();
            }


            function dblClckTablaL1EmpresaPropia(rowID, tableName) {
                pulsarModificarL1EmpresaPropia();
            }

            function procesarRespuestaEliminarL1EmpresaPropia(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    elementoVisible('off', 'barraProgresoPRACT');
                    recargarTablaL1EmpresaPropia(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarL1EmpresaPropia() {
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
        <div class="tab-page" id="tabPage123" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana123"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.titulo.pestana.L1EmpresaPropia")%></h2>
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage123"));</script>
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
            <h2 class="legendTema" align="center" id="pestanaL1EmpresaPropia"><%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal.L1EmpresaPropia")%></h2>
            <div>
                <br>
                <div id="divGeneral">
                    <div id="listaL1EmpresaPropia"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoL1EmpresaPropia" name="btnNuevoL1EmpresaPropia" class="botonGeneral"  value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoL1EmpresaPropia();">
                    <input type="button" id="btnModificarL1EmpresaPropia" name="btnModificarL1EmpresaPropia" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarL1EmpresaPropia();">
                    <input type="button" id="btnEliminarL1EmpresaPropia" name="btnEliminarL1EmpresaPropia"   class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarL1EmpresaPropia();">
                </div>
            </div>
        </div>

        <script  type="text/javascript">
            //Tabla L1EmpresaPropia
            var tablaL1EmpresaPropia;
            var listaL1EmpresaPropia = new Array();
            var listaL1EmpresaPropiaTabla = new Array();
            var listaL1EmpresaPropiaTabla_titulos = new Array();
            var listaL1EmpresaPropiaTabla_estilos = new Array();

            <%
               FilaL1EmpresaPropiaVO objectVO = null;
               List<FilaL1EmpresaPropiaVO> List = null;
               if(request.getAttribute("listaL1EmpresaPropia")!=null){
                   List = (List<FilaL1EmpresaPropiaVO>)request.getAttribute("listaL1EmpresaPropia");
               }
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);

                        String dni="";
                        if(objectVO.getDni()!=null){
                            dni=objectVO.getDni().replaceAll("\\n", " ");
                        }else{
                            dni="-";
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
                        String retr_anual_bruta="";
                        if(objectVO.getRetrAnualBruta()!=null){
                            retr_anual_bruta=String.valueOf((objectVO.getRetrAnualBruta().toString()).replace(".",","));
                        }else{
                            retr_anual_bruta="-";
                        }
                        String cc_cot_ss="";
                        if(objectVO.getCcCotSs()!=null){
                            cc_cot_ss=String.valueOf((objectVO.getCcCotSs().toString()).replace(".",","));
                        }else{
                            cc_cot_ss="-";
                        }
                        String horas_lab_anual="";
                        if(objectVO.getHorasLabAnual()!=null){
                            horas_lab_anual=String.valueOf((objectVO.getHorasLabAnual().toString()).replace(".",","));
                        }else{
                            horas_lab_anual="-";
                        }
                        String horas_imput="";
                        if(objectVO.getHorasImput()!=null){
                            horas_imput=String.valueOf((objectVO.getHorasImput().toString()).replace(".",","));
                        }else{
                            horas_imput="-";
                        }
                        String imp_gest="";
                        if(objectVO.getImpGest()!=null){
                            imp_gest=String.valueOf((objectVO.getImpGest().toString()).replace(".",","));
                        }else{
                            imp_gest="-";
                        }
                        String person_pract="";
                        if(objectVO.getPersonPract()!=null){
                            person_pract=String.valueOf((objectVO.getPersonPract().toString()).replace(".",","));
                        }else{
                            person_pract="-";
                        }
                        String imp_solic="";
                        if(objectVO.getImpSolic()!=null){
                            imp_solic=String.valueOf((objectVO.getImpSolic().toString()).replace(".",","));
                        }else{
                            imp_solic="-";
                        }


            %>
            listaL1EmpresaPropia[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dni%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=retr_anual_bruta%>', '<%=cc_cot_ss%>', '<%=horas_lab_anual%>', '<%=horas_imput%>', '<%=imp_gest%>', '<%=person_pract%>', '<%=imp_solic%>'];
            listaL1EmpresaPropiaTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dni%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=retr_anual_bruta%>', '<%=cc_cot_ss%>', '<%=horas_lab_anual%>', '<%=horas_imput%>', '<%=imp_gest%>', '<%=person_pract%>', '<%=imp_solic%>'];
            listaL1EmpresaPropiaTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dni%>', '<%=nombre%>', '<%=ape1%>', '<%=ape2%>', '<%=retr_anual_bruta%>', '<%=cc_cot_ss%>', '<%=horas_lab_anual%>', '<%=horas_imput%>', '<%=imp_gest%>', '<%=person_pract%>', '<%=imp_solic%>'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaL1EmpresaPropia();
        </script>
        <div id="popupcalendar" class="text"></div>
    </body>
</html>
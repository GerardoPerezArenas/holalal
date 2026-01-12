<%-- 
    Document   : espacio
    Created on : 18-jul-2022, 10:18:08
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            if(request.getParameter("idioma") != null) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex){}
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        if (usuario != null) {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                } catch (Exception ex){}
            }

            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String numExpediente    = request.getParameter("numero");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if(request.getAttribute("datoEspecialidad") != null) {
                datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
            }

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript">
            function pulsarAltaEspacio() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE50&operacion=cargarNuevoEspacio&tipo=0&numero=<%=numExpediente%>&nuevo=1&idEpsol=<%=datoEspecialidad.getId()%>', 600, 850, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaEspacios(result);
                        }
                    }
                });
            }

            function pulsarModificarEspacio() {
                if (tablaEspacios.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE50&operacion=cargarModificarEspacio&tipo=0&nuevo=0&numero=<%=numExpediente%>&idEpsol=<%=datoEspecialidad.getId()%>&id=' + listaEspacios[tablaEspacios.selectedIndex][0], 600, 850, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaEspacios(result);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarEspacio() {
                if (tablaEspacios.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=eliminarEspacio&tipo=0&numero=<%=numExpediente%>&idEpsol=<%=datoEspecialidad.getId()%>&id=' + listaEspacios[tablaEspacios.selectedIndex][0];
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        if (ajax != null) {
                            try {
                                ajax.open("POST", url, false);
                                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                                ajax.send(parametros);
                                if (ajax.readyState == 4 && ajax.status == 200) {
                                    var xmlDoc = null;
                                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                        // En IE el XML viene en responseText y no en la propiedad responseXML
                                        var text = ajax.responseText;
                                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                        xmlDoc.async = "false";
                                        xmlDoc.loadXML(text);
                                    } else {
                                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                        xmlDoc = ajax.responseXML;
                                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                                }//if (ajax.readyState==4 && ajax.status==200)

                                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                var listaEspaciosNueva = extraerListaEspacios(nodos);
                                var codigoOperacion = listaEspaciosNueva[0];
                                if (codigoOperacion == "0") {
                                    recargarTablaEspacios(listaEspaciosNueva);
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            } catch (Err) {
                                jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaEspacios() {
                tablaEspacios = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaEspacios'), 1010);
                tablaEspacios.addColumna('270', 'left', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"espacios.tablaEspecialidades.col1")%>"); // Espacios acreditados
                tablaEspacios.addColumna('270', 'left', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"espacios.tablaEspecialidades.col2")%>"); // Espacios solicitados
                tablaEspacios.addColumna('75', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"espacios.tablaEspecialidades.col3")%>"); // num alumnos
                tablaEspacios.addColumna('75', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"espacios.tablaEspecialidades.col4")%>"); // num alumnos nuevos

                tablaEspacios.displayCabecera = true;
                tablaEspacios.height = 100;
                tablaEspacios.lineas = listaEspaciosTabla;
                tablaEspacios.displayTabla();
            }

            function recargarTablaEspacios(espacios) {
                var espacio;
                listaEspacios = new Array();
                listaEspaciosTabla = new Array();
                for (var i = 1; i < espacios.length; i++) {
                    espacio = espacios[i];
                    listaEspacios[i - 1] = [espacio[0], espacio[1], espacio[2], espacio[3].replace("\n\r", "<br>").replace("\r", "<br>").replace("\n", "<br>"), espacio[4].replace("\n\r", "<br>").replace("\r", "<br>").replace("\n", "<br>"), espacio[5], espacio[6]];
                    listaEspaciosTabla[i - 1] = [espacio[3].replace("\n\r", "<br>").replace("\r", "<br>").replace("\n", "<br>"), espacio[4].replace("\n\r", "<br>").replace("\r", "<br>").replace("\n", "<br>"), espacio[5], espacio[6]];
                }
                crearTablaEspacios();
            }

        </script>
    </head>
    <body>
        <div class="tab-page" id="tabPage504" style="height:420px; width: 98%;">
            <div style="clear: both;">
                <label class="legendAzul" style="text-align: center; position: relative; left: 18%;"><%=meLanbide50I18n.getMensaje(idiomaUsuario, "espacios.legend.titulo")%></label>
                <div id="divGeneral">
                    <div id="listaEspacios" align="center"></div>
                    <div class="botonera">
                        <input type="button" id="btnNuevoEspacio"       name="btnNuevoEspacio"      class="botonGeneral"    value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>"     onclick="pulsarAltaEspacio();">
                        <input type="button" id="btnEliminarEspacio"    name="btnEliminarEspacio"   class="botonGeneral"    value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>"  onclick="pulsarEliminarEspacio();">
                        <input type="button" id="btnModificarEspacio"   name="btnModificarEspacio"  class="botonGeneral"    value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarEspacio();">
                    </div> 
                    <br><br>
                    <div class="botonera">
                        <input type="button" id="btnVolverEspacio" name="btnVolverEspacio" class="botonMasLargo" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();">
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            var tablaEspacios;
            var listaEspacios = new Array();
            var listaEspaciosTabla = new Array();
            <% 
            EspacioVO objectVO = null;
            List<EspacioVO> List = null;
            if(request.getAttribute("listaEspacios")!=null) {
                List = (List<EspacioVO>)request.getAttribute("listaEspacios");
            }
            if (List!= null && List.size() >0){
                for (int indice=0;indice<List.size();indice++) {
                    objectVO = List.get(indice);
                    String idEsp = "-";
                    if (objectVO.getIdEspSol() != null && !"".equals(objectVO.getIdEspSol())) {
                        idEsp = Integer.toString(objectVO.getIdEspSol());
                    }
                    String denominacion="-";
                    if(objectVO.getDenominacion()!=null){
                        denominacion=objectVO.getDenominacion();
                    }
                    String espAcred="-";
                    if(objectVO.getEspAcred()!=null){
                        espAcred=objectVO.getEspAcred().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>");
                    }
                    String espAutor="-";
                    if(objectVO.getEspAutor()!=null){
                        espAutor=objectVO.getEspAutor().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>");
                    }
                    String numAlumnos = "-";
                    if (objectVO.getNumAlumnos() != null && !"".equals(objectVO.getNumAlumnos())) {
                        numAlumnos = Integer.toString(objectVO.getNumAlumnos());
                    }
                    String alumNuevos = "-";
                    if (objectVO.getAlumNuevos() != null && !"".equals(objectVO.getAlumNuevos())) {
                        alumNuevos = Integer.toString(objectVO.getAlumNuevos());
                    }
            %>
            listaEspacios[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=idEsp%>', '<%=denominacion%>', '<%=espAcred%>', '<%=espAutor%>', '<%=numAlumnos%>', '<%=alumNuevos%>'];
            listaEspaciosTabla[<%=indice%>] = ['<%=espAcred%>', '<%=espAutor%>', '<%=numAlumnos%>', '<%=alumNuevos%>'];
            <% 
                } //for
            }// if
            %>
            crearTablaEspacios();
        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex)
                {}
            }
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {
        
            }
                //nuevas
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

            //Clase para internacionalizar los mensajes de la aplicaci�n.
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExpediente");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            
            function pulsarNuevoTecnico() {
                var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioAltaTec&tipo=0&numExp=<%=numExpediente%>';
                var result = null;
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 550, 1000, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaTecnico(result);
                        }
                    }
                });
            }

            function pulsarModificarTecnico() {
                if (TablaAnexoB.selectedIndex != -1) {
                    var urlParam = 'tarea=preparar&modulo=MELANBIDE65&operacion=cargarFormularioModTec&tipo=0&numExp=<%=numExpediente%>&id=' + listaTecnicos[TablaAnexoB.selectedIndex][0];
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?' + urlParam, 650, 1000, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaTecnico(result);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarExportarExcellTecnico() {
                        
              var parametros = "";
              parametros = '?tarea=preparar&modulo=MELANBIDE65&operacion=generarExcelAnexoB&tipo=0&numExp=<%=numExpediente%>';
              window.open(baseUrl + parametros, "_blank");
        }
            

            function pulsarEliminarTecnico() {
                if (TablaAnexoB.selectedIndex != -1) {
                    var eliminar = jsp_alerta('', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarFila")%>');
                    if (eliminar == 1) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        parametros = 'tarea=preparar&modulo=MELANBIDE65&operacion=eliminarTecnico&tipo=0&expediente=<%=numExpediente%>&identificador=' + listaTecnicos[TablaAnexoB.selectedIndex][0];
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
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var listaNueva = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            for (var j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaNueva[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                else if (hijos[j].nodeName == "FILA") {
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                        if (hijosFila[cont].nodeName == "ID") {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDO1") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDO2") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DNI") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECHAALTA") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPOCONTRATO") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPOR") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[9].toString();
                                                tex = tex.replace(".", ",");
                                                fila[9] = tex;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECHAALTAT") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[10] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECHABAJAT") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[11] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPOJORNADA") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[12] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PENSIONISTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[13].toString();
                                            fila[13] = tex;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    }
                                     else if (hijosFila[cont].nodeName == "TIPPENSIONISTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[14].toString();
                                            fila[14] = tex;
                                        } else {
                                            fila[14] = '-';
                                        }
                                    }
                                    }// for elementos de la fila
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaTecnico(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
                            } else if (codigoOperacion == "3" || codigoOperacion == "5") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                            } else if (codigoOperacion == "4") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                            } else if (codigoOperacion == "6") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                            } else if (codigoOperacion == "7") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                            } else if (codigoOperacion == "-1") {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaTecnico() {
                TablaAnexoB = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaAnexoB'), 880);
                TablaAnexoB.addColumna('250', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBNombreC")%>");
                TablaAnexoB.addColumna('70', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBDNI")%>");
                TablaAnexoB.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBPensionista")%>");
                TablaAnexoB.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBTipoPensionista")%>");
                TablaAnexoB.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBTipoContrato")%>");
                TablaAnexoB.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBFechaAlta")%>");
                TablaAnexoB.addColumna('75', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBFechaAltaT")%>");
                TablaAnexoB.addColumna('75', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBFechaBajaT")%>");
                TablaAnexoB.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBTipoJornada")%>");
                TablaAnexoB.addColumna('75', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"etiq.TablaAnexoBJornadaLaboralParcialPorcjB")%>");

                TablaAnexoB.displayCabecera = true;
                TablaAnexoB.height = 300;
            }

            function recargarTablaTecnico(result) {
                var fila;
                listaTecnicos = new Array();
                listaTecnTabla = new Array();
                for (var i = 1; i < result.length; i++) {
                    var sexo;
                    fila = result[i];
                    //listaTecnicos[i - 1] = fila;//NO funciona en IE9
                    listaTecnicos[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];

                    listaTecnTabla[i - 1] = [fila[2] + " " + fila[3] + ", " + fila[4], fila[6],fila[13] != 'null' ? fila[13] : "",fila[14] != 'null' ? fila[14] : "", fila[8] != -1 ? fila[8] : "",
                        fila[7], fila[10], fila[11], fila[12] != "-1" ? fila[12] : "", fila[9] != "-1,0" ? fila[9] : ""];
                }

                crearTablaTecnico();

                TablaAnexoB.lineas = listaTecnTabla;
                TablaAnexoB.displayTabla();
                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    try {
                        var div = document.getElementById('tablaAnexoB');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    } catch (err) {

                    }
                }
            }

        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide65/melanbide65.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    </head>
    <body>
        <div class="tab-page" id="tabPage652" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana652"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoB")%></h2>       
            <br/>
            <!--form-->
            <h2 class="legendAzul" id="pestanaPrincAnexoB"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalAnexoB")%></h2>
            <div> 
                <div>
                    <br/>
                    <div id="divGeneralAnexoB"  style="overflow-y: auto; overflow-x: hidden; height: 290px;">     
                        <div id="tablaAnexoB"  align="center"></div>
                    </div>
                    <br/><br>
                    <div class="botonera" style="text-align: center; margin-top: 100px">
                        <input type="button" id="btnNuevoAnexoB" name="btnNuevoAnexoB" class="botonGeneral"  value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoTecnico();">
                        <input type="button" id="btnModificarAnexoB" name="btnModificarAnexoB" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTecnico();">
                        <input type="button" id="btnEliminarAnexoB" name="btnEliminarAnexoB"   class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTecnico();">
                        <input type="button" id="btnExportarAnexoB" name="btnExportarAnexoB"   class="botonMasLargo" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarExcellTecnico();">

                    </div>
                </div>
            </div>
            <!--/form-->    
        </div>
        <script  type="text/javascript">
            var TablaAnexoB;
            var listaTecnicos = new Array();
            var listaTecnTabla = new Array();

            crearTablaTecnico();

            <%  		
               EncargadoVO objectVO = null;
               ArrayList<EncargadoVO> relacion = null;
               if(request.getAttribute("relacionTecnicos")!=null){
                   relacion = (ArrayList<EncargadoVO>)request.getAttribute("relacionTecnicos");
               }													
               if (relacion!= null && relacion.size() >0){
                   for (int indice=0;indice<relacion.size();indice++)
                   {
                       objectVO = relacion.get(indice);

            %>
            listaTecnicos[<%=indice%>] = ['<%=objectVO.getIdentificador()%>', "<%=objectVO.getApellido1()%>", "<%=objectVO.getApellido2()%>", "<%=objectVO.getNombre()%>",
                '<%=objectVO.getDni()%>', '<%=objectVO.getFecAltaContrIndef()%>'
                        , '<%=objectVO.getJornadaParcialPor()%>'];
            listaTecnTabla[<%=indice%>] = ["<%=objectVO.getApellido1()%>" + ' ' + "<%=objectVO.getApellido2()%>" + ', ' + "<%=objectVO.getNombre()%>",
                '<%=objectVO.getDni()%>','<%=objectVO.getDescPensionista()!=null?objectVO.getDescPensionista():""%>','<%=objectVO.getDescTipoPensionista()!=null?objectVO.getDescTipoPensionista():""%>', '<%=objectVO.getDescTipoContrato()%>', '<%=objectVO.getFecAltaAsStr()%>', '<%=objectVO.getFecAltaContrTempAsStr()%>', '<%=objectVO.getFecBajaContrTempAsStr()%>', '<%=objectVO.getDescTipoJornada()%>', '<%=objectVO.getJornadaParcialPor()!=-1.0?String.valueOf(objectVO.getJornadaParcialPor()).replace(".",","):""%>'
            ];

            <%
                   }// for
               }// if
            %>

            TablaAnexoB.lineas = listaTecnTabla;
            TablaAnexoB.displayTabla();
            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                try {
                    var div = document.getElementById('tablaAnexoB');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                } catch (err) {

                }
            }

        </script>
    </body>
</html>

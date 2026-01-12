<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
        <%
            EspecialidadesVO datModif = new EspecialidadesVO();
            EspecialidadesVO objectVO = new EspecialidadesVO();
            List<EspecialidadesVO> listaEspSol = new ArrayList<EspecialidadesVO>();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
            
            String codCertificado = "";
            String codigo = "";

            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try
                {
                    if (session != null) 
                    {
                        if (usuario != null) 
                        {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                }
                catch(Exception ex)
                {

                }

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                numExpediente    = request.getParameter("numero");
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (EspecialidadesVO)request.getAttribute("datModif");
                }
                if(request.getAttribute("listaEspSol") != null){
                    listaEspSol  = (List<EspecialidadesVO>)request.getAttribute("listaEspSol");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide50/RgcfmUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';

            var mensajeValidacion = '';

            var comboListaCertificados;
            var listaCodigosCertificados = new Array();
            var listaDescripcionesCertificados = new Array();

            var comboPresencial;
            var comboTeleformacion;
            var comboAcreditado;


            var listaSiNo = new Array();
            var listaCodigosSiNo = new Array();
            listaCodigosSiNo[0] = 0;
            listaCodigosSiNo[1] = 1;
            listaCodigosSiNo[2] = 3;
            listaSiNo[0] = '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.si")%>';
            listaSiNo[1] = '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.no")%>';
            listaSiNo[2] = '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.desistido")%>';

            function rellenardatEspModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoCertificado('<%=datModif.getCodCP() != null ? datModif.getCodCP() : ""%>');
                    buscaCodPresencial('<%=datModif.getInscripcionPresencial() != null ? datModif.getInscripcionPresencial() : ""%>');
                    buscaCodTeleformacion('<%=datModif.getInscripcionTeleformacion() != null ? datModif.getInscripcionTeleformacion() : ""%>');
                    buscaCodAcreditado('<%=datModif.getAcreditacion() != null ? datModif.getAcreditacion() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function guardarDatos() {
                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";

                    var motDeneg = $("input[name='codListaMotivos[]']:checked").map(function () {
                        return this.value;
                    }).get();

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=crearEspecialidad&tipo=0&numero=<%=numExpediente%>"
                                + "&codigocp=" + document.getElementById('codListaCertificados').value
                                + "&denominacion=" + document.getElementById('descListaCertificados').value
                                + "&presencial=" + document.getElementById('codPresencial').value
                                + "&teleformacion=" + document.getElementById('codTeleformacion').value
                                + "&acreditacion=" + document.getElementById('codAcreditado').value
                                + "&motDeneg=" + motDeneg;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarEspecialidad&tipo=0&numero=<%=numExpediente%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&codigocp=" + document.getElementById('codListaCertificados').value
                                + "&denominacion=" + document.getElementById('descListaCertificados').value
                                + "&presencial=" + document.getElementById('codPresencial').value
                                + "&teleformacion=" + document.getElementById('codTeleformacion').value
                                + "&acreditacion=" + document.getElementById('codAcreditado').value
                                + "&motDeneg=" + motDeneg;
                    }
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
                        var listaEspecialidades = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaEspecialidades[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[0] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_CODCP") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_DENOM") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_PRESE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_TELEF") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_ACRED") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "ESP_MOT_DENEG") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    }
                                }
                                listaEspecialidades[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(listaEspecialidades);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function buscaCodigoCertificado(codCertificado) {
                comboListaCertificados.buscaCodigo(codCertificado);
            }

            function buscaCodPresencial(codPresencial) {
                comboPresencial.buscaCodigo(codPresencial);
            }

            function buscaCodTeleformacion(codTeleformacion) {
                comboTeleformacion.buscaCodigo(codTeleformacion);
            }

            function buscaCodAcreditado(codAcreditado) {
                comboAcreditado.buscaCodigo(codAcreditado);
            }

            function cargarDatosCertificado() {
                var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
                buscaCodigoCertificado(codCertificadoSeleccionado);
            }

            function presencial() {
                //Recuperamos el valor seleccionado en el combo.
                if (document.getElementById("codPresencial") != null) {
                    var codigo = document.getElementById("codPresencial").value;
                    buscaCodPresencial(codigo);
                }
            }//teleformacion

            function teleformacion() {
                //Recuperamos el valor seleccionado en el combo.
                if (document.getElementById("codTeleformacion") != null) {
                    var codigo = document.getElementById("codTeleformacion").value;
                    buscaCodTeleformacion(codigo);
                }
            }//teleformacion

            function acreditado() {
                //Recuperamos el valor seleccionado en el combo.
                if (document.getElementById("codAcreditado") != null) {
                    var codigo = document.getElementById("codAcreditado").value;
                    if (codigo == "1") {
                        document.getElementById("divMotDeneg").style.display = "block";
                        $(".multiselect").scrollTop(0);
                    } else {
                        document.getElementById("divMotDeneg").style.display = "none";
                        $(".multiselect").cleanMultiselect();
                    }
                    buscaCodAcreditado(codigo);
                }
            }//acreditado

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;
                var codigocp = document.getElementById('codListaCertificados').value;
                if (codigocp == null || codigocp == '') {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "especialidades.msg.todosCamposOblig")%>';
                    return false;
                }

                var denominacion = document.getElementById('descListaCertificados').value;
                if (denominacion == null || denominacion == '') {
                    mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "especialidades.msg.todosCamposOblig")%>';
                    return false;
                }
                return correcto;
            }

            function compruebacodesp(codigocp) {
                var codcplista;
            <%
                if (listaEspSol!= null && listaEspSol.size() >0){
                    for (int indice=0;indice<listaEspSol.size();indice++)
                    {
                        objectVO = listaEspSol.get(indice);
            %>
                codcplista = '<%=objectVO.getCodCP()%>';
                if (codcplista != null && codcplista == codigocp) {
                    if (nuevo != null && nuevo == "0") {
                        var datModif = '<%=datModif != null && datModif.getCodCP()!= null ? datModif.getCodCP() : 0 %>';
                        if (datModif == codigocp)
                            return true;
                    }
                    return false;
                }
            <%
                    }// for
                }// if
            %>
                return true;
            }
            jQuery.fn.multiselect = function (clean) {
                $(this).each(function () {
                    var checkboxes = $(this).find("input:checkbox");
                    checkboxes.each(function () {
                        var checkbox = $(this);
                        // Highlight pre-selected checkboxes
                        if (checkbox.prop("checked"))
                            checkbox.parent().addClass("multiselect-on");

                        // Highlight checkboxes that the user selects
                        checkbox.click(function () {
                            if (checkbox.prop("checked"))
                                checkbox.parent().addClass("multiselect-on");
                            else
                                checkbox.parent().removeClass("multiselect-on");
                        });
                    });
                });
            };

            jQuery.fn.cleanMultiselect = function (clean) {
                $(this).each(function () {
                    var checkboxes = $(this).find("input:checkbox");
                    checkboxes.each(function () {
                        var checkbox = $(this);
                        checkbox.prop("checked", false);
                        checkbox.parent().removeClass("multiselect-on");
                    });
                });
            };
            $(function () {
                $(".multiselect").multiselect();
            });
        </script>
    </head>
    <body class="bandaBody">
        <div class="contenidoPantalla">
            <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.nuevaEspecialidadSol")%>
                        </span>
                    </div>

                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;" >
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.label.descEspecialidad")%>
                        </div>
                        <br>
                        <div>
                            <div class="etiqueta" >
                                <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.certificado")%>
                            </div>
                            <div>
                                <input type="text" name="codListaCertificados" id="codListaCertificados" size="12" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaCertificados"  id="descListaCertificados" size="90" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaCertificados" name="anchorListaCertificados" onclick="return false;">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario" style="display: none;">
                        <div class="etiqueta" style="width: 190px; float: left; font-weight: bold;" >
                            <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.inscripcion")%>
                        </div>
                        <br>
                        <div style="width: 450px; float: left;">
                            <div class="etiqueta" style="width: 290px; float: left;">
                                <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.presencial")%>
                            </div>
                            <div>
                                <input type="text" name="codPresencial" id="codPresencial" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                                <input type="text" name="descPresencial"  id="descPresencial" size="10" class="inputTexto" readonly="true" value=""/>
                                <a href="" id="anchorPresencial" name="anchorPresencial" onclick="return false;">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonPresencial"
                                         name="botonPresencial" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                        <br>                
                        <div style="width: 450px; float: left;">
                            <div class="etiqueta"  style="width: 290px; float: left;">
                                <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.teleformacion")%>
                            </div>
                            <div>
                                <input type="text" name="codTeleformacion" id="codTeleformacion" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                                <input type="text" name="descTeleformacion"  id="descTeleformacion" size="10" class="inputTexto" readonly="true" value=""/>
                                <a href="" id="anchorTeleformacion" name="anchorTeleformacion" onclick="return false;">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTeleformacion"
                                         name="botonTeleformacion" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario" >
                        <div style="width: 450px; float: left;">
                            <div class="etiqueta"  style="width: 190px; float: left;">
                                <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.acreditado")%>
                            </div>
                            <div>
                                <input type="text" name="codAcreditado" id="codAcreditado" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                                <input type="text" name="descAcreditado"  id="descAcreditado" size="10" class="inputTexto" readonly="true" value=""/>
                                <a href="" id="anchorAcreditado" name="anchorAcreditado" onclick="return false;">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAcreditado"
                                         name="botonAcreditado" height="14" width="14" border="0" style="cursor:hand;">
                                </a>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario" id="divMotDeneg" style="display:none;margin-bottom: 1em">
                        <div>
                            <div class="etiqueta" >
                                <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.especialidades.motivos_denegacion")%>
                            </div>
                            <div class="multiselect">
                                <!-- Lista con los certificados recuperados de la BBDD -->
                                <logic:iterate id="motivosDeneg" name="listaMotivos" scope="request">
                                    <label class='multiselectCheck'><input type='checkbox' name='codListaMotivos[]' value='<bean:write name="motivosDeneg" property="codigo" />'><bean:write name="motivosDeneg" property="descripcion" /></label>
                                    </logic:iterate>
                            </div>
                        </div>
                    </div>


                    <br><br>                     
                    <div class="botonera">
                        <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();">
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                    </div>
                </div>
            </form>
        </div>

        <script type="text/javascript">

            listaCodigosCertificados[0] = "";
            listaDescripcionesCertificados[0] = "";

            /* Lista con los certificados recuperados de la BBDD */
            var contador = 0;
            <logic:iterate id="certificados" name="listaCertificados" scope="request">
            listaCodigosCertificados[contador] = ['<bean:write name="certificados" property="codCertificado" />'];
            listaDescripcionesCertificados[contador] = ['<bean:write name="certificados" property="descCertificadoC" />'];
            contador++;
            </logic:iterate>

            var comboListaCertificados = new Combo("ListaCertificados");
            comboListaCertificados.addItems(listaCodigosCertificados, listaDescripcionesCertificados);
            comboListaCertificados.change = cargarDatosCertificado;

            var comboPresencial = new Combo("Presencial");
            comboPresencial.addItems(listaCodigosSiNo, listaSiNo);
            comboPresencial.change = presencial;

            var comboTeleformacion = new Combo("Teleformacion");
            comboTeleformacion.addItems(listaCodigosSiNo, listaSiNo);
            comboTeleformacion.change = teleformacion;

            var comboAcreditado = new Combo("Acreditado");
            comboAcreditado.addItems(listaCodigosSiNo, listaSiNo);
            comboAcreditado.change = acreditado;

            var nuevo = "<%=nuevo%>";
            if (nuevo == 0) {
                rellenardatEspModificar();
            }

            //seleccionar valores dejados en la request (sólo llegan valores al modificar)
            <logic:present name="codListaMotivos">
                <logic:iterate id="codListaMotivos" name="codListaMotivos" scope="request">
            $("input:checkbox[name='codListaMotivos[]'][value='" + <bean:write name="codListaMotivos"/> + "']").attr('checked', 'checked');
                </logic:iterate>
            </logic:present>
        </script>
    </body>
</html>


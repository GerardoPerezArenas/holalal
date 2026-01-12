<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";

    String sIdioma = request.getParameter("idioma");
    idiomaUsuario = Integer.parseInt(sIdioma);
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
    }
    catch(Exception ex) {
    }
     //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");

    String numDoc = (String)request.getAttribute("numDoc");
    String codTram = request.getParameter("codigoTramite");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide50/TablaNueva.js"></script><!--OCULTAR COLUMNAS-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<body>
    <div class="tab-page" id="tabPage332" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana332"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentos")%></h2>
        <script type="text/javascript">
    tp1_p332 = tp1.addTabPage(document.getElementById("tabPage332"));
        </script>
        <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide50I18n.getMensaje(idiomaUsuario, "label.documentos")%></label>
        <div id="listDocumentos" align="center" onload="cargarDatos();" ></div>
        <!--<input type="button" name="btnAltaRegistro" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.botonDocumentos")%>" class="botonLargo" onclick="pulsarBoton()" />-->

        <fieldset id="fieldsetBuscador" name="fieldsetBuscador" style="width: 20%; margin-top:20px; display: none;">
            <legend class="legendAzul"><%=meLanbide50I18n.getMensaje(idiomaUsuario, "label.BuscadorDocumentos")%></legend>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;" class="etiqueta">
                    <%=meLanbide50I18n.getMensaje(idiomaUsuario, "label.nombredocumento")%>
                </div>
                <input type="text" name="txtnombredocumentacion" value="" class="inputTexto"  size="300px"  id="txtnombredocumentacion">
            </div>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;" class="etiqueta">
                    <%=meLanbide50I18n.getMensaje(idiomaUsuario, "label.fechaBuscador")%>
                </div>
                <div style="width: 150px; float: left;">
                    <div style="float: left;">
                        <input type="text" class="inputTxtFecha"
                               id="txtfechadocumento" name="txtfechadocumento"
                               maxlength="10"  size="10"
                               value=""
                               onkeyup = "return SoloCaracteresFechaLanbide(this);"
                               onfocus="javascript:this.select();"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaAB(event);return false;" style="text-decoration:none;">
                            <IMG style="border: 0px solid" height="17" id="calFecha" name="calFecha" border="0"
                                 src="<c:url value='/images/calendario/icono.gif'/>" >
                        </A>
                    </div>
                </div>
            </div>
        </fieldset>
        <div class="lineaFormulario" align="center" style="margin-bottom: 10px">
            <input type="button" name="btnBuscarDocumento" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentosbuscar")%>" class="botonLargo" onclick="buscar()" />
        </div>
        <br><br>
        <div id="listDocumentos2" align="center"></div>
        <div id="divBotonConsultar" style="display:none">
            <input type="button" name="btnConsultarDocumento" id="btnConsultarDocumento" class="botonLargo" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.documentosConsultar")%>" onclick="pulsarBotonConsultar()" />
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabDocumentos;
    var listDocumentos;
    var listDocumentosTabla;
    var documentoenviado;
    var listDocumentos2;
    var listDocumentosTabla2;
    var tabDocumentosBusq;
    var tipoDocumental;
    var fechaDocAnterior;

    $(document).ready(function () {
//Tabla Documentos
//debugger;
        listDocumentos = new Array();
        listDocumentosTabla = new Array();
        tabDocumentos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listDocumentos'));
        tabDocumentos.addColumna('200', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentos.col1")%>", "", false); //"TIPO_DOCUMENTO"
        tabDocumentos.addColumna('500', 'left', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentos.col2")%>", "", false);//"NOMBRE_DOCUMENTO"
        tabDocumentos.addColumna('200', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentos.col3")%>", "", false);//"ORGANO"
        tabDocumentos.addColumna('100', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentos.col4")%>", "", false);//"FECHA"
        tabDocumentos.addColumna('100', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentos.col5")%>", "", true);//"TIPO DOCUMENTAL"
        tabDocumentos.displayCabecera = true;
        tabDocumentos.height = 150;
    <%
   DocumentosVO objectVO = null;
   List<DocumentosVO> List = (List<DocumentosVO>)request.getAttribute("listDocumentos");
   if (List!= null && List.size() >0){
       for (int indice=0;indice<List.size();indice++) {
           objectVO = List.get(indice);

    %>
        listDocumentosTabla[<%=indice%>] = ['<%=objectVO.getTIPO_DOCUMENTO()%>', '<%=objectVO.getNOMBRE_DOCUMENTO()%>', '<%=objectVO.getORGANO()%>', '<%=objectVO.getFECHA()%>', '<%=(objectVO.getTIPO_DOCUMENTAL()!= null ? objectVO.getTIPO_DOCUMENTAL() : "")%>'];
        listDocumentos[<%=indice%>] = ['<%=objectVO.getTIPO_DOCUMENTO()%>', '<%=objectVO.getNOMBRE_DOCUMENTO()%>', '<%=objectVO.getORGANO()%>', '<%=objectVO.getFECHA()%>', '<%=(objectVO.getTIPO_DOCUMENTAL()!= null ? objectVO.getTIPO_DOCUMENTAL() : "")%>'];
    <%
       }// for
   }// if
    %>


        tabDocumentos.lineas = listDocumentosTabla;
        tabDocumentos.displayTabla();
    });

    function buscar() {
        if (tabDocumentos.selectedIndex !== -1) {
            var nombreDocumentoD = listDocumentos[tabDocumentos.selectedIndex][1];
            var fechaDocumentoD = listDocumentos[tabDocumentos.selectedIndex][3];
            var tipoDocumentalD;
            var asuntoDocumental = listDocumentos[tabDocumentos.selectedIndex][4];
            if (asuntoDocumental.indexOf('.') !== -1) {
                //alert("tiene punto");
                var res = asuntoDocumental.split(".");
                tipoDocumentalD = res[0];
            }

            var numeroexpediente = "<%=numExpediente%>";

            documentoenviado = tipoDocumentalD + "," + asuntoDocumental + "," + nombreDocumentoD + "," + fechaDocumentoD + "," + numeroexpediente;

            var ajax = getXMLHttpRequest();
            var nodos = null;
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=BusquedaDocumentosAportadosDokusi&tipo=0&numero=" + documentoenviado;
            //alert(parametros);
            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
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
                listDocumentos2 = new Array();
                listDocumentosTabla2 = new Array();
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        //listaNueva[j] = codigoOperacion;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                    else if (hijos[j].nodeName == "FILA") {
                        nodoFila = hijos[j];
                        hijosFila = nodoFila.childNodes;
                        for (var cont = 0; cont < hijosFila.length; cont++) {
                            if (hijosFila[cont].nodeName == "TITULO") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    listDocumentosTabla2.push(fila[0]);
                                } else {
                                    fila[0] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    listDocumentosTabla2.push(fila[1]);
                                } else {
                                    fila[1] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "FECHA") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    listDocumentosTabla2.push(fila[2]);
                                } else {
                                    fila[2] = '-';
                                }
                            } else if (hijosFila[cont].nodeName == "ID_DOCUMENTO") {
                                if (hijosFila[cont].childNodes.length > 0) {
                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    listDocumentosTabla2.push(fila[3]);
                                } else {
                                    fila[3] = '-';
                                }
                            }
                        }
                        listaNueva[j - 1] = fila;
                        listDocumentosTabla2[j - 1] = fila;
                        listDocumentos2[j - 1] = fila;
                        fila = new Array();

                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion != null && codigoOperacion != undefined && codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"msg.consulta.documentos.noTipoDocumental")%>');
                } else if (codigoOperacion != null && codigoOperacion != undefined && codigoOperacion.indexOf("2") == 0) {
                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"msg.consulta.documentos.error.dokusi")%>' + ' - ' + codigoOperacion);
                }



                tabDocumentosBusq = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listDocumentos2'));
                tabDocumentosBusq.addColumna('200', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentosBusq.col1")%>", "", false); //"TIPO_DOCUMENTO"
                tabDocumentosBusq.addColumna('500', 'left', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentosBusq.col2")%>", "", false);//"NOMBRE_DOCUMENTO"
                tabDocumentosBusq.addColumna('100', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentosBusq.col3")%>", "", false);//"FECHA_CREACION"
                tabDocumentosBusq.addColumna('100', 'center', "<%= meLanbide50I18n.getMensaje(idiomaUsuario,"documentos.tablaDocumentosBusq.col4")%>", "", true);//"ID_DOCUMENTO"

                tabDocumentosBusq.displayCabecera = true;
                tabDocumentosBusq.height = 150;

                tabDocumentosBusq.lineas = listaNueva;
//                tabDocumentosBusq.dblClkFunction = 'callFromTableTo';
                tabDocumentosBusq.displayTabla();


                if (listaNueva.length > 0) {
                    document.getElementById("divBotonConsultar").style.display = 'block';
                } else {
                    document.getElementById("divBotonConsultar").style.display = 'none';
                }

            } catch (Err) {
                alert(Err);
            }
        } else {
            jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarBoton() {
        //alert("Pulsado boton ");
        //jsp_alerta("A", "Pulsado boton");
        if (tabDocumentos.selectedIndex != -1) {
            var tipoDocAnterior = listDocumentos[tabDocumentos.selectedIndex][0];
            var nombreDocAnterior = listDocumentos[tabDocumentos.selectedIndex][1];
            fechaDocAnterior = listDocumentos[tabDocumentos.selectedIndex][3];
            tipoDocumental = listDocumentos[tabDocumentos.selectedIndex][4];
            if (tipoDocumental.indexOf('.') != -1) {
                //alert("tiene punto");
                var res = tipoDocumental.split(".");
                tipoDocumental = res[0];
            } else {
                tipoDocumental
            }

            // alert("Nuevo tipo documental");


            var codigo = tabDocumentos.selectedIndex;
            // alert("Tipo Documental"+ tipoDocumental+" Hay que tratarlo hasta le puto. si lo hay");

            //alert("pulsado boton " + tipoDocAnterior + " " + nombreDocAnterior + " " + organoDocAnterior + " " + fechaDocAnterior);
            //alert("Enviamos datos al formulario.");
            //document.getElementById("txtipodocumentacion").value = tipoDocumental;
            document.getElementById("txtnombredocumentacion").value = nombreDocAnterior;
            //document.getElementById("txtorgano").value = organoDocAnterior;
            document.getElementById("txtfechadocumento").value = fechaDocAnterior;


        }
    }


    function pulsarBotonConsultar() {
        // alert("Pulsado boton ");
        var iddocumentoDossier;
        if (tabDocumentosBusq.selectedIndex != -1) {
            //var tipoDocAnterior = listDocumentos[tabDocumentos.selectedIndex][0];
            iddocumentoDossier = listDocumentosTabla2[tabDocumentosBusq.selectedIndex][3];
            // alert("Documento id dossier"+ iddocumentoDossier);
            var numeroexpediente = "<%=numExpediente%>";
            var documentoenviado2 = numeroexpediente + "," + iddocumentoDossier;
            var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
            var parametros = "?tarea=preparar&modulo=MELANBIDE50&operacion=ConsultaDocumentosAportadosDokusi&tipo=0&numero=" + documentoenviado2;
            //document.forms[0].target = "ocultoPendientesAPA";
            document.forms[0].action = url + parametros;
            document.forms[0].submit();
        } else {
            jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }



    if (navigator.appName.indexOf("Internet Explorer") != -1) {
        try {
            var div = document.getElementById('listDocumentos');
            div.children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[0].children[1].style.width = '100%';
        } catch (err) {

        }
    }

    function mostrarCalFechaAB(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFecha").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'txtfechadocumento', null, null, null, '', 'calFecha', '', null, null, null, null, null, null, null, null, evento);

    }
</script>
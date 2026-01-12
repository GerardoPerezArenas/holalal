<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.i18n.MeLanbide36I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide36.util.MeLanbide36Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config" %>
<%@page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
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

    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide36I18n meLanbide36I18n = MeLanbide36I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String desdeListarExpM36    = request.getParameter("desdeListarExpM36");
    
    int totDias = 0;
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<!--meta http-equiv="X-UA-Compatible" content="IE=edge" /-->
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide36/melanbide36.css'/>">
<script type="text/javascript">
    var tabListaExpedientesRel;
    var listalistaExpedientesRel = new Array();
    var listalistaExpedientesRelTabla = new Array();

    // --- recibo los NIF de rol de persona sustituida y rol de persona contratada (si los hay) ---- Rosa 2018/01/16 -----------
    //var NIFSustituida = <//%=request.getAttribute("NIFSustituida")%>;
    //alert("NIFSustituida: " + NIFSustituida);
    //var NIFContratada = <//%=request.getAttribute("NIFContratada")%>;
    //alert("NIFContratada: " + NIFContratada);
    // ---- ----- - - - --- ---- ---- ----- ---- ---- --
    <%  		
        FilaExpedienteVO act = null;
        
        // ---- recojo el atributo con la lista de expedientes ------------------------
        List<FilaExpedienteVO> expRel = (List<FilaExpedienteVO>)request.getAttribute("listaExpedientesRelacionados");
        if (expRel!= null && expRel.size() >0){
            for (int i = 0; i <expRel.size(); i++)
            {
                act = expRel.get(i);
                totDias += act.getNumDias();        
    %>
    listalistaExpedientesRel[<%=i%>] = ['<%=act.getNumExpedienteP29()%>', '<%=act.getNumExpediente()%>', '<%=MeLanbide36Utils.integerToFormattedString(act.getNumDias())%>', '<%=MeLanbide36Utils.doubleToFormattedString(act.getImporteAbonado())%>', '<%=act.getSituacion()%>', '<%=act.getTipoSubvencion()%>', '<%=act.getFecNacPersDepen()%>', '<%=act.getFeIni()%>', '<%=act.getFeFin()%>'];
    listalistaExpedientesRelTabla[<%=i%>] = ['<%=act.getNumExpedienteP29()%>', '<%=act.getNumExpediente()%>', '<%=MeLanbide36Utils.integerToFormattedString(act.getNumDias())%>', '<%=MeLanbide36Utils.doubleToFormattedString(act.getImporteAbonado())%>', '<%=act.getSituacion()%>', '<%=act.getTipoSubvencion()%>', '<%=act.getFecNacPersDepen()%>', '<%=act.getFeIni()%>', '<%=act.getFeFin()%>'];
    <%
            }// for
        }// if
    %>
    function cargarExpedienteRelacionado(rowID) {
        //alert(rowID);
        //alert(tabListaExpedientesRel.id);
        var tipoEntradaForm = <%=desdeListarExpM36%>;
        if ("1" == tipoEntradaForm) {
            jsp_alerta("A", "Accion no valida, estas en modo consulta. Vuelve al expediente Original y consulta de nuevo el expediente relacionado que deseas.");
        } else {
            var indice = tabListaExpedientesRel.selectedIndex;
            if (indice != undefined && indice != null && indice >= 0) {
                rowID = indice;
                var numExp = listalistaExpedientesRel[rowID][1];
                var datos = numExp.split("/");
                var codProcedimientoAnte = document.forms[0].codProcedimiento.value;
                var ejercicioAnte = document.forms[0].ejercicio.value;
                var numeroAnte = document.forms[0].numero.value;

                //document.forms[0].codProcedimiento.value = datos[1]; // No deberia cambiar pero por si acaso
                //document.forms[0].ejercicio.value = datos[0]; // Puede cambiar, expedientes en diferentes años
                //document.forms[0].numero.value = numExp;
                //document.forms[0].opcion.value="cargar";
                //document.forms[0].target="mainFrame";
                //document.forms[0].modoConsulta.value="si";
                /*
                 * Codigo desde Expedientes Relacionados
                 */
                var argumentos = new Array();
                var source = "<c:url value='/sge/FichaExpediente.do'/>" + "?modoConsulta=si";
                var nCS = document.forms[0].codMunicipio.value;
                var codEjercicio = datos[0];//document.forms[0].ejercicio.value;
                var codProc = datos[1];//document.forms[0].numExpediente.value;
                var codTram = "si"; //document.forms[0].modoConsulta.value;
                var desdeConsulta = "no"; //document.forms[0].modoConsulta.value;
                // desdeAltaRE la ponermos como si, para que al pulsar volver, solo cierre la ventana, no haga mas operaciones-
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + source + "&codMunicipio=" + nCS + "&ejercicio=" +
                        codEjercicio + "&numero=" + numExp + "&codProcedimiento=" + codProc + "&desdeConsulta=" + desdeConsulta + "&desdeAltaRE=si"
                        + "&opcion=cargar&desdeListarExpM36=1",
                        argumentos, 'width=990,height=650,status=' + '<%=statusBar%>' + '',
                        function (datos) {
                            // No hacemos recarga del expediente. 
                            /*
                             document.forms[0].codProcedimiento.value = codProcedimientoAnte;
                             document.forms[0].ejercicio.value = ejercicioAnte;
                             document.forms[0].numero.value = numeroAnte;
                             document.forms[0].modoConsulta.value="no";
                             document.forms[0].opcion.value="cargar";
                             document.forms[0].target="mainFrame";
                             document.forms[0].action="< c:url value='/sge/FichaExpediente.do'/>"; // + "?modoConsulta=" + document.forms[0].modoConsulta.value + "&desdeAltaRE=" + desdeAltaRE + "&desdeExpRel=no";            
                             document.forms[0].submit();
                             */
                        });
                // Fin Expedientes relacionados

                //alert('Expediente ' + numExp);
                //alert('Ejercicio: ' + datos[0]);
                //alert('Procedimiento ' + datos[1]);
                //alert('Numero ' + datos[2]);
                // document.forms[0] Es rl formularios que se defines en la jsp fichaExpediente.jsp (< html:form action="/sge/FichaExpediente.do" method="post">)
                //document.forms[0].codMunicipio.value  // Ests valor no se modifica, no deberiamos cambiar de esquema en BBDD
                // Codigo desplegado en pro.
                /*
                 pleaseWait('on');
                 
                 document.forms[0].codProcedimiento.value = datos[1]; // No deberia cambiar pero por si acaso
                 document.forms[0].ejercicio.value = datos[0]; // Puede cambiar, expedientes en diferentes años
                 document.forms[0].numero.value = numExp;
                 document.forms[0].opcion.value="cargar";
                 document.forms[0].target="mainFrame"; // Hay que poner esta opcion sino no cambia el numExp del frame de arriba y no coge bien los  valores.
                 document.forms[0].action="< c:url value='/sge/FichaExpediente.do'/>";
                 document.forms[0].submit();
                 */
            } else {
                jsp_alerta("A", "No se ha seleccionado ninguna fila");
            }
        }
    }
</script>

<body>
    <div class="tab-page" id="tabPage361" style="height: 100%; width: 100%;margin-top: 2.5em;">
        <h2 class="tab" id="pestana361"><%=meLanbide36I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">var tp1_p361 = tp1.addTabPage(document.getElementById("tabPage361"));</script>        
        <div style="clear: both;">
            <table>
                <tr>
                    <td>
                        <div id="listaExpedientesRel"  style="padding: 5px; width:930px; height: 350px; text-align: center; margin:0px;margin-top:0px;" align="center">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right;">
                        <input type="button" id="btnCargarExpediente" name="btnCargarExpediente" class="botonMasLargo" onclick="cargarExpedienteRelacionado(-1);" value="<%=meLanbide36I18n.getMensaje(idiomaUsuario, "btn.cargar.ExpteRelacionado")%>"/>
                    </td>
                </tr>
            </table>
            <div style="clear: both; float: left; width: 930px; text-align: right; margin-top: 4em; margin-bottom:1em;">
                <!-- etiquetas y cuadros de texto para los NIF de persona sustituida y contratada -- y boton de carga -- Rosa 2018/01/16 ----------->
                <label><%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.NIFSustituida")%></label>
                <input type="text" id="NIFSust" name="NIFSust" value="<%=request.getAttribute("NIFSustituida")%>" size="10" maxlength="9" style="margin-left: 5px; margin-right: 20px; text-align: left"/>
                <label><%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.NIFContratada")%></label>
                <input type="text" id="NIFContr" name="NIFContr"  value="<%=request.getAttribute("NIFContratada")%>" size="10" maxlength="9" style="margin-left: 5px; margin-right: 30px; text-align: left"/>
                <input type="button" id="btnCargar" name="btnCargar" class="botonGeneral" value="<%=meLanbide36I18n.getMensaje(idiomaUsuario, "btn.cargar")%>" style="margin-right: 40px;" 
                       onclick="cargarTerceros(true);"/> 
                <!-- ---- ---- ----- ---- ---- -->
                <label><%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.totalDias")%></label>
                <input type="text" value="<%=MeLanbide36Utils.integerToFormattedString(totDias)%>" size="13" style="margin-left: 5px; text-align: right;" disabled="true"/>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">

    tabListaExpedientesRel = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaExpedientesRel'), 924);
    tabListaExpedientesRel.addColumna('160', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col1")%>');
    tabListaExpedientesRel.addColumna('140', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col2")%>');
    tabListaExpedientesRel.addColumna('60', 'right', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col3")%>');
    tabListaExpedientesRel.addColumna('70', 'right', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col4")%>');
    tabListaExpedientesRel.addColumna('65', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col5")%>');
    tabListaExpedientesRel.addColumna('165', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col6")%>');
    tabListaExpedientesRel.addColumna('70', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col7")%>');
    tabListaExpedientesRel.addColumna('70', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col8")%>');
    tabListaExpedientesRel.addColumna('70', 'left', '<%=meLanbide36I18n.getMensaje(idiomaUsuario, "label.listaExpedientes.col9")%>');


    tabListaExpedientesRel.height = '410';

    tabListaExpedientesRel.displayCabecera = true;
    tabListaExpedientesRel.lineas = listalistaExpedientesRelTabla;
    tabListaExpedientesRel.displayTabla();

    // funcion que carga el tercero sustituido y contratado en el expediente
    function cargarTerceros() {
        var numExpediente = document.getElementById("numExpediente").value;
        var NIFSust = document.getElementById("NIFSust").value;
        var NIFContr = document.getElementById("NIFContr").value;

        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();

        parametros = "tarea=preparar&modulo=MELANBIDE36&operacion=cargarTerceros&tipo=0"
                + "&numExpediente=" + numExpediente
                + "&NIFSust=" + NIFSust
                + "&NIFContr=" + NIFContr
                + "&control=" + control.getTime();

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
                }   //if(navigator.appName.indexOf("Internet Explorer")!=-1)
            }    //if (ajax.readyState==4 && ajax.status==200)
            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue;
            // ---------- muestro el mensaje de OK o ERROR y recargo el expediente en el caso de que pueda haber habido una actualización de datos -----
            if (codigoOperacion == "0") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"msg.NIFsgrabadosOK")%>');
                recargarExpediente(0);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorGrabarNIFSust")%>');
//            }else if(codigoOperacion=="2"){
//                jsp_alerta("A",'<//%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorNIFSustExiste")%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorNIFSustnoExiste")%>');
            } else if (codigoOperacion == "5") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorNIFiguales")%>');
            } else if (codigoOperacion == "6") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorGrabarNIFCont")%>');
                recargarExpediente(0);
            } else if (codigoOperacion == "7") {
                recargarExpediente(0);
//                jsp_alerta("A",'<//%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorNIFContrExiste")%>');
            } else if (codigoOperacion == "8") {
                jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorNIFContrnoExiste")%>');
                recargarExpediente(0);
            }
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide36I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        } //try-catch               
    }

    function recargarExpediente(i) {
        pleaseWait('on');
        var numExpediente = document.getElementById("numExpediente").value;
        var datos = numExpediente.split("/");
        //document.forms[0].codMunicipio.value = listaOriginalE[i][0];
        document.forms[0].codProcedimiento.value = datos[1];       //listaOriginalE[i][1];
        document.forms[0].ejercicio.value = datos[0];              //listaOriginalE[i][3];
        document.forms[0].numero.value = numExpediente;            //listaOriginalE[i][4];
        document.forms[0].opcion.value = "cargar";
        document.forms[0].target = "mainFrame";
        // original
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        //document.forms[0].action="<//c:url value='/sge/FichaExpediente.do'/>?filtro="+filtroPendientes;
        document.forms[0].submit();
    }

    // Añadimos este codigo para poder asignar un metodo personalizado el evento dobleclick de la tabla 
    // De otra manera va  llamar a la funcion callFromTableTo que ya esta redifinida en la fichaExpediente 
    // nosotros estamos dentro de esa jsp por tanto no podemos redefinirla
    /*
     $("#listaExpedientesRel tbody tr").on("dblclick", function() {
     cargarExpedienteRelacionado($("#listaExpedientesRel tbody tr").index(this));
     });
     */
    var dbclick = "0";
    // Redifinimos la funcion del js NuevaTabla y comprobamos que se invoque desde la tabla de expedientes relacionados del M36
    function rellenarDatos1(tableName, rowID) {
        alert(tableName + ' - ' + rowID);
        var continuar = true;
        if (tableName != null && tableName != undefined) {
            try {
                var ide = tableName.id;
            } catch (Err) {
                alert('Error al leer el ID de la tabla' + Err.message);
                continuar = false;
            }
            if (continuar) {
                if (tableName.id != undefined && tableName.id != null) {
                    // Si es la tabla del M36, como salta primero el evento de selccion de linea
                    // renombranos el evento de doble click para que se llame solo en caso de doble Click
                    if (tabListaExpedientesRel.id == tableName.id) {
                        if ("1" == dbclick) {
                            cargarExpedienteRelacionado(rowID);
                        }
                    }
                }
            }
        }
    }

</script>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.i18n.MeLanbide84I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConstantesMeLanbide84"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<head>
    <%         
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idiomaUsuario = 1;
        int apl = 5;
        String css = "";
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idiomaUsuario = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
        }

        //Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide84I18n meLanbide84I18n = MeLanbide84I18n.getInstance();
        String numExpediente = (String)request.getAttribute("numExp");
      //  String estaEn110 = (String)request.getAttribute("estaEn110");
        boolean enTramite = request.getAttribute("enTramite") != null ? (Boolean)request.getAttribute("enTramite"): true;
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide84/melanbide84.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide84/EntapUtils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript">
        var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
        var enTramiteInicio = <%=enTramite%>;

        function pulsarNuevaPersona() {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE84&operacion=cargarNuevaPersona&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 600, 1200, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaEntap(result[1]);
                    }
                }
            });
        }

        function pulsarModificarPersona() {
            if (tablaEntap.selectedIndex != -1) {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE84&operacion=cargarModificarPersona&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaEntap[tablaEntap.selectedIndex][0], 600, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaEntap(result[1]);
                        }
                    }
                });
            } else {
                jsp_alerta('A', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarEliminarPersona() {
            if (tablaEntap.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    elementoVisible('on', 'barraProgresoCargaXML');
                    var parametros = 'tarea=preparar&modulo=MELANBIDE84&operacion=eliminarPersona&tipo=0&numExp=<%=numExpediente%>&id=' + listaEntap[tablaEntap.selectedIndex][0];
                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaActualizarPestana,
                            error: mostrarErrorEliminarTrabajador
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoCargaXML');
                        mostrarErrorPeticion();
                    }
                }
            } else {
                jsp_alerta('A', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function crearTablaEntap() {
            tablaEntap = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaEntap'));

            tablaEntap.addColumna('0', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.id")%>");// fila0
            tablaEntap.addColumna('100', 'left', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.programa")%>");// fila1
            tablaEntap.addColumna('200', 'left', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.nombreCompleto")%>"); // nombreCompleto = APE1 + [APE2] + NOMBRE => 3-[4]-2
            tablaEntap.addColumna('100', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.dni")%>"); // fila5
            tablaEntap.addColumna('100', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.fecNac")%>");// fila6
            tablaEntap.addColumna('100', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.sexo")%>");// fila 16             
            tablaEntap.addColumna('250', 'left', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.direccion")%>"); // direccion = CALLE + NUMERO + LETRA + PLANTA + PUERTA => 7-8-9-10
            tablaEntap.addColumna('150', 'left', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.poblacion")%>"); // poblacion = CP + LOCALIDAD => 11-12
            tablaEntap.addColumna('150', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.nivel")%>");// fila 13
            tablaEntap.addColumna('100', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.fecInicio")%>");// fila 14
            tablaEntap.addColumna('100', 'center', "<%=meLanbide84I18n.getMensaje(idiomaUsuario,"persona.fecSolicitud")%>");// fila 15

            tablaEntap.displayCabecera = true;
            tablaEntap.height = 360;
        }

        function recargarTablaEntap(personas) {
            listaEntap = new Array();
            listaEntapTabla = new Array();
            var direccion;
            for (var i = 0; i < personas.length; i++) {
                var per = personas[i];
                listaEntap[i] = [
                    per.id,
                    per.programa,
                    per.nombre,
                    per.apel1,
                    (per.apel2 != undefined ? per.apel2 : ''),
                    per.dni,
                    per.fechaNacimiento,
                    per.fecNacStr,
                    per.sexo,
                    per.descSexo,
                    per.calle,
                    (per.numero != undefined ? per.numero : ''),
                    (per.letra != undefined ? per.letra : ''),
                    (per.planta != undefined ? per.planta : ''),
                    (per.puerta != undefined ? per.puerta : ''),
                    per.codPost,
                    per.localidad,
                    per.nivelAcademico,
                    per.descNivel,
                    per.fechaInicio,
                    per.fecIniStr,
                    per.fechaSolicitud,
                    per.fecSolStr
                ];

                direccion = per.calle + (per.numero != undefined ? (' nº ' + per.numero) : '') + (per.letra != undefined ? per.letra : '') + ' ' + (per.planta != undefined ? per.planta : '') + ' ' + (per.puerta != undefined ? per.puerta : '');

                listaEntapTabla[i] = [
                    per.id,
                    per.programa,
                    per.apel1 + ' ' + (per.apel2 != undefined ? per.apel2 : '') + ', ' + per.nombre,
                    per.dni,
                    per.fecNacStr,
                    per.descSexo,
                    direccion,
                    per.codPost + ' - ' + per.localidad,
                    per.descNivel,
                    per.fecIniStr,
                    per.fecSolStr
                ];
            }
            crearTablaEntap();
            tablaEntap.lineas = listaEntapTabla;
            tablaEntap.displayTabla();
        }

        function pulsarCargarXML() {
            var hayFicheroSeleccionado = false;
            if (document.getElementById('fichero_xml').files) {
                if (document.getElementById('fichero_xml').files[0]) {
                    hayFicheroSeleccionado = true;
                }
            } else if (document.getElementById('fichero_xml').value != '') {
                hayFicheroSeleccionado = true;
            }
            if (hayFicheroSeleccionado) {
                var extension = document.getElementById('fichero_xml').value.split('.').pop().toLowerCase();
                if (extension != 'xml') {
                    var resultado = jsp_alerta('A', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                    return false;
                }
                var resultado = jsp_alerta('', '<%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE84&operacion=procesarXML&tipo=0&numero=<%=numExpediente%>';
                    document.forms[0].action = url + '?' + parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].encoding = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    document.forms[0].target = 'uploadFrameCarga';
                    document.forms[0].submit();
                }
                return true;
            } else {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                return false;
            }
        }

        function pulsarIniciarExpedientes() {
            elementoVisible('on', 'barraProgresoCargaXML');
            var parametros = 'tarea=preparar&modulo=MELANBIDE84&operacion=iniciarExpedientes&tipo=0&numero=<%=numExpediente%>';
            try {
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: true,
                    data: parametros,
                    success: procesarRespuestaInicioExpedientes,
                    error: mostrarErrorInicioExpedientes
                });
            } catch (Err) {
                elementoVisible('off', 'barraProgresoCargaXML');
                mostrarErrorPeticion();
            }
        }

        function actualizarPestana() {
            elementoVisible('on', 'barraProgresoCargaXML');
            var parametros = 'tarea=preparar&modulo=MELANBIDE84&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
            try {
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: true,
                    data: parametros,
                    success: procesarRespuestaActualizarPestana,
                    error: mostrarErrorActualizarPestana
                });
            } catch (Err) {
                elementoVisible('off', 'barraProgresoCargaXML');
                mostrarErrorPeticion();
            }
        }

        function actualizarTablaEntap() {
            try {
                actualizarPestana();
                limpiarFormulario();
            } catch (err) {
            }
        }

        function limpiarFormulario() {
            document.getElementById('fichero_xml').value = "";
        }

        // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
        function procesarRespuestaActualizarPestana(ajaxResult) {
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            if (codigoOperacion == "0") {
                var personas = datos.tabla.lista;
                if (personas.length > 0) {
                    elementoVisible('off', 'barraProgresoCargaXML');
                    recargarTablaEntap(personas);
                } else {
                    mostrarErrorPeticion();
                }
            } else {
                mostrarErrorPeticion(codigoOperacion);
            }
        }

        function procesarRespuestaInicioExpedientes(ajaxResult) {
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            var expIniciados = new Array();
            if (codigoOperacion == "0" || codigoOperacion == "8" || codigoOperacion == "9") {
                elementoVisible('off', 'barraProgresoCargaXML');
                expIniciados = datos.tabla.expedientesIniciados;
                var totExpedientes = expIniciados.length;
                var msgtitle = "PROCESO CORRECTO";
                var mensaje = "Se han iniciado los siguientes expedientes:<br>";
                for (var i = 0; i < totExpedientes; i++) {
                    mensaje += ' - ' + expIniciados[i] + '<br>';
                }
                mensaje += 'Puede consultarlos en Expedientes Relacionados.';
                if (codigoOperacion == "0") {
                    msgtitle = "PROCESO CORRECTO";
                } else {
                    msgtitle = "PROCESO INCOMPLETO";
                    if (codigoOperacion == "8") {
                        mensaje += '<br>' + '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorAsociarExpedientes")%>';
                    } else {
                        mensaje += '<br>' + '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorMisGestiones")%>';
                    }
                }
                jsp_alerta_Mediana("A", mensaje, msgtitle);
            } else {
                mostrarErrorPeticion(codigoOperacion);
            }
        }

        function mostrarErrorEliminarTrabajador() {
            mostrarErrorPeticion("6");
        }

        function mostrarErrorActualizarPestana() {
            mostrarErrorPeticion("5");
        }

        function mostrarErrorInicioExpedientes(ajaxResult) {
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            mostrarErrorPeticion(codigoOperacion);
        }

        function mostrarErrorPeticion(codigo) {
            var msgtitle = "¡¡ ERROR !!";
            elementoVisible('off', 'barraProgresoCargaXML');
            if (codigo == "1") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
            } else if (codigo == "2") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
            } else if (codigo == "3") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
            } else if (codigo == "4") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
            } else if (codigo == "5") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
            } else if (codigo == "6") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
            } else if (codigo == "7") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorInicioExpediente")%>', msgtitle);
            } else if (codigo == "8") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorAsociarExpedientes")%>', msgtitle);
            } else if (codigo == "9") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorMisGestiones")%>', msgtitle);
            } else if (codigo == "-1") {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
            } else {
                jsp_alerta("A", '<%=meLanbide84I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
            }
        }
    </script> 
</head>
<body class="bandaBody" onload="javascript:{
            elementoVisible('off', 'barraProgresoCargaXML');
        }" >
    <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana352"><%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
        <div id="barraProgresoCargaXML" style="visibility: hidden;">
            <div class="contenedorHidepage">
                <div class="textoHide">
                    <span>
                        <%=meLanbide84I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                    </span>
                </div>
                <div class="imagenHide">
                    <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                </div>
            </div>
        </div>
        <br/>
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide84I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
        <div>    
            <br>
            <div id="divGeneral">     
                <div id="listaEntap"  align="center"></div>
            </div>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnNuevaPersona" name="btnNuevaPersona" class="botonGeneral"  value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaPersona();">
                <input type="button" id="btnModificarPersona" name="btnModificarPersona" class="botonGeneral" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarPersona();">
                <input type="button" id="btnEliminarPersona" name="btnEliminarPersona"   class="botonGeneral" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarPersona();">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnCargarRegistros" name="btnCargarRegistros" class="botonGeneral" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXML();">
                <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
            </div>
            <br>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnIniciarExpedientes" name="btnIniciarExpedientes" class="botonMasLargo" value="<%=meLanbide84I18n.getMensaje(idiomaUsuario, "btn.iniciarExp")%>" onclick="pulsarIniciarExpedientes();" >
            </div>
        </div>  
    </div>
    <iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
    <script  type="text/javascript">
        //Tabla Personas
        var tablaEntap;
        var listaEntap = new Array();
        var listaEntapTabla = new Array();

        crearTablaEntap();
        <%  		
            PersonaVO objectVO = null;
            List<PersonaVO> List = null;
            if(request.getAttribute("listaEntap")!=null){
                List = (List<PersonaVO>)request.getAttribute("listaEntap");
            }													
            if (List!= null && List.size() >0){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                for (int indice=0;indice<List.size();indice++) {
                    objectVO = List.get(indice);

                    String programa="";
                    if(objectVO.getPrograma()!=null){
                        programa=objectVO.getPrograma();
                    }else{
                        programa="-";
                    }
                    // nombreCompleto = APE1 + [APE2] + NOMBRE => 3-[4]-2
                    String nombreCompleto="";
                    String nombre = "";
                    if(objectVO.getNombre()!=null){
                        nombre=objectVO.getNombre();
                    }else{
                        nombre="-";
                    }
                    String apel1 = "";               
                    if(objectVO.getApel1()!=null){
                        apel1=objectVO.getApel1();
                    }else{
                        apel1="-";
                    }
                    String apel2 = "";
                    if(objectVO.getApel2()!=null){
                        apel2=objectVO.getApel2();
                    } 
                    nombreCompleto = apel1 + " " + apel2 + ", " + nombre;

                    String dni="";
                    if(objectVO.getDni()!=null){
                        dni=objectVO.getDni();
                    }else{
                        dni="-";
                    }               

                    String fecNacimiento="-";
                    if(objectVO.getFechaNacimiento()!=null){
                        fecNacimiento=dateFormat.format(objectVO.getFechaNacimiento());
                    }
                    String sexo="";
                    if(objectVO.getSexo()!=null){
                        sexo=String.valueOf(objectVO.getSexo());
                    }
                    String descSexo ="-";
                    if(objectVO.getDescSexo()!=null){
                        descSexo=objectVO.getDescSexo();
                    }
                    // direccion = CALLE + NUMERO + LETRA + PLANTA + PUERTA => 7-8- -9-10               
                    String direccion="";
                    String calle = "";
                    String numero = ""; //Integer
                    String letra = "";
                    String planta = "";
                    String puerta = "";
                    if(objectVO.getCalle()!=null){
                        calle = objectVO.getCalle();
                        direccion = objectVO.getCalle();
                    }else{
                        calle ="-";
                    }   
                    if(objectVO.getNumero()!=null){
                        numero = String.valueOf(objectVO.getNumero());
                        direccion += " nº ";
                        direccion += numero;
                    }
                    if(objectVO.getLetra()!=null){
                        letra = String.valueOf(objectVO.getLetra());
                        direccion += letra;
                    }
                    if(objectVO.getPlanta()!=null){
                        planta = objectVO.getPlanta();
                        direccion += " - ";
                        direccion += objectVO.getPlanta();
                    }
                    if(objectVO.getPuerta()!=null){
                        puerta = objectVO.getPuerta();
                        direccion += " ";
                        direccion += objectVO.getPuerta();
                    } 
                    // poblacion = CP + LOCALIDAD => 11-12
                    String poblacion = "";
                    String codP = ""; //Integer
                    String localidad = "";
                    if(objectVO.getCodPost()!=null){
                       codP = String.valueOf(objectVO.getCodPost());
                    } 
                    if(objectVO.getLocalidad()!=null){
                        localidad = objectVO.getLocalidad();
                    } 
                    poblacion = codP + " - " + localidad;

                    String nivel = ""; //13
                    if(objectVO.getNivelAcademico()!=null){
                        nivel = objectVO.getNivelAcademico();
                    }
                    String descNivel = "-"; //13
                    if(objectVO.getDescNivel()!=null){
                        descNivel = objectVO.getDescNivel();
                    }
                    String fechaInicio = ""; //14
                    if(objectVO.getFechaInicio() != null){
                        fechaInicio = dateFormat.format(objectVO.getFechaInicio());
                    }else{
                        fechaInicio = "-";
                    }
                    String fechaSolicitud=""; //15
                    if(objectVO.getFechaSolicitud() != null){
                        fechaSolicitud = dateFormat.format(objectVO.getFechaSolicitud());
                    }else{
                        fechaSolicitud = "-";
                    }
        %>
        listaEntap[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=programa%>', '<%=nombre%>', '<%=apel1%>', '<%=apel2%>', '<%=dni%>',
            '<%=fecNacimiento%>', '<%=sexo%>', '<%=descSexo%>', '<%=calle%>', '<%=numero%>', '<%=letra%>', '<%=planta%>', '<%=puerta%>', '<%=codP%>', '<%=localidad%>',
            '<%=nivel%>', '<%=descNivel%>', '<%=fechaInicio%>', '<%=fechaSolicitud%>'];
        listaEntapTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=programa%>', '<%=nombreCompleto%>', '<%=dni%>',
            '<%=fecNacimiento%>', '<%=descSexo%>', '<%=direccion%>', '<%=poblacion%>', '<%=descNivel%>', '<%=fechaInicio%>', '<%=fechaSolicitud%>'];
        <%
                }// for
            }// if
        %>
        tablaEntap.lineas = listaEntapTabla;
        tablaEntap.displayTabla();

        if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
            try {
                var div = document.getElementById('listaEntap');
                if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                } else {
                    div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].style.width = '100%';
                }
            } catch (err) {
            }
        }

        if (!enTramiteInicio) {
            document.getElementById('btnIniciarExpedientes').disabled = true;
        }
    </script>
    <div id="popupcalendar" class="text"></div>                
</body>



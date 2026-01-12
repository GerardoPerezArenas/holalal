<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.TrabajadorCAPVValueObject" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
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

    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExpediente");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide61/melanbide61.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>

<style type="text/css">
    #numCCC,#numTrabajadores{
        font-family: Verdana,Arial;
        font-size: 11px;
        color: #000000;
        border: #999999 1px solid;
        background-color: #fffea3;
        text-transform:uppercase;
    }
</style>

<script type="text/javascript">
    // FUNCIONES QUE RESPONDE AL EVENTO ONCLICK DE LOS BOTONES
    function pulsarNuevoTrabajador() {
        if(validarDatos()){
            if(!existeFila()){
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                var numExp = '<%=numExpediente%>';
                var urlParam = 'tarea=preparar&modulo=MELANBIDE61&operacion=darAltaTrabajadorCAPV&tipo=0&numExp=' + numExp
                        + '&numCCC=' + escape($("#numCCC").val()) + '&numTr=' + $("#numTrabajadores").val();

                try{
                    $.ajax({
                        url:  url,
                        type: 'POST',
                        async: true,
                        data: urlParam,
                        success: procesarRespuestaAltaTrabajador,
                        error: muestraErrorRespuestaAltaTrabajador
                    });
               }catch(Err){
                   jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
                }
            } else {
                jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.filaYaExiste")%>');
            }
        } else {
            jsp_alerta('A', mensajeValidacion);
        }
    }

    
    function pulsarModificarTrabajador() {
        if (TablaTrabajadores.selectedIndex != -1) {
            if(validarDatos()){
                if(!existeFila()){
                    var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                    var numExp = '<%=numExpediente%>';
                    var urlParam = 'tarea=preparar&modulo=MELANBIDE61&operacion=modificarTrabajadorCAPV&tipo=0&numExp=' + numExp
                            + '&numCCC=' + escape($("#numCCC").val()) + '&numTr=' + $("#numTrabajadores").val()
                            + '&ident=' + listaIds[TablaTrabajadores.selectedIndex];

                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: urlParam,
                            success: procesarRespuestaModTrabajador,
                            error: muestraErrorRespuestaModTrabajador
                        });
                   }catch(Err){
                       jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.filaYaExiste")%>');
                }
            } else {
                jsp_alerta('A', mensajeValidacion);
            }
        } else {
            jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarTrabajador() {
        if (TablaTrabajadores.selectedIndex != -1) {
            var eliminar = jsp_alerta('', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarFila")%>');
            if (eliminar == 1) {
                var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                var numExp = '<%=numExpediente%>';
                var urlParam = 'tarea=preparar&modulo=MELANBIDE61&operacion=eliminarTrabajadorCAPV&tipo=0&numExp=' + numExp
                        + '&ident=' + listaIds[TablaTrabajadores.selectedIndex];

                try{
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: urlParam,
                        success: procesarRespuestaElimTrabajador,
                        error: muestraErrorRespuestaElimTrabajador
                    });
               }catch(Err){
                   jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
                }
            }
        } else {
            jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarLimpiarForm(){
        limpiarInputs();
    }

    // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICI�N AJAX
    function procesarRespuestaAltaTrabajador(ajaxResult){
        procesarRespuestaAjax(ajaxResult);
    }

    function procesarRespuestaModTrabajador(ajaxResult){
        procesarRespuestaAjax(ajaxResult);
    }

    function procesarRespuestaElimTrabajador(ajaxResult){
        procesarRespuestaAjax(ajaxResult);
    }

    function procesarRespuestaAjax(ajaxResult){
        var datos = JSON.parse(ajaxResult);
        var codigoOperacion = datos[0];
        var filas = datos[1];
        listaTrabajadores = new Array();
        listaIds = new Array();

        if(codigoOperacion=="0"){
            for(var j=0; j<filas.length; j++){
                var fila = filas[j];

                listaTrabajadores[j] = [fila.numCCC,fila.numTrabajadorFijo];
                listaIds[j] = [fila.idTrabajador];

                recargarTablaCAPV(listaTrabajadores);
                limpiarInputs();
            }
        } else {
            mostrarError(codigoOperacion);
        }
    }

    function muestraErrorRespuestaAltaTrabajador(){
        jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
    }

    function muestraErrorRespuestaModTrabajador(){
        jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
    }

    function muestraErrorRespuestaElimTrabajador(){
        jsp_alerta("A",'<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
    }

    // FUNCIONES DE VALIDACI�N Y OTRAS
    function validarDatos() {
        mensajeValidacion = "";

        var numCCC = $("#numCCC").val();
        var numTrabajadores = $("#numTrabajadores").val();

        if(numCCC=="" || numTrabajadores==""){
            mensajeValidacion = '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.camposOblig")%>';
            return false;
        } else if(!validarNumerico(numTrabajadores)){
            mensajeValidacion = '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.campoNumerico")%>';
            return false;
        }

        return true;
    }

    function validarNumerico(numero){
        try{
            if (Trim(numero)!='') {
                return /^([0-9])*$/.test(numero);
            }
        }
        catch(err){
            return false;
        }
    }

    function existeFila(){
        var existe = false;
        var numCCC = $("#numCCC").val();
        var numTrabajadores = $("#numTrabajadores").val();

         if(listaTrabajadores.length>0) {
             for(var j=0; j<listaTrabajadores.length; j++){
                 if(listaTrabajadores[j][0]==numCCC && listaTrabajadores[j][1]==numTrabajadores){
                    existe = true;
                    break;
                }
            }
        }
        return existe;
    }

    function limpiarInputs(){
        var numCCC = $("#numCCC").val();
        var numTrabajadores = $("#numTrabajadores").val();

        if(numCCC!="") $("#numCCC").val("");
        if(numTrabajadores!="") $("#numTrabajadores").val("");
    }

    function mostrarError(codigo){
        if (codigo == "1") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorConexBD")%>');
        } else if (codigo == "2") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
        } else if (codigo == "3") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorID")%>');
        } else if (codigo == "4") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
        } else if (codigo == "5") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
        } else if (codigo == "6") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
        } else if (codigo == "-1") {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
        }else {
            jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorGenServ")%>');
        }
    }

    // FUNCIONES DE TABLA
    function crearTablaCAPV(){
        TablaTrabajadores = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaCAPV'), 465);
        TablaTrabajadores.addColumna('295','center',"<%=meLanbide61I18n.getMensaje(idiomaUsuario,"CCCCAPV.tabla.col1")%>");
        TablaTrabajadores.addColumna('155','center',"<%=meLanbide61I18n.getMensaje(idiomaUsuario,"CCCCAPV.tabla.col2")%>");

        TablaTrabajadores.displayCabecera=true;
        TablaTrabajadores.height = 400;
    }

    function recargarTablaCAPV(listado) {
        crearTablaCAPV();

        TablaTrabajadores.lineas = listado;
        TablaTrabajadores.displayTabla();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            try{
                var div = document.getElementById('listaCAPV');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
            }
            catch(err){

            }
        }
    }

    function rellenarDatos(tableName,rowID){
        if(tableName==TablaTrabajadores){
            var index = TablaTrabajadores.selectedIndex;
            $("#numCCC").val(listaTrabajadores[index][0]);
            $("#numTrabajadores").val(listaTrabajadores[index][1]);
        }
    }

</script>

<body>
    <div class="tab-page" id="tabPage611" style="height:520px; width: 100%;">
        <h1 class="tab" id="pestana611"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.tituloPestanaCAPV")%></h1>       
        <br/>
        <!--form-->
        <h2 class="legendAzul" id="pestanaPrinc" style="text-align: center;"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.tituloPaginaCAPV")%></h2>
        <div> 
            <div>
                <br/>
                <div id="divGeneral"  style="height: 500px;" align="center">     
                    <p id="listaCAPV" style="padding: 5px; width:990px; height: 350px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;"></p>
                    <p>
                        <input type="text" id="numCCC" name="numCCC" style="width: 28%"/>
                        <input type="text" id="numTrabajadores" name="numTrabajadores" style="width: 15%" />
                    </p>
                    <p class="botonera" style="text-align: center;">
                        <input type="button" id="btnNuevo" name="btnNuevo" class="botonGeneral"  value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarNuevoTrabajador();">
                        <input type="button" id="btnModificar" name="btnModificar" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTrabajador();">
                        <input type="button" id="btnEliminar" name="btnEliminar"   class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTrabajador();">
                        <input type="button" id="btnLimpiar" name="btnLimpiar"   class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarForm();">
                    </p>
                </div>
            </div>
        </div>
        <!--/form-->    
    </div>
    <script  type="text/javascript">
        var mensajeValidacion = "";
        var TablaTrabajadores;
        var listaTrabajadores = new Array();
        var listaIds = new Array();

        crearTablaCAPV();

        <%  		
           TrabajadorCAPVValueObject objectVO = null;
           ArrayList<TrabajadorCAPVValueObject> relacion = null;
           if(request.getAttribute("relacionTrabajadores")!=null){
               relacion = (ArrayList<TrabajadorCAPVValueObject>)request.getAttribute("relacionTrabajadores");
           }													
           if (relacion!= null && relacion.size() >0){
               for (int indice=0;indice<relacion.size();indice++)
               {
                   objectVO = relacion.get(indice);

        %>
            listaTrabajadores[<%=indice%>] = ['<%=objectVO.getNumCCC()%>','<%=objectVO.getNumTrabajadorFijo()%>'];
        listaIds[<%=indice%>] = ['<%=objectVO.getIdTrabajador()%>'];

        <%
               }// for
           }// if
        %>

        recargarTablaCAPV(listaTrabajadores);
    </script>
</body>


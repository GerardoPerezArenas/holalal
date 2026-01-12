<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide83.i18n.MeLanbide83I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide83.util.ConstantesMeLanbide83" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InfoDesplegableVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide83.vo.ElementoDesplegableVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide83.vo.FacturaVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
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
    MeLanbide83I18n meLanbide83I18n = MeLanbide83I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String[] partes = numExpediente.split("/");
    Integer ejercicio=0;
    if(partes!=null && partes.length>1)
        ejercicio = Integer.parseInt(partes[0]);
    
 
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide83/melanbide83.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide83/lanbide.js"></script>

<script type="text/javascript">   
    var baseUrl = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do?';
    var mensajeValidacion;
    var mostrarValidacionInformativa=false;
    var tablaFacturas;
    var listaFacturas;
    var listaTablaFacturas;
    var codsEstado = new Array();
    var descsEstado = new Array();
    var codsConcepto = new Array();
    var descsConcepto = new Array();
    var codsEntrega = new Array();
    var descsEntrega = new Array();
    var comboEstados;
    var comboConcepto;
    var comboEntregaJustif;
    
    //fechas fijas para los conceptos 1 - Arrendamiento, 2 - CuotaRETA
    var fechaIniArrend = '01/03/2020';
    var fechaFinArrend = '31/08/2020';
    var fechaIniCuot= '01/08/2020';
    var fechaFinCuot = '31/12/2020';
    
    var fechaIniArrendamiento = convertFecha(fechaIniArrend);
    var fechaFinArrendamiento = convertFecha(fechaFinArrend);
    var fechaIniCuotaRETA = convertFecha(fechaIniCuot);
    var fechaFinCuotaRETA = convertFecha(fechaFinCuot);
    
    //convierte fecha para cáculos y comparaciones
    function convertFecha(stringFecha){
        var fechaArray=stringFecha.split("/");
        var fecha = (fechaArray!=null && fechaArray.length==3 ? new Date(fechaArray[2],new Number(fechaArray[1])-1,fechaArray[0]) : null);
        
        return fecha;
    }
    
    // FUNCIONES DE RESPUESTA A EVENTOS
    function rellenarDatos(tableName,rowID){
        if(tablaFacturas==tableName){
            if(rowID>=0){
                comboEstados.buscaCodigo(listaFacturas[rowID][2]);
                $('#fechaFact').val(listaTablaFacturas[rowID][1]);
                $('#importeFact').val(listaTablaFacturas[rowID][3]);
                comboConcepto.buscaCodigo(listaFacturas[rowID][4]);
                comboEntregaJustif.buscaCodigo(listaFacturas[rowID][6]);
                $('#observFact').val(listaFacturas[rowID][7]);
                
                if (listaFacturas[rowID][8] == 'S'){
                    checkImputada(true);
                } else {
                    checkImputada(false);
                }
                
            } else mostrarMensajeAviso(1);
        }
    }
    
    function isImputada () {
        return $('#ckImputada').is(':checked');
    }
    
    function checkImputada (cheched) {
        $('#ckImputada').prop('checked', cheched);
    }

    
    function guardarDatosFactura(){
        if(validaFormulario()){
            if(mostrarValidacionInformativa){
                jsp_alerta("A", mensajeValidacion);
            }
            var estado = $('#codEstadosFact').val();
            var fecha = $('#fechaFact').val();
            var codConcepto = $('#codConceptoFact').val();
            var importe = ($('#importeFact').val());
            var justificada = $('#codEntregaJustif').val();
            var imputada = 'N';
            if (isImputada()) {
                imputada = 'S';
            }
            var observ = $('#observFact').val();
            var parametros = 'tarea=preparar&modulo=MELANBIDE83&operacion=guardarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                + '&estado='+estado+'&fecha='+fecha+'&codConcepto='+codConcepto+'&importe='+importe
                + '&justif='+justificada+'&imputada='+imputada+'&observ='+escape(observ);
        
            alert ("guardarDatosFactura1");
            realizarPeticionAjax(parametros);
            alert ("guardarDatosFactura2");
        } else jsp_alerta("A", mensajeValidacion);
    }
    
    function modificarDatosFactura(){
        if(tablaFacturas.selectedIndex!=-1){
            if(validaFormulario()){
                if(mostrarValidacionInformativa){
                    jsp_alerta("A", mensajeValidacion);
                }
                var estado = $('#codEstadosFact').val();
                var fecha = $('#fechaFact').val();
                var codConcepto = $('#codConceptoFact').val();
                var importe = $('#importeFact').val();
                var justificada = $('#codEntregaJustif').val();
                var imputada = 'N';
                if (isImputada()) {
                    imputada = 'S';
                }
                var observ = $('#observFact').val();
                var id = listaFacturas[tablaFacturas.selectedIndex][0];
                var parametros = 'tarea=preparar&modulo=MELANBIDE83&operacion=guardarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                    + '&estado='+estado+'&fecha='+fecha+'&codConcepto='+codConcepto+'&importe='+importe
                    + '&justif='+justificada+'&imputada='+imputada+'&observ='+escape(observ)+'&id='+id;

                realizarPeticionAjax(parametros);
            } else jsp_alerta("A", mensajeValidacion);
        } else mostrarMensajeAviso(1);
    }
    
    function eliminarFactura(){
        if(tablaFacturas.selectedIndex!=-1){
            var id = listaFacturas[tablaFacturas.selectedIndex][0];
            var parametros = 'tarea=preparar&modulo=MELANBIDE83&operacion=eliminarFactura&tipo=0&numExp=<%=numExpediente%>&codOrg=<%=codOrganizacion%>'
                + '&id='+id;
            if(jsp_alerta("C",'<%=meLanbide83I18n.getMensaje(idiomaUsuario,"msg.preguntaEliminar")%>') ==1) {
                realizarPeticionAjax(parametros);
            }
        } else mostrarMensajeAviso(1);
    }
    
    function limpiarFormulario(){
        comboEstados.selectItem(-1);
        $('#fechaFact').val("");
        comboConcepto.selectItem(-1);
        $('#importeFact').val("");
        comboEntregaJustif.selectItem(-1);
        $('#observFact').val("");
        checkImputada(false);
    }

    function realizarPeticionAjax(parametros){
        var ajax = getXMLHttpRequest();

        if(ajax!=null){
            pleaseWait('on');
            
            ajax.open("POST",baseUrl,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);
            try{
                
                if (ajax.readyState==4 && ajax.status==200){
                    alert("realizarPeticionAjax0");
                    pleaseWait('off');
                    var text = ajax.responseText; 
                    var datos = JSON.parse(text);
                    if(datos.tabla.error=="0"){
                        alert("realizarPeticionAjax1");
                        var facturas = datos.tabla.lista;
                        if(facturas.length>0){
                            listaFacturas = new Array();
                            listaTablaFacturas = new Array();

                            for(var contador=0; contador<facturas.length; contador++){
                                var fact = facturas[contador];
                                var observ = fact.observaciones;

                                listaTablaFacturas[contador] = [
                                    fact.codEstado!=undefined?fact.codEstado:'',fact.fechaStr,fact.descConcepto,fact.importeStr,fact.descEntregaJustif,fact.codImputada
                                ];
                                listaFacturas[contador] = [
                                    fact.codIdent,fact.numExpediente,fact.codEstado,fact.fecha,fact.codConcepto,
                                    fact.importe,fact.codEntregaJustif,unescape(observ),fact.codImputada
                                ];
                            }
                            recargarTabla(listaTablaFacturas);
                        } else {
                            recargarTabla(new Array());
                        }
                        limpiarFormulario();
                    } else {
                        alert("realizarPeticionAjax2");
                        var error = datos.tabla.error;
                        mostrarMensajeError(error);
                    }
                        
                }//if (ajax.readyState==4 && ajax.status==200)
            }catch(Err){
                alert("realizarPeticionAjax3");
                mostrarMensajeError();
                //alert("Error.descripcion: " + Err.description);
            }//try-catch
        }//if(ajax!=null)
    }

    // OTRAS FUNCIONES
    function recargarTabla(datos){
        var justificado = calcularImporte(datos);
        datos[datos.length] = [
            '','','<b>'+'<%=meLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.filaTotal.col5")%>'+'</b>',
            doubleToFormattedString(justificado),'',''
        ];
        tablaFacturas.lineas = datos;
        tablaFacturas.displayTabla();
    }
    
    function inicializarDesplegables(){
        var contador = 0;
        <%
            InfoDesplegableVO desplegable = null;
            ArrayList<ElementoDesplegableVO> elementos = null;
            if(request.getAttribute("comboEstados")!=null){
                desplegable = (InfoDesplegableVO) request.getAttribute("comboEstados");
                elementos = desplegable.getParesCodVal();
                for(int i=0; i<elementos.size(); i++){
                    ElementoDesplegableVO elem = elementos.get(i);
        %>
                codsEstado[contador] = '<%=elem.getCodigo()%>';
                descsEstado[contador++] = '<%=elem.getValor()%>';
        <%  } } %>

        comboEstados = new Combo("EstadosFact");
        comboEstados.addItems(codsEstado, descsEstado);

        contador = 0;
        <%
            if(request.getAttribute("comboConceptos")!=null){
                desplegable = (InfoDesplegableVO) request.getAttribute("comboConceptos");
                elementos = desplegable.getParesCodVal();
                for(int i=0; i<elementos.size(); i++){
                    ElementoDesplegableVO elem = elementos.get(i);
        %>
                codsConcepto[contador] = '<%=elem.getCodigo()%>';
                descsConcepto[contador++] = '<%=elem.getValor()%>';
        <%  } } %>

        comboConcepto = new Combo("ConceptoFact");
        comboConcepto.addItems(codsConcepto, descsConcepto);

        contador = 0;
        <%
            if(request.getAttribute("comboSiNo")!=null){
                desplegable = (InfoDesplegableVO) request.getAttribute("comboSiNo");
                elementos = desplegable.getParesCodVal();
                for(int i=0; i<elementos.size(); i++){
                    ElementoDesplegableVO elem = elementos.get(i);
        %>
                codsEntrega[contador] = '<%=elem.getCodigo()%>';
                descsEntrega[contador++] = '<%=elem.getValor()%>';
        <%  } } %>

        comboEntregaJustif = new Combo("EntregaJustif");
        comboEntregaJustif.addItems(codsEntrega, descsEntrega);
        
    }
    
    function inicializarListas(){
        listaFacturas = new Array();
        listaTablaFacturas = new Array();
        
        var contador = 0;
        <%
            if(request.getAttribute("relacionFacturas")!=null){
                ArrayList<FacturaVO> relacionFacturas = (ArrayList<FacturaVO>) request.getAttribute("relacionFacturas");
                if(relacionFacturas.size()>0){
                    for(int i=0; i<relacionFacturas.size(); i++){
                        FacturaVO factura = relacionFacturas.get(i);
                        String observ = factura.getObservaciones();
                        if(observ==null) observ = "";
        %>
                    listaTablaFacturas[contador] = [
                        '<%=factura.getCodEstado()!=null?factura.getCodEstado():""%>','<%=factura.getFechaStr()%>','<%=factura.getDescConcepto()%>',
                        '<%=factura.getImporteStr()%>','<%=factura.getDescEntregaJustif()%>','<%=factura.getCodImputada()%>'
                    ];
                    listaFacturas[contador++] = [
                        '<%=factura.getCodIdent()%>','<%=factura.getNumExpediente()%>','<%=factura.getCodEstado()!=null?factura.getCodEstado():""%>','<%=factura.getFecha()%>',
                        '<%=factura.getCodConcepto()%>','<%=factura.getImporte()%>',
                        '<%=factura.getCodEntregaJustif()%>','<%=StringEscapeUtils.escapeJava(observ)%>','<%=factura.getCodImputada()%>'
                    ];
        <%  } } } %>
        
        recargarTabla(listaTablaFacturas);
    }
    
    function mostrarMensajeError(codigo){
        pleaseWait('off');
        if(codigo == "1") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
        } else if (codigo == "2") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorInsDatos")%>');
        } else if (codigo == "3") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
        } else if (codigo == "4") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.falloOperacionIns")%>');
        } else if (codigo == "5") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.falloOperacionMod")%>');
        } else if (codigo == "6") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorModDatos")%>');
        } else if (codigo == "7") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.falloOperacionElim")%>');
        } else if (codigo == "8") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorElimDatos")%>');
        } else if (codigo == "9") {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorCargarLista")%>');
        } else {
            jsp_alerta("A", '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
        } 
    }
    
    /*
    * Muestra mensajes de aviso según los siguientes códigos:
    * 1: No se ha seleccionado ninguna fila
    * 2: El cálculo se ha realizado correctamente
    * 3: No se puede realizar el cálculo. Rellene los campos suplementarios necesarios previamente
    * 4: La lista de facturas está vacía
    */
    function mostrarMensajeAviso(codigo){
        var mensaje = "";
        if(codigo == "1") {
            mensaje = '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"aviso.noFilaSeleccionada")%>';
        } else if(codigo == "2") {
            mensaje = '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"aviso.exitoOperacionCalcular")%>';
        } else if(codigo == "3") {
            mensaje = '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"aviso.sinValoresParaCalcular")%>';
        } else if(codigo == "4") {
            mensaje = '<%=meLanbide83I18n.getMensaje(idiomaUsuario,"aviso.noHayFacturas")%>';
        } 
        jsp_alerta("A", mensaje);
    }
    
    function mostrarCalFechaFactura(evento) { 
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFechaFact").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaFact',null,null,null,'','calFechaFact','',null,null,null,null,null,null,null, null,evento);  
    }
   
    function calcularImporte(lineasTabla){
        var total = -1;
        if(lineasTabla.length>0){
            total = 0;
            for(var cont=0; cont<lineasTabla.length; cont++){
                if(lineasTabla[cont][0]=="A"){
                    var valor = lineasTabla[cont][3];
                    while(valor.indexOf(".")!=-1){
                        valor = valor.replace(".","");
                    }
                    valor = valor.replace(",",".");
                    total += eval(valor);
                }
            }
        }
        return total;
    }

    //VALIDACIONES
    function validaFormulario(){
        mensajeValidacion = "";
        var correcto = true;
            
        var codEstado = $('#codEstadosFact').val();
        var estado = $('#descEstadosFact').val();
        var fecha = $('#fechaFact').val();
        var codConcepto = $('#codConceptoFact').val();
        var concepto = $('#descConceptoFact').val();
   
        var importe = $('#importeFact').val();
        while(importe.indexOf(".")!=-1){
            importe = importe.replace(".","");
        }
    
        var codJustificada = $('#codEntregaJustif').val();
        var justificada = $('#descEntregaJustif').val();
         
        //alert("validaFormulario1");
        if (codEstado == null || codEstado == '' || estado == null || estado == '') {
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.estadoObligatorio")%>';
            return false;
        } 
        //alert("validaFormulario2");
        if (fecha == null || fecha == '') {
             alert("validaFormulario2.1");
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
            return false;
        } else if(!ValidarFechaConFormatoLanbide(fecha)){
             alert("validaFormulario2.2");
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.fechaIncorrecta")%>';
            return false;
        }
        //alert("validaFormulario3");
        if (codConcepto == null || codConcepto == '' || concepto == null || concepto == '') {
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.conceptoObligatorio")%>';
            return false;
        }
  
        //codigo concepto
        //fecha
        //var fechaFactArray=fecha.split("/");
        //var fechaFact = (fechaFactArray!=null && fechaFactArray.length==3 ? new Date(fechaFactArray[2],new Number(fechaFactArray[1])-1,fechaFactArray[0]) : null);
        //alert("validaFormulario4");
        var fechaFact = convertFecha(fecha);
  
        switch (codConcepto) {
            case "1":
                if(fechaFact <  fechaIniArrendamiento || fechaFact >  fechaFinArrendamiento){
                    mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.validaFechaFactura.fueraRangoPeriodos1")%>';
                    if(codEstado==="N"){
                        mostrarValidacionInformativa=true;
                        return correcto;
                    }else{
                        mostrarValidacionInformativa=false;
                        return false;
                    }
                }
                break;
            case "2" :
                if(fechaFact <  fechaIniCuotaRETA || fechaFact >  fechaFinCuotaRETA){
                    mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.validaFechaFactura.fueraRangoPeriodos2")%>';
                    if(codEstado==="N"){
                        mostrarValidacionInformativa=true;
                        return correcto;
                    }else{
                        mostrarValidacionInformativa=false;
                        return false;
                    }
                }
                break;
        }
        //alert("validaFormulario5");
        if (importe == null || importe == '') {
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.importeObligatorio")%>';
            return false;
        } else{
            if(!validarNumericoDecimal(importe)){
                mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.importeIncorrecto")%>';
                return false;
            }
        }
        //alert("validaFormulario6");
        if (codJustificada == null || codJustificada == '' || justificada == null || justificada == '') {
            mensajeValidacion = '<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "msg.entregaJustifObligatorio")%>';
            return false;
        }
        
        return correcto;
    }
   
    function validarNumericoDecimal(numero){
        try{
            if(Trim(numero) != ''){
                return /^([0-9])*(,([0-9])*)?$/.test(numero);
            }
        }
        catch(err){
            return false;
        }
    }

    function getListaFacturas(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();        
        parametros = 'tarea=preparar&modulo=MELANBIDE83&operacion=getListaFacturas&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
        realizarPeticionAjax(parametros);        
    }
    
    function pulsarInformeInterno(){
        var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
        var parametros = "?tarea=preparar&modulo=MELANBIDE83&operacion=descargarInformeInterno&tipo=0";
        document.forms[0].action = url + parametros;       
        document.forms[0].submit();
    }
    
    
</script>

    <div class="tab-page" id="tabPage831" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana831"><%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">var tp1_p831 = tp1.addTabPage( document.getElementById( "tabPage831" ) );</script>
       
        <div style="clear: both;">
            <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
                <div class=""><!--bloqueDatos-->
                    <div class="lineaFormulario" style="width: 100%; margin-bottom: 10px;">
                        <%-- Estado --%>
                        <div class="etiqueta" style="float: left; width: 7%; margin-right: 15px;">
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqEstado")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 30%">
                            <input type="text" style='width: 10%' name="codEstadosFact" id="codEstadosFact" class="inputTexto" onkeyup="xAMayusculas(this);"/>
                            <input type="text" style='width: 80%' name="descEstadosFact" id="descEstadosFact" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorEstadosFact" name="anchorEstadosFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonEstadosFact" name="botonEstadosFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                        <%--Fecha de factura--%>
                        <div class="etiqueta" style="float: left;width: 12%;margin-right: 15px" >
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqFechaFact")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 20%">
                            <input type="text" class="inputTxtFecha" id="fechaFact" name="fechaFact" maxlength="10" onkeyup = "return SoloCaracteresFechaLanbide(this);" 
                                   onblur = "javascript:return comprobarFechaLanbide(this);" onfocus="javascript:this.select();" style="width: 55%"/>
                            <A href="javascript:calClick();return false;" onClick="mostrarCalFechaFactura(event);return false;"  style="text-decoration:none;" >
                                <IMG style="border: 0px solid none" height="17" id="calFechaFact" name="calFechaFact" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > 
                            </A>
                        </div>
                        <%--Importe--%>
                        <div class="etiqueta" style="float: left;width: 7%;margin-right: 15px" >
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqImporte")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 10%">
                            <input type="text" name="importeFact" id="importeFact" class="inputTexto" style="width: 60%"/>
                        </div>
                    </div>
                    <br/><br/>
                    <div class="lineaFormulario" style="float: left; width: 100%; margin-bottom: 10px;">
                        <%--Concepto--%>
                        <div class="etiqueta" style="width: 7%; margin-right: 15px;float: left;">
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqConcepto")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 30%">
                            <input type="text" style='width: 10%' name="codConceptoFact" id="codConceptoFact" class="inputTexto" onkeyup="xAMayusculas(this);" />
                            <input type="text" style='width: 80%' name="descConceptoFact"  id="descConceptoFact" class="inputTexto" readonly="true"/>
                            <a href="" id="anchorConceptoFact" name="anchorConceptoFact">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonConceptoFact" name="botonConceptoFact" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                        <%--Entrega Justificante--%>
                        <div class="etiqueta" style="width: 12%;margin-right: 15px;float:left;">
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqEntregaJustif")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 20%">
                            <input type="text" style='width: 12%' name="codEntregaJustif" id="codEntregaJustif" class="inputTexto" onkeyup="xAMayusculas(this);" />
                            <input type="text" style='width: 38%' name="descEntregaJustif"  id="descEntregaJustif" class="inputTexto" readonly="true" />
                            <a href="" id="anchorEntregaJustif" name="anchorEntregaJustif">
                                <img src="<c:url value='/images/listas/botondesplegable.gif'/>" id="botonEntregaJustif" name="botonEntregaJustif" width="14" height="14" style="cursor:pointer; border: 0px none">
                            </a>
                        </div>
                        <%--Imputada--%>
                        <div class="etiqueta" style="width: 7%;margin-right: 15px;float:left;">
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqImputada")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 10%">
                            <input type="checkbox" value="false" id="ckImputada" />
                        </div>
                    </div>  
                    <%--Observaciones--%>
                    <div class="lineaFormulario" style="float: left; width: 100%; margin-bottom: 10px;">
                        <div class="etiqueta" style="margin-right: 41px;float: left;" >
                            <%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.etiqObservaciones")%>:
                        </div>
                        <div class="columnP" style="float:left; width: 85%">
                            <textarea style="width: 100%" name="observFact" id="observFact" rows="5" class="melanbide83_txtSinMayusculas"></textarea>
                        </div>
                    </div>
                    <div class="botonera" style="text-align: right; margin-bottom: 5px;">
                        <input type="button" id="btnRegistrarFact" name="btnRegistrarFact" class="botonGeneral" value="<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "button.etiqGrabar")%>" onclick="guardarDatosFactura();">
                        <input type="button" id="btnModificarFact" name="btnModificarFact" class="botonGeneral" value="<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "button.etiqModificar")%>" onclick="modificarDatosFactura();">
                        <input type="button" id="btnEliminarFact" name="btnEliminarFact" class="botonGeneral" value="<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "button.etiqEliminar")%>" onclick="eliminarFactura();">
                        <input type="button" id="btnLimpiarFact" name="btnLimpiarFact" class="botonGeneral" value="<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "button.etiqLimpiar")%>" onclick="limpiarFormulario();">
                    </div>

                    <div id="tablaFacturas" style="width: 100%"></div>
                    
                    <br/>
                    <div class="botonera" style="text-align: right;">
                         <input type="button" id="btnFactASubsanar" name="btnFactASubsanar" class="botonMasLargo" value="<%=MeLanbide83I18n.getMensaje(idiomaUsuario, "button.etiqInformeAtase")%>" onclick=" pulsarInformeInterno();">
                    </div>
               
                </div>
        </div>
    </div>
<iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 


<script type="text/javascript">
    function barraProgresoEca(valor, idBarra) {
        if(valor=='on'){
            document.getElementById(idBarra).style.visibility = 'inherit';
        }
        else if(valor=='off'){
            document.getElementById(idBarra).style.visibility = 'hidden';
        }
    }
    
    // Creamos los combos
    inicializarDesplegables();
            
    // Creamos la tabla
    tablaFacturas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaFacturas'), 890);

    tablaFacturas.addColumna('30','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col1")%>');
    tablaFacturas.addColumna('65','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col2")%>');
    tablaFacturas.addColumna('280','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col3")%>');
    tablaFacturas.addColumna('60','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col5")%>');
    tablaFacturas.addColumna('35','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col7")%>');
    tablaFacturas.addColumna('35','left','<%=MeLanbide83I18n.getMensaje(idiomaUsuario,"label.tablaFacturas.col8")%>');

    tablaFacturas.height = 200;
    tablaFacturas.displayCabecera=true;
    
    inicializarListas();
</script>
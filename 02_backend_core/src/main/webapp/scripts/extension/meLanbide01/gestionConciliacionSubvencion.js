/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    //Cargamos los datos de la tabla Causantes
    var listaCausanteSubvencionExpteJSON = JSON.parse($("#listaCausanteSubvencionExpte").val());
    if(listaCausanteSubvencionExpteJSON!=null && listaCausanteSubvencionExpteJSON!=undefined){
        crearCargarTablaCausantes(listaCausanteSubvencionExpteJSON);
    }
    //Cargamos los datos de la tabla Historial
    var listaLineaHistorialSubvExpteJSON = JSON.parse($("#listaLineaHistorialSubvExpte").val());
    if(listaLineaHistorialSubvExpteJSON!=null && listaLineaHistorialSubvExpteJSON!=undefined){
        crearCargarTablaLineaHistorialSubv(listaLineaHistorialSubvExpteJSON);
    }
    // Ocultamos los calendariosde los campos de fecha de la pestana campos suplementarios par evitar edicion : 
    // FECINISITHIST y FECFINSITPROHIST
//    if($("#FECFINSITPROHIST")!=null && $("#FECFINSITPROHIST")!=undefined){
//        $("#FECFINSITPROHIST").parent().find('[name^="cal"]').attr("style","display:none;");
//    }
//    if($("#FECFINSITPROHIST")!=null && $("#FECFINSITPROHIST")!=undefined){
//        $("#FECFINSITPROHIST").parent().find('[name^="cal"]').attr("style","display:none;");
//    }    
    
    //Evento limpiar div edicion
    $('#divGeneralAltaEdicionCausante').on('hidden', function () {
            limpiarDatosFormCausante();
	});
    $('#divGeneralAltaEdicionHistorial').on('hidden', function () {
            limpiarDatosFormHistorial();
	});
    $('#modalGestionMensajesM01').on('hidden.bs.modal', function () {
        $("#divValidacionesAlarmasExpte").empty();
        $("#divValidacionesAlarmasExpte").html($("#textoMensajeM01").html());
        $("#textoMensajeM01").empty();
        if($("#divValidacionesAlarmasExpte").is(':empty')){
            $('#divValidacionesAlarmasExpte').hide();
        }else{
            $('#divValidacionesAlarmasExpte').slideDown();
        }
    });
    
    /* 
    //Esta seccion de momento la obviamos, se recarga la pagina al guardar expedientes invocando al metodo: recargarDatosPagina()
    $('h2#pestana8.tab').on('click', function () {
        if($("#refrescarValidacionesAlarmasExpte")!=null && $("#refrescarValidacionesAlarmasExpte")!= undefined
                && $("#refrescarValidacionesAlarmasExpte").val()!=undefined && $("#refrescarValidacionesAlarmasExpte").val()!=""
                && $("#refrescarValidacionesAlarmasExpte").val()!="0" ){
            validarControlAlarmasCONCM();
            $("#refrescarValidacionesAlarmasExpte").val("0");
        }  
    });
    // Pruebas Actualizar alarmas cuado se guardan datos del expediente
    $('#capaBotones3 input.botonGeneral[name="cmdGrabarGeneral"]').on('click', function () {
        if($("#refrescarValidacionesAlarmasExpte")!=null && $("#refrescarValidacionesAlarmasExpte")!= undefined){
            var refresca = $("#refrescarValidacionesAlarmasExpte").val();
            refresca = (refresca != null && refresca != undefined ? parseInt(refresca) + 1 : refresca);
            $("#refrescarValidacionesAlarmasExpte").val(refresca);
        }
    });
    */
        
        mostrarValidacionesAlarmasExpte();
});

// Variables generales para manejo de la tabla
var parametrosLLamadaM01={
        tarea:'preparar'
        ,modulo:'MELANBIDE01'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,jsonCausantesSubvencion:null
        ,jsonHistorialSibvencion:null
        ,identificadorBDEliminar:null
    };

var jsonMelanbide01DepenPerSut ={
        id:null
        ,numeroExpediente:null
        ,correlativo:null
        ,tipoDependiente:null
        ,parentezco:null
        ,nombre:null
        ,apellidos:null
        ,tipoDocumento:null
        ,numeroDocumento:null
        ,fechaNacimiento:null
        ,esMinusvalido:null
        ,porcentajeMinusvalia:null
        ,fechaHoraRegistro:null
        ,fechaHoraModificacion:null  
};

var jsonMelanbide01HistoSubv ={
        id:null
        ,numeroExpediente:null
        ,fechaFinInterrupSituacion:null
        ,fechaProrrReanudSituacion:null
        ,fechaHoraRegistro:null
        ,fechaHoraModificacion:null  
};

var tabCausanteSubvencionExpte;
var listaCausanteSubvencionExpte = new Array();
var listaCausanteSubvencionExpteTabla = new Array();

var tabLineaHistorialSubvExpte;
var listaLineaHistorialSubvExpte = new Array();
var listaLineaHistorialSubvExpteTabla = new Array();


function crearCargarTablaCausantes(listaCausantesSubv){
    // Resetrear la tabla
    listaCausanteSubvencionExpte = new Array();
    listaCausanteSubvencionExpteTabla = new Array();
    tabCausanteSubvencionExpte = new Tabla(true,$("#causanteSubv_descriptor_buscar").val(),$("#causanteSubv_descriptor_anterior").val(),$("#causanteSubv_descriptor_siguiente").val(), $("#causanteSubv_descriptor_mosFilasPag").val(), $("#causanteSubv_descriptor_msgNoResultBusq").val(), $("#causanteSubv_descriptor_mosPagDePags").val(),$("#causanteSubv_descriptor_noRegDisp").val(),$("#causanteSubv_descriptor_filtrDeTotal").val(),$("#causanteSubv_descriptor_primero").val(),$("#causanteSubv_descriptor_ultimo").val(), $("#listaCausantesSubvContenedor")[0], 900);
    tabCausanteSubvencionExpte.addColumna('90', 'center',$("#causanteSubv_textoColumna1").val());
    tabCausanteSubvencionExpte.addColumna('90', 'center', $("#causanteSubv_textoColumna2").val());
    tabCausanteSubvencionExpte.addColumna('90', 'left',$("#causanteSubv_textoColumna3").val());
    tabCausanteSubvencionExpte.addColumna('90', 'left',$("#causanteSubv_textoColumna4").val());
    tabCausanteSubvencionExpte.addColumna('90', 'left',$("#causanteSubv_textoColumna5").val());
    tabCausanteSubvencionExpte.addColumna('90', 'center',$("#causanteSubv_textoColumna6").val());
    tabCausanteSubvencionExpte.addColumna('90', 'center',$("#causanteSubv_textoColumna7").val());
    tabCausanteSubvencionExpte.addColumna('90', 'center',$("#causanteSubv_textoColumna8").val());
    tabCausanteSubvencionExpte.addColumna('90', 'center',$("#causanteSubv_textoColumna9").val());
    tabCausanteSubvencionExpte.addColumna('90', 'right',$("#causanteSubv_textoColumna10").val());
    tabCausanteSubvencionExpte.displayCabecera = true;
    tabCausanteSubvencionExpte.height = 200;
    
    if(listaCausantesSubv!=null && listaCausantesSubv!=undefined){
        for (var i = 0; i < listaCausantesSubv.length; i++) {
            var causanteSubvencionExpte = listaCausantesSubv[i];
            listaCausanteSubvencionExpteTabla[i]=[causanteSubvencionExpte.correlativo,causanteSubvencionExpte.tipoDependienteDesc,causanteSubvencionExpte.parentezco,causanteSubvencionExpte.nombre,causanteSubvencionExpte.apellidos,causanteSubvencionExpte.tipoDocumentoDesc,causanteSubvencionExpte.numeroDocumento,causanteSubvencionExpte.fechaNacimiento,causanteSubvencionExpte.esMinusvalido,formatNumero(causanteSubvencionExpte.porcentajeMinusvalia)];
            listaCausanteSubvencionExpte[i]=[causanteSubvencionExpte.id,causanteSubvencionExpte.numeroExpediente,causanteSubvencionExpte.correlativo,causanteSubvencionExpte.tipoDependiente,causanteSubvencionExpte.parentezco,causanteSubvencionExpte.nombre,causanteSubvencionExpte.apellidos,causanteSubvencionExpte.tipoDocumento,causanteSubvencionExpte.numeroDocumento,causanteSubvencionExpte.fechaNacimiento,causanteSubvencionExpte.esMinusvalido,causanteSubvencionExpte.porcentajeMinusvalia,causanteSubvencionExpte.fechaHoraRegistro,causanteSubvencionExpte.fechaHoraModificacion];
        }
    }
    tabCausanteSubvencionExpte.lineas=listaCausanteSubvencionExpteTabla;
    tabCausanteSubvencionExpte.displayTabla();
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        try {
            var div = $("#listaCausantesSubvContenedor")[0];
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
        }catch(err){
        }
    }
}

function crearCargarTablaLineaHistorialSubv(listaLineaHistorialSubv){
    listaLineaHistorialSubvExpte = new Array();
    listaLineaHistorialSubvExpteTabla = new Array();
    tabLineaHistorialSubvExpte = new Tabla(true,$("#historialSubv_descriptor_buscar").val(),$("#historialSubv_descriptor_anterior").val(),$("#historialSubv_descriptor_siguiente").val(), $("#historialSubv_descriptor_mosFilasPag").val(), $("#historialSubv_descriptor_msgNoResultBusq").val(), $("#historialSubv_descriptor_mosPagDePags").val(),$("#historialSubv_descriptor_noRegDisp").val(),$("#historialSubv_descriptor_filtrDeTotal").val(),$("#historialSubv_descriptor_primero").val(),$("#historialSubv_descriptor_ultimo").val(), $("#listaLineaHistorialSubvContenedor")[0], 600);
    tabLineaHistorialSubvExpte.addColumna('300', 'center',$("#historialSubv_textoColumna1").val());
    tabLineaHistorialSubvExpte.addColumna('300', 'center', $("#historialSubv_textoColumna2").val());
    tabLineaHistorialSubvExpte.displayCabecera = true;
    tabLineaHistorialSubvExpte.height = 200;
    
    if(listaLineaHistorialSubv!=null && listaLineaHistorialSubv!=undefined){
        for (var i = 0; i < listaLineaHistorialSubv.length; i++) {
            var lineaHistorialSubvExpte = listaLineaHistorialSubv[i];
            listaLineaHistorialSubvExpteTabla[i]=[lineaHistorialSubvExpte.fechaFinInterrupSituacion,lineaHistorialSubvExpte.fechaProrrReanudSituacion];
            listaLineaHistorialSubvExpte[i]=[lineaHistorialSubvExpte.id,lineaHistorialSubvExpte.numeroExpediente,lineaHistorialSubvExpte.fechaFinInterrupSituacion,lineaHistorialSubvExpte.fechaProrrReanudSituacion,lineaHistorialSubvExpte.fechaHoraRegistro,lineaHistorialSubvExpte.fechaHoraModificacion];
        }
    }
    tabLineaHistorialSubvExpte.lineas=listaLineaHistorialSubvExpteTabla;
    tabLineaHistorialSubvExpte.displayTabla();
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        try {
            var div = $("#listaLineaHistorialSubvContenedor")[0];
            div.children[0].children[0].children[1].style.width = '100%';
            div.children[1].style.width = '100%';
        }catch(err){
        }
    }
}

function pulsarAltaCausante(){
    //alert("pulsarAltaCausante");
    $("#divGeneralAltaEdicionCausante").show();
    // limpiamos el formulario Datos
    limpiarDatosFormCausante();
}

function pulsarModificarCausante(){
    //alert("pulsarModificarCausante");
    var index = tabCausanteSubvencionExpte.selectedIndex;
    if (index != -1) {
        var registro = listaCausanteSubvencionExpte[index];
        $("#divGeneralAltaEdicionCausante").show();
        // limpiamos el formulario Datos
        limpiarDatosFormCausante();
        // Cargamos datos en los input
        $("#idBD").val(registro[0]);
        $("#correlativo").val(registro[2]);
        $("#tipoDependiente").val(registro[3]);
        $("#tipoDependiente").change();
        $("#parentezco").val(registro[4]);
        $("#nombre").val(registro[5]);
        $("#apellidos").val(registro[6]);
        $("#tipoDocumento").val(registro[7]);
        $("#tipoDocumento").change();
        $("#numeroDocumento").val(registro[8]);
        $("#fechaNacimientoPD").val(registro[9]);
        $("#esMinusvalido").val(registro[10]);
        $("#esMinusvalido").change();
        $("#porcentajeMinusvalia").val(formatNumero(registro[11]));
    } else {
        jsp_alerta("A", "No se ha seleccionado ninguna fila.");
    }
}

function pulsarEliminarCausante(){
    //alert("pulsarEliminarCausante");
    var index = tabCausanteSubvencionExpte.selectedIndex;
    if (index != -1) {
        if(confirm("Está seguro de elimiar dicho registro?")){
            //Porcedemos a dar el alta
            var dataParameter = $.extend({}, parametrosLLamadaM01);
            var registro = listaCausanteSubvencionExpte[index];
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'eliminarDatosCausantesSubvencion';
            dataParameter.identificadorBDEliminar = registro[0];
            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta=JSON.parse(JSON.stringify(respuesta,function(key, value) { return value == null ? "" : value }));
                        crearCargarTablaCausantes(respuesta);
                        jsp_alerta("A", "Datos Eliminados Correctamente.");
                    }else{
                        jsp_alerta("A", "Los datos no se ha podido atualizar, si el error persite consulte con soporte.");
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al eliminar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });
        }
    } else {
        jsp_alerta("A", "No se ha seleccionado ninguna fila.");
    }
}

function pulsarAltaLineaHistorialSubv(){
    //alert("pulsarAltaLineaHistorialSubv");
    $("#divGeneralAltaEdicionHistorial").show();
    // limpiamos el formulario Datos
    limpiarDatosFormHistorial();
}

function pulsarModificarLineaHistorialSubv(){
    //alert("pulsarModificarLineaHistorialSubv");
    var index = tabLineaHistorialSubvExpte.selectedIndex;
    if (index != -1) {
        var registro = listaLineaHistorialSubvExpte[index];
        $("#divGeneralAltaEdicionHistorial").show();
        // limpiamos el formulario Datos
        limpiarDatosFormHistorial();
        // Cargamos datos en los input
        $("#idBDH").val(registro[0]);
        $("#fechaFinInterrupSituacion").val(registro[2]);
        $("#fechaProrrReanudSituacion").val(registro[3]);
    } else {
        jsp_alerta("A", "No se ha seleccionado ninguna fila.");
    }
}

function pulsarEliminarLineaHistorialSubv(){
    //alert("pulsarEliminarLineaHistorialSubv");
    var index = tabLineaHistorialSubvExpte.selectedIndex;
    if (index != -1) {
        if(confirm("Está seguro de elimiar dicho registro?")){
            var dataParameter = $.extend({}, parametrosLLamadaM01);
            var registro = listaLineaHistorialSubvExpte[index];
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'eliminarDatosHistorialSubvencion';
            dataParameter.identificadorBDEliminar = registro[0];
            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta=JSON.parse(JSON.stringify(respuesta,function(key, value) { return value == null ? "" : value }));
                        crearCargarTablaLineaHistorialSubv(respuesta);
                        jsp_alerta("A", "Datos Eliminados Correctamente.");
                    }else{
                        jsp_alerta("A", "Los datos no se ha podido atualizar, si el error persite consulte con soporte.");
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al eliminar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });            
        }
    } else {
        jsp_alerta("A", "No se ha seleccionado ninguna fila.");
    }
}


function pulsarAceptarAltaEditarDatos(tipoPestana) {
    if ("CAUSANTESSUBV" == tipoPestana) {
        if (validarDatosCausantes()) {
            //Porcedemos a dar el alta
            var dataParameter = $.extend({}, parametrosLLamadaM01);
            var tipoOperacion = $("#idBD").val() != null && $("#idBD").val() != undefined && $("#idBD").val() != "" ? "1" : "0";
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.control = new Date().getTime();
            dataParameter.tipoOperacion = tipoOperacion;
            dataParameter.operacion = 'guardarDatosCausantesSubvencion';
            dataParameter.jsonCausantesSubvencion = JSON.stringify(prepararJsonDatosCausante());
            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta=JSON.parse(JSON.stringify(respuesta,function(key, value) { return value == null ? "" : value }));
                        crearCargarTablaCausantes(respuesta);
                        $("#divGeneralAltaEdicionCausante").hide();
                        jsp_alerta("A", "Datos Guardados Correctamente.");
                    }else{
                        jsp_alerta("A", "Los datos no se ha podido atualizar, si el error persite consulte con soporte.");
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al guardar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });
        } else {
            jsp_alerta("A", "Cumplimente los datos Obligatorio");
        }
    } else if ("HISTORIALSUBV" == tipoPestana) {
        if (validarDatosHistorialSubvencion()) {
            var dataParameter = $.extend({}, parametrosLLamadaM01);
            var tipoOperacion = $("#idBDH").val() != null && $("#idBDH").val() != undefined && $("#idBDH").val() != "" ? "1" : "0";
            dataParameter.control = new Date().getTime();
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.tipoOperacion = tipoOperacion;
            dataParameter.operacion = 'guardarDatosHistorialSubvencion';
            dataParameter.jsonHistorialSibvencion = JSON.stringify(prepararJsonDatosHistorialSubvencion());
            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta=JSON.parse(JSON.stringify(respuesta,function(key, value) { return value == null ? "" : value }));
                        crearCargarTablaLineaHistorialSubv(respuesta);
                        $("#divGeneralAltaEdicionHistorial").hide();
                        jsp_alerta("A", "Datos Guardados Correctamente.");
                    }else{
                        jsp_alerta("A", "Los datos no se ha podido atualizar, si el error persite consulte con soporte.");
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al guardar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });
        } else {
            jsp_alerta("A", "Cumplimente los datos Obligatorio");
        }
    }

}

function pulsarCancelarAltaEditarDatos(tipoPestana) {
    if ("CAUSANTESSUBV" == tipoPestana) {
        limpiarDatosFormCausante();
        $("#divGeneralAltaEdicionCausante").hide();
    } else if ("HISTORIALSUBV" == tipoPestana) {
        limpiarDatosFormHistorial();
        $("#divGeneralAltaEdicionHistorial").hide();
    }
}

/**
 * Metodo que formatea con separador Miles (.) y decimal(,) 
 * el valor de un numero
 * */
function formatNumero(numero){
	if(numero!=null && numero!=undefined && numero!=""){
            if(isNaN(numero)){
                numero = numero.replace(/[^\d\.,]*/g,'');				
                numero = numero.replace(/\./g,'').replace(/\,/g,'.');
            }
            numero = parseFloat(numero).toFixed(2);
            //numero = numero.toString().split('').reverse().join('').replace(/(?=\d*\.?)(\d{3})/g,'$1.');
            //numero = numero.split('').reverse().join('').replace(/^[\.]/,'');
            var parteEnteraFormat = (numero.toString().split('.')[0] != null && numero.toString().split('.')[0] != undefined ?
                    numero.toString().split('.')[0].replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.')
                    : "");
            var parteDecimal = (numero.toString().split('.')[1] != null && numero.toString().split('.')[1] != undefined ?
                    numero.toString().split('.')[1]
                    : (parteEnteraFormat != null && parteEnteraFormat != undefined && parteEnteraFormat != "" ? "00" : "")
                    );
            numero = parteEnteraFormat.concat(",", parteDecimal);
            return numero;
	}
        return "";
}

function formatNumeroFromInputToBD(input){
    var result="";
    if(input!=null && input!=undefined && input.value!=""){
        result = input.value.replace(/\./g,'').replace(/\,/g,'.');	
    }
    return result;
}

function formatInputAsNumero(input){
    if(input!=null && input!=undefined){
        var valor = formatNumero($(input).val());
        $(input).val(valor);        
    }
}

function validarSoloUnSeparadorDecimalComa(input){
    if(input!=null && input!=undefined){
        if($(input).val().toString().indexOf(',')>=0){//!= $(input).val().toString().lastIndexOf(',')){ 
            return false;
        }
    }
}

function permiteSoloNumeroY2Decimales(e){
    var caracter;
    caracter = e.keyCode;
    //status = caracter;
    if ((caracter > 47 && caracter < 58)
            //||(caracter == 110 ||caracter == 190)
            ||($("#"+e.target.id).val().toString().indexOf(',')<0) // solo una coma
            ) {
        return true;
    }
    return false;
}

function permiteSoloFormatoFechas(e){
    var caracter;
    caracter = e.keyCode;
    //status = caracter;
    if ((caracter > 47 && caracter < 58)
            //||(caracter == 111 ||caracter == 191) 
            || (caracter==47 && contarCaracterBarraFechaEnCadena($("#"+e.target.id).val().toString())<=1)
            ) {
        return true;
    }
    return false;
}

/*
 *  
 */
function contarCaracterBarraFechaEnCadena(cadena){
    if(cadena!=null && cadena!=undefined && cadena != ""){
            var tamanoCadena = cadena.length;
            var tamanoNuevaCadena = cadena.replace(/\//g,'').length;
            return (tamanoCadena-tamanoNuevaCadena);
    }else
        return 0;
}


function mostrarCalfechaNacimientoPD(event) { 
    if(window.event) event = window.event;
    if (document.getElementById("calfechaNacimientoPD").src.indexOf("icono.gif") != -1 )
    showCalendar('forms[0]','fechaNacimientoPD',null,null,null,'','calfechaNacimientoPD','',null,null,null,null,null,null,null, '',event);        
}


function mostrarCalfechaFinInterrupSituacion(event) { 
    if(window.event) event = window.event;
    if (document.getElementById("calfechaFinInterrupSituacion").src.indexOf("icono.gif") != -1 )
    showCalendar('forms[0]','fechaFinInterrupSituacion',null,null,null,'','calfechaFinInterrupSituacion','',null,null,null,null,null,null,null, '',event);        
}


function mostrarCalfechaProrrReanudSituacion(event) { 
    if(window.event) event = window.event;
    if (document.getElementById("calfechaProrrReanudSituacion").src.indexOf("icono.gif") != -1 )
    showCalendar('forms[0]','fechaProrrReanudSituacion',null,null,null,'','calfechaProrrReanudSituacion','',null,null,null,null,null,null,null, '',event);        
}
    
function validarFormatoFecha(input){
   var validado=true;
    if(input!=null && input!=undefined && input.value!=""){
        var arregloDatos = input.value.toString().split("/");
        if(arregloDatos!=null
            && arregloDatos.length===3
            //&& (praseInt(arregloDatos[0])>0 && praseInt(arregloDatos[0])<=31 && arregloDatos[0].length===2) // Dias pendiente mirar dias meses 30 31 29 etc
            //&& (praseInt(arregloDatos[1])>0 && praseInt(arregloDatos[1])<=12 && arregloDatos[1].length===2)
            && (arregloDatos[2].length===4)  //praseInt(arregloDatos[2])>0 && 
           ){
                // Validar Enero 31 Dias, Febrero 28/29
                var fechaValida = new Date(arregloDatos[1]+"/"+arregloDatos[0]+"/"+arregloDatos[2]);
                if(fechaValida!=null && fechaValida!=undefined
                        &&(parseInt(arregloDatos[0])==fechaValida.getDate() && parseInt(arregloDatos[1])==(fechaValida.getMonth()+1))
                     ){
                    // TOdo Correcto
                }else
                    validado=false;
        }else
            validado=false;
    }
    if(!validado){
        jsp_alerta("A","Formato Fecha Invalido. dd/mm/yyyy");
    }
    return validado;
}

function limpiarDatosFormCausante(){
    $("#idBD").val("");
    $("#correlativo").val("");
    $("#tipoDependiente").val("");
    $("#tipoDependiente").change();
    $("#parentezco").val("");
    $("#nombre").val("");
    $("#apellidos").val("");
    $("#tipoDocumento").val("");
    $("#tipoDocumento").change();
    $("#numeroDocumento").val("");
    $("#fechaNacimientoPD").val("");
    $("#esMinusvalido").val("");
    $("#esMinusvalido").change();
    $("#porcentajeMinusvalia").val("");
    $("#divGeneralAltaEdicionCausante :input").prop("style", "border:1px solid #ccc;");
}

function limpiarDatosFormHistorial(){
    $("#idBDH").val("");
    $("#fechaFinInterrupSituacion").val("");
    $("#fechaProrrReanudSituacion").val("");
    $("#divGeneralAltaEdicionHistorial :input").prop("style", "border:1px solid #ccc;");
}

function prepararJsonDatosCausante(){
    var  respuesta = $.extend({}, jsonMelanbide01DepenPerSut);
    respuesta.id=$("#idBD").val()!= undefined && $("#idBD").val()!="" ? $("#idBD").val() : null;
    respuesta.numeroExpediente=$("#numeroExpediente").val()!= undefined && $("#numeroExpediente").val()!="" ? $("#numeroExpediente").val() : null;
    respuesta.correlativo=$("#correlativo").val()!= undefined && $("#correlativo").val()!="" ? $("#correlativo").val() : null; 
    respuesta.tipoDependiente=$("#tipoDependiente").val()!= undefined && $("#tipoDependiente").val()!="" ? $("#tipoDependiente").val() : null; 
    respuesta.parentezco=$("#parentezco").val()!= undefined && $("#parentezco").val()!="" ? $("#parentezco").val() : null;
    respuesta.nombre=$("#nombre").val()!= undefined && $("#nombre").val()!="" ? $("#nombre").val() : null;
    respuesta.apellidos=$("#apellidos").val()!= undefined && $("#apellidos").val()!="" ? $("#apellidos").val() : null;
    respuesta.tipoDocumento=$("#tipoDocumento").val()!= undefined && $("#tipoDocumento").val()!="" ? $("#tipoDocumento").val() : null;
    respuesta.numeroDocumento=$("#numeroDocumento").val()!= undefined && $("#numeroDocumento").val()!="" ? $("#numeroDocumento").val() : null;
    respuesta.fechaNacimiento=$("#fechaNacimientoPD").val()!= undefined && $("#fechaNacimientoPD").val()!="" ? $("#fechaNacimientoPD").val() : null;
    respuesta.esMinusvalido=$("#esMinusvalido").val()!= undefined && $("#esMinusvalido").val()!="" ? $("#esMinusvalido").val() : null;
    respuesta.porcentajeMinusvalia=$("#porcentajeMinusvalia").val()!=null && $("#porcentajeMinusvalia").val()!= undefined && $("#porcentajeMinusvalia").val() != "" ? formatNumeroFromInputToBD($("#porcentajeMinusvalia")[0]) : null;
    return respuesta;
}

function prepararJsonDatosHistorialSubvencion(){
    var  respuesta = $.extend({}, jsonMelanbide01HistoSubv);
    respuesta.id=$("#idBDH").val()!= undefined && $("#idBDH").val()!="" ? $("#idBDH").val() : null;
    respuesta.numeroExpediente=$("#numeroExpediente").val()!= undefined && $("#numeroExpediente").val()!="" ? $("#numeroExpediente").val() : null;
    respuesta.fechaFinInterrupSituacion=$("#fechaFinInterrupSituacion").val()!= undefined && $("#fechaFinInterrupSituacion").val()!="" ? $("#fechaFinInterrupSituacion").val() : null;
    respuesta.fechaProrrReanudSituacion=$("#fechaProrrReanudSituacion").val()!= undefined && $("#fechaProrrReanudSituacion").val()!="" ? $("#fechaProrrReanudSituacion").val() : null;
    return respuesta;
}

function validarDatosCausantes(){
    var validado=true;
    // Comunes
    if($("#tipoDependiente").val()==null ||$("#tipoDependiente").val()==undefined || $("#tipoDependiente").val()==""){
        $("#tipoDependiente").prop("style","border:2px solid red;");
      	validado=false;
    }else{
    	$("#tipoDependiente").prop("style","border:1px solid #ccc;");
    }
    if($("#nombre").val()==null ||$("#nombre").val()==undefined || $("#nombre").val()==""){
        $("#nombre").prop("style","border:2px solid red;");
      	validado=false;
    }else{
    	$("#nombre").prop("style","border:1px solid #ccc;");
    }
    
    if($("#apellidos").val()==null ||$("#apellidos").val()==undefined || $("#apellidos").val()==""){
        $("#apellidos").prop("style", "border:2px solid red;");
        validado = false;
    } else {
        $("#apellidos").prop("style", "border:1px solid #ccc;");
    }
    if($("#fechaNacimientoPD").val()==null ||$("#fechaNacimientoPD").val()==undefined || $("#fechaNacimientoPD").val()==""){
         $("#fechaNacimientoPD").prop("style", "border:2px solid red;");
        validado = false;
    } else {
        $("#fechaNacimientoPD").prop("style", "border:1px solid #ccc;");
        validado=validarFormatoFecha($("#fechaNacimientoPD")[0]);
    }
    
    // Caso de Familiar o Persona Enferma Indicar DNI Parentezco
    if($("#tipoDependiente").val()!="0"){
        if($("#tipoDocumento").val()==null || $("#tipoDocumento").val()==undefined  || $("#tipoDocumento").val()==""){
            $("#tipoDocumento").prop("style", "border:2px solid red;");
            validado = false;
        } else {
            $("#tipoDocumento").prop("style", "border:1px solid #ccc;");
        }
    }
    
    // Si es discapacitdo rellenar el porcentaje
    if($("#esMinusvalido").val()=="S"){
        if($("#porcentajeMinusvalia").val()==null || $("#porcentajeMinusvalia").val()==undefined  || $("#porcentajeMinusvalia").val()=="" 
                || ($("#porcentajeMinusvalia").val()=="0" || $("#porcentajeMinusvalia").val()=="0,0" || $("#porcentajeMinusvalia").val()=="0,00")){
            $("#porcentajeMinusvalia").prop("style", "border:2px solid red;");
            validado = false;
        } else {
            $("#porcentajeMinusvalia").prop("style", "border:1px solid #ccc;");
        }
    }
    return validado;
}

function validarDatosHistorialSubvencion(){
    var validado=true;
    if($("#fechaFinInterrupSituacion").val()==null ||$("#fechaFinInterrupSituacion").val()==undefined || $("#fechaFinInterrupSituacion").val()==""){
         $("#fechaFinInterrupSituacion").prop("style", "border:2px solid red;");
        validado = false;
    } else {
        $("#fechaFinInterrupSituacion").prop("style", "border:1px solid #ccc;");
        validado=validarFormatoFecha($("#fechaFinInterrupSituacion")[0]);
    }
    if($("#fechaProrrReanudSituacion").val()==null ||$("#fechaProrrReanudSituacion").val()==undefined || $("#fechaProrrReanudSituacion").val()==""){
         $("#fechaProrrReanudSituacion").prop("style", "border:2px solid red;");
        validado = false;
    } else {
        $("#fechaProrrReanudSituacion").prop("style", "border:1px solid #ccc;");
        validado=validarFormatoFecha($("#fechaProrrReanudSituacion")[0]);
    }
    return validado;
}

function mostrarValidacionesAlarmasExpte(validacionAlarmasExpteJSON){
    validacionAlarmasExpteJSON = (validacionAlarmasExpteJSON!=null && validacionAlarmasExpteJSON!= undefined ? validacionAlarmasExpteJSON :  JSON.parse($("#validacionesAlarmasExpte").val()));
    $("#divValidacionesAlarmasExpte").empty();
    if(validacionAlarmasExpteJSON!==null && validacionAlarmasExpteJSON!==undefined
            && validacionAlarmasExpteJSON!=="" && validacionAlarmasExpteJSON.length>0){
        //$('#divValidacionesAlarmasExpte').slideDown();
        var resaltarImporteMinimisEmpr=false;
        for (var i = 0; i < validacionAlarmasExpteJSON.length; i++) {
            var validacionAlarma = validacionAlarmasExpteJSON[i];
            //if($("#modalGestionMensajesM01")==null || $("#modalGestionMensajesM01")==undefined)
                var idSpan =(validacionAlarma.codigo !=null ? validacionAlarma.codigo : '-');
                $("#divValidacionesAlarmasExpte").append($('<span id="' + idSpan + '"></span><br/>').text(idSpan + " - " + validacionAlarma.descripcion));
                if(idSpan==41)// Iluminamos en rojo el campo importes recibidos
                    resaltarImporteMinimisEmpr=true;
            //$("#textoMensajeM01").append($('<span id="' + validacionAlarma.codigo + '"></span><br/>').text(validacionAlarma.codigo + " - " + validacionAlarma.descripcion));
        }
        if (resaltarImporteMinimisEmpr)// Iluminamos en rojo el campo importes recibidos
            $("#impTotalRecibidoEmpreReglaMinimis").prop("class", "badge badge-danger");
        else
            $("#impTotalRecibidoEmpreReglaMinimis").prop("class", "badge badge-info");
        //if($("#modalGestionMensajesM01")!=null && $("#modalGestionMensajesM01")!=undefined){
            //$("#modalGestionMensajesM01").modal('show');
        //}else{
            // Prueba solo DIV - En lugar de modal problema ene entorno DES WEBLOGIC
            if ($("#divValidacionesAlarmasExpte").is(':empty')) {
                $('#divValidacionesAlarmasExpte').hide();
            } else {
                $('#divValidacionesAlarmasExpte').slideDown();
            }
            //Fin Pruebas
        //}
    }else{
        //$("#divValidacionesAlarmasExpte").empty();
        $("#divValidacionesAlarmasExpte").hide();
    }
}

function validarControlAlarmasCONCM(){
    var dataParameter = $.extend({}, parametrosLLamadaM01);
    var tipoOperacion = "";
    dataParameter.control = new Date().getTime();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.tipoOperacion = tipoOperacion;
    dataParameter.operacion = 'validarControlAlarmasCONCMSubvencion';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                mostrarValidacionesAlarmasExpte(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al validar los datos datos. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            //$("#textoMensajeM01").append($('<span id="idErrorValidarMsg"></span><br/>').text(mensajeError));
            //$("#modalGestionMensajesM01").modal('show');
            jsp_alerta("A",mensajeError);
        },
        async: false
    });   
}
/***
 * Muestra el mensaje recibido en el Div de Advertencia del modulo 
 * @param {String} mensaje
 * @param {Boolean} vaciarContenidoPrevio Indica si eliminamos el contenido existente en el div; True: Solo mostrar mensaje, False se agregan mensajes.
 * @returns {No return}
 */
function mostrarMensajeErrorModuloEnDiv(mensaje,vaciarContenidoPrevio) {
    if (vaciarContenidoPrevio != null && vaciarContenidoPrevio != undefined
            && vaciarContenidoPrevio) {
        limpiarOcultarMensajeErrorModuloEnDiv()
    }
    $("#divErroresOperacionesM01").append($('<span id="mostrarMensajeErrorModuloEnDiv"></span><br/>').text(mensaje));
    $('#divErroresOperacionesM01').slideDown();
}

function limpiarOcultarMensajeErrorModuloEnDiv() {
    $("#divErroresOperacionesM01").empty();
    $("#divErroresOperacionesM01").hide();
}


function actualizarValorImportesEmpresaReglaMinimis() {
    var dataParameter = $.extend({}, parametrosLLamadaM01);
    var tipoOperacion = "";
    dataParameter.control = new Date().getTime();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.tipoOperacion = tipoOperacion;
    dataParameter.operacion = 'getImporteTotalSubvCONCMUlt3AnioEmpresaExpte';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $("#impTotalRecibidoEmpreReglaMinimis").text(respuesta);
            }else{
                $("#impTotalRecibidoEmpreReglaMinimis").text("-");
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al guardar los datos.' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            $("#impTotalRecibidoEmpreReglaMinimis").text("-");
        },
        async: false
    });
}

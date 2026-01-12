   
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var parametrosLlamada = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE08'
        , operacion: null
        , tipo: 0
        , control: new Date().getTime()
        , ejercicio: null
        ,numeroexpediente: null
        , procedimiento: null
        , estadoFirma: null
        , tipoDocumento: null
        , fechaEnvioPeticionDesde: null
        , fechaEnvioPeticionHasta: null
        , fechaEnvioPeticion: null
        , documentoNotificado:null
    };
 var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
 var tableLog;   
 var tableLogJob; 
 
$(document).ready(function() {
    //Ocultamos la tabla
    tableLog = $('#tableLog').DataTable({
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": -1});
    pleaseWait('off');
    $("#tablaLog").attr("hidden",true);
    
     tableLogJob = $('#tableLogJob').DataTable({
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": 10});
    pleaseWait('off');
    $("#tablaLogJob").attr("hidden",true);
});

function reiniciarFiltros() {
    //Ocultamos la tabla
    tableLog.destroy();
    tableLog = $('#tableLog').DataTable({
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": -1});
    pleaseWait('off');
    $("#tablaLog").attr("hidden",true);
    $("#tablaLogJob").attr("hidden",true);
    $("#filtrar").attr("hidden",false);
    $("#reiniciarFiltro").attr("hidden",true);
    
    
    $("#ejercicio").attr("disabled",false);
    $("#listaProcedimiento").attr("disabled",false);
    $("#estadoFirma").attr("disabled",false);
    $("#tipoDocumento").attr("disabled",false);
    $("#numeroExpediente").attr("disabled",false);
    $("#fechahoraenviopeticion").attr("disabled",false);
    $("#fechahoraenviopeticiondesde").attr("disabled",false);
    $("#fechahoraenviopeticionhasta").attr("disabled",false);
    $("#documentoNotificado").attr("disabled",false);

}
    
    
//Function filtrar
function lanzarProcesoFiltroTablaLog() {
    $("#tablaLog").attr("hidden",false);
     $("#tablaLogJob").attr("hidden",true);
    $("#filtrar").attr("hidden",true);
    $("#reiniciarFiltro").attr("hidden",false);
    var datosParameter = $.extend({}, parametrosLlamada);
    
    //Desactivamos los filtros 
    $("#ejercicio").attr("disabled",true);
    $("#listaProcedimiento").attr("disabled",true);
    $("#estadoFirma").attr("disabled",true);
    $("#tipoDocumento").attr("disabled",true);
    $("#numeroExpediente").attr("disabled",true);
    $("#fechahoraenviopeticion").attr("disabled",true);
    $("#fechahoraenviopeticiondesde").attr("disabled",true);
    $("#fechahoraenviopeticionhasta").attr("disabled",true);
    $("#documentoNotificado").attr("disabled",true);

    //Guardamos los valores 
    datosParameter.control = new Date().getTime();
    datosParameter.ejercicio = $("#ejercicio").val();
    datosParameter.procedimiento = $("#listaProcedimiento").val();
    datosParameter.estadoFirma = $("#estadoFirma").val();
    datosParameter.tipoDocumento = $("#tipoDocumento").val();
    datosParameter.numeroExpediente = $("#numeroExpediente").val();
    datosParameter.fechaEnvioPeticion = $("#fechahoraenviopeticion").val();
    datosParameter.fechaEnvioPeticionDesde = $("#fechahoraenviopeticiondesde").val();
    datosParameter.fechaEnvioPeticionHasta = $("#fechahoraenviopeticionhasta").val();
    datosParameter.documentoNotificado = $("#documentoNotificado").val();
    if ((datosParameter.fechaEnvioPeticionDesde === "" || validarFecha(datosParameter.fechaEnvioPeticionDesde))
    && (datosParameter.fechaEnvioPeticionHasta === "" || validarFecha(datosParameter.fechaEnvioPeticionHasta))){

        tableLog.destroy();
        datosParameter.operacion = "cargarDatosPrincipalConsultaEstadoFirmaFiltrosSinPaginar";
        tableLog = $('#tableLog').DataTable({
            "serverSide": true,
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": -1,
            "ajax": {
                "url": urlBaseLlamada,
                "data": datosParameter,
                "method": "POST",
                "beforeSend": function () {
                    pleaseWait('on');
                },
                "dataSrc": function (response) {
                    var data = response.lstRegistros;
                    if (data !== null) {
                        var all = [];
                        for (var i = 0; i < data.length; i++) {
                            var row = {
                            ejercicio: data[i].ejercicio,
                            procedimiento: data[i].procedimiento,
                            numExpediente: data[i].numExpediente,
                            fechaRegistrado: data[i].fechaRegistrado,
                            fechaFirmado: data[i].fechaFirmado,
                            descDocumento: data[i].descDocumento,
                            estado: data[i].estado,
                            descEstado: data[i].descEstado,
                            docNotificado: data[i].docNotificado
                            };
                            all.push(row);
                        }
                        pleaseWait('off');
                        return all;
                    }
                }
            },
            "columns": [
            {"data": "ejercicio"},
            {"data": "procedimiento"},
            {"data": "numExpediente"},
            {"data": "fechaRegistrado"},
            {"data": "fechaFirmado"},
            {"data": "descDocumento"},
            {"data": "estado"},
            {"data": "descEstado"},
            {"data": "docNotificado"}
            ]
        });
    } else {
        alert("Fecha con formato no valido");
    }
}


function lanzarProcesoFiltrado() {
     $("#tablaLogJob").attr("hidden",true);
    $("#tablaLog").attr("hidden",false);
    $("#filtrar").attr("hidden",true);
    $("#reiniciarFiltro").attr("hidden",false);
    var datosParameter = $.extend({}, parametrosLlamada);
    
    //Desactivamos los filtros 
    $("#ejercicio").attr("disabled",true);
    $("#listaProcedimiento").attr("disabled",true);
    $("#estadoFirma").attr("disabled",true);
    $("#tipoDocumento").attr("disabled",true);
    $("#numeroExpediente").attr("disabled",true);
    $("#fechahoraenviopeticion").attr("disabled",true);
    $("#fechahoraenviopeticiondesde").attr("disabled",true);
    $("#fechahoraenviopeticionhasta").attr("disabled",true);
    $("#documentoNotificado").attr("disabled",true);

    //Guardamos los valores 
    datosParameter.control = new Date().getTime();
    datosParameter.ejercicio = $("#ejercicio").val();
    datosParameter.procedimiento = $("#listaProcedimiento").val();
    datosParameter.estadoFirma = $("#estadoFirma").val();
    datosParameter.tipoDocumento = $("#tipoDocumento").val();
    datosParameter.numeroExpediente = $("#numeroExpediente").val();
    datosParameter.fechaEnvioPeticion = $("#fechahoraenviopeticion").val();
    datosParameter.fechaEnvioPeticionDesde = $("#fechahoraenviopeticiondesde").val();
    datosParameter.fechaEnvioPeticionHasta = $("#fechahoraenviopeticionhasta").val();
    datosParameter.documentoNotificado = $("#documentoNotificado").val();
    if ((datosParameter.fechaEnvioPeticionDesde === "" || validarFecha(datosParameter.fechaEnvioPeticionDesde))
    && (datosParameter.fechaEnvioPeticionHasta === "" || validarFecha(datosParameter.fechaEnvioPeticionHasta))){

        tableLog.destroy();
        datosParameter.operacion = "enviarAFirmarFiltrado";
        tableLog = $('#tableLog').DataTable({
            "serverSide": true,
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": -1,
            "ajax": {
                "url": urlBaseLlamada,
                "data": datosParameter,
                "method": "POST",
                "beforeSend": function () {
                    pleaseWait('on');
                },
                "dataSrc": function (response) {
                    var data = response.lstRegistros;
                    if (data !== null) {
                        var all = [];
                        for (var i = 0; i < data.length; i++) {
                            var row = {
                            ejercicio: data[i].ejercicio,
                            procedimiento: data[i].procedimiento,
                            numExpediente: data[i].numExpediente,
                            fechaRegistrado: data[i].fechaRegistrado,
                            fechaFirmado: data[i].fechaFirmado,
                            descDocumento: data[i].descDocumento,
                            estado: data[i].estado,
                            descEstado: data[i].descEstado,
                            docNotificado: data[i].docNotificado
                            };
                            all.push(row);
                        }
                        pleaseWait('off');
                        return all;
                    }
                    wait(1000);
                    reiniciarFiltros();
                    alert("Se han enviado los documentos indicados a processar para ser enviados al portafirmas");
                }
            },
            "columns": [
            {"data": "ejercicio"},
            {"data": "procedimiento"},
            {"data": "numExpediente"},
            {"data": "fechaRegistrado"},
            {"data": "fechaFirmado"},
            {"data": "descDocumento"},
            {"data": "estado"},
            {"data": "descEstado"},
            {"data": "docNotificado"}
            ]
        });
    } else {
        alert("Fecha con formato no valido");
    } 
      
}

//Function filtrar
function lanzarProcesoConsultarTablaLogJob() {
    $("#tablaLogJob").attr("hidden",false);
     $("#tablaLog").attr("hidden",true);
    $("#filtrar").attr("hidden",false);
    $("#reiniciarFiltro").attr("hidden",true);
    var datosParameter = $.extend({}, parametrosLlamada);
    


    //Guardamos los valores 
    datosParameter.control = new Date().getTime();
   

        tableLogJob.destroy();
        datosParameter.operacion = "cargarDatosConsultaEstadoLogJobFirma";
        tableLogJob = $('#tableLogJob').DataTable({
            "serverSide": true,
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "language": {
            "search": "",
            "paginate": {
                "previous": $("#texto-anterior").val(),
                "next": $("#texto-siguiente").val()
            },
            "lengthMenu": $("#texto-mosFilasPag").val(),
            "zeroRecords": $("#texto-msgNoResultBusq").val(),
            "info": $("#texto-mosPagDePags").val(),
            "infoEmpty": $("#texto-noRegDisp").val(),
            "infoFiltered": $("#texto-filtrDeTotal").val()
             },
            "ajax": {
                "url": urlBaseLlamada,
                "data": datosParameter,
                "method": "POST",
                "beforeSend": function () {
                    pleaseWait('on');
                },
                "dataSrc": function (response) {
                    var data = response.lstRegistros;
                    if (data !== null) {
                        var all = [];
                        for (var i = 0; i < data.length; i++) {
                            var row = {
                            ejercicio: data[i].ejercicio,
                            procedimiento: data[i].procedimiento,
                            numExpediente: data[i].numExpediente,
                            fechaRegistrado: data[i].fechaRegistrado,
                            fechaFirmado: data[i].fechaFirmado,
                            descDocumento: data[i].descDocumento,
                            estado: data[i].estado,
                            mensajeErrorJob: data[i].mensajeErrorJob
                            };
                            all.push(row);
                        }
                        pleaseWait('off');
                        return all;
                    }
                }
            },
            "columns": [
            {"data": "ejercicio"},
            {"data": "procedimiento"},
            {"data": "numExpediente"},
            {"data": "fechaRegistrado"},
            {"data": "fechaFirmado"},
            {"data": "descDocumento"},
            {"data": "estado"},
            {"data": "mensajeErrorJob"}

            ]
        });
 
}

function wait(ms){
   var start = new Date().getTime();
   var end = start;
   while(end < start + ms) {
     end = new Date().getTime();
  }
}

//Funcion validar fecha
function validarFecha(fecha) {
    return fecha.match(/^([0-2][0-9]|(3)[0-1])(\/)(((0)[0-9])|((1)[0-2]))(\/)\d{4}$/);
}

function numeroEntero(e) {
    var caracter;
    caracter = e.keyCode;
    status = caracter;
    if (caracter > 47 && caracter < 58) {
        return true;
    }
    return false;
}


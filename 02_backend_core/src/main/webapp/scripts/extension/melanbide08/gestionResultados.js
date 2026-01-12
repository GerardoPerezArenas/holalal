   
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
    
$(document).ready(function() {
    
    var datosParameter = $.extend({}, parametrosLlamada);
    datosParameter.ejercicio = $("#ejercicio").val();
    datosParameter.operacion = "cargarDatosPrincipalConsultaEstadoFirmaFiltros";
    tableLog = $('#tableLog').DataTable({
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
});

//Function filtrar
function lanzarProcesoFiltroTablaLog() {
    var datosParameter = $.extend({}, parametrosLlamada);
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
        datosParameter.operacion = "cargarDatosPrincipalConsultaEstadoFirmaFiltros";
        tableLog = $('#tableLog').DataTable({
            "serverSide": true,
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "language": {
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


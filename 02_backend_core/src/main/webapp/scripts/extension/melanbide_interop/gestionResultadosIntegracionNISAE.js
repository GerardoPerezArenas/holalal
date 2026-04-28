   
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var parametrosLlamada = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE_INTEROP'
        , operacion: null
        , tipo: 0
        , control: new Date().getTime()
        , ejercicioHHFF: null
        , procedimientoHHFF: null
        , webservices: null
        , estadoExpediente: null
        , numeroExpedienteDesde: null
        , numeroExpedienteHasta: null
        , fechaEnvioPeticion: null
        , estado: null
        , resultado: null
        , documentoInteresado: null
    };
 var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
 var tableLog;
 var seleccionadas = [];
 var seleccionadasMap = {};
    
$(document).ready(function() {
    
    var datosParameter = $.extend({}, parametrosLlamada);
    datosParameter.operacion = "cargarPantallaLogServiciosNISAEFiltros";
    registrarEventosSeleccion();
    actualizarResumenSeleccion();
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
        "drawCallback": function () {
            rehidratarSeleccionEnTabla();
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
                            id: data[i].id,
                            codOrganizacion: data[i].codOrganizacion,
                            ejercicioHHFF: data[i].ejercicioHHFF,
                            procedimientoHHFF: data[i].procedimientoHHFF,
                            estadoExpediente: data[i].estadoExpediente,
                            numeroExpedienteDesde: data[i].numeroExpedienteDesde,
                            numeroExpedienteHasta: data[i].numeroExpedienteHasta,
                            textoJsonDatosEnviados: data[i].textoJsonDatosEnviados,
                            numeroExpediente: data[i].numeroExpediente,
                            fechaHoraEnvioPeticion: data[i].fechaHoraEnvioPeticion,
                            codigoEstadoSecundario: data[i].codigoEstadoSecundario,
                            estado: data[i].estado,
                            descripcionEstado: data[i].descripcionEstado,
                            resultado: data[i].resultado,
                            textoJsonDatosRecibidos: data[i].textoJsonDatosRecibidos,
                            documentoInteresado: data[i].documentoInteresado,
                            tiempoEstimadoRespuesta: data[i].tiempoEstimadoRespuesta,
                            territorioHistorico: data[i].territorioHistorico,
                            observaciones: data[i].observaciones,
                            idPeticionPadre: data[i].idPeticionPadre,
                            fkWSSolicitado: data[i].fkWSSolicitado
                        };
                        row.claveSeleccion = construirClaveFila(row);
                        all.push(row);
                    }
                    pleaseWait('off');
                    return all;
                }
            }
        },
        "columns": [
            {"data": "claveSeleccion", "orderable": false, "searchable": false, "width": "30px",
             "render": function (data, type, row) {
                var checked = seleccionadasMap[data] ? " checked=\"checked\"" : "";
                return "<input type=\"checkbox\" class=\"check-fila\" data-clave=\"" + data + "\"" + checked + " />";
             }},
            {"data": "id"},
            {"data": "codOrganizacion"},
            {"data": "ejercicioHHFF"},
            {"data": "procedimientoHHFF"},
            {"data": "estadoExpediente"},
            {"data": "numeroExpedienteDesde"},
            {"data": "numeroExpedienteHasta"},
            {"data": "textoJsonDatosEnviados"},
            {"data": "numeroExpediente"},
            {"data": "fechaHoraEnvioPeticion"},
            {"data": "codigoEstadoSecundario"},
            {"data": "estado"},
            {"data": "descripcionEstado"},
            {"data": "resultado"},
            {"data": "textoJsonDatosRecibidos"},
            {"data": "documentoInteresado"},
            {"data": "tiempoEstimadoRespuesta"},
            {"data": "territorioHistorico"},
            {"data": "observaciones"},
            {"data": "idPeticionPadre"},
            {"data": "fkWSSolicitado"}
        ]
    });
});

function construirClaveFila(row) {
    if (row && row.id !== undefined && row.id !== null && row.id !== "") {
        return String(row.id);
    }
    return "";
}

function actualizarResumenSeleccion() {
    $("#numFilasSeleccionadas").text(seleccionadas.length);
}

function eliminarSeleccionada(clave) {
    var nuevasSeleccionadas = [];
    for (var i = 0; i < seleccionadas.length; i++) {
        if (seleccionadas[i] !== clave) {
            nuevasSeleccionadas.push(seleccionadas[i]);
        }
    }
    seleccionadas = nuevasSeleccionadas;
    actualizarResumenSeleccion();
}

function limpiarSeleccionadas() {
    seleccionadas = [];
    seleccionadasMap = {};
    $("#check-todas-filas").prop("checked", false);
    actualizarResumenSeleccion();
}

function sincronizarCheckboxCabecera() {
    var checkboxesVisibles = $('#tableLog tbody .check-fila:visible');
    if (checkboxesVisibles.length === 0) {
        $("#check-todas-filas").prop("checked", false);
        return;
    }

    var todosMarcados = true;
    checkboxesVisibles.each(function () {
        if (!$(this).is(":checked")) {
            todosMarcados = false;
            return false;
        }
    });
    $("#check-todas-filas").prop("checked", todosMarcados);
}

function rehidratarSeleccionEnTabla() {
    if (!tableLog) {
        return;
    }
    $('#tableLog tbody tr').each(function () {
        var rowData = tableLog.row(this).data();
        var $checkbox = $(this).find(".check-fila");
        if ($checkbox.length === 0 || !rowData) {
            return;
        }
        var clave = rowData.claveSeleccion ? rowData.claveSeleccion : construirClaveFila(rowData);
        if (!clave) {
            return;
        }
        $checkbox.attr("data-clave", clave).prop("checked", !!seleccionadasMap[clave]);
    });
    sincronizarCheckboxCabecera();
}

function registrarEventosSeleccion() {
    $(document).off("click", "#tableLog .check-fila").on("click", "#tableLog .check-fila", function () {
        var clave = $(this).attr("data-clave");
        if (!clave) {
            var rowData = tableLog.row($(this).closest("tr")).data();
            clave = rowData && rowData.claveSeleccion ? rowData.claveSeleccion : construirClaveFila(rowData);
            $(this).attr("data-clave", clave);
        }
        if (!clave) {
            return;
        }
        if ($(this).is(":checked")) {
            if (!seleccionadasMap[clave]) {
                seleccionadasMap[clave] = true;
                seleccionadas.push(clave);
                actualizarResumenSeleccion();
            }
        } else if (seleccionadasMap[clave]) {
            delete seleccionadasMap[clave];
            eliminarSeleccionada(clave);
        }
        sincronizarCheckboxCabecera();
    });

    $(document).off("click", "#check-todas-filas").on("click", "#check-todas-filas", function () {
        var marcar = $(this).is(":checked");
        $('#tableLog tbody .check-fila:visible').each(function () {
            var clave = $(this).attr("data-clave");
            if (!clave) {
                var rowData = tableLog.row($(this).closest("tr")).data();
                clave = rowData && rowData.claveSeleccion ? rowData.claveSeleccion : construirClaveFila(rowData);
                $(this).attr("data-clave", clave);
            }
            if (!clave) {
                return;
            }
            $(this).prop("checked", marcar);
            if (marcar) {
                if (!seleccionadasMap[clave]) {
                    seleccionadasMap[clave] = true;
                    seleccionadas.push(clave);
                    actualizarResumenSeleccion();
                }
            } else if (seleccionadasMap[clave]) {
                delete seleccionadasMap[clave];
                eliminarSeleccionada(clave);
            }
        });
    });
}

//Function filtrar
function lanzarProcesoFiltroTablaLog() {
    var datosParameter = $.extend({}, parametrosLlamada);
    datosParameter.control = new Date().getTime();
    datosParameter.ejercicioHHFF = $("#ejercicio").val();
    datosParameter.procedimientoHHFF = $("#listaProcedimiento").val();
    datosParameter.webservices = $("#listaWebServices").val();
    datosParameter.estadoExpediente = $("#estadoExpediente").val();
    datosParameter.numeroExpedienteDesde = $("#numeroExpedienteDesde").val();
    datosParameter.numeroExpedienteHasta = $("#numeroExpedienteHasta").val();
    datosParameter.fechaEnvioPeticion = $("#fechahoraenviopeticion").val();
    datosParameter.estado = $("#estado").val();
    datosParameter.resultado = $("#resultado").val();
    datosParameter.documentoInteresado = $("#documentoInteresado").val();
    if (datosParameter.fechaEnvioPeticion === "" || validarFecha(datosParameter.fechaEnvioPeticion)) {

        limpiarSeleccionadas();
        tableLog.destroy();
        datosParameter.operacion = "cargarPantallaLogServiciosNISAEFiltros";
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
            "drawCallback": function () {
                rehidratarSeleccionEnTabla();
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
                                id: data[i].id,
                                codOrganizacion: data[i].codOrganizacion,
                                ejercicioHHFF: data[i].ejercicioHHFF,
                                procedimientoHHFF: data[i].procedimientoHHFF,
                                estadoExpediente: data[i].estadoExpediente,
                                numeroExpedienteDesde: data[i].numeroExpedienteDesde,
                                numeroExpedienteHasta: data[i].numeroExpedienteHasta,
                                textoJsonDatosEnviados: data[i].textoJsonDatosEnviados,
                                numeroExpediente: data[i].numeroExpediente,
                                fechaHoraEnvioPeticion: data[i].fechaHoraEnvioPeticion,
                                codigoEstadoSecundario: data[i].codigoEstadoSecundario,
                                estado: data[i].estado,
                                descripcionEstado: data[i].descripcionEstado,
                                resultado: data[i].resultado,
                                textoJsonDatosRecibidos: data[i].textoJsonDatosRecibidos,
                                documentoInteresado: data[i].documentoInteresado,
                                tiempoEstimadoRespuesta: data[i].tiempoEstimadoRespuesta,
                                territorioHistorico: data[i].territorioHistorico,
                                observaciones: data[i].observaciones,
                                idPeticionPadre: data[i].idPeticionPadre,
                                fkWSSolicitado: data[i].fkWSSolicitado
                            };
                            row.claveSeleccion = construirClaveFila(row);
                            all.push(row);
                        }
                        pleaseWait('off');
                        return all;
                    }
                }
            },
            "columns": [
                {"data": "claveSeleccion", "orderable": false, "searchable": false, "width": "30px",
                 "render": function (data, type, row) {
                    var checked = seleccionadasMap[data] ? " checked=\"checked\"" : "";
                    return "<input type=\"checkbox\" class=\"check-fila\" data-clave=\"" + data + "\"" + checked + " />";
                 }},
                {"data": "id"},
                {"data": "codOrganizacion"},
                {"data": "ejercicioHHFF"},
                {"data": "procedimientoHHFF"},
                {"data": "estadoExpediente"},
                {"data": "numeroExpedienteDesde"},
                {"data": "numeroExpedienteHasta"},
                {"data": "textoJsonDatosEnviados"},
                {"data": "numeroExpediente"},
                {"data": "fechaHoraEnvioPeticion"},
                {"data": "codigoEstadoSecundario"},
                {"data": "estado"},
                {"data": "descripcionEstado"},
                {"data": "resultado"},
                {"data": "textoJsonDatosRecibidos"},
                {"data": "documentoInteresado"},
                {"data": "tiempoEstimadoRespuesta"},
                {"data": "territorioHistorico"},
                {"data": "observaciones"},
                {"data": "idPeticionPadre"},
                {"data": "fkWSSolicitado"}
            ]
        });
    } else {
        alert("Fecha con formato no valido");
    }
}

//Function exportar
function lanzarProcesoExportarTablaLog() {
    if (seleccionadas.length === 0) {
        alert("Debe seleccionar al menos una fila para exportar.");
        return;
    }
    pleaseWait('on');
    var datosParameter = $.extend({}, parametrosLlamada);
    datosParameter.operacion = "exportarLogServiciosNISAEFiltros";
    datosParameter.control = new Date().getTime();
    datosParameter.ejercicioHHFF = $("#ejercicio").val();
    datosParameter.procedimientoHHFF = $("#listaProcedimiento").val();
    datosParameter.webservices = $("#listaWebServices").val();
    datosParameter.estadoExpediente = $("#estadoExpediente").val();
    datosParameter.numeroExpedienteDesde = $("#numeroExpedienteDesde").val();
    datosParameter.numeroExpedienteHasta = $("#numeroExpedienteHasta").val();
    datosParameter.fechaEnvioPeticion = $("#fechahoraenviopeticion").val();
    datosParameter.estado = $("#estado").val();
    datosParameter.resultado = $("#resultado").val();
    datosParameter.documentoInteresado = $("#documentoInteresado").val();
    datosParameter.idsSeleccionados = seleccionadas.join(",");
    if (datosParameter.fechaEnvioPeticion === "" || validarFecha(datosParameter.fechaEnvioPeticion)) {
        window.location.href = urlBaseLlamada +  "?" + $.param(datosParameter);
    } else {
        alert("Formato de fecha no valido");
    }
    pleaseWait('off');
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

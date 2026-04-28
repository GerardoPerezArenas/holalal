   
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
 var seleccionadasDataMap = {};
    
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
    seleccionadasDataMap = {};
    $("#check-todas-filas").prop("checked", false);
    actualizarResumenSeleccion();
}

function copiarDatosFila(rowData) {
    if (!rowData) {
        return null;
    }
    return {
        id: rowData.id || "",
        codOrganizacion: rowData.codOrganizacion || "",
        ejercicioHHFF: rowData.ejercicioHHFF || "",
        procedimientoHHFF: rowData.procedimientoHHFF || "",
        estadoExpediente: rowData.estadoExpediente || "",
        numeroExpedienteDesde: rowData.numeroExpedienteDesde || "",
        numeroExpedienteHasta: rowData.numeroExpedienteHasta || "",
        numeroExpediente: rowData.numeroExpediente || "",
        fechaHoraEnvioPeticion: rowData.fechaHoraEnvioPeticion || "",
        estado: rowData.estado || "",
        descripcionEstado: rowData.descripcionEstado || "",
        resultado: rowData.resultado || "",
        documentoInteresado: rowData.documentoInteresado || "",
        tiempoEstimadoRespuesta: rowData.tiempoEstimadoRespuesta || "",
        territorioHistorico: rowData.territorioHistorico || "",
        observaciones: rowData.observaciones || ""
    };
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
        var rowData = tableLog.row($(this).closest("tr")).data();
        if (!clave) {
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
                seleccionadasDataMap[clave] = copiarDatosFila(rowData);
                actualizarResumenSeleccion();
            }
        } else if (seleccionadasMap[clave]) {
            delete seleccionadasMap[clave];
            delete seleccionadasDataMap[clave];
            eliminarSeleccionada(clave);
        }
        sincronizarCheckboxCabecera();
    });

    $(document).off("click", "#check-todas-filas").on("click", "#check-todas-filas", function () {
        var marcar = $(this).is(":checked");
        $('#tableLog tbody .check-fila:visible').each(function () {
            var clave = $(this).attr("data-clave");
            var rowData = tableLog.row($(this).closest("tr")).data();
            if (!clave) {
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
                    seleccionadasDataMap[clave] = copiarDatosFila(rowData);
                    actualizarResumenSeleccion();
                }
            } else if (seleccionadasMap[clave]) {
                delete seleccionadasMap[clave];
                delete seleccionadasDataMap[clave];
                eliminarSeleccionada(clave);
            }
        });
    });
}

function escaparHtml(valor) {
    var texto = valor === null || valor === undefined ? "" : String(valor);
    return texto.replace(/&/g, "&amp;")
               .replace(/</g, "&lt;")
               .replace(/>/g, "&gt;")
               .replace(/\"/g, "&quot;")
               .replace(/'/g, "&#39;");
}

function lanzarProcesoExportarPdfSeleccion() {
    if (seleccionadas.length === 0) {
        alert("Debe seleccionar al menos una fila para generar el PDF.");
        return;
    }

    var filas = [];
    for (var i = 0; i < seleccionadas.length; i++) {
        var clave = seleccionadas[i];
        if (seleccionadasDataMap[clave]) {
            filas.push(seleccionadasDataMap[clave]);
        }
    }

    if (filas.length === 0) {
        alert("No hay datos disponibles para las filas seleccionadas. Vuelva a cargar las filas y reintente.");
        return;
    }

    var html = "";
    html += "<html><head><title>Log resultados NISAE</title>";
    html += "<style>body{font-family:Arial,sans-serif;font-size:11px;}";
    html += "h2{margin-bottom:10px;}table{width:100%;border-collapse:collapse;}";
    html += "th,td{border:1px solid #555;padding:4px;vertical-align:top;}th{background:#efefef;}";
    html += "</style></head><body>";
    html += "<h2>Log resultados NISAE - Filas seleccionadas</h2>";
    html += "<table><thead><tr>";
    html += "<th>ID</th><th>Cod Organización</th><th>Ejercicio</th><th>Procedimiento</th>";
    html += "<th>Estado Expediente</th><th>Nº Exp. Desde</th><th>Nº Exp. Hasta</th><th>Nº Expediente</th>";
    html += "<th>Fecha Envío</th><th>Estado</th><th>Descripción Estado</th><th>Resultado</th>";
    html += "<th>Documento Interesado</th><th>Tiempo Estimado</th><th>Territorio Histórico</th><th>Observaciones</th>";
    html += "</tr></thead><tbody>";

    for (var j = 0; j < filas.length; j++) {
        var fila = filas[j];
        html += "<tr>";
        html += "<td>" + escaparHtml(fila.id) + "</td>";
        html += "<td>" + escaparHtml(fila.codOrganizacion) + "</td>";
        html += "<td>" + escaparHtml(fila.ejercicioHHFF) + "</td>";
        html += "<td>" + escaparHtml(fila.procedimientoHHFF) + "</td>";
        html += "<td>" + escaparHtml(fila.estadoExpediente) + "</td>";
        html += "<td>" + escaparHtml(fila.numeroExpedienteDesde) + "</td>";
        html += "<td>" + escaparHtml(fila.numeroExpedienteHasta) + "</td>";
        html += "<td>" + escaparHtml(fila.numeroExpediente) + "</td>";
        html += "<td>" + escaparHtml(fila.fechaHoraEnvioPeticion) + "</td>";
        html += "<td>" + escaparHtml(fila.estado) + "</td>";
        html += "<td>" + escaparHtml(fila.descripcionEstado) + "</td>";
        html += "<td>" + escaparHtml(fila.resultado) + "</td>";
        html += "<td>" + escaparHtml(fila.documentoInteresado) + "</td>";
        html += "<td>" + escaparHtml(fila.tiempoEstimadoRespuesta) + "</td>";
        html += "<td>" + escaparHtml(fila.territorioHistorico) + "</td>";
        html += "<td>" + escaparHtml(fila.observaciones) + "</td>";
        html += "</tr>";
    }

    html += "</tbody></table></body></html>";

    var ventanaPdf = window.open("", "_blank");
    if (!ventanaPdf) {
        alert("No se ha podido abrir la ventana de impresión. Revise el bloqueador de popups.");
        return;
    }

    ventanaPdf.document.open();
    ventanaPdf.document.write(html);
    ventanaPdf.document.close();
    ventanaPdf.focus();
    ventanaPdf.print();
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

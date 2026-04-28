   
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var parametrosLlamada = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE09'
        , operacion: null
        , tipo: 0
        , control: new Date().getTime()
        , ejercicio: null
        , numeroexpediente: null
        , procedimiento: null
        , tramite: null
        , tipoDocumento: null
        , fechaEnvioPeticionDesde: null
        , fechaEnvioPeticionHasta: null
        , fechaEnvioPeticion: null
        , documentoNotificado:null
        , codProcedimiento: null
    };
 var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
 var tableLog;   
 var comboTramite;
 var seleccionadas = [];
 var seleccionadasMap = {};
    
   $(document).ready(function() {
    //Ocultamos la tabla
    tableLog = $('#tableLog').DataTable({
            "ordering": false,
            "autoWidth": false,
            "scrollX": true,
            "scrollCollapse": true,
            "searching": false,
            "paging": false,
            "pageLength": -1,
            "drawCallback": function () {
                rehidratarSeleccionEnTabla();
            }});
    pleaseWait('off');
    $("#tablaLog").attr("hidden",true);
   comboTramite = document.getElementById("listaTramite");
  // comboTramite.deactivate(); 
   registrarEventosSeleccion();
   registrarValidacionExportacion();
   });

function construirClaveFila(data) {
    if (data && data.id !== undefined && data.id !== null && data.id !== "") {
        return String(data.id);
    }

    var ejercicio = data && data.ejercicio ? data.ejercicio : "";
    var procedimiento = data && data.procedimiento ? data.procedimiento : "";
    var numExpediente = data && data.numExpediente ? data.numExpediente : "";
    var fechaRegistrado = data && data.fechaRegistrado ? data.fechaRegistrado : "";
    var desTramite = data && data.desTramite ? data.desTramite : "";
    return [ejercicio, procedimiento, numExpediente, fechaRegistrado, desTramite].join("|");
}

function registrarEventosSeleccion() {
    $(document).off("click", "#tableLog .check-fila").on("click", "#tableLog .check-fila", function () {
        var clave = $(this).attr("data-clave");
        if (!clave) {
            return;
        }

        if ($(this).is(":checked")) {
            if (!seleccionadasMap[clave]) {
                seleccionadasMap[clave] = true;
                seleccionadas.push(clave);
            }
        } else {
            if (seleccionadasMap[clave]) {
                delete seleccionadasMap[clave];
                eliminarSeleccionada(clave);
            }
        }

        sincronizarCheckboxCabecera();
    });

    $(document).off("click", "#check-todas-filas").on("click", "#check-todas-filas", function () {
        var marcar = $(this).is(":checked");
        $('#tableLog tbody .check-fila:visible').each(function () {
            var clave = $(this).attr("data-clave");
            if (!clave) {
                return;
            }

            $(this).prop("checked", marcar);
            if (marcar) {
                if (!seleccionadasMap[clave]) {
                    seleccionadasMap[clave] = true;
                    seleccionadas.push(clave);
                }
            } else {
                if (seleccionadasMap[clave]) {
                    delete seleccionadasMap[clave];
                    eliminarSeleccionada(clave);
                }
            }
        });
    });
}

function eliminarSeleccionada(clave) {
    var nuevasSeleccionadas = [];
    for (var i = 0; i < seleccionadas.length; i++) {
        if (seleccionadas[i] !== clave) {
            nuevasSeleccionadas.push(seleccionadas[i]);
        }
    }
    seleccionadas = nuevasSeleccionadas;
}

function limpiarSeleccionadas() {
    seleccionadas = [];
    seleccionadasMap = {};
    $("#check-todas-filas").prop("checked", false);
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
        if (!rowData || typeof rowData !== "object") {
            return;
        }

        var clave = rowData.claveSeleccion ? rowData.claveSeleccion : construirClaveFila(rowData);
        var marcado = !!seleccionadasMap[clave];
        $(this).find(".check-fila").attr("data-clave", clave).prop("checked", marcado);
    });

    sincronizarCheckboxCabecera();
}

function validarSeleccionParaExportacion() {
    if (seleccionadas.length === 0) {
        alert("Debe seleccionar al menos una fila para exportar.");
        return false;
    }
    return true;
}

function registrarValidacionExportacion() {
    $("#exportar, #btnExportar, #exportarSeleccion, #descargar")
        .off("click.validarSeleccion")
        .on("click.validarSeleccion", function () {
            return validarSeleccionParaExportacion();
        });
}

 function cargarDesplegableTramites(){
         var datosParameter = $.extend({}, parametrosLlamada);
            var url = urlBaseLlamada + '/tarea=preparar&modulo=MELANBIDE09&operacion=obtenerTramitesDesplegable&tipo=0';
            datosParameter.codProcedimiento = document.getElementById("listaProcedimiento").value;
                    //$('#codProcedimiento').val();
            datosParameter.operacion = "obtenerTramitesDesplegable";
            
           
            pleaseWait('on');
            try{
                $.ajax({
                    url:  urlBaseLlamada,
                    type: 'POST',
                    async: true,
                    data: datosParameter,
                    success: procesarRespuestaCargarDesplegableTramites
                });           
            }catch(Err){
                pleaseWait('off');
                mostrarMensajeError();
            }
        }
    
function removeOptions(selectElement) {
   var i, L = selectElement.options.length - 1;
   for(i = L; i >= 0; i--) {
      selectElement.remove(i);
   }
}

function procesarRespuestaCargarDesplegableTramites(result){
        pleaseWait('off');
           var parser      = new DOMParser ();
            var responseDoc = parser.parseFromString (result, "text/html");
    //Eliminamos los valores actuales del select   
    removeOptions(comboTramite);

        //Obtenemos los nuevos valores del select
        var result = responseDoc.getElementById("listaTramite");
        //Assignamos los valores al select 
        for (var i = 0; i < result.length; i++){
            var resultado = result[i];
            var element = document.createElement("option");
            element.text = resultado.title;
            element.value= resultado.value;
            comboTramite.append(element);
        }
    }

//Function filtrar
function lanzarProcesoFiltroTablaLog() {
    var datosParameter = $.extend({}, parametrosLlamada);
    datosParameter.control = new Date().getTime();
    datosParameter.ejercicio = $("#ejercicio").val();
    datosParameter.procedimiento = $("#listaProcedimiento").val();
    datosParameter.tramite = $("#listaTramite").val();
    datosParameter.numeroExpediente = $("#numeroExpediente").val();
    datosParameter.fechaEnvioPeticion = $("#fechahoraenviopeticion").val();
    datosParameter.fechaEnvioPeticionDesde = $("#fechahoraenviopeticiondesde").val();
    datosParameter.fechaEnvioPeticionHasta = $("#fechahoraenviopeticionhasta").val();
    if ((datosParameter.fechaEnvioPeticionDesde === "" || validarFecha(datosParameter.fechaEnvioPeticionDesde))
    && (datosParameter.fechaEnvioPeticionHasta === "" || validarFecha(datosParameter.fechaEnvioPeticionHasta))){

        limpiarSeleccionadas();
        tableLog.destroy();
        datosParameter.operacion = "cargarDatosPrincipalConsultaSinNotificarFiltros";
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
                            var fechaRegistrado = "";
                            if (data[i].fechaRegistrado) {
                                var trozosFecha = data[i].fechaRegistrado.split(" ");
                                if (trozosFecha.length > 1) {
                                    fechaRegistrado = trozosFecha[0] + trozosFecha[1];
                                } else {
                                    fechaRegistrado = data[i].fechaRegistrado;
                                }
                            }
                            var row = {
                            id: data[i].id,
                            ejercicio: data[i].ejercicio,
                            procedimiento: data[i].procedimiento,
                            numExpediente: data[i].numExpediente,
                            fechaRegistrado: fechaRegistrado,
                            desTramite: data[i].desTramite
                            };
                            row.claveSeleccion = construirClaveFila(row);
                            all.push(row);
                        }
                        pleaseWait('off');
                        $("#tablaLog").attr("hidden",false);
                        return all;
                    }
                }
            },
            "drawCallback": function () {
                rehidratarSeleccionEnTabla();
            },
            "columns": [
            {"data": "claveSeleccion", "orderable": false, "searchable": false, "width": "30px",
             "render": function (data, type, row) {
                 var checked = seleccionadasMap[data] ? " checked=\"checked\"" : "";
                 return "<input type=\"checkbox\" class=\"check-fila\" data-clave=\"" + data + "\"" + checked + " />";
             }},
            {"data": "ejercicio"},
            {"data": "procedimiento"},
            {"data": "numExpediente"},
            {"data": "fechaRegistrado"},
            {"data": "desTramite"}
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

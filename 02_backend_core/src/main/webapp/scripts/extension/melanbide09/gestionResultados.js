   
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
   comboTramite = document.getElementById("listaTramite");
  // comboTramite.deactivate(); 
   
   });

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
                            var row = {
                            ejercicio: data[i].ejercicio,
                            procedimiento: data[i].procedimiento,
                            numExpediente: data[i].numExpediente,
                            fechaRegistrado: data[i].fechaRegistrado.split(" ")[0]+data[i].fechaRegistrado.split(" ")[1],
                            desTramite: data[i].desTramite
                            };
                            all.push(row);
                        }
                        pleaseWait('off');
                        $("#tablaLog").attr("hidden",false);
                        return all;
                    }
                }
            },
            "columns": [
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


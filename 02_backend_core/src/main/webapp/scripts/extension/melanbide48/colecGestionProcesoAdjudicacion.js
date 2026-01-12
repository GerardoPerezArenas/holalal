/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var paramLLamadaM48ProcAdj={
        tarea:'preparar'
        ,modulo:'MELANBIDE48'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,idBdConvocatoria:null
    };


$(document).ready(function() {
    cagarDesplegableConvocatoriasProcAdj();
    asignarEventos();
});

function cagarDesplegableConvocatoriasProcAdj(){
    var dataParameter = $.extend({}, paramLLamadaM48ProcAdj);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'cagarDesplegableConvocatoriasProcAdj';
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $.each(respuesta, function (index, item) {
                    $('#listaConvocatorias').append($('<option>', {
                        value: item.codigo,
                        text: item.descripcion,
                        title: item.descripcionTitle
                    }));
                });
            } else {
                jsp_alerta("A", "4"===$("#idiomaUsuario").val()?"Ezin izan dira Deialdien Konboa kargatzeko datuak jaso, persita errorea euskarri bidez kontsultatzen bada.":"No se ha podido recoger los datos para cargar el Combo de Convocatorias, si el error persite consulte con soporte.");
            }
        },
        //dataType: "json",
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function asignarEventos(){
    $("#btnEjecutarPrcoceso").click(function () {
        lanzarProcesoAdjudicacion();
    });
    $("#btnGenerarExcel").click(function () {
        generarExcelProcesoAdjudicacion();
    });
}

function lanzarProcesoAdjudicacion(){
    if(validarOperacion()){
        var dataParameter = $.extend({}, paramLLamadaM48ProcAdj);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'lanzarProcesoAdjudicacion';
        dataParameter.idBdConvocatoria = $("#listaConvocatorias").val();
        $("#expedientesEstadoNoOK").hide();
        $("#listaExpedientesEstadoNoOK").empty();
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    if("OK"===respuesta.codigo){
                        jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Prozesua behar bezala gauzatu da" : "Proceso ejecutado correctamente");
                    }else{
                        jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Errore bat aurkeztu da prozesua gauzatzean. " + respuesta.mensaje : "Se ha presentado un error en la ejecucion del proceso. " + respuesta.mensaje);
                    }
                    if(respuesta.expedientesEstadoIncorrecto){
                        $("#listaExpedientesEstadoNoOK").empty();
                        $.each(respuesta.expedientesEstadoIncorrectoList, function (index, item) {
                            $("#listaExpedientesEstadoNoOK").append($('<span>', {
                                text: item,
                            })).append($('<br>'));
                        });
                        $("#tituloListaExpedientesEstadoNoOK").text($("#textoExpedientesEstadoNoOk").val());                        
                        $("#tituloListaExpedientesEstadoNoOK").prepend("("+respuesta.expedientesEstadoIncorrectoList.length+") ");
                        $("#expedientesEstadoNoOK").attr("title",("4" === $("#idiomaUsuario").val() ? "Esleipen-prozesuan sartuta ez daudenak.": "No incluidos en el proceso adjudicacion."))
                        $("#expedientesEstadoNoOK").show();
                    }else
                        $("#expedientesEstadoNoOK").hide();
                } else {
                    jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Errore bat aurkeztu da prozesua gauzatzean, errore horrek jarraitzen badu, kontsultatu euskarri-taldea." : "Se ha presentado un error en la ejecucion del proceso, si el error persiste consulte al equipo de soporte.");
                }
            },
            //dataType: "json",
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }
}

function generarExcelProcesoAdjudicacion(){
    if(validarOperacion()){
        var dataParameter = $.extend({}, paramLLamadaM48ProcAdj);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'generarExcelProcesoAdjudicacion';
        dataParameter.idBdConvocatoria = $("#listaConvocatorias").val();
        window.open($("#urlBaseLlamadaM48").val()+"?"+$.param(dataParameter),"_blank");
//        $.ajax({
//            type: 'POST',
//            url: $("#urlBaseLlamadaM48").val(),
//            data: dataParameter,
//            success: function (respuesta) {
//                if (respuesta !== null) {
//                    jsp_alerta("A","Diferente null");
//                } else {
//                    jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Errore bat aurkeztu da prozesua gauzatzean, errore horrek jarraitzen badu, kontsultatu euskarri-taldea." : "Se ha presentado un error en la ejecucion del proceso, si el error persiste consulte al equipo de soporte.");
//                }
//            },
//            //dataType: "json",
//            error: function (jqXHR, textStatus, errorThrown) {
//                var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
//                jsp_alerta("A", mensajeError);
//            },
//            async: false
//        });
    }
}

function validarOperacion(){
    if($("#listaConvocatorias").val()=== null   || $("#listaConvocatorias").val()===undefined
            || $("#listaConvocatorias").val()===""){
        jsp_alerta("A", "4"===$("#idiomaUsuario").val()?"Aukeratu deialdi bat":"Selecciona una convocatoria");
        return false; 
    }else{
        if(Number($("#listaConvocatorias").val())<3){
            jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "2021etik aurrerako deialdietarako soilik erabil daitezkeen prozesuak" : "Procesos solo disponibles para convocatorias a partir de 2021");
            return false;
        }
    }
    return true;
}
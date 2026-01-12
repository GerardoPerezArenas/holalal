/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    //
    $('[data-toggle="tooltip"]').tooltip({ 
        placement: 'top',
        boundary: 'window', 
        container: 'body',
        trigger : 'manual'
    });
});

function guardarDatosMelanbide88(codPestana) {
    if (validarDatosObligatoriosMelanbide88(codPestana)) {
        var dataParameter = $.extend({}, parametrosLLamadaM88);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        switch (codPestana) {
            case PESTANA_SOCIOS:
                dataParameter.operacion="guardarDatosSocio"
                break;
            case PESTANA_INVERSIONES:
                dataParameter.operacion="guardarDatosInversion"
                break;
            case PESTANA_SUBVENCIONES:
                dataParameter.operacion="guardarDatosSubvencion"
                break;               
            default:
                dataParameter.operacion="sinOperacionAEjecutarError"
                break;
        }
        var tipoOperacion = $("#identificadorBDGestionar").val() !== null && $("#identificadorBDGestionar").val() !== undefined && $("#identificadorBDGestionar").val() !== "" ? "1" : "0";
        dataParameter.tipoOperacion = tipoOperacion;
        dataParameter.jsonMelanbide88Socios = JSON.stringify(prepararJsonMelanbide88Socios());
        dataParameter.jsonMelanbide88Inversiones = JSON.stringify(prepararJsonMelanbide88Inversiones());
        dataParameter.jsonMelanbide88Subsolic = JSON.stringify(prepararJsonMelanbide88Subsolic());
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM88").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                        return value === null ? "" : value;
                    }));
                    self.parent.opener.retornoXanelaAuxiliar(respuesta);
                    jsp_alerta("A", $("#msg_datos_procesados_ok").val());
                } else {
                    jsp_alerta("A", $("#msg_datos_procesados_error").val());
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al guardar los datos. Datos no actualizados. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    } else {
        jsp_alerta("A", $("#msg_generico_campos_obligatorios").val());
    }
}

function validarDatosObligatoriosMelanbide88(codPestana){
    switch (codPestana) {
        case PESTANA_SOCIOS:
            return validarDatosObligatoriosSocios();
            break;
        case PESTANA_INVERSIONES:
            return validarDatosObligatoriosIversiones();
            break;
        case PESTANA_SUBVENCIONES:
            return validarDatosObligatoriosSubvenciones();
            break;
        default:            
            break;
    }
}

function validarDatosObligatoriosSocios(){
    $('[data-toggle="tooltip"]').tooltip('hide');
    var respuesta = true;
    if(!($("#dniNieSocio").val()!==null && $("#dniNieSocio").val()!==undefined && $("#dniNieSocio").val()!=="")){
        $("#dniNieSocio").tooltip('show');
        respuesta=false;
    }
    if(!($("#nombreSocio").val()!==null && $("#nombreSocio").val()!==undefined && $("#nombreSocio").val()!=="")){
        $("#nombreSocio").tooltip('show');
        respuesta=false;
    }
    if(!($("#apellido1Socio").val()!==null && $("#apellido1Socio").val()!==undefined && $("#apellido1Socio").val()!=="")){
        $("#apellido1Socio").tooltip('show');
        respuesta=false;
    }
    return respuesta;
}

function validarDatosObligatoriosIversiones(){
    $('[data-toggle="tooltip"]').tooltip('hide');
    var respuesta = true;
    if(!($("#fecha").val()!==null && $("#fecha").val()!==undefined && $("#fecha").val()!=="")){
        $("#fecha").tooltip('show');
        respuesta=false;
    }
    if(!($("#numFactura").val()!==null && $("#numFactura").val()!==undefined && $("#numFactura").val()!=="")){
        $("#numFactura").tooltip('show');
        respuesta=false;
    }
    if(!($("#descripcion").val()!==null && $("#descripcion").val()!==undefined && $("#descripcion").val()!=="")){
        $("#descripcion").tooltip('show');
        respuesta=false;
    }
    if(!($("#nombProveedor").val()!==null && $("#nombProveedor").val()!==undefined && $("#nombProveedor").val()!=="")){
        $("#nombProveedor").tooltip('show');
        respuesta=false;
    }
    if(!($("#importe").val()!==null && $("#importe").val()!==undefined && $("#importe").val()!=="")){
        $("#importe").tooltip('show');
        respuesta=false;
    }
    if(!($("#pagada").val()!==null && $("#pagada").val()!==undefined && $("#pagada").val()!=="")){
        $("#pagada").tooltip('show');
        respuesta=false;
    }
    if(!($("#fechaPago").val()!==null && $("#fechaPago").val()!==undefined && $("#fechaPago").val()!=="")){
        $("#fechaPago").tooltip('show');
        respuesta=false;
    }
    return respuesta;
}

function validarDatosObligatoriosSubvenciones(){
    $('[data-toggle="tooltip"]').tooltip('hide');
    var respuesta = true;
    if(!($("#estado").val()!==null && $("#estado").val()!==undefined && $("#estado").val()!=="")){
        $("#estado").tooltip('show');
        respuesta=false;
    }
    if(!($("#organismo").val()!==null && $("#organismo").val()!==undefined && $("#organismo").val()!=="")){
        $("#organismo").tooltip('show');
        respuesta=false;
    }
    if(!($("#objeto").val()!==null && $("#objeto").val()!==undefined && $("#objeto").val()!=="")){
        $("#objeto").tooltip('show');
        respuesta=false;
    }
    if(!($("#importe").val()!==null && $("#importe").val()!==undefined && $("#importe").val()!=="")){
        $("#importe").tooltip('show');
        respuesta=false;
    }
    if(!($("#fecha").val()!==null && $("#fecha").val()!==undefined && $("#fecha").val()!=="")){
        $("#fecha").tooltip('show');
        respuesta=false;
    }
    return respuesta;
}

function prepararJsonMelanbide88Socios(){
    var dataParameter = $.extend({}, jsonMelanbide88Socios);
    dataParameter.id = ($("#identificadorBDGestionar").val() !== null && $("#identificadorBDGestionar").val() !== undefined && $("#identificadorBDGestionar").val() !== "" ? $("#identificadorBDGestionar").val() : null);
    dataParameter.num_exp = $("#numeroExpediente").val();
    dataParameter.dniNieSocio = $("#dniNieSocio").val();
    dataParameter.nombreSocio = $("#nombreSocio").val();
    dataParameter.apellido1Socio = $("#apellido1Socio").val();
    dataParameter.apellido2Socio = $("#apellido2Socio").val();
    return dataParameter;
}

function prepararJsonMelanbide88Inversiones(){
    var dataParameter = $.extend({}, jsonMelanbide88Inversiones);
    dataParameter.id = ($("#identificadorBDGestionar").val() !== null && $("#identificadorBDGestionar").val() !== undefined && $("#identificadorBDGestionar").val() !== "" ? $("#identificadorBDGestionar").val() : null);
    dataParameter.num_exp = $("#numeroExpediente").val();
    dataParameter.fecha = $("#fecha").val();
    dataParameter.numFactura = $("#numFactura").val();
    dataParameter.descripcion = $("#descripcion").val();
    dataParameter.nombProveedor = $("#nombProveedor").val();
    dataParameter.importe = $("#importe").val();
    dataParameter.pagada = $("#pagada").val();
    dataParameter.fechaPago = $("#fechaPago").val();
    return dataParameter;
}

function prepararJsonMelanbide88Subsolic(){
    var dataParameter = $.extend({}, jsonMelanbide88Subsolic);
    dataParameter.id = ($("#identificadorBDGestionar").val() !== null && $("#identificadorBDGestionar").val() !== undefined && $("#identificadorBDGestionar").val() !== "" ? $("#identificadorBDGestionar").val() : null);
    dataParameter.num_exp = $("#numeroExpediente").val();
    dataParameter.estado = $("#estado").val();
    dataParameter.organismo = $("#organismo").val();
    dataParameter.objeto = $("#objeto").val();
    dataParameter.importe = $("#importe").val();
    dataParameter.fecha = $("#fecha").val();
    return dataParameter;
}

function cancelarEdicionMelanbide88(codPestana){
    switch (codPestana) {
        case PESTANA_SOCIOS:
            limpiarFormularioMelanbide88Socios();            
            break;
        case PESTANA_INVERSIONES:
            limpiarFormularioMelanbide88Inversiones();            
            break;
        case PESTANA_SUBVENCIONES:
            limpiarFormularioMelanbide88Subsolic();            
            break;
        default:            
            break;
    }
    window.close();
}

function limpiarFormularioMelanbide88Socios(){
    $("#pantallaEditarDatos").val("");
    $("#numeroExpediente").val("");
    $("#codigoProcedimiento").val("");
    $("#codigoOrganizacion").val("");
    $("#urlBaseLlamadaM48").val("");
    $("#identificadorBDGestionar").val("");
    $("#msg_generico_campos_obligatorios").val("");
    $("#dniNieSocio").val("");
    $("#nombreSocio").val("");
    $("#apellido1Socio").val("");
    $("#apellido2Socio").val("");
    $('[data-toggle="tooltip"]').tooltip('hide');
}

function limpiarFormularioMelanbide88Inversiones(){
    $("#pantallaEditarDatos").val("");
    $("#numeroExpediente").val("");
    $("#codigoProcedimiento").val("");
    $("#codigoOrganizacion").val("");
    $("#urlBaseLlamadaM48").val("");
    $("#identificadorBDGestionar").val("");
    $("#msg_generico_campos_obligatorios").val("");
    $("#fecha").val("");
    $("#numFactura").val("");
    $("#descripcion").val("");
    $("#nombProveedor").val("");
    $("#importe").val("");
    $("#pagada").val("");
    $("#fechaPago").val("");
    $('[data-toggle="tooltip"]').tooltip('hide');
}

function limpiarFormularioMelanbide88Subsolic(){
    $("#pantallaEditarDatos").val("");
    $("#numeroExpediente").val("");
    $("#codigoProcedimiento").val("");
    $("#codigoOrganizacion").val("");
    $("#urlBaseLlamadaM48").val("");
    $("#identificadorBDGestionar").val("");
    $("#msg_generico_campos_obligatorios").val("");
    $("#estado").val("");
    $("#organismo").val("");
    $("#objeto").val("");
    $("#importe").val("");
    $("#fecha").val("");
    $('[data-toggle="tooltip"]').tooltip('hide');
}
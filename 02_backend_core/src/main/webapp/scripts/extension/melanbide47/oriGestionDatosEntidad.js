var paramLLamadaM47ColecDatosEntAgrup={
        tarea:'preparar'
        ,modulo:'MELANBIDE47'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
    };
    

$(document).ready(function() {
    // Convocatorias colec a partir de 2023
    if($("#idBDConvocatoriaExpediente").val()>30){
        getMostrarDatosCertificadosCalidadEntidad();
    }

});

function getMostrarDatosCertificadosCalidadEntidad(){
    var dataParameter = $.extend({}, paramLLamadaM47ColecDatosEntAgrup);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getMostrarDatosCertificadosCalidadEntidad';
    dataParameter.idEntidad = $("#codEntidadOri14").val();
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM47").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $.each(respuesta, function (index, item) {
                    if(item.valorSNSolicitud===1 && $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]') != null
                        && $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]')!= undefined
                        && $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]').length > 0  ){
                        $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]').prop("checked",true);
                    }
                    if(item.valorSNValidado===1 && $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]') != null
                        && $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]')!= undefined
                        && $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]').length > 0 ){
                        $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]').prop("checked",true);
                    }
                    if(item.valorSNValidado===0 && $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]') != null
                        && $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]')!= undefined
                        && $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]').length > 0){
                        $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]').prop("checked",true);
                    }
                });
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos de certificados de calidad, si el error persiste consulte con soporte.");
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


function getValorSeleccionadoCompromisoIgualdad(){
        var respuesta = "";
        if($("#tableCompromisoIgualdad input:checked")!= null && $("#tableCompromisoIgualdad input:checked")!= undefined)
        {
            $("#tableCompromisoIgualdad input:checked").each(function(elementoIndex,elemento){
                respuesta=elemento.id.substring(elemento.id.indexOf("_")+1);
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCertificadoCalidad(){
        var respuesta = "";
        if($("#tableCertificadoCalidad input:checked")!= null && $("#tableCertificadoCalidad input:checked")!= undefined)
        {
            $("#tableCertificadoCalidad input:checked").each(function(elementoIndex,elemento){
                respuesta+= (respuesta != "" ? ",":"");
                respuesta+= elemento.id.substring(elemento.id.indexOf("_")+1);
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCompromisoIgualdadValidado(){
        var respuesta = "";
        if($("#tableCompromisoIgualdad input:checked")!= null && $("#tableCompromisoIgualdad input:checked")!= undefined)
        {
            $("#tableCompromisoIgualdad input:checked").each(function(elementoIndex,elemento){
                // Es solo una opcion,no hace falta leer el id, basta con saber si se valida o no
                //respuesta+= (respuesta != "" ? ",":"");
                respuesta+= //elemento.id.substring(elemento.id.indexOf("_")+1)  +"_"+ (
                    $("#compIgualdadOpcionSValidado") != null && $("#compIgualdadOpcionSValidado")!=undefined && $("#compIgualdadOpcionSValidado").prop("checked")
                        ? getValorSeleccionadoCompromisoIgualdad()
                        : ($("#compIgualdadOpcionNValidado") != null && $("#compIgualdadOpcionNValidado")!=undefined && $("#compIgualdadOpcionNValidado").prop("checked")?0:"")
                //)
                ;
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCertificadoCalidadValidado(){
        var respuesta = "";
        if($('[name^="certificadoCalidadOpcionValT"]input:checked')!= null && $('[name^="certificadoCalidadOpcionValT"]input:checked')!= undefined)
        {
            $('[name^="certificadoCalidadOpcionValT"]input:checked').each(function(i,campo){
                respuesta+= (respuesta != "" ? ",":"");
                respuesta+=campo.id.substring(campo.id.indexOf("_")+1)+"_"+ campo.value;
            });
        }
        return respuesta;
    }
var paramLLamadaM48ColecDatosEntAgrup={
        tarea:'preparar'
        ,modulo:'MELANBIDE48'
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
    var dataParameter = $.extend({}, paramLLamadaM48ColecDatosEntAgrup);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getMostrarDatosCertificadosCalidadEntidad';
    dataParameter.idEntidad = $("#idBDEntidad").val();
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $.each(respuesta, function (index, item) {
                    if(item.valorSNSolicitud===1 && $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]') != null
                        && $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]')!= undefined ){
                        $('#tableCertificadoCalidad [id^="certificadoCalidad_'+item.idCertificado+'"]').prop("checked",true);
                    }
                    if(item.valorSNValidado===1 && $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]') != null
                        && $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]')!= undefined ){
                        $('[id^="certificadoCalidadOpcionValTS_'+item.idCertificado+'"]').prop("checked",true);
                    }
                    if(item.valorSNValidado===0 && $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]') != null
                        && $('[id^="certificadoCalidadOpcionValTN_'+item.idCertificado+'"]')!= undefined ){
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

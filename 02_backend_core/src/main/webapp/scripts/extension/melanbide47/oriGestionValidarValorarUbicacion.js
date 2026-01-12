var paramLLamadaM47OriDatosValValoUbic={
        tarea:'preparar'
        ,modulo:'MELANBIDE47'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,identificadorBDEliminar:null
};

var oriUbicacionCTCriterioPuntos={
    criterioPuntosTrayectoria:0.2,
    criterioPuntosSegudosLocales:2,
    criterioPuntosEspacioComplementario:1,
    zeroFloat:0.0
};

var oriUbicacionCTCriterioPuntosMaximo={
    criterioPuntosTrayectoria:12,
    criterioPuntosSegudosLocales:4,
    criterioPuntosCertificadosCalidad:5,
    criterioPuntosCompromisoIgualdad:5,
    criterioPuntosEspacioComplementario:1
};

var listaOriCertCalidadPuntuacion;
var listaOriCompIgualdadPuntuacion;


$(document).ready(function() {
    // Eventos
    $("#btnGuardarDatosValoracion").click(function () {
        guardarDatosValoracionUbicacionCentTra();
    });

    // Operaciones
    // Convocatorias a partir de 2023
    if($("#idBDConvocatoriaExpediente").val()>30){
        getMostrarDatosCertificadosCalidadValidadosEntidad();
        listaOriCertCalidadPuntuacion=getListaOriCertCalidadPuntuacion();
        listaOriCompIgualdadPuntuacion=getListaOriCompIgualdadPuntuacion();
    }

    rellenarVacios();
    calcularValoracion();

});


function getMostrarDatosCertificadosCalidadValidadosEntidad(){
    var dataParameter = $.extend({}, paramLLamadaM47OriDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getMostrarDatosCertificadosCalidadEntidad';
    dataParameter.idEntidad = $("#idBDEntidad").val();
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM47").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $.each(respuesta, function (index, item) {
                    if(item.valorSNSolicitud===1)  {
                        if($('[id^="certificadoCalidad_'+item.idCertificado+'"]input') != undefined){
                            $('[id^="certificadoCalidad_'+item.idCertificado+'"]input').prop("checked",true);
                        }
                    }
                    if(item.valorSNValidado===1)  {
                        if($('[id^="validarCertificadoCalidad_'+item.idCertificado+'"]input') != undefined){
                            $('[id^="validarCertificadoCalidad_'+item.idCertificado+'"]input').prop("checked",true);
                        }
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

function getListaOriCertCalidadPuntuacion(){
    var dataParameter = $.extend({}, paramLLamadaM47OriDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaOriCertCalidadPuntuacion';
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    var respuestaFinal = new Array();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM47").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                respuestaFinal=respuesta;
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos de certificados de calidad, si el error persiste consulte con soporte.");
                 respuestaFinal=new Array();
            }
        },
        //dataType: "json",
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuestaFinal;
}

function getListaOriCompIgualdadPuntuacion(){
    var dataParameter = $.extend({}, paramLLamadaM47OriDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaOriCompIgualdadPuntuacion';
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    var respuestaFinal = new Array();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM47").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                respuestaFinal=respuesta;
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos de Compromiso igualdad de genero, si el error persiste consulte con soporte.");
                respuestaFinal=new Array();
            }
        },
        //dataType: "json",
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado ERROR  al procesar la Peticion.  ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuestaFinal;
}

function getPuntuacionPorCompromisoIgualdadGenero(){
    var puntuacion = 0.0;
    if($("#validarCompromisoIgualdadGenero") != undefined){
        if($("#validarCompromisoIgualdadGenero").prop("checked")){
            if(listaOriCompIgualdadPuntuacion != undefined){
                $.each(listaOriCompIgualdadPuntuacion, function (index, item) {
                    if(item.codigo==$("#validarCompromisoIgualdadGenero").val()){
                        puntuacion=item.puntuacion;
                    }
                });
            }
        }
    }
    return puntuacion;
}

function getPuntuacionPorTenerCertificadoCalidad(){
    var puntuacionCertificadosCalidadCal = oriUbicacionCTCriterioPuntos.zeroFloat;
    if($('input[id^="validarCertificadoCalidad_"]') != undefined && $('input[id^="validarCertificadoCalidad_"]').length >0){
        $('input[id^="validarCertificadoCalidad_"]').each(function (index, item) {
            if(listaOriCertCalidadPuntuacion != undefined){
                $.each(listaOriCertCalidadPuntuacion, function (index2, item2) {
                    if($(item).prop("checked") && item2.codigo==$(item).prop("id").split("_").pop()){
                       puntuacionCertificadosCalidadCal = (puntuacionCertificadosCalidadCal>=5 ? 5 : puntuacionCertificadosCalidadCal+item2.puntuacion);
                    }
                    if(puntuacionCertificadosCalidadCal==5)
                        return true;
                });
            }
            if(puntuacionCertificadosCalidadCal==5)
                return true;
        });
    }
    return puntuacionCertificadosCalidadCal;
}

function rellenarVacios() {
    if (document.getElementById('numDespachosValid') != null && document.getElementById('numDespachosValid').value == '') {
        document.getElementById('numDespachosValid').value = 'N';
    }
    if (document.getElementById('aulaGrupalValid') != null && document.getElementById('aulaGrupalValid').value == '') {
        document.getElementById('aulaGrupalValid').value = 'N';
    }
    if (document.getElementById('disp1EspaAdicionalValid') != null && document.getElementById('disp1EspaAdicionalValid').value == '') {
        document.getElementById('disp1EspaAdicionalValid').value = 'N';
    }
    if (document.getElementById('dispEspaOrdeIntWifiValid') != null && document.getElementById('dispEspaOrdeIntWifiValid').value == '') {
        document.getElementById('dispEspaOrdeIntWifiValid').value = 'N';
    }
}

function calcularValoracion() {
    var total = 0;
    //Trayectoria
    try {
        if ($("#idBDConvocatoriaExpediente").val() > 4) {
            var tryaTemp = document.getElementById('trayectoriaValid').value;
            tryaTemp=(tryaTemp!=null && tryaTemp!=undefined && tryaTemp!="" ? String(tryaTemp).replace(",",".") : tryaTemp);
            var trayectoriaVal = parseFloat(tryaTemp);
            if (!isNaN(trayectoriaVal)) {
                trayectoriaVal = trayectoriaVal * 0.2;
                if (trayectoriaVal > 12) {
                    trayectoriaVal = 12;
                }
                document.getElementById('trayectoriaValor').value = trayectoriaVal.toFixed(2);
                total += trayectoriaVal;
            }
        } else {
            var trayectoriaVal = parseInt(document.getElementById('trayectoriaValid').value);
            if (!isNaN(trayectoriaVal)) {
                //trayectoriaVal = (trayectoriaVal - 2) * 2;
                if (trayectoriaVal < 0) {
                    trayectoriaVal = 0;
                } else if (trayectoriaVal > 10) {
                    trayectoriaVal = 10;
                }
                document.getElementById('trayectoriaValor').value = trayectoriaVal;
                total += trayectoriaVal;
            }
        }

    } catch (err) {

    }

    //Ubicacion
    try {
        var ubicacion = parseFloat(document.getElementById('ubicacionValid').value);
        if (!isNaN(ubicacion)) {
            if (ubicacion < 0) {
                ubicacion = 0;
            } else if (ubicacion > 10) {
                ubicacion = 10;
            }
            document.getElementById('ubicacionValor').value = ubicacion;
            total += ubicacion;
        }
    } catch (err) {

    }
    if ($("#ejercicio").val() < 2017) {
        //Numero despachos
        try {
            if (document.getElementById('numDespachosValid').checked) {
                document.getElementById('despachosValor').value = 4;
                total += 4;
            } else {
                document.getElementById('despachosValor').value = 0;
            }
        } catch (err) {

        }

        //Aula grupal
        try {
            if (document.getElementById('aulaGrupalValid').checked)
            {
                document.getElementById('aulasValor').value = 1;
                total += 1;
            } else
            {
                document.getElementById('aulasValor').value = 0;
            }
        } catch (err) {

        }
    } else {
        //Disponibilidad de un espacio adicional en el mismo ambito
        try {
            if (document.getElementById('disp1EspaAdicionalValid') != null && document.getElementById('disp1EspaAdicionalValid').checked) {
                document.getElementById('disp1EspaAdicionalValor').value = 4;
                total += 4;
            } else {
                document.getElementById('disp1EspaAdicionalValor').value = 0;
            }
        } catch (err) {

        }

        //Disponibilidad de un espacio con ordenador, internet y wifi
        try {
            if (document.getElementById('dispEspaOrdeIntWifiValid') != null && document.getElementById('dispEspaOrdeIntWifiValid').checked)
            {
                document.getElementById('dispEspaOrdeIntWifiValor').value = 1;
                total += 1;
            } else
            {
                document.getElementById('dispEspaOrdeIntWifiValor').value = 0;
            }
        } catch (err) {

        }
        if ($("#ejercicio").val() >= 2018) {
            try {
                if (document.getElementById('localPreviamenteAprobadoVAL') != null && document.getElementById('localPreviamenteAprobadoVAL').checked)
                {
                    document.getElementById('localPreviamenteAprobadoValoracion').value = 0;
                    //total += 1;
                } else
                {
                    if (document.getElementById('localPreviamenteAprobadoValoracion') != null)
                        document.getElementById('localPreviamenteAprobadoValoracion').value = 0;
                }
            } catch (err) {
            }
            try {
                if (document.getElementById('mantenimientoRequisitosLPAVAL') != null && document.getElementById('mantenimientoRequisitosLPAVAL').checked)
                {
                    document.getElementById('mantenimientoRequisitosLPAValoracion').value = 0;
                    //total += 1;
                } else
                {
                    if (document.getElementById('mantenimientoRequisitosLPAValoracion') != null)
                        document.getElementById('mantenimientoRequisitosLPAValoracion').value = 0;
                }
            } catch (err) {
            }
        }
    }
    if ($("#idBDConvocatoriaExpediente").val()  > 4) {
        if($("#idBDConvocatoriaExpediente").val()  <30 ){
            try {
                if (document.getElementById('dispPlanIgualdadValid') != null && document.getElementById('dispPlanIgualdadValid').checked)
                {
                    document.getElementById('planIgualdadValoracion').value = 2;
                    total += 2;
                } else
                {
                    document.getElementById('planIgualdadValoracion').value = 0;
                }
            } catch (err) {

            }
            try {
                if (document.getElementById('dispCertCalidadValid') != null && document.getElementById('dispCertCalidadValid').checked)
                {
                    document.getElementById('certCalidadValoracion').value = 2;
                    total += 2;
                } else
                {
                    document.getElementById('certCalidadValoracion').value = 0;
                }
            } catch (err) {

            }
        }else{
            var valoracionCalculada = getPuntuacionPorCompromisoIgualdadGenero();
            total += valoracionCalculada;
            $("#planIgualdadValoracion").val(valoracionCalculada);
            valoracionCalculada = getPuntuacionPorTenerCertificadoCalidad();
            total += valoracionCalculada;
            $("#certCalidadValoracion").val(valoracionCalculada);
        }

    }
    document.getElementById('totalValoracion').value = (total!=null && total!=undefined && total!="" ? total.toFixed(2) : "" );
}
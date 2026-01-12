var paramLLamadaM48ColecDatosValValoUbic={
        tarea:'preparar'
        ,modulo:'MELANBIDE48'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,colecubicacionesct:null
        ,identificadorBDEliminar:null
        ,colecubicctvaloracion:null
};

var colecUbicCTValoracion={
    id:null,
    numeroExpediente:null,
    idFkUbicacion:null,
    validarSegundosLocales:null,
    validarPlanIgualdad:null,
    validarCertificadoCalidad:null,
    validarEspacioComplem:null,
    validarCentroEspEmpleo:null,
    validarEmpresaInsercion:null,
    validarPromoEmpInsercion:null,
    puntuacionTrayectoriaEntidad:null,
    puntuacionUbicacionCT:null,
    puntuacionSegundosLocales:null,
    puntuacionPlanIgualdad:null,
    puntuacionCertificadoCalidad:null,
    puntuacionEspacioComplem:null,
    puntuacionCentroEspEmpleo:null,
    puntuacionEmpOpromEmpInsercion:null,
    puntuacionObservaciones:null
};

var colecUbicacionCTCriterioPuntos={
    criterioPuntosTrayectoria:0.2,
    criterioPuntosSegudosLocales:4,
    criterioPuntosPlanIgualdad:2,
    criterioPuntosCertificadoCalidad:2,
    criterioPuntosEspacioComplementario:1,
    criterioPuntosCentroEspEmpleo:5,
    criterioPuntosEmpOpromEmpInsercion:5,
    zeroFloat:0.0
};

var colecUbicacionCTCriterioMaxPuntos={
    criterioMaxPuntosTrayectoria:12,
    criterioMaxPuntosSegudosLocales:4,
    criterioMaxPuntosPlanIgualdad:2,
    criterioMaxPuntosCertificadoCalidad:2,
    criterioMaxPuntosEspacioComplementario:1,
    criterioMaxPuntosCentroEspEmpleo:5,
    criterioMaxPuntosEmpOpromEmpInsercion:5,
    criterioMaxPuntosCertificadoCalidad:5
};

var listaColecCertCalidadPuntuacion; // = new Array();
var listaColecCompIgualdadPuntuacion; /// = new Array();


$(document).ready(function() {
    // Eventos
    $("#btnGuardarDatosValoracion").click(function () {
        guardarDatosValoracionUbicacionCentTra();
    });
    $('[id^="validar"]input[type=checkbox]').click(function () {
        setPuntuacionApartadoValoracionCalculando();
        // Si salta el evento hay datos que actualizar
        $("#textoDatosNoRegistradosModif").show();
    });
    // Operaciones
    // Convocatorias colec a partir de 2023
    if($("#idBDConvocatoriaExpediente").val()>30){
        getMostrarDatosCertificadosCalidadValidadosEntidad();
        listaColecCertCalidadPuntuacion=getListaColecCertCalidadPuntuacion();
        listaColecCompIgualdadPuntuacion=getListaColecCompIgualdadPuntuacion();
    }
    if($("#idBDColecUbicCTValoracion").val()!==null && $("#idBDColecUbicCTValoracion").val()!==undefined && $("#idBDColecUbicCTValoracion").val()!=="")
        $("#puntuacionTotal").val(getPuntTotalUbicaCalculadaDatosValidados());
    formatearCamposApartadoValoracion();
    OcultarMostTexDatosValoracionNoRegistradosOActualizados();
});



function prepararJsonColecUbicCTValoracion(){
    var  respuesta = $.extend({}, colecUbicCTValoracion);
    respuesta.id=$("#idBDColecUbicCTValoracion").val()!== undefined && $("#idBDColecUbicCTValoracion").val() !== "" ? $("#idBDColecUbicCTValoracion").val() : null;
    respuesta.numeroExpediente=$("#numeroExpediente").val()!== undefined && $("#numeroExpediente").val() !== "" ? $("#numeroExpediente").val() : null;
    respuesta.idFkUbicacion=$("#idBDColecUbicacionesCT").val()!== undefined && $("#idBDColecUbicacionesCT").val() !== "" ? $("#idBDColecUbicacionesCT").val() : null;
    respuesta.validarSegundosLocales=$("#validarSegundosLocales")!== null && $("#validarSegundosLocales")!==undefined && $("#validarSegundosLocales").prop("checked")===true ? 1 : 0;
    respuesta.validarPlanIgualdad=$("#validarPlanIgualdad")!== null && $("#validarPlanIgualdad")!==undefined && $("#validarPlanIgualdad").prop("checked")===true ? 1 : 0;
    respuesta.validarCertificadoCalidad=$("#validarCertificadoCalidad")!== null && $("#validarCertificadoCalidad")!==undefined && $("#validarCertificadoCalidad").prop("checked")===true ? 1 : 0;
    respuesta.validarEspacioComplem=$("#validarEspacioComplem")!== null && $("#validarEspacioComplem")!==undefined && $("#validarEspacioComplem").prop("checked")===true ? 1 : 0;
    respuesta.validarCentroEspEmpleo=$("#validarCentroEspEmpleo")!== null && $("#validarCentroEspEmpleo")!==undefined && $("#validarCentroEspEmpleo").prop("checked")===true ? 1 : 0;
    respuesta.validarEmpresaInsercion=$("#validarEmpresaInsercion")!== null && $("#validarEmpresaInsercion")!==undefined && $("#validarEmpresaInsercion").prop("checked")===true ? 1 : 0;
    respuesta.validarPromoEmpInsercion=$("#validarPromoEmpInsercion")!== null && $("#validarPromoEmpInsercion")!==undefined && $("#validarPromoEmpInsercion").prop("checked")===true ? 1 : 0;
    respuesta.puntuacionTrayectoriaEntidad=$("#puntuacionTrayectoriaEntidad").val()!== undefined && $("#puntuacionTrayectoriaEntidad").val() !== "" ? $("#puntuacionTrayectoriaEntidad").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionUbicacionCT=$("#puntuacionUbicacionCT").val()!== undefined && $("#puntuacionUbicacionCT").val() !== "" ? $("#puntuacionUbicacionCT").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionSegundosLocales=$("#puntuacionSegundosLocales").val()!== undefined && $("#puntuacionSegundosLocales").val() !== "" ? $("#puntuacionSegundosLocales").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionPlanIgualdad=$("#puntuacionPlanIgualdad").val()!== undefined && $("#puntuacionPlanIgualdad").val() !== "" ? $("#puntuacionPlanIgualdad").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionCertificadoCalidad=$("#puntuacionCertificadoCalidad").val()!== undefined && $("#puntuacionCertificadoCalidad").val() !== "" ? $("#puntuacionCertificadoCalidad").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionEspacioComplem=$("#puntuacionEspacioComplem").val()!== undefined && $("#puntuacionEspacioComplem").val() !== "" ? $("#puntuacionEspacioComplem").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionCentroEspEmpleo=$("#puntuacionCentroEspEmpleo").val()!== undefined && $("#puntuacionCentroEspEmpleo").val() !== "" ? $("#puntuacionCentroEspEmpleo").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionEmpOpromEmpInsercion=$("#puntuacionEmpOpromEmpInsercion").val()!== undefined && $("#puntuacionEmpOpromEmpInsercion").val() !== "" ? $("#puntuacionEmpOpromEmpInsercion").val().replace(/\./gi,'').replace(/\,/gi,'.') : null;
    respuesta.puntuacionObservaciones=$("#puntuacionObservaciones").val()!== undefined && $("#puntuacionObservaciones").val() !== "" ? $("#puntuacionObservaciones").val() : null;
    return respuesta;
}

function guardarDatosValoracionUbicacionCentTra(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    var tipoOperacion = $("#idBDColecUbicCTValoracion").val() !== null && $("#idBDColecUbicCTValoracion").val() !== undefined && $("#idBDColecUbicCTValoracion").val() !== "" ? "1" : "0";
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.tipoOperacion = tipoOperacion;
    dataParameter.operacion = 'guardarDatosValoracionUbicacionCentTra';
    dataParameter.codigoConvocatoriaExpediente = $("#codigoConvocatoriaExpediente").val();
    dataParameter.colecubicctvaloracion = JSON.stringify(prepararJsonColecUbicCTValoracion());
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null && "OK"===respuesta) {
                // De momento no retornamos la lista de ubicaciones - No se visualiza Total Valoracion en la tabla
                //respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                //    return value === null ? "" : value;
                //}));
                self.parent.opener.retornoXanelaAuxiliar(null);// respuesta
                jsp_alerta("A", "4"===$("#idiomaUsuario").val()?"Zuzen gordetako datuak.":"Datos Guardados Correctamente.");
            } else {
                jsp_alerta("A", "4"===$("#idiomaUsuario").val()?"Datuak ezin izan dira eguneratu, erroreak jarraitzen badu, kontsultatu euskarriarekin.":"Los datos no se ha podido actualizar, si el error persiste consulte con soporte.");
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al guardar los datos. Datos no actualizados. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function getPuntTotalUbicaCalculadaDatosValidados(){
    var resultado = 0.0;
    var puntosUbicacionMunValidados = $("#puntuacionUbicacionMunValoracion").val()!==null && $("#puntuacionUbicacionMunValoracion").val()!== undefined && $("#mesesTrayectoriaEntidadValoracion").val()!==""? parseFloat($("#puntuacionUbicacionMunValoracion").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0;
    var puntosTrayectoria = getPuntuacionPorTrayectoriaValidada();
    resultado+=puntosUbicacionMunValidados;
    resultado+=puntosTrayectoria;
    if($("#validarSegundosLocales").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosSegudosLocales;
    if($("#validarPlanIgualdad").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosPlanIgualdad;
    if($("#validarCertificadoCalidad").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosCertificadoCalidad;
    if($("#validarEspacioComplem").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosEspacioComplementario;
    if($("#validarCentroEspEmpleo").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosCentroEspEmpleo;
    if($("#validarEmpresaInsercion").prop("checked")  || $("#validarPromoEmpInsercion").prop("checked"))
        resultado+=colecUbicacionCTCriterioPuntos.criterioPuntosEmpOpromEmpInsercion;
    if($("#validarCompromisoIgualdadGenero") != undefined && $("#validarCompromisoIgualdadGenero").prop("checked")){
            resultado+=getPuntuacionPorCompromisoIgualdadGenero();
    }
    if($('input[id^="validarCertificadoCalidad_"]:checked') != undefined && $('input[id^="validarCertificadoCalidad_"]:checked').length >0){
        resultado+=getPuntuacionPorTenerCertificadoCalidad();
    }

    return Number(resultado.toFixed(2));
}
/**
 * Calcula la puntuacion total pero solo sumando lo que hay en los campos de la seccion Valoracion. Si existen datos previos Para detectar si al entrar y se ha cambiado algun
 * @returns number Total de valoracion calulandolo segun la suma de los input del apartado valoracion
 */
function getPuntuacionSumandoCamposCalculadosApartadoValoracion(){
    var resultado=0.0;
    resultado+= ($("#puntuacionTrayectoriaEntidad").val()!==null && $("#puntuacionTrayectoriaEntidad").val()!==undefined  && $("#puntuacionTrayectoriaEntidad").val()!==""  ?  parseFloat($("#puntuacionTrayectoriaEntidad").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionUbicacionCT").val()!==null && $("#puntuacionUbicacionCT").val()!==undefined  && $("#puntuacionUbicacionCT").val()!==""  ?  parseFloat($("#puntuacionUbicacionCT").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionSegundosLocales").val()!==null && $("#puntuacionSegundosLocales").val()!==undefined  && $("#puntuacionSegundosLocales").val()!==""   ?  parseFloat($("#puntuacionSegundosLocales").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionPlanIgualdad").val()!==null && $("#puntuacionPlanIgualdad").val()!==undefined  && $("#puntuacionPlanIgualdad").val()!==""  ?  parseFloat($("#puntuacionPlanIgualdad").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionCertificadoCalidad").val()!==null && $("#puntuacionCertificadoCalidad").val()!==undefined  && $("#puntuacionCertificadoCalidad").val()!==""  ?  parseFloat($("#puntuacionCertificadoCalidad").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionEspacioComplem").val()!==null && $("#puntuacionEspacioComplem").val()!==undefined  && $("#puntuacionEspacioComplem").val()!==""  ?  parseFloat($("#puntuacionEspacioComplem").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionCentroEspEmpleo").val()!==null && $("#puntuacionCentroEspEmpleo").val()!==undefined  && $("#puntuacionCentroEspEmpleo").val()!==""  ?  parseFloat($("#puntuacionCentroEspEmpleo").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    resultado+= ($("#puntuacionEmpOpromEmpInsercion").val()!==null && $("#puntuacionEmpOpromEmpInsercion").val()!==undefined  && $("#puntuacionEmpOpromEmpInsercion").val()!=="" ?  parseFloat($("#puntuacionEmpOpromEmpInsercion").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0) ;
    return Number(resultado.toFixed(2));
}

function getPuntTotalUbicaCalculadaDatosSolicitud(){
    var resultado = 0.0;
    // Trayectoria y Putos ubicacion se validan fuera. usamos esos valores.
    var puntosUbicacionMunValidados = $("#puntuacionUbicacionMunValoracion").val() !== null && $("#puntuacionUbicacionMunValoracion").val() !== undefined && $("#mesesTrayectoriaEntidadValoracion").val() !== "" ? parseFloat($("#puntuacionUbicacionMunValoracion").val().replace(/\./gi, '').replace(/\,/gi, '.')) : 0.0;
    var puntosTrayectoria = getPuntuacionPorTrayectoriaValidada();
    resultado += puntosUbicacionMunValidados;
    resultado += puntosTrayectoria;
    if ($("#segundosLocalesMismoAmbito").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosSegudosLocales;
    if ($("#planIgualdad").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosPlanIgualdad;
    if ($("#certificadoCalidad").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosCertificadoCalidad;
    if($("#validarCompromisoIgualdadGenero") != undefined && $("#validarCompromisoIgualdadGenero").prop("checked")){
        resultado += getPuntuacionPorCompromisoIgualdadGenero();
    }
    if($('input[id^="validarCertificadoCalidad_"]:checked') != undefined && $('input[id^="validarCertificadoCalidad_"]:checked').length >0){
        resultado += getPuntuacionPorTenerCertificadoCalidad();
    }
    if ($("#espacioComplementarioWifi").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosEspacioComplementario;
    if ($("#centroEmpleoColectivo3").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosCentroEspEmpleo;
    if ($("#empresaInsercionColectivo4").prop("checked") || $("#empresaPromoInsercionColectivo4").prop("checked"))
        resultado += colecUbicacionCTCriterioPuntos.criterioPuntosEmpOpromEmpInsercion;
    return Number(resultado.toFixed(2));
}

function setPuntuacionApartadoValoracionCalculando(){
    var puntosUbicacionMunValidados = $("#puntuacionUbicacionMunValoracion").val()!==null && $("#puntuacionUbicacionMunValoracion").val()!== undefined && $("#mesesTrayectoriaEntidadValoracion").val()!==""? parseFloat($("#puntuacionUbicacionMunValoracion").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0;
    var puntosTrayectoria = getPuntuacionPorTrayectoriaValidada();
    $("#puntuacionTrayectoriaEntidad").val(formatNumberLocal_esES(puntosTrayectoria));
    $("#puntuacionUbicacionCT").val(formatNumberLocal_esES(puntosUbicacionMunValidados));

    if($("#validarSegundosLocales").prop("checked"))
        $("#puntuacionSegundosLocales").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosSegudosLocales));
    else
        $("#puntuacionSegundosLocales").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarPlanIgualdad").prop("checked"))
        $("#puntuacionPlanIgualdad").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosPlanIgualdad));
    else
        $("#puntuacionPlanIgualdad").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarCertificadoCalidad").prop("checked"))
        $("#puntuacionCertificadoCalidad").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosCertificadoCalidad));
    else
        $("#puntuacionCertificadoCalidad").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarEspacioComplem").prop("checked"))
        $("#puntuacionEspacioComplem").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosEspacioComplementario));
    else
        $("#puntuacionEspacioComplem").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarCentroEspEmpleo").prop("checked"))
        $("#puntuacionCentroEspEmpleo").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosCentroEspEmpleo));
    else
        $("#puntuacionCentroEspEmpleo").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarEmpresaInsercion").prop("checked")  || $("#validarPromoEmpInsercion").prop("checked"))
        $("#puntuacionEmpOpromEmpInsercion").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.criterioPuntosEmpOpromEmpInsercion));
    else
        $("#puntuacionEmpOpromEmpInsercion").val(formatNumberLocal_esES(colecUbicacionCTCriterioPuntos.zeroFloat));

    if($("#validarCompromisoIgualdadGenero") != undefined && $("#validarCompromisoIgualdadGenero").length > 0 ){
        $("#puntuacionPlanIgualdad").val(formatNumberLocal_esES(getPuntuacionPorCompromisoIgualdadGenero()));
    }

    if($('input[id^="validarCertificadoCalidad_"]') != undefined && $('input[id^="validarCertificadoCalidad_"]').length >0){
        $("#puntuacionCertificadoCalidad").val(formatNumberLocal_esES(getPuntuacionPorTenerCertificadoCalidad()));
    }

    $("#puntuacionTotal").val(formatNumberLocal_esES(getPuntTotalUbicaCalculadaDatosValidados()));
}

function OcultarMostTexDatosValoracionNoRegistradosOActualizados(){
    // NO hay Datos Guardados
    if($("#idBDColecUbicCTValoracion").val()===null || $("#idBDColecUbicCTValoracion").val()===undefined
            || $("#idBDColecUbicCTValoracion").val()===""){
        $("#textoDatosNoRegistradosModif").show();
        setPuntuacionApartadoValoracionCalculando();
    }else
    // NO coincide la suma de los campos puntuacion con el calculo de puntuacion total
    if(getPuntuacionSumandoCamposCalculadosApartadoValoracion()!==getPuntTotalUbicaCalculadaDatosValidados()){
        $("#divDatosNoActualizados").show();
    }
}

function refrescarCalculosApartadoValoracion(){
    setPuntuacionApartadoValoracionCalculando();
    $("#divDatosNoActualizados").hide();
    $("#textoDatosNoRegistradosModif").show();
}

function formatearCamposApartadoValoracion(){
    $("#puntuacionUbicacionCT").val(formatNumberLocal_esES(parseFloat($("#puntuacionUbicacionCT").val())));
    $("#puntuacionTrayectoriaEntidad").val(formatNumberLocal_esES(parseFloat($("#puntuacionTrayectoriaEntidad").val())));
    $("#puntuacionSegundosLocales").val(formatNumberLocal_esES(parseFloat($("#puntuacionSegundosLocales").val())));
    $("#puntuacionPlanIgualdad").val(formatNumberLocal_esES(parseFloat($("#puntuacionPlanIgualdad").val())));
    $("#puntuacionCertificadoCalidad").val(formatNumberLocal_esES(parseFloat($("#puntuacionCertificadoCalidad").val())));
    $("#puntuacionEspacioComplem").val(formatNumberLocal_esES(parseFloat($("#puntuacionEspacioComplem").val())));
    $("#puntuacionCentroEspEmpleo").val(formatNumberLocal_esES(parseFloat($("#puntuacionCentroEspEmpleo").val())));
    $("#puntuacionEmpOpromEmpInsercion").val(formatNumberLocal_esES(parseFloat($("#puntuacionEmpOpromEmpInsercion").val())));
    $("#puntuacionTotal").val(formatNumberLocal_esES(parseFloat($("#puntuacionTotal").val())));
}


function getMostrarDatosCertificadosCalidadValidadosEntidad(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecDatosValValoUbic);
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

function getListaColecCertCalidadPuntuacion(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaColecCertCalidadPuntuacion';
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    var respuestaFinal = new Array();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
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

function getListaColecCompIgualdadPuntuacion(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecDatosValValoUbic);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaColecCompIgualdadPuntuacion';
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    var respuestaFinal = new Array();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
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
            if(listaColecCompIgualdadPuntuacion != undefined){
                $.each(listaColecCompIgualdadPuntuacion, function (index, item) {
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
    var puntuacionCertificadosCalidadCal = colecUbicacionCTCriterioPuntos.zeroFloat;
    if($('input[id^="validarCertificadoCalidad_"]') != undefined && $('input[id^="validarCertificadoCalidad_"]').length >0){
        $('input[id^="validarCertificadoCalidad_"]:checked').each(function (index, item) {
            if(listaColecCertCalidadPuntuacion != undefined){
                $.each(listaColecCertCalidadPuntuacion, function (index2, item2) {
                    if(item2.codigo==$(item).prop("id").split("_").pop()){
                       puntuacionCertificadosCalidadCal = ((puntuacionCertificadosCalidadCal+item2.puntuacion) >= colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosCertificadoCalidad ? colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosCertificadoCalidad : puntuacionCertificadosCalidadCal+item2.puntuacion);
                    }
                    if(puntuacionCertificadosCalidadCal==colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosCertificadoCalidad)
                        return true;
                });
            }
            if(puntuacionCertificadosCalidadCal==colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosCertificadoCalidad)
                return true;
        });
    }
    return puntuacionCertificadosCalidadCal;
}

function getPuntuacionPorTrayectoriaValidada(){
    var mesesValidados = $("#mesesTrayectoriaEntidadValoracion").val()!==null && $("#mesesTrayectoriaEntidadValoracion").val()!== undefined && $("#mesesTrayectoriaEntidadValoracion").val()!==""? parseFloat($("#mesesTrayectoriaEntidadValoracion").val().replace(/\./gi,'').replace(/\,/gi,'.')) : 0.0;
    var puntosTrayectoria = Number((mesesValidados * colecUbicacionCTCriterioPuntos.criterioPuntosTrayectoria).toFixed(2));
    return (puntosTrayectoria > colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosTrayectoria ? colecUbicacionCTCriterioMaxPuntos.criterioMaxPuntosTrayectoria : puntosTrayectoria);
}
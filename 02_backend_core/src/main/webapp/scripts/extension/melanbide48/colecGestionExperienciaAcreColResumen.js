
var paramLLamadaM48ColecTrayectoriaEntidad={
        tarea:'preparar'
        ,modulo:'MELANBIDE48'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,colecTrayectoriaEntidad:null
        ,identificadorBDGestionar:null
        ,colecTrayectoriaEntidadLista:null
        ,colecTrayeEntiValidaLista:null
    };
    
var ColecTrayectoriaEntidadJSON={
    id:null,
    trayCodColectivo:null,
    trayIdFkProgConvActGrupo:null,
    trayIdFkProgConvActSubGrPre:null,
    trayNumExpediente:null,
    trayCodigoEntidad:null,
    trayDescripcion:null,
    trayTieneExperiencia:null,
    trayNombreAdmonPublica:null,
    trayFechaInicio:null,
    trayFechaFin:null,
    trayNumeroMeses:null,
    trayFechaAlta:null,
    trayFechaModificacion:null
};

var ColecTrayeEntiValidaJSON={
    id:null,
    numeroExpediente:null,
    idFkEntidad:null,
    idFkColectivo:null,
    numeroMesesValidados:null
};

var ColecConvocatoriasFecLimiTrayecValidada={
    fechaLimiteInicio:new Date(1000,0,1),
    fechaLimiteFin:new Date(3000,11,31)
};

// Definicion de Variables para manejo de Tablas
var tabExperienciaAcreditableResumen;
var listaExperienciaAcreditableResumen = new Array();
var listaExperienciaAcreditableResumenTabla = new Array();

  
$(document).ready(function() {
    getDatosExperienciaAcreditableSinSolapamiento();
});

function getDatosExperienciaAcreditableSinSolapamiento(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getTodaTrayectoriaAcreditableExpedienteNoSolapable';
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                crearEstructuraTablaExperienciaAcreditableResumen(respuesta);
                getNumeroTotalMesesValidadosExpediente();
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al guardar los datos.' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function getNumeroTotalMesesValidadosExpediente(){
    var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getNumeroTotalMesesValidadosExpediente';
    dataParameter.idBDEntidad = $("#idBDEntidad").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                for (var i = 0; i < respuesta.length; i++) {
                    var elementoJSON = respuesta[i];
                    if(elementoJSON.idFkColectivo===1){
                        $("#numeroTotalMesesValidadosC1").val(formatNumberLocal_esES(elementoJSON.numeroMesesValidados));
                        $("#numeroTotalMesesValidadosC1").data("idbd",elementoJSON.id);
                    }
                    if(elementoJSON.idFkColectivo===2){
                        $("#numeroTotalMesesValidadosC2").val(formatNumberLocal_esES(elementoJSON.numeroMesesValidados));
                        $("#numeroTotalMesesValidadosC2").data("idbd", elementoJSON.id);
                    }
                    if(elementoJSON.idFkColectivo===3){
                        $("#numeroTotalMesesValidadosC3").val(formatNumberLocal_esES(elementoJSON.numeroMesesValidados));
                        $("#numeroTotalMesesValidadosC3").data("idbd", elementoJSON.id);
                    }
                    if(elementoJSON.idFkColectivo===4){
                        $("#numeroTotalMesesValidadosC4").val(formatNumberLocal_esES(elementoJSON.numeroMesesValidados));
                        $("#numeroTotalMesesValidadosC4").data("idbd", elementoJSON.id);
                    }
                }
                if(respuesta.length>0){
                    $("#textoMensajeMesesValidadosNoGuardados").hide();
                }else{
                    $("#textoMensajeMesesValidadosNoGuardados").show();
                }
            }else{
                $("#textoMensajeMesesValidadosNoGuardados").show();
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error leer numero total meses validados .' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function guardarValidarTotalMesesResumen(){
    var idioma = $("#idiomaUsuario").val();
    if($("#numeroTotalMesesValidados").val() === "" && jsp_alerta("C",idioma==4?"Bete gabeko hilabete baliozkotuak guztira, jarraitu nahi duzu?":"Total meses validados no cumplimentado, desea continuar?")
            || $("#numeroTotalMesesValidados").val() !== ""){
        var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'guardarValidarTotalMesesResumen';
        dataParameter.colecTrayeEntiValidaLista = JSON.stringify(prepararGJSONGuardarValidarTotalMesesResumen());
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if ("OK" !== respuesta) {
                    jsp_alerta("A", (idioma == 4 ? "Errore bat gertatu da eskaeran. Datuak ez dira gorde" : "Ha ocurrido un error en la petici�n. Los datos no se han guardado"));
                } else {
                    // Actualizar el ID BD de cada datos guardado
                    getNumeroTotalMesesValidadosExpediente();
                    jsp_alerta("A", (idioma == 4 ? "Datuak ondo gorde dira" : "Los datos se han guardado correctamente"));
//                    if ($("#numeroTotalMesesValidadosC1").val() !== "" && $("#numeroTotalMesesValidadosC2").val() !== "" 
//                        && $("#numeroTotalMesesValidadosC3").val() !== "" && $("#numeroTotalMesesValidadosC3").val() !== "") {
//                        $("#textoMensajeMesesValidadosNoGuardados").hide();
//                    } else {
//                        $("#textoMensajeMesesValidadosNoGuardados").show();
//                    }
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al guardar los datos.' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }    
}

// Preparacion de Tablas
function crearEstructuraTablaExperienciaAcreditableResumen(respuesta){
    listaExperienciaAcreditableResumen = new Array();
    listaExperienciaAcreditableResumenTabla = new Array();
    var numeroMesesTotalC1=0.0;
    var numeroMesesTotalC2=0.0;
    var numeroMesesTotalC3=0.0;
    var numeroMesesTotalC4=0.0;
    var numeroMesesTotalValidarC1=0.0;
    var numeroMesesTotalValidarC2=0.0;
    var numeroMesesTotalValidarC3=0.0;
    var numeroMesesTotalValidarC4=0.0;
    tabExperienciaAcreditableResumen = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaExperienciaAcreditableResumen")[0], 950, 10);
    tabExperienciaAcreditableResumen.addColumna('200', 'left', $("#label_tabla_columna_colectivo").val());
    tabExperienciaAcreditableResumen.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabExperienciaAcreditableResumen.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabExperienciaAcreditableResumen.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());
    tabExperienciaAcreditableResumen.addColumna('100', 'center', $("#lable_meses").val());
    tabExperienciaAcreditableResumen.addColumna('100', 'center', $("#label_meses_validar").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            var nroMesesSolicitados = calcularMesesEntreFechas(getDateFromStringddMMyyyy(elementoJSON.trayFechaInicio),getDateFromStringddMMyyyy(elementoJSON.trayFechaFin));
            var nroMesesAValidar = calcularMesesValidadosEntreFechasxConvocatoria(getDateFromStringddMMyyyy(elementoJSON.trayFechaInicio),getDateFromStringddMMyyyy(elementoJSON.trayFechaFin));
            // Si es negativo, esta fuera del rango de la convocatoria debe ser 0
            nroMesesAValidar=nroMesesAValidar<0?0:nroMesesAValidar;
            listaExperienciaAcreditableResumen[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,nroMesesSolicitados,nroMesesAValidar];
            if(elementoJSON.trayCodColectivo===1){
                numeroMesesTotalC1+=nroMesesSolicitados;//elementoJSON.trayNumeroMeses;
                numeroMesesTotalValidarC1+=nroMesesAValidar;
            }else if (elementoJSON.trayCodColectivo===2){
                numeroMesesTotalC2+=nroMesesSolicitados;//elementoJSON.trayNumeroMeses;
                numeroMesesTotalValidarC2+=nroMesesAValidar;
            }else if (elementoJSON.trayCodColectivo===3){
                numeroMesesTotalC3+=nroMesesSolicitados;//elementoJSON.trayNumeroMeses;
                numeroMesesTotalValidarC3+=nroMesesAValidar;
            }else if (elementoJSON.trayCodColectivo===4){
                numeroMesesTotalC4+=nroMesesSolicitados;//elementoJSON.trayNumeroMeses;
                numeroMesesTotalValidarC4+=nroMesesAValidar;
            }
            var textoHtmlInfoIni = "";
            var fechaLimiteValidarTrayectoria = "CONV_2023-2025"===$("#codigoConvocatoriaExpediente").val() ? "31/03/2023" : "31/03/2021" ;
            var fechaLimiteValidarTrayectoriaEuskera = "CONV_2023-2025"===$("#codigoConvocatoriaExpediente").val() ? "2023/03/31" : "2021/03/31" ;
            if(getDateFromStringddMMyyyy(elementoJSON.trayFechaInicio)<getDateFromStringddMMyyyy("01/01/2016")){
                var title_text=($("#idiomaUsuario").val()==4?"Baliozkotu beharreko hilabeteak 2016/01/01etik aurrera":"Meses a validar solo desde 01/01/2016");
                textoHtmlInfoIni='<i class="fa fa-info-circle" aria-hidden="true" title="'+title_text+'"></i>';
            }else if (getDateFromStringddMMyyyy(elementoJSON.trayFechaInicio) > getDateFromStringddMMyyyy(fechaLimiteValidarTrayectoria)) {
                var title_text = ($("#idiomaUsuario").val() == 4 ? fechaLimiteValidarTrayectoriaEuskera + "ra arte baliozkotu beharreko hilabeteak":"Meses a validar solo hasta " + fechaLimiteValidarTrayectoria);
                textoHtmlInfoIni = '<i class="fa fa-info-circle" aria-hidden="true" title="' + title_text + '"></i>';
            } 
            var textoHtmlInfoFin = "";
            if(getDateFromStringddMMyyyy(elementoJSON.trayFechaFin)>getDateFromStringddMMyyyy(fechaLimiteValidarTrayectoria)){
                var title_text=($("#idiomaUsuario").val()==4? fechaLimiteValidarTrayectoriaEuskera + "ra arte baliozkotu beharreko hilabeteak":"Meses a validar solo hasta " + fechaLimiteValidarTrayectoria);
                textoHtmlInfoFin='<i class="fa fa-info-circle" aria-hidden="true" title="'+title_text+'"></i>';
            } 
            listaExperienciaAcreditableResumenTabla[i] = [elementoJSON.trayCodColectivo + " " + elementoJSON.colectivoDescripcion,elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio + (textoHtmlInfoIni!==""? " " + textoHtmlInfoIni:""), elementoJSON.trayFechaFin + (textoHtmlInfoFin!==""? " " + textoHtmlInfoFin:""),formatNumberLocal_esES(Number(nroMesesSolicitados.toFixed(2))),formatNumberLocal_esES(Number(nroMesesAValidar.toFixed(2)))];
        }
        $("#numeroTotalMesesSolicitudC1").val(formatNumberLocal_esES(Number(numeroMesesTotalC1.toFixed(2))));
        $("#numeroTotalMesesSolicitudC2").val(formatNumberLocal_esES(Number(numeroMesesTotalC2.toFixed(2))));
        $("#numeroTotalMesesSolicitudC3").val(formatNumberLocal_esES(Number(numeroMesesTotalC3.toFixed(2))));
        $("#numeroTotalMesesSolicitudC4").val(formatNumberLocal_esES(Number(numeroMesesTotalC4.toFixed(2))));
        $("#numeroTotalMesesValidados_CalculadosC1").val(formatNumberLocal_esES(Number(numeroMesesTotalValidarC1.toFixed(2))));
        $("#numeroTotalMesesValidados_CalculadosC2").val(formatNumberLocal_esES(Number(numeroMesesTotalValidarC2.toFixed(2))));
        $("#numeroTotalMesesValidados_CalculadosC3").val(formatNumberLocal_esES(Number(numeroMesesTotalValidarC3.toFixed(2))));
        $("#numeroTotalMesesValidados_CalculadosC4").val(formatNumberLocal_esES(Number(numeroMesesTotalValidarC4.toFixed(2))));
    }
    tabExperienciaAcreditableResumen.displayCabecera = true;
    tabExperienciaAcreditableResumen.height = 250;
    tabExperienciaAcreditableResumen.lineas = listaExperienciaAcreditableResumenTabla;
    tabExperienciaAcreditableResumen.displayTabla();
}

function limpiarFormularioColecTrayectoriaEntidad(){
    $("#numeroTotalMesesValidados").val("");
}

/**
 * Calcula el numero de meses entre dos fechas. Recibe dos parametros Date. Usa la libreria moment-with-locales.js de https://momentjs.com
 * @param {Date} fechaDesde
 * @param {Date} fechaHasta
 * @returns {unresolved}
 */
function calcularMesesEntreFechas(fechaDesde,fechaHasta){
    if(fechaDesde!==null && fechaDesde!== undefined && fechaDesde instanceof Date
            && fechaHasta!==null && fechaHasta!== undefined && fechaHasta instanceof Date){
        var fromD = moment(fechaDesde);
        fechaHasta.setDate(fechaHasta.getDate()+1); // Sumamos uno para inlcuir el intervalo final.
        var toD = moment(fechaHasta);
        return toD.diff(fromD, 'months',true);
    }
    return null;
}

/**
 * Evalua si una fecha es mayor o igual que otra. Recibe dos parametros Date. Usa la libreria moment-with-locales.js de https://momentjs.com
 * @param {Date} fechaDesde
 * @param {Date} fechaHasta
 * @returns {unresolved}
 */
function isfechaHastaMayorfechaDesde(fechaDesde,fechaHasta){
    if(fechaDesde!==null && fechaDesde!== undefined && fechaDesde instanceof Date
            && fechaHasta!==null && fechaHasta!== undefined && fechaHasta instanceof Date){
        var fromD = moment(fechaDesde);
        var toD = moment(fechaHasta);
        return toD.diff(fromD, 'days')>=0;
    }
    return null;
}

function getDateFromStringddMMyyyy(fechaStringddMMyyyy){
    if(fechaStringddMMyyyy!==null && fechaStringddMMyyyy !== undefined && fechaStringddMMyyyy!==""){
        var arreglo = fechaStringddMMyyyy.toString().split("/");
        if(arreglo.length===3){
            return new Date(arreglo[2],(arreglo[1]-1),arreglo[0]);
        }
    }
    return null;
}

function recargarDatosPestanaExpAcreditableResumen(){
    getDatosExperienciaAcreditableSinSolapamiento();
    getNumeroTotalMesesValidadosExpediente();
}

/**
 * 
 * @param {type} fechaDesde
 * @param {type} fechaHasta
 * @returns {unresolved}
 */
function  calcularMesesValidadosEntreFechasxConvocatoria(fechaDesde,fechaHasta){
    var dataValidatorFechas = $.extend({}, ColecConvocatoriasFecLimiTrayecValidada);
    if("CONV_2021-2023"===$("#codigoConvocatoriaExpediente").val()){
        dataValidatorFechas.fechaLimiteInicio= new Date(2016,0,1);
        dataValidatorFechas.fechaLimiteFin= new Date(2021,2,31);
    }
    if (fechaDesde !== null && fechaDesde !== undefined && fechaDesde instanceof Date
            && fechaHasta !== null && fechaHasta !== undefined && fechaHasta instanceof Date) {
        if(isfechaHastaMayorfechaDesde(fechaDesde,dataValidatorFechas.fechaLimiteInicio)){
            fechaDesde=dataValidatorFechas.fechaLimiteInicio;
        }
        if(isfechaHastaMayorfechaDesde(dataValidatorFechas.fechaLimiteFin,fechaHasta)){
            fechaHasta=dataValidatorFechas.fechaLimiteFin;
        }
        var fromD = moment(fechaDesde);
        fechaHasta.setDate(fechaHasta.getDate() + 1); // Sumamos uno para inlcuir el intervalo final.
        var toD = moment(fechaHasta);
        return toD.diff(fromD, 'months', true);
    }
    return null;
}

function prepararGJSONGuardarValidarTotalMesesResumen() {
    var listaRetorno = new Array();
    $('[id^="numeroTotalMesesValidadosC"]').each(function (index, element) {
        var dataParameter = $.extend({}, ColecTrayeEntiValidaJSON);
        dataParameter.id = $(element).data("idbd");
        dataParameter.numeroExpediente = $("#numeroExpediente").val();
        dataParameter.idFkEntidad = $("#idBDEntidad").val();  // La entidad general
        var elementID = $(element).attr("id");
        elementID = Number(elementID.replace('numeroTotalMesesValidadosC',''));
        dataParameter.idFkColectivo = elementID;
        var valorMeses = $(element).val();
        valorMeses = (valorMeses!==null && valorMeses!==undefined  && valorMeses!==""  ?  parseFloat(valorMeses.replace(/\./gi,'').replace(/\,/gi,'.')) : null) ;
        dataParameter.numeroMesesValidados = valorMeses;
        listaRetorno.push(dataParameter);
    });
    return listaRetorno;
}


function copiarNumeroMesesValidarToValidados(){
    $("#numeroTotalMesesValidadosC1").val($("#numeroTotalMesesValidados_CalculadosC1").val());
    $("#numeroTotalMesesValidadosC2").val($("#numeroTotalMesesValidados_CalculadosC2").val());
    $("#numeroTotalMesesValidadosC3").val($("#numeroTotalMesesValidados_CalculadosC3").val());
    $("#numeroTotalMesesValidadosC4").val($("#numeroTotalMesesValidados_CalculadosC4").val());
}
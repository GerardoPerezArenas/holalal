var urlBaseLlamadaM32_CEMP = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
var parametrosLLamadaM32_CEMP={
    tarea:'preparar'
    ,modulo:'MELANBIDE32'
    ,operacion:null
    ,numero:null
    ,tipo:0
    ,control:  null //new Date().getTime()
    ,tipoOperacion:null
};

$(document).ready(function () {
    gestionarVisibilidadHtmlxConvActiExpte();
});


function gestionarVisibilidadHtmlxConvActiExpte(){
    var convocatoriaActivaExpediente = getConvocatoriaActivaExpediente();
    if(convocatoriaActivaExpediente!= undefined && convocatoriaActivaExpediente!=null){
        if("CONV_2023-2025"===convocatoriaActivaExpediente){
            // Ocultar Lista Trayectoria
            $("#divListaTrayectoriaCE").hide();
            // Mostrar acceso a Criterios
            $("#btnAbriModalCriteriosEvaluacion").show();
            // Radios Datos entidad Pestana Otros Datos
            $("#liPregunta33").hide();
            $("#liPregunta34").hide();
            $("#liPregunta35").hide();
            $("#liPregunta4").hide();
            $("#liPregunta7").hide();
            // Texto Horario
            $("#divHorarioAtencionCE").hide();
        }else{
            $("#divListaTrayectoriaCE").show();
            $("#btnAbriModalCriteriosEvaluacion").hide();
            $("#liPregunta33").show();
            $("#liPregunta34").show();
            $("#liPregunta35").show();
            $("#liPregunta4").show();
            $("#liPregunta7").show();
            $("#divHorarioAtencionCE").show();
        }
    }else{
        console.log("Datos de convocatoria asociada al expediente no recuperada");
    }
}

function getConvocatoriaActivaExpediente(){
    // Se tendra que usar los metodos que recuperan los datos de MELANBIDE_CONVOCATORIAS
    // Metiendo una linea alli, e ir adaptando para que se validen datos por convocaoria no por anio, incluso la carga de Ambitos
    // De momento validamos por anio
    var respuesta = "";
     if($("#ejercicioExpediente").val()>=2023){
        respuesta="CONV_2023-2025";
    }
    return respuesta;
}
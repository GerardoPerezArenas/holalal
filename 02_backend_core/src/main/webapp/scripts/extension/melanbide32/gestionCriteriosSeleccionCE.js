var urlBaseLlamadaM32 = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
var parametrosLLamadaM32={
    tarea:'preparar'
    ,modulo:'MELANBIDE32'
    ,operacion:null
    ,numero:null
    ,tipo:0
    ,control:  null //new Date().getTime()
    ,tipoOperacion:null
};
// Criterios Seleccionados
var listaCritEvaluacionSeleccionadosCE = new Array();
// Criterios Validados
var listaCritEvaluacionSeleccionadosCEVal = new Array();

$(document).ready(function () {
    asignarEventos();
    //$("#modalCriteriosSeleccionCentroEmpleo").modal('show');
});

function asignarEventos(){
    // Cierre Modal
    $('#modalCriteriosSeleccionCentroEmpleo').on('hidden.bs.modal', function () {
       limpiarDatosModalCriteriosSeleccionCentroEmpleo();
    });
}

function limpiarDatosModalCriteriosSeleccionCentroEmpleo(){
    $("#tablaCriterios").empty();
}

function cargarModalCriteriosEvaluacion() {
    if($("#codAmbito")!==null && $("#codAmbito").val()!==null && $("#codAmbito").val()!== undefined && $("#codAmbito").val()!=''){
        getListaCriteriosEvaluacion();
        $("#modalCriteriosSeleccionCentroEmpleo").modal('show');
        // Al entrar al modal indicamos que si guardan los datos del centro (en caso de modificar) se actualicen los datos. 
        $("#actualizarDatosCriterios").val("1");
        getCriteriosEvaluacionSeleccionadoCE();
    }else{
        jsp_alerta("A",$("#textoSeleccionaAmbito").val());
    }
}

function getListaCriteriosEvaluacion(){
    var dataParameter = $.extend({}, parametrosLLamadaM32);
    dataParameter.control = new Date().getTime();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.operacion = 'getListaCriteriosEvaluacionByCodigo';
    dataParameter.codigoCriterio = "2"; //JSON.stringify("2");
    var codigoCriteriosComunes = ["5","6","7","8","9"];
    var codigoCriteriosCEDistribuidos = ["1","2","3","4"];
    var codigoCriteriosCERestoAmb = ["2","3","4"];
    var codigoCriteriosCEEspeciales = ["10","11"];
    var isCEEspecial = getIsAmbitoCEspecial($("#codAmbito").val());
    var isCEDistribuido = getIsAmbitoCDistribuido($("#codAmbito").val());
    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaM32,
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(respuesta);
                var respuestaFiltrada = respuesta.filter(function (criterioS) {
                    if(isCEEspecial){
                        return $.inArray(criterioS.codigoOrden, codigoCriteriosCEEspeciales) >= 0 
                                || $.inArray(criterioS.codigoOrden, codigoCriteriosComunes) >= 0;
                    }else if(isCEDistribuido){
                        return $.inArray(criterioS.codigoOrden, codigoCriteriosCEDistribuidos) >= 0 
                                || $.inArray(criterioS.codigoOrden, codigoCriteriosComunes) >= 0;
                    }else{
                        return $.inArray(criterioS.codigoOrden, codigoCriteriosCERestoAmb) >= 0 
                                || $.inArray(criterioS.codigoOrden, codigoCriteriosComunes) >= 0;
                    }
                }); 
                // Boton copiar Datos seleccionados a Validados
                var cardCopiarDatos = $("<div>",{
                        class:"card mb-3"
                        ,style:"max-width:715px;"
                }).append(($("<div>",{
                        class:"card-header"
                        ,style:"text-align: right;"
                })).append($("<input>",{
                        class:"btn btn-primary"
                        ,type:"button"
                        ,onclick : "copiarDatosSeleccionadosToValidados()"
                        ,value : "".concat($("#idiomaUsuarioModal").val()=="4"?"Kopiatu hautatutako datuak balidatutakoei":"Copiar Datos Seleccionados a Validados")
                })));
                $("#tablaCriterios").append(cardCopiarDatos);
                $.each(respuestaFiltrada,function (indiceCriterio,criterio){  
                    var card = $("<div>",{
                        class:"card mb-3"
                        ,style:"max-width:715px;"
                    });
                    var cardHeader = $("<div>",{
                        class:"card-header"
                    });
                    cardHeader.append($("<h5>",{
                        text:("".concat(criterio.codigo,".",criterio.codigoOrden,". ",
                            ($("#idiomaUsuarioModal").val()=="4"?criterio.descripcion_EU:criterio.descripcion_ES)))
                    }));
                    var cardBody = $("<div>",{
                        class:"card-body"
                    });
                    
                    $.each(criterio.listaOpciones,function (indiceCriterioOpcion,criterioOpcion){
                        var divContenGroup = $("<div>", {
                            class:"input-group mb-3"
                        });
                        var inputGroupPrepend = $("<div>", {
                            class:"input-group-prepend"
                        });
                        var inputGroupAppend = $("<div>",{
                            class:"input-group-append"
                        });
                        var spanValidarText=$("<span>",{
                            class:"input-group-text"
                        }).append($("<i>",{
                            class:"fa fa-thumbs-up"
                            //,text:($("#idiomaUsuarioModal").val()=="4"?"Baliozkotu":"Validar")
                        }).prop("aria-hidden","true")).append($("#idiomaUsuarioModal").val()==="4"?" Baliozkotu":" Validar");
                        var divValidarInput=$("<div>",{
                            class:"input-group-text"
                        }).append($("<input>", {
                                type:"checkbox"
                                , id:("v_"+criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)
                                , name:("v_"+criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)
                                , onchange : "addDeleteCheckSeleccionadoListaVal('v_"+(criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)+"')"
                        }));
                        inputGroupAppend.append(spanValidarText);
                        inputGroupAppend.append(divValidarInput);
                                                                
                        var inputGroupText= $("<div>", {
                            class:"input-group-text"
                        });
                        inputGroupText.append($("<input>", {
                                type:"checkbox"
                                , id:(criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)
                                , name:(criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)
                                , onchange : "addDeleteCheckSeleccionadoLista('"+(criterio.codigo + "_" + criterio.codigoOrden + "_" + criterioOpcion.codigoOrden)+"')"
                        }));
                        inputGroupPrepend.append(inputGroupText);
                        divContenGroup.append(inputGroupPrepend);
                        divContenGroup.append($("<span>",{
                            class:"form-control"
                            ,style:"height:auto;font-size:12px;"
                            ,text:("".concat(criterioOpcion.codigoOrden,". ",
                                $("#idiomaUsuarioModal").val()=="4"?criterioOpcion.descripcion_EU:criterioOpcion.descripcion_ES))
                        }));
                        divContenGroup.append(inputGroupAppend);
                        cardBody.append(divContenGroup);
                    });
                    card.append(cardHeader);
                    card.append(cardBody);
                    
                    
                    /*
                    <div class="card">
                        <div class="card-header">
                          Featured
                        </div>
                        <div class="card-body">
                          <div class="input-group mb-3">
                            <div class="input-group-prepend">
                                  <div class="input-group-text">
                                    <input type="checkbox" aria-label="Checkbox for following text input">
                                  </div>
                            </div>
                            <input type="text" class="form-control" aria-label="Text input with checkbox">
                          </div>
                        </div>
                  </div>
                    */
                   $("#tablaCriterios").append(card);
                });  
            } else {
                jsp_alerta("A", "Lista de criterios no recuperada...");
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al guardar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function getCriteriosEvaluacionSeleccionadoCE(){
    if($("#idBDCentroEmpleo").val()!=null && $("#idBDCentroEmpleo").val()!="" && $("#idBDCentroEmpleo").val()!="null"){
        var dataParameter = $.extend({}, parametrosLLamadaM32);
        dataParameter.control = new Date().getTime();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.operacion = 'getCriteriosEvaluacionSeleccionadoCE';
        dataParameter.idCentro = $("#idBDCentroEmpleo").val();
        $.ajax({
            type: 'POST',
            url: urlBaseLlamadaM32,
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null && respuesta !== "") {
                    respuesta = JSON.parse(respuesta);
                    $.each(respuesta, function (indiceCriterioCE, criterioCE) {
                        $("#tablaCriterios input:checkbox").filter(function (indexElemento) {
                            return $(this).prop("name") === criterioCE.idElementoHTML;
                        }).each(function (indiceElementoHTML, elementoHTML) {
                            if (criterioCE.centroSeleccionOpcion === 1)
                                $(elementoHTML).prop("checked", true);
                            else
                                $(elementoHTML).prop("checked", false);
                            addDeleteCheckSeleccionadoLista($(elementoHTML).prop("id"));
                        });
                        // Criterios Validados
                        $("#tablaCriterios input:checkbox").filter(function (indexElemento) {
                            return $(this).prop("name") === "v_"+criterioCE.idElementoHTML;
                        }).each(function (indiceElementoHTML, elementoHTML) {
                            if (criterioCE.centroSeleccionOpcionVAL === 1)
                                $(elementoHTML).prop("checked", true);
                            else
                                $(elementoHTML).prop("checked", false);
                            addDeleteCheckSeleccionadoListaVal($(elementoHTML).prop("id"));
                        });
                    });
                } else {
                    jsp_alerta("A", "Lista de criterios registrados por el centro no cumplimentada...");
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al recuperar y mostrar los datos de criterios seleccionados por el centro de empleo. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }else{
        // Estamos dando de alta, verificamos si ya se han agregado previamente alguno 
        // Criterios Seleccioados
        if(listaCritEvaluacionSeleccionadosCE.length>0){
            $.each(listaCritEvaluacionSeleccionadosCE,function (indexID, elementoID) {
                $("#"+elementoID).prop("checked", true);
            });
        }
        // Criterios Validados
        if(listaCritEvaluacionSeleccionadosCEVal.length>0){
            $.each(listaCritEvaluacionSeleccionadosCEVal,function (indexID, elementoID) {
                $("#"+elementoID).prop("checked", true);
            });
        }
    }   
}

function getIsAmbitoCEspecial(idBDAmbito){
    var ambitoVO = getAmbitoCentroEmpleoVOByID(idBDAmbito);
    // Hay que mirar si est marcada la casilla de Especial. Porque capitales puedens er distribuidos y especiales
    return (ambitoVO !== null && ambitoVO.oriAmbCeEspecial===1 && $("#checkEspecial").prop("checked"));
}
function getIsAmbitoCDistribuido(idBDAmbito){
    var ambitoVO = getAmbitoCentroEmpleoVOByID(idBDAmbito);
    return (ambitoVO !== null && ambitoVO.OriAmbDistr===1);
}

function getAmbitoCentroEmpleoVOByID(idBDAmbito){
    var dataParameter = $.extend({}, parametrosLLamadaM32);
    dataParameter.control = new Date().getTime();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.operacion = 'getAmbitoCentroEmpleoVOByID';
    dataParameter.idBDAmbito = idBDAmbito;
    var response;
    $.ajax({
        type: 'POST',
        url: urlBaseLlamadaM32,
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null && respuesta!=="") {
                response = JSON.parse(respuesta);
            } else {
                console.log("Datos de Ambito " + idBDAmbito +" no recuperados.");
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al leer datos Ambito. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return  response;
}

function addDeleteCheckSeleccionadoLista(idElementoHtml){
    if($("#"+idElementoHtml)!=undefined && $("#"+idElementoHtml)!=null){
        if($("#"+idElementoHtml).prop("checked")){
            //if(!listaCritEvaluacionSeleccionadosCE.includes(idElementoHtml))
            if(listaCritEvaluacionSeleccionadosCE.indexOf(idElementoHtml)<0)
                listaCritEvaluacionSeleccionadosCE.push(idElementoHtml);
        }else{
            //if(listaCritEvaluacionSeleccionadosCE.includes(idElementoHtml))
            if(listaCritEvaluacionSeleccionadosCE.indexOf(idElementoHtml)>=0)
                listaCritEvaluacionSeleccionadosCE=listaCritEvaluacionSeleccionadosCE.filter(function (idHtml) {
                    return idElementoHtml!=idHtml;
                });
        }
    }
    // Actualizar input con datos de Crierios seleccionados por centro
    $("#listaCritEvaluacionSeleccionadosCE").val(listaCritEvaluacionSeleccionadosCE.toString());
}

function addDeleteCheckSeleccionadoListaVal(idElementoHtml){
    if($("#"+idElementoHtml)!=undefined && $("#"+idElementoHtml)!=null){
        if($("#"+idElementoHtml).prop("checked")){
            //if(!listaCritEvaluacionSeleccionadosCEVal.includes(idElementoHtml))
            if(listaCritEvaluacionSeleccionadosCEVal.indexOf(idElementoHtml)<0)
                listaCritEvaluacionSeleccionadosCEVal.push(idElementoHtml);
        }else{
            //if(listaCritEvaluacionSeleccionadosCEVal.includes(idElementoHtml))
            if(listaCritEvaluacionSeleccionadosCEVal.indexOf(idElementoHtml)>=0)
                listaCritEvaluacionSeleccionadosCEVal=listaCritEvaluacionSeleccionadosCEVal.filter(function (idHtml) {
                    return idElementoHtml!=idHtml;
                });
        }
    }
    // Actualizar input con datos de Crierios Validados por centro
    $("#listaCritEvaluacionSeleccionadosCEVal").val(listaCritEvaluacionSeleccionadosCEVal.toString());
}

function copiarDatosSeleccionadosToValidados(){
    // Limpiamos lo que se haya marcado
    $('#tablaCriterios input:checkbox[name^="v_"]').prop("checked",false);
    listaCritEvaluacionSeleccionadosCEVal = new Array();
    // Pasamos los datos de Seleccionados a los validados
    $('#tablaCriterios input:checkbox:not([name^="v_"]):checked').each(function (indiceElementoHTML, elementoHTML) {
        $("#v_"+$(elementoHTML).prop("id")).prop("checked", true);
        addDeleteCheckSeleccionadoListaVal("v_"+$(elementoHTML).prop("id"));
    });
}

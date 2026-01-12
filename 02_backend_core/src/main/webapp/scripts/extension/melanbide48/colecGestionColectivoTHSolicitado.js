/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var urlBaseLlamada = "/PeticionModuloIntegracion.do";
var paramLLamadaM48ColectTHSolicitado={
        tarea:'preparar'
        ,modulo:'MELANBIDE48'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,colecsolicitud:null
        ,identificadorBDEliminar:null
    };
    
var colecSolicitudJSON={
    codSolicitud:null,
    numExp:null,
    ejercicio:null,
    col1Ar:null,
    col1Bi:null,
    col1Gi:null,
    col2Ar:null,
    col2Bi:null,
    col2Gi:null,
    col3Ar:null,
    col3Bi:null,
    col3Gi:null,
    col4Ar:null,
    col4Bi:null,
    col4Gi:null,
    col1Ult5Val:null,
    col2Ult5Val:null,
    totVal:null,
    fecSysdate:null,
    codigoColectivo:null,
    codigoColectivoDesc:null,
    territorioHistorico:null,
    territorioHistoricoDesc:null,
    ambitoComarca:null,
    ambitoComarcaDesc:null,
    numeroBloquesHoras:null,
    numeroUbicaciones:null
};

var tabColectivosSolicitadosTH;
var listaColectivosSolicitadosTH = new Array();
var listaColectivosSolicitadosTHTabla = new Array();
    
$(document).ready(function() {
    //Cargamos los datos las tablas segun la convocatoria cargamos la tabla si entramos en el modulo
    // Si estamos en la ventana de edicion no cargamos las tablas datos de Colectivos solicitados
    if($("#pantallaEditarDatos")!== null && $("#pantallaEditarDatos").val()!==null && $("#pantallaEditarDatos").val()!== undefined 
            && $("#pantallaEditarDatos").val()!== "" && $("#pantallaEditarDatos").val()==="S"){
        // Se ha cargado el file js en la pagina jsp de alta edicion de datos 
        // Evento Cambio de Territorio: carga desplegable Ambito
        $("#territorioHistorico").change(function () {
            //if($("#codigoColectivo").val()!=="1" && $("#codigoColectivo")!=="2")
                cargarDesplegableAmbitoSumAmbitoPorColectivoTH($("#codigoColectivo").val(),$("#territorioHistorico").val());
        });
        $("#codigoColectivo").change(function () {
            cargarDesplegableAmbitoHorasSolicitadoPorColectivo($("#codigoColectivo").val());
            ocultaMuestraCamposSegunColectivo();
            cargarDesplegableAmbitoSumAmbitoPorColectivoTH($("#codigoColectivo").val(), $("#territorioHistorico").val());
        });
        if($("#colecSolicitudVOJSON")!== null && $("#colecSolicitudVOJSON").val()!== null){
            // Cargamos datos en los elementos para modificar
            var elementoJSON = JSON.parse($("#colecSolicitudVOJSON").val());
            $("#codSolicitud").val(elementoJSON.codSolicitud);
            $("#codigoColectivo").val(elementoJSON.codigoColectivo);
            $("#territorioHistorico").val(elementoJSON.territorioHistorico);
            $("#territorioHistorico").change();
            $("#ambitoComarca").val(elementoJSON.ambitoComarca);
            $("#numeroBloquesHoras").val(elementoJSON.numeroBloquesHoras);
            $("#numeroUbicaciones").val(elementoJSON.numeroUbicaciones);
        }
        ocultaMuestraCamposSegunColectivo();
    }else{
        // Se ha cargado el js al entrar en el expediente cargando modulo de extension
        var listaColectivosSolicitadosTHJSON = JSON.parse($("#listaColectivosSolicitadosTH").val());
        if ("CONV_ANTE-2021" === $("#codigoConvocatoriaExpediente").val()) {
            $("#divColecTHSolicitadoConvAntes2021").show();
            $("#divColecTHSolicitado").hide();
            if (listaColectivosSolicitadosTHJSON !== null && listaColectivosSolicitadosTHJSON !== undefined) {
                for (var i = 0; i < listaColectivosSolicitadosTHJSON.length; i++) {
                    var elementoJSON = listaColectivosSolicitadosTHJSON[i];
                    $("#idSolicitud").val(elementoJSON.codSolicitud);
                    //Alava
                    if (1 === elementoJSON.col1Ar)
                        $("#chkArabaColec1").attr("checked", "true");
                    if (1 === elementoJSON.col2Ar)
                        $("#chkArabaColec2").attr("checked", "true");
                    if (1 === elementoJSON.col3Ar)
                        $("#chkArabaColec3").attr("checked", "true");
                    if (1 === elementoJSON.col4Ar)
                        $("#chkArabaColec4").attr("checked", "true");
                    //GIpuzkoa
                    if (1 === elementoJSON.col1Gi)
                        $("#chkGipuzkoaColec1").attr("checked", "true");
                    if (1 === elementoJSON.col2Gi)
                        $("#chkGipuzkoaColec2").attr("checked", "true");
                    if (1 === elementoJSON.col3Gi)
                        $("#chkGipuzkoaColec3").attr("checked", "true");
                    if (1 === elementoJSON.col4Gi)
                        $("#chkGipuzkoaColec4").attr("checked", "true");
                    // Bizkaia
                    if (1 === elementoJSON.col1Bi)
                        $("#chkBizkaiaColec1").attr("checked", "true");
                    if (1 === elementoJSON.col2Bi)
                        $("#chkBizkaiaColec2").attr("checked", "true");
                    if (1 === elementoJSON.col3Bi)
                        $("#chkBizkaiaColec3").attr("checked", "true");
                    if (1 === elementoJSON.col4Bi)
                        $("#chkBizkaiaColec4").attr("checked", "true");
                }
            }
        } else { // Por defecto dejamos la de 2021. La mas nueva
            $("#divColecTHSolicitadoConvAntes2021").hide();
            $("#divColecTHSolicitado").show();
            if (listaColectivosSolicitadosTHJSON !== null && listaColectivosSolicitadosTHJSON !== undefined) {
                crearCargarTablaColectivosSolicitados(listaColectivosSolicitadosTHJSON);
            }
        }
    }
});

function crearCargarTablaColectivosSolicitados(listaColectivosSolicitadosTHJSON){
    listaColectivosSolicitadosTH = new Array();
    listaColectivosSolicitadosTHTabla = new Array();
    tabColectivosSolicitadosTH = new TablaColec(true,$("#descriptor_buscar").val(),$("#descriptor_anterior").val(),$("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(),$("#descriptor_noRegDisp").val(),$("#descriptor_filtrDeTotal").val(),$("#descriptor_primero").val(),$("#descriptor_ultimo").val(), $("#listaColecTHSolicitado")[0],600,25);
    tabColectivosSolicitadosTH.addColumna('100', 'left',$("#label_tabla_columna_colectivo").val());
    tabColectivosSolicitadosTH.addColumna('100', 'center', $("#listaColecTHSolicitado_textoColumna2").val());
    tabColectivosSolicitadosTH.addColumna('100', 'center',$("#listaColecTHSolicitado_textoColumna3").val());
    tabColectivosSolicitadosTH.addColumna('100', 'center',$("#listaColecTHSolicitado_textoColumna4").val());
    tabColectivosSolicitadosTH.addColumna('100', 'center',$("#listaColecTHSolicitado_textoColumna5").val());

    if(listaColectivosSolicitadosTHJSON!==null && listaColectivosSolicitadosTHJSON!==undefined){
        for (var i = 0; i < listaColectivosSolicitadosTHJSON.length; i++) {
            var elementoJSON = listaColectivosSolicitadosTHJSON[i];
            listaColectivosSolicitadosTH[i]=[elementoJSON.codSolicitud,elementoJSON.numExp,elementoJSON.ejercicio,elementoJSON.col1Ar,elementoJSON.col1Bi,elementoJSON.col1Gi,elementoJSON.col2Ar,elementoJSON.col2Bi,elementoJSON.col2Gi,elementoJSON.col3Ar,elementoJSON.col3Bi,elementoJSON.col3Gi,elementoJSON.col4Ar,elementoJSON.col4Bi,elementoJSON.col4Gi,elementoJSON.col1Ult5Val,elementoJSON.col2Ult5Val,elementoJSON.totVal,elementoJSON.fecSysdate,elementoJSON.codigoColectivo,elementoJSON.territorioHistorico,elementoJSON.ambitoComarca,elementoJSON.numeroBloquesHoras,elementoJSON.numeroUbicaciones,elementoJSON.codigoColectivoDesc,elementoJSON.territorioHistoricoDesc,elementoJSON.ambitoComarcaDesc];
            listaColectivosSolicitadosTHTabla[i]=[elementoJSON.codigoColectivoDesc,elementoJSON.territorioHistoricoDesc,elementoJSON.ambitoComarcaDesc,elementoJSON.numeroBloquesHoras,elementoJSON.numeroUbicaciones];
        }
    }
    tabColectivosSolicitadosTH.displayCabecera = true;
    tabColectivosSolicitadosTH.height = 300;
    tabColectivosSolicitadosTH.lineas=listaColectivosSolicitadosTHTabla;
    tabColectivosSolicitadosTH.displayTabla();
    
//    if(navigator.appName.indexOf("Internet Explorer")!==-1){
//        try {
//            var div = $("#listaColecTHSolicitado")[0];
//            div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
//            div.children[0].children[0].children[0].children[0].children[1].style.width = '100%';
//        }catch(err){
//        }
//    }
}

function anadirColecTHSolicitado() {
    lanzarPopUpModal($("#urlBaseLlamadaM48").val() +"?tarea=preparar&modulo=MELANBIDE48&operacion=cargarEditarAddNuevaColecTHSolicitado&tipo=0&numero="+$("#numeroExpediente").val()+"&codProcedimiento="+$("#codigoProcedimiento").val()+"&codOrganizacion="+$("#codigoOrganizacion").val() + "&control=" + new Date().getTime(), 600, 1180, 'no', 'no', function (result) {
        if (result !== null && result !== undefined) {
            crearCargarTablaColectivosSolicitados(result);
        }
    });
}
function modificarColecTHSolicitado() {
    var index = tabColectivosSolicitadosTH.selectedIndex;
    if (index != -1) {
        var codSolicitud = listaColectivosSolicitadosTH[index][0];
        lanzarPopUpModal($("#urlBaseLlamadaM48").val() +"?tarea=preparar&modulo=MELANBIDE48&operacion=cargarEditarAddNuevaColecTHSolicitado&tipo=0&numero="+$("#numeroExpediente").val()+"&codProcedimiento="+$("#codigoProcedimiento").val()+"&codOrganizacion="+$("#codigoOrganizacion").val() + "&codSolicitud="+codSolicitud+"&control=" + new Date().getTime(), 600, 1180, 'no', 'no', function (result) {
            if (result !== null && result !== undefined) {
                crearCargarTablaColectivosSolicitados(result);
            }
        });
    }else{
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}
function eliminarColecTHSolicitado() {
    var index = tabColectivosSolicitadosTH.selectedIndex;
    if (index != -1) {
        if (confirm("Está seguro de elimiar dicho registro?")) {
            //Porcedemos a dar el alta
            var dataParameter = $.extend({}, paramLLamadaM48ColectTHSolicitado);
            dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
            dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
            var registro = listaColectivosSolicitadosTH[index];
            dataParameter.numero = registro[1];
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'eliminarLineaColecSolicitud';
            dataParameter.identificadorBDEliminar = registro[0];
            dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
            $.ajax({
                type: 'POST',
                url: $("#urlBaseLlamadaM48").val(),
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                            return value === null ? "" : value;
                        }));
                        crearCargarTablaColectivosSolicitados(respuesta);
                        jsp_alerta("A", "Datos Eliminados Correctamente.");
                    } else {
                        jsp_alerta("A", "Los datos no se ha podido atualizar, si el error persite consulte con soporte.");
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al eliminar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });
        }
    } else {
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}

function guardarDatosColectivoTHSolicitado(){
    if (validarDatosObligatorios()) {
        var dataParameter = $.extend({}, paramLLamadaM48ColectTHSolicitado);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        var tipoOperacion = $("#codSolicitud").val() !== null && $("#codSolicitud").val() !== undefined && $("#codSolicitud").val() !== "" ? "1" : "0";
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.tipoOperacion = tipoOperacion;
        dataParameter.operacion = 'guardarSolicitudAjaxResptaJSON';
        dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
        dataParameter.colecsolicitud = JSON.stringify(prepararJsoncolecSolicitud());
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                        return value == null ? "" : value
                    }));
                    self.parent.opener.retornoXanelaAuxiliar(respuesta);
                    jsp_alerta("A", "Datos Guardados Correctamente.");
                } else {
                    jsp_alerta("A", "Los datos no se ha podido actualizar, si el error persiste consulte con soporte.");
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
        jsp_alerta("A", "Cumplimente el campo obligatorio (*)");
    }
}

function prepararJsoncolecSolicitud(){
    var  respuesta = $.extend({}, colecSolicitudJSON);
    respuesta.codSolicitud=$("#codSolicitud").val()!= undefined && $("#codSolicitud").val()!="" ? $("#codSolicitud").val() : null;
    respuesta.numExp=$("#numeroExpediente").val()!= undefined && $("#numeroExpediente").val()!="" ? $("#numeroExpediente").val() : null;
    respuesta.ejercicio=$("#ejercicio").val()!= undefined && $("#ejercicio").val()!="" ? $("#ejercicio").val() : null;  
    respuesta.codigoColectivo=$("#codigoColectivo").val()!= undefined && $("#codigoColectivo").val()!="" ? $("#codigoColectivo").val() : null; 
    respuesta.territorioHistorico=$("#territorioHistorico").val()!= undefined && $("#territorioHistorico").val()!="" ? $("#territorioHistorico").val() : null; 
    respuesta.ambitoComarca=$("#ambitoComarca").val()!= undefined && $("#ambitoComarca").val()!="" ? $("#ambitoComarca").val() : null; 
    respuesta.numeroBloquesHoras=$("#numeroBloquesHoras").val()!= undefined && $("#numeroBloquesHoras").val()!="" ? $("#numeroBloquesHoras").val() : null; 
    respuesta.numeroUbicaciones=$("#numeroUbicaciones").val()!= undefined && $("#numeroUbicaciones").val()!="" ? $("#numeroUbicaciones").val() : null;
    return respuesta;
}

function validarDatosObligatorios(){
    return true;
}

function cancelarEdicionColecTHSolicitado(){
    limpiarFormularioColecTHSolicitado();
    cerrarVentanaColec();
}

function limpiarFormularioColecTHSolicitado(){
    $("#colecSolicitudVOJSON").val("");
    $("#codSolicitud").val("");
    $("#codigoColectivo").val("");
    $("#territorioHistorico").val("");
    $("#ambitoComarca").val("");
    $("#numeroBloquesHoras").val("");
    $("#numeroUbicaciones").val("");
}

function cerrarVentanaColec(){
    window.close();
}

function cargarDesplegableAmbitoSumAmbitoPorColectivoTH(idColectivo,codigoTH){
    var dataParameter = $.extend({}, paramLLamadaM48ColectTHSolicitado);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'cargarAmbitosxColectivoConvocatoriaTHJSON';
    dataParameter.codigoTH = codigoTH;
    dataParameter.idColectivo = idColectivo;
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $("#ambitoComarca").empty();
                // Opcion Vacia
                $('#ambitoComarca').append($('<option>', {
                 value: "",
                 text: $("#textoDesplegableSeleccionaOpcion").val()
                 }));
                $.each(respuesta, function (index, item) {
                    $('#ambitoComarca').append($('<option>', {
                    value: item.codigo,
                    text: item.descripcion,
                    title: item.descripcion
                    }));
                 });
                 if(idColectivo==="1" || idColectivo==="2"){
                     if($('#ambitoComarca option').length>1){
                         $('#ambitoComarca').val($($('#ambitoComarca option')[1]).val());
                     }
                 }
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos para cargar el Combo de Ambitos/Subambitos para la provincia seleccionada, si el error persite consulte con soporte.");
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

function cargarDesplegableAmbitoHorasSolicitadoPorColectivo(idColectivo){
    var dataParameter = $.extend({}, paramLLamadaM48ColectTHSolicitado);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'cargarAmbitosxColectivoConvocatoriaJSON';
    dataParameter.idColectivo = idColectivo;
    dataParameter.idBDConvocatoriaExpediente = $("#idBDConvocatoriaExpediente").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $("#ambitoComarca").empty();
                // Opcion Vacia
                $('#ambitoComarca').append($('<option>', {
                 value: "",
                 text: $("#textoDesplegableSeleccionaOpcion").val()
                 }));
                $.each(respuesta, function (index, item) {
                    $('#ambitoComarca').append($('<option>', {
                    value: item.codigo,
                    text: item.descripcion,
                    title: item.descripcion
                    }));
                 });
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos para cargar el Combo de Ambitos/Subambitos para la provincia seleccionada, si el error persite consulte con soporte.");
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

function ocultaMuestraCamposSegunColectivo(){
    if ($("#codigoColectivo").val() === "1" || $("#codigoColectivo").val() === "2") {
        //$("#ambitoComarca").val("");
        //$("#ambitoComarca").change();
        $("#ambitoComarca").attr("disabled",true);
        $("#numeroBloquesHoras").val("");
        $("#divNumeroBloquesHoras").hide();
    } else {
        $("#ambitoComarca").attr("disabled",false);        
        $("#divNumeroBloquesHoras").show();
    }
}
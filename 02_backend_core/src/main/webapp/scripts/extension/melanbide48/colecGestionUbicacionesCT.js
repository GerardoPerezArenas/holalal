/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var urlBaseLlamada = "/PeticionModuloIntegracion.do";
var paramLLamadaM48ColectUbicacionCT={
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
    };
    
var colecUbicacionesCTVO={
    codId:null,
    numExpediente:null,
    codTipoColectivo:null,
    codEntidad:null,
    territorioHist:null,
    comarca:null,
    fkIdAmbitoSolicitado:null,
    municipio:null,
    localidad:null,
    direccion:null,
    direccionPortal:null,
    direccionPiso:null,
    direccionLetra:null,
    codigoPostal:null,
    telefono:null,
    cifEntidad:null,
    nombreEntidad:null, 
    descTerritorioHist:null,
    descComarca:null,
    descAmbitoSolicitado:null,
    descMunicipio:null,
    localesPreviamenteAprobados:null,
    mantieneRequisitosLocalesAprob:null,
    disponeEspacioComplWifi:null,
    descColectivo:null
};

var tabUbicacionesCentroTrabajo;
var listaUbicacionesCentroTrabajo = new Array();
var listaUbicacionesCentroTrabajoTabla = new Array();
    
$(document).ready(function() {
    // Conprobamos Si estamos en la ventana de edicion
    if($("#pantallaEditarDatos")!== null && $("#pantallaEditarDatos").val()!==null && $("#pantallaEditarDatos").val()!== undefined 
            && $("#pantallaEditarDatos").val()!== "" && $("#pantallaEditarDatos").val()==="S"){
        // Se ha cargado el file js en la pagina jsp de alta edicion de datos 
        // Evento Cambio Pulsar Guardar Datos
        $("#btnAnadir").click(function () {
            guardarDatosUbicacionCentTraJS();
        });
    }else{
        var listaUbicacionesCTJSON1 = JSON.parse($("#listaUbicacionesCTJSON").val());
        crearCargarTablaUbicacionesCT(listaUbicacionesCTJSON1);
    }
});

function crearCargarTablaUbicacionesCT(listaUbicacionesCTJSON){
    listaUbicacionesCentroTrabajo = new Array();
    listaUbicacionesCentroTrabajoTabla = new Array();
    var anchoTabla = ($("#ejercicioExpediente").val() < 2018 ? 1455 : 1850);
    tabUbicacionesCentroTrabajo = new TablaColec(true,$("#descriptor_buscar").val(),$("#descriptor_anterior").val(),$("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(),$("#descriptor_noRegDisp").val(),$("#descriptor_filtrDeTotal").val(),$("#descriptor_primero").val(),$("#descriptor_ultimo").val(), $("#listaUbicacionesCentroTrabajo")[0],anchoTabla,25);
    tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna1").val());
    tabUbicacionesCentroTrabajo.addColumna('150', 'center', $("#listaUbicacionesCentroTrabajo_textoColumna2").val());
    tabUbicacionesCentroTrabajo.addColumna('300', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna3").val());
    tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna4").val());
    tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna5").val());
    tabUbicacionesCentroTrabajo.addColumna('300', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna6").val());
    tabUbicacionesCentroTrabajo.addColumna('100', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna7").val());
    tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna8").val());
    if($("#ejercicioExpediente").val()>=2018){
        tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna9").val());
        if ("CONV_ANTE-2021" === $("#codigoConvocatoriaExpediente").val()) {
            tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna10").val());
        }else{
            tabUbicacionesCentroTrabajo.addColumna('150', 'center',$("#listaUbicacionesCentroTrabajo_textoColumna11").val());
        }
        
    }
    
    if(listaUbicacionesCTJSON!==null && listaUbicacionesCTJSON!==undefined){
        for (var i = 0; i < listaUbicacionesCTJSON.length; i++) {
            var elementoJSON = listaUbicacionesCTJSON[i];
            listaUbicacionesCentroTrabajo[i]=[elementoJSON.codId ,elementoJSON.numExpediente ,elementoJSON.codTipoColectivo ,elementoJSON.codEntidad ,elementoJSON.territorioHist ,elementoJSON.comarca ,elementoJSON.municipio ,elementoJSON.localidad ,elementoJSON.direccion ,elementoJSON.direccionPortal ,elementoJSON.direccionPiso ,elementoJSON.direccionLetra ,elementoJSON.codigoPostal ,elementoJSON.telefono ,elementoJSON.cifEntidad ,elementoJSON.nombreEntidad  ,elementoJSON.descTerritorioHist ,elementoJSON.descComarca ,elementoJSON.descMunicipio ,elementoJSON.localesPreviamenteAprobados ,elementoJSON.mantieneRequisitosLocalesAprob ,elementoJSON.disponeEspacioComplWifi,elementoJSON.fkIdAmbitoSolicitado,elementoJSON.descAmbitoSolicitado];
             if($("#ejercicioExpediente").val()>=2018){
                 var descSiNo_localesPreviamenteAprobados = (elementoJSON.localesPreviamenteAprobados===1?$("#label_si").val():$("#label_no").val());
                 var descSiNo_mantieneRequisitosLocalesAprob = (elementoJSON.mantieneRequisitosLocalesAprob===1?$("#label_si").val():$("#label_no").val());
                 var descSiNo_disponeEspacioComplWifi = (elementoJSON.disponeEspacioComplWifi===1?$("#label_si").val():$("#label_no").val());
                 if ("CONV_ANTE-2021" === $("#codigoConvocatoriaExpediente").val()) {
                     listaUbicacionesCentroTrabajoTabla[i] = [elementoJSON.descColectivo, elementoJSON.descTerritorioHist, elementoJSON.nombreEntidad, elementoJSON.descComarca, elementoJSON.descMunicipio, elementoJSON.direccion, elementoJSON.codigoPostal, elementoJSON.telefono,descSiNo_localesPreviamenteAprobados,descSiNo_mantieneRequisitosLocalesAprob];
                 }else{
                     listaUbicacionesCentroTrabajoTabla[i] = [elementoJSON.descColectivo, elementoJSON.descTerritorioHist, elementoJSON.nombreEntidad, elementoJSON.descAmbitoSolicitado, elementoJSON.descMunicipio, elementoJSON.direccion, elementoJSON.codigoPostal, elementoJSON.telefono,descSiNo_localesPreviamenteAprobados,descSiNo_disponeEspacioComplWifi];
                 }                 
             }else{
                 listaUbicacionesCentroTrabajoTabla[i] = [elementoJSON.descColectivo, elementoJSON.descTerritorioHist, elementoJSON.nombreEntidad,elementoJSON.descComarca, elementoJSON.descMunicipio, elementoJSON.direccion, elementoJSON.codigoPostal, elementoJSON.telefono];
             }
        }
    }
    tabUbicacionesCentroTrabajo.displayCabecera = true;
    tabUbicacionesCentroTrabajo.height = 250;
    tabUbicacionesCentroTrabajo.lineas=listaUbicacionesCentroTrabajoTabla;
    tabUbicacionesCentroTrabajo.displayTabla();
}

function anadirUbicacionCentroTrabajo() {
    lanzarPopUpModal($("#urlBaseLlamadaM48").val() +"?tarea=preparar&modulo=MELANBIDE48&operacion=cargarAltaEdicionUbicacionesCentroTrabajo&tipo=0&modoDatos=0&numero="+$("#numeroExpediente").val()+"&codProcedimiento="+$("#codigoProcedimiento").val()+"&codOrganizacion="+$("#codigoOrganizacion").val() + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
        if (result !== null && result !== undefined) {
            crearCargarTablaUbicacionesCT(result);
        }
    });
}
function modificarUbicacionCentroTrabajo() {
    var index = tabUbicacionesCentroTrabajo.selectedIndex;
    if (index !== -1) {
        var idTray = listaUbicacionesCentroTrabajo[index][0];
        var codEntidad = listaUbicacionesCentroTrabajo[index][3];
        lanzarPopUpModal($("#urlBaseLlamadaM48").val() +"?tarea=preparar&modulo=MELANBIDE48&operacion=cargarAltaEdicionUbicacionesCentroTrabajo&tipo=0&modoDatos=1&numero="+$("#numeroExpediente").val()+"&codProcedimiento="+$("#codigoProcedimiento").val()+"&codOrganizacion="+$("#codigoOrganizacion").val() + "&idTray="+idTray + "&codEntidad="+codEntidad +"&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
            if (result !== null && result !== undefined) {
                crearCargarTablaUbicacionesCT(result);
            }
        });
    }else{
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}

function eliminarUbicacionCentroTrabajo() {
    var index = tabUbicacionesCentroTrabajo.selectedIndex;
    if (index !== -1) {
        if (confirm("Está seguro de elimiar dicho registro?")) {
            //Porcedemos a dar el alta
            var dataParameter = $.extend({}, paramLLamadaM48ColectUbicacionCT);
            dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
            dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
            var registro = listaUbicacionesCentroTrabajo[index];
            dataParameter.numero = registro[1];
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'eliminarUbicacionCentroTraPorCodigoYExpediente';
            dataParameter.identificadorBDEliminar = registro[0];
            dataParameter.idTray = registro[0];
            $.ajax({
                type: 'POST',
                url: $("#urlBaseLlamadaM48").val(),
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null) {
                        respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                            return value === null ? "" : value;
                        }));
                        crearCargarTablaUbicacionesCT(respuesta);
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

function guardarDatosUbicacionCentTraJS(){
    if (validarDatosObligatorios()) {
        var dataParameter = $.extend({}, paramLLamadaM48ColectUbicacionCT);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        var tipoOperacion = $("#codId").val() !== null && $("#codId").val() !== undefined && $("#codId").val() !== "" ? "1" : "0";
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.tipoOperacion = tipoOperacion;
        dataParameter.operacion = 'guardarUbicacionCTJSON';
        dataParameter.codigoConvocatoriaExpediente = $("#codigoConvocatoriaExpediente").val();
        dataParameter.colecubicacionesct = JSON.stringify(prepararJsoncolecubicacionesct());
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                        return value === null ? "" : value;
                    }));
                    self.parent.opener.retornoXanelaAuxiliar(respuesta);
                    jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Zuzen gordetako datuak." : "Datos Guardados Correctamente.");
                } else {
                    jsp_alerta("A", "4" === $("#idiomaUsuario").val() ? "Datuak ezin izan dira eguneratu, erroreak jarraitzen badu, kontsultatu euskarriarekin." : "Los datos no se ha podido actualizar, si el error persiste consulte con soporte.");
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

function prepararJsoncolecubicacionesct(){
    var  respuesta = $.extend({}, colecUbicacionesCTVO);
    respuesta.codId=$("#codId").val()!== undefined && $("#codId").val() !== "" ? $("#codId").val() : null;
    respuesta.numExpediente=$("#numeroExpediente").val()!== undefined && $("#numeroExpediente").val() !== "" ? $("#numeroExpediente").val() : null;
    respuesta.codTipoColectivo=$("#codColectivo").val()!== undefined && $("#codColectivo").val() !== "" ? $("#codColectivo").val() : null;
    respuesta.codEntidad=$("#codEntidad").val()!== undefined && $("#codEntidad").val() !== "" ? $("#codEntidad").val() : null;
    respuesta.territorioHist=$("#codAmbito").val()!== undefined && $("#codAmbito").val() !== "" ? $("#codAmbito").val() : null;
    if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
        respuesta.comarca=$("#codComarca").val()!== undefined && $("#codComarca").val() !== "" ? $("#codComarca").val() : null;
    }else{
        respuesta.fkIdAmbitoSolicitado=$("#codComarca").val()!== undefined && $("#codComarca").val() !== "" ? $("#codComarca").val() : null;
    }    
    respuesta.municipio=$("#codMunicipio").val()!== undefined && $("#codMunicipio").val() !== "" ? $("#codMunicipio").val() : null;
    //respuesta.localidad=$("#localidad").val()!== undefined && $("#localidad").val() !== "" ? $("#localidad").val() : null;
    respuesta.direccion=$("#direccionCT").val()!== undefined && $("#direccionCT").val() !== "" ? $("#direccionCT").val() : null;
    respuesta.direccionPortal=$("#direccionPortal").val()!== undefined && $("#direccionPortal").val() !== "" ? $("#direccionPortal").val() : null;
    respuesta.direccionPiso=$("#direccionPiso").val()!== undefined && $("#direccionPiso").val() !== "" ? $("#direccionPiso").val() : null;
    respuesta.direccionLetra=$("#direccionLetra").val()!== undefined && $("#direccionLetra").val() !== "" ? $("#direccionLetra").val() : null;
    respuesta.codigoPostal=$("#codigoPostalCT").val()!== undefined && $("#codigoPostalCT").val() !== "" ? $("#codigoPostalCT").val() : null;
    respuesta.telefono=$("#telefonoCT").val()!== undefined && $("#telefonoCT").val() !== "" ? $("#telefonoCT").val() : null;
    //respuesta.cifEntidad=$("#cifEntidad").val()!== undefined && $("#cifEntidad").val() !== "" ? $("#cifEntidad").val() : null;
    respuesta.nombreEntidad =$("#descEntidad").val()!== undefined && $("#descEntidad").val() !== "" ? $("#descEntidad").val() : null;
    respuesta.descTerritorioHist=$("#descAmbito").val()!== undefined && $("#descAmbito").val() !== "" ? $("#descAmbito").val() : null;
    respuesta.descComarca=$("#descComarca").val()!== undefined && $("#descComarca").val() !== "" ? $("#descComarca").val() : null;
    respuesta.descMunicipio=$("#descMunicipio").val()!== undefined && $("#descMunicipio").val() !== "" ? $("#descMunicipio").val() : null;
    respuesta.localesPreviamenteAprobados=$("#localPrevAprobado")!== null && $("#localPrevAprobado")!==undefined && $("#localPrevAprobado").prop("checked")===true ? 1 : 0;
    respuesta.mantieneRequisitosLocalesAprob=$("#mantieneRequLocalApro")!== null && $("#mantieneRequLocalApro")!==undefined && $("#mantieneRequLocalApro").prop("checked")===true ? 1 : 0;
    respuesta.disponeEspacioComplWifi=$("#disponeEspacioComplWifi")!== null && $("#disponeEspacioComplWifi")!==undefined && $("#disponeEspacioComplWifi").prop("checked")===true ? 1 : 0;
    return respuesta;
}

function validarDatosObligatorios(){
    var retorno = true;
    if(($("#codEntidad")===null || $("#codEntidad").val()==="")
       ||($("#codColectivo")===null || $("#codColectivo").val()==="")
       ||($("#codAmbito")===null || $("#codAmbito").val()==="")
       ||($("#codComarca")===null || $("#codComarca").val()==="")
       ||($("#codMunicipio")===null || $("#codMunicipio").val()==="")
       ||($("#direccionCT")===null || $("#direccionCT").val()==="")
       ||($("#codigoPostalCT")===null || $("#codigoPostalCT").val()==="")
    )
    {
        retorno=false;
    }else{
        if($("#direccionCT").val()!==null && $("#direccionCT").val()!==""){
            retorno=comprobarDireccion($("#direccionCT").val());
        }
    }
    return retorno;
}

function limpiarFormularioUbicacionCentroTrabajo(){
    $("#codId").val("");
    $("#numeroExpediente").val("");
    $("#codColectivo").val("");
    $("#codEntidad").val("");
    $("#codAmbito").val("");
    $("#codComarca").val("");
    $("#codMunicipio").val("");
    $("#localidad").val("");
    $("#direccionCT").val("");
    $("#direccionPortal").val("");
    $("#direccionPiso").val("");
    $("#direccionLetra").val("");
    $("#codigoPostalCT").val("");
    $("#telefonoCT").val("");
    $("#localPrevAprobado").attr("checked","false");
    $("#mantieneRequLocalApro").attr("checked","false");
    $("#disponeEspacioComplWifi").attr("checked","false");
}

function comprobarDireccion(direccion){
    if(direccion !== undefined && direccion !== null && direccion !== ''){
        if(direccion.length <= 200){
            if(comprobarCaracteresEspecialesColec(direccion)){
                return true;
            }else{
                jsp_alerta("A",$("#msg_nuevoCentro_direcciones_dirCaracteresNoPermitidos").val());
                return false;
            }
        }else{
            jsp_alerta("A",$("#msg_nuevoCentro_direcciones_dirExcedeLongitud").val());
            return false;
        }
    }else{
        return false;
    }
}

function getDatosListaUbicacionesExpediente(){
    var dataParameter = $.extend({}, paramLLamadaM48ColectUbicacionCT);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getDatosListaUbicacionesExpediente';
    var datosRetorno = null;
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                    return value === null ? "" : value;
                }));
                datosRetorno=respuesta;
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al recuperar los datos lista de ubicaciones. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return datosRetorno;
}

/**
 * Centraliza los metodos que permiten cargar los datos de la pestana, casos en que datos relacionados (P.E. Entidad) se modifiquen en otra pestana.
 * @returns {undefined}
 */
function refrescarPestanaUbicacionesCentrosTrabajo(){
    var datosCT = getDatosListaUbicacionesExpediente();
    crearCargarTablaUbicacionesCT(datosCT);
}

function valorarUbicacionCentroTrabajo(){
    var index = tabUbicacionesCentroTrabajo.selectedIndex;
    if (index !== -1) {
        var idTray = listaUbicacionesCentroTrabajo[index][0];
        // Este codigo es la entidad de la ubicacion, y puede ser una entidad hija en caso de asociaciones, necesitamos pasar el Id de la entidad padre.
        //var codEntidad = listaUbicacionesCentroTrabajo[index][3];
        lanzarPopUpModal($("#urlBaseLlamadaM48").val() + "?tarea=preparar&modulo=MELANBIDE48&operacion=cargarValorarUbicacionesCentroTrabajo&tipo=0&modoDatos=0&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val() + "&idTray="+idTray + "&codEntidad="+$("#idBDEntidad").val()+"&control=" + new Date().getTime(), 800, 1100, 'no', 'no', function (result) {
            if (result !== null && result !== undefined) {
                crearCargarTablaUbicacionesCT(result);
            }
        });
    }else{
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}

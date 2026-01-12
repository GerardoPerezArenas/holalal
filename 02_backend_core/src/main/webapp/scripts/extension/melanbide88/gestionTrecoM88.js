/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var tabMelanbide88Socios;
var listaMelanbide88Socios = new Array();
var listaMelanbide88SociosTabla = new Array();

var tabMelanbide88Inversiones;
var listaMelanbide88Inversiones = new Array();
var listaMelanbide88InversionesTabla = new Array();

var tabMelanbide88Subsolic;
var listaMelanbide88Subsolic = new Array();
var listaMelanbide88SubsolicTabla = new Array();

$(document).ready(function() {
    asignarManejadoresEventos();
    getListaDatosPestanaSocios();
    getListaDatosPestanaInversiones();
    getListaDatosPestanaSubvenciones();
});

function asignarManejadoresEventos(){
    //
}

// Operaciones de Creacion Estrcutura y asignacion datos tabla
function crearEstructuraBasicaTablaMelanbide88Socios(respuesta){
    listaMelanbide88Socios = new Array();
    listaMelanbide88SociosTabla = new Array();
    tabMelanbide88Socios = new Tabla(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaMelanbide88Socios")[0], 1050);
    tabMelanbide88Socios.addColumna('300', 'left', $("#label_tabla_columna_nombre").val());
    tabMelanbide88Socios.addColumna('300', 'left', $("#label_tabla_columna_apellido1").val());
    tabMelanbide88Socios.addColumna('300', 'left', $("#label_tabla_columna_apellido2").val());
    tabMelanbide88Socios.addColumna('150', 'center', $("#label_tabla_columna_documento").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaMelanbide88Socios[i] = [elementoJSON.id, elementoJSON.num_exp, elementoJSON.dniNieSocio, elementoJSON.nombreSocio, elementoJSON.apellido1Socio, elementoJSON.apellido2Socio, elementoJSON.fechaAlta];
            listaMelanbide88SociosTabla[i] = [getTextoOrCastNullOrUndefinedToVacio(elementoJSON.nombreSocio),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.apellido1Socio),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.apellido2Socio),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.dniNieSocio)];
        }
    }
    tabMelanbide88Socios.displayCabecera = true;
    tabMelanbide88Socios.height = 250;
    tabMelanbide88Socios.lineas = listaMelanbide88SociosTabla;
    tabMelanbide88Socios.displayTabla();
}


function crearEstructuraBasicaTablaMelanbide88Inversiones(respuesta){
    listaMelanbide88Inversiones = new Array();
    listaMelanbide88InversionesTabla = new Array();
    tabMelanbide88Inversiones = new Tabla(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaMelanbide88Inversiones")[0], 1250);
    tabMelanbide88Inversiones.addColumna('150', 'center', $("#label_tabla_columna_fecha").val());
    tabMelanbide88Inversiones.addColumna('100', 'center', $("#label_tabla_columna_numerofactura").val());
    tabMelanbide88Inversiones.addColumna('300', 'left', $("#label_tabla_columna_descripcion").val());
    tabMelanbide88Inversiones.addColumna('300', 'left', $("#label_tabla_columna_proveedor").val());
    tabMelanbide88Inversiones.addColumna('150', 'center', $("#label_tabla_columna_importe_noiva").val());
    tabMelanbide88Inversiones.addColumna('100', 'center', $("#label_tabla_columna_pagada").val());
    tabMelanbide88Inversiones.addColumna('150', 'center', $("#label_tabla_columna_fechapago").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaMelanbide88Inversiones[i] = [elementoJSON.id, elementoJSON.num_exp, elementoJSON.fecha, elementoJSON.numFactura, elementoJSON.descripcion, elementoJSON.nombProveedor, elementoJSON.importe,elementoJSON.pagada,elementoJSON.fechaPago,elementoJSON.fechaAlta];
            listaMelanbide88InversionesTabla[i] = [getTextoOrCastNullOrUndefinedToVacio(elementoJSON.fecha),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.numFactura),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.descripcion),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.nombProveedor),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.importe),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.pagada),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.fechaPago)];
        }
    }
    tabMelanbide88Inversiones.displayCabecera = true;
    tabMelanbide88Inversiones.height = 250;
    tabMelanbide88Inversiones.lineas = listaMelanbide88InversionesTabla;
    tabMelanbide88Inversiones.displayTabla();
}

function crearEstructuraBasicaTablaMelanbide88Subsolic(respuesta){
    listaMelanbide88Subsolic = new Array();
    listaMelanbide88SubsolicTabla = new Array();
    tabMelanbide88Subsolic = new Tabla(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaMelanbide88Subsolic")[0], 950);
    tabMelanbide88Subsolic.addColumna('100', 'center', $("#label_tabla_columna_estado").val());
    tabMelanbide88Subsolic.addColumna('300', 'left', $("#label_tabla_columna_organismo").val());
    tabMelanbide88Subsolic.addColumna('300', 'left', $("#label_tabla_columna_objeto").val());
    tabMelanbide88Subsolic.addColumna('150', 'center', $("#label_tabla_columna_importe_euros").val());
    tabMelanbide88Subsolic.addColumna('100', 'center', $("#label_tabla_columna_fecha_solicitud_concesion").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaMelanbide88Subsolic[i] = [elementoJSON.id, elementoJSON.num_exp, elementoJSON.estado, elementoJSON.organismo, elementoJSON.objeto, elementoJSON.importe, elementoJSON.fecha,elementoJSON.fechaAlta];
            listaMelanbide88SubsolicTabla[i] = [getTextoOrCastNullOrUndefinedToVacio(elementoJSON.estado),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.organismo),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.objeto),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.importe),getTextoOrCastNullOrUndefinedToVacio(elementoJSON.fecha)];
        }
    }
    tabMelanbide88Subsolic.displayCabecera = true;
    tabMelanbide88Subsolic.height = 250;
    tabMelanbide88Subsolic.lineas = listaMelanbide88SubsolicTabla;
    tabMelanbide88Subsolic.displayTabla();
}

// Operacioenes carga de Datos tablas

function getListaDatosPestanaSocios(){
    var dataParameter = $.extend({}, parametrosLLamadaM88);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaDatosPestanaSocios';
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM88").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                crearEstructuraBasicaTablaMelanbide88Socios(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al leer los datos - Socios -' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function getListaDatosPestanaInversiones(){
    var dataParameter = $.extend({}, parametrosLLamadaM88);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaDatosPestanaInversiones';
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM88").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                crearEstructuraBasicaTablaMelanbide88Inversiones(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al leer los datos - Inversiones -' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function getListaDatosPestanaSubvenciones(){
    var dataParameter = $.extend({}, parametrosLLamadaM88);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaDatosPestanaSubvenciones';
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM88").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                crearEstructuraBasicaTablaMelanbide88Subsolic(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al leer los datos - Subvenciones -' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

// Operaciones sobre Pestanas
function anadirMelanbide88(codPestana){
    switch (codPestana) {
        case PESTANA_SOCIOS:
            lanzarPopUpModal($("#urlBaseLlamadaM88").val() + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionSocios&tipo=0&modoDatos=0&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val() + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                if (result !== null && result !== undefined) {
                    crearEstructuraBasicaTablaMelanbide88Socios(result);
                }
            });
            break;
        case PESTANA_INVERSIONES:     
            lanzarPopUpModal($("#urlBaseLlamadaM88").val() + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionInversiones&tipo=0&modoDatos=0&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val() + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                if (result !== null && result !== undefined) {
                    crearEstructuraBasicaTablaMelanbide88Inversiones(result);
                }
            });
            break;
        case PESTANA_SUBVENCIONES:   
            lanzarPopUpModal($("#urlBaseLlamadaM88").val() + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionSubvenciones&tipo=0&modoDatos=0&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val() + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                if (result !== null && result !== undefined) {
                    crearEstructuraBasicaTablaMelanbide88Subsolic(result);
                }
            });
            break;
        default:
            break;
    }
}
function modificarMelanbide88(codPestana){
    switch (codPestana) {
        case PESTANA_SOCIOS:
            if (isElmentoTablaSeleccionado(tabMelanbide88Socios)) {
                var identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Socios, getIndexSeleccionadoTabla(tabMelanbide88Socios));
                lanzarPopUpModal($("#urlBaseLlamadaM88").val()
                        + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionSocios&tipo=0&modoDatos=1&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val()
                        + "&identificadorBDGestionar=" + identificadorBDGestionar
                        + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                    if (result !== null && result !== undefined) {
                        crearEstructuraBasicaTablaMelanbide88Socios(result);
                    }
                });
            } else {
                jsp_alerta("A", $("#msg_msjNoSelecFila").val());
            }
            break;
        case PESTANA_INVERSIONES:
            if (isElmentoTablaSeleccionado(tabMelanbide88Inversiones)) {
                var identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Inversiones, getIndexSeleccionadoTabla(tabMelanbide88Inversiones));
                lanzarPopUpModal($("#urlBaseLlamadaM88").val()
                        + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionInversiones&tipo=0&modoDatos=1&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val()
                        + "&identificadorBDGestionar=" + identificadorBDGestionar
                        + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                    if (result !== null && result !== undefined) {
                        crearEstructuraBasicaTablaMelanbide88Inversiones(result);
                    }
                });
            } else {
                jsp_alerta("A", $("#msg_msjNoSelecFila").val());
            }
            break;
        case PESTANA_SUBVENCIONES:
            if (isElmentoTablaSeleccionado(tabMelanbide88Subsolic)) {
                var identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Subsolic, getIndexSeleccionadoTabla(tabMelanbide88Subsolic));
                lanzarPopUpModal($("#urlBaseLlamadaM88").val()
                        + "?tarea=preparar&modulo=MELANBIDE88&operacion=cargarAltaEdicionSubvenciones&tipo=0&modoDatos=1&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val()
                        + "&identificadorBDGestionar=" + identificadorBDGestionar
                        + "&control=" + new Date().getTime(), 600, 1200, 'no', 'no', function (result) {
                    if (result !== null && result !== undefined) {
                        crearEstructuraBasicaTablaMelanbide88Subsolic(result);
                    }
                });
            } else {
                jsp_alerta("A", $("#msg_msjNoSelecFila").val());
            }
            break;
        default:
            break;
    }
}
function eliminarMelanbide88(codPestana){
    if (isElmentoTablaSeleccionado((codPestana===PESTANA_SOCIOS?tabMelanbide88Socios:(codPestana===PESTANA_INVERSIONES?tabMelanbide88Inversiones:tabMelanbide88Subsolic)))) {
        if(jsp_alerta('C',$("#msg_seguro_eliminar").val())){
            var dataParameter = $.extend({}, parametrosLLamadaM88);
            dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
            dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.control = new Date().getTime();
            switch (codPestana) {
                case PESTANA_SOCIOS:
                    dataParameter.operacion = "eliminarDatosSocio"
                    dataParameter.identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Socios, getIndexSeleccionadoTabla(tabMelanbide88Socios));
                    break;
                case PESTANA_INVERSIONES:
                    dataParameter.operacion = "eliminarDatosInversion"
                    dataParameter.identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Inversiones, getIndexSeleccionadoTabla(tabMelanbide88Inversiones));
                    break;
                case PESTANA_SUBVENCIONES:
                    dataParameter.operacion = "eliminarDatosSubvencion"
                    dataParameter.identificadorBDGestionar = getIDFromObjetoTablaLista(listaMelanbide88Subsolic, getIndexSeleccionadoTabla(tabMelanbide88Subsolic));
                    break;
                default:
                    dataParameter.operacion = "sinOperacionAEjecutarError"
                    break;
            }          
            $.ajax({
                type: 'POST',
                url: $("#urlBaseLlamadaM88").val(),
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null && respuesta !== undefined) {
                        respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                            return value === null ? "" : value;
                        }));
                        switch (codPestana) {
                            case PESTANA_SOCIOS:
                                crearEstructuraBasicaTablaMelanbide88Socios(respuesta);
                                break;
                            case PESTANA_INVERSIONES:
                                crearEstructuraBasicaTablaMelanbide88Inversiones(respuesta);
                                break;
                            case PESTANA_SUBVENCIONES:
                                crearEstructuraBasicaTablaMelanbide88Subsolic(respuesta);
                                break;
                            default:
                                break;
                        }  

                        jsp_alerta("A", $("#msg_datos_procesados_ok").val());
                    } else {
                        jsp_alerta("A", $("#msg_datos_procesados_error").val());
                    }
                },
                //dataType: dataType,
                error: function (jqXHR, textStatus, errorThrown) {
                    var mensajeError = 'Se ha presentado un error al procesar los datos. Datos no actualizados. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                    jsp_alerta("A", mensajeError);
                },
                async: false
            });
        }
    } else {
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}

function getIndexSeleccionadoTabla(objetoTabla){
    return ((objetoTabla!==undefined && objetoTabla!==null) ? objetoTabla.selectedIndex : -1);
}

function getIDFromObjetoTablaLista(objetoTablaLista,index){
    return ((objetoTablaLista!==undefined && objetoTablaLista!==null
            && index!==undefined && index!==null && index > -1) ? objetoTablaLista[index][0] : null);
}

function isElmentoTablaSeleccionado(objetoTabla){
    return objetoTabla!==null && objetoTabla!==undefined 
                && getIndexSeleccionadoTabla(objetoTabla)>=0;
}
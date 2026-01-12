
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
// Definicion de Variables para manejo de Tablas
// Tablas Convocatorias Predefinidas Lanbide 
var tabProgramaConvocatoriaEspecifCol1;
var listaProgramaConvocatoriaEspecifCol1 = new Array();
var listaProgramaConvocatoriaEspecifCol1Tabla = new Array();

var tabProgramaConvocatoriaEspecifCol2;
var listaProgramaConvocatoriaEspecifCol2 = new Array();
var listaProgramaConvocatoriaEspecifCol2Tabla = new Array();

var tabProgramaConvocatoriaEspecifCol3;
var listaProgramaConvocatoriaEspecifCol3 = new Array();
var listaProgramaConvocatoriaEspecifCol3Tabla = new Array();

var tabProgramaConvocatoriaEspecifCol4;
var listaProgramaConvocatoriaEspecifCol4 = new Array();
var listaProgramaConvocatoriaEspecifCol4Tabla = new Array();

// Tablas de Actividades Orientacion para el Empleo
var tabActividadesOEmpCol1;
var listaActividadesOEmpCol1 = new Array();
var listaActividadesOEmpCol1Tabla = new Array();

var tabActividadesOEmpCol2;
var listaActividadesOEmpCol2 = new Array();
var listaActividadesOEmpCol2Tabla = new Array();

// Tablas de Actividades Orientacion Profesional
var tabActividadesOProCol3;
var listaActividadesOProCol3 = new Array();
var listaActividadesOProCol3Tabla = new Array();

var tabActividadesOProCol4;
var listaActividadesOProCol4 = new Array();
var listaActividadesOProCol4Tabla = new Array();

// Tablas Otros Programas 
var tabOtrosProgramasCol3;
var listaOtrosProgramasCol3 = new Array();
var listaOtrosProgramasCol3Tabla = new Array();

var tabOtrosProgramasCol4;
var listaOtrosProgramasCol4 = new Array();
var listaOtrosProgramasCol4Tabla = new Array();

// Tablas Ejecucion Programas Especificos
var tabEjecucionProgramasEspeCol3;
var listaEjecucionProgramasEspeCol3 = new Array();
var listaEjecucionProgramasEspeCol3Tabla = new Array();

var tabEjecucionProgramasEspeCol4;
var listaEjecucionProgramasEspeCol4 = new Array();
var listaEjecucionProgramasEspeCol4Tabla = new Array();

// Tablas Programas Convocatorias Ordenes y Decretos
var tabColecOrdenesDecretosCol3;
var listaColecOrdenesDecretosCol3 = new Array();
var listaColecOrdenesDecretosCol3Tabla = new Array();

var tabColecOrdenesDecretosCol4;
var listaColecOrdenesDecretosCol4 = new Array();
var listaColecOrdenesDecretosCol4Tabla = new Array();
  
$(document).ready(function() {
    if ($("#pantallaEditarDatos") !== null && $("#pantallaEditarDatos").val() !== null && $("#pantallaEditarDatos").val() !== undefined
            && $("#pantallaEditarDatos").val() !== "" && $("#pantallaEditarDatos").val() === "S"){
        if("5"===$("#idGrupo").val()){
            $("#divDatosActividades").show();
            $("#divTrayDescripcionGenerico").hide();
        }else{
            $("#divDatosActividades").hide();
            $("#divTrayDescripcionGenerico").show();            
        }
    }else{
        // Asignamos los eventos 
        asignarManejadoresEventos();
        // Mostramos el desplegables si procede
        mostrarListaEntidadesExperienciaAcreditable();
        // Crean Tabla y cargan los datos
        cargarDatosPestanaTrayectoriaAcreditable();
    }
});

function guardarDatosColecConvocatoriasPredefColectivo(idColectivo){
    if(isEntidadSeleccionada()){
        var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'guardarDatosColecConvocatoriasPredefColectivoEntidad';
        dataParameter.codigoGrupo = 2; // Convocatorias Predefinidas
        dataParameter.colectivo = idColectivo;
        dataParameter.identificadorBDGestionar = getIDEntidadSeleccionada();
        dataParameter.colecTrayectoriaEntidadLista = JSON.stringify(prepararGJSONGuardarListaExpAcredConvPredefColectivoEntidad(idColectivo, 2));
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null && "OK" !== respuesta) {
                    jsp_alerta("A", "Los datos Trayectoria Predefinida no se han podido guardar para el Colectivo " + idColectivo + ", si el error persite consulte con soporte.");
                } else {
                    jsp_alerta("A", "Los datos Trayectoria Predefinida Guardados Correctamente para el Colectivo " + idColectivo + ".");
                    recargarDatosPestanaExpAcreditableResumen(); // Js colecGestionExperienciaAcreColResumen.js
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al guardar los datos. Trayectoria Entidad Colectivo ' + idColectivo + '. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }else{
        jsp_alerta("A", "Deber Guardar los datos de la Entidad o Seleccionar una opcion de la lista en caso de asociaciones.");
    }
}

function cargarDatosPestanaTrayectoriaAcreditable(){
    var entidadId = getIDEntidadSeleccionada();
    // Creacion Tablas
    crearTablaProgramaConvocatoriaEspecificaCol(2,1);
    crearTablaProgramaConvocatoriaEspecificaCol(2,2);
    crearTablaProgramaConvocatoriaEspecificaCol(2,3);
    crearTablaProgramaConvocatoriaEspecificaCol(2,4);
    // Carga de Datos en la tablas
    cargarTablasPestanaTrayectoriaPredefColectivoPorEntidad(1,entidadId);
    cargarTablasPestanaTrayectoriaPredefColectivoPorEntidad(2,entidadId);
    cargarTablasPestanaTrayectoriaPredefColectivoPorEntidad(3,entidadId);
    cargarTablasPestanaTrayectoriaPredefColectivoPorEntidad(4,entidadId);
    // Crea y carga la tabala Actividades Orientacion para el Empleo
    crearActualizardatosTablaActividadesOEmpCol1(getDatosExperienciaAcreditableEntidad(entidadId,1,5)); // Colectivo 1 Grupo  5 - Actividades para el empleo
    crearActualizardatosTablaActividadesOEmpCol2(getDatosExperienciaAcreditableEntidad(entidadId,2,5)); // Colectivo 1 Grupo  5 - Actividades para el empleo
    crearActualizardatosTablaColecOrdenesDecretosCol3(getDatosExperienciaAcreditableEntidad(entidadId,3,1)); // Colectivo 3 Grupo  1 - Programas Convocatorias en ordenes y decretos
    crearActualizardatosTablaColecOrdenesDecretosCol4(getDatosExperienciaAcreditableEntidad(entidadId,4,1)); // Colectivo 4 Grupo  1 - Programas Convocatorias en ordenes y decretos
    crearActualizardatosTablaOtrosProgramasCol3(getDatosExperienciaAcreditableEntidad(entidadId,3,3)); // Colectivo 3 Grupo  3 - Otros Programas
    crearActualizardatosTablaOtrosProgramasCol4(getDatosExperienciaAcreditableEntidad(entidadId,4,3)); // Colectivo 4 Grupo  3 - Otros Programas
    crearActualizardatosTablaActividadesOProCol3(getDatosExperienciaAcreditableEntidad(entidadId,3,4)); // Colectivo 3 Grupo  4 - Actividades Orientacion Profesional
    crearActualizardatosTablaActividadesOProCol4(getDatosExperienciaAcreditableEntidad(entidadId,4,4)); // Colectivo 4 Grupo  4 - Actividades Orientacion Profesional
    crearActualizardatosTablaEjecucionProgramasEspeCol3(getDatosExperienciaAcreditableEntidad(entidadId,3,6)); // Colectivo 3 Grupo  6 - Ejecucion Programas Esp.  Ori. Pro.
    crearActualizardatosTablaEjecucionProgramasEspeCol4(getDatosExperienciaAcreditableEntidad(entidadId,4,6)); // Colectivo 4 Grupo  6 - Ejecucion Programas Esp.  Ori. Pro.                        
}

function cargarTablasPestanaTrayectoriaPredefColectivoPorEntidad(idColectivo,idEntidadBD){
    if(idEntidadBD!==null && idEntidadBD!==undefined && idEntidadBD!==""){
        var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'obtenerDatosTrayectoriaColectivoXEntidad';
        dataParameter.identificadorBDGestionar = idEntidadBD;
        dataParameter.codigoGrupo = 2; // Convocatorias Predefinidas
        dataParameter.colectivo = idColectivo;
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                        return value === null ? "" : value;
                    }));
                    
                    for (var i = 0; i < respuesta.length; i++) {
                        var elementoJSON = respuesta[i];
                        if($('[id^="idConvPredf_' + elementoJSON.trayCodColectivo + '_' + elementoJSON.trayIdFkProgConvActGrupo + '_' + elementoJSON.trayIdFkProgConvActSubGrPre + '_"]')!=null)
                            $('[id^="idConvPredf_' + elementoJSON.trayCodColectivo + '_' + elementoJSON.trayIdFkProgConvActGrupo + '_' + elementoJSON.trayIdFkProgConvActSubGrPre + '_"]').data("identificadorBDGestionar", elementoJSON.id);
                        if(elementoJSON.trayTieneExperiencia!==null && elementoJSON.trayTieneExperiencia===1){
                            $('[id^="idConvPredf_'+elementoJSON.trayCodColectivo+'_'+elementoJSON.trayIdFkProgConvActGrupo+'_'+elementoJSON.trayIdFkProgConvActSubGrPre+'_"]').attr("checked","true");
                        }
                    }
                } else {
                    jsp_alerta("A", "Los datos Trayectoria Predefinida para Colectivo "+idColectivo+" no se han podido obtener, si el error persite consulte con soporte.");
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al recuperar los datos. Trayectoria Entidad '+idEntidadBD+' Colectivo '+idColectivo+'. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }else{
        //Reseteamos la tabla 
        crearTablaProgramaConvocatoriaEspecificaCol(2,idColectivo);
    }
}

function crearTablaProgramaConvocatoriaEspecificaCol(codigoGrupo,colectivo){    
    var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'getListaConvocatoriasPredefinidaXGrupoXColectivo';
        dataParameter.codigoGrupo =codigoGrupo;
        dataParameter.colectivo = colectivo;
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                        return value === null ? "" : value;
                    }));
                    if(1===colectivo)
                        crearEstructuraBasicaTablaTrayectoriaPredefColectivo1(respuesta);
                    else if(2===colectivo)
                        crearEstructuraBasicaTablaTrayectoriaPredefColectivo2(respuesta);
                    else if(3===colectivo)
                        crearEstructuraBasicaTablaTrayectoriaPredefColectivo3(respuesta);
                    else if(4===colectivo)
                        crearEstructuraBasicaTablaTrayectoriaPredefColectivo4(respuesta);
                } else {
                    jsp_alerta("A", "Los datos Trayectoria Colectivo "+colectivo+" no se han podido obtener. Tabla no se ha poddo crear.");
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al recuperar los datos. Trayectoria Entidad Colectivo '+colectivo+' - Crear Tabla . ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
}

// Preparacion de Tablas
function crearEstructuraBasicaTablaTrayectoriaPredefColectivo1(respuesta){
    listaProgramaConvocatoriaEspecifCol1 = new Array();
    listaProgramaConvocatoriaEspecifCol1Tabla = new Array();
    $("#listaColecConvocatoriasC1 input[type=checkbox]").remove();
    tabProgramaConvocatoriaEspecifCol1 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecConvocatoriasC1")[0], 750, 10);
    tabProgramaConvocatoriaEspecifCol1.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabProgramaConvocatoriaEspecifCol1.addColumna('50', 'center', $("#lable_tabla_columna_experiencia_previa").val());
    tabProgramaConvocatoriaEspecifCol1.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabProgramaConvocatoriaEspecifCol1.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaProgramaConvocatoriaEspecifCol1[i] = [elementoJSON.id, elementoJSON.idConvocatoriaActiva, elementoJSON.idConvocatoriaActivaDesc, elementoJSON.codigoGrupo, elementoJSON.codigoGrupoDesc, elementoJSON.codigoConvocatoriaPred, elementoJSON.codigoConvocatoriaPredDesc, elementoJSON.colectivo, elementoJSON.colectivoDesc, elementoJSON.fechaInicio, elementoJSON.fechaFin];
            listaProgramaConvocatoriaEspecifCol1Tabla[i] = [elementoJSON.codigoConvocatoriaPredDesc, '<div><input type="checkbox" id="idConvPredf_' + elementoJSON.colectivo + '_' + elementoJSON.codigoGrupo + '_' + elementoJSON.codigoConvocatoriaPred + '_' + elementoJSON.id + '" /></div>', elementoJSON.fechaInicio, elementoJSON.fechaFin];
        }
    }
    tabProgramaConvocatoriaEspecifCol1.displayCabecera = true;
    tabProgramaConvocatoriaEspecifCol1.height = 250;
    tabProgramaConvocatoriaEspecifCol1.lineas = listaProgramaConvocatoriaEspecifCol1Tabla;
    tabProgramaConvocatoriaEspecifCol1.displayTabla();
}

function crearEstructuraBasicaTablaTrayectoriaPredefColectivo2(respuesta){
    listaProgramaConvocatoriaEspecifCol2 = new Array();
    listaProgramaConvocatoriaEspecifCol2Tabla = new Array();
    $("#listaColecConvocatoriasC2 input[type=checkbox]").remove();
    tabProgramaConvocatoriaEspecifCol2 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecConvocatoriasC2")[0], 750, 10);
    tabProgramaConvocatoriaEspecifCol2.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabProgramaConvocatoriaEspecifCol2.addColumna('50', 'center', $("#lable_tabla_columna_experiencia_previa").val());
    tabProgramaConvocatoriaEspecifCol2.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabProgramaConvocatoriaEspecifCol2.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaProgramaConvocatoriaEspecifCol2[i] = [elementoJSON.id, elementoJSON.idConvocatoriaActiva, elementoJSON.idConvocatoriaActivaDesc, elementoJSON.codigoGrupo, elementoJSON.codigoGrupoDesc, elementoJSON.codigoConvocatoriaPred, elementoJSON.codigoConvocatoriaPredDesc, elementoJSON.colectivo, elementoJSON.colectivoDesc, elementoJSON.fechaInicio, elementoJSON.fechaFin];
            listaProgramaConvocatoriaEspecifCol2Tabla[i] = [elementoJSON.codigoConvocatoriaPredDesc, '<div><input type="checkbox" id="idConvPredf_' + elementoJSON.colectivo + '_' + elementoJSON.codigoGrupo + '_' + elementoJSON.codigoConvocatoriaPred + '_' + elementoJSON.id + '" /></div>', elementoJSON.fechaInicio, elementoJSON.fechaFin];
        }
    }
    tabProgramaConvocatoriaEspecifCol2.displayCabecera = true;
    tabProgramaConvocatoriaEspecifCol2.height = 250;
    tabProgramaConvocatoriaEspecifCol2.lineas = listaProgramaConvocatoriaEspecifCol2Tabla;
    tabProgramaConvocatoriaEspecifCol2.displayTabla();
}

function crearEstructuraBasicaTablaTrayectoriaPredefColectivo3(respuesta){
    listaProgramaConvocatoriaEspecifCol3 = new Array();
    listaProgramaConvocatoriaEspecifCol3Tabla = new Array();
    $("#listaColecConvocatoriasC3 input[type=checkbox]").remove();
    tabProgramaConvocatoriaEspecifCol3 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecConvocatoriasC3")[0], 750, 10);
    tabProgramaConvocatoriaEspecifCol3.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabProgramaConvocatoriaEspecifCol3.addColumna('50', 'center', $("#lable_tabla_columna_experiencia_previa").val());
    tabProgramaConvocatoriaEspecifCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabProgramaConvocatoriaEspecifCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaProgramaConvocatoriaEspecifCol3[i] = [elementoJSON.id, elementoJSON.idConvocatoriaActiva, elementoJSON.idConvocatoriaActivaDesc, elementoJSON.codigoGrupo, elementoJSON.codigoGrupoDesc, elementoJSON.codigoConvocatoriaPred, elementoJSON.codigoConvocatoriaPredDesc, elementoJSON.colectivo, elementoJSON.colectivoDesc, elementoJSON.fechaInicio, elementoJSON.fechaFin];
            listaProgramaConvocatoriaEspecifCol3Tabla[i] = [elementoJSON.codigoConvocatoriaPredDesc, '<div><input type="checkbox" id="idConvPredf_' + elementoJSON.colectivo + '_' + elementoJSON.codigoGrupo + '_' + elementoJSON.codigoConvocatoriaPred + '_' + elementoJSON.id + '" /></div>', elementoJSON.fechaInicio, elementoJSON.fechaFin];
        }
    }
    tabProgramaConvocatoriaEspecifCol3.displayCabecera = true;
    tabProgramaConvocatoriaEspecifCol3.height = 250;
    tabProgramaConvocatoriaEspecifCol3.lineas = listaProgramaConvocatoriaEspecifCol3Tabla;
    tabProgramaConvocatoriaEspecifCol3.displayTabla();
}

function crearEstructuraBasicaTablaTrayectoriaPredefColectivo4(respuesta){
    listaProgramaConvocatoriaEspecifCol4 = new Array();
    listaProgramaConvocatoriaEspecifCol4Tabla = new Array();
    $("#listaColecConvocatoriasC4 input[type=checkbox]").remove();
    tabProgramaConvocatoriaEspecifCol4 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecConvocatoriasC4")[0], 750, 10);
    tabProgramaConvocatoriaEspecifCol4.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabProgramaConvocatoriaEspecifCol4.addColumna('50', 'center', $("#lable_tabla_columna_experiencia_previa").val());
    tabProgramaConvocatoriaEspecifCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabProgramaConvocatoriaEspecifCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaProgramaConvocatoriaEspecifCol4[i] = [elementoJSON.id, elementoJSON.idConvocatoriaActiva, elementoJSON.idConvocatoriaActivaDesc, elementoJSON.codigoGrupo, elementoJSON.codigoGrupoDesc, elementoJSON.codigoConvocatoriaPred, elementoJSON.codigoConvocatoriaPredDesc, elementoJSON.colectivo, elementoJSON.colectivoDesc, elementoJSON.fechaInicio, elementoJSON.fechaFin];
            listaProgramaConvocatoriaEspecifCol4Tabla[i] = [elementoJSON.codigoConvocatoriaPredDesc, '<div><input type="checkbox" id="idConvPredf_' + elementoJSON.colectivo + '_' + elementoJSON.codigoGrupo + '_' + elementoJSON.codigoConvocatoriaPred + '_' + elementoJSON.id + '" /></div>', elementoJSON.fechaInicio, elementoJSON.fechaFin];
        }
    }
    tabProgramaConvocatoriaEspecifCol4.displayCabecera = true;
    tabProgramaConvocatoriaEspecifCol4.height = 250;
    tabProgramaConvocatoriaEspecifCol4.lineas = listaProgramaConvocatoriaEspecifCol4Tabla;
    tabProgramaConvocatoriaEspecifCol4.displayTabla();
}

function crearActualizardatosTablaActividadesOEmpCol1(respuesta){
    listaActividadesOEmpCol1 = new Array();
    listaActividadesOEmpCol1Tabla = new Array();
    tabActividadesOEmpCol1 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecActividadesOEmpC1")[0], 850, 10);
    tabActividadesOEmpCol1.addColumna('300', 'left', $("#lable_tabla_columna_nombre_adminpublica").val());
    tabActividadesOEmpCol1.addColumna('300', 'left', $("#lable_tabla_columna_descripcion_actividad").val());
    tabActividadesOEmpCol1.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabActividadesOEmpCol1.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaActividadesOEmpCol1[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,elementoJSON.trayNumeroMeses];
            listaActividadesOEmpCol1Tabla[i] = [elementoJSON.trayNombreAdmonPublica, elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaInicio];
        }
    }
    tabActividadesOEmpCol1.displayCabecera = true;
    tabActividadesOEmpCol1.height = 250;
    tabActividadesOEmpCol1.lineas = listaActividadesOEmpCol1Tabla;
    tabActividadesOEmpCol1.displayTabla();
}

function crearActualizardatosTablaActividadesOEmpCol2(respuesta){
    listaActividadesOEmpCol2 = new Array();
    listaActividadesOEmpCol2Tabla = new Array();
    tabActividadesOEmpCol2 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecActividadesOEmpC2")[0], 850, 10);
    tabActividadesOEmpCol2.addColumna('300', 'left', $("#lable_tabla_columna_nombre_adminpublica").val());
    tabActividadesOEmpCol2.addColumna('300', 'left', $("#lable_tabla_columna_descripcion_actividad").val());
    tabActividadesOEmpCol2.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabActividadesOEmpCol2.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaActividadesOEmpCol2[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,elementoJSON.trayNumeroMeses];
            listaActividadesOEmpCol2Tabla[i] = [elementoJSON.trayNombreAdmonPublica, elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabActividadesOEmpCol2.displayCabecera = true;
    tabActividadesOEmpCol2.height = 250;
    tabActividadesOEmpCol2.lineas = listaActividadesOEmpCol2Tabla;
    tabActividadesOEmpCol2.displayTabla();
}

function crearActualizardatosTablaColecOrdenesDecretosCol3(respuesta){
    listaColecOrdenesDecretosCol3 = new Array();
    listaColecOrdenesDecretosCol3Tabla = new Array();
    tabColecOrdenesDecretosCol3 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecOrdenesDecretosC3")[0], 850, 10);
    tabColecOrdenesDecretosCol3.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabColecOrdenesDecretosCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabColecOrdenesDecretosCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaColecOrdenesDecretosCol3[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,elementoJSON.trayNumeroMeses];
            listaColecOrdenesDecretosCol3Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabColecOrdenesDecretosCol3.displayCabecera = true;
    tabColecOrdenesDecretosCol3.height = 250;
    tabColecOrdenesDecretosCol3.lineas = listaColecOrdenesDecretosCol3Tabla;
    tabColecOrdenesDecretosCol3.displayTabla();
}

function crearActualizardatosTablaColecOrdenesDecretosCol4(respuesta){
    listaColecOrdenesDecretosCol4 = new Array();
    listaColecOrdenesDecretosCol4Tabla = new Array();
    tabColecOrdenesDecretosCol4 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecOrdenesDecretosC4")[0], 850, 10);
    tabColecOrdenesDecretosCol4.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabColecOrdenesDecretosCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabColecOrdenesDecretosCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaColecOrdenesDecretosCol4[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,elementoJSON.trayNumeroMeses];
            listaColecOrdenesDecretosCol4Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabColecOrdenesDecretosCol4.displayCabecera = true;
    tabColecOrdenesDecretosCol4.height = 250;
    tabColecOrdenesDecretosCol4.lineas = listaColecOrdenesDecretosCol4Tabla;
    tabColecOrdenesDecretosCol4.displayTabla();
}

function crearActualizardatosTablaOtrosProgramasCol3(respuesta){
    listaOtrosProgramasCol3 = new Array();
    listaOtrosProgramasCol3Tabla = new Array();
    tabOtrosProgramasCol3 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecOtrosProgramasC3")[0], 850, 10);
    tabOtrosProgramasCol3.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabOtrosProgramasCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabOtrosProgramasCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaOtrosProgramasCol3[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin,elementoJSON.trayNumeroMeses];
            listaOtrosProgramasCol3Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabOtrosProgramasCol3.displayCabecera = true;
    tabOtrosProgramasCol3.height = 250;
    tabOtrosProgramasCol3.lineas = listaOtrosProgramasCol3Tabla;
    tabOtrosProgramasCol3.displayTabla();
}

function crearActualizardatosTablaOtrosProgramasCol4(respuesta) {
    listaOtrosProgramasCol4 = new Array();
    listaOtrosProgramasCol4Tabla = new Array();
    tabOtrosProgramasCol4 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecOtrosProgramasC4")[0], 850, 10);
    tabOtrosProgramasCol4.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabOtrosProgramasCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabOtrosProgramasCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaOtrosProgramasCol4[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin, elementoJSON.trayNumeroMeses];
            listaOtrosProgramasCol4Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabOtrosProgramasCol4.displayCabecera = true;
    tabOtrosProgramasCol4.height = 250;
    tabOtrosProgramasCol4.lineas = listaOtrosProgramasCol4Tabla;
    tabOtrosProgramasCol4.displayTabla();
}

function crearActualizardatosTablaActividadesOProCol3(respuesta) {
    listaActividadesOProCol3 = new Array();
    listaActividadesOProCol3Tabla = new Array();
    tabActividadesOProCol3 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecActividadesOProC3")[0], 850, 10);
    tabActividadesOProCol3.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabActividadesOProCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabActividadesOProCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaActividadesOProCol3[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin, elementoJSON.trayNumeroMeses];
            listaActividadesOProCol3Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabActividadesOProCol3.displayCabecera = true;
    tabActividadesOProCol3.height = 250;
    tabActividadesOProCol3.lineas = listaActividadesOProCol3Tabla;
    tabActividadesOProCol3.displayTabla();
}

function crearActualizardatosTablaActividadesOProCol4(respuesta) {
    listaActividadesOProCol4 = new Array();
    listaActividadesOProCol4Tabla = new Array();
    tabActividadesOProCol4 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecActividadesOProC4")[0], 850, 10);
    tabActividadesOProCol4.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabActividadesOProCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabActividadesOProCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaActividadesOProCol4[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin, elementoJSON.trayNumeroMeses];
            listaActividadesOProCol4Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabActividadesOProCol4.displayCabecera = true;
    tabActividadesOProCol4.height = 250;
    tabActividadesOProCol4.lineas = listaActividadesOProCol4Tabla;
    tabActividadesOProCol4.displayTabla();
}

function crearActualizardatosTablaEjecucionProgramasEspeCol3(respuesta) {
    listaEjecucionProgramasEspeCol3 = new Array();
    listaEjecucionProgramasEspeCol3Tabla = new Array();
    tabEjecucionProgramasEspeCol3 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecEjecucionProgramasEspC3")[0], 850, 10);
    tabEjecucionProgramasEspeCol3.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabEjecucionProgramasEspeCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabEjecucionProgramasEspeCol3.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaEjecucionProgramasEspeCol3[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin, elementoJSON.trayNumeroMeses];
            listaEjecucionProgramasEspeCol3Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabEjecucionProgramasEspeCol3.displayCabecera = true;
    tabEjecucionProgramasEspeCol3.height = 250;
    tabEjecucionProgramasEspeCol3.lineas = listaEjecucionProgramasEspeCol3Tabla;
    tabEjecucionProgramasEspeCol3.displayTabla();
}

function crearActualizardatosTablaEjecucionProgramasEspeCol4(respuesta) {
    listaEjecucionProgramasEspeCol4 = new Array();
    listaEjecucionProgramasEspeCol4Tabla = new Array();
    tabEjecucionProgramasEspeCol4 = new TablaColec(true, $("#descriptor_buscar").val(), $("#descriptor_anterior").val(), $("#descriptor_siguiente").val(), $("#descriptor_mosFilasPag").val(), $("#descriptor_msgNoResultBusq").val(), $("#descriptor_mosPagDePags").val(), $("#descriptor_noRegDisp").val(), $("#descriptor_filtrDeTotal").val(), $("#descriptor_primero").val(), $("#descriptor_ultimo").val(), $("#listaColecEjecucionProgramasEspC4")[0], 850, 10);
    tabEjecucionProgramasEspeCol4.addColumna('300', 'left', $("#lable_tabla_columna_programa_convocatoria").val());
    tabEjecucionProgramasEspeCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_inicio").val());
    tabEjecucionProgramasEspeCol4.addColumna('100', 'center', $("#lable_tabla_columna_fecha_fin").val());

    if (respuesta !== null && respuesta !== undefined) {
        for (var i = 0; i < respuesta.length; i++) {
            var elementoJSON = respuesta[i];
            listaEjecucionProgramasEspeCol4[i] = [elementoJSON.id, elementoJSON.trayCodColectivo, elementoJSON.trayIdFkProgConvActGrupo, elementoJSON.trayIdFkProgConvActSubGrPre, elementoJSON.trayNumExpediente, elementoJSON.trayCodigoEntidad, elementoJSON.trayDescripcion, elementoJSON.trayTieneExperiencia, elementoJSON.trayNombreAdmonPublica, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin, elementoJSON.trayNumeroMeses];
            listaEjecucionProgramasEspeCol4Tabla[i] = [elementoJSON.trayDescripcion, elementoJSON.trayFechaInicio, elementoJSON.trayFechaFin];
        }
    }
    tabEjecucionProgramasEspeCol4.displayCabecera = true;
    tabEjecucionProgramasEspeCol4.height = 250;
    tabEjecucionProgramasEspeCol4.lineas = listaEjecucionProgramasEspeCol4Tabla;
    tabEjecucionProgramasEspeCol4.displayTabla();
}

function prepararGJSONGuardarListaExpAcredConvPredefColectivoEntidad(idColectivo,idGrupo){
    var listaRetorno = new Array();
    $('[id^="idConvPredf_'+idColectivo+'_'+idGrupo+'_"]:checked').each(function (index,element) {
        var dataParameter = $.extend({}, ColecTrayectoriaEntidadJSON);
        dataParameter.id=$(element).data("identificadorBDGestionar");
        dataParameter.trayCodColectivo=idColectivo;
        dataParameter.trayIdFkProgConvActGrupo=idGrupo;
        var elementID = $(element).attr("id");
        elementID = elementID.replace('idConvPredf_'+idColectivo+'_'+idGrupo+'_','');
        dataParameter.trayIdFkProgConvActSubGrPre=elementID.substr(0,elementID.indexOf("_")); 
        dataParameter.trayNumExpediente=$("#numeroExpediente").val();
        dataParameter.trayCodigoEntidad=getIDEntidadSeleccionada();
        dataParameter.trayTieneExperiencia= $(element).prop("checked")===true ? 1 : 0;
        dataParameter.trayNumeroMeses= 0;
        listaRetorno.push(dataParameter);
    });
    return listaRetorno;
}

function prepararGJSONAltaEdicionColecTrayectoriaEntidad(){
        var dataParameter = $.extend({}, ColecTrayectoriaEntidadJSON);
        dataParameter.id=($("#identificadorBDGestionar").val() != null && $("#identificadorBDGestionar").val() !== undefined && $("#identificadorBDGestionar").val()!= "" ? $("#identificadorBDGestionar").val() : null);
        dataParameter.trayCodColectivo=$("#idColectivo").val();
        dataParameter.trayIdFkProgConvActGrupo=$("#idGrupo").val();
        dataParameter.trayNumExpediente=$("#numeroExpediente").val();
        dataParameter.trayCodigoEntidad=$("#idBDEntidad").val();
        dataParameter.trayDescripcion=($("#idGrupo").val()==="5" ? $("#trayDescripcionAct").val() : $("#trayDescripcion").val());
        dataParameter.trayNombreAdmonPublica=$("#trayNombreAdmonPublica").val();
        dataParameter.trayFechaInicio=$("#trayFechaInicio").val();
        dataParameter.trayFechaFin=$("#trayFechaFin").val();
        dataParameter.trayNumeroMeses= calcularMesesEntreFechas(getDateFromStringddMMyyyy($("#trayFechaInicio").val()),getDateFromStringddMMyyyy($("#trayFechaFin").val()));
        return dataParameter;
}

function getIDEntidadSeleccionada(){
    if (!$("#esAsociacionS").prop("checked")) {
        return $("#idBDEntidad").val();
    } else {
        return $("#listaEntidades").val();
    }
}
function getNombreEntidadSeleccionada(){
    if (!$("#esAsociacionS").prop("checked")) {
        return $("#nombreEntidad").val();
    } else {
        return $("#listaEntidades option:selected" ).text();
    }
}

function getCifEntidadSeleccionada(){
    if (!$("#esAsociacionS").prop("checked")) {
        return $("#cifEntidad").val();
    } else {
        return $("#listaEntidades option:selected" ).attr("title");
    }
}

function asignarManejadoresEventos(){
    // Asignamos los eventos a los elementos de la pestana
    if ($("#listaEntidades") !== null && $("#listaEntidades") !== undefined) {
        $("#listaEntidades").change(function () {
            cargarDatosPestanaTrayectoriaAcreditable();
        });
    }
}

/**
 * Muesra la lista desplegable, seleccionando la primera opcion y mostrando los datos de dicha entidad en las pestanas
 * @returns void
 */
function mostrarListaEntidadesExperienciaAcreditable(){
    if (!$("#esAsociacionS").prop("checked")) {
        $("#divListaEntidades").hide();
    } else {
        $("#divListaEntidades").show();
        if ($("#listaEntidades option").size() > 1) {
            $("#listaEntidades").val($($("#listaEntidades option")[1]).val());
            $("#listaEntidades").change();
        } else
            jsp_alerta("A", "Desplegable de Lista de entidades Sin opciones cargadas");
    }
}

function getDatosExperienciaAcreditableEntidad(idEntidadBD,idColectivo,codigoGrupo) {
    var retorno = null;
    var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'obtenerDatosTrayectoriaColectivoXEntidad';
    dataParameter.identificadorBDGestionar = idEntidadBD;
    dataParameter.colectivo = idColectivo;
    dataParameter.codigoGrupo = codigoGrupo;
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                respuesta = JSON.parse(JSON.stringify(respuesta, function (key, value) {
                    return value === null ? "" : value;
                }));
                retorno=respuesta;
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al recuperar los datos. Trayectoria Entidad Colectivo ' + idColectivo + ' - Crear Tabla . ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return retorno;
}

function anadirEditarColecTrayectoriaEntidad(idColectivo,idGrupo,identificadorBDGestionar){
    if(isEntidadSeleccionada()){
        var idBDEntidad = getIDEntidadSeleccionada();
        var cifEntidad = getCifEntidadSeleccionada();
        var nombreEntidad = escape(getNombreEntidadSeleccionada());
        lanzarPopUpModal($("#urlBaseLlamadaM48").val() + "?tarea=preparar&modulo=MELANBIDE48&operacion=cargarAltaEdicionColecTrayectoriaEntidad&tipo=0&numero=" + $("#numeroExpediente").val() + "&codProcedimiento=" + $("#codigoProcedimiento").val() + "&codOrganizacion=" + $("#codigoOrganizacion").val() + "&control=" + new Date().getTime()
                + "&idColectivo=" + idColectivo + "&idGrupo=" + idGrupo + "&idBDEntidad=" + idBDEntidad + "&cifEntidad=" + cifEntidad + "&nombreEntidad=" + nombreEntidad + "&identificadorBDGestionar=" + identificadorBDGestionar
                , 600, 1200, 'no', 'no', function (result) {
                    if (result !== null && result !== undefined) {
                        if (1 === idColectivo && 5 === idGrupo)
                            crearActualizardatosTablaActividadesOEmpCol1(result);
                        else if (2 === idColectivo && 5 === idGrupo)
                            crearActualizardatosTablaActividadesOEmpCol2(result);
                        else if (3 === idColectivo && 1 === idGrupo)
                            crearActualizardatosTablaColecOrdenesDecretosCol3(result);
                        else if (4 === idColectivo && 1 === idGrupo)
                            crearActualizardatosTablaColecOrdenesDecretosCol4(result);
                        else if (3 === idColectivo && 3 === idGrupo)
                            crearActualizardatosTablaOtrosProgramasCol3(result);
                        else if (4 === idColectivo && 3 === idGrupo)
                            crearActualizardatosTablaOtrosProgramasCol4(result);
                        else if (3 === idColectivo && 4 === idGrupo)
                            crearActualizardatosTablaActividadesOProCol3(result);
                        else if (4 === idColectivo && 4 === idGrupo)
                            crearActualizardatosTablaActividadesOProCol4(result);
                        else if (3 === idColectivo && 6 === idGrupo)
                            crearActualizardatosTablaEjecucionProgramasEspeCol3(result);
                        else if (4 === idColectivo && 6 === idGrupo)
                            crearActualizardatosTablaEjecucionProgramasEspeCol4(result);
                        recargarDatosPestanaExpAcreditableResumen(); // Js colecGestionExperienciaAcreColResumen.js
                    }
                });
    }else{
        jsp_alerta("A", "Deber Guardar los datos de la entidad o Seleccionar una opcion de la lista en caso de asociaciones.");
    }
}

function modificarColecTrayectoriaEntidad(idColectivo,idGrupo){
    var index = getIndexSeleccionadoTabla(idColectivo,idGrupo);
    if (index !== -1) {
        anadirEditarColecTrayectoriaEntidad(idColectivo,idGrupo,getIDColecTrayectoriaByIndexFromListaDatosTabla(idColectivo,idGrupo,index));
    } else {
        jsp_alerta("A", $("#msg_msjNoSelecFila").val());
    }
}

function eliminarColecTrayectoriaEntidad(idColectivo,idGrupo){
    var index = getIndexSeleccionadoTabla(idColectivo,idGrupo);
    if (index !== -1) {
        if (confirm("Está seguro de elimiar dicho registro?")) {
            var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
            dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
            dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
            dataParameter.numero = $("#numeroExpediente").val();
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'eliminarDatosColecTrayectoriaEntidad';
            dataParameter.idBDEntidad = getIDEntidadSeleccionada();
            dataParameter.colectivo = idColectivo;
            dataParameter.codigoGrupo = idGrupo;
            dataParameter.identificadorBDGestionar = getIDColecTrayectoriaByIndexFromListaDatosTabla(idColectivo,idGrupo,index);
            $.ajax({
                type: 'POST',
                url: $("#urlBaseLlamadaM48").val(),
                data: dataParameter,
                success: function (respuesta) {
                    if (respuesta !== null && "OK"===respuesta) {
                        if (idColectivo === 1 && idGrupo === 5) {
                            crearActualizardatosTablaActividadesOEmpCol1(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 2 && idGrupo === 5) {
                            crearActualizardatosTablaActividadesOEmpCol2(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 3 && idGrupo === 1) {
                            crearActualizardatosTablaColecOrdenesDecretosCol3(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 4 && idGrupo === 1) {
                            crearActualizardatosTablaColecOrdenesDecretosCol4(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 3 && idGrupo === 3) {
                            crearActualizardatosTablaOtrosProgramasCol3(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 4 && idGrupo === 3) {
                            crearActualizardatosTablaOtrosProgramasCol4(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 3 && idGrupo === 4) {
                            crearActualizardatosTablaActividadesOProCol3(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 4 && idGrupo === 4) {
                            crearActualizardatosTablaActividadesOProCol4(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 3 && idGrupo === 6) {
                            crearActualizardatosTablaEjecucionProgramasEspeCol3(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        } else if (idColectivo === 4 && idGrupo === 6) {
                            crearActualizardatosTablaEjecucionProgramasEspeCol4(getDatosExperienciaAcreditableEntidad(dataParameter.idBDEntidad, idColectivo, idGrupo));
                        }
                        jsp_alerta("A", "Datos Eliminados Correctamente.");
                        recargarDatosPestanaExpAcreditableResumen(); // Js colecGestionExperienciaAcreColResumen.js
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

function guardarDatosColecTrayectoriaEntidad(idColectivo, idGrupo){
    if(datosValidados()){
        var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
        dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
        dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
        dataParameter.numero = $("#numeroExpediente").val();
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'guardarDatosColecTrayectoriaEntidad';
        dataParameter.codigoGrupo = idGrupo;
        dataParameter.colectivo = idColectivo;
        dataParameter.colecTrayectoriaEntidad = JSON.stringify(prepararGJSONAltaEdicionColecTrayectoriaEntidad());
        $.ajax({
            type: 'POST',
            url: $("#urlBaseLlamadaM48").val(),
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null && "OK" !== respuesta) {
                    jsp_alerta("A", "Los datos Trayectoria no se han podido guardar para el Colectivo " + idColectivo + ", si el error persite consulte con soporte.");
                } else {
                    var nuevaLista = getDatosExperienciaAcreditableEntidad($("#idBDEntidad").val(), idColectivo, idGrupo);
                    self.parent.opener.retornoXanelaAuxiliar(nuevaLista);
                    jsp_alerta("A", "Datos Guardados Correctamente para el Colectivo " + idColectivo + ".");
                }
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = 'Se ha presentado un error al guardar los datos. Trayectoria Entidad Colectivo ' + idColectivo + '. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
        });
    }
}

function cancelarEdicionColecTrayectoriaEntidad(){
    limpiarFormularioColecTrayectoriaEntidad();
    window.close();
}

function limpiarFormularioColecTrayectoriaEntidad(){
    $("#idColectivo").val("");
    $("#idGrupo").val("");
    $("#idBDEntidad").val("");
    $("#cifEntidad").val("");
    $("#nombreEntidad").val("");
    $("#trayNombreAdmonPublica").val("");
    $("#trayDescripcionAct").val("");
    $("#trayDescripcion").val("");
    $("#trayFechaInicio").val("");
    $("#trayFechaFin").val("");
}

function mostrarCalendarios(event,nombreIdElementoCal,nombreIdInputTexFecha) { 
    if(window.event) event = window.event;
    showCalendar('forms[0]',nombreIdElementoCal,null,null,null,'',nombreIdInputTexFecha,'',null,null,null,null,null,null,null, '',event);        
}

function ocultarCalendarios(event,nombreIdElementoCal,nombreIdInputTexFecha) { 
    $("#"+nombreIdInputTexFecha).focus();
    ocultarCalendario();
}

function permiteSoloFormatoFechas(e){
    var caracter;
    caracter = e.keyCode;
    //status = caracter;
    if ((caracter > 47 && caracter < 58)
            //||(caracter == 111 ||caracter == 191) 
            || (caracter==47 && contarCaracterBarraFechaEnCadena($("#"+e.target.id).val().toString())<=1)
            ) {
        return true;
    }
    return false;
}

function validarFormatoFecha(input){
   var validado=true;
    if(input!=null && input!=undefined && input.value!=""){
        var arregloDatos = input.value.toString().split("/");
        if(arregloDatos!=null
            && arregloDatos.length===3
            //&& (praseInt(arregloDatos[0])>0 && praseInt(arregloDatos[0])<=31 && arregloDatos[0].length===2) // Dias pendiente mirar dias meses 30 31 29 etc
            //&& (praseInt(arregloDatos[1])>0 && praseInt(arregloDatos[1])<=12 && arregloDatos[1].length===2)
            && (arregloDatos[2].length===4)  //praseInt(arregloDatos[2])>0 && 
           ){
                // Validar Enero 31 Dias, Febrero 28/29
                var fechaValida = new Date(arregloDatos[1]+"/"+arregloDatos[0]+"/"+arregloDatos[2]);
                if(fechaValida!=null && fechaValida!=undefined
                        &&(parseInt(arregloDatos[0])==fechaValida.getDate() && parseInt(arregloDatos[1])==(fechaValida.getMonth()+1))
                     ){
                    // TOdo Correcto
                }else
                    validado=false;
        }else
            validado=false;
    }
    if(!validado){
        jsp_alerta("A","Formato Fecha Invalido. dd/mm/yyyy");
    }
    return validado;
}

function contarCaracterBarraFechaEnCadena(cadena){
    if(cadena!=null && cadena!=undefined && cadena != ""){
            var tamanoCadena = cadena.length;
            var tamanoNuevaCadena = cadena.replace(/\//g,'').length;
            return (tamanoCadena-tamanoNuevaCadena);
    }else
        return 0;
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
        return toD.diff(fromD, 'months');
    }
    return null;
}

/**
 * Evalua si una fecha es mayor que otra. Recibe dos parametros Date. Usa la libreria moment-with-locales.js de https://momentjs.com
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

function getIndexSeleccionadoTabla(idColectivo, idGrupo){
    var index = -1;
    if (idColectivo === 1 && idGrupo === 5) {
        index = tabActividadesOEmpCol1.selectedIndex;
    } else if (idColectivo === 2 && idGrupo === 5) {
        index = tabActividadesOEmpCol2.selectedIndex;
    } else if (idColectivo === 3 && idGrupo === 1) {
        index = tabColecOrdenesDecretosCol3.selectedIndex;
    } else if (idColectivo === 4 && idGrupo === 1) {
        index = tabColecOrdenesDecretosCol4.selectedIndex;
    } else if (idColectivo === 3 && idGrupo === 3) {
        index = tabOtrosProgramasCol3.selectedIndex;
    } else if (idColectivo === 4 && idGrupo === 3) {
        index = tabOtrosProgramasCol4.selectedIndex;
    } else if (idColectivo === 3 && idGrupo === 4) {
        index = tabActividadesOProCol3.selectedIndex;
    } else if (idColectivo === 4 && idGrupo === 4) {
        index = tabActividadesOProCol4.selectedIndex;
    } else if (idColectivo === 3 && idGrupo === 6) {
        index = tabEjecucionProgramasEspeCol3.selectedIndex;
    } else if (idColectivo === 4 && idGrupo === 6) {
        index = tabEjecucionProgramasEspeCol4.selectedIndex;
    }
    return index;
}

function getIDColecTrayectoriaByIndexFromListaDatosTabla(idColectivo, idGrupo, index){
    var identificadorBDGestionar = null;
    if (idColectivo === 1 && idGrupo === 5) {
        if (index !== -1)
            identificadorBDGestionar = listaActividadesOEmpCol1[index][0];
    } else if (idColectivo === 2 && idGrupo === 5) {
        if (index !== -1)
            identificadorBDGestionar = listaActividadesOEmpCol2[index][0];
    } else if (idColectivo === 3 && idGrupo === 1) {
        if (index !== -1)
            identificadorBDGestionar = listaColecOrdenesDecretosCol3[index][0];
    } else if (idColectivo === 4 && idGrupo === 1) {
        if (index !== -1)
            identificadorBDGestionar = listaColecOrdenesDecretosCol4[index][0];
    } else if (idColectivo === 3 && idGrupo === 3) {
        if (index !== -1)
            identificadorBDGestionar = listaOtrosProgramasCol3[index][0];
    } else if (idColectivo === 4 && idGrupo === 3) {
        if (index !== -1)
            identificadorBDGestionar = listaOtrosProgramasCol4[index][0];
    } else if (idColectivo === 3 && idGrupo === 4) {
        if (index !== -1)
            identificadorBDGestionar = listaActividadesOProCol3[index][0];
    } else if (idColectivo === 4 && idGrupo === 4) {
        if (index !== -1)
            identificadorBDGestionar = listaActividadesOProCol4[index][0];
    }else if (idColectivo === 3 && idGrupo === 6) {
        if (index !== -1)
            identificadorBDGestionar = listaEjecucionProgramasEspeCol3[index][0];
    } else if (idColectivo === 4 && idGrupo === 6) {
        if (index !== -1)
            identificadorBDGestionar = listaEjecucionProgramasEspeCol4[index][0];
    }
    return identificadorBDGestionar;
}

function isEntidadSeleccionada(){
    var entidadID = getIDEntidadSeleccionada();
    return entidadID!==null && entidadID!==undefined && entidadID !=="";
}

function datosValidados(){
    var datosOK=true;
    if ("5" === $("#idGrupo").val()) {
        if($("#trayNombreAdmonPublica").val()==="" || $("#trayDescripcionAct").val===""){
            datosOK=false;
        }
    } else {
        if($("#trayDescripcion").val()===""){
            datosOK=false;
        }
    }
    if ($("#trayFechaInicio").val() === "" || $("#trayFechaFin").val()==="") {
        datosOK=false;
    }else{
        if(!isfechaHastaMayorfechaDesde(getDateFromStringddMMyyyy($("#trayFechaInicio").val()),getDateFromStringddMMyyyy($("#trayFechaFin").val()))){
            jsp_alerta("A", "Verifica las fechas Inicio y Fin. Fecha Fin no puede ser anterior a fecha Inicio");
            return false;
        }
    }
    if(!datosOK){
        jsp_alerta("A", "Cumplimente todos los datos");
        return datosOK;
    }
    return datosOK;
}

/**
 * Centraliza los metos que permiten cargar los datos de la pestana, casos en que datos relacionados (P.E. Entidad) se modifiquen en otra pestana.
 * @returns {undefined}
 */
function refrescarPestanaExperienciaAcreditable(){
    // Asignamos los eventos 
    //asignarManejadoresEventos();
    //Leer la lista de entidades y actualizar el desplegables
    cargarDesplegableListaEntidades();
    // Mostramos el desplegables si procede
    mostrarListaEntidadesExperienciaAcreditable();
    // Crean Tabla y cargan los datos
    cargarDatosPestanaTrayectoriaAcreditable();
}

function cargarDesplegableListaEntidades() {
    var dataParameter = $.extend({}, paramLLamadaM48ColecTrayectoriaEntidad);
    dataParameter.codProcedimiento = $("#codigoProcedimiento").val();
    dataParameter.codOrganizacion = $("#codigoOrganizacion").val();
    dataParameter.numero = $("#numeroExpediente").val();
    dataParameter.control = new Date().getTime();
    dataParameter.operacion = 'getListaEntidadesAsociacion';
    dataParameter.identificadorBDGestionar = $("#idBDEntidad").val();
    $.ajax({
        type: 'POST',
        url: $("#urlBaseLlamadaM48").val(),
        data: dataParameter,
        success: function (respuesta) {
            if (respuesta !== null) {
                $("#listaEntidades").empty();
                // Opcion Vacia
                $('#listaEntidades').append($('<option>', {
                    value: "",
                    text: $("#textoDesplegableSeleccionaOpcion").val()
                }));
                $.each(respuesta, function (index, item) {
                    $('#listaEntidades').append($('<option>', {
                        value: item.codEntidad,
                        text: item.nombre,
                        title: item.cif
                    }));
                });
            } else {
                jsp_alerta("A", "No se ha podido recoger los datos la lista de entidades, si el error persite consulte con soporte.");
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

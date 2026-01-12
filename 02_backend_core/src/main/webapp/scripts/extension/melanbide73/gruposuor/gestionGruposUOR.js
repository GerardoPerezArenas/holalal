/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

var parametrosLLamadaM73 = {
    tarea: 'preparar'
    , modulo: 'MELANBIDE73'
    , operacion: null
    , numero: null
    , tipo: 0
    , control: new Date().getTime()
    , tipoOperacion: null
};

$(document).ready(function () {
    cargarCombos();
});

function cargarCombos() {
    comboUsuarios();
    comboGrupos();
    comboUOR();
}

var continuarAdelante = false;

/* Para documentación
 function hola() {
 alert("Evento Change " + $("#codUsuarios").val())
 }
 
 // $('[name="codUsuarios"]').val() 
 */

function comboUsuarios() {

    var listaUsuariosJSON = JSON.parse($("#listaUsuarios").val());
    var variableComboUsuarios = new Combo("Usuarios");
//    variableComboUsuarios.change = hola;
    var lcodUserGA = new Array();
    var ldescUserGA = new Array();

    if (listaUsuariosJSON != null && listaUsuariosJSON.length > 0)
    {
        listaUsuariosJSON.forEach(function (usuario, index) {
            lcodUserGA.push(usuario.codigo);
            ldescUserGA.push(usuario.descripcion);
        });
        variableComboUsuarios.addItems(lcodUserGA, ldescUserGA);
    }
}

function comboGrupos() {
    var listaGruposJSON = JSON.parse($("#listaGrupos").val());
    var variableComboGrupos = new Combo("Grupos");
    variableComboGrupos.change = actualizarTxt;
    var lcodGruposGA = new Array();
    var ldescGruposGA = new Array();

    if (listaGruposJSON != null && listaGruposJSON.length > 0) {
        listaGruposJSON.forEach(function (grupo, index) {
            lcodGruposGA.push(grupo.codigo);
            ldescGruposGA.push(decodeURIComponent(grupo.descripcion));
        });
        variableComboGrupos.addItems(lcodGruposGA, ldescGruposGA);
    }
}

function comboUOR() {
    var listaUORJSON = JSON.parse($("#listaUOR").val());
    var variableComboUOR = new Combo("UOR");
    var lcodUORGA = new Array();
    var ldescUORGA = new Array();

    if (listaUORJSON != null && listaUORJSON.length > 0)
    {
        listaUORJSON.forEach(function (uor, index) {
            lcodUORGA.push(uor.codigo);
            ldescUORGA.push(uor.descripcion);
        });
        variableComboUOR.addItems(lcodUORGA, ldescUORGA);
    }
}

function actualizarTxt() {
    console.log("actualizarTxt");
    $("#nombreGrupoUOR").val($("#descGrupos").val());
    consultarUOR();
}

function pulsarLimpiar() {
    console.log("pulsarLimpiar");
    $("#nombreGrupoUOR").val("");
}

function pulsarAlta() {
    var respuesta;
    if ($("#nombreGrupoUOR").val() == "") {
        alert($("#errorAnadirGrupoUOR").val());
        $("#nombreGrupoUOR").focus();
        return;
    }

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    console.log($("#nombreGrupoUOR").val() + ", " + encodeURIComponent($("#nombreGrupoUOR").val()));
    dataParameter.nombreGrupo = encodeURIComponent($("#nombreGrupoUOR").val());
    dataParameter.operacion = 'insertarGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                if (parseInt(respuesta.toString()) > 0) {
                    actualizarComboGrupos();
                    actualizarElementoSeleccionado(respuesta.toString());
                } else if (parseInt(respuesta.toString()) == -1)
                    alert($("#errorAnadirDuplicadoGrupoUOR").val());
                else if (parseInt(respuesta.toString()) == -2)
                    alert($("#errorNombreGrupoUORDemasiadoLargo").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function pulsarModificar() {
    if ($("#nombreGrupoUOR").val() == "" || $("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false) {
        alert($("#errorModificarNombreGrupoUOR").val());
        $("#nombreGrupoUOR").focus();
        return;
    }

    var respuesta;
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.nombreGrupo = encodeURIComponent($("#nombreGrupoUOR").val());
    dataParameter.operacion = 'modificarGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarModificar respuesta = " + respuesta);
                if (parseInt(respuesta.toString()) > 0) {
                    actualizarComboGrupos();
                    actualizarElementoSeleccionado(respuesta);
                } else if (parseInt(respuesta.toString()) == -1)
                    alert($("#errorAnadirDuplicadoGrupoUOR").val());
                else if (parseInt(respuesta.toString()) == -2)
                    alert($("#errorNombreGrupoUORDemasiadoLargo").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function pulsarEliminar() {
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false) {
        alert($("#errorEliminarGrupoUOR").val());
        return;
    }

    var respuesta;
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.operacion = 'borrarGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarEliminar " + respuesta);
                if (respuesta) {
                    actualizarComboGrupos();
                } else {
                    alert($("#errorEliminarGrupoNoVacioUOR").val());
                }
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function actualizarComboGrupos() {
    var respuesta;
    var primerCodigo = "-1";
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.operacion = 'getGruposUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                lcodGruposGA = new Array();
                ldescGruposGA = new Array();
                primerCodigo = -1;
                respuesta.forEach(function (grupo, index) {
//                    console.log("actualizarComboGrupos: Código " + grupo.codigo + " | Descripción: " + grupo.descripcion);
                    if (index == 0) {
                        primerCodigo = grupo.codigo;
                    }
                    lcodGruposGA.push(grupo.codigo);
                    ldescGruposGA.push(decodeURIComponent(grupo.descripcion));
                });
                var variableComboGrupos = new Combo("Grupos");
                console.log("actualizarComboGrupos primerCodigo=" + primerCodigo);
                if (primerCodigo != -1) {
                    variableComboGrupos.change = actualizarTxt;
                    variableComboGrupos.addItems(lcodGruposGA, ldescGruposGA);
                    actualizarElementoSeleccionado(primerCodigo);
                } else {
                    actualizarElementoSeleccionado("");
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function pulsarAnadirUOR() {
    var respuesta;
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false ||
            $("#codUOR").val() == "" || parseInt($("#codUOR").val()) == false) {
        alert($("#errorAnadirUORaGrupo").val());
        return;
    }

    if (!mostrarAdvertenciaAnadirEliminarUOR())
        return;

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'insertarUOREnGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarAnadirUOR respuesta = " + respuesta)
                if (respuesta)
                    consultarUOR();
                else
                    alert($("#errorAnadirUORExistenteaGrupo").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function pulsarEliminarUOR() {
    var respuesta;
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false ||
            $("#codUOR").val() == "" || parseInt($("#codUOR").val()) == false) {
        alert($("#errorEliminarUORdeGrupo").val());
        return;
    }

    var usuariosAfectados = listaAString(consultarUsuarios(), $("#conjuncion").val());
    if (usuariosAfectados != "") {
        var textoAMostrar = $("#advertenciaEliminarUOR").val()
                + ": " + usuariosAfectados + ", " + $("#respuestaSiNO").val();
        if (!confirm(textoAMostrar))
            return;
    }
    
    if (!mostrarAdvertenciaAnadirEliminarUOR())
        return;

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'eliminarUORDeGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarEliminarUOR respuesta = " + respuesta)
                if (respuesta)
                    consultarUOR();
                else
                    alert($("#errorEliminarUORInexistenteEnGrupo").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function pulsarAltaUsuario() {
    var respuesta;
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false ||
            $("#codUsuarios").val() == "" || parseInt($("#codUsuarios").val()) == false) {
        alert($("#errorAnadirUsuarioaGrupo").val());
        return;
    }

    if (!mostrarAdvertenciaAnadirEliminarUsuario())
        return;

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUsuario = $("#codUsuarios").val();
    dataParameter.operacion = 'insertarUsuarioEnGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarAltaUsuario respuesta = " + respuesta)
                if (parseInt(respuesta) == 1)
                    consultarUsuarios();
                else if (parseInt(respuesta) == 2)
                    alert($("#errorAnadirUsuarioExistenteaGrupo").val());
                else if (parseInt(respuesta) == 3)
                    alert($("#errorAnadirUsuarioaGrupoSinUOR").val());
                else if (parseInt(respuesta) == 4)
                    alert($("#errorAnadirUsuarioaGrupoConOUInactiva").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function pulsarQuitarUsuario() {
    var respuesta;
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false ||
            $("#codUsuarios").val() == "" || parseInt($("#codUsuarios").val()) == false) {
        alert($("#errorEliminarUsuarioDeGrupo").val());
        return;
    }

    var uorsAfectadas = listaAString(consultarUOR(), $("#conjuncion").val());
    if (uorsAfectadas != "") {
        var textoAMostrar = $("#advertenciaQuitarUsuarioGrupoUOR1").val()
                + " " + $("#descUsuarios").val() + " " + $("#advertenciaQuitarUsuarioGrupoUOR2").val() + " " +
                uorsAfectadas + ", " + $("#respuestaSiNO").val();
        if (!confirm(textoAMostrar))
            return;
    }
    
    if (!mostrarAdvertenciaAnadirEliminarUsuario())
        return;

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUsuario = $("#codUsuarios").val();
    dataParameter.operacion = 'eliminarUsuarioDeGrupoUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("pulsarQuitarUsuario respuesta = " + respuesta)
                if (parseInt(respuesta) == 1)
                    consultarUsuarios();
                else if (parseInt(respuesta) == 3)
                    alert($("#errorEliminarUsuarioInexistenteDeGrupo").val());
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
}

function consultarUOR() {
    var respuesta = "";
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false) {
        alert($("#errorEliminarUsuarioDeGrupo").val());
        return respuesta;
    }

    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.operacion = 'getUnidadesOrganicas';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
//                console.log("consultarUOR " + respuesta);
                actualizarListaUOR(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function consultarUsuarios() {
    var respuesta;
    if ($("#codGrupos").val() == "" || parseInt($("#codGrupos").val()) == false) {
        alert($("#errorEliminarUsuarioDeGrupo").val());
        return respuesta;
    }
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.operacion = 'getUsuarios';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("consultarUsuarios");
                actualizarListaUsuarios(respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function actualizarListaUOR(respuesta) {
    $("#divUORS").empty();
    respuesta.forEach(function (uor, index) {
        $("#divUORS").append("<button type='button' id='" + uor.codigo + "' class='list-group-item list-group-item-action' onclick='elementoListaUORs(" + uor.codigo + ");'>" +
                uor.descripcion + "</button>");
    });
    consultarUsuarios();
}

function actualizarListaUsuarios(respuesta) {
    $("#divUsuarios").empty();
    respuesta.forEach(function (usuario, index) {
        $("#divUsuarios").append("<button type='button' id='" + usuario.codigo + "' class='list-group-item list-group-item-action' onclick='elementoListaUsuarios(" + usuario.codigo + ");'>" +
                usuario.descripcion + "</button>");
    });
}

function elementoListaUORs(codigo) {
    $("#codUOR").val(codigo);
    $("#codUOR").focus();
    $("#descUOR").focus();
}

function elementoListaUsuarios(codigo) {
    $("#codUsuarios").val(codigo);
    $("#codUsuarios").focus();
    $("#descUsuarios").focus();
}

function actualizarElementoSeleccionado(codigo) {
    $("#codGrupos").val(codigo.toString());
    $("#codGrupos").focus();
    $("#descGrupos").focus();
}

function getUORsAfectadas() {
    var result = "";
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'getUORsAfectadas';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                result = listaAString(respuesta, $("#conjuncion").val());
                console.log("getUORsAfectadas " + result);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return result;
}

function getGruposUORsAfectados() {
    var result = "";
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'getGruposUORsAfectados';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                result = listaAString(respuesta, $("#conjuncion").val());
                console.log("getGruposUORsAfectados " + result);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return result;
}

function getGruposUORsAfectadosAlAnadirEliminarUOR() {
    var result = "";
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'getGruposUORsAfectadosAlAnadirEliminarUOR';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                result = listaAString(respuesta, $("#conjuncion").val());
                console.log("getGruposUORsAfectadosAlAnadirEliminarUOR " + result);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return result;
}

function existeUOREnOtroGrupo() {
    var respuesta = false;
    var dataParameter = $.extend({}, parametrosLLamadaM73);
    dataParameter.idGrupo = $("#codGrupos").val();
    dataParameter.idUOR = $("#codUOR").val();
    dataParameter.operacion = 'existeUOREnOtroGrupo';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'GET',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if (response != null) {
                respuesta = JSON.parse(response);
                console.log("existeUOREnOtroGrupo " + respuesta);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = 'Se ha presentado un error al rescatar el código del procedimiento. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    return respuesta;
}

function listaAString(lista, conjuncion) {
    var result = "";

    if (lista.length == 1) {
        result += decodeURIComponent(lista[0].descripcion);
    } else {
        for (var i = 0; i < lista.length; i++) {
            if (i < lista.length - 2)
                result += decodeURIComponent(lista[i].descripcion) + ", ";
            else if (i < lista.length - 1)
                result += decodeURIComponent(lista[i].descripcion) + " ";
            else
                result += conjuncion + " " + decodeURIComponent(lista[i].descripcion);
        }
    }
    return result;
}

function mostrarAdvertenciaAnadirEliminarUOR() {
    var result = true;
    if ($("#divUsuarios").contents().length > 0) {
        resultado = existeUOREnOtroGrupo();
        if (!resultado)
            result = true;
        else {
            var gruposAfectados = getGruposUORsAfectadosAlAnadirEliminarUOR();
            var textoAMostrar = $("#advertenciaGrupoAnadirEliminarUOR").val()
                    + ": " + gruposAfectados + ". " + $("#respuestaSiNO").val();
            result = confirm(textoAMostrar);
        }
    }
    console.log("mostrarAdvertenciaAnadirEliminarUOR " + result + ", " + $("#divUsuarios").contents().length);
    return result;
}

function mostrarAdvertenciaAnadirEliminarUsuario() {
    var result = true;
    if ($("#divUORS").contents().length > 0) {
        var uorsAfectadas = getUORsAfectadas();
        if (uorsAfectadas != "") {
            var gruposAfectados = getGruposUORsAfectados();
            var textoAMostrar = $("#advertenciaUor").val()
                    + ": " + uorsAfectadas + ", " + $("#advertenciaGrupo").val()
                    + ": " + gruposAfectados + ". " + $("#respuestaSiNO").val();
            result = confirm(textoAMostrar);
        }
    }
    console.log("mostrarAdvertenciaAnadirEliminarUsuario " + result + ", " + $("#divUORS").contents().length);
    return result;
}
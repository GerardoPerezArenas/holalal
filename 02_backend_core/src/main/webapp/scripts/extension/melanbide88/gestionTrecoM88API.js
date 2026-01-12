/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


// Variables Generales

var PESTANA_SOCIOS=1;
var PESTANA_INVERSIONES=2;
var PESTANA_SUBVENCIONES=3;

var parametrosLLamadaM88={
        tarea:'preparar'
        ,modulo:'MELANBIDE88'
        ,operacion:null
        ,numero:null
        ,tipo:0
        ,control:new Date().getTime()
        ,tipoOperacion:null
        ,codOrganizacion:null
        ,codProcedimiento:null
        ,jsonMelanbide88Socios:null
        ,jsonMelanbide88Inversiones:null
        ,jsonMelanbide88Subsolic:null
        ,identificadorBDGestionar:null
        
    };

var jsonMelanbide88Socios ={
    id:null
    ,num_exp:null
    ,dniNieSocio:null
    ,nombreSocio: null
    ,apellido1Socio:null
    ,apellido2Socio:null
    ,fechaAlta:null
};

var jsonMelanbide88Inversiones ={
    id: null
    ,num_exp: null
    ,fecha: null
    ,numFactura: null
    ,descripcion: null
    ,nombProveedor: null
    ,importe: null  
    ,pagada: null  
    ,fechaPago: null  
    ,fechaAlta: null  
};

var jsonMelanbide88Subsolic ={
    id: null
    ,num_exp: null
    ,estado: null
    ,organismo: null
    ,objeto: null
    ,importe: null  
    ,fecha: null  
    ,fechaAlta: null  
};



function mostrarCalendarios(event, nombreIdElementoCal, nombreIdInputTexFecha) {
    if (window.event)
        event = window.event;
    showCalendar('forms[0]', nombreIdElementoCal, null, null, null, '', nombreIdInputTexFecha, '', null, null, null, null, null, null, null, '', event);
}

function ocultarCalendarios(event, nombreIdElementoCal, nombreIdInputTexFecha) {
    $("#" + nombreIdInputTexFecha).focus();
    ocultarCalendario();
}

function permiteSoloFormatoFechas(e) {
    var caracter;
    caracter = e.keyCode;
    //status = caracter;
    if ((caracter > 47 && caracter < 58)
            //||(caracter == 111 ||caracter == 191) 
            || (caracter == 47 && contarCaracterBarraFechaEnCadena($("#" + e.target.id).val().toString()) <= 1)
            ) {
        return true;
    }
    return false;
}

function validarFormatoFecha(input) {
    var validado = true;
    if (input != null && input != undefined && input.value != "") {
        var arregloDatos = input.value.toString().split("/");
        if (arregloDatos != null
                && arregloDatos.length === 3
                //&& (praseInt(arregloDatos[0])>0 && praseInt(arregloDatos[0])<=31 && arregloDatos[0].length===2) // Dias pendiente mirar dias meses 30 31 29 etc
                //&& (praseInt(arregloDatos[1])>0 && praseInt(arregloDatos[1])<=12 && arregloDatos[1].length===2)
                && (arregloDatos[2].length === 4)  //praseInt(arregloDatos[2])>0 && 
                ) {
            // Validar Enero 31 Dias, Febrero 28/29
            var fechaValida = new Date(arregloDatos[1] + "/" + arregloDatos[0] + "/" + arregloDatos[2]);
            if (fechaValida != null && fechaValida != undefined
                    && (parseInt(arregloDatos[0]) == fechaValida.getDate() && parseInt(arregloDatos[1]) == (fechaValida.getMonth() + 1))
                    ) {
                // TOdo Correcto
            } else
                validado = false;
        } else
            validado = false;
    }
    if (!validado) {
        jsp_alerta("A", "Formato Fecha Invalido/Data baliogabearen formatua. dd/mm/yyyy");
    }
    return validado;
}

function contarCaracterBarraFechaEnCadena(cadena) {
    if (cadena != null && cadena != undefined && cadena != "") {
        var tamanoCadena = cadena.length;
        var tamanoNuevaCadena = cadena.replace(/\//g, '').length;
        return (tamanoCadena - tamanoNuevaCadena);
    } else
        return 0;
}



function validarCIF(cif)
{
    var par = 0;
    var non = 0;
    var letras = "ABCDEFGHJKLMNPQRSUVW";
    var letrasInicio = "KPQS";
    var letrasFin = "ABEH";
    var letrasPosiblesFin = "JABCDEFGHI";
    var let = cif.charAt(0).toUpperCase();
    var caracterControl;
    var zz;
    var nn;
    var parcial;
    var control;


    if (cif.length != 9)
    {
        return false;
    } else {
        caracterControl = cif.charAt(8).toUpperCase();
    }

    for (zz = 2; zz < 8; zz += 2)
    {
        par = par + parseInt(cif.charAt(zz));
    }

    for (zz = 1; zz < 9; zz += 2)
    {
        nn = 2 * parseInt(cif.charAt(zz));
        if (nn > 9)
            nn = 1 + (nn - 10)
        non = non + nn
    }

    parcial = par + non;
    control = (10 - (parcial % 10));
    if (control == 10)
        control = 0;


    /*
     * El valor del último carácter:
     * Será una LETRA si la clave de entidad es K, P, Q ó S.
     * Será un NUMERO si la entidad es A, B, E ó H.
     * Para otras claves de entidad: el dígito podrá ser tanto número como letra.
     * */

    if (letrasInicio.indexOf(let) != -1) {
        return (letrasPosiblesFin.charAt(control) == caracterControl);
    } else if (letrasFin.indexOf(let) != -1) {
        return (caracterControl == control);
    } else if (letras.indexOf(let) != -1) {
        return ((letrasPosiblesFin.charAt(control) == caracterControl) || (caracterControl == control));
    } else {
        return false;
    }
}

function validarNif(dni) {
    var LONGITUD = 9;
    var exito = false;
    var numero;
    var let;
    var letra;

    if (dni != null && dni.length == LONGITUD) {
        numero = dni.substr(0, dni.length - 1);
        let = dni.substr(dni.length - 1, 1);
        numero = numero % 23;
        letra = 'TRWAGMYFPDXBNJZSQVHLCKET';
        letra = letra.substring(numero, numero + 1);

        if (letra.toUpperCase() == let.toUpperCase())
            exito = true;
    }//if

    return exito;
}


function validarNie(documento)
{
    var LONGITUD = 9;

    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.
    if (documento.length != LONGITUD) {
        return false;
    }

    // Comprobas que el formato se corresponde con el de un NIE
    var primeraLetra = documento.substring(0, 1).toUpperCase();
    var numero = documento.substring(1, 8);
    var ultimaLetra = documento.substring(8, 9).toUpperCase();
    if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {
        return false;
    }

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra == "Y")
        numero = parseInt(numero, 10) + 10000000;
    else if (primeraLetra == "Z")
        numero = parseInt(numero, 10) + 20000000;
    else if (primeraLetra != "X") {
        return false;
    }

    // Validamos el caracter de control.
    var letraCorrecta = getLetraNif(numero);
    if (ultimaLetra.toUpperCase() != letraCorrecta.toUpperCase()) {
        return false;
    }
    return true;
}

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}

function getTextoOrCastNullOrUndefinedToVacio(texto){
    if(texto===null || texto === undefined || texto === "null" )
        return "";
    else return texto;
}
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function elementoVisible(valor, idBarra) {
    if (valor == 'on') {
        document.getElementById(idBarra).style.visibility = 'inherit';
    } else if (valor == 'off') {
        document.getElementById(idBarra).style.visibility = 'hidden';
    }
}

function cerrarVentana() {
    if (navigator.appName == "Microsoft Internet Explorer") {
        window.parent.window.opener = null;
        window.parent.window.close();
    } else if (navigator.appName == "Netscape") {
        top.window.opener = top;
        top.window.open('', '_parent', '');
        top.window.close();
    } else {
        window.close();
    }
}
function reemplazarPuntos(campo) {
    try {
        var valor = campo.value;
        if (valor != null && valor != '') {
            valor = valor.replace(/\./g, ',');
            campo.value = valor;
        }
    } catch (err) {
    }
}


function reemplazarComas(campo) {
    try {
        var valor = campo.value;
        if (valor != null && valor != '') {
            valor = valor.replace(/\,/g, '.');
            campo.value = valor;
        }
    } catch (err) {
    }
}
// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD_MM_YY,DD_MM_YYYY,DD-MM-YY,DD-MM-YYYY.
function SoloCaracteresFecha(objeto) {

    var valores = '0123456789/_-.';
    xAMayusculas(objeto);

    if (objeto) {
        var original = objeto.value;
        var salida = "";
        for (i = 0; i < original.length; i++) {
            if (valores.indexOf(original.charAt(i).toUpperCase()) != -1) {
                salida = salida + original.charAt(i);
            }
        }

        objeto.value = salida.toUpperCase();
    }
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
        jsp_alerta("A", "Formato Fecha Invalido. dd/mm/yyyy");
    }
    return validado;
}


function mostrarCalfechaNacimiento(event) {
    if (window.event)
        event = window.event;
    if (document.getElementById("calfechaNacimiento").src.indexOf("icono.gif") != -1)
        showCalendar('forms[0]', 'fechaNacimiento', null, null, null, '', 'calfechaNacimiento', '', null, null, null, null, null, null, null, '', event);
}

function ValidarFechaConFormato(fecha, formato) {
    if (formato == null)
        formato = "dd/mm/yyyy";
    if (formato == "mm/yyyy")
        fecha = "01/" + fecha;
    else if (formato == "yyyy")
        fecha = "01/01/" + fecha;
    else if (formato == "mmyyyy")
        fecha = "01" + fecha;

    var D = DataValida(fecha);
    if (formato == "dd/mm/yyyy")
        D[1] = D[0] ? D[1].ISOlocaldateStr() : fecha;
    else if (formato == "mm/yyyy")
        D[1] = D[0] ? D[1].ISOlocaldateStr().substring(3) : fecha;
    else if (formato == "yyyy")
        D[1] = D[0] ? D[1].ISOlocaldateStr().substring(6) : fecha;
    else if (formato == "mmyyyy")
        D[1] = D[0] ? D[1].ISOlocaldateStr().substring(3) : fecha;
    return D;
}


function DataValida(Q) {
    var Mon, x, Rex, B, Y, ND = 0
    Q = Q.trim()
    var separador = ' _-./'
    F = 0
    for (i = 0; i < Q.length; i++)
    {
        var c = Q.charAt(i);
        if (separador.indexOf(c) != -1)
            F = 2;
    }

    if (F == 0) {
        Rex = /(\d{2})(\d{2})(\d+)$/     // D5+ as Y+MMDD
        Q = (Q.search(Rex) == -1 ? '' : Q.replace(Rex, '$3 $2 $1')) // split
    } // optional paragraph

    Rex = /^(\d+)\D+(\d+)\D+(\d+)$/ // three digit fields
    if (F == 2)
        Q = Q.replace(Rex, '$3 $2 $1') // EU

    B = Rex.test(Q) // Split into $1 $2 $3

    if (B)
        with (RegExp) {
            Y = +$1
            if (Y < 100)
                Y += (Y < 60 ? 2000 : 1900)      // optional century line
            with (ND = new Date(Y, $2 - 1, $3))
                B = ((getMonth() == $2 - 1) && (getDate() == $3))
        } // test Y M D valid
    return [B, ND] // [Valid, DateObject]
} // end DataValida

var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

function validarDniNie(campo) {
    const dniNie = campo.value.toUpperCase(); // Convertir a mayúsculas
    const longitud = dniNie.length;
    const letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"; // Letras válidas para DNI/NIE

    // Validar longitud
    if (longitud !== 9) {
        return false; // Longitud inválida
    }

    const letra = dniNie.charAt(8); // Último carácter como letra
    const numeros = dniNie.substring(0, 8); // Primeros 8 caracteres como números

    // Validar NIE (empieza con X, Y, Z)
    if (/^[XYZ]/.test(dniNie)) {
        const prefijo = dniNie.charAt(0);
        const numeroConvertido = numeros
                .replace("X", "0")
                .replace("Y", "1")
                .replace("Z", "2");

        if (!/^[0-9]{8}$/.test(numeroConvertido)) {
            return false; // Los caracteres después del prefijo no son números válidos
        }

        const posicion = parseInt(numeroConvertido, 10) % 23;
        return letra === letrasDni.charAt(posicion); // Comparar letra calculada con la proporcionada
    }

    // Validar DNI (8 dígitos + letra)
    if (/^[0-9]{8}[A-Z]$/.test(dniNie)) {
        const posicion = parseInt(numeros, 10) % 23;
        return letra === letrasDni.charAt(posicion); // Comparar letra calculada con la proporcionada
    }

    // Si no cumple con ninguno, no es válido
    return false;
}


function validarNumericoPorcentajeCien(numero) {
    try {
        if (numero < 0 || numero > 100) {
            return false;
        } else {
            return true;
        }
    } catch (err) {
        return false;
    }
}
// pasar .value
function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
    try {
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if (Trim(numero) != '') {
            var valor = numero;
            var pattern = '^[-]?[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
            var regex = new RegExp(pattern);
            var result = regex.test(valor);
            return result;
        } else {
            return true;
        }
    } catch (err) {
        return false;
    }
}

function contarCaracterBarraFechaEnCadena(cadena) {
    if (cadena != null && cadena != undefined && cadena != "") {
        var tamanoCadena = cadena.length;
        var tamanoNuevaCadena = cadena.replace(/\//g, '').length;
        return (tamanoCadena - tamanoNuevaCadena);
    } else
        return 0;
}
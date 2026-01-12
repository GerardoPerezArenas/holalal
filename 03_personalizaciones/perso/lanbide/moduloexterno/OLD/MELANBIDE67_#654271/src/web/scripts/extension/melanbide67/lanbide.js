// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD_MM_YY,DD_MM_YYYY,DD-MM-YY,DD-MM-YYYY.
function SoloCaracteresFechaLanbide(objeto) {
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

function SoloDigitosNumericosLanbide(e) {
    var tecla, caracter;
    var numeros = "0123456789";
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == null)
        return true;
    if ((tecla == 0) || (tecla == 8))
        return true;
    caracter = String.fromCharCode(tecla);
    if (numeros.indexOf(caracter) != -1) {
        return true;
    } else {
        return false;
    }
}

function SoloDigitosDecimalesLanbide(e) {
    var tecla, caracter;
    var numeros = "0123456789,";
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla == null)
        return true;
    if ((tecla == 0) || (tecla == 8))
        return true;
    caracter = String.fromCharCode(tecla);
    if (numeros.indexOf(caracter) != -1) {
        if (caracter == ",") {
            var target;
            if (e.target)
                target = e.target;
            else
            if (e.srcElement)
                target = e.srcElement;
            var dato = target.value;
            if (dato != "" && dato.length > 0 && contarComasDecimales(dato) == 0)
                return true;
            else
                return false;
        } else
            return true;
    } else {
        return false;
    }
}

function contarComasDecimales(dato) {
    var contador = 0;
    for (i = 0; dato != null && dato != undefined && i < dato.length; i++) {
        if (dato.charAt(i) == ",") {
            contador++;
        }
    }
    return contador;
}

function validarFechaAnterior(fechaAnterior, fechaPosterior, mensaje) {
    //Suponiendo que ambas son validas
    var dia1 = fechaAnterior.substring(0, 2);
    var mes1 = fechaAnterior.substring(3, 5);
    var anho1 = fechaAnterior.substring(6);
    var dia2 = fechaPosterior.substring(0, 2);
    var mes2 = fechaPosterior.substring(3, 5);
    var anho2 = fechaPosterior.substring(6);
    var fecha1 = new Date(anho1, (mes1 - 1), dia1);
    var fecha2 = new Date(anho2, (mes2 - 1), dia2);
    if (fecha1 > fecha2) {
        jsp_alerta("A", mensaje);
        return false;
    }
    return true;
}

function ValidarFechaConFormatoLanbide(fecha, formato) {
    if (formato == null)
        formato = "dd/mm/yyyy";
    if (formato == "mm/yyyy")
        fecha = "01/" + fecha;
    else if (formato == "yyyy")
        fecha = "01/01/" + fecha;
    else if (formato == "mmyyyy")
        fecha = "01" + fecha;
    var D = DataValidaLanbide(fecha);
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

function DataValidaLanbide(Q) {
    var Mon, x, Rex, B, Y, ND = 0;
    Q = Q.trim();
    var separador = ' _-./';
    F = 0;
    for (i = 0; i < Q.length; i++) {
        var c = Q.charAt(i);
        if (separador.indexOf(c) != -1)
            F = 2;
    }
    if (F == 0) {
        Rex = /(\d{2})(\d{2})(\d+)$/;    // D5+ as Y+MMDD
        Q = (Q.search(Rex) == -1 ? '' : Q.replace(Rex, '$3 $2 $1')); // split
    } // optional paragraph

    Rex = /^(\d+)\D+(\d+)\D+(\d+)$/; // three digit fields
    if (F == 2) {
        Q = Q.replace(Rex, '$3 $2 $1'); // EU
    }

    B = Rex.test(Q); // Split into $1 $2 $3

    if (B)
        with (RegExp) {
            Y = +$1;
            if (Y < 100)
                Y += (Y < 60 ? 2000 : 1900);     // optional century line
            with (ND = new Date(Y, $2 - 1, $3))
                B = ((getMonth() == $2 - 1) && (getDate() == $3));
        } // test Y M D valid
    return [B, ND]; // [Valid, DateObject]
} // end DataValida

function validarFechaAnteriorLanbide(fechaAnterior, fechaPosterior, mensaje) {
    // Suponiendo que ambas son validas
    var dia1 = fechaAnterior.substring(0, 2);
    var mes1 = fechaAnterior.substring(3, 5);
    var anho1 = fechaAnterior.substring(6);
    var dia2 = fechaPosterior.substring(0, 2);
    var mes2 = fechaPosterior.substring(3, 5);
    var anho2 = fechaPosterior.substring(6);
    var fecha1 = new Date(anho1, (mes1 - 1), dia1);
    var fecha2 = new Date(anho2, (mes2 - 1), dia2);
    if (fecha1 > fecha2) {
        jsp_alerta("A", mensaje);
        return false;
    }
    return true;
}

function redondearDecimales(dato) {
    var origen = parseFloat(dato);
    return Math.round(origen * 100) / 100;
}

function formatearFloat(numero) {
    if (numero != null) {
        numero = "" + redondearDecimales(numero);
        numero = numero.replace(".", ",");
    } else
        numero = "";

    return numero;
}

// Función para formatear números decimales
// Pone una coma y un cero en caso de que no los haya,
// Pensada para llamar al onblur de un campo de texto pasándole this como parámetro
// campolectura = true, -> Es un campo de solo lectura
function formatNumeroDecimal(valor) {
    var salida;
    if (valor != "") {
        // si no hay puntos ni comas
        if ((valor.indexOf(".") == -1) && (valor.indexOf(",") == -1)) {
            salida = processIntPart(valor);
            salida = salida + ",00";
        }

        // si hay punto pero no hay coma
        if ((valor.indexOf(".") != -1) && (valor.indexOf(",") == -1)) {
            pos = valor.lastIndexOf(".");
            intPart = processIntPart(valor.substring(0, pos));
            decPart = processDecPart(valor.substring(pos + 1, valor.length));
            if (decPart != null && decPart != "" && decPart.length == 1) {
                decPart = decPart + "0";
            }
            salida = intPart + "," + decPart;
        }
        // si hay coma pero no hay punto
        if ((valor.indexOf(".") == -1) && (valor.indexOf(",") != -1)) {
            pos = valor.lastIndexOf(",");
            intPart = processIntPart(valor.substring(0, pos));
            decPart = processDecPart(valor.substring(pos + 1, valor.length));
            if (decPart == 0) {
                decPart = "00";
            }
            salida = intPart + "," + decPart;
        }
        // si hay punto pero no hay coma
        if ((valor.indexOf(".") != -1) && (valor.indexOf(",") == -1)) {
            pos = valor.lastIndexOf(".");
            intPart = processIntPart(valor.substring(0, pos));
            if (valor.substring(pos + 1, valor.length) != "00") {
                decPart = processDecPart(valor.substring(pos + 1, valor.length));
                if (decPart == 0) {
                    decPart = "00";
                }
                if (decPart != null && decPart != "" && decPart.length == 1)
                    decPart = decPart + "0";
                salida = intPart + "," + decPart;
            } else {
                salida = intPart;
            }
        }
        // si hay puntos y comas
        if ((valor.indexOf(".") != -1) && (valor.indexOf(",") != -1)) {
            posComa = valor.lastIndexOf(",");
            posPunt = valor.lastIndexOf(".");
            pos = posComa > posPunt ? posComa : posPunt;
            intPart = processIntPart(valor.substring(0, pos));
            decPart = processDecPart(valor.substring(pos + 1, valor.length));
            salida = intPart + "," + decPart;
        }
    }
    return salida;
}

function processIntPart(valor) {
    if (valor == "") {
        valor = "0";
    }
// quitar los puntos y comas
    valor = trimPointers(valor);
// quitar los ceros a la izquierda
    valor = trimLeftZeroes(valor);
// añadir los puntos dando la vuelta
    var valor1 = "";
    var cont = 0;
    for (n = valor.length; n >= 0; n--) {
        valor1 += valor.charAt(n);
        if (cont == 3) {
            cont = 0;
            valor1 += ".";
        }
        cont++;
    }
// dar la vuelta
    valor1 = reverse(valor1);
// si hay un punto inicial lo quitamos
    if (valor1.charAt(0) == ".") {
        valor1 = valor1.substring(1, valor1.length);
    }
    return valor1;
}

function trimPointers(valor) {
    var valor1 = "";
    for (n = 0; n < valor.length; n++) {
        if ((valor.charAt(n) != ".") && (valor.charAt(n) != ",")) {
            valor1 += valor.charAt(n);
        }
    }
    return valor1;
}

function trimLeftZeroes(valor) {
    var valor1 = "";
    var trimer = true;
    for (n = 0; n < valor.length; n++) {
        if (!((valor.charAt(n) == "0") && (trimer))) {
            valor1 += valor.charAt(n);
            trimer = false;
        }
    }
    if (valor1 == "") {
        valor1 = "0";
    }
    return valor1;
}

function processDecPart(valor) {
    if (valor == "") {
        valor = "0";
    }
    valor = trimPointers(valor);
    valor = trimRightZeroes(valor);
    // Trunco a dos decimales
    if (valor.length > 2) {
        valor = valor.substring(0, 2);
    }
    return valor;
}

function trimRightZeroes(valor) {
    var valor1 = "";
    var trimer = true;
    for (n = valor.length - 1; n >= 0; n--) {
        if (!((valor.charAt(n) == "0") && (trimer))) {
            valor1 += valor.charAt(n);
            trimer = false;
        }
    }
    valor1 = reverse(valor1);
    if (valor1 == "") {
        valor1 = "0";
    }
    return valor1;
}

function SoloDigitosNumericosDecimalesLanbide(objeto) {
    var tecla = "";
    if (event.keyCode) {
        tecla = event.keyCode;
    } else {
        tecla = event.which;
    }
    if (tecla != 9) {     //NO ES EL TABULADOR
        xAMayusculas(objeto);
        var numeros = "0123456789,";
        if (objeto) {
            var original = objeto.value;
            var salida = "";
            for (i = 0; original != undefined && i < original.length; i++) {
                if (numeros.indexOf(original.charAt(i).toUpperCase()) != -1 || numeros.indexOf(original.charAt(i).toUpperCase()) != -1) {
                    salida = salida + original.charAt(i);
                }
            }
            objeto.value = salida.toUpperCase();
        }//if
    } else {
        objeto.select();
    }
}
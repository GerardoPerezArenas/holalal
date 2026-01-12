
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

//function reemplazarComas(campo) {
//    try {
//        var valor = campo.value;
//        if (valor != null && valor != '') {
//            valor = valor.replace(/\,/g, '.');
//            campo.value = valor;
//        }
//    } catch (err) {
//    }
//}

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
     var Mon, x, Rex, B, Y, ND=0
     Q=Q.trim()
     var separador = ' _-./'
     F=0
     for (i = 0; i < Q.length; i++)
     {
         var c = Q.charAt(i);
         if (separador.indexOf(c) != -1)  F=2;
     }

     if (F==0) { Rex = /(\d{2})(\d{2})(\d+)$/     // D5+ as Y+MMDD
      Q = (Q.search(Rex)==-1 ? '' : Q.replace(Rex, '$3 $2 $1') ) // split
      } // optional paragraph

     Rex = /^(\d+)\D+(\d+)\D+(\d+)$/ // three digit fields
     if (F==2) Q = Q.replace(Rex, '$3 $2 $1') // EU

     B = Rex.test(Q) // Split into $1 $2 $3

     if (B) with (RegExp) { Y = +$1
      if (Y<100) Y += (Y<60?2000:1900)      // optional century line
      with (ND = new Date(Y, $2-1, $3))
       B = ((getMonth()==$2-1) && (getDate()==$3))  } // test Y M D valid
     return [B, ND] // [Valid, DateObject]
 }

var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

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


/**
 * Metodo que formatea con separador Miles (.) y decimal(,) 
 * el valor de un numero
 * */
function formatNumero(numero) {
    if (numero != null && numero != undefined && numero != "") {
        if (isNaN(numero)) {
            numero = numero.replace(/[^\d\.,]*/g, '');
            numero = numero.replace(/\./g, '').replace(/\,/g, '.');
        }
        numero = parseFloat(numero).toFixed(2);

        var parteEnteraFormat = (numero.toString().split('.')[0] != null && numero.toString().split('.')[0] != undefined ? numero.toString().split('.')[0].replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.') : "");
        var parteDecimal = (numero.toString().split('.')[1] != null && numero.toString().split('.')[1] != undefined ? numero.toString().split('.')[1] : (parteEnteraFormat != null && parteEnteraFormat != undefined && parteEnteraFormat != "" ? "00" : ""));
        numero = parteEnteraFormat.concat(",", parteDecimal);
        return numero;
    }
    return "";
}

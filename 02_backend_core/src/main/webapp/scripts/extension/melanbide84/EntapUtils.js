/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

function validarNif(campo) {
    var letra;
    var aux;
    var posicion;
    var longitud;
    var dni = campo.value;
    longitud = dni.length;
    aux = dni.substring(longitud - 1).toUpperCase();
    if ((longitud >= 9) && (longitud < 10)) {
        if (isNaN(aux)) {
            posicion = dni.substring(0, longitud - 1) % 23;
            letra = cadena.charAt(posicion);
            if (isNaN(dni.substring(0, longitud - 1))) {
                //return false;//PUEDE Q NIE
                var temp = dni.toUpperCase();
                var cadenadni = "TRWAGMYFPDXBNJZSQVHLCKE";
                if (/^[XYZ]{1}/.test(temp))
                {
                    temp = temp.replace('X', '0')
                    temp = temp.replace('Y', '1')
                    temp = temp.replace('Z', '2')
                    pos = temp.substring(0, 8) % 23;

                    if (dni.toUpperCase().charAt(8) == cadenadni.substring(pos, pos + 1))
                    {
                        return true;
                    } else
                    {
                        return false;
                    }
                }
            }
            if (aux != letra) {
                return false;
            }
        } else {
            if (isNaN(dni.substring(0, longitud))) {
                return false;
            }
            posicion = dni.substring(0, longitud) % 23;
            letra = cadena.charAt(posicion);
            campo.value = dni + letra;
            return true;
        }
    } else {
        return false;
    }
    campo.value = campo.value.toUpperCase();
    return true;
    ;
}

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

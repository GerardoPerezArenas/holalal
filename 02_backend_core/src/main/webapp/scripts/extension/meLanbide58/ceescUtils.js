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
    var correcto = true;
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
                    temp = temp.replace('X', '0');
                    temp = temp.replace('Y', '1');
                    temp = temp.replace('Z', '2');
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
    return correcto;
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

// pasar .value
function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
    try {
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if (Trim(numero) != '') {
            var valor = numero;
            var pattern = '^[-]?[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
            var regex = new RegExp(pattern);
            //var result = valor.match(regex);
            var result = regex.test(valor);
            return result;
            //return /^[0-]{1,}(,[0-9]{1,longParteDecimal})?$/.test(numero.value);
        } else {
            //alert("TRUEEEEEEE");
            return true;
        }
    } catch (err) {
        alert(err);
        return false;
    }
}

// sin value
function validarNumericoDecimal(numero) {
    try {
        if (Trim(numero.value) != '') {
            return /^[-]?[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
        }
    } catch (err) {
        return false;
    }
}

// sin .value
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
function validarNumericoVacio(numero) {
    try {
        if (numero == null || numero == '') {
            return true;
        } else {
            if (Trim(numero) != '') {
                return /^([0-9])*$/.test(numero);
            }
        }
    } catch (err) {
        return false;
    }
}

// sin value  
function validarNumerico(numero) {
    try {
        if (Trim(numero.value) != '') {
            return /^([0-9])*$/.test(numero.value);
        }
    } catch (err) {
        return false;
    }
}

function validarTresCaracteresApellido(apellido) {
    try {
        var numCarac;
        if (apellido == null || apellido == "") {
            return true;
        } else {
            if (Trim(apellido) != "") {
                numCarac = apellido.length;
                if (numCarac < 3) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    } catch (err) {
        return false;
    }
}

function validarCaracteresCertificado(certificado) {
    try {
        var numCarac;
        if (certificado == null || certificado == "") {
            return true;
        } else {
            if (Trim(certificado) != "") {
                numCarac = certificado.length;
                if (numCarac > 83) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    } catch (err) {
        return false;
    }
}

function compruebaTamanoCampo(elemento, maxTex) {
    if (elemento == null || elemento == "") {
        return false;
    } else {
        if (Trim(elemento) != "") {
            if (elemento.length < maxTex) {
                return false;
            }
        }
    }
    return true;
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

function getXMLHttpRequest() {
    var aVersions = ["MSXML2.XMLHttp.5.0",
        "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
        "MSXML2.XMLHttp", "Microsoft.XMLHttp"
    ];

    if (window.XMLHttpRequest) {
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
            try {
                var oXmlHttp = new ActiveXObject(aVersions[i]);
                return oXmlHttp;
            } catch (error) {
                //no necesitamos hacer nada especial
            }
        }
    } else {
        return null;
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

function recargarDatosExpediente() {
    pleaseWait('on');
    document.forms[0].opcion.value = "cargarPestTram";
    document.forms[0].target = "mainFrame";
    document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
    document.forms[0].submit();
}

function elementoVisible(valor, idElemento) {
    if (valor == 'on') {
        document.getElementById(idElemento).style.visibility = 'inherit';
    } else if (valor == 'off') {
        document.getElementById(idElemento).style.visibility = 'hidden';
    }
}
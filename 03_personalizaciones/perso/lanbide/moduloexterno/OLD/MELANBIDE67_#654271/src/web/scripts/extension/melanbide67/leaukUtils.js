var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

function validarNumericoLak(numero) {
    try {
        if (Trim(numero.value) != '') {
            numero.value = reemplazarTextoLak(numero.value, /\./g, '');//nuevo
            return /^([0-9])*$/.test(numero.value);
        } else {
            return true;
        }
    } catch (err) {
        return false;
    }
    return true;
}

function validarNumericoDecimalLak(numero, longTotal, longParteDecimal) {
    try {
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if (Trim(numero.value) != '') {
            var valor = numero.value;
            valor = reemplazarTextoLak(valor, /\./g, '');//nuevo
            var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
            var regex = new RegExp(pattern);
            //var result = valor.match(regex);
            var result = regex.test(valor);
            return result;
        } else {
            return true;
        }
    } catch (err) {
        //alert(err);
        return false;
    }
}

function validarEmailLak(w_email) {
    if (Trim(w_email) != '') {
        w_email = normalizarTextoLak(w_email);
        var test = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        return test.test(w_email);
    }
}

function reemplazarPuntosLak(campo) {
    try {
        var valor = campo.value;
        if (valor != null && valor != '') {
            valor = valor.replace(/\./g, ',');
            campo.value = valor;
            //FormatNumber(valor, 8, 2, campo.id);//nuevo
        }
    } catch (err) {
    }
}

function convertirANumero(valor) {
    valor = valor.replace(/\./g, '');
    valor = valor.replace(/,/g, '.');
    return valor;
}

function reemplazarTextoLak(text, regExp, replacement) {
    try {
        if (text != null && text != '') {
            text = text.replace(regExp, replacement);
        }
    } catch (err) {
    }
    return text;
}

function validarNIFLak(campo) {
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
                if (/^[XYZ]{1}/.test(temp)) {
                    temp = temp.replace('X', '0');
                    temp = temp.replace('Y', '1');
                    temp = temp.replace('Z', '2');
                    pos = temp.substring(0, 8) % 23;

                    if (dni.toUpperCase().charAt(8) == cadenadni.substring(pos, pos + 1)) {
                        return true;
                    } else {
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

function calcularLetraNifLak(numero) {
    try {
        var retStr = '';
        var numStr = '' + numero;
        if (numStr.length >= 8) {
            var num = numStr.substring(0, 8);
            if (!isNaN(num)) {
                var posicion = parseInt(num) % 23;
                if (!isNaN(posicion)) {
                    return cadena.charAt(posicion);
                }
            }
        }
        return retStr;
    } catch (err) {
        return '';
    }
}

function validarCif(campo) {
    var par = 0;
    var non = 0;
    var nn = 0;
    var parcial = 0;
    var control = 0;
    var letras = "ABCDEFGHKLMNPQS";
    var valor = campo.value;
    var let = valor.charAt(0);

    if (valor.length != 9) {
        return false;
    }
    if (letras.indexOf(let.toUpperCase()) == -1) {
        return false;
    }
    for (var zz = 2; zz < 8; zz += 2) {
        par = par + parseInt(valor.charAt(zz));
    }
    for (var zz = 1; zz < 9; zz += 2) {
        nn = 2 * parseInt(valor.charAt(zz));
        if (nn > 9)
            nn = 1 + (nn - 10);
        non = non + nn;
    }
    parcial = par + non;
    control = (10 - (parcial % 10));
    if (control == 10)
        control = 0;
    if (control != valor.charAt(8)) {
        return false;
    }
    return true;
}

function validarFechaLak(form, inputFecha) {
    if (Trim(inputFecha.value) != '') {
        if (!ValidarFechaConFormatoLak(inputFecha.value, 'dd/mm/yyyy')) {
            return false;
        }
    }
    return true;
}

function comprobarCaracteresEspecialesLak(valor) {
    if (Trim(valor) != '') {
        valor = normalizarTextoLak(valor);
        var patron = /^[a-zA-Z0-9,.ÂºÂª_@#!()=Â¿?Â¡*\[\]-]*$/;
        var result = patron.test(valor);
        if (result) {
            return true;
        } else {
            return false;
        }
    } else {
        return true;
    }
}

function barraProgresoLak(valor, idBarra) {
    if (valor == 'on') {
        document.getElementById(idBarra).style.visibility = 'inherit';
    } else if (valor == 'off') {
        document.getElementById(idBarra).style.visibility = 'hidden';
    }
}

function randomUUID() {
    var s = [], itoh = '0123456789ABCDEF';
    // Make array of random hex digits. The UUID only has 32 digits in it, but we
    // allocate an extra items to make room for the '-'s we'll be inserting.
    for (var i = 0; i < 36; i++)
        s[i] = Math.floor(Math.random() * 0x10);
    // Conform to RFC-4122, section 4.4
    s[14] = 4;  // Set 4 high bits of time_high field to version
    s[19] = (s[19] & 0x3) | 0x8;  // Specify 2 high bits of clock sequence
    // Convert to hex chars
    for (var i = 0; i < 36; i++)
        s[i] = itoh[s[i]];
    // Insert '-'s
    s[8] = s[13] = s[18] = s[23] = '-';
    return s.join('');
}

function getListAsText(lista) {
    var retStr = '';
    try {
        for (var i = 0; i < lista.length; i++) {
            retStr = retStr + (i + 1) + ') ' + lista[i] + '\r\n';
        }
    } catch (err) {
    }
    return retStr;
}

function comprobarFechaLak(inputFecha, msgError) {
    var formato = 'dd/mm/yyyy';
    if (Trim(inputFecha.value) != '') {
        var D = ValidarFechaConFormatoLak(inputFecha.value, formato);
        if (!D[0]) {
            jsp_alerta("A", msgError);
            document.getElementById(inputFecha.name).focus();
            document.getElementById(inputFecha.name).select();
            return false;
        } else {
            inputFecha.value = D[1];
            return true;
        }
    }
    return true;
}

function ValidarFechaConFormatoLak(fecha, formato) {
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

function normalizarTextoLak(text) {
    var acentos = 'ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç-';
    var original = 'AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuuNncc_';
    text = text.toUpperCase();
    for (var i = 0; i < acentos.length; i++) {
        text = text.replace(new RegExp(acentos.charAt(i), 'g'), original.charAt(i));
    }
    text = text.replace(/\r\n|\n|\r/g, '');
    text = text.replace(/\s/g, "_");
    return text;
}

function trim(myString) {
    return myString.replace(/^\s+/g, '').replace(/\s+$/g, '');
}

function ajustarDecimalesLak(num, numDecimales) {
    try {
        var txt = '' + num;
        if (txt.indexOf(".") >= 0) {
            txt = txt + '001';
        }
        var f = parseFloat(txt);
        return f.toFixed(numDecimales);
    } catch (err) {
        return num;
    }
}

var shiftPressed = false;
function FormatNumber(Numero, Enteros, Decimales, idControl, evento) {
    try {
        var campo = document.getElementById(idControl);
        var valor = campo.value;
        if (evento == undefined || (!evento.shiftKey && !evento.ctrlKey && !evento.altKey)) {
            var caretPos = doGetCaretPosition(campo);
            var evtobj = window.event ? window.event : evento; //distinguish between IE's explicit event object (window.event) and Firefox's implicit.
            var unicode = -1;
            if (evtobj) {
                unicode = evtobj.charCode ? evtobj.charCode : evtobj.keyCode;
            }
            if (unicode == 16 || unicode == 17 || unicode == 18) {
                return;
            }
            //var valor = campo.value;	
            var puntosInicio = 0;
            if (valor.match(/\./g)) {
                puntosInicio = valor.match(/\./g).length;
            }
            var temp = valor;
            temp = temp.replace(/\./g, '');
            temp = temp.replace(/,/g, '.');
            if (isNaN(temp)) {
                return;
            }
            if (unicode == 110 || unicode == 190) {
                var charpos = valor.lastIndexOf('.');
                valor = campo.value;
                if (charpos >= 0) {
                    var ptone = valor.substring(0, charpos);
                    var pttwo = valor.substring(charpos + 1);
                    valor = ptone + ',' + pttwo;
                    campo.value = valor;
                }
                return;
            } else if (unicode == 188) {
                return;
            } else {
                var valorAux = temp;
                var valorDec = '';
                if (valor.indexOf(',') != -1) {
                    var partes = valor.split(',');
                    valorAux = partes[0];
                    valorDec = partes[1];
                }

                if (valorAux.length > 3) {
                    valorAux = valorAux.replace(/\./g, '');
                    var nuevoStr = '';
                    var posAnt = valorAux.length;
                    var subStr = '';
                    var inicio = 0;
                    for (var i = 1; i < (valorAux.length / 3); i++) {
                        inicio = posAnt - (3);
                        subStr = valorAux.substring(inicio, posAnt);
                        posAnt = inicio;
                        if (subStr.length == 3) {
                            nuevoStr = '.' + subStr + nuevoStr;
                        }
                    }
                    subStr = valorAux.substring(0, posAnt);
                    nuevoStr = subStr + nuevoStr;

                    valor = nuevoStr + (valorDec != '' ? ',' + valorDec : '');
                } else {
                    valor = valorAux + (valorDec != '' ? ',' + valorDec : '');
                }
                if ((valorAux && valorAux != '' && isNaN(valorAux)) || (valorDec && valorDec != '' && isNaN(valorDec))) {
                    campo.value = '';
                } else {
                    campo.value = valor;
                }
            }
            if (evtobj && evtobj.type != 'blur' && evtobj.type != 'change') {
                var puntosFin = 0;
                if (valor.match(/\./g)) {
                    puntosFin = valor.match(/\./g).length;
                }
                var puntosDiferencia = puntosFin - puntosInicio;
                caretPos = caretPos + puntosDiferencia;
                setCaretPosition(campo, caretPos);
            } else {
                try {
                    if (document.activeElement && document.activeElement.id) {
                        if (document.activeElement.id == idControl) {
                            setCaretPosition(campo, valor.length);
                        }
                    }
                } catch (err) {
                    alert(err);
                }
            }
        } else {
            shiftPressed = true;
        }
    } catch (err) {

    }
}

function doGetCaretPosition(ctrl) {
    var CaretPos = 0;
    // IE Support
    if (document.selection) {
        //ctrl.focus ();
        var Sel = document.selection.createRange();
        Sel.moveStart('character', -ctrl.value.length);
        CaretPos = Sel.text.length;
    }
    // Firefox support
    else if (ctrl.selectionStart || ctrl.selectionStart == '0')
        CaretPos = ctrl.selectionStart;
    return (CaretPos);
}

function setCaretPosition(ctrl, pos) {
    if (ctrl.setSelectionRange) {
        ctrl.focus();
        ctrl.setSelectionRange(pos, pos);
    } else if (ctrl.createTextRange) {
        var range = ctrl.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
}

function ajustarDecimalesCampo(campo, numDecimales) {
    try {
        var valor = campo.value;
        valor = convertirANumero(valor);
        if (valor && valor != '') {
            if (!isNaN(valor)) {
                var num = parseFloat(valor);
                num = num.toFixed(numDecimales);
                valor = '' + num;
                valor = valor.replace(/\./g, ',');
                campo.value = valor;
                FormatNumber(campo.value, 8, 2, campo.id);
                campo.removeAttribute("style");
            } else {
                campo.style.border = '1px solid red';
            }
        }
    } catch (err) {
        campo.style.border = '1px solid red';
    }
}

function tratarDatosDocumento(tipo, documento) {
    var correcto = true;

    if (documento == '' && tipo != "" && tipo != "0") {
        correcto = false;
    } else {
        if ((documento == "" && tipo == "") || (documento != "" && tipo == "")) {
            correcto = false;
        } else {
            if (tipo == "4" || tipo == "5") {
                // Si se trata de un CIF
                var cifValido = validarCIF(documento);
                if (!cifValido) {
                    correcto = false;
                }
            } else
            if (tipo == "1") {
                var nifValido = validarNif(documento);
                if (!nifValido) {
                    correcto = false;
                }
            } else
            // Validamos el pasaporte.
            if (tipo == "2") {
                correcto = true;
            } else
            // Validamos la tarjeta de residencia
            if (tipo == "3") {
                var nieCorrecto = validarNie(documento);
                if (!nieCorrecto) {
                    correcto = false;
                }
            }
        }//else
    }
    return correcto;
}

function validarCIF(cif) {
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
    if (cif.length != 9) {
        return false;
    } else {
        caracterControl = cif.charAt(8).toUpperCase();
    }
    for (zz = 2; zz < 8; zz += 2) {
        par = par + parseInt(cif.charAt(zz));
    }

    for (zz = 1; zz < 9; zz += 2) {
        nn = 2 * parseInt(cif.charAt(zz));
        if (nn > 9) {
            nn = 1 + (nn - 10);
        }
        non = non + nn;
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

function validarNie(documento) {
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

function compruebaTamanoCampo(elemento, maxTex) {
    var texto = elemento.value;
    if (texto.length > maxTex) {
        elemento.focus();
        return false;
    }
    return true;
}

function soloNumerosComa(objeto){
    var valores = "0123456789,";
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
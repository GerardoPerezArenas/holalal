/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

function extraerListaContrataciones(nodos) {
    var elemento = nodos[0];
    var hijos = elemento.childNodes;
    var codigoOperacion = null;
    var listaNueva = new Array();
    var fila = new Array();
    var nodoFila;
    var hijosFila;
    for (j = 0; hijos != null && j < hijos.length; j++) {
        if (hijos[j].nodeName == "CODIGO_OPERACION") {
            codigoOperacion = hijos[j].childNodes[0].nodeValue;
            listaNueva[j] = codigoOperacion;
        } else if (hijos[j].nodeName == "FILA") {
            nodoFila = hijos[j];
            hijosFila = nodoFila.childNodes;
            for (var cont = 0; cont < hijosFila.length; cont++) {
                if (hijosFila[cont].nodeName == "ID") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[0] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "PRIORIDAD") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[1] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DENOMPUESTO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[2] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NIVELCUALIFICACION") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[3] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_NIVEL") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[4] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "MODCONTRATO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[5] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DURCONTRATO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[6] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_DURCONTRATO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[7] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "GRUPOCOTIZ") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[8] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "COSTESALARIAL") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[9].toString();
                        tex = tex.replace(".", ",");
                        fila[9] = tex;
                    } else {
                        fila[9] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SUBVSOLICITADA") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[10].toString();
                        tex = tex.replace(".", ",");
                        fila[10] = tex;
                    } else {
                        fila[10] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "MUNICIPIOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[11] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NOMBREINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[12] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "APELLIDO1INICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[13] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "APELLIDO2INICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[14] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DNINIEINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[15] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "CV2") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[16] = '';
                    }
                } else if (hijosFila[cont].nodeName == "FECHACV2") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[17] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DEMANDA2") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[18] = '';
                    }
                } else if (hijosFila[cont].nodeName == "FECHADEMANDA2") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[19] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "FECHANACIMIENTOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[20] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SEXOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[21] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[21] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_SEXOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[22] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[22] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NIVELCUALIFICACIONINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[23] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[23] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_NIVELINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[24] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[24] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "PUESTOTRABAJOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[25] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[25] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NOFERTAINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[26] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[26] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "GRUPOCOTIZINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[27] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[27] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_GRUPOCOTIZINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[28] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[28] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DURACIONINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[29] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[29] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_DURACIONINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[30] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[30] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "FECHAINICIOINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[31] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[31] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "EDADINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[32] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[32] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "RETRIBUCIONBRUTAINICIO") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[33] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[33].toString();
                        tex = tex.replace(".", ",");
                        fila[33] = tex;
                    } else {
                        fila[33] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SISTGRANTIAJUVE_INI") {
                    if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'F') {
                        fila[34] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[34] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_SISTGRANTIAJUVE_INI") {
                    if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'F') {
                        fila[35] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[35] = '-';
                    }                    
                } else if (hijosFila[cont].nodeName == "MUNICIPIOFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[36] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[36] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NOMBREFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[37] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[37] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "APELLIDO1FIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[38] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[38] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "APELLIDO2FIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[39] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[39] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DNINIEFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[40] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[40] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SEXOFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[41] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[41] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_SEXOFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[42] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[42] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "GRUPOCOTIZFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[43] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[43] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_GRUPOCOTIZFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[44] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[44] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DURACIONFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[45] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[45] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "DESC_DURACIONFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[46] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[46] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "FECHAINICIOFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[47] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[47] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "FECHAFINFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[48] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[48] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "RETRIBUCIONBRUTAFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[49] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[49].toString();
                        tex = tex.replace(".", ",");
                        fila[49] = tex;
                    } else {
                        fila[49] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "COSTEFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[50] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[50].toString();
                        tex = tex.replace(".", ",");
                        fila[50] = tex;
                    } else {
                        fila[50] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "COSTESSFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[51] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[51].toString();
                        tex = tex.replace(".", ",");
                        fila[51] = tex;
                    } else {
                        fila[51] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "INDEMFINCONTRATOFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[52] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[52].toString();
                        tex = tex.replace(".", ",");
                        fila[52] = tex;
                    } else {
                        fila[52] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "COSTEREALFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[53] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[53].toString();
                        tex = tex.replace(".", ",");
                        fila[53] = tex;
                    } else {
                        fila[53] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SUBCONCEDIDAFIN") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[54] = hijosFila[cont].childNodes[0].nodeValue;
                        var tex = fila[54].toString();
                        tex = tex.replace(".", ",");
                        fila[54] = tex;
                    } else {
                        fila[54] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "SISTGRANTIAJUVE_FIN") {
                    if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'F') {
                        fila[55] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[55] = '-';
                    } 
                } else if (hijosFila[cont].nodeName == "DESC_SISTGRANTIAJUVE_FIN") {
                    if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'F') {
                        fila[56] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[56] = '-';
                    } 
                }
            }
            listaNueva[j] = fila;
            fila = new Array();
        }
    }
    return listaNueva;
}

function elementoVisible(valor, elemento) {
    if (valor == 'on') {
        document.getElementById(elemento).style.visibility = 'inherit';
    } else if (valor == 'off') {
        document.getElementById(elemento).style.visibility = 'hidden';
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
                if (/^[XYZ]{1}/.test(temp)) {
                    temp = temp.replace('X', '0')
                    temp = temp.replace('Y', '1')
                    temp = temp.replace('Z', '2')
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
    return true;
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

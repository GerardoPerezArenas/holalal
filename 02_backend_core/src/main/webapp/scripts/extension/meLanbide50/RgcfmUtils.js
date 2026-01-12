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

function cancelar() {
    cerrarVentana();
}

function volver() {
    cerrarVentana();
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

// sin value  
function validarNumericoRgcfm(numero) {
    try {
        if (Trim(numero.value) != '') {
            return /^([0-9])*$/.test(numero.value);
        }
    } catch (err) {
        return false;
    }
}

function validarNumericoDecimalRgcfm(numero) {
    try {
        if (Trim(numero.value) != '') {
            return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
        }
    } catch (err) {
        return false;
    }
}

function validarNumericoDecimalPrecisionRgcfm(numero, longTotal, longParteDecimal) {
    try {
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if (Trim(numero.value) != '') {
            var valor = numero.value;
            var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
            var regex = new RegExp(pattern);
            var result = regex.test(valor);
            return result;
        } else {
            return true;
        }
    } catch (err) {
        alert(err);
        return false;
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

function extraerListaEspacios(nodos) {
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
                } else if (hijosFila[cont].nodeName == "ID_ESPSOL") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[1] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NESP_DENESP") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[2] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NESP_ESPACRED") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[3] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NESP_ESPAUT") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[4] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NESP_ALUM") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[5] = '-';
                    }
                } else if (hijosFila[cont].nodeName == "NESP_ALUMNUEV") {
                    if (hijosFila[cont].childNodes.length > 0) {
                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                    } else {
                        fila[6] = '-';
                    }
                }
            }// for
            listaNueva[j] = fila;
            fila = new Array();
        }
    } //for
    return listaNueva;
}




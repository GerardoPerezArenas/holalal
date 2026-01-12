
function validarNumerico(numero){
    try{
        if (Trim(numero.valueOf())!='') {
            numero = reemplazarTexto(numero.valueOf(), /\./g, '');
            return /^([0-9])*$/.test(numero.valueOf());
        }else{
            return true;
        }
    }
    catch(err){
        return false;
    }
    return true;
}

function reemplazarTexto(text, regExp, replacement){
    try{
        if(text != null && text != ''){
            text = text.replace(regExp, replacement);
        }
    }
    catch(err){
    }
    return text;
}
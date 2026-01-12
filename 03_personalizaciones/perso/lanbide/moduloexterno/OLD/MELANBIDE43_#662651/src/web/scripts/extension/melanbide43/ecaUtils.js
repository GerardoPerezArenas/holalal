function comprobarFechaEca(inputFecha, msgError) {
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {    
    // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
      var D = ValidarFechaConFormatoEca(inputFecha.value,formato);
      if (!D[0]){
        jsp_alerta("A",msgError);
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

function ValidarFechaConFormatoEca(fecha, formato) {
  if (formato==null) formato ="dd/mm/yyyy";
  if (formato=="mm/yyyy")
      fecha = "01/"+fecha;
  else if (formato=="yyyy")
      fecha ="01/01/"+fecha;
  else if (formato =="mmyyyy")
      fecha = "01"+fecha;

  var D = DataValida(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D;
}

function barraProgreso(valor, idBarra) {
    if(valor=='on'){
        document.getElementById(idBarra).style.visibility = 'inherit';
    }
    else if(valor=='off'){
        document.getElementById(idBarra).style.visibility = 'hidden';
    }
}

function validarNumerico(numero){
    try{
        /*if (Trim(numero.value)!='') {
            alert("numero.value = "+numero.value)
            numero.value = reemplazarTexto(numero.value, /\./g, '');
            return /^([0-9])*$/.test(numero.value);*/
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
            //nuevo
            // text = text.replace(".", ""); 
            text = text.replace(regExp, replacement);
        }
    }
    catch(err){
    }
    return text;
}

function trim(myString){
    return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}

function convertirANumero(valor)    {   
    valor = valor.replace(/\./g, '');
    valor = valor.replace(/,/g , '.');
    return valor;
}
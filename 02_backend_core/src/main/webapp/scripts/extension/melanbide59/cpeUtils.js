
function validarNumericoCpe(numero, longTotal){
    try{
        if (Trim(numero.value)!='') {
            //return /^([0-9]){1,'+longParteEntera+'}$/.test(numero.value);
            var valor = numero.value;
            var pattern = '^[0-9]{1,'+longTotal+'}$';
            var regex = new RegExp(pattern);
            var result = regex.test(valor);
            return result;
        }else{
            return true;
        }
    }
    catch(err){
        return false;
    }
    return true;
}

function convertirANumero(valor)    {   
    valor = valor.replace(/\./g, '');
    valor = valor.replace(/,/g , '.');
    return valor;
}
            
function validarNumericoDecimalCpe(numero, longTotal, longParteDecimal){
    try{
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if(Trim(numero.value) != ''){
            var valor = numero.value;
            var pattern = '^[0-9]{1,'+longParteEntera+'}(,[0-9]{1,'+longParteDecimal+'})?$';
            var regex = new RegExp(pattern);
            //var result = valor.match(regex);
            var result = regex.test(valor);
            return result;
            //return /^[0-]{1,}(,[0-9]{1,longParteDecimal})?$/.test(numero.value);
        }else{
            return true;
        }
    }
    catch(err){
        //alert(err);
        return false;
    }
}
        
function reemplazarPuntosCpe(campo){
    try{
        var valor = campo.value;
        if(valor != null && valor != ''){
            valor = valor.replace(/\./g,',');
            campo.value = valor;
        }
    }
    catch(err){
    }
}

function reemplazarComasCpe(campo){
    try{
        var valor = campo.value;
        if(valor != null && valor != ''){
            valor = valor.replace(/\,/g,'.');
            campo.value = valor;
        }
    }
    catch(err){
    }
}


            
/*function comprobarCaracteresEspecialesCpe(texto){
    //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
    var iChars = "&'<>|^/\\%?∆¨??^~?¢≥";
    for (var i = 0; i < texto.length; i++) {
        if (iChars.indexOf(texto.charAt(i)) != -1) {
            return false;
        }
    }
    return true;
} */

function comprobarCaracteresEspecialesCpe(valor){
    if(Trim(valor) != ''){
        valor = normalizarTextoCpe(valor);
        //var patron='^[a-ZÒ·ÈÌÛ˙¡…Õ”⁄]{2,60}$';
        var patron = /^[a-zA-Z0-9,.∫™_@#!()=ø?°*\[\]-]*$/;

        //var regex = new RegExp(patron);
        //var result = valor.match(regex);
        var result = patron.test(valor);
        if(result) {
            return true;
        }else{
            return false;
        }
    }else{
        return true;
    }
} 
        
function barraProgresoCpe(valor, idBarra) {
    if(valor=='on'){
        document.getElementById(idBarra).style.visibility = 'inherit';
    }
    else if(valor=='off'){
        document.getElementById(idBarra).style.visibility = 'hidden';
    }
}
        
function validarFechaCpe(form, inputFecha)
{
    if (Trim(inputFecha.value)!='') {
        if (!ValidarFechaConFormatoCpe(inputFecha.value,'dd/mm/yyyy')){
            return false;
        }
    }
    return true;
}

function ValidarFechaConFormatoCpe(fecha, formato) {
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

function trim (myString)
{
    return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}
        
function reemplazarTextoCpe(text, regExp, replacement){
    try{
        if(text != null && text != ''){
            text = text.replace(regExp, replacement);
        }
    }
    catch(err){
    }
    return text;
}

function getListAsTextCpe(lista){
    var retStr = '';
    var act = '';
    try{
        for(var i = 0; i < lista.length; i++){
            act = lista[i] != null && lista[i] != undefined && lista[i] != '' ? lista[i] : '';
            if(act != ''){
                retStr = retStr + lista[i] + '\r\n';
            }
        }
    }catch(err){
        
    }
    return retStr.toUpperCase();
}

function tratarDatosDocumento(tipo, documento){  
  var correcto = true; 

  if (documento == '' && tipo!="" && tipo!="0"){
      correcto = false;
  }else
  if ((documento=="" && tipo=="") || (documento!="" && tipo=="")){
      correcto = false;
  }
  else{
      if(tipo=="4" || tipo=="5"){
          // Si se trata de un CIF
          var cifValido = validarCIF(documento);          
          if(!cifValido){
              correcto = false;
          }          
      }else
      if(tipo=="1"){
        var nifValido= validarNif(documento);
        if(!nifValido){
            correcto = false;
        }
      }else
      // Validamos el pasaporte.
      if (tipo=="2"){
          correcto = true;
      }else
      // Validamos la tarjeta de residencia
      if (tipo=="3") {
        var nieCorrecto = validarNie(documento);
        if (!nieCorrecto){
            correcto = false;
        }
      }
  }//else
  return correcto;
}

function validarCIF(cif)
{
    var par = 0;
    var non = 0;
    var letras="ABCDEFGHJKLMNPQRSUVW";
    var letrasInicio="KPQS";
    var letrasFin="ABEH";
    var letrasPosiblesFin="JABCDEFGHI";
    var let=cif.charAt(0).toUpperCase();
    var caracterControl;
    var zz;
    var nn;
    var parcial;
    var control;


    if (cif.length!=9)
    {
        return false;
    } else{
        caracterControl =cif.charAt(8).toUpperCase();
    }

    for (zz=2;zz<8;zz+=2)
    {
        par = par+parseInt(cif.charAt(zz));
    }

    for (zz=1;zz<9;zz+=2)
    {
        nn = 2*parseInt(cif.charAt(zz));
        if (nn > 9) nn = 1+(nn-10)
        non = non+nn
    }

    parcial = par + non;
    control = (10 - ( parcial % 10));
    if (control==10) control=0;


    /*
    * El valor del ˙ltimo car·cter:
    * Ser· una LETRA si la clave de entidad es K, P, Q Û S.
    * Ser· un NUMERO si la entidad es A, B, E Û H.
    * Para otras claves de entidad: el dÌgito podr· ser tanto n˙mero como letra.
    * */

    if (letrasInicio.indexOf(let)!=-1){
        return (letrasPosiblesFin.charAt(control)==caracterControl);
    } else if (letrasFin.indexOf(let)!=-1){
        return (caracterControl==control);
    } else if (letras.indexOf(let)!=-1){
        return ((letrasPosiblesFin.charAt(control)==caracterControl)||(caracterControl==control));
    } else{
        return false;
    }
 }

function validarNif(dni) { 
    var LONGITUD = 9;
    var exito = false;
    var numero;
    var let;
    var letra;
    
    if(dni!=null && dni.length==LONGITUD){
        numero = dni.substr(0,dni.length-1);
        let = dni.substr(dni.length-1,1);
        numero = numero % 23;
        letra='TRWAGMYFPDXBNJZSQVHLCKET';
        letra=letra.substring(numero,numero+1);
        
        if (letra.toUpperCase()==let.toUpperCase())
            exito = true;
    }//if
    
    return exito;
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
    var primeraLetra = documento.substring(0,1).toUpperCase();
    var numero = documento.substring(1,8);
    var ultimaLetra = documento.substring(8,9).toUpperCase();
    if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {        
        return false;
    }

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra == "Y") numero = parseInt(numero,10) + 10000000;
    else if (primeraLetra == "Z") numero = parseInt(numero,10) + 20000000;
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


function comprobarFechaCpe(inputFecha, msgError) {
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {
    /*if (consultando) {
      var validas = true;
      var fechaFormateada=inputFecha.value;
      var pos=0;
      var fechas = Trim(inputFecha.value);
      var fechas_array = fechas.split(/[:|&<>!=]/);
      for (var loop=0; loop < fechas_array.length; loop++) {
        f = fechas_array[loop];
        formato = formatoFecha(Trim(f));
        var D = ValidarFechaConFormato(f,formato);
        if (!D[0]) validas=false;
        else {
          if (fechaFormateada.indexOf(f,pos) != -1) {
            var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
            var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
            pos=fechaFormateada.indexOf(f,pos);
            fechaFormateada = toTheLeft + D[1]+ toTheRight;
          }
        }
      }
      if (!validas) {

        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
       document.getElementById(inputFecha.name).focus();
       document.getElementById(inputFecha.name).select();
       return false;
      } else {
        inputFecha.value = fechaFormateada;
        return true;
      }
    }*/
    // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
      var D = ValidarFechaConFormatoCpe(inputFecha.value,formato);
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

function mesesDiferencia(fechaDesde, fechaHasta){
    var numMeses = 0;
    try{
        if(fechaDesde && fechaHasta){
            if(fechaDesde != '' && fechaHasta != ''){
                var array_fecha_ini = fechaDesde.split("/");
                var array_fecha_fin = fechaHasta.split("/");
                var dIni = new Date(array_fecha_ini[2], array_fecha_ini[1], array_fecha_ini[0]);
                var dFin = new Date(array_fecha_fin[2], array_fecha_fin[1], array_fecha_fin[0]);

                var diferencia = Math.abs(dFin.getTime() - dIni.getTime());

                var anos = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 365));
                diferencia -= anos * (1000 * 60 * 60 * 24 * 365);
                var meses = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 30.4375));

                numMeses = anos*12 + meses;
            }
        }
    }catch(err){
        
    }
    return numMeses;
}

function ajustarDecimalesCpe(num, numDecimales){
    try{
        var txt = ''+num;
        if(txt.indexOf(".") >= 0){
            txt = txt + '001';
        }
        var f = parseFloat(txt);
        return f.toFixed(numDecimales);
    }catch(err){
        return num;
    }
}


function normalizarTextoCpe(text){
    var acentos = '√¿¡ƒ¬»…À ÃÕœŒ“”÷‘Ÿ⁄‹€„‡·‰‚ËÈÎÍÏÌÔÓÚÛˆÙ˘˙¸˚—Ò«Á-';
    var original = 'AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuuNncc_';
    text = text.toUpperCase();
    for (var i=0; i<acentos.length; i++) {
        text = text.replace(new RegExp(acentos.charAt(i), 'g'), original.charAt(i));
        
    }
    text = text.replace(/\r\n|\n|\r/g, '')
    text = text.replace(/\s/g,"_");
    return text;
}
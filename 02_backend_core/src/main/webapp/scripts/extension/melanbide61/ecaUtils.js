var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

function validarNumericoEca(numero){
    try{
        if (Trim(numero.value)!='') {
            numero.value = reemplazarTextoEca(numero.value, /\./g, '');//nuevo
            return /^([0-9])*$/.test(numero.value);
        }else{
            return true;
        }
    }
    catch(err){
        return false;
    }
    return true;
}
     
function validarNumericoDecimalReple(numero, longTotal, longParteDecimal){
    try{
        var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
        if(Trim(numero.value) != ''){
            var valor = numero.value;
            valor = reemplazarTextoEca(valor, /\./g, '');//nuevo
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

function validarEmailEca(w_email) {  
    if(Trim(w_email) != ''){
        w_email = normalizarTextoEca(w_email);
        var test = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; 
        //var emailReg = new RegExp(test);   
        //return emailReg.test(w_email); 
        return test.test(w_email);
    }
} 


/*
function reemplazarPuntosEca(campo){
    try{
        var valor = campo.value;
        
        if(valor != null && valor != ''){
            valor = valor.replace(/\./g,',');
            valor = valor.substring(0, valor.indexof(','));
            var decimales = valor.substring(valor.indexOf(",")+1,valor.lenght);
            if (decimales=="") decimales="00";
            valor = valor.toString().split('').reverse().join('').replace(/(?=\d*\.?)(\d{3})/g,'$1.');
            valor = valor.split('').reverse().join('').replace(/^[\.]/,'');
            valor = valor+","+decimales;
            campo.value = valor;
        }
    }
    catch(err){
    }
 }
     
   */
    function reemplazarPuntosReple(campo){
    try{
        var valor = campo.value;
        if(valor != null && valor != ''){
            valor = valor.replace(/\./g,',');
            campo.value = valor;
            //FormatNumber(valor, 8, 2, campo.id);//nuevo
        }
    }
    catch(err){
    }

}
    
function convertirANumero(valor)    {   
    valor = valor.replace(/\./g, '');
    valor = valor.replace(/,/g , '.');
    return valor;
}
function reemplazarTextoEca(text, regExp, replacement){
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

function validarNIFEca (campo) {
    var letra;
    var aux;
    var posicion;
    var longitud;
    var correcto=true;
    var dni = campo.value;
    longitud=dni.length;
    aux=dni.substring(longitud-1).toUpperCase();
    if ((longitud>=9)&&(longitud<10)){
        if (isNaN(aux)){	
            posicion = dni.substring(0,longitud-1) % 23;
            letra=cadena.charAt(posicion);
            if (isNaN(dni.substring(0,longitud-1))) {
                    //return false;//PUEDE Q NIE
                    var temp = dni.toUpperCase();
                    var cadenadni = "TRWAGMYFPDXBNJZSQVHLCKE";
                    if( /^[XYZ]{1}/.test( temp ) )
                    {
                            temp = temp.replace( 'X','0' )
                            temp = temp.replace( 'Y','1' )
                            temp = temp.replace( 'Z','2' )
                            pos = temp.substring(0, 8) % 23;

                            if( dni.toUpperCase().charAt( 8 ) == cadenadni.substring( pos, pos + 1 ) )
                            {
                                    return true;
                            }
                            else
                            {
                                    return false;
                            }
                    }
            }
            if(aux!=letra) {
                    return false;
            }
        }else{            
            if (isNaN(dni.substring(0,longitud))) {
                    return false;                     
            }
            posicion = dni.substring(0,longitud) % 23;
            letra=cadena.charAt(posicion);
            campo.value =dni+letra;
            return true;
        }
    }else {
      return false;
    }
    campo.value=campo.value.toUpperCase();
    return correcto;
}

function calcularLetraNifEca(numero){
    try{
        var retStr = '';
        var numStr = ''+numero;
        if(numStr.length >= 8){
            var num = numStr.substring(0, 8);
            if(!isNaN(num)){
                var posicion = parseInt(num) % 23;
                if(!isNaN(posicion)){
                    return cadena.charAt(posicion);   
                }
            }
        }
        return retStr;
    }catch(err){
        return '';
    }
}

function validarCif(campo){ 
    var par = 0; 
    var non = 0; 
    var nn = 0;
    var parcial = 0;
    var control = 0;
    var letras = "ABCDEFGHKLMNPQS"; 
    var valor = campo.value;
    var let = valor.charAt(0); 
    
    if (valor.length!=9) { 
        return false; 
    }   
    if (letras.indexOf(let.toUpperCase())==-1) { 
        return false; 
    }   
    for (var zz=2;zz<8;zz+=2) { 
        par = par+parseInt(valor.charAt(zz)); 
    }
    for (var zz=1;zz<9;zz+=2) { 
        nn = 2*parseInt(valor.charAt(zz)); 
        if (nn > 9) nn = 1+(nn-10); 
        non = non+nn; 
    }   
    parcial = par + non; 
    control = (10 - ( parcial % 10)); 
    if (control==10) control=0; 
    if (control!=valor.charAt(8)) { 
        return false; 
    } 
    return true; 
}


        
function validarFechaEca(form, inputFecha)
{
    if (Trim(inputFecha.value)!='') {
        if (!ValidarFechaConFormatoEca(inputFecha.value,'dd/mm/yyyy')){
            return false;
        }
    }
    return true;
}


            
/*function comprobarCaracteresEspecialesEca(texto){
    //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
    var iChars = "&'<>|^/\\%";
    for (var i = 0; i < texto.length; i++) {
        if (iChars.indexOf(texto.charAt(i)) != -1) {
            return false;
        }
    }
    return true;
} */

function comprobarCaracteresEspecialesEca(valor){
    if(Trim(valor) != ''){
        valor = normalizarTextoEca(valor);
        //var patron='^[a-Z√±√°√©√≠√≥√∫√Å√â√ç√ì√ö]{2,60}$';
        var patron = /^[a-zA-Z0-9,.¬∫¬™_@#!()=¬ø?¬°*\[\]-]*$/;

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
        
function barraProgresoReple(valor, idBarra) {
    if(valor=='on'){
        document.getElementById(idBarra).style.visibility = 'inherit';
    }
    else if(valor=='off'){
        document.getElementById(idBarra).style.visibility = 'hidden';
    }
}
    
function randomUUID() {
  var s = [], itoh = '0123456789ABCDEF';

  // Make array of random hex digits. The UUID only has 32 digits in it, but we
  // allocate an extra items to make room for the '-'s we'll be inserting.
  for (var i = 0; i <36; i++) s[i] = Math.floor(Math.random()*0x10);

  // Conform to RFC-4122, section 4.4
  s[14] = 4;  // Set 4 high bits of time_high field to version
  s[19] = (s[19] & 0x3) | 0x8;  // Specify 2 high bits of clock sequence

  // Convert to hex chars
  for (var i = 0; i <36; i++) s[i] = itoh[s[i]];

  // Insert '-'s
  s[8] = s[13] = s[18] = s[23] = '-';

  return s.join('');
}

function getListAsText(lista){
    var retStr = '';
    try{
        for(var i = 0; i < lista.length; i++){
            retStr = retStr + (i+1) + ') ' + lista[i] + '\r\n';
        }
    }catch(err){
        
    }
    return retStr;
}


function comprobarFechaEca(inputFecha, msgError) {
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

function normalizarTextoEca(text){
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

function trim(myString){
    return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}

function ajustarDecimalesEca(num, numDecimales){
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


// function FormatNumber(Numero, Enteros, Decimales,idControl) {
//    if(Numero.indexOf('.')!= -1)
//    {
//         if(Numero.indexOf(',')!= -1)
//         {
//             Numero = Numero.toString().replace(/\./g, '');
//             Numero = Numero.toString().replace(/,/g, '.');
//         }
//         else
//         {
//             Numero = Numero.toString().replace(/\./g, '');
//         }
//    }
//    else
//    {
//          if(Numero.indexOf(',') != Numero.lastIndexOf(','))
//          {
//            Numero = "";
//            return Numero;
//          }
//          else
//          {
//             Numero = Numero.toString().replace(/,/g, '.');
//          }
//    }
//    if (isNaN(Numero))
//    {
//       Numero = "";
//       return Numero;
//    }
//    if(Numero == null || Numero == "")
//    {
//         numtemp = "";
//         return numtemp;
//    }
//    else
//    {
//        Numero = parseFloat(Numero);
//        num = parseInt(Numero);
//        nume = Numero.toString().split('.');
//        entero = nume[0];
//        decima = nume[1];
//        fin = 0;
//        tieneDecimales = false;
//        if (decima != undefined) {
//         var pDec = parseFloat('0.'+decima);
//         pDec = pDec.toFixed(Decimales);
//         decima = ''+pDec;
//         if(decima.match(/\./g)){
//             tieneDecimales = true;
//            var partes = decima.split('.');
//            decima = partes[1];
//            if(decima != undefined){
//                fin = Decimales-decima.length; 
//             }
//         }else{
//             entero = ''+(parseInt(entero)+parseInt(decima));
//         }
//        }
//        else {
//         decima = '';
//         fin = Decimales; 
//        }
//        for(i=0;i<fin;i++)
//         decima+=String.fromCharCode(48); 
//        buffer="";
//        marca=entero.length-1;
//        chars=1;
//        while(marca>=0){
//         if(((chars%4)==0) && (entero.charAt(marca)!= '-')){
//             buffer="."+buffer;
//         }
//         buffer=entero.charAt(marca)+buffer;
//         marca--;
//         chars++;
//         if(chars==5)
//             chars=2;
//        }
//        if(Decimales>0)
//          num=buffer+','+decima;
//        else
//          num=buffer;
//      noSeparadores=0;//nuevo
//       /* if(Enteros==7){
//          noSeparadores = 2;}
//        if(Enteros==13){
//          noSeparadores = 4;}
//        if(Enteros<=6 && Enteros > 3 ){
//          noSeparadores = 1;}*/
//         
//        try{
//            if(buffer.match(/\./g)){
//                noSeparadores = buffer.match(/\./g).length; 
//            }
//        }catch(err){
//            
//        }
//         
//         
//        if((buffer != undefined && buffer.length> Enteros + noSeparadores) || (decima != undefined && tieneDecimales && decima.length>Decimales))
//        {
//           //alert("Solo caracteres numericos, m·ximo" + Enteros + " enteros y " + Decimales + " decimales : ** " + num + " **");
//           if(idControl != null && idControl != "")
//           {
//             document.getElementById(idControl).focus();
//             
//           }
//           //num=""
//        }
//     document.getElementById(idControl).value=num;
//     return num;
//    }
//}


var shiftPressed = false;
function FormatNumber(Numero, Enteros, Decimales,idControl, evento){
    try{
        var campo = document.getElementById(idControl);
        var valor = campo.value;
        if(evento == undefined || (!evento.shiftKey && !evento.ctrlKey && !evento.altKey)){
                var caretPos = doGetCaretPosition(campo);
                var evtobj=window.event? window.event : evento; //distinguish between IE's explicit event object (window.event) and Firefox's implicit.
                var unicode = -1;
                if(evtobj){
                    unicode = evtobj.charCode? evtobj.charCode : evtobj.keyCode;
                }
                if(unicode == 16 || unicode == 17 || unicode == 18){
                        return;
                }
                //var valor = campo.value;	
                var puntosInicio = 0;
                if(valor.match(/\./g)){
                        puntosInicio = valor.match(/\./g).length; 
                }
                var temp = valor;
                temp = temp.replace(/\./g, '');
                temp = temp.replace(/,/g , '.');
                if(isNaN(temp)){
                        return;
                }
                if(unicode == 110 || unicode == 190){
                        var charpos = valor.lastIndexOf('.');
                        valor = campo.value;
                        if (charpos >= 0){
                            var ptone = valor.substring(0,charpos);
                            var pttwo = valor.substring(charpos+1);
                            valor = ptone + ',' + pttwo;
                            campo.value = valor;
                        }
                        return;
                }else if(unicode == 188){
                        return;
                }else{
                        var valorAux = temp;
                        var valorDec = '';
                        if(valor.indexOf(',') != -1){
                                var partes = valor.split(',');
                                valorAux = partes[0];
                                valorDec = partes[1];
                        }

                        if(valorAux.length > 3){
                                valorAux = valorAux.replace(/\./g, '');
                                var nuevoStr = '';
                                var posAnt = valorAux.length;
                                var subStr = '';
                                var inicio = 0;
                                for(var i = 1; i < (valorAux.length/3); i++){
                                        inicio = posAnt - (3);
                                        subStr = valorAux.substring(inicio, posAnt);
                                        posAnt = inicio;
                                        if(subStr.length == 3){
                                                nuevoStr = '.'+subStr+nuevoStr;
                                        }
                                }
                                subStr = valorAux.substring(0, posAnt);
                                nuevoStr = subStr + nuevoStr;

                                valor = nuevoStr + (valorDec != '' ? ','+valorDec : '');
                        }else{
                            valor = valorAux + (valorDec != '' ? ','+valorDec : '');
                        }
                        if((valorAux && valorAux != '' && isNaN(valorAux)) || (valorDec && valorDec != '' && isNaN(valorDec))){
                            campo.value = '';
                        }else{
                            campo.value = valor;
                        }
                }
                if(evtobj && evtobj.type != 'blur' && evtobj.type != 'change'){
                    var puntosFin = 0;
                    if(valor.match(/\./g)){
                            puntosFin = valor.match(/\./g).length; 
                    }
                    var puntosDiferencia = puntosFin - puntosInicio;
                    caretPos = caretPos + puntosDiferencia;
                    setCaretPosition(campo, caretPos);
                }else{
                    try{
                        if(document.activeElement && document.activeElement.id){
                            if(document.activeElement.id == idControl){
                                setCaretPosition(campo, valor.length);
                            }
                        }
                    }catch(err){
                        alert(err);
                    }
                }
        }else{
                shiftPressed = true;
        }
    }catch(err){
        
    }
}

function doGetCaretPosition (ctrl) {
    var CaretPos = 0;
    // IE Support
    if (document.selection) {
            //ctrl.focus ();
            var Sel = document.selection.createRange ();

            Sel.moveStart ('character', -ctrl.value.length);

            CaretPos = Sel.text.length;
    }
    // Firefox support
    else if (ctrl.selectionStart || ctrl.selectionStart == '0')
            CaretPos = ctrl.selectionStart;

    return (CaretPos);
}


function setCaretPosition(ctrl, pos)
{
    if(ctrl.setSelectionRange)
    {
        ctrl.focus();
        ctrl.setSelectionRange(pos,pos);
    }
    else if (ctrl.createTextRange) {
        var range = ctrl.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
}

function ajustarDecimalesCampo(campo, numDecimales){
    try{
        var valor = campo.value;
        valor = convertirANumero(valor);
        if(valor && valor != ''){
            if(!isNaN(valor)){
                var num = parseFloat(valor);
                num = num.toFixed(numDecimales);
                valor = ''+num;				
                valor = valor.replace(/\./g, ',');
                campo.value = valor;
                FormatNumber(campo.value, 8, 2, campo.id);
                campo.removeAttribute("style");
            }else{
                campo.style.border = '1px solid red';
            }
        }
    }catch(err){
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
     * El valor del ˙ltimo car·cter:
     * Ser· una LETRA si la clave de entidad es K, P, Q Û S.
     * Ser· un NUMERO si la entidad es A, B, E Û H.
     * Para otras claves de entidad: el dÌgito podr· ser tanto n˙mero como letra.
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
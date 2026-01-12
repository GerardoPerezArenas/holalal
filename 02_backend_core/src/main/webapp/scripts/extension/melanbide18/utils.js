var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';

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
    var acentos = 'ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç-';
    var original = 'AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuuNncc_';
    text = text.toUpperCase();
    for (var i=0; i<acentos.length; i++) {
        text = text.replace(new RegExp(acentos.charAt(i), 'g'), original.charAt(i));
        
    }
    text = text.replace(/\r\n|\n|\r/g, '');
    text = text.replace(/\s/g,"_");
    return text;
}

function trim(myString){
    return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');
}

function ajustarDecimalesEca(num, numDecimales){
    try{
        return num.toFixed(numDecimales);
    }catch(err){
        return num;
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
var shiftPressed = false;

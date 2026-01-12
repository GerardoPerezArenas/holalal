// FECHAS CON FORMATOS: DDMMYY, DDMMYYYY,DD/MM/YY,DD/MM/YYYY,DD_MM_YY,DD_MM_YYYY,DD-MM-YY,DD-MM-YYYY.
function SoloCaracteresFechaLanbide(objeto) {

   var valores='0123456789/_-.';
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
        }

        objeto.value=salida.toUpperCase();
    }
}

function ValidarFechaConFormatoLanbide(fecha, formato) {
  if (formato==null) formato ="dd/mm/yyyy";
  if (formato=="mm/yyyy")
      fecha = "01/"+fecha;
  else if (formato=="yyyy")
      fecha ="01/01/"+fecha;
  else if (formato =="mmyyyy")
      fecha = "01"+fecha;

  var D = DataValidaLanbide(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D;
}

function validarFechaAnteriorLanbide(fechaAnterior,fechaPosterior,mensaje) {
    // Suponiendo que ambas son validas
    var dia1 = fechaAnterior.substring(0,2);
    var mes1 = fechaAnterior.substring(3,5);
    var anho1 = fechaAnterior.substring(6);
    var dia2 = fechaPosterior.substring(0,2);
    var mes2 = fechaPosterior.substring(3,5);
    var anho2 = fechaPosterior.substring(6);
    var fecha1 = new Date(anho1,(mes1-1),dia1);
    var fecha2 = new Date(anho2,(mes2-1),dia2);
    if (fecha1>fecha2) {
        jsp_alerta("A",mensaje);
        return false;
    }
    return true;
}

function DataValidaLanbide(Q) { 
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
                if (/^[XYZ]{1}/.test(temp))
                {
                    temp = temp.replace('X', '0')
                    temp = temp.replace('Y', '1')
                    temp = temp.replace('Z', '2')
                    var pos = temp.substring(0, 8) % 23;

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

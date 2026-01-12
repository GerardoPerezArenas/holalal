/*
 * Función para no permitir el uso de comillas simples en las cajas de texto
 */
function quitarComillasSimples(objeto){
    objeto.value = objeto.value.replace(/[']/g, '')
}

/*
 * Función para limitar el número de caracteres a 2000 de los elementos textArea
 */
function limitar2000Caracteres(objeto){
    // Separamos cada linea para poder contar los retornos de carro como caracter y además tener la posibilidad de volver a montar la cadena del modo original
    // ya que substr() no teniía en cuenta los retornos de carro como caracter y al guardar fallaba, y además de esto al retornar el control al jsp los retornos desaparecían.
    var arrayDeLineas = objeto.value.split('\n');
    var cadenaResultado = '';
    
    for (var i=0; i < arrayDeLineas.length; i++) {
        var sumaProxima = cadenaResultado.length + arrayDeLineas[i].length;
        
        if ((sumaProxima < 2000) && (i < (arrayDeLineas.length - 1))) {
            cadenaResultado += arrayDeLineas[i] + '\r\n';
        }else{
            var caracteresUltimaLinea = 2000 - cadenaResultado.length ;
            cadenaResultado += arrayDeLineas[i].substr(0,caracteresUltimaLinea);
            i = arrayDeLineas.length;
        }
    }
    
    objeto.value = cadenaResultado;
}

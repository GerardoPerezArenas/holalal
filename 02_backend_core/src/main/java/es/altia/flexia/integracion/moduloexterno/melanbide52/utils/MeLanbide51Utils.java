package es.altia.flexia.integracion.moduloexterno.melanbide52.utils;

/**
 *
 * @author oscar
 */
public class MeLanbide51Utils {
    
    
    public static boolean isInteger(String dato){
        boolean exito;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(Exception e){
            exito = false;
        }        
        return exito;
    }
}

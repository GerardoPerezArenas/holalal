package es.altia.flexia.integracion.moduloexterno.melanbide18.util;

import java.util.ResourceBundle;

/**
 * Clase que recupera propiedades de un fichero de propiedades
 * 
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edicion inicial</li>
 * </ol> 
 */
public class ConfigurationParameter {

    /**
     * Metodo que recupera la propiedad de que le indicamos como parametro. Ademas le enviamos el nombre del fichero de propiedades.
     * 
     * @param property
     * @param FICHERO_CONFIGURACION
     * @return 
     */
    public static String getParameter(String property,String FICHERO_CONFIGURACION){
        String valor = "";
        try{
            ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        return valor;
    }//getParameter
    
}//class
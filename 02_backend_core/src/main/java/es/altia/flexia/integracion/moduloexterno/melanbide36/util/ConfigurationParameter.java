package es.altia.flexia.integracion.moduloexterno.melanbide36.util;

import java.util.ResourceBundle;

/**
 * Clase que recupera propiedades de un fichero de propiedades
 * 
 * @author david.lopez
 * @version 17/10/2017 1.0  
 */
public class ConfigurationParameter {

    /**
     * M�todo que recupera la propiedad de que le indicamos como par�metro. Adem�s le enviamos el nombre del fichero de propiedades.
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

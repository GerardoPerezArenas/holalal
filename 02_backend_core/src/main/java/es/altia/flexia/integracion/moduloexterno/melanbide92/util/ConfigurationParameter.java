package es.altia.flexia.integracion.moduloexterno.melanbide92.util;

import java.util.ResourceBundle;

/**
 *
 * @author kepa
 */
public class ConfigurationParameter {

    /**
     * Método que recupera la propiedad de que le indicamos como parámetro.
     * Además le enviamos el nombre del fichero de propiedades.
     *
     * @param property
     * @param FICHERO_CONFIGURACION
     * @return
     */
    public static String getParameter(String property, String FICHERO_CONFIGURACION) {
        String valor = "";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        } catch (Exception e) {
        }//try-catch
        return valor;
    }//getParameter

}//class

package es.altia.flexia.integracion.moduloexterno.melanbide06.util;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 * @author david.caamano
 * @version 17/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide06ConfigurationParameter {
    
    //Logger
    private final static Logger log = Logger.getLogger(MeLanbide06ConfigurationParameter.class);
    
    /**
     * Recupera un parámetro del fichero de configuración.
     * @param property
     * @param FICHERO_CONFIGURACION
     * @return 
     */
    public static String getParameter(String property,String FICHERO_CONFIGURACION){
        String valor = "";
        try{
            ResourceBundle bundle =   ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando la propiedad = " + property + " del fichero de propiedades = " + FICHERO_CONFIGURACION);
        }//try-catch
        return valor;
    }//getParameter
    
}//class

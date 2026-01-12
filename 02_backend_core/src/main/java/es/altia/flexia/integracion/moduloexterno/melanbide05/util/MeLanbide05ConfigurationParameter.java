package es.altia.flexia.integracion.moduloexterno.melanbide05.util;

import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 14/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 14/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide05ConfigurationParameter {
   
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide05ConfigurationParameter.class);
    
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

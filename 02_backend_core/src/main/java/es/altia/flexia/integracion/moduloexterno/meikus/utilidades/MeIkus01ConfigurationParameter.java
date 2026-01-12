package es.altia.flexia.integracion.moduloexterno.meikus.utilidades;

import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author david.caamano
 */
public class MeIkus01ConfigurationParameter {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeIkus01ConfigurationParameter.class);
    
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
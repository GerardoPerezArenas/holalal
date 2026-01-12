package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author mikel
 */
public class MELanbide42Properties {
    //Logger
    private final static Logger log = LogManager.getLogger(MELanbide42Properties.class);
    private static final Properties melanbide42Props = new Properties();

    static {
        try {
            InputStream is = MELanbide42Properties.class.getClassLoader().getResourceAsStream("MELANBIDE42.properties");
            melanbide42Props.load(is);
        } catch (Exception e) {
            log.error("ERROR al inicializar el Properties MELANBIDE42.properties", e);
            throw new ExceptionInInitializerError(e);
        }

    }

    private MELanbide42Properties(){
        // No instanciable
    }
     
    public static String getProperty(String key){
        return melanbide42Props.getProperty(key);
    }
}

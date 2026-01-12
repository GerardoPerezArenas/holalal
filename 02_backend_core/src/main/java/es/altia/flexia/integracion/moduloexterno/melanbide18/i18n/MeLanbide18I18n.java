/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18.i18n;

import es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConstantesMeLanbide18;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *
 * @author santiagoc
 */
public class MeLanbide18I18n 
{
    private static ResourceBundle bundlePropiedades = ResourceBundle.getBundle(ConstantesMeLanbide18.FICHERO_PROPIEDADES);
    
    private static HashMap<Integer,ResourceBundle> arrayBundleMensajes = new HashMap<Integer, ResourceBundle>();
    
    private static MeLanbide18I18n instance = null;
    
    public static String LATIN_DATE_FORMAT = "dd/MM/yyyy";
    public static String DESC_DATE_FORMAT_SLASH = "yyyy/MM/dd";
    
    public static MeLanbide18I18n getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide18I18n.class)
            {
                if(instance == null)
                {
                    instance = new MeLanbide18I18n();
                }
            }
        }
        return instance;
    }    
    
    /**
     * Constructor privado
     * Recupera los ficheros de propiedades del módulo y los carga para que solo se lean una vez.
     */
    private MeLanbide18I18n()
    {
        //Recuperamos del bundle de propiedades la propiedad que indica los archivos de mensajes de que dispone el módulo.
        String archivos = bundlePropiedades.getString("FICHEROS_MENSAJES");
        String ubicacion = bundlePropiedades.getString("UBICACION_FICHEROS_MENSAJES");
        if(archivos != null && !archivos.equalsIgnoreCase(""))
        {
            String[]arrayArchivos = archivos.split(";");
            for(int x=0; x<arrayArchivos.length; x++)
            {
                try
                {
                    ResourceBundle bundle = ResourceBundle.getBundle(ubicacion + "." + arrayArchivos[x]);
                    if(bundle != null)
                    {
                        char cod = arrayArchivos[x].charAt(arrayArchivos[x].length()-1);
                        //Le restamos 48 por que es el valor de 0 en ascii por que el char es el codigo ascii del valor.
                        Integer codIdioma = Integer.valueOf(Integer.valueOf(cod) - 48);
                        arrayBundleMensajes.put(codIdioma, bundle);
                    }
                }
                catch(Exception ex)
                {
                    
                }
            }
        }
    }
    
    /**
     * Devuelve el valor de la clave de los ficheros de properties de mensajes que están cargados por defecto.
     * Si no existe devuelve un string vacío.
     * @param codIdioma
     * @param key
     * @return String
     */
    public String getMensaje(Integer codIdioma, String key)
    {
        String mensaje ="";
        if(arrayBundleMensajes != null)
        {
            try
            {
                ResourceBundle bundle = arrayBundleMensajes.get(codIdioma);
                mensaje = bundle.getString(key);
            }
            catch(Exception ex)
            {
                
            }
        }
        return mensaje;
    }
}

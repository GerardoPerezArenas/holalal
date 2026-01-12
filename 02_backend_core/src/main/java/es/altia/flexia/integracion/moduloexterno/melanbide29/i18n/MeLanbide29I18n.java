package es.altia.flexia.integracion.moduloexterno.melanbide29.i18n;

import es.altia.flexia.integracion.moduloexterno.melanbide29.util.ConstantesMeLanbide29;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide29I18n {
   
    private static ResourceBundle bundlePropiedades = ResourceBundle.getBundle(ConstantesMeLanbide29.FICHERO_PROPIEDADES);
    
    private static HashMap<Integer,ResourceBundle> arrayBundleMensajes = new HashMap<Integer, ResourceBundle>();
    
    private static MeLanbide29I18n instance = null;
    
    public static String LATIN_DATE_FORMAT = "dd/MM/yyyy";
    public static String DESC_DATE_FORMAT_SLASH = "yyyy/MM/dd";
    
    public static MeLanbide29I18n getInstance(){
        if(instance == null){
            synchronized(MeLanbide29I18n.class){
                if(instance == null){
                    instance = new MeLanbide29I18n();
                }//if(instance == null)
            }//synchronized(MeLanbide03I18n.class)
        }//if(instance == null)
        return instance;
    }//getInstance
    
    /**
     * Constructor privado
     * Recupera los ficheros de propiedades del módulo y los carga para que solo se lean una vez.
     */
    private MeLanbide29I18n(){
        //Recuperamos del bundle de propiedades la propiedad que indica los archivos de mensajes de que dispone el módulo.
        String archivos = bundlePropiedades.getString("FICHEROS_MENSAJES");
        String ubicacion = bundlePropiedades.getString("UBICACION_FICHEROS_MENSAJES");
        if(archivos != null && !archivos.equalsIgnoreCase("")){
            String[]arrayArchivos = archivos.split(";");
            for(int x=0; x<arrayArchivos.length; x++){
                try{
                    ResourceBundle bundle = ResourceBundle.getBundle(ubicacion + "." + arrayArchivos[x]);
                    if(bundle != null){
                        char cod = arrayArchivos[x].charAt(arrayArchivos[x].length()-1);
                        //Le restamos 48 por que es el valor de 0 en ascii por que el char es el codigo ascii del valor.
                        Integer codIdioma = Integer.valueOf(Integer.valueOf(cod) - 48);
                        arrayBundleMensajes.put(codIdioma, bundle);
                    }//if(bundle != null)
                }catch(Exception ex){
                }//try-catch
            }//for(int x=0; x>arrayArchivos.length; x++)
        }//if(archivos != null && archivos != "")
    }//MeLanbide03I18N
    
    /**
     * Devuelve el valor de la clave de los ficheros de properties de mensajes que están cargados por defecto.
     * Si no existe devuelve un string vacío.
     * @param codIdioma
     * @param key
     * @return String
     */
    public String getMensaje(Integer codIdioma, String key){
        String mensaje ="";
        if(arrayBundleMensajes != null){
            try{
                ResourceBundle bundle = arrayBundleMensajes.get(codIdioma);
                mensaje = bundle.getString(key);
            }catch(Exception ex){
            }//try-catch
        }//if(arrayBundleMensajes) != null
        return mensaje;
    }//getKey
    
    /**
     * Devuelve el valor de la fecha que enviamos como parámetro en una fecha de tipo String según el código de idioma que le pasemos
     * como parametro buscara en el archivo de propiedades de ese idioma la propiedad dateFormat con el formato para ese idioma.
     * Si no encuentra la propiedad en el fichero o se produce algún error devuelve un String vacío
     * 
     * @param codIdioma
     * @param fecha
     * @return String
     */
    public String getFechaConvertida(Integer codIdioma, Date fecha){
        String mensaje="";
        String stringFecha = "";
        if(arrayBundleMensajes != null){
            try{
                ResourceBundle bundle = arrayBundleMensajes.get(codIdioma);
                mensaje = bundle.getString("dateFormater");
                SimpleDateFormat formateador = new SimpleDateFormat(mensaje);
                stringFecha = formateador.format(fecha);
            }catch(Exception ex){
            }//try-catch
        }//if(arrayBundleMensajes != null)
        return stringFecha;
    }//getFechaConvertida
    
    /**
     * Devuelve el valor de la fecha que enviamos como parámetro en una fecha de tipo String según el código de idioma que le pasemos
     * como parametro buscara en el archivo de propiedades de ese idioma la propiedad dateFormatAlternativo con el formato alternativo
     * para ese idioma.
     * Si no encuentra la propiedad en el fichero o se produce algún error devuelve un String vacío
     * 
     * @param codIdioma
     * @param fecha
     * @return String
     */
    public String getFechaConvertidaAlternativa(Integer codIdioma, Date fecha){
        String mensaje="";
        String stringFecha = "";
        if(arrayBundleMensajes != null){
            try{
                ResourceBundle bundle = arrayBundleMensajes.get(codIdioma);
                mensaje = bundle.getString("dateFormaterAlt");
                SimpleDateFormat formateador = new SimpleDateFormat(mensaje);
                stringFecha = formateador.format(fecha);
            }catch(Exception ex){
            }//try-catch
        }//if(arrayBundleMensajes != null)
        return stringFecha;
    }//getFechaConvertida
    
    /**
     * Devuelve el valor de la fecha que enviamos como parámetro en una fecha de tipo String segun el patron que le pasamos como
     * parámetro-
     * Si no existe el patron o se produce algún error devuelve un String vacio.
     * 
     * @param datePattern
     * @param fecha
     * @return 
     */
    public String getFechaConvertida (String datePattern, Date fecha){
        String stringFecha = "";
        try{
            SimpleDateFormat formateador = new SimpleDateFormat(datePattern);
            stringFecha = formateador.format(fecha);
        }catch(Exception ex){
        }//try-catch
        return stringFecha;
    }//getFechaConvertida
    
}//class

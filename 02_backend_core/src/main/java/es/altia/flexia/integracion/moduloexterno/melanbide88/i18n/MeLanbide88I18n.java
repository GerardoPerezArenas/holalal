/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.i18n;

import es.altia.flexia.integracion.moduloexterno.melanbide88.util.ConstantesMeLanbide88;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *
 * @author INGDGC
 */
public class MeLanbide88I18n {
    
    private static final ResourceBundle BUNDLE_PROPERTIES = ResourceBundle.getBundle(ConstantesMeLanbide88.FICHERO_PROPIEDADES);
    
    private static final HashMap<Integer,ResourceBundle> ARRAY_BUNDLE_MENSAJES = new HashMap<Integer, ResourceBundle>();
    
    private static MeLanbide88I18n instance = null;
       
    public static MeLanbide88I18n getInstance(){
        if(instance == null){
            synchronized(MeLanbide88I18n.class){
                if(instance == null){
                    instance = new MeLanbide88I18n();
                }
            }
        }
        return instance;
    }
    
    /**
     * Constructor privado
     * Recupera los ficheros de propiedades del modulo y los carga para que solo se lean una vez.
     */
    private MeLanbide88I18n(){
        //Recuperamos del bundle de propiedades la propiedad que indica los archivos de mensajes de que dispone el módulo.
        String archivos = BUNDLE_PROPERTIES.getString("FICHEROS_MENSAJES");
        String ubicacion = BUNDLE_PROPERTIES.getString("UBICACION_FICHEROS_MENSAJES");
        if(archivos != null && !archivos.equalsIgnoreCase("")){
            String[]arrayArchivos = archivos.split(";");
            for(int x=0; x<arrayArchivos.length; x++){
                try{
                    ResourceBundle bundle = ResourceBundle.getBundle(ubicacion + "." + arrayArchivos[x]);
                    if(bundle != null){
                        char cod = arrayArchivos[x].charAt(arrayArchivos[x].length()-1);
                        //Le restamos 48 por que es el valor de 0 en ascii por que el char es el codigo ascii del valor.
                        Integer codIdioma = Integer.valueOf(Integer.valueOf(cod) - 48);
                        ARRAY_BUNDLE_MENSAJES.put(codIdioma, bundle);
                    }
                }catch(Exception ex){
                }
            }
        }
    }
    
    /**
     * Devuelve el valor de la clave de los ficheros de properties de mensajes que estan cargados por defecto.
     * Si no existe devuelve un string vacio.
     * @param codIdioma
     * @param key
     * @return String
     */
    public String getMensaje(Integer codIdioma, String key){
        String mensaje ="";
        if(ARRAY_BUNDLE_MENSAJES != null){
            try{
                ResourceBundle bundle = ARRAY_BUNDLE_MENSAJES.get(codIdioma);
                mensaje = bundle.getString(key);
            }catch(Exception ex){
            }
        }
        return mensaje;
    }
}

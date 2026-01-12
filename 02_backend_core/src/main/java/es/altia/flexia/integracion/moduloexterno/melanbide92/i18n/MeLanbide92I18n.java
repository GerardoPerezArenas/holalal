package es.altia.flexia.integracion.moduloexterno.melanbide92.i18n;

import java.util.HashMap;
import java.util.ResourceBundle;

public class MeLanbide92I18n {

    private static final ResourceBundle bundlePropiedades = ResourceBundle.getBundle("MELANBIDE92");

    private static final HashMap<Integer, ResourceBundle> arrayBundleMensajes = new HashMap<Integer, ResourceBundle>();

    private static MeLanbide92I18n instance = null;

    public static MeLanbide92I18n getInstance() {
        if (instance == null) {
            synchronized (MeLanbide92I18n.class) {
                if (instance == null) {
                    instance = new MeLanbide92I18n();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor privado Recupera los ficheros de propiedades del módulo y los
     * carga para que solo se lean una vez.
     */
    private MeLanbide92I18n() {
        //Recuperamos del bundle de propiedades la propiedad que indica los archivos de mensajes de que dispone el mÃ³dulo.
        String archivos = bundlePropiedades.getString("FICHEROS_MENSAJES");
        String ubicacion = bundlePropiedades.getString("UBICACION_FICHEROS_MENSAJES");
        if (archivos != null && !archivos.isEmpty()) {
            String[] arrayArchivos = archivos.split(";");
            for (String arrayArchivo : arrayArchivos) {
                try {
                    ResourceBundle bundle = ResourceBundle.getBundle(ubicacion + "." + arrayArchivo);
                    if (bundle != null) {
                        char cod = arrayArchivo.charAt(arrayArchivo.length() - 1);
                        //Le restamos 48 por que es el valor de 0 en ascii por que el char es el codigo ascii del valor.
                        Integer codIdioma = Integer.valueOf(cod) - 48;
                        arrayBundleMensajes.put(codIdioma, bundle);
                    }
                } catch (Exception ex) {

                }
            }
        }
    }

    /**
     * Devuelve el valor de la clave de los ficheros de properties de mensajes
     * que están cargados por defecto. Si no existe devuelve un string vacío.
     *
     * @param codIdioma
     * @param key
     * @return String
     */
    public String getMensaje(Integer codIdioma, String key) {
        String mensaje = "";
        if (arrayBundleMensajes != null) {
            try {
                ResourceBundle bundle = arrayBundleMensajes.get(codIdioma);
                mensaje = bundle.getString(key);
            } catch (Exception ex) {
            }
        }
        return mensaje;
    }

}

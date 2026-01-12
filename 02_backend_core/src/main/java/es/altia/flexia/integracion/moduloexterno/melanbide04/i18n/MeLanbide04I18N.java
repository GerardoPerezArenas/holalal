package es.altia.flexia.integracion.moduloexterno.melanbide04.i18n;

import java.util.HashMap;
import java.util.ResourceBundle;

public class MeLanbide04I18N
{
  private static ResourceBundle bundlePropiedades = ResourceBundle.getBundle("MELANBIDE04");

  private static HashMap<Integer, ResourceBundle> arrayBundleMensajes = new HashMap();

  private static MeLanbide04I18N instance = null;

  public static MeLanbide04I18N getInstance() {
    if (instance == null) {
      synchronized (MeLanbide04I18N.class) {
        if (instance == null) {
          instance = new MeLanbide04I18N();
        }
      }
    }
    return instance;
  }

  private MeLanbide04I18N()
  {
    String archivos = bundlePropiedades.getString("FICHEROS_MENSAJES");
    String ubicacion = bundlePropiedades.getString("UBICACION_FICHEROS_MENSAJES");
    if ((archivos != null) && (!archivos.equalsIgnoreCase(""))) {
      String[] arrayArchivos = archivos.split(";");
      for (int x = 0; x < arrayArchivos.length; x++)
        try {
          ResourceBundle bundle = ResourceBundle.getBundle(ubicacion + "." + arrayArchivos[x]);
          if (bundle != null) {
            char cod = arrayArchivos[x].charAt(arrayArchivos[x].length() - 1);

            Integer codIdioma = Integer.valueOf(Integer.valueOf(cod).intValue() - 48);
            arrayBundleMensajes.put(codIdioma, bundle);
          }
        }
        catch (Exception ex)
        {
        }
    }
  }

  public String getMensaje(Integer codIdioma, String key)
  {
    String mensaje = "";
    if (arrayBundleMensajes != null)
      try {
        ResourceBundle bundle = (ResourceBundle)arrayBundleMensajes.get(codIdioma);
        mensaje = bundle.getString(key);
      }
      catch (Exception ex) {
      }
    return mensaje;
  }
}
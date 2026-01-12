package es.altia.flexia.integracion.moduloexterno.lanbide02.util;

import java.util.ResourceBundle;

public class ConfigurationParameter {

    public static String getParameter(String property,String FICHERO_CONFIGURACION){
        String valor = "";
        try{
            ResourceBundle bundle = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
            valor = bundle.getString(property);
        }catch(Exception e){
            e.printStackTrace();
        }

        return valor;
    }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide07.util;

import java.util.ResourceBundle;

/**
 *
 * @author oscar
 */
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

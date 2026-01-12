/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide45.util;

/**
 *
 * @author davidg
 */
public class MeLanbide45Utils {
    
    
    public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide45.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public static String getNombreModulo()
    {
        try
        {
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide45.NOMBRE_MODULO, ConstantesMeLanbide45.FICHERO_PROPIEDADES);
            return nombreModulo;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    
    public static boolean isInteger(String dato){
        boolean exito;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(Exception e){
            exito = false;
        }        
        return exito;
    }
    
    
}

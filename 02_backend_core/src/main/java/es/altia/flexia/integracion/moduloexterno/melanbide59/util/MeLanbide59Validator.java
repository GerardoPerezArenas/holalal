/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide59.util;

/**
 *
 * @author santiagoc
 */
public class MeLanbide59Validator 
{
    public static boolean validarNumerico(String numero, int longitud)
    {
        try
        {
            if(numero != null && !numero.equals(""))
            {
                if(numero.length() <= longitud)
                {
                    Long l = Long.parseLong(numero);
                    if(l >= 0)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return true;
            }
        }
        catch(Exception ex)
        {
            
        }
        return false;
    }
    
    public static boolean validarNumericoDecimal(String numero, int longTotal, int longParteDecimal)
    {
        try
        {
            if(numero != null && !numero.equals(""))
            {
                int longParteEntera = longTotal - longParteDecimal;
                String pattern = "^[0-9]{1,"+longParteEntera+"}(,[0-9]{1,"+longParteDecimal+"})?$";
                return MeLanbide59Utils.evaluarExpresionRegular(pattern, numero);
            }
            else
            {
                return true;
            }
        }
        catch(Exception ex)
        {
            
        }
        return false;
    }
    
    public static boolean validarTexto(String valor, int longitud)
    {
        try
        {
            if(valor != null && !valor.equals(""))
            {
                if(valor.length() <= longitud)
                {
                    //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
                    String iChars = "&'<>|^/\\%";
                    for (int i = 0; i < valor.length(); i++) {
                        if (iChars.indexOf(valor.charAt(i)) != -1) {
                            return false;
                        }
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        catch(Exception ex)
        {
            
        }
        return false;
    }
}

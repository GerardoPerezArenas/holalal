/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide31.util;


import es.altia.flexia.integracion.moduloexterno.melanbide31.exception.ValidarCuentaBancariaException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author SantiagoC
 */
public class MeLanbide31Utils 
{
    
    private static Logger log = LogManager.getLogger(MeLanbide31Utils.class);
    
    public static String formatearMensajeColor(String mensaje, String color)
    {
       return "<font color='"+color+"'>"+mensaje+"</font>";
    }
    
    public static boolean validarCuentaBancaria(String numCuenta) throws ValidarCuentaBancariaException
    {
        String regExp = "^\\d{4}-\\d{4}-\\d{2}-\\d{10}$";
        Pattern patron = Pattern.compile(regExp);
        Matcher encaja = patron.matcher(numCuenta);
        if(!encaja.matches())
        {
            return false;
        }
        else
        {
            String cadena = ConfigurationParameter.getParameter("pesosDC", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
            String[] pesos = cadena.split(",");
            if(pesos.length != 10)
            {
                throw new ValidarCuentaBancariaException();
            }
            else
            {
                String[] partesCuenta = numCuenta.split("-");
                //Calculamos el primer digito de control
                int digitoControl1 = calcularDigitoControl("00"+partesCuenta[0]+partesCuenta[1], pesos);
                //Calculamos el segundo digito de control
                int digitoControl2 = calcularDigitoControl(partesCuenta[3], pesos);
                
                String cadenaControl = digitoControl1+""+digitoControl2;
                if(cadenaControl.equalsIgnoreCase(partesCuenta[2]))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }
    
    public static int calcularDigitoControl(String cadenaNumeros, String[] pesos) 
    {
        int longNumeros = cadenaNumeros.length();
        int calculo = 0;
        int total = 0;
        int numAct = 0;
        int peso = 0;
        int resto = 0;
        int resultado = 0;
        for(int i = 0; i < longNumeros; i++)
        {
            numAct = Integer.parseInt(Character.toString(cadenaNumeros.charAt(i)));
            peso = Integer.parseInt(pesos[i]);
            calculo = numAct*peso;
            total += calculo;
        }
        resto = total%11;
        resultado = 11-resto;
        if(resultado == 10)
        {
            resultado = 1;
        }
        else if(resultado == 11)
        {
            resultado = 0;
        }
        return resultado;
    }
    
    


    public static String pad(String str, int size, String padChar, String dir) throws Exception
    {
      StringBuffer padded = new StringBuffer(str);
      if(padded.length()>size)
      {
          String aux=padded.substring(0, (size));
          padded=new StringBuffer(aux);
      }
      else
      {
          while (padded.length() < size)
          {
              if(dir.equalsIgnoreCase(ConstantesMeLanbide31.RPAD))
              {
                padded.append(padChar);
              }
              else if(dir.equalsIgnoreCase(ConstantesMeLanbide31.LPAD))
              {
                  padded.insert(0, padChar);
              }
          }
        }
      return padded.toString();
    }
}

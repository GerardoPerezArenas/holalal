/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide60.util;

import es.altia.flexia.integracion.moduloexterno.melanbide60.util.ConstantesMeLanbide60;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author santiagoc
 */
public class MeLanbide60Utils 
{
    public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide60.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
    }   
    
    public static boolean evaluarExpresionRegular(String exp, String valor)
    {
        try
        {
            Pattern patron = Pattern.compile(exp);
            Matcher encaja = patron.matcher(valor);
            return encaja.matches();
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    public static String getCodigoCampoSuplementario(String campo)
    {
        return ConfigurationParameter.getParameter(campo, ConstantesMeLanbide60.FICHERO_PROPIEDADES);
    }
    
    public static Integer calcularEdad(Date feNac)
    {
        if(feNac != null)
        {
            try
            {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(feNac);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);

                Date fechaHoy = new Date();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fechaHoy);
                int ahoraDia = cal2.get(Calendar.DAY_OF_MONTH);
                int ahoraMes = cal2.get(Calendar.MONTH);
                int ahoraAno = cal2.get(Calendar.YEAR);

                int edad = (ahoraAno + 1900) - ano;

                if(ahoraMes < (mes - 1)){
                    edad--;
                }
                if(((mes - 1) == ahoraMes) && (ahoraDia < dia)) {
                    edad--;
                }
                if(edad > 1900){
                    edad -= 1900;
                }  

                return edad;
            }
            catch(Exception ex)
            {
                
            }
        }
        return null;
    }
    
    public static Integer calcularEdad2(Date feNac, Date fecIni)
    {
        if(feNac != null)
        {
            try
            {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(feNac);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);

                /*Date fechaHoy = new Date();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fechaHoy);*/
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTime(fecIni);
                int ahoraDia = cal2.get(Calendar.DAY_OF_MONTH);
                int ahoraMes = cal2.get(Calendar.MONTH);
                int ahoraAno = cal2.get(Calendar.YEAR);

                int edad = (ahoraAno + 1900) - ano;

                if(ahoraMes < (mes - 1)){
                    edad--;
                }
                if(((mes - 1) == ahoraMes) && (ahoraDia < dia)) {
                    edad--;
                }
                if(edad > 1900){
                    edad -= 1900;
                }  

                return edad;
            }
            catch(Exception ex)
            {
                
            }
        }
        return null;
    }
    
    public static Integer calcularMeses(Date feIni, Date feFin)
    {
        if(feIni != null && feFin != null)
        {
            try
            {
                Double numMeses = 0.0;

                long diferencia = Math.abs(feFin.getTime() - feIni.getTime());

                double anos = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 365));
                diferencia -= anos * (1000 * 60 * 60 * 24 * 365);
                double meses = Math.floor(diferencia / (1000 * 60 * 60 * 24 * 30.4375));

                numMeses = anos*12 + meses;
                
                return numMeses.intValue();
            }
            catch(Exception ex)
            {

            }
        }
        return null;
    }
    
    public static String redondearDecimalesString(String num, int numDecimales)
    {
        if(num == null)
        {
            return null;
        }
        else
        {
            try
            {
                BigDecimal bd = new BigDecimal(num);
                return redondearDecimalesString(bd, numDecimales);
            }
            catch(Exception ex)
            {

            }
            return "";
        }
    }
    
    public static String redondearDecimalesString(BigDecimal num, int numDecimales)
    {
        if(num == null)
        {
            return null;
        }
        else
        {
            try
            {
                String strCero  = "0.";
                for(int i = 0; i < numDecimales; i++)
                {
                    strCero += "0";
                }
                BigDecimal cero = new BigDecimal(strCero);
                num = num.stripTrailingZeros();
                num = num.add(cero);
                num = num.setScale(numDecimales, RoundingMode.HALF_EVEN);
                return num.toPlainString();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return "";
        }
    }
    
    public static BigDecimal redondearDecimalesStringBigDecimal(String num, int numDecimales)
    {
        if(num == null)
        {
            return null;
        }
        else
        {
            try
            {
                BigDecimal bd = new BigDecimal(num);
                return redondearDecimalesBigDecimal(bd, numDecimales);
            }
            catch(Exception ex)
            {

            }
            return null;
        }
    }
    
    public static BigDecimal redondearDecimalesBigDecimal(BigDecimal num, int numDecimales)
    {
        if(num == null)
        {
            return null;
        }
        else
        {
            try
            {
                String strCero  = "0.";
                for(int i = 0; i < numDecimales; i++)
                {
                    strCero += "0";
                }
                BigDecimal cero = new BigDecimal(strCero);
                num = num.stripTrailingZeros();
                num = num.add(cero);
                num.setScale(numDecimales, RoundingMode.DOWN);
                return num;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return null;
        }
    }
}

package es.altia.flexia.integracion.moduloexterno.melanbide70.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MeLanbide70Util {
    /**
    * Convierte un tipo de dato BigDecimal a String.
    * @param big
    * @return String
    */
    public static String integerToFormattedString(Integer num)
    {
        /**
        * Los # indican valores no obligatorios
        * Los 0 indican que si no hay valor se pondr� un cero
        */

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("es", "ES"));
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.'); 

        NumberFormat formatter = new DecimalFormat("#,#00", otherSymbols);



        //NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));

        String pattern = ((DecimalFormat) formatter).toPattern();
        String newPattern = pattern.replace("\u00A4", "").trim();
        //NumberFormat newFormat = new DecimalFormat(newPattern);

        //System.out.println(nf.format(12345678));
        // Resultado: 12.345.678,00 ? (m�s quisiera yo...)



        String ret = formatter.format(num);
        return ret;
    }//metodo

    /**
    * Convierte un tipo de dato BigDecimal a String.
    * @param big
    * @return String
    */
    public static String doubleToFormattedString(Double num)
    {
        /**
        * Los # indican valores no obligatorios
        * Los 0 indican que si no hay valor se pondr� un cero
        */

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("es", "ES"));
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.'); 

        NumberFormat formatter = new DecimalFormat(ConstantesMeLanbide70.getFORMATO_DECIMAL(), otherSymbols);



        //NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));

        String pattern = ((DecimalFormat) formatter).toPattern();
        String newPattern = pattern.replace("\u00A4", "").trim();
        //NumberFormat newFormat = new DecimalFormat(newPattern);

        //System.out.println(nf.format(12345678));
        // Resultado: 12.345.678,00 ? (m�s quisiera yo...)



        String ret = formatter.format(num);
        return ret;
    }//metodo
    
    /**
    * Convierte un tipo de dato BigDecimal a String sin decimales si son 00.
    * @param big
    * @return String
    */
    public static String doubleToFormattedStringSinZeros(Double num)
    {
        String formatDouble = doubleToFormattedString(num);
        int pos = formatDouble.indexOf(",00");
        
        if(pos!=-1) return formatDouble.substring(0, pos);        
        return formatDouble;
    }//metodo

    /**
     * String de un Date seg?l formato indicado
     */
    public static String dateToFormattedString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide70.getFORMATO_FECHA());
        if(date!=null) 
            return format.format(date);
        return null;
    }

    public static Integer calcularEdad(Date fecInicio, Date fecFin) throws Exception
    {
        if(fecInicio != null)
        {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String sFecInicio = formato.format(fecFin);
            String sFecFin = formato.format(fecInicio);
            String[] dat1 = sFecFin.split("/");
            String[] dat2 = sFecInicio.split("/");
            int anos = Integer.parseInt(dat2[2]) - Integer.parseInt(dat1[2]);
            int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
            if (mes < 0) {
              anos = anos - 1;
            } else if (mes == 0) {
              if (Integer.parseInt(dat2[0]) < Integer.parseInt(dat1[0])) {
                anos = anos - 1;
              }
            }
            return anos;
        }
        return null;
    }

   
    
}

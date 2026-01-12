package es.altia.flexia.integracion.moduloexterno.melanbide83.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

public class MeLanbide83Util {
    
    //Instancia
    private static MeLanbide83Util instance = null;
    /**
    * Convierte un tipo de dato BigDecimal a String.
    * @param big
    * @return String
    */
    public static String integerToFormattedString(Integer num)
    {
        /**
        * Los # indican valores no obligatorios
        * Los 0 indican que si no hay valor se pondrá un cero
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
        // Resultado: 12.345.678,00 ? (más quisiera yo...)



        String ret = formatter.format(num);
        return ret;
    }//metodo

    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide83Util getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide83Util.class)
            {
                instance = new MeLanbide83Util();
            }
        }
        return instance;
    }
    /**
    * Convierte un tipo de dato BigDecimal a String.
    * @param big
    * @return String
    */
    public static String doubleToFormattedString(Double num)
    {
        /**
        * Los # indican valores no obligatorios
        * Los 0 indican que si no hay valor se pondrá un cero
        */

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("es", "ES"));
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.'); 

        NumberFormat formatter = new DecimalFormat(ConstantesMeLanbide83.getFORMATO_DECIMAL(), otherSymbols);



        //NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));

        String pattern = ((DecimalFormat) formatter).toPattern();
        String newPattern = pattern.replace("\u00A4", "").trim();
        //NumberFormat newFormat = new DecimalFormat(newPattern);

        //System.out.println(nf.format(12345678));
        // Resultado: 12.345.678,00 ? (más quisiera yo...)



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
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide83.getFORMATO_FECHA());
        if(date!=null) 
            return format.format(date);
        return null;
    }

    /**
     * Date de un String seg?l formato indicado
     */
    public static Date formattedStringToDate(String fecha) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide83.getFORMATO_FECHA());
        Date date = null;
        if(fecha!=null) 
            date = format.parse(fecha);
        return date;
    }

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
      public int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
          UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide83.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            idioma = ConstantesMeLanbide83.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    

      }
}

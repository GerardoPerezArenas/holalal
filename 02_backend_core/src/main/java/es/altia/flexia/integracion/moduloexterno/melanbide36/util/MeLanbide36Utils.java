/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author santiagoc
 */
public class MeLanbide36Utils 
{
    
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
		// Resultado: 12.345.678,00 € (más quisiera yo...)
		
		
		
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
		* Los 0 indican que si no hay valor se pondrá un cero
		*/
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("es", "ES"));
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.'); 
		
		NumberFormat formatter = new DecimalFormat(ConstantesMeLanbide36.FORMATO_DECIMAL, otherSymbols);
		
		
		
		//NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
		
		String pattern = ((DecimalFormat) formatter).toPattern();
		String newPattern = pattern.replace("\u00A4", "").trim();
		//NumberFormat newFormat = new DecimalFormat(newPattern);
		
		//System.out.println(nf.format(12345678));
		// Resultado: 12.345.678,00 € (más quisiera yo...)
		
		
		
		String ret = formatter.format(num);
		return ret;
	}//metodo
}

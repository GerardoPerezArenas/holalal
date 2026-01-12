/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

/**
 *
 * @author santiagoc
 */
public class MeLanbide48Utils 
{
    private static Logger log = LogManager.getLogger(MeLanbide48Utils.class);
    static final SimpleDateFormat formatoddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide48.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public static String getCodProcedimientoDeExpediente(String numExpediente) {
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide48.BARRA_SEPARADORA);
            return datos[1];
        } catch (Exception ex) {
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
        return ConfigurationParameter.getParameter(campo, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
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
    
    /**
     * Calcula el numero de Meses etre dos fechas -1 Si son fechas no validas o si Fecha Fin < Fecha Inicio
     * @param inicio
     * @param fin
     * @return Numero de Meses entre dos fechas. -1 Si son fechas no validas (Errores al convertir) o si Fecha Fin < Fecha Inicio
     */
    public int calcularMeses(Date inicio, Date fin)
    {
        int retorno = -1;
        try {
            if (inicio != null && fin != null) {
                String inicioH00 = formatoddMMyyyy.format(inicio);
                String finH00 = formatoddMMyyyy.format(fin);
                if (formatoddMMyyyy.parse(inicioH00).before(formatoddMMyyyy.parse(finH00))) {
                    String[] inicioH00Array = inicioH00.split("/");
                    String[] finH00Array = finH00.split("/");
                    LocalDate local1 = new LocalDate(Integer.valueOf(inicioH00Array[2]), Integer.valueOf(inicioH00Array[1]), Integer.valueOf(inicioH00Array[0]));
                    LocalDate local2 = new LocalDate(Integer.valueOf(finH00Array[2]), Integer.valueOf(finH00Array[1]), Integer.valueOf(finH00Array[0]));
                    retorno = Months.monthsBetween(local1, local2.plusDays(1)).getMonths();
                } else if (formatoddMMyyyy.parse(inicioH00).after(formatoddMMyyyy.parse(finH00))) {
                    retorno = -1;
                } else {
                    retorno = 0;
                }
            }
        } catch (Exception e) {
            log.error("calcularMeses: ", e);
            retorno = -1;
        }
        return retorno;
    }
    
    public Double calcularMesesAsDouble(Date inicio, Date fin)
    {
        Double retorno = -1.0;
        try {
            if (inicio != null && fin != null) {
                String inicioH00 = formatoddMMyyyy.format(inicio);
                String finH00 = formatoddMMyyyy.format(fin);
                if (formatoddMMyyyy.parse(inicioH00).before(formatoddMMyyyy.parse(finH00))) {
                    String[] inicioH00Array = inicioH00.split("/");
                    String[] finH00Array = finH00.split("/");
                    LocalDate local1 = new LocalDate(Integer.valueOf(inicioH00Array[2]), Integer.valueOf(inicioH00Array[1]), Integer.valueOf(inicioH00Array[0]));
                    LocalDate local2 = new LocalDate(Integer.valueOf(finH00Array[2]), Integer.valueOf(finH00Array[1]), Integer.valueOf(finH00Array[0]));
                    retorno = Days.daysBetween(local1, local2.plusDays(1)).getDays()/30.416;  // Average 365/12 
                } else if (formatoddMMyyyy.parse(inicioH00).after(formatoddMMyyyy.parse(finH00))) {
                    retorno = -1.0;
                } else {
                    retorno = 0.0;
                }
            }
        } catch (Exception e) {
            log.error("calcularMesesAsDouble: ", e);
            retorno = -1.0;
        }
        return retorno;
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
    
    public static String escapeNewLineToSimpleSpace(String texto){
        if(texto!=null){
            texto=texto.replaceAll("\n"," ");
        }
        return texto;
    }
    
    public static String formatearFecha_ddmmyyyy(Date fecha){
        String dateFormateada="";
        if(fecha!=null){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //HH:mm:ss
            dateFormateada=dateFormat.format(fecha); //2016/11/16 12:08:43
        }
        return dateFormateada;
    }
    public static Date parsearString_ddmmyyyy_Fecha(String fecha) throws ParseException{
        Date dateFormateada=null;
        if(fecha!=null && fecha!=""){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //HH:mm:ss
            dateFormateada=dateFormat.parse(fecha); //2016/11/16 12:08:43
        }
        return dateFormateada;
    }
    /**
     * Decodifica un array de bite en una cadena e texto
     *
     * @param input
     * @return Cadena de texto resultado de la decodificacion desde el byte[]
     * @throws IOException
     */
    public static String decodeText(byte[] input) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(input)
                )
        ).readLine();
    }

    /**
     * Decodifica una cadena de texto a un endogin especifico.
     *
     * @param textoInput
     * @param encodig
     * @return
     * @throws IOException
     */
    public static String decodeText(String textoInput, String encodig) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(textoInput.getBytes(encodig)
                        )
                )
        ).readLine();
    }
    
    public static int getIdiomaUsuarioFromRequest(HttpServletRequest request){
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            return (usuario != null ? usuario.getIdioma() : 1);
        } catch (Exception ex) {
            log.error("Error leyendo Idioma del Usuario: " + ex.getMessage(), ex);
            ex.printStackTrace();
            return 1;
        }
    }
}

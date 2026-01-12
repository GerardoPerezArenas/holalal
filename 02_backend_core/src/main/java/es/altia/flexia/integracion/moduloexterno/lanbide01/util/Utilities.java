package es.altia.flexia.integracion.moduloexterno.lanbide01.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Utilities {
    private static Logger log = LogManager.getLogger(Utilities.class);
    private static final String COMMA = ",";
    private static final String DOT   = ".";

    /**
     * Convierte una fecha en formato String en la misma pero en formato Calendar
     * @param fecha: Fecha en formato String
     * @param formato: formato de la fecha, por ejemplo dd/MM/yyyy
     * @return
     */
    public static Calendar toCalendar(String fecha,String formato){
        Calendar cal = null;
        try{
            if(fecha!=null && formato!=null){
                SimpleDateFormat sf = new SimpleDateFormat(formato);
                java.util.Date date = sf.parse(fecha);

                cal = Calendar.getInstance();
                cal.clear();
                cal.setTime(date);
            }
        }catch(Exception e){
            log.error("Error en la conversión a Calendar de la fecha: " + fecha + " con formato: " + formato);
        }

        return cal;
    }


    /**
     * Redondea a número con decimales
     * @param numero: Número a redondear
     * @param numDecimales: Nº de decimales a los que se redondea
     * @return Un double
     */
    public static double redondearDosDecimales(double numero){
        return Math.rint(numero*100)/100;        
    }


    public static java.sql.Timestamp calendarToTimestamp(Calendar cal){
        java.sql.Timestamp result = null;

        if (cal!=null)
            result = new java.sql.Timestamp(cal.getTime().getTime());

        return result;
    }

    public static Calendar timestampToCalendar(java.sql.Timestamp timestamp){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(timestamp.getTime());

        return c;

    }
    
    public static String getCodigoProcedimientoFromNumExpediente(String numExpediente){
        String retorno="";
        try {
            if(numExpediente!=null){
            String[] arrayDatos = numExpediente.split("/");
            if(arrayDatos!=null && arrayDatos.length==3){
                retorno=arrayDatos[1];
            }
        }
        } catch (Exception e) {
            retorno="";
            log.error("Error al recuperar el cod procedimiento desde el numero de expediente - retornamos vacio. ", e);
        }
        return retorno;
    }
    
    public static int getEjercicioFromNumExpediente(String numExpediente){
        int retorno=0;
        try {
            if(numExpediente!=null){
            String[] arrayDatos = numExpediente.split("/");
            if(arrayDatos!=null && arrayDatos.length==3){
                retorno=Integer.valueOf(arrayDatos[0]);
            }
        }
        } catch (Exception e) {
            retorno=0;
            log.error("Error al recuperar el Ejercicio desde el numero de expediente - retornamos 0 . ", e);
        }
        return retorno;
    }
    
    /**
     * Decodifica un array de bite en una cadena e texto
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
     * @param textoInput
     * @param encodig
     * @return
     * @throws IOException 
     */
    public static String decodeText(String textoInput,String encodig) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(textoInput.getBytes(encodig)
                        )
                )
        ).readLine();
    }
    
    public static String formatearNumeroDecimalSeparadores(Double numero){
        String respuesta=null;
        try{
            if(numero!=null){
                DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                Currency currency = Currency.getInstance(new Locale("es", "ES"));
                simbolo.setCurrency(currency);
                simbolo.setGroupingSeparator('.');
                simbolo.setDecimalSeparator(',');
                DecimalFormat formateador = new DecimalFormat("###,##0.00", simbolo);
                respuesta=formateador.format(numero);
            }            
        }catch(Exception e){
            log.error("Error al formatear un numero con separadores decimales y de grupo", e);
            respuesta=null;
        }
        return respuesta;
    }
}

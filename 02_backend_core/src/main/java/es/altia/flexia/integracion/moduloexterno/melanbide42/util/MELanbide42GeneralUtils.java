/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MELanbide42GeneralUtils {
     
    private static Logger log = LogManager.getLogger(MELanbide42GeneralUtils.class);
    private static SimpleDateFormat formatDateddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
     
    public static String getAnioFromNumExpediente(String numExpediente){
        log.error("getAnioFromNumExpediente  - Begin()");
        String numExp ="";
        try {
            if(numExpediente!=null && !"".equals(numExpediente)){
                numExp=numExpediente.substring(0,4);
            }
        } catch (Exception e) {
            log.error("Error al extraer el anio desde  el Num expte. retornamos "+numExp+". Recibido : " + numExpediente, e);
            return numExp;
        }
        log.error("getAnioFromNumExpediente  - End() : " + numExpediente+"-->"+ numExp);
        return numExp;
    }
    
    public static String getCodigoProcFromNumExpediente(String numExpediente){
        log.error("getCodigoProcFromNumExpediente  - Begin()");
        String codPro ="";
        try {
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] arrayDatExp = numExpediente.split("/");
                codPro=arrayDatExp[1];
            }
        } catch (Exception e) {
            log.error("Error al extraer el codigo procedimiento desde  el Num expte. retornamos "+codPro+". Recibido : " + numExpediente, e);
            return codPro;
        }
        log.error("getCodigoProcFromNumExpediente  - End() : " + numExpediente+"-->"+ codPro);
        return codPro;
    }
    public static String getNumeroConsecutivoExpedienteFromNumExpediente(String numExpediente){
        log.error("getNumeroConsecutivoExpedienteFromNumExpediente  - Begin()");
        String numero ="";
        try {
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] arrayDatExp = numExpediente.split("/");
                numero=arrayDatExp[2];
            }
        } catch (Exception e) {
            log.error("Error al extraer el numero consecutivo de expediente desde  el Num expte. retornamos "+numero+". Recibido : " + numExpediente, e);
            return numero;
        }
        log.error("getNumeroConsecutivoExpedienteFromNumExpediente  - End() : " + numExpediente+"-->"+ numero);
        return numero;
    }
    
    public static String parsearTextoSI_S_NO_N_aNumero1_0(String texto){
        String numero = "";
        log.error("parsearTextoSI_S_NO_N_aNumero1_0  - Begin() - " + texto);
        try {
            if(texto!=null && !"".equals(texto)){
                texto = texto.toUpperCase();
                if ("SI".equalsIgnoreCase(texto) || "S".equalsIgnoreCase(texto)) {
                    numero="1";
                } else if ("NO".equalsIgnoreCase(texto) || "N".equalsIgnoreCase(texto)) {
                    numero="0";
                }else if("1".equalsIgnoreCase(texto) ||"0".equalsIgnoreCase(texto)){
                    numero=texto;
                }
            }
        } catch (Exception e) {
            log.error("parsearTextoSI_S_NO_N_aNumero1_0 - Error al pasar texto SiNo a Numero 1_0, devolvemos Null ", e);
            return "";
        }
        log.error("parsearTextoSI_S_NO_N_aNumero1_0  - End() - " + numero);
        return numero;
    }
    
    public static String parsearNumero1_0aTexto_S_N(String numero){
        String texto = "";
        log.error("parsearNumero1_0aTexto_S_N  - Begin() - " + numero);
        try {
            if(numero!=null && !"".equals(numero)){
                numero=numero.trim();
                if ("1".equalsIgnoreCase(numero)) {
                    texto="S";
                } else if ("0".equalsIgnoreCase(numero)) {
                    texto="N";
                }else if("S".equalsIgnoreCase(numero) || "N".equalsIgnoreCase(numero)){
                    texto=numero;
                }
            }
        } catch (Exception e) {
            log.error("parsearNumero1_0aTexto_S_N - Error al pasar numero 1_0 a texto S_N, devolvemos Null ", e);
            return "";
        }
        log.error("parsearNumero1_0aTexto_S_N  - End() - " + texto);
        return texto;
    }

    public static String mapearNroAnexoFormToCodColecBBDDUbicacion(String nroAnexo) {
        String numero = null;
        log.error("mapearNroAnexoFormToCodColecBBDDUbicacion  - Begin() - " + nroAnexo);
        try {
            if (nroAnexo != null && !nroAnexo.equalsIgnoreCase("")) {
                // Anexo I--> Colectivo 1, Anexo III--> Colectivo 2, Anexo IV--> Colectivo 3
                // Anexo V--> Colectivo 4
                if ("2".equalsIgnoreCase(nroAnexo)) {
                    numero = "1";
                } else if ("3".equalsIgnoreCase(nroAnexo)) {
                    numero = "2";
                }else if ("4".equalsIgnoreCase(nroAnexo)) {
                    numero = "3";
                }else if ("5".equalsIgnoreCase(nroAnexo)) {
                    numero = "4";
                }
            }
        } catch (Exception e) {
            log.error("mapearNroAnexoFormToCodColecBBDDUbicacion - Error al pasar nro deanexo del formulario a codigo colectivo en las ubicaciones, devolvemos Null ", e);
            return null;
        }
        log.error("mapearNroAnexoFormToCodColecBBDDUbicacion  - End() - " + numero);
        return numero;
    }
    
    public static String getCodEntidadDsdMapNroPestanaFormCOLEC(Map<String,String> mapaCodEntidad,String esAgrupacion,String numPestana) {
        String numero = null;
        log.error("getCodEntidadDsdMapNroPestanaFormCOLEC  - Begin() - numPestana/esAgrupacion : " + numPestana+"/"+esAgrupacion);
        try {
            if (esAgrupacion != null && !esAgrupacion.equalsIgnoreCase("")) {
                if(numPestana!=null && !numPestana.equalsIgnoreCase("") && "1".equals(esAgrupacion)){
                    numero = mapaCodEntidad.get(numPestana);
                }else
                    numero=mapaCodEntidad.get("0");
            }
        } catch (Exception e) {
            log.error("getCodEntidadDsdMapNroPestanaFormCOLEC - Mapeando Numero de pestaña y codigo entidad. ", e);
            return null;
        }
        log.error("getCodEntidadDsdMapNroPestanaFormCOLEC  - End() - " + numero);
        return numero;
    }

    /**
     * Calcula el numero de meses entre dos Fechas. Se recibe como parametros dos variables String deben Venir Formato: dd/MM/yyyy
     * @param fecha1
     * @param fecha2
     * @return Integer Valor numero de meses entre fechas. Null si algunas no cumple formato o No es cumplimentada.
     */
    static Integer calularMesesEntreFechas(String fecha1, String fecha2) {
        log.info("calularMesesEntreFechas: " + fecha1 + " " + fecha2);
        Integer respuesta = null;
        if(fecha1!=null && !fecha1.isEmpty() && (fecha2!=null && !fecha2.isEmpty())){
            String[] fecha1Array = fecha1.split("/");
            String[] fecha2Array = fecha2.split("/");
            if(fecha1Array!=null && fecha1Array.length==3 
                    && fecha2Array!=null && fecha2Array.length==3){
                try {
                    GregorianCalendar date1 = new GregorianCalendar(Integer.valueOf(fecha1Array[2]),(Integer.valueOf(fecha1Array[1])-1),Integer.valueOf(fecha1Array[0]),0,0,0);
                    GregorianCalendar date2 = new GregorianCalendar(Integer.valueOf(fecha2Array[2]),(Integer.valueOf(fecha2Array[1])-1),Integer.valueOf(fecha2Array[0]),0,0,0);
                    log.info("Fechas en Calendar: " 
                            + date1.toString()
                            + " - Date2: " + date2.toString()
                            );
                    if(date1.compareTo(date2)<0){
                        //respuesta  = Math.abs(((date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR)) * 12))
                        //        + Math.abs((date2.get(Calendar.MONTH) - date1.get(Calendar.MONTH)));
                        int dato = Math.abs(((date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR)) * 12));
                        log.info("Meses de anios : " + dato);
                        int dato2 =  Math.abs((date2.get(Calendar.MONTH) - date1.get(Calendar.MONTH)));
                        log.info("Meses de meses : " + dato2);
                        respuesta = dato+dato2;
                    }else{
                        log.info("Fecha Fin del calculo es previa a fecha inicio...Retorna Null.");
                    }
                } catch (Exception e) {
                    log.error("Error al calularMesesEntreFechas - Generico : " + e.getMessage() , e);
                }
            }
        }
        log.info("respuesta: " + respuesta);
        return respuesta;
    }
   
}

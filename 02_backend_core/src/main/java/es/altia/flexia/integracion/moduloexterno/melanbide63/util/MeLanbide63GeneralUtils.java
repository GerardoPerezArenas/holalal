/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide63.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide63GeneralUtils {
    
    private static Logger log = LogManager.getLogger(MeLanbide63GeneralUtils.class);

    public static String[] getDatosExpteDsdNumExpediente(String numExp) {
        String[] respVacia = new String[3];
        try {
            if (numExp != null && !numExp.equalsIgnoreCase("")) {
                String[] resp = numExp.split("/");
                return resp;
            }else{
                log.error("Ojo !! Se devuelve l arreglo de DatosExpteDsdNumExp con cadenas vacias.");
            }            
        } catch (Exception e) {
            log.error("Error al recoger los datos del expediente desde el Numero de Expediente: " + e.getMessage(), e);
        }
        return respVacia;
    }

    public static String preparaNombreFicheroLogCargaMasivaDoc(String codProc) {
        String nombre = "";
        try {
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
            nombre=dateFormat.format(fecha);           
        } catch (Exception e) {
            log.error("Error al preparar el nombre del fichero log carga masiva " + e.getMessage(), e);
            nombre="nombre";
        }
        return nombre+"_"+codProc;
    }
    
    
       
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

/**
 * Operaciones Genericas 
 * @author INGDGC
 */
public class MeLanbide43GeneralUtils {
    
    public static String getProcedimientoFromNumeroExpediente(String numeroExpediente){
        String respuesta = null;
        if(numeroExpediente!=null && !numeroExpediente.isEmpty()){
            String[] datosExp = numeroExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            if(datosExp!=null && datosExp.length==3)
                respuesta=datosExp[1];
        }
        return respuesta;
    }
}

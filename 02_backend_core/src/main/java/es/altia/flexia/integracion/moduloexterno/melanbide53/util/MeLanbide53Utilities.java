/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.util;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide53Utilities {
    private static final Logger _log = LogManager.getLogger(MeLanbide53Utilities.class.getName());
    private static MeLanbide53Utilities instance = null;

    public MeLanbide53Utilities() {
    }

    public static MeLanbide53Utilities getInstance() {
        if (instance == null) {
            synchronized (MeLanbide53Utilities.class) {
                instance = new MeLanbide53Utilities();
            }
        }
        return instance;
    }

     public static String getNombreMesGregCalendar(int mesNumero){
        switch(mesNumero){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 0:
                return "Diciembre";
            default: 
                return "";
        }
    }    
    
    
    public String escapeSpecialCharacterXML(String texToEscape) throws Exception {
        String textoRturn = "";
        try {
            String [] caracteresEspeciales = (ConfigurationParameter.getParameter(ConstantesMeLanbide53.CARACTERES_ESPECIALES_XML,ConstantesMeLanbide53.FICHERO_PROPIEDADES)).split(ConstantesMeLanbide53.SEPARADOR_VALORES_CONF);
            String [] escapeCaracteresEspeciales = (ConfigurationParameter.getParameter(ConstantesMeLanbide53.ESCAPE_CARACTERES_ESPECIALES_XML,ConstantesMeLanbide53.FICHERO_PROPIEDADES)).split(ConstantesMeLanbide53.SEPARADOR_VALORES_CONF);
            if(caracteresEspeciales != null && caracteresEspeciales.length>0){
                if(escapeCaracteresEspeciales != null && escapeCaracteresEspeciales.length>0){
                    for(int i=0;i<caracteresEspeciales.length;i++){
                        texToEscape = texToEscape.replaceAll(caracteresEspeciales[i], escapeCaracteresEspeciales[i]);
                    }
                    textoRturn = texToEscape;
                }else{
                    _log.error("No se ha recuperado ningun valor para escapar los caracter especialesdel el XML en el fichero properties");
                }
            }else{
                _log.error("No se ha recuperado ningun caracter especial desde el fichero de properties para escapar en el XML");
            }
        } catch (Exception e) {
            _log.error(instance + " - Erorr al realizar el escape de caracteres especiales en el XML", e);
            throw e;
        }
        return textoRturn;
    }
    
    
}

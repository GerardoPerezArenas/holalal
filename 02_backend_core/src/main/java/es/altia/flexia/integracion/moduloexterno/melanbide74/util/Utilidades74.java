/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide74.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Kepa
 */
public class Utilidades74 {

    //Logger
    private static final Logger log = LogManager.getLogger(Utilidades74.class);

    public static String traducirTipoDocZorku(String tipoFlexia) {
        log.debug("traducirTipoDocZorku.BEGIN. Documento de Flexia: " + tipoFlexia);

        String tipoCIF = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_CIF_ZK, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String tipoNIE = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_NIE_ZK, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String tipoPasaporte = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_PASAPORTE_ZK, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String tipoOtro = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_OTRO_ZK, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String tipoDNI = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_DNI_ZK, ConstantesMeLanbide74.FICHERO_PROPIEDADES);

        // DNI
        if (ConstantesMeLanbide74.TIPO_NIF_FLEXIA.equals(tipoFlexia)) {
            return tipoDNI;
            }
        // CIF or CIF ENT. PUBLICA
        if (ConstantesMeLanbide74.TIPO_CIF_FLEXIA.equals(tipoFlexia) || ConstantesMeLanbide74.TIPO_CIF_ENT_FLEXIA.equals(tipoFlexia)) {
            return tipoCIF;
        }
        // NIE
        if (ConstantesMeLanbide74.TIPO_NIE_FLEXIA.equals(tipoFlexia)) {
            return tipoNIE;
        }
        // Pasaporte
        if (ConstantesMeLanbide74.TIPO_PASAPORTE_FLEXIA.equals(tipoFlexia)) {
            return tipoPasaporte;
        }
        return tipoOtro;
    }

    public static String traducirIdProvincia(String idFlexia) {
        log.debug("traducirIdProvincia. BEGIN: IdFlexia:" + idFlexia);
        String idWS = "";
        int tamanhoIdWS = 2;
        int tamanhoIdFlexia = idFlexia.length();
            try {
            if (tamanhoIdFlexia == tamanhoIdWS) {
                log.debug("traducirIdProvincia. END. IdWS: " + idFlexia);
                return idFlexia;
            }
            if (idFlexia != null) {
                if (!"".equals(idFlexia.trim())) {

                    idWS = "0" + idFlexia;
                    tamanhoIdFlexia = idWS.length();
                    while (tamanhoIdFlexia < tamanhoIdWS) {
                        idWS = "0" + idFlexia;
                        tamanhoIdFlexia = idWS.length();
                    }//while (tamanhoIdFlexia < tamanhoCodigoIdWS)
            }
            }
            log.debug("traducirIdProvincia. END: IdWS:" + idWS);
        } catch (Exception e) {
            log.error("Salta excepcion en el metodo traducirIdProvincia con idFlexia recibido:" + idFlexia);
        }
        return idWS;
    }

    public static String traducirIdMunicipio(String idFlexia) {
        log.debug("traducirIdPMunicipio. BEGIN: IdFlexia:" + idFlexia);
        String idWS = "";
        int tamanhoIdWS = 3;
        int tamanhoIdFlexia = idFlexia.length();
            try {
            if (tamanhoIdFlexia == tamanhoIdWS) {
                log.debug("traducirIdMunicipio. END. IdWS: " + idFlexia);
                return idFlexia;
            }
            if (!"".equals(idFlexia.trim())) {
                idWS = "0" + idFlexia;
                tamanhoIdFlexia = idWS.length();
                while (tamanhoIdFlexia < tamanhoIdWS) {
                    idWS = "0" + idWS;
                    tamanhoIdFlexia = idWS.length();
                }//while (tamanhoIdFlexia < tamanhoCodigoIdWS)
            }
            log.debug("traducirIdMunicipio. END: IdWS:" + idWS);
        } catch (Exception e) {
            log.error("Salta excepcion en el metodo traducirIdMunicipio con idFlexia recibido: " + idFlexia);
        }
        return idWS;
        }

    public static String recuperaCifrasTelefono(String telefonoTer) {
        log.debug("recuperaTelefono del tercero:" + telefonoTer);
        String telRetorno = "";
        for (int i = 0; i < telefonoTer.length(); i++) {
            if (Character.isDigit(telefonoTer.charAt(i))) {
                telRetorno = telRetorno + telefonoTer.charAt(i);
            }
        }
        if (telRetorno.length() > 9) {
            try {
                telRetorno = telRetorno.substring(0, 9);
            } catch (Exception e) {
                log.error("Error al recortar el telefono " + e);
            }
        }
        log.debug("Telefono Retorno: " + telRetorno);
        return telRetorno;
    }

    public static int dameAcCodZorku(int codArea) {
        int acCod = 0;
        switch (codArea) {
            case 2:
                acCod = 4;
                break;
            case 3:
                acCod = 6;
                break;
            case 4:
                acCod = 3;
                break;
            case 5:
                acCod = 7;
                break;
            case 8:
                acCod = 9;
                break;
            case 9:
                acCod = 10;
                break;
            case 10:
                acCod = 11;
                break;
            case 12:
                acCod = 8;
        }
        return acCod;
    }

    public static String convierteFechaZorku(Calendar fecha) {
        String fechaZORKU = null;
        try {
            XMLGregorianCalendar fechaConvertida = null;
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(fecha.getTime());
            fechaConvertida = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            String valorXmlGcal = String.valueOf(fechaConvertida);
            fechaZORKU = valorXmlGcal.split("\\u002b")[0];
        } catch (DatatypeConfigurationException ex) {
            log.error("Salta excepcion en el metodo convierteFechaZorku : " + ex);
        }
        return fechaZORKU;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }

}

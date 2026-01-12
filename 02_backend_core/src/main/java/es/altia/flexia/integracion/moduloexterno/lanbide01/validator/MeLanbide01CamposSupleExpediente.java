/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.validator;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import es.altia.flexia.integracion.moduloexterno.lanbide01.exception.MeLanbide01Exception;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01TipoCampoSuplementarioEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide01CamposSupleExpediente {
    
    private final Logger log = LogManager.getLogger(MeLanbide01CamposSupleExpediente.class);
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFecha_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    
    
    public double getCampoSuplementarioNumericoExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioNumeroExpediente() : BEGIN ");
        return new Double(getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.NUMERICO.getCodigo()),numExpediente));
    }
    
    public double getCampoSuplementarioNumericoCalculadoExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioNumericoCalculadoExpediente() : BEGIN ");
        return new Double(getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.NUMERICO_CALCULADO.getCodigo()),numExpediente));
    }
    
    public String getCampoSuplementarioTextoExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioTextoExpediente() : BEGIN ");
        return getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.TEXTO.getCodigo()),numExpediente);
    }
    
    
    public String getCampoSuplementarioTextoLargoExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioTextoLargoExpediente() : BEGIN ");
        return getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.TEXTO_LARGO.getCodigo()),numExpediente);
    }
    
    
    public byte[] getCampoSuplementarioFicheroExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFicheroExpediente() : BEGIN ");
        return Base64.decode(getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.FICHERO.getCodigo()),numExpediente));
    }
    
    public String getCampoSuplementarioDesplegableExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioDesplegableExpediente() : BEGIN ");
        return getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE.getCodigo()),numExpediente);
    }
    
    public String getCampoSuplementarioDesplegableExternoExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioDesplegableExternoExpediente() : BEGIN ");
        return getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE_EXTERNO.getCodigo()),numExpediente);
    }
    
    public Calendar getCampoSuplementarioFechaExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioDesplegableExternoExpediente() : BEGIN ");
        String valor = getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.FECHA.getCodigo()),numExpediente);
        Calendar response  = null;
        if(valor != null ){
            response = Calendar.getInstance();
            try {
                response.setTime(formatFecha_dd_MM_yyyy.parse(valor));
            } catch (ParseException ex) {
                log.error("Error Parsenado Fecha ..", ex);
                response=null;
            }
        }
        return response;
    }
    
    public Date getCampoSuplementarioFechaDateExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFechaDateExpediente() : BEGIN ");
        String valor = getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.FECHA.getCodigo()),numExpediente);
        Date response  = null;
        if(valor != null ){
            try {
                response = formatFecha_dd_MM_yyyy.parse(valor);
            } catch (ParseException ex) {
                log.error("Error Parsenado Fecha ..", ex);
                response=null;
            }
        }
        return response;
    }
    
    public Calendar getCampoSuplementarioFechaCalculadaExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFechaCalculadaExpediente() : BEGIN ");
        String valor = getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.FECHA_CALCULADA.getCodigo()),numExpediente);
        Calendar response  = null;
        if(valor != null ){
            response = Calendar.getInstance();
            try {
                response.setTime(formatFecha_dd_MM_yyyy.parse(valor));
            } catch (ParseException ex) {
                log.error("Error Parsenado FechaCalculada ..", ex);
                response=null;
            }
        }
        return response;
    }
    
    public Date getCampoSuplementarioFechaCalculadaDateExpediente(Integer codOrganizacion, String codigoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFechaCalculadaDateExpediente() : BEGIN ");
        String valor = getCampoSuplementarioExpediente(codOrganizacion, codigoCampo,Integer.parseInt(Melanbide01TipoCampoSuplementarioEnum.FECHA_CALCULADA.getCodigo()),numExpediente);
        Date response  = null;
        if(valor != null ){
            try {
                response=formatFecha_dd_MM_yyyy.parse(valor);
            } catch (ParseException ex) {
                log.error("Error Parsenado FechaCalculada ..", ex);
                response=null;
            }
        }
        return response;
    }
    
    
    /**
     * Obtiene el valor de un campo suplementario d expediente
     * @param codOrganizacion
     * @param codigoCampo
     * @param tipoCampo
     * @param numExpediente
     * @return String con el valor el campo, en caso de ser tipo fichero devuelve el contenido en CODIFICADO en Base64
     * @throws MeLanbide01Exception 
     */
    private String getCampoSuplementarioExpediente(Integer codOrganizacion, String codigoCampo,int tipoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioNumeroExpediente() : - BEGIN - " + codOrganizacion  + " - "  + codigoCampo + " - "  + tipoCampo + " - "  + numExpediente);
        String valor = "";
        try {
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            SalidaIntegracionVO salida = new SalidaIntegracionVO();
            CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
            salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion),
                    String.valueOf(Utilities.getEjercicioFromNumExpediente(numExpediente)),
                    numExpediente,
                    Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente),
                    codigoCampo, tipoCampo);
            if (salida.getStatus() == 0) {
                campoSuplementario = salida.getCampoSuplementario();
                if(Melanbide01TipoCampoSuplementarioEnum.NUMERICO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                        || Melanbide01TipoCampoSuplementarioEnum.NUMERICO_CALCULADO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorNumero();
                } else if(Melanbide01TipoCampoSuplementarioEnum.TEXTO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                        || Melanbide01TipoCampoSuplementarioEnum.TEXTO_LARGO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorTexto();
                } else if(Melanbide01TipoCampoSuplementarioEnum.FECHA.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorFechaAsString();
                } else if(Melanbide01TipoCampoSuplementarioEnum.FICHERO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = Base64.encode(campoSuplementario.getValorFichero());
                } else if(Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorDesplegable();
                } else if(Melanbide01TipoCampoSuplementarioEnum.FECHA_CALCULADA.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorFechaAsString();
                } else if(Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE_EXTERNO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))){
                    valor = campoSuplementario.getValorDesplegable();
                }
            }
        } catch (NumberFormatException ex) {
            log.error("getCampoSuplementarioNumeroExpediente - Error por tipo de fomato al convertir en numerico", ex);
            throw new MeLanbide01Exception("getCampoSuplementarioNumeroExpediente - Error por tipo de fomato al convertir en numerico", ex);
        } catch (Exception ex) {
            log.error("getCampoSuplementarioNumeroExpediente - ", ex);
            throw new MeLanbide01Exception("Error al ejecutar getCampoSuplementarioNumeroExpediente ", ex);
        }
        log.info("getCampoSuplementarioNumeroExpediente: " + codigoCampo + ": " + valor);
        return valor;
    }
    
    /**
     * Graba el valor de un campo suplementario, si existe, elimina dato anterior. Valor de ficheros debe venir en Codificado Base64
     * @param codOrganizacion
     * @param codigoCampo
     * @param valorCampo
     * @param tipoCampo
     * @param numExpediente
     * @return
     * @throws MeLanbide01Exception 
     */
    public boolean grabarCampoSuplementarioExpediente(Integer codOrganizacion, String codigoCampo,String valorCampo, int tipoCampo, String numExpediente) throws MeLanbide01Exception {
        log.info("grabarCampoSuplementarioExpediente() : - BEGIN - " + codOrganizacion  + " - "  + codigoCampo + " - "  + tipoCampo + " - "  + numExpediente);
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        try {
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
            campoSuplementario.setCodOrganizacion(String.valueOf(codOrganizacion));
            campoSuplementario.setEjercicio(String.valueOf(Utilities.getEjercicioFromNumExpediente(numExpediente)));
            campoSuplementario.setCodProcedimiento(Utilities.getCodigoProcedimientoFromNumExpediente(numExpediente));
            campoSuplementario.setNumExpediente(numExpediente);
            campoSuplementario.setCodigoCampo(codigoCampo);
            campoSuplementario.setTipoCampo(tipoCampo);
            
            if (Melanbide01TipoCampoSuplementarioEnum.NUMERICO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                    || Melanbide01TipoCampoSuplementarioEnum.NUMERICO_CALCULADO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))) {
                campoSuplementario.setValorNumero(valorCampo);
            } else if (Melanbide01TipoCampoSuplementarioEnum.TEXTO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                    || Melanbide01TipoCampoSuplementarioEnum.TEXTO_LARGO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))) {
                campoSuplementario.setValorTexto(valorCampo);
            } else if (Melanbide01TipoCampoSuplementarioEnum.FECHA.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                    || Melanbide01TipoCampoSuplementarioEnum.FECHA_CALCULADA.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))) {
                try {
                    Calendar response = Calendar.getInstance();
                    response.setTime(formatFecha_dd_MM_yyyy.parse(valorCampo));
                    campoSuplementario.setValorFecha(response);
                } catch (ParseException ex) {
                    log.error("Error Parsenado Fecha .. Al Guardar CS: " + ex.getMessage(), ex);
                }
            } else if (Melanbide01TipoCampoSuplementarioEnum.FICHERO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))) {
                campoSuplementario.setValorFichero(Base64.decode(valorCampo));
            } else if (Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))
                    || Melanbide01TipoCampoSuplementarioEnum.DESPLEGABLE_EXTERNO.getCodigo().equalsIgnoreCase(String.valueOf(tipoCampo))) {
                campoSuplementario.setValorDesplegable(valorCampo);
            }
            salida = el.grabarCampoSuplementario(campoSuplementario);
        } catch (NumberFormatException ex) {
            log.error("grabarCampoSuplementarioExpediente - Error por tipo de fomato al convertir en numerico", ex);
            throw new MeLanbide01Exception("getCampoSuplementarioNumeroExpediente - Error por tipo de fomato al convertir en numerico", ex);
        } catch (Exception ex) {
            log.error("grabarCampoSuplementarioExpediente - ", ex);
            throw new MeLanbide01Exception("Error al ejecutar getCampoSuplementarioNumeroExpediente ", ex);
        }
        log.info("grabarCampoSuplementarioExpediente: " + codigoCampo + ": " + valorCampo
         + " === "  + salida.getStatus() +" == " + salida.getDescStatus());
        return salida.getStatus() == 0;
    }

    
}

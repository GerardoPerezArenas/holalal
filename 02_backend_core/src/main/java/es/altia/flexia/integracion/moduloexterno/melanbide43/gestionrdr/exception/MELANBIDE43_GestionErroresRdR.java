/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.exception;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Util;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6ErrorBean;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MELANBIDE43_GestionErroresRdR {
    
    private static final Logger LOG = LogManager.getLogger(MELANBIDE43_GestionErroresRdR.class);
    
    public static int  grabarError(Lan6Excepcion ex, String codError, String descripcionErrorDetalle, String numExpediente, Integer idNotificacion, String idProcedimientoEnPlatea,String documentoRepresentante, String claseMetodoError) throws Exception {
        String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
        String mensajeExcepcion = prepararMensajeErrorCompleto(ex.getMessage(),ex.getMessages());
        String trazaError = ex.getTrazaExcepcion();
        Lan6ErrorBean eB = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                trazaError, "REGEXLAN",
                codError, descripcionErrorDetalle, ex.gettipologia());
        ErrorLan6ExcepcionBean eL6EB = new ErrorLan6ExcepcionBean(eB, ex);
        
        
        ErrorBean errorBean = new ErrorBean();
        int respuestaGrabarErrorInt=0;
        MELANBIDE43_GestionRdR_Util mELANBIDE43_GestionRdR_Util = MELANBIDE43_GestionRdR_Util.getInstance();  
        try {            
            errorBean.setMensajeError(eL6EB.getMessage()!=null && !eL6EB.getMessage().isEmpty()? eL6EB.getMessage() : descripcionErrorDetalle);
            // Mensaje lanzado por el error
            errorBean.setMensajeExcepError(eL6EB.getMensajeExcepcion()!=null? eL6EB.getMensajeExcepcion().replaceAll("'", "") : "Lan6Excepcion/MensajeExcepcion Recibido a Null");
            // Causa lanzada por el error
            errorBean.setCausa(eL6EB.getCausaExcepcion()!=null?eL6EB.getCausaExcepcion().replaceAll("'", ""):"Lan6Excepcion/CausaExcepcion Recibida a Null");
            // Traza lanzada por el error: límitar a 4000 caracteres
            String traza = eL6EB.getTrazaExcepcion()!=null?eL6EB.getTrazaExcepcion():"Lan6Excepcion/TrazaExcepcion Recibida a Null";
            traza=(traza!=null && !traza.isEmpty() ? limitarString(traza,4000).replaceAll("'", "") :"");
            errorBean.setTraza(traza);
            // Procedimiento donde se ha producido el error
            String idProcedimiento=mELANBIDE43_GestionRdR_Util.getCodigoProcedimientoDsdNumExpediente(numExpediente);
            idProcedimiento=(idProcedimiento !=null && !idProcedimiento.isEmpty() ? idProcedimiento + " - " + (idProcedimientoEnPlatea!=null && !idProcedimientoEnPlatea.isEmpty() ? idProcedimientoEnPlatea : "Codigo Procedimiento Platea no recuperado.") : (idProcedimientoEnPlatea!=null && !idProcedimientoEnPlatea.isEmpty() ? idProcedimientoEnPlatea : ""));
            errorBean.setIdProcedimiento(idProcedimiento);
            // Evento que produce el error
            String evento = "";
            errorBean.setEvento(evento);
            // Código del error
            errorBean.setIdError(codError);
            // Identificador del expediente en Flexia
            String identificadorExpediente = "";
            identificadorExpediente=(numExpediente!=null && !numExpediente.isEmpty()?numExpediente:"");
            identificadorExpediente=(idNotificacion!=null && idNotificacion > 0 ? (identificadorExpediente + " - Notificacion: " + idNotificacion) : identificadorExpediente );
            errorBean.setIdFlexia(identificadorExpediente);
            // Oid de solicitud
            errorBean.setIdClave("");
            // Sistema que ha producido el error
            errorBean.setSistemaOrigen("REGEXLAN");
            // Clase y método que graba el error
            errorBean.setSituacion(claseMetodoError);
            // Nombre del log donde se ha registrado
            String ficheroLog = mELANBIDE43_GestionRdR_Util.recogerNombreFicheroLog();
            errorBean.setErrorLog(ficheroLog);
            // Grabar error
            String respuestaGrabarError = RegistroErrores.registroError(errorBean);
            respuestaGrabarErrorInt= (respuestaGrabarError!=null && !respuestaGrabarError.isEmpty()) ? Integer.valueOf(respuestaGrabarError) : 0;
            LOG.error("respuestaGrabarError RegistroErrores.registroError() : " + respuestaGrabarError);
        } catch (Exception exG) {
            LOG.error("GestionarErroresDokusi - Error al registrar en BD Errores presentados en Al validar Representantes Legales contra RdR : " + exG.getMessage(), exG);
            throw exG;
        }
        return respuestaGrabarErrorInt;
    }

    private static String  prepararMensajeErrorCompleto(String message, ArrayList messages) {
        String mensajeCompleto ="";
        try {
            if(message != null && !message.isEmpty()){
                mensajeCompleto=message;
            }
            if(messages!=null && messages.size() > 0){
                for (int i = 0; i < messages.size(); i++) {
                    mensajeCompleto = (mensajeCompleto.isEmpty() ? (String) messages.get(i) : " - " + (String) messages.get(i));
                }
            }
        } catch (Exception e) {
            LOG.error("Error a procesar los mensajes de la excepcion : " + e.getMessage(), e);
            mensajeCompleto="";
        }
        return mensajeCompleto;
    }
    
    private static String limitarString(String trazaExcepcion, int limite) {
        String cadenaLimitada = "";
        if(trazaExcepcion!=null && !trazaExcepcion.isEmpty()){
            if(trazaExcepcion.length()>limite)
                cadenaLimitada = trazaExcepcion.substring(0, limite);
            else
                cadenaLimitada=trazaExcepcion;
        }
        return cadenaLimitada;
    }

}

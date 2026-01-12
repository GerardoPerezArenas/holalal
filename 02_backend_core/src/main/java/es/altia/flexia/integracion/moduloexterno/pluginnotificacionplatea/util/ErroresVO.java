/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.util;


import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.*;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ErroresVO {

    private int idRegError;
    private String mensajeError;
    private String mensajeExcepError;
    private String causa;
    private String traza;
    private String idProcedimiento;
    private String idError;
    private String idClave;
    private String idFlexia;
    private String observaciones;
    private String sistemaOrigen;
    private String situacion;
    private String errorLog;
    private String evento;

    public int getIdRegError() {
        return idRegError;
    }

    public void setIdRegError(int idRegError) {
        this.idRegError = idRegError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getMensajeExcepError() {

        return mensajeExcepError;
    }

    public void setMensajeExcepError(String mensajeExcepError) {
        this.mensajeExcepError = mensajeExcepError;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getTraza() {
        return traza;
    }

    public void setTraza(String traza) {
        this.traza = traza;
    }

    public String getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(String idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
    }

    public String getIdClave() {
        return idClave;
    }

    public void setIdClave(String idClave) {
        this.idClave = idClave;
    }

    public String getIdFlexia() {
        return idFlexia;
    }

    public void setIdFlexia(String idFlexia) {
        this.idFlexia = idFlexia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }
    
}

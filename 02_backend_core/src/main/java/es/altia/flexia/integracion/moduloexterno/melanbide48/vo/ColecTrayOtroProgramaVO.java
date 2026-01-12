/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class ColecTrayOtroProgramaVO {
    private Integer codIdOtroPrograma;
    private String tipoOtroPrograma; // De momento solo es uno.
    private Integer ejercicio;
    private String numExpediente;
    private Integer codEntidad;
    private String programa;
    private Date fechaInicio;
    private Date fechaFin;
    //Información Adicional de la entidad
    private String cifEntidad;
    private String nombreEntidad;

    public Integer getCodIdOtroPrograma() {
        return codIdOtroPrograma;
    }

    public void setCodIdOtroPrograma(Integer codIdOtroPrograma) {
        this.codIdOtroPrograma = codIdOtroPrograma;
    }

    public String getTipoOtroPrograma() {
        return tipoOtroPrograma;
    }

    public void setTipoOtroPrograma(String tipoOtroPrograma) {
        this.tipoOtroPrograma = tipoOtroPrograma;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Integer codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCifEntidad() {
        return cifEntidad;
    }

    public void setCifEntidad(String cifEntidad) {
        this.cifEntidad = cifEntidad;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    
}

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
public class ColecTrayActividadVO {
    
    private Integer codIdActividad;
    private String tipoActividad; // De momento solo es uno.
    private Integer ejercicio;
    private String numExpediente;
    private Integer codEntidad;
    private String desActividadyServPublEmp;
    private Date fechaInicio;
    private Date fechaFin;
    //Información Adicional de la entidad
    private String cifEntidad;
    private String nombreEntidad;

    public Integer getCodIdActividad() {
        return codIdActividad;
    }

    public void setCodIdActividad(Integer codIdActividad) {
        this.codIdActividad = codIdActividad;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
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

    public String getDesActividadyServPublEmp() {
        return desActividadyServPublEmp;
    }

    public void setDesActividadyServPublEmp(String desActividadyServPublEmp) {
        this.desActividadyServPublEmp = desActividadyServPublEmp;
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

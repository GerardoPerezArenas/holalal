/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.sql.Timestamp;

/**
 *
 * @author INGDGC
 */
public class MeLanbide43JobsTareaEje {
    private String nombreProcesoJob;
    private Integer numeroTarea;
    private Integer codigoUsuario;
    private String nombreUsuario;
    private Timestamp fechaHoraAltaPeticion;

    public String getNombreProcesoJob() {
        return nombreProcesoJob;
    }

    public void setNombreProcesoJob(String nombreProcesoJob) {
        this.nombreProcesoJob = nombreProcesoJob;
    }

    public Integer getNumeroTarea() {
        return numeroTarea;
    }

    public void setNumeroTarea(Integer numeroTarea) {
        this.numeroTarea = numeroTarea;
    }

    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Timestamp getFechaHoraAltaPeticion() {
        return fechaHoraAltaPeticion;
    }

    public void setFechaHoraAltaPeticion(Timestamp fechaHoraAltaPeticion) {
        this.fechaHoraAltaPeticion = fechaHoraAltaPeticion;
    }
    
    
}

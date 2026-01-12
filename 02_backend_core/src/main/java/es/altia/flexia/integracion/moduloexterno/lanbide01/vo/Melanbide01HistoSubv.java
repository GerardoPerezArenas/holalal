/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class Melanbide01HistoSubv {
    
    private Long id;
    private String numeroExpediente;
    private Date fechaFinInterrupSituacion;
    private Date fechaProrrReanudSituacion;
    private Date fechaHoraRegistro;
    private Date fechaHoraModificacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public Date getFechaFinInterrupSituacion() {
        return fechaFinInterrupSituacion;
    }

    public void setFechaFinInterrupSituacion(Date fechaFinInterrupSituacion) {
        this.fechaFinInterrupSituacion = fechaFinInterrupSituacion;
    }

    public Date getFechaProrrReanudSituacion() {
        return fechaProrrReanudSituacion;
    }

    public void setFechaProrrReanudSituacion(Date fechaProrrReanudSituacion) {
        this.fechaProrrReanudSituacion = fechaProrrReanudSituacion;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Date getFechaHoraModificacion() {
        return fechaHoraModificacion;
    }

    public void setFechaHoraModificacion(Date fechaHoraModificacion) {
        this.fechaHoraModificacion = fechaHoraModificacion;
    }
    
    
    
}

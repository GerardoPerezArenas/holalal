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
public class MeLanbide43JobsEjeInf {
    
private long id;
private int numeroTarea;
private int ejercicio;
private String procedimiento;
private String numeroExpediente;
private String nombreProcesoJob;
private Timestamp fechaHoraAltaPeticion;
private Timestamp fechaHoraModificaPeticion;
private int procesado;
private String resultado;
private String detalles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumeroTarea() {
        return numeroTarea;
    }

    public void setNumeroTarea(int numeroTarea) {
        this.numeroTarea = numeroTarea;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getNombreProcesoJob() {
        return nombreProcesoJob;
    }

    public void setNombreProcesoJob(String nombreProcesoJob) {
        this.nombreProcesoJob = nombreProcesoJob;
    }

    public Timestamp getFechaHoraAltaPeticion() {
        return fechaHoraAltaPeticion;
    }

    public void setFechaHoraAltaPeticion(Timestamp fechaHoraAltaPeticion) {
        this.fechaHoraAltaPeticion = fechaHoraAltaPeticion;
    }

    public Timestamp getFechaHoraModificaPeticion() {
        return fechaHoraModificaPeticion;
    }

    public void setFechaHoraModificaPeticion(Timestamp fechaHoraModificaPeticion) {
        this.fechaHoraModificaPeticion = fechaHoraModificaPeticion;
    }

    public int getProcesado() {
        return procesado;
    }

    public void setProcesado(int procesado) {
        this.procesado = procesado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
    
}

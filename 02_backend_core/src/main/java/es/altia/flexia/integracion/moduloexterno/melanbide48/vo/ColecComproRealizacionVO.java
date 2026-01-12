/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author INGDGC
 */
public class ColecComproRealizacionVO {
    
    private Integer codigoID;
    private Integer ejercicio;
    private String numExpediente;
    private Integer codigoEntidad;
    private Integer colectivo;
    private Integer territorioHistorico;
    private Double PorcentajeCompReal;
    // Adicionales para Interfaz Tabla (coger en join)
    private String nombreEntidad;

    public Integer getCodigoID() {
        return codigoID;
    }

    public void setCodigoID(Integer codigoID) {
        this.codigoID = codigoID;
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

    public Integer getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(Integer codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }
    
    public Integer getTerritorioHistorico() {
        return territorioHistorico;
    }

    public void setTerritorioHistorico(Integer territorioHistorico) {
        this.territorioHistorico = territorioHistorico;
    }

    public Double getPorcentajeCompReal() {
        return PorcentajeCompReal;
    }

    public void setPorcentajeCompReal(Double PorcentajeCompReal) {
        this.PorcentajeCompReal = PorcentajeCompReal;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }
    
    
}

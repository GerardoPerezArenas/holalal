/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 * Value Object de la tabla : Guarda numero de meses de la trayectoria validad por los tramitadores de acuerdo al colectivo.
 * @author INGDGC
 */
public class ColecTrayeEntiValida {
    
    private Integer id;
    private String numeroExpediente;
    private Integer idFkEntidad;
    private Integer idFkColectivo;
    private Double numeroMesesValidados;

    public ColecTrayeEntiValida() {
    }

    public ColecTrayeEntiValida(Integer id, String numeroExpediente, Integer idFkEntidad, Integer idFkColectivo, Double numeroMesesValidados) {
        this.id = id;
        this.numeroExpediente = numeroExpediente;
        this.idFkEntidad = idFkEntidad;
        this.idFkColectivo = idFkColectivo;
        this.numeroMesesValidados = numeroMesesValidados;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public Integer getIdFkEntidad() {
        return idFkEntidad;
    }

    public void setIdFkEntidad(Integer idFkEntidad) {
        this.idFkEntidad = idFkEntidad;
    }

    public Integer getIdFkColectivo() {
        return idFkColectivo;
    }

    public void setIdFkColectivo(Integer idFkColectivo) {
        this.idFkColectivo = idFkColectivo;
    }

    public Double getNumeroMesesValidados() {
        return numeroMesesValidados;
    }

    public void setNumeroMesesValidados(Double numeroMesesValidados) {
        this.numeroMesesValidados = numeroMesesValidados;
    }

    @Override
    public String toString() {
        return "ColecTrayeEntiValida{" + "id=" + id + ", numeroExpediente=" + numeroExpediente + ", idFkEntidad=" + idFkEntidad + ", idFkColectivo=" + idFkColectivo + ", numeroMesesValidados=" + numeroMesesValidados + '}';
    }
       
}

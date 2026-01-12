/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide77.vo;

/**
 *
 * @author davidl
 */
public class RegistroBatchVO {

    private Integer id;
    private String fecha;
    private Integer ejerRegistro;
    private Integer numRegistro;
    private Integer numSolicitud;
    private String numExpediente;
    private Integer codTramite;
    private String operacion;
    private String resultado;
    private Integer codOperacion;
    private Integer relanzar;
    private String observaciones;

    public RegistroBatchVO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getEjerRegistro() {
        return ejerRegistro;
    }

    public void setEjerRegistro(Integer ejerRegistro) {
        this.ejerRegistro = ejerRegistro;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Integer getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(Integer numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(Integer codTramite) {
        this.codTramite = codTramite;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Integer getCodOperacion() {
        return codOperacion;
    }

    public void setCodOperacion(Integer codOperacion) {
        this.codOperacion = codOperacion;
    }

    public Integer getRelanzar() {
        return relanzar;
    }

    public void setRelanzar(Integer relanzar) {
        this.relanzar = relanzar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}

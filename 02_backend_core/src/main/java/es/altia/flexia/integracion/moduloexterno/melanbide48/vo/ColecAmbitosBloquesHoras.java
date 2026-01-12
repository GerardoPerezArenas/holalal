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
public class ColecAmbitosBloquesHoras {
    private int id; 
    private int colectivo;
    private int idFkConvocatoriaActiva;
    private String ambitoDescripcion;
    private String ambitoDescripcionEu;
    private Double numeroBloqueshoras;
    private int numeroCentrosminimos;
    private boolean ubicImprescindibles;

    public ColecAmbitosBloquesHoras() {
    }

    public ColecAmbitosBloquesHoras(int id, int colectivo, int idFkConvocatoriaActiva, String ambitoDescripcion, String ambitoDescripcionEu, Double numeroBloqueshoras, int numeroCentrosminimos, boolean ubicImprescindibles) {
        this.id = id;
        this.colectivo = colectivo;
        this.idFkConvocatoriaActiva = idFkConvocatoriaActiva;
        this.ambitoDescripcion = ambitoDescripcion;
        this.ambitoDescripcionEu = ambitoDescripcionEu;
        this.numeroBloqueshoras = numeroBloqueshoras;
        this.numeroCentrosminimos = numeroCentrosminimos;
        this.ubicImprescindibles = ubicImprescindibles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColectivo() {
        return colectivo;
    }

    public void setColectivo(int colectivo) {
        this.colectivo = colectivo;
    }

    public int getIdFkConvocatoriaActiva() {
        return idFkConvocatoriaActiva;
    }

    public void setIdFkConvocatoriaActiva(int idFkConvocatoriaActiva) {
        this.idFkConvocatoriaActiva = idFkConvocatoriaActiva;
    }

    public String getAmbitoDescripcion() {
        return ambitoDescripcion;
    }

    public void setAmbitoDescripcion(String ambitoDescripcion) {
        this.ambitoDescripcion = ambitoDescripcion;
    }

    public String getAmbitoDescripcionEu() {
        return ambitoDescripcionEu;
    }

    public void setAmbitoDescripcionEu(String ambitoDescripcionEu) {
        this.ambitoDescripcionEu = ambitoDescripcionEu;
    }

    public Double getNumeroBloqueshoras() {
        return numeroBloqueshoras;
    }

    public void setNumeroBloqueshoras(Double numeroBloqueshoras) {
        this.numeroBloqueshoras = numeroBloqueshoras;
    }

    public int getNumeroCentrosminimos() {
        return numeroCentrosminimos;
    }

    public void setNumeroCentrosminimos(int numeroCentrosminimos) {
        this.numeroCentrosminimos = numeroCentrosminimos;
    }

    public boolean isUbicImprescindibles() {
        return ubicImprescindibles;
    }

    public void setUbicImprescindibles(boolean ubicImprescindibles) {
        this.ubicImprescindibles = ubicImprescindibles;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;

/**
 *
 * @author altia
 */
public class SMIVO {

    private Integer id;
    private String numExp;
    private Integer numLinea;
    private String nombre;
    private String apellidos;
    private Double numDiasSinIncidencias;
    private Double numDiasIncidencia;
    private String causaIncidencia;
    private String desCausaIncidencia;
    private Double importeSolicitado;
    private Double importeRecalculo;
    private Date fecha;
    private Double porcJornada;
    private Double porcReduccion;
    private Double importeLanbide;
    private String observaciones;
    private String nif;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

    public Double getNumDiasSinIncidencias() {
        return numDiasSinIncidencias;
    }

    public void setNumDiasSinIncidencias(Double numDiasSinIncidencias) {
        this.numDiasSinIncidencias = numDiasSinIncidencias;
    }

    public Double getNumDiasIncidencia() {
        return numDiasIncidencia;
    }

    public void setNumDiasIncidencia(Double numDiasIncidencia) {
        this.numDiasIncidencia = numDiasIncidencia;
    }

    public Double getImporteSolicitado() {
        return importeSolicitado;
    }

    public void setImporteSolicitado(Double importeSolicitado) {
        this.importeSolicitado = importeSolicitado;
    }

    public Double getImporteRecalculo() {
        return importeRecalculo;
    }

    public void setImporteRecalculo(Double importeRecalculo) {
        this.importeRecalculo = importeRecalculo;
    }

    public String getCausaIncidencia() {
        return causaIncidencia;
    }

    public void setCausaIncidencia(String causaIncidencia) {
        this.causaIncidencia = causaIncidencia;
    }

    public String getDesCausaIncidencia() {
        return desCausaIncidencia;
    }

    public void setDesCausaIncidencia(String desCausaIncidencia) {
        this.desCausaIncidencia = desCausaIncidencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getPorcJornada() {
        return porcJornada;
    }

    public void setPorcJornada(Double porcJornada) {
        this.porcJornada = porcJornada;
    }

    public Double getPorcReduccion() {
        return porcReduccion;
    }

    public void setPorcReduccion(Double porcReduccion) {
        this.porcReduccion = porcReduccion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getImporteLanbide() {
        return importeLanbide;
    }

    public void setImporteLanbide(Double importeLanbide) {
        this.importeLanbide = importeLanbide;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

}

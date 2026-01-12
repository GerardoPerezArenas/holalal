/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;

/**
 *
 * @author kepa
 */
public class PensionistaVO {

    private Integer id;
    private String numExp;
    private Integer numLinea;
    private String nombre;
    private String apellidos;
    private String nif;
    private String tipoContrato;
    private String tipoContratoDesc;
    private String tipoContDurac;
    private String tipoContDuracDesc;
    private Date fechaIni;
    private Date fechaFin;
    private Date fechaEfecInss;
    private Date fechaEfecSinE;
    private Date fechaEfecConE;
    private Double gradoDisc;
    private Double porcJornada;
    

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

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getTipoContratoDesc() {
        return tipoContratoDesc;
    }

    public void setTipoContratoDesc(String tipoContratoDesc) {
        this.tipoContratoDesc = tipoContratoDesc;
    }

    public String getTipoContDurac() {
        return tipoContDurac;
    }

    public void setTipoContDurac(String tipoContDurac) {
        this.tipoContDurac = tipoContDurac;
    }

    public String getTipoContDuracDesc() {
        return tipoContDuracDesc;
    }

    public void setTipoContDuracDesc(String tipoContDuracDesc) {
        this.tipoContDuracDesc = tipoContDuracDesc;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaEfecInss() {
        return fechaEfecInss;
    }

    public void setFechaEfecInss(Date fechaEfecInss) {
        this.fechaEfecInss = fechaEfecInss;
    }

    public Date getFechaEfecSinE() {
        return fechaEfecSinE;
    }

    public void setFechaEfecSinE(Date fechaEfecSinE) {
        this.fechaEfecSinE = fechaEfecSinE;
    }

    public Date getFechaEfecConE() {
        return fechaEfecConE;
    }

    public void setFechaEfecConE(Date fechaEfecConE) {
        this.fechaEfecConE = fechaEfecConE;
    }

    public Double getGradoDisc() {
        return gradoDisc;
    }

    public void setGradoDisc(Double gradoDisc) {
        this.gradoDisc = gradoDisc;
    }

    public Double getPorcJornada() {
        return porcJornada;
    }

    public void setPorcJornada(Double porcJornada) {
        this.porcJornada = porcJornada;
    }

}

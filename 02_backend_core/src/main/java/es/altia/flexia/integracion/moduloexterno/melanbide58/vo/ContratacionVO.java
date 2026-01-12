/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;

/**
 *
 * @author sergio
 */
public class ContratacionVO {
    
    private Integer id;
    private String numExp;
    private Integer numLinea;
    private String nif;
    private Date fechaAlta;
    private Date fechaBaja;
    private Double porcJornada;
    private Integer claveContrato;
    private Boolean docVerificada;
    private Boolean docValidada;
    private String motNoVal;

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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Double getPorcJornada() {
        return porcJornada;
    }

    public void setPorcJornada(Double porcJornada) {
        this.porcJornada = porcJornada;
    }

    public Integer getClaveContrato() {
        return claveContrato;
    }

    public void setClaveContrato(Integer claveContrato) {
        this.claveContrato = claveContrato;
    }

    public String getMotNoVal() {
        return motNoVal;
    }

    public void setMotNoVal(String motNoVal) {
        this.motNoVal = motNoVal;
    }

    public Boolean getDocVerificada() {
        return docVerificada;
    }

    public void setDocVerificada(Boolean docVerificada) {
        this.docVerificada = docVerificada;
    }

    public Boolean getDocValidada() {
        return docValidada;
    }

    public void setDocValidada(Boolean docValidada) {
        this.docValidada = docValidada;
    }
    
    
    
    
    
}

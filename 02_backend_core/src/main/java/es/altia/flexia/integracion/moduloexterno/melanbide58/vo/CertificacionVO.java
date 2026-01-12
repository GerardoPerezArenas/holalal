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
public class CertificacionVO {

    private Integer id;
    private String numExp;
    private Integer numLinea;
    private Date fechaCertMes;
    private Double plantMediaMes;
    private Date fechaCert12;
    private Double plantMedia12;

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

    public Date getFechaCertMes() {
        return fechaCertMes;
    }

    public void setFechaCertMes(Date fechaCertMes) {
        this.fechaCertMes = fechaCertMes;
    }

    public Double getPlantMediaMes() {
        return plantMediaMes;
    }

    public void setPlantMediaMes(Double plantMediaMes) {
        this.plantMediaMes = plantMediaMes;
    }

    public Date getFechaCert12() {
        return fechaCert12;
    }

    public void setFechaCert12(Date fechaCert12) {
        this.fechaCert12 = fechaCert12;
    }

    public Double getPlantMedia12() {
        return plantMedia12;
    }

    public void setPlantMedia12(Double plantMedia12) {
        this.plantMedia12 = plantMedia12;
    }
    
    
}

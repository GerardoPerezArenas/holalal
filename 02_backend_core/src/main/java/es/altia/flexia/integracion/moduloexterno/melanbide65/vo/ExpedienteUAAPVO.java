/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide65.vo;

import java.math.BigDecimal;

/**
 *
 * @author Isabel
 */
public class ExpedienteUAAPVO {
    private String numeroExpediente;
    private String nombreInteresado;
    private String CP;
    private BigDecimal importeConini;
    private String importeTotalRecal;
    
    
    // Numero de registros obtenidos en las consultas -- se pone para recuperar datos de la paginacio
    private Integer noTotalRegConsulta;
    private Integer noRegEnLaConsulta;
    
    

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
 
    public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }
    
    public BigDecimal getImporteConini() {
        return importeConini;
    }

    public void setImporteConini(BigDecimal importeConini) {
        this.importeConini = importeConini;
    }
    
     public String getImporteTotalRecal() {
        return importeTotalRecal;
    }

    public void setImporteTotalRecal(String importeTotalRecal) {
        this.importeTotalRecal = importeTotalRecal;
    }

    public Integer getNoTotalRegConsulta() {
        return noTotalRegConsulta;
    }

    public void setNoTotalRegConsulta(Integer noTotalRegConsulta) {
        this.noTotalRegConsulta = noTotalRegConsulta;
    }

    public Integer getNoRegEnLaConsulta() {
        return noRegEnLaConsulta;
    }

    public void setNoRegEnLaConsulta(Integer noRegEnLaConsulta) {
        this.noRegEnLaConsulta = noRegEnLaConsulta;
    }
    
    
}

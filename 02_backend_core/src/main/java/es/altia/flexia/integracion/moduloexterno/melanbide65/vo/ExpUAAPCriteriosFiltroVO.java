/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide65.vo;

/**
 *
 * @author Isabel
 */
public class ExpUAAPCriteriosFiltroVO {
    
    private String ejercicio;
    private String tipoProcedimiento;
    private String periodoSubvencionable;
    
    // Par�metros para paginaci�n
    private Integer numeroLineasxPagina;
    private Integer paginaActual;
    private Integer numeroInicialLinea;
    private Integer numeroFinalLinea;


    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }
    
    public String getTipoProcedimiento() {
        return tipoProcedimiento;
    }

    public void setTipoProcedimiento(String tipoProcedimiento) {
        this.tipoProcedimiento = tipoProcedimiento;
    }
    
     public String getPeriodoSubvencionable() {
        return periodoSubvencionable;
    }

    public void setPeriodoSubvencionable(String periodoSubvencionable) {
        this.periodoSubvencionable = periodoSubvencionable;
    }

    public Integer getNumeroLineasxPagina() {
        return numeroLineasxPagina;
    }

    public void setNumeroLineasxPagina(Integer numeroLineasxPagina) {
        this.numeroLineasxPagina = numeroLineasxPagina;
    }

    public Integer getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(Integer paginaActual) {
        this.paginaActual = paginaActual;
    }

    public Integer getNumeroInicialLinea() {
        return numeroInicialLinea;
    }

    public void setNumeroInicialLinea(Integer numeroInicialLinea) {
        this.numeroInicialLinea = numeroInicialLinea;
    }

    public Integer getNumeroFinalLinea() {
        return numeroFinalLinea;
    }

    public void setNumeroFinalLinea(Integer numeroFinalLinea) {
        this.numeroFinalLinea = numeroFinalLinea;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class EcaConfiguracionVO 
{
    private Integer ano;
    private BigDecimal poMaxSeguimientos;
    private BigDecimal imSeguimiento;
    private BigDecimal poGastos;
    private BigDecimal imC1h;
    private BigDecimal imC1m;
    private BigDecimal imC2h;
    private BigDecimal imC2m;
    private BigDecimal imC3h;
    private BigDecimal imC3m;
    private BigDecimal imC4h;
    private BigDecimal imC4m;
    private Integer minEmpVisit;
    private Integer maxEmpVisit;
    private BigDecimal impVisita;
    
    public EcaConfiguracionVO()
    {
     
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public BigDecimal getPoMaxSeguimientos() {
        return poMaxSeguimientos;
    }

    public void setPoMaxSeguimientos(BigDecimal poMaxSeguimientos) {
        this.poMaxSeguimientos = poMaxSeguimientos;
    }

    public BigDecimal getImSeguimiento() {
        return imSeguimiento;
    }

    public void setImSeguimiento(BigDecimal imSeguimiento) {
        this.imSeguimiento = imSeguimiento;
    }

    public BigDecimal getPoGastos() {
        return poGastos;
    }

    public void setPoGastos(BigDecimal poGastos) {
        this.poGastos = poGastos;
    }

    public BigDecimal getImC1h() {
        return imC1h;
    }

    public void setImC1h(BigDecimal imC1h) {
        this.imC1h = imC1h;
    }

    public BigDecimal getImC1m() {
        return imC1m;
    }

    public void setImC1m(BigDecimal imC1m) {
        this.imC1m = imC1m;
    }

    public BigDecimal getImC2h() {
        return imC2h;
    }

    public void setImC2h(BigDecimal imC2h) {
        this.imC2h = imC2h;
    }

    public BigDecimal getImC2m() {
        return imC2m;
    }

    public void setImC2m(BigDecimal imC2m) {
        this.imC2m = imC2m;
    }

    public BigDecimal getImC3h() {
        return imC3h;
    }

    public void setImC3h(BigDecimal imC3h) {
        this.imC3h = imC3h;
    }

    public BigDecimal getImC3m() {
        return imC3m;
    }

    public void setImC3m(BigDecimal imC3m) {
        this.imC3m = imC3m;
    }

    public BigDecimal getImC4h() {
        return imC4h;
    }

    public void setImC4h(BigDecimal imC4h) {
        this.imC4h = imC4h;
    }

    public BigDecimal getImC4m() {
        return imC4m;
    }

    public void setImC4m(BigDecimal imC4m) {
        this.imC4m = imC4m;
    }

    public Integer getMinEmpVisit() {
        return minEmpVisit;
    }

    public void setMinEmpVisit(Integer minEmpVisit) {
        this.minEmpVisit = minEmpVisit;
    }

    public Integer getMaxEmpVisit() {
        return maxEmpVisit;
    }

    public void setMaxEmpVisit(Integer maxEmpVisit) {
        this.maxEmpVisit = maxEmpVisit;
    }

    public BigDecimal getImpVisita() {
        return impVisita;
    }

    public void setImpVisita(BigDecimal impVisita) {
        this.impVisita = impVisita;
    }
}

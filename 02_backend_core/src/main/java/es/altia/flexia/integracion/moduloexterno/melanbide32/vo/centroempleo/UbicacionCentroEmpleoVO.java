/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class UbicacionCentroEmpleoVO 
{
    private Long oriUbiCod;
    private Integer oriUbicAno;
    private Integer oriAmbCod;
    private Integer munPai;
    private Integer munPrv;
    private Integer munCod;
    private Integer prvPai;
    
    public UbicacionCentroEmpleoVO()
    {
        
    }

    public Long getOriUbiCod() {
        return oriUbiCod;
    }

    public void setOriUbiCod(Long oriUbiCod) {
        this.oriUbiCod = oriUbiCod;
    }

    public Integer getOriUbicAno() {
        return oriUbicAno;
    }

    public void setOriUbicAno(Integer oriUbicAno) {
        this.oriUbicAno = oriUbicAno;
    }

    public Integer getOriAmbCod() {
        return oriAmbCod;
    }

    public void setOriAmbCod(Integer oriAmbCod) {
        this.oriAmbCod = oriAmbCod;
    }

    public Integer getMunPai() {
        return munPai;
    }

    public void setMunPai(Integer munPai) {
        this.munPai = munPai;
    }

    public Integer getMunPrv() {
        return munPrv;
    }

    public void setMunPrv(Integer munPrv) {
        this.munPrv = munPrv;
    }

    public Integer getMunCod() {
        return munCod;
    }

    public void setMunCod(Integer munCod) {
        this.munCod = munCod;
    }

    public Integer getPrvPai() {
        return prvPai;
    }

    public void setPrvPai(Integer prvPai) {
        this.prvPai = prvPai;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class S75PagosVO 
{
    private Long pagMun;
    private String pagPro;
    private Long pagEje;
    private String pagNum;
    private String pagConcep;
    private String pagAnoPago;
    private Long pagNumpago;
    
    private Date pagFecpag;
    private Date pagFecvcto;
    private BigDecimal pagImpanu;
    private BigDecimal pagImpcon;
    private BigDecimal pagImppag;
    private BigDecimal pagImprec;
    private BigDecimal pagImprei;
    
    public S75PagosVO()
    {

    }

    public Long getPagMun() {
        return pagMun;
    }

    public void setPagMun(Long pagMun) {
        this.pagMun = pagMun;
    }

    public String getPagPro() {
        return pagPro;
    }

    public void setPagPro(String pagPro) {
        this.pagPro = pagPro;
    }

    public Long getPagEje() {
        return pagEje;
    }

    public void setPagEje(Long pagEje) {
        this.pagEje = pagEje;
    }

    public String getPagNum() {
        return pagNum;
    }

    public void setPagNum(String pagNum) {
        this.pagNum = pagNum;
    }

    public String getPagConcep() {
        return pagConcep;
    }

    public void setPagConcep(String pagConcep) {
        this.pagConcep = pagConcep;
    }

    public String getPagAnoPago() {
        return pagAnoPago;
    }

    public void setPagAnoPago(String pagAnoPago) {
        this.pagAnoPago = pagAnoPago;
    }

    public Long getPagNumpago() {
        return pagNumpago;
    }

    public void setPagNumpago(Long pagNumpago) {
        this.pagNumpago = pagNumpago;
    }

    public Date getPagFecpag() {
        return pagFecpag;
    }

    public void setPagFecpag(Date pagFecpag) {
        this.pagFecpag = pagFecpag;
    }

    public Date getPagFecvcto() {
        return pagFecvcto;
    }

    public void setPagFecvcto(Date pagFecvcto) {
        this.pagFecvcto = pagFecvcto;
    }

    public BigDecimal getPagImpanu() {
        return pagImpanu;
    }

    public void setPagImpanu(BigDecimal pagImpanu) {
        this.pagImpanu = pagImpanu;
    }

    public BigDecimal getPagImpcon() {
        return pagImpcon;
    }

    public void setPagImpcon(BigDecimal pagImpcon) {
        this.pagImpcon = pagImpcon;
    }

    public BigDecimal getPagImppag() {
        return pagImppag;
    }

    public void setPagImppag(BigDecimal pagImppag) {
        this.pagImppag = pagImppag;
    }

    public BigDecimal getPagImprec() {
        return pagImprec;
    }

    public void setPagImprec(BigDecimal pagImprec) {
        this.pagImprec = pagImprec;
    }

    public BigDecimal getPagImprei() {
        return pagImprei;
    }

    public void setPagImprei(BigDecimal pagImprei) {
        this.pagImprei = pagImprei;
    }
}

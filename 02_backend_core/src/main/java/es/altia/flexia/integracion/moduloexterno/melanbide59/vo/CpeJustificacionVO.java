/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide59.vo;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class CpeJustificacionVO 
{
    private Integer idJustificacion;
    private Integer idOferta;
    private String codPuesto;
    private String numExpediente;
    private Integer ejercicio;
    private String codEstado;
    private String flVariosTrab;
    private BigDecimal impJustificado;
    private BigDecimal salarioSub;
    //private BigDecimal dietasJusti;
    //private BigDecimal gastosGestion;
    private BigDecimal bonif;
    
    private BigDecimal minoracion;
    private BigDecimal baseCC;
    private BigDecimal salario;
    private BigDecimal baseAT;
    private BigDecimal coeficienteApli;
    private BigDecimal porcFogasa;
    private BigDecimal porcCoeficiente;
    private BigDecimal porcAportacion;
    private Integer diasTrab;
    private Integer diasSegSoc;
    private BigDecimal mesesExt;
    private BigDecimal gastosVisado;
    private BigDecimal gastosSeguro;
    private BigDecimal aportEpsv;
    
    public CpeJustificacionVO()
    {
        
    }

    public Integer getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(Integer idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public Integer getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Integer idOferta) {
        this.idOferta = idOferta;
    }

    public String getCodPuesto() {
        return codPuesto;
    }

    public void setCodPuesto(String codPuesto) {
        this.codPuesto = codPuesto;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getFlVariosTrab() {
        return flVariosTrab;
    }

    public void setFlVariosTrab(String flVariosTrab) {
        this.flVariosTrab = flVariosTrab;
    }

    public BigDecimal getImpJustificado() {
        return impJustificado;
    }

    public void setImpJustificado(BigDecimal impJustificado) {
        this.impJustificado = impJustificado;
    }
    
    public BigDecimal getSalarioSub() {
        return salarioSub;
    }
    public void setSalarioSub(BigDecimal salarioSub) {
        this.salarioSub = salarioSub;
    }
    
    public BigDecimal getGastosVisado() {
        return gastosVisado;
    }
    public void setGastosVisado(BigDecimal gastosVisado) {
        this.gastosVisado = gastosVisado;
    }
    
    public BigDecimal getGastosSeguro() {
        return gastosSeguro;
    }
    public void setGastosSeguro(BigDecimal gastosSeguro) {
        this.gastosSeguro = gastosSeguro;
    }
    
    public BigDecimal getBonif() {
        return bonif;
    }

    public void setBonif(BigDecimal bonif) {
        this.bonif = bonif;
    }

    public BigDecimal getBaseCC() {
        return baseCC;
    }

    public void setBaseCC(BigDecimal baseCC) {
        this.baseCC = baseCC;
    }

    public BigDecimal getBaseAT() {
        return baseAT;
    }

    public void setBaseAT(BigDecimal baseAT) {
        this.baseAT = baseAT;
    }

    public BigDecimal getCoeficienteApli() {
        return coeficienteApli;
    }

    public void setCoeficienteApli(BigDecimal coeficienteApli) {
        this.coeficienteApli = coeficienteApli;
    }

    public BigDecimal getPorcFogasa() {
        return porcFogasa;
    }

    public void setPorcFogasa(BigDecimal porcFogasa) {
        this.porcFogasa = porcFogasa;
    }

    public BigDecimal getPorcCoeficiente() {
        return porcCoeficiente;
    }

    public void setPorcCoeficiente(BigDecimal porcCoeficiente) {
        this.porcCoeficiente = porcCoeficiente;
    }

    public BigDecimal getPorcAportacion() {
        return porcAportacion;
    }

    public void setPorcAportacion(BigDecimal porcAportacion) {
        this.porcAportacion = porcAportacion;
    }

    public BigDecimal getMinoracion() {
        return minoracion;
    }

    public void setMinoracion(BigDecimal minoracion) {
        this.minoracion = minoracion;
    }
    
    

    public Integer getDiasTrab() {
        return diasTrab;
    }

    public void setDiasTrab(Integer diasTrab) {
        this.diasTrab = diasTrab;
    }
    

    public Integer getDiasSegSoc() {
        return diasSegSoc;
    }

    public void setDiasSegSoc(Integer diasSegSoc) {
        this.diasSegSoc = diasSegSoc;
    }
    
    public BigDecimal getMesesExt() {
        return mesesExt;
    }

    public void setMesesExt(BigDecimal mesesExt) {
        this.mesesExt = mesesExt;
    }
    
    
    
    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    
    public BigDecimal getAportEpsv() {
        return aportEpsv;
    }

    public void setAportEpsv(BigDecimal aportEpsv) {
        this.aportEpsv = aportEpsv;
    }
}

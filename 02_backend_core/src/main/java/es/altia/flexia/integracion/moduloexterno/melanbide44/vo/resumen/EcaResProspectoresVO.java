/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class EcaResProspectoresVO 
{
   private Long ecaResProspectoresCod;
   private String ecaNumExp;
   private String nif;
   private String nombre;
   private BigDecimal gastosSalarialesSolicitados;
   private BigDecimal gastosSalarialesConcedidos;
   private BigDecimal gastosSalarialesJustificados;
   private Integer visitasConcedidas;
   private Integer visitasJustificadas;
   private BigDecimal importeVisitas;
   private BigDecimal aPagar;
   private Date fecSysdate;
   
   public EcaResProspectoresVO()
   {
       
   }

    public Long getEcaResProspectoresCod() {
        return ecaResProspectoresCod;
    }

    public void setEcaResProspectoresCod(Long ecaResProspectoresCod) {
        this.ecaResProspectoresCod = ecaResProspectoresCod;
    }

    public String getEcaNumExp() {
        return ecaNumExp;
    }

    public void setEcaNumExp(String ecaNumExp) {
        this.ecaNumExp = ecaNumExp;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getGastosSalarialesSolicitados() {
        return gastosSalarialesSolicitados;
    }

    public void setGastosSalarialesSolicitados(BigDecimal gastosSalarialesSolicitados) {
        this.gastosSalarialesSolicitados = gastosSalarialesSolicitados;
    }

    public BigDecimal getGastosSalarialesConcedidos() {
        return gastosSalarialesConcedidos;
    }

    public void setGastosSalarialesConcedidos(BigDecimal gastosSalarialesConcedidos) {
        this.gastosSalarialesConcedidos = gastosSalarialesConcedidos;
    }

    public BigDecimal getGastosSalarialesJustificados() {
        return gastosSalarialesJustificados;
    }

    public void setGastosSalarialesJustificados(BigDecimal gastosSalarialesJustificados) {
        this.gastosSalarialesJustificados = gastosSalarialesJustificados;
    }

    public Integer getVisitasConcedidas() {
        return visitasConcedidas;
    }

    public void setVisitasConcedidas(Integer visitasConcedidas) {
        this.visitasConcedidas = visitasConcedidas;
    }

    public Integer getVisitasJustificadas() {
        return visitasJustificadas;
    }

    public void setVisitasJustificadas(Integer visitasJustificadas) {
        this.visitasJustificadas = visitasJustificadas;
    }

    public BigDecimal getImporteVisitas() {
        return importeVisitas;
    }

    public void setImporteVisitas(BigDecimal importeVisitas) {
        this.importeVisitas = importeVisitas;
    }

    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }    
}

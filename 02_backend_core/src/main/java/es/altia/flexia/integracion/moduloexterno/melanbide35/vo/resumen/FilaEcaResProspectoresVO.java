/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class FilaEcaResProspectoresVO 
{
   private Long ecaResProspectoresCod;
   private Long ecaJusProspectoresCod;
   private String numExp;
   private String nif;
   private String nombre;
   private String gastosSalarialesSolicitados;
   private String gastosSalarialesConcedidos;
   private String gastosSalarialesJustificados;
   private String visitasConcedidas;
   private String importeVisitasConc;
   private String visitasJustificadas;
   private String importeVisitas;
   private String impPagar;
   private String tipoSust;
   
   public FilaEcaResProspectoresVO()
   {
       
   }

    public Long getEcaResProspectoresCod() {
        return ecaResProspectoresCod;
    }

    public void setEcaResProspectoresCod(Long ecaResProspectoresCod) {
        this.ecaResProspectoresCod = ecaResProspectoresCod;
    }

    public Long getEcaJusProspectoresCod() {
        return ecaJusProspectoresCod;
    }

    public void setEcaJusProspectoresCod(Long ecaJusProspectoresCod) {
        this.ecaJusProspectoresCod = ecaJusProspectoresCod;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
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

    public String getGastosSalarialesSolicitados() {
        return gastosSalarialesSolicitados;
    }

    public void setGastosSalarialesSolicitados(String gastosSalarialesSolicitados) {
        this.gastosSalarialesSolicitados = gastosSalarialesSolicitados;
    }

    public String getGastosSalarialesConcedidos() {
        return gastosSalarialesConcedidos;
    }

    public void setGastosSalarialesConcedidos(String gastosSalarialesConcedidos) {
        this.gastosSalarialesConcedidos = gastosSalarialesConcedidos;
    }

    public String getGastosSalarialesJustificados() {
        return gastosSalarialesJustificados;
    }

    public void setGastosSalarialesJustificados(String gastosSalarialesJustificados) {
        this.gastosSalarialesJustificados = gastosSalarialesJustificados;
    }

    public String getVisitasConcedidas() {
        return visitasConcedidas;
    }

    public void setVisitasConcedidas(String visitasConcedidas) {
        this.visitasConcedidas = visitasConcedidas;
    }

    public String getImporteVisitasConc() {
        return importeVisitasConc;
    }

    public void setImporteVisitasConc(String importeVisitasConc) {
        this.importeVisitasConc = importeVisitasConc;
    }

    public String getVisitasJustificadas() {
        return visitasJustificadas;
    }

    public void setVisitasJustificadas(String visitasJustificadas) {
        this.visitasJustificadas = visitasJustificadas;
    }

    public String getImporteVisitas() {
        return importeVisitas;
    }

    public void setImporteVisitas(String importeVisitas) {
        this.importeVisitas = importeVisitas;
    }

    public String getImpPagar() {
        return impPagar;
    }

    public void setImpPagar(String impPagar) {
        this.impPagar = impPagar;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }
}

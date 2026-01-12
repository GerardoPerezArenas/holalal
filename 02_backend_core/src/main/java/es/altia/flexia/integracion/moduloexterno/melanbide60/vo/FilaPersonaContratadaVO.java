/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide60.vo;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class FilaPersonaContratadaVO 
{
    private Integer idOferta;
    private Integer idJustificacion;
    private String nif;
    private String nomApel;
    private String feDesde;
    private String feHasta;
    private String impJustif;
    private String impConcedido;
    private String salarioSub;
    private String dietasJusti;
    private String gastosGestion;
    private String bonif;
    
    public FilaPersonaContratadaVO()
    {
        
    }

    public Integer getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Integer idOferta) {
        this.idOferta = idOferta;
    }

    public Integer getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(Integer idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNomApel() {
        return nomApel;
    }

    public void setNomApel(String nomApel) {
        this.nomApel = nomApel;
    }

    public String getFeDesde() {
        return feDesde;
    }

    public void setFeDesde(String feDesde) {
        this.feDesde = feDesde;
    }

    public String getFeHasta() {
        return feHasta;
    }

    public void setFeHasta(String feHasta) {
        this.feHasta = feHasta;
    }

    public String getImpJustif() {
        return impJustif;
    }

    public void setImpJustif(String impJustif) {
        this.impJustif = impJustif;
    }

    public String getImpConcedido() {
        return impConcedido;
    }

    public void setImpConcedido(String impConcedido) {
        this.impConcedido = impConcedido;
    }
    
    public String getSalarioSub() {
        return salarioSub;
    }
    public void setSalarioSub(String salarioSub) {
        this.salarioSub = salarioSub;
    }
    
    public String getDietasJusti() {
        return dietasJusti;
    }
    public void setDietasJusti(String dietasJusti) {
        this.dietasJusti = dietasJusti;
    }
    
    public String getGastosGestion() {
        return gastosGestion;
    }
    public void setGastosGestion(String gastosGestion) {
        this.gastosGestion = gastosGestion;
    }
    
    public String getBonif() {
        return bonif;
    }

    public void setBonif(String bonif) {
        this.bonif = bonif;
    }
}

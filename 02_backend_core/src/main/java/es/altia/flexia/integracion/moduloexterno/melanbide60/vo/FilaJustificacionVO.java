/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide60.vo;

/**
 *
 * @author santiagoc
 */
public class FilaJustificacionVO 
{
    private Integer idJustificacion;
    private Integer idOferta;
    private String codPuesto;
    private String descPuesto;
    private String codOferta;
    private String impSolic;
    private String impJustif;
    private String numContrataciones;
    private String estado;
    private String salarioSub;
    private String dietasJusti;
    private String gastosGestion;
    private String bonif;
    
    public FilaJustificacionVO()
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

    public String getDescPuesto() {
        return descPuesto;
    }

    public void setDescPuesto(String descPuesto) {
        this.descPuesto = descPuesto;
    }

    public String getCodOferta() {
        return codOferta;
    }

    public void setCodOferta(String codOferta) {
        this.codOferta = codOferta;
    }

    public String getImpSolic() {
        return impSolic;
    }

    public void setImpSolic(String impSolic) {
        this.impSolic = impSolic;
    }

    public String getImpJustif() {
        return impJustif;
    }

    public void setImpJustif(String impJustif) {
        this.impJustif = impJustif;
    }

    public String getNumContrataciones() {
        return numContrataciones;
    }

    public void setNumContrataciones(String numContrataciones) {
        this.numContrataciones = numContrataciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

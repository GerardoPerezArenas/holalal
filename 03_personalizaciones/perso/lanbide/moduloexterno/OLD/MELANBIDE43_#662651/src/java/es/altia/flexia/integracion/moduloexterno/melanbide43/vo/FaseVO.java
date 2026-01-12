/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author javier
 */
public class FaseVO {
   private String codProcedimiento;
   private String codTramite;
   private String codFase;
   private String descFaseCas;
   private String descFaseEus;
   
     /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }
    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(String codTramite) {
        this.codTramite = codTramite;
    }

    public String getCodFase() {
        return codFase;
    }

    public void setCodFase(String codFase) {
        this.codFase = codFase;
    }

    public String getDescFaseCas() {
        return descFaseCas;
    }

    public void setDescFaseCas(String descFaseCas) {
        this.descFaseCas = descFaseCas;
    }

    public String getDescFaseEus() {
        return descFaseEus;
    }

    public void setDescFaseEus(String descFaseEus) {
        this.descFaseEus = descFaseEus;
    }

   
}
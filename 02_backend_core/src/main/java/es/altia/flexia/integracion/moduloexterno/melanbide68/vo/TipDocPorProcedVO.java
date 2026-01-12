/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68.vo;

/**
 *
 * @author davidl
 */
public class TipDocPorProcedVO {

    private String procedimiento;
    private int tipoDocumental;
    private String tipoDoc_es;
    private String tipoDoc_eu;
    
    public TipDocPorProcedVO() {
    }    

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public int getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(int tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }

    public String getTipoDoc_es() {
        return tipoDoc_es;
    }

    public void setTipoDoc_es(String tipoDoc_es) {
        this.tipoDoc_es = tipoDoc_es;
    }

    public String getTipoDoc_eu() {
        return tipoDoc_eu;
    }

    public void setTipoDoc_eu(String tipoDoc_eu) {
        this.tipoDoc_eu = tipoDoc_eu;
    }
    
}

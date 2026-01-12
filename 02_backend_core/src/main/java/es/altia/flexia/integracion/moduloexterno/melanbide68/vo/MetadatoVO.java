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
public class MetadatoVO {
    private int tipoDocumental;
    private String metadato;
    private String metadatoDCTM; 
    private String obligatorio;
    private String fecha_baja;
    
    public MetadatoVO()
    {
    }

    public int getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(int tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }

    public String getMetadato() {
        return metadato;
    }

    public void setMetadato(String metadato) {
        this.metadato = metadato;
    }
    
    public String getMetadatoDCTM() {
        return metadatoDCTM;
    }

    public void setMetadatoDCTM(String metadatoDCTM) {
        this.metadatoDCTM = metadatoDCTM;
    }

    public String getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(String obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getFecha_baja() {
        return fecha_baja;
    }

    public void setFecha_baja(String fecha_baja) {
        this.fecha_baja = fecha_baja;
    }
    
}

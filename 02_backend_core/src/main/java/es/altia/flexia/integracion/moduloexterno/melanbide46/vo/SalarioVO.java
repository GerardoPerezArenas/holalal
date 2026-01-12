/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide46.vo;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class SalarioVO 
{
    private Integer codPai;
    private String codGrCot;
    private String descripcion;
    private BigDecimal importe;
    
    public SalarioVO()
    {
        
    }

    public Integer getCodPai() {
        return codPai;
    }

    public void setCodPai(Integer codPai) {
        this.codPai = codPai;
    }

    public String getCodGrCot() {
        return codGrCot;
    }

    public void setCodGrCot(String codGrCot) {
        this.codGrCot = codGrCot;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}

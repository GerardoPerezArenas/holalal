/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18.vo;

/**
 *
 * @author pbugia
 */
public class FilaDeudaFraccVO {
    private Integer id;
    private String numExp;
    private String numExpDeuda;
    private Double deudaImporte;
    private Long numLiquidacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getNumExpDeuda() {
        return numExpDeuda;
    }

    public void setNumExpDeuda(String numExpDeuda) {
        this.numExpDeuda = numExpDeuda;
    }

    public Double getDeudaImporte() {
        return deudaImporte;
    }

    public void setDeudaImporte(Double deudaImporte) {
        this.deudaImporte = deudaImporte;
    }

    public Long getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(Long numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public FilaDeudaFraccVO() {
    }

    public FilaDeudaFraccVO(Integer id, String numExp, String numExpDeuda, Double deudaImporte, Long numLiquidacion) {
        this.id = id;
        this.numExp = numExp;
        this.numExpDeuda = numExpDeuda;
        this.deudaImporte = deudaImporte;
        this.numLiquidacion = numLiquidacion;
    }
    
    @Override
    public String toString() {
        return "FilaDeudaFraccVO{" + "id=" + id + ", numExp=" + numExp + ", numExpDeuda=" + numExpDeuda + ", deudaImporte=" + deudaImporte + ", numLiquidacion=" + numLiquidacion + '}';
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class OriTrayectoriaVO 
{
    private Integer oriOritrayCod;
    private Long oriEntCod;
    private String oriOritrayDescripcion;
    private BigDecimal oriOritrayDuracion;
    private String oriOritrayOrganismo;
    
    public OriTrayectoriaVO()
    {
        
    }

    public Integer getOriOritrayCod() {
        return oriOritrayCod;
    }

    public void setOriOritrayCod(Integer oriOritrayCod) {
        this.oriOritrayCod = oriOritrayCod;
    }

    public Long getOriEntCod() {
        return oriEntCod;
    }

    public void setOriEntCod(Long oriEntCod) {
        this.oriEntCod = oriEntCod;
    }

    public String getOriOritrayDescripcion() {
        return oriOritrayDescripcion;
    }

    public void setOriOritrayDescripcion(String oriOritrayDescripcion) {
        this.oriOritrayDescripcion = oriOritrayDescripcion;
    }

    public BigDecimal getOriOritrayDuracion() {
        return oriOritrayDuracion;
    }

    public void setOriOritrayDuracion(BigDecimal oriOritrayDuracion) {
        this.oriOritrayDuracion = oriOritrayDuracion;
    }

    public String getOriOritrayOrganismo() {
        return oriOritrayOrganismo;
    }

    public void setOriOritrayOrganismo(String oriOritrayOrganismo) {
        this.oriOritrayOrganismo = oriOritrayOrganismo;
    }
}

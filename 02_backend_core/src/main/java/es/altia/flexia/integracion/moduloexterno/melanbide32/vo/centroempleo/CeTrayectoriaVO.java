/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class CeTrayectoriaVO 
{
    private Integer oriCeCod;
    private Long oriEntCod;
    private String oriCetraDescripcion;
    private BigDecimal oriCetraDuracion;
    private BigDecimal oriCetraDuracionValidada;
    private Integer oriCetraConvocatoria;
    
    public CeTrayectoriaVO()
    {
        
    }

    public Integer getOriCeCod() {
        return oriCeCod;
    }

    public void setOriCeCod(Integer oriCeCod) {
        this.oriCeCod = oriCeCod;
    }

    public Long getOriEntCod() {
        return oriEntCod;
    }

    public void setOriEntCod(Long oriEntCod) {
        this.oriEntCod = oriEntCod;
    }

    public String getOriCetraDescripcion() {
        return oriCetraDescripcion;
    }

    public void setOriCetraDescripcion(String oriCetraDescripcion) {
        this.oriCetraDescripcion = oriCetraDescripcion;
    }

    public BigDecimal getOriCetraDuracion() {
        return oriCetraDuracion;
    }

    public void setOriCetraDuracion(BigDecimal oriCetraDuracion) {
        this.oriCetraDuracion = oriCetraDuracion;
    }

    public BigDecimal getOriCetraDuracionValidada() {
        return oriCetraDuracionValidada;
    }

    public void setOriCetraDuracionValidada(BigDecimal oriCetraDuracionValidada) {
        this.oriCetraDuracionValidada = oriCetraDuracionValidada;
    }

    public Integer getOriCetraConvocatoria() {
        return oriCetraConvocatoria;
    }

    public void setOriCetraConvocatoria(Integer oriCetraConvocatoria) {
        this.oriCetraConvocatoria = oriCetraConvocatoria;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform;

import java.math.BigDecimal;

/**
 *
 * @author davidg
 */
public class EspacioFormVO {
    
    private Integer id;             //ID
    private String numExp;              //EPF_NUM
    private String descripcion;               //EPF_DES
    private BigDecimal superficie;    //EPF_SUP
  
    
    
    public EspacioFormVO()
    {
        
    }

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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public BigDecimal getSuperficie() {
        return superficie;
    }
    
    public void setSuperficie(BigDecimal superficie) {
        this.superficie = superficie;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios;

import java.math.BigDecimal;

/**
 *
 * @author davidg
 */
public class ServiciosVO {
    
    private Integer id;              //ID
    private String numExp;                   //SER_NUM
    private String descripcion;              //SER_DESC
    private String ubicacion;                //SER_UBIC
    private BigDecimal superficie;          //SER_SUPE
    
    
    public ServiciosVO()
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
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public BigDecimal getSuperficie() {
        return superficie;
    }
    
    public void setSuperficie(BigDecimal superficie) {
        this.superficie = superficie;
    }
}

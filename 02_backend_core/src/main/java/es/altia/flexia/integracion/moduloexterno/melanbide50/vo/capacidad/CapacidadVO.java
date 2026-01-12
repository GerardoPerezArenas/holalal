/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad;

import java.math.BigDecimal;

/**
 *
 * @author davidg
 */
public class CapacidadVO {
    
    private Integer id;             //ID
    private Integer idEspSol;   //ID_ESPSOL
    private String numExp;                  //CAIN_NUM
    private String idetificacionEspFor;     //CAIN_IDEF
    private String ubicacion;               //CAIN_UBI
    private String superficie;          //CAIN_SUP
    
    
    public CapacidadVO()
    {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdEspSol() {
        return idEspSol;
    }

    public void setIdEspSol(Integer idEspSol) {
        this.idEspSol = idEspSol;
    }

    public String getNumExp() {
        return numExp;
    }
    
    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }
    
    public String getIdetificacionEspFor() {
        return idetificacionEspFor;
    }
    
    public void setIdetificacionEspFor(String idetificacionEspFor) {
        this.idetificacionEspFor = idetificacionEspFor;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getSuperficie() {
        return superficie;
    }
    
    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }
    
}

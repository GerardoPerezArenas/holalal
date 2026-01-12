/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide50.vo.material;

import java.sql.Date;

/**
 *
 * @author davidg
 */
public class MaterialVO {

    
    private Integer id;           //ID
    private Integer idEspSol;   //ID_ESPSOL
    private String numExp;                //MAC_NUM
    private Integer cantidad;             //MAC_CANT
    private String denominacionET;        //MAC_DET

    
    
    public MaterialVO()
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
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getDenominacionET() {
        return denominacionET;
    }
    
    public void setDenominacionET(String denominacionET) {
        this.denominacionET = denominacionET;
    }
   
}

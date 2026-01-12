/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion;

/**
 *
 * @author davidg
 */
public class DotacionVO {
    
    private Integer id;           //ID
    private Integer idEspSol;   //ID_ESPSOL
    private String numExp;                //DOT_NUM
    private String cantidad;             //DOT_CANT
    private String denominacionET;        //DOT_DET
    private String fechaAdq;                //DOT_FAD
    
    
    public DotacionVO()
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
    
    public String getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getDenominacionET() {
        return denominacionET;
    }
    
    public void setDenominacionET(String denominacionET) {
        this.denominacionET = denominacionET;
    }
    
    public String getFechaAdq() {
        return fechaAdq;
    }
    
    public void setFechaAdq(String fechaAdq) {
        this.fechaAdq = fechaAdq;
    }
  
}

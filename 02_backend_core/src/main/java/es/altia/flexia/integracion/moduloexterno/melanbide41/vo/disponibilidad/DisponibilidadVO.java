/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad;

/**
 *
 * @author davidg
 */

import java.math.BigDecimal;

public class DisponibilidadVO {
    
    private Integer id;   //ID
    private Integer idEspSol;   //ID_ESPSOL
    private String numExp;              //DRE_NUM
    private String codCp;               //DRE_CODCP
    private String propiedadCedidos;    //DRE_PRCE
    private String situados;            //DRE_SIT
    private String supAulas;        //DRE_AUL
    private String supTaller;       //DRE_TAL
    private String supAulaTaller;   //DRE_AUTA
    private String supCampoPract;   //DRE_CAPR
    
    
    public DisponibilidadVO()
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
    
    public String getCodCp() {
        return codCp;
    }
    
    public void setCodCp(String codCp) {
        this.codCp = codCp;
    }
    
    public String getPropiedadCedidos() {
        return propiedadCedidos;
    }
    
    public void setPropiedadCedidos(String propiedadCedidos) {
        this.propiedadCedidos = propiedadCedidos;
    }
    
    
    public String getSituados() {
        return situados;
    }
    
    public void setSituados(String situados) {
        this.situados = situados;
    }
    
    public String getSupAulas() {
        return supAulas;
    }
    
    public void setSupAulas(String supAulas) {
        this.supAulas = supAulas;
    }
    
    public String getSupTaller() {
        return supTaller;
    }
    
    public void setSupTaller(String supTaller) {
        this.supTaller = supTaller;
    }
    
    public String getSupAulaTaller() {
        return supAulaTaller;
    }
    
    public void setSupAulaTaller(String supAulaTaller) {
        this.supAulaTaller = supAulaTaller;
    }
    
    public String getSupCampoPract() {
        return supCampoPract;
    }
    
    public void setSupCampoPract(String supCampoPract) {
        this.supCampoPract = supCampoPract;
    }
    
}

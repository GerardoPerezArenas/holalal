/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform;

import java.math.BigDecimal;

/**
 *
 * @author davidg
 */
public class ModuloFormVO {
    
    private Integer id;             //ID
    private String numExp;              //MDF_NUM
    private String denominacion;               //MDF_DEN
    private BigDecimal duracion;    //MDF_DUR
    private String objetivo;            //MDF_OBJ
    private String contenidoTP;        //MDF_CTP
    private String codMod;
    private String codUC;
    private String desUC;
    private BigDecimal duracMax; 
    private String uc_nivel;
    private String uc_existe;
    private String nivel;
    private String existe;
    
    
    public ModuloFormVO()
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
    
    public String getCodMod() {
        return codMod;
    }    
    public void setCodMod(String codMod) {
        this.codMod = codMod;
    }
    
    public String getCodUC() {
        return codUC;
    }    
    public void setCodUC(String codUC) {
        this.codUC = codUC;
    }
    
    public String getDesUC() {
        return desUC;
    }    
    public void setDesUC(String desUC) {
        this.desUC = desUC;
    }
    
    public BigDecimal getDuracMax() {
        return duracMax;
    }    
    public void setDuracMax(BigDecimal duracMax) {
        this.duracMax = duracMax;
    }
    
    public String getDenominacion() {
        return denominacion;
    }
    
    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }
    
    public BigDecimal getDuracion() {
        return duracion;
    }
    
    public void setDuracion(BigDecimal duracion) {
        this.duracion = duracion;
    }
    
    public String getObjetivo() {
        return objetivo;
    }
    
    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
    
    
    public String getContenidoTP() {
        return contenidoTP;
    }
    
    public void setContenidoTP(String contenidoTP) {
        this.contenidoTP = contenidoTP;
    }
    
    
    
    public String getUc_nivel() {
        return uc_nivel;
    }
    
    public void setUc_nivel(String uc_nivel) {
        this.uc_nivel = uc_nivel;
    }
    
    
    public String getUc_existe() {
        return uc_existe;
    }
    
    public void setUc_existe(String uc_existe) {
        this.uc_existe = uc_existe;
    }
    
    public String getNivel() {
        return nivel;
    }
    
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    
    public String getExiste() {
        return existe;
    }
    
    public void setExiste(String existe) {
        this.existe = existe;
    }
}

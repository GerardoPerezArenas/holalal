/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68.vo;

/**
 *
 * @author davidl
 */
public class FilaTipDocProcVO {
    private int codTipDoc;
    private int codTipDocBBDD;
    private String descTipDoc_es;
    private String descTipDoc_eu;    
    private String tipDocDokusi; 
    
    public FilaTipDocProcVO(){
    }

    public int getCodTipDoc() {
        return codTipDoc;
    }

    public void setCodTipDoc(int codTipDoc) {
        this.codTipDoc = codTipDoc;
    }

    public String getDescTipDoc_es() {
        return descTipDoc_es;
    }

    public void setDescTipDoc_es(String descTipDoc_es) {
        this.descTipDoc_es = descTipDoc_es;
    }

    public String getDescTipDoc_eu() {
        return descTipDoc_eu;
    }

    public void setDescTipDoc_eu(String descTipDoc_eu) {
        this.descTipDoc_eu = descTipDoc_eu;
    }
    
    public int getCodTipDocBBDD() {
        return codTipDocBBDD;
    }

    public void setCodTipDocBBDD(int codTipDocBBDD) {
        this.codTipDocBBDD = codTipDocBBDD;
    }
    
     public String getTipDocDokusi() {
        return tipDocDokusi;
    }

    public void setTipDocDokusi(String tipDocDokusi) {
        this.tipDocDokusi = tipDocDokusi;
    }
    
}

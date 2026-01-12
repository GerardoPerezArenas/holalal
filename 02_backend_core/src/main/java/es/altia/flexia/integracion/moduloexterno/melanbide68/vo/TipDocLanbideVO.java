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
public class TipDocLanbideVO {
    private int codTipDoc;
    private int codTipDocBBDD;
    private String descTipDoc;
    
    public TipDocLanbideVO(){
    }

    public int getCodTipDoc() {
        return codTipDoc;
    }

    public void setCodTipDoc(int codTipDoc) {
        this.codTipDoc = codTipDoc;
    }

    public String getDescTipDoc() {
        return descTipDoc;
    }

    public void setDescTipDoc(String descTipDoc) {
        this.descTipDoc = descTipDoc;
    }

    /**
     * @return the codTipDocBBDD
     */
    public int getCodTipDocBBDD() {
        return codTipDocBBDD;
    }

    /**
     * @param codTipDocBBDD the codTipDocBBDD to set
     */
    public void setCodTipDocBBDD(int codTipDocBBDD) {
        this.codTipDocBBDD = codTipDocBBDD;
    }

}

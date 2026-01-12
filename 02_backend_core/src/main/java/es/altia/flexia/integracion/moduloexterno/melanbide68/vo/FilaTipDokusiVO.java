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
public class FilaTipDokusiVO {
    private int tipDocID;

    public int getTipDocID() {
        return tipDocID;
    }

    public void setTipDocID(int tipDocID) {
        this.tipDocID = tipDocID;
    }

    private String codDokusi;
    private String codDokusiPadre;
    private Integer codDokusiFamilia;
    private String desDokusi_es;
    private String desDokusi_eu;
    private int codTipDoc;

    public int getCodTipDoc() {
        return codTipDoc;
    }

    public void setCodTipDoc(int codTipDoc) {
        this.codTipDoc = codTipDoc;
    }
     
    public FilaTipDokusiVO() {
    
    }

    public String getCodDokusi() {
        return codDokusi;
    }

    public void setCodDokusi(String codDokusi) {
        this.codDokusi = codDokusi;
    }

    public String getCodDokusiPadre() {
        return codDokusiPadre;
    }

    public void setCodDokusiPadre(String codDokusiPadre) {
        this.codDokusiPadre = codDokusiPadre;
    }

    public Integer getCodDokusiFamilia() {
        return codDokusiFamilia;
    }

    public void setCodDokusiFamilia(Integer codDokusiFamilia) {
        this.codDokusiFamilia = codDokusiFamilia;
    }

    public String getDesDokusi_es() {
        return desDokusi_es;
    }

    public void setDesDokusi_es(String desDokusi_es) {
        this.desDokusi_es = desDokusi_es;
    }

    public String getDesDokusi_eu() {
        return desDokusi_eu;
    }

    public void setDesDokusi_eu(String desDokusi_eu) {
        this.desDokusi_eu = desDokusi_eu;
    }
    
}

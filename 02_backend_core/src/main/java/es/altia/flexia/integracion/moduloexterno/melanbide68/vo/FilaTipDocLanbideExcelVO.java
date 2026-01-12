/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68.vo;

import java.util.Date;

/**
 *
 * @author sergio
 */
public class FilaTipDocLanbideExcelVO {
    
    private int tipDocId;
    private String tipDocLanbideEs;
    private String tipDocLanbideEu;
    private Date fechaBaja;
    private String descTipDocLanbideEs;
    private String descTipDocLanbideEu;
    private int codTipDoc;
    private String codGrupoTipDoc;
    private String tipDocDokusi;
    private String Familia;
    
    
    public FilaTipDocLanbideExcelVO(){
        
    }
    
    public int getTipDocId() {
        return tipDocId;
    }

    public void setTipDocId(int tipDocId) {
        this.tipDocId = tipDocId;
    }

    public String getTipDocLanbideEs() {
        return tipDocLanbideEs;
    }

    public void setTipDocLanbideEs(String tipDocLanbideEs) {
        this.tipDocLanbideEs = tipDocLanbideEs;
    }

    public String getTipDocLanbideEu() {
        return tipDocLanbideEu;
    }

    public void setTipDocLanbideEu(String tipDocLanbideEu) {
        this.tipDocLanbideEu = tipDocLanbideEu;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getDescTipDocLanbideEs() {
        return descTipDocLanbideEs;
    }

    public void setDescTipDocLanbideEs(String descTipDocLanbideEs) {
        this.descTipDocLanbideEs = descTipDocLanbideEs;
    }

    public String getDescTipDocLanbideEu() {
        return descTipDocLanbideEu;
    }

    public void setDescTipDocLanbideEu(String descTipDocLanbideEu) {
        this.descTipDocLanbideEu = descTipDocLanbideEu;
    }

    public int getCodTipDoc() {
        return codTipDoc;
    }

    public void setCodTipDoc(int codTipDoc) {
        this.codTipDoc = codTipDoc;
    }

    public String getCodGrupoTipDoc() {
        return codGrupoTipDoc;
    }

    public void setCodGrupoTipDoc(String codGrupoTipDoc) {
        this.codGrupoTipDoc = codGrupoTipDoc;
    }

    public String getTipDocDokusi() {
        return tipDocDokusi;
    }

    public void setTipDocDokusi(String tipDocDokusi) {
        this.tipDocDokusi = tipDocDokusi;
    }

    public String getFamilia() {
        return Familia;
    }

    public void setFamilia(String Familia) {
        this.Familia = Familia;
    }
    
    
}

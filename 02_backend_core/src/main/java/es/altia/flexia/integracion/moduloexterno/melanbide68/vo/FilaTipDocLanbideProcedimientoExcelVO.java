
package es.altia.flexia.integracion.moduloexterno.melanbide68.vo;

import java.util.Date;

public class FilaTipDocLanbideProcedimientoExcelVO {
    
    private String codProc; // Nuevo campo para el código del procedimiento
    private int codTipDoc;
    private String tipDocLanbideEs;
    private String tipDocLanbideEu;
    private String tipDocDokusi;

    public FilaTipDocLanbideProcedimientoExcelVO() {
    }

    public String getCodProc() {
        return codProc;
    }

    public void setCodProc(String codProc) {
        this.codProc = codProc;
    }

    public int getCodTipDoc() {
        return codTipDoc;
    }

    public void setCodTipDoc(int codTipDoc) {
        this.codTipDoc = codTipDoc;
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

    public String getTipDocDokusi() {
        return tipDocDokusi;
    }

    public void setTipDocDokusi(String tipDocDokusi) {
        this.tipDocDokusi = tipDocDokusi;
    }
}

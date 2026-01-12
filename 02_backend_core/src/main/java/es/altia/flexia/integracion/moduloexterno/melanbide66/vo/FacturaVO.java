package es.altia.flexia.integracion.moduloexterno.melanbide66.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide66.util.MeLanbide66Util;
import java.util.ArrayList;
import java.util.Date;

public class FacturaVO {
    private int codIdent;
    private String numExpediente;
    private String codEstado;
    private String descEstado;
    private Date fecha;
    private String fechaStr;
    private double importe;
    private String importeStr;
    private String codConcepto;
    private String descConcepto;
    private String codDesgloseCpto;
    private String descDesgloseCpto;
    private String codEntregaFact;
    private String descEntregaFact;
    private String codEntregaJustif;
    private String descEntregaJustif;
    private String observaciones;
    private String identFactura;

    /**
     * @return the identificador
     */
    public int getCodIdent() {
        return codIdent;
    }

    /**
     * @param codIdent the identificador to set
     */
    public void setCodIdent(int codIdent) {
        this.codIdent = codIdent;
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codEstado
     */
    public String getCodEstado() {
        return codEstado;
    }

    /**
     * @param codEstado the codEstado to set
     */
    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    /**
     * @return the descEstado
     */
    public String getDescEstado() {
        return descEstado;
    }

    /**
     * @param descEstado the descEstado to set
     */
    public void setDescEstado(String descEstado) {
        this.descEstado = descEstado;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the fechaStr
     */
    public String getFechaStr() {
        return fechaStr;
    }

    /**
     * Formatea la propiedad fecha al String correspondiente
     */
    public void setFechaStr() {
        if(fecha!=null) this.fechaStr = MeLanbide66Util.dateToFormattedString(fecha);
        else fechaStr = "-";
    }

    /**
     * @return the importe
     */
    public double getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }

    /**
     * @return the codConcepto
     */
    public String getCodConcepto() {
        return codConcepto;
    }

    /**
     * @param codConcepto the codConcepto to set
     */
    public void setCodConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
    }

    /**
     * @return the descConcepto
     */
    public String getDescConcepto() {
        return descConcepto;
    }

    /**
     * @param descConcepto the descConcepto to set
     */
    public void setDescConcepto(String descConcepto) {
        this.descConcepto = descConcepto;
    }

    /**
     * @return the codDesgloseCpto
     */
    public String getCodDesgloseCpto() {
        return codDesgloseCpto;
    }

    /**
     * @param codDesgloseCpto the codDesgloseCpto to set
     */
    public void setCodDesgloseCpto(String codDesgloseCpto) {
        this.codDesgloseCpto = codDesgloseCpto;
    }

    /**
     * @return the descDesgloseCpto
     */
    public String getDescDesgloseCpto() {
        return descDesgloseCpto;
    }

    /**
     * @param descDesgloseCpto the descDesgloseCpto to set
     */
    public void setDescDesgloseCpto(String descDesgloseCpto) {
        this.descDesgloseCpto = descDesgloseCpto;
    }

    /**
     * @return the importeStr
     */
    public String getImporteStr() {
        return importeStr;
    }

    /**
     * Formatea la propiedad importe a String
     */
    public void setImporteStr() {
        this.importeStr = MeLanbide66Util.doubleToFormattedString(importe);
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        if(observaciones==null) observaciones = "";
        this.observaciones = observaciones;
    }

    /**
     * @return the codEntregaFact
     */
    public String getCodEntregaFact() {
        return codEntregaFact;
    }

    /**
     * @param codEntregaFact the codEntregaFact to set
     */
    public void setCodEntregaFact(String codEntregaFact) {
        this.codEntregaFact = codEntregaFact;
    }

    /**
     * @return the descEntregaFact
     */
    public String getDescEntregaFact() {
        return descEntregaFact;
    }

    public void setDescEntregaFact() {
        this.descEntregaFact = getTxtSN(codEntregaFact);
    }

    /**
     * @return the codEntregaJustif
     */
    public String getCodEntregaJustif() {
        return codEntregaJustif;
    }

    /**
     * @param codEntregaJustif the codEntregaJustif to set
     */
    public void setCodEntregaJustif(String codEntregaJustif) {
        this.codEntregaJustif = codEntregaJustif;
    }

    /**
     * @return the descEntregaJustif
     */
    public String getDescEntregaJustif() {
        return descEntregaJustif;
    }

    public void setDescEntregaJustif() {
        this.descEntregaJustif = getTxtSN(codEntregaJustif);
    }
    
    private String getTxtSN(String codigo){
        if(codigo.equals("S")) return "SI";
        else if(codigo.equals("N")) return "NO";
        return null;
    }

    /**
     * @return the identFactura
     */
    public String getIdentFactura() {
        return identFactura;
    }

    /**
     * @param identFactura the identFactura to set
     */
    public void setIdentFactura(String identFactura) {
        if(identFactura==null) identFactura = "";
        this.identFactura = identFactura;
    }
    
    public String factToString(boolean conObs){
        StringBuilder sb = new StringBuilder("");
         sb.append("ESTADO: ").append(descEstado).append("\n");
         sb.append("FECHA: ").append(fechaStr).append("\n");
         sb.append("IDENTIFICACIÓN: ").append(identFactura).append("\n");
         sb.append("CONCEPTO: ").append(descConcepto).append("\n");
         sb.append("SUB-CONCEPTO: ").append(descDesgloseCpto).append("\n");
         sb.append("IMPORTE: ").append(importeStr).append("\n");
         sb.append("ENTREGA FACTURA: ").append(descEntregaFact).append("\n");
         sb.append("ENTREGA JUSTIFICANTE: ").append(descEntregaJustif).append("\n");
         if(conObs)
             sb.append("OBSERVACIONES: ").append(observaciones).append("\n");
        return sb.toString();
    }
    
    public boolean esFacturaASubsanar(){
        if(codEntregaFact.equals("N") || codEntregaJustif.equals("N"))
            return true;
        return false;
    }
}

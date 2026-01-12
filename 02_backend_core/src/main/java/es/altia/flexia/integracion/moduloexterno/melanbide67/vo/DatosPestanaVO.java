/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.vo;

import java.math.BigDecimal;

/**
 *
 * @author Kepa
 */
public class DatosPestanaVO {

    private String numExp;
    private String codCausa;
    private String descCausa;
    private String codMotivo;
    private String descMotivo;
    private String fechaInicio;
    private String duracion;
    private String fechaFinEsperada;
    private String fechaFinReal;
    // fila IMPORTE CONCEDIDO
    private String importeTotalIni;
    private String pago1; // del expediente
    private String pago2;
    // fila PAGADO
    private String pagado1; // tramite 310
    private String pagado2; // tramite 320
    private String totalPagado;
    // fila NUEVO IMPORTE
    private String pagoNuevo1;
    private String pagoNuevo2;
    private String totalNuevo;
    // fila A DEVOLVER
    private String devolver1;
    private String devolver2;
    private String devolverTotal;
    
    // Datos a actualizar a nivel de expediente en sobre la subvencion total y los pagos
    private String importeTotalActualizado;
    private String importePrimerPagoActualizado;
    private String importeSegundoPagoActualizado;
    // Importe sunvencion inicialmente concedido, priera resolucion
    // Hay que tenerlo en cuenta porque con las modificaciones de resolucion hay que recalcular lo que se paga en funcion de esete valor
    private BigDecimal impSuvencionIniTramResolEstTec; // Traimite : 200 RESOLUCION ESTUDIO TECNICO Campo Numerico : IMPTOT
    

    public DatosPestanaVO() {
    }

    // getters y setters
    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getCodCausa() {
        return codCausa;
    }

    public void setCodCausa(String codCausa) {
        this.codCausa = codCausa;
    }

    public String getDescCausa() {
        return descCausa;
    }

    public void setDescCausa(String descCausa) {
        this.descCausa = descCausa;
    }

    public String getCodMotivo() {
        return codMotivo;
    }

    public void setCodMotivo(String codMotivo) {
        this.codMotivo = codMotivo;
    }

    public String getDescMotivo() {
        return descMotivo;
    }

    public void setDescMotivo(String descMotivo) {
        this.descMotivo = descMotivo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFechaFinEsperada() {
        return fechaFinEsperada;
    }

    public void setFechaFinEsperada(String fechaFinEsperada) {
        this.fechaFinEsperada = fechaFinEsperada;
    }

    public String getFechaFinReal() {
        return fechaFinReal;
    }

    public void setFechaFinReal(String fechaFinReal) {
        this.fechaFinReal = fechaFinReal;
    }

    public String getImporteTotalIni() {
        return importeTotalIni;
    }

    public void setImporteTotalIni(String importeTotalIni) {
        this.importeTotalIni = importeTotalIni;
    }

    public String getPago1() {
        return pago1;
    }

    public void setPago1(String pago1) {
        this.pago1 = pago1;
    }

    public String getPago2() {
        return pago2;
    }

    public void setPago2(String pago2) {
        this.pago2 = pago2;
    }

    public String getPagado1() {
        return pagado1;
    }

    public void setPagado1(String pagado1) {
        this.pagado1 = pagado1;
    }

    public String getPagado2() {
        return pagado2;
    }

    public void setPagado2(String pagado2) {
        this.pagado2 = pagado2;
    }

    public String getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(String totalPagado) {
        this.totalPagado = totalPagado;
    }

    public String getPagoNuevo1() {
        return pagoNuevo1;
    }

    public void setPagoNuevo1(String pagoNuevo1) {
        this.pagoNuevo1 = pagoNuevo1;
    }

    public String getPagoNuevo2() {
        return pagoNuevo2;
    }

    public void setPagoNuevo2(String pagoNuevo2) {
        this.pagoNuevo2 = pagoNuevo2;
    }

    public String getTotalNuevo() {
        return totalNuevo;
    }

    public void setTotalNuevo(String totalNuevo) {
        this.totalNuevo = totalNuevo;
    }

    public String getDevolver1() {
        return devolver1;
    }

    public void setDevolver1(String devolver1) {
        this.devolver1 = devolver1;
    }

    public String getDevolver2() {
        return devolver2;
    }

    public void setDevolver2(String devolver2) {
        this.devolver2 = devolver2;
    }

    public String getDevolverTotal() {
        return devolverTotal;
    }

    public void setDevolverTotal(String devolverTotal) {
        this.devolverTotal = devolverTotal;
    }

    public String getImporteTotalActualizado() {
        return importeTotalActualizado;
    }

    public void setImporteTotalActualizado(String importeTotalActualizado) {
        this.importeTotalActualizado = importeTotalActualizado;
    }

    public String getImportePrimerPagoActualizado() {
        return importePrimerPagoActualizado;
    }

    public void setImportePrimerPagoActualizado(String importePrimerPagoActualizado) {
        this.importePrimerPagoActualizado = importePrimerPagoActualizado;
    }

    public String getImporteSegundoPagoActualizado() {
        return importeSegundoPagoActualizado;
    }

    public void setImporteSegundoPagoActualizado(String importeSegundoPagoActualizado) {
        this.importeSegundoPagoActualizado = importeSegundoPagoActualizado;
    }

    public BigDecimal getImpSuvencionIniTramResolEstTec() {
        return impSuvencionIniTramResolEstTec;
    }

    public void setImpSuvencionIniTramResolEstTec(BigDecimal impSuvencionIniTramResolEstTec) {
        this.impSuvencionIniTramResolEstTec = impSuvencionIniTramResolEstTec;
    }

    
}

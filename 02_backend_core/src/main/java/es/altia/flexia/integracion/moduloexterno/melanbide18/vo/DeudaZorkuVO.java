package es.altia.flexia.integracion.moduloexterno.melanbide18.vo;

import java.sql.Date;

public class DeudaZorkuVO {

    private Long numLiquidacion;
    private String numDocumento;
    private String expediente;
    private Double importeDeuda;
    private Double importePendiente;
    private Double importeCobrado;
    private Integer codTipoPago;
    private String tipoPago;
    private Integer codEstadoDeuda;
    private String estadoDeuda;
    private String periodo;
    private Date fechaLimitePago;
    private String fechaLimitePagoStr;

    public Long getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(Long numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Double getImporteDeuda() {
        return importeDeuda;
    }

    public void setImporteDeuda(Double importeDeuda) {
        this.importeDeuda = importeDeuda;
    }

    public Double getImportePendiente() {
        return importePendiente;
    }

    public void setImportePendiente(Double importePendiente) {
        this.importePendiente = importePendiente;
    }

    public Double getImporteCobrado() {
        return importeCobrado;
    }

    public void setImporteCobrado(Double importeCobrado) {
        this.importeCobrado = importeCobrado;
    }

    public Integer getCodTipoPago() {
        return codTipoPago;
    }

    public void setCodTipoPago(Integer codTipoPago) {
        this.codTipoPago = codTipoPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getCodEstadoDeuda() {
        return codEstadoDeuda;
    }

    public void setCodEstadoDeuda(Integer codEstadoDeuda) {
        this.codEstadoDeuda = codEstadoDeuda;
    }

    public String getEstadoDeuda() {
        return estadoDeuda;
    }

    public void setEstadoDeuda(String estadoDeuda) {
        this.estadoDeuda = estadoDeuda;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Date getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(Date fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public String getFechaLimitePagoStr() {
        return fechaLimitePagoStr;
    }

    public void setFechaLimitePagoStr(String fechaLimitePagoStr) {
        this.fechaLimitePagoStr = fechaLimitePagoStr;
    }
    
    

 
}

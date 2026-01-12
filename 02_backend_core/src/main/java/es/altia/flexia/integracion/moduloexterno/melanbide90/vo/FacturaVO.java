package es.altia.flexia.integracion.moduloexterno.melanbide90.vo;

import java.sql.Date;

/**
 *
 * @author kigonzalez
 */
public class FacturaVO {

    private Integer id;
    private String numExp;
    private String familia;
    private String descFamiliaCas;
    private String descFamiliaEus;
    private Integer numOrden;
    private String tipoGasto;
    private String descTipoGasto;
    private String proveedor;
    private String numFactura;
    private Date fecEmision;
    private String fecEmisionStr;
    private Date fecPago;
    private String fecPagoStr;
    private Double importeBase;
    private Double importeIva;
    private Double importeTotal;
    private String validada;
    private String descValidada;
    private String ivaSub;
    private String descIvaSub;
    private Double importeVali;
    private String motNoVal;
    private String descMotNoVal;
    private String porcentajeIva;
    private String descPorcentajeIva;
    private String exento;
    private Integer prorrata;
    private Double importeBaseValidado;
    private String porcentajeIvaValidado;
    private String descPorcentajeIvaValidado;
    private Double importeIvaValidado;
    private Integer prorrataValidado;

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

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDescFamiliaCas() {
        return descFamiliaCas;
    }

    public void setDescFamiliaCas(String descFamiliaCas) {
        this.descFamiliaCas = descFamiliaCas;
    }

    public String getDescFamiliaEus() {
        return descFamiliaEus;
    }

    public void setDescFamiliaEus(String descFamiliaEus) {
        this.descFamiliaEus = descFamiliaEus;
    }

    public Integer getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(Integer numOrden) {
        this.numOrden = numOrden;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public String getDescTipoGasto() {
        return descTipoGasto;
    }

    public void setDescTipoGasto(String descTipoGasto) {
        this.descTipoGasto = descTipoGasto;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public Date getFecEmision() {
        return fecEmision;
    }

    public void setFecEmision(Date fecEmision) {
        this.fecEmision = fecEmision;
    }

    public String getFecPagoStr() {
        return fecPagoStr;
    }

    public void setFecPagoStr(String fecPagoStr) {
        this.fecPagoStr = fecPagoStr;
    }

    public String getFecEmisionStr() {
        return fecEmisionStr;
    }

    public void setFecEmisionStr(String fecEmisionStr) {
        this.fecEmisionStr = fecEmisionStr;
    }

    public Date getFecPago() {
        return fecPago;
    }

    public void setFecPago(Date fecPago) {
        this.fecPago = fecPago;
    }

    public Double getImporteBase() {
        return importeBase;
    }

    public void setImporteBase(Double importeBase) {
        this.importeBase = importeBase;
    }

    public Double getImporteIva() {
        return importeIva;
    }

    public void setImporteIva(Double importeIva) {
        this.importeIva = importeIva;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Double getImporteVali() {
        return importeVali;
    }

    public void setImporteVali(Double importeVali) {
        this.importeVali = importeVali;
    }

    public String getValidada() {
        return validada;
    }

    public void setValidada(String validada) {
        this.validada = validada;
    }

    public String getDescValidada() {
        return descValidada;
    }

    public void setDescValidada(String descValidada) {
        this.descValidada = descValidada;
    }

    public String getIvaSub() {
        return ivaSub;
    }

    public void setIvaSub(String ivaSub) {
        this.ivaSub = ivaSub;
    }

    public String getDescIvaSub() {
        return descIvaSub;
    }

    public void setDescIvaSub(String descIvaSub) {
        this.descIvaSub = descIvaSub;
    }

    public String getMotNoVal() {
        return motNoVal;
    }

    public void setMotNoVal(String motNoVal) {
        this.motNoVal = motNoVal;
    }

    public String getDescMotNoVal() {
        return descMotNoVal;
    }

    public void setDescMotNoVal(String descMotNoVal) {
        this.descMotNoVal = descMotNoVal;
    }

    public String getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(String porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getDescPorcentajeIva() {
        return descPorcentajeIva;
    }

    public void setDescPorcentajeIva(String descPorcentajeIva) {
        this.descPorcentajeIva = descPorcentajeIva;
    }

    public String getExento() {
        return exento;
    }

    public void setExento(String exento) {
        this.exento = exento;
    }

    public Integer getProrrata() {
        return prorrata;
    }

    public void setProrrata(Integer prorrata) {
        this.prorrata = prorrata;
    }

    public Double getImporteBaseValidado() {
        return importeBaseValidado;
    }
    
    public void setImporteBaseValidado(Double importeBaseValidado) {
        this.importeBaseValidado = importeBaseValidado;
    }

    public String getPorcentajeIvaValidado() {
        return porcentajeIvaValidado;
    }

    public void setPorcentajeIvaValidado(String porcentajeIvaValidado) {
        this.porcentajeIvaValidado = porcentajeIvaValidado;
    }

    public String getDescPorcentajeIvaValidado() {
        return descPorcentajeIvaValidado;
    }

    public void setDescPorcentajeIvaValidado(String descPorcentajeIvaValidado) {
        this.descPorcentajeIvaValidado = descPorcentajeIvaValidado;
    }

    public Double getImporteIvaValidado() {
        return importeIvaValidado;
    }

    public void setImporteIvaValidado(Double importeIvaValidado) {
        this.importeIvaValidado = importeIvaValidado;
    }

    public Integer getProrrataValidado() {
        return prorrataValidado;
    }

    public void setProrrataValidado(Integer prorrataValidado) {
        this.prorrataValidado = prorrataValidado;
    }
}

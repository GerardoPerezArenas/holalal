package es.altia.flexia.integracion.moduloexterno.melanbide90.vo;

/**
 *
 * @author kigonzalez
 */
public class FamiliaSolicitadaVO {

    private String codigo;
    private String descFamiliaCas;
    private String descFamiliaEus;
    private Double baseTotal;
    private Double baseTotalValidado;
    private Double ivaTotal;
    private Double ivaTotalValidado;
    private Double importeTotal;
    private Double importeTotalValidado;
    private Double validadoTotal;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Double getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(Double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public Double getIvaTotal() {
        return ivaTotal;
    }

    public void setIvaTotal(Double ivaTotal) {
        this.ivaTotal = ivaTotal;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Double getValidadoTotal() {
        return validadoTotal;
    }

    public void setValidadoTotal(Double validadoTotal) {
        this.validadoTotal = validadoTotal;
    }
    
    public Double getBaseTotalValidado() {
        return baseTotalValidado;
    }

    public void setBaseTotalValidado(Double baseTotalValidado) {
        this.baseTotalValidado = baseTotalValidado;
    }

    public Double getIvaTotalValidado() {
        return ivaTotalValidado;
    }

    public void setIvaTotalValidado(Double ivaTotalValidado) {
        this.ivaTotalValidado = ivaTotalValidado;
    }

    public Double getImporteTotalValidado() {
        return importeTotalValidado;
    }

    public void setImporteTotalValidado(Double importeTotalValidado) {
        this.importeTotalValidado = importeTotalValidado;
    }
}

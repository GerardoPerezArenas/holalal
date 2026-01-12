/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class EcaResumenVO 
{
    //Datos persistentes
    private Integer ecaCodResumen;
    private Integer solicitud;
    private BigDecimal ecaResSubPriv;
    private BigDecimal ecaResSubPub;
    private BigDecimal ecaResTotSubv;
    
    
    
    private Boolean otrasSubv;
    
    //Columna SOLICITADO
    private Integer numeroProspectores_solic;
    private BigDecimal importeProspectores_solic;
    private Integer numeroPreparadores_solic;
    private BigDecimal importePreparadores_solic;
    private BigDecimal gastosGenerales_solic;
    private BigDecimal subvOrgPublicos_solic;
    private BigDecimal subvOrgPrivados_solic;
    private BigDecimal totSubv_solic;
    private BigDecimal totAprobado_solic;
    
    //Columna CONCEDIDO
    private Integer numeroProspectores_conc;
    private BigDecimal importeProspectores_conc;
    private Integer numeroPreparadores_conc;
    private BigDecimal importePreparadores_conc;
    private BigDecimal gastosGenerales_conc;
    private BigDecimal totSubv_conc;
    //nuevos--
    private BigDecimal subvOrgPublicos_conc;
    private BigDecimal subvOrgPrivados_conc;
    
    //Columna JUSTIFICADO
    private Integer numeroProspectores_justif;
    private BigDecimal importeProspectores_justif;
    private Integer numeroPreparadores_justif;
    private BigDecimal importePreparadores_justif;
    private BigDecimal gastosGenerales_justif;
    private BigDecimal subvOrgPublicos_justif;
    private BigDecimal subvOrgPrivados_justif;
    private BigDecimal totSubv_justif;
    
    //Columna PAGADO
    private Integer numeroProspectores_pag;
    private BigDecimal importeProspectores_pag;
    private Integer numeroPreparadores_pag;
    private BigDecimal importePreparadores_pag;
    private BigDecimal gastosGenerales_pag;
    private BigDecimal subvOrgPublicos_pag;
    private BigDecimal subvOrgPrivados_pag;
    private BigDecimal totSubv_pag;
    
    public EcaResumenVO()
    {
        
    }

    public Integer getEcaCodResumen() {
        return ecaCodResumen;
    }

    public void setEcaCodResumen(Integer ecaCodResumen) {
        this.ecaCodResumen = ecaCodResumen;
    }

    public Integer getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Integer solicitud) {
        this.solicitud = solicitud;
    }

    public BigDecimal getEcaResSubPriv() {
        return ecaResSubPriv;
    }

    public void setEcaResSubPriv(BigDecimal ecaResSubPriv) {
        this.ecaResSubPriv = ecaResSubPriv;
    }

    public BigDecimal getEcaResSubPub() {
        return ecaResSubPub;
    }

    public void setEcaResSubPub(BigDecimal ecaResSubPub) {
        this.ecaResSubPub = ecaResSubPub;
    }

    public BigDecimal getEcaResTotSubv() {
        return ecaResTotSubv;
    }

    public void setEcaResTotSubv(BigDecimal ecaResTotSubv) {
        this.ecaResTotSubv = ecaResTotSubv;
    }

    public Boolean getOtrasSubv() {
        return otrasSubv;
    }

    public void setOtrasSubv(Boolean otrasSubv) {
        this.otrasSubv = otrasSubv;
    }

    public Integer getNumeroProspectores_solic() {
        return numeroProspectores_solic;
    }

    public void setNumeroProspectores_solic(Integer numeroProspectores_solic) {
        this.numeroProspectores_solic = numeroProspectores_solic;
    }

    public BigDecimal getImporteProspectores_solic() {
        return importeProspectores_solic;
    }

    public void setImporteProspectores_solic(BigDecimal importeProspectores_solic) {
        this.importeProspectores_solic = importeProspectores_solic;
    }

    public Integer getNumeroPreparadores_solic() {
        return numeroPreparadores_solic;
    }

    public void setNumeroPreparadores_solic(Integer numeroPreparadores_solic) {
        this.numeroPreparadores_solic = numeroPreparadores_solic;
    }

    public BigDecimal getImportePreparadores_solic() {
        return importePreparadores_solic;
    }

    public void setImportePreparadores_solic(BigDecimal importePreparadores_solic) {
        this.importePreparadores_solic = importePreparadores_solic;
    }

    public BigDecimal getGastosGenerales_solic() {
        return gastosGenerales_solic;
    }

    public void setGastosGenerales_solic(BigDecimal gastosGenerales_solic) {
        this.gastosGenerales_solic = gastosGenerales_solic;
    }

    public BigDecimal getSubvOrgPublicos_solic() {
        return subvOrgPublicos_solic;
    }

    public void setSubvOrgPublicos_solic(BigDecimal subvOrgPublicos_solic) {
        this.subvOrgPublicos_solic = subvOrgPublicos_solic;
    }

    public BigDecimal getSubvOrgPrivados_solic() {
        return subvOrgPrivados_solic;
    }

    public void setSubvOrgPrivados_solic(BigDecimal subvOrgPrivados_solic) {
        this.subvOrgPrivados_solic = subvOrgPrivados_solic;
    }

    public BigDecimal getTotSubv_solic() {
        return totSubv_solic;
    }

    public void setTotSubv_solic(BigDecimal totSubv_solic) {
        this.totSubv_solic = totSubv_solic;
    }

    public BigDecimal getTotAprobado_solic() {
        return totAprobado_solic;
    }

    public void setTotAprobado_solic(BigDecimal totAprobado_solic) {
        this.totAprobado_solic = totAprobado_solic;
    }  
    
    public Integer getNumeroProspectores_conc() {
        return numeroProspectores_conc;
    }

    public void setNumeroProspectores_conc(Integer numeroProspectores_conc) {
        this.numeroProspectores_conc = numeroProspectores_conc;
    }

    public BigDecimal getImporteProspectores_conc() {
        return importeProspectores_conc;
    }

    public void setImporteProspectores_conc(BigDecimal importeProspectores_conc) {
        this.importeProspectores_conc = importeProspectores_conc;
    }

    public Integer getNumeroPreparadores_conc() {
        return numeroPreparadores_conc;
    }

    public void setNumeroPreparadores_conc(Integer numeroPreparadores_conc) {
        this.numeroPreparadores_conc = numeroPreparadores_conc;
    }

    public BigDecimal getImportePreparadores_conc() {
        return importePreparadores_conc;
    }

    public void setImportePreparadores_conc(BigDecimal importePreparadores_conc) {
        this.importePreparadores_conc = importePreparadores_conc;
    }

    public BigDecimal getGastosGenerales_conc() {
        return gastosGenerales_conc;
    }

    public void setGastosGenerales_conc(BigDecimal gastosGenerales_conc) {
        this.gastosGenerales_conc = gastosGenerales_conc;
    }

    public BigDecimal getTotSubv_conc() {
        return totSubv_conc;
    }

    public void setTotSubv_conc(BigDecimal totSubv_conc) {
        this.totSubv_conc = totSubv_conc;
    }

    public Integer getNumeroProspectores_justif() {
        return numeroProspectores_justif;
    }

    public void setNumeroProspectores_justif(Integer numeroProspectores_justif) {
        this.numeroProspectores_justif = numeroProspectores_justif;
    }

    public BigDecimal getImporteProspectores_justif() {
        return importeProspectores_justif;
    }

    public void setImporteProspectores_justif(BigDecimal importeProspectores_justif) {
        this.importeProspectores_justif = importeProspectores_justif;
    }

    public Integer getNumeroPreparadores_justif() {
        return numeroPreparadores_justif;
    }

    public void setNumeroPreparadores_justif(Integer numeroPreparadores_justif) {
        this.numeroPreparadores_justif = numeroPreparadores_justif;
    }

    public BigDecimal getImportePreparadores_justif() {
        return importePreparadores_justif;
    }

    public void setImportePreparadores_justif(BigDecimal importePreparadores_justif) {
        this.importePreparadores_justif = importePreparadores_justif;
    }

    public BigDecimal getGastosGenerales_justif() {
        return gastosGenerales_justif;
    }

    public void setGastosGenerales_justif(BigDecimal gastosGenerales_justif) {
        this.gastosGenerales_justif = gastosGenerales_justif;
    }

    public BigDecimal getSubvOrgPublicos_justif() {
        return subvOrgPublicos_justif;
    }

    public void setSubvOrgPublicos_justif(BigDecimal subvOrgPublicos_justif) {
        this.subvOrgPublicos_justif = subvOrgPublicos_justif;
    }

    public BigDecimal getSubvOrgPrivados_justif() {
        return subvOrgPrivados_justif;
    }

    public void setSubvOrgPrivados_justif(BigDecimal subvOrgPrivados_justif) {
        this.subvOrgPrivados_justif = subvOrgPrivados_justif;
    }

    public BigDecimal getTotSubv_justif() {
        return totSubv_justif;
    }

    public void setTotSubv_justif(BigDecimal totSubv_justif) {
        this.totSubv_justif = totSubv_justif;
    }

    public Integer getNumeroProspectores_pag() {
        return numeroProspectores_pag;
    }

    public void setNumeroProspectores_pag(Integer numeroProspectores_pag) {
        this.numeroProspectores_pag = numeroProspectores_pag;
    }

    public BigDecimal getImporteProspectores_pag() {
        return importeProspectores_pag;
    }

    public void setImporteProspectores_pag(BigDecimal importeProspectores_pag) {
        this.importeProspectores_pag = importeProspectores_pag;
    }

    public Integer getNumeroPreparadores_pag() {
        return numeroPreparadores_pag;
    }

    public void setNumeroPreparadores_pag(Integer numeroPreparadores_pag) {
        this.numeroPreparadores_pag = numeroPreparadores_pag;
    }

    public BigDecimal getImportePreparadores_pag() {
        return importePreparadores_pag;
    }

    public void setImportePreparadores_pag(BigDecimal importePreparadores_pag) {
        this.importePreparadores_pag = importePreparadores_pag;
    }

    public BigDecimal getGastosGenerales_pag() {
        return gastosGenerales_pag;
    }

    public void setGastosGenerales_pag(BigDecimal gastosGenerales_pag) {
        this.gastosGenerales_pag = gastosGenerales_pag;
    }

    public BigDecimal getSubvOrgPublicos_pag() {
        return subvOrgPublicos_pag;
    }

    public void setSubvOrgPublicos_pag(BigDecimal subvOrgPublicos_pag) {
        this.subvOrgPublicos_pag = subvOrgPublicos_pag;
    }

    public BigDecimal getSubvOrgPrivados_pag() {
        return subvOrgPrivados_pag;
    }

    public void setSubvOrgPrivados_pag(BigDecimal subvOrgPrivados_pag) {
        this.subvOrgPrivados_pag = subvOrgPrivados_pag;
    }

    public BigDecimal getTotSubv_pag() {
        return totSubv_pag;
    }

    public void setTotSubv_pag(BigDecimal totSubv_pag) {
        this.totSubv_pag = totSubv_pag;
    }

    public BigDecimal getSubvOrgPublicos_conc() {
        return subvOrgPublicos_conc;
    }

    public void setSubvOrgPublicos_conc(BigDecimal subvOrgPublicos_conc) {
        this.subvOrgPublicos_conc = subvOrgPublicos_conc;
    }

    public BigDecimal getSubvOrgPrivados_conc() {
        return subvOrgPrivados_conc;
    }

    public void setSubvOrgPrivados_conc(BigDecimal subvOrgPrivados_conc) {
        this.subvOrgPrivados_conc = subvOrgPrivados_conc;
    }
    
    
}

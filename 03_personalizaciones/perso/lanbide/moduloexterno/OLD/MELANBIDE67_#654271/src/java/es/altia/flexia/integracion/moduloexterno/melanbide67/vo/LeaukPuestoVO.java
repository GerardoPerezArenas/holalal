/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class LeaukPuestoVO 
{
    private String numExp;
    private int idPuesto;
    private String codPuesto;
    private String descPuesto;
    private String sector;
    private String apellido1;
    private String apellido2;
    private String nombre;
    private BigDecimal impSubvSol;
    private BigDecimal impSubvEst;
    private BigDecimal impSubvOfe;
    private BigDecimal reintegros;
    private BigDecimal impTotal;
    private BigDecimal salarioSol;
    private BigDecimal salarioOfe;
    private Integer sexoSol;
    private Integer sexoOfe;
    private String dptoSol;
    private String dptoOfe;
    private String modalidadSol;
    private String modalidadOfe;
    private String codTitulacionSol;
    private String codTitulacionOfe;
    private String codModSol;
    private String codModOfe;
    private BigDecimal jornadaLabSol;
    private BigDecimal jornadaLabOfe;
    private String centroTrabSol;
    private String centroTrabOfe;
    private String ctaCotizSol;
    private String ctaCotizOfe;
    private Date fecIniSol;
    private Date fecFinSol;
    private Date fecIniOfe;
    private Date fecFinOfe;
    private String grupoCotizSol;
    private String grupoCotizOfe;
    private String convenioColSol;
    private String convenioColOfe;
    private String observaciones;
    private String codTipoNif;
    private String nif;
    private Date fecNacimiento;
    private String num_oferta;
    private String centroColGestion;
    private String centroColCaptacion;
    private BigDecimal impPago1;
    private BigDecimal impPago2;
    
    public LeaukPuestoVO()
    {
        
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getCodPuesto() {
        return codPuesto;
    }

    public void setCodPuesto(String codPuesto) {
        this.codPuesto = codPuesto;
    }

    public String getDescPuesto() {
        return descPuesto;
    }

    public void setDescPuesto(String descPuesto) {
        this.descPuesto = descPuesto;
    }
    
    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    
    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
     public BigDecimal getImpSubvSol() {
        return impSubvSol;
    }

    public void setImpSubvSol(BigDecimal impSubvSol) {
        this.impSubvSol = impSubvSol;
    }
    
    public BigDecimal getImpSubvEst() {
        return impSubvEst;
    }

    public void setImpSubvEst(BigDecimal impSubvEst) {
        this.impSubvEst = impSubvEst;
    }
    
    public BigDecimal getImpSubvOfe() {
        return impSubvOfe;
    }

    public void setImpSubvOfe(BigDecimal impSubvOfe) {
        this.impSubvOfe = impSubvOfe;
    }
    
    public BigDecimal getReintegros() {
        return reintegros;
    }

    public void setReintegros(BigDecimal reintegros) {
        this.reintegros = reintegros;
    }
    
    public BigDecimal getImpTotal() {
        return impTotal;
    }

    public void setImpTotal(BigDecimal impTotal) {
        this.impTotal = impTotal;
    }
    
    public BigDecimal getSalarioSol() {
        return salarioSol;
    }

    public void setSalarioSol(BigDecimal salarioSol) {
        this.salarioSol = salarioSol;
    }
    
    public BigDecimal getSalarioOfe() {
        return salarioOfe;
    }

    public void setSalarioOfe(BigDecimal salarioOfe) {
        this.salarioOfe = salarioOfe;
    }
    
    public String getDptoSol() {
        return dptoSol;
    }

    public void setDptoSol(String dptoSol) {
        this.dptoSol = dptoSol;
    }
    
    public String getDptoOfe() {
        return dptoOfe;
    }

    public void setDptoOfe(String dptoOfe) {
        this.dptoOfe = dptoOfe;
    }

    /**
     * @return the modalidadSol
     */
    public String getModalidadSol() {
        return modalidadSol;
    }

    /**
     * @param modalidadSol the modalidadSol to set
     */
    public void setModalidadSol(String modalidadSol) {
        this.modalidadSol = modalidadSol;
    }

    /**
     * @return the modalidadOfe
     */
    public String getModalidadOfe() {
        return modalidadOfe;
    }

    /**
     * @param modalidadOfe the modalidadOfe to set
     */
    public void setModalidadOfe(String modalidadOfe) {
        this.modalidadOfe = modalidadOfe;
    }

    /**
     * @return the jornadaLabSol
     */
    public BigDecimal getJornadaLabSol() {
        return jornadaLabSol;
    }

    /**
     * @param jornadaLabSol the jornadaLabSol to set
     */
    public void setJornadaLabSol(BigDecimal jornadaLabSol) {
        this.jornadaLabSol = jornadaLabSol;
    }

    /**
     * @return the jornadaLabOfe
     */
    public BigDecimal getJornadaLabOfe() {
        return jornadaLabOfe;
    }

    /**
     * @param jornadaLabOfe the jornadaLabOfe to set
     */
    public void setJornadaLabOfe(BigDecimal jornadaLabOfe) {
        this.jornadaLabOfe = jornadaLabOfe;
    }

    /**
     * @return the centroTrabSol
     */
    public String getCentroTrabSol() {
        return centroTrabSol;
    }

    /**
     * @param centroTrabSol the centroTrabSol to set
     */
    public void setCentroTrabSol(String centroTrabSol) {
        this.centroTrabSol = centroTrabSol;
    }

    /**
     * @return the centroTrabOfe
     */
    public String getCentroTrabOfe() {
        return centroTrabOfe;
    }

    /**
     * @param centroTrabOfe the centroTrabOfe to set
     */
    public void setCentroTrabOfe(String centroTrabOfe) {
        this.centroTrabOfe = centroTrabOfe;
    }

    /**
     * @return the ctaCotizSol
     */
    public String getCtaCotizSol() {
        return ctaCotizSol;
    }

    /**
     * @param ctaCotizSol the ctaCotizSol to set
     */
    public void setCtaCotizSol(String ctaCotizSol) {
        this.ctaCotizSol = ctaCotizSol;
    }

    /**
     * @return the ctaCotizOfe
     */
    public String getCtaCotizOfe() {
        return ctaCotizOfe;
    }

    /**
     * @param ctaCotizOfe the ctaCotizOfe to set
     */
    public void setCtaCotizOfe(String ctaCotizOfe) {
        this.ctaCotizOfe = ctaCotizOfe;
    }

    /**
     * @return the grupoCotizSol
     */
    public String getGrupoCotizSol() {
        return grupoCotizSol;
    }

    /**
     * @param grupoCotizSol the grupoCotizSol to set
     */
    public void setGrupoCotizSol(String grupoCotizSol) {
        this.grupoCotizSol = grupoCotizSol;
    }

    /**
     * @return the grupoCotizOfe
     */
    public String getGrupoCotizOfe() {
        return grupoCotizOfe;
    }

    /**
     * @param grupoCotizOfe the grupoCotizOfe to set
     */
    public void setGrupoCotizOfe(String grupoCotizOfe) {
        this.grupoCotizOfe = grupoCotizOfe;
    }

    /**
     * @return the convenioColSol
     */
    public String getConvenioColSol() {
        return convenioColSol;
    }

    /**
     * @param convenioColSol the convenioColSol to set
     */
    public void setConvenioColSol(String convenioColSol) {
        this.convenioColSol = convenioColSol;
    }

    /**
     * @return the convenioColOfe
     */
    public String getConvenioColOfe() {
        return convenioColOfe;
    }

    /**
     * @param convenioColOfe the convenioColOfe to set
     */
    public void setConvenioColOfe(String convenioColOfe) {
        this.convenioColOfe = convenioColOfe;
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
        this.observaciones = observaciones;
    }

    /**
     * @return the sexoSol
     */
    public Integer getSexoSol() {
        return sexoSol;
    }

    /**
     * @param sexoSol the sexoSol to set
     */
    public void setSexoSol(Integer sexoSol) {
        this.sexoSol = sexoSol;
    }

    /**
     * @return the sexoOfe
     */
    public Integer getSexoOfe() {
        return sexoOfe;
    }

    /**
     * @param sexoOfe the sexoOfe to set
     */
    public void setSexoOfe(Integer sexoOfe) {
        this.sexoOfe = sexoOfe;
    }

    /**
     * @return the fecIniSol
     */
    public Date getFecIniSol() {
        return fecIniSol;
    }

    /**
     * @param fecIniSol the fecIniSol to set
     */
    public void setFecIniSol(Date fecIniSol) {
        this.fecIniSol = fecIniSol;
    }

    /**
     * @return the fecFinSol
     */
    public Date getFecFinSol() {
        return fecFinSol;
    }

    /**
     * @param fecFinSol the fecFinSol to set
     */
    public void setFecFinSol(Date fecFinSol) {
        this.fecFinSol = fecFinSol;
    }

    /**
     * @return the fecIniOfe
     */
    public Date getFecIniOfe() {
        return fecIniOfe;
    }

    /**
     * @param fecIniOfe the fecIniOfe to set
     */
    public void setFecIniOfe(Date fecIniOfe) {
        this.fecIniOfe = fecIniOfe;
    }

    /**
     * @return the fecFinOfe
     */
    public Date getFecFinOfe() {
        return fecFinOfe;
    }

    /**
     * @param fecFinOfe the fecFinOfe to set
     */
    public void setFecFinOfe(Date fecFinOfe) {
        this.fecFinOfe = fecFinOfe;
    }

  
    /**
     * @return the codModSol
     */
    public String getCodModSol() {
        return codModSol;
    }

    /**
     * @param codModSol the codModSol to set
     */
    public void setCodModSol(String codModSol) {
        this.codModSol = codModSol;
    }

    /**
     * @return the codModOfe
     */
    public String getCodModOfe() {
        return codModOfe;
    }

    /**
     * @param codModOfe the codModOfe to set
     */
    public void setCodModOfe(String codModOfe) {
        this.codModOfe = codModOfe;
    }

    /**
     * @return the codTitulacionSol
     */
    public String getCodTitulacionSol() {
        return codTitulacionSol;
    }

    /**
     * @param codTitulacionSol the codTitulacionSol to set
     */
    public void setCodTitulacionSol(String codTitulacionSol) {
        this.codTitulacionSol = codTitulacionSol;
    }

    /**
     * @return the codTitulacionOfe
     */
    public String getCodTitulacionOfe() {
        return codTitulacionOfe;
    }

    /**
     * @param codTitulacionOfe the codTitulacionOfe to set
     */
    public void setCodTitulacionOfe(String codTitulacionOfe) {
        this.codTitulacionOfe = codTitulacionOfe;
    }

    /**
     * @return the sector
     */
    public String getSector() {
        return sector;
    }

    /**
     * @param sector the sector to set
     */
    public void setSector(String sector) {
        this.sector = sector;
    }

    /**
     * @return the codTipoNif
     */
    public String getCodTipoNif() {
        return codTipoNif;
    }

    /**
     * @param codTipoNif the codTipoNif to set
     */
    public void setCodTipoNif(String codTipoNif) {
        this.codTipoNif = codTipoNif;
    }

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param nif the nif to set
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * @return the fecNacimiento
     */
    public Date getFecNacimiento() {
        return fecNacimiento;
    }

    /**
     * @param fecNacimiento the fecNacimiento to set
     */
    public void setFecNacimiento(Date fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    /**
     * @return the num_oferta
     */
    public String getNum_oferta() {
        return num_oferta;
    }

    /**
     * @param num_oferta the num_oferta to set
     */
    public void setNum_oferta(String num_oferta) {
        this.num_oferta = num_oferta;
    }

    /**
     * @return the centroColGestion
     */
    public String getCentroColGestion() {
        return centroColGestion;
    }

    /**
     * @param centroColGestion the centroColGestion to set
     */
    public void setCentroColGestion(String centroColGestion) {
        this.centroColGestion = centroColGestion;
    }

    /**
     * @return the centroColCaptacion
     */
    public String getCentroColCaptacion() {
        return centroColCaptacion;
    }

    /**
     * @param centroColCaptacion the centroColCaptacion to set
     */
    public void setCentroColCaptacion(String centroColCaptacion) {
        this.centroColCaptacion = centroColCaptacion;
    }

    /**
     * @return the impPago1
     */
    public BigDecimal getImpPago1() {
        return impPago1;
    }

    /**
     * @param impPago1 the impPago1 to set
     */
    public void setImpPago1(BigDecimal impPago1) {
        this.impPago1 = impPago1;
    }

    /**
     * @return the impPago2
     */
    public BigDecimal getImpPago2() {
        return impPago2;
    }

    /**
     * @param impPago2 the impPago2 to set
     */
    public void setImpPago2(BigDecimal impPago2) {
        this.impPago2 = impPago2;
    }

    

   
}

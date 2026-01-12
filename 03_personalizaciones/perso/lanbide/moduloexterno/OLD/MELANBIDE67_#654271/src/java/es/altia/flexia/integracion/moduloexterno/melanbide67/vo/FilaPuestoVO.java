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
public class FilaPuestoVO {
    private int idPuesto;
    private String codPuesto;
    private String descPuesto;
    private Integer codSector;
    private String descSector;
    private String apellido1;
    private String apellido2;
    private String nombre;
    private String impSubvSol;
    private String impSubvEst;
    private String impSubvOfe;
    private String reintegros;
    private String impTotal;
    private String salarioSol;
    private String salarioOfe;
    private String sexoSol;
    private String sexoOfe;
    private String dptoSol;
    private String dptoOfe;
    private String codTitulacionSol;
    private String codTitulacionOfe;
    private String modalidadSol;
    private String modalidadOfe;
    private String codModSol;
    private String codModOfe;
    private String jornadaLabSol;
    private String jornadaLabOfe;
    private String centroTrabSol;
    private String centroTrabOfe;
    private String ctaCotizSol;
    private String ctaCotizOfe;
    private String fecIniSol;
    private String fecFinSol;
    private String fecIniOfe;
    private String fecFinOfe;
    private String grupoCotizSol;
    private String grupoCotizOfe;
    private String convenioColSol;
    private String convenioColOfe;
    private String observaciones;
    private String dni;
    private String fecNacimiento;
    private String num_oferta;
    private String centroColGestion;
    private String centroColCaptacion;
    private String impPago1;
    private String impPago2;
    
    public FilaPuestoVO()
    {
        
    }

    public int getIdPuesto() {
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
    
    public String getImpSubvSol() {
        return impSubvSol;
    }

    public void setImpSubvSol(String impSubvSol) {
        this.impSubvSol = impSubvSol;
    }
    
    public String getImpSubvEst() {
        return impSubvEst;
    }

    public void setImpSubvEst(String impSubvEst) {
        this.impSubvEst = impSubvEst;
    }
    
    public String getImpSubvOfe() {
        return impSubvOfe;
    }

    public void setImpSubvOfe(String impSubvOfe) {
        this.impSubvOfe = impSubvOfe;
    }
    
    public String getReintegros() {
        return reintegros;
    }

    public void setReintegros(String reintegros) {
        this.reintegros = reintegros;
    }

     public String getImpTotal() {
        return impTotal;
    }

    public void setImpTotal(String impTotal) {
        this.impTotal = impTotal;
    }
    
    public String getSalarioSol() {
        return salarioSol;
    }

    public void setSalarioSol(String salarioSol) {
        this.salarioSol = salarioSol;
    }
    
    public String getSalarioOfe() {
        return salarioOfe;
    }

    public void setSalarioOfe(String salarioOfe) {
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
    public String getJornadaLabSol() {
        return jornadaLabSol;
    }

    /**
     * @param jornadaLabSol the jornadaLabSol to set
     */
    public void setJornadaLabSol(String jornadaLabSol) {
        this.jornadaLabSol = jornadaLabSol;
    }

    /**
     * @return the jornadaLabOfe
     */
    public String getJornadaLabOfe() {
        return jornadaLabOfe;
    }

    /**
     * @param jornadaLabOfe the jornadaLabOfe to set
     */
    public void setJornadaLabOfe(String jornadaLabOfe) {
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
    public String getSexoSol() {
        return sexoSol;
    }

    /**
     * @param sexoSol the sexoSol to set
     */
    public void setSexoSol(String sexoSol) {
        this.sexoSol = sexoSol;
    }

    /**
     * @return the sexoOfe
     */
    public String getSexoOfe() {
        return sexoOfe;
    }

    /**
     * @param sexoOfe the sexoOfe to set
     */
    public void setSexoOfe(String sexoOfe) {
        this.sexoOfe = sexoOfe;
    }

    /**
     * @return the fecIniSol
     */
    public String getFecIniSol() {
        return fecIniSol;
    }

    /**
     * @param fecIniSol the fecIniSol to set
     */
    public void setFecIniSol(String fecIniSol) {
        this.fecIniSol = fecIniSol;
    }

    /**
     * @return the fecFinSol
     */
    public String getFecFinSol() {
        return fecFinSol;
    }

    /**
     * @param fecFinSol the fecFinSol to set
     */
    public void setFecFinSol(String fecFinSol) {
        this.fecFinSol = fecFinSol;
    }

    /**
     * @return the fecIniOfe
     */
    public String getFecIniOfe() {
        return fecIniOfe;
    }

    /**
     * @param fecIniOfe the fecIniOfe to set
     */
    public void setFecIniOfe(String fecIniOfe) {
        this.fecIniOfe = fecIniOfe;
    }

    /**
     * @return the fecFinOfe
     */
    public String getFecFinOfe() {
        return fecFinOfe;
    }

    /**
     * @param fecFinOfe the fecFinOfe to set
     */
    public void setFecFinOfe(String fecFinOfe) {
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
     * @return the codSector
     */
    public Integer getCodSector() {
        return codSector;
    }

    /**
     * @param codSector the codSector to set
     */
    public void setCodSector(Integer codSector) {
        this.codSector = codSector;
    }

    /**
     * @return the descSector
     */
    public String getDescSector() {
        return descSector;
    }

    /**
     * @param descSector the descSector to set
     */
    public void setDescSector(String descSector) {
        this.descSector = descSector;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the fecNacimiento
     */
    public String getFecNacimiento() {
        return fecNacimiento;
    }

    /**
     * @param fecNacimiento the fecNacimiento to set
     */
    public void setFecNacimiento(String fecNacimiento) {
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
    public String getImpPago1() {
        return impPago1;
    }

    /**
     * @param impPago1 the impPago1 to set
     */
    public void setImpPago1(String impPago1) {
        this.impPago1 = impPago1;
    }

    /**
     * @return the impPago2
     */
    public String getImpPago2() {
        return impPago2;
    }

    /**
     * @param impPago2 the impPago2 to set
     */
    public void setImpPago2(String impPago2) {
        this.impPago2 = impPago2;
    }

    

    

}

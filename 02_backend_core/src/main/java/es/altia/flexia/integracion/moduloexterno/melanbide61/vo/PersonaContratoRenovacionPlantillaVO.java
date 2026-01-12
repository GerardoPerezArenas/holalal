/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class PersonaContratoRenovacionPlantillaVO 
{
    private Integer codTipoPersona;//PEXCO_CTPE
    private String numDoc;//PEXCO_DOC
    private Long codTercero;//PEXCO_TER_COD
    private String nombre;//PEXCO_NOM
    private String apellido1;//PEXCO_AP1
    private String apellido2;//PEXCO_AP2
    private Date feNac;//PEXCO_FNA
    private Integer sexo;//PEXCO_SEX
    private String flMinusvalido;//PEXCO_MIN
    private String flInmigrante;//PEXCO_INM
    private String flPld;//PEXCO_PLD
    private String flRml;//PEXCO_RML
    private String flOtr;//PEXCO_OTR
    private Integer colectivo;//PEXCO_COL
    private Integer mesesDesempleo;//PEXCO_MDE
    private Integer nivelEstudios;//PEXCO_NES
    private String tipoContrato;//PEXCO_TCO
    private Date feAlta;//PEXCO_FAC
    private String tipoJornada;//PEXCO_TJO
    private Date feFinContrato;//PEXCO_FFC
    private Integer duracionContrato;//PEXCO_DCO
    private String cnoe;//PEXCO_CNOE
    private BigDecimal retSalarial;//PEXCO_SAL
    private BigDecimal impSubvencion;//PEXCO_IMP
    private Date feBaja;//PEXCO_FBE
    private BigDecimal porReduJor;//PEXCO_PRJ
    private Date fecContrato;
    private Date fecInConPre;
    private Date fecFinConPre;
    private Date fecInConPre2;
    private Date fecFinConPre2;
    private Date fecInConAd;
    private Date fecFinConAd;
    private String convenio;
    private Date fechaPublica;    
    private String dias;
    private Date fechaIniContrato;
    private Date fechaFinContrato;
    private String diasF;
    private String diasI;
    private String diasContrato;
    private String fondo;
    private String sitPrevia;
    
    public PersonaContratoRenovacionPlantillaVO()
    {
        
    }

    public Integer getCodTipoPersona() {
        return codTipoPersona;
    }
    public void setCodTipoPersona(Integer codTipoPersona) {
        this.codTipoPersona = codTipoPersona;
    }

    public String getNumDoc() {
        return numDoc;
    }
    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Long getCodTercero() {
        return codTercero;
    }
    public void setCodTercero(Long codTercero) {
        this.codTercero = codTercero;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Date getFeNac() {
        return feNac;
    }
    public void setFeNac(Date feNac) {
        this.feNac = feNac;
    }

    public Integer getSexo() {
        return sexo;
    }
    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public String getFlMinusvalido() {
        return flMinusvalido;
    }
    public void setFlMinusvalido(String flMinusvalido) {
        this.flMinusvalido = flMinusvalido;
    }

    public String getFlInmigrante() {
        return flInmigrante;
    }
    public void setFlInmigrante(String flInmigrante) {
        this.flInmigrante = flInmigrante;
    }

    public String getFlPld() {
        return flPld;
    }
    public void setFlPld(String flPld) {
        this.flPld = flPld;
    }

    public String getFlRml() {
        return flRml;
    }
    public void setFlRml(String flRml) {
        this.flRml = flRml;
    }

    public String getFlOtr() {
        return flOtr;
    }
    public void setFlOtr(String flOtr) {
        this.flOtr = flOtr;
    }

    public Integer getColectivo() {
        return colectivo;
    }
    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public Integer getMesesDesempleo() {
        return mesesDesempleo;
    }
    public void setMesesDesempleo(Integer mesesDesempleo) {
        this.mesesDesempleo = mesesDesempleo;
    }

    public Integer getNivelEstudios() {
        return nivelEstudios;
    }
    public void setNivelEstudios(Integer nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }
    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Date getFeAlta() {
        return feAlta;
    }
    public void setFeAlta(Date feAlta) {
        this.feAlta = feAlta;
    }

    public String getTipoJornada() {
        return tipoJornada;
    }
    public void setTipoJornada(String tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public Date getFeFinContrato() {
        return feFinContrato;
    }
    public void setFeFinContrato(Date feFinContrato) {
        this.feFinContrato = feFinContrato;
    }

    public Integer getDuracionContrato() {
        return duracionContrato;
    }
    public void setDuracionContrato(Integer duracionContrato) {
        this.duracionContrato = duracionContrato;
    }

    public String getCnoe() {
        return cnoe;
    }
    public void setCnoe(String cnoe) {
        this.cnoe = cnoe;
    }

    public BigDecimal getRetSalarial() {
        return retSalarial;
    }
    public void setRetSalarial(BigDecimal retSalarial) {
        this.retSalarial = retSalarial;
    }

    public BigDecimal getImpSubvencion() {
        return impSubvencion;
    }
    public void setImpSubvencion(BigDecimal impSubvencion) {
        this.impSubvencion = impSubvencion;
    }

    public Date getFeBaja() {
        return feBaja;
    }
    public void setFeBaja(Date feBaja) {
        this.feBaja = feBaja;
    }

    public BigDecimal getPorReduJor() {
        return porReduJor;
    }
    public void setPorReduJor(BigDecimal porReduJor) {
        this.porReduJor = porReduJor;
    }
    
    public Date getFecContrato() {
        return fecContrato;
    }
    public void setFecContrato(Date fecContrato) {
        this.fecContrato = fecContrato;
    }
    
    public Date getFecInConPre() {
        return fecInConPre;
    }
    public void setFecInConPre(Date fecInConPre) {
        this.fecInConPre = fecInConPre;
    }
    
    public Date getFecFinConPre() {
        return fecFinConPre;
    }
    public void setFecFinConPre(Date fecFinConPre) {
        this.fecFinConPre = fecFinConPre;
    }
    
    public Date getFecInConPre2() {
        return fecInConPre2;
    }
    public void setFecInConPre2(Date fecInConPre2) {
        this.fecInConPre2 = fecInConPre2;
    }
    
    public Date getFecFinConPre2() {
        return fecFinConPre2;
    }
    public void setFecFinConPre2(Date fecFinConPre2) {
        this.fecFinConPre2 = fecFinConPre2;
    }
    
    public Date getFecInConAd() {
        return fecInConAd;
    }
    public void setFecInConAd(Date fecInConAd) {
        this.fecInConAd = fecInConAd;
    }
    
    public Date getFecFinConAd() {
        return fecFinConAd;
    }
    public void setFecFinConAd(Date fecFinConAd) {
        this.fecFinConAd = fecFinConAd;
    }
    
    public String getConvenio() {
        return convenio;
    }
    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }
    
    public Date getFechaPublica() {
        return fechaPublica;
    }
    public void setFechaPublica(Date fechaPublica) {
        this.fechaPublica = fechaPublica;
    }    
    
    public String getDias() {
        return dias;
    }
    public void setDias(String dias) {
        this.dias = dias;
    }
    
    public Date getFechaIniContrato() {
        return fechaIniContrato;
    }
    public void setFechaIniContrato(Date fechaIniContrato) {
        this.fechaIniContrato = fechaIniContrato;
    }  
    
    public Date getFechaFinContrato() {
        return fechaFinContrato;
    }
    public void setFechaFinContrato(Date fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }  
    
    public String getDiasF() {
        return diasF;
    }
    public void setDiasF(String diasF) {
        this.diasF = diasF;
    }
    
    public String getDiasI() {
        return diasI;
    }
    public void setDiasI(String diasI) {
        this.diasI = diasI;
    }
    
    public String getDiasContrato() {
        return diasContrato;
    }
    public void setDiasContrato(String diasContrato) {
        this.diasContrato = diasContrato;
    }
    
    public String getFondo() {
        return fondo;
    }
    public void setFondo(String fondo) {
        this.fondo = fondo;
    }
    
    public String getSitPrevia() {
        return sitPrevia;
    }
    public void setSitPrevia(String sitPrevia) {
        this.sitPrevia = sitPrevia;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof PersonaContratoRenovacionPlantillaVO)
        {
            PersonaContratoRenovacionPlantillaVO aux = (PersonaContratoRenovacionPlantillaVO)o;
            if(this.getNumDoc() != null && aux.getNumDoc() != null)
            {
                return this.getNumDoc().equalsIgnoreCase(aux.getNumDoc());
            }
            else if(this.getNumDoc() == null && aux.getNumDoc() == null)
            {
                return true;
            }
        }
        return false;
    } 
       
}

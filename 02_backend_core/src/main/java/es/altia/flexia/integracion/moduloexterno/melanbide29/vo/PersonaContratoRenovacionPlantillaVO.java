/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide29.vo;

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
    private Integer tipoContrato;//PEXCO_TCO
    private Date feAlta;//PEXCO_FAC
    private String tipoJornada;//PEXCO_TJO
    private Date feFinContrato;//PEXCO_FFC
    private Integer duracionContrato;//PEXCO_DCO
    private String cnoe;//PEXCO_CNOE
    private BigDecimal retSalarial;//PEXCO_SAL
    private BigDecimal impSubvencion;//PEXCO_IMP
    private Date feBaja;//PEXCO_FBE
    private BigDecimal porReduJor;//PEXCO_PRJ
    
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

    public Integer getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Integer tipoContrato) {
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

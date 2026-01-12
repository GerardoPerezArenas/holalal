/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SantiagoC
 */
public class EcaJusProspectoresVO 
{
    private Integer solProspectoresCod;
    private Integer jusProspectoresCod;
    private String nif;
    private String nombre;
    private Date fecIni;
    private Date fecFin;
    private BigDecimal horasJC;
    private BigDecimal horasCont;
    private BigDecimal horasEca;
    private BigDecimal impSSJC;
    private BigDecimal impSSJR;
    private BigDecimal impSSECA;
    private Integer numEmpVisitar;
    private BigDecimal impVisitas;
    private BigDecimal impTotalSolic;   
    private Integer solicitud;
    private Integer tipoContrato;
    private Integer jusProspectorOrigen;
    private String tipoSust;
    
    public EcaJusProspectoresVO()
    {
        
    }

    public Integer getSolProspectoresCod() {
        return solProspectoresCod;
    }

    public void setSolProspectoresCod(Integer solProspectoresCod) {
        this.solProspectoresCod = solProspectoresCod;
    }

    public Integer getJusProspectoresCod() {
        return jusProspectoresCod;
    }

    public void setJusProspectoresCod(Integer jusProspectoresCod) {
        this.jusProspectoresCod = jusProspectoresCod;
    }    

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public BigDecimal getHorasJC() {
        return horasJC;
    }

    public void setHorasJC(BigDecimal horasJC) {
        this.horasJC = horasJC;
    }

    public BigDecimal getHorasCont() {
        return horasCont;
    }

    public void setHorasCont(BigDecimal horasCont) {
        this.horasCont = horasCont;
    }

    public BigDecimal getHorasEca() {
        return horasEca;
    }

    public void setHorasEca(BigDecimal horasEca) {
        this.horasEca = horasEca;
    }

    public BigDecimal getImpSSJC() {
        return impSSJC;
    }

    public void setImpSSJC(BigDecimal impSSJC) {
        this.impSSJC = impSSJC;
    }

    public BigDecimal getImpSSJR() {
        return impSSJR;
    }

    public void setImpSSJR(BigDecimal impSSJR) {
        this.impSSJR = impSSJR;
    }

    public BigDecimal getImpSSECA() {
        return impSSECA;
    }

    public void setImpSSECA(BigDecimal impSSECA) {
        this.impSSECA = impSSECA;
    }

    public Integer getNumEmpVisitar() {
        return numEmpVisitar;
    }

    public void setNumEmpVisitar(Integer numEmpVisitar) {
        this.numEmpVisitar = numEmpVisitar;
    }

    public BigDecimal getImpVisitas() {
        return impVisitas;
    }

    public void setImpVisitas(BigDecimal impVisitas) {
        this.impVisitas = impVisitas;
    }

    public BigDecimal getImpTotalSolic() {
        return impTotalSolic;
    }

    public void setImpTotalSolic(BigDecimal impTotalSolic) {
        this.impTotalSolic = impTotalSolic;
    }

    

    public Integer getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Integer solicitud) {
        this.solicitud = solicitud;
    }

    public Integer getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Integer tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Integer getJusProspectorOrigen() {
        return jusProspectorOrigen;
    }

    public void setJusProspectorOrigen(Integer jusProspectorOrigen) {
        this.jusProspectorOrigen = jusProspectorOrigen;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }
}

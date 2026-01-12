/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SantiagoC
 */
public class EcaJusPreparadoresVO 
{
    private Integer solPreparadoresCod;
    private Integer jusPreparadoresCod;
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
    private BigDecimal segAnt;
    private BigDecimal impSegAnt;//TODO: revisar tipo dato Integer-BigDecimal   
    private BigDecimal insImporte;
    private BigDecimal insSegImporte;
    private BigDecimal coste;
    private Integer solicitud;
    private Integer tipoContrato;
    private Integer jusPreparadorOrigen;
    private String tipoSust;
    
    public EcaJusPreparadoresVO()
    {
        
    }

    public Integer getSolPreparadoresCod() {
        return solPreparadoresCod;
    }

    public void setSolPreparadoresCod(Integer solPreparadoresCod) {
        this.solPreparadoresCod = solPreparadoresCod;
    }
    
    public Integer getJusPreparadoresCod() {
        return jusPreparadoresCod;
    }

    public void setJusPreparadoresCod(Integer jusPreparadoresCod) {
        this.jusPreparadoresCod = jusPreparadoresCod;
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

    public BigDecimal getSegAnt() {
        return segAnt;
    }

    public void setSegAnt(BigDecimal segAnt) {
        this.segAnt = segAnt;
    }

    public BigDecimal getImpSegAnt() {
        return impSegAnt;
    }

    public void setImpSegAnt(BigDecimal impSegAnt) {
        this.impSegAnt = impSegAnt;
    }

    public BigDecimal getInsImporte() {
        return insImporte;
    }

    public void setInsImporte(BigDecimal insImporte) {
        this.insImporte = insImporte;
    }

    public BigDecimal getInsSegImporte() {
        return insSegImporte;
    }

    public void setInsSegImporte(BigDecimal insSegImporte) {
        this.insSegImporte = insSegImporte;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
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

    public Integer getJusPreparadorOrigen() {
        return jusPreparadorOrigen;
    }

    public void setJusPreparadorOrigen(Integer jusPreparadorOrigen) {
        this.jusPreparadorOrigen = jusPreparadorOrigen;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }
}

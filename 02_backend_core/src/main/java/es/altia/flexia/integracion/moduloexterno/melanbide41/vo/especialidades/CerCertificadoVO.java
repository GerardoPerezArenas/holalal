package es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 *  <li>david.caamano * 27-09-2012 * #90628 Edición inicial</li>
 * </ol> 
 */
public class CerCertificadoVO {
    
    //Propiedades
    private String codCertificado;
    private String numExpediente;
    private Integer codOrganizacion;
    private String codProcedimiento;
    private String descCertificadoC;
    private String descCertificadoE;
    //private String codArea;
    private String nivel;
    private String tieneModulo;
    private String abreviado;
    private String estado;
    private String docBase;
    private String decreto;
    private String tieneRD;
    //private CerAreaVO area;
    //private ArrayList<CerUnidadeCompetencialVO> unidades;
    //private ArrayList<CerModuloFormativoVO> modulosFormativos;
    private Integer tipoCP;
    private Date fechaRD;
    private String RDModif;
    private Date fechaRDModif;
    private String RDDeroga;
    private Date fechaRDDeroga;
    private String decretoMod;
    
    //Métodos de acceso
    public String getCodCertificado() {
        return codCertificado;
    }//getCodCertificado
    public void setCodCertificado(String codCertificado) {
        this.codCertificado = codCertificado;
    }//setCodCertificado
    
    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente
    
    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion

    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento
    
    public String getDescCertificadoC() {
        return descCertificadoC;
    }//getDescCertificadoC
    public void setDescCertificadoC(String descCertificadoC) {
        this.descCertificadoC = descCertificadoC;
    }//setDescCertificadoC

    public String getDescCertificadoE() {
        return descCertificadoE;
    }//getDescCertificadoE
    public void setDescCertificadoE(String descCertificadoE) {
        this.descCertificadoE = descCertificadoE;
    }//setDescCertificadoE
    
    /*
    public String getCodArea() {
        return codArea;
    }//getCodArea
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }//setCodArea
    */
    
    public String getNivel() {
        return nivel;
    }//getNivel
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }//setNivel

    public String getTieneModulo() {
        return tieneModulo;
    }//getTieneModulo
    
    public void setTieneModulo(String tieneModulo) {
        this.tieneModulo = tieneModulo;
    }//setTieneModulo
    
    public String getAbreviado() {
        return abreviado;
    }//getAbreviado
    public void setAbreviado(String abreviado) {
        this.abreviado = abreviado;
    }//setAbreviado
    
    public String getEstado() {
        return estado;
    }//getEstado
    public void setEstado(String estado) {
        this.estado = estado;
    }//setEstado
    
    public String getDocBase() {
        return docBase;
    }//getDocBase
    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }//setDocBase
    
    public String getDecreto() {
        return decreto;
    }//getDecreto
    public void setDecreto(String decreto) {
        this.decreto = decreto;
    }//setDecreto
    
    public String getTieneRD() {
        return tieneRD;
    }//getTieneRD
    public void setTieneRD(String tieneRD) {
        this.tieneRD = tieneRD;
    }//setTieneRD
    
    /*
    public CerAreaVO getArea() {
        return area;
    }//getArea
    public void setArea(CerAreaVO area) {
        this.area = area;
    }//setArea
    
    public ArrayList<CerUnidadeCompetencialVO> getUnidades() {
        return unidades;
    }//getUnidades
    public void setUnidades(ArrayList<CerUnidadeCompetencialVO> unidades) {
        this.unidades = unidades;
    }//setUnidades

    public ArrayList<CerModuloFormativoVO> getModulosFormativos() {
        return modulosFormativos;
    }//getModulosFormativos
    public void setModulosFormativos(ArrayList<CerModuloFormativoVO> modulosFormativos) {
        this.modulosFormativos = modulosFormativos;
    }//setModulosFormativos
    
    */

    public Integer getTipoCP() {
        return tipoCP;
    }//getTipoCP
    public void setTipoCP(Integer tipoCP) {
        this.tipoCP = tipoCP;
    }//setTipoCP

    public Date getFechaRD() {
        return fechaRD;
    }//getFechaRD
    public void setFechaRD(Date fechaRD) {
        this.fechaRD = fechaRD;
    }//setFechaRD

    public String getRDModif() {
        return RDModif;
    }//getRDModif
    public void setRDModif(String RDModif) {
        this.RDModif = RDModif;
    }//setRDModif

    public Date getFechaRDModif() {
        return fechaRDModif;
    }//getFechaRDModif
    public void setFechaRDModif(Date fechaRDModif) {
        this.fechaRDModif = fechaRDModif;
    }//setFechaRDModif

    public String getRDDeroga() {
        return RDDeroga;
    }//getRDDeroga
    public void setRDDeroga(String RDDeroga) {
        this.RDDeroga = RDDeroga;
    }//setRDDeroga

    public Date getFechaRDDeroga() {
        return fechaRDDeroga;
    }//getFechaRDDeroga
    public void setFechaRDDeroga(Date fechaRDDeroga) {
        this.fechaRDDeroga = fechaRDDeroga;
    }//setFechaRDDeroga
    
    /*
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("CerCertificadoVO{ ");
        sb.append("codCertificado = ");
        sb.append(codCertificado);
        sb.append(" ");
        sb.append("numExpediente = ");
        sb.append(numExpediente);
        sb.append(" ");
        sb.append("codOrganizacion = ");
        sb.append(codOrganizacion);
        sb.append(" ");
        sb.append("codProcedimiento = ");
        sb.append(codProcedimiento);
        sb.append(" ");
        sb.append("descCertificadoC = ");
        sb.append(descCertificadoC);
        sb.append(" ");
        sb.append("descCertificadoE = ");
        sb.append(descCertificadoE);
        sb.append(" ");
        sb.append("codArea = ");
        sb.append(codArea);
        sb.append(" ");
        sb.append("nivel = ");
        sb.append(nivel);
        sb.append(" ");
        sb.append("tieneModulo = ");
        sb.append(tieneModulo);
        sb.append(" ");
        sb.append("abreviado = ");
        sb.append(abreviado);
        sb.append(" ");
        sb.append("estado = ");
        sb.append(estado);
        sb.append(" ");
        sb.append("docBase = ");
        sb.append(docBase);
        sb.append(" ");
        sb.append("decreto  = ");
        sb.append(decreto);
        if(tieneRD != null){
            sb.append(" ");
            sb.append("tieneRD = ");
            sb.append(tieneRD.toString());
        }//if(tieneRD != null)
        if(unidades != null){
            sb.append(" ");
            sb.append("areas { ");
            for(CerUnidadeCompetencialVO cerUnidadeCompetencialVO : unidades){
                sb.append("area ( ");
                sb.append(cerUnidadeCompetencialVO);
                sb.append(" ) ");
            }//for(CerUnidadeCompetencialVO cerUnidadeCompetencialVO : area)
            sb.append("} ");
        }//if(area != null){
        sb.append(" }; ");
        return sb.toString();
    }//toString
    
    */

    /**
     * @return the decretoMod
     */
    public String getDecretoMod() {
        return decretoMod;
    }

    /**
     * @param decretoMod the decretoMod to set
     */
    public void setDecretoMod(String decretoMod) {
        this.decretoMod = decretoMod;
    }
}//class

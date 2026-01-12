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
public class CentroColVO 
{
    private String numExp;
    private int idCentroCol;
    private String ofertaEmpleo;
    private int numOfertaSol;
    private int numOfertaOfe;
    private int numConSubvSol;
    private int numConSubvOfe;
    private int subvencion;
    private BigDecimal impSubvSol;
    private BigDecimal impSubvOfe;
    private String observaciones;
  

    public CentroColVO()
    {
        
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    /**
     * @return the idCentroCol
     */
    public int getIdCentroCol() {
        return idCentroCol;
    }

    /**
     * @param idCentroCol the idCentroCol to set
     */
    public void setIdCentroCol(int idCentroCol) {
        this.idCentroCol = idCentroCol;
    }

    /**
     * @return the ofertaEmpleo
     */
    public String getOfertaEmpleo() {
        return ofertaEmpleo;
    }

    /**
     * @param ofertaEmpleo the ofertaEmpleo to set
     */
    public void setOfertaEmpleo(String ofertaEmpleo) {
        this.ofertaEmpleo = ofertaEmpleo;
    }

    /**
     * @return the numOfertaSol
     */
    public int getNumOfertaSol() {
        return numOfertaSol;
    }

    /**
     * @param numOfertaSol the numOfertaSol to set
     */
    public void setNumOfertaSol(int numOfertaSol) {
        this.numOfertaSol = numOfertaSol;
    }

    /**
     * @return the numOfertaOfe
     */
    public int getNumOfertaOfe() {
        return numOfertaOfe;
    }

    /**
     * @param numOfertaOfe the numOfertaOfe to set
     */
    public void setNumOfertaOfe(int numOfertaOfe) {
        this.numOfertaOfe = numOfertaOfe;
    }

    /**
     * @return the numConSubvSol
     */
    public int getNumConSubvSol() {
        return numConSubvSol;
    }

    /**
     * @param numConSubvSol the numConSubvSol to set
     */
    public void setNumConSubvSol(int numConSubvSol) {
        this.numConSubvSol = numConSubvSol;
    }

    /**
     * @return the numConSubvOfe
     */
    public int getNumConSubvOfe() {
        return numConSubvOfe;
    }

    /**
     * @param numConSubvOfe the numConSubvOfe to set
     */
    public void setNumConSubvOfe(int numConSubvOfe) {
        this.numConSubvOfe = numConSubvOfe;
    }

    /**
     * @return the subvencion
     */
    public int getSubvencion() {
        return subvencion;
    }

    /**
     * @param subvencion the subvencion to set
     */
    public void setSubvencion(int subvencion) {
        this.subvencion = subvencion;
    }

    /**
     * @return the impSubvSol
     */
    public BigDecimal getImpSubvSol() {
        return impSubvSol;
    }

    /**
     * @param impSubvSol the impSubvSol to set
     */
    public void setImpSubvSol(BigDecimal impSubvSol) {
        this.impSubvSol = impSubvSol;
    }

    /**
     * @return the impSubvOfe
     */
    public BigDecimal getImpSubvOfe() {
        return impSubvOfe;
    }

    /**
     * @param impSubvOfe the impSubvOfe to set
     */
    public void setImpSubvOfe(BigDecimal impSubvOfe) {
        this.impSubvOfe = impSubvOfe;
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

   

   
   
}

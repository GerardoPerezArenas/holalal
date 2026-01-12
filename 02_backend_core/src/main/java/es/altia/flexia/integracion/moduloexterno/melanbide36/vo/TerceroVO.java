/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.vo;

/**
 *
 * @author santiagoc
 */
public class TerceroVO 
{

    private boolean existeTer;
    private String  codTercero;
    private Integer verTercero;
    private String  codDocumento;
    private String  codDomicilio;
    private Integer numGraba;
    
    public TerceroVO()
    {
        
    }

    /**
     * @return the existeTer
     */
    public boolean isExisteTer() {
        return existeTer;
    }

    /**
     * @param existeTer the existeTer to set
     */
    public void setExisteTer(boolean existeTer) {
        this.existeTer = existeTer;
    }
    
     /**
     * @return the codTercero
     */
    public String getCodTercero() {
        return codTercero;
    }

    /**
     * @param codTercero the codTercero to set
     */
    public void setCodTercero(String codTercero) {
        this.codTercero = codTercero;
    }

    /**
     * @return the verTercero
     */
    public Integer getVerTercero() {
        return verTercero;
    }

    /**
     * @param verTercero the verTercero to set
     */
    public void setVerTercero(Integer verTercero) {
        this.verTercero = verTercero;
    }
    
    /**
     * @return the codDocumento
     */
    public String getCodDocumento() {
        return codDocumento;
    }

    /**
     * @param codDocumento the codDocumento to set
     */
    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }

    /**
     * @return the codDomicilio
     */
    public String getCodDomicilio() {
        return codDomicilio;
    }

    /**
     * @param codDomicilio the codDomicilio to set
     */
    public void setCodDomicilio(String codDomicilio) {
        this.codDomicilio = codDomicilio;
    }

    /**
     * @return the numGraba
     */
    public Integer getNumGraba() {
        return numGraba;
    }

    /**
     * @param numGraba the numGraba to set
     */
    public void setNumGraba(Integer numGraba) {
        this.numGraba = numGraba;
    }
}

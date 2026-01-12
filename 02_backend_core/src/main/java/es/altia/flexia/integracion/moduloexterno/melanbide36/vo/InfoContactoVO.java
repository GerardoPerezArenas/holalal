/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.vo;

/**
 *
 * @author santiagoc
 */
public class InfoContactoVO 
{

    private Integer codOrganizacion;
    private String  numExpediente;
    private String  numExpAnterior;
    private String  codTercero;
    private Integer verTercero;
    
    public InfoContactoVO()
    {
        
    }
    /**
     * @return the codOrganizacion
     */
    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }
    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the numExpAnterior
     */
    public String getNumExpAnterior() {
        return numExpAnterior;
    }

    /**
     * @param numExpAnterior the numExpAnterior to set
     */
    public void setNumExpAnterior(String numExpAnterior) {
        this.numExpAnterior = numExpAnterior;
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

}

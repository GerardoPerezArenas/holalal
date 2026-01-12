/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author laura
 */
public class ExpTram {
    String numExpediente;
    int codTramite;

    public ExpTram(String numExpediente, int codTramite)
    {
        this.numExpediente = numExpediente;
        this.codTramite = codTramite;
    } 
    public ExpTram()
    {       
    } 

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }
    
}

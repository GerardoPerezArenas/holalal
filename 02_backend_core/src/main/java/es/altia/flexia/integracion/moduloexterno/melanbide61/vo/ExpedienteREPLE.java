/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.vo;

import java.util.Date;

/**
 *
 * @author laura
 */
public class ExpedienteREPLE {
    String numExpediente;
    Date fecResolucion;
    Date fecContratoRelevo;

    public ExpedienteREPLE()
    {
        
    }
    
    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Date getFecResolucion() {
        return fecResolucion;
    }

    public void setFecResolucion(Date fecResolucion) {
        this.fecResolucion = fecResolucion;
    }

    public Date getFecContratoRelevo() {
        return fecContratoRelevo;
    }

    public void setFecContratoRelevo(Date fecContratoRelevo) {
        this.fecContratoRelevo = fecContratoRelevo;
    }
    
    
    
}

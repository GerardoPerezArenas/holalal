/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.vo;

import java.util.Date;

/**
 *
 * @author lauras
 */
public class DatosAviso {
    private String numExp;
    private Date fecEstudio;
    private Date fecSolicitud;    
    private String solicitante;
    
    public DatosAviso()
    {
        
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Date getFecEstudio() {
        return fecEstudio;
    }

    public void setFecEstudio(Date fecEstudio) {
        this.fecEstudio = fecEstudio;
    }

    public Date getFecSolicitud() {
        return fecSolicitud;
    }

    public void setFecSolicitud(Date fecSolicitud) {
        this.fecSolicitud = fecSolicitud;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

  
}

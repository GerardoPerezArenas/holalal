/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.util.Date;

/**
 *
 * @author javier
 */
public class TramiteVO {
   private String codTramInterno;
   private String descTramite;
   // Cierre y apertura de Esperas COLEC/ORI/CEMP #350951
   private Date fechaInicioTramite;
   private Date fechaFinTramite;

    /**
     * @return the codTramInterno
     */
    public String getCodTramInterno() {
        return codTramInterno;
    }

    /**
     * @param codTramInterno the codTramInterno to set
     */
    public void setCodTramInterno(String codTramInterno) {
        this.codTramInterno = codTramInterno;
    }

    /**
     * @return the descTramite
     */
    public String getDescTramite() {
        return descTramite;
    }

    /**
     * @param descTramite the descTramite to set
     */
    public void setDescTramite(String descTramite) {
        this.descTramite = descTramite;
    }

    public Date getFechaInicioTramite() {
        return fechaInicioTramite;
    }

    public void setFechaInicioTramite(Date fechaInicioTramite) {
        this.fechaInicioTramite = fechaInicioTramite;
    }

    public Date getFechaFinTramite() {
        return fechaFinTramite;
    }

    public void setFechaFinTramite(Date fechaFinTramite) {
        this.fechaFinTramite = fechaFinTramite;
    }
    
    
   
}

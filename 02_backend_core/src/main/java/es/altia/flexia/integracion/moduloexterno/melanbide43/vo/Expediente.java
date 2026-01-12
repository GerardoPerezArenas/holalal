package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.util.Date;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edici�n inicial</li>
 * </ol> 
 */
public class Expediente {
    
    //Propiedades
    private String numExp;
    private Integer ejercicio;
    private String asunto;
    private Date fechaApertura;
    private Date fechaGenerado;
    private Date fechaSolicitud;
    private Date fechaCierre;
    private String estado;
    private String canal;
    private String numExpRel;
    private Integer ejercicioRel;
    
    //M�todos de acceso


    public String getNumExp() {
        return numExp;
    }//getDescUnidadC
    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }//setDescUnidadC
    
    public String getNumExpRel() {
        return numExpRel;
    }//getDescUnidadC
    public void setNumExpRel(String numExpRel) {
        this.numExpRel = numExpRel;
    }
    
    public Integer getEjercicio() {
        return ejercicio;
    }//getCentroAcreditado
    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }
    
    public Integer getEjercicioRel() {
        return ejercicioRel;
    }//getCentroAcreditado
    public void setEjercicioRel(Integer ejercicioRel) {
        this.ejercicioRel = ejercicioRel;
    }

    public String getAsunto() {
        return asunto;
    }//getCentroAcreditado
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }//setCentroAcreditado
    
    public Date getFechaGenerado() {
        return fechaGenerado;
    }//getCodOrganizacion
    public void setFechaGenerado(Date fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }//setCodOrganizacion
    
    public Date getFechaApertura() {
        return fechaApertura;
    }//getCodOrganizacion
    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }//setCodOrganizacion
    
    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }//getCodOrganizacion
    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
    
    public String getEstado() {
        return estado;
    }//getCodOrganizacion
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCanal() {
        return canal;
    }
    public void setCanal(String canal) {
        this.canal = canal;
    }    
    
    public Date getFechaCierre() {
        return fechaCierre;
    }//getCodOrganizacion
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
}//class

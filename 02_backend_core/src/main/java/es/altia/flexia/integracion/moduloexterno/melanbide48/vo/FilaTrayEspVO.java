/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author santiagoc
 */
public class FilaTrayEspVO 
{
    private Long codTray;
    private Long codEntidad;
    private String numExpediente;
    private Integer ejercicio;
    private Integer colectivo;
    private String nombreAdm;
    private String descAct;
    private Integer validada;
    private String cifEntidad;
    private String nomEntidad;
    
    public FilaTrayEspVO()
    {
        
    }

    public Long getCodTray() {
        return codTray;
    }

    public void setCodTray(Long codTray) {
        this.codTray = codTray;
    }

    public Long getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public String getNombreAdm() {
        return nombreAdm;
    }

    public void setNombreAdm(String nombreAdm) {
        this.nombreAdm = nombreAdm;
    }

    public String getDescAct() {
        return descAct;
    }

    public void setDescAct(String descAct) {
        this.descAct = descAct;
    }
    
    public String getCifEntidad() {
        return cifEntidad;
    }

    public void setCifEntidad(String cifEntidad) {
        this.cifEntidad = cifEntidad;
    }
    
    

    public String getNomEntidad() {
        return nomEntidad;
    }

    public void setNomEntidad(String nomEntidad) {
        this.nomEntidad = nomEntidad;
    }


    public Integer getValidada() {
        return validada;
    }

    public void setValidada(Integer validada) {
        this.validada = validada;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }
    
}

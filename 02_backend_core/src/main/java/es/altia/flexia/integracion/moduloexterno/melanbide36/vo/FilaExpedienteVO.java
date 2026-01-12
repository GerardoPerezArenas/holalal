/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.vo;

/**
 *
 * @author santiagoc
 */
public class FilaExpedienteVO 
{
    private String numExpedienteP29;
    private String numExpediente;
    private Integer numDias;
    private String tipActUsub;
    private String desTipActUsub;
    private Double importeAbonado;
    private String situacion;
    private String tipoSubvencion;
    private String feIni;
    private String feFin;
    private String fecNacPersDepen;
    
    public FilaExpedienteVO()
    {
        
    }

    public String getNumExpedienteP29() {
        return numExpedienteP29;
    }

    public void setNumExpedienteP29(String numExpedienteP29) {
        this.numExpedienteP29 = numExpedienteP29;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getNumDias() {
        return numDias;
    }

    public void setNumDias(Integer numDias) {
        this.numDias = numDias;
    }

    public String getTipActUsub() {
        return tipActUsub;
    }

    public void setTipActUsub(String tipActUsub) {
        this.tipActUsub = tipActUsub;
    }

    public String getDesTipActUsub() {
        return desTipActUsub;
    }

    public void setDesTipActUsub(String desTipActUsub) {
        this.desTipActUsub = desTipActUsub;
    }

    public Double getImporteAbonado() {
        return importeAbonado;
    }

    public void setImporteAbonado(Double importeAbonado) {
        this.importeAbonado = importeAbonado;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getTipoSubvencion() {
        return tipoSubvencion;
    }

    public void setTipoSubvencion(String tipoSubvencion) {
        this.tipoSubvencion = tipoSubvencion;
    }

    public String getFeIni() {
        return feIni;
    }

    public void setFeIni(String feIni) {
        this.feIni = feIni;
    }

    public String getFeFin() {
        return feFin;
    }

    public void setFeFin(String feFin) {
        this.feFin = feFin;
    }

    public String getFecNacPersDepen() {
        return fecNacPersDepen;
    }

    public void setFecNacPersDepen(String fecNacPersDepen) {
        this.fecNacPersDepen = fecNacPersDepen;
    }
}

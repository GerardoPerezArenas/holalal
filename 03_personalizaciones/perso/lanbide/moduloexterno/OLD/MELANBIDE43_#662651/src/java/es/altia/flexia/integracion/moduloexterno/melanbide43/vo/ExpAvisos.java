/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author sergio
 */
public class ExpAvisos {
    private String ejercicio;
    private String procedimiento;
    private String numExpediente;
    private String tipoNotificacion;
    private String email;

    public ExpAvisos(String ejercicio, String procedimiento, String numExpediente, String tipoNotificacion, String email)
    {
        this.ejercicio = ejercicio;
        this.procedimiento = procedimiento;
        this.numExpediente = numExpediente;
        this.tipoNotificacion = tipoNotificacion;
        this.email = email;        
    } 
    public ExpAvisos()
    {
        
    } 

    /**
     * @return the ejercicio
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
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
     * @return the tipoNotificacion
     */
    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    /**
     * @param tipoNotificacion the tipoNotificacion to set
     */
    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the procedimiento
     */
    public String getProcedimiento() {
        return procedimiento;
    }

    /**
     * @param procedimiento the procedimiento to set
     */
    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    
    
}

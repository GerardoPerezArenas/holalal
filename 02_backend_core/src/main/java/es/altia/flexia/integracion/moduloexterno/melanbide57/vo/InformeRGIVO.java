package es.altia.flexia.integracion.moduloexterno.melanbide57.vo;
import java.util.Date;

/**
 *
 * @author altia
 */
public class InformeRGIVO {

    
    private String numExpediente;
    private String procedimiento;
    private String interesadoNif;
    private String interesadoNombre;
    private String interesadoAp1;
    private String interesadoAp2;
    private String asunto;
    private Date fechaInicio;
    private Date fechaFin;
    private String usuInicio;

    
    
    public InformeRGIVO()
    {
        
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getInteresadoNif() {
        return interesadoNif;
    }

    public void setInteresadoNif(String interesadoNif) {
        this.interesadoNif = interesadoNif;
    }

    public String getInteresadoNombre() {
        return interesadoNombre;
    }

    public void setInteresadoNombre(String interesadoNombre) {
        this.interesadoNombre = interesadoNombre;
    }

    public String getInteresadoAp1() {
        return interesadoAp1;
    }

    public void setInteresadoAp1(String interesadoAp1) {
        this.interesadoAp1 = interesadoAp1;
    }

    public String getInteresadoAp2() {
        return interesadoAp2;
    }

    public void setInteresadoAp2(String interesadoAp2) {
        this.interesadoAp2 = interesadoAp2;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    
    public String getUsuInicio() {
        return usuInicio;
    }

    public void setUsuInicio(String usuInicio) {
        this.usuInicio = usuInicio;
    }


}

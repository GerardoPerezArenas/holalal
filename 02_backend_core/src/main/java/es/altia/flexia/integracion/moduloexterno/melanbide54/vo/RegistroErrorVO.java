package es.altia.flexia.integracion.moduloexterno.melanbide54.vo;

/**
 * Representa a una ubicación de un centro
 * @author oscar
 */
public class RegistroErrorVO {
    
    private String id;
    private String fechaErr;
    private String mensaje;
    private String mensajeExc;
    private String causa;
    private String errRev;
    private String fechaRev;
    private String observaciones;
    

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    
    public String getFechaErr() {
        return fechaErr;
    }
    public void setFechaErr(String fechaErr) {
        this.fechaErr = fechaErr;
    }
    
    
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
    public String getMensajeExc() {
        return mensajeExc;
    }
    public void setMensajeExc(String mensajeExc) {
        this.mensajeExc = mensajeExc;
    }

    
    public String getCausa() {
        return causa;
    }
    public void setCausa(String causa) {
        this.causa = causa;
    }

   
    public String getErrRev() {
        return errRev;
    }
    public void setErrRev(String errRev) {
        this.errRev = errRev;
    }

    
    public String getFechaRev() {
        return fechaRev;
    }
    public void setFechaRev(String fechaRev) {
        this.fechaRev = fechaRev;
    }

    
    public String getDescProvincia() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}

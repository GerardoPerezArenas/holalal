package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.util.Date;

/**
 * Clase VO para el report
 * 
 * @author david.caamano
 * @version 15/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 15-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03ReportVO {
    
    private String nombre;
    private Date fechaCreacion;
    private Integer codOrganizacion;
    private String numExpediente;
    private byte[] report;
    private Boolean localizado;
    private String idDokusi;
    private String mimeType;
    private String fechaString;
    
    public String getNombre() {
        return nombre;
    }//getNombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }//setNombre

    public Date getFechaCreacion() {
        return fechaCreacion;
    }//getFechaCreacion
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }//setFechaCreacion
    
    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion

    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente

    public byte[] getReport() {
        return report;
    }//getReport
    public void setReport(byte[] report) {
        this.report = report;
    }//setReport

    public String getIdDokusi() {return idDokusi;}
    public void setIdDokusi(String idDokusi) {this.idDokusi = idDokusi;}

    public Boolean getLocalizado() {return localizado;}
    public void setLocalizado(Boolean localizado) {this.localizado = localizado;}

    public String getMimeType() {
        return mimeType;
    }//getMimeType
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }//setMimeType

    public String getFechaString() {
        return fechaString;
    }//getFechaString
    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }//setFechaString

}//class
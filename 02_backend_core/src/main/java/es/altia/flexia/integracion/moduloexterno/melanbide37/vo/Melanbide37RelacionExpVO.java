package es.altia.flexia.integracion.moduloexterno.melanbide37.vo;

import java.io.Serializable;


public class Melanbide37RelacionExpVO implements Serializable {    
    //Propiedades
    private String codExpediente;
    private String codExpedienteRel;
    private String nif;
    private String nombreApe;
    private String fechaSoli;
    private String codigoCP;
    private String descCP;
    
    //M�todos de acceso
    public String getCodExpediente() {
        return codExpediente;
    }//getCodExpediente
    public void setCodExpediente(String codExpediente) {
        this.codExpediente = codExpediente;
    }//setCodExpediente
    
    public String getCodExpedienteRel() {
        return codExpedienteRel;
    }
    public void setCodExpedienteRel(String codExpedienteRel) {
        this.codExpedienteRel = codExpedienteRel;
    }

    public String getNif() {
        return nif;
    }
    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombreApe() {
        return nombreApe;
    }
    public void setNombreApe(String nombreApe) {
        this.nombreApe = nombreApe;
    }

    public String getFechaSoli() {
        return fechaSoli;
    }
    public void setFechaSoli(String fechaSoli) {
        this.fechaSoli = fechaSoli;
    }

    public String getCodigoCP() {
        return codigoCP;
    }
    public void setCodigoCP(String codigoCP) {
        this.codigoCP = codigoCP;
    }

    public String getDescCP() {
        return descCP;
    }
    public void setDescCP(String descCP) {
        this.descCP = descCP;
    }
    
}//class

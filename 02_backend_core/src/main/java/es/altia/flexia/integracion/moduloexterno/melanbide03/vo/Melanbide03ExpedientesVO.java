package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.io.Serializable;


public class Melanbide03ExpedientesVO implements Serializable {    
    //Propiedades
    private String codExpediente;
    private String desmotivo;
    
    //Métodos de acceso
    public String getCodExpediente() {
        return codExpediente;
    }//getCodExpediente
    public void setCodExpediente(String codExpediente) {
        this.codExpediente = codExpediente;
    }//setCodExpediente

    public String getDesMotivo() {
        return desmotivo;
    }//getDesMotivo
    public void setDesMotivo(String desmotivo) {
        this.desmotivo = desmotivo;
    }//setDesMotivo
}//class

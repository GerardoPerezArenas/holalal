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
public class Tramite {
    
    //Propiedades
    private String cod;
    private String fase;
    private String descripcion;
    private String descripcion_eu;
    
    //M�todos de acceso


    public String getCod() {
        return cod;
    }
    public void setCod(String cod) {
        this.cod = cod;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcionEu() {
        return descripcion_eu;
    }
    public void setDescripcionEu(String descripcion_eu) {
        this.descripcion_eu = descripcion_eu;
    }
    
    public String getFase() {
        return fase;
    }
    public void setFase(String fase) {
        this.fase = fase;
    }
}//class

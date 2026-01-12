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
public class Justificante {
    
    //Propiedades
    private String nombre;
    private Date fecha;
    private String contenido;
    
    //M�todos de acceso


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    

    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}//class

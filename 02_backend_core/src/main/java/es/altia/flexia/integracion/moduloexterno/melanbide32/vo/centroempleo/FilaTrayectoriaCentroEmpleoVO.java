/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author santiagoc
 */
public class FilaTrayectoriaCentroEmpleoVO 
{
    private Integer oriCeCod;
    private Integer oriCetraConvocatoria;
    private String descripcion;
    private String duracion;
    private String duracionValidada;
    
    public FilaTrayectoriaCentroEmpleoVO()
    {
        
    }

    public Integer getOriCeCod() {
        return oriCeCod;
    }

    public void setOriCeCod(Integer oriCeCod) {
        this.oriCeCod = oriCeCod;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDuracionValidada() {
        return duracionValidada;
    }

    public void setDuracionValidada(String duracionValidada) {
        this.duracionValidada = duracionValidada;
    }

    public Integer getOriCetraConvocatoria() {
        return oriCetraConvocatoria;
    }

    public void setOriCetraConvocatoria(Integer oriCetraConvocatoria) {
        this.oriCetraConvocatoria = oriCetraConvocatoria;
    }
    
    
}

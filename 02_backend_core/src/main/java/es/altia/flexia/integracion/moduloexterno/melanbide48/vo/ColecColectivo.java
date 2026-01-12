/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author INGDGC
 */
public class ColecColectivo {
    
    private int id;
    private String descripcion;
    private String descripcioneu;

    public ColecColectivo() {
    }

    public ColecColectivo(int id, String descripcion, String descripcioneu) {
        this.id = id;
        this.descripcion = descripcion;
        this.descripcioneu = descripcioneu;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcioneu() {
        return descripcioneu;
    }

    public void setDescripcioneu(String descripcioneu) {
        this.descripcioneu = descripcioneu;
    }

    @Override
    public String toString() {
        return "ColecColectivoDAO{" + "id=" + id + ", descripcion=" + descripcion + ", descripcioneu=" + descripcioneu + '}';
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.util;

/**
 *
 * @author INGDGC
 */
public enum Melanbide01SexoPersonaEnum {
    COD_SEXO_HOMBRE("1", "HOMBRE|GIZONEZKOA"),
    COD_SEXO_MUJER("2", "MUJER|EMAKUMEZKOA")
;

    /**
     *
     * @param codigo
     * @param descripcion
     */
    private Melanbide01SexoPersonaEnum(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    private String codigo;
    private String descripcion;

    /**
     * Get the value of descripcion
     *
     * @return the value of descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Set the value of descripcion
     *
     * @param descripcion new value of descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Get the value of codigo
     *
     * @return the value of codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Set the value of codigo
     *
     * @param codigo new value of codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

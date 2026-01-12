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
public enum MeLanbide01RolesCONCMEnum {
    
    EMPRESA_SOLICITANTE("1","EMPRESA SOLICITANTE"),
    REPRESENTANTE_LEGAL("2","REPRESENTANTE LEGAL"),
    PERSONA_SUSTITUIDA("3","PERSONA SUSTITUIDA"),
    PERSONA_CONTRATADA("4","PERSONA CONTRATADA"),
    PERSONA_CONTACTO("5","PERSONA DE CONTACTO");

    /**
     * 
     * @param codigo
     * @param descripcion 
     */
    private MeLanbide01RolesCONCMEnum(String codigo, String descripcion) {
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

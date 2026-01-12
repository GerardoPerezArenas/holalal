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
public enum Melanbide01TipoCampoSuplementarioEnum {
    /**
     *  1	NUMERICO	E_TNU
        2	TEXTO	E_TXT
        3	FECHA	E_TFE
        4	TEXTO LARGO	E_TTL
        5	FICHERO	E_TFI
        6	DESPLEGABLE	E_TDE
        8	NUMERICO CALCULADO	E_TNUC
        9	FECHA CALCULADA	E_TFEC
        10	DESPLEGABLE EXTERNO	E_TDEX
     */
    NUMERICO("1", "NUMERICO|E_TNU"),
    TEXTO("2", "TEXTO|E_TXT"),
    FECHA("3", "FECHA|E_TFE"),
    TEXTO_LARGO("4", "TEXTO LARGO|E_TTL"),
    FICHERO("5", "FICHERO|E_TFI"),
    DESPLEGABLE("6", "DESPLEGABLE|E_TDE"),
    NUMERICO_CALCULADO("8", "NUMERICO CALCULADO|E_TNUC"),
    FECHA_CALCULADA("9", "FECHA CALCULADA|E_TFEC"),
    DESPLEGABLE_EXTERNO("10", "DESPLEGABLE EXTERNO|E_TDEX")
;

    /**
     *
     * @param codigo
     * @param descripcion
     */
    private Melanbide01TipoCampoSuplementarioEnum(String codigo, String descripcion) {
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

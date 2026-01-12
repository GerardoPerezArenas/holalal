/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.procesos;

/**
 *
 * @author INGDGC
 */
public enum ColecProcesoAdjExcelEstilosEnum {
    LIGHT_GREEN_BOLD_BORDER("LIGHT_GREEN_BOLD_BORDER", "LIGHT_GREEN_BOLD_BORDER"),
    TAN_BOLD_BORDER("TAN_BOLD_BORDER", "TAN_BOLD_BORDER"),
    LIGHT_BLUE_BOLD_BORDER("LIGHT_BLUE_BOLD_BORDER", "LIGHT_BLUE_BOLD_BORDER"),
    GREY_25_PERCENT_BOLD_BORDER("GREY_25_PERCENT_BOLD_BORDER", "GREY_25_PERCENT_BOLD_BORDER"),
    NOTAS_CURSIVA_SIZE_8("NOTAS_CURSIVA_SIZE_8", "NOTAS_CURSIVA_SIZE_8");
    
    private String codigoEstilo;
    private String descripcionEstilo;

    private ColecProcesoAdjExcelEstilosEnum(String codigoEstilo, String descripcionEstilo) {
        this.codigoEstilo = codigoEstilo;
        this.descripcionEstilo = descripcionEstilo;
    }

    public String getCodigoEstilo() {
        return codigoEstilo;
    }

    public void setCodigoEstilo(String codigoEstilo) {
        this.codigoEstilo = codigoEstilo;
    }

    public String getDescripcionEstilo() {
        return descripcionEstilo;
    }

    public void setDescripcionEstilo(String descripcionEstilo) {
        this.descripcionEstilo = descripcionEstilo;
    }

}

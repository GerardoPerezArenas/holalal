/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.util;

/**
 *
 * @author INGDGC
 */
public enum Melanbide48DecretoExpedienteEnum {
    
    DANTE_2021("CONV_ANTE-2021", "CONVOCATORIAS ANTERIORES A 2021"),
    D2021_2023("CONV_2021-2023", "CONVOCATORIA LANBIDE 2021/2023");
    
    private String codigoDecreto;
    private String descripcionCortaDecreto;
    
    private Melanbide48DecretoExpedienteEnum(String codigoDecreto, String descripcionCortaDecreto) {
        this.codigoDecreto = codigoDecreto;
        this.descripcionCortaDecreto = descripcionCortaDecreto;
    }

    public String getCodigoDecreto() {
        return codigoDecreto;
    }

    public void setCodigoDecreto(String codigoDecreto) {
        this.codigoDecreto = codigoDecreto;
    }

    public String getDescripcionCortaDecreto() {
        return descripcionCortaDecreto;
    }

    public void setDescripcionCortaDecreto(String descripcionCortaDecreto) {
        this.descripcionCortaDecreto = descripcionCortaDecreto;
    }
    
    public String getDescripcionPorCodigo(String codigo) {
        String descripcionEstado = "";

        if (codigo!=null && !codigo.isEmpty()) {
            if (DANTE_2021.getCodigoDecreto().equals(codigo)) {
                descripcionEstado = DANTE_2021.getDescripcionCortaDecreto();
            } else if (D2021_2023.getCodigoDecreto().equals(codigo)) {
                descripcionEstado = D2021_2023.getDescripcionCortaDecreto();
            } 
        }
        return descripcionEstado;
    }    
    
}

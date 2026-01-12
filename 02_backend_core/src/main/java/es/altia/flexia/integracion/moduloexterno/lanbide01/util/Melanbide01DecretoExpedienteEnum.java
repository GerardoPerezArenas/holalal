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
public enum Melanbide01DecretoExpedienteEnum {
    
    D2010_177("177/2010", "DECRETO 177/2010"),
    D2019_164("164/2019", "DECRETO 164/2019");
    
    private String codigoDecreto;
    private String descripcionCortaDecreto;
    
    private Melanbide01DecretoExpedienteEnum(String codigoDecreto, String descripcionCortaDecreto) {
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
            if (D2010_177.getCodigoDecreto().equals(codigo)) {
                descripcionEstado = D2010_177.getDescripcionCortaDecreto();
            } else if (D2019_164.getCodigoDecreto().equals(codigo)) {
                descripcionEstado = D2019_164.getDescripcionCortaDecreto();
            } 
        }

        return descripcionEstado;
    }    
    
}

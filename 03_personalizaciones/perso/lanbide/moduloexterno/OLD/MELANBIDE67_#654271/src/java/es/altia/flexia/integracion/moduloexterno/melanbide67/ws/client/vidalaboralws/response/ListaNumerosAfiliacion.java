/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

import java.util.List;

/**
 *
 * @author pablo.bugia
 */
public class ListaNumerosAfiliacion {
    private List<String> numeroAfiliacionVL;

    public ListaNumerosAfiliacion(List<String> numeroAfiliacionVL) {
        this.numeroAfiliacionVL = numeroAfiliacionVL;
    }

    public List<String> getNumeroAfiliacionVL() {
        return numeroAfiliacionVL;
    }

    public void setNumeroAfiliacionVL(List<String> numeroAfiliacionVL) {
        this.numeroAfiliacionVL = numeroAfiliacionVL;
    }

    @Override
    public String toString() {
        return "ListaNumerosAfiliacion{" + "numeroAfiliacionVL=" + numeroAfiliacionVL + '}';
    }
}

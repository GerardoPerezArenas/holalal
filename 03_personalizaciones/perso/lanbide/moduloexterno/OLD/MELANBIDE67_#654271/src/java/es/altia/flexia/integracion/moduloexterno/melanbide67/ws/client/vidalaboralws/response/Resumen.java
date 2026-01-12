/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

/**
 *
 * @author pablo.bugia
 */
public class Resumen {
    private Totales totales;
    private Pluriempleo pluriempleo;

    public Resumen(Totales totales, Pluriempleo pluriempleo) {
        this.totales = totales;
        this.pluriempleo = pluriempleo;
    }

    public Totales getTotales() {
        return totales;
    }

    public void setTotales(Totales totales) {
        this.totales = totales;
    }

    public Pluriempleo getPluriempleo() {
        return pluriempleo;
    }

    public void setPluriempleo(Pluriempleo pluriempleo) {
        this.pluriempleo = pluriempleo;
    }

    @Override
    public String toString() {
        return "Resumen{" + "totales=" + totales + ", pluriempleo=" + pluriempleo + '}';
    }
}

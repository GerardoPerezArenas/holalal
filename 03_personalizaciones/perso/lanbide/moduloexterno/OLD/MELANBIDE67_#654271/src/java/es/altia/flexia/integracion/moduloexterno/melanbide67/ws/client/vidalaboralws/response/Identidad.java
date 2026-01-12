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
public class Identidad {
    private int numeroSituaciones;
    private Cabecera cabecera;
    private Resumen resumen;
    private ListaSituaciones listaSituaciones;

    public Identidad(int numeroSituaciones, Cabecera cabecera, Resumen resumen, ListaSituaciones listaSituaciones) {
        this.numeroSituaciones = numeroSituaciones;
        this.cabecera = cabecera;
        this.resumen = resumen;
        this.listaSituaciones = listaSituaciones;
    }

    public int getNumeroSituaciones() {
        return numeroSituaciones;
    }

    public void setNumeroSituaciones(int numeroSituaciones) {
        this.numeroSituaciones = numeroSituaciones;
    }

    public Cabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(Cabecera cabecera) {
        this.cabecera = cabecera;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

    public ListaSituaciones getListaSituaciones() {
        return listaSituaciones;
    }

    public void setListaSituaciones(ListaSituaciones listaSituaciones) {
        this.listaSituaciones = listaSituaciones;
    }

    @Override
    public String toString() {
        return "Identidad{" + "numeroSituaciones=" + numeroSituaciones + ", cabecera=" + cabecera + ", resumen=" + resumen + ", listaSituaciones=" + listaSituaciones + '}';
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

/**
 *
 * @author pablo.bugia
 */
public class Cabecera {
    private ListaNumerosAfiliacion listaNumerosAfiliacion;
    
    private String fechaNacimiento;
    
    private String transferenciaDerechosCEE;

    public Cabecera(ListaNumerosAfiliacion listaNumerosAfiliacion) {
        this.listaNumerosAfiliacion = listaNumerosAfiliacion;
    }

    public ListaNumerosAfiliacion getListaNumerosAfiliacion() {
        return listaNumerosAfiliacion;
    }

    public void setListaNumerosAfiliacion(ListaNumerosAfiliacion listaNumerosAfiliacion) {
        this.listaNumerosAfiliacion = listaNumerosAfiliacion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTransferenciaDerechosCEE() {
        return transferenciaDerechosCEE;
    }

    public void setTransferenciaDerechosCEE(String transferenciaDerechosCEE) {
        this.transferenciaDerechosCEE = transferenciaDerechosCEE;
    }

    @Override
    public String toString() {
        return "Cabecera{" + "listaNumerosAfiliacion=" + listaNumerosAfiliacion + ", fechaNacimiento=" + fechaNacimiento + ", transferenciaDerechosCEE=" + transferenciaDerechosCEE + '}';
    }

    
}

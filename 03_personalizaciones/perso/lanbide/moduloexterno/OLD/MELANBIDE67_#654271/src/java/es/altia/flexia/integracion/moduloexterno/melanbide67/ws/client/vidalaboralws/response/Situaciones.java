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
public class Situaciones {
    private List<Situacion> situacion;

    public Situaciones(List<Situacion> situacion) {
        this.situacion = situacion;
    }

    public List<Situacion> getSituacion() {
        return situacion;
    }

    public void setSituacion(List<Situacion> situacion) {
        this.situacion = situacion;
    }

    @Override
    public String toString() {
        return "Situaciones{" + "situacion=" + situacion + '}';
    }
}
